package org.demo.huyminh.Service;

import org.demo.huyminh.DTO.Reponse.BriefPostResponse;
import org.demo.huyminh.DTO.Reponse.PostResponse;
import org.demo.huyminh.DTO.Request.PostCreationRequest;
import org.demo.huyminh.DTO.Request.PostUpdateRequest;
import org.demo.huyminh.Entity.Post;
import org.demo.huyminh.Entity.User;
import java.time.LocalDate;
import java.util.List;

public interface PostService {
    PostResponse createPost(PostCreationRequest request, User user);

    List<BriefPostResponse> findTopLikedPosts(LocalDate dateFrom, LocalDate dateTo, int limit);

    List<BriefPostResponse> getPostsByCriteria(
            String title, String username, LocalDate dateFrom,
            LocalDate dateTo, List<String> tags, String category
    );

    List<BriefPostResponse> getAllPost();

    Post getPostById(int postId);

    String likePost(int postId, User user);

    void updatePost(int postId, PostUpdateRequest request, User user);

    void deletePost(int postId, User user);
}
