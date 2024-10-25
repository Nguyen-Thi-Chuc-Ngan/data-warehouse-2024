package com.example.demo.services.emailService;

import com.example.demo.entities.EmailDetails;

public interface IEmailService {

    String sendSimpleMail(EmailDetails details);

    String sendMailWithAttachment(EmailDetails details);

}
