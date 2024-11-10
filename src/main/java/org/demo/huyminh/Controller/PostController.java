package org.demo.huyminh.Controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Reponse.BriefPostResponse;
import org.demo.huyminh.DTO.Reponse.PostResponse;
import org.demo.huyminh.DTO.Request.PostCreationRequest;
import org.demo.huyminh.DTO.Request.PostUpdateRequest;
import org.demo.huyminh.Entity.Post;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Service.PostService;
import org.demo.huyminh.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/** @author Minh
* Date: 10/27/2024
* Time: 1:56 AM
*/

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {

    UserService userService;;
    PostService postService;

    @PostMapping
    public ApiResponse<PostResponse> createPost(
            @RequestBody PostCreationRequest request ,
            @RequestHeader("Authorization") String token
    ) {
       String jwt = token.substring(7);
       User user = userService.findByToken(jwt);

       return ApiResponse.<PostResponse>builder()
               .code(HttpStatus.CREATED.value())
               .message("Create post successfully")
               .result(postService.createPost(request, user))
               .build();
    }

    @GetMapping("/{postId}")
    public ApiResponse<Post> getPostById(@PathVariable int postId) {
        return ApiResponse.<Post>builder()
                .code(HttpStatus.OK.value())
                .message("Get posts successfully")
                .result(postService.getPostById(postId))
                .build();
    }

    @GetMapping
    public ApiResponse<List<BriefPostResponse>> getAllPosts() {
        return ApiResponse.<List<BriefPostResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get posts successfully")
                .result(postService.getAllPost())
                .build();
    }

    @GetMapping("/topLikedPosts")
    public ApiResponse<List<BriefPostResponse>> getTopLikedPosts(
            @RequestParam(value = "dateFrom", required = false) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false) LocalDate dateTo,
            @RequestParam(value = "limit", defaultValue = "10") int limit
    ) {
        return ApiResponse.<List<BriefPostResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get top liked posts successfully")
                .result(postService.findTopLikedPosts(dateFrom, dateTo, limit))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<BriefPostResponse>> getPostsByCriteria(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "dateFrom", required = false) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false) LocalDate dateTo,
            @RequestParam(value = "tags", required = false) List<String> tags,
            @RequestParam(value = "category", required = false) String category
    ) {
        return ApiResponse.<List<BriefPostResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get posts successfully")
                .result(postService.getPostsByCriteria(title, username, dateFrom, userId, dateTo, tags, category))
                .build();
    }

    @PutMapping("/{postId}/liked")
    public ApiResponse<Void> likePost(
            @PathVariable int postId,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        User user = userService.findByToken(jwt);

        String message = postService.likePost(postId, user);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(message)
                .build();
    }

    @PutMapping("/{postId}")
    public ApiResponse<Void> updatePost(
            @PathVariable int postId,
            @RequestBody PostUpdateRequest request,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        User user = userService.findByToken(jwt);

        postService.updatePost(postId, request, user);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Update post successfully")
                .build();
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(
            @PathVariable int postId,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        User user = userService.findByToken(jwt);

        postService.deletePost(postId, user);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete post successfully")
                .build();
    }
}
