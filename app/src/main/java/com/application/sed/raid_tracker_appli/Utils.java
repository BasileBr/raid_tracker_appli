package com.application.sed.raid_tracker_appli;

import android.util.Log;

public class Utils {

    public static void info(String TAG, String log){
        Log.i(TAG, log);
    }

    public static void warm(String TAG, String log){
        Log.w(TAG, log);
    }

    public static void err(String TAG, String log){
        Log.e(TAG, log);
    }

    public static void debug(String TAG, String log){
        Log.d(TAG, log);
    }
}
