package org.demo.huyminh.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.List;

/** @author Minh
* Date: 9/24/2024
* Time: 10:09 AM
*/ 

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserUpdateRequest {

    String username;
    String firstname;
    String lastname;
    List<String> roles;
}
