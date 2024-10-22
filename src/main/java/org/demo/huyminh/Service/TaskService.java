package org.demo.huyminh.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.BriefIssueResponse;
import org.demo.huyminh.DTO.Reponse.TaskResponse;
import org.demo.huyminh.DTO.Request.FeedbackCreationRequest;
import org.demo.huyminh.DTO.Request.InvitationEventData;
import org.demo.huyminh.DTO.Request.TaskCreationRequest;
import org.demo.huyminh.DTO.Request.TaskUpdateRequest;
import org.demo.huyminh.Entity.*;
import org.demo.huyminh.Enums.InvitationStatus;
import org.demo.huyminh.Enums.Roles;
import org.demo.huyminh.Enums.Status;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Mapper.FeedbackMapper;
import org.demo.huyminh.Mapper.TaskMapper;
import org.demo.huyminh.Mapper.UserMapper;
import org.demo.huyminh.Repository.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 12:37 PM
 */

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TaskService {

    TaskRepository taskRepository;
    ApplicationRepository applicationRepository;
    UserRepository userRepository;
    TaskMapper taskMapper;
    TagRepository tagRepository;
    UserMapper userMapper;
    FeedbackMapper feedbackMapper;
    FeedbackService feedbackService;
    FeedbackRepository feedbackRepository;
    InvitationRepository invitationRepository;
    RoleService roleService;


    @PreAuthorize("hasRole('ADMIN')")
    public TaskResponse createTask(TaskCreationRequest task, User user) {
        task.setOwner(user);
        Task newTask = taskMapper.toTask(task);
        if(newTask.getDueDate().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.INVALID_DUE_DATE);
        }

        newTask.getTeam().add(user);
        saveTaskWithTags(newTask);

        try {
            taskRepository.save(newTask);
        } catch (Exception e) {
            throw new AppException(ErrorCode.TASK_EXISTS);
        }

        TaskResponse temp = taskMapper.toTaskResponse(newTask);
        temp.setOwner(userMapper.toUserResponseForTask(user));
        temp.setTeam(newTask.getTeam().stream().map(userMapper::toUserResponseForTask).collect(Collectors.toList()));
        temp.setTags(taskMapper.mapTagsToString(newTask.getTags()));

        return temp;
    }

    public void saveTaskWithTags(Task task) {
        if (task.getTags() == null || task.getTags().isEmpty()) {
            throw new AppException(ErrorCode.TASK_HAS_NO_TAGS);
        }

        Set<Tag> existingTags = new HashSet<>();

        for (Tag tag : task.getTags()) {
            Optional<Tag> existingTag = tagRepository.findById(tag.getName());

            if(!existingTag.isEmpty() && existingTag.get().getType().equals(Tag.TagType.TASK_LABEL)) {
                existingTags.add(existingTag.get());
            }
        }

        if(existingTags.isEmpty()) {
            throw new AppException(ErrorCode.TASK_HAS_NO_TAGS);
        }

        task.setTags(existingTags);
    }

    public List<TaskResponse> getTaskByTeam(User user) {

        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTS);
        }

        List<Task> tasks = taskRepository.findByTeamContainingOrOwner(user, user);
        log.info(tasks.toString());

        List<TaskResponse> taskResponses = tasks.stream().map(taskMapper::toTaskResponse).collect(Collectors.toList());

        for (int i = 0; i < taskResponses.size(); i++) {
            Task currentTask = tasks.get(i);
            TaskResponse taskResponse = taskResponses.get(i);

            taskResponse.setOwner(userMapper.toUserResponseForTask(currentTask.getOwner()));
            taskResponse.setTeam(currentTask.getTeam().stream().map(userMapper::toUserResponseForTask).collect(Collectors.toList()));
            taskResponse.setTags(currentTask.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
            taskResponse.setIssues(currentTask.getIssues().stream().map(issue -> BriefIssueResponse.builder()
                    .title(issue.getTitle())
                    .description(issue.getDescription())
                    .status(issue.getStatus().toString())
                    .dueDate(issue.getDueDate()).build())
                    .collect(Collectors.toList()));
            taskResponse.setFeedbacks(currentTask.getFeedbacks().stream().map(feedbackMapper::toFeedbackResponse).collect(Collectors.toList()));

            if (currentTask.getCategory().equalsIgnoreCase("Adoption") && currentTask.getAdopter() != null) {
                taskResponse.setAdopter(userMapper.toUserResponseForTask(currentTask.getAdopter()));
            }
        }

        log.info(taskResponses.toString());

        if (taskResponses.isEmpty()) {
            throw new AppException(ErrorCode.LIST_TAG_IS_EMPTY);
        }

        return taskResponses;
    }

    public TaskResponse getTaskByApplicationId(String applicationId, User user) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICATION_NOT_EXISTS));

        Task task = application.getTask();
        if(task == null) {
            throw new AppException(ErrorCode.TASK_NOT_EXISTS);
        }

        if (user.getRoles() == null || user.getRoles().isEmpty() ||
                !(user.getRoles().contains(Roles.ADMIN) || user.getRoles().contains(Roles.VOLUNTEER))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        TaskResponse taskResponse = taskMapper.toTaskResponse(task);
        taskResponse.setOwner(userMapper.toUserResponseForTask(task.getOwner()));
        taskResponse.setTeam(task.getTeam().stream().map(userMapper::toUserResponseForTask).collect(Collectors.toList()));
        taskResponse.setTags(task.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
        taskResponse.setIssues(task.getIssues().stream().map(issue -> BriefIssueResponse.builder()
                        .title(issue.getTitle())
                        .description(issue.getDescription())
                        .status(issue.getStatus().toString())
                        .dueDate(issue.getDueDate()).build())
                .collect(Collectors.toList()));
        taskResponse.setFeedbacks(task.getFeedbacks().stream().map(feedbackMapper::toFeedbackResponse).toList());
        return taskResponse;
    }

    public TaskResponse getTaskById(int taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if(optionalTask.isEmpty()) {
            throw new AppException(ErrorCode.TASK_NOT_EXISTS);
        }

        TaskResponse taskResponse = taskMapper.toTaskResponse(optionalTask.get());
        taskResponse.setOwner(userMapper.toUserResponseForTask(optionalTask.get().getOwner()));
        taskResponse.setIssues(optionalTask.get().getIssues().stream().map(issue -> BriefIssueResponse.builder()
                .title(issue.getTitle())
                .description(issue.getDescription())
                .status(issue.getStatus().toString())
                .dueDate(issue.getDueDate())
                .build()).collect(Collectors.toList()));
        taskResponse.setTeam(optionalTask.get().getTeam().stream().map(userMapper::toUserResponseForTask).collect(Collectors.toList()));
        taskResponse.setTags(optionalTask.get().getTags().stream().map(Tag::getName).collect(Collectors.toList()));
        taskResponse.setAdopter(optionalTask.get().getAdopter() != null ? userMapper.toUserResponseForTask(optionalTask.get().getAdopter()) : null);

        return taskResponse;
    }

    @Transactional
    public void deleteTask(int taskId, User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        if(!task.getOwner().equals(existingUser)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        taskRepository.deleteById(task.getId());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public TaskResponse updateTask(TaskUpdateRequest updatedTask, User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        Task task = taskRepository.findById(updatedTask.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        if(!(task.getOwner().equals(existingUser))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Task updateTask = taskMapper.updateTask(updatedTask);
        if(updateTask.getDueDate().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.INVALID_DUE_DATE);
        }
        log.info("Update Task: " + updateTask);
        updateTask.setTeam(task.getTeam());
        updateTask.setIssues(task.getIssues());
        updateTask.setOwner(task.getOwner());

        if (updatedTask.getTags() != null && !updatedTask.getTags().isEmpty()) {
            Set<Tag> existingTags = new HashSet<>(task.getTags());

            for (Tag updatedTag : updatedTask.getTags()) {
                Optional<Tag> existingTag = tagRepository.findById(updatedTag.getName());

                if(!existingTag.isEmpty() && existingTag.get().getType().equals(Tag.TagType.TASK_LABEL)) {
                    existingTags.add(existingTag.get());
                }
            }

            updateTask.setTags(existingTags);
        }
        Task savedTask = taskRepository.save(updateTask);

        TaskResponse taskResponse = taskMapper.toTaskResponse(savedTask);
        taskResponse.setOwner(userMapper.toUserResponseForTask(savedTask.getOwner()));
        taskResponse.setTeam(savedTask.getTeam().stream().map(userMapper::toUserResponseForTask).collect(Collectors.toList()));
        taskResponse.setTags(taskMapper.mapTagsToString(savedTask.getTags()));

        return taskResponse;
    }

    @Transactional
    public void changeStatus(int taskId, String status, FeedbackCreationRequest feedback, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        if (Status.valueOf(status.toUpperCase()).equals(task.getStatus())) {
            throw new AppException(ErrorCode.CANNOT_CHANGE_STATUS_TO_SAME_STATUS);
        }

        if (!task.getTeam().contains(user)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_CHANGE_TASK_STATUS);
        }

        Status newStatus = Status.valueOf(status.toUpperCase());
        if (Status.valueOf(status.toUpperCase()).equals(Status.DONE)) {
            if (feedback == null || feedback.getContent() == null || feedback.getContent().isEmpty()) {
                throw new AppException(ErrorCode.NO_FEEDBACK);
            }

            if (feedbackRepository.findByUser(user) != null) {
                throw new AppException(ErrorCode.EACH_USER_CAN_POST_ONE_FEEDBACK);
            }

            log.info("Create feedback: " + feedback);
            Feedback newFeedback = Feedback.builder()
                    .content(feedback.getContent())
                    .task(task)
                    .reporter(user)
                    .build();

            Rating rating = feedback.getRating();
            if (rating != null) {
                rating.setFeedback(newFeedback);
                if (task.getCategory().equals("Adoption")) {
                    rating.setApplication(applicationRepository.getApplicationByTaskId(task.getId()));
                }
                newFeedback.setRating(rating);
            }

            List<Image> images = feedback.getImages().stream()
                    .map(imageUrl -> Image.builder()
                            .feedback(newFeedback)
                            .imageUrl(imageUrl)
                            .build())
                    .toList();

            newFeedback.setImages(images);
            task.getFeedbacks().add(newFeedback);

            feedbackRepository.save(newFeedback);
        }

        log.info("Change status: " + newStatus);
        task.setStatus(newStatus);
        taskRepository.save(task);
    }

    public void addUserToTask(int taskId, String userId, User existingUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        if(!(task.getOwner().equals(existingUser) || task.getTeam().contains(existingUser))) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_ADD_USER_TO_TASK);
        }

        if(!task.getTeam().contains(user)) {
            task.getTeam().add(user);
            taskRepository.save(task);
        } else {
            throw new AppException(ErrorCode.USER_ALREADY_IN_TASK);
        }
    }

    public void removeUserFromTask(int taskId, String userId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        User userToRemove = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        if(!(task.getOwner().equals(user))) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_DELETE_USER_FROM_TASK);
        }

        if(!task.getTeam().contains(userToRemove)) {
            throw new AppException(ErrorCode.USER_NOT_IN_TEAM);
        }

        if(task.getOwner().equals(userToRemove)) {
            throw new AppException(ErrorCode.CANNOT_REMOVE_OWNER);
        }

        if(task.getTeam().size() == 1) {
            throw new AppException(ErrorCode.CANNOT_REMOVE_LAST_USER);
        }
        task.getTeam().remove(userToRemove);
        taskRepository.save(task);
    }

    public List<Task> searchTask(String keyword) {
        String partialName = "%" + keyword + "%";
        log.info("Task Service: search Task");
        log.info("PartialName: " + keyword);
        var result = taskRepository.findByPartialName(partialName);

        if(result.isEmpty()) {
            throw new AppException(ErrorCode.TASK_NOT_FOUND);
        }
        return result;
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('VOLUNTEER')")
    public List<TaskResponse> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();

        List<TaskResponse> taskResponses = tasks.stream().map(taskMapper::toTaskResponse).collect(Collectors.toList());

        for (int i = 0; i < taskResponses.size(); i++) {
            Task currentTask = tasks.get(i);
            TaskResponse taskResponse = taskResponses.get(i);

            // Gán thông tin cho taskResponse từ currentTask
            taskResponse.setOwner(userMapper.toUserResponseForTask(currentTask.getOwner()));
            taskResponse.setTeam(currentTask.getTeam().stream().map(userMapper::toUserResponseForTask).collect(Collectors.toList()));
            taskResponse.setTags(currentTask.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
            taskResponse.setIssues(currentTask.getIssues().stream().map(issue -> BriefIssueResponse.builder()
                            .title(issue.getTitle())
                            .description(issue.getDescription())
                            .status(issue.getStatus().toString())
                            .dueDate(issue.getDueDate()).build())
                    .collect(Collectors.toList()));
            taskResponse.setFeedbacks(currentTask.getFeedbacks().stream().map(feedbackMapper::toFeedbackResponse).collect(Collectors.toList()));

            if ("Adoption".equalsIgnoreCase(currentTask.getCategory())) {
                taskResponse.setAdopter(userMapper.toUserResponseForTask(currentTask.getAdopter()));
            }
        }

        return taskResponses;
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('VOLUNTEER')")
    public InvitationEventData inviteUserToTask(int taskId, String username, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        if(!(task.getOwner().equals(user)) && !task.getTeam().contains(user)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_INVITE_USER_TO_TASK);
        }

        User invitedUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        if(task.getTeam().contains(invitedUser)) {
            throw new AppException(ErrorCode.USER_ALREADY_IN_TASK);
        }

        if(user.equals(invitedUser)) {
            throw new AppException(ErrorCode.CANNOT_INVITE_YOURSELF);
        }

        if (!roleService.hasRole(invitedUser, "VOLUNTEER") && !roleService.hasRole(invitedUser, "ADMIN")) {
            throw new AppException(ErrorCode.USER_NOT_HAVE_PROPER_ROLE);
        }

        InvitationEventData eventData = InvitationEventData.builder()
                                            .taskId(String.valueOf(taskId))
                                            .user(invitedUser)
                                            .build();

        Invitation invitation = invitationRepository.findByTaskIdAndUserId(taskId, invitedUser.getId());
        if (invitation != null && (invitation.getStatus().equals(InvitationStatus.ACCEPTED)
                || invitation.getStatus().equals(InvitationStatus.PENDING))) {
            throw new AppException(ErrorCode.USER_ALREADY_INVITED);
        } else if (invitation != null && (invitation.getStatus().equals(InvitationStatus.REJECTED)
                || invitation.getStatus().equals(InvitationStatus.EXPIRED))) {
            invitation.setStatus(InvitationStatus.PENDING);
            invitation.setExpiredAt(LocalDateTime.now().plusDays(1));
            invitationRepository.save(invitation);

            return eventData;
        }

        invitationRepository.save(Invitation.builder()
                .userId(invitedUser.getId())
                .taskId(taskId)
                .expiredAt(LocalDateTime.now().plusDays(1))
                .build());

        return eventData;
    }

    public String acceptInvitation(int taskId, String username, User user, String choice) {
        User invitedUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        Invitation invitation = invitationRepository.findByTaskIdAndUserId(taskId, invitedUser.getId());

        if(invitation == null) {
            throw new AppException(ErrorCode.INVITATION_NOT_FOUND);
        }

        if(!invitation.getExpiredAt().isAfter(LocalDateTime.now())) {
            invitation.setStatus(InvitationStatus.EXPIRED);
            invitationRepository.save(invitation);
            throw new AppException(ErrorCode.INVITATION_EXPIRED);
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        if(choice.equalsIgnoreCase("Reject")) {
            invitation.setStatus(InvitationStatus.REJECTED);
            invitationRepository.save(invitation);
            return "User has declined the invitation";
        } else if(!choice.equalsIgnoreCase("Accept")) {
            throw new AppException(ErrorCode.INVALID_CHOICE);
        }

        if(task.getTeam().contains(invitedUser)) {
            throw new AppException(ErrorCode.USER_ALREADY_IN_TASK);
        }

        task.getTeam().add(invitedUser);
        taskRepository.save(task);
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);

        return "User has accepted the invitation";
    }
}
