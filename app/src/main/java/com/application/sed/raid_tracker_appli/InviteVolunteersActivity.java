package com.application.sed.raid_tracker_appli;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import android.widget.AdapterView;


import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.application.sed.raid_tracker_appli.organizer.CourseActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class InviteVolunteersActivity extends AppCompatActivity implements OnItemSelectedListener {
    private static Context context;
    private Toolbar toolbar;
    Button send;
    EditText inputmail1;
    EditText inputmail2;
    EditText inputmail3;
    TextView text;
    private int i;
    private String iduser;
    private String token;
    private String getselectedraid;
    private static Spinner spinner;
    private static List<String> nomRaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_volunteers);

        Intent intent=getIntent();
        context = this;

        if (intent != null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(InviteVolunteersActivity.this, LandingActivity.class);
                    startActivity(intent);

                }
            });
        }


        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);


        iduser = Bdd.getUserid();
        token = Bdd.getValue();


//


        ApiRequestGet.getSpecificRaid(context, token, iduser, "InviteActivity");
    }

    //méthode pour récupérer l'élement selectionné dans la liste des raids
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Object item = parent.getItemAtPosition(position);
        getselectedraid=item.toString();
    }
    //si aucun élément n'est selectionné, là par defaut premier raid de la liste
    public void onNothingSelected(AdapterView<?> arg0) {
    }


    public void sendMail(View view) {
        send = (Button) findViewById(R.id.sendEmail);

        inputmail1 = (EditText) findViewById(R.id.input_mail1);
        final String getmail1 = inputmail1.getText().toString();

        inputmail2 = (EditText) findViewById(R.id.input_mail2);
        final String getmail2 = inputmail2.getText().toString();

        inputmail3 = (EditText) findViewById(R.id.input_mail3);
        final String getmail3 = inputmail3.getText().toString();

        text = (TextView) findViewById(R.id.text);

        if ((isEmpty(inputmail1) && isEmpty(inputmail2)) && isEmpty(inputmail3)) {
            text.setError("Aucune adresse mail entrée ");
        }


        final ArrayList<String> mailListe = new ArrayList<String>();

        if (getmail1.isEmpty()){
            mailListe.add(getmail1);
        }
        if (!getmail2.isEmpty()){
            mailListe.add(getmail2);
        }
        if (!getmail3.isEmpty()){
            mailListe.add(getmail3);
        }



        new Thread(new Runnable() {

            @Override
            public void run() {

                for (i = 0; i < mailListe.size(); i++) {
                    try {
//                            inputmail1=(EditText) findViewById(R.id.input_mail1);
//                            String getmail1= inputmail1.getText().toString();
                        Utils.info("mail1", getmail1);
                        GMailSender sender = new GMailSender("sporteventdevelopment@gmail.com",
                                "Sp6!b&hsv89%");
                        sender.sendMail("Rejoins le raid"+getselectedraid, "Bonjour,nous recherchons des bénévoles pour le raid"+getselectedraid +"si toi aussi tu aimes aider et assurer la sécurité, rejoins nous !! ",
                                "sporteventdevelopment@gmail.com", mailListe.get(i));

                        Utils.info("envoi", "mail");
                    } catch (Exception e) {
                        Log.e("SendMail", e.getMessage(), e);
                    }
                }
            }

        }).start();
        Intent intent = new Intent(InviteVolunteersActivity.this, LandingActivity.class);
        startActivity(intent);
    }



        //});
   // }

    public static void createSpinner(List<String> raidliste){
        // Spinner Drop down elements
        //List<String> raidliste = new ArrayList<String>();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, raidliste);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    //récupérer la liste des raids
    public static void raidlist(String response){

        JsonParser parser = new JsonParser();
        JsonArray raidlist = (JsonArray) parser.parse(response);
        Utils.debug("raidlist + size", "size : " + raidlist.size() + " raidlist : "+raidlist.toString());
        nomRaid = new ArrayList<>();

        for (int i = 0; i < raidlist.size(); i ++) {

            JsonParser parser1 = new JsonParser();
            JsonObject raid = (JsonObject) raidlist.get(i);
            String nomraid = raid.get("nom").toString().replace("\""," ");
            nomRaid.add(nomraid);

        }

        createSpinner(nomRaid);

    }

    // vérifier si l'adresse mail entrée correspond à une adresse correcte
    boolean isEmail(EditText text){
        CharSequence email=text.getText().toString();
        return (!TextUtils.isEmpty(email)&& Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    //vérifier qu'un edit text n'est pas vide
    boolean isEmpty (EditText text){
        CharSequence str= text.getText().toString();
        return TextUtils.isEmpty(str);
    }


    public void cancelSend (View view){
        Intent intent = new Intent(InviteVolunteersActivity.this, LandingActivity.class);
        startActivity(intent);
    }

}

