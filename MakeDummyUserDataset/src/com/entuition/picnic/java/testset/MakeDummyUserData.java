package com.entuition.picnic.java.testset;

import java.util.Random;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.entuition.lambda.authentication.UserAuthentication.UserInfo;
import com.entuition.lambda.authentication.Utilities;

public class MakeDummyUserData {
	
	private static final int TEST_SET_COUNT = 500;
	
	private static final String TEST_USER_NAME_PREFIX = "testUser-";
	private static final String TEST_PASSWORD = "password00";
	private static final String TEST_NICKNAME = "nicknameTEST-";
	private static final String[] ARRAY_GENDER = {"male", "female"};
	private static final String TEST_PHONE_NUMBER = "01012341234";
	
	public static void main(String[] args) throws Exception {
		
		long startTime = System.nanoTime();
		System.out.println("!!!!!!! start make dummay user data!!!!");
		
		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
		
		DynamoDBMapper mapper = new DynamoDBMapper(client);
		
		for (int i = 0 ; i < TEST_SET_COUNT ; i++) {
			
			String userName = TEST_USER_NAME_PREFIX + String.format("%04d",i);
			String password = TEST_PASSWORD + i;
			String status = "ACTIVE";
			String registeredTime = Utilities.getTimestamp();
			String nickname = TEST_NICKNAME + i;
			String gender = ARRAY_GENDER[randInt(0, 1)];
			int birth = randInt(1950, 1997);
			String phone = TEST_PHONE_NUMBER;
			int balloon = randInt(0, 100);
			
			UserInfo userInfo = new UserInfo();
			userInfo.setUsername(userName);
			userInfo.setHashedPassword(password);
			userInfo.setStatus(status);
			userInfo.setRegisteredTime(registeredTime);
			userInfo.setNickname(nickname);
			userInfo.setGender(gender);
			userInfo.setBirth(birth);
			userInfo.setPhone(phone);
			userInfo.setEndpointARN("EndPoint");
			userInfo.setBalloon(balloon);
			
			mapper.save(userInfo);
		}
		
		long endTime = System.nanoTime();
		long processTime = endTime - startTime;
		
		System.out.println("!!!!!! make dummy test set Time : " + processTime / 1000000000.0 + "(s)");
	}
	
	public static int randInt(int min, int max) {
	    Random rand = new Random();

	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
}
