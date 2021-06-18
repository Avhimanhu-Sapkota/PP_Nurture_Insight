package com.example.nurture_insight.Model;

public class Mood_Tracker {
    private String moodDate, moodType;

    public Mood_Tracker() {
    }

    public Mood_Tracker(String moodDate, String moodType) {
        this.moodDate = moodDate;
        this.moodType = moodType;
    }

    public String getMoodDate() {
        return moodDate;
    }

    public void setMoodDate(String moodDate) {
        this.moodDate = moodDate;
    }

    public String getMoodType() {
        return moodType;
    }

    public void setMoodType(String moodType) {
        this.moodType = moodType;
    }
}
