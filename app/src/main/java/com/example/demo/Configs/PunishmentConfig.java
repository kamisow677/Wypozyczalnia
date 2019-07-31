package com.example.demo.Configs;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "punishment")
@Getter @Setter
public class PunishmentConfig {
    private int NORMAL_LENGTH;
    private int DAYS_TO_READ;
    private double CRITICAL_PUNISHMENT;
    private double BASE_PRICE;
    private double INTEREST_PRICE;
}
