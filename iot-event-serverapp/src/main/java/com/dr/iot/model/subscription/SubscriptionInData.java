package com.dr.iot.model.subscription;

import org.springframework.stereotype.Component;

import com.dr.iot.service.ISubscriptionService;

import lombok.Data;

/**
 * Input data for the {@link ISubscriptionService} service. 
 * 
 * @author dr
 *
 */
@Data
@Component
public class SubscriptionInData {

	private String deviceName;				
	private String subscriberId;

}
