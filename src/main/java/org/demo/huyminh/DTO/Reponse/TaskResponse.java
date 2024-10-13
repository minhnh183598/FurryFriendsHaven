package org.demo.huyminh.DTO.Reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
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

    private int id;
    private String name;
    private String description;
    private String category;
    private List<String> tags;
    private List<String> issues;
    private UserResponse owner;
    private List<UserResponse> team;
}
