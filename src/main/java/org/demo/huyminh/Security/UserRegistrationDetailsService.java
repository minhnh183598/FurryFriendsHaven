package org.demo.huyminh.Security;

import lombok.RequiredArgsConstructor;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Minh
 * Date: 9/28/2024
 * Time: 11:23 PM
 */

@Service
@RequiredArgsConstructor
public class UserRegistrationDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(UserRegistrationDetails::new)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
    }
}
