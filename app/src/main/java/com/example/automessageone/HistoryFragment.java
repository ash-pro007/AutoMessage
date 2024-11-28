package com.example.automessageone;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {


    private ScrollView svHistory;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_history, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        // Initialize svHistory by finding it using its ID from the layout
        ScrollView svHistory = view.findViewById(R.id.svHistory); // Replace R.id.sv_history with the ID of your ScrollView

        LinearLayout linearLayoutScrollableMainFrame = new LinearLayout(view.getContext());



        // Check if svHistory is not null before using it
        try {


            svHistory.addView(linearLayoutScrollableMainFrame);

            if (svHistory != null) {
                DataBaseHelper db = new DataBaseHelper(view.getContext());
                List<MessageHistory> messageHistories = db.getHistory();

                Collections.reverse(messageHistories);

                for(MessageHistory messageHistory: messageHistories) {

                    String st = "Contact Number: " + messageHistory.getContactNumber() +
                            ", Day: " + messageHistory.getDay() + ", Time: " + messageHistory.getTime()
                            + ", Date: " + messageHistory.getDate() + ", Message: " + messageHistory.getDeliveredMessage();

                    Log.i("mes324", st);

                }

                for(MessageHistory messageHistory: messageHistories) {


                    /*

                        _________________________________________________________
                        |       |      999780980  Sunday 10:20 AM  10/04/2024   |  <--- main layout
                        | img   | ----------------------------------------------
                        |       |       Message: this is the message            |
                        ---------------------------------------------------------

                     */

                    //

                    Bitmap scaled;

                    scaled =  retrieveContactPhoto(view.getContext(),
                            messageHistory.contactNumber);

                    scaled = RoundedImageView.getCroppedBitmap(scaled, 130);

                    LinearLayout linearLayoutMainCardHorizontal = new LinearLayout(view.getContext());
                    LinearLayout linearLayoutInfoAndMessageVertical = new LinearLayout(view.getContext());
                    LinearLayout linearLayoutInfoHorizontal = new LinearLayout(view.getContext());

                    linearLayoutMainCardHorizontal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(view.getContext(), "Long press to delete history",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    linearLayoutMainCardHorizontal.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            startActivity(new Intent(getContext(), DeleteMessageHistoryActivity.class));

                            return false;
                        }
                    });

                    ImageView imgViewContactImage = new ImageView(view.getContext());

                    // TODO: changing the default image to contact image



                    imgViewContactImage.setImageBitmap(scaled);
                    LinearLayout.LayoutParams imageParam = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    imageParam.setMargins(20, 15, 20, 20);

                    imgViewContactImage.setLayoutParams(imageParam);

                    //

                    TextView tvContactNameOrNumber = new TextView(view.getContext());
                    TextView tvDay = new TextView(view.getContext());
                    TextView tvTime = new TextView(view.getContext());
                    TextView tvDate = new TextView(view.getContext());

                    //

                    tvContactNameOrNumber.setText(getContactName(view.getContext(),
                            messageHistory.getContactNumber()));
                    tvDay.setText(messageHistory.getDay());
                    tvTime.setText(messageHistory.getTime());
                    tvDate.setText(messageHistory.getDate());

                    //

                    LinearLayout.LayoutParams paramsContactNameOrNumber = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    paramsContactNameOrNumber.setMargins(10, 10, 100, 10);
                    tvContactNameOrNumber.setLayoutParams(paramsContactNameOrNumber);

                    LinearLayout.LayoutParams paramsInfo = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    paramsInfo.setMargins(10, -10, 10, 10);

                    tvDay.setLayoutParams(paramsInfo);
                    tvTime.setLayoutParams(paramsInfo);
                    tvDate.setLayoutParams(paramsInfo);



                    //

                    tvContactNameOrNumber.setTextSize(16);

                    tvDay.setTextSize(11);
                    tvTime.setTextSize(11);
                    tvDate.setTextSize(11);

                    //

                    linearLayoutInfoHorizontal.addView(tvContactNameOrNumber);
                    linearLayoutInfoHorizontal.addView(tvDay);
                    linearLayoutInfoHorizontal.addView(tvTime);
                    linearLayoutInfoHorizontal.addView(tvDate);

                    //

                    TextView tvMessage = new TextView(view.getContext());
                    tvMessage.setText(messageHistory.getDeliveredMessage());

                    tvMessage.setTextSize(15);

                    //

                    LinearLayout.LayoutParams paramsMessage = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    paramsMessage.setMargins(20, 0, 10, 10);
                    tvMessage.setLayoutParams(paramsMessage);

                    //

                    linearLayoutInfoHorizontal.setPadding(10, 10, 10, 10);

                    linearLayoutInfoAndMessageVertical.addView(linearLayoutInfoHorizontal);
                    linearLayoutInfoAndMessageVertical.addView(tvMessage);

                    linearLayoutInfoAndMessageVertical.setOrientation(LinearLayout.VERTICAL);

                    //

                    linearLayoutMainCardHorizontal.addView(imgViewContactImage);

                    //

                    LinearLayout.LayoutParams paramsMainCard = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    paramsMainCard.setMargins(0, 5, 0, 5);
                    linearLayoutMainCardHorizontal.setLayoutParams(paramsMainCard);

                    linearLayoutMainCardHorizontal.addView(linearLayoutInfoAndMessageVertical);

                    linearLayoutMainCardHorizontal.setOrientation(LinearLayout.HORIZONTAL);



                    if(MainActivity.isNightMode(view.getContext())) {
                        linearLayoutMainCardHorizontal.setBackgroundColor(
                                Color.parseColor("#302f2d")
                        );
                    } else {
//                        linearLayoutMainCardHorizontal.setBackgroundColor(
//                                Color.parseColor("#ebedf0")
//                        );
                    }

                    linearLayoutScrollableMainFrame.setOrientation(LinearLayout.VERTICAL);
                    linearLayoutScrollableMainFrame.addView(linearLayoutMainCardHorizontal);


                }

            } else {
                Toast.makeText(view.getContext(), "ScrollView svHistory is null",
                        Toast.LENGTH_SHORT).show();
                Log.e("rr345", "ScrollView svHistory is null");
            }
        }catch (Exception e) {
            Toast.makeText(view.getContext(), "Can't load history", Toast.LENGTH_SHORT).show();
            Log.e("rr345", "ScrollView svHistory is null");

        }



    }


    public static Bitmap retrieveContactPhoto(Context context, String number) {
        ContentResolver contentResolver = context.getContentResolver();
        String contactId = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                contentResolver.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            cursor.close();
        }

        Bitmap photo = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_image);

        try {
            if(contactId != null) {
                InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactId)));

                if (inputStream != null) {
                    photo = BitmapFactory.decodeStream(inputStream);
                }

                assert inputStream != null;
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }


    public static String getContactName(Context context, String phoneNumber) {



        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return phoneNumber;
        }
        String contactName = phoneNumber;
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }

}

