package com.entuition.lambda.authentication;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.entuition.lambda.authentication.exception.DataAccessException;

public class DeviceAuthentication {
	
	private static final String DEVICE_TABLE = Configuration.DEVICE_TABLE;
	private static final String DEVICE_INDEX_USERNAME_UID = "username-uid-index";
	
	private static final String ATTRIBUTE_USERNAME = Configuration.ATTRIBUTE_USERNAME;
	private static final String ATTRIBUTE_UID = Configuration.ATTRIBUTE_UID;
	private static final String ATTRIBUTE_KEY = Configuration.ATTRIBUTE_KEY;
	private static final String ATTRIBUTE_USERID = Configuration.ATTRIBUTE_USERID;
	
	private final AmazonDynamoDB ddbClient;
	// TODO : change ddbclient to DynamoDBMapper
	private final DynamoDBMapper mapper;
	
	public DeviceAuthentication() {
		ddbClient = new AmazonDynamoDBClient();
		ddbClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
		mapper = new DynamoDBMapper(ddbClient);
		
		try {
			if (!doesTableExists(DEVICE_TABLE)) {
				createDeviceTable();
			}			
		} catch (DataAccessException e) {
			throw new RuntimeException("Failed to create device table.", e);
		}
	}
	
	/**
     * Returns device info for given device ID (UID)
     * 
     * @param uid Unique device identifier
     * @return device info for the given uid
     */
    public DeviceInfo getDeviceInfo(String uid) throws DataAccessException {
        try {
            return mapper.load(DeviceInfo.class, uid);
        } catch (AmazonClientException e) {
        	throw new DataAccessException("Failed to get Device : " + uid, e);
        }
    }

    /**
     * 
     * @param uid
     * @param key
     * @return
     */
    public boolean authenticateDevice(String uid, String key) throws DataAccessException {
    	DeviceInfo device = getDeviceInfo(uid);
    	return device != null && key.equals(device.getKey());
    }
    
    /**
     * register Device
     * @param uid
     * @param key
     * @param username
     * @return
     */
    public boolean registerDevice(String uid, String key, String username, String userid) throws DataAccessException {
    	DeviceInfo device = getDeviceInfo(uid);
    	if (device != null && !username.equals(device.getUsername())) {
    		return false;
    	}
    	
    	storeDevice(uid, key, username, userid);
    	return true;
    }
    
    public List<DeviceInfo> retrieveDevice(String username) throws DataAccessException {
    	
    	System.out.println("retrieveDevice > username : " + username);
    	
    	DeviceInfo hashKey = new DeviceInfo();
    	hashKey.setUsername(username);
    	
    	DynamoDBQueryExpression<DeviceInfo> queryExpression = new DynamoDBQueryExpression<DeviceInfo>()
    			.withIndexName(DEVICE_INDEX_USERNAME_UID)
    			.withHashKeyValues(hashKey)
    			.withConsistentRead(false);
    	try {
    		return mapper.query(DeviceInfo.class, queryExpression);
    	} catch (Exception e) {
    		System.out.println("retrieveDevice > error : " + e.toString());
    	}
    	
    	return null;
    }
    
    /**
     * store device info to Device table
     * @param uid
     * @param key
     * @param username
     */
    protected void storeDevice(String uid, String key, String username, String userid) throws DataAccessException {
    	
    	DeviceInfo deviceInfo = new DeviceInfo();
    	deviceInfo.setUid(uid);
    	deviceInfo.setKey(key);
    	deviceInfo.setUsername(username);
    	deviceInfo.setUserid(userid);
    	
    	try {
    		mapper.save(deviceInfo);
    	} catch (AmazonClientException e) {
        	throw new DataAccessException(String.format("Failed to store device uid: %s; key: %s; username: %s", uid,
                    key, username) + ", exception : " + e.toString(), e);
        }
    }
    
    protected void createDeviceTable() throws DataAccessException {
    	
    	ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
    			.withReadCapacityUnits(10L)
    			.withWriteCapacityUnits(5L);
    	
    	CreateTableRequest createTableRequest = mapper.generateCreateTableRequest(DeviceInfo.class);
    	createTableRequest.setProvisionedThroughput(provisionedThroughput);
    	
    	try {
    		ddbClient.createTable(createTableRequest);
    	} catch (AmazonClientException e) {
    		throw new DataAccessException("Failed to create table : " + DEVICE_TABLE, e);
    	}
    }
    
    protected boolean doesTableExists(String tableName) throws DataAccessException {
    	try {
    		DescribeTableRequest request = new DescribeTableRequest().withTableName(tableName);
    		DescribeTableResult result = ddbClient.describeTable(request);
    		return "ACTIVE".equals(result.getTable().getTableStatus());
    	} catch (ResourceNotFoundException e) {
    		return false;
    	} catch (AmazonClientException e) {
    		throw new DataAccessException("Failed to get status of table : " + tableName, e);
    	}
    }
    
    /**
     * 
     * @param uid
     */
    public void deleteDevice(String uid) throws DataAccessException {
    	try {
    		DeviceInfo deviceInfo = mapper.load(DeviceInfo.class, uid);
    		mapper.delete(deviceInfo);
    	} catch (AmazonClientException e) {
    		throw new DataAccessException("Failed to delete device: " + uid, e);
    	}
    }
    
	/**
     * Checks to see if the device id (UID) already exist in the device table
     * 
     * @param uid Unique device identifier
     * @return true if the given UID already exist, false otherwise
     * @throws DataAccessException
     */
    public boolean checkUidExists(String uid) throws DataAccessException {
        return getDeviceInfo(uid) != null;
    }

    @DynamoDBTable(tableName = DEVICE_TABLE)
    public static class DeviceInfo {
    	
        private String uid;
        private String key;
        private String username;
        private String userid;

        @DynamoDBHashKey(attributeName = ATTRIBUTE_UID)
        @DynamoDBIndexRangeKey(globalSecondaryIndexName = DEVICE_INDEX_USERNAME_UID)
        public String getUid() {
            return uid;
        }
        
        public void setUid(String uid) {
        	this.uid = uid;
        }

        @DynamoDBAttribute(attributeName = ATTRIBUTE_KEY)
        public String getKey() {
            return key;
        }
        
        public void setKey(String key) {
        	this.key = key;
        }

        @DynamoDBIndexHashKey(globalSecondaryIndexName = DEVICE_INDEX_USERNAME_UID)
        @DynamoDBAttribute(attributeName = ATTRIBUTE_USERNAME)
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
        	this.username = username;
        }

        @DynamoDBAttribute(attributeName = ATTRIBUTE_USERID)
		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}
    }
}
