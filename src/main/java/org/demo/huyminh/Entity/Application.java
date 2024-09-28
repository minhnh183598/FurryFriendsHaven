package org.demo.huyminh.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
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
    //status = 0 = pending
    //status = 1 = accept
    //status = 2 = refuse
    int status;
    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime create_at;

}
