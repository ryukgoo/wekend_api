package com.entuition.picnic.java;

import java.io.File;
import java.util.Date;
import java.util.Iterator;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.util.DateUtils;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class LoadJsonToDynamoDB {
	
	private static final String PRODUCT_TABLE_NAME = "product_db";
	
	private static final String PRODUCT_ID = "ProductId";
	private static final String PRODUCT_NAME_KOR = "TitleKor";
	private static final String PRODUCT_NAME_ENG = "TitleEng";
	private static final String PRODUCT_REGION = "ProductRegion";
	private static final String PRODUCT_MAIN_CATEGORY = "MainCategory";
	private static final String PRODUCT_SUB_CATEGORY = "SubCategory";
	private static final String PRODUCT_SUB_TITLE = "SubTitle";
	private static final String PRODUCT_DESCRIPTION = "Description";
	private static final String PRODUCT_TELEPHONE = "Telephone";
	private static final String PRODUCT_ADDRESS = "Address";
	private static final String PRODUCT_SUBADDRESS = "SubAddress";
	private static final String PRODUCT_PRICE = "Price";
	private static final String PRODUCT_PARKING = "Parking";
	private static final String PRODUCT_OPERATION_TIME = "OperationTime";
	private static final String PRODUCT_FACEBOOK = "Facebook";
	private static final String PRODUCT_BLOG = "Blog";
	private static final String PRODUCT_INSTAGRAM = "Instagram";
	private static final String PRODUCT_HOMEPAGE = "Homepage";
	private static final String PRODUCT_ETC = "Etc";
	private static final String PRODUCT_IMAGE_COUNT = "ImageCount";
	private static final String PRODUCT_LIKE_COUNT = "LikeCount";
	private static final String PRODUCT_UPDATED_TIME = "UpdatedTime";
	private static final String PRODUCT_STATUS = "ProductStatus";
	
	private static final int SEPERATOR_LIKECOUNT = 1000000;
	
	public static void main(String[] args) throws Exception {

		long startTime = System.nanoTime();
		System.out.println("!!!!!!!! start Put Json to DynamoDB");
		
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable(PRODUCT_TABLE_NAME);

        JsonParser parser = new JsonFactory()
            .createParser(new File("product.json"));
                
        JsonNode rootNode = new ObjectMapper().readTree(parser);
        Iterator<JsonNode> iter = rootNode.iterator();
        
        ObjectNode currentNode;
        while (iter.hasNext()) {
            currentNode = (ObjectNode) iter.next();
           
            int id = currentNode.path(PRODUCT_ID).asInt();
            String productNameKor = currentNode.path(PRODUCT_NAME_KOR).asText();
            String productNameEng = currentNode.path(PRODUCT_NAME_ENG).asText();
            int region = currentNode.path(PRODUCT_REGION).asInt();
            int mainCategory = currentNode.path(PRODUCT_MAIN_CATEGORY).asInt();
            int subCategory = currentNode.path(PRODUCT_SUB_CATEGORY).asInt();
            String subTitle = currentNode.path(PRODUCT_SUB_TITLE).asText();
            String description = currentNode.path(PRODUCT_DESCRIPTION).asText();
            String telephone = currentNode.path(PRODUCT_TELEPHONE).asText();
            String address = currentNode.path(PRODUCT_ADDRESS).asText();
            String subAddress = currentNode.path(PRODUCT_SUBADDRESS).asText();
            String price = currentNode.path(PRODUCT_PRICE).asText();
            String parking = currentNode.path(PRODUCT_PARKING).asText();
            String operationTime = currentNode.path(PRODUCT_OPERATION_TIME).asText();
            String facebook = currentNode.path(PRODUCT_FACEBOOK).asText();
            String blog = currentNode.path(PRODUCT_BLOG).asText();
            String instagram = currentNode.path(PRODUCT_INSTAGRAM).asText();
            String homepage = currentNode.path(PRODUCT_HOMEPAGE).asText();
            String etc = currentNode.path(PRODUCT_UPDATED_TIME).asText();
            int imageCount = currentNode.path(PRODUCT_IMAGE_COUNT).asInt();
            
            Item item = new Item().withPrimaryKey(PRODUCT_ID, id);

            if (!isTextEmpty(productNameKor)) item.withString(PRODUCT_NAME_KOR, productNameKor);
            if (!isTextEmpty(productNameEng)) item.withString(PRODUCT_NAME_ENG, productNameEng);
            item.withInt(PRODUCT_REGION, region);
            item.withInt(PRODUCT_MAIN_CATEGORY, mainCategory);
            item.withInt(PRODUCT_SUB_CATEGORY, subCategory);
            if (!isTextEmpty(subTitle)) item.withString(PRODUCT_SUB_TITLE, subTitle);
            if (!isTextEmpty(description)) item.withString(PRODUCT_DESCRIPTION, description);
            if (!isTextEmpty(telephone)) item.withString(PRODUCT_TELEPHONE, telephone);
            if (!isTextEmpty(address)) item.withString(PRODUCT_ADDRESS, address);
            if (!isTextEmpty(subAddress)) item.withString(PRODUCT_SUBADDRESS, subAddress);
            if (!isTextEmpty(price)) item.withString(PRODUCT_PRICE, price);
            if (!isTextEmpty(parking)) item.withString(PRODUCT_PARKING, parking);
            if (!isTextEmpty(operationTime)) item.withString(PRODUCT_OPERATION_TIME, operationTime);
            if (!isTextEmpty(facebook)) item.withString(PRODUCT_FACEBOOK, facebook);
            if (!isTextEmpty(blog)) item.withString(PRODUCT_BLOG, blog);
            if (!isTextEmpty(instagram)) item.withString(PRODUCT_INSTAGRAM, instagram);
            if (!isTextEmpty(homepage)) item.withString(PRODUCT_HOMEPAGE, homepage);
            if (!isTextEmpty(etc)) item.withString(PRODUCT_ETC, etc);
            item.withInt(PRODUCT_IMAGE_COUNT, imageCount);
            int likeCount = 0;
            item.withInt(PRODUCT_LIKE_COUNT, likeCount * SEPERATOR_LIKECOUNT + id);
            item.withString(PRODUCT_UPDATED_TIME, DateUtils.formatISO8601Date(new Date()));
            item.withString(PRODUCT_STATUS, "Enabled");
            
            try {
            	table.putItem(item);
            } catch (Exception e) {
            	System.err.println("unable to add product > id : " + id);
            	System.err.println(e.getMessage());
            }
            
            System.out.println("!!!!!! Item NO. " + id + " is uploaded");
            
            /*
            table.putItem(new Item()
                .withPrimaryKey("year", year, "title", title)
                .withJSON("info", currentNode.path("info").toString()));
            */
        }
        parser.close();
        
        long endTime = System.nanoTime();
        long processTime = endTime - startTime;
        
        System.out.println("!!!!!!!! put Item Time : " + processTime / 1000000000.0 + "(s)");
    }
	
	private static boolean isTextEmpty(String str) {
		if (str == null || str.trim().length() == 0 || str.equals("0")) return true;
		else return false;
	}
}
