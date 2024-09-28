package org.demo.huyminh.Controller;

import org.demo.huyminh.DTO.Request.ApplicationCreationRequest;
import org.demo.huyminh.Entity.Application;
import org.demo.huyminh.Service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    Application createApplication(@RequestBody ApplicationCreationRequest request){
     return applicationService.createApplication(request);
    }


}
