package org.demo.huyminh.Controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.DTO.Request.ApplicationRequest;
import org.demo.huyminh.DTO.Request.ApplicationUpdateRequest;
import org.demo.huyminh.Entity.Application;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Service.ApplicationService;
import org.demo.huyminh.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/applications")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ApplicationController {
    ApplicationService applicationService;
    UserService userService;

    //Create Application
    @PostMapping
    public ResponseEntity<Application> submitApplication(@RequestBody ApplicationRequest request){
        Application application = applicationService.submitApplication(request.getId(),
                request.getPetId(),request.getFullName(),request.getYob(),
                request.getGender(),request.getAddress(),request.getCity(),
                request.getJob(),request.getPhone(),request.getLiveIn(),request.getLiveWith(),
                request.getFirstPerson(),request.getFirstPhone(),request.getSecondPerson(),
                request.getSecondPhone(),request.getDateIn(), request.getTimeIn(),request.getTimeOut());

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
    List<Application> getApplications() {
        return applicationService.getApplications();
    }

    @GetMapping("status/all")
    List<Application> getAllApplication() {
        return applicationService.getAllApplications();
    }

    //Accept Applicaiton
    @GetMapping("status/1")
    List<Application> getApplicationsWithStatus1() {
        return applicationService.getApplicationsWithStatus1();
    }

    //Refuse Application
    @GetMapping("status/2")
    List<Application> getApplicationsWithStatus2() {
        return applicationService.getApplicationsWithStatus2();
    }

    //Accept Adoption
    @GetMapping("status/3")
    List<Application> getApplicationsWithStatus3() {
        return applicationService.getApplicationsWithStatus3();
    }

    //Denied Adoption
    @GetMapping("status/4")
    List<Application> getApplicationsWithStatus4() {
        return applicationService.getApplicationsWithStatus4();
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

    @GetMapping("/status/{status}/sorted")
    public List<Application> getSortedApplications(@PathVariable("status") int status) {
        return applicationService.getApplicationsSortedByUpdateAt(status);
    }

    //Get Applications sorted by User ID
    @GetMapping("/sorted-by-user/{userId}")
    public List<Application> getApplicationsSortedByUserId(@PathVariable("userId") String userId) {
        // Lấy danh sách application của user có userId, và sắp xếp theo applicationId
        return applicationService.getApplicationsByUserId(userId)
                .stream()
                .sorted(Comparator.comparing(Application::getApplicationId)) // Sắp xếp theo applicationId
                .collect(Collectors.toList());
    }

    //Delete Application
    @DeleteMapping("/{applicationId}")
    String deleteApplication(@PathVariable("applicationId") String applicationId) {
        applicationService.deleteApplication(applicationId);
        return "Application has been deleted";
    }
}