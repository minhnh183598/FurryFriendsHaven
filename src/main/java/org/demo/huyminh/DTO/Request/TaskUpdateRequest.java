package org.demo.huyminh.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.DTO.Reponse.UserResponse;
import org.demo.huyminh.Entity.Tag;
import org.demo.huyminh.Entity.User;

import java.util.List;
import java.util.Set;

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
public class TaskUpdateRequest {

    int id;
    String name;
    String description;
    String status;
    String category;
    String dueDate;
    Set<String> tags;
}
