package org.demo.huyminh.DTO.Reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 8:06 PM
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class IssueResponse {

    String title;
    String description;
    int taskId;
    String status;
    String priority;
    LocalDate dueDate;
    List<String> tags;
    UserResponse reporter;
}