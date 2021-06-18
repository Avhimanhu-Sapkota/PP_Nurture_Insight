package com.example.nurture_insight.Model;

public class DataPoint {
    public String moodDate;
    public String moodType;

    public DataPoint() {
    }

    public DataPoint(String eachDate, String moodType) {
        this.moodDate = eachDate;
        this.moodType = moodType;
    }

    public String getEachDate() {
        return moodDate;
    }

    public void setEachDate(String eachDate) {
        this.moodDate = eachDate;
    }

    public String getMoodType() {
        return moodType;
    }

    public void setMoodType(String moodType) {
        this.moodType = moodType;
    }
}
