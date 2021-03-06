package com.example.neighbourinneed;

import android.net.Uri;

public class Advertisement {
    private String date;
    private String days;
    private String description;
    private String mainCategory;
    private String name;
    private String shipping;
    private String subCategory;
    private String city;
    private String image;
    private String user;

    public Advertisement() {
    }

    public Advertisement(String date, String days, String description, String mainCategory, String name, String shipping, String subCategory, String city, String image, String user
            ) {
        this.date = date;
        this.days = days;
        this.description = description;
        this.mainCategory = mainCategory;
        this.name = name;
        this.shipping = shipping;
        this.subCategory = subCategory;
        this.city = city;
        this.city = image;
        this.city = user;
    }

    public String getDate() {
        return this.date;
    }

    public String getDays() {
        return this.days;
    }

    public String getDescription() {
        return this.description;
    }

    public String getMainCategory() {
        return this.mainCategory;
    }

    public String getName() {
        return this.name;
    }

    public String getShipping() {
        return this.shipping;
    }

    public String getSubCategory() {
        return this.subCategory;
    }

    public String getCity() {
        return this.city;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
