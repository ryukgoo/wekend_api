package com.entuition.lambda.function;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "product_db")
public class ProductInfo {

	public static final String INDEX_MAIN_CATEGORY = "MainCategory-UpdatedTime-index";
	
    private static final String ATTRIBUTE_PRODUCT_ID = "ProductId";
    private static final String ATTRIBUTE_TITLE_KOR = "TitleKor";
    private static final String ATTRIBUTE_TITLE_ENG = "TitleEng";
    private static final String ATTRIBUTE_SUB_TITLE = "SubTitle";
    private static final String ATTRIBUTE_PRODUCT_REGION = "ProductRegion";
    private static final String ATTRIBUTE_MAIN_CATEGORY = "MainCategory";
    private static final String ATTRIBUTE_SUB_CATEGORY = "SubCategory";
    private static final String ATTRIBUTE_DESCRIPTION = "Description";
    private static final String ATTRIBUTE_TELEPHONE = "Telephone";
    private static final String ATTRIBUTE_ADDRESS = "Address";
    private static final String ATTRIBUTE_SUB_ADDRESS = "SubAddress";
    private static final String ATTRIBUTE_PRICE = "Price";
    private static final String ATTRIBUTE_PARKING = "Parking";
    private static final String ATTRIBUTE_OPERATION_TIME = "OperationTime";
    private static final String ATTRIBUTE_FACEBOOK = "Facebook";
    private static final String ATTRIBUTE_BLOG = "Blog";
    private static final String ATTRIBUTE_INSTAGRAM = "Instagram";
    private static final String ATTRIBUTE_HOMEPAGE = "Homepage";
    private static final String ATTRIBUTE_ETC = "Etc";
    private static final String ATTRIBUTE_IMAGE_COUNT = "ImageCount";
    private static final String ATTRIBUTE_UPDATED_TIME = "UpdatedTime";
    private static final String ATTRIBUTE_LIKECOUNT = "LikeCount";
    private static final String ATTRIBUTE_PRODUCT_STATUS = "ProductStatus";
    
	private int id;
    private String titleKor;
    private String titleEng;
    private String subTitle;
    private int region;
    private int mainCategory;
    private int subCategory;
    private String description;
    private String telephone;
    private String address;
    private String subAddress;
    private String price;
    private String operationTime;
    private String parking;
    private String facebook;
    private String blog;
    private String instagram;
    private String homepage;
    private String etc;
    private int imageCount;
    private int likeCount;
    private String updatedTime;
    private String status;

    public ProductInfo() { }

	public ProductInfo(int id, String titleKor, String titleEng, String subTitle, int region, int mainCategory,
			int subCategory, String description, String telephone, String address, String subAddress, String price,
			String operationTime, String parking, String facebook, String blog, String instagram, String homepage,
			String etc, int imageCount, int likeCount, String updatedTime, String status) {
		super();
		this.id = id;
		this.titleKor = titleKor;
		this.titleEng = titleEng;
		this.subTitle = subTitle;
		this.region = region;
		this.mainCategory = mainCategory;
		this.subCategory = subCategory;
		this.description = description;
		this.telephone = telephone;
		this.address = address;
		this.subAddress = subAddress;
		this.price = price;
		this.operationTime = operationTime;
		this.parking = parking;
		this.facebook = facebook;
		this.blog = blog;
		this.instagram = instagram;
		this.homepage = homepage;
		this.etc = etc;
		this.imageCount = imageCount;
		this.likeCount = likeCount;
		this.updatedTime = updatedTime;
		this.status = status;
	}

	@DynamoDBHashKey(attributeName = ATTRIBUTE_PRODUCT_ID)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_TITLE_KOR)
    public String getTitleKor() {
        return titleKor;
    }

    public void setTitleKor(String titleKor) {
        this.titleKor = titleKor;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_TITLE_ENG)
    public String getTitleEng() {
        return titleEng;
    }

    public void setTitleEng(String titleEng) {
        this.titleEng = titleEng;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_SUB_TITLE)
    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_PRODUCT_REGION)
    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = INDEX_MAIN_CATEGORY)
    @DynamoDBAttribute(attributeName = ATTRIBUTE_MAIN_CATEGORY)
    public int getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(int mainCategory) {
        this.mainCategory = mainCategory;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_SUB_CATEGORY)
    public int getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(int subCategory) {
        this.subCategory = subCategory;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_DESCRIPTION)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_TELEPHONE)
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_ADDRESS)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_SUB_ADDRESS)
    public String getSubAddress() {
		return subAddress;
	}

	public void setSubAddress(String subAddress) {
		this.subAddress = subAddress;
	}

	@DynamoDBAttribute(attributeName = ATTRIBUTE_PRICE)
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_OPERATION_TIME)
    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_PARKING)
    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_FACEBOOK)
    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_BLOG)
    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_INSTAGRAM)
    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_HOMEPAGE)
    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_LIKECOUNT)
    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_ETC)
    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = INDEX_MAIN_CATEGORY)
    @DynamoDBAttribute(attributeName = ATTRIBUTE_UPDATED_TIME)
    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_IMAGE_COUNT)
    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    @DynamoDBAttribute(attributeName = ATTRIBUTE_PRODUCT_STATUS)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String desctiprion() {
    	
    	String desc = "";
    	
    	desc = "ProductId : " + this.id;
    	desc += "\ntitleKor : " + this.titleKor;
    	desc += "\ntitleEng : " + this.titleEng;
    	
    	return desc;
    }
}
