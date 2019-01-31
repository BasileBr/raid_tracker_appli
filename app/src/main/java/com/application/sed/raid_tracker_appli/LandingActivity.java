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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.sed.raid_tracker_appli.API.ApiRequestDelete;
import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.application.sed.raid_tracker_appli.helper.PosteDescription;
import com.application.sed.raid_tracker_appli.helper.VolunteerPreferenceActivity;
import com.application.sed.raid_tracker_appli.organizer.CourseActivity;
import com.application.sed.raid_tracker_appli.organizer.CreateCourse;
import com.application.sed.raid_tracker_appli.organizer.InviteVolunteersActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class LandingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String iduser;
    private String token;
    private static String TAG = "WelcomeActivity";
    View button;
    Button dec;
    public ActionBarDrawerToggle toggle;

    private static ArrayList<String> listIdRaid = new ArrayList<>();
    public static String idRaid = "0";
    private static ArrayList<String> listidRaidBenevolesUser = new ArrayList<>();

    private static Context context;
    private static LinearLayout layout;
    private static LinearLayout ll;
    private static LinearLayout ll2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        context = this;
        Utils.info(TAG, "OnCreate");

        Intent intent = getIntent();
        String nomutilisateur = Bdd.getNomUtilisateur();
        TextView nameofuser = findViewById(R.id.nameofuser);
        nameofuser.setText(nomutilisateur);

        if (intent != null) {
            nomutilisateur = Bdd.getNomUtilisateur().replace("\"", " ");
            nameofuser = findViewById(R.id.nameofuser);
            nameofuser.setText(nomutilisateur);
            iduser = Bdd.getUserid();
            token = Bdd.getValue();
            Bdd.setUserid(iduser);
            ll = findViewById(R.id.RaidLayout);
            ll2 = findViewById(R.id.BenevoleLayout);
        }

        ApiRequestGet.getSpecificRaid(context, token, iduser, "LandingActivity");
        ApiRequestGet.getAllRaidsBenevolesofOneUser(context, token, iduser, "LandingActivity");

        // Attributs pour le menu Hamburger et l'actionBar
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView b1 = findViewById(R.id.testvalue);
        b1.setText(getIntent().getStringExtra("switch_value"));

        layout = findViewById(R.id.ListeRaid);
        ApiRequestGet.getAllRaidswithoutRaidsBenevoles(context,token,iduser ,"LandingActivity");
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

    /**
     *
     * @param view
     */
    public void logout(View view) {

        String value = Bdd.getValue();
        String id = Bdd.getId();

        ApiRequestDelete.deleteToken(this, value, id);

        Intent intent = new Intent(LandingActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }

    /**
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();
        if (id == R.id.mail) {
            Intent intent = new Intent(context, InviteVolunteersActivity.class);
            context.startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     *
     * @param response
     */
    public static void raidlistBenevole(String response){

        ArrayList<Button> listButtonBene = new ArrayList<>();
        int kbene;
        int taillebene = listidRaidBenevolesUser.size();
        for (kbene = taillebene - 1; kbene > -1; kbene--) {
            listidRaidBenevolesUser.remove(kbene);
            Utils.debug("taille", "Je suis dans la boucle " + kbene);
        }

        JsonParser parser = new JsonParser();
        JsonArray raidlistbene = (JsonArray) parser.parse(response);

        for (int i = 0; i < raidlistbene.size(); i++) {
            Button myButton = new Button(context);

            JsonObject raid = (JsonObject) raidlistbene.get(i);
            String nomraid = raid.get("nom").toString().replace("\"", " ");
            String idraid = raid.get("id").toString();
            String dateY=raid.get("date").toString().replace("\"", " ").substring(1,5);
            String dateM=raid.get("date").toString().replace("\"", " ").substring(6,8);
            String dateD=raid.get("date").toString().replace("\"", " ").substring(9,11);
            String date=dateD+'/'+dateM+'/'+dateY;

            listidRaidBenevolesUser.add(idraid);

            myButton.setText(nomraid + System.getProperty("line.separator") + date);
            myButton.setId(i);
            listButtonBene.add(myButton);
        }

        Bdd.setListIdRaidBene(listidRaidBenevolesUser);
        for (int i = 0; i < listButtonBene.size(); i++) {
            Button myButton2 = listButtonBene.get(i);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll2.addView(myButton2, lp);
        }
        ButtonListBenevole(listButtonBene);
    }

    /**
     *
     * @param listButton
     */
    public static void ButtonListBenevole(ArrayList<Button> listButton) {

        final ArrayList<String> listid = Bdd.getlistIdRaidBene();
        Utils.debug("raid list", listid.toString());

        for (int k = 0; k< listButton.size(); k++) {
            final Button newButton = listButton.get(k);
            newButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    int id = newButton.getId();
                    String idraid = listid.get(id);
                    Intent intent = new Intent(context, PosteDescription.class);
                    intent.putExtra("idRaid", idraid);
                    context.startActivity(intent);
                }
            });
        }
    }

    /**
     *
     * @param response
     */
    public static void raidlist(String response) {

        ArrayList<Button> listButton;
        listButton = new ArrayList<>();
        int k;
        int taille = listIdRaid.size();
        for (k = taille - 1; k > -1; k--) {
            listIdRaid.remove(k);
        }

        JsonParser parser = new JsonParser();
        JsonArray raidlist = (JsonArray) parser.parse(response);

        for (int i = 0; i < raidlist.size(); i++) {
            Button myButton = new Button(context);

            JsonObject raid = (JsonObject) raidlist.get(i);
            String nomraid = raid.get("nom").toString().replace("\"", " ");
            String idraid = raid.get("id").toString();
            String dateY=raid.get("date").toString().replace("\"", " ").substring(1,5);
            String dateM=raid.get("date").toString().replace("\"", " ").substring(6,8);
            String dateD=raid.get("date").toString().replace("\"", " ").substring(9,11);
            String date=dateD+'/'+dateM+'/'+dateY;

            listIdRaid.add(idraid);

            myButton.setText(nomraid + System.getProperty("line.separator") + date);
            myButton.setId(i);
            listButton.add(myButton);
        }

        Bdd.setListIdRaid(listIdRaid);
        for (int i = 0; i < listButton.size(); i++) {
            Button myButton2 = listButton.get(i);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(myButton2, lp);
        }

        buttonlist(listButton);
    }

    /**
     *
     * @param listButton2
     */
    public static void buttonlist(ArrayList<Button> listButton2) {

        final ArrayList<String> listid = Bdd.getlistIdRaid();

        for (int k  = 0; k < listButton2.size(); k++) {
            final Button newButton = listButton2.get(k);
            newButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    int id = newButton.getId();
                    String idraid = listid.get(id);
                    Intent intent = new Intent(context, CourseActivity.class);
                    intent.putExtra("idRaid", idraid);
                    context.startActivity(intent);
                }
            });
        }
    }

    /**
     *
     * @param response
     */
    public static void recupRaid(String response) {

        ArrayList<Button> listRaidstoJoin = new ArrayList<>();
        LinkedHashMap<String, String> meMap = new LinkedHashMap<>();

        //recupération des raids benevoles dispo
        JsonParser parser = new JsonParser();

        //on parse les réponses
        JsonArray listRaidsBenevoles = (JsonArray) parser.parse(response);

        //boucle pour parcourir la requête
        for (int k = 0; k < listRaidsBenevoles.size(); k++) {

            //récupére chaque élément
            JsonObject raidVisible = (JsonObject) listRaidsBenevoles.get(k);

            // on récupère chaque élément important
            String nom_raid = raidVisible.get("nom").toString().replace("\"", " ");
            String id_raid = raidVisible.get("id").toString();
            meMap.put(id_raid, nom_raid);
        }

        for (Map.Entry<String, String> entry : meMap.entrySet()) {

            //création des éléments
            Button button = new Button(context);
            TextView textView = new TextView(context);

            //affectation des valeurs
            textView.setText(entry.getValue() + '\n' + "Rejoignez l\'aventure");
            textView.setTextColor(context.getResources().getColor(R.color.black));
            textView.setX(20);
            button.setText("Rejoins-nous !");
            button.setTag(entry.getKey());
            button.setX(200);
            button.setBackgroundResource(R.color.black);
            button.getBackground().setAlpha(100);
            button.setTextColor(context.getResources().getColor(R.color.Blancnacre));

            LinearLayout ll = new LinearLayout(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 150);
            ll.addView(textView);
            ll.addView(button,layoutParams);
            ll.setBackgroundResource(R.drawable.coureur2);
            ll.getBackground().setAlpha(150);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)layout.getLayoutParams();
            params.setMargins(30, 0, 35, 0); //substitute parameters for left, top, right, bottom
            layout.setLayoutParams(params);

            listRaidstoJoin.add(button);
            layout.addView(ll);
        }
        parcoursButton(listRaidstoJoin);
    }

    /**
     *
     * @param listButton
     */
    public static void parcoursButton(ArrayList<Button> listButton){

        int j;

        for (j = 0; j<listButton.size(); j++) {
            final Button newButton = listButton.get(j);
            Utils.debug("parcoursButtontest", "idParcours : "+newButton.getTag());
            newButton.setOnClickListener( new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent =  new Intent(context, VolunteerPreferenceActivity.class);
                    String idRaidpourVolunteer = newButton.getTag().toString();
                    intent.putExtra("idRaidpourVolunteer",idRaidpourVolunteer);
                    context.startActivity(intent);
                }
            });
        }

    }

    public static void raidOrgaEmpty(){
        TextView textView = new TextView(context);
        textView.setText(R.string.raid_orga_vide);
        textView.setTextColor(context.getResources().getColor(R.color.black));
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setPadding(0,20,0,0);
        ll.addView(textView);
    }

    public static void raidBeneEmpty(){
        TextView textView = new TextView(context);
        textView.setText(R.string.raid_bene_vide);
        textView.setTextColor(context.getResources().getColor(R.color.black));
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setPadding(0,0,0,100);
        ll2.addView(textView);
    }
}
