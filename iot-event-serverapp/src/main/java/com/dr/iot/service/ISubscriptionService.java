package com.dr.iot.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dr.iot.model.subscription.Subscription;
import com.dr.iot.model.subscription.SubscriptionInData;

@Service
public interface ISubscriptionService {

	public static final ResponseEntity<String> RESP_WHEN_UNABLE_TO_DELETE_SUB = new ResponseEntity<String>(
			"Unable to delete subscription. No subscriptions is associated with a given subscriptionId",
			HttpStatus.NOT_FOUND);
	public static final ResponseEntity<String> RESP_WHEN_DELETED_SUB = new ResponseEntity<String>(
			"Deleted subscription", HttpStatus.OK);

	/**
	 * <p>
	 * Subscribes a subscriber to the IoT device.
	 * </p>
	 * 
	 * @param an instance of {@link SubscriptionInData} describing the subscriber's id and device's name.
	 * @return an instance of {@link Subscription}
	 */
	public Subscription subscribe(SubscriptionInData data);

	/**
	 * <p>
	 * Deletes an instance of subscription from a database using the subscription
	 * id.
	 * </p>
	 * 
	 * @param subscriptionId
	 * @return an instance of {@link ResponseEntity} indicating success or failure
	 *         operation when deleting the subscription.
	 */
	public ResponseEntity<String> deleteSubscriptionBy(Long subscriptionId);

}
