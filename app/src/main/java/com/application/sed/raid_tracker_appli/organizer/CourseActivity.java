package com.application.sed.raid_tracker_appli.organizer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.API.ApiRequestPost;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class CourseActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private static final String switch_value = "switch_value";

    private ArrayList<Button> listebouton;
    private ArrayList<List> raidlist;
    private static ArrayList<String> listeIdParcours;
    //private static ArrayList<String> listsport;

    private static Context context;


    private static LinearLayout ll;
    private static String idRaid;

    //récupération des informations du raid
    private static String id;
    private static String nom;
    private static String lieu;
    private static String date;
    private static String edition;
    private static String equipe;
    private static Boolean visibility;
    private static Switch simpleSwitch;
    private static TextView setTextVisibility;

    public static MapView map = null;

    private static String idTrace;
    private static ArrayList<GeoPoint> listFinale;
    private static ArrayList<GeoPoint> listFinalePoste;
    private static ArrayList<GeoPoint> finaleListGeoPoint;
    public static int ParcoursListGeoPoint;
    public static ArrayList<GeoPoint> parcours;
    public static int compteur = 0;
    public static Marker standardmarker;
    public static Marker standardmarker1;
    public static Marker standardmarker2;
    public static Marker standarmarker3;
    public static GeoPoint pointa = new GeoPoint(51.489878, 6.143294);
    public static GeoPoint pointb = new GeoPoint(51.488978, 6.746994);
    public static GeoPoint geotemporaire;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Utils.info("test", "Creation of the new activity");

        Intent intent=getIntent();
        context = this;

        if (intent != null) {

            Utils.debug("CourseActivity", "je rentre ici");
            idRaid = intent.getStringExtra("idRaid");
            Utils.debug("CourseActivity","idRaid = "+idRaid);
            ll = (LinearLayout) findViewById(R.id.ParcoursLayout);

            /*listsport = intent.getStringArrayListExtra("Sports");
            Utils.debug("Sports",listsport.toString());*/
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(CourseActivity.this, LandingActivity.class);
                    intent.putExtra("idRaid",idRaid);
                    startActivity(intent);

                }
            });

            // créer map
            onCreateMap();

            // bouton switch pour la visiblité du raid
            simpleSwitch = (Switch) findViewById(R.id.switchVisibility);

            //texte associé à la visibilité du raid
           setTextVisibility = (TextView) findViewById(R.id.setTextVisibility);

            //
            ApiRequestGet.getSpecificParcours(context, Bdd.getValue(), idRaid);

            //récupération des informations du raid pour ensuite exploiter la visibilité
            ApiRequestGet.getSpecificRaidforCourseActivity(context,Bdd.getValue(),idRaid,"CourseActivity");




            /*Handler myHandler = new Handler();
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // api request
                    for (int i=0;i<listeIdParcours.size();i++) {
                        ApiRequestGet.getSpecificTraceFromParcours(context,Bdd.getValue(),listeIdParcours.get(i),"CourseActivity");
                    }
                }
            },1000);
            */
        }

    }

    public void onCreateMap(){

        //création de la map
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setClickable(true);
        map.setFocusable(true);
        map.setDuplicateParentStateEnabled(false);

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

    public static void recupParcours(String response){

        JsonParser parser = new JsonParser();
        JsonArray listPoints = (JsonArray) parser.parse(response);


        listFinalePoste = new ArrayList<>();
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
                Utils.debug("recupParcours", "Point de passage");
                standardmarker2 = new Marker(map);
                standardmarker2.setIcon(context.getResources().getDrawable(R.drawable.passage23));
                standardmarker2.setPosition(myPoint);
                standardmarker2.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                Utils.debug("longPressHelper", "Lat " + myPoint.getLatitude() + "long " + myPoint.getLongitude());

                standardmarker2.setTitle("Point de passage" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());

                map.getOverlays().add(standardmarker2);
                finaleListGeoPoint.add(myPoint);
                map.invalidate();
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
            standarmarker3 = new Marker(map);
            standarmarker3.setIcon(context.getResources().getDrawable(R.drawable.poi1));
            standarmarker3.setPosition(myPoint);
            standarmarker3.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
            Utils.debug("recupPoste", "Lat " + myPoint.getLatitude() + "long " + myPoint.getLongitude());

            standarmarker3.setTitle("Poste" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());

            map.getOverlays().add(standarmarker3);

            map.invalidate();




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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit_raid) {
            //return true;
            Intent intent = new Intent(CourseActivity.this, EditCourse.class);
            intent.putExtra("idRaid",idRaid);
            startActivity(intent);
        }
        else if (id==R.id.manage_points){
           Intent intent = new Intent(CourseActivity.this, ManageVolunteersPositionActivity.class);
           intent.putExtra("idRaid",idRaid);
           startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    public void Course(View view){
        Intent intent = new Intent(CourseActivity.this, CreateParcours.class);
        intent.putExtra("idRaid",idRaid);
        //intent.putExtra("listsport",listsport);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(CourseActivity.this, LandingActivity.class);
        startActivity(intent);
    }



    public static void afficheParcours(String response){

        listeIdParcours= new ArrayList<>();

        ArrayList<Button> listButton = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonArray parcourslist = (JsonArray) parser.parse(response);

        for (int i =0; i<parcourslist.size();i++){

            Button myButton = new Button(context);
            Utils.debug("Ajout du bouton", "Je rentre dans le for "+i);

            JsonObject parcours = (JsonObject) parcourslist.get(i);
            String nomParcours = parcours.get("nom").toString().replace("\"", " ");
            String idParcours = parcours.get("id").toString();

            myButton.setText("Nom :" + nomParcours);
            myButton.setId(i);
            myButton.setTag(idParcours);

            listButton.add(myButton);

            //listeIdParcours.add(idParcours);
            ApiRequestGet.getSpecificTraceFromParcours(context,Bdd.getValue(),idParcours,"CourseActivity");

            //Utils.debug("listbutton", listButton.get(i).toString());
            //Utils.debug("listeIdParcours", listeIdParcours.get(i));
        }

        for (int i = 0; i < listButton.size(); i ++){

            Utils.debug("Rajout des boutons", "Valeurs de i" +i);
            Button myButton2 = listButton.get(i);

//                myButton2.setBackgroundColor(getColor(5));    // Ajout de la couleur en fond du bouton

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(myButton2, lp);

        }

        parcoursButton(listButton);
    }

    public static void parcoursButton(ArrayList<Button> listButton){

        for (int j = 0; j<listButton.size(); j++) {
            final Button newButton = listButton.get(j);

            newButton.setOnClickListener( new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent =  new Intent(context, ManageParcoursActivity.class);
                    String idParcours= (String) newButton.getTag();
                    Utils.debug("parcoursButton", "idRaid : "+idRaid);
                    //Id du parcours qu'on veut récupérer
                    intent.putExtra("idParcours",idParcours);
                    intent.putExtra("idRaid",idRaid);
                    context.startActivity(intent);

                }
            });
        }

    }

    public static void getRaid(String response){

        JsonParser parser = new JsonParser();
        JsonObject infoRaid = (JsonObject) parser.parse(response);
        id = infoRaid.get("id").toString();
        nom = infoRaid.get("nom").toString().replace("\"", " ");
        Utils.debug("nom",nom);
        lieu=infoRaid.get("lieu").toString().replace("\"", " ");
        String inter = infoRaid.get("date").toString();
        edition=infoRaid.get("edition").toString();
        equipe= infoRaid.get("equipe").toString().replace("\"", " ");
        visibility=infoRaid.get("visibility").getAsBoolean();
        String annee = inter.substring(1,5);
        String mois = inter.substring(6,8);
        String jours = inter.substring(9,11);
        String heure = inter.substring(12,14);
        String min = inter.substring(15,17);


        date = annee+"/"+mois+"/"+jours+" "+heure+":"+min;

            Utils.debug("yvantest",date);

            if (visibility){
                simpleSwitch.setChecked(true);
                setTextVisibility.setText(" Le raid est partagé aux bénévoles ");

            }else {
                simpleSwitch.setChecked(false);
                setTextVisibility.setText(" Le raid n'est pas partagé aux bénévoles");

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

            ApiRequestGet.getPointsfromSpecificTrace(context,Bdd.getValue(),idTrace2,"CourseActivity");

            //ApiRequestGet.getPointsfromSpecificTrace(context, Bdd.getValue(), idTrace, "ManageParcoursActivity");

        }
    }

    public void checkSwitch(View view){
        //modifier le texte le texte en fonction de l'action de l'état actuel du bouton

                //String statusSwitch1, statusSwitch2;
                if (simpleSwitch.isChecked()) {
                    setTextVisibility.setText(" Le raid est partagé aux bénévoles avec switch");

                    //nom = "ENSSAT RAID 100";
                    ApiRequestPost.postUpdateRaid(context,Bdd.getValue(),id,nom,lieu,date,edition,equipe,true);

                    //LandingActivity.diffuserRaid();
                    Intent intent = new Intent(CourseActivity.this, LandingActivity.class);
                    //intent.putExtra(switch_value,"coucou");
                    startActivity(intent);

                } else if (!simpleSwitch.isChecked()){
                    setTextVisibility.setText(" Le raid n'est pas partagé aux bénévoles avec switch");
                    //nom = "ENSSAT RAID 100";
                    ApiRequestPost.postUpdateRaid(context,Bdd.getValue(),id,nom,lieu,date,edition,equipe,false);
                    Intent intent = new Intent(CourseActivity.this, LandingActivity.class);
                    startActivity(intent);

                }
            }




}


