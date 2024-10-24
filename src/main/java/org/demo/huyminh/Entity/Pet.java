package org.demo.huyminh.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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
    private String petId;

    private String petName;
    private String petType;
    private String petAge;
    private String petBreed;
    private String petColor;
    private String petDescription;
    private String petSize;
    private float petWeight;
    private String petGender;
    private String petVaccin;
    private String petStatus;
}
