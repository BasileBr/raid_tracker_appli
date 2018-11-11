package com.application.sed.raid_tracker_appli;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class Accueil extends AppCompatActivity {
    private static Context context;
    private String TAG="Accueil";


    // Liste pour récupérer tous les comptes

    EditText mEdit;
    EditText mEdit1;
    String recupere;

    //Liste des infos de chaque compte
    private ArrayList<String> listUsers;
    private ArrayList<List> AccountInfo = new ArrayList<>();

    private String userid;
    Toolbar toolbar;
    //private ArrayList<Button> listButton;



    public static Context getAppContext() {
        return Accueil.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        Accueil.context = getApplicationContext();
        // Récupération des informations de la liste


        toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Accueil.this, WelcomeActivity.class);
                startActivity(intent);

            }
        });

    }

    public void login(View view){

        /*
         * TODO login
         * Bouton du login -> direction vers page HOME si ok (HomeActivity)
         * Sinon, message d'erreur
         */
//        final EditText user = findViewById(R.id.addrmail);
//        final EditText pass = findViewById(R.id.passwordconnexion);


        mEdit = (EditText) findViewById(R.id.addrmail);
        mEdit1 = (EditText) findViewById(R.id.passwordconnexion);

        String email = mEdit.getText().toString();
        String mdp=mEdit1.getText().toString();


        boolean isValid;

        AccountInfo = Bdd.getAccount();

        for (int i = 0; i < AccountInfo.size(); i++) {
            // Liste pour un user
            List infoUsers  = AccountInfo.get(i);
            String info = infoUsers.toString();
            Utils.debug("Je suis ici", info);
            if (infoUsers.get(1).equals(email)& infoUsers.get(2).equals(mdp)){
                isValid =true;
                Intent intent = new Intent(Accueil.this, LandingActivity.class);
                intent.putExtra("name",infoUsers.get(0).toString());
                Bdd.setNomUtilisateur(infoUsers.get(0).toString());
                startActivity(intent);
            }
             else {
                Utils.info("oh oh","je pas");
                mEdit.setError("adresse mail ou mot de passe invalide");
            }

            isValid=false;


        }
        //ApiRequestPost.postToken(this, email, mdp);


//        /*vérification lors de la connexon */
//        for (int j = 0; j < listUsers.size(); j ++){
//
//            String test= listUsers.get(j);
//
//            Utils.info("Identifiant  eet je sais pas quoi",test);
//
//
//        }


        Utils.info(TAG,"Login Button action");

    }

    public void createAccount(View view){
        Intent intent =  new Intent(Accueil.this, CreateAccount.class);
        intent.putExtra("Classname","Accueil");
        startActivity(intent);
    }

    public static void redirection(String response){
        String toto = response;


        JsonParser parser = new JsonParser();
        JsonObject token = (JsonObject) parser.parse(toto);

        Utils.debug("Accueil", token.toString());

        String id = token.get("id").toString();
        String value = token.get("value").toString();

        JsonObject user = token.get("user").getAsJsonObject();
        String userid = user.get("id").toString();
        Bdd.setUserid(userid);
        Utils.debug("Accueil", "id : " + id + " Value " +value);

        Bdd.setValue(value,id);

        String utilisateur = user.get("username").toString();
        Utils.debug("Accueil",utilisateur);
        Intent intent = new Intent(Accueil.getAppContext(), LandingActivity.class);
        intent.putExtra("name",utilisateur);
        Bdd.setNomUtilisateur(utilisateur);
        LandingActivity.getAppContext().startActivity(intent);

        //ApiRequestGet.getSpecificUsers(Accueil.getAppContext(), value, userid);


    }

    public static void change(String utilisateur){
        Intent intent = new Intent(Accueil.getAppContext(), LandingActivity.class);
        intent.putExtra("name",utilisateur);
        Bdd.setNomUtilisateur(utilisateur);
        Accueil.getAppContext().startActivity(intent);
        Utils.info("Accueil","Login Button action");
    }



}
