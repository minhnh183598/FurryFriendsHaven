package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Minh
 * Date: 9/28/2024
 * Time: 8:14 PM
 */

public interface OptRepository extends JpaRepository<Otp, Integer> {
     @Query("SELECT o FROM Otp o WHERE o.code = ?1 AND o.user.id = ?2")
     Otp findByCode(String otp, String userId);

     @Query("SELECT o FROM Otp o WHERE o.user.id = ?1")
     Otp findByUserId(String userId);
}
