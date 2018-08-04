package com.VodafoneCodeChallenge.CustomerDatabase.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MsisdnExistsException extends RuntimeException {

	public MsisdnExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
