package com.application.sed.raid_tracker_appli;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.application.sed.raid_tracker_appli.API.*;
import com.application.sed.raid_tracker_appli.organizer.CreateParcours;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class ConnexionActivity extends AppCompatActivity {
    private static Context context;
    private String TAG="ConnexionActivity";


    // Liste pour récupérer tous les comptes

    EditText mEdit;
    EditText mEdit1;
    String recupere;

    //Liste des infos de chaque compte
    private ArrayList<String> listUsers;
    private ArrayList<List> AccountInfo = new ArrayList<>();

    private String userid;
    Toolbar toolbar;
    String classname;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
    // Récupération des informations de la liste

        context = this;

        Intent intent=getIntent();

        if (intent != null) {

            classname = intent.getStringExtra("Classname");
        }

        if (classname.equals("CreateAccount")){
            Toast.makeText(context, "Votre compte a bien été créé ", Toast.LENGTH_LONG).show();
        }else if (classname.equals("Welcome")){
            //nothing to do
        }


        Utils.debug("Context",this.toString());
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ConnexionActivity.this, WelcomeActivity.class);
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

        ApiRequestPost.postToken(this, email, mdp);




        Utils.info(TAG,"Login Button action");

    }

    public static void erreurConnexion(){
        Toast.makeText(context, "Vos identifiants sont incorrects ", Toast.LENGTH_LONG).show();
    }

    public void createAccount(View view){
        Intent intent =  new Intent(ConnexionActivity.this, CreateAccount.class);
        intent.putExtra("Classname","ConnexionActivity");
        startActivity(intent);
    }

    public static void redirection(String response){
        String toto = response;


        JsonParser parser = new JsonParser();
        JsonObject token = (JsonObject) parser.parse(toto);

        Utils.debug("ConnexionActivity", token.toString());

        String id = token.get("id").toString();
        String test = token.get("value").toString();
        String value = test.replace("\""," ");

        JsonObject user = token.get("user").getAsJsonObject();
        String userid = user.get("id").toString();
        Bdd.setUserid(userid);
        Utils.debug("ConnexionActivity", "id : " + id + " Value " +value);

        Bdd.setValue(value,id);


        String utilisateur = user.get("username").toString();
        String iduser = user.get("id").toString();
        Utils.debug("ConnexionActivity",utilisateur);
        Intent intent = new Intent(context, LandingActivity.class);
        intent.putExtra("name",utilisateur);

        Bdd.setNomUtilisateur(utilisateur);
        context.startActivity(intent);



    }


    public static void redirectionaftercreateparcours(String reponse){
        Intent intent = new Intent(context, CreateParcours.class);
        context.startActivity(intent);

    }

    public static void change(String utilisateur){
        Intent intent = new Intent(context, LandingActivity.class);
        intent.putExtra("name",utilisateur);
        Bdd.setNomUtilisateur(utilisateur);
        context.startActivity(intent);
        Utils.info("ConnexionActivity","Login Button action");
    }

}
