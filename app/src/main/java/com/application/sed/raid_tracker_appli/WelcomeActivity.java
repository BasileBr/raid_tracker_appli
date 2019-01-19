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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class WelcomeActivity extends AppCompatActivity {

    private String TAG = "WelcomeActivity";
    private static Context context;
    private static LinearLayout layout;
    private static String mois;
    private static Date dateEventsfinal;



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


            //format de la date pour comparer les évenements
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            // récupération de la date du jour
            Date today = Calendar.getInstance().getTime();

            //préparation de la date au format yyyy-MM-dd
            String dateY1=raidVisible.get("date").toString().replace("\"", " ").substring(1,5);
            String dateM1=raidVisible.get("date").toString().replace("\"", " ").substring(6,8);
            String dateD1=raidVisible.get("date").toString().replace("\"", " ").substring(9,11);
            String dateEventstest= dateY1+"-"+dateM1+"-"+dateD1;

            //parser la date string au format date
            try {
                 dateEventsfinal=df.parse(dateEventstest);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //comparaison de la date du jour avec celle de l'évènement (le cas où l'evenement est trop vieux)
            if (today.compareTo(dateEventsfinal)>0){
                Utils.debug("dateEventinf",today.toString()+">"+dateEventsfinal.toString());


            }
            //comparaison de la date du jour avec celle de l'évènement (cas où tout est bon)
            else if (today.compareTo(dateEventsfinal)<0){
                Utils.debug("dateEventsup",today.toString()+"<"+dateEventsfinal.toString());
                String dateY=raidVisible.get("date").toString().replace("\"", " ").substring(1,5);
                String dateM=raidVisible.get("date").toString().replace("\"", " ").substring(6,8);
                String dateD=raidVisible.get("date").toString().replace("\"", " ").substring(9,11);


                if (dateD.contains("0")) {
                    dateD = dateD.replace("0", " ");
                }

                HashMap<String,String> map = new HashMap<>();
                map.put("01","Janvier");
                map.put("02","Février");
                map.put("03","Mars");
                map.put("04","Avril");
                map.put("05","Mai");
                map.put("06","Juin");
                map.put("07","Juillet");
                map.put("08","Aout");
                map.put("09","Septembre");
                map.put("10","Octobre");
                map.put("11","Novembre");
                map.put("12","Décembre");

                for (Map.Entry months : map.entrySet()){
                    if (months.getKey().equals(dateM)){
                        mois = months.getValue().toString();
                    }
                }

                String date=dateD+' '+mois+' '+dateY;
                //String date=dateD+'/'+dateM+'/'+dateY;

                String nomRaid = nom.getAsString();

                //création du visuel
                TextView text = new TextView(context);
                Button btn = new Button(context);

                //ajout d'un id au bouton
                btn.setId(k);



                //ajout du texte
                text.setText(nomRaid + '\n'+"Rejoignez l'aventure"+'\n'+"le "+date);
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
                        intent.putExtra("Classname","Welcome");
                        context.startActivity(intent);
                    }
                });
            }



        }

    }
    /**
     *
     */
    public void join(View view) {
        Intent intent = new Intent(WelcomeActivity.this, Accueil.class);
        intent.putExtra("Classname","Welcome");
        startActivity(intent);
    }


    public void Connexion(View view){
        Intent intent =  new Intent(WelcomeActivity.this, Accueil.class);
        intent.putExtra("Classname", "Welcome");

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
