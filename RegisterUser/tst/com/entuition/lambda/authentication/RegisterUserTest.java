package com.entuition.lambda.authentication;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class RegisterUserTest {

    private static RegisterRequest input;

    @BeforeClass
    public static void createInput() throws IOException {
        // TODO: set up your sample input object here.
        input = new RegisterRequest();
        input.setUsername("Juit_test");
        input.setPassword("12341234");
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("RegisterUser");

        return ctx;
    }

    @Test
    public void testRegisterUser() {
        RegisterUser handler = new RegisterUser();
        Context ctx = createContext();

        RegisterResponse output = handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        if (output != null) {
            System.out.println(output.toString());
        }
    }
}
