package com.example.nurture_insight.Model;

import com.google.firebase.database.DatabaseReference;

public class Self_care {
    String category, description, duration, name, url, video_by;
    DatabaseReference eachRef;

    public Self_care() {
    }

    public Self_care(String category, String description, String duration, String name, String url, String video_by) {
        this.category = category;
        this.description = description;
        this.duration = duration;
        this.name = name;
        this.url = url;
        this.video_by = video_by;
    }

    public Self_care(DatabaseReference eachRef) {
        this.eachRef = eachRef;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVideo_by() {
        return video_by;
    }

    public void setVideo_by(String video_by) {
        this.video_by = video_by;
    }

    public DatabaseReference getEachRef() {
        return eachRef;
    }

    public void setEachRef(DatabaseReference eachRef) {
        this.eachRef = eachRef;
    }
}
