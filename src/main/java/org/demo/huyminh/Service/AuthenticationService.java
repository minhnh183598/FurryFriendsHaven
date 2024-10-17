package org.demo.huyminh.Service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.IntrospectResponse;
import org.demo.huyminh.DTO.Reponse.LoginResponse;
import org.demo.huyminh.DTO.Request.*;
import org.demo.huyminh.Entity.InvalidatedToken;
import org.demo.huyminh.Entity.RefreshToken;
import org.demo.huyminh.Entity.Role;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Enums.Roles;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Mapper.UserMapper;
import org.demo.huyminh.Repository.HttpClient.OutboundIdentityClient;
import org.demo.huyminh.Repository.HttpClient.OutboundUserClient;
import org.demo.huyminh.Repository.InvalidateRepository;
import org.demo.huyminh.Repository.RefreshTokenRepository;
import org.demo.huyminh.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 1:59 PM
 */

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;
    InvalidateRepository invalidateRepository;
    RefreshTokenRepository refreshTokenRepository;
    UserMapper userMapper;
    PasswordEncoder encoder;
    OutboundIdentityClient outboundIdentityClient;
    OutboundUserClient outboundUserClient;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SECRET_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${outbound.identity.client-id}")
    protected String CLIENT_ID;

    @NonFinal
    @Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET;

    @NonFinal
    @Value("${outbound.identity.redirect-uri}")
    protected String REDIRECT_URI;

    @NonFinal
    protected final String GRANT_TYPE = "authorization_code";

    public LoginResponse outboundAuthenticate(String code) {
        var response = outboundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                .code(code)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .redirectUri(REDIRECT_URI)
                .grantType(GRANT_TYPE)
                .build());
        log.info("TOKEN RESPONSE {}", response);

        var userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());

        log.info("USER INFO {}", userInfo);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().name(Roles.USER.name()).build());

        var user = userRepository.findByUsername(userInfo.getEmail()).orElseGet(
                () -> userRepository.save(User.builder()
                        .username(userInfo.getEmail())
                        .firstname(userInfo.getGivenName())
                        .lastname(userInfo.getFamilyName())
                        .email(userInfo.getEmail())
                        .roles(roles)
                        .isEnabled(true)
                        .build())
        );

        var token = generateToken(user, VALID_DURATION);
        var refreshToken = generateToken(user, REFRESHABLE_DURATION);

        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }

    @Transactional
    public User register(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTS);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }

        User user = userMapper.toUser(request);
        user.setPassword(encoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.builder().name(Roles.USER.name()).build());
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        if (!user.isEnabled()) {
            throw new AppException(ErrorCode.USER_IS_DISABLED);
        } else if (!user.isPasswordChangeable()) {
            throw new AppException(ErrorCode.USER_IS_NOT_CHANGEABLE);
        }

        user.setPassword(encoder.encode(request.getNewPassword()));
        user.setPasswordChangeable(false);
        userRepository.save(user);
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {

        var token = request.getToken();

        boolean isValid = true;
        try {
            verifyJWT(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    @Transactional
    public LoginResponse authenticate(LoginRequest request) throws ParseException {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        if (!user.isEnabled()) {
            throw new AppException(ErrorCode.USER_IS_DISABLED);
        }

        if(!user.getUsername().equals(request.getUsername())){
            throw new AppException(ErrorCode.USERNAME_NOT_FOUND);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.PASSWORD_INVALID);
        }

        var token = generateToken(user, VALID_DURATION);
        var refreshToken = generateToken(user, REFRESHABLE_DURATION);

        String tokenId = SignedJWT.parse(token).getJWTClaimsSet().getJWTID();
        String refreshTokenId = SignedJWT.parse(refreshToken).getJWTClaimsSet().getJWTID();

        // Find current refreshToken if exists
        Optional<RefreshToken> existingTokenOpt = refreshTokenRepository.findByUserId(user.getId());

        RefreshToken refreshTokenEntity;
        Date tokenExpiryTime = SignedJWT.parse(token).getJWTClaimsSet().getExpirationTime();
        Date refreshTokenExpiryTime = SignedJWT.parse(refreshToken).getJWTClaimsSet().getExpirationTime();

        if (existingTokenOpt.isPresent()) {

            //Cancel invalid access token
            String oldToken = existingTokenOpt.get().getToken();
            Date oldTokenExpiryTime = existingTokenOpt.get().getTokenExpiryTime();
            invalidateRepository.save(InvalidatedToken.builder().id(oldToken).expiryTime(oldTokenExpiryTime).build());

            refreshTokenEntity = existingTokenOpt.get();
            refreshTokenEntity.setToken(tokenId);
            refreshTokenEntity.setRefreshToken(refreshTokenId);
            refreshTokenEntity.setTokenExpiryTime(tokenExpiryTime);
            refreshTokenEntity.setRefreshTokenExpiryTime(refreshTokenExpiryTime);
        } else {

            refreshTokenEntity = RefreshToken.builder()
                    .token(tokenId)
                    .refreshToken(refreshTokenId)
                    .user(user)
                    .tokenExpiryTime(tokenExpiryTime)
                    .refreshTokenExpiryTime(refreshTokenExpiryTime)
                    .build();
        }

        refreshTokenRepository.save(refreshTokenEntity);

        return LoginResponse.builder()
                .authenticated(authenticated)
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signedToken = verifyJWT(request.getToken(), false);

        String jit = signedToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        invalidateRepository.save(invalidatedToken);
    }

    @Transactional
    public LoginResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedToken = verifyJWT(request.getToken(), true);
        var refreshTokenId = signedToken.getJWTClaimsSet().getJWTID();

        var refreshTokenExpiryTime = signedToken.getJWTClaimsSet().getExpirationTime();
        if (!(refreshTokenRepository.existsByRefreshToken(refreshTokenId) && refreshTokenExpiryTime.after(Date.from(Instant.now())))) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        var tokenId = refreshTokenRepository.getToken(refreshTokenId);
        var tokenExpiryTime = refreshTokenRepository.getTokenExpiryTime(refreshTokenId);

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(tokenId)
                .expiryTime(tokenExpiryTime)
                .build();

        invalidateRepository.save(invalidatedToken);

        var username = signedToken.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHENTICATED)
        );

        var token = generateToken(user, VALID_DURATION);
        return LoginResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    String generateToken(User user, long ExpiryTime) {
        //Build header for JWT token
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet;
        //Build payload for JWT token
        if (ExpiryTime == VALID_DURATION) {
            jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("furryfriendshaven.com")
                    .issueTime(new Date())
                    .expirationTime(new Date(Instant.now().plus(ExpiryTime, ChronoUnit.HOURS).toEpochMilli()))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("scope", buildScope(user))
                    .build();
        } else {
            jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("furryfriendshaven.com")
                    .issueTime(new Date())
                    .expirationTime(new Date(Instant.now().plus(ExpiryTime, ChronoUnit.HOURS).toEpochMilli()))
                    .jwtID(UUID.randomUUID().toString())
                    .build();
        }

        //Transform JWTClaimsSet to Payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        //Symmetric sign Payload - that means encrypt key and decrypt key is the same (Secret key) - MACSigner
        //Asymmetric sign Payload - that means encrypt key and decrypt key is different (Private key and Public key) - RSASSASigner
        try {
            //Sign JWT
            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Can not generate token", e);
            throw new RuntimeException(e);
        }
    }

    private SignedJWT verifyJWT(String token, boolean isRefresh) throws ParseException, JOSEException {

        //Build MAC verifier to verify JWT signature
        JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

        //Parse JWT token
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getExpirationTime().toInstant()
                .plus(REFRESHABLE_DURATION, ChronoUnit.HOURS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verifiedJWT = signedJWT.verify(verifier);

        if (!(verifiedJWT && expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidateRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }


    private String buildScope(User user) {
        StringJoiner scope = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(
                    role -> {
                        scope.add("ROLE_" + role.getName());
                        if (!CollectionUtils.isEmpty(role.getPermissions())) {
                            role.getPermissions().forEach(
                                    permission -> scope.add(permission.getName())
                            );
                        }
                    }
            );
        }
        return scope.toString();
    }
}
