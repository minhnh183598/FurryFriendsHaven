package org.demo.huyminh.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.Entity.ChecklistItemTemplate;
import java.util.List;

/**
 * @author Minh
 * Date: 10/24/2024
 * Time: 7:02 PM
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TemplateUpdateRequest {

    int id;
    String name;
    List<ChecklistItemTemplate> items;
}
