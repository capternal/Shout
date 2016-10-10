package com.shout.shoutapplication.main.Adapter;

/**
 * Created by jupitor on 03/10/16.
 */

public class UploadResourceModel {
    private int id;
    private String shoutId;
    private String tumbnail_Path;
    private String video_path;

    public UploadResourceModel() {

    }

    public UploadResourceModel(int id, String shoutId, String tumbnail_Path, String video_path) {
        this.id = id;
        this.shoutId = shoutId;
        this.tumbnail_Path = tumbnail_Path;
        this.video_path = video_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShoutId() {
        return shoutId;
    }

    public void setShoutId(String shoutId) {
        this.shoutId = shoutId;
    }

    public String getTumbnail_Path() {
        return tumbnail_Path;
    }

    public void setTumbnail_Path(String tumbnail_Path) {
        this.tumbnail_Path = tumbnail_Path;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }
}
