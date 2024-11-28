package com.example.automessageone;

import static com.example.automessageone.MainActivity.context;
import static com.example.automessageone.MainActivity.getAppContext;
import static com.example.automessageone.MainActivity.saveObjectToSharedPreference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AutoMessageActivity extends AppCompatActivity {

    private static final int ON_DO_NOT_DISTURB_CALLBACK_CODE = 1;
    private CardView cardViewMessage;
    private CardView cardSetMuteUnmute;
    private CardView cardAddException;
    private EditText edtCreateMessage;
    private EditText edtInputForRingSilent;
    private TextView txtInputForRingSilent;
    private TextView txtServices;

    private LinearLayout linearlayoutShowServices;
    private CardView cardServices;

    private TextView txtShowNumberOfRingSilent;

    private LinearLayout linearLayoutForInput;
    private String date;

    private String messageToSend;



    // Time picker
    private TimePicker timePicker1;
    private TextView time;
    private Calendar calendar;
    private String format = "";
    public static String timeSelectedByUser = "";
    private String currentTimeStringWithFormat;


    public static int hour;
    public static int min;

    public static int hour24;

    public static AutoMessage autoMessage = null;
    public static ChangeSoundProfile chp;
    private Button btnAddSession;


    public static AudioManager audioManager;

    // visibility variables
    boolean isEditCreateMessageVisible = false;
    boolean isBtnCreateMessageVisible = true;
    boolean isBtnUseTemplateVisible = true;

    // TODO _
    public static String enteredMessage = "";
    private static int noOfSilentRings;

    //
    private View dialogView;
    private static final int ALARM_REQUEST_CODE = 1001;

    AlarmManager alarmManager;


    // TODO: variable for timer to end the session with timer.

    public static boolean isSessionCreated = false;
    public static boolean isTimeRemaining = true;


    public static CreateFragment createFragment;

    // TODO -----------------------  main() function starts here.

    public static String soundProfileToBeSet = "";

    // TODO ------------------------- linear layout

    LinearLayout ll1, ll2, ll3, ll4, ll5;

    // Recycler view




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_message);

        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll4 = findViewById(R.id.ll4);
        ll5 = findViewById(R.id.ll5);


        // TODO --> Invocation of the alarm manager

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                //process


                ll1 = findViewById(R.id.ll1);
                ll2 = findViewById(R.id.ll2);
                ll3 = findViewById(R.id.ll3);
                ll4 = findViewById(R.id.ll4);
                ll5 = findViewById(R.id.ll5);

                ll1.setBackgroundColor(R.color.black);
                ll2.setBackgroundColor(R.color.black);
                ll3.setBackgroundColor(R.color.black);
                ll4.setBackgroundColor(R.color.black);
                ll5.setBackgroundColor(R.color.black);


                break;
            case Configuration.UI_MODE_NIGHT_NO:
                // process

                break;
        }

        //

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Set Message

        cardViewMessage = findViewById(R.id.cardMessage);
        cardViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AutoMessageActivity.this,R.style.CustomAlertDialog);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.popup_create_message, viewGroup, false);
                Button buttonOk=dialogView.findViewById(R.id.buttonOk);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();


                // Making edt create message visible
                Button btnUseTemplate = dialogView.findViewById(R.id.btnUseTemplate);
                Button btnCreateMessage =  dialogView.findViewById(R.id.btnCreateMessage);
                edtCreateMessage =  dialogView.findViewById(R.id.edtCreateMessage);

                // TODO Drop down spinner


                Spinner spinnerTemplate = (Spinner) dialogView.findViewById(R.id.spinner);
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AutoMessageActivity.this,
                        R.array.templates, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinnerTemplate.setAdapter(adapter);


                //


                txtInputForRingSilent = dialogView.findViewById(R.id.txtInputForRingSilent);
                edtInputForRingSilent = dialogView.findViewById(R.id.edtInputForRingSilent);

                btnCreateMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isBtnCreateMessageVisible = false;
                        isEditCreateMessageVisible = true;
                        edtCreateMessage.setVisibility(View.VISIBLE);

                        txtInputForRingSilent.setVisibility(View.VISIBLE);
                        edtInputForRingSilent.setVisibility(View.VISIBLE);


                        btnCreateMessage.setVisibility(View.GONE);
                        spinnerTemplate.setVisibility(View.GONE);
                        btnUseTemplate.setVisibility(View.VISIBLE);

                        // Getting the data from user

                        enteredMessage =  edtCreateMessage.getText().toString();
                        noOfSilentRings = Integer.parseInt(edtInputForRingSilent.getText().toString());

                    }
                });

                btnUseTemplate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnUseTemplate.setVisibility(View.GONE);
                        btnCreateMessage.setVisibility(View.VISIBLE);
                        edtCreateMessage.setVisibility(View.GONE);


                        spinnerTemplate.setVisibility(View.VISIBLE);




                        spinnerTemplate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {



                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                                // checking that if night mode is on

                                //  ((TextView) spinnerTemplate.getChildAt(0)).setTextColor(R.color.white);



                                String[] arr = getResources().getStringArray(R.array.templates);

                                enteredMessage = arr[i];

                                Toast.makeText(AutoMessageActivity.this, "You selected: "  + enteredMessage
                                        , Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }

                });


                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Setting the Entered message to the autoMessageActivity's card of create message

                        // CardView cardCreateMessage = findViewById(R.id.cardMessage);


                        if(edtCreateMessage.getText().toString().length() == 0 && enteredMessage == "") {
                            Toast.makeText(AutoMessageActivity.this, "Please enter a message to send"
                                    , Toast.LENGTH_SHORT).show();
                        }
//                        else if(noOfSilentRings < 1) {
//                            Toast.makeText(AutoMessageActivity.this, "Please enter a valid number to silent ring"
//                                    , Toast.LENGTH_SHORT).show();
//                        }

                        else {

                            if(edtCreateMessage.getText().toString().length() != 0) {
                                enteredMessage = edtCreateMessage.getText().toString();
                            }

                            TextView txtCreateMessage = findViewById(R.id.txtCreateMessage);
                            txtCreateMessage.setText("Set Message:  " + enteredMessage );

                            edtInputForRingSilent = dialogView.findViewById(R.id.edtInputForRingSilent);
                            txtShowNumberOfRingSilent = findViewById(R.id.txtShowNumberOfRingSilent);

                            String inputForRingSilent = edtInputForRingSilent.getText().toString();
                            txtShowNumberOfRingSilent.setText("Number of ring silent: " +
                                    inputForRingSilent);


                            if(enteredMessage.length() >= 15 && enteredMessage.length() <= 20) {
                                ll1.getLayoutParams().height = (enteredMessage.length() * 10) ;
                                ll1.getLayoutParams().width = -1;
                                ll1.requestLayout();
                            }

                            if(enteredMessage.length() >= 20 && enteredMessage.length() <= 30) {
                                ll1.getLayoutParams().height = (enteredMessage.length() * 9) ;
                                ll1.getLayoutParams().width = -1;
                                ll1.requestLayout();
                            }

                            if(enteredMessage.length() >= 30) {
                                ll1.getLayoutParams().height = (enteredMessage.length() * 7) ;
                                ll1.getLayoutParams().width = -1;
                                ll1.requestLayout();
                            }


                            // Todo -> setting media volume to 90

                            AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, SettingsActivity.mediaVolume, 0);


                            alertDialog.dismiss();
                        }




                    }
                });


                alertDialog.show();



            }
        });


        // TODO: Set Mute and UnMute
        cardSetMuteUnmute = findViewById(R.id.cardSetMuteUnmute);
        cardSetMuteUnmute.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AutoMessageActivity.this,R.style.CustomAlertDialog);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.popup_mute_unmute, viewGroup, false);
                Button buttonOk2=dialogView.findViewById(R.id.buttonOk2);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                buttonOk2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Setting the Entered message to the autoMessageActivity's card of create message

                        // CardView cardCreateMessage = findViewById(R.id.cardMessage);

                        timePicker1 = (TimePicker) dialogView.findViewById(R.id.timePicker1);
                        // time = (TextView) findViewById(R.id.textView1);
                        calendar = Calendar.getInstance();

                        timePicker1.setOnTimeChangedListener( new TimePicker.OnTimeChangedListener() {

                            @Override
                            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                                // TODO Auto-generated method stub
                                date = String.valueOf(hourOfDay).toString() + ":" + String.valueOf(minute).toString();
                                TextView txtTime = findViewById(R.id.txtTime);
                                txtTime.setText(new StringBuilder().
                                        append(String.valueOf(hourOfDay).toString()).
                                        append(" : ").append(String.valueOf(minute).toString())
                                        .append(" ").append(format));
                            }

                        });



                        alertDialog.dismiss();
                        // Toast.makeText(AutoMessageActivity.this, date, Toast.LENGTH_SHORT).show();

                        hour = timePicker1.getHour();
                        hour24 = hour;
                        min = timePicker1.getMinute();

                        Log.i("12312time", "hours = " + hour + " | min = " + min);

                        if (hour == 0) {
                            hour += 12;
                            format = "AM";
                        } else if (hour == 12) {
                            format = "PM";
                        } else if (hour > 12) {
                            hour -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }

                        TextView txtTime = findViewById(R.id.txtTime);

                        String temHour = hour + "", temMin = min + "";

                        if(hour <= 9)
                            temHour = "0" + temHour;


                        if(min <= 9)
                            temMin = "0" + temMin;

                        txtTime.setText(new StringBuilder().append("Till ~ " + temHour).append(" : ").append(temMin)
                                .append(" ").append(format));

                        // timeSelectedByUser = hour + ":" + min + ":" + format;


                        timeSelectedByUser = temHour + ":" + temMin + " " + format;

                        Log.i("seletedTime", timeSelectedByUser);

                    }
                });



                alertDialog.show();
            }







        });

        // TODO: Exception
        cardAddException = findViewById(R.id.cardAddException1);
        cardAddException.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(AutoMessageActivity.this,R.style.CustomAlertDialog);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.pop_add_exception, viewGroup, false);
                Button buttonOk3=dialogView.findViewById(R.id.buttonOk3);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                // TODO: Clearing all the previous contact from list which were added in list eariler

                MainActivity.selectedContactNumbers.clear();


                // Making edt create message visible

                Button btnAddExceptionalContacts;
                btnAddExceptionalContacts = dialogView.findViewById(R.id.btnAddExceptionalContacts);
                btnAddExceptionalContacts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                       showContactsOnPopup();

                        startActivity(new Intent(AutoMessageActivity.this, AddExceptionForAutoMessageActivity.class));
                    }
                });

                // Apply to specific

                Button applyToSpecificContacts = dialogView.findViewById(R.id.applyToSpecific);

                applyToSpecificContacts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(AutoMessageActivity.this,
                                "This feature is coming soon...", Toast.LENGTH_SHORT).show();

                    }
                });


                //

                buttonOk3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        // -------------------------------------------------------------------------

                        // TODO: code to show added contact upfront

//                        llAddContactForExeception = findViewById(R.id.llAddContactForExeception);
//
//                        llAddContactForExeception.getLayoutParams().height = 200 ;
//                        llAddContactForExeception.getLayoutParams().width = -1;
//                        llAddContactForExeception.requestLayout();
//
//
//
//                        recyclerViewToShowTheAddContact = findViewById(R.id.recyclerViewAddedContactsShow);
//
//                        recyclerViewToShowTheAddContact.setAdapter(contactAdapter);
//
//                        LinearLayout llRecyclerViewContactsShowContainer = findViewById(R.id.llRecyclerViewContactsShowContainer);
//
//                        llRecyclerViewContactsShowContainer.setVisibility(View.VISIBLE);
//
//
//                        Toast.makeText(AutoMessageActivity.this, "button pressed", Toast.LENGTH_SHORT).show();
//


                        // -------------------------------------------------------------------------


                        alertDialog.dismiss();


                    }
                });

                alertDialog.show();

            }
        });


        // Services
        linearlayoutShowServices = findViewById(R.id.linearlayout_show_services);

        cardServices = findViewById(R.id.cardServices);
        cardServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                txtServices = findViewById(R.id.txtServices);
//
//                linearlayoutShowServices.setVisibility(View.VISIBLE);
//                txtServices.setVisibility(View.GONE);

                Toast.makeText(AutoMessageActivity.this,
                        "This is feature is coming soon!", Toast.LENGTH_SHORT).show();


            }
        });


        // TODO: Radio button
        RadioGroup radioSoundProfile = findViewById(R.id.rdSoundProfile);

        radioSoundProfile.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Get the selected Radio Button
                RadioButton radioButton = group.findViewById(checkedId);

                soundProfileToBeSet = radioButton.getText().toString();


            }
        });


        btnAddSession = findViewById(R.id.btnAddSession);
        btnAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSession();
            }
        });


    }

    public void createSession() {

        if(Objects.equals(enteredMessage, "")) {
            Toast.makeText(AutoMessageActivity.this,
                    "Please create a message to send!", Toast.LENGTH_SHORT).show();
        } else if(Objects.equals(timeSelectedByUser, "")) {
            Toast.makeText(AutoMessageActivity.this,
                    "Please select time to end session!", Toast.LENGTH_SHORT).show();
        } else {

            if(Objects.equals(soundProfileToBeSet, "Vibrate")) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            }
            else {
                try {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                }catch (Exception e) {
                    Toast.makeText(AutoMessageActivity.this, "Couldn't set ring mode to silent",
                            Toast.LENGTH_SHORT).show();
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                }
            }
            // TODO:

            MainActivity.isAutoMessageSessionExists = true;

            // TODO: AutoMessage object
            autoMessage = new AutoMessage(1, enteredMessage,
                    Integer.parseInt(edtInputForRingSilent.getText().toString()));
            // TODO:
            chp = new ChangeSoundProfile(AutoMessageActivity.this);

            finish();

            createFragment = new CreateFragment();

            createFragment.addSessionCard(
                    "Auto Message Service \nis activated till ",
                    timeSelectedByUser);

            // MainActivity.endSessionByTime(getApplicationContext());


            // TODO - automatic end of the object by given time of user --------------------

            int hours = AutoMessageActivity.hour24;
            int mins = AutoMessageActivity.min;


            Log.i("tm3424", hours + ":" + min);

            // TODO  --- cal

            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();


            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, mins);
            calendar.set(Calendar.SECOND, 0);

            long alarmTime = calendar.getTimeInMillis();

            if (System.currentTimeMillis() > alarmTime) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                alarmTime = calendar.getTimeInMillis();
            }


            Intent intent = new Intent(AutoMessageActivity.this, AlarmBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(AutoMessageActivity.this, 0, intent,  PendingIntent.FLAG_IMMUTABLE);

            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);

            // TODO ::: this is the object which will be saved in shared pref

            saveObjectToSharedPreference(AutoMessageActivity.this,
                    "autoMessageObject", "key1ForObject",
                    autoMessage);

            Log.i("dslkf", alarmTime + "");

            //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);

            // TODO -------------------------

//                    Intent iBroadCast = new Intent(AutoMessageActivity.this, AlarmBroadcastReceiver.class);
//                    PendingIntent pi = PendingIntent.getBroadcast(AutoMessageActivity.this, ALARM_REQUEST_CODE,
//                            iBroadCast ,PendingIntent.FLAG_IMMUTABLE);
//
//                    Log.i("lgo123", calendar.getTimeInMillis()+"");
//
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);

//                    String st = calendar.getTimeInMillis() + "";
//                    Log.i("t3434", st);
//
//                    scheduleAlarm(calendar);


            // TODO ------------------------- Shared Preference


            SharedPreferences sharedPreferenceObj = getSharedPreferences("autoMessage",
                    MODE_PRIVATE);

            SharedPreferences.Editor autoMessageEditor = sharedPreferenceObj.edit();

            autoMessageEditor.putString("isThereAnySession", "true");

            autoMessageEditor.putString("messageType", "autoMessage");

            autoMessageEditor.putString("autoMessage", autoMessage.getMessage());
            autoMessageEditor.putString("edtInputForRingSilent", edtInputForRingSilent.getText().toString() );
            autoMessageEditor.putString("timeSelectedByUser", timeSelectedByUser);
            autoMessageEditor.putString("hour24", hour24 + "");
            autoMessageEditor.putString("min", min + "");

            autoMessageEditor.apply();

            Log.i("shared", "SharedPref created");



            // TODO: forcing app to be in background


            startService(new Intent(this, YourService.class));
        }


    }

    private void scheduleAlarm(Calendar selectedTime) {
        // Create an Intent for your BroadcastReceiver
        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get the AlarmManager service
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Schedule the alarm
        if (alarmManager != null) {
            // Use setExactAndAllowWhileIdle for API >= 23
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        selectedTime.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, selectedTime.getTimeInMillis(),
                        pendingIntent);
            }

            // Check if the alarm is correctly set
            Toast.makeText(this, "Alarm set successfully for " + selectedTime.getTime().toString(), Toast.LENGTH_SHORT).show();
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
            return null;
        }
        return photo;
    }

    private void requestMutePermissions() {
        try {
            if (Build.VERSION.SDK_INT < 23) {
                AudioManager audioManager = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else if( Build.VERSION.SDK_INT >= 23 ) {
                this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
            }
        } catch ( SecurityException e ) {

        }
    }

    private void requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp() {

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // if user granted access else ask for permission
        if ( notificationManager.isNotificationPolicyAccessGranted()) {
            AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else{
            // Open Setting screen to ask for permisssion
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivityForResult( intent, ON_DO_NOT_DISTURB_CALLBACK_CODE );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ON_DO_NOT_DISTURB_CALLBACK_CODE) {
            this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
        }
    }

}

