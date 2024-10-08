package org.demo.huyminh.DTO.Request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetCreationRequest {

     @Size(min = 4, message = "PET NAME HAS BEEN AT LEAST 4 CHARACTERS")
     String petName;
     @Pattern(regexp = "Young|Old|Full Grown", message = "INVALID PET AGE !!!" +
             "\n (PETAGE : YOUNG, OLD, FULL GROWN)")
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
}
