package com.entuition.lambda.sns;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.entuition.lambda.authentication.Configuration;

@DynamoDBTable(tableName = Configuration.LIKE_READ_STATE_TALBE)
public class LikeReadState {

    private String likeId;
    private String userId;
    private int productId;
    private String likeUserId;
    private String readTime;

    @DynamoDBHashKey(attributeName = Configuration.ATTRIBUTE_LIKE_READ_STATE_LIKE_ID)
    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    @DynamoDBRangeKey(attributeName = Configuration.ATTRIBUTE_LIKE_READ_STATE_USERID)
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = Configuration.INDEX_READSTATE_PRODUCTID_USERID)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_LIKE_READ_STATE_PRODUCTID)
    @DynamoDBIndexHashKey(globalSecondaryIndexName = Configuration.INDEX_READSTATE_PRODUCTID_USERID)
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_LIKE_READ_STATE_LIKEUSERID)
    public String getLikeUserId() {
        return likeUserId;
    }

    public void setLikeUserId(String likeUserId) {
        this.likeUserId = likeUserId;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_LIKE_READ_STATE_READTIME)
    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }
}
