package com.calvin.lab;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {

        try (ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml")) {
            System.out.println("Spring Core Lab Application");

            // NotificationManager manager = (NotificationManager)
            // ctx.getBean("notificationManager");

            // manager.sendNotification();
        }
    }
}
