package org.demo.huyminh.DTO.Reponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 12:49 PM
 */

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BriefVolunteerResponse {
    String username;

    @JsonIgnore
    String firstname;

    @JsonIgnore
    String lastname;

    @JsonProperty("fullname")
    public String getFullname() {
        return lastname + " " + firstname;
    }
}