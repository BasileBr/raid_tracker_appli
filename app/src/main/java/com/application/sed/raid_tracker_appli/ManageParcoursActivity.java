package com.application.sed.raid_tracker_appli;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.API.ApiRequestPost;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.application.sed.raid_tracker_appli.organizer.CreateParcours;
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
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.UTFDataFormatException;
import java.util.ArrayList;

import okhttp3.internal.Util;

public class ManageParcoursActivity extends AppCompatActivity {

    Toolbar toolbar1;

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
    private static String idParcours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_parcours);

        context = this;

        Intent intent = getIntent();


        //récupération de l'id de la toolbar
        toolbar1 = (Toolbar) findViewById(R.id.toolbar);

        // on définit la toolbar dans notre activity
        setSupportActionBar(toolbar1);

        //on ajoute un bouton retour dans l'action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //si on appuie sur le bouton retour, on arrive sur la page X
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(ManageParcoursActivity.this, LandingActivity.class);
                //startActivity(intent);
            }
        });

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

        map2 = map;

        if( intent != null) {

            idParcours = intent.getStringExtra("idParcours");
            ApiRequestGet.getSpecificTraceFromParcours(ctx,Bdd.getValue(),idParcours);

        }
        else {
            ApiRequestGet.getSpecificTraceFromParcours(ctx, Bdd.getValue(), "30");
        }
    }

    public static void recupParcours(String response){

        listGeoPoint = new ArrayList<>();
        //listInter = new ArrayListAnySize<>();
        listFinale = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonArray listPoints = (JsonArray) parser.parse(response);

        GeoPoint ini = new GeoPoint(1.1,1.1);

        Utils.debug("recupParcours","Taille du Json : "+listPoints.size());
        Utils.debug("Nom", "listePoits "+listPoints.toString());
        GeoPoint listInter[] = new GeoPoint[listPoints.size()+10];


        for (int k = 0; k < listPoints.size() ; k++) {

            GeoPoint newPoint;
            JsonObject myPoint = (JsonObject) listPoints.get(k);
            JsonElement lat = myPoint.get("lat");
            JsonElement lon = myPoint.get("lon");
            JsonElement ord = myPoint.get("ordre");

            Double longitude = lon.getAsDouble();
            Double latitude = lat.getAsDouble();
            int ordre = ord.getAsInt();
            Utils.debug("NomPoint", "Ordre : " + ordre + " lat : "+latitude.toString()+" lon : " +longitude.toString());
            Utils.debug("recupParcours", "Longitude : " + longitude + " Latitude : " + latitude);
            //int type = t.getAsInt();

            newPoint = new GeoPoint(latitude, longitude);

            listInter[ordre]= newPoint;
            //listType.add(ordre,type);
            Utils.debug("Trace",listInter.toString());
            //Utils.debug("Trace",listType.toString());

        }

        for (int k = 0;  k< listInter.length; k++){
            if (listInter[k] == null){
                Utils.debug("recupParcours","Point null");
            }
            else {
                listFinale.add(listInter[k]);
            }
        }

        Utils.debug("NomPoint", "list : " + listFinale.toString());

        trace(listFinale);
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
                //        //Liste de points
                //        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                //
                //        //waypoints.add(startPoint);
                //
                //        //waypoints.add(endPoint);
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

                //        //Liste de points
                //        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                //
                //        //waypoints.add(startPoint);
                //
                //        //waypoints.add(endPoint);

                standardmarker1.setTitle("Point d'arrivée" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());

                //ajouter un icone particuliere
                //startMarker.setIcon(getResources().getDrawable(R.drawable.pointer));
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

                //        //Liste de points
                //        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                //
                //        //waypoints.add(startPoint);
                //
                //        //waypoints.add(endPoint);

                standardmarker2.setTitle("Point de passage" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());

                //ajouter un icone particuliere
                //startMarker.setIcon(getResources().getDrawable(R.drawable.pointer));
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
                //new PerfomCalculations(getApplicationContext(),this).execute(toto);
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
                //new PerfomCalculations(getApplicationContext(),this).execute(new GeoPoint(){parcours.get(0),parcours.get(1)});
                ParcoursListGeoPoint += 1;
                new PerfomCalculations().execute(geotemporaire, parcours.get(1));

            }

        }
    }
    /*public static void trace (ArrayList<GeoPoint> myListe){

        finaleListGeoPoint = new ArrayList<>();
        GeoPoint myPoint;
        ParcoursListGeoPoint = -1;
        compteur = -1;
        parcours = new ArrayList<>();
        for (int i = 0; i < myListe.size() ; i++) {

            myPoint = new GeoPoint(myListe.get(i));
            if (i == 1) {
                standardmarker = new Marker(map2);
                standardmarker.setIcon(context.getResources().getDrawable(R.drawable.green_flag2));

                standardmarker.setPosition(myPoint);
                standardmarker.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                standardmarker.setTitle("Point de départ" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());
                map2.getOverlays().add(standardmarker);
                ParcoursListGeoPoint = ParcoursListGeoPoint + 1;
                finaleListGeoPoint.add(myPoint);
                map2.invalidate();
                if ((compteur == -1) || (compteur == 0) || (compteur == 1)) {
                    compteur = compteur + 1;
                    Utils.debug("cpt ", "cpt : " + compteur);
                }
                ParcoursListGeoPoint = ParcoursListGeoPoint + 1;
                setRoad(finaleListGeoPoint, ParcoursListGeoPoint, compteur, parcours);
            }

            else if (i == myListe.size()-1){
                standardmarker1 = new Marker(map2);
                standardmarker1.setIcon(context.getResources().getDrawable(R.drawable.red_flag2));
                standardmarker1.setPosition(myPoint);
                standardmarker1.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);

                standardmarker1.setTitle("Point d'arrivée" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());

                //ajouter un icone particuliere
                //startMarker.setIcon(getResources().getDrawable(R.drawable.pointer));
                map2.getOverlays().add(standardmarker1);
                finaleListGeoPoint.add(myPoint);
                map2.invalidate();
                if ((compteur == -1) || (compteur == 0) || (compteur == 1)) {
                    compteur = compteur + 1;
                    Utils.debug("cpt ", "cpt : " + compteur);
                }
                ParcoursListGeoPoint = ParcoursListGeoPoint + 1;
                setRoad(finaleListGeoPoint, ParcoursListGeoPoint, compteur, parcours);
            }

            else {
                standardmarker2 = new Marker(map2);
                standardmarker2.setIcon(context.getResources().getDrawable(R.drawable.passage23));
                standardmarker2.setPosition(myPoint);
                standardmarker2.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                standardmarker2.setTitle("Point de passage" + "\n" + "latitude: " + myPoint.getLatitude()+ '\n' + "longitude: " + myPoint.getLongitude());

                //ajouter un icone particuliere
                //startMarker.setIcon(getResources().getDrawable(R.drawable.pointer));
                map2.getOverlays().add(standardmarker2);
                finaleListGeoPoint.add(myPoint);
                map2.invalidate();
                if ((compteur == -1) || (compteur == 0) || (compteur == 1)) {
                    compteur = compteur + 1;
                    Utils.debug("cpt ", "cpt : " + compteur);
                }
                ParcoursListGeoPoint = ParcoursListGeoPoint + 1;
                setRoad(finaleListGeoPoint, ParcoursListGeoPoint, compteur, parcours);
            }
            //setRoad(myListe, ParcoursListGeoPoint, compteur, parcours);




        }
    }
    public static void setRoad(ArrayList<GeoPoint> myListe, int cpt, int mycompteurs, ArrayList<GeoPoint> parc){


        //parcours l'arraylist contenant tous les geopoints lors d'un appui long
        Utils.debug("setRoad","Je rentre dans setRoad "+mycompteurs + " cpt : "+cpt);


        for (int i = mycompteurs; i<myListe.size();i++){
            // on ajoute le premier point dans l'arraylist (parcours) de deux pts max
            if (cpt==0){
                Utils.debug("setRoad","If cpt = 0 ");
                parc.add(myListe.get(i));
                pointa = parc.get(0);
                Utils.debug("setRoad","PointA" + pointa.toString());
                //compteur +=1;
                mycompteurs += 1;
            }

            //on ajoute le deuxième arraylist (parcours) puis on envoi la tache de fond à perfomCalculations
            else if (cpt==1){
                Utils.debug("setRoad","If cpt = 1 ");
                parc.add(myListe.get(i));
                Utils.debug("setRoad","Parcours ");
                //compteur +=1;
                pointb = parc.get(1);
                Utils.debug("setRoad","pointB " + pointb.toString());

                //tache de fond
                //new PerfomCalculations(getApplicationContext(),this).execute(new GeoPoint(){parcours.get(0),parcours.get(1)});
                GeoPoint[] toto = new GeoPoint[2];
                toto[0] = pointa;
                toto[1] = pointb;
                mycompteurs += 1;
                //new PerfomCalculations(getApplicationContext(),this).execute(toto);
                new PerfomCalculations().execute(pointa,pointb);
            }

            // on écrase la prremiere valeur de l'arraylist et on postionne le nouveau point
            else if (cpt==2){

                Utils.debug("setRoad","If cpt = 2 ");
                //recupere le deuxieme point dans parcours
                geotemporaire = parc.get(1);
                //on l'ajoute en écrasant l'indice 0
                parc.add(0,geotemporaire);
                parc.add(1,myListe.get(i));

                //balance la tache de fond
                //new PerfomCalculations(getApplicationContext(),this).execute(new GeoPoint(){parcours.get(0),parcours.get(1)});
                mycompteurs += 1;
                new PerfomCalculations().execute(geotemporaire,parcours.get(1));

            }

        }
    }*/

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
//            } catch (Exception e) {
//                return -1;
        }


        /*
         affiche la ligne entre deux points
         */
        protected void onPostExecute(Polyline line) {
////
            // Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
            map2.getOverlays().add(line);

            map2.invalidate();

//
            //MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(getApplicationContext(), );
            // map.getOverlays().add(0, mapEventsOverlay);
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

}
