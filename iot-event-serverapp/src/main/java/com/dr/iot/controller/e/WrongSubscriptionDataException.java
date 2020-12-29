package com.dr.iot.controller.e;

public class WrongSubscriptionDataException extends RuntimeException {

	public WrongSubscriptionDataException() {
		super("Empty data.");
	}
	
	public WrongSubscriptionDataException(String data) {
		super("Wrong subscription data: "+ data);
	}
}
