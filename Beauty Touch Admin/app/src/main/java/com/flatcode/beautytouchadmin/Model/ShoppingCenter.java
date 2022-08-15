package com.flatcode.beautytouchadmin.Model;

public class ShoppingCenter {

    private String id;
    private String name;
    private String imageurl;
    private String imageurl2;
    private String location;
    private String location2;
    private String location3;
    private String numberPhone;
    private String publisher;
    private String aname;

    public ShoppingCenter() {
    }

    public ShoppingCenter(String id, String name, String imageurl, String imageurl2, String location,
                          String location2, String location3, String numberPhone, String publisher, String aname) {
        this.id = id;
        this.name = name;
        this.imageurl = imageurl;
        this.imageurl2 = imageurl2;
        this.location = location;
        this.location2 = location2;
        this.location3 = location3;
        this.numberPhone = numberPhone;
        this.publisher = publisher;
        this.aname = aname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getImageurl2() {
        return imageurl2;
    }

    public void setImageurl2(String imageurl2) {
        this.imageurl2 = imageurl2;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation2() {
        return location2;
    }

    public void setLocation2(String location2) {
        this.location2 = location2;
    }

    public String getLocation3() {
        return location3;
    }

    public void setLocation3(String location3) {
        this.location3 = location3;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }
}