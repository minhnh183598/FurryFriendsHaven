package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Minh
 * Date: 9/25/2024
 * Time: 10:05 PM
 */

public interface InvalidateRepository extends JpaRepository<InvalidatedToken, String> {
}
