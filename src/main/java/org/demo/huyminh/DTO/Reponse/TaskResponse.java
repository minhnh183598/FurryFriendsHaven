package org.demo.huyminh.DTO.Reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
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
public class TaskResponse {

    int id;
    String name;
    String description;
    String status;
    String category;
    LocalDateTime dueDate;
    List<String> tags;
    List<BriefIssueResponse> issues;
    UserResponse owner;
    UserResponse adopter;
    List<UserResponse> team;
    List<FeedbackResponse> feedbacks;
}
