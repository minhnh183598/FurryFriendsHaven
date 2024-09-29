package org.demo.huyminh.Controller;

import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Reponse.LoginResponse;
import org.demo.huyminh.DTO.Reponse.IntrospectResponse;
import org.demo.huyminh.DTO.Reponse.UserResponse;
import org.demo.huyminh.DTO.Request.*;
import org.demo.huyminh.Entity.Otp;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Event.VerificationEmailEvent;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Repository.OptRepository;
import org.demo.huyminh.Repository.UserRepository;
import org.demo.huyminh.Service.AuthenticationService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.Date;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 1:55 PM
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;
    ApplicationEventPublisher eventPublisher;
    OptRepository optRepository;
    UserRepository userRepository;

    @PostMapping("/register")
    ApiResponse<UserResponse> register(@RequestBody UserCreationRequest request) {
        User user = authenticationService.register(request);

        eventPublisher.publishEvent(new VerificationEmailEvent(user));

        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Register successfully. Please, verify your email !")
                .result(UserResponse.builder().id(user.getId()).build())
                .build();
    }

    @GetMapping("/verifyEmail")
     ApiResponse<Void> verifyEmail(@RequestParam("otp") String otp, @RequestParam("userId") String userId) {
        Otp theOtp = optRepository.findByCode(otp, userId);

        if(theOtp == null) {
            throw new AppException(ErrorCode.OTP_NOT_EXISTS);
        }
        if (theOtp.getExpireTime().before(new Date())) {
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }

        User user = theOtp.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        optRepository.delete(theOtp);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Email verified successfully. Please, login your new account !")
                .build();
    }

    @GetMapping ("/resendVerifyEmail")
    ApiResponse<Void> resendVerifyEmail(@RequestParam("userId") String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        if (user.isEnabled()) {
            throw new AppException(ErrorCode.USER_IS_ENABLED);
        }

        eventPublisher.publishEvent(new VerificationEmailEvent(user));
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Email resend successfully. Please, verify your email !")
                .build();
    }

    @PostMapping("/login")
    ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse result;
        try {
            result = authenticationService.authenticate(request);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return ApiResponse.<LoginResponse>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
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
                .message("Logout success")
                .build();
    }
}
