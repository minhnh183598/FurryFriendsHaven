package org.demo.huyminh.Service;

import org.demo.huyminh.DTO.Reponse.CommentResponse;
import org.demo.huyminh.DTO.Request.CommentRequest;
import org.demo.huyminh.Entity.User;

import java.util.List;

/**
 * @author Minh
 * Date: 10/26/2024
 * Time: 9:47 PM
 */

public interface CommentService {
    CommentResponse createComment(int issueId, int taskId, CommentRequest request, User user);

    void deleteComment(int commentId, int issueId, int taskId, User user);

    CommentResponse updateComment(int commentId, int issueId, int taskId, CommentRequest request, User user);

    List<CommentResponse> findCommentByIssueId(int issueId, int taskId);
}
