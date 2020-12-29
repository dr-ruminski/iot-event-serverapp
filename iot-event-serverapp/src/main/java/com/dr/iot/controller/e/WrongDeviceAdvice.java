package com.dr.iot.controller.e;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WrongDeviceAdvice {

	@ResponseBody
	@ExceptionHandler(WrongDeviceException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	String eventNotFoundHandler(EventNotFoundException e) {
		return e.getMessage();
	}
}
