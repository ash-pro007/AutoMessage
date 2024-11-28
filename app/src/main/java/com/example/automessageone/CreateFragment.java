package com.example.automessageone;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;
import static com.example.automessageone.AutoMessageActivity.createFragment;
import static com.example.automessageone.MainActivity.context;
import static com.example.automessageone.MainActivity.getSavedObjectFromPreference;
import static com.example.automessageone.MainActivity.saveObjectToSharedPreference;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Calendar;

public class CreateFragment extends Fragment {

    private AdView adView;

    private CardView autoText;
    private CardView stopRing;

    private CardView cvSchedule;

    private CardView openMessage;

    private CardView cvReminder;

    private CardView cvSettings;

    private Context mContext = getContext();

    private static View view1;

    public static AudioManager audioManager;




    LinearLayout scrollViewAllActivity;

    public CreateFragment() {
        // Required empty public constructor
    }

    public static CreateFragment newInstance(String param1, String param2) {
        CreateFragment fragment = new CreateFragment();
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




        return inflater.inflate(R.layout.fragment_create, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null && isAdded()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(this);
            transaction.commitAllowingStateLoss();
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        view1 = view;

        // =======================================================


        if(getSavedObjectFromPreference(getContext(), "autoMessageObject",
                "key1ForObject", AutoMessage.class) != null) {

            AutoMessageActivity.autoMessage = getSavedObjectFromPreference(getContext(),
                    "autoMessageObject", "key1ForObject",
                    AutoMessage.class);

            // addSessionCard("From Shared Pref", AutoMessageActivity.timeSelectedByUser); // ----removed temporary
        }
//        } else {
////            Toast.makeText(getContext(), "There is no Auto Message Session exiting!",
////                    Toast.LENGTH_SHORT).show();
//        }
//


        // ========================================================


        //  TODO:

        LinearLayout adLinearLayout = view1.findViewById(R.id.ad1);

        loadBanner(adLinearLayout);

        autoText = (CardView) view.findViewById(R.id.cvAutoMessage);

        autoText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(MainActivity.isAutoMessageSessionExists) {
                    Toast.makeText(getContext(),
                            "Please delete existing session before creating new one!",
                            Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(getContext(), AutoMessageActivity.class));
                }

            }
        });
        super.onViewCreated(view, savedInstanceState);

        // TODO- Stop ring only

        stopRing = (CardView) view.findViewById(R.id.cvStopRing);
        stopRing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(MainActivity.isAutoMessageSessionExists) {
                    Toast.makeText(getContext(),
                            "Please delete existing session before creating new one!",
                            Toast.LENGTH_LONG).show();
                } else {

                    startActivity(new Intent(getContext(), StopRingActivity.class));

                }
                // The permission
            }
        });


        // TODO - shedule

        cvSchedule = view.findViewById(R.id.cvSchedule);

        cvSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // startActivity(new Intent(getContext(), ScheduleActivity.class));

                Toast.makeText(getContext(), "This feature is coming soon!", Toast.LENGTH_SHORT).show();

            }
        });


        // TODO - Message

        openMessage = view.findViewById(R.id.openMessage);

        openMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                startActivity(sendIntent);

            }
        });


        // TODO: Reminder

        cvReminder = view.findViewById(R.id.cvReminder);

        cvReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("tel:"));
                startActivity(sendIntent);
            }
        });


        // TODO: Settings

        cvSettings = view.findViewById(R.id.cvSettings);

        cvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SettingsActivity.class));

            }
        });


        // TODO --------------------------- This is the shared pref --------------------------------

        // TODO: -> shared preference

        SharedPreferences sh = getContext().getSharedPreferences("autoMessage", MODE_PRIVATE);

        if(sh.getString("isThereAnySession", "").equals("true")) {

            String autoMessage = sh.getString("autoMessage","");
            String inputForRingSilent = sh.getString("edtInputForRingSilent", "");


            AutoMessageActivity.autoMessage =  new AutoMessage(1, autoMessage,
                    Integer.parseInt(inputForRingSilent));

            AutoMessageActivity.chp = new ChangeSoundProfile(getContext());

            if(sh.getString("messageType", "").equals("stopRingActivity")) {
                addSessionCard("Silent only Service \nis activated till ",
                        sh.getString("timeSelectedByUser", ""));
            } else {
                addSessionCard("Auto Message Service \nis activated till ",
                        sh.getString("timeSelectedByUser", ""));
            }

            int hours = Integer.parseInt(sh.getString("hour24", ""));
            int mins = Integer.parseInt(sh.getString("min", ""));

            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();


            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, mins);
            calendar.set(Calendar.SECOND, 0);

            long alarmTime = calendar.getTimeInMillis();

            if (System.currentTimeMillis() > alarmTime) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                alarmTime = calendar.getTimeInMillis();
            }

            Intent intent = new Intent(getContext(), AlarmBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent,  PendingIntent.FLAG_IMMUTABLE);

            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);

            Toast.makeText(getContext(), "Found the shared pref and Created", Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor = sh.edit();

            editor.putString("isThereAnySession", "false");
            editor.apply();

        } else {
            Toast.makeText(getContext(), "Not Created", Toast.LENGTH_SHORT).show();
        }




    }



















    @Override
    public void onStart() {
        super.onStart();

    }

    public void addSessionCard(String sessionName, String time) {

        // TODO: ad

        // TODO: Adjusting margin to add ad

        ScrollView scrollViewAllActivity = view1.findViewById(R.id.scrollViewAllActivity);

        ScrollView.LayoutParams scrollViewParams = new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.WRAP_CONTENT
        );

        scrollViewParams.setMargins(0, 550, 0, 0);
        scrollViewAllActivity.setLayoutParams(scrollViewParams);



        // >>>

        LinearLayout linearLayout = view1.findViewById(R.id.linearlayoutForAddedSession);


        CardView cardView = new CardView(view1.getContext());
        cardView.setBackgroundColor(R.color.black);

        CardView.LayoutParams cardLayoutParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                200
        );

        cardLayoutParams.setMargins(40, 20, 40, 20);

        if(MainActivity.isNightMode(view1.getContext()))
            cardView.setBackgroundResource(R.drawable.custom_card_rectangle_round_dark);
        else
            cardView.setBackgroundResource(R.drawable.custom_card_rectangle_round);


        TextView textView = new TextView(view1.getContext());
        textView.setText(sessionName + " ~ " + time);



        Button buttonEndSession = new Button(view1.getContext());
        buttonEndSession.setText("End Session");

        buttonEndSession.setTextSize(10);

        buttonEndSession.setBackgroundResource(R.drawable.custom_button6);

        LinearLayout linearLayoutHorizontal = new LinearLayout(view1.getContext());

        linearLayoutHorizontal.addView(textView);
        linearLayoutHorizontal.addView(buttonEndSession);



        cardView.addView(linearLayoutHorizontal);
        linearLayout.addView(cardView);


        // TODO - End Session button functionality ------------------------------------------------

        buttonEndSession.setTextColor(R.color.white);

        buttonEndSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: Clear the shared pref if exits;

                saveObjectToSharedPreference(getContext(), "autoMessageObject",
                        "key1ForObject", null);



                RestartUtil.restartApp(getContext());


                MediaPlayer mp = AlarmBroadcastReceiver.mp;

                AddExceptionForAutoMessageActivity.selectedContacts.clear();

                // -------------------------

                audioManager = AutoMessageActivity.audioManager;
                if(audioManager != null) {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                    MainActivity.isAutoMessageSessionExists = false;


                    AutoMessageActivity.autoMessage = null;

                    StopRingActivity.autoMessage = null;

                    try {
                        AutoMessageActivity.createFragment.onDestroy();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Can't delete the session!", Toast.LENGTH_SHORT).show();
                    }

                    Intent intent1 = new Intent(view1.getContext(), MainActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    view1.getContext().startActivity(intent1);

                    saveObjectToSharedPreference(context, "autoMessageObject",
                            "key1ForObject", null);

                }


                // TODO: Shared PRef

                try{
                    SharedPreferences sh = getContext().getSharedPreferences("autoMessage", MODE_PRIVATE);

                    SharedPreferences.Editor editor = sh.edit();

                    editor.putString("isThereAnySession", "false");
                    editor.apply();
                } catch (Exception e) {
                    Log.i("createFrag", String.valueOf(e));
                }

            }
        });


    }



    private void loadBanner(LinearLayout container) {

        // Create a new ad view.
        AdView adView = new AdView(getContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/9214589741");

        // Replace ad container with new ad view.
        container.addView(adView);

        // Start loading the ad in the background.
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


}