package com.lilley.modernnoise.Services.Mail;

import com.lilley.modernnoise.Data.Dtos.Mail.EmailDetails;
import com.lilley.modernnoise.Services.Interfaces.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailerServiceImpl implements MailService {
    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(EmailDetails emailDetails) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("no-reply@modernnoise.it.com");
            message.setTo(emailDetails.recipient());
            message.setSubject(emailDetails.subject());
            message.setText(emailDetails.body());
            mailSender.send(message);
        } catch (Exception e){

            log.error("Failed to send email of type {}: {}. User: {}", emailDetails.mailType(), e.getMessage(), emailDetails.recipient());
        }
    }
}
