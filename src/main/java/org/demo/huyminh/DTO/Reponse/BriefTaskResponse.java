package org.demo.huyminh.DTO.Reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * @author Minh
 * Date: 10/25/2024
 * Time: 1:34 PM
 */

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BriefTaskResponse {

    int id;
    String name;
    String status;
    LocalDateTime dueDate;
}
