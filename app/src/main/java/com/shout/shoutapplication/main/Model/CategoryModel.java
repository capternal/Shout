package com.shout.shoutapplication.main.Model;

/**
 * Created by CapternalSystems on 5/21/2016.
 */
public class CategoryModel {

    private String id;
    private String title;
    private String image;
    private String selected_image;
    private String created;
    private int flag;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    boolean selected=false;
    public CategoryModel(String id, String title, String image, String selected_image, String created, int flag) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.selected_image = selected_image;
        this.created = created;
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSelected_image() {
        return selected_image;
    }

    public void setSelected_image(String selected_image) {
        this.selected_image = selected_image;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
