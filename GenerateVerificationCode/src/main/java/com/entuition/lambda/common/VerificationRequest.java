package com.entuition.lambda.common;

public class VerificationRequest {

	String phone;

	public VerificationRequest() {}
	
	public VerificationRequest(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
