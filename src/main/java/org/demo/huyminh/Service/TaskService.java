package org.demo.huyminh.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.TaskResponse;
import org.demo.huyminh.DTO.Request.TaskCreationRequest;
import org.demo.huyminh.DTO.Request.TaskUpdateRequest;
import org.demo.huyminh.Entity.Issue;
import org.demo.huyminh.Entity.Tag;
import org.demo.huyminh.Entity.Task;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Mapper.TaskMapper;
import org.demo.huyminh.Mapper.UserMapper;
import org.demo.huyminh.Repository.TagRepository;
import org.demo.huyminh.Repository.TaskRepository;
import org.demo.huyminh.Repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 12:37 PM
 */

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TaskService {

    final TaskRepository taskRepository;
    final UserRepository userRepository;
    final TaskMapper taskMapper;
    final TagRepository tagRepository;
    private final UserMapper userMapper;

    public TaskResponse createTask(TaskCreationRequest task, User user) {
        task.setOwner(user);
        Task newTask = taskMapper.toTask(task);

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
        temp.setTags(newTask.getTags().stream().map(Tag::getName).collect(Collectors.toList()));

        return temp;
    }

    public void saveTaskWithTags(Task task) {
        if (task.getTags() == null || task.getTags().isEmpty()) {
            throw new AppException(ErrorCode.TASK_HAS_NO_TAGS);
        }

        Set<Tag> existingTags = new HashSet<>();

        for (Tag tag : task.getTags()) {
            Optional<Tag> existingTag = tagRepository.findById(tag.getName());

            existingTag.ifPresent(existingTags::add);
        }

        task.setTags(existingTags);
    }

    public List<TaskResponse> getTaskByTeam(User user, String category, String tag) {
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTS);
        }

        List<Task> tasks = taskRepository.findByTeamContainingOrOwner(user, user);

        if (category != null && !category.isEmpty()) {
            tasks = tasks.stream()
                    .filter(task -> category.equals(task.getCategory()))
                    .collect(Collectors.toList());
        }

        if (tag != null && !tag.isEmpty()) {
            tasks = tasks.stream()
                    .filter(task -> task.getTags().contains(tag))
                    .collect(Collectors.toList());
        }

        List<TaskResponse> taskResponses = tasks.stream().map(taskMapper::toTaskResponse).collect(Collectors.toList());

        for (TaskResponse taskResponse : taskResponses) {
            taskResponse.setOwner(userMapper.toUserResponseForTask(tasks.getFirst().getOwner()));
            taskResponse.setTeam(tasks.getFirst().getTeam().stream().map(userMapper::toUserResponseForTask).collect(Collectors.toList()));
            taskResponse.setTags(tasks.getFirst().getTags().stream().map(Tag::getName).collect(Collectors.toList()));
            taskResponse.setIssues(tasks.getFirst().getIssues().stream().map(Issue::getTitle).collect(Collectors.toList()));
        }

        return taskResponses;
    }

    public TaskResponse getTaskById(int taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if(optionalTask.isEmpty()) {
            throw new AppException(ErrorCode.TASK_NOT_EXISTS);
        }

        TaskResponse taskResponse = taskMapper.toTaskResponse(optionalTask.get());
        taskResponse.setOwner(userMapper.toUserResponseForTask(optionalTask.get().getOwner()));
        taskResponse.setTeam(optionalTask.get().getTeam().stream().map(userMapper::toUserResponseForTask).collect(Collectors.toList()));
        taskResponse.setTags(optionalTask.get().getTags().stream().map(Tag::getName).collect(Collectors.toList()));

        return taskResponse;
    }

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

    public TaskResponse updateTask(TaskUpdateRequest updatedTask, User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        Task task = taskRepository.findById(updatedTask.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        if(!task.getOwner().equals(existingUser)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Task updateTask = taskMapper.updateTask(updatedTask);
        log.info("Update Task: " + updateTask);
        updateTask.setTeam(task.getTeam());
        updateTask.setIssues(task.getIssues());
        updateTask.setOwner(task.getOwner());

        if (updatedTask.getTags() != null && !updatedTask.getTags().isEmpty()) {
            Set<Tag> existingTags = new HashSet<>(task.getTags());

            for (Tag updatedTag : updatedTask.getTags()) {
                Optional<Tag> existingTag = tagRepository.findById(updatedTag.getName());

                existingTag.ifPresent(existingTags::add);
            }

            updateTask.setTags(existingTags);
        }

        Task savedTask = taskRepository.save(updateTask);

        TaskResponse taskResponse = taskMapper.toTaskResponse(savedTask);

        taskResponse.setOwner(userMapper.toUserResponseForTask(savedTask.getOwner()));
        taskResponse.setTeam(savedTask.getTeam().stream().map(userMapper::toUserResponseForTask).collect(Collectors.toList()));
        taskResponse.setTags(savedTask.getTags().stream().map(Tag::getName).collect(Collectors.toList()));

        return taskResponse;
    }


    public void addUserToTask(int taskId, String userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        if(!task.getTeam().contains(user)) {
            task.getTeam().add(user);
            taskRepository.save(task);
        } else {
            throw new AppException(ErrorCode.USER_ALREADY_IN_TASK);
        }
    }

    public void removeUserFromTask(int taskId, String userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTS));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        if(!task.getTeam().contains(user)) {
            task.getTeam().remove(user);
            taskRepository.save(task);
        }
    }

    public List<Task> searchTask(String keyword) {
        String partialName = "%" + keyword + "%";
        log.info("Task Service: search Task");
        log.info("PartialName: " + keyword);
        var result = taskRepository.findByPartialName(partialName);

        for(Task index: result) {
            log.info(String.valueOf(index));
        }
        return result;
    }
}
