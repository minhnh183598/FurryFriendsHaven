package org.demo.huyminh.DTO.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionRatingRequest {
    @NotNull(message = "Living space rating is required")
    @Min(1) @Max(5)
    int livingSpace;

    @NotNull(message = "Family income rating is required")
    @Min(1) @Max(5)
    int familyIncome;

    @NotNull(message = "Pet experience rating is required")
    @Min(1) @Max(5)
    int petExperience;

    @NotNull(message = "Family stability rating is required")
    @Min(1) @Max(5)
    int familyStability;

    @NotNull(message = "Time commitment rating is required")
    @Min(1) @Max(5)
    int timeCommitment;
}