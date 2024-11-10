package org.demo.huyminh.Listener;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.DTO.Request.AdoptionFeedbackData;
import org.demo.huyminh.Entity.Pet;
import org.demo.huyminh.Event.AdoptionFeedbackEvent;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Repository.PetRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.io.UnsupportedEncodingException;
import org.thymeleaf.context.Context;

/**
 * @author Minh
 * Date: 11/10/2024
 * Time: 7:16 PM
 */

@Slf4j
@EnableAsync
@Component
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AdoptionFeedbackListener implements ApplicationListener<AdoptionFeedbackEvent> {

    JavaMailSender mailSender;
    SpringTemplateEngine templateEngine;
    PetRepository petRepository;

    @Override
    public void onApplicationEvent(AdoptionFeedbackEvent event) {

        AdoptionFeedbackData adoptionFeedbackData = event.getAdoptionFeedbackData();

        if(adoptionFeedbackData == null) {
            throw new AppException(ErrorCode.EMAIL_PROCESSING_FAILED);
        }

        try {
            sendAdoptionFeedbackEmail(adoptionFeedbackData);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new AppException(ErrorCode.EMAIL_PROCESSING_FAILED);
        }
    }

    private void sendAdoptionFeedbackEmail(AdoptionFeedbackData adoptionFeedbackData)
            throws MessagingException, UnsupportedEncodingException {
        Pet pet = petRepository.findById(adoptionFeedbackData.getPetId())
                .orElseThrow(() -> new AppException(ErrorCode.PET_NOT_FOUND));

        String subject = "🐾 Share Your Pet Adoption Experience";
        String senderName = "Pet Adoption Service";

        String feedbackLink = "http://localhost:3000/adopter-feedback/" +
                adoptionFeedbackData.getPetId();

        Context context = new Context();
        context.setVariable("adopterName", adoptionFeedbackData.getUser().getUsername());
        context.setVariable("petName", pet.getPetName());
        context.setVariable("feedbackLink", feedbackLink);

        String htmlContent = templateEngine.process("AdoptionFeedbackEmail", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("auzemon@gmail.com", senderName);
        helper.setTo(adoptionFeedbackData.getUser().getEmail());
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    @Override
    public boolean supportsAsyncExecution() {
        return true;
    }
}
