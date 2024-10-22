package org.demo.huyminh.Repository;


import org.demo.huyminh.Entity.Event;
import org.demo.huyminh.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategory(String category);

    List<Post> findByLikeCountBetween(int minLikes, int maxLikes);

    @Query("SELECT p FROM Post p WHERE (:name IS NULL OR p.name LIKE %:name%) " +
            "AND (:postedBy IS NULL OR p.postedBy = :postedBy) " +
            "AND (:date IS NULL OR p.date = :date) " +
            "AND (:tags IS NULL OR p.tags IN :tags)")
    List<Post> findPostsByCriteria(@Param("name") String name,
                                   @Param("postedBy") String postedBy,
                                   @Param("date") LocalDate date,
                                   @Param("tags") List<String> tags);
}
