package org.demo.huyminh.Service;

import jakarta.transaction.Transactional;
import org.demo.huyminh.DTO.Reponse.FeedbackResponse;
import org.demo.huyminh.DTO.Request.FeedbackCreationRequest;
import org.demo.huyminh.Entity.User;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * @author Minh
 * Date: 10/26/2024
 * Time: 9:50 PM
 */

public interface FeedbackService {
    FeedbackResponse createFeedback(int taskId, FeedbackCreationRequest request, User reporter);

    List<FeedbackResponse> getPotentialAdopters(double minRating);

    List<FeedbackResponse> getHighRatingApplication(String petId, User user);

    List<FeedbackResponse> getFeedbacksByTaskId(int taskId);

    void updateFeedback(int feedbackId, int taskId, FeedbackCreationRequest request, User user);

    void deleteFeedback(int feedbackId, int taskId, User user);
}
