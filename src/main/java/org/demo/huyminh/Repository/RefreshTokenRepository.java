package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.RefreshToken;
import org.demo.huyminh.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.Optional;

/**
 * @author Minh
 * Date: 9/26/2024
 * Time: 4:16 PM
 */

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    @Query("SELECT t.token FROM RefreshToken t WHERE t.refreshToken = ?1")
    String getToken(String refreshToken);

    @Query("SELECT t.tokenExpiryTime FROM RefreshToken t WHERE t.refreshToken = ?1")
    Date getTokenExpiryTime(String refreshToken);

    boolean existsByRefreshToken(String refreshTokenId);

    Optional<RefreshToken> findByUserId(String id);
}
