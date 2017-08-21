package com.entuition.lambda.authentication;

public class LoginRequest {
	
	String username;
	String timestamp;
	String signature;
	String uid;
	
	public LoginRequest() { }
	
	public LoginRequest(String username, String timestamp, String signature, String uid) {
		this.username = username;
		this.timestamp = timestamp;
		this.signature = signature;
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
}
