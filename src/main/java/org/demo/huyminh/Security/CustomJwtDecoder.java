package org.demo.huyminh.Security;

import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.demo.huyminh.DTO.Reponse.IntrospectResponse;
import org.demo.huyminh.DTO.Request.IntrospectRequest;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

/**
 * @author Minh
 * Date: 9/26/2024
 * Time: 12:06 AM
 */

@Component
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.signerKey}")
    private String SECRET_KEY;

    @Autowired
    private AuthenticationService authenticationService;

    private NimbusJwtDecoder jwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            IntrospectResponse response = authenticationService.introspect(IntrospectRequest.builder().token(token).build());
            if (!response.isValid()) throw new JwtException("Invalid token");
        } catch (ParseException | JOSEException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        if (Objects.isNull(jwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HS512");
            jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return jwtDecoder.decode(token);
    }
}