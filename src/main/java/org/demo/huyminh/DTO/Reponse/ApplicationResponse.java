package org.demo.huyminh.DTO.Reponse;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationResponse {

    String applicationId;
    String fullName;
    int yob;
    String gender;
    String address;
    String city;
    String job;
    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})\\b")
    @Column(unique = true)
    String phone;
    String liveIn;
    String liveWith;
    String firstPerson;
    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})\\b")
    @Column(unique = true)
    String firstPhone;
    String secondPerson;
    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})\\b")
    @Column(unique = true)
    String secondPhone;
    int status;


}
