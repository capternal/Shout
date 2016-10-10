package com.shout.shoutapplication.main.Model;

/**
 * Created by CapternalSystems on 9/8/2016.
 */
public class MyShoutModel {

    private String SHOUT_ID;
    private String USER_ID;
    private String USER_NAME;
    private String PROFILE_IMAGE_URL;
    private String COMMENT_COUNT;
    private String LIKE_COUNT;
    private String ENGAGE_COUNT;
    private String SHOUT_TYPE;
    private String TITLE;
    private String DESCRIPTION;
    private int IS_SHOUT_LIKED;
    private String DATE_TIME;
    private String SHOUT_IMAGE;
    private int IS_SHOUT_HIDDEN;
    private int DEFAULT_PAGE_POSITION;
    private int DYNAMIC_HEIGHT;
    private float DYNAMIC_Y;
    private String strShoutImages;
    // ADDED FOR SHOUT DETAIL SCREEN
    private String IS_SEARCHABLE;
    private String SHOUT_LATITUDE;
    private String SHOUT_LONGITUDE;
    private String SHOUT_ADDRESS;
    private String SHOUT_CATEGORY_NAME;
    private String SHOUT_CATEGORY_ID;
    private String IS_HIDE;
    private String START_DATE;
    private String END_DATE;
    private String RE_SHOUT;
    private String CONTINUE_CHAT;
    private String DISTANCE;

    public MyShoutModel() {

    }

    public MyShoutModel(String SHOUT_ID, String USER_ID, String USER_NAME, String PROFILE_IMAGE_URL, String COMMENT_COUNT, String LIKE_COUNT, String ENGAGE_COUNT, String SHOUT_TYPE, String TITLE, String DESCRIPTION, int IS_SHOUT_LIKED, String DATE_TIME, String SHOUT_IMAGE, int IS_SHOUT_HIDDEN, int DEFAULT_PAGE_POSITION, int DYNAMIC_HEIGHT, float DYNAMIC_Y, String strShoutImages, String IS_SEARCHABLE, String SHOUT_LATITUDE, String SHOUT_LONGITUDE, String SHOUT_ADDRESS, String SHOUT_CATEGORY_NAME, String SHOUT_CATEGORY_ID, String IS_HIDE, String START_DATE, String END_DATE, String RE_SHOUT, String CONTINUE_CHAT, String DISTANCE) {
        this.SHOUT_ID = SHOUT_ID;
        this.USER_ID = USER_ID;
        this.USER_NAME = USER_NAME;
        this.PROFILE_IMAGE_URL = PROFILE_IMAGE_URL;
        this.COMMENT_COUNT = COMMENT_COUNT;
        this.LIKE_COUNT = LIKE_COUNT;
        this.ENGAGE_COUNT = ENGAGE_COUNT;
        this.SHOUT_TYPE = SHOUT_TYPE;
        this.TITLE = TITLE;
        this.DESCRIPTION = DESCRIPTION;
        this.IS_SHOUT_LIKED = IS_SHOUT_LIKED;
        this.DATE_TIME = DATE_TIME;
        this.SHOUT_IMAGE = SHOUT_IMAGE;
        this.IS_SHOUT_HIDDEN = IS_SHOUT_HIDDEN;
        this.DEFAULT_PAGE_POSITION = DEFAULT_PAGE_POSITION;
        this.DYNAMIC_HEIGHT = DYNAMIC_HEIGHT;
        this.DYNAMIC_Y = DYNAMIC_Y;
        this.strShoutImages = strShoutImages;
        this.IS_SEARCHABLE = IS_SEARCHABLE;
        this.SHOUT_LATITUDE = SHOUT_LATITUDE;
        this.SHOUT_LONGITUDE = SHOUT_LONGITUDE;
        this.SHOUT_ADDRESS = SHOUT_ADDRESS;
        this.SHOUT_CATEGORY_NAME = SHOUT_CATEGORY_NAME;
        this.SHOUT_CATEGORY_ID = SHOUT_CATEGORY_ID;
        this.IS_HIDE = IS_HIDE;
        this.START_DATE = START_DATE;
        this.END_DATE = END_DATE;
        this.RE_SHOUT = RE_SHOUT;
        this.CONTINUE_CHAT = CONTINUE_CHAT;
        this.DISTANCE = DISTANCE;
    }

    public String getSHOUT_ID() {
        return SHOUT_ID;
    }

    public void setSHOUT_ID(String SHOUT_ID) {
        this.SHOUT_ID = SHOUT_ID;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getPROFILE_IMAGE_URL() {
        return PROFILE_IMAGE_URL;
    }

    public void setPROFILE_IMAGE_URL(String PROFILE_IMAGE_URL) {
        this.PROFILE_IMAGE_URL = PROFILE_IMAGE_URL;
    }

    public String getCOMMENT_COUNT() {
        return COMMENT_COUNT;
    }

    public void setCOMMENT_COUNT(String COMMENT_COUNT) {
        this.COMMENT_COUNT = COMMENT_COUNT;
    }

    public String getLIKE_COUNT() {
        return LIKE_COUNT;
    }

    public void setLIKE_COUNT(String LIKE_COUNT) {
        this.LIKE_COUNT = LIKE_COUNT;
    }

    public String getENGAGE_COUNT() {
        return ENGAGE_COUNT;
    }

    public void setENGAGE_COUNT(String ENGAGE_COUNT) {
        this.ENGAGE_COUNT = ENGAGE_COUNT;
    }

    public String getSHOUT_TYPE() {
        return SHOUT_TYPE;
    }

    public void setSHOUT_TYPE(String SHOUT_TYPE) {
        this.SHOUT_TYPE = SHOUT_TYPE;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public int getIS_SHOUT_LIKED() {
        return IS_SHOUT_LIKED;
    }

    public void setIS_SHOUT_LIKED(int IS_SHOUT_LIKED) {
        this.IS_SHOUT_LIKED = IS_SHOUT_LIKED;
    }

    public String getDATE_TIME() {
        return DATE_TIME;
    }

    public void setDATE_TIME(String DATE_TIME) {
        this.DATE_TIME = DATE_TIME;
    }

    public String getSHOUT_IMAGE() {
        return SHOUT_IMAGE;
    }

    public void setSHOUT_IMAGE(String SHOUT_IMAGE) {
        this.SHOUT_IMAGE = SHOUT_IMAGE;
    }

    public int getIS_SHOUT_HIDDEN() {
        return IS_SHOUT_HIDDEN;
    }

    public void setIS_SHOUT_HIDDEN(int IS_SHOUT_HIDDEN) {
        this.IS_SHOUT_HIDDEN = IS_SHOUT_HIDDEN;
    }

    public int getDEFAULT_PAGE_POSITION() {
        return DEFAULT_PAGE_POSITION;
    }

    public void setDEFAULT_PAGE_POSITION(int DEFAULT_PAGE_POSITION) {
        this.DEFAULT_PAGE_POSITION = DEFAULT_PAGE_POSITION;
    }

    public int getDYNAMIC_HEIGHT() {
        return DYNAMIC_HEIGHT;
    }

    public void setDYNAMIC_HEIGHT(int DYNAMIC_HEIGHT) {
        this.DYNAMIC_HEIGHT = DYNAMIC_HEIGHT;
    }

    public float getDYNAMIC_Y() {
        return DYNAMIC_Y;
    }

    public void setDYNAMIC_Y(float DYNAMIC_Y) {
        this.DYNAMIC_Y = DYNAMIC_Y;
    }

    public String getStrShoutImages() {
        return strShoutImages;
    }

    public void setStrShoutImages(String strShoutImages) {
        this.strShoutImages = strShoutImages;
    }

    public String getIS_SEARCHABLE() {
        return IS_SEARCHABLE;
    }

    public void setIS_SEARCHABLE(String IS_SEARCHABLE) {
        this.IS_SEARCHABLE = IS_SEARCHABLE;
    }

    public String getSHOUT_LATITUDE() {
        return SHOUT_LATITUDE;
    }

    public void setSHOUT_LATITUDE(String SHOUT_LATITUDE) {
        this.SHOUT_LATITUDE = SHOUT_LATITUDE;
    }

    public String getSHOUT_LONGITUDE() {
        return SHOUT_LONGITUDE;
    }

    public void setSHOUT_LONGITUDE(String SHOUT_LONGITUDE) {
        this.SHOUT_LONGITUDE = SHOUT_LONGITUDE;
    }

    public String getSHOUT_ADDRESS() {
        return SHOUT_ADDRESS;
    }

    public void setSHOUT_ADDRESS(String SHOUT_ADDRESS) {
        this.SHOUT_ADDRESS = SHOUT_ADDRESS;
    }

    public String getSHOUT_CATEGORY_NAME() {
        return SHOUT_CATEGORY_NAME;
    }

    public void setSHOUT_CATEGORY_NAME(String SHOUT_CATEGORY_NAME) {
        this.SHOUT_CATEGORY_NAME = SHOUT_CATEGORY_NAME;
    }

    public String getSHOUT_CATEGORY_ID() {
        return SHOUT_CATEGORY_ID;
    }

    public void setSHOUT_CATEGORY_ID(String SHOUT_CATEGORY_ID) {
        this.SHOUT_CATEGORY_ID = SHOUT_CATEGORY_ID;
    }

    public String getIS_HIDE() {
        return IS_HIDE;
    }

    public void setIS_HIDE(String IS_HIDE) {
        this.IS_HIDE = IS_HIDE;
    }

    public String getSTART_DATE() {
        return START_DATE;
    }

    public void setSTART_DATE(String START_DATE) {
        this.START_DATE = START_DATE;
    }

    public String getEND_DATE() {
        return END_DATE;
    }

    public void setEND_DATE(String END_DATE) {
        this.END_DATE = END_DATE;
    }

    public String getRE_SHOUT() {
        return RE_SHOUT;
    }

    public void setRE_SHOUT(String RE_SHOUT) {
        this.RE_SHOUT = RE_SHOUT;
    }

    public String getCONTINUE_CHAT() {
        return CONTINUE_CHAT;
    }

    public void setCONTINUE_CHAT(String CONTINUE_CHAT) {
        this.CONTINUE_CHAT = CONTINUE_CHAT;
    }

    public String getDISTANCE() {
        return DISTANCE;
    }

    public void setDISTANCE(String DISTANCE) {
        this.DISTANCE = DISTANCE;
    }

}
