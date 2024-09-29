package org.demo.huyminh.DTO.Reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.Set;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 12:49 PM
 */

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    String id;
    String username;
    String firstname;
    String lastname;
    String email;
    LocalDate dob;
    Set<RoleResponse> roles;
}