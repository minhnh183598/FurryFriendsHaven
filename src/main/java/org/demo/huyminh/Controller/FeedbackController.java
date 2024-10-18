package org.demo.huyminh.Controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Reponse.FeedbackResponse;
import org.demo.huyminh.DTO.Request.FeedbackCreationRequest;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Service.FeedbackService;
import org.demo.huyminh.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Minh
 * Date: 10/17/2024
 * Time: 7:28 PM
 */

@RestController
@RequestMapping("/api/v1/feedbacks")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class FeedbackController {

    FeedbackService feedbackService;
    UserService userService;

    @PostMapping("/task/{taskId}")
    public ApiResponse<FeedbackResponse> createFeedback(
            @RequestBody FeedbackCreationRequest request,
            @PathVariable int taskId,
            @RequestHeader("Authorization") String token
    ) {

        String jwt = token.substring(7);
        User user = userService.findByToken(jwt);
        FeedbackResponse response = feedbackService.createFeedback(taskId, request, user);
        return ApiResponse.<FeedbackResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create feedback successfully")
                .result(response)
                .build();
    }

    @GetMapping("task/{taskId}")
    public ApiResponse<List<FeedbackResponse>> getFeedbacksByTaskId(@PathVariable int taskId) {
        return ApiResponse.<List<FeedbackResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get feedbacks successfully")
                .result(feedbackService.getFeedbacksByTaskId(taskId))
                .build();
    }

    @DeleteMapping("/{feedbackId}/task/{taskId}")
    public ApiResponse<Void> deleteFeedback(
            @PathVariable int feedbackId,
            @PathVariable int taskId,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        User user = userService.findByToken(jwt);
        feedbackService.deleteFeedback(feedbackId, taskId, user);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete feedback successfully")
                .build();
    }

    @PutMapping("/{feedbackId}/task/{taskId}")
    public ApiResponse<Void> updateFeedback(
            @PathVariable int feedbackId,
            @PathVariable int taskId,
            @RequestBody FeedbackCreationRequest request,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        User user = userService.findByToken(jwt);
        log.info("Request: {}", request);
        feedbackService.updateFeedback(feedbackId, taskId, request, user);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Update feedback successfully")
                .build();
    }
}
