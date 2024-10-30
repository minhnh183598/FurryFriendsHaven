package org.demo.huyminh.Service;

import org.demo.huyminh.DTO.Request.ApplicationUpdateRequest;
import org.demo.huyminh.Entity.Application;
import org.demo.huyminh.Entity.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Minh
 * Date: 10/26/2024
 * Time: 9:22 PM
 */

public interface ApplicationService {

    Application submitApplication(
            String userId, String petId, String fullName, int yob, String gender, String address,
            String city, String job, String phone, String liveIn, String liveWith, String firstPerson,
            String firstPhone, String secondPerson, String secondPhone, Date dateIn, String timeIn, String timeOut
    );

    Application updateApplicationStatus(String applicationId, ApplicationUpdateRequest request, User user);

    List<Application> getAllApplications();

    List<Application> getApplications();

    List<Application> getApplicationsWithStatus1();

    List<Application> getApplicationsWithStatus2();

    List<Application> getApplicationsWithStatus3();

    List<Application> getApplicationsWithStatus4();

    Optional<Application> getApplication(String applicationId);

    Application updateApplication(String applicationId, ApplicationUpdateRequest request);

    List<Application> getApplicationsSortedByUpdateAt(int status);

    List<Application> getApplicationsByUserId(String userId);

    void deleteApplication(String applicationId);
}
