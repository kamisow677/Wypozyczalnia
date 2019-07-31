package com.example.demo.Kafka;

import com.example.demo.Services.MailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class Consumer {
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);
    private final MailService mailService;

    @KafkaListener(topics = "my-replicated-topic2", groupId = "id-1")
    public void consume(Mail mail){

        mailService.sendEmail(mail);

        logger.info(String.format("$$ -> Consumed Message -> %s",mail));
    }
}