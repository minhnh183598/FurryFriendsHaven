package org.demo.huyminh.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Pattern;
import org.demo.huyminh.DTO.Request.ApplicationUpdateRequest;
import org.demo.huyminh.Entity.*;
import org.demo.huyminh.Enums.Status;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ChecklistTemplateRepository checklistTemplateRepository;
    @Autowired
    private ChecklistRepository checklistRepository;

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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public Application updateApplicationStatus(String applicationId, ApplicationUpdateRequest request, User user) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application Id Not Existed"));

        application.setStatus(request.getStatus());

        Application savedApplication = applicationRepository.save(application);

        if (savedApplication.getStatus() == 1) {
            Task newTask = Task.builder()
                    .name("Visit the house of " + application.getFullName() + " with applicationId (" + applicationId + ")")
                    .description("Visit the house of " + application.getFullName() + " to check whether it is suitable for adoption")
                    .status(Status.NOT_STARTED)
                    .category("Adoption")
                    .dueDate(LocalDateTime.now().plusDays(7))
                    .owner(user)
                    .adopter(application.getUser())
                    .build();

            if (newTask.getTeam() == null) {
                newTask.setTeam(new ArrayList<>());
            }
            newTask.getTeam().add(user);
            Task savedTask = taskRepository.save(newTask);

            ChecklistTemplate template = checklistTemplateRepository.findByName("Adoption")
                    .orElseThrow(() -> new AppException(ErrorCode.CHECKLIST_TEMPLATE_NOT_EXISTS));
            Checklist checklist = Checklist.builder()
                    .task(savedTask)
                    .checklistItems(new ArrayList<>())
                    .build();

            Checklist savedChecklist = checklistRepository.save(checklist);

            List<ChecklistItem> checklistItems = new ArrayList<>();
            for (ChecklistItemTemplate itemTemplate : template.getItems()) {
                ChecklistItem item = ChecklistItem.builder()
                        .entry(itemTemplate.getEntry())
                        .completed(false)
                        .checklist(savedChecklist)
                        .build();
                checklistItems.add(item);
            }
            savedChecklist.setChecklistItems(checklistItems);
            checklistRepository.save(savedChecklist);

            savedTask.setChecklist(savedChecklist);
            savedTask = taskRepository.save(savedTask);

            user.getTasks().add(savedTask);
            userRepository.save(user);

            application.setTask(savedTask);
            applicationRepository.save(application);
        }

        return savedApplication;
    }
    //Get All Application
    public List<Application> getAllApplications(){
        return applicationRepository.findAllByOrderByCreateAtDesc();
    }

    //GET APPLICATION LIST
    public List<Application> getApplications(){
        return applicationRepository.findByStatusOrderByCreateAtAsc(0);
    }

    //Accept Applicaiton
    public List<Application> getApplicationsWithStatus1(){
        return applicationRepository.findByStatusOrderByUpdateAtDesc(1);
    }

    //Refuse Application
    public List<Application> getApplicationsWithStatus2(){
        return applicationRepository.findByStatusOrderByUpdateAtDesc(2);
    }

    //Accept Adoption
    public List<Application> getApplicationsWithStatus3(){
        return applicationRepository.findByStatusOrderByUpdateAtDesc(3);
    }

    //Denied Adoption
    public List<Application> getApplicationsWithStatus4(){
        return applicationRepository.findByStatusOrderByUpdateAtDesc(4);
    }

    //GET APPLICATION BY ID
    public Optional<Application> getApplication(String applicationId){
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

    // Phương thức để lấy danh sách đơn ứng dụng đã sắp xếp theo updateAt
    public List<Application> getApplicationsSortedByUpdateAt(int status) {
        List<Application> applications = applicationRepository.findAll();
        // Lọc ra các đơn ứng dụng với status = 1 và sắp xếp
        return applications.stream()
                .filter(application -> application.getStatus() == status)
                .sorted(Comparator.comparing(Application::getUpdateAt).reversed())
                .collect(Collectors.toList());
    }


    // Lấy danh sách application của user dựa vào userId
    public List<Application> getApplicationsByUserId(String userId) {
        return applicationRepository.findAll()
                .stream()
                .filter(application -> application.getUser().getId().equals(userId)) // Lọc theo userId
                .collect(Collectors.toList());
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