package org.demo.huyminh.Service;

import org.demo.huyminh.DTO.Reponse.CommentResponse;
import org.demo.huyminh.DTO.Request.CommentRequest;
import org.demo.huyminh.Entity.User;

import java.util.List;

public interface CommentBlogService {

    CommentResponse createComment(int postId, CommentRequest request, User user);

    void deleteComment(int commentId, int postId, User user);

    CommentResponse updateComment(int commentId, int postId, CommentRequest request, User user);

    List<CommentResponse> findCommentByPostId(int postId);
}
