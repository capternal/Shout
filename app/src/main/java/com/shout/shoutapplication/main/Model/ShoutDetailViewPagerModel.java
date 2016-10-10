package com.shout.shoutapplication.main.Model;

/**
 * Created by CapternalSystems on 6/21/2016.
 */
public class ShoutDetailViewPagerModel {

    private String ID;
    private String IMAGE_URL;
    private String TYPE;
    private String VIDEO_URL;
    private String VIDEO_LOCAL_PATH;

    public ShoutDetailViewPagerModel(String ID, String IMAGE_URL, String TYPE, String VIDEO_URL, String VIDEO_LOCAL_PATH) {
        this.ID = ID;
        this.IMAGE_URL = IMAGE_URL;
        this.TYPE = TYPE;
        this.VIDEO_URL = VIDEO_URL;
        this.VIDEO_LOCAL_PATH = VIDEO_LOCAL_PATH;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIMAGE_URL() {
        return IMAGE_URL;
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL = IMAGE_URL;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getVIDEO_URL() {
        return VIDEO_URL;
    }

    public void setVIDEO_URL(String VIDEO_URL) {
        this.VIDEO_URL = VIDEO_URL;
    }

    public String getVIDEO_LOCAL_PATH() {
        return VIDEO_LOCAL_PATH;
    }

    public void setVIDEO_LOCAL_PATH(String VIDEO_LOCAL_PATH) {
        this.VIDEO_LOCAL_PATH = VIDEO_LOCAL_PATH;
    }
}
