package org.demo.huyminh.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User adopter;

    @CreationTimestamp
    @Column(name = "created_pet_at")
    LocalDateTime createdPetAt;


    @Override
    public String toString() {
        return "Pet{" +
                "petId='" + petId + '\'' +
                ", petName='" + petName + '\'' +
                ", petType='" + petType + '\'' +
                ", petAge='" + petAge + '\'' +
                ", petBreed='" + petBreed + '\'' +
                ", petColor='" + petColor + '\'' +
                ", petDescription='" + petDescription + '\'' +
                ", petSize='" + petSize + '\'' +
                ", petWeight=" + petWeight +
                ", petGender='" + petGender + '\'' +
                ", petVaccin='" + petVaccin + '\'' +
                ", petStatus='" + petStatus + '\'' +
                ", petImage='" + petImage + '\'' +
                ", createdPetAt=" + createdPetAt +
                '}';
    }
}
