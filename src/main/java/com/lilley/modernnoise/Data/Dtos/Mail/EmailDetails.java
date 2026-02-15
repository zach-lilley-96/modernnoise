package com.lilley.modernnoise.Data.Dtos.Mail;

public record EmailDetails(String recipient, String subject, String body, MailType mailType) {
}