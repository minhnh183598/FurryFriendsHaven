package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Issue;
import org.demo.huyminh.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 1:32 PM
 */

public interface IssueRepository extends JpaRepository<Issue, Integer> {

    List<Issue> findByTaskId(int taskId);
}
