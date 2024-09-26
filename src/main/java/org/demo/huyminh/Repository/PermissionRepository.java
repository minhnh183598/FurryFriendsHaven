package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 11:23 PM
 */

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {

    @Modifying
    @Query("DELETE FROM Permission p WHERE p.name = ?1")
    void deleteByName(String permissionName);

    boolean existsByName(String permissionName);
}
