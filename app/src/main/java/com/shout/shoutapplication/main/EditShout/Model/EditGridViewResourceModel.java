package com.shout.shoutapplication.main.EditShout.Model;

import android.net.Uri;

/**
 * Created by Prasad on 6/23/2016.
 */
public class EditGridViewResourceModel {

    private Uri PATH;
    private String imagePath;
    private String RESUORCE_TYPE;//Photo->C,Video->V,Document->D
    private Boolean IS_ADD_BUTTON;
    private Uri VIDEO_PATH;
    private String RESOURCE_ID;

    public EditGridViewResourceModel(Uri PATH, String imagePath, String RESUORCE_TYPE, Boolean IS_ADD_BUTTON, Uri VIDEO_PATH, String RESOURCE_ID) {
        this.PATH = PATH;
        this.imagePath = imagePath;
        this.RESUORCE_TYPE = RESUORCE_TYPE;
        this.IS_ADD_BUTTON = IS_ADD_BUTTON;
        this.VIDEO_PATH = VIDEO_PATH;
        this.RESOURCE_ID = RESOURCE_ID;
    }

    public Uri getPATH() {
        return PATH;
    }

    public void setPATH(Uri PATH) {
        this.PATH = PATH;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getRESUORCE_TYPE() {
        return RESUORCE_TYPE;
    }

    public void setRESUORCE_TYPE(String RESUORCE_TYPE) {
        this.RESUORCE_TYPE = RESUORCE_TYPE;
    }

    public Boolean getIS_ADD_BUTTON() {
        return IS_ADD_BUTTON;
    }

    public void setIS_ADD_BUTTON(Boolean IS_ADD_BUTTON) {
        this.IS_ADD_BUTTON = IS_ADD_BUTTON;
    }

    public Uri getVIDEO_PATH() {
        return VIDEO_PATH;
    }

    public void setVIDEO_PATH(Uri VIDEO_PATH) {
        this.VIDEO_PATH = VIDEO_PATH;
    }

    public String getRESOURCE_ID() {
        return RESOURCE_ID;
    }

    public void setRESOURCE_ID(String RESOURCE_ID) {
        this.RESOURCE_ID = RESOURCE_ID;
    }
}
