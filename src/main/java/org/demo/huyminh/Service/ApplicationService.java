package org.demo.huyminh.Service;

import jakarta.validation.constraints.Pattern;
import org.demo.huyminh.DTO.Request.ApplicationUpdateRequest;
import org.demo.huyminh.Entity.Application;
import org.demo.huyminh.Entity.Pet;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Repository.ApplicationRepository;
import org.demo.huyminh.Repository.PetRepository;
import org.demo.huyminh.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private UserRepository userRepository;

    //CREATE APPLICATION

    public Application submitApplication(String userId ,String petId, String fullName, int yob, String gender, String address, String city, String job, @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})\\b") String phone, String liveIn, String liveWith, String firstPerson, @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})\\b") String firstPhone, String secondPerson, @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})\\b") String secondPhone) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not Found"));
        user.setApplicationQuantity(user.getApplicationQuantity() + 1);
        userRepository.save(user);

        Application application = new Application();
        application.setId(userId);
        application.setUser(user);
        application.setPetId(petId);
        application.setPet(pet);
        application.setFullName(fullName);
        application.setYob(yob);
        application.setGender(gender);
        application.setAddress(address);
        application.setCity(city);
        application.setJob(job);
        application.setPhone(phone);
        application.setLiveIn(liveIn);
        application.setLiveWith(liveWith);
        application.setFirstPerson(firstPerson);
        application.setFirstPhone(firstPhone);
        application.setSecondPerson(secondPerson);
        application.setSecondPhone(secondPhone);

        return applicationRepository.save(application);
    }
    //Update Application Status
    public Application updateAppilicationStatus(String applicationId, ApplicationUpdateRequest request){
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application Id Not Existed"));

        application.setStatus(request.getStatus());


        return applicationRepository.save(application);

    }

    //GET APPLICATION LIST
    public List<Application> getApplications(){
        return applicationRepository.findByStatusOrderByCreateAtAsc(0);
    }

    public List<Application> getApplicationsWithStatus1(){
        return applicationRepository.findByStatusOrderByUpdateAtDesc(1);
    }

    public List<Application> getApplicationsWithStatus2(){
        return applicationRepository.findByStatusOrderByUpdateAtDesc(2);
    }

    //GET APPLICATION BY ID
    public Optional<Application> getApplicaiton(String applicationId){
        return Optional.ofNullable(applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application Id Not Existed")));
    }
    //UPDATE APPLICATION
    public Application updateApplication(String applicationId, ApplicationUpdateRequest request){
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application Id not Existed"));

        if(application.getStatus() == 0){
            application.setFullName(request.getFullName());
            application.setYob(request.getYob());
            application.setGender(request.getGender());
            application.setAddress(request.getAddress());
            application.setCity(request.getCity());
            application.setJob(request.getJob());
            application.setPhone(request.getPhone());
            application.setLiveIn(request.getLiveIn());
            application.setLiveWith(request.getLiveWith());
            application.setFirstPerson(request.getFirstPerson());
            application.setFirstPhone(request.getFirstPhone());
            application.setSecondPerson(request.getSecondPerson());
            application.setSecondPhone(request.getSecondPhone());
            return applicationRepository.save(application);
        }
        else{
            throw new RuntimeException("Can Not Update Application Form");
        }
    }
    //UPDATE APPLICATION STATUS BY ADMIN
    public Application updateApplicationStatus(String applicationId,ApplicationUpdateRequest request){
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application Id not Existed"));

        application.setStatus(request.getStatus());
        return applicationRepository.save(application);

    }

    //DELETE APPLICATION
    public void deleteApplication(String applicationId) {
        applicationRepository.deleteById(applicationId);
    }

}

//Mapper
//    //CREATE APPLICATION
////    public Application createApplication(ApplicationCreationRequest request){
////        Application application = applicationMapper.toApplication(request);
////        return applicationRepository.save(application);
////    }
//GET APPLICATION BY ID
//        return applicationMapper.toApplicationResponse(applicationRepository.findById(applicationId)
//                .orElseThrow(() -> new RuntimeException("Application not found")));
//UPDATE APPLICATION
//        Application application = applicationRepository.findById(applicationId)
//                .orElseThrow(() -> new RuntimeException("Application Id not existed"));
//                applicationMapper.updateApplication(application,request);
//                return applicationMapper.toApplicationResponse(applicationRepository.save(application));