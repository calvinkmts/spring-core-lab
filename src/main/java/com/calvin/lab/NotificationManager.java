package com.calvin.lab;

import org.springframework.stereotype.Component;

@Component
public class NotificationManager {

    private MessageService messageService;

    public NotificationManager(MessageService messageService) {
        this.messageService = messageService;
    }

    public void sendNotification() {
        String message = messageService.getMessage();
        System.out.println("Sending notification: " + message);
    }
}
