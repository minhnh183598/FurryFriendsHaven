package org.demo.huyminh.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.HashSet;
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
    String email;
    LocalDate dob;

    String applicationId;

    @ManyToMany
    Set<Role> roles;


    public User(String username, String password, String firstname, String lastname, LocalDate dob) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
    }
}
