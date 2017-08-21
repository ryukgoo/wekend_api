package com.entuition.wekend.notice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.util.DateUtils;

public class UploadNoticeToServer {

	private static final String NOTICE_DB = "notice_db";
	
	public static void main(String[] args) throws Exception {
		
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_1).build();
		
		Scanner scanner = new Scanner(System.in);
		
		do {
			
			System.out.print("Notice Type :");
			String type = scanner.nextLine();
			System.out.println(type);
		
			System.out.print("Notice Title: ");
			String title = scanner.nextLine();
			System.out.println(title);
			
			System.out.print("Notice SubTitle: ");
			String subTitle = scanner.nextLine();
			System.out.println(subTitle);
			
			System.out.print("Notice Content: ");
			String content = scanner.nextLine();
			System.out.println(content);
			
			System.out.println("title: " + title);
			System.out.println("subTitle: " + subTitle);
			System.out.println("content: " + content);

			// check start Time
			long startTime = System.nanoTime();
			
			Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
			item.put("noticeType", new AttributeValue().withS(type));
			item.put("noticeId", new AttributeValue().withS(UUID.randomUUID().toString()));
			item.put("updatedTime", new AttributeValue().withS(DateUtils.formatISO8601Date(new Date())));
			item.put("title", new AttributeValue().withS(title));
			item.put("subTitle", new AttributeValue().withS(subTitle));
			item.put("content", new AttributeValue().withS(content));
			
			try {
				client.putItem(NOTICE_DB, item);
			} catch (Exception e) {
				System.err.println("Error message : " + e.getMessage());
			}
			
			long endTime = System.nanoTime();
			long uploadTime = endTime - startTime;
			
			System.out.println("Item uploaded > upload Time : " + uploadTime / 1000000000.0 + "s");
			
			System.out.print("continue?(y/n): ");
			
		} while (scanner.nextLine().equals("y"));
		
		scanner.close();
	}
	
}
