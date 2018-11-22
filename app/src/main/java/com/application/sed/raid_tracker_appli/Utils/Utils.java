package com.application.sed.raid_tracker_appli.Utils;

import android.util.Log;

import com.application.sed.raid_tracker_appli.GMailSender;

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

//    public static void email(final String getmail1){
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                try {
////                            inputmail1=(EditText) findViewById(R.id.input_mail1);
////                            String getmail1= inputmail1.getText().toString();
//                    Utils.info("mail1",getmail1);
//                    GMailSender sender = new GMailSender("sporteventdevelopment@gmail.com",
//                            "Sp6!b&hsv89%");
//                    sender.sendMail("Hello from JavaMail", "Body from JavaMail",
//                            "sporteventdevelopment@gmail.com", getmail1);
//                    Utils.info("envoi","mail");
//                } catch (Exception e) {
//                    Log.e("SendMail", e.getMessage(), e);
//                }
//            }
//
//        }).start();
//    }
}


/**
 * Liens utiles
 *
 *
 *
 * http://www.tutos-android.com/changement-vues-passage-donnees-android
 **/
