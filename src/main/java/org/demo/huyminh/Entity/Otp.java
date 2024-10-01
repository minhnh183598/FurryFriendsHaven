package org.demo.huyminh.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
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

    final static int EXPIRATION_TIME = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String code;
    Date expireTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    public Otp(String code, User user) {
        this.code = code;
        this.user = user;
        this.expireTime = generateExpireTime();
    }

    public Date generateExpireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);

        return new Date(calendar.getTime().getTime());
    }
}
