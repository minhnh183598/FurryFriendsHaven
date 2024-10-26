package org.demo.huyminh.Service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.PermissionResponse;
import org.demo.huyminh.DTO.Request.PermissionRequest;
import org.demo.huyminh.Entity.Permission;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Mapper.PermissionMapper;
import org.demo.huyminh.Repository.PermissionRepository;
import org.demo.huyminh.Service.PermissionService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 11:27 PM
 */

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Transactional
    @Override
    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public List<PermissionResponse> findAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @Transactional
    @Override
    public void delete(String permissionName) {
        if (!permissionRepository.existsByName(permissionName)) {
            throw new AppException(ErrorCode.PERMISSION_NOT_EXISTS);
        }

        permissionRepository.deleteByName(permissionName);

        if (permissionRepository.existsByName(permissionName)) {
            log.error("Failed to delete permission: {}", permissionName);
            throw new AppException(ErrorCode.DELETE_PERMISSION_FAILED);
        }
    }
}
