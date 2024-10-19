package org.demo.huyminh.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 7:20 AM
 */

@Entity
@Builder
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String username;
    String password;
    String firstname;
    String lastname;

    @Column(unique = true)
    String email;
    boolean isEnabled;
    boolean isPasswordChangeable;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Application> applications;

    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Issue> assignedIssues;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Task> tasks;

    @ManyToMany
    Set<Role> roles;

    @Column
    private int applicationQuantity;

    public User(String username, String password, String firstname, String lastname) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isEnabled = false;
        this.isPasswordChangeable = false;
        this.createdAt = LocalDateTime.now();
        this.applicationQuantity = 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", isEnabled=" + isEnabled +
                ", isPasswordChangeable=" + isPasswordChangeable +
                ", createdAt=" + createdAt +
                ", applicationQuantity=" + applicationQuantity +
                ", roles=" + roles.stream().map(Role::getName).toList() +
                '}';
    }
}