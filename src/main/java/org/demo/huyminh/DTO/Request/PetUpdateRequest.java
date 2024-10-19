package org.demo.huyminh.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetUpdateRequest {

    String petName;
    String petAge;
    String petType;
    String petBreed;
    String petColor;
    String petDescription;
    String petSize;
    float petWeight;
    String petGender;
    String petVaccin;
    String petStatus;
    String petImage;

}
