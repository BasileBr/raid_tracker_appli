package com.application.sed.raid_tracker_appli;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    //temps de splash screen
    private static int SPLASH_TIME_OUT= 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(HomeActivity.this, Accueil.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
}
