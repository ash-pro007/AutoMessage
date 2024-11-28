package com.example.automessageone;

import static com.example.automessageone.MainActivity.saveObjectToSharedPreference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

public class StopRingActivity extends AppCompatActivity {

    private static final int ON_DO_NOT_DISTURB_CALLBACK_CODE = 1;

    private TimePicker timePicker1;
    CardView cardSetMuteUnmute;
    Calendar calendar;
    String date;
    private String format = "";
    public static int hour;
    public static int min;
    public static int hour24;
    private String timeSelectedByUser = "";
    private TextView txtInputForRingSilent;
    private EditText edtInputForRingSilent;
    private static int noOfSilentRings;
    private TextView txtShowNumberOfRingSilent;
    private CardView cardAddException;
    private View dialogView;

    public static String soundProfileToBeSet = "Vibrate";
    Button btnAddSession;
    public static AudioManager audioManager;

    public static AutoMessage autoMessage;
    public static ChangeSoundProfile chp;

    public static CreateFragment createFragment;
    private static final int ALARM_REQUEST_CODE = 1001;

    AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_ring);


        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        CardView cardNoOfRingSilent = findViewById(R.id.cardNumberOfRing2);
        cardNoOfRingSilent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(
                        StopRingActivity.this,R.style.CustomAlertDialog);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(view.getContext()).
                        inflate(R.layout.popup_ringonly    , viewGroup, false);
                Button buttonOk=dialogView.findViewById(R.id.buttonOkStopRingOnly);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();



                //

                txtInputForRingSilent = dialogView.findViewById(R.id.txtInputForRingSilent);
                edtInputForRingSilent = dialogView.findViewById(R.id.edtInputForRingSilent);




                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Setting the Entered message to the autoMessageActivity's card of create message

                        // CardView cardCreateMessage = findViewById(R.id.cardMessage);


                        edtInputForRingSilent = dialogView.findViewById(R.id.edtInputForRingSilent);
                        txtShowNumberOfRingSilent = findViewById(R.id.txtShowNumberOfRingSilent);

                        String inputForRingSilent = edtInputForRingSilent.getText().toString();
                        txtShowNumberOfRingSilent.setText("Number of ring silent: " + inputForRingSilent);


                        alertDialog.dismiss();





                    }
                });


                alertDialog.show();



            }
        });



        // TODO: Mute unmute


        cardSetMuteUnmute = findViewById(R.id.cardSetMuteUnmute2);
        cardSetMuteUnmute.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(StopRingActivity.this,R.style.CustomAlertDialog);
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


        cardAddException = findViewById(R.id.cardAddException1);
        cardAddException.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(StopRingActivity.this,R.style.CustomAlertDialog);
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

                        startActivity(new Intent(StopRingActivity.this, AddExceptionForAutoMessageActivity.class));
                    }
                });

                // Apply to specific

                Button applyToSpecificContacts = dialogView.findViewById(R.id.applyToSpecific);

                applyToSpecificContacts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(StopRingActivity .this,
                                "This feature is coming soon...", Toast.LENGTH_SHORT).show();

                    }
                });


                //

                buttonOk3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        alertDialog.dismiss();


                    }
                });

                alertDialog.show();

            }
        });

        // TODO: RadioGroup

        RadioGroup radioSoundProfile = findViewById(R.id.rdSoundProfileStopRingOnly);

        radioSoundProfile.clearCheck();

        RadioButton rbVibrate = findViewById(R.id.rb_Vibrate_ring_only);
        rbVibrate.setChecked(true);

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

               if(Objects.equals(timeSelectedByUser, "")) {
                    Toast.makeText(StopRingActivity.this,
                            "Please select time to end session!", Toast.LENGTH_SHORT).show();
                } else {

                    if(Objects.equals(soundProfileToBeSet, "Vibrate")) {
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    }
                    else {
                        try {
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        }catch (Exception e) {
                            Toast.makeText(StopRingActivity.this, "Couldn't set ring mode to silent",
                                    Toast.LENGTH_SHORT).show();
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        }
                    }
                    // TODO:

                    MainActivity.isAutoMessageSessionExists = true;

                    // TODO: AutoMessage object
                    autoMessage = new AutoMessage(2, "Stop Ring Only",
                            Integer.parseInt(edtInputForRingSilent.getText().toString()));
                    // TODO:
                    chp = new ChangeSoundProfile(StopRingActivity.this);


                    finish();

                    createFragment = new CreateFragment();
                    createFragment.addSessionCard("Silent only Service \nis activated till ",
                            timeSelectedByUser);

                    // MainActivity.endSessionByTime(getApplicationContext());



                    // TODO - automatic end of the object by given time of user -----------------------------------------------------------------------------------

                    int hours = StopRingActivity.hour24;
                    int mins = StopRingActivity.min;


                    Log.i("tm3424", hours + ":" + mins);

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


                   Intent intent = new Intent(StopRingActivity.this,
                           AlarmBroadcastReceiver.class);

                   PendingIntent pendingIntent = PendingIntent.getBroadcast(
                           StopRingActivity.this, 0, intent,
                           PendingIntent.FLAG_IMMUTABLE);

                   alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);

                   Log.i("lrar", alarmTime + "");

                    // TODO -------------------------

                   saveObjectToSharedPreference(StopRingActivity.this,
                           "autoMessageObject", "key1ForObject",
                           autoMessage);


                   // TODO ------------------------- Shared Preference


                   SharedPreferences sharedPreferenceObj = getSharedPreferences("autoMessage",
                           MODE_PRIVATE);

                   SharedPreferences.Editor autoMessageEditor = sharedPreferenceObj.edit();

                   autoMessageEditor.putString("isThereAnySession", "true");
                   autoMessageEditor.putString("messageType", "stopRingActivity");

                   autoMessageEditor.putString("autoMessage", autoMessage.getMessage());
                   autoMessageEditor.putString("edtInputForRingSilent", edtInputForRingSilent.getText().toString() );
                   autoMessageEditor.putString("timeSelectedByUser", timeSelectedByUser);
                   autoMessageEditor.putString("hour24", hour24 + "");
                   autoMessageEditor.putString("min", min + "");

                   autoMessageEditor.apply();

                   Log.i("shared", "SharedPref created");


               }

            }
        });

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