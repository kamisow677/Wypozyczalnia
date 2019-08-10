//package com.example.demo.Kafka;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import lombok.RequiredArgsConstructor;
//import org.codehaus.jackson.node.TextNode;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping(value = "/kafka")
//public class KafkaController {
//    private final Producer producer;
//
//    @PostMapping(value = "/publish")
//    public void sendMessageToKafkaTopic(@Valid @RequestBody Mail mail) throws JsonProcessingException {
//        this.producer.sendMessage(mail);
//    }
//}
