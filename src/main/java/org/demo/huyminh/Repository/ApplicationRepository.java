package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, String> {

    List<Application> findByUserId(String userId);

    Optional<Application> findById(String applicationId);

    List<Application> findByStatusOrderByCreateAtAsc(int status);

    List<Application> findByStatusOrderByUpdateAtDesc(int status);

    Application getApplicationByTaskId(int taskId);
}
