package com.application.sed.raid_tracker_appli;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class InscriptionActivity extends AppCompatActivity {
    private String TAG = "InscriptionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        Utils.warm(TAG, "OnCreate");


    }
}
