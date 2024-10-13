package org.demo.huyminh.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Reponse.TaskResponse;
import org.demo.huyminh.DTO.Request.TaskCreationRequest;
import org.demo.huyminh.DTO.Request.TaskUpdateRequest;
import org.demo.huyminh.Entity.Task;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Service.TaskService;
import org.demo.huyminh.Service.UserService;
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
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @GetMapping
    public ApiResponse<List<TaskResponse>> getTasks(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag,
            @RequestHeader("Authorization") String jwt
    ) {
        String token = jwt.substring(7);
        User user = userService.findByToken(token);
        List<TaskResponse> tasks = taskService.getTaskByTeam(user, category, tag);
        return ApiResponse.<List<TaskResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Find tasks successfully")
                .result(tasks)
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

    @PutMapping("{taskId}/user/{userId}")
    public ApiResponse<Void> addUserToTask(
            @PathVariable("taskId") int taskId,
            @PathVariable("userId") String userId,
            @RequestHeader("Authorization") String jwt
    ) {
        String token = jwt.substring(7);
        User user = userService.findByToken(token);
        taskService.addUserToTask(taskId, userId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Add user to task successfully")
                .build();
    }
}
