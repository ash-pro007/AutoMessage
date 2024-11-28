package com.example.automessageone;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

public class ContactUtils {

    public static ContactInfo getContactInfoFromPhoneNumber(Context context, String phoneNumber) {
        ContactInfo contactInfo = new ContactInfo();

        // Query the ContactsContract to retrieve the contact ID
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            contactInfo.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)));
            long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
            cursor.close();

            // Get contact photo URI
            Uri photoUri = getContactPhotoUri(context, contactId);
            if (photoUri != null) {
                contactInfo.setPhotoUri(photoUri);
            }
        }

        return contactInfo;
    }

    private static Uri getContactPhotoUri(Context context, long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            byte[] data = cursor.getBlob(0);
            if (data != null) {
                return Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/" + contactId);
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return null;
    }

    public static class ContactInfo {
        private String name;
        private Uri photoUri;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Uri getPhotoUri() {
            return photoUri;
        }

        public void setPhotoUri(Uri photoUri) {
            this.photoUri = photoUri;
        }
    }
}
