package com.entuition.picnic.java.testset;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.entuition.lambda.authentication.UserAuthentication.UserInfo;
import com.entuition.lambda.authentication.Utilities;
import com.entuition.lambda.data.LikeDBItem;
import com.entuition.lambda.data.ProductInfo;

public class MakeDummyLikeDataset {
	
	private static final int TEST_SET_COUNT_PER_USER = 3;
	
	public static void main(String[] args) throws Exception {
		
		long startTime = System.nanoTime();
		System.out.println("!!!!!!! start make dummay user data!!!!");
		
		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
		
		DynamoDBMapper mapper = new DynamoDBMapper(client);
		
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		List<UserInfo> userInfoList = mapper.scan(UserInfo.class, scanExpression);
		
		List<ProductInfo> productInfos = mapper.scan(ProductInfo.class, new DynamoDBScanExpression());
		int productInfoSize = productInfos.size();
		
		Iterator<UserInfo> iterator = userInfoList.iterator();
		
		UserInfo userInfo;
		while (iterator.hasNext()) {
			userInfo = iterator.next();
			
			String userId = userInfo.getUserid();
			String gender = userInfo.getGender();
			
			for (int i = 0 ; i < TEST_SET_COUNT_PER_USER ; i ++) {
				LikeDBItem likeItem = new LikeDBItem();
				likeItem.setUserId(userId);
				ProductInfo productInfo = productInfos.get(randInt(0, productInfoSize - 1));
				likeItem.setNickname(userInfo.getGender() + String.format("%04d",i));
				likeItem.setLikeId(Utilities.generateRandomString());
				likeItem.setProductId(productInfo.getId());
				likeItem.setProductTitle(productInfo.getTitleKor());
				likeItem.setProductDesc(productInfo.getDescription());
				likeItem.setGender(gender);
				likeItem.setUpdatedTime(Utilities.getTimestamp());
				
				mapper.save(likeItem);
			}
			
			System.out.println("!!!!! UserId > " + userId + " is Done@@!!!!@!@!@");
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
