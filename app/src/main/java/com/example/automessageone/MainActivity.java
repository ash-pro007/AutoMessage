package com.example.automessageone;

import static com.example.automessageone.AutoMessageActivity.createFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static Context context;

    private static final int REQUEST_PERMISSIONS_CODE = 1001;
    // public static ArrayList<SentMessageInfo> historyList = new ArrayList<>();
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentAdapter fragmentAdapter;
    private Context mContext= this;
    private static final int REQUEST_CONTACT = 112;
    public static boolean isAutoMessageSessionExists = false;
    public static List<String> selectedContactNumbers = new ArrayList<>();
    private DataBaseHelper dbHelper;


    // TODO ---------- shared pref



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        }


        // TODO -> Checking if automessage exits

        try{
            AutoMessageActivity.autoMessage = getSavedObjectFromPreference(this, "autoMessageObject",
                    "key1ForObject", AutoMessage.class);
        }catch (Exception e) {
            //
        }


        dbHelper = new DataBaseHelper(this);
        // This will trigger onCreate() in your DBHelper if the database doesn't exist yet
        dbHelper.getWritableDatabase();



        //


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        // Code --

        DataBaseHelper db = new DataBaseHelper(this);


        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);

        // setSupportActionBar(toolbar);

        tabLayout.addTab(tabLayout.newTab().setText("Create"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(fragmentAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }


    // All required

    // TODO ----------------------------------- onCraete() =======================================<>


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // TODO - permission

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode == REQUEST_CONTACT) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mContext, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }


//        if(requestCode == REQUEST_SMS) {
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(mContext, "Permission Granted", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//        }

        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            // Check if all permissions are granted
            boolean allGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                // Permissions granted
            } else {
                // Permissions denied
                // Handle the user's denial
                // You can show a message or disable functionality
            }
        }



    }

    private void checkPermissions() {
        // Check if permissions are granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_MMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_MMS) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

                    // Show an explanation to the user
                    // This is asynchronous and the application will continue its execution right after this code
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.RECEIVE_MMS,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_CONTACTS,
                    }, REQUEST_PERMISSIONS_CODE);

                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.RECEIVE_MMS,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_CONTACTS,

                    }, REQUEST_PERMISSIONS_CODE);
                }

            }
        }
    }





    public static void endAutoMessageSession() {
    }

    public static void restartActivity(Activity act){

        Intent intent=new Intent();
        intent.setClass(act, act.getClass());
        act.startActivity(intent);
        act.finish();

    }

    public static void endSessionByTime(Context context) {


    }

    public static void endSession() {

    }


    public void updatePref(SentMessageInfo messageInfo) {
        //set variables of 'myObject', etc.
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Fetching the stored data from the SharedPreference

        AutoMessageActivity.autoMessage = getSavedObjectFromPreference(this, "autoMessageObject",
                "key1ForObject", AutoMessage.class);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    public static boolean isNightMode(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }


    public static void saveObjectToSharedPreference(Context context, String preferenceFileName,
                                                    String serializedObjectKey, AutoMessage object) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            final Gson gson = new Gson();
            String serializedObject = gson.toJson(object);
            sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
            sharedPreferencesEditor.apply();
        }catch (Exception e) {
            ///
        }
    }

    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Class<GenericClass> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }

    public static void clearSharedPreferences(Context context, String preferenceFileName) {
        SharedPreferences preferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply(); // or editor.commit() if you need immediate execution
    }

}
