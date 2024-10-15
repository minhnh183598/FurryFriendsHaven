package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Role;
import org.demo.huyminh.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** @author Minh
* Date: 9/24/2024
* Time: 7:32 AM
*/ 

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findById(String userId);
  
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE ?1 MEMBER OF u.roles")
    List<User> findUsersByRole(Role role);
//
//    @Query("SELECT u FROM User u ORDER BY u.username :ASC")
//    List<User> sortUsersByName();
}
