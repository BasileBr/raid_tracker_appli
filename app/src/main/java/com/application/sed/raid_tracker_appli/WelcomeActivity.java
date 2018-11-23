package com.application.sed.raid_tracker_appli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.application.sed.raid_tracker_appli.Utils.Utils;


public class WelcomeActivity extends AppCompatActivity {

    private String TAG = "WelcomeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Utils.info(TAG, "OnCreate");

    }


    /**
     *
     */
    public void join(View view) {


        Intent intent = new Intent(WelcomeActivity.this, Accueil.class);
        startActivity(intent);
    }


    public void Connexion(View view){
        Intent intent =  new Intent(WelcomeActivity.this, Accueil.class);
        startActivity(intent);
    }

    public void createAccount(View view){
        Intent intent =  new Intent(WelcomeActivity.this, CreateAccount.class);
        intent.putExtra("Classname","Welcome");
        startActivity(intent);
    }

    public void asupprimer(View view){
        Intent intent =  new Intent(WelcomeActivity.this, ManageParcoursActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        Utils.debug("WelcomeActivity","J'ai presse le back");
    }
}
