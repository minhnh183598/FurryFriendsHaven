package org.demo.huyminh.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Minh
 * Date: 9/29/2024
 * Time: 3:29 PM
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ResetPasswordRequest {

    String userId;
    String newPassword;
}
