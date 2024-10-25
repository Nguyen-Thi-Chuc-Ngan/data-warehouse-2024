package com.example.demo.service.emailService;

import com.example.demo.model.EmailDetails;

public interface IEmailService {

    String sendSimpleMail(EmailDetails details);

    String sendMailWithAttachment(EmailDetails details);
}
