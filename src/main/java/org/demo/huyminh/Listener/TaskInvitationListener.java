package org.demo.huyminh.Listener;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.DTO.Request.InvitationEventData;
import org.demo.huyminh.Event.TaskInvitationEvent;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskInvitationListener implements ApplicationListener<TaskInvitationEvent> {

    JavaMailSender mailSender;
    SpringTemplateEngine templateEngine;

    @Override
    public void onApplicationEvent(TaskInvitationEvent event) {
        InvitationEventData invitationData = event.getInvitationEventData();

        if (invitationData == null) {
            throw new AppException(ErrorCode.EMAIL_PROCESSING_FAILED);
        }

        try {
            sendInvitationEmail(invitationData);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new AppException(ErrorCode.EMAIL_PROCESSING_FAILED);
        }
    }

    public void sendInvitationEmail(InvitationEventData invitationData)
            throws MessagingException, UnsupportedEncodingException {

        String subject = "📣 You are invited to join a task!";
        String senderName = "Task Management Service";

        String confirmationLink = "http://localhost:3000/invitation" +
                "?taskId=" + invitationData.getTaskId() +
                "&username=" + invitationData.getUser().getUsername();

        Context context = new Context();
        context.setVariable("userName", invitationData.getUser().getUsername());
        context.setVariable("confirmationLink", confirmationLink);

        String htmlContent = templateEngine.process("TaskInvitationEmail", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("auzemon@gmail.com", senderName);
        helper.setTo(invitationData.getUser().getEmail());
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
