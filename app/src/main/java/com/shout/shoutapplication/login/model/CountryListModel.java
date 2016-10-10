package com.shout.shoutapplication.login.model;

/**
 * Created by CapternalSystems on 4/3/2016.
 */
public class CountryListModel {

    private String id;
    private String iso;
    private String name;
    private String nicename;
    private String iso3;
    private String numcode;
    private String phonecode;

    public CountryListModel(String id, String iso, String name, String nicename, String iso3, String numcode, String phonecode) {
        this.id = id;
        this.iso = iso;
        this.name = name;
        this.nicename = nicename;
        this.iso3 = iso3;
        this.numcode = numcode;
        this.phonecode = phonecode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNicename() {
        return nicename;
    }

    public void setNicename(String nicename) {
        this.nicename = nicename;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getNumcode() {
        return numcode;
    }

    public void setNumcode(String numcode) {
        this.numcode = numcode;
    }

    public String getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(String phonecode) {
        this.phonecode = phonecode;
    }
}
