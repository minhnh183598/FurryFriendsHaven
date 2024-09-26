package org.demo.huyminh.Controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Reponse.PermissionResponse;
import org.demo.huyminh.DTO.Request.PermissionRequest;
import org.demo.huyminh.Service.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 11:45 PM
 */

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {

        return ApiResponse.<PermissionResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Permission created successfully")
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {

        return ApiResponse.<List<PermissionResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get permissions successfully")
                .result(permissionService.findAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<String> delete(@PathVariable String permission) {

        permissionService.delete(permission);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Permission deleted successfully")
                .build();
    }
}
