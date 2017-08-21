package com.entuition.lambda.authentication;

public class RegisterResponse {
	String result;
	String userid;
	
	public RegisterResponse() {}
	
	public RegisterResponse(String result, String userid) {
		this.result = result;
		this.userid = userid;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
}
