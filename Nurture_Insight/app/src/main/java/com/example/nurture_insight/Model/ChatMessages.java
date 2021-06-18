package com.example.nurture_insight.Model;

public class ChatMessages {
    private String messageDate, messageTime, message;

    public ChatMessages() {
    }

    public ChatMessages(String messageDate, String messageTime, String message) {
        this.messageDate = messageDate;
        this.messageTime = messageTime;
        this.message = message;
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
}
