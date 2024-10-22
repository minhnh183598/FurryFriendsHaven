package org.demo.huyminh.Controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Reponse.TaskResponse;
import org.demo.huyminh.DTO.Request.FeedbackCreationRequest;
import org.demo.huyminh.DTO.Request.InvitationEventData;
import org.demo.huyminh.DTO.Request.TaskCreationRequest;
import org.demo.huyminh.DTO.Request.TaskUpdateRequest;
import org.demo.huyminh.Entity.Task;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Event.TaskInvitationEvent;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Service.TaskService;
import org.demo.huyminh.Service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author Minh
 * Date: 10/11/2024
 * Time: 12:55 AM
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {

    TaskService taskService;
    UserService userService;
    ApplicationEventPublisher eventPublisher;

    @GetMapping("/team")
    public ApiResponse<List<TaskResponse>> getTaskByTeam(
            @RequestHeader("Authorization") String jwt
    ) {
        String token = jwt.substring(7);
        User user = userService.findByToken(token);
        List<TaskResponse> tasks = taskService.getTaskByTeam(user);
        return ApiResponse.<List<TaskResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Find tasks successfully")
                .result(tasks)
                .build();
    }

    @GetMapping
    public ApiResponse<List<TaskResponse>> getAllTasks() {
        return ApiResponse.<List<TaskResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Find tasks successfully")
                .result(taskService.getAllTasks())
                .build();
    }

    @GetMapping("/{taskId}")
    public ApiResponse<TaskResponse> getTaskById(@PathVariable int taskId) {
        return ApiResponse.<TaskResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Find task successfully")
                .result(taskService.getTaskById(taskId))
                .build();
    }

    @PostMapping
    public ApiResponse<TaskResponse> createTask(
            @RequestBody TaskCreationRequest request,
            @RequestHeader("Authorization") String jwt
    ) {
        String token = jwt.substring(7);
        User user = userService.findByToken(token);
        return ApiResponse.<TaskResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Create task successfully")
                .result(taskService.createTask(request, user))
                .build();
    }

    @PutMapping
    public ApiResponse<TaskResponse> updateTask(
            @RequestBody TaskUpdateRequest task,
            @RequestHeader("Authorization") String jwt
    ) {
        String token = jwt.substring(7);
        User user = userService.findByToken(token);
        TaskResponse updatedProject = taskService.updateTask(task, user);
        return ApiResponse.<TaskResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update task successfully")
                .result(updatedProject)
                .build();
    }

    @DeleteMapping("/{taskId}")
    public ApiResponse<Void> deleteTask(
            @PathVariable int taskId,
            @RequestHeader("Authorization") String jwt
    ) {
        String token = jwt.substring(7);
        User user = userService.findByToken(token);
        taskService.deleteTask(taskId, user);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete task successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<Task>> searchTaskByPartialName(@RequestParam String keyword) {
        List<Task> tasks = taskService.searchTask(keyword);
        return ApiResponse.<List<Task>>builder()
                .code(HttpStatus.OK.value())
                .message("Find tasks successfully")
                .result(tasks)
                .build();
    }

    @GetMapping("/{taskId}/invitation")
    public ApiResponse<Void> inviteUserToTask(
            @PathVariable("taskId") int taskId,
            @RequestParam("username") String username,
            @RequestHeader("Authorization") String jwt
    ) {
        String token = jwt.substring(7);
        User user = userService.findByToken(token);

        InvitationEventData invitationEventData = taskService.inviteUserToTask(taskId, username, user);

        eventPublisher.publishEvent(new TaskInvitationEvent(invitationEventData));

        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Invite user to task successfully. Please check your email")
                .build();
    }


    @GetMapping("{taskId}/invitation/user")
    public ApiResponse<String> getInvitation(
            @PathVariable("taskId") int taskId,
            @RequestParam("username") String username,
            @RequestParam("choice") String choice,
            @RequestHeader("Authorization") String jwt
    ) {
        String token = jwt.substring(7);
        User user = userService.findByToken(token);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .result(taskService.acceptInvitation(taskId, username, user, choice))
                .build();
    }

    @PutMapping("{taskId}/user/{userId}")
    public ApiResponse<Void> addUserToTask(
            @PathVariable("taskId") int taskId,
            @PathVariable("userId") String userId,
            @RequestHeader("Authorization") String jwt
    ) {
        String token = jwt.substring(7);
        User user = userService.findByToken(token);
        taskService.addUserToTask(taskId, userId, user);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Add user to task successfully")
                .build();
    }

    @DeleteMapping("{taskId}/user/{userId}")
    public ApiResponse<Void> deleteUserToTask(
            @PathVariable("taskId") int taskId,
            @PathVariable("userId") String userId,
            @RequestHeader("Authorization") String jwt
    ) {
        String token = jwt.substring(7);
        User user = userService.findByToken(token);
        taskService.removeUserFromTask(taskId, userId, user);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete user from task successfully")
                .build();
    }

    @PutMapping("{taskId}/status/{status}")
    public ApiResponse<Void> changeTaskStatus(
            @PathVariable("taskId") int taskId,
            @PathVariable("status") String status,
            @RequestBody FeedbackCreationRequest feedback,
            @RequestHeader("Authorization") String jwt
    ) {
        String token = jwt.substring(7);
        User user = userService.findByToken(token);
        taskService.changeStatus(taskId, status, feedback, user);
        log.info("Change status successfully");
        log.info("User: {}", user);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Change status successfully")
                .build();
    }
}
