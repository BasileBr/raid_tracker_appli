package com.application.sed.raid_tracker_appli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.application.sed.raid_tracker_appli.organizer.NewraidActivity;


public class WelcomeActivity extends AppCompatActivity {

    private String TAG = "WelcomeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Utils.info(TAG, "OnCreate");

    }

    /**
     * Mettre la méthode en public pour que le bouton y ait accés
     * @param view view
     *
     */
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
            Utils.Name = "toto";
            Intent intent = new Intent(WelcomeActivity.this, LandingActivity.class);
            Utils.info(TAG,"connexion, new activity");
            startActivity(intent);
        }
        else {
            Utils.info(TAG, "pas cool");
        }

        // fin du test


        Utils.info(TAG,"Login Button action");

    }

    /**
     *
     */
    public void join(View view) {


        /*
         * TODO subscription
         */

        Intent intent = new Intent(WelcomeActivity.this, ConnexionActivity.class);
        startActivity(intent);
    }


    public void createAccount(View view){
        Intent intent =  new Intent(WelcomeActivity.this, CreateAccount.class);
        startActivity(intent);
    }
}
