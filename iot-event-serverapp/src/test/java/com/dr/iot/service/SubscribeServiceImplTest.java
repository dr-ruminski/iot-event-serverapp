package com.dr.iot.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.dr.iot.controller.e.WrongSubscriptionDataException;
import com.dr.iot.model.ESP8266Device;
import com.dr.iot.model.Subscriber;
import com.dr.iot.model.subscription.Subscription;
import com.dr.iot.model.subscription.SubscriptionInData;

@SpringBootTest
@RunWith(SpringRunner.class)
class SubscribeServiceImplTest {


	@Autowired
	private ISubscriptionService subscriptionService;

	private ESP8266Device device;
	private Subscriber subscriber;
	private SubscriptionInData subscriptionInData;

	private static boolean dataLoaded = false;

	@BeforeEach
	public void setUp() throws Exception {

		// intentional workaround in order to initialize db data only once
		if (!dataLoaded) {
			device = new ESP8266Device();
			device.setId(1000l);// the same as exists in db (initial insert)
			device.setName("ESP_X1230"); 

			subscriber = new Subscriber();
			subscriber.setId(1001l);

			dataLoaded = true;
			subscriptionInData = new SubscriptionInData();
			subscriptionInData.setSubscriberId("1001");
			subscriptionInData.setDeviceName("ESP_X1230");
		}
	}

	@Test
	void subscribe_ThrowsException_ifInvalidInData() {
		// null, null
		SubscriptionInData data = new SubscriptionInData();
		WrongSubscriptionDataException e = assertThrows(WrongSubscriptionDataException.class,
				() -> subscriptionService.subscribe(data));
		assertTrue(e instanceof WrongSubscriptionDataException);
		
		// 234, null
		data.setDeviceName("234"); // invalids name
		e = assertThrows(WrongSubscriptionDataException.class, () -> subscriptionService.subscribe(data));
		assertTrue(e instanceof WrongSubscriptionDataException);
		
		// null, mock device
		data.setDeviceName(null);
		e = assertThrows(WrongSubscriptionDataException.class, () -> subscriptionService.subscribe(data));
		assertTrue(e instanceof WrongSubscriptionDataException);
		
	}
	
	
	@Test
	void subscribe_WithSuccess() {
		
		Subscription s = subscriptionService.subscribe(subscriptionInData);
		assertTrue(s.getId() != null);
	}
}

