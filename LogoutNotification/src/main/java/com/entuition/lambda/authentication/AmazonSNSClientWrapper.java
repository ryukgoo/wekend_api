package com.entuition.lambda.authentication;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreatePlatformApplicationRequest;
import com.amazonaws.services.sns.model.CreatePlatformApplicationResult;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.DeleteEndpointRequest;
import com.amazonaws.services.sns.model.GetEndpointAttributesRequest;
import com.amazonaws.services.sns.model.GetEndpointAttributesResult;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SetEndpointAttributesRequest;
import com.amazonaws.services.sns.model.SetEndpointAttributesResult;
import com.amazonaws.util.StringUtils;
import com.entuition.lambda.authentication.MessageGenerator.Platform;

public class AmazonSNSClientWrapper {

	private static final String KEY_PLATFORM_PRINCIPAL = "PlatformPrincipal";
	private static final String KEY_PLATFORM_CREDENTIAL = "PlatformCredential";
	private static final String KEY_USERID = "UserId";
	private static final String KEY_CHANNELID = "ChannelId";
	
	private final AmazonSNS client;

	public AmazonSNSClientWrapper(AmazonSNS client) {
		this.client = client;
	}
	
	private CreatePlatformApplicationResult createPlatformApplication(String applicationName, 
			Platform platform, String principal, String credential) {
		
		CreatePlatformApplicationRequest platformApplicationRequest = new CreatePlatformApplicationRequest();
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put(KEY_PLATFORM_PRINCIPAL, principal);
		attributes.put(KEY_PLATFORM_CREDENTIAL, credential);
		platformApplicationRequest.setAttributes(attributes);
		platformApplicationRequest.setName(applicationName);
		platformApplicationRequest.setPlatform(platform.name());
		return client.createPlatformApplication(platformApplicationRequest);
	}
	
	private CreatePlatformEndpointResult createPlatformEndpoint(Platform platform,
			String customData, String platformToken, String applicationArn) {
		
		CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest();
		platformEndpointRequest.setCustomUserData(customData);
		String token = platformToken;
		String userId = null;
		
		if (platform == Platform.BAIDU) {
			String[] tokenBits = platformToken.split("\\|");
			token = tokenBits[0];
			userId = tokenBits[1];
			Map<String, String> endpointAttributes = new HashMap<String, String>();
			endpointAttributes.put(KEY_USERID, userId);
			endpointAttributes.put(KEY_CHANNELID, token);
			platformEndpointRequest.setAttributes(endpointAttributes);
		}
		platformEndpointRequest.setToken(token);
		platformEndpointRequest.setPlatformApplicationArn(applicationArn);
		return client.createPlatformEndpoint(platformEndpointRequest);
	}
	
	private PublishResult publish(String endpointArn, Platform platform,
			Map<Platform, Map<String, MessageAttributeValue>> attributesMap, String message) {
		
		PublishRequest publishRequest = new PublishRequest();
		Map<String, MessageAttributeValue> notificationAttributes = getValidNotificationAttributes(attributesMap.get(platform));
		
		if (notificationAttributes != null && !notificationAttributes.isEmpty()) {
			publishRequest.setMessageAttributes(notificationAttributes);
		}
		publishRequest.setMessageStructure("json");
		
//		String platformMessage = getPlatformSampleMessage(platform, message);
//		Map<String, String> messageMap = new HashMap<String, String>();
//		messageMap.put(platform.name(), platformMessage);
//		platformMessage = MessageGenerator.jsonify(messageMap);
		
		Map<String, String> messageMap = new HashMap<String, String>();
		messageMap.put(platform.name(), message);
		String platformMessage = MessageGenerator.jsonify(messageMap);
		
		publishRequest.setTargetArn(endpointArn);
		
		System.out.println("{Message Body : " + platformMessage + "}");
		StringBuilder builder = new StringBuilder();
		builder.append("{Message Attributes : ");
		for (Map.Entry<String, MessageAttributeValue> entry : notificationAttributes.entrySet()) {
			builder.append("(\"" + entry.getKey() + "\" : \"" + entry.getValue().getStringValue() + "\"),");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append("}");
		System.out.println(builder.toString());
		
		publishRequest.setMessage(platformMessage);
		return client.publish(publishRequest);
	}
	
	public void demoNotification(Platform platform, String principal, String credential,
			String platformToken, String applicationName, Map<Platform, Map<String, MessageAttributeValue>> attrsMap, String message) {
		
		CreatePlatformApplicationResult platformApplicationResult =
				createPlatformApplication(applicationName, platform, principal, credential);
		
		System.out.println(platformApplicationResult);
		
		String platformApplicationArn = platformApplicationResult.getPlatformApplicationArn();
		
		CreatePlatformEndpointResult platformEndpointResult = createPlatformEndpoint(
				platform,
				"CustomData - Useful to store endpoint specific data",
				platformToken,
				platformApplicationArn);
		System.out.println(platformApplicationResult);
		
		PublishResult publishResult = publish(platformEndpointResult.getEndpointArn(), platform, attrsMap, message);
		System.out.println("Published! \n{MessageId="
				+ publishResult.getMessageId() + "}");
		
		deletePlatformApplication(platformApplicationArn);
	}
	
	public void notification(String endpointArn, Platform platform, Map<Platform, Map<String, MessageAttributeValue>> attrsMap, String message) {
		PublishResult publishResult = publish(endpointArn, platform, attrsMap, message);
		System.out.println("Published! \n{MessageId="
				+ publishResult.getMessageId() + "}");
		
//		deletePlatformApplication(endpointArn);
	}
	
	public void deletePlatformApplication(String applicationArn) {
		try {
			DeleteEndpointRequest deleteEndpointRequest = new DeleteEndpointRequest();
			deleteEndpointRequest.setEndpointArn(applicationArn);
			client.deleteEndpoint(deleteEndpointRequest);
//			DeletePlatformApplicationRequest deleteRequest = new DeletePlatformApplicationRequest();
//			deleteRequest.setPlatformApplicationArn(applicationArn);
//			snsClient.deletePlatformApplication(deleteRequest);
		} catch (Exception e) {
			System.out.println("error : " + e.toString());
		}
	}
	
	public String getPlatformEndpointARN(Platform platform, String principal, String credential,
			String platformToken, String applicationName, String customData) {
		
		CreatePlatformApplicationResult platformApplicationResult =
				createPlatformApplication(applicationName, platform, principal, credential);
		
		System.out.println(platformApplicationResult);
		
		String platformApplicationArn = platformApplicationResult.getPlatformApplicationArn();
		
		CreatePlatformEndpointResult platformEndpointResult = createPlatformEndpoint(
				platform,
				customData,
				platformToken,
				platformApplicationArn);
		System.out.println(platformApplicationResult);
		
		return platformEndpointResult.getEndpointArn();
	}
	
	private Map<String, String> getEndpointArnFromSNS(String platformToken) {
		
		GetEndpointAttributesRequest getEndpointAttributesRequest = new GetEndpointAttributesRequest();
		getEndpointAttributesRequest.setEndpointArn(platformToken);
		
		GetEndpointAttributesResult getEndpointAttributesResult = client.getEndpointAttributes(getEndpointAttributesRequest);
		Map<String, String> attributes = getEndpointAttributesResult.getAttributes();
		return attributes;
	}
	
	private SetEndpointAttributesResult setEndpointArnToSNS(String platformToken) {
		SetEndpointAttributesRequest setEndpointAttributesRequest = new SetEndpointAttributesRequest();
		setEndpointAttributesRequest.setEndpointArn(platformToken);
		
		Map<String, String> attributes = new HashMap<String, String>();
		setEndpointAttributesRequest.setAttributes(attributes);
		
		return client.setEndpointAttributes(setEndpointAttributesRequest);
	}
	
	/*
	private String getPlatformSampleMessage(Platform platform, String message) {
		switch (platform) {
		case APNS :
			return MessageGenerator.getAppleMessage(message);
		case APNS_SANDBOX :
			return MessageGenerator.getAppleMessage(message);
		case GCM :
			return MessageGenerator.getAndroidMessage(message);
		case BAIDU :
			return "";
		case WNS :
			return "";
		case MPNS :
			return "";
		default :
			throw new IllegalArgumentException("Platform not supported : " + platform.name());
		}
	}
	*/
	
	public static Map<String, MessageAttributeValue> getValidNotificationAttributes(
			Map<String, MessageAttributeValue> notificationAttributes) {
		
		Map<String, MessageAttributeValue> validAttributes = new HashMap<String, MessageAttributeValue>();
		
		if (notificationAttributes != null) {
			for (Map.Entry<String, MessageAttributeValue> entry : notificationAttributes.entrySet()) {
				if (!StringUtils.isNullOrEmpty(entry.getValue().getStringValue())) {
					validAttributes.put(entry.getKey(), entry.getValue());
				}
			}
		}
		
		return validAttributes;
	}
	
}
