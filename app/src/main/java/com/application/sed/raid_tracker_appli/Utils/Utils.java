package com.application.sed.raid_tracker_appli.Utils;

import android.util.Log;

public class Utils {

    public static String Name;

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


/**
 * Liens utiles
 *
 *
 *
 * http://www.tutos-android.com/changement-vues-passage-donnees-android
 **/