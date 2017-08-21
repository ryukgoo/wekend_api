package com.entuition.lambda.authentication;

public class AuthenticateUserResponse {
	
	String openIdToken;
	String status;
	
	public AuthenticateUserResponse() {}
	
	public AuthenticateUserResponse(String openIdToken, String status) {
		this.openIdToken = openIdToken;
		this.status = status;
	}

	public String getOpenIdToken() {
		return openIdToken;
	}

	public void setOpenIdToken(String openIdToken) {
		this.openIdToken = openIdToken;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
