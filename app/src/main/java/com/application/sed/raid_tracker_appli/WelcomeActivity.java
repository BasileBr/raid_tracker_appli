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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.mail.imap.Rights;

import org.osmdroid.util.GeoPoint;
import org.w3c.dom.Text;

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
        ApiRequestGet.getAllRaids(context,"WelcomeActivity");

    }

    /**
     * récupération des raids visibles
     * @param response
     */
    public static void recupRaid(String response){

        //parse la réponse
        JsonParser parser = new JsonParser();
        JsonArray listRaids = (JsonArray) parser.parse(response);

        //parcours la liste de raids
        for (int k = 0; k < listRaids.size() ; k++) {

            //on récupère les infos du raid sous forme d'un JsonObject
            JsonObject raidVisible = (JsonObject) listRaids.get(k);
            //on récupère le nom du raid de cet objet
            JsonElement nom = raidVisible.get("nom");
            //on convertit en String
            String nomRaid = nom.getAsString();

            //création du visuel
            TextView text = new TextView(context);
            Button btn = new Button(context);

            //ajout d'un id au bouton
            btn.setId(k);

            //ajout du texte
            text.setText(nomRaid + '\n'+"Rejoignez l'aventure");
            btn.setText("Rejoignez -NOUS ");

            //choix de la couleur du texte
            text.setTextColor(context.getResources().getColor(R.color.black));
            btn.setTextColor(context.getResources().getColor(R.color.black));

            //btn.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

             //btn.setGravity(Gravity.END);

             btn.setX(200);
            //btn.setHeight(150);
            //btn.setBackgroundColor(80000000);

           // lp.setMargins(left, top, right, bottom);



            // Create LinearLayout
            LinearLayout ll = new LinearLayout(context);
            LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);


            //ll.setOrientation(LinearLayout.HORIZONTAL);


            ll.setBackgroundResource(R.drawable.coureur2);
            ll.getBackground().setAlpha(200);

            //ll.setBackgroundResource(R.color.VertPrimaire);

            //ajout des éléments au linear layout
            ll.addView(text);
            ll.addView(btn);

            ll.setBackgroundResource(R.color.VertPrimaire);

            //ajout des élement au linear layout parent
            layout.addView(ll);

            //on affiche la page de connexion si on appuie sur le rejoins -nous
              btn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(context, Accueil.class);
                  context.startActivity(intent);
              }
          });;
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
