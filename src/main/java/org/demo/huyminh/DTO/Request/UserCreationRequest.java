package org.demo.huyminh.DTO.Request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.Validator.DobConstraint;

import java.time.LocalDate;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 8:34 AM
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min = 4, message = "USERNAME_INVALID")
    String username;

    @Size(min = 6, message = "PASSWORD_INVALID")
    String password;
    String firstname;
    String lastname;
    String email;

    @DobConstraint(min = 10, message = "INVALID_DOB")
    LocalDate dob;
}
