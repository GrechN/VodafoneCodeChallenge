package com.VodafoneCodeChallenge.CustomerDatabase.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubscriberNotFoundException extends RuntimeException {

	public SubscriberNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
