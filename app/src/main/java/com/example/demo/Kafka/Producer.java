package com.example.demo.Kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class Producer {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "my-replicated-topic2";
    private final ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String,Mail> kafkaTemplate;

    public void sendMessage(Mail mail) throws JsonProcessingException {
        logger.info(String.format("$$ -> Producing message --> %s",
                objectMapper.writeValueAsString(mail)));
        this.kafkaTemplate.send(TOPIC,mail);
    }
}