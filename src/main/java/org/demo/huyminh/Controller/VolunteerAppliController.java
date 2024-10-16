package org.demo.huyminh.Controller;

import jakarta.validation.Valid;
import org.demo.huyminh.DTO.Request.ApplicationUpdateRequest;
import org.demo.huyminh.DTO.Request.VolunteerAppliCreationRequest;
import org.demo.huyminh.DTO.Request.VolunteerAppliUpdateRequest;
import org.demo.huyminh.Entity.Application;
import org.demo.huyminh.Entity.VolunteerApplication;
import org.demo.huyminh.Service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/volunteer/application")
@CrossOrigin("http://localhost:3000")
public class VolunteerAppliController {

    @Autowired
    private VolunteerService volunteerService;

    //Creare Application
    @PostMapping
    public ResponseEntity<VolunteerApplication> createVolunterrAppli(@RequestBody @Valid VolunteerAppliCreationRequest request){
        VolunteerApplication application = volunteerService.createVolunteerAppli(request.getId(),
                request.getFullName(), request.getYob(), request.getGender(),
                request.getAddress(), request.getPhone(), request.getAdoptionExp(),
                request.getDaysOfWeek(), request.getMorning(), request.getAfternoon(),
                request.getReason());
        return new ResponseEntity<>(application, HttpStatus.CREATED);
    }

    //GetAllApplication
    @GetMapping
    List<VolunteerApplication> getApplications(){
        return volunteerService.getVolunteerApplications();
    }

    //GetApplicaitonById
    @GetMapping("/{volunteerAppliId}")
    public VolunteerApplication getApplication(@PathVariable("volunteerAppliId") String volunteerAppliId){
        return volunteerService.getVolunteerApplication(volunteerAppliId);
    }

    //GetApplicationStatus = 1
    @GetMapping("status/1")
    List<VolunteerApplication> getApplicationsWithStatus1(){
        return volunteerService.getVolunteerApplicationsWithStatus1();
    }
    //GetApplicationStatus = 2
    @GetMapping("status/2")
    List<VolunteerApplication> getApplicationsWithStatus2(){
        return volunteerService.getVolunteerApplicationsWithStatus2();
    }
    //Update Application Status
    @PutMapping("status/{volunteerAppliId}")
    VolunteerApplication updateApplicationStatus(@PathVariable("volunteerAppliId") String volunteerAppliId, @RequestBody VolunteerAppliUpdateRequest  request) {
        return volunteerService.updateAppilicationStatus(volunteerAppliId, request);
    }



    //UpdateApplicaitonById
    @PutMapping("/{volunteerAppliId}")
    VolunteerApplication updateApplication(@PathVariable("volunteerAppliId") String volunteerAppliId,@RequestBody VolunteerAppliUpdateRequest request){
        return volunteerService.updateVolunApplication(volunteerAppliId,request);
    }
    //DeleteApplicaitonById
    @DeleteMapping("/{volunteerAppliId}")
    String deleteApplication(@PathVariable("volunteerAppliId") String volunteerAppliId){
        volunteerService.deleteVolunApplication(volunteerAppliId);
        return "Volunteer Application has been deleted";
    }

}
