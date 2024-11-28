package com.example.automessageone;

import android.graphics.Bitmap;

public class ContactDetails {

    private String number;
    private String name;
    private Bitmap photo = null;

    public ContactDetails(String number, String name, Bitmap photo) {
        this.number = number;
        this.name = name;
        this.photo = photo;
    }

    public ContactDetails(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public Bitmap getPhoto() {
        return photo;
    }
}
