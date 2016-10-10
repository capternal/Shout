package com.shout.shoutapplication.login.model;

/**
 * Created by CapternalSystems on 6/18/2016.
 */
public class ResourceModel {

    private String RESOURCE_PATH;
    private String RESOURCE_TYPE;
    private int IMAGE_ID;

    public ResourceModel(String RESOURCE_PATH, String RESOURCE_TYPE, int IMAGE_ID) {
        this.RESOURCE_PATH = RESOURCE_PATH;
        this.RESOURCE_TYPE = RESOURCE_TYPE;
        this.IMAGE_ID = IMAGE_ID;
    }

    public String getRESOURCE_PATH() {
        return RESOURCE_PATH;
    }

    public void setRESOURCE_PATH(String RESOURCE_PATH) {
        this.RESOURCE_PATH = RESOURCE_PATH;
    }

    public String getRESOURCE_TYPE() {
        return RESOURCE_TYPE;
    }

    public void setRESOURCE_TYPE(String RESOURCE_TYPE) {
        this.RESOURCE_TYPE = RESOURCE_TYPE;
    }

    public int getIMAGE_ID() {
        return IMAGE_ID;
    }

    public void setIMAGE_ID(int IMAGE_ID) {
        this.IMAGE_ID = IMAGE_ID;
    }
}
