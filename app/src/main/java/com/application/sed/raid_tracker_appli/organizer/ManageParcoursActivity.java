package com.application.sed.raid_tracker_appli.organizer;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.API.ApiRequestPost;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
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

import java.util.ArrayList;
import java.util.List;

public class ManageParcoursActivity extends AppCompatActivity {

    private static String TAG = "ManageParcoursActivity";
    Toolbar toolbar1;

    ImageButton fincalibration;
    ImageButton startcalibration;

    MapView map = null;
    public static MapView map2 = null;
    public static Marker standardmarker;
    public static Marker standardmarker1;
    public static Marker standardmarker2;
    public static Marker standarmarker3;

    public static GeoPoint pointa = new GeoPoint(51.489878, 6.143294);
    public static GeoPoint pointb = new GeoPoint(51.488978, 6.746994);
    public static GeoPoint geotemporaire;

    public static int ParcoursListGeoPoint;
    public static int compteur = 0;

    public static Context context;
    public static ArrayList<GeoPoint> parcours;

    private static String idParcours;

    private String idRaid;

    //partie calibration //
    LocationManager locationManager = null;
    private int etat;
    private String fournisseur;
    private Integer checkEndLocation=0;
    private Integer checkstartcalibration=0;

    MyLocationNewOverlay mLocationOverlay;
    private static ArrayList<GeoPoint> trajet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.info(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_parcours);

        context = this;

        Intent intent = getIntent();
        //récupération de l'id de la toolbar
        toolbar1 = findViewById(R.id.toolbar);

        fincalibration= findViewById(R.id.fincalibration);
        startcalibration= findViewById(R.id.calibrationstart);

        fincalibration.setEnabled(false);

        // on définit la toolbar dans notre activity
        setSupportActionBar(toolbar1);

        //on ajoute un bouton retour dans l'action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //charger/initialiser la configuration osmdroid
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
//        final MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), map);
//        mLocationOverlay.enableMyLocation();
//        map.getOverlays().add(mLocationOverlay);

        // ajouter l'echelle
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(map);
        map.getOverlays().add(myScaleBarOverlay);

        // ajouter boussole
        CompassOverlay mCompassOverlay = new CompassOverlay(getApplicationContext(), new InternalCompassOrientationProvider(getApplicationContext()), map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(mCompassOverlay);

        //partie calibration
        trajet = new ArrayList<>();
        map2 = map;

        if( intent != null) {

            idParcours = intent.getStringExtra("idParcours");
            idRaid = intent.getStringExtra("idRaid");
            ApiRequestGet.getSpecificTraceFromParcours(ctx,Bdd.getValue(),idParcours,"ManageParcoursActivity");
            ApiRequestGet.getAllPostesfromSpecParcours(ctx,Bdd.getValue(),idParcours);

            final AlertDialog.Builder alert = new AlertDialog.Builder(context);

            //si on appuie sur le bouton retour, on arrive sur la page X
            toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checkEndLocation==0 && checkstartcalibration ==0){
                        alert.setTitle("Quitter la page actuelle ?");
                        //indique que la popup ne peut pas disparaître si on appuie en dehors de la popup
                        alert.setCancelable(false);
                        alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //mLocationOverlay.disableMyLocation();
                                //arreterLocalisation();
                                //mLocationOverlay.disableMyLocation();
                                Intent intent = new Intent(ManageParcoursActivity.this, CourseActivity.class);
                                intent.putExtra("idRaid",idRaid);
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

                    else if (checkstartcalibration ==1){
                        alert.setTitle("Quitter la page actuelle ?");
                        //indique que la popup ne peut pas disparaître si on appuie en dehors de la popup
                        checkstartcalibration=0;
                        alert.setCancelable(false);
                        alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //mLocationOverlay.disableMyLocation();
                                 arreterLocalisation();
                                 mLocationOverlay.disableMyLocation();
                                Intent intent = new Intent(ManageParcoursActivity.this, CourseActivity.class);
                                intent.putExtra("idRaid",idRaid);
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
                    else {

                        checkstartcalibration=0;
                        alert.setTitle("Quitter la page actuelle ?");
                        //indique que la popup ne peut pas disparaître si on appuie en dehors de la popup
                        alert.setCancelable(false);
                        alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mLocationOverlay.disableMyLocation();

                                //arreterLocalisation();
                                Intent intent = new Intent(ManageParcoursActivity.this, CourseActivity.class);
                                intent.putExtra("idRaid",idRaid);
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
                }


            });
        }
    }

    /**
     *
     * @param view
     */
    public void FinCalibration(View view){
        Toast.makeText( context, "fin de la calibration", Toast.LENGTH_LONG).show();
        checkEndLocation=1;
        checkstartcalibration=0;
        arreterLocalisation();
        mLocationOverlay.disableMyLocation();
        ApiRequestPost.postTrace(context,Bdd.getValue(),idParcours,"true");
        fincalibration.setEnabled(false);
        startcalibration.setEnabled(false);
        startcalibration.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
    }

    public static void recupTrace(String response){
        JsonArray jsonArray;
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject;
        jsonArray = (JsonArray) jsonParser.parse(response);

        for (int i = 0; i < jsonArray.size(); i++){
            jsonObject = (JsonObject) jsonArray.get(i);
            String idTrace = jsonObject.get("id").toString();

            ApiRequestGet.getPointsfromSpecificTrace(context, Bdd.getValue(), idTrace,"ManageParcoursActivity");
        }
    }

    /**
     *
     * @param response
     */
    public static void recupParcours(String response){

        ArrayList<GeoPoint> listFinale = new ArrayList<>();
        ArrayList<GeoPoint> listFinalePoste = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonArray listPoints = (JsonArray) parser.parse(response);
        GeoPoint listInter[] = new GeoPoint[listPoints.size()+3];
        GeoPoint listPoste[] = new GeoPoint[listPoints.size()+3];

        for (int k = 0; k < listPoints.size() ; k++) {
            GeoPoint newPoint;
            JsonObject myPoint = (JsonObject) listPoints.get(k);
            JsonObject jsonObject = myPoint.getAsJsonObject("idTrace");

            JsonElement lat = myPoint.get("lat");
            JsonElement lon = myPoint.get("lon");
            JsonElement ord = myPoint.get("ordre");
            JsonElement type = myPoint.get("type");

            Double longitude = lon.getAsDouble();
            Double latitude = lat.getAsDouble();
            int ordre = ord.getAsInt();
            int typePoint = type.getAsInt();
            if ( typePoint == 3){
                newPoint = new GeoPoint(latitude, longitude);
                listPoste[ordre]= newPoint;
            }
            else {

                newPoint = new GeoPoint(latitude, longitude);
                listInter[ordre] = newPoint;
            }
        }

        for (int k = 0;  k< listInter.length; k++){
            if (listInter[k] == null){
                //Nothing to do
            }
            else {
                listFinale.add(listInter[k]);
            }
        }
        for (int k = 0; k< listPoste.length; k++){
            if (listPoste[k] == null){
                //Nothing to do
            }
            else {
                listFinalePoste.add(listPoste[k]);
            }
        }

        trace(listFinale);
        tracePoste(listFinalePoste);
    }

    /**
     *
     * @param myListe
     */
    public static void trace(ArrayList<GeoPoint> myListe){
        ArrayList<GeoPoint> finaleListGeoPoint = new ArrayList<>();
        ParcoursListGeoPoint = 0;
        parcours = new ArrayList<>();
        compteur = 0;
        for (int i = 0; i < myListe.size(); i ++) {
            GeoPoint myPoint = myListe.get(i);
            if (i == 0) {
                // Point de départ
                standardmarker = new Marker(map2);
                standardmarker.setIcon(context.getResources().getDrawable(R.drawable.green_flag2));
                standardmarker.setPosition(myPoint);
                standardmarker.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                standardmarker.setTitle("Point de départ" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());
                map2.getOverlays().add(standardmarker);
                finaleListGeoPoint.add(myPoint);
                map2.invalidate();
                setRoad(finaleListGeoPoint);
            }
            else if (i == myListe.size() -1){
                // Point d'arrivée
                standardmarker1 = new Marker(map2);
                standardmarker1.setIcon(context.getResources().getDrawable(R.drawable.red_flag2));
                standardmarker1.setPosition(myPoint);
                standardmarker1.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                standardmarker1.setTitle("Point d'arrivée" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());
                map2.getOverlays().add(standardmarker1);
                finaleListGeoPoint.add(myPoint);
                map2.invalidate();
                setRoad(finaleListGeoPoint);
            }

            else {
                // Point de Passage
                standardmarker2 = new Marker(map2);
                standardmarker2.setIcon(context.getResources().getDrawable(R.drawable.passage23));
                standardmarker2.setPosition(myPoint);
                standardmarker2.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                standardmarker2.setTitle("Point de passage" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());
                map2.getOverlays().add(standardmarker2);
                finaleListGeoPoint.add(myPoint);
                map2.invalidate();
                setRoad(finaleListGeoPoint);
            }
        }
    }

    /**
     *
     * @param listGeoPoint
     */
    public static void setRoad(ArrayList<GeoPoint> listGeoPoint) {
        //parcours l'arraylist contenant tous les geopoints lors d'un appui long
        for (int i = ParcoursListGeoPoint; i < listGeoPoint.size(); i++) {
            // on ajoute le premier point dans l'arraylist (parcours) de deux pts max
            if (compteur == 0) {
                parcours.add(listGeoPoint.get(i));
                pointa = parcours.get(0);
                compteur += 1;
                ParcoursListGeoPoint += 1;
            }

            //on ajoute le deuxième arraylist (parcours) puis on envoi la tache de fond à perfomCalculations
            else if (compteur == 1) {
                parcours.add(listGeoPoint.get(i));
                compteur += 1;
                pointb = parcours.get(1);

                GeoPoint[] toto = new GeoPoint[2];
                toto[0] = pointa;
                toto[1] = pointb;
                ParcoursListGeoPoint += 1;
                new PerfomCalculations().execute(pointa, pointb);
            }

            // on écrase la prremiere valeur de l'arraylist et on postionne le nouveau point
            else if (compteur == 2) {
                //recupere le deuxieme point dans parcours
                geotemporaire = parcours.get(1);
                //on l'ajoute en écrasant l'indice 0
                parcours.add(0, geotemporaire);
                parcours.add(1, listGeoPoint.get(i));

                //balance la tache de fond
                ParcoursListGeoPoint += 1;
                new PerfomCalculations().execute(geotemporaire, parcours.get(1));
            }
        }
    }

    /**
     *
     * @param myListe
     */
    public static void tracePoste(ArrayList<GeoPoint> myListe){

        for (int i = 0; i < myListe.size(); i ++) {
            GeoPoint myPoint = myListe.get(i);
            //Poste
            standarmarker3 = new Marker(map2);
            standarmarker3.setIcon(context.getResources().getDrawable(R.drawable.poi1));
            standarmarker3.setPosition(myPoint);
            standarmarker3.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
            standarmarker3.setTitle("Poste" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());
            map2.getOverlays().add(standarmarker3);
            map2.invalidate();
        }
    }

    /**
     *
     */
    private static class PerfomCalculations extends AsyncTask<GeoPoint,Void,Polyline> {
        @Override
        protected Polyline doInBackground(GeoPoint[] params) {
            Polyline line = new Polyline(map2);
            line .addPoint(params[0]);
            line .addPoint(params[1]);
            line.setWidth(3);
            return line;
        }

        /**
         * affiche la ligne entre deux points
         */
        protected void onPostExecute(Polyline line) {
            map2.getOverlays().add(line);
            map2.invalidate();
        }
    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        arreterLocalisation();
    }

    private void initialiserLocalisation()
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
        }
        if (fournisseur != null)
        {
            // dernière position connue
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }

            Location localisation = locationManager.getLastKnownLocation(fournisseur);
            if(localisation != null)
            {
                //Nothing to do
            }

            // on configure la mise à jour automatique : au moins 10 mètres et 15 secondes
            locationManager.requestLocationUpdates(fournisseur, 15000, 10, ecouteurGPS);
        }
    }

    private void arreterLocalisation()
    {
        if(locationManager != null)
        {
            locationManager.removeUpdates(ecouteurGPS);
            ecouteurGPS = null;
        }
    }


    public void startcalibration(View view){
        checkstartcalibration =1;
        initialiserLocalisation();
        Toast.makeText(getApplicationContext(), "Localisation en cours...", Toast.LENGTH_SHORT).show();
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), map);
        mLocationOverlay.enableMyLocation();
        map.setMultiTouchControls(true);
        map.getOverlays().add(mLocationOverlay);
        fincalibration.setEnabled(true);
        startcalibration.setBackgroundColor(Color.rgb(209, 196, 190));
    }

    //partie calibration //
    LocationListener ecouteurGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location localisation)
        {
            Toast.makeText(getApplicationContext(), " localisation "+fournisseur  , Toast.LENGTH_SHORT).show();
            map.getController().setCenter(new GeoPoint(localisation.getLatitude(), localisation.getLongitude()));
            trajet.add(new GeoPoint(localisation.getLatitude(), localisation.getLongitude()));

            // Un tracé à base de lignes rouges
            Polyline line = new Polyline();
            line.setTitle("Un trajet");
            line.setSubDescription(Polyline.class.getCanonicalName());
            line.setWidth(10f);
            line.setColor(Color.RED);
            line.setPoints(trajet);
            line.setGeodesic(true);
            line.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, map));
            map.getOverlayManager().add(line);
            map.invalidate();
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

    /**
     *
     * @param idTrace
     */
    public static void recupIdTrace(String idTrace){
        for (int i=0;i<trajet.size();i++){

            Double lon = trajet.get(i).getLongitude();
            Double lat = trajet.get(i).getLatitude();
            if (i == 0){
                //Point départ type = 1
                ApiRequestPost.postPoint(context,Bdd.getValue(),idTrace,lon,lat,1, i, null);
            }
            else if ( trajet.size() - i ==1){
                //Point arrivée type = 2
                ApiRequestPost.postPoint(context,Bdd.getValue(),idTrace,lon,lat,2, i, null);
            }
            else {
                //Point passage type = 0
                ApiRequestPost.postPoint(context,Bdd.getValue(),idTrace,lon,lat,0, i, null);
            }
        }
    }
    //fin partie calibration //


    public static void posteListe(String response){

        JsonParser parser = new JsonParser();
        JsonArray posteliste = (JsonArray) parser.parse(response);
        List<String> posteRaid = new ArrayList<>();

        //parcours la liste avec le Json
        for (int i = 0; i < posteliste.size(); i ++) {

            Utils.debug(TAG,"posteListe");
            JsonObject raid = (JsonObject) posteliste.get(i);

            //récupération de l'id de point d'un poste
            JsonObject deuxiem=raid.getAsJsonObject("idPoint");

            String type = raid.get("type").toString().replace("\"", " ");;
            Integer ListIdPoste2= raid.get("id").getAsInt();
            Double longitudePoste = deuxiem.get("lon").getAsDouble();
            Double latitudePoste = deuxiem.get("lat").getAsDouble();

            GeoPoint geoposte = new GeoPoint(latitudePoste,longitudePoste);

            Marker markerPoste = new Marker(map2);
            markerPoste.setIcon(context.getResources().getDrawable(R.drawable.poi1));
            markerPoste.setPosition(geoposte);
            markerPoste.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
            markerPoste.setTitle("Poste : "+type+"\n"+"latitude : "+latitudePoste +"\n"+"longitude : "+longitudePoste);
            map2.getOverlays().add(markerPoste);

            /*posteRaid.add(type);
            ListIdPoste.add(ListIdPoste2);
            ListGeopointPoste.add(geoposte);*/
        }
    }


}
