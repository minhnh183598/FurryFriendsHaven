package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Task;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Enums.Status;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 11:47 AM
 */

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query("select t from Task t where t.name like ?1")
    List<Task> findByPartialName(String partialName);

    @Query("SELECT t FROM Task t WHERE :user MEMBER OF t.team OR t.owner = :owner")
    List<Task> findByTeamContainingOrOwner(@Param("user") User user, @Param("owner") User owner);

    @Query(value = "SELECT * FROM task", nativeQuery = true)
    List<Task> findAllTasks();

    @Query("SELECT t FROM Task t WHERE (:category IS NULL OR t.category = :category) " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:dueDate IS NULL OR t.dueDate = :dueDate) " +
            "AND (:keyword IS NULL OR t.name LIKE %:keyword%) " +
            "ORDER BY t.dueDate ASC")
    List<Task> findByFiltersAscOrder(
            @Param("category") String category,
            @Param("status") Status status,
            @Param("dueDate") LocalDateTime dueDate,
            @Param("keyword") String keyword
    );

    @Query("SELECT t FROM Task t WHERE (:category IS NULL OR t.category = :category) " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:dueDate IS NULL OR t.dueDate = :dueDate) " +
            "AND (:keyword IS NULL OR t.name LIKE %:keyword%) " +
            "ORDER BY t.dueDate DESC")
    List<Task> findByFiltersDescOrder(
            @Param("category") String category,
            @Param("status") Status status,
            @Param("dueDate") LocalDateTime dueDate,
            @Param("keyword") String keyword
    );
}