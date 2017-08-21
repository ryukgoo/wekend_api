package com.entuition.lambda.authentication;

import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClient;
import com.amazonaws.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityRequest;
import com.amazonaws.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityResult;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AuthenticateUser implements RequestHandler<UserInfo, AuthenticateUserResponse> {
	
	private static LambdaLogger logger;
	
	private static final String ENCODING_FORMAT = "UTF8";
	private static final String SIGNATURE_METHOD = "HmacSHA256";
	
	private static final String IDENTITY_POOL_ID = "ap-northeast-1:52d1d0ac-b4ef-4943-aa0e-a92871a81c7c";
	private static final String DEVELOPER_PROVIDER_NAME = "login.entuition.picnic";

    @Override
    public AuthenticateUserResponse handleRequest(UserInfo input, Context context) {
    	logger = context.getLogger();
    	
    	AuthenticateUserResponse response = new AuthenticateUserResponse();

    	UserInfo user = authenticateUser(input);
    	
    	if (user != null) {
    		response.setStatus("true");
    		response.setOpenIdToken(user.getOpenIdToken());
    	} else {
    		response.setStatus("false");
    		response.setOpenIdToken(null);
    	}
    	
        return response;
    }
    
    /**
     * check userInfo and authenticate user
     * @param input
     * @return
     */
    private UserInfo authenticateUser(UserInfo input) {
    	UserInfo user = null;
    	
    	String userName = input.getUserName();
    	String password = input.getPassword();
    	String hashedPassword = sign(userName, password);
    	
    	try {
    		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
    		client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
    		DynamoDBMapper mapper = new DynamoDBMapper(client);
    		
    		user = mapper.load(UserInfo.class, userName);
    		
    		if (user != null) {
    			logger.log("user.getPassword() : " + user.getPassword());
				logger.log("hashedPassword : " + hashedPassword);
    			if (user.getPassword().equalsIgnoreCase(hashedPassword)) {
    				String openIdToken = getOpenIdToken(userName);
    				user.setOpenIdToken(openIdToken);
    				return user;
    			} else { // password not matched.
    				logger.log("password not matched.");
    			}
    		} else { // cannot find user info.
    			logger.log("cannot find user info.");
    		}
    	} catch (Exception e) {
    		logger.log("!!! Exception > e : " + e.toString());
    	}
    	return null;
    }

    /**
     * get OpenIdToken from Amazon Cognito.
     * @param userName
     * @return
     */
	private String getOpenIdToken(String userName) {
		AmazonCognitoIdentityClient client = new AmazonCognitoIdentityClient();
		client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
		GetOpenIdTokenForDeveloperIdentityRequest tokenRequest = new GetOpenIdTokenForDeveloperIdentityRequest();
		tokenRequest.setIdentityPoolId(IDENTITY_POOL_ID);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(DEVELOPER_PROVIDER_NAME, userName);
		
		tokenRequest.setLogins(map);
		tokenRequest.setTokenDuration(new Long(10001));
		
		GetOpenIdTokenForDeveloperIdentityResult result = client.getOpenIdTokenForDeveloperIdentity(tokenRequest);
		String token = result.getToken();
		
		return token;
	}
	
	private String sign(String content, String key) {
    	try {
    		byte[] data = content.getBytes(ENCODING_FORMAT);
    		Mac mac = Mac.getInstance(SIGNATURE_METHOD);
    		mac.init(new SecretKeySpec(key.getBytes(ENCODING_FORMAT), SIGNATURE_METHOD));
    		char[] signature = Hex.encodeHex(mac.doFinal(data));
    		return new String(signature);
    	} catch (Exception e) {
    		logger.log("!!! Exception > e : " + e.toString());
    	}
    	return null;
    }

}
