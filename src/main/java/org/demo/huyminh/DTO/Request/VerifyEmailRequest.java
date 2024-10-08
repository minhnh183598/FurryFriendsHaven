package org.demo.huyminh.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Minh
 * Date: 9/29/2024
 * Time: 5:30 PM
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class VerifyEmailRequest {

    String otp;
    String userId;
}
