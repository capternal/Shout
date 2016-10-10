package com.shout.shoutapplication.main.Model;

import com.shout.shoutapplication.login.model.ContactModel;

import java.util.ArrayList;

/**
 * Created by CapternalSystems on 6/8/2016.
 */
public class Continent {

    private String Header;
    private ArrayList<ContactModel> arrContactList = new ArrayList<ContactModel>();

    public Continent(String header, ArrayList<ContactModel> countryList) {
        Header = header;
        this.arrContactList = countryList;
    }

    public String getHeader() {
        return Header;
    }

    public void setHeader(String header) {
        Header = header;
    }

    public ArrayList<ContactModel> getArrContactList() {
        return arrContactList;
    }

    public void setArrContactList(ArrayList<ContactModel> arrContactList) {
        this.arrContactList = arrContactList;
    }
}
