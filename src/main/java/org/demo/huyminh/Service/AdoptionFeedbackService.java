package org.demo.huyminh.Service;

import jakarta.transaction.Transactional;
import org.demo.huyminh.DTO.Reponse.AdoptionFeedbackResponse;
import org.demo.huyminh.DTO.Request.AdoptionFeedbackRequest;
import org.demo.huyminh.Entity.AdoptionFeedback;
import org.demo.huyminh.Entity.User;

import java.util.List;

/**
 * @author Minh
 * Date: 11/9/2024
 * Time: 11:06 PM
 */

public interface AdoptionFeedbackService {
    AdoptionFeedback createFeedback(AdoptionFeedbackRequest request, User user);

    List<AdoptionFeedbackResponse> getAllFeedback();

    AdoptionFeedback getFeedbackById(int feedbackId);

    AdoptionFeedback updateFeedback(int feedbackId, AdoptionFeedbackRequest request);

    void deleteFeedback(int feedbackId);
}
