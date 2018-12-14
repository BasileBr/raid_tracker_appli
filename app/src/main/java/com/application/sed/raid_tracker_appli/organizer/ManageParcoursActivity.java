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
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
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
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ManageParcoursActivity extends AppCompatActivity {

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

    private static ArrayList<GeoPoint> listGeoPoint;
    private static ArrayListAnySize<GeoPoint> listInter;
    private static ArrayList<GeoPoint> finaleListGeoPoint;
    private static ArrayList<GeoPoint> listFinale;
    private static ArrayList<GeoPoint> listFinalePoste;
    private static String idParcours;

    private static String idTrace;

    private String idRaid;

    //partie calibration //
    LocationManager locationManager = null;
    private int etat;
    private String fournisseur;
    private TextView latitude;
    private TextView longitude;
    private TextView Adresse;
    private Integer checkEndLocation=0;



    private MapView myOpenMapView;
    ScaleBarOverlay myScaleBarOverlay;
    CompassOverlay mCompassOverlay;
    MyLocationNewOverlay mLocationOverlay;
    RotationGestureOverlay mRotationGestureOverlay;
    private static ArrayList<GeoPoint> trajet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_parcours);

        context = this;

        Intent intent = getIntent();


        //récupération de l'id de la toolbar
        toolbar1 = (Toolbar) findViewById(R.id.toolbar);

        fincalibration= (ImageButton) findViewById(R.id.fincalibration);
        startcalibration=(ImageButton) findViewById(R.id.calibrationstart);


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
        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), map);
        mLocationOverlay.enableMyLocation();
        map.getOverlays().add(mLocationOverlay);


        // ajouter l'echelle
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(map);
        map.getOverlays().add(myScaleBarOverlay);

        // ajouter boussole
        CompassOverlay mCompassOverlay = new CompassOverlay(getApplicationContext(), new InternalCompassOrientationProvider(getApplicationContext()), map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(mCompassOverlay);


        //partie calibration

        trajet = new ArrayList<GeoPoint>();

        Log.d("GPS", "onCreate");

        map2 = map;


        if( intent != null) {

            idParcours = intent.getStringExtra("idParcours");
            Utils.debug("idParcous",idParcours);
            idRaid = intent.getStringExtra("idRaid");
            ApiRequestGet.getSpecificTraceFromParcours(ctx,Bdd.getValue(),idParcours,"ManageParcoursActivity");

            Utils.debug("idRaidParcours",idRaid);


            final AlertDialog.Builder alert = new AlertDialog.Builder(context);

            //si on appuie sur le bouton retour, on arrive sur la page X
            toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (checkEndLocation==0){

                        alert.setTitle("Quitter la page actuelle ?");
                        //indique que la popup ne peut pas disparaître si on appuie en dehors de la popup
                        alert.setCancelable(false);

                        alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.debug("cas normal","on stop la loca");
                                arreterLocalisation();
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
                    } else {

                        alert.setTitle("Quitter la page actuelle ?");
                        //indique que la popup ne peut pas disparaître si on appuie en dehors de la popup
                        alert.setCancelable(false);

                        alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.debug("cas critique","c'est maitrise");
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




      /*  fincalibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( context, "fin de la calibration", Toast.LENGTH_LONG).show();
                // locationManager.removeUpdates(ecouteurGPS);
                // ecouteurGPS = null;
                checkEndLocation=1;
                arreterLocalisation();


                ApiRequestPost.postTrace(context,Bdd.getValue(),idParcours,"true");


            }



        });*/
    }
    public void FinCalibration(View view){
        Toast.makeText( context, "fin de la calibration", Toast.LENGTH_LONG).show();
        // locationManager.removeUpdates(ecouteurGPS);
        // ecouteurGPS = null;
        checkEndLocation=1;
        arreterLocalisation();
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




        //Utils.debug("getSpecificTraceFromParcours",jsonArray.get(0).toString());


    }
    public static void recupParcours(String response){

        listGeoPoint = new ArrayList<>();
        //listInter = new ArrayListAnySize<>();
        listFinale = new ArrayList<>();
        listFinalePoste = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonArray listPoints = (JsonArray) parser.parse(response);

        GeoPoint ini = new GeoPoint(1.1,1.1);

        Utils.debug("recupParcours","Taille du Json : "+listPoints.size());
        Utils.debug("Nom", "listePoits "+listPoints.toString());
        GeoPoint listInter[] = new GeoPoint[listPoints.size()+3];
        GeoPoint listPoste[] = new GeoPoint[listPoints.size()+3];


        for (int k = 0; k < listPoints.size() ; k++) {

            GeoPoint newPoint;
            JsonObject myPoint = (JsonObject) listPoints.get(k);
            Utils.debug("yvantest",myPoint.toString());

            JsonObject jsonObject = myPoint.getAsJsonObject("idTrace");

            idTrace = jsonObject.get("id").toString();
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

                Utils.debug("NomPointPoste", "Ordre : " + ordre + " lat : " + latitude.toString() + " lon : " + longitude.toString());
                Utils.debug("recupParcoursPoste", "Longitude : " + longitude + " Latitude : " + latitude);
                listPoste[ordre]= newPoint;

                Utils.debug("Trace",listPoste.toString());
            }
            else {
                Utils.debug("NomPoint", "Ordre : " + ordre + " lat : " + latitude.toString() + " lon : " + longitude.toString());
                Utils.debug("recupParcours", "Longitude : " + longitude + " Latitude : " + latitude);

                newPoint = new GeoPoint(latitude, longitude);

                listInter[ordre] = newPoint;

                Utils.debug("Trace", listInter.toString());
            }
        }

        for (int k = 0;  k< listInter.length; k++){
            if (listInter[k] == null){
                Utils.debug("recupParcours","Point null");
            }
            else {
                listFinale.add(listInter[k]);
            }
        }
        for (int k = 0; k< listPoste.length; k++){
            if (listPoste[k] == null){
                Utils.debug("recupParcours","Point null");
            }
            else {
                listFinalePoste.add(listPoste[k]);
            }
        }

        Utils.debug("NomPoint", "list : " + listFinale.toString());

        trace(listFinale);
        tracePoste(listFinalePoste);
    }

    public static void trace(ArrayList<GeoPoint> myListe){

        finaleListGeoPoint = new ArrayList<>();
        ParcoursListGeoPoint = 0;
        parcours = new ArrayList<>();
        compteur = 0;
        for (int i = 0; i < myListe.size(); i ++) {

            GeoPoint myPoint = myListe.get(i);

            if (i == 0) {
                // Point de départ
                Utils.debug("recupParcours", "Point de départ");
                standardmarker = new Marker(map2);
                standardmarker.setIcon(context.getResources().getDrawable(R.drawable.green_flag2));
                standardmarker.setPosition(myPoint);
                standardmarker.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                Utils.debug("longPressHelper", "Lat " + myPoint.getLatitude() + "long " + myPoint.getLongitude());

                standardmarker.setTitle("Point de départ" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());
                map2.getOverlays().add(standardmarker);
                finaleListGeoPoint.add(myPoint);
                map2.invalidate();
                setRoad(finaleListGeoPoint);

            }
            else if (i == myListe.size() -1){
                // Point d'arrivée
                Utils.debug("recupParcours", "Point de d'arrivée");
                standardmarker1 = new Marker(map2);
                standardmarker1.setIcon(context.getResources().getDrawable(R.drawable.red_flag2));
                standardmarker1.setPosition(myPoint);
                standardmarker1.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                Utils.debug("longPressHelper", "Lat " + myPoint.getLatitude() + "long " + myPoint.getLongitude());

                standardmarker1.setTitle("Point d'arrivée" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());
                map2.getOverlays().add(standardmarker1);
                finaleListGeoPoint.add(myPoint);
                map2.invalidate();
                setRoad(finaleListGeoPoint);

            }

            else {
                // Point de Passage
                Utils.debug("recupParcours", "Point de passage");
                standardmarker2 = new Marker(map2);
                standardmarker2.setIcon(context.getResources().getDrawable(R.drawable.passage23));
                standardmarker2.setPosition(myPoint);
                standardmarker2.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                Utils.debug("longPressHelper", "Lat " + myPoint.getLatitude() + "long " + myPoint.getLongitude());

                standardmarker2.setTitle("Point de passage" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());

                map2.getOverlays().add(standardmarker2);
                finaleListGeoPoint.add(myPoint);
                map2.invalidate();
                setRoad(finaleListGeoPoint);
            }
        }
    }
    public static void setRoad(ArrayList<GeoPoint> listGeoPoint) {
        //parcours l'arraylist contenant tous les geopoints lors d'un appui long
        for (int i = ParcoursListGeoPoint; i < listGeoPoint.size(); i++) {
            // on ajoute le premier point dans l'arraylist (parcours) de deux pts max
            if (compteur == 0) {
                Utils.debug("setRoad", "If cpt = 0 ");
                parcours.add(listGeoPoint.get(i));
                pointa = parcours.get(0);
                Utils.debug("setRoad", "PointA" + pointa.toString());
                compteur += 1;
                ParcoursListGeoPoint += 1;
            }

            //on ajoute le deuxième arraylist (parcours) puis on envoi la tache de fond à perfomCalculations
            else if (compteur == 1) {
                Utils.debug("setRoad", "If cpt = 1 ");
                parcours.add(listGeoPoint.get(i));
                Utils.debug("setRoad", "Parcours ");
                compteur += 1;
                pointb = parcours.get(1);
                Utils.debug("setRoad", "pointB " + pointb.toString());

                //tache de fond
                //new PerfomCalculations(getApplicationContext(),this).execute(new GeoPoint(){parcours.get(0),parcours.get(1)});
                GeoPoint[] toto = new GeoPoint[2];
                toto[0] = pointa;
                toto[1] = pointb;
                ParcoursListGeoPoint += 1;
                new PerfomCalculations().execute(pointa, pointb);
            }

            // on écrase la prremiere valeur de l'arraylist et on postionne le nouveau point
            else if (compteur == 2) {

                Utils.debug("setRoad", "If cpt = 2 ");
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

    public static void tracePoste(ArrayList<GeoPoint> myListe){


        for (int i = 0; i < myListe.size(); i ++) {

            GeoPoint myPoint = myListe.get(i);
            //Poste
            Utils.debug("recupPoste", "poste numéro"+i);
            standarmarker3 = new Marker(map2);
            standarmarker3.setIcon(context.getResources().getDrawable(R.drawable.poi1));
            standarmarker3.setPosition(myPoint);
            standarmarker3.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
            Utils.debug("recupPoste", "Lat " + myPoint.getLatitude() + "long " + myPoint.getLongitude());

            standarmarker3.setTitle("Poste" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());

            map2.getOverlays().add(standarmarker3);

            map2.invalidate();




        }
    }

    private static class PerfomCalculations extends AsyncTask<GeoPoint,Void,Polyline> {
        @Override
        protected Polyline doInBackground(GeoPoint[] params) {
            ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
            //waypoints.add(params[0]); //POINT A
            //waypoints.add(params[1]); // point b

//            try {
//            RoadManager roadManager = new MapQuestRoadManager("o7gFRAppOrsTtcBhEVYrY6L7AGRtXldE");
//            roadManager.addRequestOption("routeType=pedestrian");
//            Road road = roadManager.getRoad(waypoints);

            Polyline line = new Polyline(map2);
            line .addPoint(params[0]);
            line .addPoint(params[1]);
            line.setWidth(3);

            //RoadManager roadManager = new OSRMRoadManager(getApplicationContext()); // your context
            //Road road = roadManager.getRoad(waypoints);

            return line;

        }


        /*
         affiche la ligne entre deux points
         */
        protected void onPostExecute(Polyline line) {

            map2.getOverlays().add(line);

            map2.invalidate();

        }

    }

    public static class ArrayListAnySize<E> extends ArrayList<E>{
        @Override
        public void add(int index, E element){
            if(index >= 0 && index <= size()){
                super.add(index, element);
                return;
            }
            int insertNulls = index - size();
            for(int i = 0; i < insertNulls; i++){
                super.add(null);
            }
            super.add(element);
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
                // on notifie la localisation
//                ecouteurGPS.onLocationChanged(localisation);
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
        Utils.debug("startCalibration","Je rentre dans le bouton");
        initialiserLocalisation();

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
            //myOpenMapView.setMapOrientation(localisation.getBearing());

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



            /*Marker tec = new Marker(myOpenMapView);
            tec.setPosition(new GeoPoint(localisation.getLatitude(), localisation.getLongitude()));
            tec.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            tec.setIcon(getResources().getDrawable(R.drawable.trottinette));
            tec.setTitle("TEC");
            myOpenMapView.getOverlays().add(tec);*/

            /*ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
            items.add(new OverlayItem("Title", "Description", new GeoPoint(localisation.getLatitude(), localisation.getLongitude())));

            ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getApplicationContext(), items,
                    new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                        @Override
                        public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                            Toast.makeText( getApplicationContext(), "Overlay Titled: " +
                                    item.getTitle() + " Single Tapped" + "\n" + "Description: " +
                                    item.getSnippet(), Toast.LENGTH_LONG).show();
                            return true;
                        }
                        @Override
                        public boolean onItemLongPress(final int index, final OverlayItem item) {
                            return false;
                        }
                    });
            //mOverlay.setFocusItemsOnTap(true);
            myOpenMapView.getOverlays().add(mOverlay);*/

            map.invalidate();

         /*   List<Address> adresses = null;
            try
            {
                adresses = geocoder.getFromLocation(localisation.getLatitude(), localisation.getLongitude(), 1);
            }
            catch (IOException ioException)
            {
                Log.e("GPS", "erreur", ioException);
            } catch (IllegalArgumentException illegalArgumentException)
            {
                Log.e("GPS", "erreur " + coordonnees, illegalArgumentException);
            }

            if (adresses == null || adresses.size()  == 0)
            {
                Log.e("GPS", "erreur aucune adresse !");
            }
            else
            {
                Address adresse = adresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<String>();

                String strAdresse = adresse.getAddressLine(0) + ", " + adresse.getLocality();
                Log.d("GPS", "adresse : " + strAdresse);

                for(int i = 0; i <= adresse.getMaxAddressLineIndex(); i++)
                {
                    addressFragments.add(adresse.getAddressLine(i));
                }
                Log.d("GPS", TextUtils.join(System.getProperty("line.separator"), addressFragments));
                Adresse.setText(TextUtils.join(System.getProperty("line.separator"), addressFragments));
            }*/
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


    public static void recupIdTrace(String idTrace){
        for (int i=0;i<trajet.size();i++){

            //Utils.debug("onClick",idTrace);
            Double lon = trajet.get(i).getLongitude();
            Double lat = trajet.get(i).getLatitude();
            Utils.debug("NomPoint","Ordre : "+i+" Lat : "+lat.toString() + " Lon : "+ lon.toString());

                    /*Utils.debug("onClick"," long : "+ lon +" lat : "+lat);
                    Utils.debug("onClick","k : "+String.valueOf(k) + " taille list point : " + String.valueOf(ListGeoPoint.size()));*/

            if (i == 0){
                //Point départ type = 1
                //Utils.debug("onclick", "k if : "+String.valueOf(k));
                ApiRequestPost.postPoint(context,Bdd.getValue(),idTrace,lon,lat,1, i, null);
            }

            else if ( trajet.size() - i ==1){
                //Point arrivée type = 2
                //Utils.debug("onclick", "k elseif : "+String.valueOf(k));
                ApiRequestPost.postPoint(context,Bdd.getValue(),idTrace,lon,lat,2, i, null);
            }
            else {
                //Point passage type = 0
                //Utils.debug("onclick", "k else : "+String.valueOf(k));
                ApiRequestPost.postPoint(context,Bdd.getValue(),idTrace,lon,lat,0, i, null);
            }

        }
    }
    //fin partie calibration //


}
