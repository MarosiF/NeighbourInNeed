package com.example.neighbourinneed.Prevalent;

import com.example.neighbourinneed.Model.Users;

public class Prevalent {
    public static Users currentUser;

    public static final String usernamekey ="UserName";
    public static final String userpasswordkey ="UserPassword";

    public static Users getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Users currentUser) {
        Prevalent.currentUser = currentUser;
    }

    public static String getUsernamekey() {
        return usernamekey;
    }

    public static String getUserpasswordkey() {
        return userpasswordkey;
    }
}
