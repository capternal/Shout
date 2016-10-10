package com.shout.shoutapplication.main.Model;

/**
 * Created by CapternalSystems on 5/23/2016.
 */
public class MessageBoardModel {

    private String ShoutId;
    private String MessageTitle;
    private String MessageCount;
    private String ItemImage;
    private String shoutOwnerId;
    private String users;
    private String profilePic;
    private String userName;
    private String shout_type;

    public MessageBoardModel(String shoutId, String messageTitle, String messageCount, String itemImage, String shoutOwnerId, String users, String profilePic, String userName, String shout_type) {
        ShoutId = shoutId;
        MessageTitle = messageTitle;
        MessageCount = messageCount;
        ItemImage = itemImage;
        this.shoutOwnerId = shoutOwnerId;
        this.users = users;
        this.profilePic = profilePic;
        this.userName = userName;
        this.shout_type = shout_type;
    }

    public String getShoutId() {
        return ShoutId;
    }

    public void setShoutId(String shoutId) {
        ShoutId = shoutId;
    }

    public String getMessageTitle() {
        return MessageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        MessageTitle = messageTitle;
    }

    public String getMessageCount() {
        return MessageCount;
    }

    public void setMessageCount(String messageCount) {
        MessageCount = messageCount;
    }

    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getShoutOwnerId() {
        return shoutOwnerId;
    }

    public void setShoutOwnerId(String shoutOwnerId) {
        this.shoutOwnerId = shoutOwnerId;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getShout_type() {
        return shout_type;
    }

    public void setShout_type(String shout_type) {
        this.shout_type = shout_type;
    }
}
