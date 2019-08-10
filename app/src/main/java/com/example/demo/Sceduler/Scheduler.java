package com.example.demo.Sceduler;

import com.example.demo.Services.BookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class Scheduler {
    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
    private final BookService bookService;

//    @Scheduled(fixedRate = 1000000, initialDelay = 1000000)
//    public void executeTask() {
//        log.info("Task executed at {}", LocalDate.now());
//        bookService.updatePunishments();
//    }
}