package com.shout.shoutapplication.login.model;

/**
 * Created by CapternalSystems on 6/8/2016.
 */
public class ContactModel {

    private int tableId;
    private String ContactName;
    private String ContactNumber;
    private String Id;
    private String isFacebookFriend;
    private String isPhoneFriend;
    private Boolean IsCheckBokChecked;
    private String profileImage;
    private int GroupPosition;
    private String ButtonType;
    private String IsShoutFriend;


    public ContactModel(int tableId, String contactName, String contactNumber, String id, String isFacebookFriend, String isPhoneFriend, Boolean isCheckBokChecked, String profileImage, int groupPosition, String buttonType, String isShoutFriend) {
        this.tableId = tableId;
        ContactName = contactName;
        ContactNumber = contactNumber;
        Id = id;
        this.isFacebookFriend = isFacebookFriend;
        this.isPhoneFriend = isPhoneFriend;
        IsCheckBokChecked = isCheckBokChecked;
        this.profileImage = profileImage;
        GroupPosition = groupPosition;
        ButtonType = buttonType;
        IsShoutFriend = isShoutFriend;
    }

    public ContactModel() {

    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getIsFacebookFriend() {
        return isFacebookFriend;
    }

    public void setIsFacebookFriend(String isFacebookFriend) {
        this.isFacebookFriend = isFacebookFriend;
    }

    public String getIsPhoneFriend() {
        return isPhoneFriend;
    }

    public void setIsPhoneFriend(String isPhoneFriend) {
        this.isPhoneFriend = isPhoneFriend;
    }

    public Boolean getCheckBokChecked() {
        return IsCheckBokChecked;
    }

    public void setCheckBokChecked(Boolean checkBokChecked) {
        IsCheckBokChecked = checkBokChecked;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public int getGroupPosition() {
        return GroupPosition;
    }

    public void setGroupPosition(int groupPosition) {
        GroupPosition = groupPosition;
    }

    public String getButtonType() {
        return ButtonType;
    }

    public void setButtonType(String buttonType) {
        ButtonType = buttonType;
    }

    public String getIsShoutFriend() {
        return IsShoutFriend;
    }

    public void setIsShoutFriend(String isShoutFriend) {
        IsShoutFriend = isShoutFriend;
    }
}
