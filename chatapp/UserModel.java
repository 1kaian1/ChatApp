package com.example.chatapp;

public class UserModel {

    String userID;
    String userName;
    String userEmail;
    String userPassword;

    public UserModel() {}

    public UserModel(String userID, String userName, String userEmail, String userPassword) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

}
