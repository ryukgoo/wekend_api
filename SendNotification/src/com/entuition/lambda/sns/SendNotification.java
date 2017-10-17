package com.entuition.lambda.sns;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
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
import com.entuition.lambda.authentication.Configuration;
import com.entuition.lambda.authentication.UserAuthentication.UserInfo;
import com.entuition.lambda.authentication.Utilities;
import com.entuition.lambda.data.SendMail;
import com.entuition.lambda.sns.MessageGenerator.Platform;

/**
 * When receiveMailDB is updated..
 * @author ryukgoo
 *
 */
public class SendNotification implements RequestHandler<DynamodbEvent, Object> {
	
	private final String TAG = getClass().getSimpleName();
	
//	private static final String FILTER_KEY_RECEIVER_ID = ":ReceiverId";
//	private static final String FILTER_KEY_PRODUCT_ID = ":ProductId";

	private LambdaLogger logger;
	
	private final AmazonDynamoDBClient client;
	private final DynamoDBMapper mapper;
	private final AmazonSNSClientWrapper snsClientWrapper;
	
    public SendNotification() {
    	this.client = new AmazonDynamoDBClient();
    	this.client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
    	this.mapper = new DynamoDBMapper(client);
    	
    	AmazonSNSClient snsClient = new AmazonSNSClient();
    	snsClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
    	this.snsClientWrapper = new AmazonSNSClientWrapper(snsClient);
	}

	@Override
    public Object handleRequest(DynamodbEvent dynamodbEvent, Context context) {
		
		logger = context.getLogger();
        logger.log("Input: " + dynamodbEvent);
        logger.log("record's size : " + dynamodbEvent.getRecords().size());

        for (DynamodbStreamRecord record : dynamodbEvent.getRecords()) {
        	
        	logger.log(TAG + " > EventId : " + record.getEventID());
        	logger.log(TAG + " > EventName : " + record.getEventName());
        	logger.log(TAG + " > Dynamodb : " + record.getDynamodb());
        	
        	StreamRecord item = record.getDynamodb();
        	Map<String, AttributeValue> newImage = item.getNewImage();
        	Map<String, AttributeValue> oldImage = item.getOldImage();
        	
        	if (record.getEventName().equals("INSERT")) {	
        		sendMessage(newImage);
        	} else if (record.getEventName().equals("MODIFY")) {
        		
        		String oldStatus = oldImage.get(Configuration.ATTRIBUTE_MAIL_STATUS).getS();
        		String newStatus = newImage.get(Configuration.ATTRIBUTE_MAIL_STATUS).getS();
        		
        		logger.log("oldStatus : " + oldStatus);
        		logger.log("newStatus : " + newStatus);
        		
        		if (oldStatus.equals(Configuration.PROPOSE_STATUS_NOT_MADE)) {
        			if (newStatus.equals(Configuration.PROPOSE_STATUS_MADE)
        					|| newStatus.equals(Configuration.PROPOSE_STATUS_REJECT)) {
        				
        				logger.log("updateSendMail > newStatus : " + newStatus);            			
            			updateSendMail(newImage);        				
            			
        			}
        		}
        	}
        }
        
        return null;
    }
	
	private void sendMessage(Map<String, AttributeValue> newImage) {
		String receiverId = newImage.get(Configuration.ATTRIBUTE_MAIL_USER_ID).getS();
    	String status = newImage.get(Configuration.ATTRIBUTE_MAIL_STATUS).getS();
    	String senderNickname = newImage.get(Configuration.ATTRIBUTE_MAIL_SENDER_NICKNAME).getS();
    	
    	logger.log(TAG + " > receiverId : " + receiverId);
    	logger.log(TAG + " > status : " + status);
    	logger.log(TAG + "> senderNickname : " + senderNickname);
    	
    	String body = "";
    	
    	try {
        	
        	switch (status) {
	        	case Configuration.PROPOSE_STATUS_NOT_MADE :
	        		body = senderNickname + "님으로부터 함께가기 신청이 왔습니다.";
	        		break;
	        	case Configuration.PROPOSE_STATUS_MADE :
//	        		message += senderNickname + "님과 함께가기가 성공하였습니다.";
//	        		endPointArn = mapper.load(UserInfo.class, senderId).getEndpointARN();
//	        		break;
	        	case Configuration.PROPOSE_STATUS_REJECT :
	        	case Configuration.PROPOSE_STATUS_ALREADY_MADE :
	        	case Configuration.PROPOSE_STATUS_DELETE :
	        		return;
        		default :
        			break;
        	}
        	
        	logger.log(TAG + " > message : " + body);
    		
        	UserInfo userInfo = mapper.load(UserInfo.class, receiverId);
        	String endpointArn = userInfo.getEndpointARN();
        	
        	int newLikeCount = userInfo.getNewLikeCount();
    		int newSendCount = userInfo.getNewSendCount();
    		int newReceiveCount = userInfo.getNewReceiveCount();
    		
    		if (!StringUtils.isNullOrEmpty(endpointArn) && !endpointArn.equals("EndPoint")) {
    			
    			++newReceiveCount;
    			int totalNotificationCount = newLikeCount + newSendCount + newReceiveCount;
    		
	    		Map<Platform, Map<String, MessageAttributeValue>> attrsMap = new HashMap<Platform, Map<String, MessageAttributeValue>>();
				
				if (endpointArn.startsWith("arn:aws:sns:ap-northeast-1:039291388167:endpoint/GCM/Wekend/")) {
					
					logger.log(TAG + " > Platform is Android");
					
					attrsMap.put(Platform.GCM, null);
					String gcmMessage = MessageGenerator.getAndroidMessage(body, Configuration.TYPE_NOTIFICATION_RECEIVE_MAIL, 0, totalNotificationCount);
					snsClientWrapper.notification(endpointArn, Platform.GCM, attrsMap, gcmMessage);
				} else {
					
					logger.log(TAG + " > Platform is iOS");
					
//					attrsMap.put(Platform.APNS_SANDBOX, null);
					attrsMap.put(Platform.APNS, null);
					String apnsMessage = MessageGenerator.getAppleMessage(body, Configuration.TYPE_NOTIFICATION_RECEIVE_MAIL, 0, receiverId, totalNotificationCount);
//					snsClientWrapper.notification(endpointArn, Platform.APNS_SANDBOX, attrsMap, apnsMessage);
					snsClientWrapper.notification(endpointArn, Platform.APNS, attrsMap, apnsMessage);
				}
	    		
				userInfo.setNewLikeCount(newLikeCount);
        		userInfo.setNewSendCount(newSendCount);
        		userInfo.setNewReceiveCount(newReceiveCount);
        		
        		mapper.save(userInfo);
				
	    		/*
	    		if (status.equals(Configuration.PROPOSE_STATUS_MADE)) {
	    			
	    			SendMail sendMail = new SendMail();
	    			sendMail.setUserId(senderId);
	    			
	    			String filterExpression = Configuration.ATTRIBUTE_MAIL_RECEIVER_ID + " = " + FILTER_KEY_RECEIVER_ID
	    					+ " and " + Configuration.ATTRIBUTE_MAIL_PRODUCT_ID + " <> " + productId;
	    			Map<String, AttributeValue> filterMap = new HashMap<String, AttributeValue>();
	    			filterMap.put(FILTER_KEY_RECEIVER_ID, new AttributeValue().withS(receiverId));
	    			filterMap.put(FILTER_KEY_PRODUCT_ID, new AttributeValue().withN(productId));
	    			
	    			DynamoDBQueryExpression<SendMail> queryExpression = new DynamoDBQueryExpression<SendMail>()
	    					.withHashKeyValues(sendMail)
	    					.withFilterExpression(filterExpression)
	    					.withExpressionAttributeValues(filterMap)
	    					.withConsistentRead(false);
	    			
	    			List<SendMail> sendMailList = mapper.query(SendMail.class, queryExpression);
	    			for (SendMail sendMailItem : sendMailList) {
	    				sendMailItem.setStatus(Configuration.PROPOSE_STATUS_ALREADY_MADE);
	    				mapper.save(sendMailItem);
	    			}	
	    		}
	    		*/
    		}
    		
    	} catch (AmazonClientException e) {
    		logger.log(TAG + " > Exception : " + e.getMessage());
    	} catch (Exception e) {
    		logger.log(TAG + " > Exception : " + e.getMessage());
    	}
	}
	
	private void updateSendMail(Map<String, AttributeValue> newImage) {
		String senderId = newImage.get(Configuration.ATTRIBUTE_MAIL_SENDER_ID).getS();
		String receiverId = newImage.get(Configuration.ATTRIBUTE_MAIL_USER_ID).getS();
		String updatedTime = newImage.get(Configuration.ATTRIBUTE_UPDATED_TIME).getS();
		int productId = Integer.parseInt(newImage.get(Configuration.ATTRIBUTE_MAIL_PRODUCT_ID).getN());
		String productTitle = newImage.get(Configuration.ATTRIBUTE_MAIL_PRODUCT_TITLE).getS();
		String senderNickname = newImage.get(Configuration.ATTRIBUTE_MAIL_SENDER_NICKNAME).getS();
		String receiverNickname = newImage.get(Configuration.ATTRIBUTE_MAIL_RECEIVER_NICKNAME).getS();
		String status = newImage.get(Configuration.ATTRIBUTE_MAIL_STATUS).getS();
		
		if (status.equals(Configuration.PROPOSE_STATUS_NOT_MADE)) return;
		
		SendMail mail = new SendMail();
		mail.setUserId(senderId);
		mail.setReceiverId(receiverId);
		mail.setUpdatedTime(updatedTime);
		mail.setResponseTime(Utilities.getTimestamp());
		mail.setProductId(productId);
		mail.setProductTitle(productTitle);
		mail.setSenderNickname(senderNickname);
		mail.setReceiverNickname(receiverNickname);
		mail.setStatus(status);
		mail.setIsRead(Configuration.STATUS_MAIL_UNREAD);
		
		try {
    		mapper.save(mail);
    	} catch (AmazonClientException e) {
    		logger.log(TAG + " : " + e.getMessage());
    	} catch (Exception e) {
    		logger.log(TAG + " : " + e.getMessage());
    	}
	}

}
