package com.application.sed.raid_tracker_appli;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import com.application.sed.raid_tracker_appli.organizer.NewraidActivity;

public class Accueil extends AppCompatActivity {
    private String TAG="Accueil";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);


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
        if (user.getText().toString().equals("User") & pass.getText().toString().equals("Password")){
            Utils.debug(TAG, "cool");
            Utils.Name = "toto";
            Intent intent = new Intent(Accueil.this, LandingActivity.class);
            Utils.info(TAG,"connexion, new activity");
            intent.putExtra("name","test");
            startActivity(intent);
        }
        else {
            Utils.info(TAG, "pas cool");
        }

        // fin du test


        Utils.info(TAG,"Login Button action");

    }

    public void createAccount(View view){
        Intent intent =  new Intent(Accueil.this, CreateAccount.class);
        startActivity(intent);
    }

}
