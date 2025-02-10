package com.rts.tap.exception;

public class EmailCheckException extends RuntimeException {
	public EmailCheckException(String message) {
		super(message);
	}

	public EmailCheckException(String message, Throwable cause) {
		super(message, cause);
	}
}