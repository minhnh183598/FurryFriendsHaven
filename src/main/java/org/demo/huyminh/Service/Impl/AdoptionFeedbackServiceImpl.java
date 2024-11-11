package org.demo.huyminh.Service.Impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Reponse.AdoptionFeedbackResponse;
import org.demo.huyminh.DTO.Request.AdoptionFeedbackData;
import org.demo.huyminh.DTO.Request.AdoptionFeedbackRequest;
import org.demo.huyminh.Entity.AdoptionFeedback;
import org.demo.huyminh.Entity.Pet;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Event.AdoptionFeedbackEvent;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Mapper.AdoptionMapper;
import org.demo.huyminh.Repository.AdoptionFeedbackRepository;
import org.demo.huyminh.Repository.PetRepository;
import org.demo.huyminh.Repository.UserRepository;
import org.demo.huyminh.Service.AdoptionFeedbackService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Minh
 * Date: 11/9/2024
 * Time: 10:53 PM
 */

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AdoptionFeedbackServiceImpl implements AdoptionFeedbackService {

    AdoptionFeedbackRepository adoptionFeedbackRepository;
    AdoptionMapper adoptionMapper;
    UserRepository userRepository;
    PetRepository petRepository;
    ApplicationEventPublisher eventPublisher;

    @Override
    public AdoptionFeedback createFeedback(AdoptionFeedbackRequest request, User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        Pet existingPet = petRepository.findByPetName(request.getPetName())
                .orElseThrow(() -> new AppException(ErrorCode.PET_NOT_EXISTS));

        if(!existingUser.equals(existingPet.getAdopter())) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TO_CREATE_FEEDBACK);
        }

        if(adoptionFeedbackRepository.findByPetName(request.getPetName()).isPresent()) {
            throw new AppException(ErrorCode.FEEDBACK_ALREADY_EXISTS);
        }

        AdoptionFeedback adoptionFeedback = adoptionMapper.toAdoptionFeedback(request);
        adoptionFeedback.setAdopterName(existingUser.getUsername());
        return adoptionFeedbackRepository.save(adoptionFeedback);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdoptionFeedbackResponse> getAllFeedback() {
        List<AdoptionFeedback> adoptionFeedbacks = adoptionFeedbackRepository.findAll();

        return adoptionFeedbacks.stream()
                .map(adoptionMapper::toAdoptionFeedbackResponse)
                .toList();
    }

    @Override
    public AdoptionFeedback getFeedbackById(int feedbackId) {
        return adoptionFeedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_FOUND));
    }

    @Transactional
    @Override
    public AdoptionFeedback updateFeedback(int feedbackId, AdoptionFeedbackRequest request) {
        AdoptionFeedback existingFeedback = adoptionFeedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_FOUND));

        existingFeedback.setPetStatus(request.getPetStatus());
        existingFeedback.setChallenges(request.getChallenges());
        existingFeedback.setMedicalRecommendation(request.getMedicalRecommendation());
        existingFeedback.setBehavioralChanges(request.getBehavioralChanges());
        existingFeedback.setActivityLevel(request.getActivityLevel());
        existingFeedback.setFoodType(request.getFoodType());
        existingFeedback.setAdditionalNotes(request.getAdditionalNotes());
        existingFeedback.setEditedAt(LocalDateTime.now(Clock.systemDefaultZone()));

        return adoptionFeedbackRepository.save(existingFeedback);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteFeedback(int feedbackId) {
        AdoptionFeedback existingFeedback = adoptionFeedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_FOUND));

        adoptionFeedbackRepository.delete(existingFeedback);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void sendEmailForAdopter(String petId) {
        Pet existingPet = petRepository.findById(petId)
                .orElseThrow(() -> new AppException(ErrorCode.PET_NOT_EXISTS));

        if(existingPet.getAdopter() == null) {
            throw new AppException(ErrorCode.PET_NOT_ADOPTED);
        }

        AdoptionFeedbackData adoptionFeedbackData = AdoptionFeedbackData.builder()
                .petId(petId)
                .user(existingPet.getAdopter())
                .build();

        eventPublisher.publishEvent(new AdoptionFeedbackEvent(adoptionFeedbackData));
    }
}
