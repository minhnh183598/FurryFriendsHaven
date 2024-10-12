package org.demo.huyminh.DTO.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetCreationRequest {

     String petName;
     @Pattern(regexp = "Young|Old|Full Grown", message = "INVALID PET AGE !!!" +
             "\n (PETAGE : Young, Old, Full Grown)")
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

     @CreationTimestamp
     @Column(name = "create_pet_at")
     private LocalDateTime createdPetAt;
}
