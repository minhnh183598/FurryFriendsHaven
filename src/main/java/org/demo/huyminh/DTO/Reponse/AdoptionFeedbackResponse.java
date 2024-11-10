package org.demo.huyminh.DTO.Reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * @author Minh
 * Date: 11/9/2024
 * Time: 11:26 PM
 */

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdoptionFeedbackResponse {

    int id;
    String adopterName;
    String petName;
    LocalDateTime createdAt;
    LocalDateTime editedAt;
}
