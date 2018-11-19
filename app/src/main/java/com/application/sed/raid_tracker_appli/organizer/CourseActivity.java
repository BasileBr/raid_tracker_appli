package com.application.sed.raid_tracker_appli.organizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.ManageParcoursActivity;
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
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.security.KeyManagementException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

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
                    startActivity(intent);

                }
            });


            //création de la map
          /*  map = (MapView) findViewById(R.id.map);
            map.setTileSource(TileSourceFactory.MAPNIK);
            map.setBuiltInZoomControls(true);
            map.setMultiTouchControls(true);

            //positionnement lors de l'ouverture de la carte
            IMapController mapController = map.getController();
            mapController.setZoom(9.0);
            GeoPoint centermap = new GeoPoint(48.732084, -3.4591440000000375);
            mapController.setCenter(centermap);

            //géolocaliser l'appareil
            MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), map);
            mLocationOverlay.enableMyLocation();
            map.getOverlays().add(mLocationOverlay);

            // ajouter l'echelle
            ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(map);
            map.getOverlays().add(myScaleBarOverlay);

            // ajouter boussolle
            CompassOverlay mCompassOverlay = new CompassOverlay(getApplicationContext(), new InternalCompassOrientationProvider(getApplicationContext()), map);
            mCompassOverlay.enableCompass();
            map.getOverlays().add(mCompassOverlay);
    */

            // bouton switch pour la visiblité du raid
            final Switch simpleSwitch = (Switch) findViewById(R.id.switchVisibility);

            //texte associé à la visibilité du raid
            final TextView setTextVisibility = (TextView) findViewById(R.id.setTextVisibility);

            //final String switch_value="coucou";

            //message lorsque rien n'est sélectionné
            setTextVisibility.setText(" Le raid n'est pas partagé aux bénévoles");

            //modifier le texte le texte en fonction de l'action de l'état actuel du bouton
            simpleSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //String statusSwitch1, statusSwitch2;
                    if (simpleSwitch.isChecked()) {
                        setTextVisibility.setText(" Le raid est partagé aux bénévoles ");
                        //LandingActivity.diffuserRaid();
                        Intent intent = new Intent(CourseActivity.this, LandingActivity.class);
                        //intent.putExtra(switch_value,"coucou");
                        startActivity(intent);

                    } else {
                        setTextVisibility.setText(" Le raid n'est pas partagé aux bénévoles");
                    }
                }
            });


            // garder la position du switch
            //boolean value = false; // default value if no value was found
            final SharedPreferences sharedPreferences = getSharedPreferences("isChecked", 0);
            //value = sharedPreferences.getBoolean("isChecked",value); // retrieve the value of your key
            //simpleSwitch.setChecked(value);

            simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        sharedPreferences.edit().putBoolean("isChecked", true).apply();
                        setTextVisibility.setText("Le raid est partagé aux bénévoles");
                    } else {
                        sharedPreferences.edit().putBoolean("isChecked", false).apply();
                        setTextVisibility.setText("Le raid n'est pas partagé aux bénévoles");
                    }
                }
            });

            ApiRequestGet.getSpecificParcours(context, Bdd.getValue(), idRaid);
        }







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
                    String idParcours = (String) newButton.getTag();
                    Utils.debug("parcoursButton", "idParcours : "+idParcours);
                    //Id du parcours qu'on veut récupérer
                    //intent.putExtra("idRaid",idRaid);
                    context.startActivity(intent);

                }
            });
        }

    }


}
