package org.demo.huyminh.DTO.Reponse;

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
    String phone;
    String liveIn;
    int NumOfChild;
    String AgeOfChild;
    String liveWith;
    String firstPerson;
    String firstPhone;
    String secondPerson;
    String secondPhone;
    int status;


}
