package com.entuition.lambda.authentication;

public class LoginResponse {
	
	String key;
	String enable;
	String userid;
	
	public LoginResponse() { }
	
	public LoginResponse(String key, String enable) {
		this.key = key;
		this.enable = enable;
	}
	
	public LoginResponse(String key, String enable, String userid) {
		this.key = key;
		this.enable = enable;
		this.userid = userid;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public String getEnable() {
		return enable;
	}
	
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
}