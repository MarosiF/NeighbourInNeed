package com.example.neighbourinneed;

public class Advertisement {
    private String date;
    private String days;
    private String description;
    private String mainCategory;
    private String name;
    private String shipping;
    private String subCategory;

    public Advertisement() {
    }

    public Advertisement(String date, String days, String description, String mainCategory, String name, String shipping, String subCategory) {
        this.date = date;
        this.days = days;
        this.description = description;
        this.mainCategory = mainCategory;
        this.name = name;
        this.shipping = shipping;
        this.subCategory = subCategory;
    }

    public String getDate() {
        return date;
    }

    public String getDays() {
        return days;
    }

    public String getDescription() {
        return description;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public String getName() {
        return name;
    }

    public String getShipping() {
        return shipping;
    }

    public String getSubCategory() {
        return subCategory;
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
}
