package org.demo.huyminh.Exception;

import lombok.*;
import org.springframework.http.HttpStatus;

/** @author Minh
* Date: 9/24/2024
* Time: 9:14 AM
*/ 

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unauthorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTS(HttpStatus.BAD_REQUEST.value(), "Username already exists", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTS(HttpStatus.BAD_REQUEST.value(), "Permission does not exists", HttpStatus.NOT_FOUND),
    USER_NOT_EXISTS(HttpStatus.BAD_REQUEST.value(), "Username does not exists", HttpStatus.NOT_FOUND),
    INVALID_KEY(HttpStatus.BAD_REQUEST.value(), "Invalid message key", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(HttpStatus.FORBIDDEN.value(), "Invalid token", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(HttpStatus.BAD_REQUEST.value(), "Username must be at least {min} characters long", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(HttpStatus.BAD_REQUEST.value(), "Password must be at least {min} characters long", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED.value(), "Your account is not authenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(HttpStatus.FORBIDDEN.value(), "You don't have permission to access this resource", HttpStatus.FORBIDDEN),
    INVALID_DOB(HttpStatus.BAD_REQUEST.value(), "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    DELETE_PERMISSION_FAILED(HttpStatus.BAD_REQUEST.value(), "Delete permission failed", HttpStatus.BAD_REQUEST),
    DELETE_USER_FAILED(HttpStatus.BAD_REQUEST.value(), "Delete user failed", HttpStatus.BAD_REQUEST),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST.value(), "Invalid refresh token", HttpStatus.BAD_REQUEST),
    EMAIL_PROCESSING_FAILED(HttpStatus.SERVICE_UNAVAILABLE.value(), "Email processing failed", HttpStatus.SERVICE_UNAVAILABLE),
    USER_NOT_ENABLED(HttpStatus.FORBIDDEN.value(), "User is not enabled", HttpStatus.FORBIDDEN),
    OTP_NOT_EXISTS(HttpStatus.BAD_REQUEST.value(), "OTP is not available", HttpStatus.BAD_REQUEST),
    OTP_EXPIRED(HttpStatus.BAD_REQUEST.value(), "OTP expired", HttpStatus.BAD_REQUEST),
    USER_IS_DISABLED(HttpStatus.BAD_REQUEST.value(), "Your account is not enabled", HttpStatus.BAD_REQUEST),
    USER_IS_ENABLED(HttpStatus.BAD_REQUEST.value(), "Your account is enabled", HttpStatus.BAD_REQUEST),
    OTP_IS_NOT_USED(HttpStatus.BAD_REQUEST.value(), "OTP is not used. You can not change password until you finish verification step.", HttpStatus.BAD_REQUEST),
    PASSWORD_EXISTED(HttpStatus.FOUND.value(), "Password existed", HttpStatus.FOUND),
    USER_IS_NOT_CHANGEABLE(HttpStatus.FORBIDDEN.value(), "User password is not changeable", HttpStatus.FORBIDDEN),
    USER_NOT_EXISTED(HttpStatus.NOT_FOUND.value(), "User not existed", HttpStatus.NOT_FOUND),
    EMAIL_EXISTS(HttpStatus.FORBIDDEN.value(), "Email is existed", HttpStatus.FORBIDDEN),
    FORGOT_PASSWORD_REQUIRED_BEFORE_RESEND(HttpStatus.FORBIDDEN.value(), "Forgot password required before resend otp", HttpStatus.FORBIDDEN),
    OTP_IS_USED(HttpStatus.FORBIDDEN.value(), "OTP is used", HttpStatus.FORBIDDEN),
    OTP_LOCKED_OUT(HttpStatus.FORBIDDEN.value(), "OTP locked out. Please wait for 5 minutes to try again.", HttpStatus.FORBIDDEN),
    OLD_PASSWORD_WRONG(HttpStatus.FORBIDDEN.value(), "Old password is wrong", HttpStatus.FORBIDDEN),
    CONFIRM_PASSWORD_WRONG(HttpStatus.FORBIDDEN.value(), "Confirm password is wrong", HttpStatus.FORBIDDEN),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Username not found", HttpStatus.NOT_FOUND),
    ;

    private int code;
    private String message;
    private HttpStatus httpStatus;
}
