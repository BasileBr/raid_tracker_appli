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

import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Utils;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private static final String switch_value = "switch_value";

    MapView map = null;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Utils.info("test", "Creation of the new activity");



        toolbar =(Toolbar) findViewById(R.id.toolbar);
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
        final TextView setTextVisibility =(TextView)findViewById(R.id.setTextVisibility);

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
                    Intent intent =  new Intent(CourseActivity.this, LandingActivity.class);
                    intent.putExtra(switch_value,"coucou");
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
                if(isChecked){
                    sharedPreferences.edit().putBoolean("isChecked", true).apply();
                    setTextVisibility.setText("Le raid est partagé aux bénévoles");
                }else {
                    sharedPreferences.edit().putBoolean("isChecked", false).apply();
                    setTextVisibility.setText("Le raid n'est pas partagé aux bénévoles");
                }
            }
        });



        LinearLayout ll = findViewById(R.id.ParcoursLayout);
        ArrayList<Button> listebouton = new ArrayList<>();
        listebouton = Bdd.getButton();
        for (int i =0; i<Bdd.getButton().size();i++){

            Button button;
            button = listebouton.get(i);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(button, lp);
        }

        for (int j = 0; j<listebouton.size(); j++) {
            Button newButton = listebouton.get(j);

            newButton.setOnClickListener( new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent =  new Intent(CourseActivity.this, CreateCourse.class);
                    startActivity(intent);

                }
            });
        }

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(CourseActivity.this, LandingActivity.class);
        startActivity(intent);
    }

    public void Course(View view){

        Button mybutton = new Button(this);
        mybutton.setId(Bdd.getButton().size()+1);
        mybutton.setText("Ceci est mon parcours " + Bdd.getButton().size()+1);
        Bdd.addButton(mybutton);
        Intent intent2= new Intent(CourseActivity.this, CreateParcours.class);
        startActivity(intent2);

    }
//    /* Méthodes pour le Drawer */
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        // synchroniser le drawerToggle après la restauration via onRestoreInstanceState
//        drawerToggle.syncState();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        drawerToggle.onConfigurationChanged(newConfig);
//    }
}
