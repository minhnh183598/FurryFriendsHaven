package org.demo.huyminh.DTO.Request;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.Entity.Pet;
import org.demo.huyminh.Entity.User;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.Date;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequest {
    String applicationId;

    String petId;
    String id;
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
    int status = 0;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateIn;
    @DateTimeFormat(pattern = "HH:mm")
    LocalTime timeIn;
    @DateTimeFormat(pattern = "HH:mm")
    LocalTime timeOut;

    @ManyToOne
    @JoinColumn(name = "petId", referencedColumnName = "petId", insertable = false, updatable = false)
    Pet pet;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    User user;
}
