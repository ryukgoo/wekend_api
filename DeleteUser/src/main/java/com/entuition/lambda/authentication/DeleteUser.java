package com.entuition.lambda.authentication;

import java.util.List;
import java.util.Map;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
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
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.entuition.lambda.authentication.Configuration;
import com.entuition.lambda.authentication.DeviceAuthentication.DeviceInfo;
import com.entuition.lambda.data.LikeDBItem;
import com.entuition.lambda.data.ReceiveMail;
import com.entuition.lambda.data.SendMail;
import com.entuition.lambda.sns.AmazonSNSClientWrapper;

public class DeleteUser implements RequestHandler<DynamodbEvent, Object> {

	public static final String TAG = DeleteUser.class.getSimpleName();
	
	private final DynamoDBMapper mapper;
	private final AmazonSNSClientWrapper snsClientWrapper;
	
	public DeleteUser() {
		AmazonDynamoDB dynamoDBClient = new AmazonDynamoDBClient();
        dynamoDBClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
        mapper = new DynamoDBMapper(dynamoDBClient);
        
        AmazonSNS snsClient = new AmazonSNSClient();
        snsClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
        snsClientWrapper = new AmazonSNSClientWrapper(snsClient);
	}
	
    @Override
    public Object handleRequest(DynamodbEvent event, Context context) {
        
    	LambdaLogger logger = context.getLogger();
    	logger.log("record's size : " + event.getRecords().size());
    	
    	for (DynamodbStreamRecord record : event.getRecords()) {
    		logger.log(TAG + " > EventId : " + record.getEventID());
        	logger.log(TAG + " > EventName : " + record.getEventName());
        	logger.log(TAG + " > Dynamodb : " + record.getDynamodb());
        	
        	StreamRecord item = record.getDynamodb();
        	Map<String, AttributeValue> oldImage = item.getOldImage();
//        	Map<String, AttributeValue> newImage = item.getNewImage();
        	
        	if (record.getEventName().equals("REMOVE")) {
        		removeRelatedData(oldImage);
        	}	
    	}
        return null;
    }
    
    private void removeRelatedData(Map<String, AttributeValue> image) {
    	
    	if (image.get(Configuration.ATTRIBUTE_ENDPOINT_ARN) != null) {
    		String endpoint = image.get(Configuration.ATTRIBUTE_ENDPOINT_ARN).getS();
        	snsClientWrapper.deletePlatformApplication(endpoint);
        	System.out.println("DeleteUser > endpoint : " + endpoint);        	
    	}
    	
    	if (image.get(Configuration.ATTRIBUTE_USERID) != null) {
    		String userId = image.get(Configuration.ATTRIBUTE_USERID).getS();
    		deleteLikeDB(userId);
        	deleteReceiveMailDB(userId);
        	deleteSendMailDB(userId);
    		System.out.println("DeleteUser > id : " + userId);
    		if (image.get(Configuration.ATTRIBUTE_PHOTOS) != null) {
//        		List<String> photos = image.get(Configuration.ATTRIBUTE_PHOTOS).getSS();
        		deleteProfileImages(userId);
        	}
    	}
    	
    	if (image.get(Configuration.ATTRIBUTE_USERNAME) != null) {
    		String username = image.get(Configuration.ATTRIBUTE_USERNAME).getS();
    		deleteDeviceInfo(username);
    		System.out.println("DeleteUser > username : " + username);
    	}
    }
    
    private void deleteLikeDB(String userId) {
    	System.out.println("deleteLikeDB > userId : " + userId);
    	LikeDBItem item = new LikeDBItem();
    	item.setUserId(userId);
    	
    	DynamoDBQueryExpression<LikeDBItem> queryExpression = new DynamoDBQueryExpression<LikeDBItem>()
    			.withHashKeyValues(item)
    			.withConsistentRead(false);
    	
    	try {
    		
    		List<LikeDBItem> deletedItems = mapper.query(LikeDBItem.class, queryExpression);
    		
    		for (LikeDBItem deleteItem : deletedItems) {
    			System.out.println("deleteItem > id : " + deleteItem.getProductId());
    			mapper.delete(deleteItem);
    		}
    		
//	    	mapper.batchDelete(deletedItems);
    	} catch (Exception e) {
    		System.err.println("deleteLikeDB > Exception : " + e.toString());
    	}
    }
    
    private void deleteReceiveMailDB(String userId) {
    	System.out.println("deleteReceiveMailDB > userId : " + userId);
    	
    	ReceiveMail mail = new ReceiveMail();
    	mail.setUserId(userId);
    	
    	DynamoDBQueryExpression<ReceiveMail> queryExpression = new DynamoDBQueryExpression<ReceiveMail>()
    			.withHashKeyValues(mail)
    			.withConsistentRead(false);
    	
    	try {
    		List<ReceiveMail> deletedMails = mapper.query(ReceiveMail.class, queryExpression);
    		for (ReceiveMail receiveMail : deletedMails) {
    			mapper.delete(receiveMail);
    		}
    	} catch (Exception e) {
    		System.err.println("deleteReceiveMail > Exception : " + e.toString());
    	}
    	
    	deleteReceiveMailBySender(userId);
    }
    
    private void deleteReceiveMailBySender(String senderId) {
    	System.out.println("deleteReceiveMailBySender > senderId : "+ senderId);
    	ReceiveMail mail = new ReceiveMail();
    	mail.setSenderId(senderId);
    	
    	DynamoDBQueryExpression<ReceiveMail> queryExpression = new DynamoDBQueryExpression<ReceiveMail>()
    			.withIndexName("SenderId-index")
    			.withHashKeyValues(mail)
    			.withConsistentRead(false);
    	try {
    		List<ReceiveMail> deletedMails = mapper.query(ReceiveMail.class, queryExpression);
    		for (ReceiveMail receiveMail : deletedMails) {
    			mapper.delete(receiveMail);
    		}
    	} catch (Exception e) {
    		System.err.println("deleteReceiveMailBySender > Exception : " + e.toString());
    	}
    }
    
    private void deleteSendMailDB(String userId) {
    	System.out.println("deleteSendMailDB > userId : " + userId);
    	
    	SendMail mail = new SendMail();
    	mail.setUserId(userId);
    	
    	DynamoDBQueryExpression<SendMail> queryExpression = new DynamoDBQueryExpression<SendMail>()
    			.withHashKeyValues(mail)
    			.withConsistentRead(false);
    	
    	try {
    		List<SendMail> deletedMails = mapper.query(SendMail.class, queryExpression);
    		for (SendMail sendMail : deletedMails) {
    			mapper.delete(sendMail);
    		}
    	} catch (Exception e) {
    		System.err.println("deleteSendMail > Exception : " + e.toString());
    	}
    	deleteSendMailByReceiver(userId);
    }
    
    private void deleteSendMailByReceiver(String receiverId) {
    	System.out.println("deleteSendMailByReceiver > receiverId : " + receiverId);
    	SendMail mail = new SendMail();
    	mail.setReceiverId(receiverId);
    	
    	DynamoDBQueryExpression<SendMail> queryExpression = new DynamoDBQueryExpression<SendMail>()
    			.withIndexName("ReceiverId-index")
    			.withHashKeyValues(mail)
    			.withConsistentRead(false);
    	
    	try {
    		List<SendMail> deletedMails = mapper.query(SendMail.class, queryExpression);
    		for (SendMail sendMail : deletedMails) {
    			mapper.delete(sendMail);
    		}
    	} catch (Exception e) {
    		System.err.println("deleteSendMail > Exception : " + e.toString());
    	}
    }
    
    private void deleteDeviceInfo(String username) {
    	
    	System.out.println("deleteUserInfo > username : " + username);    
    	DeviceInfo deviceInfo = new DeviceInfo();
    	deviceInfo.setUsername(username);
    	
    	DynamoDBQueryExpression<DeviceInfo> queryExpression = new DynamoDBQueryExpression<DeviceAuthentication.DeviceInfo>()
    			.withIndexName("username-uid-index")
    			.withHashKeyValues(deviceInfo)
    			.withConsistentRead(false);
    	
    	try {
    		List<DeviceInfo> devices = mapper.query(DeviceInfo.class, queryExpression);
    		
    		for (DeviceInfo device : devices) {
    			System.out.println("deleteDeviceInfo > device uid : " + device.getUid());
    			System.out.println("deleteDeviceInfo > device username : " + device.getUsername());
    		}
    		
    		if (devices.size() == 1) {
    			mapper.delete(devices.get(0));
    		} else {
    			mapper.batchDelete(devices);
    		}
    	} catch (Exception e) {
    		System.err.println("deleteDeviceInfo > Exception : " + e.toString());
    	}
    }
    
    private void deleteProfileImages(String userId) {
    	
    	AmazonS3 s3Client = new AmazonS3Client();
    	s3Client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
    	
    	try {
    		S3Object object = s3Client.getObject(new GetObjectRequest("entuition-user-profile", userId + "/profile_image_0.jpg"));
    		System.out.println("obejct : " + object.getBucketName());
    		
    		s3Client.deleteObject("entuition-user-profile", userId + "/profile_image_0.jpg");
//    		s3Client.deleteObject("entuition-user-profile", userId);
        	s3Client.deleteObject("entuition-user-profile-thumb", userId + "/profile_image_0.jpg");
//        	s3Client.deleteObject("entuition-user-profile-thumb", userId);
    	} catch (AmazonS3Exception s3e) {
    		System.err.println("deleteProfileImage > S3 AmazonS3Exception : " + s3e.getMessage());
    	} catch (Exception e) {
    		System.err.println("deleteProfileImage > S3 Exception : " + e.getMessage());
    	}
    	
    }
}
