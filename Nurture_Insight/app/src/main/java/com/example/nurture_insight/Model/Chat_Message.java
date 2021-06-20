package com.example.nurture_insight.Model;

public class Chat_Message {
    private String messageDate, messageTime, message, username;
    private String anonymousStatus;

    public Chat_Message() {
    }

    public Chat_Message(String messageDate, String messageTime, String message, String username, String anonymousStatus) {
        this.messageDate = messageDate;
        this.messageTime = messageTime;
        this.message = message;
        this.username = username;
        this.anonymousStatus = anonymousStatus;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAnonymousStatus() {
        return anonymousStatus;
    }

    public void setAnonymousStatus(String anonymousStatus) {
        this.anonymousStatus = anonymousStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
