package com.entuition.lambda.authentication;

import java.util.Map;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClient;
import com.amazonaws.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityRequest;
import com.amazonaws.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.entuition.lambda.authentication.DeviceAuthentication.DeviceInfo;
import com.entuition.lambda.authentication.UserAuthentication.UserInfo;
import com.entuition.lambda.authentication.exception.DataAccessException;
import com.entuition.lambda.authentication.exception.NotFoundDeviceException;
import com.entuition.lambda.authentication.exception.UnauthorizedException;

public class GetToken implements RequestHandler<GetTokenRequest, GetTokenResponse> {

	private LambdaLogger logger;
	
	private UserAuthentication userAuthenticator;
	private DeviceAuthentication deviceAuthenticator;
	
    @Override
    public GetTokenResponse handleRequest(GetTokenRequest input, Context context) {
    	
    	logger = context.getLogger();
        logger.log("Input: " + input);
        
        userAuthenticator = new UserAuthentication();
        deviceAuthenticator = new DeviceAuthentication();

        String uid = input.getUid();
        String signature = input.getSignature();
        String timestamp = input.getTimestamp();
        String identityId = input.getIdentityId();
        String loginString = input.getLoginString();
        Map<String, String> logins = Utilities.stringToMap(loginString);
        
        logger.log("loginString: " + loginString);
        
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(timestamp);
        
        for (Map.Entry<String, String> entry : logins.entrySet()) {
        	String provider = entry.getKey();
        	logger.log("provider : " + provider);
        	String token = entry.getValue();
        	logger.log("token : " + token);
        	stringToSign.append(provider);
        	stringToSign.append(token);
        }
        
        if (identityId != null) {
        	stringToSign.append(identityId);
        }
        
        try {
			validateTokenRequest(uid, signature, timestamp, stringToSign.toString());
			GetOpenIdTokenForDeveloperIdentityResult result = getToken(uid, logins, identityId);
			return covertOpenIdTokenResultToResponse(result);
		} catch (DataAccessException e) {
			logger.log("!!!! DataAccessException > e : " + e);
		} catch (UnauthorizedException e) {
			logger.log("!!!! UnauthorizedException > e : " + e);
		} catch (NotFoundDeviceException e) {
			logger.log("!!!! NotFoundDeviceException > e : " + e);
		} catch (Exception e) {
			logger.log("!!!! Exception > e : " + e);
		}
        return null;
    }
    
    private void validateTokenRequest(String uid, String signature, String timestamp, String stringToSign)
    		throws DataAccessException, UnauthorizedException { 
    	if (!Utilities.isTimestampValid(timestamp)) {
    		throw new UnauthorizedException("Invalid timestamp : " + timestamp);
    	}
    	
    	DeviceInfo device = deviceAuthenticator.getDeviceInfo(uid);
    	if (device == null) {
    		throw new UnauthorizedException("Couldn't find device : " + uid);
    	}
    	
    	if (!Utilities.validateSignature(stringToSign, device.getKey(), signature)) {
    		logger.log("stringToSign : " + stringToSign);
        	logger.log("device.getKey(); : " + device.getKey());
        	logger.log("signature : " + signature);
        	logger.log("computedSignature : " + Utilities.sign(stringToSign, device.getKey()));
    		throw new UnauthorizedException("Invalid signature : " + signature);
    	}
	}

    private GetOpenIdTokenForDeveloperIdentityResult getToken(String uid, Map<String, String> logins, String identityId) throws Exception {
    	
    	DeviceInfo device = deviceAuthenticator.getDeviceInfo(uid);
    	if (device == null) {
    		throw new UnauthorizedException("Could't find device : " + uid);
    	}
    	
    	UserInfo user = userAuthenticator.getUserInfoByUserId(device.getUserid());
    	if (user == null) {
//    		throw new UnauthorizedException("Could't find user : " + device.getUsername());
    		throw new NotFoundDeviceException("Could't find user : " + device.getUsername());
    	}
    	
    	String usernameInLogins = logins.get(Configuration.DEVELOPER_PROVIDER_NAME);
    	if (usernameInLogins == null) usernameInLogins = logins.get(Configuration.DEVELOPER_PROVIDER_NAME_FOR_IOS);
    	
    	if (user != null && !user.getUsername().equals(usernameInLogins)) {
    		throw new UnauthorizedException("User mismatch for device and logins map");
    	}
    	
    	GetOpenIdTokenForDeveloperIdentityResult result = getOpenIdTokenFromCognito(user.getUsername(), logins, identityId);
    	return result;
    }
    
    private GetOpenIdTokenForDeveloperIdentityResult getOpenIdTokenFromCognito(
    		String username, Map<String, String> logins, String identityId) throws Exception {
    	
    	AmazonCognitoIdentityClient cognitoIdentityClient = new AmazonCognitoIdentityClient();
    	cognitoIdentityClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
    	
    	if ((Configuration.IDENTITY_POOL_ID == null) || username == null) {
    		return null;
    	} else {
    		try {
	    		GetOpenIdTokenForDeveloperIdentityRequest request = new GetOpenIdTokenForDeveloperIdentityRequest();
	    		request.setIdentityPoolId(Configuration.IDENTITY_POOL_ID);
	    		request.setTokenDuration(Long.parseLong(Configuration.SESSION_DURATION));
	    		request.setLogins(logins);
	    		if (identityId != null && !identityId.equals("")) {
	    			request.setIdentityId(identityId);
	    		}
	    		
	    		GetOpenIdTokenForDeveloperIdentityResult result = cognitoIdentityClient.getOpenIdTokenForDeveloperIdentity(request);
	    		logger.log("identityId : " + result.getIdentityId() + ", token : " + result.getToken());
	    		return result;
    		} catch (Exception e) {
    			throw e;
    		}
    	}
    }
    
    private GetTokenResponse covertOpenIdTokenResultToResponse(GetOpenIdTokenForDeveloperIdentityResult result) {
    	return new GetTokenResponse(result.getIdentityId(), Configuration.IDENTITY_POOL_ID, result.getToken());
    }
}
