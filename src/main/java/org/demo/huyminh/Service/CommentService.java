package org.demo.huyminh.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.DTO.Request.CommentRequest;
import org.demo.huyminh.DTO.Request.DeleteCommentRequest;
import org.demo.huyminh.Entity.Comment;
import org.demo.huyminh.Entity.Issue;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Repository.CommentRepository;
import org.demo.huyminh.Repository.IssueRepository;
import org.demo.huyminh.Repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 11:48 PM
 */

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentService {

    CommentRepository commentRepository;
    IssueRepository issueRepository;
    UserRepository userRepository;

    public Comment createComment(CommentRequest request, String userId) throws Exception {
        Optional<Issue> issueOptional = issueRepository.findById(request.getIssueId());
        Optional<User> userOptional = userRepository.findById(userId);

        if(issueOptional.isEmpty()) {
            throw new Exception("Issue not found with id: " + request.getIssueId());
        }

        if(userOptional.isEmpty()) {
            throw new Exception("User not found with id: " + userId);
        }

        Issue issue = issueOptional.get();
        User user = userOptional.get();
        Comment comment = Comment.builder()
                .content(request.getContent())
                .issue(issue)
                .user(user)
                .build();

        issue.getComments().add(comment);

        return commentRepository.save(comment);
    }

    public void deleteComment(DeleteCommentRequest request, String userId) throws Exception {
        Optional<Comment> commentOptional = commentRepository.findById(request.getCommentId());
        Optional<Issue> issueOptional = issueRepository.findById(request.getIssueId());
        Optional<User> userOptional = userRepository.findById(userId);

        if(commentOptional.isEmpty()) {
            throw new Exception("Comment not found with id: " + request.getCommentId());
        }

        if(issueOptional.isEmpty()) {
            throw new Exception("Issue not found with id: " + request.getIssueId());
        }

        if(userOptional.isEmpty()) {
            throw new Exception("User not found with id: " + userId);
        }

        Comment comment = commentOptional.get();
        User user = userOptional.get();
        Issue issue = issueOptional.get();

        if(comment.getUser().equals(user) && comment.getIssue().equals(issue)) {
            commentRepository.delete(comment);
        } else {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_DELETE_COMMENT);
        }
    }

    public List<Comment> findCommentByIssueId(int issueId) {
        return commentRepository.findCommentByIssueId(issueId);
    }
}