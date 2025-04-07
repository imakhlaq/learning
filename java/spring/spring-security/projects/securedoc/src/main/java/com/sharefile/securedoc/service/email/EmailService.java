package com.sharefile.securedoc.service.email;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.sharefile.securedoc.utils.EmailUtils.getEmailMessage;
import static com.sharefile.securedoc.utils.EmailUtils.getPasswordResetMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements IEmailService {

    private final JavaMailSender mailSender;

    private final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    private final String PASSWORD_REST_VERIFICATION = "Password Rest Verification";

    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async
    public void sendNewAccountEmail(String name, String to, String token) {
        try {
            var message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getEmailMessage(name, host, token));
            mailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @Async
    public void sendPasswordResetEmail(String name, String to, String token) {
        try {
            var message = new SimpleMailMessage();
            message.setSubject(PASSWORD_REST_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getPasswordResetMessage(name, host, token));
            mailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

}