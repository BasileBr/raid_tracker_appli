package com.application.sed.raid_tracker_appli;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import com.application.sed.raid_tracker_appli.organizer.NewraidActivity;


public class ConnexionActivity extends AppCompatActivity {
    private String TAG = "ConnexionActivity";

    private DrawerLayout drawerLayout;

    private Toolbar toolbar2;
    View button;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        Utils.warm(TAG, "OnCreate");

        this.toolbar2 = findViewById(R.id.toolbar2);

        //definir notre toolbar en tant qu'actionBar
        setSupportActionBar(toolbar2);

        //afficher le bouton retour
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        this.drawerLayout = findViewById(R.id.drawerLayout2);
        this.drawerToggle = new ActionBarDrawerToggle(this,this.drawerLayout,0,0);
        this.drawerLayout.setDrawerListener(this.drawerToggle);

        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.info("NICK", "button button button..................");

                drawerLayout.openDrawer(Gravity.START);

            }
        });

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // synchroniser le drawerToggle après la restauration via onRestoreInstanceState
        drawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }



    public void login_connexion(View view){
        final EditText user = findViewById(R.id.addrmail);
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
