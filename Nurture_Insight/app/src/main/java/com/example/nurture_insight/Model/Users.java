package com.example.nurture_insight.Model;

public class Users {
    private String phoneNo, username, email, password, type, therapistID, usersID;

    public Users(){

    }

    public Users(String phone, String username, String email, String password, String type, String therapistID) {
        this.phoneNo = phone;
        this.username = username;
        this.email = email;
        this.password = password;
        this.type = type;
        this.therapistID = therapistID;
    }

    public Users(String usersID) {
        this.usersID = usersID;
    }

    public String getUsersID() {
        return usersID;
    }

    public void setUsersID(String usersID) {
        this.usersID = usersID;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phone) {
        this.phoneNo = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTherapistID() {
        return therapistID;
    }

    public void setTherapistID(String therapistID) {
        this.therapistID = therapistID;
    }
}
