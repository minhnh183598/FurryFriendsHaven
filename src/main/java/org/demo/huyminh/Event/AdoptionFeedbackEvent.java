package org.demo.huyminh.Event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.DTO.Request.AdoptionFeedbackData;
import org.springframework.context.ApplicationEvent;

/**
 * @author Minh
 * Date: 11/10/2024
 * Time: 7:10 PM
 */

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdoptionFeedbackEvent extends ApplicationEvent {

    AdoptionFeedbackData adoptionFeedbackData;

    public AdoptionFeedbackEvent(AdoptionFeedbackData adoptionFeedbackData) {
        super(adoptionFeedbackData);
        this.adoptionFeedbackData = adoptionFeedbackData;
    }
}