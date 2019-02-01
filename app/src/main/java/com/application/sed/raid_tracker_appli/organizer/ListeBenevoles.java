package com.application.sed.raid_tracker_appli.organizer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ListeBenevoles extends AppCompatActivity {

    public static Context context;
    public static String TAG;

    public static String idRaid;
    public static LinearLayout ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_benevoles);
        TAG = "ListeBenevoles";

        Intent intent=getIntent();
        context = this;
        if (intent != null) {
            idRaid = intent.getStringExtra("idRaid");
        }


        // Partie sur la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ListeBenevoles.this, CourseActivity.class);
                intent.putExtra("idRaid",idRaid);
                startActivity(intent);

            }
        });
        // Fin de la partie sur la Toolbar

        ll = findViewById(R.id.listeraidbenevole);
        ApiRequestGet.getBenevolesOfOneRaid(context,Bdd.getValue(),idRaid);
    }

    public static void AfficheListe(String response){


        JsonParser parser = new JsonParser();
        JsonArray parcourslist = (JsonArray) parser.parse(response);

        for (int i=0; i<parcourslist.size(); i++){

            JsonObject benevole = (JsonObject) parcourslist.get(i);
            JsonObject utilisateur = (JsonObject) benevole.get("idUser");
            //Utils.debug(TAG, parcourslist.get(i).toString());
            //Utils.debug(TAG, benevole.get("id").toString());
            Utils.debug(TAG,utilisateur.get("usernamex").toString());
        }


    }

    /**
     * Méthode pour override la méthode retour du bouton Android du smartphone
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ListeBenevoles.this, CourseActivity.class);
        intent.putExtra("idRaid",idRaid);
        startActivity(intent);
    }
}
