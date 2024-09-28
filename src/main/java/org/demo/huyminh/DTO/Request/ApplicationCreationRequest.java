package org.demo.huyminh.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationCreationRequest {

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
    int status = 0;

}
