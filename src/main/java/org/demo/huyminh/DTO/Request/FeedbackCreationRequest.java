package org.demo.huyminh.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.Entity.Rating;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
public class FeedbackCreationRequest {
    @NotBlank(message = "Content is required")
    String content;

    List<String> images;

    @NotNull(message = "Adoption rating is required")
    Rating rating;
}