package com.application.sed.raid_tracker_appli.helper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.ManageVolunteersPositionActivity;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.application.sed.raid_tracker_appli.organizer.CreateParcours;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EditPosteActivity extends AppCompatActivity {

    public static Context context;
    private Toolbar toolbar;
    public String idRaid;

    public static EditText nomposteentry;
    public static EditText nombreentry;

    public static EditText anneedebut;
    public static EditText moisdebut;
    public static EditText joursdebut;
    public static EditText heuredebut;
    public static EditText minutedebut;

    public static EditText anneefin;
    public static EditText moisfin;
    public static EditText joursfin;
    public static EditText heurefin;
    public static EditText minutefin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_poste);

        Intent intent = getIntent();

        if (intent != null){

            toolbar = (Toolbar) findViewById(R.id.toolbar);

            idRaid = intent.getStringExtra("idRaid");
            // on définit la toolbar
            setSupportActionBar(toolbar);

            //ajouter un bouton retour dans l'action bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            //si on appuie sur le bouton retour, on arrive sur la page landing
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditPosteActivity.this, ManageVolunteersPositionActivity.class);
                    intent.putExtra("idRaid",idRaid);
                    startActivity(intent);
                }
            });

            //récuperation du context
            context = this;
            String idPoste = intent.getStringExtra("idPoste");
            Utils.debug("EditPosteActicity",context.toString());
            Utils.debug("EditPosteActicity", String.valueOf(context.toString().contains("com.application.sed.raid_tracker_appli.helper.EditPosteActivity")));


            nomposteentry = findViewById(R.id.nomposteentry);
            nombreentry = findViewById(R.id.nombreentry);

            anneedebut = findViewById(R.id.anneedebut);
            moisdebut = findViewById(R.id.moisdebut);
            joursdebut = findViewById(R.id.joursdebut);
            heuredebut = findViewById(R.id.heuredebut);
            minutedebut = findViewById(R.id.minutedebut);

            anneefin = findViewById(R.id.anneefin);
            moisfin = findViewById(R.id.moisfin);
            joursfin = findViewById(R.id.joursfin);
            heurefin = findViewById(R.id.heurefin);
            minutefin = findViewById(R.id.minutefin);
            ApiRequestGet.getOnePoste(context, Bdd.getValue(), idPoste);
        }
    }


    public static void AfficheInfoPoste(String response){

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = new JsonObject();

        jsonObject = (JsonObject)parser.parse(response);

        Utils.debug("AfficheInfoPoste",jsonObject.toString());
        nomposteentry.setText(jsonObject.get("type").toString().replace("\"",""));
        nombreentry.setText(jsonObject.get("nombre").toString());


    }
}
