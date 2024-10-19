package org.demo.huyminh.Controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Request.ApplicationRequest;
import org.demo.huyminh.DTO.Request.ApplicationUpdateRequest;
import org.demo.huyminh.Entity.Application;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Service.ApplicationService;
import org.demo.huyminh.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/applications")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ApplicationController {
    ApplicationService applicationService;
    UserService userService;

    //Create Application
    @PostMapping
    public ResponseEntity<Application> submitApplication(
            @RequestBody ApplicationRequest request
    ){
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
    Application updateApplicationStatus(
            @PathVariable("applicationId") String applicationId,
            @RequestBody ApplicationUpdateRequest request,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        User user = userService.findByToken(jwt);
        return applicationService.updateApplicationStatus(applicationId, request, user);
    }
    //Get List Application
    @GetMapping
    List<Application> getApplications(){
        return applicationService.getApplications();
    }


//    @GetMapping("/{applicationId}/task")
//    public ApiResponse<TaskResponse> getTaskByApplicationId(
//            @PathVariable String applicationId
//    ) {
//
//    }

    @GetMapping("status/1")
    List<Application> getApplicationsWithStatus1(){
        return applicationService.getApplicationsWithStatus1();
    }

    @GetMapping("status/2")
    List<Application> getApplicationsWithStatus2(){
        return applicationService.getApplicationsWithStatus2();
    }

    //Get Application By Id
    @GetMapping("/{applicationId}")
    Optional<Application> getApplication(@PathVariable("applicationId") String applicationId){
           return applicationService.getApplication(applicationId);
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