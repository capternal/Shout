package com.shout.shoutapplication.main.Model;

/**
 * Created by CapternalSystems on 5/26/2016.
 */
public class ShoutCommentModel {

    private String COMMENT_ID;
    private String USER_ID;
    private String USER_NAME;
    private String IMAGE_URL;
    private String COMMENT_MESSAGE;
    private String TIME_IN_HOUR;

    public ShoutCommentModel(String COMMENT_ID, String USER_ID, String USER_NAME, String IMAGE_URL, String COMMENT_MESSAGE, String TIME_IN_HOUR) {
        this.COMMENT_ID = COMMENT_ID;
        this.USER_ID = USER_ID;
        this.USER_NAME = USER_NAME;
        this.IMAGE_URL = IMAGE_URL;
        this.COMMENT_MESSAGE = COMMENT_MESSAGE;
        this.TIME_IN_HOUR = TIME_IN_HOUR;
    }

    public String getCOMMENT_ID() {
        return COMMENT_ID;
    }

    public void setCOMMENT_ID(String COMMENT_ID) {
        this.COMMENT_ID = COMMENT_ID;
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

    public String getIMAGE_URL() {
        return IMAGE_URL;
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL = IMAGE_URL;
    }

    public String getCOMMENT_MESSAGE() {
        return COMMENT_MESSAGE;
    }

    public void setCOMMENT_MESSAGE(String COMMENT_MESSAGE) {
        this.COMMENT_MESSAGE = COMMENT_MESSAGE;
    }

    public String getTIME_IN_HOUR() {
        return TIME_IN_HOUR;
    }

    public void setTIME_IN_HOUR(String TIME_IN_HOUR) {
        this.TIME_IN_HOUR = TIME_IN_HOUR;
    }
}
