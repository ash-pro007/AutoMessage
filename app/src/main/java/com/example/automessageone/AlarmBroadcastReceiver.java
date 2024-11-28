package com.example.automessageone;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;
import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.startActivity;

import static com.example.automessageone.MainActivity.getAppContext;
import static com.example.automessageone.MainActivity.saveObjectToSharedPreference;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    public static MediaPlayer mp;
    AudioManager audioManager;

    @Override
    public void onReceive(Context context, Intent intent) {


        mp = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        mp.setLooping(true);

        // mp.start();

        // TODO: Activities that will happen when time comes

        audioManager = AutoMessageActivity.audioManager;
        if (audioManager != null) {
            // AudioManager is not null, so it's safe to use
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL); // or any other mode you need
        } else {
            // AudioManager is null, handle this situation accordingly
            Log.e("audioMan", "AudioManager is null");
        }
        MainActivity.isAutoMessageSessionExists = false;


        AutoMessageActivity.autoMessage = null;

        StopRingActivity.autoMessage = null;

        AddExceptionForAutoMessageActivity.selectedContacts.clear();

        // TODO: Shared Pref

        SharedPreferences sh = context.getSharedPreferences("autoMessage",
                MODE_PRIVATE);


        SharedPreferences.Editor editor = sh.edit();

        editor.putString("isThereAnySession", "false");
        editor.apply();


        try {
            AutoMessageActivity.createFragment.onDestroy();
        } catch(Exception e) {
            Log.i("AlarmBroad", String.valueOf(e));
        }


        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

        saveObjectToSharedPreference(context, "autoMessageObject",
                "key1ForObject", null);


    }
}
