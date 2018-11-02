package com.application.sed.raid_tracker_appli;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

import okhttp3.Route;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Route;


//piste garder les éléments lors d'un changement d'orientation setRetainInstance
public class CreateParcours extends Activity implements MapEventsReceiver {
    MapView map = null;
    private ArrayList<GeoPoint> ListGeoPoint = new ArrayList<>();
    int ParcoursListGeoPoint = 0;
    MyLocationNewOverlay mLocationOverlay;

    Marker standardmarker; // = new Marker(map);
    Marker standardmarker1; // = new Marker(map);
    Marker standardmarker2;
    Marker standarmarker3;

    GeoPoint pointa = new GeoPoint(51.489878, 6.143294);
    GeoPoint pointb = new GeoPoint(51.488978, 6.746994);
    GeoPoint geotemporaire;

    int numbouton = 0;
    int compteur=0;

    public static ArrayList<List> Liste =new ArrayList<>();
    ArrayList<GeoPoint> parcours = new ArrayList<>();


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



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
        setContentView(R.layout.activity_create_parcours);

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
        GeoPoint centermap = new GeoPoint(48.732084,-3.4591440000000375);
        mapController.setCenter(centermap);

        //géolocaliser l'appareil
        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), map);
        mLocationOverlay.enableMyLocation();
        map.getOverlays().add(mLocationOverlay);

        /*
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx),map);
        this.mLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.mLocationOverlay);*/

        // ajouter l'echelle
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(map);
        map.getOverlays().add(myScaleBarOverlay);

        // ajouter boussolle
        CompassOverlay mCompassOverlay = new CompassOverlay(getApplicationContext(), new InternalCompassOrientationProvider(getApplicationContext()), map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(mCompassOverlay);



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



        //récupère les id des boutons
        final ImageButton greenflag = (ImageButton) findViewById(R.id.greenflag);
        final ImageButton redflag = (ImageButton) findViewById(R.id.redflag);
        final ImageButton passagepoint = (ImageButton) findViewById(R.id.passagepoint);
        final ImageButton poi = (ImageButton) findViewById(R.id.poi);



        //action listener sur le drapeau de depart
        greenflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.info("coucou", "ouiii");
                numbouton = 1;
                greenflag.setBackgroundColor(Color.rgb(209, 196, 190));
                redflag.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                passagepoint.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                poi.setBackgroundColor(getResources().getColor(R.color.Blancnacre));

                //map.getOverlays().add(standardmarker);

            }
        });


        //action listenner sur le drapeau d'arrivé

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

        //action listener sur les points d'interets
//        redflag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.info("coucou", "rouuuuge");
//                standardmarker1 = new Marker(map);
//                numbouton = 2;
//                standardmarker1.setIcon(getResources().getDrawable(R.drawable.red_flag));
//                //map.getOverlays().add(standardmarker1);
//            }
//        });

        //action listener sur les points de passage
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

        //action listener sur les points de passage
        poi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numbouton = 4;

                poi.setBackgroundColor(Color.rgb(209, 196, 190));
                greenflag.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                passagepoint.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                redflag.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                //poi.setBackgroundColor(Color.rgb(209, 196, 190));
                //map.getOverlays().add(standardmarker1);
            }
        });



        //ajouter marqueur
//        GeoPoint enssatpoint =  new GeoPoint(48.729673,-3.4624261999999817);
//
//        Marker startMarker = new Marker(map);
//        startMarker.setPosition(enssatpoint);
//        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//        String latitude=String.valueOf(enssatpoint.getLatitude());
//        String longitude=String.valueOf(enssatpoint.getLongitude());
//        startMarker.setTitle("ENSSAT"+"\n"+"latitude: "+latitude+'\n'+"longitude: "+longitude);
//
//        //ajouter un icone particuliere
//        startMarker.setIcon(getResources().getDrawable(R.drawable.pointer));
//        map.getOverlays().add(startMarker);

        //afficher une popup pour sélectionner le type de sport
        //ShowAlertDialog(map);

    }

    //effectuer des actions lors d'un appui long
    @Override public boolean longPressHelper(GeoPoint p) {

        GeoPoint tmpgeo = new GeoPoint(p.getLatitude(),p.getLongitude());
        //Marker startMarker = new Marker(map);
        //startMarker.setPosition(tmpgeo);
        String latitude=String.valueOf(p.getLatitude());
        String longitude=String.valueOf(p.getLongitude());
        switch (numbouton){


            case 0:
                break;
            case 1:

                standardmarker = new Marker(map);
                standardmarker.setIcon(getResources().getDrawable(R.drawable.green_flag));
                standardmarker.setPosition(tmpgeo);
                standardmarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);


                Utils.debug("longPressHelper","Lat "+latitude + "long " + longitude);

        //        //Liste de points
        //        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        //
        //        //waypoints.add(startPoint);
        //
        //        //waypoints.add(endPoint);

                standardmarker.setTitle("point de départ"+"\n"+"latitude: "+latitude+'\n'+"longitude: "+longitude);

                //ajouter un icone particuliere
                //startMarker.setIcon(getResources().getDrawable(R.drawable.pointer));
                map.getOverlays().add(standardmarker);
                ListGeoPoint.add(tmpgeo);
                map.invalidate();
                //setRoad();

                setRoad();

                break;
            case 2:

                standardmarker1 = new Marker(map);
                standardmarker1.setIcon(getResources().getDrawable(R.drawable.red_flag));
                standardmarker1.setPosition(tmpgeo);
                standardmarker1.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                Utils.debug("longPressHelper","Lat "+latitude + "long " + longitude);

                //        //Liste de points
                //        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                //
                //        //waypoints.add(startPoint);
                //
                //        //waypoints.add(endPoint);

                standardmarker1.setTitle("point d'arrivée"+"\n"+"latitude: "+latitude+'\n'+"longitude: "+longitude);

                //ajouter un icone particuliere
                //startMarker.setIcon(getResources().getDrawable(R.drawable.pointer));
                map.getOverlays().add(standardmarker1);
                ListGeoPoint.add(tmpgeo);
                map.invalidate();
                setRoad();

                break;

            case 3:
                standardmarker2 = new Marker(map);
                standardmarker2.setIcon(getResources().getDrawable(R.drawable.passage));
                standardmarker2.setPosition(tmpgeo);
                standardmarker2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                Utils.debug("longPressHelper","Lat "+latitude + "long " + longitude);

                //        //Liste de points
                //        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                //
                //        //waypoints.add(startPoint);
                //
                //        //waypoints.add(endPoint);

                standardmarker2.setTitle("nouveau point de passage"+"\n"+"latitude: "+latitude+'\n'+"longitude: "+longitude);

                //ajouter un icone particuliere
                //startMarker.setIcon(getResources().getDrawable(R.drawable.pointer));
                map.getOverlays().add(standardmarker2);
                ListGeoPoint.add(tmpgeo);
                map.invalidate();
                setRoad();
                break;


            case 4:
                standarmarker3 = new Marker(map);
                standarmarker3.setIcon(getResources().getDrawable(R.drawable.poi));
                standarmarker3.setPosition(tmpgeo);
                standarmarker3.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                Utils.debug("longPressHelper","Lat "+latitude + "long " + longitude);

                //        //Liste de points
                //        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                //
                //        //waypoints.add(startPoint);
                //
                //        //waypoints.add(endPoint);

                standarmarker3.setTitle("point d'intérêt"+"\n"+"latitude: "+latitude+'\n'+"longitude: "+longitude);

                //ajouter un icone particuliere
                //startMarker.setIcon(getResources().getDrawable(R.drawable.pointer));
                map.getOverlays().add(standarmarker3);
                ListGeoPoint.add(tmpgeo);
                map.invalidate();
                //setRoad();
                break;


        }


        return false;



    }

    /**
     * algo pour ajouter les points d'un parcours et envoi du traitement dans l'asynctask
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
    private String m_Text = "";
    /**
     * création de la popup
     * @param view
     */
    public void ShowAlertDialog(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Nom du parcours");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alert.setView(input);

        // Set up the buttons
        alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
            }
        });
        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CreateParcours.this, LandingActivity.class);
                startActivity(intent);
            }
        });

        alert.show();
    }





}

