package com.entuition.lambda.authentication;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LoginUserTest {

    private static LoginRequest input;

    @BeforeClass
    public static void createInput() throws IOException {
        // TODO: set up your sample input object here.
    	
    	String timestamp = Utilities.getTimestamp(); 
    	
        input = new LoginRequest();
        input.setUsername("test01");
        input.setUid(Utilities.generateRandomString());
        input.setTimestamp(timestamp);
        input.setSignature(timestamp);
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("LoginUser");

        return ctx;
    }

    @Test
    public void testLoginUser() {
        LoginUser handler = new LoginUser();
        Context ctx = createContext();

        LoginResponse output = handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        if (output != null) {
            System.out.println("key : " + output.getKey());
            System.out.println("enable : " + output.getEnable());
        }
    }
}
