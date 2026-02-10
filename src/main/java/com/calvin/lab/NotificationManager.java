package com.calvin.lab;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class NotificationManager {

    // Create a logger
    private static final Logger LOGGER = Logger.getLogger(NotificationManager.class.getName());

    private MessageService messageService;

    @Value("#{config['env.name']}")
    private String environmentName;

    @Value("#{config['secret.key']}")
    private String secretKey;

    public NotificationManager(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostConstruct
    public void init() {
        LOGGER.info("--- [NotificationManager] ---");
        LOGGER.info("Environment: " + environmentName);
        LOGGER.info("Secret Key:  " + secretKey);
        String message = messageService.getMessage();
        LOGGER.info("Sending notification: " + message);
    }
}
