package org.demo.huyminh.DTO.Reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.Entity.Rating;

import java.time.LocalDateTime;
import java.util.List;

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
    LocalDateTime feedbackFinishedAt;
    LocalDateTime taskCreatedAt;
}