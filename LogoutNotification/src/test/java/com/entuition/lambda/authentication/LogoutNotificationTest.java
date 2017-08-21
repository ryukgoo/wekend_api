package com.entuition.lambda.authentication;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LogoutNotificationTest {

    private static DynamodbEvent input;

    @BeforeClass
    public static void createInput() throws IOException {
        input = TestUtils.parse("dynamodb-update-event.json", DynamodbEvent.class);
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testLogoutNotification() {
        LogoutNotification handler = new LogoutNotification();
        Context ctx = createContext();

        Object output = handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        if (output != null) {
            System.out.println(output.toString());
        }
    }
}
