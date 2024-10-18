package org.demo.huyminh.Controller;

import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Reponse.CommentResponse;
import org.demo.huyminh.DTO.Request.CommentRequest;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Service.CommentService;
import org.demo.huyminh.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Minh
 * Date: 10/18/2024
 * Time: 2:38 PM
 */

@RestController
@RequestMapping("/api/v1/comments")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentController {

    CommentService commentService;
    UserService userService;

    @PostMapping("/issue/{issueId}/task/{taskId}")
    public ApiResponse<CommentResponse> createComment(
            @RequestBody CommentRequest request,
            @PathVariable int issueId,
            @PathVariable int taskId,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        User user = userService.findByToken(jwt);

        return ApiResponse.<CommentResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Create comment successfully")
                .result(commentService.createComment(issueId, taskId, request, user))
                .build();
    }

    @DeleteMapping("/{commentId}/issue/{issueId}/task/{taskId}")
    public ApiResponse<Void> deleteComment(
            @PathVariable int commentId,
            @PathVariable int issueId,
            @PathVariable int taskId,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        User user = userService.findByToken(jwt);

        commentService.deleteComment(commentId, issueId, taskId, user);

        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete comment successfully")
                .build();
    }

    @PutMapping("/{commentId}/issue/{issueId}/task/{taskId}")
    public ApiResponse<CommentResponse> updateComment(
            @PathVariable int commentId,
            @PathVariable int issueId,
            @PathVariable int taskId,
            @RequestBody CommentRequest request,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        User user = userService.findByToken(jwt);

        return ApiResponse.<CommentResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update comment successfully")
                .result(commentService.updateComment(commentId, issueId, taskId, request, user))
                .build();
    }

    @GetMapping("issue/{issueId}/task/{taskId}")
    public ApiResponse<List<CommentResponse>> findCommentByIssueId(
            @PathVariable int issueId,
            @PathVariable int taskId
    ) {
        List<CommentResponse> comments = commentService.findCommentByIssueId(issueId, taskId);
        return ApiResponse.<List<CommentResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Find comment successfully")
                .result(comments)
                .build();
    }
}
