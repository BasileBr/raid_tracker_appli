package com.application.sed.raid_tracker_appli;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.application.sed.raid_tracker_appli.API.ApiRequestPost;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConnexionActivity extends AppCompatActivity {
    private static Context context;
    private static String TAG="ConnexionActivity";

    EditText mEdit;
    EditText mEdit1;
    Toolbar toolbar;
    String classname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

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


        Utils.debug(TAG,this.toString());
        toolbar = findViewById(R.id.toolbar);
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

    /**
     *
     * @param view
     */
    public void login(View view){

        mEdit = findViewById(R.id.addrmail);
        mEdit1 = findViewById(R.id.passwordconnexion);
        String email = mEdit.getText().toString();
        String mdp=mEdit1.getText().toString();
        ApiRequestPost.postToken(this, email, mdp);
    }

    /**
     *
     */
    public static void erreurConnexion(){
        Toast.makeText(context, "Vos identifiants sont incorrects ", Toast.LENGTH_LONG).show();
    }

    /**
     *
     * @param view
     */
    public void createAccount(View view){
        Intent intent =  new Intent(ConnexionActivity.this, CreateAccount.class);
        intent.putExtra("Classname","ConnexionActivity");
        startActivity(intent);
    }

    /**
     *
     * @param response
     */
    public static void redirection(String response){

        JsonParser parser = new JsonParser();
        JsonObject token = (JsonObject) parser.parse(response);

        String id = token.get("id").toString();
        String test = token.get("value").toString();
        String value = test.replace("\""," ");

        JsonObject user = token.get("user").getAsJsonObject();
        String userid = user.get("id").toString();

        Bdd.setUserid(userid);
        Bdd.setValue(value,id);

        String utilisateur = user.get("username").toString();
        Intent intent = new Intent(context, LandingActivity.class);
        intent.putExtra("name",utilisateur);

        Bdd.setNomUtilisateur(utilisateur);
        context.startActivity(intent);

    }

    /**
     *
     * @param utilisateur
     */
    public static void change(String utilisateur){
        Intent intent = new Intent(context, LandingActivity.class);
        intent.putExtra("name",utilisateur);
        Bdd.setNomUtilisateur(utilisateur);
        context.startActivity(intent);
    }

}
