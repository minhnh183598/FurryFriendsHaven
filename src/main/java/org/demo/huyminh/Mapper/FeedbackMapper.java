package org.demo.huyminh.Mapper;

import org.demo.huyminh.DTO.Reponse.FeedbackResponse;
import org.demo.huyminh.DTO.Request.FeedbackCreationRequest;
import org.demo.huyminh.Entity.Feedback;
import org.demo.huyminh.Entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author Minh
 * Date: 10/17/2024
 * Time: 6:13 PM
 */

@Mapper(componentModel = "Spring")
public interface FeedbackMapper {

    @Mapping(target = "images", expression = "java(mapStringsToImages(feedbackRequest.getImages(), null))")
    Feedback toFeedback(FeedbackCreationRequest feedbackRequest);

    @Mapping(target = "images", expression = "java(mapImagesToStrings(feedback.getImages()))")
    FeedbackResponse toFeedbackResponse(Feedback feedback);

    default Image stringToImage(String imageUrl, Feedback feedback) {
        if (imageUrl == null || feedback == null) {
            return null;
        }
        Image image = new Image();
        image.setImageUrl(imageUrl);
        image.setFeedback(feedback);
        return image;
    }

    default String imageToString(Image image) {
        if (image == null) {
            return null;
        }
        return image.getImageUrl();
    }

    default List<String> mapImagesToStrings(List<Image> images) {
        if (images == null) {
            return null;
        }
        return images.stream()
                .map(this::imageToString)
                .toList();
    }

    default List<Image> mapStringsToImages(List<String> imageUrls, Feedback feedback) {
        if (imageUrls == null) {
            return null;
        }
        return imageUrls.stream()
                .map(url -> stringToImage(url, feedback))
                .toList();
    }
}
