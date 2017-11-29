package com.entuition.lambda.authentication;

import java.util.List;
import java.util.Set;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.entuition.lambda.authentication.exception.DataAccessException;

public class UserAuthentication {

	private static final String USER_TABLE = Configuration.USER_TABLE;
	private static final String USERNAME_INDEX = Configuration.USERNAME_INDEX;
	private static final String NICKNAME_INDEX = Configuration.NICKNAME_INDEX;
	
	private static final String ATTRIBUTE_USERNAME = Configuration.ATTRIBUTE_USERNAME;
	private static final String ATTRIBUTE_HASHED_PASSWORD = Configuration.ATTRIBUTE_HASHED_PASSWORD;
	private static final String ATTRIBUTE_USERID = Configuration.ATTRIBUTE_USERID;
	private static final String ATTRIBUTE_STATUS = Configuration.ATTRIBUTE_STATUS;
	private static final String ATTRIBUTE_REGISTERED_TIME = Configuration.ATTRIBUTE_REGISTERED_TIME;
	private static final String ATTRIBUTE_NICKNAME = Configuration.ATTRIBUTE_NICKNAME;
	private static final String ATTRIBUTE_GENDER = Configuration.ATTRIBUTE_GENDER;
	private static final String ATTRIBUTE_BIRTH = Configuration.ATTRIBUTE_BIRTH;
	private static final String ATTRIBUTE_PHONE = Configuration.ATTRIBUTE_PHONE;
	private static final String ATTRIBUTE_PHOTOS = Configuration.ATTRIBUTE_PHOTOS;
	private static final String ATTRIBUTE_BALLOON = Configuration.ATTRIBUTE_BALLOON;
	private static final String ATTRIBUTE_ENDPOINT_ARN = Configuration.ATTRIBUTE_ENDPOINT_ARN;
	private static final String ATTRIBUTE_UPDATED_TIME = Configuration.ATTRIBUTE_UPDATED_TIME;
	private static final String ATTRIBUTE_NEW_LIKE_COUNT = Configuration.ATTRIBUTE_NEW_LIKE_COUNT;
	private static final String ATTRIBUTE_NEW_SEND_COUNT = Configuration.ATTRIBUTE_NEW_SEND_COUNT;
	private static final String ATTRIBUTE_NEW_RECEIVE_COUNT = Configuration.ATTRIBUTE_NEW_RECEIVE_COUNT;
	
	private final AmazonDynamoDB ddbClient;
	private final DynamoDBMapper mapper;
	
	public UserAuthentication() {
		ddbClient = new AmazonDynamoDBClient();
		ddbClient.setRegion(Configuration.REGION);
		mapper = new DynamoDBMapper(ddbClient);
		
		try {
			if (!doesTableExist(USER_TABLE)) {
				createIdentityTable();
			}
		} catch (DataAccessException e) {
			throw new RuntimeException("Failed to create user table", e);
		}
	}
	
	public UserInfo getUserInfoByUsername(String username) throws DataAccessException {
		
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername(username);
		
		DynamoDBQueryExpression<UserInfo> queryExpression = new DynamoDBQueryExpression<UserInfo>()
				.withIndexName(USERNAME_INDEX)
				.withConsistentRead(false)
				.withHashKeyValues(userInfo);
		
		try {
			List<UserInfo> userInfos = mapper.query(UserInfo.class, queryExpression);
			for (int i = 0 ; i < userInfos.size() ; i ++) {
				UserInfo userInfoItem = userInfos.get(i);
				if (userInfoItem != null && userInfoItem.getUsername().equals(username)) {
					return mapper.load(UserInfo.class, userInfoItem.getUserid());
				}
			}
			
			return null;
		} catch (AmazonClientException e) {
			throw new DataAccessException("Failed to get item username : " + username + ", exception : " + e.toString(), e);
		}
	}
	
	public UserInfo getUserInfoByUserId(String userid) throws DataAccessException {
		try {
			return mapper.load(UserInfo.class, userid);
		} catch (AmazonClientException e) {
			throw new DataAccessException("Failed to get item userid : " + userid, e);
		}
	}
	
	public String registerUser(String username, String password, String nickname, String gender, int birth, String phone) throws DataAccessException {
		if (checkUsernameExists(username)) {
			return null;
		}
		return storeUser(username, password, nickname, gender, birth, phone);
	}
	
	public void deleteUser(String userid) throws DataAccessException {
		
		try {
			UserInfo userInfo = getUserInfoByUserId(userid);
			mapper.delete(userInfo);
		} catch (AmazonClientException e) {
			throw new DataAccessException("Failed to delete username : " + userid, e);
		}
	}
	
	public void updateUser(UserInfo userInfo) throws DataAccessException {
		try {
			mapper.save(userInfo);
		} catch (AmazonClientException e) {
			throw new DataAccessException("Failed to update user : " + userInfo.getUserid(), e);
		}
	}
	
	protected String storeUser(String username, String password, String nickname, String gender, int birth, String phone) throws DataAccessException {
		if (null == username || null == password) {
			return null;
		}
		
		String hashedSaltedPassword = Utilities.getSaltedPassword(username, password);
		
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername(username);
		userInfo.setHashedPassword(hashedSaltedPassword);
		userInfo.setStatus("ACTIVE");
		userInfo.setRegisteredTime(Utilities.getTimestamp());
		
		userInfo.setNickname(nickname);
		userInfo.setGender(gender);
		userInfo.setBirth(birth);
		userInfo.setPhone(phone);
		userInfo.setBalloon(1000);
		
		userInfo.setNewLikeCount(0);
		userInfo.setNewSendCount(0);
		userInfo.setNewReceiveCount(0);
		
		try {
			mapper.save(userInfo);
			return userInfo.getUserid();
		} catch (Exception e) {
			throw new DataAccessException("Failed to store user : " + username + ", exception : " + e.toString(), e);
		}
	}
	
	public boolean checkUsernameExists(String username) throws DataAccessException {
		return getUserInfoByUsername(username) != null;
	}
	
	protected void createIdentityTable() throws DataAccessException {
		
		CreateTableRequest createTableRequest = mapper.generateCreateTableRequest(UserInfo.class);
		
		ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
				.withReadCapacityUnits(10L)
				.withWriteCapacityUnits(5L);
		
		createTableRequest.setProvisionedThroughput(provisionedThroughput);
		
		GlobalSecondaryIndex usernameIndex = new GlobalSecondaryIndex()
				.withIndexName("username-index")
				.withProvisionedThroughput(new ProvisionedThroughput()
						.withReadCapacityUnits(10L)
						.withWriteCapacityUnits(1L))
				.withProjection(new Projection().withProjectionType(ProjectionType.KEYS_ONLY))
				.withKeySchema(new KeySchemaElement()
						.withAttributeName("username")
						.withKeyType(KeyType.HASH));
		
		createTableRequest.withGlobalSecondaryIndexes(usernameIndex);
		
		try {
			ddbClient.createTable(createTableRequest);
		} catch (AmazonClientException e) {
			throw new DataAccessException("Failed to create table : " + USER_TABLE, e);
		}
	}
	
	protected boolean doesTableExist(String tableName) throws DataAccessException {
		try {
			DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(USER_TABLE);
			DescribeTableResult describeTableResult = ddbClient.describeTable(describeTableRequest);
			return "ACTIVE".equals(describeTableResult.getTable().getTableStatus());
		} catch (ResourceNotFoundException e) {
			return false;
		} catch (AmazonClientException e) {
			throw new DataAccessException("Failed to get status of table : " + tableName, e);
		}
	}
	
	/**
     * A class that represents the item stored in user table.
     */
	@DynamoDBTable(tableName = USER_TABLE)
    public static class UserInfo {
		
		private String userid;
        private String username;
        private String hashedPassword;
        private String status;
        private String registeredTime;
        
        private String nickname;
        private String gender;
        private int birth;
        private String phone;
        private Set<String> photos;
        
        private int balloon;
        private String endpointARN;
        private String updatedTime;
        private int newLikeCount;
        private int newSendCount;
        private int newReceiveCount;
        
        @DynamoDBAutoGeneratedKey
        @DynamoDBHashKey(attributeName = ATTRIBUTE_USERID)
        public String getUserid() {
        	return userid;
        }
        
        public void setUserid(String userid) {
        	this.userid = userid;
        }
        
        @DynamoDBIndexHashKey(globalSecondaryIndexName = USERNAME_INDEX)
        @DynamoDBAttribute(attributeName = ATTRIBUTE_USERNAME)
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
        	this.username = username;
        }

        @DynamoDBAttribute(attributeName = ATTRIBUTE_HASHED_PASSWORD)
        public String getHashedPassword() {
            return hashedPassword;
        }
        
        public void setHashedPassword(String hashedPassword) {
        	this.hashedPassword = hashedPassword;
        }

        @DynamoDBAttribute(attributeName = ATTRIBUTE_STATUS)
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
        	this.status = status;
        }
        
        @DynamoDBAttribute(attributeName = ATTRIBUTE_REGISTERED_TIME)
        public String getRegisteredTime() {
        	return registeredTime;
        }
        
        public void setRegisteredTime(String registeredTime) {
        	this.registeredTime = registeredTime;
        }

        @DynamoDBIndexHashKey(globalSecondaryIndexName = NICKNAME_INDEX)
        @DynamoDBAttribute(attributeName = ATTRIBUTE_NICKNAME)
		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		@DynamoDBAttribute(attributeName = ATTRIBUTE_GENDER)
		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		@DynamoDBAttribute(attributeName = ATTRIBUTE_BIRTH)
		public int getBirth() {
			return birth;
		}

		public void setBirth(int birth) {
			this.birth = birth;
		}

		@DynamoDBAttribute(attributeName = ATTRIBUTE_PHONE)
		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		@DynamoDBAttribute(attributeName = ATTRIBUTE_PHOTOS)
		public Set<String> getPhotos() {
			return photos;
		}

		public void setPhotos(Set<String> photos) {
			this.photos = photos;
		}

		@DynamoDBAttribute(attributeName = ATTRIBUTE_BALLOON)
		public int getBalloon() {
			return balloon;
		}

		public void setBalloon(int balloon) {
			this.balloon = balloon;
		}

		@DynamoDBAttribute(attributeName = ATTRIBUTE_ENDPOINT_ARN)
		public String getEndpointARN() {
			return endpointARN;
		}

		public void setEndpointARN(String endpointARN) {
			this.endpointARN = endpointARN;
		}

		@DynamoDBAttribute(attributeName = ATTRIBUTE_UPDATED_TIME)
		public String getUpdatedTime() {
			return updatedTime;
		}

		public void setUpdatedTime(String updatedTime) {
			this.updatedTime = updatedTime;
		}

		@DynamoDBAttribute(attributeName = ATTRIBUTE_NEW_LIKE_COUNT)
		public int getNewLikeCount() {
			return newLikeCount;
		}

		public void setNewLikeCount(int newLikeCount) {
			this.newLikeCount = newLikeCount;
		}

		@DynamoDBAttribute(attributeName = ATTRIBUTE_NEW_SEND_COUNT)
		public int getNewSendCount() {
			return newSendCount;
		}

		public void setNewSendCount(int newSendCount) {
			this.newSendCount = newSendCount;
		}

		@DynamoDBAttribute(attributeName = ATTRIBUTE_NEW_RECEIVE_COUNT)
		public int getNewReceiveCount() {
			return newReceiveCount;
		}

		public void setNewReceiveCount(int newReceiveCount) {
			this.newReceiveCount = newReceiveCount;
		}
	}
}
