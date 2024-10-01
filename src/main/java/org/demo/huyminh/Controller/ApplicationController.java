package org.demo.huyminh.Controller;

import org.demo.huyminh.DTO.Reponse.ApplicationResponse;
import org.demo.huyminh.DTO.Request.ApplicationCreationRequest;
import org.demo.huyminh.DTO.Request.ApplicationUpdateRequest;
import org.demo.huyminh.Entity.Application;
import org.demo.huyminh.Service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;
    //Create Application
    @PostMapping
    Application createApplication(@RequestBody ApplicationCreationRequest request){
     return applicationService.createApplication(request);
    }
    //Get List Application
    @GetMapping
    List<Application> getApplications(){
        return applicationService.getApplications();
    }
    //Get Application By Id
    @GetMapping("/{applicationId}")
    ApplicationResponse getApplication(@PathVariable("applicationId") String applicationId){
           return applicationService.getApplicaiton(applicationId);
    }
    //Update Application
    @PutMapping("/{applicationId}")
    ApplicationResponse updateApplication(@PathVariable("applicationId") String applicationId, @RequestBody ApplicationUpdateRequest request){
             return applicationService.updateApplication(applicationId,request);
    }
    //Delete Application
    @DeleteMapping("/{applicationId}")
    String deleteApplication(@PathVariable("applicationId") String applicationId){
        applicationService.deleteApplication(applicationId);
        return "Application has been deleted";
    }


}
