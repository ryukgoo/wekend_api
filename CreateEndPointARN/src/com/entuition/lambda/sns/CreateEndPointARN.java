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
import com.amazonaws.services.sns.model.NotFoundException;
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
			String endpointArn = userInfo.getEndpointARN();
			logger.log("UserInfo's EndpointARN : " + endpointArn);
			
			if (endpointArn == null) {
				endpointArn = createPlatformEndpoint(platform, deviceToken, userId);
				userInfo.setEndpointARN(endpointArn);
				userAuthenticator.updateUser(userInfo);
			} else {
				try {
					Map<String, String> attributes = snsClientWrapper.getEndpointArnFromSNS(endpointArn);
					if (!attributes.get("Token").equals(deviceToken) || !attributes.get("Enabled").equals("true")) {
						snsClientWrapper.resetPlatformEndpointArn(endpointArn, deviceToken);
					}					
				} catch (NotFoundException nfe) {
					logger.log("NotFoundException > e : " + nfe.getMessage());
					endpointArn = createPlatformEndpoint(platform, deviceToken, userId);
					userInfo.setEndpointARN(endpointArn);
					userAuthenticator.updateUser(userInfo);
				} catch (Exception e) {
					logger.log("getEndpointArn > Exception > e : " + e.getMessage());
				}
			}
			
	        response.setEndpointARN(endpointArn);
			
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
	
	private String createPlatformEndpoint(String platform, String deviceToken, String userId) {
		
		String applicationName = "Wekend";
        String endpoint = "";
		
        switch (platform) {
        case "Android" :
        	logger.log("CreateEndPointARN Platform is Android");
        	endpoint = getPlatformEndpointForAndroid(deviceToken, applicationName, userId);
        	break;
        	
        case "iOS" :
        	logger.log("CreateEndPointARN Platform is iOS");
        	endpoint = getPlatformEndpointForiOS(deviceToken, applicationName, userId);
        	break;
        }
        
        return endpoint;
	}
	
	private String getPlatformEndpointForiOS(String deviceToken, String applicationName, String userId) {
		String certificate = Configuration.APNS_CERTIFICATE;
		String privateKey = Configuration.APNS_PRIVATEKEY;
//		return snsClientWrapper.getPlatformEndpointARN(Platform.APNS_SANDBOX, certificate, privateKey, deviceToken, applicationName, userId);
		return snsClientWrapper.getPlatformEndpointARN(Platform.APNS, certificate, privateKey, deviceToken, applicationName, userId);
	}
	
	private String getPlatformEndpointForAndroid(String deviceToken, String applicationName, String userId) {
		String serverAPIKey = Configuration.GCM_SERVER_API_KEY;
		return snsClientWrapper.getPlatformEndpointARN(Platform.GCM, "", serverAPIKey, deviceToken, applicationName, userId);
	}
	
}
