package org.demo.huyminh.Service;

import org.demo.huyminh.DTO.Reponse.BriefFeedbackResponse;
import org.demo.huyminh.DTO.Reponse.FeedbackResponse;
import org.demo.huyminh.DTO.Reponse.PetFeedbackResponse;
import org.demo.huyminh.DTO.Request.FeedbackCreationRequest;
import org.demo.huyminh.Entity.User;
import java.util.List;

/**
 * @author Minh
 * Date: 10/26/2024
 * Time: 9:50 PM
 */

public interface FeedbackService {
    FeedbackResponse createFeedback(int taskId, FeedbackCreationRequest request, User reporter);

    List<PetFeedbackResponse> getAvailablePets();

    List<FeedbackResponse> getPotentialAdopters(double minRating);

    List<BriefFeedbackResponse> getHighRatingApplication(String petId, String sortBy, String sortDir, User user);

    List<FeedbackResponse> getFeedbacksByTaskId(int taskId);

    void updateFeedback(int feedbackId, int taskId, FeedbackCreationRequest request, User user);

    void deleteFeedback(int feedbackId, int taskId, User user);
}
