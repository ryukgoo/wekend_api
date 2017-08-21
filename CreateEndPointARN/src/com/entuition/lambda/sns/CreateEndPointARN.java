package com.entuition.lambda.sns;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.entuition.lambda.authentication.Configuration;
import com.entuition.lambda.authentication.UserAuthentication;
import com.entuition.lambda.authentication.UserAuthentication.UserInfo;
import com.entuition.lambda.authentication.exception.DataAccessException;
import com.entuition.lambda.sns.MessageGenerator.Platform;

public class CreateEndPointARN implements RequestHandler<CreateEndPointARNRequest, CreateEndPointARNResponse> {
	
	private LambdaLogger logger;
	
	private AmazonSNSClientWrapper snsClientWrapper;
	private UserAuthentication userAuthenticator;
	
	public static final Map<Platform, Map<String, MessageAttributeValue>> attributesMap = new HashMap<Platform, Map<String, MessageAttributeValue>>();
	static {
		attributesMap.put(Platform.ADM, null);
		attributesMap.put(Platform.GCM, null);
		attributesMap.put(Platform.APNS, null);
		attributesMap.put(Platform.APNS_SANDBOX, null);
		attributesMap.put(Platform.BAIDU, null);
		attributesMap.put(Platform.WNS, null);
		attributesMap.put(Platform.MPNS, null);
	}

	@Override
    public CreateEndPointARNResponse handleRequest(CreateEndPointARNRequest input, Context context) {
    	
    	logger = context.getLogger();
        logger.log("Input: " + input);
        
        AmazonSNSClient snsClient = new AmazonSNSClient();
        snsClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
        
        snsClientWrapper = new AmazonSNSClientWrapper(snsClient);
        
        CreateEndPointARNResponse response = new CreateEndPointARNResponse();
        
        String userId = input.getUserId();
        String deviceToken = input.getSnsToken();
        String platform = input.getPlatform();
        
        userAuthenticator = new UserAuthentication();
        
        logger.log("CreateEndPointARN > enter try and catch");
        
        try {
        	
			UserInfo userInfo = userAuthenticator.getUserInfoByUserId(userId);
			
			if (userInfo != null) {
				logger.log("UserInfo's EndpointARN : " + userInfo.getEndpointARN());				
			}
			
			if (userInfo.getEndpointARN() != null) {
				snsClientWrapper.deletePlatformApplication(userInfo.getEndpointARN());
			}
			
	        String applicationName = "Wekend";
	        
	        logger.log("platform : " + platform);
	        logger.log("deviceToken : " + deviceToken);
	        logger.log("userId : " + userId);
	        
	        switch (platform) {
	        case "Android" :
	        	
	        	logger.log("CreateEndPointARN Platform is Android");
	        	
	        	String serverAPIKey = Configuration.GCM_SERVER_API_KEY;
				response.setEndpointARN(snsClientWrapper.getPlatformEndpointARN(Platform.GCM, "",
	        			serverAPIKey, deviceToken, applicationName, userId));
	        	break;
	        	
	        case "iOS" :
	        	
	        	logger.log("CreateEndPointARN Platform is iOS");
	        	
	        	String certificate = Configuration.APNS_CERTIFICATE;
	        	String privateKey = Configuration.APNS_PRIVATEKEY;
	        	response.setEndpointARN(snsClientWrapper.getPlatformEndpointARN(Platform.APNS_SANDBOX,
	        			certificate, privateKey, deviceToken, applicationName, userId));
	        	break;
	        }
			
			userInfo.setEndpointARN(response.getEndpointARN());
			
			userAuthenticator.updateUser(userInfo);
			
			return response;
			
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			logger.log("DataAccessException > e : " + e.getMessage());
		} catch (AmazonServiceException ase) {
			logger.log("Caught an AmazonServiceException, which means your request made it "
							+ "to Amazon SNS, but was rejected with an error response for some reason.");
			logger.log("Error Message:    " + ase.getMessage());
			logger.log("HTTP Status Code: " + ase.getStatusCode());
			logger.log("AWS Error Code:   " + ase.getErrorCode());
			logger.log("Error Type:       " + ase.getErrorType());
			logger.log("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
        	logger.log("Caught an AmazonClientException, which means the client encountered "
							+ "a serious internal problem while trying to communicate with SNS, such as not "
							+ "being able to access the network.");
        	logger.log("Error Message: " + ace.getMessage());
        }
        
        return response;
    }
	
}
