package com.shout.shoutapplication.main.Model;

import android.net.Uri;

/**
 * Created by Capternal on 18/06/16.
 */
public class ChatMessage {
    public boolean left;
    public String message;
    public String profile_image_url;
    public String created_date;
    public String message_type;
    public String image_url;
    public Uri temp_uri;
    public String shout_id;
    public String isProcessed;
    public String from_id;
    public String to_id;
    public String apponent_user_name;
    public String shout_title;
    public String chat_row_id;
    public String chat_thumb_image;

    public ChatMessage(boolean left, String message, String profile_image_url, String created_date, String message_type, String image_url, Uri temp_uri, String shout_id, String isProcessed, String from_id, String to_id, String apponent_user_name, String shout_title, String chat_row_id, String chat_thumb_image) {
        this.left = left;
        this.message = message;
        this.profile_image_url = profile_image_url;
        this.created_date = created_date;
        this.message_type = message_type;
        this.image_url = image_url;
        this.temp_uri = temp_uri;
        this.shout_id = shout_id;
        this.isProcessed = isProcessed;
        this.from_id = from_id;
        this.to_id = to_id;
        this.apponent_user_name = apponent_user_name;
        this.shout_title = shout_title;
        this.chat_row_id = chat_row_id;
        this.chat_thumb_image = chat_thumb_image;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Uri getTemp_uri() {
        return temp_uri;
    }

    public void setTemp_uri(Uri temp_uri) {
        this.temp_uri = temp_uri;
    }

    public String getShout_id() {
        return shout_id;
    }

    public void setShout_id(String shout_id) {
        this.shout_id = shout_id;
    }

    public String getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(String isProcessed) {
        this.isProcessed = isProcessed;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getApponent_user_name() {
        return apponent_user_name;
    }

    public void setApponent_user_name(String apponent_user_name) {
        this.apponent_user_name = apponent_user_name;
    }

    public String getShout_title() {
        return shout_title;
    }

    public void setShout_title(String shout_title) {
        this.shout_title = shout_title;
    }

    public String getChat_row_id() {
        return chat_row_id;
    }

    public void setChat_row_id(String chat_row_id) {
        this.chat_row_id = chat_row_id;
    }

    public String getChat_thumb_image() {
        return chat_thumb_image;
    }

    public void setChat_thumb_image(String chat_thumb_image) {
        this.chat_thumb_image = chat_thumb_image;
    }
}