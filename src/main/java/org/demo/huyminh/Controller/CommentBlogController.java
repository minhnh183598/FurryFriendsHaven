package org.demo.huyminh.Controller;

import org.demo.huyminh.Service.CommentBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin
public class CommentBlogController {

    @Autowired
    private CommentBlogService commentBlogService;

    @PostMapping("comments/create")
    public ResponseEntity<?> createComment(@RequestParam Long postId, @RequestParam String content) {
        try {
            // Lấy tên người dùng hiện tại từ SecurityContextHolder
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String postedBy = authentication.getName();  // Lấy tên người dùng hiện tại

            return ResponseEntity.ok(commentBlogService.createComment(postId, postedBy, content));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @GetMapping("comments/{postId}")
    public ResponseEntity<?> getCommetsByPostId(@PathVariable Long postId) {
        try {
            return ResponseEntity.ok(commentBlogService.getCommentsByPostId(postId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something Went Wrong.");
        }
    }
}
