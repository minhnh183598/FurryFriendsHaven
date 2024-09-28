package org.demo.huyminh.Service;

import org.demo.huyminh.DTO.Request.ApplicationCreationRequest;
import org.demo.huyminh.DTO.Request.ApplicationUpdateRequest;
import org.demo.huyminh.Entity.Application;
import org.demo.huyminh.Mapper.ApplicationMapper;
import org.demo.huyminh.Repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    //GET APPLICATION BY ID
    //UPDATE APPLICATION
    //DELETE APPLICATION

}
