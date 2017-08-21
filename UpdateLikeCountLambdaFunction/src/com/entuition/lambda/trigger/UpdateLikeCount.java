package com.entuition.lambda.trigger;

import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.StreamRecord;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import com.entuition.lambda.authentication.Configuration;
import com.entuition.lambda.data.LikeDBItem;
import com.entuition.lambda.data.ProductInfo;

public class UpdateLikeCount implements RequestHandler<DynamodbEvent, Object> {

	private final String TAG = getClass().getSimpleName();
	
	private static final int LIKE_COUNT_DELIMETER = 1000000;
	
	private final AmazonDynamoDBClient client;
	private final DynamoDBMapper mapper;
	
    public UpdateLikeCount() {
		this.client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
		this.mapper = new DynamoDBMapper(client);
	}

	@Override
    public Object handleRequest(DynamodbEvent dynamodbEvent, Context context) {
		
        context.getLogger().log("Input: " + dynamodbEvent);
        context.getLogger().log("record's size : " + dynamodbEvent.getRecords().size());
        
        try {
        	
	        for (DynamodbStreamRecord record : dynamodbEvent.getRecords()) {
	        	
	        	context.getLogger().log(TAG + " > EventId : " + record.getEventID());
	        	context.getLogger().log(TAG + " > EventName : " + record.getEventName());
	        	context.getLogger().log(TAG + " > Dynamodb : " + record.getDynamodb());
	        	
	        	StreamRecord item = record.getDynamodb();
	        	Map<String, AttributeValue> keys = item.getKeys();
	        	String userId = keys.get(Configuration.ATTRIBUTE_LIKE_USER_ID).getS();
	        	int productId = Integer.parseInt(keys.get(Configuration.ATTRIBUTE_LIKE_PRODUCT_ID).getN());
	        	
	        	context.getLogger().log(TAG + " > userId : " + userId);
	        	context.getLogger().log(TAG + " > productId : " + productId);
	        	
	        	LikeDBItem key = new LikeDBItem();
	        	key.setProductId(productId);
	        	
	        	DynamoDBQueryExpression<LikeDBItem> queryExpression = new DynamoDBQueryExpression<LikeDBItem>()
	        			.withIndexName(Configuration.INDEX_LIKE_PRODUCTID_UPDATEDTIME)
	        			.withHashKeyValues(key)
	        			.withConsistentRead(false);
	        	
	        	int count = mapper.count(LikeDBItem.class, queryExpression);
	        	int likeCount = count * LIKE_COUNT_DELIMETER + productId;
	        	
	        	ProductInfo productInfo = mapper.load(ProductInfo.class, productId);
	        	productInfo.setLikeCount(likeCount);
	        	mapper.save(productInfo);
	        }
	        
        } catch (AmazonClientException e) {
        	
        } catch (Exception e) {
        	
        }
        
        // TODO: implement your handler
        return null;
    }

}
