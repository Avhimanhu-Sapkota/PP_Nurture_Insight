package com.example.nurture_insight.Model;

import com.google.firebase.database.DatabaseReference;

public class Comments {

    String comment, postedBy, postedDate, postedTime;
    DatabaseReference chatRef;

    public Comments() {
    }

    public Comments(String comment, String postedBy, String postedDate, String postedTime) {
        this.comment = comment;
        this.postedBy = postedBy;
        this.postedDate = postedDate;
        this.postedTime = postedTime;
    }

    public Comments(DatabaseReference chatRef) {
        this.chatRef = chatRef;
    }

    public DatabaseReference getChatRef() {
        return chatRef;
    }

    public void setChatRef(DatabaseReference chatRef) {
        this.chatRef = chatRef;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(String postedTime) {
        this.postedTime = postedTime;
    }
}
