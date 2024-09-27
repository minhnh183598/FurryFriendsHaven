package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** @author Minh
* Date: 9/24/2024
* Time: 7:32 AM
*/ 

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}