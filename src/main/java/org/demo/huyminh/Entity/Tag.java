package org.demo.huyminh.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.util.Set;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 11:18 AM
 */

@Entity
@Builder
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    @Column(nullable = false, unique = true)
    String name;

    @JsonIgnore
    @Column(length = 255)
    String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TagType type;

    @ManyToMany
    @JsonIgnore
    Set<Task> tasks;

    @ManyToMany
    @JsonIgnore
    Set<Post> posts;

    public enum TagType {
        TASK_LABEL,
        ISSUE_LABEL,
        POST_LABEL,
        EVENT_LABEL
    }
}

