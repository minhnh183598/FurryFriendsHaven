package org.demo.huyminh.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "application")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String applicationId;
    String id;

    String petId;
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

    //status = 0 = pending
    //status = 1 = adopted
    //status = 2 = Reject
    int status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateIn;
    @DateTimeFormat(pattern = "HH:mm")
    LocalTime timeIn;
    @DateTimeFormat(pattern = "HH:mm")
    LocalTime timeOut;

    @UpdateTimestamp
    @Column(name = "update_status_at")
    private LocalDateTime updateAt;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    User user;

    @ManyToOne
    @JoinColumn(name = "petId", referencedColumnName = "petId", insertable = false, updatable = false)
    Pet pet;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    Task task;

    @Override
    public String toString() {
        return "Application{" +
                "applicationId='" + applicationId + '\'' +
                ", petId='" + pet.getPetId() + '\'' +
                ", fullName='" + fullName + '\'' +
                ", status=" + status +
                ", userId=" + (user != null ? user.getId() : "null") +
                '}';
    }
}