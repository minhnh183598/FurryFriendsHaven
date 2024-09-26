package org.demo.huyminh.Mapper;

import org.demo.huyminh.DTO.Reponse.PermissionResponse;
import org.demo.huyminh.DTO.Request.PermissionRequest;
import org.demo.huyminh.Entity.Permission;
import org.mapstruct.Mapper;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 11:32 PM
 */

@Mapper(componentModel = "Spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
