package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, String> {
}
