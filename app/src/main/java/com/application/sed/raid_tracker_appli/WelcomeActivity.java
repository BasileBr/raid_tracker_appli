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

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.mail.imap.Rights;

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


        ApiRequestGet.getAllRaids(context,"WelcomeActivity");
//
//        for (int i=0; i<4; i++) {
//            Button btn = new Button(this);
//            btn.setId(i);
//            btn.setText("some_text");
//            layout.addView(btn);
//        }

    }


    public static void recupRaid(String response){


        //LinearLayout.LayoutParams test= new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

        JsonParser parser = new JsonParser();
        JsonArray listRaids = (JsonArray) parser.parse(response);

        for (int k = 0; k < listRaids.size() ; k++) {

            JsonObject raidVisible = (JsonObject) listRaids.get(k);
            JsonElement nom = raidVisible.get("nom");

            String nomRaid = nom.getAsString();

            Button btn = new Button(context);
            Button btn2 = new Button(context);
            btn.setId(k);
            btn.setText(nomRaid + '\n'+"Rejoignez l'aventure");
            btn2.setText("Rejoignez -NOUS ");
            btn.setTextColor(context.getResources().getColor(R.color.black));
            btn2.setTextColor(context.getResources().getColor(R.color.black));

             btn.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

             btn2.setGravity(Gravity.RIGHT);
            //btn.setHeight(150);
            //btn.setBackgroundColor(80000000);

           // lp.setMargins(left, top, right, bottom);



            // Create LinearLayout
            LinearLayout ll = new LinearLayout(context);
            LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            //ll.setOrientation(LinearLayout.HORIZONTAL);


            ll.setBackgroundResource(R.drawable.coureur2);
            //ll.setBackgroundResource(R.color.VertPrimaire);

            ll.addView(btn);
            ll.addView(btn2);


            layout.addView(ll);



//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(left, top, right, bottom);
//            imageView.setLayoutParams(lp);

          btn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  Intent intent = new Intent(context, Accueil.class);
                  context.startActivity(intent);
              }
          });

            //layout.addView(btn);
           // layout.addView(btn2);
            //layout.setPadding(20,20,20,20);
            //btn.setBackgroundResource(R.drawable.coureur2);

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
