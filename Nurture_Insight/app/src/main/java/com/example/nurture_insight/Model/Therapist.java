package com.example.nurture_insight.Model;

public class Therapist {
    String bio, degree, expertise, worksAt, phoneNo;

    public Therapist() {
    }

    public Therapist(String bio, String degree, String expertise, String worksAt, String phoneNo) {
        this.bio = bio;
        this.degree = degree;
        this.expertise = expertise;
        this.worksAt = worksAt;
        this.phoneNo = phoneNo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getWorksAt() {
        return worksAt;
    }

    public void setWorksAt(String worksAt) {
        this.worksAt = worksAt;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
