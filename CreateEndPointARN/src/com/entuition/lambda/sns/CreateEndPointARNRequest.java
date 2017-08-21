package com.entuition.lambda.sns;

public class CreateEndPointARNRequest {
	
	private String userId;
	private String snsToken;
	private String platform;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSnsToken() {
		return snsToken;
	}
	public void setSnsToken(String snsToken) {
		this.snsToken = snsToken;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
}
