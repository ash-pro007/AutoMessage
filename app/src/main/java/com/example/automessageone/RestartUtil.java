package com.example.automessageone;

import android.content.Context;
import android.content.Intent;


import android.content.pm.PackageManager;
import android.widget.Toast;

public class RestartUtil {
    public static void restartApp(Context context) {
        try {
            if (context != null) {
                PackageManager packageManager = context.getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            }
        } catch (NullPointerException e) {
            //
        }
    }
}
