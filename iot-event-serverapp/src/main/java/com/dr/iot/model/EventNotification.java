package com.dr.iot.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * POJO class used as a 'proxy' output data to the external event Notification
 * service.
 * 
 * @author dr
 *
 */
@Data
@SuppressWarnings("serial")
public class EventNotification implements Serializable {

	private final long subscriberId;
	private final String message;

	public EventNotification(@JsonProperty("subscriberId") long subscriberId, @JsonProperty("message") String message) {
		this.subscriberId = subscriberId;
		this.message = message;
	}
}
