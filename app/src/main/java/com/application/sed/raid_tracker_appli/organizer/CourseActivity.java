package com.application.sed.raid_tracker_appli.organizer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.API.ApiRequestPost;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class CourseActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private static final String switch_value = "switch_value";

    private ArrayList<Button> listebouton;
    private ArrayList<List> raidlist;
    //private static ArrayList<String> listsport;

    private static Context context;



    private static LinearLayout ll;
    private static String idRaid;

    //récupération des informations du raid
    private static String id;
    private static String nom;
    private static String lieu;
    private static String date;
    private static String edition;
    private static String equipe;
    private static Boolean visibility;
    private static Switch simpleSwitch;
    private static TextView setTextVisibility;


    MapView map = null;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Utils.info("test", "Creation of the new activity");

        Intent intent=getIntent();
        context = this;

        if (intent != null) {

            Utils.debug("CourseActivity", "je rentre ici");
            idRaid = intent.getStringExtra("idRaid");
            Utils.debug("CourseActivity","idRaid = "+idRaid);
            ll = (LinearLayout) findViewById(R.id.ParcoursLayout);

            /*listsport = intent.getStringArrayListExtra("Sports");
            Utils.debug("Sports",listsport.toString());*/
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(CourseActivity.this, LandingActivity.class);
                    intent.putExtra("idRaid",idRaid);
                    startActivity(intent);

                }
            });



            // bouton switch pour la visiblité du raid
            simpleSwitch = (Switch) findViewById(R.id.switchVisibility);

            //texte associé à la visibilité du raid
           setTextVisibility = (TextView) findViewById(R.id.setTextVisibility);


            ApiRequestGet.getSpecificParcours(context, Bdd.getValue(), idRaid);

            //récupération des informations du raid pour ensuite exploiter la visibilité
            ApiRequestGet.getSpecificRaidforCourseActivity(context,Bdd.getValue(),idRaid,"CourseActivity");
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit_raid) {
            //return true;
            Intent intent = new Intent(CourseActivity.this, EditCourse.class);
            intent.putExtra("idRaid",idRaid);
            startActivity(intent);
        }
        else if (id==R.id.manage_points){
           Intent intent = new Intent(CourseActivity.this, ManageVolunteersPositionActivity.class);
           intent.putExtra("idRaid",idRaid);
           startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    public void Course(View view){
        Intent intent = new Intent(CourseActivity.this, CreateParcours.class);
        intent.putExtra("idRaid",idRaid);
        //intent.putExtra("listsport",listsport);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(CourseActivity.this, LandingActivity.class);
        startActivity(intent);
    }



    public static void afficheParcours(String response){

        ArrayList<Button> listButton;
        listButton = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonArray parcourslist = (JsonArray) parser.parse(response);

        for (int i =0; i<parcourslist.size();i++){

            Button myButton = new Button(context);
            Utils.debug("Ajout du bouton", "Je rentre dans le for "+i);

            JsonObject parcours = (JsonObject) parcourslist.get(i);
            String nomParcours = parcours.get("nom").toString();
            String idParcours = parcours.get("id").toString();
            myButton.setText("Nom :" + nomParcours);

            myButton.setId(i);
            myButton.setTag(idParcours);

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


        parcoursButton(listButton);

    }

    public static void parcoursButton(ArrayList<Button> listButton){

        for (int j = 0; j<listButton.size(); j++) {
            final Button newButton = listButton.get(j);

            newButton.setOnClickListener( new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent =  new Intent(context, ManageParcoursActivity.class);
                    String idParcours= (String) newButton.getTag();
                    Utils.debug("parcoursButton", "idRaid : "+idRaid);
                    //Id du parcours qu'on veut récupérer
                    intent.putExtra("idParcours",idParcours);
                    intent.putExtra("idRaid",idRaid);
                    context.startActivity(intent);

                }
            });
        }

    }

    public static void getRaid(String response){

        JsonParser parser = new JsonParser();
        JsonObject infoRaid = (JsonObject) parser.parse(response);
        id = infoRaid.get("id").toString();
        nom = infoRaid.get("nom").toString().replace("\"", " ");
        Utils.debug("nom",nom);
        lieu=infoRaid.get("lieu").toString().replace("\"", " ");
        String inter = infoRaid.get("date").toString();
        edition=infoRaid.get("edition").toString();
        equipe= infoRaid.get("equipe").toString().replace("\"", " ");
        visibility=infoRaid.get("visibility").getAsBoolean();
        String annee = inter.substring(1,5);
        String mois = inter.substring(6,8);
        String jours = inter.substring(9,11);
        String heure = inter.substring(12,14);
        String min = inter.substring(15,17);


        date = annee+"/"+mois+"/"+jours+" "+heure+":"+min;

            Utils.debug("yvantest",date);

            if (visibility){
                simpleSwitch.setChecked(true);
                setTextVisibility.setText(" Le raid est partagé aux bénévoles ");

            }else {
                simpleSwitch.setChecked(false);
                setTextVisibility.setText(" Le raid n'est pas partagé aux bénévoles");

            }
    }

    public void checkSwitch(View view){
        //modifier le texte le texte en fonction de l'action de l'état actuel du bouton

                //String statusSwitch1, statusSwitch2;
                if (simpleSwitch.isChecked()) {
                    setTextVisibility.setText(" Le raid est partagé aux bénévoles avec switch");

                    //nom = "ENSSAT RAID 100";
                    ApiRequestPost.postUpdateRaid(context,Bdd.getValue(),id,nom,lieu,date,edition,equipe,true);

                    //LandingActivity.diffuserRaid();
                    Intent intent = new Intent(CourseActivity.this, LandingActivity.class);
                    //intent.putExtra(switch_value,"coucou");
                    startActivity(intent);

                } else if (!simpleSwitch.isChecked()){
                    setTextVisibility.setText(" Le raid n'est pas partagé aux bénévoles avec switch");
                    //nom = "ENSSAT RAID 100";
                    ApiRequestPost.postUpdateRaid(context,Bdd.getValue(),id,nom,lieu,date,edition,equipe,false);
                    Intent intent = new Intent(CourseActivity.this, LandingActivity.class);
                    startActivity(intent);

                }
            }

}


