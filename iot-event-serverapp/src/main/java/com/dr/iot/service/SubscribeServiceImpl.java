package com.dr.iot.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dr.iot.controller.e.WrongSubscriptionDataException;
import com.dr.iot.model.ESP8266Device;
import com.dr.iot.model.Subscriber;
import com.dr.iot.model.subscription.Subscription;
import com.dr.iot.model.subscription.SubscriptionInData;
import com.dr.iot.repository.DeviceRepository;
import com.dr.iot.repository.SubscriberRepository;
import com.dr.iot.repository.SubscriptionRepository;
import com.google.common.primitives.Longs;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
public class SubscribeServiceImpl implements ISubscriptionService {

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	private SubscriberRepository subscriberRepository;

	@Autowired
	private DeviceRepository deviceRepository;

	@Override
	public Subscription subscribe(SubscriptionInData data) {

		// sanity check
		if (data == null || data.getDeviceName() == null || data.getSubscriberId() == null)
			throw new WrongSubscriptionDataException();

		// retrieving subscribers
		Subscriber subscriber = evaluateSubscriber(data.getSubscriberId());
		ESP8266Device device = evaluateDevice(data.getDeviceName());

		Subscription subscription = new Subscription();
		subscription.setSubscriber(subscriber);
		subscription.setSubscribedDevice(device);

		// persisting the subscription
		subscription = subscriptionRepository.save(subscription);
		log.info("A subscription has been created: {}", subscription);

		return subscription;
	}

	@Override
	public ResponseEntity<String> deleteSubscriptionBy(Long subscriptionId) {
		try {
			subscriptionRepository.deleteById(subscriptionId);
			log.info("The subscription with id:{} has been deleted:", subscriptionId);
			return RESP_WHEN_DELETED_SUB;
		} catch (EmptyResultDataAccessException e) {
			return RESP_WHEN_UNABLE_TO_DELETE_SUB;
		}

	}

	/**
	 * <p>
	 * Evaluates incoming subscriber data. First, it converts the subscriber's id to
	 * {@link Long}. Second, it retrieves data of the subscriber.
	 * </p>
	 * 
	 * @param subscriberId
	 * @return an instance of {@link Subscriber}
	 * @throws WrongSubscriptionDataException if {@literal subscriberId} cannot be
	 *                                        found or is {@literal null}.
	 */
	private Subscriber evaluateSubscriber(String subscriberIdStr) {
		Long subscriberId = Optional.ofNullable(subscriberIdStr).map(Longs::tryParse)
				.orElseThrow(() -> new WrongSubscriptionDataException(subscriberIdStr));

		return subscriberRepository.findById(subscriberId)
				.orElseThrow(() -> new WrongSubscriptionDataException(subscriberIdStr));
	}

	/**
	 * <p>
	 * Evaluates the device's name by checking whether the device exists within a
	 * repository.
	 * </p>
	 * 
	 * @param deviceName
	 * @return an instance of {@link ESP8266Device}
	 * @throws WrongSubscriptionDataException if {@literal userId} cannot be found
	 *                                        or is {@literal null}.
	 */
	private ESP8266Device evaluateDevice(String deviceName) {
		return deviceRepository.findDeviceByName(deviceName)
				.orElseThrow(() -> new WrongSubscriptionDataException(deviceName));
	}
}
