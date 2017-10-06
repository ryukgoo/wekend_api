package com.entuition.lambda.function;

import java.util.Date;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.DateUtils;

public class UploadProductContents implements RequestHandler<ProductInfo, String> {

    @Override
    public String handleRequest(ProductInfo input, Context context) {
        context.getLogger().log("Input: " + input);

        AmazonDynamoDB dynamoDBClient = new AmazonDynamoDBClient();
        dynamoDBClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
        
        System.out.println(input.desctiprion());
        
        try {
        	
        	input.setTitleKor(input.getTitleKor().trim());
        	input.setTitleEng(input.getTitleEng().trim());
        	input.setSubTitle(input.getSubTitle().trim());
        	input.setTelephone(input.getTelephone().trim());
        	input.setAddress(input.getAddress().trim());
        	input.setSubAddress(input.getSubAddress().trim());
        	input.setPrice(input.getPrice().trim());
        	input.setOperationTime(input.getOperationTime().trim());
        	input.setParking(input.getParking().trim());
        	input.setFacebook(input.getFacebook().trim());
        	input.setBlog(input.getBlog().trim());
        	input.setInstagram(input.getInstagram().trim());
        	input.setHomepage(input.getHomepage().trim());
        	input.setEtc(input.getEtc().trim());
        	
        	input.setUpdatedTime(DateUtils.formatISO8601Date(new Date()));
        	input.setLikeCount(input.getId());
        	input.setStatus("Enabled");
        	
        	mapper.save(input);
        	
        } catch (Exception e) {
        	System.err.println("Exception > e : " + e.toString());
        }
        
        // TODO: implement your handler
        return null;
    }

}