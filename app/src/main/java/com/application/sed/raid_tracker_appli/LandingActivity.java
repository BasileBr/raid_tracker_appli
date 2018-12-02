package com.application.sed.raid_tracker_appli;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.sed.raid_tracker_appli.API.ApiRequestDelete;
import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.application.sed.raid_tracker_appli.organizer.CourseActivity;
import com.application.sed.raid_tracker_appli.organizer.CreateCourse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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
    private static ArrayList<String> listIdRaid = new ArrayList<>();
    private static ArrayList<String> listid;
    private static String nomRaid;
    public static String idRaid = "0";
    public static int cptid;


    private static int j;
    String Element;
    private ActionBarDrawerToggle drawerToggle;


    private static TextView b1;
    private ArrayList<Button> listButton;
    private ArrayList<List> raidlist;
    private static String idRaidBenevole;
    private static ArrayList<String> listidRaidBenevoles;

    private static HashMap<String, String> meMap;
   // private static LinkedHashMap<String, String> profileMap;

    private static Context context;


    private static LinearLayout layout;

    private static LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TextView setNameParcours = (TextView) findViewById(R.id.textname);


        context = this;


        Utils.info(TAG, "OnCreate");

        nomutilisateur = "toto";
        setContentView(R.layout.activity_landing);
        Intent intent = getIntent();
        nomutilisateur = Bdd.getNomUtilisateur();
        this.nameofuser = findViewById(R.id.nameofuser);
        nameofuser.setText(nomutilisateur);

        if (intent != null) {

            Utils.debug(TAG, "je rentre ici");

            nomutilisateur = Bdd.getNomUtilisateur();
            this.nameofuser = findViewById(R.id.nameofuser);
            nameofuser.setText(nomutilisateur);

            iduser = Bdd.getUserid();
            token = Bdd.getValue();
            //Utils.debug("Utilisateur",iduser);
            Bdd.setUserid(iduser);

            Utils.debug(TAG, "Je rentre la");

            ll = (LinearLayout) findViewById(R.id.Myfuckinglayout);

        }


        Utils.debug("Juste avant api request", "JE suis la");

        ApiRequestGet.getSpecificRaid(context, token, iduser, "LandingActivity");

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        b1 = findViewById(R.id.testvalue);
        b1.setText(getIntent().getStringExtra("switch_value"));

        layout = (LinearLayout) findViewById(R.id.ListeRaid);

        ApiRequestGet.getAllRaids(context, "LandingActivity");
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
        Intent intent = new Intent(LandingActivity.this, VolunteerPreferenceActivity.class);
        startActivity(intent);
    }

    public void CreateCourse(View view) {
        Intent intent = new Intent(LandingActivity.this, CreateCourse.class);
        startActivity(intent);
    }

    public void logout(View view) {


        value = Bdd.getValue();
        id = Bdd.getId();

        ApiRequestDelete.deleteToken(this, value, id);

        Intent intent = new Intent(LandingActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }

    public static void recupereraid() {
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.mail) {
            Intent intent = new Intent(context, InviteVolunteersActivity.class);
            context.startActivity(intent);
            Utils.debug("coucou", "basile");
        }
//            Utils.debug("Nav","item 1");
//        } else if (id == R.id.nav_item_two) {
//            Utils.debug("Nav","item 2");
//        } else if (id == R.id.nav_item_three) {
//            Utils.debug("Nav", "item 3");
//        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static void diffuserRaid() {

//        TextView b1=(TextView) findViewById(R.id.testvalue);
        //Utils.info("je passe la ", "ici meme");

        //b1.setText("coucou");
        //b1.setTextColor(Color.parseColor("#FFFFFF"));
    }

    public void asupprimer(View view) {
        Intent intent = new Intent(context, ManageParcoursActivity.class);
        context.startActivity(intent);
    }


    public static void raidlist(String response) {

        Utils.debug("raidlist", "JE suis ici");
        ArrayList<Button> listButton;
        listButton = new ArrayList<>();
        int k;
        int taille = listIdRaid.size();
        Utils.debug("raid", "raid : " + listIdRaid.toString() + " taille : " + taille);
        for (k = taille - 1; k > -1; k--) {

            listIdRaid.remove(k);
            Utils.debug("taille", "Je suis dans la boucle " + k);
        }

        Utils.debug("raid ", "taille : " + taille + " Raid list " + listIdRaid.toString());
        //String response = Bdd.getListFromApi();
        JsonParser parser = new JsonParser();
        JsonArray raidlist = (JsonArray) parser.parse(response);
        Utils.debug("raidlist + size", "size : " + raidlist.size() + " raidlist : " + raidlist.toString());

        for (int i = 0; i < raidlist.size(); i++) {

            Button myButton = new Button(context);
            Utils.debug("Ajout du bouton", "Je rentre dans le for " + i);

            JsonParser parser1 = new JsonParser();
            JsonObject raid = (JsonObject) raidlist.get(i);
            String nomraid = raid.get("nom").toString().replace("\"", " ");
            String idraid = raid.get("id").toString();
            String date = raid.get("date").toString().replace("\"", " ").substring(0, 11);

            listIdRaid.add(idraid);
            Utils.debug("raid ", "Nomraid : " + nomraid + " date : " + date + " idraid" + idraid);

            myButton.setText(nomraid + System.getProperty("line.separator") + date);
            //myButton.setText(attributlist.get(2).toString());
            myButton.setId(i);

            listButton.add(myButton);
            Utils.debug("listbutton", listButton.get(i).toString());

        }
        Utils.debug("raid", "idRaid " + listIdRaid.toString());

        Bdd.setListIdRaid(listIdRaid);
        Utils.debug("raid", "idRaid " + Bdd.getlistIdRaid().toString());
        for (int i = 0; i < listButton.size(); i++) {

            Utils.debug("Rajout des boutons", "Valeurs de i" + i);
            Button myButton2 = listButton.get(i);

//                myButton2.setBackgroundColor(getColor(5));    // Ajout de la couleur en fond du bouton

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(myButton2, lp);

        }


        buttonlist(listButton);
    }

    public static void buttonlist(ArrayList<Button> listButton) {

//        ArrayList<Button> listButton = new ArrayList<>();
        listid = new ArrayList<>();
        listid = Bdd.getlistIdRaid();
        Utils.debug("raid list", listid.toString());

        for (j = 0; j < listButton.size(); j++) {
            final Button newButton = listButton.get(j);
            //Bdd.setIdRaid(listIdRaid.get(j));
            newButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //Bdd.setIdRaid(Bdd.getIdRaid());
                    int id = newButton.getId();
                    //Utils.debug("CourseActivity","idraid : "+listIdRaid.get(j)+" j:"+j);
                    String idraid = listid.get(id);
                    Utils.debug("CourseActivity", "idraid :" + idraid);
                    Intent intent = new Intent(context, CourseActivity.class);
                    intent.putExtra("idRaid", idraid);
                    context.startActivity(intent);

                }
            });
        }
    }

    public static void recupRaid(String response) {

        ArrayList<Button> listRaidstoJoin= new ArrayList<>();

        ArrayList<String> listIdRaidBenevoles = new ArrayList<>();

        meMap=new HashMap<>();

        //recupération des raids benevoles dispo
        JsonParser parser = new JsonParser();

        //on parse les réponses
        JsonArray listRaidsBenevoles = (JsonArray) parser.parse(response);

        //boucle pour parcourir la requête
        for (int k = 0; k < listRaidsBenevoles.size(); k++) {

            //récupére chaque élément
            JsonObject raidVisible = (JsonObject) listRaidsBenevoles.get(k);

            // on récupère chaque élément important
            String nom_raid = raidVisible.get("nom").toString().replace("\"", " ");;
            String id_raid = raidVisible.get("id").toString();

            meMap.put(id_raid,nom_raid);


            for (Map.Entry<String, String> entry : meMap.entrySet())
            {
                TextView textView = new TextView(context);
                textView.setText(entry.getValue());
                LinearLayout ll = new LinearLayout(context);
                ll.addView(textView);
                layout.addView(ll);
                System.out.println(entry.getKey() + "/" + entry.getValue());
            }
            //déclare un bouton pour
           /* Button myButton = new Button(context);


            //on recupere le nom du raid et l'id associé
            JsonElement nom = raidVisible.get("nom");
            JsonElement id_raid = raidVisible.get("id");

            //on les convertit en string
            nomRaid = nom.getAsString();
            String idraid2 = id_raid.getAsString();

            //on ajoute un tag qui corrspond à l'id du raid
            //btn.setId(k);
            btn.setTag(idraid2);

            Utils.debug("idRaidtest",btn.getTag().toString());

            //
            //listIdRaidBenevoles.add(idraid2);


            //on a une arraylist de buttons pour chaque bouton on a le tag qui correspond à l'id du raid
            listRaidstoJoin.add(btn);*/

        }

       /* for (int i = 0; i < listRaidstoJoin.size(); i ++){


            Button btn2 = listRaidstoJoin.get(i);
            String idRaidBenevole = listIdRaid.get(i);

            //création des boutons
            Button btn = new Button(context);
            //Button btn2 = new Button(context);

            //ajout de l'id
            btn2.setId(i);
            btn.setText(nomRaid+ '\n' + "Rejoignez l'aventure");
            btn2.setText("Rejoignez -NOUS ");
            btn.setTextColor(context.getResources().getColor(R.color.black));
            btn2.setTextColor(context.getResources().getColor(R.color.black));
            btn2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            btn2.setGravity(Gravity.END);
            btn2.setTag(idRaidBenevole);
            btn.setTag(idRaidBenevole);

           // btn2.setTag(idraid2);
           // listRaidstoJoin.add(btn2);


            //btn.setHeight(150);
            //btn.setBackgroundColor(80000000);

            // Create LinearLayout
            LinearLayout ll = new LinearLayout(context);
            //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.setBackgroundResource(R.drawable.coureur2);
            //ll.setBackgroundResource(R.color.VertPrimaire);
            ll.addView(btn);
            ll.addView(btn2);
            layout.addView(ll);

        }
        parcoursButton(listRaidstoJoin);
    }

    public static void parcoursButton(ArrayList<Button> listButton){

        int j;
        for (j = 0; j<listButton.size(); j++) {
            final Button newButton = listButton.get(j);
            Utils.debug("parcoursButtontest", "idParcours : "+newButton.getTag());
            newButton.setOnClickListener( new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent =  new Intent(context, VolunteerPreferenceActivity.class);
                    String idRaid = newButton.getTag().toString();
                    intent.putExtra("idRaid",idRaid);
                    context.startActivity(intent);

                }
            });
        }*/

    }
}
