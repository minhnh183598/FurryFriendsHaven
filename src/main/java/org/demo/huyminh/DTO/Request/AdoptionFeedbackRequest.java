package org.demo.huyminh.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.Enums.*;

/**
 * @author Minh
 * Date: 11/9/2024
 * Time: 10:57 PM
 */

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
public class AdoptionFeedbackRequest {

    String petName;
    PetStatus petStatus;
    Challenge challenges;
    MedicalRecommendation medicalRecommendation;
    BehaviorChange behavioralChanges;
    ActivityLevel activityLevel;
    FoodType foodType;
    String additionalNotes;
}
