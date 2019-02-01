package com.application.sed.raid_tracker_appli.organizer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
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

public class CourseActivity extends AppCompatActivity {

    private static String TAG="CourseActivity";


    private static Context context;

    private static LinearLayout ll;
    private static String idRaid;
    private static String token;
    private static String Externaldate;

    //récupération des informations du raid
    private static String id;
    private static String nom;
    private static String lieu;
    private static String date;
    private static String edition;
    private static String equipe;
    private static Switch simpleSwitch;
    private static TextView setTextVisibility;

    public static MapView map = null;

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
        Utils.info(TAG, "Creation of the new activity");

        Intent intent=getIntent();
        context = this;

        if (intent != null) {

            idRaid = intent.getStringExtra("idRaid");
            ll = findViewById(R.id.ParcoursLayout);

            token = Bdd.getValue();

            Toolbar toolbar = findViewById(R.id.toolbar);
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
            simpleSwitch =findViewById(R.id.switchVisibility);

            //texte associé à la visibilité du raid
           setTextVisibility = findViewById(R.id.setTextVisibility);

            ApiRequestGet.getSpecificParcours(context, token, idRaid,"CourseActivity");

            //récupération des informations du raid pour ensuite exploiter la visibilité
            ApiRequestGet.getSpecificRaidforCourseActivity(context,token,idRaid,"CourseActivity");

        }

    }

    /**
     *
     */
    public void onCreateMap(){

        //création de la map
        map = findViewById(R.id.map);
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

    /**
     *
     * @param response
     */
    public static void recupParcours(String response){

        JsonParser parser = new JsonParser();
        JsonArray listPoints = (JsonArray) parser.parse(response);

        ArrayList<GeoPoint> listFinalePoste = new ArrayList<>();
        ArrayList<GeoPoint> listFinale = new ArrayList<>();
        GeoPoint listInter[] = new GeoPoint[listPoints.size()+3];
        GeoPoint listPoste[] = new GeoPoint[listPoints.size()+3];

        for (int k = 0; k < listPoints.size() ; k++) {
            GeoPoint newPoint;
            JsonObject myPoint = (JsonObject) listPoints.get(k);
            JsonObject jsonObject = myPoint.getAsJsonObject("idTrace");

            String idTrace = jsonObject.get("id").toString();
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
                standardmarker = new Marker(map);
                standardmarker.setIcon(context.getResources().getDrawable(R.drawable.green_flag2));
                standardmarker.setPosition(myPoint);
                standardmarker.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                standardmarker.setTitle("Point de départ" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());
                map.getOverlays().add(standardmarker);
                finaleListGeoPoint.add(myPoint);
                map.invalidate();
                setRoad(finaleListGeoPoint);
            }
            else if (i == myListe.size() -1){
                // Point d'arrivée
                standardmarker1 = new Marker(map);
                standardmarker1.setIcon(context.getResources().getDrawable(R.drawable.red_flag2));
                standardmarker1.setPosition(myPoint);
                standardmarker1.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                standardmarker1.setTitle("Point d'arrivée" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());
                map.getOverlays().add(standardmarker1);
                finaleListGeoPoint.add(myPoint);
                map.invalidate();
                setRoad(finaleListGeoPoint);
            }

            else {
                // Point de Passage
//                standardmarker2 = new Marker(map);
//                //standardmarker2.setIcon(context.getResources().getDrawable(R.drawable.passage23));
//                standardmarker2.setPosition(myPoint);
//                standardmarker2.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
//                //standardmarker2.setTitle("Point de passage" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());
//                map.getOverlays().add(standardmarker2);
                finaleListGeoPoint.add(myPoint);
                map.invalidate();
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

                //tache de fond
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
            standarmarker3 = new Marker(map);
            standarmarker3.setIcon(context.getResources().getDrawable(R.drawable.poi1));
            standarmarker3.setPosition(myPoint);
            standarmarker3.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
            standarmarker3.setTitle("Poste" + "\n" + "latitude: " + myPoint.getLatitude() + '\n' + "longitude: " + myPoint.getLongitude());
            map.getOverlays().add(standarmarker3);
            map.invalidate();
        }
    }

    /**
     *
     */
    private static class PerfomCalculations extends AsyncTask<GeoPoint,Void,Polyline> {

        @Override
        protected Polyline doInBackground(GeoPoint[] params) {
            ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
            Polyline line = new Polyline(map);
            line .addPoint(params[0]);
            line .addPoint(params[1]);
            line.setWidth(3);
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
        else if (id==R.id.listebenevole){
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
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CourseActivity.this, LandingActivity.class);
        startActivity(intent);
    }

    /**
     *
     * @param response
     */
    public static void afficheParcours(String response){


        ArrayList<Button> listButton = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonArray parcourslist = (JsonArray) parser.parse(response);

        for (int i =0; i<parcourslist.size();i++){
            Button myButton = new Button(context);

            JsonObject parcours = (JsonObject) parcourslist.get(i);
            String nomParcours = parcours.get("nom").toString().replace("\"", " ");
            String idParcours = parcours.get("id").toString();

            JsonObject raid = (JsonObject) parcours.get("idRaid");
            Utils.debug(TAG,raid.toString());


            Externaldate=raid.get("date").toString().substring(1,11);


            myButton.setText("Nom :" + nomParcours);
            myButton.setId(i);
            myButton.setTag(idParcours);

            listButton.add(myButton);

            ApiRequestGet.getSpecificTraceFromParcours(context,token,idParcours,"CourseActivity");
        }

        for (int i = 0; i < listButton.size(); i ++){
            Button myButton2 = listButton.get(i);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(myButton2, lp);
        }
        parcoursButton(listButton);
    }

    /**
     *
     * @param listButton
     */
    public static void parcoursButton(ArrayList<Button> listButton){

        for (int j = 0; j<listButton.size(); j++) {
            final Button newButton = listButton.get(j);
            newButton.setOnClickListener( new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent =  new Intent(context, ManageParcoursActivity.class);
                    String idParcours= (String) newButton.getTag();
                    //Id du parcours qu'on veut récupérer
                    intent.putExtra("idParcours",idParcours);
                    intent.putExtra("idRaid",idRaid);
                    //intent.putExtra("Externaldate",Externaldate);
                    context.startActivity(intent);
                }
            });
        }
    }

    /**
     *
     * @param response
     */
    public static void getRaid(String response){

        JsonParser parser = new JsonParser();
        JsonObject infoRaid = (JsonObject) parser.parse(response);

        id = infoRaid.get("id").toString();
        nom = infoRaid.get("nom").toString().replace("\"", " ");
        lieu=infoRaid.get("lieu").toString().replace("\"", " ");
        String inter = infoRaid.get("date").toString();
        edition=infoRaid.get("edition").toString();
        equipe= infoRaid.get("equipe").toString().replace("\"", " ");
        Boolean visibility = infoRaid.get("visibility").getAsBoolean();
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
        }
        else {
            simpleSwitch.setChecked(false);
            setTextVisibility.setText(" Le raid n'est pas partagé aux bénévoles");
        }
    }

    /**
     *
     * @param response
     */
    public static void recupTrace(String response) {
        JsonArray jsonArray;
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject;
        jsonArray = (JsonArray) jsonParser.parse(response);

        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = (JsonObject) jsonArray.get(i);
            String idTrace2 = jsonObject.get("id").toString();
            ApiRequestGet.getPointsfromSpecificTrace(context,token,idTrace2,"CourseActivity");
        }
    }

    /**
     *
     * @param view
     */
    public void checkSwitch(View view){
        //modifier le texte le texte en fonction de l'action de l'état actuel du bouton

        if (simpleSwitch.isChecked()) {
            setTextVisibility.setText(" Le raid est partagé aux bénévoles avec switch");
            ApiRequestPost.postUpdateRaid(context,token,id,nom,lieu,date,edition,equipe,true);

            Intent intent = new Intent(CourseActivity.this, LandingActivity.class);
            startActivity(intent);
        }
        else if (!simpleSwitch.isChecked()){
            setTextVisibility.setText(" Le raid n'est pas partagé aux bénévoles avec switch");

            ApiRequestPost.postUpdateRaid(context,token,id,nom,lieu,date,edition,equipe,false);
            Intent intent = new Intent(CourseActivity.this, LandingActivity.class);
            startActivity(intent);
        }
    }

}


