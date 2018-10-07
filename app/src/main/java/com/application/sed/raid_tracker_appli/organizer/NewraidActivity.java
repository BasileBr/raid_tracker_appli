package com.application.sed.raid_tracker_appli.organizer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.application.sed.raid_tracker_appli.Utils;

public class NewraidActivity extends AppCompatActivity {

    private String TAG = "NewRaidActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.info(TAG,"Creation of the new activity");
    }
}
