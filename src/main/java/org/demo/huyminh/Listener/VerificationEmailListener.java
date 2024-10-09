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
import org.demo.huyminh.Repository.OtpRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

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

    final OtpRepository otpRepository;
    final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void onApplicationEvent(VerificationEmailEvent event) {

        User user = event.getUser();

        String otp = generateOtp();
        Otp existingOtp = otpRepository.findByUserId(user.getId());
        if(existingOtp != null) {

            otpRepository.save(Otp.builder()
                    .id(existingOtp.getId())
                    .expireTime(existingOtp.generateExpireTime())
                    .user(existingOtp.getUser())
                    .refreshCount(existingOtp.getRefreshCount() + 1)
                    .code(otp)
                    .build()
            );
            log.info("Refresh Count: {}", existingOtp.getRefreshCount());
        } else {
            otpRepository.save(new Otp(otp, user));
        }

        try {
            sendVerificationEmail(otp, user);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new AppException(ErrorCode.EMAIL_PROCESSING_FAILED);
        }
    }

    public void sendVerificationEmail(String otp, User user) throws MessagingException, UnsupportedEncodingException {
        String subject = "🎊 Your otp is here!";
        String senderName = "User Registration Portal Service";

        Context context = new Context();
        context.setVariable("name", user.getUsername());
        context.setVariable("otp", otp);
        context.setVariable("expirationTime", "1");

        String htmlContent = templateEngine.process("VerificationEmail", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("auzemon@gmail.com", senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

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
