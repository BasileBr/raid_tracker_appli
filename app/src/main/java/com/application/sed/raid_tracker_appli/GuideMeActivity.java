package com.application.sed.raid_tracker_appli;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.application.sed.raid_tracker_appli.organizer.CourseActivity;
import com.application.sed.raid_tracker_appli.organizer.CreateParcours;
import com.application.sed.raid_tracker_appli.organizer.ManageParcoursActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

public class GuideMeActivity extends AppCompatActivity {

    private static Context context;
    static MapView map = null;
    Toolbar toolbar1;
    Marker standardmarker;
    private static Road road;
    private static MyLocationNewOverlay mLocationOverlay;
    Boolean gps_enabled=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guideme);

        //récuperation du context
        context = this;

        //création de la map
        createMap();

        //géolocaliser l'utilisateur
        geolocateMe();

        toolbar1 = findViewById(R.id.toolbarguid);

        // on définit la toolbar
        setSupportActionBar(toolbar1);

        //afficher le poste de l'utilisateur
        displayPoste();

        //ajouter un bouton retour dans l'action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        final AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //actions sur le bouton de retour
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    alert.setTitle("Quitter la navigation ?");

                    //indique que la popup ne peut pas disparaître si on appuie en dehors de la popup
                    alert.setCancelable(false);


                    alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mLocationOverlay.disableMyLocation();
                            Intent intent = new Intent(GuideMeActivity.this, PosteDescription.class);
                            startActivity(intent);
                        }
                    });

                    alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }
        });

    }


    /**
     ** affichage de la map
     */
    public void createMap() {
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        //positionnement lors de l'ouverture de la carte
        IMapController mapController = map.getController();
        mapController.setZoom(9.0);
        GeoPoint centermap = new GeoPoint(48.732084, -3.4591440000000375);
        mapController.setCenter(centermap);

        // ajouter l'echelle
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(map);
        map.getOverlays().add(myScaleBarOverlay);

        // ajouter boussolle
        CompassOverlay mCompassOverlay = new CompassOverlay(getApplicationContext(), new InternalCompassOrientationProvider(getApplicationContext()), map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(mCompassOverlay);
    }

    /**
     ** Géolocalisation de l'utilsateur
     */
    public void geolocateMe() {
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), map);
        mLocationOverlay.enableMyLocation();
        map.getOverlays().add(mLocationOverlay);
    }


    /**
     ** Afficher le poste selectionné par l'utilisateur sur la carte
     */
    public void displayPoste() {
        GeoPoint tmpgeo = new GeoPoint(48.729673, -3.4624261999999817);
        standardmarker = new Marker(map);
        standardmarker.setIcon(getResources().getDrawable(R.drawable.poi1));
        standardmarker.setPosition(tmpgeo);
        standardmarker.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(standardmarker);
        map.invalidate();
    }

    /**
     * Démarrer la navigation jusqu'au poste selectionné par l'utilisateur
     * @param view
     */
    public void startNavigation(View view) {

        //https://github.com/MKergall/osmbonuspack/wiki/Tutorial_1

         ImageButton guideme = (ImageButton) findViewById(R.id.startnavigation);
         guideme.setBackgroundColor(Color.rgb(209, 196, 190));

        gps_enabled = mLocationOverlay.isMyLocationEnabled();
        Utils.debug("gpsenabled",gps_enabled.toString());



        GeoPoint tmpgeo2 = new GeoPoint(48.815456, -3.4449429999999666);
        GeoPoint tmpgeo3 = new GeoPoint(49.1, -0.3);

//        //Boolean isGPSEnabled = mLocationOverlay.isMyLocationEnabled();
//
//        /**
//         * à utiliser si besoin de tracer un parcours en passant par la route
//         */
//        //créer un road manager (Appel vers l'api pour guider d'un point à un autre
////        RoadManager roadManager = new MapQuestRoadManager("o7gFRAppOrsTtcBhEVYrY6L7AGRtXldE");
//        RoadManager roadManager = new OSRMRoadManager(this);
//        //fixer le point de départ et le point d'arrivée
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
//        waypoints.add(tmpgeo2);
        GeoPoint position = new GeoPoint(standardmarker.getPosition().getLatitude(),standardmarker.getPosition().getLongitude());

        waypoints.add(position);
//
        for (int i=0;i<waypoints.size();i++){
           Utils.debug("values",waypoints.get(i).toString());
        }
//
////        //choisir le type de route
//        //roadManager.addRequestOption("routeType=pedestrian");
//        Road road = roadManager.getRoad(waypoints);
////
////        //créer les lignes
//        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
//        map.getOverlays().add(roadOverlay);
////
////        // permet de mettre à jour la carte
//        map.invalidate();

        new PerfomCalculations2().execute(position, tmpgeo3);


    }


    private class PerfomCalculations2 extends AsyncTask<GeoPoint, Void, Polyline> {
        @Override
        protected Polyline doInBackground(GeoPoint[] params) {
            ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
            waypoints.add(params[0]); //POINT A
            waypoints.add(params[1]); // point b


            RoadManager roadManager = new MapQuestRoadManager("o7gFRAppOrsTtcBhEVYrY6L7AGRtXldE");
            roadManager.addRequestOption("routeType=pedestrian");
            road = roadManager.getRoad(waypoints);

            Polyline roadOverlay = RoadManager.buildRoadOverlay(road);

//            map.getOverlays().add(roadOverlay);
//             map.invalidate();

//            Polyline line = new Polyline(map);
//            line.addPoint(params[0]);
//            line.addPoint(params[1]);
//            line.setWidth(3);

            //RoadManager roadManager = new OSRMRoadManager(getApplicationContext()); // your context
            //Road road = roadManager.getRoad(waypoints);

            return roadOverlay;
//            } catch (Exception e) {
//                return -1;
//         }
        }


        /*
         affiche la ligne entre deux points
         */
        protected void onPostExecute(Polyline line) {
////
            //Polyline roadOverlay = RoadManager.buildRoadOverlay(road);


            Drawable nodeIcon = getResources().getDrawable(R.drawable.passage);
            for (int i=0; i<road.mNodes.size(); i++){
                RoadNode node = road.mNodes.get(i);
              //  node.mManeuverType=maneuver
                Marker nodeMarker = new Marker(map);
                nodeMarker.setPosition(node.mLocation);
                nodeMarker.setIcon(nodeIcon);
                nodeMarker.setTitle("Step "+i);
                map.getOverlays().add(nodeMarker);
            }

            map.getOverlays().add(line);
            map.invalidate();

//
            //MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(getApplicationContext(), );
            // map.getOverlays().add(0, mapEventsOverlay);
        }

    }
}
