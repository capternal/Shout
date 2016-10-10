package com.shout.shoutapplication.main.EditShout.Model;

/**
 * Created by CapternalSystems on 8/25/2016.
 */
public class OriginalDataModel {

    private String id;
    private String shout_id;
    private String photo;
    private String thumbnail;
    private String thumb;
    private String photo_type;


    public OriginalDataModel(String id, String shout_id, String photo, String thumbnail, String thumb, String photo_type) {
        this.id = id;
        this.shout_id = shout_id;
        this.photo = photo;
        this.thumbnail = thumbnail;
        this.thumb = thumb;
        this.photo_type = photo_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShout_id() {
        return shout_id;
    }

    public void setShout_id(String shout_id) {
        this.shout_id = shout_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getPhoto_type() {
        return photo_type;
    }

    public void setPhoto_type(String photo_type) {
        this.photo_type = photo_type;
    }
}
