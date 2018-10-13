package com.application.sed.raid_tracker_appli;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.application.sed.raid_tracker_appli.organizer.NewraidActivity;


public class ConnexionActivity extends AppCompatActivity {
    private String TAG = "ConnexionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        Utils.warm(TAG, "OnCreate");


    }

    public void login_connexion(View view){
        final EditText user = findViewById(R.id.username);
        final EditText pass = findViewById(R.id.password);

        Utils.info(TAG,"Login Button action");
        // Test du bouton avec le user toto et password test
        if (user.getText().toString().equals("User") & pass.getText().toString().equals("Password")){
            Utils.debug(TAG, "cool");
            Intent intent = new Intent(ConnexionActivity.this, NewraidActivity.class);
            Utils.info(TAG,"connexion, new activity");
            startActivity(intent);
        }
        else {
            Utils.info(TAG, "pas cool");
        }

        // fin du test




    }
    public void cancel(View view){
        Intent intent = new Intent(ConnexionActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }
}
