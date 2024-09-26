package org.demo.huyminh.Controller;

import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Reponse.LoginResponse;
import org.demo.huyminh.DTO.Reponse.IntrospectResponse;
import org.demo.huyminh.DTO.Request.LoginRequest;
import org.demo.huyminh.DTO.Request.IntrospectRequest;
import org.demo.huyminh.DTO.Request.LogoutRequest;
import org.demo.huyminh.DTO.Request.RefreshRequest;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 1:55 PM
 */

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse result = null;
        try {
            result = authenticationService.authenticate(request);
        } catch (ParseException e) {
            throw new AppException(ErrorCode.USER_NOT_EXISTS);
        }

        return ApiResponse.<LoginResponse>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> login(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse result = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<LoginResponse> refresh(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);

        return ApiResponse.<LoginResponse>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);

        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .build();
    }
}
