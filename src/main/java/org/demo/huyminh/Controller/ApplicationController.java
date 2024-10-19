package org.demo.huyminh.Controller;

import org.demo.huyminh.DTO.Request.ApplicationRequest;
import org.demo.huyminh.DTO.Request.ApplicationUpdateRequest;
import org.demo.huyminh.Entity.Application;
import org.demo.huyminh.Repository.PetRepository;
import org.demo.huyminh.Service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;
    private PetRepository petRepository;

    //Create Application
    @PostMapping
    public ResponseEntity<Application> submitApplication(@RequestBody ApplicationRequest request){
        Application application = applicationService.submitApplication(request.getId(),
                request.getPetId(),request.getFullName(),request.getYob(),
                request.getGender(),request.getAddress(),request.getCity(),
                request.getJob(),request.getPhone(),request.getLiveIn(),request.getLiveWith(),
                request.getFirstPerson(),request.getFirstPhone(),request.getSecondPerson(),
                request.getSecondPhone());
        return new ResponseEntity<>(application, HttpStatus.CREATED);
    }
    //Update Application Status
    @PutMapping("status/{applicationId}")
    Application updateApplicationStatus(@PathVariable("applicationId") String applicationId, @RequestBody ApplicationUpdateRequest request) {
        return applicationService.updateAppilicationStatus(applicationId, request);
    }
    //Get List Application
    @GetMapping
    List<Application> getApplications(){
        return applicationService.getApplications();
    }
    //Accept Applicaiton
    @GetMapping("status/1")
    List<Application> getApplicationsWithStatus1(){
        return applicationService.getApplicationsWithStatus1();
    }

    //Refuse Application
    @GetMapping("status/2")
    List<Application> getApplicationsWithStatus2(){
        return applicationService.getApplicationsWithStatus2();
    }

    //Accept Adoption
    @GetMapping("status/3")
    List<Application> getApplicationsWithStatus3(){
        return applicationService.getApplicationsWithStatus3();
    }

    //Denied Adoption
    @GetMapping("status/4")
    List<Application> getApplicationsWithStatus4(){
        return applicationService.getApplicationsWithStatus4();
    }

    //Get Application By Id
    @GetMapping("/{applicationId}")
    Optional<Application> getApplication(@PathVariable("applicationId") String applicationId){
           return applicationService.getApplicaiton(applicationId);
    }

    //Update Application
    @PutMapping("/{applicationId}")
    Application updateApplication(@PathVariable("applicationId") String applicationId, @RequestBody ApplicationUpdateRequest request){
             return applicationService.updateApplication(applicationId,request);
    }

    //Delete Application
    @DeleteMapping("/{applicationId}")
    String deleteApplication(@PathVariable("applicationId") String applicationId){
        applicationService.deleteApplication(applicationId);
        return "Application has been deleted";
    }
}


//    Application createApplication(@RequestBody ApplicationCreationRequest request){
//     return applicationService.createApplication(request);
//    }
//    public ResponseEntity<Application> createApplication(@RequestBody Application application) {
//        Application savedApplication = applicationService.saveApplication(application);
//        return ResponseEntity.status(201).body(savedApplication);
//    }
