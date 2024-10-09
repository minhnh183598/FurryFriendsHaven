package org.demo.huyminh.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Minh
 * Date: 9/28/2024
 * Time: 7:57 PM
 */

@Entity
@Builder
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Otp {

    public final static int EXPIRATION_TIME = 1;
    public final static int MAX_REFRESH_COUNT = 5;
    public final static long LOCKOUT_DURATION_MS = 5;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String code;
    Date expireTime;
    int refreshCount;
    Date lockoutTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    public Otp(String code, User user) {
        this.code = code;
        this.user = user;
        this.expireTime = generateExpireTime();
        this.refreshCount = 0;
        this.lockoutTime = null;
    }

    public Date generateExpireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);

        return new Date(calendar.getTime().getTime());
    }

    public boolean isLockedOut() {
        return lockoutTime != null && new Date().before(lockoutTime);
    }

    public void lockOut() {
        this.lockoutTime = Date.from(Instant.now().plus(LOCKOUT_DURATION_MS, ChronoUnit.MINUTES));
    }
}
