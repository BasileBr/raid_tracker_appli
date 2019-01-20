package com.application.sed.raid_tracker_appli.helper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.application.sed.raid_tracker_appli.API.ApiRequestPost;
import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.application.sed.raid_tracker_appli.organizer.CourseActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VolunteerPreferenceActivity extends AppCompatActivity implements OnItemSelectedListener{
    private static MapView map = null;
    private static Context context;
    private Toolbar toolbar;
    private static Spinner spinner;
    private static List<String> posteRaid;
    private static ArrayList<Integer> ListIdPoste= new ArrayList<>();
    private static ArrayList<GeoPoint> ListGeopointPoste= new ArrayList<>();
    private static ArrayList<GeoPoint> finaleListGeoPoint;
    private static ArrayList<GeoPoint> listFinalePoste;
    public static int ParcoursListGeoPoint;
    private static ArrayList<GeoPoint> listFinale;
    private String getselectedposte;
    private static HashMap<String, String>getidBenevole;
    private static String iduser;
    private static String idTrace;
    private static String test2;
    private static HashMap<String, String>meMap;
    private static String token;
    private static TextView dispMission;
    private static Button submit;
    private static String idRaid;
    private static Integer stockerIdPoste;
    private static String latitudePoste1;
    private static IMapController mapController;
    public static ArrayList<GeoPoint> parcours;
    public static int compteur = 0;
    public static Marker standardmarker;
    public static Marker standardmarker1;
    public static Marker standardmarker2;
    public static GeoPoint pointa = new GeoPoint(51.489878, 6.143294);
    public static GeoPoint pointb = new GeoPoint(51.488978, 6.746994);
    public static GeoPoint geotemporaire;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_preference);

        Intent intent=getIntent();
        context = this;

        if (intent != null) {

            idRaid= intent.getStringExtra("idRaidpourVolunteer");
            Utils.debug("idRaidVolunteer",idRaid);

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

        //récupération du token
        token = Bdd.getValue();


        //récupération de l'id de l'utilisateur
        iduser = Bdd.getUserid();

        //récupération des postes à partir de l'id d'un Raid
        ApiRequestGet.getAllPostesfromOneRaid(context, token,idRaid);

        ApiRequestGet.getSpecificParcours(context, token, idRaid,"VolunteerPreferenceActivity");


        //création de la map
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        //positionnement lors de l'ouverture de la carte
        mapController = map.getController();
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



        //récupérer le TextView pour afficher la mission
        dispMission= (TextView) findViewById(R.id.displayMissions);

        //récupération du bouton pour inscrire un bénévole à un poste
        submit=(Button) findViewById(R.id.submit);


        //idraid= intent.getStringExtra("idRaid");

    }


    public static void recupParcours(String response){

        JsonParser parser = new JsonParser();
        JsonArray listPoints = (JsonArray) parser.parse(response);


        //listFinalePoste = new ArrayList<>();
        listFinale = new ArrayList<>();
        Utils.debug("recupParcours","Taille du Json : "+listPoints.size());
        Utils.debug("Nom", "listePoits "+listPoints.toString());
        GeoPoint listInter[] = new GeoPoint[listPoints.size()+3];
        GeoPoint listPoste[] = new GeoPoint[listPoints.size()+3];



        for (int k = 0; k < listPoints.size() ; k++) {

            GeoPoint newPoint;
            JsonObject myPoint = (JsonObject) listPoints.get(k);
            Utils.debug("que",myPoint.toString());

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
//        for (int k = 0; k< listPoste.length; k++){
//            if (listPoste[k] == null){
//                Utils.debug("recupParcours","Point null");
//            }
//            else {
//                listFinalePoste.add(listPoste[k]);
//            }


        Utils.debug("NomPoint", "list : " + listFinale.toString());

        trace(listFinale);
        //tracePoste(listFinalePoste);
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
                standardmarker = new Marker(map);
                standardmarker.setIcon(context.getResources().getDrawable(R.drawable.green_flag2));
                standardmarker.setPosition(myPoint);
                standardmarker.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                Utils.debug("longPressHelper", "Lat " + myPoint.getLatitude() + "long " + myPoint.getLongitude());

                standardmarker.setTitle("Point de départ" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());
                map.getOverlays().add(standardmarker);
                finaleListGeoPoint.add(myPoint);
                map.invalidate();
                setRoad(finaleListGeoPoint);

            }
            else if (i == myListe.size() -1){
                // Point d'arrivée
                Utils.debug("recupParcours", "Point de d'arrivée");
                standardmarker1 = new Marker(map);
                standardmarker1.setIcon(context.getResources().getDrawable(R.drawable.red_flag2));
                standardmarker1.setPosition(myPoint);
                standardmarker1.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                Utils.debug("longPressHelper", "Lat " + myPoint.getLatitude() + "long " + myPoint.getLongitude());

                standardmarker1.setTitle("Point d'arrivée" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());
                map.getOverlays().add(standardmarker1);
                finaleListGeoPoint.add(myPoint);
                map.invalidate();
                setRoad(finaleListGeoPoint);

            }

            else {
                // Point de Passage
//                Utils.debug("recupParcours", "Point de passage");
//                standardmarker2 = new Marker(map);
//                standardmarker2.setIcon(context.getResources().getDrawable(R.drawable.passage23));
//                standardmarker2.setPosition(myPoint);
//                standardmarker2.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
//                Utils.debug("longPressHelper", "Lat " + myPoint.getLatitude() + "long " + myPoint.getLongitude());
//
//                standardmarker2.setTitle("Point de passage" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());
//
//                map.getOverlays().add(standardmarker2);
                finaleListGeoPoint.add(myPoint);
               // map.invalidate();
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
                new PerformCalculations().execute(pointa, pointb);
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
                new PerformCalculations().execute(geotemporaire, parcours.get(1));

            }

        }
    }

    private static class PerformCalculations extends AsyncTask<GeoPoint,Void,Polyline> {
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

        }
        /*
        affiche la ligne entre deux points
        */
        protected void onPostExecute(Polyline line) {

            map.getOverlays().add(line);
            map.invalidate();
        }
    }




    //méthode pour récupérer l'élement selectionné dans la liste des postes
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Object item = parent.getItemAtPosition(position);
        getselectedposte=item.toString();
        Utils.debug("Item", String.valueOf(position));

        stockerIdPoste=ListIdPoste.get(position);

        //centrer la map en fonction du poste selectionné dans le menu déroulant
        mapController.setCenter(ListGeopointPoste.get(position));

        //lors de la selection d'un poste, on affiche la mission associée
        ApiRequestGet.getMissionsofOnePoste(context,token, ListIdPoste.get(position));

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


        //parcours la liste avec le Json
        for (int i = 0; i < posteliste.size(); i ++) {

            JsonParser parser1 = new JsonParser();
            JsonObject raid = (JsonObject) posteliste.get(i);

            //récupération de l'id de point d'un poste
            JsonObject deuxiem=raid.getAsJsonObject("idPoint");

             String test=deuxiem.get("id").toString();


            String type = raid.get("type").toString().replace("\"", " ");;
            Integer ListIdPoste2= raid.get("id").getAsInt();
            Double longitudePoste = deuxiem.get("lon").getAsDouble();
            Double latitudePoste = deuxiem.get("lat").getAsDouble();

            GeoPoint geoposte = new GeoPoint(latitudePoste,longitudePoste);


            Marker markerPoste = new Marker(map);
            markerPoste.setIcon(context.getResources().getDrawable(R.drawable.poi1));
            markerPoste.setPosition(geoposte);
            markerPoste.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
            markerPoste.setTitle("Poste : "+type+"\n"+"latitude : "+latitudePoste +"\n"+"longitude : "+longitudePoste);
            map.getOverlays().add(markerPoste);


            posteRaid.add(type);

            ListIdPoste.add(ListIdPoste2);

            ListGeopointPoste.add(geoposte);

        }
        createSpinner(posteRaid);

    }



    //afficher la mission associée au poste selectionné
    public static void getMission(String response){

        JsonParser parser = new JsonParser();
        JsonArray mission = (JsonArray) parser.parse(response);
        //Utils.debug(" + size", "size : " + posteliste.size() + " raidlist : "+posteliste.toString());
       // posteRaid = new ArrayList<>();

       //String missionassocie= mission.get(2).toString();

        JsonObject miss = (JsonObject) mission.get(0);

        String missionDescription=miss.get("objectif").toString().replace("\"", " ");
        //récupération de l'id de point d'un poste
       Utils.debug("affichage_mission",missionDescription);

       dispMission.setText(missionDescription);
    }

    //ajouter un bénévole à un raid
    public static void  validerPreference(View view) {
        ApiRequestPost.postNewBenevole(context, token, idRaid, iduser);
        Intent intent = new Intent(context, LandingActivity.class);
        //intent.putExtra("idRaid", idraid);
        context.startActivity(intent);


    }

    public static void afficheParcours(String response) {

        // listeIdParcours= new ArrayList<>();

        // ArrayList<Button> listButton = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonArray parcourslist = (JsonArray) parser.parse(response);

        for (int i = 0; i < parcourslist.size(); i++) {
            JsonObject parcours = (JsonObject) parcourslist.get(i);
            String idParcours = parcours.get("id").toString();
            ApiRequestGet.getSpecificTraceFromParcours(context, Bdd.getValue(), idParcours, "VolunteerPreferenceActivity");

        }
    }

    public static void recupTrace(String response) {
        JsonArray jsonArray;
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject;
        jsonArray = (JsonArray) jsonParser.parse(response);

        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = (JsonObject) jsonArray.get(i);
            String idTrace2 = jsonObject.get("id").toString();

            ApiRequestGet.getPointsfromSpecificTrace(context,Bdd.getValue(),idTrace2,"VolunteerPreferenceActivity");

            //ApiRequestGet.getPointsfromSpecificTrace(context, Bdd.getValue(), idTrace, "ManageParcoursActivity");

        }
    }

    //récupération de l'id bénévole d'un utilisateur
    public static void recupId(String response) {

        JsonParser parser = new JsonParser();
        JsonObject RepAjoutUser = (JsonObject) parser.parse(response);

        Utils.debug("recupId",RepAjoutUser.get("id").toString());


        //ajouter la préférence de poste
        ApiRequestPost.postPrefPostes(context,token,stockerIdPoste,RepAjoutUser.get("id").toString());

        Utils.debug("idPoste", response);

    }


}
