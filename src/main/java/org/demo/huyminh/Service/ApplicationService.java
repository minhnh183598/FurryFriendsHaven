package org.demo.huyminh.Service;

import org.demo.huyminh.DTO.Reponse.ApplicationResponse;
import org.demo.huyminh.DTO.Request.ApplicationCreationRequest;
import org.demo.huyminh.DTO.Request.ApplicationUpdateRequest;
import org.demo.huyminh.Entity.Application;
import org.demo.huyminh.Mapper.ApplicationMapper;
import org.demo.huyminh.Repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ApplicationMapper applicationMapper;

    //CREATE APPLICATION
    public Application createApplication(ApplicationCreationRequest request){
        Application application = applicationMapper.toApplication(request);
        return applicationRepository.save(application);
    }
    //GET APPLICATION LIST
    public List<Application> getApplications(){
        return applicationRepository.findAll();
    }
    //GET APPLICATION BY ID
    public ApplicationResponse getApplicaiton(String applicationId){
        return applicationMapper.toApplicationResponse(applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found")));
    }
    //UPDATE APPLICATION
    public ApplicationResponse updateApplication(String applicationId,ApplicationUpdateRequest request){
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application Id not existed"));
                applicationMapper.updateApplication(application,request);
                return applicationMapper.toApplicationResponse(applicationRepository.save(application));
    }
    //DELETE APPLICATION
    public void deleteApplication(String applicationId) {
        applicationRepository.deleteById(applicationId);
    }


}
