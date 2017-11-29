package com.entuition.lambda.data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.entuition.lambda.authentication.Configuration;

@DynamoDBTable(tableName = Configuration.LIKE_TABLE)
public class LikeDBItem {
	
	private String userId;
	private String nickname;
    private int productId;
    private String gender;
    private String productTitle;
    private String productDesc;
    private String updatedTime;
    private String readTime;
    private String likeId;

    @DynamoDBHashKey(attributeName = Configuration.ATTRIBUTE_LIKE_USER_ID)
    @DynamoDBIndexHashKey(globalSecondaryIndexName = Configuration.INDEX_LIKE_USERID_UPDATEDTIME)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = Configuration.INDEX_LIKE_PRODUCTID_UPDATEDTIME)
    @DynamoDBRangeKey(attributeName = Configuration.ATTRIBUTE_LIKE_PRODUCT_ID)
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_LIKE_GENDER)
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_LIKE_PRODUCT_TITLE)
    public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	@DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_LIKE_PRODUCT_DESC)
	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	@DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_LIKE_UPDATED_TIME)
    @DynamoDBIndexRangeKey(globalSecondaryIndexNames = {Configuration.INDEX_LIKE_PRODUCTID_UPDATEDTIME, Configuration.INDEX_LIKE_USERID_UPDATEDTIME})
    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_LIKE_NICKNAME)
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	@DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_LIKE_READ_TIME)
	public String getReadTime() {
		return readTime;
	}

	public void setReadTime(String readTime) {
		this.readTime = readTime;
	}

	@DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_LIKE_ID)
	public String getLikeId() {
		return likeId;
	}

	public void setLikeId(String likeId) {
		this.likeId = likeId;
	}
}
