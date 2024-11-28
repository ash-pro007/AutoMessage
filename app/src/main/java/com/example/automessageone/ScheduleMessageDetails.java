package com.example.automessageone;

public class ScheduleMessageDetails {
    private String message;
    private String noOfMessage;
    private String date;
    private String time;

    public ScheduleMessageDetails(String message, String noOfMessage, String date, String time) {
        this.message = message;
        this.noOfMessage = noOfMessage;
        this.date = date;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNoOfMessage() {
        return noOfMessage;
    }

    public void setNoOfMessage(String noOfMessage) {
        this.noOfMessage = noOfMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
