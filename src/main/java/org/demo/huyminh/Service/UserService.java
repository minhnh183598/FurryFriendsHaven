package org.demo.huyminh.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.UserResponse;
import org.demo.huyminh.DTO.Request.UserUpdateRequest;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Mapper.UserMapper;
import org.demo.huyminh.Repository.RoleRepository;
import org.demo.huyminh.Repository.UserRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 7:34 AM
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;


//    @PreAuthorize("hasAuthority('APPROVE_POST')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    @PostAuthorize("hasRole('ADMIN') || returnObject.username == authentication.name")
    public UserResponse getUser(String id) {
        log.info("In method get User: {}", id);
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        userMapper.updateUser(user, request);
        user.setPassword(encoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String id) {

        if(!userRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTS);
        }

        userRepository.deleteById(id);

        if(userRepository.existsById(id)) {
            log.error("Failed to delete permission: {}", id);
            throw new AppException(ErrorCode.DELETE_USER_FAILED);
        }
    }

    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTS);
        }

        return user.get();
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        var name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        return userMapper.toUserResponse(user);
    }
}
