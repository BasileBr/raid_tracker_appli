package com.application.sed.raid_tracker_appli;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.application.sed.raid_tracker_appli.organizer.NewraidActivity;

public class CreateAccount extends AppCompatActivity{
    private String TAG="CreateAccount";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creationaccount);
        Utils.info(TAG, "OnCreate");
    }

    public void login(View view){

        /*
         * TODO login
         * Bouton du login -> direction vers page HOME si ok (HomeActivity)
         * Sinon, message d'erreur
         */
        final EditText user = findViewById(R.id.username);
        final EditText pass = findViewById(R.id.password);

        // Test du bouton avec le user toto et password test
        if (user.getText().toString().equals("Username") & pass.getText().toString().equals("Password")){
            Utils.debug(TAG, "cool");
            Intent intent = new Intent(CreateAccount.this, NewraidActivity.class);
            Utils.info(TAG,"connexion, new activity");
            startActivity(intent);
        }
        else {
            Utils.info(TAG, "pas cool");
        }

        // fin du test


        Utils.info(TAG,"Login Button action");

    }

    public void cancel(View view){
        Intent intent = new Intent(CreateAccount.this, WelcomeActivity.class);
        startActivity(intent);
    }
}

