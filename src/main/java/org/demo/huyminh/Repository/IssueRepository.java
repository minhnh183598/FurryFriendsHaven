package org.demo.huyminh.Repository;

import feign.Param;
import org.demo.huyminh.Entity.Issue;
import org.demo.huyminh.Entity.Task;
import org.demo.huyminh.Enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 1:32 PM
 */

public interface IssueRepository extends JpaRepository<Issue, Integer> {

    @Query("SELECT i FROM Issue i WHERE i.task.id = :taskId ORDER BY i.dueDate DESC")
    List<Issue> findByTaskId(@Param("taskId") int taskId);

    @Query("SELECT i FROM Issue i WHERE i.task.id = :taskId AND i.status = :status ORDER BY i.dueDate DESC")
    List<Issue> findByTaskIdAndStatusDescOrder(@Param("taskId") int taskId, @Param("status") Status status);


    @Query("SELECT i FROM Issue i WHERE i.task.id = :taskId AND i.status = :status ORDER BY i.dueDate ASC")
    List<Issue> findByTaskIdAndStatusAscOrder(@Param("taskId") int taskId, @Param("status") Status status);
}
