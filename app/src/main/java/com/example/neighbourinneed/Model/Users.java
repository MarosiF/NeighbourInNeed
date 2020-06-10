package com.example.neighbourinneed.Model;

public class Users {
    private String name, password, email, city, postcode;

    public Users() {

    }

    public Users(String name, String password, String email, String city, String postcode) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.city = city;
        this.postcode = postcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getPostcode() {
        return postcode;
    }
}
