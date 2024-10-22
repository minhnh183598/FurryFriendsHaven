package org.demo.huyminh.Controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Reponse.RoleResponse;
import org.demo.huyminh.DTO.Request.RoleRequest;
import org.demo.huyminh.Service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 11:45 PM
 */

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createPermission(@RequestBody RoleRequest request) {

        return ApiResponse.<RoleResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Roles created successfully")
                .result(roleService.createRole(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {

        return ApiResponse.<List<RoleResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get permissions successfully")
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable String role) {

        roleService.deleteRole(role);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Permission deleted successfully")
                .build();
    }
}
