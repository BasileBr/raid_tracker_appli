package com.application.sed.raid_tracker_appli;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.application.sed.raid_tracker_appli.API.ApiRequestPost;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.application.sed.raid_tracker_appli.Utils.Bdd;

import java.util.ArrayList;
import java.util.List;

public class CreateAccount extends AppCompatActivity{
    private String TAG="CreateAccount";

    //Liste contenant les informations lors de la création d'un compte
    public static List myListe; //= new ArrayList<>();

    //Bouton de validation de création d'un compte
    Button mButton;


    //EditText identifiant;
    EditText prenom;
    //EditText nom;
    EditText mail;
    EditText password1;
    EditText password2;


    //String recupere_identifiant;
    String recupere_prenom;
    //String recupere_nom;
    String recupere_mail;
    String recupere_password1;
    String recupere_password2;
    private static String nom="";
    private static String mail1="";
    private static Context context;

    Toolbar toolbar;
    String classname;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creationaccount);
        Utils.info(TAG, "OnCreate");

        context = this;


        Intent intent=getIntent();

        if (intent != null) {

            classname = intent.getStringExtra("Classname");
        }


            /* récupération des identifiants des éléments pour créer un compte */
        mButton = (Button) findViewById(R.id.createAccount);
        //identifiant = (EditText) findViewById(R.id.identifiant);
        prenom = (EditText) findViewById(R.id.prenom);
        //nom = (EditText) findViewById(R.id.nom);
        mail = (EditText) findViewById(R.id.mail);
        password1 = (EditText) findViewById(R.id.input_password1);
        password2 = (EditText) findViewById(R.id.input_password2);


        toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classname.equals("Accueil")) {
                    Intent intent = new Intent(CreateAccount.this, Accueil.class);
                    startActivity(intent);
                }
                else if (classname.equals("Welcome")) {
                    Intent intent = new Intent(CreateAccount.this, WelcomeActivity.class);
                    startActivity(intent);
                }

            }
        });


    }

    public void login(View view){

        //récupère les infos du compte

        mButton = (Button) findViewById(R.id.createAccount);
        //recupere_identifiant=identifiant.getText().toString();
        recupere_prenom=prenom.getText().toString();
        //recupere_nom=nom.getText().toString();
        recupere_mail=mail.getText().toString();
        recupere_password1=password1.getText().toString();
        recupere_password2=password2.getText().toString();

        nom = recupere_prenom;
        mail1 = recupere_mail;
        String pwd1 = recupere_password1;
        String pwd2 = recupere_password2;

        int checkprenom=1;
        int checkmail=1;
        int checkpassword1=1;
        int checkpassword2=1;
        int checklengthpassword=1;


        if (isEmpty(prenom)) {
            checkprenom=0;
            prenom.setError("le prénom n'est pas renseigné");
            }

        if (password1.length()<8){
            checklengthpassword=0;
            password1.setError("Le mot de passe doit être composé de 8 caractères au minimum");
        }

        if (!isEmail(mail)){
            checkmail=0;
            mail.setError("l'adresse mail n'est pas conforme");
        }
        if (isEmpty(password2)){
            checkpassword1=0;
            password2.setError("le champ est vide ");
        }

        if (isEmpty(password1 )){
            checkpassword2=0;
            password1.setError("le champ est vide ");
        }else if (!pwd1.equals(pwd2)){
            password1.setError("mot de passe non identique");
            password2.setError("mot de passe non identique");
        }



        if (checkprenom==1 && checkpassword1==1 && checkpassword2==1 && checkmail==1 && pwd1.equals(pwd2) &&checklengthpassword==1){
            Utils.info("je vérifie tout","youhou");

            ApiRequestPost.postUser(this, nom, mail1, pwd1);
            myListe = new ArrayList();
            //myListe.add(recupere_identifiant);
            myListe.add(recupere_prenom);
            //myListe.add(recupere_nom);
            myListe.add(recupere_mail);
            myListe.add(recupere_password1);


            Utils.info("Toutes les valeurs du tableau",myListe.toString()); // OK
            Bdd.addAccount(myListe);

        }

    }


    //confirmation de la création d'un compte
    public static void sendmail (){
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("sporteventdevelopment@gmail.com",
                            "Sp6!b&hsv89%");
                    sender.sendMail("Bienvenue sur RaidTracker", "Bonjour "+nom+", vous venez de créer votre compte sur l'application RaidTracker",
                            "sporteventdevelopment@gmail.com",mail1);

                    Utils.info("envoi", "mail");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();


        Intent intent =  new Intent(context, Accueil.class);
        context.startActivity(intent);
    }


    public static void creationFailure (){
        Toast.makeText(context, "La création du compte a échoué ", Toast.LENGTH_LONG).show();
    }



    boolean isEmail(EditText text){
        CharSequence email=text.getText().toString();
        return (!TextUtils.isEmpty(email)&& Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty (EditText text){
        CharSequence str= text.getText().toString();
        return TextUtils.isEmpty(str);
    }




    public void cancel(View view){
        if (classname.equals("Accueil")) {
            Intent intent = new Intent(CreateAccount.this, Accueil.class);
            startActivity(intent);
        }
        else if (classname.equals("Welcome")) {
            Intent intent = new Intent(CreateAccount.this, WelcomeActivity.class);
            startActivity(intent);
        }
    }


}

