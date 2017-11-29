package com.entuition.lambda.data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.entuition.lambda.authentication.Configuration;

@DynamoDBTable(tableName = Configuration.PRODUCT_TABLE)
public class ProductInfo {
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

    @DynamoDBHashKey(attributeName = Configuration.ATTRIBUTE_PRODUCT_ID)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_TITLE_KOR)
    public String getTitleKor() {
        return titleKor;
    }

    public void setTitleKor(String titleKor) {
        this.titleKor = titleKor;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_TITLE_ENG)
    public String getTitleEng() {
        return titleEng;
    }

    public void setTitleEng(String titleEng) {
        this.titleEng = titleEng;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_SUB_TITLE)
    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_PRODUCT_REGION)
    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = Configuration.INDEX_MAIN_CATEGORY)
    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_MAIN_CATEGORY)
    public int getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(int mainCategory) {
        this.mainCategory = mainCategory;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_SUB_CATEGORY)
    public int getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(int subCategory) {
        this.subCategory = subCategory;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_DESCRIPTION)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_TELEPHONE)
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_ADDRESS)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_PRICE)
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_OPERATION_TIME)
    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_PARKING)
    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_FACEBOOK)
    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_BLOG)
    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_INSTAGRAM)
    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_HOMEPAGE)
    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_LIKECOUNT)
    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_ETC)
    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = Configuration.INDEX_MAIN_CATEGORY)
    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_UPDATED_TIME)
    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_IMAGE_COUNT)
    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    @DynamoDBAttribute(attributeName = Configuration.ATTRIBUTE_PRODUCT_STATUS)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}