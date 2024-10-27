package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByCategory(String category);

    @Query("SELECT p FROM Post p LEFT JOIN p.tags t WHERE " +
            "(:title IS NULL OR p.title LIKE %:title%) " +
            "AND (:postedBy IS NULL OR p.username LIKE %:postedBy%) " +
            "AND (:dateFrom IS NULL OR p.createAt >= :dateFrom) " +
            "AND (:dateTo IS NULL OR p.createAt <= :dateTo) " +
            "AND (:#{#tags.size()} = 0 OR t.name IN :tags) " +
            "AND (:category IS NULL OR p.category = :category) " +
            "ORDER BY p.createAt DESC")
    List<Post> findPostsByCriteriaAndUsername(
            @Param("title") String title,
            @Param("postedBy") String username,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            @Param("tags") List<String> tags,
            @Param("category") String category
    );

    @Query("SELECT p FROM Post p LEFT JOIN p.tags t WHERE " +
            "(:title IS NULL OR p.title LIKE %:title%) " +
            "AND (:postedBy IS NULL OR p.nickname LIKE %:postedBy%) " +
            "AND (:dateFrom IS NULL OR p.createAt >= :dateFrom) " +
            "AND (:dateTo IS NULL OR p.createAt <= :dateTo) " +
            "AND (:#{#tags.size()} = 0 OR t.name IN :tags) " +
            "AND (:category IS NULL OR p.category = :category) " +
            "ORDER BY p.createAt DESC")
    List<Post> findPostsByCriteriaAndNickname(
            @Param("title") String title,
            @Param("postedBy") String nickname,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            @Param("tags") List<String> tags,
            @Param("category") String category
    );


    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Post p WHERE p.nickname = :nickname AND p.username != :username")
    boolean existsByNicknameAndDifferentUsername(@Param("nickname") String nickname,
                                                 @Param("username") String username);
}
