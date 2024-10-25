package com.example.demo.services.emailService;

import com.example.demo.entities.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender emailSender;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Value("${spring.mail.username}") private String sender;

    public void sendSuccessEmail(String notificationEmails, String csvFilePath, int productCount , LocalDateTime startTime) throws MessagingException {
        // Tạo một đối tượng MimeMessage để gửi email với file đính kèm
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        // Định dạng thời gian
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedStartTime = startTime.format(formatter);

        try {
            helper.setTo(notificationEmails);
            helper.setSubject("Crawl thành công");

            // Tạo nội dung email HTML
            // Tạo nội dung email HTML
            String body = "<html>" +
                    "<body>" +
                    "<h2 style='color:green;'>Crawl thành công!</h2>" +
                    "<p>Chúng tôi đã lưu trữ dữ liệu sản phẩm thành công với các thông tin sau:</p>" +
                    "<ul>" +
                    "<li><strong>Số lượng sản phẩm:</strong> " + productCount + "</li>" +
                    "<li><strong>File đính kèm:</strong> " + csvFilePath + "</li>" +
                    "<li><strong>Thời điểm bắt đầu crawl:</strong> " + formattedStartTime + "</li>" + // Thêm thời điểm bắt đầu crawl
                    "</ul>" +
                    "<p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!</p>" +
                    "</body>" +
                    "</html>";

            helper.setText(body, true); // true để chỉ định rằng nội dung là HTML

            // Đính kèm file CSV
            FileSystemResource file = new FileSystemResource(csvFilePath);
            helper.addAttachment(file.getFilename(), file);

            // Gửi email
            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace(); // Xử lý lỗi gửi email
        }
    }

    public String sendFailureEmail(String recipient, String errorMessage) {
        String subject = "Thông báo lỗi trong quá trình lưu dữ liệu";
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        String body = "<html>" +
                "<body>" +
                "<h2 style='color:red;'>Đã xảy ra lỗi trong quá trình lưu dữ liệu</h2>" +
                "<p style='font-size:16px;'>Mô tả lỗi: <strong>" + errorMessage + "</strong></p>" +
                "<p style='font-size:16px;'>Thời gian xảy ra lỗi: <strong>" + currentTime + "</strong></p>" +
                "<p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!</p>" +
                "</body>" +
                "</html>";

        EmailDetails details = new EmailDetails(recipient, body, subject, "");
        return sendHtmlEmail(details);
    }


    public String sendHtmlEmail(EmailDetails details) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody(), true);
            mimeMessageHelper.setSubject(details.getSubject());

            emailSender.send(mimeMessage);
            return "Mail sent Successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while Sending Mail: " + e.getMessage();
        }
    }

    @Override
    public String sendSimpleMail(EmailDetails details) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            // Sending the mail
            emailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }

        catch (Exception e) {
            e.printStackTrace(); // In thông tin chi tiết lỗi
            return "Error while Sending Mail: " + e.getMessage();
        }
    }

    @Override
    public String sendMailWithAttachment(EmailDetails details) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(details.getSubject());

            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
            mimeMessageHelper.addAttachment(file.getFilename(), file);

            emailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }

        catch (Exception e) {
            e.printStackTrace();
            return "Error while Sending Mail: " + e.getMessage();
        }
    }
}