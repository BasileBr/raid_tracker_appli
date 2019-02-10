package com.application.sed.raid_tracker_appli.organizer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class InviteVolunteersActivity extends AppCompatActivity implements OnItemSelectedListener,View.OnFocusChangeListener {
    private String TAG = "InviteVolunteersActivity";
    private static Context context;
    Button send;
    EditText inputmail1;
    EditText inputmail2;
    EditText inputmail3;
    EditText inputcontenu;
    TextView text;
    TextView body;
    private int i;
    private String getselectedraid;
    private static Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.info(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_volunteers);

        Intent intent=getIntent();
        context = this;

        if (intent != null) {
            Toolbar toolbar = findViewById(R.id.toolbar);
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
        spinner = findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        String iduser = Bdd.getUserid();
        String token = Bdd.getValue();

        ApiRequestGet.getSpecificRaid(context, token, iduser, "InviteActivity");
    }

    //méthode pour récupérer l'élement selectionné dans la liste des raids
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object item = parent.getItemAtPosition(position);
        getselectedraid=item.toString();
    }

    /**
     * si aucun élément n'est selectionné, là par defaut premier raid de la liste
     * @param arg0
     */
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    public void sendMail(View view) {
        send = findViewById(R.id.sendEmail);

        inputmail1 = findViewById(R.id.input_mail1);
        inputmail1.setOnFocusChangeListener(this);
        final String getmail1 = inputmail1.getText().toString();

        inputmail2 = findViewById(R.id.input_mail2);
        inputmail2.setOnFocusChangeListener(this);
        final String getmail2 = inputmail2.getText().toString();

        inputmail3 = findViewById(R.id.input_mail3);
        inputmail3.setOnFocusChangeListener(this);
        final String getmail3 = inputmail3.getText().toString();


        inputcontenu=findViewById(R.id.input_body);
        inputcontenu.setOnFocusChangeListener(this);
        final String getcontenu = inputcontenu.getText().toString();


        text = findViewById(R.id.text);
        body = findViewById(R.id.body);

        if ((isEmpty(inputmail1) && isEmpty(inputmail2)) && isEmpty(inputmail3)) {
            text.setError("Aucune adresse mail entrée ");
        }

        final ArrayList<String> mailListe = new ArrayList<String>();
        if (!getmail1.isEmpty()){
            mailListe.add(getmail1);
        }
        if (!getmail2.isEmpty()){
            mailListe.add(getmail2);
        }
        if (!getmail3.isEmpty()){
            mailListe.add(getmail3);
        }

        if (isEmpty(inputcontenu)){
            body.setError("Vous n'avez pas saisi le contenu du mail");
        }



        if ((!(getmail1.isEmpty())) || (!(getmail2.isEmpty())) || (!(getmail3.isEmpty())) || (!(getcontenu.isEmpty())) ) {
            new Thread(new Runnable() {

                @Override
                public void run() {

                    for (i = 0; i < mailListe.size(); i++) {
                        try {
                            GMailSender sender = new GMailSender("sporteventdevelopment@gmail.com",
                                    "Sp6!b&hsv89%");
                            sender.sendMail("Rejoins le raid" + getselectedraid, getcontenu,
                                    "sporteventdevelopment@gmail.com", mailListe.get(i));
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }
                    }
                }

            }).start();
            Toast.makeText(context, "Votre mail est envoyé", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(InviteVolunteersActivity.this, LandingActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasfocus) {
        switch(view.getId()){
            case R.id.input_mail1:
                text.setError(null);
                break;

            case R.id.input_mail2:
                text.setError(null);
                break;
            case R.id.input_mail3:
                text.setError(null);
                break;
            case R.id.input_body:
                body.setError(null);
        }
    }

    /**
     *
     * @param raidliste
     */
    public static void createSpinner(List<String> raidliste){

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, raidliste);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    /**
     * récupérer la liste des raids
     * @param response
     */
    public static void raidlist(String response){

        JsonParser parser = new JsonParser();
        JsonArray raidlist = (JsonArray) parser.parse(response);
        List<String> nomRaid = new ArrayList<>();

        for (int i = 0; i < raidlist.size(); i ++) {
            JsonObject raid = (JsonObject) raidlist.get(i);
            String nomraid = raid.get("nom").toString().replace("\""," ");
            nomRaid.add(nomraid);
        }
        createSpinner(nomRaid);
    }

    /**
     * vérifier si l'adresse mail entrée correspond à une adresse correcte
     * @param text
     * @return
     */
    boolean isEmail(EditText text){
        CharSequence email=text.getText().toString();
        return (!TextUtils.isEmpty(email)&& Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    /**
     * vérifier qu'un edit text n'est pas vide
     * @param text
     * @return
     */
    boolean isEmpty (EditText text){
        CharSequence str= text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    /**
     *
     * @param view
     */
    public void cancelSend (View view){
        Intent intent = new Intent(InviteVolunteersActivity.this, LandingActivity.class);
        startActivity(intent);
    }
}

