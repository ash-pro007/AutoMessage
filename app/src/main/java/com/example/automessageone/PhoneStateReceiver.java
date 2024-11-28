package com.example.automessageone;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

public class PhoneStateReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

        try {

            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){

                AutoMessage autoMessage = AutoMessageActivity.autoMessage;

                autoMessage.addCaller(incomingNumber);

                int numberOfCall = autoMessage.getNumberOfCalls();

                Log.i("tag1", "number of call = " + numberOfCall +
                        " || autoMessage.HowManyMissCalls(incomingNumber) = "
                        + autoMessage.getHowManyMissCalls(incomingNumber));

//                if((numberOfCall - 1) < autoMessage.getHowManyMissCalls(incomingNumber)) {



                if(numberOfCall < autoMessage.getHowManyMissCalls(incomingNumber) || MainActivity.selectedContactNumbers.contains(incomingNumber)) {

//                    if(numberOfCall < autoMessage.getHowManyMissCalls(incomingNumber)) {
//                        Toast.makeText(context,"Inside if...",Toast.LENGTH_SHORT).show();
//
//
//                    }
//                    else {
//
//                        AutoMessageActivity.chp.changeToNormal();
//                        SmsManager smsManager=SmsManager.getDefault();
//                        smsManager.sendTextMessage(incomingNumber,null, autoMessage.getMessage()
//                                ,null,null);
//                    }

                    AutoMessageActivity.chp.startRing();


                }
                else {

                    if(autoMessage.getType() == 1) {
                        SmsManager smsManager=SmsManager.getDefault();
                        smsManager.sendTextMessage(incomingNumber,null, autoMessage.getMessage()
                                ,null,null);

                        // TODO : creating object that will store sent message
                        // Replace this with the phone number you want to search for

                        ContactUtils.ContactInfo contactInfo = ContactUtils.getContactInfoFromPhoneNumber(context, incomingNumber);


                        String contactName = "Unknown";
                        if (!TextUtils.isEmpty(contactInfo.getName())) {
                            // Contact name found
                            contactName = contactInfo.getName();
                            // Use contactName as needed
                            Log.i("llg34", contactName);
                        }

                        Uri photoUri = null;
                        if (contactInfo.getPhotoUri() != null) {
                            // Contact photo found
                            photoUri = contactInfo.getPhotoUri();
                            Log.i("llg34", " -> Photo Found");
                            // Use photoUri as needed
                        }



                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH); // Note: Month starts from 0 (January)
                        // int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        int day = calendar.get(Calendar.DAY_OF_WEEK);


                        // TODO: Creating object
                        String date = day + "/" + month + "/" + year;
                        String time = hour + ":" + minute;
                        SentMessageInfo messageInfo = new SentMessageInfo(incomingNumber, contactName,
                                photoUri, time, date);


                        // TODO: Sending sent message data to database

                        try {
                            DataBaseHelper db = new DataBaseHelper(context);

                            MessageHistory messageHistory = new MessageHistory(time+date, incomingNumber,
                                    autoMessage.getMessage(), time, date, getDay(day));

                            db.addHistory(messageHistory);
                        } catch (Exception e) {
                            Toast.makeText(context, "Well Data base is not working I think",
                                    Toast.LENGTH_LONG).show();
                        }
                    }


                }




            }
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
                // Toast.makeText(context,"Call Received State",Toast.LENGTH_SHORT).show();

                AutoMessageActivity.chp.onPause();
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                // Toast.makeText(context,"Call Idle State",Toast.LENGTH_SHORT).show();

                AutoMessageActivity.chp.onPause();
                AutoMessageActivity.chp.requestMutePermissions();

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    private String getDay(int day) {
        switch (day) {
            case 1:
                return "Mon";
            case 2:
                return "Tue";
            case 3:
                return "Wed";
            case 4:
                return "Thr";
            case 5:
                return "Fri";
            case 6:
                return "Sat";
            case 7:
                return "Sun";
            default:
                return "Day";

        }

    }


}