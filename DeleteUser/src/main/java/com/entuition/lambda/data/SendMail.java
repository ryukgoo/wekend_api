package com.entuition.lambda.data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.entuition.lambda.authentication.Configuration;

@DynamoDBTable(tableName = Configuration.SEND_MAIL_TABLE)
public class SendMail {
	private String userId;
    private String updatedTime;
    private String responseTime;
    private int productId;
    private String productTitle;
    private String receiverId;
    private String senderNickname;
    private String receiverNickname;
    private String status;
    private int isRead;

    @DynamoDBHashKey(attributeName = Configuration.ATTRIBUTE_MAIL_USER_ID)
    @DynamoDBIndexHashKey(globalSecondaryIndexName = Configuration.SEND_MAIL_INDEX_USERID_RESPONSETIME)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBRangeKey(attributeName = Configuration.ATTRIBUTE_MAIL_UPDATED_TIME)
    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }
    
    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_MAIL_RESPONSETIME)
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = Configuration.SEND_MAIL_INDEX_USERID_RESPONSETIME)
    public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	@DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_MAIL_PRODUCT_ID)
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_MAIL_PRODUCT_TITLE)
    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_MAIL_RECEIVER_ID)
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "ReceiverId-index")
    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_MAIL_SENDER_NICKNAME)
    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_MAIL_RECEIVER_NICKNAME)
    public String getReceiverNickname() {
        return receiverNickname;
    }

    public void setReceiverNickname(String receiverNickname) {
        this.receiverNickname = receiverNickname;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_MAIL_STATUS)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_MAIL_IS_READ)
	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
    
}
