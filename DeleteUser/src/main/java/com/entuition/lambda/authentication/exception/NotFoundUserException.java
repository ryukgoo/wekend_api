package com.entuition.lambda.authentication.exception;

public class NotFoundUserException extends Exception {

	private static final long serialVersionUID = 1L;

	public NotFoundUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundUserException(String message) {
		super(message);
	}
}
