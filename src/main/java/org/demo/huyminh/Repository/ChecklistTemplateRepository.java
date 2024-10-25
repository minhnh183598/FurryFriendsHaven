package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.ChecklistTemplate;
import org.hibernate.annotations.DialectOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author Minh
 * Date: 10/24/2024
 * Time: 3:20 PM
 */

public interface ChecklistTemplateRepository extends JpaRepository<ChecklistTemplate, Integer> {

    Optional<ChecklistTemplate> findByName(String name);

    boolean existsByName(String name);
}
