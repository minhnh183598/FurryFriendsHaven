package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Application;
import org.demo.huyminh.Entity.VolunteerApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<VolunteerApplication, String> {

    Optional<VolunteerApplication> findById(String volunteerAppliId);

    List<VolunteerApplication> findByStatusOrderByCreateAtAsc(int status);

    List<VolunteerApplication> findByStatusOrderByUpdateAtDesc(int status);
}