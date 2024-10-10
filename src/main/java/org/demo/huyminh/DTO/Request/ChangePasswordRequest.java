package org.demo.huyminh.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 5:25 PM
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {

    String oldPassword;
    String newPassword;
    String confirmPassword;
}
