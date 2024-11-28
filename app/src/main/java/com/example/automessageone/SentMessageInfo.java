package com.example.automessageone;

import android.net.Uri;

public class SentMessageInfo {

    private String contactNumber;
    private String contactName;
    private Uri contactImage;
    private String time;
    private String date;

    public SentMessageInfo(String contactNumber, String contactName, Uri contactImage, String time, String date) {
        this.contactNumber = contactNumber;
        this.contactName = contactName;
        this.contactImage = contactImage;
        this.time = time;
        this.date = date;
    }

}
