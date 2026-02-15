package com.lilley.modernnoise.Services.Interfaces;

import com.lilley.modernnoise.Data.Dtos.Mail.EmailDetails;

public interface MailService {
    void sendEmail(EmailDetails emailDetails);
}
