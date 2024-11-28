package com.example.automessageone;

import android.net.Uri;

// Contact.java
public class Contact {
    private String name;
    private String number;
    private Uri imageUri;

    public Contact(String name, String number, Uri imageUri) {
        this.name = name;
        this.number = number;
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}