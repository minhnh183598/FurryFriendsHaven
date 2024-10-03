package org.demo.huyminh.Controller;

import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
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
import org.demo.huyminh.Service.UserService;
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
    UserService userService;

    @PostMapping("/outbound/authentication")
    ApiResponse<LoginResponse> outboundAuthentication(@RequestParam("code") String code) {
        var result = authenticationService.outboundAuthenticate(code);
        return ApiResponse.<LoginResponse>builder().result(result).build();
    }

    @PostMapping("/create-password")
    ApiResponse<Void> createPassword(@RequestBody @Valid PasswordCreationRequest request) {
        userService.createPassword(request);
        return ApiResponse.<Void>builder()
                .message("Password has been created, you could use it to log-in")
                .build();
    }

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


    @PostMapping("/verifyEmail")
     ApiResponse<Void> verifyEmail(@RequestBody VerifyEmailRequest request) {
        Otp theOtp = optRepository.findByCode(request.getOtp(), request.getUserId());

        if(theOtp == null) {
            throw new AppException(ErrorCode.OTP_NOT_EXISTS);
        }
        if (theOtp.getExpireTime().before(new Date())) {
            optRepository.delete(theOtp);
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }

        User user = theOtp.getUser();
        String message = "";
        if(!user.isEnabled()) {
            user.setEnabled(true);
            userRepository.save(user);
            message = "Email verified successfully. Please, login your account!";
        }
        if(!user.isPasswordChangeable()) {
            user.setPasswordChangeable(true);
            message = "Email verified successfully. Please, change your password!";
        }

        optRepository.delete(theOtp);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(message)
                .build();
    }

    @PostMapping("/forgotPassword")
    ApiResponse<UserResponse> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        User user = userRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        eventPublisher.publishEvent(new VerificationEmailEvent(user));

        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Email send successfully. Please, verify your email to change your password!")
                .result(UserResponse.builder().id(user.getId()).build())
                .build();
    }

    @PostMapping("/resetPassword")
    ApiResponse<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        Otp theOtp = optRepository.findByUserId(request.getUserId());

        if(theOtp != null) {
            throw new AppException(ErrorCode.OTP_IS_NOT_USED);
        }
        authenticationService.resetPassword(request);

        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Change password successfully. Please login your account again!")
                .build();
    }

    @PostMapping ("/resendVerifyEmail")
    ApiResponse<Void> resendVerifyEmail(@RequestBody ResendEmailRequest request) {
        User user = userRepository.findById(request.getUserId())
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
