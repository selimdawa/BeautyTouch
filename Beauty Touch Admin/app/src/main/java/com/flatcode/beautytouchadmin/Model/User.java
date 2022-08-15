package com.flatcode.beautytouchadmin.Model;


public class User {
    private int adLoad, adClick;
    private String id, imageurl, mversion, password, phonenumber, started, username;

    public User() {
    }

    public User(int adLoad, int adClick, String id, String imageurl, String mversion, String password,
                String phonenumber, String started, String username) {
        this.adLoad = adLoad;
        this.adClick = adClick;
        this.id = id;
        this.imageurl = imageurl;
        this.mversion = mversion;
        this.password = password;
        this.phonenumber = phonenumber;
        this.started = started;
        this.username = username;
    }

    public int getAdLoad() {
        return adLoad;
    }

    public void setAdLoad(int adLoad) {
        this.adLoad = adLoad;
    }

    public int getAdClick() {
        return adClick;
    }

    public void setAdClick(int adClick) {
        this.adClick = adClick;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getMversion() {
        return mversion;
    }

    public void setMversion(String mversion) {
        this.mversion = mversion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}