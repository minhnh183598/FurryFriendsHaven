package org.demo.huyminh.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    //status = 0 = pending
    //status = 1 = accept
    //status = 2 = refuse
    int status;

}
