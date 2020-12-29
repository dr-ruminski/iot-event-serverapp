package com.dr.iot.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dr.iot.model.Event;

@Service
public interface IEventService {

	/**
	 * Gets an instance of Event type using an event's id. In case, DAO cannot find the
	 * proper object it return optional.
	 * 
	 * @param event's id
	 * @return Optional
	 */
	public Optional<Event> findById(Long eventId);

	/**
	 * Creates a persistent Event instance augmented with tags. 
	 * @param newEvent - transient instance of Event type
	 * @return an instance of Event
	 */
	public Event createEvent(Event newEvent);

}
