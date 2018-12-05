package com.application.sed.raid_tracker_appli.organizer;

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
import com.application.sed.raid_tracker_appli.API.ApiRequestPost;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EditPosteActivity extends AppCompatActivity {

    public static Context context;
    private Toolbar toolbar;
    public static String idRaid;
    public static String dateDebut;
    public static String dateFin;

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

    public static int boolnom = 0;
    public static int boolnombre = 0;
    public static int booldebut = 0;
    public static int boolfin = 0;
    public static int boolmission = 0;

    public static String idPoint;
    public static String idPoste;

    public static int upMission = 0;
    public static String  idMission;

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
            idPoste = intent.getStringExtra("idPoste");
            Utils.debug("EditPosteActicity",context.toString());
            Utils.debug("EditPosteActicity", String.valueOf(context.toString().contains("com.application.sed.raid_tracker_appli.organizer.EditPosteActivity")));



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


            ApiRequestGet.getMissionsofOnePoste(context,Bdd.getValue(),Integer.valueOf(idPoste));
            ApiRequestGet.getOnePoste(context, Bdd.getValue(), idPoste);
        }
    }


    public static void AfficheInfoPoste(String response){

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = new JsonObject();

        jsonObject = (JsonObject)parser.parse(response);

        JsonObject Point = (JsonObject) jsonObject.get("idPoint");
        idPoint = Point.get("id").toString().replace("\""," ");
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

    }

    public static void updateinfo(View view){


        if (isEmpty(nomposteentry)){
            nomposte.setError("Le champ est vide");
            boolnom = 1;
        }
        else {
            nomposte.setError(null);
            boolnom = 0;
        }

        if (isEmpty(nombreentry)){
            nombre.setError("Le champ est vide");
            boolnombre = 1;
        }
        else {
            nombre.setError(null);
            boolnombre = 0;
        }

        if ( (isEmpty(anneedebut)) || (isEmpty(moisdebut)) || (isEmpty(joursdebut)) || (isEmpty(heuredebut)) || (isEmpty(minutedebut)) ){
            debut.setError("Le champ est vide");
            booldebut = 1;
        }
        else {
            debut.setError(null);
            booldebut = 0;
        }

        if ( (isEmpty(anneefin)) || (isEmpty(moisfin)) || (isEmpty(joursfin)) || (isEmpty(heurefin)) || (isEmpty(minutefin)) ){
            fin.setError("Le champ est vide");
            boolfin = 1;
        }
        else {
            fin.setError(null);
            boolfin = 0;
        }

        if (isEmpty(missionEntry)){
            mission.setError("Le champ est vide");
            boolmission = 1;
        }
        else {
            mission.setError(null);
            boolmission = 0;
        }
        if( booldebut==0 && boolfin==0 && boolmission==0 && boolnom==0 && boolnombre==0){
            dateDebut = joursdebut.getText().toString()+'/'+moisdebut.getText().toString()+'/'+anneedebut.getText().toString()+" "+heuredebut.getText().toString()+":"+minutedebut.getText().toString();
            dateFin = joursfin.getText().toString()+'/'+moisfin.getText().toString()+'/'+anneefin.getText().toString()+" "+heurefin.getText().toString()+":"+minutefin.getText().toString();
            int nb = Integer.valueOf(nombreentry.getText().toString());
            String  nm = nomposteentry.getText().toString();
            Utils.debug("addPoste", "debut "+dateDebut+" fin "+dateFin+ "nm : " +nm + " nb "+ nb);
            ApiRequestPost.postPosteUpdate(context,Bdd.getValue(),idPoint,nm,nb,dateDebut,dateFin,idPoste);
        }

    }


    public static void AddMission(){

        if (upMission == 0) {
            ApiRequestPost.postMission(context, Bdd.getValue(), idPoste, missionEntry.getText().toString());
        }
        else if (upMission == 1){
            ApiRequestPost.postUpdateMission(context, Bdd.getValue(), idPoste, missionEntry.getText().toString(), idMission);
        }
        Intent intent =  new Intent(context, ManageVolunteersPositionActivity.class);


        //Id du parcours qu'on veut récupérer
        intent.putExtra("idPoste",idPoste);
        intent.putExtra("idRaid",idRaid);
        context.startActivity(intent);
    }

    public static boolean isEmpty (EditText text){
        CharSequence str= text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public static void UpdateMission(String response){

        JsonParser parser = new JsonParser();
        JsonArray jsonArray = (JsonArray) parser.parse(response);

        JsonObject jsonObject = (JsonObject) jsonArray.get(0);
        upMission = 1;
        idMission = jsonObject.get("id").toString();
        missionEntry.setText(jsonObject.get("objectif").toString().replace("\"", ""));
    }
}
