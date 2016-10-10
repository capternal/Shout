package com.shout.shoutapplication.main.Model;

/**
 * Created by CapternalSystems on 9/8/2016.
 */
public class LikedShoutersModel {

    private String UserId;
    private String UserName;
    private String UserProfile;
    private String UserLikeTime;

    public LikedShoutersModel(String userId, String userName, String userProfile, String userLikeTime) {
        UserId = userId;
        UserName = userName;
        UserProfile = userProfile;
        UserLikeTime = userLikeTime;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserProfile() {
        return UserProfile;
    }

    public void setUserProfile(String userProfile) {
        UserProfile = userProfile;
    }

    public String getUserLikeTime() {
        return UserLikeTime;
    }

    public void setUserLikeTime(String userLikeTime) {
        UserLikeTime = userLikeTime;
    }

}
