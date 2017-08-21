package com.entuition.lambda.authentication;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.entuition.lambda.authentication.DeviceAuthentication.DeviceInfo;
import com.entuition.lambda.authentication.UserAuthentication.UserInfo;
import com.entuition.lambda.authentication.exception.DataAccessException;
import com.entuition.lambda.authentication.exception.NotFoundUserException;
import com.entuition.lambda.authentication.exception.UnauthorizedException;

public class LoginUser implements RequestHandler<LoginRequest, LoginResponse> {

	private LambdaLogger logger;
	
	private DeviceAuthentication deviceAuthenticator;
	private UserAuthentication userAuthenticator;
	
    @Override
    public LoginResponse handleRequest(LoginRequest input, Context context) {
    	
    	logger = context.getLogger();
    	logger.log("Input: " + input);
    	
    	deviceAuthenticator = new DeviceAuthentication();
    	userAuthenticator = new UserAuthentication();
    	
    	String username = input.getUsername();
    	logger.log("username : " + username);
    	String timestamp = input.getTimestamp();
    	logger.log("timestamp : " + timestamp);
    	String signature = input.getSignature();
    	logger.log("signature : " + signature);
    	String uid = input.getUid();
    	logger.log("uid : " + uid);
    	
    	try {
    		String userid = validateLoginRequest(username, uid, signature, timestamp);
    		return new LoginResponse(getKey(username, uid), "true", userid);
    	} catch (DataAccessException e) {
    		logger.log("DataAccessException : " + e.toString());
    		return new LoginResponse(null, "false");
    	} catch (UnauthorizedException e) {
    		logger.log("UnauthorizedException : " + e.toString());
    		return new LoginResponse(null, "false");
    	} catch (NotFoundUserException e) {
    		logger.log("NotFoundUserException : " + e.toString());
    		return new LoginResponse(null, "false");
    	} catch (Exception e) {
    		logger.log("exception : " + e.toString());
    		return new LoginResponse(null, "false");
    	}
    }

    /**
     * 
     * @param username
     * @param uid
     * @param signature
     * @param timestamp
     * @throws DataAccessException
     * @throws UnauthorizedException
     */
    private String validateLoginRequest(String username, String uid, String signature, String timestamp) 
    		throws DataAccessException, UnauthorizedException, NotFoundUserException {
    	if (!Utilities.isTimestampValid(timestamp)) {
    		throw new UnauthorizedException("Invalid timestamp : " + timestamp);
    	}
    	
    	UserInfo user = userAuthenticator.getUserInfoByUsername(username);
    	
    	if (user == null) {
//    		throw new UnauthorizedException("Couldn't find user : " + username);
    		throw new NotFoundUserException("Could not find user : " + username);
    	} else {
    		logger.log("password : " + user.getHashedPassword());
    		logger.log("userid : " + user.getUserid());
    	}
    	
    	// TODO....
    	if (!Utilities.validateSignature(timestamp, user.getHashedPassword(), signature)) {
    		logger.log("Utilities.sign(timestamp, user.getHashedPassword()) : " + Utilities.sign(timestamp, user.getHashedPassword()));
    		logger.log("signature : " + signature);
    		throw new UnauthorizedException("Invalid signature : " + signature);
    	}
    	
    	deleteOldDevices(uid, user);
    	
    	DeviceInfo device = regenerateKey(uid, user.getUsername(), user.getUserid());
    	
    	if (!deviceBelongsToUser(user.getUsername(), device.getUsername())) {
    		throw new UnauthorizedException(String.format("User [ %s ] doesn't match the device's owner [ %s ]",
                    user.getUsername(), device.getUsername()));
    	}
    	
    	return user.getUserid();
    }
    
    /**
     * 
     * @param username
     * @param uid
     * @return
     * @throws DataAccessException
     * @throws UnauthorizedException
     */
    private String getKey(String username, String uid) throws DataAccessException, UnauthorizedException {
    	DeviceInfo device = deviceAuthenticator.getDeviceInfo(uid);
    	if (device == null) {
    		throw new UnauthorizedException("Couldn't find device : " + uid);
    	}
    	
    	return device.getKey();
    }
    
    /**
     * 
     * @param uid
     * @param userInfo
     * @throws DataAccessException
     */
    private void deleteOldDevices(String uid, UserInfo userInfo) throws DataAccessException {
    	List<DeviceInfo> retrieveDevices = deviceAuthenticator.retrieveDevice(userInfo.getUsername()); 
    	
    	if (retrieveDevices != null) {
    		for (DeviceInfo device : retrieveDevices) {
	    		if (!uid.equals(device.getUid())) {
	    			deviceAuthenticator.deleteDevice(device.getUid());
	    		}
	    	}
    	}
    }
    
    /**
     * 
     * @param uid
     * @param username
     * @return
     * @throws DataAccessException
     */
    private DeviceInfo regenerateKey(String uid, String username, String userid) throws DataAccessException {
    	// TODO : How to generate encrytionKey..
    	String encryptionKey = Utilities.generateRandomString();
    	
    	if (deviceAuthenticator.registerDevice(uid, encryptionKey, username, userid)) {
    		return deviceAuthenticator.getDeviceInfo(uid);
    	}
    	
    	return null;
    }
    
    private boolean deviceBelongsToUser(String useridFromUser, String useridFromDevice) {
    	return useridFromUser.equals(useridFromDevice);
    }
}
