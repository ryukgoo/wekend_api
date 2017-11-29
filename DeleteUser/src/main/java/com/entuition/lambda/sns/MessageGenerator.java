package com.entuition.lambda.sns;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageGenerator {

	public static final String defaultMessage = "This is default message";
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public static enum Platform {
		// Apple Push Notification Service
		APNS,
		// Sandbox version of Apple Push Notification Service
		APNS_SANDBOX,
		// Amazon Device Messaging
		ADM,
		// Google Cloud Messaging
		GCM,
		// Baidu CloudMessaging Service
		BAIDU,
		// Windows Notification Service
		WNS,
		// Microsoft Push Notificaion Service
		MPNS;
	}
	
	public static String jsonify(Object message) {
		try {
			return objectMapper.writeValueAsString(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw (RuntimeException) e;
		}
	}
	
	private static Map<String, String> getData(String message) {
		Map<String, String> payload = new HashMap<String, String>();
		payload.put("message", message);
		return payload;
	}
	
	public static String getAppleMessage(String body, String type, int productId, String userId, int badge) {
		Map<String, Object> appleMessageMap = new HashMap<String, Object>();
		Map<String, Object> appMessageMap = new HashMap<String, Object>();
		appMessageMap.put("content-available", "1");
		appMessageMap.put("alert", body);
		appMessageMap.put("badge", badge);
		appMessageMap.put("sound", "default");
		appMessageMap.put("priority", "10");
		appleMessageMap.put("aps", appMessageMap);
		appleMessageMap.put("type", type);
		appleMessageMap.put("productId", productId);
		appleMessageMap.put("userId", userId);
		return jsonify(appleMessageMap);
	}
	
	public static String getAndroidMessage(String body, String type, int productId, int badge) {
		String message = type + "||" + body + "||" + productId + "||" + badge;
		Map<String, Object> androidMessageMap = new HashMap<String, Object>();
		androidMessageMap.put("collapse_key", "Welcome");
		androidMessageMap.put("data", getData(message));
		androidMessageMap.put("delay_while_idle", false);
		androidMessageMap.put("time_to_live", 125);
		androidMessageMap.put("dry_run", false);
		return jsonify(androidMessageMap);
	}
	
}
