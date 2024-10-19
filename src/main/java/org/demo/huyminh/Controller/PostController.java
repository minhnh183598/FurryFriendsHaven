package org.demo.huyminh.Controller;

import jakarta.persistence.EntityNotFoundException;
import org.demo.huyminh.Entity.Post;
import org.demo.huyminh.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();  // Lấy tên người dùng hiện tại

            post.setPostedBy(username);
            Post createdPost = postService.savePost(post,username);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Trả về lỗi 400 nếu tag không hợp lệ
        } catch (Exception e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getPostByCategory(@PathVariable String category) {
        try {
            // Lấy tên người dùng hiện tại
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            List<Post> posts = postService.getPostsByCategory(category);
            if (posts.isEmpty()) {
                // Nếu không có bài viết nào, trả về 200 OK với thông điệp
                return ResponseEntity.status(HttpStatus.OK).body("No posts found for the given category.");
            }
            return ResponseEntity.status(HttpStatus.OK).body(posts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPost() {
        try {
            // Lấy tên người dùng hiện tại
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName(); // Nếu cần sử dụng tên người dùng

            return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPost());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        try {
            // Lấy tên người dùng hiện tại
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName(); // Nếu cần sử dụng tên người dùng

            Post post = postService.getPostById(postId);
            return ResponseEntity.ok(post);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();  // Lấy tên người dùng hiện tại


            postService.likePost(postId, username);  // Truyền tên người dùng vào method likePost
            return ResponseEntity.ok(new String[]{"Post liked successfully."});
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPost(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String postedBy,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) List<String> tags) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();  // Lấy tên người dùng hiện tại

            List<Post> posts = postService.searchPost(name, postedBy, date, tags);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Post post = postService.getPostById(postId);

            // Kiểm tra nếu người dùng hiện tại không phải là người tạo bài viết
            if (!post.getPostedBy().equals(username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this post.");
            }
            postService.deletePost(postId);
            return ResponseEntity.status(HttpStatus.OK).body("Post deleted successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Thêm hàm updatePost
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody Post post) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Post existingPost = postService.getPostById(postId);

            // Kiểm tra nếu người dùng hiện tại không phải là người tạo bài viết
            if (!existingPost.getPostedBy().equals(username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to update this post.");
            }
            Post updatedPost = postService.updatePost(postId, post, username);
            return ResponseEntity.ok(updatedPost);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
