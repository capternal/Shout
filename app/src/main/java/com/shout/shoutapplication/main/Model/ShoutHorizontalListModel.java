package com.shout.shoutapplication.main.Model;

/**
 * Created by CapternalSystems on 5/26/2016.
 */
public class ShoutHorizontalListModel {

    private String id;
    private String name;
    private String photo;

    public ShoutHorizontalListModel(String id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
