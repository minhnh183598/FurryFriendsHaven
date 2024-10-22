package org.demo.huyminh.DTO.Reponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * @author Minh
 * Date: 10/21/2024
 * Time: 9:51 AM
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExcelResponse {
    String message;
}
