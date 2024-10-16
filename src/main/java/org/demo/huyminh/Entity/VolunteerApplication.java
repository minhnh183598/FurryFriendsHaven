package org.demo.huyminh.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "volunteer_application")
public class VolunteerApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String volunteerAppliId;

    String id;

    String fullName;
    int yob;
    String gender;
    String address;
    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})\\b")
    String phone;
    String adoptionExp;

    String daysOfWeek;
    String morning;
    String afternoon;
    String reason;
    //status = 0 = pending
    //status = 1 = accept
    //status = 2 = refuse
    int status = 0;

    //Thoi gian update Status
    @UpdateTimestamp
    @Column(name = "update_Status_at")
    private LocalDateTime updateAt;

    @CreationTimestamp
    @Column(name = "create_Appli_at")
    private LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    User user;
}
