package com.example.automessageone;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;

public class ChangeSoundProfile {
    
    private Activity activity;

    private MediaPlayer mediaPlayer;

    private static final int ON_DO_NOT_DISTURB_CALLBACK_CODE = 1;

    public static int ringtoneNo = 1;

    public ChangeSoundProfile(Context context) {
        activity = (Activity) context;
    }

    public void changeToNormal() {

        AudioManager audioManager;

        audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.MODE_RINGTONE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

    }

    public void requestMutePermissions() {
        try {
            if (Build.VERSION.SDK_INT < 23) {
                AudioManager audioManager = (AudioManager) activity.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else if( Build.VERSION.SDK_INT >= 23 ) {
                this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
            }
        } catch ( SecurityException e ) {

        }
    }

    public void requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp() {

        NotificationManager notificationManager = (NotificationManager) activity.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // if user granted access else ask for permission
        if ( notificationManager.isNotificationPolicyAccessGranted()) {
            AudioManager audioManager = (AudioManager) activity.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else{
            // Open Setting screen to ask for permisssion
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            activity.startActivityForResult( intent, ON_DO_NOT_DISTURB_CALLBACK_CODE );
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ON_DO_NOT_DISTURB_CALLBACK_CODE) {
            this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
        }
    }


    public void startRing() {

        if(ringtoneNo == 2)
            mediaPlayer = MediaPlayer.create(activity.getApplicationContext(), R.raw.ringtone2);
        else if(ringtoneNo == 3)
            mediaPlayer = MediaPlayer.create(activity.getApplicationContext(), R.raw.ringtone3);
        else if(ringtoneNo == 4)
            mediaPlayer = MediaPlayer.create(activity.getApplicationContext(), R.raw.ringtone4);
        else if(ringtoneNo == 5)
            mediaPlayer = MediaPlayer.create(activity.getApplicationContext(), R.raw.ringtone5);
        else if(ringtoneNo == 6)
            mediaPlayer = MediaPlayer.create(activity.getApplicationContext(), R.raw.ringtone6);
        else
            mediaPlayer = MediaPlayer.create(activity.getApplicationContext(), R.raw.ringtone1);


        mediaPlayer.start();
    }

    protected void onPause() {

        mediaPlayer.stop();
        mediaPlayer.release();

    }

}
