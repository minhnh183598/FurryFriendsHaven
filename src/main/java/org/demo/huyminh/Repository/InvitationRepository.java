package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author Minh
 * Date: 10/22/2024
 * Time: 8:43 AM
 */

public interface InvitationRepository extends JpaRepository<Invitation, Integer> {

    @Query("SELECT i FROM Invitation i WHERE i.taskId = ?1 AND i.userId = ?2")
    Invitation findByTaskIdAndUserId(int taskId, String id);
}
