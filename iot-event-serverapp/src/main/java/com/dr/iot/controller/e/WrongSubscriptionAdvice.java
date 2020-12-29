package com.dr.iot.controller.e;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WrongSubscriptionAdvice {

	@ResponseBody
	@ExceptionHandler(WrongSubscriptionDataException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public String wrongSubscriptionHandler(WrongSubscriptionDataException e) {
		return e.getMessage();
	}
}
