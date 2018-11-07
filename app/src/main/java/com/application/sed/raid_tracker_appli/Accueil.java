package com.application.sed.raid_tracker_appli;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.application.sed.raid_tracker_appli.organizer.NewraidActivity;

import java.util.ArrayList;
import java.util.List;

public class Accueil extends AppCompatActivity {
    private String TAG="Accueil";


    // Liste pour récupérer tous les comptes

    EditText mEdit;
    EditText mEdit1;
    String recupere;

    //Liste des infos de chaque compte
    private ArrayList<String> listUsers;
    private ArrayList<List> AccountInfo = new ArrayList<>();


    Toolbar toolbar;
    //private ArrayList<Button> listButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);


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
            // else afficher la popup erreur de connexion

            isValid=false;


        }


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


}
