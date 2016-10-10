package com.shout.shoutapplication.main.Model;

/**
 * Created by CapternalSystems on 5/23/2016.
 */
public class UserMessageListModel {

    private String ApponentUserId;
    private String MessageTitle;
    private String ItemImage;
    private String shoutLastSeenTime;
    private String MessageCount;

    public UserMessageListModel(String apponentUserId, String messageTitle, String itemImage, String shoutLastSeenTime, String messageCount) {
        ApponentUserId = apponentUserId;
        MessageTitle = messageTitle;
        ItemImage = itemImage;
        this.shoutLastSeenTime = shoutLastSeenTime;
        MessageCount = messageCount;
    }

    public String getApponentUserId() {
        return ApponentUserId;
    }

    public void setApponentUserId(String apponentUserId) {
        ApponentUserId = apponentUserId;
    }

    public String getMessageTitle() {
        return MessageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        MessageTitle = messageTitle;
    }

    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getShoutLastSeenTime() {
        return shoutLastSeenTime;
    }

    public void setShoutLastSeenTime(String shoutLastSeenTime) {
        this.shoutLastSeenTime = shoutLastSeenTime;
    }

    public String getMessageCount() {
        return MessageCount;
    }

    public void setMessageCount(String messageCount) {
        MessageCount = messageCount;
    }
}
