package org.demo.huyminh.Service;

import jakarta.transaction.Transactional;
import org.demo.huyminh.DTO.Reponse.PermissionResponse;
import org.demo.huyminh.DTO.Request.PermissionRequest;

import java.util.List;

/**
 * @author Minh
 * Date: 10/26/2024
 * Time: 9:53 PM
 */

public interface PermissionService {
    PermissionResponse create(PermissionRequest request);

    List<PermissionResponse> findAll();

    void delete(String permissionName);
}
