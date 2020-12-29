package com.dr.iot.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.dr.iot.controller.e.WrongDeviceException;
import com.dr.iot.model.ESP8266Device;
import com.dr.iot.model.Event;
import com.dr.iot.model.Tag;
import com.dr.iot.repository.DeviceRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EventServiceImplTest {

	private ESP8266Device device;
	private Event event;
	private Set<Tag> tags;

	@Autowired
	private IEventService eventService;

	@Autowired
	private DeviceRepository deviceRepo;

	@BeforeEach
	public void setUp() throws Exception {
		device = new ESP8266Device();
		device.setName(generateUniqueName());
		deviceRepo.save(device);

		event = new Event();
		event.setContent("content");
		event.setSubject("subject");
		event.setDevice(device);

		Tag t1 = new Tag();
		t1.setName("temp");
		Tag t2 = new Tag();
		t2.setName("relay");

		tags = Stream.of(t1, t2).collect(Collectors.toSet());

	}

	@Test
	void creatingEventWithoutTags() {
		Event eventFromRepo = eventService.createEvent(event);
		assertNotNull(eventFromRepo);
	}

	@Test
	void creatingEventWithTags() {
		event.setTags(tags);
		Event eventFromRepo = eventService.createEvent(event);
		assertNotNull(eventFromRepo);

		Set<Tag> tagsFromRepo = eventFromRepo.getTags();
		assertNotNull(tagsFromRepo);
		assertEquals(2, tagsFromRepo.size());
	}


	@Test
	void thrwoingDataIntegrityViolation_whenCreatingEventFor_forInvalidUser() {
		ESP8266Device nonExistentDevice = new ESP8266Device();
		nonExistentDevice.setId(1213l);
		nonExistentDevice.setName("non-existent-device");
		final Event event = new Event();
		event.setContent("contentTags");
		event.setSubject("subjectTags");
		event.setDevice(nonExistentDevice);
		eventService.createEvent(event);
		WrongDeviceException e = assertThrows(
				WrongDeviceException.class, () -> eventService.createEvent(event)
		);
		
		assertTrue(e instanceof WrongDeviceException);
	}


	private String generateUniqueName() {
		byte[] array = new byte[7]; // length is bounded by 7
		new Random().nextBytes(array);
		return new String(array, Charset.forName("UTF-8"));
	}

}
