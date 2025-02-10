package com.rts.tap.exception;

public class EmailValidationException extends RuntimeException {
    public EmailValidationException(String message) {
        super(message);
    }
}