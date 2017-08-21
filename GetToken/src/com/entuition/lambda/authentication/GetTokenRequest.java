package com.entuition.lambda.authentication;

public class GetTokenRequest {
	
	String uid;
	String signature;
	String timestamp;
	String identityId;
	String loginString;
	
	public GetTokenRequest() {}
	
	public GetTokenRequest(String uid, String signature, String timestamp, String identityId, String loginString) {
		this.uid = uid;
		this.signature = signature;
		this.timestamp = timestamp;
		this.identityId = identityId;
		this.loginString = loginString;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public String getLoginString() {
		return loginString;
	}

	public void setLoginString(String loginString) {
		this.loginString = loginString;
	}
}
