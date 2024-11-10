package org.demo.huyminh.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    String content;

    @Column(columnDefinition = "TEXT", nullable = false)
    String description;

    String username;

    String nickname;

    String category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    List<Image> images;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createAt;

    @UpdateTimestamp
    LocalDateTime editedAt;

    int likeCount;

    @ElementCollection
    List<String> likedByUsers = new ArrayList<>();

    int viewCount;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    List<Tag> tags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Comment> comments;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", description='" + description + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", category='" + category + '\'' +
                ", createAt=" + createAt +
                ", editedAt=" + editedAt +
                ", likeCount=" + likeCount +
                ", likedByUsers=" + likedByUsers +
                ", viewCount=" + viewCount +
                '}';
    }
}
