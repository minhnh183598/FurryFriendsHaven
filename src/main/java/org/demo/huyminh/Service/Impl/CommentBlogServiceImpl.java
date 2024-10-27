package org.demo.huyminh.Service.Impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.DTO.Reponse.CommentResponse;
import org.demo.huyminh.DTO.Request.CommentRequest;
import org.demo.huyminh.Entity.Comment;
import org.demo.huyminh.Entity.Post;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Enums.Roles;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Repository.CommentRepository;
import org.demo.huyminh.Repository.PostRepository;
import org.demo.huyminh.Service.CommentBlogService;
import org.springframework.stereotype.Service;
import java.util.List;

/** @author Minh
* Date: 10/27/2024
* Time: 3:29 PM
*/ 

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentBlogServiceImpl implements CommentBlogService {

    PostRepository postRepository;
    CommentRepository commentRepository;

    @Override
    public CommentResponse createComment(int postId, CommentRequest request, User user) {
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .username(user.getUsername())
                .post(post)
                .build();
        post.getComments().add(comment);

        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.builder()
                .id(savedComment.getId())
                .createdAt(savedComment.getCreatedAt())
                .editedAt(savedComment.getUpdatedAt())
                .content(savedComment.getContent())
                .user(savedComment.getUsername())
                .build();
    }

    @Override
    public void deleteComment(int commentId, int postId, User user) {
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        if(!post.getComments().contains(comment)) {
            throw new AppException(ErrorCode.COMMENT_NOT_IN_POST);
        }

        if(!comment.getUsername().equalsIgnoreCase(user.getUsername()) && user.getRoles().contains(Roles.ADMIN)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_DELETE_COMMENT);
        }

        commentRepository.delete(comment);
    }

    @Override
    public CommentResponse updateComment(int commentId, int postId, CommentRequest request, User user) {
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        if(!post.getComments().contains(comment)) {
            throw new AppException(ErrorCode.COMMENT_NOT_IN_POST);
        }

        if(!comment.getUsername().equalsIgnoreCase(user.getUsername()) && user.getRoles().contains(Roles.ADMIN)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_UPDATE_COMMENT);
        }

        comment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return CommentResponse.builder()
                .id(updatedComment.getId())
                .createdAt(updatedComment.getCreatedAt())
                .editedAt(updatedComment.getUpdatedAt())
                .content(updatedComment.getContent())
                .user(updatedComment.getUsername())
                .build();
    }

    @Override
    public List<CommentResponse> findCommentByPostId(int postId) {
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        List<Comment> comments = commentRepository.findCommentByPostId(postId);

        if(comments.isEmpty()) {
            throw new AppException(ErrorCode.COMMENT_NOT_FOUND);
        }

        return comments.stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .createdAt(comment.getCreatedAt())
                        .editedAt(comment.getUpdatedAt())
                        .content(comment.getContent())
                        .user(comment.getUsername())
                        .build())
                .toList();
    }
}
