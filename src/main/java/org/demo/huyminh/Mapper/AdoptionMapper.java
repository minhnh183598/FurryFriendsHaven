package org.demo.huyminh.Mapper;

import org.demo.huyminh.DTO.Reponse.AdoptionFeedbackResponse;
import org.demo.huyminh.DTO.Reponse.AdoptionTaskResponse;
import org.demo.huyminh.DTO.Request.AdoptionFeedbackRequest;
import org.demo.huyminh.Entity.AdoptionFeedback;
import org.mapstruct.Mapper;

/**
 * @author Minh
 * Date: 11/9/2024
 * Time: 11:01 PM
 */

@Mapper(componentModel = "Spring")
public interface AdoptionMapper {

    AdoptionFeedback toAdoptionFeedback(AdoptionFeedbackRequest request);

    AdoptionFeedbackResponse toAdoptionFeedbackResponse(AdoptionFeedback task);
}
