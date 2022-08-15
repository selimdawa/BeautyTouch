package com.flatcode.beautytouch.Model;


public class User {
    private String category, id, imageurl, password, phonenumber, status, username, city,
            typingTo, mversion;

    public User() {
    }

    public User(String category, String id, String imageurl, String location, String location2, String password,
                String phonenumber, String status, String username, String city, String typingTo, String mversion) {

        this.category = category;
        this.id = id;
        this.imageurl = imageurl;
        this.password = password;
        this.phonenumber = phonenumber;
        this.status = status;
        this.username = username;
        this.city = city;
        this.typingTo = typingTo;
        this.mversion = mversion;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTypingTo() {
        return typingTo;
    }

    public void setTypingTo(String typingTo) {
        this.typingTo = typingTo;
    }

    public String getMversion() {
        return mversion;
    }

    public void setMversion(String mversion) {
        this.mversion = mversion;
    }
}