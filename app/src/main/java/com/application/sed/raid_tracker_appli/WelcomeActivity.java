package com.application.sed.raid_tracker_appli;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.application.sed.raid_tracker_appli.organizer.ManageParcoursActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


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
        Utils.Permissionlocation(this);
        Utils.Permissioncoarselocation(this);
        Utils.Permissionexternalstorage(this);
        Utils.Permissionnetworkstate(this);
        Utils.Permissionreadexternalstorage(this);

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
            String date = raidVisible.get("date").toString().replace("\"", " ").substring(0, 11);

            String nomRaid = nom.getAsString();

            //création du visuel
            TextView text = new TextView(context);
            Button btn = new Button(context);

            //ajout d'un id au bouton
            btn.setId(k);



            //ajout du texte
            text.setText(nomRaid + '\n'+"Rejoignez l'aventure"+'\n'+date);
            text.setX(20);
            btn.setText("Rejoins-nous ! ");

            //choix de la couleur du texte
            text.setTextColor(context.getResources().getColor(R.color.black));

            //btn.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

             //btn.setGravity(Gravity.END);

             btn.setX(200);
             btn.setBackgroundResource(R.color.black);

             btn.setTextColor(context.getResources().getColor(R.color.Blancnacre));
            btn.getBackground().setAlpha(100);
            //btn.setHeight(150);
            //btn.setBackgroundColor(80000000);

           // lp.setMargins(left, top, right, bottom);



            // Create LinearLayout
            LinearLayout ll = new LinearLayout(context);



            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 150);

            ll.addView(text);
            ll.addView(btn,layoutParams);


            ll.setBackgroundResource(R.drawable.coureur2);
            ll.getBackground().setAlpha(150);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)layout.getLayoutParams();
            params.setMargins(30, 0, 35, 0); //substitute parameters for left, top, right, bottom
            layout.setLayoutParams(params);

            layout.addView(ll);
            //layout.getLayoutParams();

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
