package org.demo.huyminh.Service;


import jakarta.persistence.EntityNotFoundException;
import org.demo.huyminh.Entity.Event;
import org.demo.huyminh.Entity.Post;
import org.demo.huyminh.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostRepository postRepository;

    private static final Set<String> VALID_TAGS = new HashSet<>(Arrays.asList(
            "Animal Rescue",
            "Pet Adoption",
            "Veterinary Care",
            "Animal Welfare",
            "Success Stories",
            "Volunteer Work",
            "Pet Care Tips"
    ));
    private static final List<String> VALID_CATEGORIES = Arrays.asList(
            "ANIMAL_RESCUE",
            "ADOPTION",
            "FUNDRAISING",
            "VOLUNTEER"
    );

    @Override
    public Post savePost(Post post, String username) {
        // Gán tên người dùng vào bài viết
        post.setPostedBy(username);

        // Kiểm tra category hợp lệ
        if (!VALID_CATEGORIES.contains(post.getCategory())) {
            throw new IllegalArgumentException("Category '" + post.getCategory() + "' is not valid.");
        }

        if (post.getTags() != null) {
            for (String tagName : post.getTags()) {
                if (!VALID_TAGS.contains(tagName)) {
                    throw new IllegalArgumentException("Tag '" + tagName + "' is not valid."); // Ném lỗi nếu tag không hợp lệ
                }
            }
        }
        // Khởi tạo các giá trị mặc định
        post.setLikeCount(0);
        post.setViewCount(0);
        post.setDate(LocalDate.now());

        // Lưu bài viết vào cơ sở dữ liệu
        return postRepository.save(post);
    }

    public List<Post> getPostsByCategory(String category) {
        return postRepository.findByCategory(category);
    }

    public List<Post> getAllPost() {
        return postRepository.findAll();
    }

    public Post getPostById(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setViewCount(post.getViewCount() + 1);
            return postRepository.save(post);
        } else {
            throw new EntityNotFoundException("Post not found");
        }
    }

    public void likePost(Long postId, String username) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            // Kiểm tra xem người dùng đã thích bài viết chưa (nếu bạn đang lưu danh sách người thích)
            if (!post.getLikedByUsers().contains(username)) {
                post.setLikeCount(post.getLikeCount() + 1); // Tăng số lượng likes
                post.getLikedByUsers().add(username); // Thêm người dùng vào danh sách đã thích
                postRepository.save(post); // Lưu bài viết
            } else {
                // Có thể xử lý nếu người dùng đã thích bài viết trước đó
                throw new IllegalArgumentException("User has already liked this post.");
            }
        } else {
            throw new EntityNotFoundException("Post not found with id: " + postId);
        }
    }

    public List<Post> searchPost(String name, String postedBy, LocalDate date, List<String> tags) {
        // Logic tìm kiếm để lọc các bài viết theo tiêu chí
        // Giả định bạn có một repository tương ứng để truy vấn cơ sở dữ liệu
        // Sử dụng phương thức tìm kiếm dựa trên tiêu chí đã cho
        return postRepository.findPostsByCriteria(name, postedBy, date, tags);
    }

    public List<Post> searchByLikeCount(int minLikes, int maxLikes) {
        return postRepository.findByLikeCountBetween(minLikes, maxLikes);
    }

    public Post updatePost(Long postId, Post post, String username) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            // Cập nhật các trường cần thiết
            existingPost.setName(post.getName());
            existingPost.setContent(post.getContent());
            existingPost.setPostedBy(username); // Cập nhật người đăng nếu cần
            return postRepository.save(existingPost);
        } else {
            throw new EntityNotFoundException("Post not found with id: " + postId);
        }
    }


    public void deletePost(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            postRepository.deleteById(postId);
        } else {
            throw new EntityNotFoundException("Post not found with id: " + postId);
        }
    }


}
