package com.dr.iot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dr.iot.controller.e.EventNotFoundException;
import com.dr.iot.model.Event;
import com.dr.iot.service.IEventService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping(path = "/events")
public class EventController {

	@Autowired
	private IEventService eventService;

	@GetMapping("/{eventId}")
	@ResponseStatus(HttpStatus.OK)
	public Event getEvent(@PathVariable Long eventId) {

		log.debug("get an event with the id: %s", eventId);
		return eventService.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Long createEvent(@RequestBody Event event) {
		log.debug("Create an event: %s", event);

		// creates a persistent instance of an event instance
		event = eventService.createEvent(event);

		return event.getId();
	}
}
