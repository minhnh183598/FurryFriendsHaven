package org.demo.huyminh.DTO.Reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * @author Minh
 * Date: 11/4/2024
 * Time: 11:14 PM
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetFeedbackResponse {

    String petId;
    String petName;
    int feedbackCount;
    String petStatus;
}
