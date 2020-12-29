package com.dr.iot.controller.e;

public class WrongDeviceException extends RuntimeException {

	public WrongDeviceException(Long userId) {
		super("Wrong device id passed into the json object:" + userId);
	}
}
