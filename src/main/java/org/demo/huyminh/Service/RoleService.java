package org.demo.huyminh.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.RoleResponse;
import org.demo.huyminh.DTO.Request.RoleRequest;
import org.demo.huyminh.Entity.Permission;
import org.demo.huyminh.Mapper.RoleMapper;
import org.demo.huyminh.Repository.PermissionRepository;
import org.demo.huyminh.Repository.RoleRepository;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;

/**
 * @author Minh
 * Date: 9/25/2024
 * Time: 12:21 AM
 */


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {

    RoleRepository roleRepository;
    RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;

    @Transactional
    public RoleResponse createRole(RoleRequest roleRequest) {
        var role = roleMapper.toRole(roleRequest);
        List<Permission> permissions = permissionRepository.findAllById(roleRequest.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse).toList();
    }

    @Transactional
    public void deleteRole(String roleName) {
        roleRepository.deleteById(roleName);
    }

}
