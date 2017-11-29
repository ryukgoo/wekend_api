package com.entuition.lambda.data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.entuition.lambda.authentication.Configuration;

@DynamoDBTable(tableName = Configuration.PRODUCT_LIKE_TABLE)
public class ProductLikeTime {
	private int productId;
	private String maleLikeTime;
	private String femaleLikeTime;
	
	@DynamoDBHashKey(attributeName = "ProductId")
	public int getProductId() {
		return productId;
	}
	
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	@DynamoDBAttribute(attributeName = "MaleLikeTime")
	public String getMaleLikeTime() {
		return maleLikeTime;
	}
	
	public void setMaleLikeTime(String maleLikeTime) {
		this.maleLikeTime = maleLikeTime;
	}
	
	@DynamoDBAttribute(attributeName = "FemaleLikeTime")
	public String getFemaleLikeTime() {
		return femaleLikeTime;
	}
	
	public void setFemaleLikeTime(String femaleLikeTime) {
		this.femaleLikeTime = femaleLikeTime;
	}
}
