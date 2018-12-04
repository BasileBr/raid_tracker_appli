package com.application.sed.raid_tracker_appli.organizer;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import com.android.volley.toolbox.StringRequest;
import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.API.ApiRequestPost;
import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.internal.Util;


//piste garder les éléments lors d'un changement d'orientation setRetainInstance
public class CreateParcours extends AppCompatActivity implements MapEventsReceiver {
    static MapView map = null;
    private ArrayList<GeoPoint> ListGeoPoint = new ArrayList<>();
    private static ArrayList<GeoPoint> ListGeopoi = new ArrayList<>();


    int ParcoursListGeoPoint = 0;
    MyLocationNewOverlay mLocationOverlay;

    private int emptyname=0;
    private static int emptynom=0;
    private static int emptynombre=0;
    private static int emptydebut=0;
    private static int emptyfin=0;

    private String m_Text = "";
    private static String m_Textnom ="";
    private static String m_Textnombre ="";

    private static String m_Textanneedebut ="";
    private static String m_Textmoisdebut ="";
    private static String m_Textjoursdebut ="";
    private static String m_Textheuredebut ="";
    private static String m_Textminutedebut ="";

    private static String m_Textanneefin ="";
    private static String m_Textmoisfin ="";
    private static String m_Textjoursfin ="";
    private static String m_Textheurefin ="";
    private static String m_Textminutefin ="";



    private static Context context;
    private static String idTrace;
    public static int cpt = 0;

    private static int hours;
    private static int min;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView selectdate;
    private String getDate = "";

    Marker standardmarker; // = new Marker(map);
    Marker standardmarker1; // = new Marker(map);
    Marker standardmarker2;
    static Marker standarmarker3;

    //private static ArrayList<String> listsport;
    private Spinner test;

    Toolbar toolbar1;

    GeoPoint pointa = new GeoPoint(51.489878, 6.143294);
    GeoPoint pointb = new GeoPoint(51.488978, 6.746994);
    GeoPoint geotemporaire;
    public static GeoPoint poste = new GeoPoint(51.1,6.1);

    int numbouton = 0;
    int compteur=0;
    String idRaid;

    public static String nomParcours;
    public static ArrayList<List> Liste =new ArrayList<>();
    ArrayList<GeoPoint> parcours = new ArrayList<>();


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_parcours);

        //on récupère l'identifiant de la toolbar
        Intent intent = getIntent();
        if (intent != null){

            idRaid = intent.getStringExtra("idRaid");
            toolbar1 = (Toolbar) findViewById(R.id.toolbar);

            // on définit la toolbar
            setSupportActionBar(toolbar1);

            //ajouter un bouton retour dans l'action bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            //si on appuie sur le bouton retour, on arrive sur la page landing
            toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CreateParcours.this, LandingActivity.class);
                    startActivity(intent);
                }
            });

            //récuperation du context
            context = this;

            //listsport = intent.getStringArrayListExtra("listsport");

            //handle permissions first, before map is created. not depicted here TODO

            //load/initialize the osmdroid configuration, this can be done
            Context ctx = getApplicationContext();
            Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
            //setting this before the layout is inflated is a good idea
            //it 'should' ensure that the map has a writable location for the map cache, even without permissions
            //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
            //see also StorageUtils
            //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's tile servers will get you banned based on this string

            //inflate and create the map
            // setContentView(R.layout.activity_create_parcours);

            // à utiliser en phase de développement, autorise toutes les permissions sur le thread UI (pas terrible)
            //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            //StrictMode.setThreadPolicy(policy);

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


            /**
             * à utiliser si besoin de tracer un parcours en passant par la route
             */
            //créer un road manager (Appel vers l'api pour guider d'un point à un autre
//        RoadManager roadManager = new MapQuestRoadManager("o7gFRAppOrsTtcBhEVYrY6L7AGRtXldE");

            // fixer le point de départ et le point d'arrivée
            // ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
            //waypoints.add(startPoint);
            //GeoPoint endPoint = new GeoPoint(48.4, -1.9);
            //waypoints.add(endPoint);


            //choisir le type de route
            //roadManager.addRequestOption("routeType=pedestrian");
            // Road road = roadManager.getRoad(waypoints);

            //créer les lignes
            // Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
            //map.getOverlays().add(roadOverlay);

            // permet de mettre à jour la carte
            //map.invalidate();

            MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
            map.getOverlays().add(0, mapEventsOverlay);


            //récupère les identifiants des drapeaux
            final ImageButton greenflag = (ImageButton) findViewById(R.id.greenflag);
            final ImageButton redflag = (ImageButton) findViewById(R.id.redflag);
            final ImageButton passagepoint = (ImageButton) findViewById(R.id.passagepoint);
            final ImageButton poi = (ImageButton) findViewById(R.id.poi);


            //action sur le drapeau de départ (ajout d'un fond ecran et fond ecran par defaut pour les autres boutons)
            greenflag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.info("coucou", "ouiii");
                    numbouton = 1;
                    greenflag.setBackgroundColor(Color.rgb(209, 196, 190));
                    redflag.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                    passagepoint.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                    poi.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                }
            });


            //action sur le drapeau d'arrivée (ajout d'un fond ecran et fond ecran par defaut pour les autres boutons)
            redflag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.info("coucou", "rouuuuge");
                    numbouton = 2;
                    redflag.setBackgroundColor(Color.rgb(209, 196, 190));
                    greenflag.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                    passagepoint.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                    poi.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                    //map.getOverlays().add(standardmarker1);
                }
            });

            //action sur le drapeau de point de passage (ajout d'un fond ecran et fond ecran par defaut pour les autres boutons)
            passagepoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.info("coucou", "bleuuu");
                    numbouton = 3;

                    passagepoint.setBackgroundColor(Color.rgb(209, 196, 190));
                    greenflag.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                    redflag.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                    poi.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                    //poi.setBackgroundColor(Color.rgb(209, 196, 190));
                    //map.getOverlays().add(standardmarker1);
                }
            });

            ////action sur le drapeau des points d'intérêtes (ajout d'un fond ecran et fond ecran par defaut pour les autres boutons)
            poi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numbouton = 4;
                    poi.setBackgroundColor(Color.rgb(209, 196, 190));
                    greenflag.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                    passagepoint.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                    redflag.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                }
            });


            //ajouter marqueur
//        GeoPoint enssatpoint =  new GeoPoint(48.729673,-3.4624261999999817);
//        Marker startMarker = new Marker(map);
//        startMarker.setPosition(enssatpoint);
//        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//        String latitude=String.valueOf(enssatpoint.getLatitude());
//        String longitude=String.valueOf(enssatpoint.getLongitude());
//        startMarker.setTitle("ENSSAT"+"\n"+"latitude: "+latitude+'\n'+"longitude: "+longitude);
//        //ajouter un icone particuliere
//        startMarker.setIcon(getResources().getDrawable(R.drawable.pointer));
//        map.getOverlays().add(startMarker);

            //afficher une popup pour sélectionner le type de sport
            ShowAlertDialog(map);
        }



    }

    public static void trace(String id){
        idTrace = id;

    }
    //initialisation de compteur pour tester si on peut ajouter un nouveau drapeau
    int compteurpointdepart=0;
    int compteurpointarrivee=0;
    int compteurpointpoi=0;

    //effectuer des actions lors d'un appui long
    @Override public boolean longPressHelper(GeoPoint p) {

        // création d'un nouveau point de géolocalisation
        GeoPoint tmpgeo = new GeoPoint(p.getLatitude(),p.getLongitude());
        //Marker startMarker = new Marker(map);
        //startMarker.setPosition(tmpgeo);

        //récupération des valeurs de localisation (latitude, longitude)
        final String latitude=String.valueOf(p.getLatitude());
        String longitude=String.valueOf(p.getLongitude());
        switch (numbouton) {
            case 0:
                break;
            case 1: // cas du point de départ, on vérifie s'il est possible d'en créer un nouveau
                if (compteurpointdepart==0 && compteurpointpoi == 0 && compteurpointarrivee == 0) {
                    compteurpointdepart = 1;
                    standardmarker = new Marker(map);
                    standardmarker.setIcon(getResources().getDrawable(R.drawable.green_flag2));
                    standardmarker.setPosition(tmpgeo);
                    standardmarker.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                    Utils.debug("longPressHelper", "Lat " + latitude + "long " + longitude);
                    //        //Liste de points
                    //        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                    //
                    //        //waypoints.add(startPoint);
                    //
                    //        //waypoints.add(endPoint);
                    standardmarker.setTitle("Point de départ" + "\n" + "latitude: " + latitude + '\n' + "longitude: " + longitude);
                    map.getOverlays().add(standardmarker);
                    ListGeoPoint.add(tmpgeo);
                    map.invalidate();
                    setRoad();
                    break;
                    }
                else{ // sinon impossible de le créer
                        Toast.makeText(getApplicationContext(), "Vous ne pouvez pas ajouter ce point", Toast.LENGTH_SHORT).show();
                        break;
                    }


            case 2:

                if (compteurpointdepart == 1 && compteurpointpoi == 1 && compteurpointarrivee==0) {
                    compteurpointarrivee = 1;
                    standardmarker1 = new Marker(map);
                    standardmarker1.setIcon(getResources().getDrawable(R.drawable.red_flag2));
                    standardmarker1.setPosition(tmpgeo);
                    standardmarker1.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                    Utils.debug("longPressHelper", "Lat " + latitude + "long " + longitude);

                    //        //Liste de points
                    //        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                    //
                    //        //waypoints.add(startPoint);
                    //
                    //        //waypoints.add(endPoint);

                    standardmarker1.setTitle("Point d'arrivée" + "\n" + "latitude: " + latitude + '\n' + "longitude: " + longitude);

                    //ajouter un icone particuliere
                    //startMarker.setIcon(getResources().getDrawable(R.drawable.pointer));
                    map.getOverlays().add(standardmarker1);
                    ListGeoPoint.add(tmpgeo);
                    map.invalidate();
                    setRoad();

                    break;
                } else if (compteurpointarrivee==1) {
                    Toast.makeText(getApplicationContext(), "Vous ne pouvez pas ajouter ce point", Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    Toast.makeText(getApplicationContext(), "Vous ne pouvez pas ajouter ce point", Toast.LENGTH_SHORT).show();
                    break;
                }
            case 3:
                if (compteurpointdepart == 1 && compteurpointarrivee == 0 ) {
                    compteurpointpoi = 1;
                    standardmarker2 = new Marker(map);
                    standardmarker2.setIcon(getResources().getDrawable(R.drawable.passage23));
                    standardmarker2.setPosition(tmpgeo);
                    standardmarker2.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                    Utils.debug("longPressHelper", "Lat " + latitude + "long " + longitude);

                    map.getOverlays().add(standarmarker3);
                    //        //Liste de points
                    //        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                    //
                    //        //waypoints.add(startPoint);
                    //
                    //        //waypoints.add(endPoint);

                    standardmarker2.setTitle("Point de passage" + "\n" + "latitude: " + latitude + '\n' + "longitude: " + longitude);

                    //ajouter un icone particuliere
                    //startMarker.setIcon(getResources().getDrawable(R.drawable.pointer));
                    map.getOverlays().add(standardmarker2);
                    ListGeoPoint.add(tmpgeo);
                    map.invalidate();
                    setRoad();
                    break;
                }
                else {
                    Toast.makeText(getApplicationContext(), "Vous ne pouvez pas ajouter ce point", Toast.LENGTH_SHORT).show();
                    break;
                }


            case 4:
                standarmarker3 = new Marker(map);
                ListGeopoi.add(tmpgeo);
                poste.setCoords(tmpgeo.getLatitude(),tmpgeo.getLongitude());

                standarmarker3.setIcon(getResources().getDrawable(R.drawable.poi1));
                standarmarker3.setPosition(tmpgeo);
                standarmarker3.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                Utils.debug("longPressHelper","Lat "+latitude + "long " + longitude);
                ApiRequestPost.postPoint(context,Bdd.getValue(),idTrace,tmpgeo.getLongitude(),tmpgeo.getLatitude(),3,cpt, map);
                /*map.getOverlays().add(standarmarker3);
                map.invalidate();*/



                //        //Liste de points
                //        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                //
                //        //waypoints.add(startPoint);
                //
                //        //waypoints.add(endPoint);

                standarmarker3.setTitle("nom : "+nomParcours+"\n"+"latitude: "+latitude+'\n'+"longitude: "+longitude);

                //ajouter un icone particuliere
                //startMarker.setIcon(getResources().getDrawable(R.drawable.pointer));

                //setRoad();
                break;


        }

        // Création d'un nouveau bouton
        Button b1=(Button)findViewById(R.id.validateparcours);

        //si le point d'arrivée a été placé alors on propose d'enregistrer les points
        if (compteurpointarrivee==1) {
            b1.setVisibility(View.VISIBLE);
        }

        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Utils.debug("ListGeoPoint", ListGeoPoint.toString());
                int k;
                for (k = 0; k<ListGeoPoint.size(); k++ ){


                    //Utils.debug("onClick",idTrace);
                    Double lon = ListGeoPoint.get(k).getLongitude();
                    Double lat = ListGeoPoint.get(k).getLatitude();
                    Utils.debug("NomPoint","Ordre : "+k+" Lat : "+lat.toString() + " Lon : "+ lon.toString());

                    /*Utils.debug("onClick"," long : "+ lon +" lat : "+lat);
                    Utils.debug("onClick","k : "+String.valueOf(k) + " taille list point : " + String.valueOf(ListGeoPoint.size()));*/

                    if (k == 0){
                        //Point départ type = 1
                        //Utils.debug("onclick", "k if : "+String.valueOf(k));
                        ApiRequestPost.postPoint(context,Bdd.getValue(),idTrace,lon,lat,1, k, null);
                    }

                    else if ( ListGeoPoint.size() - k ==1){
                        //Point arrivée type = 2
                        //Utils.debug("onclick", "k elseif : "+String.valueOf(k));
                        ApiRequestPost.postPoint(context,Bdd.getValue(),idTrace,lon,lat,2, k, null);
                    }
                    else {
                        //Point passage type = 0
                        //Utils.debug("onclick", "k else : "+String.valueOf(k));
                        ApiRequestPost.postPoint(context,Bdd.getValue(),idTrace,lon,lat,0, k, null);
                    }

                }


                /*for (int i =0; i< ListGeopoi.size(); i++ ){
                    Double lon = ListGeopoi.get(i).getLongitude();
                    Double lat = ListGeopoi.get(i).getLatitude();

                    ApiRequestPost.postPoint(context,Bdd.getValue(),idTrace,lon,lat,3,i);
                }*/


                //envoyer les points d'intérêts
               //if (!ListGeopoi.isEmpty()){
                for (int y=0; y<ListGeopoi.size(); y++){
                    Double lon = ListGeopoi.get(y).getLongitude();
                    Double lat = ListGeopoi.get(y).getLatitude();
                    Utils.info("onclick",String.valueOf(y));
                    ApiRequestPost.postPoint(context,Bdd.getValue(),idTrace,lon,lat,3,k, null);
                    Utils.debug("NomPoint","Ordre : "+k+" Lat : "+lat.toString() + " Lon : "+ lon.toString());
                    k = k+1;
                }
                //}

                Intent intent = new Intent(CreateParcours.this, LandingActivity.class);
                startActivity(intent);


            }
        });
        return false;
    }


    /**
     * Algorithme pour ajouter les points d'un parcours et envoi du traitement en tache de fond (asynctask)
     */
    public void setRoad(){
        //parcours l'arraylist contenant tous les geopoints lors d'un appui long
        for (int i = ParcoursListGeoPoint; i<ListGeoPoint.size();i++){
            // on ajoute le premier point dans l'arraylist (parcours) de deux pts max
            if (compteur==0){
                Utils.debug("setRoad","If cpt = 0 ");
                parcours.add(ListGeoPoint.get(i));
                pointa = parcours.get(0);
                Utils.debug("setRoad","PointA" + pointa.toString());
                compteur +=1;
                ParcoursListGeoPoint += 1;
            }

            //on ajoute le deuxième arraylist (parcours) puis on envoi la tache de fond à perfomCalculations
            else if (compteur==1){
                Utils.debug("setRoad","If cpt = 1 ");
                parcours.add(ListGeoPoint.get(i));
                Utils.debug("setRoad","Parcours ");
                compteur +=1;
                pointb = parcours.get(1);
                Utils.debug("setRoad","pointB " + pointb.toString());

                //tache de fond
                //new PerfomCalculations(getApplicationContext(),this).execute(new GeoPoint(){parcours.get(0),parcours.get(1)});
                GeoPoint[] toto = new GeoPoint[2];
                toto[0] = pointa;
                toto[1] = pointb;
                ParcoursListGeoPoint += 1;
                //new PerfomCalculations(getApplicationContext(),this).execute(toto);
                new PerfomCalculations().execute(pointa,pointb);
            }

            // on écrase la prremiere valeur de l'arraylist et on postionne le nouveau point
            else if (compteur==2){

                Utils.debug("setRoad","If cpt = 2 ");
                //recupere le deuxieme point dans parcours
                geotemporaire = parcours.get(1);
                //on l'ajoute en écrasant l'indice 0
                parcours.add(0,geotemporaire);
                parcours.add(1,ListGeoPoint.get(i));

                //balance la tache de fond
                //new PerfomCalculations(getApplicationContext(),this).execute(new GeoPoint(){parcours.get(0),parcours.get(1)});
                ParcoursListGeoPoint += 1;
                new PerfomCalculations().execute(geotemporaire,parcours.get(1));

            }

        }




//        if (ParcoursListGeoPoint <1) {
//            ParcoursListGeoPoint += 1;
//        }
//        else {
//            for (int i = ParcoursListGeoPoint; i < ListGeoPoint.size(); i++){
//                parcours.add(ListGeoPoint.get(i-1));
//                parcours.add(ListGeoPoint.get(i));
//                RoadManager roadManager = new MapQuestRoadManager("o7gFRAppOrsTtcBhEVYrY6L7AGRtXldE");
//
//                roadManager.addRequestOption("routeType=pedestrian");
//                Road road = roadManager.getRoad(parcours);
//
//                Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
//
//                map.getOverlays().add(roadOverlay);
//
//                map.invalidate();
//
//                MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
//
//                map.getOverlays().add(0, mapEventsOverlay);
//                ParcoursListGeoPoint += 1;
//
//            }
//        }
    }

//    @Override
//    public void calculroute(boolean success, Route route) {
//        if (success) {
//                Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
//
//                map.getOverlays().add(roadOverlay);
//
//                map.invalidate();
//
//                MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
//
//                map.getOverlays().add(0, mapEventsOverlay);
//        }
//    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        Utils.debug("SingleTapConfirmedHelped", "Je rentre ici");
        return false;
    }



    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up


    }

    public void geolocateme (View view){
//        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(view.getContext()),map);ml
//        this.mLocationOverlay.enableMyLocation();
//        map.getOverlays().add(this.mLocationOverlay);
//
//        GeoPoint myPoint = new GeoPoint(this.mLocationOverlay.getMyLocation().getLatitude(),this.mLocationOverlay.getMyLocation().getLongitude());
//        map.getController().setCenter(myPoint);
//        map.getController().setZoom(14.0);

    }


    public void onPause(){
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up

    }

//    @Override
//    protected void onSaveInstanceState(Bundle outstate){
//        super.onSaveInstanceState(outstate);
//        outstate.put
//    }

    /*
     ** réaliser le calcul de tracé d'une ligne entre deux points
     */
    private class PerfomCalculations extends AsyncTask<GeoPoint,Void,Polyline> {
        @Override
        protected Polyline doInBackground(GeoPoint[] params) {
            ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
            //waypoints.add(params[0]); //POINT A
            //waypoints.add(params[1]); // point b

//            try {
//            RoadManager roadManager = new MapQuestRoadManager("o7gFRAppOrsTtcBhEVYrY6L7AGRtXldE");
//            roadManager.addRequestOption("routeType=pedestrian");
//            Road road = roadManager.getRoad(waypoints);

            Polyline line = new Polyline(map);
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
            map.getOverlays().add(line);

            map.invalidate();

//
            //MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(getApplicationContext(), );
            // map.getOverlays().add(0, mapEventsOverlay);
        }

    }






    /**
     * création de la popup pour définir le nom du parcours
     * @param view
     */
    public void ShowAlertDialog(final View view){



        //création textview pour afficher le nom du parcours
        final TextView setNameParcours = (TextView) findViewById(R.id.textname);

        //création de la popup
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Nom du parcours");
        //indique que la popup ne peut pas disparaître si on appuie en dehors de la popup
        alert.setCancelable(false);

        //alert.setMessage(listsport.toString());
        // création d'un edittext pour récupérer le nom du parcours
        final EditText input = new EditText(this);

        // indiquer que l'input est de type texte
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        //ajouter l'edittext à la popup
        alert.setView(input);



        //si aucun nom de parcours n'est entré, on affiche une erreure
        if(emptyname==1){
            input.setError("le champ est vide");
        }

        // Effectuer une action si on appuie le bouton valider la popup
        alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //on récupère le nom du parcours entré par l'utilisateur
                m_Text = input.getText().toString();

                // si aucun nom de parcours n'est entrée, on incrémente le compteur et on affiche de nouveau la popup
                if(m_Text.isEmpty()){
                    emptyname=1;
                    ShowAlertDialog(view);
                }else // sinon on ajoute le nom du parcours dans la textview
                {
                    emptyname=0;
                    ApiRequestPost.postParcours(context,Bdd.getValue(),idRaid,"-1",m_Text,"toto",false);
                    setNameParcours.setText(m_Text);
                }

            }
        });
        // si on appuie sur annuler, on retourne à la page de landing
        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CreateParcours.this, LandingActivity.class);
                startActivity(intent);
            }
        });
        alert.show();
    }

    public static void ShowPoste(final View view, final String response){

        //création de la popup
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Création d'un poste.");
        //indique que la popup ne peut pas disparaître si on appuie en dehors de la popup
        alert.setCancelable(false);


        Utils.debug("ShowPoste",response);
        JsonObject jsonObject = new JsonObject();
        JsonParser jsonParser = new JsonParser();

        jsonObject = (JsonObject) jsonParser.parse(response);

        final String id = jsonObject.get("id").toString();

        Utils.debug("ShowPoste",id);

        final LinearLayout finale = new LinearLayout(context);
        finale.setOrientation(LinearLayout.VERTICAL);

        final TextView info = new TextView(context);
        info.setText("Les informations seront modifiable ultérieurement");
        finale.addView(info);
        //alert.setMessage(listsport.toString());

        final LinearLayout ll1 = new LinearLayout(context);

        final TextView nomP = new TextView(context);
        nomP.setText("Merci d'indiquer le nom du poste");
        //ajouter l'edittext à la popup
        //alert.setView(nomP);
        // création d'un edittext pour récupérer le nom du parcours
        final EditText nom = new EditText(context);
        // indiquer que l'input est de type texte
        nom.setInputType(InputType.TYPE_CLASS_TEXT);
        //ajouter l'edittext à la popup
        //alert.setView(nom);

        ll1.addView(nomP);
        ll1.addView(nom);
        finale.addView(ll1);

        final LinearLayout ll2 = new LinearLayout(context);
        final TextView nombre = new TextView(context);
        nombre.setText("Nombre de bénévole");

        final EditText nombreEntry = new EditText(context);
        nombreEntry.setInputType(InputType.TYPE_CLASS_TEXT);

        ll2.addView(nombre);
        ll2.addView(nombreEntry);

        finale.addView(ll2);


        final LinearLayout ll3 = new LinearLayout(context);
        final TextView heuredebut = new TextView(context);
        heuredebut.setText("Date/heure début");

        final EditText anneedebutEntry = new EditText(context);
        anneedebutEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        anneedebutEntry.setHint("YYYY");

        final EditText moisdebutEntry = new EditText(context);
        moisdebutEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        moisdebutEntry.setHint("MM");

        final EditText joursdebutEntry = new EditText(context);
        joursdebutEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        joursdebutEntry.setHint("DD");

        final EditText heuredebutEntry = new EditText(context);
        heuredebutEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        heuredebutEntry.setHint("HH");

        final EditText minutedebutEntry = new EditText(context);
        minutedebutEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        minutedebutEntry.setHint("MM");

        ll3.addView(heuredebut);
        ll3.addView(anneedebutEntry);
        ll3.addView(moisdebutEntry);
        ll3.addView(joursdebutEntry);
        ll3.addView(heuredebutEntry);
        ll3.addView(minutedebutEntry);


        finale.addView(ll3);

        final LinearLayout ll4 = new LinearLayout(context);
        final TextView heurefin = new TextView(context);
        heurefin.setText("Date/heure début");

        final EditText anneefinEntry = new EditText(context);
        anneefinEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        anneefinEntry.setHint("YYYY");

        final EditText moisfinEntry = new EditText(context);
        moisfinEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        moisfinEntry.setHint("MM");

        final EditText joursfinEntry = new EditText(context);
        joursfinEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        joursfinEntry.setHint("DD");

        final EditText heurefinEntry = new EditText(context);
        heurefinEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        heurefinEntry.setHint("HH");

        final EditText minutefinEntry = new EditText(context);
        minutefinEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        minutefinEntry.setHint("MM");

        ll4.addView(heurefin);
        ll4.addView(anneefinEntry);
        ll4.addView(moisfinEntry);
        ll4.addView(joursfinEntry);
        ll4.addView(heurefinEntry);
        ll4.addView(minutefinEntry);

        finale.addView(ll4);


        alert.setView(finale);



        //si aucun nom de parcours n'est entré, on affiche une erreure
        if(emptynom==1){
            nomP.setError("le champ est vide");
        }
        if(emptynombre==1){
            nombre.setError("le champ est vide");
        }
        if(emptydebut==1){
            heuredebut.setError("le champ est vide");
        }
        if(emptyfin==1){
            heurefin.setError("le champ est vide");
        }

        // Effectuer une action si on appuie le bouton valider la popup
        alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //on récupère le nom du parcours entré par l'utilisateur
                m_Textnom = nom.getText().toString();
                m_Textnombre = nombreEntry.getText().toString();

                m_Textanneedebut = anneedebutEntry.getText().toString();
                m_Textmoisdebut = moisdebutEntry.getText().toString();
                m_Textjoursdebut = joursdebutEntry.getText().toString();
                m_Textheuredebut = heuredebutEntry.getText().toString();
                m_Textminutedebut = minutedebutEntry.getText().toString();

                m_Textanneefin = anneefinEntry.getText().toString();
                m_Textmoisfin = moisfinEntry.getText().toString();
                m_Textjoursfin = joursfinEntry.getText().toString();
                m_Textheurefin = heurefinEntry.getText().toString();
                m_Textminutefin = minutefinEntry.getText().toString();


                // si aucun nom de parcours n'est entrée, on incrémente le compteur et on affiche de nouveau la popup
                if(m_Textnom.isEmpty()){
                    emptynom=1;
                    ShowPoste(view, response);
                }
                else if(m_Textnombre.isEmpty()){
                    emptynombre=1;
                    ShowPoste(view, response);
                }
                else if(m_Textanneedebut.isEmpty() || m_Textmoisdebut.isEmpty() || m_Textjoursdebut.isEmpty() || m_Textheuredebut.isEmpty() || m_Textminutedebut.isEmpty() ) {
                    emptydebut=1;
                    ShowPoste(view, response);
                }

                else if(m_Textanneefin.isEmpty() || m_Textmoisfin.isEmpty() || m_Textjoursfin.isEmpty() || m_Textheurefin.isEmpty() || m_Textminutefin.isEmpty() ) {
                    emptyfin=1;
                    ShowPoste(view, response);
                }

                else // sinon on ajoute le nom du parcours dans la textview
                {
                    Utils.debug("cpt",String.valueOf(cpt));
                    emptynom=0;
                    emptynombre=0;
                    emptydebut=0;
                    emptyfin=0;

                    cpt = cpt + 1;

                    int nbbene = Integer.valueOf(m_Textnombre);
                    String heure = m_Textjoursdebut+"/"+m_Textmoisdebut+"/"+m_Textanneedebut+" "+m_Textheuredebut+":"+m_Textminutedebut;
                    String fin = m_Textjoursfin+"/"+m_Textmoisfin+"/"+m_Textanneefin+" "+m_Textheurefin+":"+m_Textminutefin;

                    ApiRequestPost.postPoste(context, Bdd.getValue(), id, m_Textnom, nbbene, heure, fin);
                    map.getOverlays().add(standarmarker3);
                    map.invalidate();

                }

            }
        });
        // si on appuie sur annuler, on retourne à la page de landing
        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               dialog.dismiss();
            }
        });
        alert.show();
    }



    public static void createParcours(JSONObject jsonObject) throws JSONException {
        String idRaid = jsonObject.getString("idRaid");
        String idParcoursPere = jsonObject.getString("idParcoursPere");
        String name = jsonObject.getString("nom");
        String type = jsonObject.getString("type");
        String etat = jsonObject.getString("etat");


        Intent intent2= new Intent(context, LandingActivity.class);
        context.startActivity(intent2);
    }

    public static void setNomParcours(String nom){
        nomParcours = nom;
    }
    public static String getNomParcours(){
        return nomParcours;
    }

    public void changeNom(){
        standarmarker3.setTitle("Nom : "+getNomParcours());
    }

    public static void creationParcours(){

    }


}

