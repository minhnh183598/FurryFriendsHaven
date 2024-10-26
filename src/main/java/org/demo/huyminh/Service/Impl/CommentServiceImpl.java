package org.demo.huyminh.Service.Impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.DTO.Reponse.CommentResponse;
import org.demo.huyminh.DTO.Request.CommentRequest;
import org.demo.huyminh.Entity.Comment;
import org.demo.huyminh.Entity.Issue;
import org.demo.huyminh.Entity.Task;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Repository.CommentRepository;
import org.demo.huyminh.Repository.IssueRepository;
import org.demo.huyminh.Repository.TaskRepository;
import org.demo.huyminh.Service.CommentService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 11:48 PM
 */

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    CommentRepository commentRepository;
    IssueRepository issueRepository;
    TaskRepository taskRepository;

    @Override
    public CommentResponse createComment(int issueId, int taskId, CommentRequest request, User user) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new AppException(ErrorCode.ISSUE_NOT_FOUND));

        if(!task.getIssues().contains(issue)) {
            throw new AppException(ErrorCode.ISSUE_NOT_IN_TASK);
        }

        if (!issue.getReporter().equals(user) && !issue.getAssignees().contains(user)) {
            throw new AppException(ErrorCode.USER_NOT_IN_ISSUE);
        }

        Comment comment = Comment.builder()
                .content(request.getContent())
                .issue(issue)
                .username(user.getUsername())
                .build();
        issue.getComments().add(comment);

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
    public void deleteComment(int commentId, int issueId, int taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new AppException(ErrorCode.ISSUE_NOT_FOUND));

        if(!task.getIssues().contains(issue)) {
            throw new AppException(ErrorCode.ISSUE_NOT_IN_TASK);
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        if(comment.getUsername().equalsIgnoreCase(user.getUsername()) && comment.getIssue().equals(issue)) {
            commentRepository.delete(comment);
        } else {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_DELETE_COMMENT);
        }
    }


    @Override
    public CommentResponse updateComment(int commentId, int issueId, int taskId, CommentRequest request, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new AppException(ErrorCode.ISSUE_NOT_FOUND));

        if (!task.getIssues().contains(issue)) {
            throw new AppException(ErrorCode.ISSUE_NOT_IN_TASK);
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUsername().equalsIgnoreCase(user.getUsername())) {
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
    public List<CommentResponse> findCommentByIssueId(int issueId, int taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new AppException(ErrorCode.ISSUE_NOT_FOUND));

        if(!task.getIssues().contains(issue)) {
            throw new AppException(ErrorCode.ISSUE_NOT_IN_TASK);
        }
        List<Comment> comments = commentRepository.findCommentByIssueId(issueId);

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