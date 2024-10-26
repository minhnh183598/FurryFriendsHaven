package org.demo.huyminh.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import org.demo.huyminh.DTO.Reponse.IntrospectResponse;
import org.demo.huyminh.DTO.Reponse.LoginResponse;
import org.demo.huyminh.DTO.Request.*;
import org.demo.huyminh.Entity.User;

import java.text.ParseException;

/**
 * @author Minh
 * Date: 10/26/2024
 * Time: 9:34 PM
 */

public interface AuthenticationService {

    LoginResponse outboundAuthenticate(String code);

    User register(UserCreationRequest request);

    void resetPassword(ResetPasswordRequest request);

    IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException;

    LoginResponse authenticate(LoginRequest request) throws ParseException;

    void logout(LogoutRequest request) throws ParseException, JOSEException;

    LoginResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;

    String generateToken(User user, long ExpiryTime);

    SignedJWT verifyJWT(String token, boolean isRefresh) throws ParseException, JOSEException;
}
