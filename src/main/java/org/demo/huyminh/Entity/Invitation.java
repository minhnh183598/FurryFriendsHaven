package org.demo.huyminh.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.Enums.InvitationStatus;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@Table(name = "invitation")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int Id;
    String userId;
    int taskId;

    @Enumerated(EnumType.STRING)
    InvitationStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;
    LocalDateTime expiredAt;

    public Invitation(String userId, int taskId, LocalDateTime expiredAt, InvitationStatus status) {
        this.userId = userId;
        this.taskId = taskId;
        this.expiredAt = expiredAt;
        this.status = InvitationStatus.PENDING;
    }
}
