package org.demo.huyminh.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String petId;
    String petName;
    String petType;
    String petAge;
    String petBreed;
    String petColor;
    String petDescription;
    String petSize;
    float petWeight;
    String petGender;
    String petVaccin;
    String petStatus;
    String petImage;

    @CreationTimestamp
    @Column(name = "created_pet_at")
    LocalDateTime createdPetAt;
}
