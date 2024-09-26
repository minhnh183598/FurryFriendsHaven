package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Minh
 * Date: 9/25/2024
 * Time: 12:20 AM
 */


@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
