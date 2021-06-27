package com.example.nurture_insight.Model;

import com.google.firebase.database.DatabaseReference;

public class events {
    String conductor, contact, eventDate, eventTime, eventDesc, eventLocation, eventTitle;

    DatabaseReference eventRef;

    public events(DatabaseReference eventRef) {
        this.eventRef = eventRef;
    }

    public events() {
    }

    public events(String conductor, String contact, String eventDate, String eventTime, String eventDesc, String eventLocation, String eventTitle) {
        this.conductor = conductor;
        this.contact = contact;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventDesc = eventDesc;
        this.eventLocation = eventLocation;
        this.eventTitle = eventTitle;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public DatabaseReference getEventRef() {
        return eventRef;
    }

    public void setEventRef(DatabaseReference eventRef) {
        this.eventRef = eventRef;
    }
}
