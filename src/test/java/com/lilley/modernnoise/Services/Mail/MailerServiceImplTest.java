package com.lilley.modernnoise.Services.Mail;

import com.lilley.modernnoise.Data.Dtos.Mail.EmailDetails;
import com.lilley.modernnoise.Data.Dtos.Mail.MailType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailerServiceImplTest {

    @Autowired
    private MailerServiceImpl mailerService;

    @Test
    void sendTestEmail() {
        EmailDetails emailDetails = new EmailDetails(
                "zlilley96@gmail.com",
                "Test Email from ModernNoise",
                "This is a test email sent from the MailerServiceImplTest.",
                MailType.NOTIFICATION
        );

        mailerService.sendEmail(emailDetails);
    }
}
