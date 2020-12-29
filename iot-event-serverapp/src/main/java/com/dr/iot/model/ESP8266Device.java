package com.dr.iot.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.dr.iot.model.subscription.Subscription;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Domain model representing an ESP 8266 device. It is bound by events (OneToMany)
 * and subscriptions (OneToMany) relationships. 
 * 
 * @author dr
 */

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
public class ESP8266Device extends BaseEntity {

	@Column(name = "name", nullable = true)
	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
	private List<Event> events = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "subscribedDevice")
	private List<Subscription> subscriptions = new ArrayList<>();

	/**
	 * Retrieves a list of subscribed users.
	 * @return list of subscribers ids
	 */
	public List<Long> getSubscribersIds() {
		return subscriptions.stream()
				.map(sub -> sub.getId())
				.collect(Collectors.toList());
	}
}
