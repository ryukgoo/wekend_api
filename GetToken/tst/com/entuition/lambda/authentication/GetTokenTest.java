package com.entuition.lambda.authentication;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class GetTokenTest {

    private static GetTokenRequest input;

    @BeforeClass
    public static void createInput() throws IOException {
        // TODO: set up your sample input object here.
        input = new GetTokenRequest();
        input.setIdentityId(null);
//        HashMap<String, String> hashMap = new HashMap<String, String>();
//        hashMap.put("login.entuition.picnic", "test01");
//        input.setLogins(hashMap);
        input.setTimestamp(Utilities.getTimestamp());
        input.setUid(Utilities.generateRandomString());
        input.setSignature(Utilities.sign(Utilities.getTimestamp(), Utilities.generateRandomString()));
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testGetToken() {
        GetToken handler = new GetToken();
        Context ctx = createContext();

        GetTokenResponse output = handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        if (output != null) {
            System.out.println(output.toString());
        }
    }
}
