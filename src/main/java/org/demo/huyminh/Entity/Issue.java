package org.demo.huyminh.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
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
    private String title;
    private String description;
    private String status;
    private int taskID;
    private String priority;
    private LocalDate dueDate;

    @OneToMany
    private Set<Tag> tags;

    @OneToMany
    private List<User> assignees;

    @ManyToOne
    private User reporter;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @JsonIgnore
    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}