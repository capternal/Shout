package com.shout.shoutapplication.main.Model;

/**
 * Created by CapternalSystems on 7/19/2016.
 */
public class MyPreferencesModel {

    private String id;
    private String preference_id;
    private String title;
    private String status;
    private boolean IsCheckBoxChecked;

    public MyPreferencesModel(String id,String preference_id, String title, String status, boolean isCheckBoxChecked) {
        this.id = id;
        this.preference_id = preference_id;
        this.title = title;
        this.status = status;
        this.IsCheckBoxChecked = isCheckBoxChecked;
    }

    public MyPreferencesModel() {

    }

    public String getPreference_id() {
        return preference_id;
    }

    public void setPreference_id(String preference_id) {
        this.preference_id = preference_id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCheckBoxChecked() {
        return IsCheckBoxChecked;
    }

    public void setCheckBoxChecked(boolean checkBoxChecked) {
        IsCheckBoxChecked = checkBoxChecked;
    }
}
