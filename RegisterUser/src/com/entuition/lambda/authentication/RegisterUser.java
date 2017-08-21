package com.entuition.lambda.authentication;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.entuition.lambda.authentication.exception.DataAccessException;

public class RegisterUser implements RequestHandler<RegisterRequest, RegisterResponse> {
	
	private static LambdaLogger logger;
	
	private static final String RESPONSE_SUCCESS = "success";
	private static final String RESPONSE_FAILED = "failed";
	
	private UserAuthentication userAuthenticator;

    @Override
    public RegisterResponse handleRequest(RegisterRequest input, Context context) {

    	logger = context.getLogger();
    	
    	String username = input.getUsername();
    	String password = input.getPassword();
    	String nickname = input.getNickname();
    	String gender = input.getGender();
    	int birth = input.getBirth();
    	String phone = input.getPhone();
    	
        context.getLogger().log("userName: " + username);
        context.getLogger().log("password: " + password);
        
        userAuthenticator = new UserAuthentication();
        try {
        	String userid = userAuthenticator.registerUser(username, password, nickname, gender, birth, phone); 
			if (userid != null) {
				logger.log("Register User Success!!");
				return new RegisterResponse(RESPONSE_SUCCESS, userid);
			} else {
				logger.log("Register User Failed!!");
				return new RegisterResponse(RESPONSE_FAILED, null);
			}
		} catch (DataAccessException e) {
			logger.log("Exception > e : " + e.toString());
		}

        return new RegisterResponse(RESPONSE_FAILED, null);
    }
}
