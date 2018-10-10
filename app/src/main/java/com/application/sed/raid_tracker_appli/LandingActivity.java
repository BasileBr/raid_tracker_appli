package com.application.sed.raid_tracker_appli;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.application.sed.raid_tracker_appli.organizer.NewraidActivity;


public class LandingActivity extends AppCompatActivity {

    private String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Utils.info(TAG, "OnCreate");

        final TextView user = findViewById(R.id.username);
        user.setText("Bienvenue " +Utils.Name);
    }

    public void join(View view) {

    }

}
