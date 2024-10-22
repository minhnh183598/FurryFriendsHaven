package org.demo.huyminh.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.Enums.Status;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 11:00 AM
 */

@Entity
@Builder
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(name = "task")
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false, unique = true)
    String name;
    String description;
    Status status;
    String category;
    LocalDateTime dueDate;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "task_tags",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    Set<Tag> tags;

    @ManyToOne
    User owner;

    @ManyToOne
    @JsonIgnore
    User adopter;

    @OneToMany(fetch = FetchType.LAZY , mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Issue> issues;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<User> team;

    @OneToMany
    List<Feedback> feedbacks;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", tags=" + tags +'}';
    }
}