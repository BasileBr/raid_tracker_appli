package com.application.sed.raid_tracker_appli;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.application.sed.raid_tracker_appli.API.ApiRequestDelete;
import com.application.sed.raid_tracker_appli.API.ApiRequestGet;

public class HomeActivity extends AppCompatActivity {

    //temps de splash screen
    private static int SPLASH_TIME_OUT= 2000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(HomeActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);

        //ApiRequestGet.getraid(this);


    }

}
