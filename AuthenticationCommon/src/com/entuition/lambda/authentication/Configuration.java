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
												"MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCihcdtK7HhCkocefMeve8iQd/33z2dMS1jL6VvoaHt4vqFjmWH6ADCMlGZqtbrIFCgmSld2Ks2Z9Z2aMInlfD1ctgOxbceO6sO47cptIb7aQq0f/ll3aibr1iLOaMHMqixD8+CmWqysShE9moXeYnumfQakRzcfNnlCqBGlljGmRlv/PTKajJyX+HdaQFJYxulhKunEf3Xh7FGc2Na4XtKv0eFmJZsnZZ639C2qaPsztWtpKU1s1PwywNA2ud4X9NPXm8UI+2Q51fV1ZBi54fUorfqOceLv1B3ZrWVzRLlbvZjaGvC0IiP268dq4JQm7oUcD8/DzsHPBh3A16ND3fHAgMBAAECggEAH1ht/kBPLngtkxByM5uuY40RBV2pJIg3mMm3vrAN6zO73cZ5Dp1QiCPsWRb6HJ8+7bqcn90KKJQeaPgLelcRAkYEJdU8XDhl5ZlF2mVXI2Y/ClEkaOE3g89t5GxZZSoAHyZNf4LXcV95xNNB4wUt9Tb8PaN/TYdG/0XtY6/br3HJB2hEgxMOj8jiKofuCMIpJHroLy3+FhIVRJgZtypakxawh3UBaIS+IidMknxssft/wzM7XnEKgAaaAA2UTCfvYqEGcpB2PGzPyYQqfDcmkN0w/BRcICdIJ8ZhPVt9lrn8rJgcfwzVpXgNjRDMSt+GIhu5EBo6rN0x8IAr3vjkAQKBgQDX2YdKhYTUvzeNvSex1DOLhdlJGinRu7y3ov6uoxoy6Ca8C+q29LEt3MD4DONjaVhsBlo9Q3KfNx4pZFf/Dge31zvIrXJYjfMjIPnKroDFOAiHicKD9j8UeeRbcQmvxaiDk1Qfy+ino+JLtg5M9qJYEelxQ8uzFO9fhrBOT/SyEQKBgQDAwOJOAk/yDraMHhs5FZY/n+lYn+abf+vMddFrLFqwx/+FCCY9fken/e3wxM/9d6F54eIXOUrk/Ls68Rja7qIUxjkmVBU2ILRx0upGBWGE4TmVne1gVUUWvllW09EZhQyy129+sgSfva54sDwgsyw9CPhR9GRdrR4VVwF55YO0VwKBgGK5bKbKEOoZl3gttvUgAH9lNe+4lr4F5xf65g/MK9Ry2ByUOsN5gXxfhqcthedXLIkkEqwIqGloFVSLP7A5o5ATUpXDtxQHwNglYfqw+9a4fTY47Q3cWBeudKLaPbr2Zc/gV86/PedHDt7gIwRSngg4ERSsFIQRJDcF7Ke07E9xAoGADuremMr1BPa+ifmKgdB54FKv7HvzjPVJenljy7eTDTttOw52Zyvuv8GXOJsCvcsUZCpnJQT6Ft64Xsdp9Qei45izs5OuUJIXZPe9yc2AzpffrQSb6mivACObJm0eMatqYCzsJBIC6Zn0dsKs5Jr5QYAAN9eCLP3I5j3lj1mD5+ECgYAM9nTU3yjZD0kLLmxLzosvTWZHw0SJGqoMvqNtyP/tEwrr0pBCGVXKT2YpjzNyTTijtLi+dl2wycah7VAsDJTBxxqc9BEzdCQC82J6ypcI3oSgnUPLZ+Okau86o9WqKY21Bcs9CMlwQebRj9U/rTGhfpQDTbSQ1Ygb5jKaFoCRCw==\n" +
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
