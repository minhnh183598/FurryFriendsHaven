package org.demo.huyminh.DTO.Reponse;

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
public class FeedbackResponse {
    int id;
    String content;
    LocalDateTime editedAt;
    LocalDateTime createdAt;
    List<String> images;
    UserResponse reporter;
    String taskId;
    String petName;
    Rating rating;
}