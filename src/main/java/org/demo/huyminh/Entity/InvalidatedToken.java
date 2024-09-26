package org.demo.huyminh.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 7:20 AM
 */

@Entity
@Builder
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class InvalidatedToken {

    @Id
    String id;
    Date expiryTime;
}
