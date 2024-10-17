package org.demo.huyminh.Entity;

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
    private int id;

    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    private Status status;
    private String category;
    private LocalDateTime dueDate;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "task_tags",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @ManyToOne
    private User owner;

    @OneToMany(fetch = FetchType.LAZY , mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Issue> issues;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> team;

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