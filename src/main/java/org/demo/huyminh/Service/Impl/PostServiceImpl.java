package org.demo.huyminh.Service.Impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.BriefPostResponse;
import org.demo.huyminh.DTO.Reponse.PostResponse;
import org.demo.huyminh.DTO.Request.PostCreationRequest;
import org.demo.huyminh.DTO.Request.PostUpdateRequest;
import org.demo.huyminh.Entity.*;
import org.demo.huyminh.Enums.Roles;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Mapper.PostMapper;
import org.demo.huyminh.Repository.PostRepository;
import org.demo.huyminh.Repository.TagRepository;
import org.demo.huyminh.Service.PostService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    PostRepository postRepository;
    PostMapper postMapper;
    TagRepository tagRepository;
    EntityManager entityManager;

    private static final List<String> VALID_CATEGORIES = Arrays.asList(
            "ANIMAL_RESCUE",
            "ADOPTION",
            "EVENT",
            "FUNDRAISING",
            "VOLUNTEER"
    );

    @Override
    public PostResponse createPost(PostCreationRequest request, User user) {
        if(postRepository.existsByNicknameAndDifferentUsername(request.getNickname(), user.getUsername())) {
            throw new AppException(ErrorCode.NICKNAME_WAS_ALREADY_TAKEN);
        }

        if (!VALID_CATEGORIES.contains(request.getCategory().toUpperCase())) {
            throw new AppException(ErrorCode.INVALID_CATEGORY);
        }

        Post newPost = postMapper.toPost(request);
        newPost.setUsername(user.getUsername());


        List<Image> images = request.getImages().stream()
                .map(imageUrl -> Image.builder()
                        .post(newPost)
                        .imageUrl(imageUrl)
                        .build())
                .toList();
        newPost.setImages(images);
        savePostWithTags(newPost);

        Post savedPost = postRepository.save(newPost);

        return postMapper.toPostResponse(savedPost);
    }

    public void savePostWithTags(Post post) {
        if (post.getTags() == null || post.getTags().isEmpty()) {
            throw new AppException(ErrorCode.POST_HAS_NO_TAGS);
        }

        List<Tag> existingTags = new ArrayList<>();

        for (Tag tag : post.getTags()) {
            Optional<Tag> existingTag = tagRepository.findById(tag.getName());

            if (existingTag.isPresent() && existingTag.get().getType().equals(Tag.TagType.POST_LABEL)) {
                existingTags.add(existingTag.get());
            }
        }

        if (existingTags.isEmpty()) {
            throw new AppException(ErrorCode.POST_HAS_NO_TAGS);
        }

        post.setTags(existingTags);
    }

    @Override
    public List<BriefPostResponse> findTopLikedPosts(LocalDate dateFrom, LocalDate dateTo, int limit) {
        if (dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        LocalDateTime dateFromDateTime = dateFrom != null ? dateFrom.atStartOfDay() : null;
        LocalDateTime dateToDateTime = dateTo != null ? dateTo.atTime(23, 59, 59) : null;

        String jpql = "SELECT p FROM Post p " +
                "WHERE (:dateFrom IS NULL OR p.createAt >= :dateFrom) " +
                "AND (:dateTo IS NULL OR p.createAt <= :dateTo) " +
                "ORDER BY p.likeCount DESC";

        TypedQuery<Post> query = entityManager.createQuery(jpql, Post.class);

        if (dateFromDateTime != null) {
            query.setParameter("dateFrom", dateFromDateTime);
        }

        if (dateToDateTime != null) {
            query.setParameter("dateTo", dateToDateTime);
        }

        query.setMaxResults(limit);
        List<BriefPostResponse> posts = query.getResultList().stream().map(postMapper::toBriefPostResponse).toList();
        if(posts.isEmpty()) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        return posts;
    }

    @Override
    public List<BriefPostResponse> getPostsByCriteria(
            String title, String username, LocalDate dateFrom,
            LocalDate dateTo, List<String> tags, String category
    ) {
        if (!VALID_CATEGORIES.contains(category.toUpperCase())) {
            throw new AppException(ErrorCode.INVALID_CATEGORY);
        }

        if (dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        LocalDateTime dateFromDateTime = dateFrom != null ? dateFrom.atStartOfDay() : null;
        LocalDateTime dateToDateTime = dateTo != null ? dateTo.atStartOfDay() : null;

        List<Post> posts = postRepository.findPostsByCriteriaAndUsername(title, username, dateFromDateTime, dateToDateTime, tags, category);
        if(posts.isEmpty()) {
            posts = postRepository.findPostsByCriteriaAndNickname(title, username, dateFromDateTime, dateToDateTime, tags, category);
        }

        if(posts.isEmpty()) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        return posts.stream().map(postMapper::toBriefPostResponse).toList();
    }

    @Override
    public List<BriefPostResponse> getAllPost() {
        List<Post> posts = postRepository.findAll();
        if(posts.isEmpty()) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        return posts.stream().map(postMapper::toBriefPostResponse).toList();
    }

    @Override
    public Post getPostById(int postId) {
         return postRepository.findById(postId)
                 .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
    }

    @Override
    public String likePost(int postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (!post.getLikedByUsers().contains(user.getUsername())) {
            post.setLikeCount(post.getLikeCount() + 1);
            post.getLikedByUsers().add(user.getUsername());
            postRepository.save(post);
            return "Like post successfully";
        } else {
            post.setLikeCount(post.getLikeCount() - 1);
            post.getLikedByUsers().remove(user.getUsername());
            postRepository.save(post);
            return "Unlike post successfully";
        }
    }

    @Override
    public void updatePost(int postId, PostUpdateRequest request, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if(!post.getUsername().equalsIgnoreCase(user.getUsername()) && !user.getRoles().contains(Roles.ADMIN)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_UPDATE_POST);
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setDescription(request.getDescription());
        post.setNickname(request.getNickname());
        post.setCategory(request.getCategory());

        if(request.getImages() != null) {
            post.getImages().clear();

            if(!request.getImages().isEmpty()) {
                List<Image> newImages = request.getImages().stream()
                        .map(imageUrl -> Image.builder()
                                .imageUrl(imageUrl)
                                .post(post)
                                .build())
                        .toList();
                post.getImages().addAll(newImages);
            }
        }

        if(request.getTags() != null) {
            post.getTags().clear();

            if(!request.getTags().isEmpty()) {
                List<Tag> newTags = request.getTags().stream()
                        .map(updatedTag -> tagRepository.findById(updatedTag.getName())
                                .filter(tag -> tag.getType().equals(Tag.TagType.POST_LABEL))
                                .orElse(null))
                        .filter(Objects::nonNull)
                        .toList();
                post.getTags().addAll(newTags);
            }
        }

        postRepository.save(post);
    }

    @Override
    public void deletePost(int postId, User user) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if(!existingPost.getUsername().equalsIgnoreCase(user.getUsername()) && user.getRoles().contains(Roles.ADMIN)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_DELETE_POST);
        }

        postRepository.deleteById(postId);
    }
}
