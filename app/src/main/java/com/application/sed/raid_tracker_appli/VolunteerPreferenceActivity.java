package com.application.sed.raid_tracker_appli;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.application.sed.raid_tracker_appli.organizer.CourseActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class VolunteerPreferenceActivity extends AppCompatActivity implements OnItemSelectedListener{
    MapView map = null;
    private static Context context;
    private Toolbar toolbar;
    private static Spinner spinner;
    private static List<String> posteRaid;
    private String getselectedposte;

    private String idraid;
    private String token;
    private static TextView dispMission;

    private static Button submit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_preference);

        Intent intent=getIntent();
        context = this;

        if (intent != null) {

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(VolunteerPreferenceActivity.this, LandingActivity.class);
                    startActivity(intent);

                }
            });
        }

        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        //création de la map
        map = (MapView) findViewById(R.id.map);
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

        //récupérer le TextView pour afficher la mission
        dispMission= (TextView) findViewById(R.id.displayMissions);

        //récupération du bouton pour inscrire un bénévole à un poste
        submit=(Button) findViewById(R.id.submit);



        token = Bdd.getValue();
        idraid= intent.getStringExtra("idRaid");

        //récupération des postes à partir de l'id d'un Raid
        ApiRequestGet.getAllPostesfromOneRaid(context, token, idraid);


    }

    //méthode pour récupérer l'élement selectionné dans la liste des postes
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Object item = parent.getItemAtPosition(position);
        getselectedposte=item.toString();

        //lors de la selection d'un poste, on affiche la mission associée
        ApiRequestGet.getMissionsofOnePoste(context,token,getselectedposte);

    }
    //si aucun élément n'est selectionné, là par defaut premier raid de la liste
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    //afficher la liste des postes
    public static void createSpinner(List<String> posteListe){
        // Spinner Drop down elements
        //List<String> raidliste = new ArrayList<String>();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, posteListe);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    //récupérer la liste des postes d'un raid
    public static void posteListe(String response){

        JsonParser parser = new JsonParser();
        JsonArray posteliste = (JsonArray) parser.parse(response);
        //Utils.debug(" + size", "size : " + posteliste.size() + " raidlist : "+posteliste.toString());
        posteRaid = new ArrayList<>();

        for (int i = 0; i < posteliste.size(); i ++) {

            JsonParser parser1 = new JsonParser();
            JsonObject raid = (JsonObject) posteliste.get(i);
            //String posteraid = raid.get("nom").toString().replace("\""," ");

            String id_point = raid.get("idPoint").toString();
            posteRaid.add(id_point);

        }

        createSpinner(posteRaid);

    }


    //afficher la mission associée au poste selectionné
    public static void getMission(String response){

        JsonParser parser = new JsonParser();
        JsonArray mission = (JsonArray) parser.parse(response);
        //Utils.debug(" + size", "size : " + posteliste.size() + " raidlist : "+posteliste.toString());
       // posteRaid = new ArrayList<>();

       String missionassocie= mission.get(0).toString();

       Utils.debug("affichage_mission",missionassocie);

       dispMission.setText(missionassocie);
    }

    public static void  validerPreference (){


    }



}
