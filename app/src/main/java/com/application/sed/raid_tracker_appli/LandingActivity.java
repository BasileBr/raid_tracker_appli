package com.application.sed.raid_tracker_appli;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class LandingActivity extends AppCompatActivity {

    //public static List Tata;
    private String nomutilisateur = "test";



    private String TAG = "WelcomeActivity";
    private static String TAGs = "WelcomeActivity";
    // Attributs pour le menu Hamburger et l'actionBar
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    View button;

    private TextView nameofuser;



    String Element;
    private ActionBarDrawerToggle drawerToggle;



    private ArrayList<List> raidlist;
    private ArrayList<Button> listButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.info(TAG, "OnCreate");

        setContentView(R.layout.activity_landing);
        Intent intent=getIntent();

        if (intent != null) {

            Utils.debug(TAG,"je rentre ici");

            nomutilisateur = Bdd.getNomUtilisateur();
            this.nameofuser = findViewById(R.id.nameofuser);
            nameofuser.setText(nomutilisateur);

            Utils.debug(TAG, "Je rentre la");
            /* get List from Create Raid */
            raidlist = Bdd.getArrayList();

            listButton = new ArrayList<>();

            for (int i = 0; i < raidlist.size(); i ++){

                Button myButton = new Button(this);
                Utils.debug("Ajout du bouton", "Je rentre dans le for "+i);

                List attributlist;
                attributlist = raidlist.get(i);

                myButton.setText(attributlist.get(0).toString()+System.getProperty("line.separator")+attributlist.get(2).toString());
                //myButton.setText(attributlist.get(2).toString());
                myButton.setId(i);

                listButton.add(myButton);
                Utils.debug("listbutton", listButton.get(i).toString());

            }

            for (int i = 0; i < listButton.size(); i ++){

                Utils.debug("Rajout des boutons", "Valeurs de i" +i);
                Button myButton2 = listButton.get(i);

                LinearLayout ll = (LinearLayout) findViewById(R.id.Myfuckinglayout);

//                RelativeLayout but = findViewById(R.id.boutooondemerde);
//                ll.setNextFocusRightId(but.getId());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll.addView(myButton2, lp);

            }

        }


        this.toolbar = findViewById(R.id.toolbar);

        //definir notre toolbar en tant qu'actionBar
        setSupportActionBar(toolbar);

        //getSupportActionBar().setIcon(getDrawable(R.drawable.ic_exit_to_app_black_24dp));


        //getSupportActionBar().setTitle("Home");
        //afficher le bouton retour
        getSupportActionBar().setHomeButtonEnabled(true);


        //getSupportActionBar().setDisplayShowTitleEnabled(false);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        this.drawerLayout = findViewById(R.id.drawerLayout);
        this.drawerToggle = new ActionBarDrawerToggle(this,this.drawerLayout,0,0);
        this.drawerLayout.setDrawerListener(this.drawerToggle);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.info("NICK", "button button button..................");

                drawerLayout.openDrawer(Gravity.START);

            }
        });
//




    }

    public void getDescriptionRAid(View view) {
        //RelativeLayout ll=(RelativeLayout) findViewById(R.id.boutooondemerde);
//        /* get List from Create Raid */
//        raidlist = Bdd.getArrayList();
//
//        /*récupérer la première Liste*/
//        Tata = raidlist.get(0);
//
//        /* */
//        String titre = Tata.get(0).toString();
//
////
//        Button buttonView = new Button(this);
//        buttonView.setText(titre);
//        ll.addView(buttonView);
//
//        Utils.info("EditText", titre);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // synchroniser le drawerToggle après la restauration via onRestoreInstanceState
        drawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    public void join(View view) {

    }

    public void CreateCourse(View view){
        Intent intent =  new Intent(LandingActivity.this, CreateCourse.class);
        startActivity(intent);
    }

    public void logout(View view){
        Intent intent =  new Intent(LandingActivity.this, Accueil.class);
        startActivity(intent);
    }

    public static void recupereraid(){
    }

}
