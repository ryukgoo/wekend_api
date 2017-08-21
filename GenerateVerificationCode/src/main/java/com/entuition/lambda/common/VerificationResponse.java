package com.entuition.lambda.common;

public class VerificationResponse {
	
	String verificationCode;

	public VerificationResponse() {}
	
	public VerificationResponse(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
}
