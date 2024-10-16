package org.demo.huyminh.Service;

import jakarta.validation.constraints.Pattern;
import org.demo.huyminh.DTO.Request.ApplicationUpdateRequest;
import org.demo.huyminh.DTO.Request.VolunteerAppliCreationRequest;
import org.demo.huyminh.DTO.Request.VolunteerAppliUpdateRequest;
import org.demo.huyminh.Entity.Application;
import org.demo.huyminh.Entity.VolunteerApplication;
import org.demo.huyminh.Repository.ApplicationRepository;
import org.demo.huyminh.Repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolunteerService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    //CreateApplicaiton
    public VolunteerApplication createVolunteerAppli(String userId, String fullName
            , int yob, String gender, String address
            , @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})\\b") String phone
            ,String adoptionExp,String daysOfWeek,String morning,String afternoon,String reason){

        VolunteerApplication application = new VolunteerApplication();

        application.setId(userId);
        application.setFullName(fullName);
        application.setYob(yob);
        application.setGender(gender);
        application.setAddress(address);
        application.setPhone(phone);
        application.setAdoptionExp(adoptionExp);
        application.setDaysOfWeek(daysOfWeek);
        application.setMorning(morning);
        application.setAfternoon(afternoon);
        application.setReason(reason);

        return volunteerRepository.save(application);
    }

    //GetAllApplication
    public List<VolunteerApplication> getVolunteerApplications(){
        return volunteerRepository.findByStatusOrderByCreateAtAsc(0);
    }

    //GetApplicationStatus = 1
    public List<VolunteerApplication> getVolunteerApplicationsWithStatus1(){
        return volunteerRepository.findByStatusOrderByUpdateAtDesc(1);
    }

    //GetApplicationStatus = 2
    public List<VolunteerApplication> getVolunteerApplicationsWithStatus2(){
        return volunteerRepository.findByStatusOrderByUpdateAtDesc(2);
    }

    //GetApplicaitonById
    public VolunteerApplication getVolunteerApplication(String volunteerAppliId){
        return volunteerRepository.findById(volunteerAppliId)
                .orElseThrow(() -> new RuntimeException("Volunteer Application not founded"));
    }


    //UpdateApplicaitonById
    public VolunteerApplication updateVolunApplication(String volunteerAppliId, VolunteerAppliUpdateRequest request){
        VolunteerApplication application = new VolunteerApplication();

        application.setFullName(request.getFullName());
        application.setYob(request.getYob());
        application.setGender(request.getGender());
        application.setAddress(request.getAddress());
        application.setPhone(request.getPhone());
        application.setAdoptionExp(request.getAdoptionExp());
        application.setDaysOfWeek(request.getDaysOfWeek());
        application.setMorning(request.getMorning());
        application.setAfternoon(request.getAfternoon());
        application.setReason(request.getReason());

        return volunteerRepository.save(application);
    }

    //Update Application Status
    public VolunteerApplication updateAppilicationStatus(String volunteerAppliId, VolunteerAppliUpdateRequest request){
        VolunteerApplication application = volunteerRepository.findById(volunteerAppliId)
                .orElseThrow(() -> new RuntimeException("Application Id Not Existed"));

        application.setStatus(request.getStatus());

        return volunteerRepository.save(application);

    }

    //DeleteApplicaitonById
    public void deleteVolunApplication(String volunteerAppliId){
        volunteerRepository.deleteById(volunteerAppliId);
    }

}
