package org.demo.huyminh.Service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.BriefIssueResponse;
import org.demo.huyminh.DTO.Reponse.BriefTaskResponse;
import org.demo.huyminh.DTO.Reponse.TaskResponse;
import org.demo.huyminh.DTO.Request.*;
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
import org.demo.huyminh.Service.RoleService;
import org.demo.huyminh.Service.TaskService;
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
@PreAuthorize("hasRole('ADMIN') || hasRole('VOLUNTEER')")
public class TaskServiceImpl implements TaskService {

    TaskRepository taskRepository;
    ApplicationRepository applicationRepository;
    UserRepository userRepository;
    TaskMapper taskMapper;
    TagRepository tagRepository;
    UserMapper userMapper;
    FeedbackMapper feedbackMapper;
    InvitationRepository invitationRepository;
    RoleService roleService;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public TaskResponse createTask(TaskCreationRequest task, User user) {
        task.setOwner(user);
        Task newTask = taskMapper.toTask(task);
        if (newTask.getDueDate().isBefore(LocalDateTime.now())) {
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

            if (existingTag.isPresent() && existingTag.get().getType().equals(Tag.TagType.TASK_LABEL)) {
                existingTags.add(existingTag.get());
            }
        }

        if (existingTags.isEmpty()) {
            throw new AppException(ErrorCode.TASK_HAS_NO_TAGS);
        }

        task.setTags(existingTags);
    }

    @Override
    public List<BriefTaskResponse> getTaskByTeam(User user) {

        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTS);
        }

        List<Task> tasks = taskRepository.findByTeamContainingOrOwner(user, user);
        log.info(tasks.toString());

        List<BriefTaskResponse> taskResponses = tasks.stream()
                .map(task -> BriefTaskResponse.builder()
                        .id(task.getId())
                        .dueDate(task.getDueDate())
                        .name(task.getName())
                        .status(task.getStatus().toString())
                        .build())
                .collect(Collectors.toList());

        log.info(taskResponses.toString());

        if (taskResponses.isEmpty()) {
            throw new AppException(ErrorCode.LIST_TASK_IS_EMPTY);
        }

        return taskResponses;
    }

    @Override
    public List<BriefTaskResponse> searchAndSortTasks(String category, Status status, LocalDateTime dueDate, String order, String keyword) {
        if (category == null && status == null && dueDate == null && order == null) {
            getAllTasks();
        }

        List<Task> tasks;
        if (order.equalsIgnoreCase("ASC")) {
            tasks = taskRepository.findByFiltersAscOrder(category, status, dueDate, keyword);
        } else if (order.equalsIgnoreCase("DESC")) {
            tasks = taskRepository.findByFiltersDescOrder(category, status, dueDate, keyword);
        } else {
            throw new AppException(ErrorCode.INVALID_ORDER);
        }

        List<BriefTaskResponse> taskResponses = tasks.stream()
                .map(task -> BriefTaskResponse.builder()
                        .id(task.getId())
                        .dueDate(task.getDueDate())
                        .name(task.getName())
                        .status(task.getStatus().toString())
                        .build())
                .collect(Collectors.toList());

        if (taskResponses.isEmpty()) {
            throw new AppException(ErrorCode.LIST_TASK_IS_EMPTY);
        }

        return taskResponses;
    }

    @Override
    public TaskResponse getTaskByApplicationId(String applicationId, User user) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.APPLICATION_NOT_EXISTS));

        Task task = application.getTask();
        if (task == null) {
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
        taskResponse.setChecklist(task.getChecklist());
        return taskResponse;
    }

    @Override
    public TaskResponse getTaskById(int taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
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
        taskResponse.setAdopter(optionalTask.get().getAdopter() != null ? userMapper.toAdopterResponseForTask(optionalTask.get().getAdopter()) : null);
        if(optionalTask.get().getAdopter() != null) {
            Application application = optionalTask.get().getAdopter().getApplications().getFirst();
            taskResponse.getAdopter().setAddress(application.getAddress() + ", " + application.getCity());
            taskResponse.getAdopter().setPhone(application.getPhone());
            taskResponse.getAdopter().setGender(application.getGender());
        }

        if (optionalTask.get().getCategory().equalsIgnoreCase("Adoption") && optionalTask.get().getAdopter() != null) {
            taskResponse.setPetName(optionalTask.get().getAdopter().getApplications().getFirst().getPet().getPetName());
        }

        taskResponse.setFeedbacks(optionalTask.get().getFeedbacks().stream().map(feedbackMapper::toFeedbackResponse).toList());
        taskResponse.setChecklist(optionalTask.get().getChecklist());

        return taskResponse;
    }

    @Transactional
    @Override
    public void deleteTask(int taskId, User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        if (!task.getOwner().equals(existingUser)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        taskRepository.deleteById(task.getId());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public TaskResponse updateTask(TaskUpdateRequest updatedTask, User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        Task task = taskRepository.findById(updatedTask.getId())
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        if (!(task.getOwner().equals(existingUser))) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_UPDATE_TASK);
        }

        Task updateTask = taskMapper.updateTask(updatedTask);
        if (updateTask.getDueDate().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.INVALID_DUE_DATE);
        }
        updateTask.setTeam(task.getTeam());
        updateTask.setIssues(task.getIssues());
        updateTask.setOwner(task.getOwner());

        if (updatedTask.getTags() != null && !updatedTask.getTags().isEmpty()) {
            Set<Tag> existingTags = new HashSet<>(task.getTags());

            for (Tag updatedTag : updatedTask.getTags()) {
                Optional<Tag> existingTag = tagRepository.findById(updatedTag.getName());

                if (!existingTag.isEmpty() && existingTag.get().getType().equals(Tag.TagType.TASK_LABEL)) {
                    existingTags.add(existingTag.get());
                }
            }

            updateTask.setTags(existingTags);
        }
        Task savedTask = taskRepository.save(updateTask);

        return toTaskResponse(savedTask);
    }

    @Transactional
    @Override
    public TaskResponse updateSimpleTask(BriefUpdateTaskRequest updateTask, int taskId, User user) {
        Task existingtask = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        if (!existingtask.getOwner().equals(user)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_UPDATE_TASK);
        }

        existingtask.setDescription(updateTask.getDescription());
        Task savedTask = taskRepository.save(existingtask);

        return toTaskResponse(savedTask);
    }

    private TaskResponse toTaskResponse(Task task) {
        TaskResponse taskResponse = taskMapper.toTaskResponse(task);
        taskResponse.setOwner(userMapper.toUserResponseForTask(task.getOwner()));
        taskResponse.setTeam(task.getTeam().stream().map(userMapper::toUserResponseForTask).toList());
        taskResponse.setTags(taskMapper.mapTagsToString(task.getTags()));
        return taskResponse;
    }

    @Transactional
    @Override
    public void changeStatus(int taskId, String status, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        if (Status.valueOf(status.toUpperCase()).equals(task.getStatus())) {
            throw new AppException(ErrorCode.CANNOT_CHANGE_STATUS_TO_SAME_STATUS);
        }

        if (!task.getTeam().contains(user)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_CHANGE_TASK_STATUS);
        }

        Status newStatus = Status.valueOf(status.toUpperCase());

        task.setStatus(newStatus);
        taskRepository.save(task);
    }

    @Transactional
    @Override
    public void attendToTask(int taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        if (task.getTeam().contains(user)) {
            throw new AppException(ErrorCode.USER_ALREADY_IN_TASK);
        }

        if (!task.getStatus().equals(Status.NOT_STARTED)) {
            throw new AppException(ErrorCode.CANNOT_ATTEND_TO_TASK);
        }

        if (!roleService.hasRole(user, "VOLUNTEER") && !roleService.hasRole(user, "ADMIN")) {
            throw new AppException(ErrorCode.USER_NOT_HAVE_PROPER_ROLE);
        }

        task.getTeam().add(user);
        task.getChecklist().setAssignee(user);
        task.setStatus(Status.IN_PROGRESS);
        taskRepository.save(task);
    }

    @Transactional
    @Override
    public void addUserToTask(int taskId, String userId, User existingUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        if (!(task.getOwner().equals(existingUser) || task.getTeam().contains(existingUser))) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_ADD_USER_TO_TASK);
        }

        if (!task.getTeam().contains(user)) {
            task.getTeam().add(user);
            taskRepository.save(task);
        } else {
            throw new AppException(ErrorCode.USER_ALREADY_IN_TASK);
        }
    }

    @Override
    public void removeUserFromTask(int taskId, String userId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        User userToRemove = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        if (!(task.getOwner().equals(user))) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_DELETE_USER_FROM_TASK);
        }

        if (!task.getTeam().contains(userToRemove)) {
            throw new AppException(ErrorCode.USER_NOT_IN_TEAM);
        }

        if (task.getOwner().equals(userToRemove)) {
            throw new AppException(ErrorCode.CANNOT_REMOVE_OWNER);
        }

        if (task.getTeam().size() == 1) {
            throw new AppException(ErrorCode.CANNOT_REMOVE_LAST_USER);
        }
        task.getTeam().remove(userToRemove);
        taskRepository.save(task);
    }

    @Override
    public List<BriefTaskResponse> searchTask(String keyword) {
        String partialName = "%" + keyword + "%";
        var tasks = taskRepository.findByPartialName(partialName);

        List<BriefTaskResponse> taskResponses = tasks.stream()
                .map(task -> BriefTaskResponse.builder()
                        .id(task.getId())
                        .dueDate(task.getDueDate())
                        .name(task.getName())
                        .status(task.getStatus().toString())
                        .build())
                .collect(Collectors.toList());

        if (taskResponses.isEmpty()) {
            throw new AppException(ErrorCode.LIST_TASK_IS_EMPTY);
        }

        return taskResponses;
    }

    @Override
    public List<BriefTaskResponse> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();

        List<BriefTaskResponse> taskResponses = tasks.stream()
                .map(task -> BriefTaskResponse.builder()
                        .id(task.getId())
                        .dueDate(task.getDueDate())
                        .name(task.getName())
                        .status(task.getStatus().toString())
                        .build())
                .collect(Collectors.toList());

        if (taskResponses.isEmpty()) {
            throw new AppException(ErrorCode.LIST_TASK_IS_EMPTY);
        }

        return taskResponses;
    }

    @Override
    public InvitationEventData inviteUserToTask(int taskId, String username, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        if (!(task.getOwner().equals(user)) && !task.getTeam().contains(user)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_INVITE_USER_TO_TASK);
        }

        User invitedUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        if (task.getTeam().contains(invitedUser)) {
            throw new AppException(ErrorCode.USER_ALREADY_IN_TASK);
        }

        if (user.equals(invitedUser)) {
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

    @Transactional
    @Override
    public String acceptInvitation(int taskId, String username, User user, String choice) {
        User invitedUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        Invitation invitation = invitationRepository.findByTaskIdAndUserId(taskId, invitedUser.getId());

        if (invitation == null) {
            throw new AppException(ErrorCode.INVITATION_NOT_FOUND);
        }

        if (!invitation.getExpiredAt().isAfter(LocalDateTime.now())) {
            invitation.setStatus(InvitationStatus.EXPIRED);
            invitationRepository.save(invitation);
            throw new AppException(ErrorCode.INVITATION_EXPIRED);
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        if (choice.equalsIgnoreCase("Reject")) {
            invitation.setStatus(InvitationStatus.REJECTED);
            invitationRepository.save(invitation);
            return "User has declined the invitation";
        } else if (!choice.equalsIgnoreCase("Accept")) {
            throw new AppException(ErrorCode.INVALID_CHOICE);
        }

        if (task.getTeam().contains(invitedUser)) {
            throw new AppException(ErrorCode.USER_ALREADY_IN_TASK);
        }

        task.getTeam().add(invitedUser);
        taskRepository.save(task);
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);

        return "User has accepted the invitation";
    }
}