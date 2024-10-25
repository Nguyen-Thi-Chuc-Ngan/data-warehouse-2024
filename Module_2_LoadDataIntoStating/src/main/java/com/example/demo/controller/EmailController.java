package com.example.demo.controller;


import com.example.demo.entities.EmailDetails;
import com.example.demo.services.emailService.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// Annotation
@RestController
// Class
public class EmailController {

    @Autowired
    private IEmailService emailService;

    @PostMapping("/api/sendMail")
    public String
    sendMail(@RequestBody EmailDetails details) {

        String status = emailService.sendSimpleMail(details);
        return status;
    }

    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(
            @RequestBody EmailDetails details) {
        String status = emailService.sendMailWithAttachment(details);
        return status;
    }
}