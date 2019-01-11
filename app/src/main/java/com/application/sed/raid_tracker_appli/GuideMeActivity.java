package com.application.sed.raid_tracker_appli;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
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
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.UTFDataFormatException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GuideMeActivity extends AppCompatActivity {

    private static Context context;
    static MapView map = null;
    Toolbar toolbar1;
    Marker standardmarker;
    private static Road road;
    private static MyLocationNewOverlay mLocationOverlay;
    Boolean gps_enabled=false;
    GeoPoint centermap;
    Integer checkstartlocalisation=0;
    LocationManager locationManager = null;
    private String fournisseur;
    private int etat;
    private static ArrayList<GeoPoint> trajet;
    Location localisation;
    Integer checkthing=0;
    Polyline roadOverlay;


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

        //popup pour s'assurer que l'utilisateur souhaite bien quitter la page
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //actions sur le bouton de retour
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    alert.setTitle("Quitter la navigation ?");

                    //indique que la popup ne peut pas disparaître si on appuie en dehors de la popup
                    alert.setCancelable(false);

                    //si validation arrête de la localisation
                    alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(GuideMeActivity.this, PosteDescription.class);
                            startActivity(intent);
                            mLocationOverlay.disableMyLocation();
                        }
                    });
                    //retour à la navigation
                    alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }
        });

        //


        if (checkstartlocalisation ==1){
            Handler myHandler = new Handler();
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            },1000);
        }


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
        centermap = new GeoPoint(48.732084, -3.4591440000000375);
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
     *
     */
    public void startNavigation() {

        //https://github.com/MKergall/osmbonuspack/wiki/Tutorial_1

        checkstartlocalisation = 1;

         ImageButton guideme = (ImageButton) findViewById(R.id.startnavigation);
         guideme.setBackgroundColor(Color.rgb(209, 196, 190));

        gps_enabled = mLocationOverlay.isMyLocationEnabled();
        Utils.debug("gpsenabled",gps_enabled.toString());


        // à changer par le point du poste
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
        //GeoPoint position = new GeoPoint(standardmarker.getPosition().getLatitude(),standardmarker.getPosition().getLongitude());

//        double lat = (int) (localisation.getLatitude() * 1E6);
//        double lng = (int) (localisation.getLongitude() * 1E6);
//        GeoPoint position = new GeoPoint(lat, lng);


        GeoPoint position = new GeoPoint(mLocationOverlay.getMyLocation().getLatitude(),mLocationOverlay.getMyLocation().getLongitude());

        waypoints.add(tmpgeo3);
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


//            if (checkthing==0){
//                checkthing=1;

                map.getOverlays().remove(roadOverlay);
                RoadManager roadManager = new MapQuestRoadManager("o7gFRAppOrsTtcBhEVYrY6L7AGRtXldE");
                roadManager.addRequestOption("routeType=pedestrian");
                road = roadManager.getRoad(waypoints);

                roadOverlay = RoadManager.buildRoadOverlay(road);



//            }
//            else if (!checkthing.equals(0)){
//                map.getOverlays().clear();
//
//            }



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
                nodeMarker.setSnippet(node.mInstructions);

                Utils.debug("instruct",node.mInstructions);
                Utils.debug("instruct2",node.mLocation.toString());

              //  Utils.debug("instruct3");

                nodeMarker.setSubDescription(Road.getLengthDurationText(context, node.mLength, node.mDuration));
                nodeMarker.setIcon(nodeIcon);
                nodeMarker.setTitle("Step "+i);
                Drawable icon = getResources().getDrawable(R.drawable.ic_continue);
                nodeMarker.setImage(icon);
                map.getOverlays().add(nodeMarker);
            }


            map.getOverlays().add(line);
            map.invalidate();

//
            //MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(getApplicationContext(), );
            // map.getOverlays().add(0, mapEventsOverlay);
        }

    }

    public void initialiserLocalisation(View view)
    {
        if (locationManager == null)
        {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteres = new Criteria();

            // la précision  : (ACCURACY_FINE pour une haute précision ou ACCURACY_COARSE pour une moins bonne précision)
            criteres.setAccuracy(Criteria.ACCURACY_FINE);

            // l'altitude
            criteres.setAltitudeRequired(true);

            // la direction
            criteres.setBearingRequired(true);

            // la vitesse
            criteres.setSpeedRequired(true);

            // la consommation d'énergie demandée
            criteres.setCostAllowed(true);
            //criteres.setPowerRequirement(Criteria.POWER_HIGH);
            criteres.setPowerRequirement(Criteria.POWER_MEDIUM);

            fournisseur = locationManager.getBestProvider(criteres, true);
            Log.d("GPS", "fournisseur : " + fournisseur);
        }

        if (fournisseur != null)
        {
            // dernière position connue
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                Log.d("GPS", "no permissions !");
                return;
            }

            Location localisation = locationManager.getLastKnownLocation(fournisseur);
            if(localisation != null)
            {
                ImageButton guideme = (ImageButton) findViewById(R.id.startnavigation);
                guideme.setBackgroundColor(Color.rgb(209, 196, 190));


                // on notifie la localisation
//                ecouteurGPS.onLocationChanged(localisation);
            }

            // on configure la mise à jour automatique : au moins 10 mètres et 15 secondes
            locationManager.requestLocationUpdates(fournisseur, 15000, 10, ecouteurGPS);
        }
    }




    //partie calibration //
    LocationListener ecouteurGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location localisation)
        {
            Toast.makeText(getApplicationContext(), fournisseur + " localisation", Toast.LENGTH_SHORT).show();

            Log.d("GPS", "localisation : " + localisation.toString());
            String coordonnees = String.format("Latitude : %f - Longitude : %f\n", localisation.getLatitude(), localisation.getLongitude());
            Log.d("GPS", coordonnees);
            String autres = String.format("Vitesse : %f - Altitude : %f - Cap : %f\n", localisation.getSpeed(), localisation.getAltitude(), localisation.getBearing());
            Log.d("GPS", autres);
            //String timestamp = String.format("Timestamp : %d\n", localisation.getTime());
            //Log.d("GPS", "timestamp : " + timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date(localisation.getTime());
            Log.d("GPS", sdf.format(date));

            String strLatitude = String.format("Latitude : %f", localisation.getLatitude());
            String strLongitude = String.format("Longitude : %f", localisation.getLongitude());
//            latitude.setText(strLatitude);
            //    longitude.setText(strLongitude);

           map.getController().setCenter(new GeoPoint(localisation.getLatitude(), localisation.getLongitude()));
           map.getController().setZoom(20.0);
//
//            //myOpenMapView.setMapOrientation(localisation.getBearing());
//
//            trajet = new ArrayList<GeoPoint>();
//
//            trajet.add(new GeoPoint(localisation.getLatitude(), localisation.getLongitude()));
//
//
//            // Un tracé à base de lignes rouges
//            Polyline line = new Polyline();
//            line.setTitle("Un trajet");
//            line.setSubDescription(Polyline.class.getCanonicalName());
//            line.setWidth(10f);
//            line.setColor(Color.RED);
//            line.setPoints(trajet);
//            line.setGeodesic(true);
//            line.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, map));
//            map.getOverlayManager().add(line);
//
//
//            map.invalidate();

            startNavigation();

        }

        @Override
        public void onProviderDisabled(String fournisseur)
        {
            Toast.makeText(getApplicationContext(), fournisseur + " désactivé !", Toast.LENGTH_SHORT).show();
        }


        @Override
        public void onProviderEnabled(String fournisseur)
        {
            Toast.makeText(getApplicationContext(), fournisseur + " activé !", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String fournisseur, int status, Bundle extras)
        {
            if (etat != status)
            {
                switch (status)
                {
                    case LocationProvider.AVAILABLE:
                        Toast.makeText(getApplicationContext(), fournisseur + " état disponible", Toast.LENGTH_SHORT).show();
                        break;
                    case LocationProvider.OUT_OF_SERVICE:
                        Toast.makeText(getApplicationContext(), fournisseur + " état indisponible", Toast.LENGTH_SHORT).show();
                        break;
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        Toast.makeText(getApplicationContext(), fournisseur + " état temporairement indisponible", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), fournisseur + " état : " + status, Toast.LENGTH_SHORT).show();
                }
            }
            etat = status;
        }
    };

}
