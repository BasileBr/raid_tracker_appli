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


    private ArrayList<List> AccountInfo;
    EditText mEdit;
    EditText mEdit1;
    String recupere;


    private ArrayList<String> listUsers;




    //private ArrayList<Button> listButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);


        // Récupération des informations de la liste
        AccountInfo = Bdd.getAccount();


        for (int i = 0; i < AccountInfo.size(); i++) {

            // Liste pour un user
            List infoUsers = new ArrayList();


            // Récupère toutes les infos
            infoUsers = AccountInfo.get(i);
            String values = infoUsers.toString();

            Utils.info("Je suis capable d'afficher toutes les valeurs d'un compte crée", values);

            //récuperer le premier élément
            String identifiant = infoUsers.get(0).toString();
            String password= infoUsers.get(4).toString();

            Utils.info("J'affiche l'identifiant", identifiant);
            Utils.info("J'affiche le mot de passe ",password);

            //Liste pour stocker les identifiants et mot de passe
            listUsers = new ArrayList<>();

            //ajout de l'identifiant dans la liste
            listUsers.add(identifiant);

            //ajout du mot de passe dans la liste
            listUsers.add(password);


        }


    }

    public void login(View view){

        /*
         * TODO login
         * Bouton du login -> direction vers page HOME si ok (HomeActivity)
         * Sinon, message d'erreur
         */
        final EditText user = findViewById(R.id.username);
        final EditText pass = findViewById(R.id.password);


        mEdit = (EditText) findViewById(R.id.username);
        mEdit1 = (EditText) findViewById(R.id.password);

        String identifiant = mEdit.getText().toString();
        String mdp=mEdit1.getText().toString();


        boolean isValid;



        // Test du bouton avec le user toto et password test
//        if (user.getText().toString().equals("User") & pass.getText().toString().equals("Password")){
//            Utils.debug(TAG, "cool");
//            Utils.Name = "toto";
//            Intent intent = new Intent(Accueil.this, LandingActivity.class);
//            Utils.info(TAG,"connexion, new activity");
//            intent.putExtra("name","test");
//            startActivity(intent);
//        }
//        else {
//            Utils.info(TAG, "pas cool");
//        }



        /*vérification lors de la connexon */
        for (int j = 0; j < listUsers.size(); j ++){

            String test= listUsers.get(j);

            Utils.info("Identifiant  eet je sais pas quoi",test);


            if (listUsers.get(0).equals(identifiant)& listUsers.get(1).equals(mdp)){
                isValid =true;
                Intent intent = new Intent(Accueil.this, LandingActivity.class);
                intent.putExtra("name","test");
                startActivity(intent);
            }
            // else afficher la popup erreur de connexion
        isValid=false;
        }


        Utils.info(TAG,"Login Button action");

    }

    public void createAccount(View view){
        Intent intent =  new Intent(Accueil.this, CreateAccount.class);
        startActivity(intent);
    }

}
