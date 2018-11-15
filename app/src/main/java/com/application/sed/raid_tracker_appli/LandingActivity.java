package com.application.sed.raid_tracker_appli;


import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.sed.raid_tracker_appli.API.ApiRequestDelete;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.application.sed.raid_tracker_appli.organizer.CourseActivity;
import com.application.sed.raid_tracker_appli.organizer.CreateCourse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;


public class LandingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //public static List Tata;
    private String nomutilisateur = "test";
    private String iduser;
    private String token;



    private String TAG = "WelcomeActivity";
    private static String TAGs = "WelcomeActivity";

    // Attributs pour le menu Hamburger et l'actionBar
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    View button;
    Button dec;

    private TextView nameofuser;
    private ActionBarDrawerToggle toggle;
    private String value;
    private String id;


    String Element;
    private ActionBarDrawerToggle drawerToggle;


    private static TextView b1;
    private ArrayList<Button> listButton;
    private ArrayList<List> raidlist;


    private static Context context;


    private static LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        //ApiRequestGet.postMethod();

        Utils.info(TAG, "OnCreate");

        nomutilisateur = "toto";
        setContentView(R.layout.activity_landing);
        Intent intent=getIntent();
        nomutilisateur = Bdd.getNomUtilisateur();
        this.nameofuser = findViewById(R.id.nameofuser);
        nameofuser.setText(nomutilisateur);
       // Utils.debug("Landing",nomutilisateur);

        if (intent != null) {

            Utils.debug(TAG,"je rentre ici");

            nomutilisateur = Bdd.getNomUtilisateur();
            this.nameofuser = findViewById(R.id.nameofuser);
            nameofuser.setText(nomutilisateur);

            iduser = Bdd.getUserid();
            token = Bdd.getValue();

            Utils.debug(TAG, "Je rentre la");

            ll = (LinearLayout) findViewById(R.id.Myfuckinglayout);

            /* get List from Create Raid */
//            raidlist = Bdd.getArrayList();
//
//            listButton = new ArrayList<>();
//
//
//            for (int i = 0; i < raidlist.size(); i ++){
//
//                Button myButton = new Button(this);
//                Utils.debug("Ajout du bouton", "Je rentre dans le for "+i);
//
//                List attributlist;
//                attributlist = raidlist.get(i);
//
//                myButton.setText(attributlist.get(0).toString()+System.getProperty("line.separator")+attributlist.get(2).toString());
//                //myButton.setText(attributlist.get(2).toString());
//                myButton.setId(i);
//
//                listButton.add(myButton);
//                Utils.debug("listbutton", listButton.get(i).toString());
//
//            }
//
//            for (int i = 0; i < listButton.size(); i ++){
//
//                Utils.debug("Rajout des boutons", "Valeurs de i" +i);
//                Button myButton2 = listButton.get(i);
//
////                myButton2.setBackgroundColor(getColor(5));    // Ajout de la couleur en fond du bouton
//                LinearLayout ll = (LinearLayout) findViewById(R.id.Myfuckinglayout);
//
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                ll.addView(myButton2, lp);
//
//            }
//
//
//
        }
//
//
//
//        //getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        //ArrayList<Button> listButton = new ArrayList<>();
//        for (int j = 0; j<listButton.size(); j++) {
//            Button newButton = listButton.get(j);
//
//            newButton.setOnClickListener( new View.OnClickListener() {
//                public void onClick(View view) {
//                    Intent intent =  new Intent(LandingActivity.this, CourseActivity.class);
//                    startActivity(intent);
//
//                }
//            });
//        }


        /**
         * Création vieux Drawer
         */
//
//        this.toolbar = findViewById(R.id.toolbar);
//
//        //definir notre toolbar en tant qu'actionBar
//        setSupportActionBar(toolbar);
//
//        //getSupportActionBar().setIcon(getDrawable(R.drawable.ic_exit_to_app_black_24dp));
//
//
//        //getSupportActionBar().setTitle("Home");
//        //afficher le bouton retour
//        getSupportActionBar().setHomeButtonEnabled(true);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//
//
//        this.drawerLayout = findViewById(R.id.drawerLayout);
//        this.drawerToggle = new ActionBarDrawerToggle(this,this.drawerLayout,0,0);
//        this.drawerLayout.setDrawerListener(this.drawerToggle);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.info("NICK", "button button button..................");
//
//                drawerLayout.openDrawer(Gravity.START);
//
//            }
//        });


        /**
         * Fin de la création du vieux layout
         */
//


        Utils.debug("Juste avant api request","JE suis la");

        ApiRequestGet.getSpecificRaid(context, token, iduser);

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

//        drawerLayout = findViewById(R.id.drawer_layout);
//
//        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,0,R.string.app_name);
//        drawerLayout.addDrawerListener(toggle);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//
//
//        NavigationView navigationView = findViewById(R.id.nav_view);
//
//        navigationView.setNavigationItemSelectedListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //NavigationView navigationView = (Navi  gationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);


        b1 = findViewById(R.id.testvalue);
        b1.setText(getIntent().getStringExtra("switch_value"));
    }


    /*@Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id){
            case R.id.dec :
                break;
            case R.id.adraid:
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }*/



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


//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        // synchroniser le drawerToggle après la restauration via onRestoreInstanceState
//        toggle.syncState();
//    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void join(View view) {

    }

    public void CreateCourse(View view){
        Intent intent =  new Intent(LandingActivity.this, CreateCourse.class);
        startActivity(intent);
    }

    public void logout(View view){


        value = Bdd.getValue();
        id = Bdd.getId();

        ApiRequestDelete.deleteToken(this,value,id);

        Intent intent =  new Intent(LandingActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }

    public static void recupereraid(){
    }

//    public void createparcours(View view){
//        Intent intent=new Intent(LandingActivity.this,CreateParcours.class);
//        startActivity(intent);
//
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.nav_item_one) {
            Intent intent =  new Intent(LandingActivity.this, WelcomeActivity.class);
            startActivity(intent);
            Utils.debug("Nav","item 1");
        } else if (id == R.id.nav_item_two) {
            Utils.debug("Nav","item 2");
        } else if (id == R.id.nav_item_three) {
            Utils.debug("Nav", "item 3");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public static void diffuserRaid(){

//        TextView b1=(TextView) findViewById(R.id.testvalue);
        //Utils.info("je passe la ", "ici meme");

        //b1.setText("coucou");
        //b1.setTextColor(Color.parseColor("#FFFFFF"));
    }


    public static void raidlist (String response){

        Utils.debug("raidlist","JE suis ici");
        ArrayList<Button> listButton;
        listButton = new ArrayList<>();

        //String response = Bdd.getListFromApi();
        JsonParser parser = new JsonParser();
        JsonArray raidlist = (JsonArray) parser.parse(response);
        Utils.debug("raidlist + size", "size : " + raidlist.size() + " raidlist : "+raidlist.toString());

        for (int i = 0; i < raidlist.size(); i ++){

            Button myButton = new Button(context);
            Utils.debug("Ajout du bouton", "Je rentre dans le for "+i);

            JsonParser parser1 = new JsonParser();
            JsonObject raid = (JsonObject) raidlist.get(i);
            String nomraid = raid.get("nom").toString().replace("\""," ");
            String idraid = raid.get("id").toString();
            String date = raid.get("date").toString().replace("\""," ").substring(0,11);


            Utils.debug("raid ", "Nomraid : " + nomraid + " date : "+ date);

            myButton.setText(nomraid+System.getProperty("line.separator")+date);
            //myButton.setText(attributlist.get(2).toString());
            myButton.setId(i);

            listButton.add(myButton);
            Utils.debug("listbutton", listButton.get(i).toString());

        }

        for (int i = 0; i < listButton.size(); i ++){

            Utils.debug("Rajout des boutons", "Valeurs de i" +i);
            Button myButton2 = listButton.get(i);

//                myButton2.setBackgroundColor(getColor(5));    // Ajout de la couleur en fond du bouton

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(myButton2, lp);

        }


        buttonlist(listButton);
    }

    public static void buttonlist(ArrayList<Button> listButton){

//        ArrayList<Button> listButton = new ArrayList<>();
        for (int j = 0; j<listButton.size(); j++) {
            Button newButton = listButton.get(j);

            newButton.setOnClickListener( new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent =  new Intent(context, CourseActivity.class);
                    context.startActivity(intent);

                }
            });
        }
    }
}
