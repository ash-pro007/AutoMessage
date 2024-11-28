package com.example.automessageone;

import static com.example.automessageone.HistoryFragment.retrieveContactPhoto;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteMessageHistoryActivity extends AppCompatActivity {

    ImageButton imgDeleteMessageHistory;
    LinearLayout linearLayoutOfScrollView;

    private HashMap<Integer, Boolean> isLayoutChecked = new HashMap<>();

    private ArrayList<Integer> messageToBeDeleted = new ArrayList<>();  // <<----------< this is the list which will bet
                                                                        // have final id's of message object which are going to be removed

    private HashMap<Integer, MessageHistory> allMessageWithId = new HashMap<>();
    int id;

    String random = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_message_history);

        imgDeleteMessageHistory = findViewById(R.id.imgDeleteMessageHistory);


        if (MainActivity.isNightMode(this)) {

            imgDeleteMessageHistory.setImageResource(R.drawable.baseline_delete_forever_24_dark);
            imgDeleteMessageHistory.setBackgroundColor(R.color.dark_mode_color);

        }

        DataBaseHelper db = new DataBaseHelper(this);
        List<MessageHistory> messageHistories = db.getHistory();

        id = messageHistories.size();

        // id = db.getHistory().size();

        try {

            linearLayoutOfScrollView = findViewById(R.id.linearLayScrollViewDeleteHistory);


            //


            Collections.reverse(messageHistories);

            for(MessageHistory messageHistory: messageHistories) {

                // setting message object with id's

                allMessageWithId.put(id, messageHistory);

                //

                isLayoutChecked.put(id, false);

                Bitmap scaled;

                scaled =  retrieveContactPhoto(this, messageHistory.contactNumber);

                scaled = RoundedImageView.getCroppedBitmap(scaled, 130);

                LinearLayout linearLayoutMainCardHorizontal = new LinearLayout(this);
                linearLayoutMainCardHorizontal.setId(id);

                //

                LinearLayout linearLayoutInfoAndMessageVertical = new LinearLayout(this);
                LinearLayout linearLayoutInfoHorizontal = new LinearLayout(this);



                ImageView imgViewContactImage = new ImageView(this);

                // TODO: changing the default image to contact image



                imgViewContactImage.setImageBitmap(scaled);
                LinearLayout.LayoutParams imageParam = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                imageParam.setMargins(20, 15, 20, 20);

                imgViewContactImage.setLayoutParams(imageParam);

                //

                TextView tvContactNameOrNumber = new TextView(this);
                TextView tvDay = new TextView(this);
                TextView tvTime = new TextView(this);
                TextView tvDate = new TextView(this);

                //



                tvContactNameOrNumber.setText(HistoryFragment.getContactName(this,
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

                TextView tvMessage = new TextView(this);
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





                linearLayoutOfScrollView.setOrientation(LinearLayout.VERTICAL);
                linearLayoutOfScrollView.addView(linearLayoutMainCardHorizontal);


                linearLayoutMainCardHorizontal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(Boolean.FALSE.equals(isLayoutChecked.get(linearLayoutMainCardHorizontal.getId()))) {


                            isLayoutChecked.replace(linearLayoutMainCardHorizontal.getId(), true);

                            messageToBeDeleted.add(linearLayoutMainCardHorizontal.getId());

                            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                                case Configuration.UI_MODE_NIGHT_YES:
                                    //process
//                                    Toast.makeText(DeleteMessageHistoryActivity.this,
//                                            "Dark Mode is on", Toast.LENGTH_SHORT).show();
                                    linearLayoutMainCardHorizontal.setBackgroundColor(getResources().getColor(android.R.color.white));

                                    tvContactNameOrNumber.setTextColor(R.color.black);
                                    tvDate.setTextColor(R.color.black);
                                    tvTime.setTextColor(R.color.black);
                                    tvDay.setTextColor(R.color.black);
                                    tvMessage.setTextColor(R.color.black);



                                    break;
                                case Configuration.UI_MODE_NIGHT_NO:
                                    // process
//                                    Toast.makeText(DeleteMessageHistoryActivity.this,
//                                            "Light Mode is on", Toast.LENGTH_SHORT).show();
                                    linearLayoutMainCardHorizontal.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                                    break;
                            }


                        } else {

                            isLayoutChecked.replace(linearLayoutMainCardHorizontal.getId(), false);

                            for(int x=0; x<messageToBeDeleted.size(); x++) {
                                if(messageToBeDeleted.get(x) == linearLayoutMainCardHorizontal.getId()) {
                                     messageToBeDeleted.remove(x);
                                }
                            }

                            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                                case Configuration.UI_MODE_NIGHT_YES:
                                    //process
//                                    Toast.makeText(DeleteMessageHistoryActivity.this,
//                                            "Dark Mode is on", Toast.LENGTH_SHORT).show();

                                    tvContactNameOrNumber.setTextColor(R.color.slight_blue);
                                    tvDate.setTextColor(R.color.white);
                                    tvTime.setTextColor(R.color.white);
                                    tvDay.setTextColor(R.color.white);
                                    tvMessage.setTextColor(R.color.white);

                                    linearLayoutMainCardHorizontal.setBackgroundColor(getResources().getColor(R.color.dark_mode_color));

                                    break;
                                case Configuration.UI_MODE_NIGHT_NO:
                                    // process
//                                    Toast.makeText(DeleteMessageHistoryActivity.this,
//                                            "Light Mode is on", Toast.LENGTH_SHORT).show();
                                    linearLayoutMainCardHorizontal.setBackgroundColor(getResources().getColor(android.R.color.white));

                                    break;
                            }
                        }


                    }
                });



                if(MainActivity.isNightMode(this)) {
                    linearLayoutMainCardHorizontal.setBackgroundColor(
                            Color.parseColor("#10130f")
                    );
                } else {
                    linearLayoutMainCardHorizontal.setBackgroundColor(
                            Color.parseColor("#ebedf0")
                    );
                }


                id--;
            }


        } catch (Exception e) {
            Toast.makeText(this, "There is some error in loading message...", Toast.LENGTH_SHORT).show();
        }

        imgDeleteMessageHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                db.deleteAllRecord();

                for(int i=0; i<messageToBeDeleted.size(); i++) {
                    allMessageWithId.remove(messageToBeDeleted.get(i));
                }


                for(Map.Entry<Integer,MessageHistory> entry : allMessageWithId.entrySet())
                    db.addHistory(entry.getValue());

                Toast.makeText(DeleteMessageHistoryActivity.this, "Messages have been deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DeleteMessageHistoryActivity.this, MainActivity.class));



            }
        });

    }


}