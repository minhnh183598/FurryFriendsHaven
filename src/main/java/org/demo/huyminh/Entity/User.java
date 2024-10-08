package org.demo.huyminh.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 7:20 AM
 */

@Entity
@Builder
@Getter
@Setter
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
    LocalDate dob;
    boolean isEnabled;
    boolean isPasswordChangeable;

    @ManyToMany
    Set<Role> roles;

    public User(String username, String password, String firstname, String lastname, LocalDate dob) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.isEnabled = false;
        this.isPasswordChangeable = false;
    }
}
