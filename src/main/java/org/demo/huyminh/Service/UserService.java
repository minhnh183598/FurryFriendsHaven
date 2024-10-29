package org.demo.huyminh.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.demo.huyminh.DTO.Reponse.BriefVolunteerResponse;
import org.demo.huyminh.DTO.Reponse.UserResponse;
import org.demo.huyminh.DTO.Request.ChangePasswordRequest;
import org.demo.huyminh.DTO.Request.PasswordCreationRequest;
import org.demo.huyminh.DTO.Request.UserUpdateRequest;
import org.demo.huyminh.Entity.User;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * @author Minh
 * Date: 10/26/2024
 * Time: 10:03 PM
 */

public interface UserService {
    List<UserResponse> getUsers();

    List<BriefVolunteerResponse> getVolunteers();

    List<UserResponse> getUsersByRole(String role, String sort, String sortBy, String keyword);

    UserResponse getUser(String id);

    UserResponse updateUser(String id, UserUpdateRequest request);

    void deleteUser(String id);

    User findByUsername(String username);

    UserResponse getMyInfo();

    UserResponse createPassword(@Valid PasswordCreationRequest request);

    User findByToken(String token);

    void changePassword(String id, ChangePasswordRequest request);
}
