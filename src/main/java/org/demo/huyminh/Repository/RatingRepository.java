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
    Double getAverageRatingByTaskId(@Param("taskId") Integer taskId);

    List<Rating> findByAverageRatingGreaterThan(Double threshold);

    @Query("SELECT r FROM Rating r " +
            "WHERE r.livingSpace >= :spaceMin " +
            "AND r.familyIncome >= :incomeMin")
    List<Rating> findHighQualityRatings(
            @Param("spaceMin") Integer spaceMin,
            @Param("incomeMin") Integer incomeMin
    );

    @Query("SELECT r FROM Rating r " +
            "ORDER BY r.averageRating DESC " +
            "LIMIT :limit")
    List<Rating> findTopRatings(@Param("limit") int limit);
}
