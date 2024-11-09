package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.AdoptionFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author Minh
 * Date: 11/9/2024
 * Time: 10:50 PM
 */

public interface AdoptionFeedbackRepository extends JpaRepository<AdoptionFeedback, Integer> {

    @Query("SELECT f FROM AdoptionFeedback f WHERE f.petName = ?1")
    Optional<AdoptionFeedback> findByPetName(String petName);
}
