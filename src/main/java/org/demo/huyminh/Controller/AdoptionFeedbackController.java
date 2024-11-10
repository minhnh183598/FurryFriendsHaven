package org.demo.huyminh.Controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.AdoptionFeedbackResponse;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Request.AdoptionFeedbackRequest;
import org.demo.huyminh.Entity.AdoptionFeedback;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Service.AdoptionFeedbackService;
import org.demo.huyminh.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author Minh
 * Date: 11/9/2024
 * Time: 11:38 PM
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/adoptionFeedbacks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdoptionFeedbackController {

    AdoptionFeedbackService adoptionFeedbackService;
    UserService userService;

    @PostMapping
    public ApiResponse<AdoptionFeedback> submitFeedback(
            @RequestBody AdoptionFeedbackRequest request,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        User user = userService.findByToken(jwt);

        AdoptionFeedback feedback = adoptionFeedbackService.createFeedback(request, user);
        return ApiResponse.<AdoptionFeedback>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create feedback successfully")
                .result(feedback)
                .build();
    }

    @GetMapping
    public ApiResponse<List<AdoptionFeedbackResponse>> getAllFeedback() {
        List<AdoptionFeedbackResponse> feedbackResponses = adoptionFeedbackService.getAllFeedback();
        return ApiResponse.<List<AdoptionFeedbackResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Get all post-adoption feedback successfully")
                .result(feedbackResponses)
                .build();
    }

    @GetMapping("/{feedbackId}")
    public ApiResponse<AdoptionFeedback> getFeedbackById(@PathVariable int feedbackId) {
        AdoptionFeedback feedback = adoptionFeedbackService.getFeedbackById(feedbackId);
        return ApiResponse.<AdoptionFeedback>builder()
                .code(HttpStatus.OK.value())
                .message("Get feedback by id successfully")
                .result(feedback)
                .build();
    }

    @PutMapping("/{feedbackId}")
    public ApiResponse<AdoptionFeedback> updateFeedback(
            @PathVariable int feedbackId,
            @RequestBody AdoptionFeedbackRequest request
    ) {
        AdoptionFeedback updatedFeedback = adoptionFeedbackService.updateFeedback(feedbackId, request);
        return ApiResponse.<AdoptionFeedback>builder()
                .code(HttpStatus.OK.value())
                .message("Update feedback successfully")
                .result(updatedFeedback)
                .build();
    }

    @DeleteMapping("/{feedbackId}")
    public ApiResponse<Void> deleteFeedback(@PathVariable int feedbackId) {
        adoptionFeedbackService.deleteFeedback(feedbackId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete feedback successfully")
                .build();
    }

    @GetMapping("/pet/{petId}")
    public ApiResponse<Void> getFeedbackByPetId(@PathVariable String petId) {
        adoptionFeedbackService.sendEmailForAdopter(petId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Send email for adopter successfully")
                .build();
    }
}
