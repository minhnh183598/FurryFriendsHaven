package org.demo.huyminh.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.demo.huyminh.Enums.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 11:23 AM
 */

@Builder
@Entity
@Data
@Table(name = "issue")
@NoArgsConstructor
@AllArgsConstructor
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String title;
    private String description;
    private Status status;
    private int taskID;
    private String priority;
    private LocalDate dueDate;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Tag> tags;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> assignees;

    @ManyToOne
    @JsonIgnore
    private User reporter;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private Task task;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", taskID=" + taskID +
                ", priority='" + priority + '\'' +
                ", dueDate=" + dueDate +
                ", tags=" + tags.stream().map(Tag::getName).toList() +
                ", comments=" + comments +
                '}';
    }
}