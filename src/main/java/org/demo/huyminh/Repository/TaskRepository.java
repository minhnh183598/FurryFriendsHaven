package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Task;
import org.demo.huyminh.Entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
}
