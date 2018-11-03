package com.application.sed.raid_tracker_appli;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.API.ApiRequestPost;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creationaccount);
        Utils.info(TAG, "OnCreate");



        /* récupération des identifiants des éléments pour créer un compte */
        mButton = (Button) findViewById(R.id.createAccount);
        //identifiant = (EditText) findViewById(R.id.identifiant);
        prenom = (EditText) findViewById(R.id.prenom);
        //nom = (EditText) findViewById(R.id.nom);
        mail = (EditText) findViewById(R.id.mail);
        password1 = (EditText) findViewById(R.id.input_password1);
        password2 = (EditText) findViewById(R.id.input_password2);





//        getitem = (CheckBox) findViewById(R.id.checkbox_meat);
//        getitem2 = (CheckBox) findViewById(R.id.checkbox_cheese);
//        getLinear = (LinearLayout) findViewById(R.id.checkbox);

        /* ActionListener sur le bouton Valider */
//        mButton.setOnClickListener(
//                new View.OnClickListener() {
//                    public void onClick(View view) {
//                        recupere_identifiant=identifiant.getText().toString();
//                        recupere_prenom=prenom.getText().toString();
//                        recupere_nom=nom.getText().toString();
//                        recupere_mail=mail.getText().toString();
//                        recupere_password=password.getText().toString();
//
//
//                        myListe = new ArrayList();
//                        myListe.add(recupere_identifiant);
//                        myListe.add(recupere_prenom);
//                        myListe.add(recupere_nom);
//                        myListe.add(recupere_mail);
//                        myListe.add(recupere_password);
//
//
//                        Utils.info("Toutes les valeurs du tableau",myListe.toString()); // OK
//                        Bdd.addAccount(myListe);
//
//                        Intent intent =  new Intent(CreateAccount.this, Accueil.class);
//                        startActivity(intent);
//
//                    }
//
//                });
    }

    public void login(View view){

        /*
         * TODO login
         * Bouton du login -> direction vers page HOME si ok (HomeActivity)
         * Sinon, message d'erreur
//         */
//        final EditText user = findViewById(R.id.username);
//        final EditText pass = findViewById(R.id.password);
//
//        // Test du bouton avec le user toto et password test
//        if (user.getText().toString().equals("Username") & pass.getText().toString().equals("Password")){
//            Utils.debug(TAG, "cool");
//            Intent intent = new Intent(CreateAccount.this, NewraidActivity.class);
//            Utils.info(TAG,"connexion, new activity");
//            startActivity(intent);
//        }
//        else {
//            Utils.info(TAG, "pas cool");
//        }
//
//        // fin du test
//
//
//        Utils.info(TAG,"Login Button action");



        //récupère les infos du compte

        mButton = (Button) findViewById(R.id.createAccount);
        //recupere_identifiant=identifiant.getText().toString();
        recupere_prenom=prenom.getText().toString();
        //recupere_nom=nom.getText().toString();
        recupere_mail=mail.getText().toString();
        recupere_password1=password1.getText().toString();
        recupere_password2=password2.getText().toString();

        String nom = recupere_prenom;
        String mail1 = recupere_mail;
        String pwd1 = recupere_password1;
        String pwd2 = recupere_password2;

        int checkprenom=1;
        int checkmail=1;
        int checkpassword1=1;
        int checkpassword2=1;


        if (isEmpty(prenom)) {
            checkprenom=0;
            prenom.setError("eh oh le prenom là");
            }

        if (isEmail(mail)==false){
            checkmail=0;
            mail.setError("le mail là");
        }
        if (isEmpty(password1)){
            checkpassword1=0;
            password1.setError("password 1");
        }

        if (isEmpty(password2)){
            checkpassword2=0;
            password2.setError("password 2");
        }



        if (checkprenom==1 && checkpassword1==1 && checkpassword2==1 && checkmail==1 && pwd1.equals(pwd2)){
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

            Intent intent =  new Intent(CreateAccount.this, Accueil.class);
            startActivity(intent);
        }



//       ApiRequestPost.postUser(this, nom, mail1, pwd1, pwd2);
//        myListe = new ArrayList();
//        //myListe.add(recupere_identifiant);
//        myListe.add(recupere_prenom);
//        //myListe.add(recupere_nom);
//        myListe.add(recupere_mail);
//        myListe.add(recupere_password1);
//
//
//        Utils.info("Toutes les valeurs du tableau",myListe.toString()); // OK
//        Bdd.addAccount(myListe);

        //Intent intent =  new Intent(CreateAccount.this, Accueil.class);
        //startActivity(intent);

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
        Intent intent = new Intent(CreateAccount.this, Accueil.class);
        startActivity(intent);
    }


}

