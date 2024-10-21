package org.demo.huyminh.Controller;

import lombok.RequiredArgsConstructor;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Reponse.IssueResponse;
import org.demo.huyminh.DTO.Request.IssueRequest;
import org.demo.huyminh.Entity.Issue;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Service.IssueService;
import org.demo.huyminh.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author Minh
 * Date: 10/11/2024
 * Time: 5:04 PM
 */

@RestController
@RequestMapping("/api/v1/issues")
@RequiredArgsConstructor
public class IssueController {

    final IssueService issueService;
    final UserService userService;
    final TaskController taskController;

    @GetMapping("{issueId}")
    public ApiResponse<Issue> getIssueById(@PathVariable int issueId) {
        return ApiResponse.<Issue>builder()
                .code(HttpStatus.OK.value())
                .message("Find issue successfully")
                .result(issueService.getIssueById(issueId))
                .build();
    }

    @GetMapping("tasks/{taskId}/detail")
    public ApiResponse<List<IssueResponse>> getIssuesByTaskId(
            @PathVariable int taskId,
            @RequestParam(value = "status", defaultValue = "ALL") String status,
            @RequestParam(value = "sort", defaultValue = "ASC") String sort
    ) {
        return ApiResponse.<List<IssueResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Find issues successfully")
                .result(issueService.getIssuesByTasId(taskId, status, sort))
                .build();
    }

    @PostMapping("tasks/{taskId}")
    public ApiResponse<IssueResponse> createIssue(
            @RequestBody IssueRequest request,
            @PathVariable int taskId,
            @RequestHeader("Authorization") String jwt
    ) {
        String token = jwt.substring(7);
        User user = userService.findByToken(token);
        return ApiResponse.<IssueResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Create issue successfully")
                .result(issueService.createIssue(request, user, taskId))
                .build();
    }

    @DeleteMapping("{issueId}/task/{taskId}")
    public ApiResponse<Void> deleteIssue(
            @PathVariable("issueId") int issueId,
            @PathVariable("taskId") int taskId,
            @RequestHeader("Authorization") String jwt
    ) {
        String token = jwt.substring(7);
        User user = userService.findByToken(token);
        issueService.deleteIssue(issueId, taskId,user);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete issue successfully")
                .build();
    }

    @PutMapping("{issueId}/task/{taskId}/user")
    public ApiResponse<Void> addUserToIssue(
            @PathVariable("issueId") int issueId,
            @PathVariable("taskId") int taskId,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "username", required = false) String username,
            @RequestHeader("Authorization") String jwt
    ) {
        String token = jwt.substring(7);
        User user = userService.findByToken(token);
        if(userId == null && username == null) {
            throw new AppException(ErrorCode.PARAMETER_INVALID);
        }

        issueService.addUserToIssue(issueId, taskId, user, userId, username);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Add user to issue successfully")
                .build();
    }

    @PutMapping("{issueId}/task/{taskId}")
    public ApiResponse<Void> updateIssue(
            @PathVariable("issueId") int issueId,
            @PathVariable("taskId") int taskId,
            @RequestBody IssueRequest request,
            @RequestHeader("Authorization") String jwt
    ) {
        String token = jwt.substring(7);
        User user = userService.findByToken(token);
        issueService.updateIssue(request, issueId, taskId, user);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Update issue successfully")
                .build();
    }
}
