package com.example.demo.Services;

import com.example.demo.Kafka.Mail;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@RequiredArgsConstructor
public class MailService {
    private final Logger logger = LoggerFactory.getLogger(MailService.class);
    private final JavaMailSender javaMailSender;

    public void sendEmail (Mail mail){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(mail.getMail());
        msg.setSubject(mail.getSubject());
        msg.setText(mail.getText());
        javaMailSender.send(msg);
        logger.info(String.format("$$ -> Mail to: %s with subject: %s",mail.getMail(),mail.getSubject()));
    }
}