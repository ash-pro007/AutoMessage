package com.example.automessageone;



import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.widget.Toast;

public class AddExceptionForAutoMessageActivity extends AppCompatActivity {

    private static final int REQUEST_SELECT_CONTACT = 1;
    private RecyclerView recyclerView;
    private Button btnSelectContacts;
    public static List<Contact> selectedContacts = new ArrayList<>();

    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exception_for_auto_message);

        recyclerView = findViewById(R.id.recyclerViewContacts);
        btnSelectContacts = findViewById(R.id.btnSelectContacts);

        btnSelectContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectContacts();
            }
        });

        contactAdapter = new ContactAdapter(selectedContacts, this);
        recyclerView.setAdapter(contactAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        Button btnAddExceptionToList = (Button) findViewById(R.id.btnAddToList);

        btnAddExceptionToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(AddExceptionForAutoMessageActivity.this,
                        "Contacts for Exception added", Toast.LENGTH_SHORT).show();

                for(Contact contact: selectedContacts) {
                    Log.i("lg123",contact.getName() + " : " + contact.getNumber());
                }

                finish();

            }
        });

    }

    private void selectContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, REQUEST_SELECT_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getData() != null) {
                    // Single selection
                    Uri contactUri = data.getData();
                    String contactName = getContactName(contactUri);
                    String contactNumber = getContactNumber(AddExceptionForAutoMessageActivity.this, contactUri);
                    Uri contactImageUri = getContactImageUri(contactUri);




                    if(checkIfContactExists(contactNumber))
                        selectedContacts.add(new Contact(contactName, contactNumber, contactImageUri));
                    else
                        Toast.makeText(this, "This Contact is already added!", Toast.LENGTH_SHORT).show();



                    contactNumber = contactNumber.replace(" ", "");

                    MainActivity.selectedContactNumbers.add(contactNumber);

                } else {
                    // Multiple selections
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            Uri contactUri = data.getClipData().getItemAt(i).getUri();
                            String contactName = getContactName(contactUri);
                            String contactNumber = getContactNumber(contactUri);
                            Uri contactImageUri = getContactImageUri(contactUri);




                            if(checkIfContactExists(contactNumber))
                                selectedContacts.add(new Contact(contactName, contactNumber, contactImageUri));
                            else
                                Toast.makeText(this, "This Contact is already added!", Toast.LENGTH_SHORT).show();


                            contactNumber = contactNumber.replace(" ", "");
                            MainActivity.selectedContactNumbers.add(contactNumber);

                        }
                    }
                }
                contactAdapter.notifyDataSetChanged();
            }
        }
    }

    private String getContactName(Uri contactUri) {
        String contactName = "";
        String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
        try (Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactName;
    }

    private String getContactNumber(Uri contactUri) {
        String contactNumber = "";
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
        try (Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactNumber;
    }


    public static String getContactNumber(Context context, Uri contactUri) {
        String contactNumber = "";

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;

        try {
            cursor = contentResolver.query(
                    ContactsContract.Data.CONTENT_URI,
                    new String[]{ContactsContract.Data.DATA1},
                    ContactsContract.Data.CONTACT_ID + " = ? AND " +
                            ContactsContract.Data.MIMETYPE + " = ?",
                    new String[]{contactUri.getLastPathSegment(),
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE},
                    null);

            if (cursor != null && cursor.moveToFirst()) {
                contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DATA1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return contactNumber;
    }

    private Uri getContactImageUri(Uri contactUri) {
        Uri imageUri = null;
        InputStream inputStream = null;
        try {
            inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), contactUri);
            if (inputStream != null) {
                imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), BitmapFactory.decodeStream(inputStream), "Contact Image", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imageUri;
    }

    private boolean checkIfContactExists(String contactNumber) {

        for(Contact contact: selectedContacts) {
            if(Objects.equals(contact.getNumber(), contactNumber))
                return false;
        }

        return true;
    }
}
