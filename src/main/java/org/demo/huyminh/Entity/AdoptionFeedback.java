package org.demo.huyminh.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.Enums.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * @author Minh
 * Date: 11/9/2024
 * Time: 10:12 PM
 */

@Entity
@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false)
    String adopterName;
    @Column(nullable = false)
    String petName;
    @Column(nullable = false)
    PetStatus petStatus;
    @Column(nullable = false)
    Challenge challenges;
    @Column(nullable = false)
    MedicalRecommendation medicalRecommendation;
    @Column(nullable = false)
    BehaviorChange behavioralChanges;
    @Column(nullable = false)
    ActivityLevel activityLevel;
    @Column(nullable = false)
    FoodType foodType;
    String additionalNotes;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime editedAt;
}
