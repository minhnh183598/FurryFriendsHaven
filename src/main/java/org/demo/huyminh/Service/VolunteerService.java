package org.demo.huyminh.Service;

import jakarta.validation.constraints.Pattern;
import org.demo.huyminh.DTO.Request.VolunteerAppliUpdateRequest;
import org.demo.huyminh.Entity.VolunteerApplication;

import java.util.List;

/**
 * @author Minh
 * Date: 10/26/2024
 * Time: 10:05 PM
 */

public interface VolunteerService {
    VolunteerApplication createVolunteerAppli(String userId, String fullName
            , int yob, String gender, String address
            , @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})\\b") String phone
            , String adoptionExp, String daysOfWeek, String morning, String afternoon, String reason);

    List<VolunteerApplication> getVolunteerApplications();

    List<VolunteerApplication> getVolunteerApplicationsWithStatus1();

    List<VolunteerApplication> getVolunteerApplicationsWithStatus2();

    VolunteerApplication getVolunteerApplication(String volunteerAppliId);

    VolunteerApplication updateVolunApplication(String volunteerAppliId, VolunteerAppliUpdateRequest request);

    VolunteerApplication updateAppilicationStatus(String volunteerAppliId, VolunteerAppliUpdateRequest request);

    void deleteVolunApplication(String volunteerAppliId);
}
