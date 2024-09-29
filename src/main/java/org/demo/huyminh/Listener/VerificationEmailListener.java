package org.demo.huyminh.Listener;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.demo.huyminh.Entity.Otp;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Event.VerificationEmailEvent;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Repository.OptRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * @author Minh
 * Date: 9/28/2024
 * Time: 7:19 PM
 */

@Slf4j
@EnableAsync
@Component
@FieldDefaults(level = lombok. AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class VerificationEmailListener implements ApplicationListener<VerificationEmailEvent> {

    final OptRepository optRepository;
    final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(VerificationEmailEvent event) {

        User user = event.getUser();

        String otp = generateOtp();
        Otp existingOtp = optRepository.findByUserId(user.getId());
        if(existingOtp != null) {
            optRepository.save(Otp.builder()
                    .id(existingOtp.getId())
                    .expireTime(existingOtp.generateExpireTime())
                    .user(existingOtp.getUser())
                    .code(otp)
                    .build()
            );
        } else {
            optRepository.save(new Otp(otp, user));
        }

        try {
            sendVerificationEmail(otp, user);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new AppException(ErrorCode.EMAIL_PROCESSING_FAILED);
        }
    }

    public void sendVerificationEmail(String otp, User user) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "User Registration Portal Service";
        String mailContent = "<p> Hi, "+ user.getUsername()+ ", </p>"+
                "<p>Thank you for registering with us,"+"</p>" +
                "Please, enter the following OTP to complete your registration.</p>"+
                 otp +
                "<p> Thank you <br> Users Registration Portal Service";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("auzemon@gmail.com", senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    String generateOtp() {
        Random random = new Random();
        int otp = random.nextInt(999999);
        return String.format("%06d", otp);
    }

    @Override
    public boolean supportsAsyncExecution() {
        return true;
    }
}
