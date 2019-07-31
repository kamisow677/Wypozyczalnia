package com.example.demo.mail;

import com.example.demo.Kafka.Mail;
import com.example.demo.Services.MailService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class SpringMailIntegrationTest {

    @Autowired
    private MailService mailService;

    @Rule
    public SmtpServerRule smtpServerRule = new SmtpServerRule(2525);

    @Test
    public void shouldSendSingleMail() throws MessagingException, IOException {
        Mail mail = new Mail();
        mail.setMail("info@memorynotfound.com");
        mail.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
        mail.setText("We show how to write Integration Tests using Spring and GreenMail.");

        mailService.sendEmail(mail);

        MimeMessage[] receivedMessages = smtpServerRule.getMessages();
        assertEquals(1, receivedMessages.length);

        MimeMessage current = receivedMessages[0];

        assertEquals(mail.getSubject(), current.getSubject());
        assertEquals(mail.getMail(), current.getAllRecipients()[0].toString());
        assertTrue(String.valueOf(current.getContent()).contains(mail.getText()));

    }

}
