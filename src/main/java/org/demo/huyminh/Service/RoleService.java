package org.demo.huyminh.Service;

import jakarta.transaction.Transactional;
import org.demo.huyminh.DTO.Reponse.RoleResponse;
import org.demo.huyminh.DTO.Request.RoleRequest;
import org.demo.huyminh.Entity.User;

import java.util.List;

/**
 * @author Minh
 * Date: 10/26/2024
 * Time: 9:57 PM
 */

public interface RoleService {
    RoleResponse createRole(RoleRequest roleRequest);

    List<RoleResponse> getAll();

    void deleteRole(String roleName);

    boolean hasRole(User user, String roleName);
}
