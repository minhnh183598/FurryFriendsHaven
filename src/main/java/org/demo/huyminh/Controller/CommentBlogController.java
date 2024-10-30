package org.demo.huyminh.Controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Reponse.CommentResponse;
import org.demo.huyminh.DTO.Request.CommentRequest;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Service.CommentBlogService;
import org.demo.huyminh.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentBlogController {

    CommentBlogService commentBlogService;
    UserService userService;

    @PostMapping("/post/{postId}")
    public ApiResponse<CommentResponse> createPostComment(
            @PathVariable int postId,
            @RequestBody CommentRequest request,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        User user = userService.findByToken(jwt);

        return ApiResponse.<CommentResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Create comment successfully")
                .result(commentBlogService.createComment(postId, request, user))
                .build();
    }

    @GetMapping("/post/{postId}")
    public ApiResponse<List<CommentResponse>> getPostComments(@PathVariable int postId) {
        return ApiResponse.<List<CommentResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get comments successfully")
                .result(commentBlogService.findCommentByPostId(postId))
                .build();
    }

    @PutMapping("/{commentId}/post/{postId}")
    public ApiResponse<CommentResponse> updatePostComment(
            @PathVariable int commentId,
            @PathVariable int postId,
            @RequestBody CommentRequest request,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        User user = userService.findByToken(jwt);

        return ApiResponse.<CommentResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update comment successfully")
                .result(commentBlogService.updateComment(commentId, postId, request, user))
                .build();
    }

    @DeleteMapping("/{commentId}/post/{postId}")
    public ApiResponse<Void> deletePostComment(
            @PathVariable int commentId,
            @PathVariable int postId,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        User user = userService.findByToken(jwt);

        commentBlogService.deleteComment(commentId, postId, user);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete comment successfully")
                .build();
    }
}
