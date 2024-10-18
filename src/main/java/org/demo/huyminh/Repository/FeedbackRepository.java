package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * @author Minh
 * Date: 10/17/2024
 * Time: 5:35 PM
 */

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    @Query("SELECT f FROM Feedback f WHERE f.task.id = ?1")
    List<Feedback> findAllByTaskId(int taskId);
}
