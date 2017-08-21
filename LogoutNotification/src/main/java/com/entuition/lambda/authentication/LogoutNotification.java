package com.entuition.lambda.authentication;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.StreamRecord;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.util.StringUtils;
import com.entuition.lambda.authentication.MessageGenerator.Platform;

public class LogoutNotification implements RequestHandler<DynamodbEvent, Object> {

	private final String TAG = getClass().getSimpleName();
	
	private static final String ATTRIBUTE_USERID = "userid";
	private static final String ATTRIBUTE_ENDPOINT_ARN = "EndpointARN";
	private static final String TYPE_NOTIFICATION_LOGOUT = "logout";
	
	private LambdaLogger logger;
	
	private final AmazonSNSClientWrapper snsClientWrapper;
	
    public LogoutNotification() {	
    	AmazonSNSClient snsClient = new AmazonSNSClient();
		snsClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
		this.snsClientWrapper = new AmazonSNSClientWrapper(snsClient);
	}

	@Override
    public Object handleRequest(DynamodbEvent dynamodbEvent, Context context) {
    	
		logger = context.getLogger();
        logger.log("Input: " + dynamodbEvent);
        logger.log("record's size : " + dynamodbEvent.getRecords().size());
        
        try {
        	
        	for (DynamodbStreamRecord record : dynamodbEvent.getRecords()) {
        		
        		logger.log(TAG + " > EventId : " + record.getEventID());
	        	logger.log(TAG + " > EventName : " + record.getEventName());
	        	logger.log(TAG + " > Dynamodb : " + record.getDynamodb());
	        	
	        	if (record.getEventName().equals("MODIFY")) {
	        		
	        		StreamRecord item = record.getDynamodb();
	        		Map<String, AttributeValue> oldImage = item.getOldImage();
	        		Map<String, AttributeValue> newImage = item.getNewImage();
	        		
	        		String oldEndPointARN = oldImage.get(ATTRIBUTE_ENDPOINT_ARN).getS();
	        		String newEndPointARN = newImage.get(ATTRIBUTE_ENDPOINT_ARN).getS();
	        		
	        		if (!oldEndPointARN.equals(newEndPointARN)) {
	        			String userId = oldImage.get(ATTRIBUTE_USERID).getS();
	        			sendLogoutNotification(oldEndPointARN, userId);
	        		}
	        	}
        	}
        	
        } catch (Exception e) {
        	logger.log(TAG + " > exception : " + e.getMessage());
        }
        
        return null;
    }

	private void sendLogoutNotification(String endPoint, String userId) {
		
		System.out.println("sendLogoutNotification > endPoint : " + endPoint);
		System.out.println("sendLogoutNotification > userId : " + userId);
		
		if (!StringUtils.isNullOrEmpty(endPoint) && !endPoint.equals("EndPoint")) {
			
			Map<Platform, Map<String, MessageAttributeValue>> attrsMap =
					new HashMap<Platform, Map<String, MessageAttributeValue>>();
			
			// For GCM(Android)
    		if (endPoint.startsWith("arn:aws:sns:ap-northeast-1:039291388167:endpoint/GCM/Wekend/")) {
    			attrsMap.put(Platform.GCM, null);
    			String gcmMessage = MessageGenerator.getAndroidMessage("나가", TYPE_NOTIFICATION_LOGOUT, 0, 0);
    			snsClientWrapper.notification(endPoint, Platform.GCM, attrsMap, gcmMessage);
    		}
    		// For iOS
    		else {
    			attrsMap.put(Platform.APNS_SANDBOX, null);
    			String apnsMssage = MessageGenerator.getAppleMessage("나가", TYPE_NOTIFICATION_LOGOUT, 0, userId, 0);
    			snsClientWrapper.notification(endPoint, Platform.APNS_SANDBOX, attrsMap, apnsMssage);
    		}
    		
//    		snsClientWrapper.deletePlatformApplication(endPoint);
		}
	}
	
}
