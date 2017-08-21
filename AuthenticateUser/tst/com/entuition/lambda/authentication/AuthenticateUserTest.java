package com.entuition.lambda.authentication;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class AuthenticateUserTest {

    private static UserInfo input;

    @BeforeClass
    public static void createInput() throws IOException {
        // TODO: set up your sample input object here.
    	input = new UserInfo();
    	input.setUserName("test02");
    	input.setPassword("asdfasdf");
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testAuthenticateUser() {
        AuthenticateUser handler = new AuthenticateUser();
        Context ctx = createContext();

        AuthenticateUserResponse output = handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        if (output != null) {
            System.out.println("response.status : " + output.getStatus());
            System.out.println("response.openIdToken : " + output.getOpenIdToken());
        }
    }
}
