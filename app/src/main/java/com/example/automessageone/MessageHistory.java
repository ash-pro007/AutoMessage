package com.example.automessageone;

public class MessageHistory {
    String id;
    String contactNumber;
    String deliveredMessage;
    String time;
    String date;
    String day;

    public MessageHistory(String id, String contactNumber, String deliveredMessage, String time, String date, String day) {
        this.id = id;
        this.contactNumber = contactNumber;
        this.deliveredMessage = deliveredMessage;
        this.time = time;
        this.date = date;
        this.day = day;
    }

    public MessageHistory( String contactNumber, String deliveredMessage, String time, String date, String day) {
        this.contactNumber = contactNumber;
        this.deliveredMessage = deliveredMessage;
        this.time = time;
        this.date = date;
        this.day = day;
    }


    public String getId() {
        return id;
    }
    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDeliveredMessage() {
        return deliveredMessage;
    }

    public void setDeliveredMessage(String deliveredMessage) {
        this.deliveredMessage = deliveredMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
