package com.shout.shoutapplication.main.Model;

/**
 * Created by CapternalSystems on 9/26/2016.
 */
public class NotificationListModel {

    private String id;
    private String message;
    private String user_id;
    private String username;
    private String notification_type;
    private String created;
    private String photo;

    public NotificationListModel(String id, String message, String user_id, String username, String notification_type, String created, String photo) {
        this.id = id;
        this.message = message;
        this.user_id = user_id;
        this.username = username;
        this.notification_type = notification_type;
        this.created = created;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
