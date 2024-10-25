package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Minh
 * Date: 10/24/2024
 * Time: 3:18 PM
 */

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Integer> {
}
