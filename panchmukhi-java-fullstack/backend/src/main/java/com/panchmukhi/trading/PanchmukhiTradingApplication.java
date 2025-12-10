package com.panchmukhi.trading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
@EnableAsync
@EnableScheduling
public class PanchmukhiTradingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PanchmukhiTradingApplication.class, args);
    }
}
