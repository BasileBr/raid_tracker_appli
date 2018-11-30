package com.application.sed.raid_tracker_appli;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;


public class WelcomeActivity extends AppCompatActivity {

    private String TAG = "WelcomeActivity";
    private static Context context;

    private static LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Utils.info(TAG, "OnCreate");

        context = this;


        // Here, thisActivity is the current activity
        Utils.Permission(this);



        layout = (LinearLayout)findViewById(R.id.ListeRaid);


        ApiRequestGet.getAllRaids(context);
//
//        for (int i=0; i<4; i++) {
//            Button btn = new Button(this);
//            btn.setId(i);
//            btn.setText("some_text");
//            layout.addView(btn);
//        }

    }


    public static void recupRaid(String response){

        JsonParser parser = new JsonParser();
        JsonArray listRaids = (JsonArray) parser.parse(response);

        for (int k = 0; k < listRaids.size() ; k++) {

            JsonObject raidVisible = (JsonObject) listRaids.get(k);
            JsonElement nom = raidVisible.get("nom");

            String nomRaid = nom.getAsString();

            Button btn = new Button(context);
            btn.setId(k);
            btn.setText(nomRaid);

          btn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  Intent intent = new Intent(context, Accueil.class);
                  context.startActivity(intent);
              }
          });

            layout.addView(btn);

        }

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
