package org.demo.huyminh.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

/**
 * @author Minh
 * Date: 9/26/2024
 * Time: 4:05 PM
 */


@Entity
@Builder
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @Column(unique = true)
    String refreshToken;

    String token;

    Date tokenExpiryTime;

    Date refreshTokenExpiryTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
