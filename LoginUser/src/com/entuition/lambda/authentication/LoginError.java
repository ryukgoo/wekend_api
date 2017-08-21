package com.entuition.lambda.authentication;

public class LoginError {
	
	String errorType;
	String requestId;
	String message;
	
	public LoginError(String errorType) {
		this.errorType = errorType;
	}

	public LoginError(String errorType, String message) {
		this.errorType = errorType;
		this.message = message;
	}

	public LoginError(String errorType, String requestId, String message) {
		this.errorType = errorType;
		this.requestId = requestId;
		this.message = message;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
