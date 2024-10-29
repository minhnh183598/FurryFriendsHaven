package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 11:44 PM
 */

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findCommentByIssueId(int issueId);

    List<Comment> findCommentByPostId(int postId);
}
