package com.example.automessageone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    public static int mediaVolume = 90;

    TextView tvShowMediaVolPercentage;
    SeekBar seekBarMediaVolume;

    LinearLayout llSelectRingtone;
    RadioButton rbRingtone1;
    RadioButton rbRingtone2;
    RadioButton rbRingtone3;
    RadioButton rbRingtone4;
    RadioButton rbRingtone5;
    RadioButton rbRingtone6;

    String selectRingtone = "Ringtone 1";

    Button btnSaveSettings;
    Button btnDiscardSettings;

    TextView tvShowSelectedRingtone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        seekBarMediaVolume = findViewById(R.id.seekBar_media_volume);

        tvShowMediaVolPercentage = findViewById(R.id.tv_show_media_volume_percentage);

        seekBarMediaVolume
                .setOnSeekBarChangeListener(
                        new SeekBar
                                .OnSeekBarChangeListener() {

                            // When the progress value has changed
                            @Override
                            public void onProgressChanged(
                                    SeekBar seekBar,
                                    int progress,
                                    boolean fromUser)
                            {

                                // increment 1 in progress and
                                // increase the textsize
                                // with the value of progress
//                                message.setTextSize(progress + 1);

                                Log.i("logVOl", progress + "");

                                String progressString = progress + "";

                                tvShowMediaVolPercentage.setText(progressString);
                                mediaVolume = progress;

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar)
                            {

                                // This method will automatically
                                // called when the user touches the SeekBar
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar)
                            {

                                // This method will automatically
                                // called when the user
                                // stops touching the SeekBar
                            }
                        });

        llSelectRingtone = findViewById(R.id.ll_select_ringtone);

        llSelectRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this,R.style.CustomAlertDialog);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.pop_select_ringtone, viewGroup, false);

                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                // TODO -

                RadioButton rbRingtone1;
                RadioButton rbRingtone2;
                RadioButton rbRingtone3;
                RadioButton rbRingtone4;
                RadioButton rbRingtone5;
                RadioButton rbRingtone6;

                rbRingtone1 = dialogView.findViewById(R.id.rb_ringtone1);
                rbRingtone2 = dialogView.findViewById(R.id.rb_ringtone2);
                rbRingtone3 = dialogView.findViewById(R.id.rb_ringtone3);
                rbRingtone4 = dialogView.findViewById(R.id.rb_ringtone4);
                rbRingtone5 = dialogView.findViewById(R.id.rb_ringtone5);
                rbRingtone6 = dialogView.findViewById(R.id.rb_ringtone6);

                ChangeSoundProfile changeSoundProfile = new ChangeSoundProfile(SettingsActivity.this);


                rbRingtone1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            changeSoundProfile.onPause();
                        }catch (Exception e) {

                        }

                        ChangeSoundProfile.ringtoneNo = 1;
                        changeSoundProfile.startRing();

                    }
                });



                rbRingtone2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            changeSoundProfile.onPause();
                        }catch (Exception e) {

                        }

                        ChangeSoundProfile.ringtoneNo = 2;
                        changeSoundProfile.startRing();

                        selectRingtone = "Ringtone 2";
                    }
                });

                rbRingtone3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            changeSoundProfile.onPause();
                        }catch (Exception e) {

                        }

                        ChangeSoundProfile.ringtoneNo = 3;
                        changeSoundProfile.startRing();

                        selectRingtone = "Ringtone 3";

                    }
                });

                rbRingtone4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            changeSoundProfile.onPause();
                        }catch (Exception e) {

                        }

                        ChangeSoundProfile.ringtoneNo = 4;
                        changeSoundProfile.startRing();

                        selectRingtone = "Ringtone 4";

                    }
                });

                rbRingtone5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            changeSoundProfile.onPause();
                        }catch (Exception e) {

                        }

                        ChangeSoundProfile.ringtoneNo = 5;
                        changeSoundProfile.startRing();

                        selectRingtone = "Ringtone 5";

                    }
                });

                rbRingtone6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            changeSoundProfile.onPause();
                        }catch (Exception e) {

                        }

                        ChangeSoundProfile.ringtoneNo = 6;
                        changeSoundProfile.startRing();

                        selectRingtone = "Ringtone 6";

                    }
                });

                Button btnSelectRingtone;
                btnSelectRingtone = dialogView.findViewById(R.id.btn_selection_ringtone_done);

                btnSelectRingtone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            changeSoundProfile.onPause();
                            tvShowSelectedRingtone.setText(selectRingtone);

                            alertDialog.dismiss();
                        }catch (Exception e) {

                        }
                    }
                });


                alertDialog.show();

            }
        });


        // TODO -\

        btnSaveSettings = findViewById(R.id.btn_discard_settings);

        tvShowSelectedRingtone = findViewById(R.id.tv_show_selected_ringtone);

        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDiscardSettings = findViewById(R.id.btn_save_settings);



    }
}