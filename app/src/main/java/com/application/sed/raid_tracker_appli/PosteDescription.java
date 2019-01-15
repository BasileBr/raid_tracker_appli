package com.application.sed.raid_tracker_appli;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.helper.VolunteerPreferenceActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PosteDescription extends AppCompatActivity {

private static String idRaidReceive="";
private static Context context;
private static String token;
private static String iduser;
private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poste_description);

        //récupération de l'intent
        Intent intent=getIntent();

        //récupération du contexte
        context = this;

        //récupération du token
        token = Bdd.getValue();

        //récupération de l'identifiant de l'utilisateur
        iduser = Bdd.getUserid();

        //récupération de l'id du Raid depuis Landing Activity
        idRaidReceive= intent.getStringExtra("idRaid");

        //récupération de la toolbar depuis le XML
        toolbar = (Toolbar) findViewById(R.id.toolbarDescr);

        //définir la toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // page de retour
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PosteDescription.this, LandingActivity.class);
                startActivity(intent);

            }
        });


        //récupérer la répartition d'un utilisateur sur un raid
        ApiRequestGet.getRepartitionfromIdUserIdRaid(context,token,idRaidReceive,iduser);


    }

    public void onClickGuideMe(View view){
        Intent intent = new Intent(PosteDescription.this, GuideMeActivity.class);
        startActivity(intent);
    }

    public void launcher(View view){
        Uri gmmIntentUri  = Uri.parse("google.streetview:cbll=46.414382, 10.013988");
        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    /*
    ** Traiter les informations de la répartition d'un bénévole en fonction du raid
     */
    public static void recupInfosPoste(String response) {

        JsonParser parser = new JsonParser();
        JsonArray posteinfos = (JsonArray) parser.parse(response);






//        //parcours la liste avec le Json
//        for (int i = 0; i < posteinfos.size(); i++) {
//
//            //JsonParser parser1 = new JsonParser();
//            JsonObject raid = (JsonObject) posteinfos.get(i);
//
//            //récupération de l'id de point d'un poste
//            JsonObject deuxiem = raid.getAsJsonObject("idPoint");
//
//            String test = deuxiem.get("id").toString();
//            //String posteraid = raid.get("nom").toString().replace("\""," ");
//
//
//            String type = raid.get("type").toString().replace("\"", " ");
//            ;

//        }
    }
}
