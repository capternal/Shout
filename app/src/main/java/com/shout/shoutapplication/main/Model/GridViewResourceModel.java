package com.shout.shoutapplication.main.Model;

import android.net.Uri;

/**
 * Created by Prasad on 6/23/2016.
 */
public class GridViewResourceModel {

    private Uri PATH;
    private String RESUORCE_TYPE;//Photo->C,Video->V,Document->D
    private Boolean IS_ADD_BUTTON;
    private Boolean IS_VISIBLE;
    private Uri VIDEO_PATH;

    public GridViewResourceModel(Uri PATH, String RESUORCE_TYPE, Boolean IS_ADD_BUTTON, Boolean IS_VISIBLE, Uri VIDEO_PATH) {
        this.PATH = PATH;
        this.RESUORCE_TYPE = RESUORCE_TYPE;
        this.IS_ADD_BUTTON = IS_ADD_BUTTON;
        this.IS_VISIBLE = IS_VISIBLE;
        this.VIDEO_PATH = VIDEO_PATH;
    }

    public Uri getPATH() {
        return PATH;
    }

    public void setPATH(Uri PATH) {
        this.PATH = PATH;
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

    public Boolean getIS_VISIBLE() {
        return IS_VISIBLE;
    }

    public void setIS_VISIBLE(Boolean IS_VISIBLE) {
        this.IS_VISIBLE = IS_VISIBLE;
    }

    public Uri getVIDEO_PATH() {
        return VIDEO_PATH;
    }

    public void setVIDEO_PATH(Uri VIDEO_PATH) {
        this.VIDEO_PATH = VIDEO_PATH;
    }
}
