package org.demo.huyminh.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * @author Minh
 * Date: 10/17/2024
 * Time: 4:17 PM
 */

@Entity
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "feedback_id", nullable = false)
    Feedback feedback;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JsonIgnore
    Application application;

    @Column(nullable = false)
    int livingSpace;

    @Column(nullable = false)
    int familyIncome;

    @Column(nullable = false)
    int petExperience;

    @Column(nullable = false)
    int familyStability;

    @Column(nullable = false)
    int timeCommitment;

    @Column(nullable = false)
   double averageRating;

    @PrePersist
    @PreUpdate
    public void calculateAverageRating() {
        this.averageRating = (double) (livingSpace + familyIncome + petExperience +
                familyStability + timeCommitment) / 5.0;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", livingSpace=" + livingSpace +
                ", familyIncome=" + familyIncome +
                ", petExperience=" + petExperience +
                ", familyStability=" + familyStability +
                ", timeCommitment=" + timeCommitment +
                ", averageRating=" + averageRating +
                '}';
    }
}