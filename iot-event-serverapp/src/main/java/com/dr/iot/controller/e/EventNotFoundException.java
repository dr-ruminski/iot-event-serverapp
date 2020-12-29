package com.dr.iot.controller.e;

import com.dr.iot.service.EventServiceImpl;

/**
 * An exception thrown an instance of {@link EventServiceImpl} cannot find a particular
 * event within a database.
 * 
 * @author dr
 *
 */
public class EventNotFoundException extends RuntimeException {

	public EventNotFoundException(Long id) {
		super("There is no event with the following id:" + id);
	}
}
