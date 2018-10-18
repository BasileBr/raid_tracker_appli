package com.application.sed.raid_tracker_appli;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.application.sed.raid_tracker_appli.organizer.NewraidActivity;

import java.util.ArrayList;
import java.util.List;

public class CreateAccount extends AppCompatActivity{
    private String TAG="CreateAccount";

    //Liste contenant les informations lors de la création d'un compte
    public static List myListe;

    //Bouton de validation de création d'un compte
    Button mButton;


    EditText identifiant;
    EditText prenom;
    EditText nom;
    EditText mail;
    EditText password;


    String recupere_identifiant;
    String recupere_prenom;
    String recupere_nom;
    String recupere_mail;
    String recupere_password;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creationaccount);
        Utils.info(TAG, "OnCreate");




        /* récupération des identifiants des éléments pour créer un compte */
        mButton = (Button) findViewById(R.id.createAccount);
        identifiant = (EditText) findViewById(R.id.identifiant);
        prenom = (EditText) findViewById(R.id.prenom);
        nom = (EditText) findViewById(R.id.nom);
        mail = (EditText) findViewById(R.id.mail);
        password = (EditText) findViewById(R.id.input_password);


//        getitem = (CheckBox) findViewById(R.id.checkbox_meat);
//        getitem2 = (CheckBox) findViewById(R.id.checkbox_cheese);
//        getLinear = (LinearLayout) findViewById(R.id.checkbox);

        /* ActionListener sur le bouton Valider */
        mButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        myListe = new ArrayList<>();
                        recupere_identifiant=identifiant.getText().toString();
                        recupere_prenom=prenom.getText().toString();
                        recupere_nom=nom.getText().toString();
                        recupere_mail=mail.getText().toString();
                        recupere_password=password.getText().toString();

                        myListe.add(recupere_identifiant);
                        myListe.add(recupere_prenom);
                        myListe.add(recupere_nom);
                        myListe.add(recupere_mail);
                        myListe.add(recupere_password);


                        Utils.info("Toutes les valeurs du tableau",myListe.toString()); // OK
                        Bdd.addString(myListe);

                    }

                });
    }

    public void login(View view){

        /*
         * TODO login
         * Bouton du login -> direction vers page HOME si ok (HomeActivity)
         * Sinon, message d'erreur
         */
        final EditText user = findViewById(R.id.username);
        final EditText pass = findViewById(R.id.password);

        // Test du bouton avec le user toto et password test
        if (user.getText().toString().equals("Username") & pass.getText().toString().equals("Password")){
            Utils.debug(TAG, "cool");
            Intent intent = new Intent(CreateAccount.this, NewraidActivity.class);
            Utils.info(TAG,"connexion, new activity");
            startActivity(intent);
        }
        else {
            Utils.info(TAG, "pas cool");
        }

        // fin du test


        Utils.info(TAG,"Login Button action");

    }



    public void cancel(View view){
        Intent intent = new Intent(CreateAccount.this, Accueil.class);
        startActivity(intent);
    }
}

