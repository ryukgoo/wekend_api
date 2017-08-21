package com.entuition.lambda.authentication;

public class GetTokenResponse {

	String identityId;
    String identityPoolId;
    String token;

    public GetTokenResponse() {}

    public GetTokenResponse(String identityId, String identityPoolId, String token) {
        this.identityId = identityId;
        this.identityPoolId = identityPoolId;
        this.token = token;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentitiyId(String identitiyId) {
        this.identityId = identitiyId;
    }

    public String getIdentityPoolId() {
        return identityPoolId;
    }

    public void setIdentityPoolId(String identityPoolId) {
        this.identityPoolId = identityPoolId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
