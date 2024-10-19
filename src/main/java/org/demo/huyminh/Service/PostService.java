package org.demo.huyminh.Service;

import org.demo.huyminh.Entity.Post;

import java.time.LocalDate;
import java.util.List;

public interface PostService {

    Post savePost(Post post, String username);

    List<Post> getPostsByCategory(String category);

    List<Post> getAllPost();

    Post getPostById(Long postId);

    void likePost(Long postId, String username);

    List<Post> searchPost(String name, String postedBy, LocalDate date, List<String> tags);

    Post updatePost(Long postId, Post post, String username);

    void deletePost(Long postId);


}
