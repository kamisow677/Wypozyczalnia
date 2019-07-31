package com.example.demo.Configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "trello")
@Getter @Setter
public class TrelloConfig {

    private  String KEY ;
    private  String TOKEN;
    private  String GET_ALL_BOARDS_URL;
    private  String GET_LIST_URL;
    private  String POST_LIST_URL;
    private  String POST_NEW_CARD_URL;

    private  String KEY_KEY;
    private  String KEY_TOKEN;
    private  String KEY_ID;

    private  String BOARD_NAME;
}