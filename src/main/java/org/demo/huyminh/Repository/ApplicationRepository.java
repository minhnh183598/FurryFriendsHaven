package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, String> {

    Optional<Application> findById(String applicationId);
}