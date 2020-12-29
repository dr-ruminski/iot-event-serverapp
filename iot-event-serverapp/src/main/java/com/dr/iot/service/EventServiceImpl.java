package com.dr.iot.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dr.iot.controller.e.WrongDeviceException;
import com.dr.iot.model.ESP8266Device;
import com.dr.iot.model.Event;
import com.dr.iot.model.EventNotification;
import com.dr.iot.model.Tag;
import com.dr.iot.rabbitmq.RabbitMQConfiguration;
import com.dr.iot.repository.DeviceRepository;
import com.dr.iot.repository.EventRepository;
import com.dr.iot.repository.TagRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class EventServiceImpl implements IEventService {

	// @TODO: a good place for optimization -> perform load test with JMeter
	private ExecutorService executorService = 
			  new ThreadPoolExecutor(20, 50, 0L, TimeUnit.MILLISECONDS,   
			  new LinkedBlockingQueue<Runnable>());
	
	@Autowired
	private DeviceRepository deviceRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private AmqpTemplate batchingRabbitTemplate;

	@Override
	public Optional<Event> findById(Long eventId) {
		return eventRepository.findById(eventId);
	}

	@Override
	public Event createEvent(Event event) {
		Long deviceId = event.getDevice().getId();
		ESP8266Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new WrongDeviceException(deviceId));

		Set<Tag> tags = event.getTags();

		persistTags(event, tags);
		event.setDevice(device);

		// this method will be problematic due to the bottleneck. Ideas:
		// 1) Change strategy of caching subscribersIds? The best case would be to use
		// 3th party providers? Redis? 
		// 2) Run it within separate thread?
		// 3) parallel stream?
		// 4) Perform load tests (Jmeter)
		// 5) Is it a good place? Take into account SOLID.
		try {
			enqueueMessages(device, event.getSubject());
		} catch (AmqpException e) {
			log.error(e.getMessage(), e);
		}

		return eventRepository.save(event);
	}

	/**
	 * Enqueues {@link EventNotification} messages to external message-broker server
	 * using {@link ExecutorService} framework.
	 * 
	 * @param device
	 * @param subject
	 * @throws AmqpException
	 */
	private void enqueueMessages(final ESP8266Device device, final String subject) throws AmqpException {
		
		Runnable task = () -> {
			device.getSubscribersIds().stream()
			.forEach(subscriberId -> batchingRabbitTemplate.convertAndSend(
					RabbitMQConfiguration.EVENT_NOTIFICATION_QUEUE_NAME,
					new EventNotification(subscriberId, subject)));
		};
		
		executorService.execute(task);
	}

	/**
	 * <p>
	 * Persists a set of tags supposed to be transient. Checks whether an instance
	 * of tag exists within database. Finally, it ties the set with a 'parent' event.
	 * </p>
	 * 
	 * @param an event with transient state
	 */
	private void persistTags(Event event, Set<Tag> transientTags) {

		// do nothing when an event has no tags
		if (transientTags.size() != 0) {

			// it'll contain persistent tags
			Set<Tag> tags = new HashSet<>();
			for (Tag transientTag : transientTags) {
				Optional<Tag> t = tagRepository.findByName(transientTag.getName());
				if (!t.isPresent())
					tags.add(tagRepository.save(transientTag));
				else
					tags.add(t.get()); // transient -> persist
			}
			// updates persistent tags
			event.setTags(tags);
		}

	}

}
