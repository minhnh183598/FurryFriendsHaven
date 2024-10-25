package org.demo.huyminh.DTO.Reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.Entity.Checklist;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 1:43 PM
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AdoptionTaskResponse {

    int id;
    String name;
    String description;
    String status;
    String category;
    LocalDateTime dueDate;
    UserResponse volunteer;
    UserResponse adopter;
    Checklist checklist;
    List<FeedbackResponse> feedbacks;
}
