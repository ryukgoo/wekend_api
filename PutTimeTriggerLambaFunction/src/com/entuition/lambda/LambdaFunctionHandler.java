package com.entuition.lambda;

import com.amazonaws.services.dynamodbv2.model.StreamRecord;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;

public class LambdaFunctionHandler implements RequestHandler<DynamodbEvent, String> {

    @Override
    public String handleRequest(DynamodbEvent ddbEvent, Context context) {
        context.getLogger().log("ddbEvent: " + ddbEvent);

        for (DynamodbStreamRecord record : ddbEvent.getRecords()){
            System.out.println(record.getEventID());
            System.out.println(record.getEventName());
            System.out.println(record.getDynamodb().toString());
            
            StreamRecord item = record.getDynamodb();
            System.out.println("getKeys : " + item.getKeys().toString());
            
            
         }
        
        // TODO: implement your handler
        return null;
    }

}
