package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Minh
 * Date: 10/24/2024
 * Time: 3:11 PM
 */

public interface ChecklistRepository extends JpaRepository<Checklist, Integer> {
}
