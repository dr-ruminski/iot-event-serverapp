package com.dr.iot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dr.iot.model.subscription.Subscription;
import com.dr.iot.model.subscription.SubscriptionInData;
import com.dr.iot.service.ISubscriptionService;

@RestController
@RequestMapping(path = "/subscriptions")
public class SubscriptionController {

	@Autowired
	private ISubscriptionService subscriptionService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Subscription subscribe(@RequestBody SubscriptionInData data) {
		return subscriptionService.subscribe(data);
	}

	@DeleteMapping(path = "/{subscriptionId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> unsubscribe(@PathVariable Long subscriptionId) {
		return subscriptionService.deleteSubscriptionBy(subscriptionId);
	}

}
