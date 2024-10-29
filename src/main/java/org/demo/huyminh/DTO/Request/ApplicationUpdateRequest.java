package org.demo.huyminh.DTO.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUpdateRequest {

    String fullName;
    int yob;
    String gender;
    String address;
    String city;
    String job;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})\\b")
    String phone;
    String liveIn;
    String liveWith;

    String firstPerson;
    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})\\b")
    String firstPhone;

    String secondPerson;
    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})\\b")
    String secondPhone;

    int status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateIn;
    String timeIn;
    String timeOut;
    
    @Column(name = "update_status_at")
    private LocalDateTime updateAt;
}
