package org.demo.huyminh.Mapper;

import org.demo.huyminh.DTO.Reponse.RoleResponse;
import org.demo.huyminh.DTO.Request.RoleRequest;
import org.demo.huyminh.Entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 10:27 AM
 */

@Mapper(componentModel = "Spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
