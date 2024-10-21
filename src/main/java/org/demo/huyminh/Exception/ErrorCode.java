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
    TASK_NOT_EXISTS(HttpStatus.NOT_FOUND.value(), "Task not found", HttpStatus.NOT_FOUND),
    ISSUE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Issue not found", HttpStatus.NOT_FOUND),
    TAG_NOT_EXISTS(HttpStatus.NOT_FOUND.value(), "Tag not exists", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_TO_DELETE_COMMENT(HttpStatus.FORBIDDEN.value(), "You don't have authority to delete this comment", HttpStatus.FORBIDDEN),
    TASK_HAS_NO_TAGS(HttpStatus.BAD_REQUEST.value(), "Task must have at least one tag", HttpStatus.BAD_REQUEST),
    TASK_EXISTS(HttpStatus.BAD_REQUEST.value(), "Task already exists. Please choose another name.", HttpStatus.BAD_REQUEST),
    TAG_ALREADY_EXISTS(HttpStatus.BAD_REQUEST.value(), "Tag already exists. Please choose another name.", HttpStatus.BAD_REQUEST),
    USER_NOT_IN_TEAM(HttpStatus.BAD_REQUEST.value(), "User not in team", HttpStatus.BAD_REQUEST),
    ISSUE_HAS_NO_TAGS(HttpStatus.BAD_REQUEST.value(), "Issue must have at least one tag", HttpStatus.BAD_REQUEST),
    TAGS_NOT_EXISTS(HttpStatus.NOT_FOUND.value(), "Tags was not exists. Pleas choose valid tags.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_TO_UPDATE_STATUS(HttpStatus.FORBIDDEN.value(), "You don't have authority to update this status", HttpStatus.FORBIDDEN),
    UNAUTHORIZED_TO_ADD_USER_TO_ISSUE(HttpStatus.FORBIDDEN.value(), "You don't have authority to add user to this issue", HttpStatus.FORBIDDEN),
    UNAUTHORIZED_TO_DELETE_ISSUE(HttpStatus.FORBIDDEN.value(), "You don't have authority to delete this issue", HttpStatus.FORBIDDEN),
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Task not found", HttpStatus.NOT_FOUND),
    ISSUE_NOT_IN_TASK(HttpStatus.BAD_REQUEST.value(), "Issue not in task", HttpStatus.BAD_REQUEST),
    PARAMETER_INVALID(HttpStatus.BAD_REQUEST.value(), "Invalid parameter", HttpStatus.BAD_REQUEST),
    USER_ALREADY_IN_TASK(HttpStatus.BAD_REQUEST.value(), "User already in task", HttpStatus.BAD_REQUEST),
    INVALID_TAG_TYPE(HttpStatus.BAD_REQUEST.value(), "Invalid tag type", HttpStatus.BAD_REQUEST),
    TAG_WAS_ON_USE(HttpStatus.BAD_REQUEST.value(), "Tag was on use. So you can not update this tag name.", HttpStatus.BAD_REQUEST),
    LIST_TAG_IS_EMPTY(HttpStatus.NOT_FOUND.value(), "List tag is empty", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_TO_ADD_USER_TO_TASK(HttpStatus.FORBIDDEN.value(), "You don't have authority to add user to this task", HttpStatus.FORBIDDEN),
    UNAUTHORIZED_TO_DELETE_USER_FROM_TASK(HttpStatus.FORBIDDEN.value(), "You don't have authority to delete user from this task", HttpStatus.FORBIDDEN),
    TASK_HAS_NO_ISSUES(HttpStatus.NOT_FOUND.value(), "Task has no issue", HttpStatus.NOT_FOUND),
    ROLE_NOT_EXISTS(HttpStatus.BAD_REQUEST.value(), "Role does not exists", HttpStatus.BAD_REQUEST),
    LIST_USER_IS_EMPTY(HttpStatus.NOT_FOUND.value(), "List user is empty", HttpStatus.NOT_FOUND),
    DUE_DATE_IS_REQUIRED(HttpStatus.BAD_REQUEST.value(), "Due date is required", HttpStatus.BAD_REQUEST),
    INVALID_DUE_DATE(HttpStatus.BAD_REQUEST.value(), "Due date must be prior to the current date", HttpStatus.BAD_REQUEST),
    DUE_DATE_IS_BEFORE_TASK_DUE_DATE(HttpStatus.BAD_REQUEST.value(), "Issue Due date must be before Task due date", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_TO_UPDATE_ISSUE(HttpStatus.FORBIDDEN.value(), "You don't have authority to update this issue", HttpStatus.FORBIDDEN),
    INVALID_STATUS(HttpStatus.BAD_REQUEST.value(), "Invalid status", HttpStatus.BAD_REQUEST),
    STATUS_IS_REQUIRED(HttpStatus.BAD_REQUEST.value(), "Status is required", HttpStatus.BAD_REQUEST),
    OLD_PASSWORD_WRONG(HttpStatus.FORBIDDEN.value(), "Old password is wrong", HttpStatus.FORBIDDEN),
    CONFIRM_PASSWORD_WRONG(HttpStatus.FORBIDDEN.value(), "Confirm password is wrong", HttpStatus.FORBIDDEN),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Username not found", HttpStatus.NOT_FOUND),
    FEEDBACK_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Feedback not found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_TO_DELETE_FEEDBACK(HttpStatus.FORBIDDEN.value(), "You don't have authority to delete this feedback", HttpStatus.FORBIDDEN),
    FEEDBACK_NOT_BELONG_TO_TASK(HttpStatus.BAD_REQUEST.value(), "Feedback not belong to task", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_TO_UPDATE_FEEDBACK(HttpStatus.FORBIDDEN.value(), "You don't have authority to update this feedback", HttpStatus.FORBIDDEN),
    USER_NOT_IN_ISSUE(HttpStatus.FORBIDDEN.value(), "User not in issue", HttpStatus.FORBIDDEN),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Comment not found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_TO_UPDATE_COMMENT(HttpStatus.FORBIDDEN.value(), "You don't have authority to update this comment", HttpStatus.FORBIDDEN),
    UNAUTHORIZED_TO_CHANGE_APPLICATION_STATUS(HttpStatus.FORBIDDEN.value(), "You don't have authority to change application status", HttpStatus.FORBIDDEN),
    APPLICATION_NOT_EXISTS(HttpStatus.NOT_FOUND.value(), "Application not exists", HttpStatus.NOT_FOUND),
    CANNOT_REMOVE_LAST_USER(HttpStatus.BAD_REQUEST.value(), "You can not remove last user in task", HttpStatus.BAD_REQUEST),
    CANNOT_REMOVE_OWNER(HttpStatus.BAD_REQUEST.value(), "You can not remove owner in task", HttpStatus.BAD_REQUEST),
    PET_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Pet not found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_TO_GET_THIS_INFORMATION(HttpStatus.FORBIDDEN.value(), "You don't have authority to get this information", HttpStatus.FORBIDDEN),
    NO_FEEDBACK(HttpStatus.NOT_FOUND.value(), "You need to create feedback before change status to DONE", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_TO_CHANGE_TASK_STATUS(HttpStatus.FORBIDDEN.value(), "You don't have authority to change task status", HttpStatus.FORBIDDEN),
    CANNOT_CHANGE_STATUS_TO_SAME_STATUS(HttpStatus.BAD_REQUEST.value(), "You can not change status to same status", HttpStatus.BAD_REQUEST),
    EACH_USER_CAN_POST_ONE_FEEDBACK(HttpStatus.FORBIDDEN.value(), "Each volunteer was allowed to post one feedback per task", HttpStatus.FORBIDDEN),
    INVALID_SORT_ORDER(HttpStatus.BAD_REQUEST.value(), "Invalid sort order", HttpStatus.BAD_REQUEST),
    ISSUE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST.value(), "Issue already exists. Please choose another name.", HttpStatus.BAD_REQUEST),
    ;

    private int code;
    private String message;
    private HttpStatus httpStatus;
}
