package org.demo.huyminh.DTO.Reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BriefFeedbackResponse {

    int taskId;
    String adopterName;
    double rating;
    String applicationId;
    LocalDateTime feedbackFinishedAt;
    LocalDateTime taskCreatedAt;
}