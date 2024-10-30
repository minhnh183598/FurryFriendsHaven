package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Minh
 * Date: 10/17/2024
 * Time: 5:38 PM
 */

public interface RatingRepository extends JpaRepository<Rating, Integer> {

    @Query("SELECT AVG(r.averageRating) FROM Rating r " +
            "WHERE r.feedback.task.id = :taskId")
    Double getAverageRatingByTaskId(@Param("taskId") int taskId);

    List<Rating> findByAverageRatingGreaterThan(Double threshold);

    @Query("SELECT r FROM Rating r " +
            "WHERE r.livingSpace >= :spaceMin " +
            "AND r.familyIncome >= :incomeMin")
    List<Rating> findHighQualityRatings(
            @Param("spaceMin") int spaceMin,
            @Param("incomeMin") int incomeMin
    );

    @Query("SELECT r FROM Rating r " +
            "WHERE (:petId IS NULL OR r.application.petId = :petId) " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'RATING' AND :sortDir = 'DESC' THEN r.averageRating END DESC, " +
            "CASE WHEN :sortBy = 'RATING' AND :sortDir = 'ASC' THEN r.averageRating END ASC, " +
            "CASE WHEN :petId IS NULL AND :sortDir = 'DESC' THEN r.feedback.createdAt END DESC, " +
            "CASE WHEN :petId IS NULL AND :sortDir = 'ASC' THEN r.feedback.createdAt END ASC")
    List<Rating> findTopRatings(
            @Param("petId") String petId,
            @Param("sortBy") String sortBy,
            @Param("sortDir") String sortDir
    );
}
