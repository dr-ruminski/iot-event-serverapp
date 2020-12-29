package com.dr.iot.service;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dr.iot.controller.e.WrongSubscriptionDataException;
import com.dr.iot.model.EventNotification;
import com.dr.iot.rabbitmq.RabbitMQConfiguration;

import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

/**
 * A service responsible for observing a message-broker messages. When it gets
 * an instance {@link List} of messages uses Spring Webflux stack in order to
 * delegate messages to external Event Notification Server. 
 * {@link WebClient} is thread-safe.
 * 
 * @author dr
 *
 */
@Service
public class NotificationService {

	// webflux webClient responsible for requesting external notification service
	private final WebClient webClient;

	// uri to the external Notification service, properties are read from notificationServer.properties
	private final String notificationsServerUri;

	// hack: Spring can't inject values into fields until it hasn't been
	// instantiated
	// So, that's why the constructor looks like a crap
	public NotificationService(@Value("${event.notification.server.address}") String address,
			@Value("${event.notification.server.port}") String port,
			@Value("${event.notification.server.context}") String context) {

		// building uri of the notification server
		StringBuilder notificationServerURIsb = new StringBuilder();
		notificationServerURIsb.append(address).append(':').append(port).append(context);
		this.notificationsServerUri = notificationServerURIsb.toString();

		HttpClient httpClient = HttpClient.create().wiretap(true);

		// configuring a webclient instance
		WebClient.Builder webClientBuilder = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient)).baseUrl(notificationsServerUri)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		this.webClient = webClientBuilder.build();
	}

	/**
	 * Rabbit observer. It triggers asynchronous event messages to the external
	 * Notification server (ENS).
	 * 
	 * @param notifications
	 */
	@RabbitListener(queues = RabbitMQConfiguration.EVENT_NOTIFICATION_QUEUE_NAME, containerFactory = "batchContainerFactory")
	@SuppressWarnings("rawtypes")
	public void sendNotification(List<EventNotification> notifications) {
		Mono<ResponseEntity> response = webClient.post()
//					.body(Mono.just(notifications), ArrayList.class)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(notifications).retrieve()
				.onStatus(org.springframework.http.HttpStatus::is5xxServerError,
						clientResp -> Mono.error(new WrongSubscriptionDataException()))
				.bodyToMono(ResponseEntity.class).onErrorMap(Exception::new);
//					.timeout(Duration.ofMillis(10_00))

		response.subscribe(System.out::println); 
	}

}
