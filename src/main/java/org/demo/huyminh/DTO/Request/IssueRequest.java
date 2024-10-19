package org.demo.huyminh.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.Entity.Tag;
import org.demo.huyminh.Entity.User;
import java.time.LocalDate;
import java.util.Set;

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
public class IssueRequest {

    String title;
    String description;
    String status;
    String priority;
    LocalDate dueDate;
    Set<Tag> tags;
    User reporter;
}