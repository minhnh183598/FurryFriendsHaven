package org.demo.huyminh.DTO.Reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskResponse {

    int id;
    String name;
    String description;
    String status;
    String category;
    LocalDateTime createdAt;
    LocalDateTime finishedAt;
    LocalDateTime dueDate;
    List<String> tags;
    List<BriefIssueResponse> issues;
    UserResponse owner;
    AdopterResponse adopter;
    List<UserResponse> team;
    List<FeedbackResponse> feedbacks;
    Checklist checklist;
}
