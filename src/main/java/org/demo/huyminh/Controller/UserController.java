package org.demo.huyminh.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Reponse.UserResponse;
import org.demo.huyminh.DTO.Request.ChangePasswordRequest;
import org.demo.huyminh.DTO.Request.UserUpdateRequest;
import org.demo.huyminh.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 7:43 AM
 */

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    ApiResponse<List<UserResponse>> getUsers() {
        //Get user's info when they are logged in
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(role -> log.info("Roles: {}", role.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get users successfully")
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("users/search")
    ApiResponse<List<UserResponse>> getUsersByRole(
            @RequestParam(value = "role", defaultValue = "ALL") String role,
            @RequestParam(value = "sort", defaultValue = "ASC") String sort,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword
    ) {
        return ApiResponse.<List<UserResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get users successfully")
                .result(userService.getUsersByRole(role, sort, sortBy, keyword))
                .build();
    }

    @GetMapping("/users/{id}")
    ApiResponse<UserResponse> getUser(@PathVariable String id) {
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get user successfully")
                .result(userService.getUser(id))
                .build();
    }

    @GetMapping("/users/info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get your info successfully")
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("/users/{id}")
    public ApiResponse<Void> updateUser(@PathVariable String id, @RequestBody UserUpdateRequest request) {
        userService.updateUser(id, request);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Update user successfully")
                .build();
    }

    @DeleteMapping("/users/{id}")
    ApiResponse<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete user successfully")
                .build();
    }

    @PutMapping("/users/{id}/change-password")
    ApiResponse<Void> changePassword(@PathVariable String id, @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Change password successfully")
                .build();
    }
}
