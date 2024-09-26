package org.demo.huyminh.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Date;

/**
 * @author Minh
 * Date: 9/26/2024
 * Time: 4:05 PM
 */


@Entity
@Builder
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String refreshToken;

    String token;

    Date tokenExpiryTime;

    Date refreshTokenExpiryTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;
}
