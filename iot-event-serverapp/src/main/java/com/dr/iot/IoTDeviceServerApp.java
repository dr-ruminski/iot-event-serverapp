package com.dr.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * <p>
 * Main application. Also, Spring loads 'externalNotificationServer.properties' containing
 * host, port, and context of the Event Notification service (ENS). The RabbitMq instance is a middle man
 * that enqueues messages and pass them to ENS in order to notify subscribers.
 * </p>
 */
@SpringBootApplication
@PropertySource("classpath:eventNotificationServer.properties")
public class IoTDeviceServerApp {

	public static void main(String[] args) {
		SpringApplication.run(IoTDeviceServerApp.class, args);
	}
}
