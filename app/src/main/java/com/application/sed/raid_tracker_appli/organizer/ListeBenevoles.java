package com.application.sed.raid_tracker_appli.organizer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.LineNumberReader;
import java.util.ArrayList;

public class ListeBenevoles extends AppCompatActivity {

    public static Context context;
    public static String TAG;

    public static String idRaid;
    public static LinearLayout ll;
    public static ArrayList<String> listeNom;


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
        listeNom = new ArrayList<>();
        TextView entete = new TextView(context);
        entete.setText("Liste des bénévoles inscrits sur le raid");
        entete.setTextSize(20);
        entete.setPadding(0,10,0,30);
        ll.addView(entete);
        ApiRequestGet.getBenevolesOfOneRaid(context,Bdd.getValue(),idRaid);
    }

    public static void AfficheListe(String response){




        JsonParser parser = new JsonParser();
        JsonArray parcourslist = (JsonArray) parser.parse(response);

        for (int i=0; i<parcourslist.size(); i++){

            JsonObject benevole = (JsonObject) parcourslist.get(i);
            JsonObject utilisateur = (JsonObject) benevole.get("idUser");
            //Utils.debug(TAG, parcourslist.get(i).toString());
            Utils.debug(TAG, benevole.get("id").toString());
            Utils.debug(TAG,utilisateur.get( "username").toString());

            String nom = utilisateur.get( "username").toString().replace("\"", "");


            listeNom.add(nom);

        }

        // id raid prefpost : 110
        // id user q@q : 133
        // id benevole : 109
        // id poste : 66
        // id repartition : 9
        ApiRequestGet.getCheckinOneRaid(context,Bdd.getValue(),idRaid);

    }


    public static void afficheCheck(String response){

        if (response.isEmpty()){
            for (int i = 0; i < listeNom.size(); i++) {


                TextView textView = new TextView(context);
                textView.setTextSize(20);
                textView.setPadding(20, 20, 10, 0);
                textView.setText(listeNom.get(i));

                ll.addView(textView);
            }

        }
        else {
            Utils.debug(TAG, response);
            JsonParser parser = new JsonParser();
            JsonArray parcourslist = (JsonArray) parser.parse(response);
            JsonObject inter = (JsonObject) parcourslist.get(0);
            JsonObject repartition = (JsonObject) inter.get("idRepartition");
            JsonObject benevole = (JsonObject) repartition.get("idBenevole");
            JsonObject iduser = (JsonObject) benevole.get("idUser");
            String user = iduser.get("username").toString().replace("\"", "");


            for (int i = 0; i < listeNom.size(); i++) {

                LinearLayout linearLayout = new LinearLayout(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayout.setLayoutParams(layoutParams);

                TextView textView = new TextView(context);
                textView.setTextSize(20);
                textView.setPadding(20, 20, 10, 0);
                textView.setText(listeNom.get(i));

                linearLayout.addView(textView);
                TextView check = new TextView(context);


                check.setTextSize(20);
                check.setPadding(300, 20, 20, 0);
                check.setGravity(Gravity.END);

                if (user.equals(listeNom.get(i))) {

                    check.setText("Est a son poste");
                    check.setTextColor(context.getResources().getColor(R.color.VertPrimaire));
                } else {
                    check.setText("N'est pas a son poste");
                    check.setTextColor(context.getResources().getColor(R.color.MarronPrimaire));
                }
                linearLayout.addView(check);
                //textView.setGravity(Gravity.END);
                ll.addView(linearLayout);
            }
            Utils.debug(TAG, user);
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
