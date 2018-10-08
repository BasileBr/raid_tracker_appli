package com.application.sed.raid_tracker_appli.organizer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils;

public class NewraidActivity extends AppCompatActivity {

    private String TAG = "NewRaidActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_newraid);
        Utils.info(TAG,"Creation of the new activity");
    }
}
