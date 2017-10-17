package com.entuition.lambda.authentication;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class Configuration {

	/**
	 * 
	 */
	public static final String IDENTITY_POOL_ID = "ap-northeast-1:7fd2e15f-b246-4086-a019-dc6d446bdd99";
	
	/**
	 * 
	 */
	public static final String DEVELOPER_PROVIDER_NAME = "login.entuition.picnic";
	
	public static final String DEVELOPER_PROVIDER_NAME_FOR_IOS = "cognito-identity.amazonaws.com";
	
	/**
	 * 
	 */
	public static final Region REGION = Region.getRegion(Regions.AP_NORTHEAST_1);
	
	/**
	 * 
	 */
	public static final String SESSION_DURATION = "86400";
	
	/**
	 * Need to sign password
	 */
	public static final String APP_NAME = getAppName();
	
	/**
	 * 
	 */
	public static final String USER_TABLE = "picnic_users";
	
	public static final String USERNAME_INDEX = "username-index";
	
	public static final String NICKNAME_INDEX = "nickname-index";
	
	/**
	 * attritubtes in user table
	 */
	public static final String ATTRIBUTE_USERNAME = "username";
	public static final String ATTRIBUTE_HASHED_PASSWORD = "hashed_password";
	public static final String ATTRIBUTE_USERID = "userid";
	public static final String ATTRIBUTE_STATUS = "status";
	public static final String ATTRIBUTE_REGISTERED_TIME = "registered_time";
	public static final String ATTRIBUTE_NICKNAME = "nickname";
	public static final String ATTRIBUTE_GENDER = "gender";
	public static final String ATTRIBUTE_BIRTH = "birth";
	public static final String ATTRIBUTE_PHONE = "phone";
	public static final String ATTRIBUTE_PHOTOS = "photos";
	public static final String ATTRIBUTE_BALLOON = "balloon";
	public static final String ATTRIBUTE_ENDPOINT_ARN = "EndpointARN";
	public static final String ATTRIBUTE_NEW_LIKE_COUNT = "NewLikeCount";
	public static final String ATTRIBUTE_NEW_SEND_COUNT = "NewSendCount";
	public static final String ATTRIBUTE_NEW_RECEIVE_COUNT = "NewReceiveCount";
	
	// UserInfo Status
	public static final String USER_STATUS_ENABLED = "status_enable";
	public static final String USER_STATUS_VERIFIED = "status_verified";
	public static final String USER_STATUS_NOT_VERIFIED = "status_not_verified";
	public static final String USER_STATUS_NOT_INPUT_INFO = "status_not_input_info";
		
	/**
	 * attributes in device table
	 */
	public static final String DEVICE_TABLE = "picnic_devices";
	
	/**
	 * attributes in product table
	 */
	public static final String PRODUCT_TABLE = "product_db";
    public static final String INDEX_STATUS_UPDATED_TIME = "";
    public static final String INDEX_STATUS_LIKECOUNT = "";
    public static final String INDEX_MAIN_CATEGORY = "MainCategory-UpdatedTime-index";

    public static final String ATTRIBUTE_PRODUCT_ID = "ProductId";
    public static final String ATTRIBUTE_TITLE_KOR = "TitleKor";
    public static final String ATTRIBUTE_TITLE_ENG = "TitleEng";
    public static final String ATTRIBUTE_SUB_TITLE = "SubTitle";
    public static final String ATTRIBUTE_PRODUCT_REGION = "ProductRegion";
    public static final String ATTRIBUTE_MAIN_CATEGORY = "MainCategory";
    public static final String ATTRIBUTE_SUB_CATEGORY = "SubCategory";
    public static final String ATTRIBUTE_DESCRIPTION = "Description";
    public static final String ATTRIBUTE_TELEPHONE = "Telephone";
    public static final String ATTRIBUTE_ADDRESS = "Address";
    public static final String ATTRIBUTE_PRICE = "Price";
    public static final String ATTRIBUTE_PARKING = "Parking";
    public static final String ATTRIBUTE_OPERATION_TIME = "OperationTime";
    public static final String ATTRIBUTE_FACEBOOK = "Facebook";
    public static final String ATTRIBUTE_BLOG = "Blog";
    public static final String ATTRIBUTE_INSTAGRAM = "Instagram";
    public static final String ATTRIBUTE_HOMEPAGE = "Homepage";
    public static final String ATTRIBUTE_ETC = "Etc";
    public static final String ATTRIBUTE_IMAGE_COUNT = "ImageCount";
    public static final String ATTRIBUTE_UPDATED_TIME = "UpdatedTime";
    public static final String ATTRIBUTE_LIKECOUNT = "LikeCount";
    public static final String ATTRIBUTE_PRODUCT_STATUS = "Status";
    
    /**
     * attributes product LIKE time
     */
    public static final String PRODUCT_LIKE_TABLE = "product_like_state";
    
    /**
     * attributes in like table
     */
    public static final String LIKE_TABLE = "picnic_like_db";
    public static final String INDEX_LIKE_PRODUCTID_UPDATEDTIME = "ProductId-UpdatedTime-index";
    public static final String INDEX_LIKE_USERID_UPDATEDTIME = "UserId-UpdatedTime-index";

    public static final String ATTRIBUTE_LIKE_USER_ID = "UserId";
    public static final String ATTRIBUTE_LIKE_NICKNAME = "Nickname";
    public static final String ATTRIBUTE_LIKE_PRODUCT_ID = "ProductId";
    public static final String ATTRIBUTE_LIKE_GENDER = "Gender";
    public static final String ATTRIBUTE_LIKE_PRODUCT_DESC = "ProductDesc";
    public static final String ATTRIBUTE_LIKE_PRODUCT_TITLE = "ProductTitle";
    public static final String ATTRIBUTE_LIKE_UPDATED_TIME = "UpdatedTime";
    public static final String ATTRIBUTE_LIKE_READ_TIME = "ReadTime";
    public static final String ATTRIBUTE_LIKE_HAS_NEW = "HasNew";
    public static final String ATTRIBUTE_LIKE_ID = "LikeId";
    
    /**
     * Like read state
     */
    public static final String LIKE_READ_STATE_TALBE = "like_read_state";
    public static final String INDEX_READSTATE_PRODUCTID_USERID = "ProductId-UserId-index";

    public static final String ATTRIBUTE_LIKE_READ_STATE_LIKE_ID = "LikeId";
    public static final String ATTRIBUTE_LIKE_READ_STATE_USERID = "UserId";
    public static final String ATTRIBUTE_LIKE_READ_STATE_PRODUCTID = "ProductId";
    public static final String ATTRIBUTE_LIKE_READ_STATE_LIKEUSERID = "LikeUserId";
    public static final String ATTRIBUTE_LIKE_READ_STATE_READTIME = "ReadTime";
	
	/**
	 * attributes in propose table
	 */
	public static final String PROPOSE_TABLE = "picnic_propose_db";
    public static final String INDEX_PROPOSERID_UPDATEDTIME = "ProposerId-UpdatedTime-index";
    public static final String INDEX_PROPOSEEID_UPDATEDTIME = "ProposeeId-UpdatedTime-index";

    public static final String ATTRIBUTE_PROPOSER_ID = "ProposerId";
    public static final String ATTRIBUTE_PROPOSEE_ID = "ProposeeId";
    public static final String ATTRIBUTE_PROPOSER_NICKNAME = "ProposerNickname";
    public static final String ATTRIBUTE_PROPOSEE_NICKNAME = "ProposeeNickname";
    public static final String ATTRIBUTE_PROPOSE_PRODUCTID = "ProposeProductId";
    public static final String ATTRIBUTE_PROPOSE_PRODUCT_TITLE = "ProposeProductTitle";
    public static final String ATTRIBUTE_PROPOSE_UPDATED_TIME = "UpdatedTime";
    public static final String ATTRIBUTE_PROPOSE_STATUS = "Status";
    
    public static final int STATUS_MAIL_UNREAD = 0;
    public static final int STATUS_MAIL_READ = 1;

    public static final String PROPOSE_STATUS_NOT_MADE = "notMade";
    public static final String PROPOSE_STATUS_MADE = "Made";
    public static final String PROPOSE_STATUS_REJECT = "reject";
    public static final String PROPOSE_STATUS_DELETE = "delete";
    public static final String PROPOSE_STATUS_ALREADY_MADE = "alreadyMade";
    
    /**
     * SEND MAIL 
     */
	public static final String SEND_MAIL_TABLE = "send_mail_db";
	public static final String SEND_MAIL_INDEX_USERID_RESPONSETIME = "UserId-ResponseTime-index";
	public static final String RECEIVE_MAIL_TABLE = "receive_mail_db";
	public static final String RECEIVE_MAIL_INDEX_USERID_RESPONSETIME = "UserId-ResponseTime-index";
	public static final String ATTRIBUTE_MAIL_ID = "Id";
	public static final String ATTRIBUTE_MAIL_USER_ID = "UserId";
	public static final String ATTRIBUTE_MAIL_UPDATED_TIME = "UpdatedTime";
	public static final String ATTRIBUTE_MAIL_RESPONSETIME = "ResponseTime";
	public static final String ATTRIBUTE_MAIL_SENDER_ID = "SenderId";
	public static final String ATTRIBUTE_MAIL_RECEIVER_ID = "ReceiverId";
	public static final String ATTRIBUTE_MAIL_SENDER_NICKNAME = "SenderNickname";
	public static final String ATTRIBUTE_MAIL_RECEIVER_NICKNAME = "ReceiverNickname";
	public static final String ATTRIBUTE_MAIL_PRODUCT_ID = "ProductId";
	public static final String ATTRIBUTE_MAIL_PRODUCT_TITLE = "ProductTitle";
	public static final String ATTRIBUTE_MAIL_STATUS = "ProposeStatus";
	public static final String ATTRIBUTE_MAIL_IS_READ = "IsRead";
    
	public static final String TYPE_NOTIFICATION_LIKE = "like";
	public static final String TYPE_NOTIFICATION_RECEIVE_MAIL = "receiveMail";
	public static final String TYPE_NOTIFICATION_SEND_MAIL = "sendMail";
	public static final String TYPE_NOTIFICATION_LOGOUT = "logout";
    
	/**
	 * 
	 */
	public static final String GCM_SERVER_API_KEY = "AIzaSyCLdODKLzgV59gNIjWR7Le1IN9VBvGtFnY";
	
	public static final String APNS_CERTIFICATE = "-----BEGIN CERTIFICATE-----\n" + 
												"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\n" +
												"-----END CERTIFICATE-----";
	public static final String APNS_PRIVATEKEY = "-----BEGIN PRIVATE KEY-----\n" +
												"MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCjWh9dMhmF4EFJKqVaUfrlgIImWVMT5UXpPNY2RE2Q/2QkkBdDsRdkvrnibsiohjwbMuyKSYzAFTqEjn6R+q2Er0iYC8Sjbu9uoJRnzJbfARj09WmUJw6AS8qmDprByELl65FQS2fpF9kCqwM1CisPiGE+ZzpiwAMjwfVca1DpWA8KXv4alrP9hcsEfJhfyErPFtyO/0FonQ3fuLtxhIIFS3zg/sOGr6hDVM83NdW1dTnKDyAFOI3XdS8dv/jDkf6QyD6+GkBGJjnXG/Ys0q0mMg56NF620uecsAQWhPTNqQAa2P1nHWaeRu8IE2KIH/e9nYP2LoEkUFUuNIwd8DipAgMBAAECggEAWTojI674Dg6e2q52v81D9RSrAvOL07RK6coXYHESxX8bXnVun8Ud+CPOOYc2CAe60UiRuD/QqCNwYXPMY0TAf1eXmaXj9Tnlg1InfkNcrddVoIhk8U4cN6wut2IG2o7fK+Pu7iMiUUp7eV+cUt6JAuUF6SZN2B72GBQcSC0+dxaI9Cw+tA9Lf65Y4CbikR6pptlvkAyz5FsPJbr5R0IIriF4kLCuzHZHLSCeBJlMur4mfixoIIlQhwFDRlfjEGMT7siep15hSTbzDkBpHx19iTga8vmygoHCWsaRPKel3N5XlCTU7JXBlldfw+z+jMGAXUQ5TXxTROmV+R6x42ozWQKBgQDX9KU5+jjmHCFCPx8G2uoFlvgdcgx55isLRjCWKKLn9CusoLg65R/IwRdF+lw4M4r+Bt5zUui7qdZEKez37lrWpUhRz/kutZ4LfzQNyaFK6nVjvvIX+mdg7cz3VkarmkYlPJlF7AmYf7iGnYOYF8G6ysKcVumq3TlZe2uitZo7ywKBgQDBpGYIfXYUJDqyDwK6cDiSGTgPGKCKX4pe7k09p4o6e3ZLqn23YLFM8PUSUXb9fbfSm7zLNhWhhNa5WrGRfHQKDq4KbAbGpq9Ph+T+mkRxu26aUZ2+i+AgxjF/sPKX8ktLqxYEclHDImWS0/59EHg7YlpdRsDn1RB4H/cSq1722wKBgAboHCCukANMAwRLYIKnnbRwD59hdmSvp9DEfelPg2ZHuN3chgfcLBgPlN74Etr5Swi0z4iArlDSLbggXyPOy7jxsE5tiFqmFFfpyZBnUnFC7l4yTSvXJldDQ7rlV31FgdjPB1flJKlr7Erh3sOOfvOqaG73mKGOUbc660ZY6IXhAoGALNlx1GeSkKmveowlep8BSz1SEZUHoeTs/H2/IBeM2Wy9AltzkKRxjDuG135My/txNvNhL8PQEL/ep8GMvQCRM78gqkNI8nuqwQXdkOaUEa9yt4VNvtNQzuZ4knWeSnkQ3W67w73imAqAchhtqfPFlSN7gH0hstBTZy8oH4fXj98CgYEAqNAVa3Vi/hzHIRXtjTyWxNYeAQoI2qJM1+mdmtuuVp3ljy/wo9wCxw1ws+GJWQd34VtzwogI1QHXf6qAktTnhXcIN/Wv0Bs75r5Rr11r0V9gylqPQsHE66XwCSYYvl/+GoYVAU2pTyLrBuzytUzOiDxBciwq3tA0oeFxTaXoRJ4=\n" +
												"-----END PRIVATE KEY-----";
	
	/**
	 * attributes in device table
	 */
	public static final String ATTRIBUTE_UID = "uid";
	public static final String ATTRIBUTE_KEY = "key";
	
	private static String getAppName() {
		return System.getProperty("APP_NAME", "Wekend").toLowerCase();
	}
	
	/*
	private static String getRegion() {
		return System.getProperty("REGION", "ap-northeast-1");
	}
	*/
}
