package org.phamxuantruong.asm2.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, copy this code to fill your form:\n" + resetLink);
        mailSender.send(message);
    }
}
