package com.example.nurture_insight.Model;

import com.google.firebase.database.DatabaseReference;

public class Assessment {
    String score, assessmentDate;
    DatabaseReference assessmentRef;

    public Assessment() {
    }

    public Assessment(String score, String assessmentDate) {
        this.score = score;
        this.assessmentDate = assessmentDate;
    }

    public Assessment(DatabaseReference assessmentRef) {
        this.assessmentRef = assessmentRef;
    }

    public String getScore() {
        return score;
    }

    public DatabaseReference getAssessmentRef() {
        return assessmentRef;
    }

    public void setAssessmentRef(DatabaseReference assessmentRef) {
        this.assessmentRef = assessmentRef;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(String assessmentDate) {
        this.assessmentDate = assessmentDate;
    }
}
