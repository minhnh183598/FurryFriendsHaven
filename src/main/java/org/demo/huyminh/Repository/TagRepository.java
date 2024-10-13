package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 9:20 PM
 */

public interface TagRepository extends JpaRepository<Tag, String> {
}
