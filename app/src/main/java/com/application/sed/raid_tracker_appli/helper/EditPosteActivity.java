package com.application.sed.raid_tracker_appli.helper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    public static String idRaid;

    public static TextView nomposte;
    public static EditText nomposteentry;
    public static TextView nombre;
    public static EditText nombreentry;

    public static TextView debut;
    public static EditText anneedebut;
    public static EditText moisdebut;
    public static EditText joursdebut;
    public static EditText heuredebut;
    public static EditText minutedebut;

    public static TextView fin;
    public static EditText anneefin;
    public static EditText moisfin;
    public static EditText joursfin;
    public static EditText heurefin;
    public static EditText minutefin;

    public static Button valider;
    public static Button annuler;

    public static TextView mission;
    public static EditText missionEntry;

    public static int boolnom;
    public static int boolnombre;
    public static int booldebut;
    public static int boolfin;
    public static int boolmission;


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


            nomposte = findViewById(R.id.nomposte);
            nomposteentry = findViewById(R.id.nomposteentry);
            nombre = findViewById(R.id.nombre);
            nombreentry = findViewById(R.id.nombreentry);

            debut = findViewById(R.id.debut);
            anneedebut = findViewById(R.id.anneedebut);
            moisdebut = findViewById(R.id.moisdebut);
            joursdebut = findViewById(R.id.joursdebut);
            heuredebut = findViewById(R.id.heuredebut);
            minutedebut = findViewById(R.id.minutedebut);

            fin = findViewById(R.id.fin);
            anneefin = findViewById(R.id.anneefin);
            moisfin = findViewById(R.id.moisfin);
            joursfin = findViewById(R.id.joursfin);
            heurefin = findViewById(R.id.heurefin);
            minutefin = findViewById(R.id.minutefin);

            valider = findViewById(R.id.valider);
            annuler = findViewById(R.id.annuler);

            mission = findViewById(R.id.mission);
            missionEntry = findViewById(R.id.missionentry);

            annuler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ManageVolunteersPositionActivity.class);
                    intent.putExtra("idRaid",idRaid);
                    context.startActivity(intent);
                }
            });


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

        anneedebut.setText(jsonObject.get("heureDebut").toString().substring(1,5));
        moisdebut.setText(jsonObject.get("heureDebut").toString().substring(6,8));
        joursdebut.setText(jsonObject.get("heureDebut").toString().substring(9,11));
        heuredebut.setText(jsonObject.get("heureDebut").toString().substring(12,14));
        minutedebut.setText(jsonObject.get("heureDebut").toString().substring(15,17));

        anneefin.setText(jsonObject.get("heureFin").toString().substring(1,5));
        moisfin.setText(jsonObject.get("heureFin").toString().substring(6,8));
        joursfin.setText(jsonObject.get("heureFin").toString().substring(9,11));
        heurefin.setText(jsonObject.get("heureFin").toString().substring(12,14));
        minutefin.setText(jsonObject.get("heureFin").toString().substring(15,17));


        Utils.debug("heuredebut",jsonObject.get("heureDebut").toString());
        Utils.debug("heurefin",jsonObject.get("heureFin").toString());

        if (isEmpty(nomposteentry)){
            nomposte.setError("Le champ est vite");
        }
        if (isEmpty(nombreentry)){
            nombre.setError("Le champ est vite");
        }
        if ( (isEmpty(anneedebut)) || (isEmpty(moisdebut)) || (isEmpty(joursdebut)) || (isEmpty(heuredebut)) || (isEmpty(minutedebut)) ){
            debut.setError("Le champ est vite");
        }
        if ( (isEmpty(anneefin)) || (isEmpty(moisfin)) || (isEmpty(joursfin)) || (isEmpty(heurefin)) || (isEmpty(minutefin)) ){
            fin.setError("Le champ est vite");
        }

        if (isEmpty(missionEntry)){
            mission.setError("Le champ est vite");
        }


    }

    public static void updateinfo(){



        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static boolean isEmpty (EditText text){
        CharSequence str= text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}
