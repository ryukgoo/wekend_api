package com.entuition.lambda.sns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
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
import com.entuition.lambda.data.LikeDBItem;
import com.entuition.lambda.data.ProductLikeTime;
import com.entuition.lambda.sns.MessageGenerator.Platform;

public class RecommendFriendNotification implements RequestHandler<DynamodbEvent, Object> {
	
	private final String TAG = getClass().getSimpleName();
	
	private static final String FILTER_KEY_LIKE_GENDER = ":gender";
	
	private LambdaLogger logger;
	
	private final AmazonDynamoDBClient client;
	private final DynamoDBMapper mapper;
	private final AmazonSNSClientWrapper snsClientWrapper;
	
    public RecommendFriendNotification() {
		this.client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
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
        	
        	if (record.getEventName().equals("INSERT")) {
        		
	        	StreamRecord item = record.getDynamodb();
	        	Map<String, AttributeValue> newImage = item.getNewImage();
	        	
	        	String userId = newImage.get(Configuration.ATTRIBUTE_LIKE_USER_ID).getS();
	        	int productId = Integer.parseInt(newImage.get(Configuration.ATTRIBUTE_LIKE_PRODUCT_ID).getN());
	        	String gender = newImage.get(Configuration.ATTRIBUTE_LIKE_GENDER).getS();
	        	
	        	logger.log(TAG + " > productId : " + productId);
	        	logger.log(TAG + " > gender : " + gender);
	        	
	        	updateProductLikeTime(productId, gender);
	        	
	        	List<LikeDBItem> friendsList = getLikedFriendList(productId, gender);
	        	sendRecommendNotification(friendsList);
	        	
	        	if (friendsList.size() > 0) sendSelfNotification(productId, userId);
        	}
        }
        
        return null;
    }
	
	private void updateProductLikeTime(int productId, String gender) {
		
		logger.log(TAG + " > updateProductLikeTime > productId : " + productId + ", gender : " + gender);
		
		String timestamp = Utilities.getTimestamp();
		
		try {
			ProductLikeTime likeTime = mapper.load(ProductLikeTime.class, productId);
			if (likeTime == null) {
				likeTime = new ProductLikeTime();
				likeTime.setMaleLikeTime(timestamp);
				likeTime.setFemaleLikeTime(timestamp);
			}
			
			likeTime.setProductId(productId);
			
	    	if (gender.equals("male")) {
	    		likeTime.setMaleLikeTime(timestamp);
	    	} else {
	    		likeTime.setFemaleLikeTime(timestamp);
	    	}
	    	
	    	mapper.save(likeTime);
		} catch (Exception e) {
			logger.log("updateProductLikeTime > error : " + e.toString());
		}
	}
	
	private List<LikeDBItem> getLikedFriendList(int productId, String gender) {
		LikeDBItem likeDBItem = new LikeDBItem();
		likeDBItem.setProductId(productId);
		
		String filterExpression = Configuration.ATTRIBUTE_LIKE_GENDER + " <> " + FILTER_KEY_LIKE_GENDER;
		Map<String, AttributeValue> filterKeyMap = new HashMap<String, AttributeValue>();
		filterKeyMap.put(FILTER_KEY_LIKE_GENDER, new AttributeValue().withS(gender));
		
		try {
			DynamoDBQueryExpression<LikeDBItem> queryExpression = new DynamoDBQueryExpression<LikeDBItem>()
					.withIndexName(Configuration.INDEX_LIKE_PRODUCTID_UPDATEDTIME)
					.withHashKeyValues(likeDBItem)
					.withFilterExpression(filterExpression)
					.withExpressionAttributeValues(filterKeyMap)
					.withConsistentRead(false)
					.withScanIndexForward(false);
			
//			int count = mapper.count(LikeDBItem.class, queryExpression);
//			logger.log("getLikedFriendList > count : " + count);
			
			List<LikeDBItem> friendsList = mapper.query(LikeDBItem.class, queryExpression);
			return friendsList;
			
		} catch (Exception e) {
			logger.log("getLikedFriendList > exception : " + e.getMessage());
		}
		
		return null;
	}
	
	private void sendRecommendNotification(List<LikeDBItem> friendsList) {
		for (int i = 0 ; i < friendsList.size() ; i++) {
    		LikeDBItem likeDBItem = friendsList.get(i);
    		
    		String userId = likeDBItem.getUserId();
    		UserInfo userInfo = mapper.load(UserInfo.class, userId);
    		String endpointArn = userInfo.getEndpointARN();
    		
    		int newLikeCount = userInfo.getNewLikeCount();
    		int newSendCount = userInfo.getNewSendCount();
    		int newReceiveCount = userInfo.getNewReceiveCount();
    		
    		logger.log(TAG + " > index : " + i + " > endpointArn : " + endpointArn);
    		
    		if (!StringUtils.isNullOrEmpty(endpointArn) && !endpointArn.equals("EndPoint")) {
    			
    			++newLikeCount;
    			int totalNotificationCount = newLikeCount + newSendCount + newReceiveCount;
    			
    			String body = "새로운 친구 추천이 왔습니다";
    			Map<Platform, Map<String, MessageAttributeValue>> attrsMap = new HashMap<Platform, Map<String, MessageAttributeValue>>();
        		
    			try {
	    			
	        		if (endpointArn.startsWith("arn:aws:sns:ap-northeast-1:039291388167:endpoint/GCM/Wekend/")) {
	        			attrsMap.put(Platform.GCM, null);
	        			String gcmMessage = MessageGenerator.getAndroidMessage(body, Configuration.TYPE_NOTIFICATION_LIKE, likeDBItem.getProductId(), totalNotificationCount);
	        			snsClientWrapper.notification(endpointArn, Platform.GCM, attrsMap, gcmMessage);
	        		} else {
	        			attrsMap.put(Platform.APNS_SANDBOX, null);
	        			
	        			String apnsMessage = MessageGenerator.getAppleMessage(body, Configuration.TYPE_NOTIFICATION_LIKE, likeDBItem.getProductId(), userId, totalNotificationCount); 
	        			snsClientWrapper.notification(endpointArn, Platform.APNS_SANDBOX, attrsMap, apnsMessage);
	        		}
	        		
	        		userInfo.setNewLikeCount(newLikeCount);
	        		userInfo.setNewSendCount(newSendCount);
	        		userInfo.setNewReceiveCount(newReceiveCount);
	        		
	        		mapper.save(userInfo);
    			} catch (Exception e) {
    				logger.log("sendRecommendNotification > error : " + e.toString());
    				continue;
    			}
    		}
    	}
	}
	
	private void sendSelfNotification(int productId, String userId) {
		
		UserInfo userInfo = mapper.load(UserInfo.class, userId);
		
		String endpointArn = userInfo.getEndpointARN();
		int newLikeCount = userInfo.getNewLikeCount();
		int newSendCount = userInfo.getNewSendCount();
		int newReceiveCount = userInfo.getNewReceiveCount();
		
		if (!StringUtils.isNullOrEmpty(endpointArn) && !endpointArn.equals("EndPoint")) {
			
			++newLikeCount;
			int totalNotificationCount = newLikeCount + newSendCount + newReceiveCount;
			
			String body = "새로운 친구 추천이 왔습니다";
    		Map<Platform, Map<String, MessageAttributeValue>> attrsMap = new HashMap<Platform, Map<String, MessageAttributeValue>>();
    		
    		try {
    		
	    		if (endpointArn.startsWith("arn:aws:sns:ap-northeast-1:039291388167:endpoint/GCM/Wekend/")) {
	    			attrsMap.put(Platform.GCM, null);
	    			String gcmMessage = MessageGenerator.getAndroidMessage(body, Configuration.TYPE_NOTIFICATION_LIKE, productId, totalNotificationCount);
	    			snsClientWrapper.notification(endpointArn, Platform.GCM, attrsMap, gcmMessage);
	    		} else {
	    			attrsMap.put(Platform.APNS_SANDBOX, null);
	    			String apnsMessage = MessageGenerator.getAppleMessage(body, Configuration.TYPE_NOTIFICATION_LIKE, productId, userId, totalNotificationCount); 
	    			snsClientWrapper.notification(endpointArn, Platform.APNS_SANDBOX, attrsMap, apnsMessage);
	    		}
	    		
	    		userInfo.setNewLikeCount(newLikeCount);
	    		userInfo.setNewSendCount(newSendCount);
	    		userInfo.setNewReceiveCount(newReceiveCount);
	    		
	    		mapper.save(userInfo);
    		} catch (Exception e) {
    			logger.log("sendSelfNotification > errror : " + e.toString());
    		}
		}
	}

}
