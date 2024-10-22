package org.demo.huyminh.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 11:57 PM
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CommentRequest {

    @NotBlank(message = "Comment content cannot be blank")
    @Size(max = 10000, message = "Comment cannot exceed 10000 characters")
    String content;
}
