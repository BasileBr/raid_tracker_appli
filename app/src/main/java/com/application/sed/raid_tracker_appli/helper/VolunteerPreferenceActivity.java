package com.application.sed.raid_tracker_appli.helper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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

import java.util.ArrayList;
import java.util.List;

public class VolunteerPreferenceActivity extends AppCompatActivity implements OnItemSelectedListener {

    private static String TAG = "VolunteerPreferenceActivity";
    private static MapView map = null;
    private static Context context;
   // private static Spinner spinner;
    private static ArrayList<Integer> ListIdPoste= new ArrayList<>();
    private static ArrayList<GeoPoint> ListGeopointPoste= new ArrayList<>();

    private static ArrayList<Integer> Posteselectionnechoix1= new ArrayList<>();
    private static ArrayList<Integer> Posteselectionnechoix2= new ArrayList<>();
    private static ArrayList<Integer> Posteselectionnechoix3= new ArrayList<>();
    private static ArrayList<Integer> Posteselectionnechoix4= new ArrayList<>();
    private static ArrayList<Integer> Posteselectionnechoix5= new ArrayList<>();


    public static int ParcoursListGeoPoint;
    private static String iduser;
    private static String token;
    private static TextView dispMission;
    private static Button submit;
    private static String idRaid;
    private static Integer stockerIdPoste;
    private static IMapController mapController;
    public static ArrayList<GeoPoint> parcours;
    public static int compteur = 0;
    public static Marker standardmarker;
    public static Marker standardmarker1;
    public static GeoPoint pointa = new GeoPoint(51.489878, 6.143294);
    public static GeoPoint pointb = new GeoPoint(51.488978, 6.746994);
    public static GeoPoint geotemporaire;
    private static LinearLayout parent;
    public static Spinner choix1;
    public static Spinner choix2;
    public static Spinner choix3;
    public static Spinner choix4;
    public static Spinner choix5;

    private static TextView missionschoix1;
    private static TextView missionschoix2;
    private static TextView missionschoix3;
    private static TextView missionschoix4;
    private static TextView missionschoix5;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.info(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_preference);

        Intent intent=getIntent();
        context = this;

        if (intent != null) {

            idRaid= intent.getStringExtra("idRaidpourVolunteer");

            Toolbar toolbar = findViewById(R.id.toolbar);
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
       // spinner = findViewById(R.id.spinner);

        // Spinner click listener
//        choix1.setOnItemSelectedListener(this);

        parent = findViewById(R.id.parent);


        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        TextView choixposte = new TextView(context);
        choixposte.setText("Choix poste");

        TextView missions = new TextView(context);
        missions.setText("Missions");
        ll.addView(choixposte);
        ll.addView(missions);
        parent.addView(ll);

        //choix 1
//        LinearLayout ll2 = new LinearLayout(context);
//        ll2.setOrientation(LinearLayout.HORIZONTAL);
//        Spinner choix1 = new Spinner(context);
//        List<String> listchoix1 = new ArrayList<String>();
//        listchoix1.add("list 1");
//        listchoix1.add("list 2");
//        listchoix1.add("list 3");
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, listchoix1);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        choix1.setAdapter(dataAdapter);
//        TextView missionschoix1 = new TextView(context);
//        missionschoix1.setText("faire crepe");
//        ll2.addView(choix1);
//        ll2.addView(missionschoix1);
//        parent.addView(ll2);

        //choix 2
//        LinearLayout ll3 = new LinearLayout(context);
//        ll3.setOrientation(LinearLayout.HORIZONTAL);
//        Spinner choix2 = new Spinner(context);
//        List<String> listchoix2 = new ArrayList<String>();
//        listchoix2.add("list 1");
//        listchoix2.add("list 2");
//        listchoix2.add("list 3");
//        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, listchoix2);
//        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        choix2.setAdapter(dataAdapter2);
//        TextView missionschoix2 = new TextView(context);
//        missionschoix2.setText("faire crepe");
//        ll3.addView(choix2);
//        ll3.addView(missionschoix2);
//        ll3.setPadding(0,20,0,0);
//        parent.addView(ll3);




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
        map = findViewById(R.id.map);
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
        //dispMission= (TextView) findViewById(R.id.displayMissions);

        //récupération du bouton pour inscrire un bénévole à un poste
        submit = (Button) findViewById(R.id.submit);
        Posteselectionnechoix1.add(0);
        Posteselectionnechoix1.add(0);

        Posteselectionnechoix2.add(0);
        Posteselectionnechoix2.add(0);

        Posteselectionnechoix3.add(0);
        Posteselectionnechoix3.add(0);

        Posteselectionnechoix4.add(0);
        Posteselectionnechoix4.add(0);

        Posteselectionnechoix5.add(0);
        Posteselectionnechoix5.add(0);


    }


    public static void recupParcours(String response){

        JsonParser parser = new JsonParser();
        JsonArray listPoints = (JsonArray) parser.parse(response);

        //listFinalePoste = new ArrayList<>();
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
                //nothing to do
            }
            else {
                listFinale.add(listInter[k]);
            }
        }
        trace(listFinale);
    }

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
                finaleListGeoPoint.add(myPoint);
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
                new PerformCalculations().execute(pointa, pointb);
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
                new PerformCalculations().execute(geotemporaire, parcours.get(1));
            }
        }
    }

    /**
     *
     */
    private static class PerformCalculations extends AsyncTask<GeoPoint,Void,Polyline> {
        @Override
        protected Polyline doInBackground(GeoPoint[] params) {
            Polyline line = new Polyline(map);
            line .addPoint(params[0]);
            line .addPoint(params[1]);
            line.setWidth(3);

            return line;

        }
        /**
        * affiche la ligne entre deux points
        */
        protected void onPostExecute(Polyline line) {
            map.getOverlays().add(line);
            map.invalidate();
        }
    }

    /**
     * méthode pour récupérer l'élement selectionné dans la liste des postes
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinner1:

                if (choix1.getSelectedItem() == "select an item" ){
                    missionschoix1.setText("choix poste 1");
                    missionschoix1.setError(null);
                }else if(Posteselectionnechoix2.get((Posteselectionnechoix2.size())-1) == ListIdPoste.get(position-1)
                        || Posteselectionnechoix3.get((Posteselectionnechoix3.size())-1) == ListIdPoste.get(position-1)
                        ||Posteselectionnechoix4.get((Posteselectionnechoix4.size())-1) == ListIdPoste.get(position-1)
                        ||Posteselectionnechoix5.get((Posteselectionnechoix5.size())-1) == ListIdPoste.get(position-1)) {

                    missionschoix1.setError("poste déjà selectionné dans une autre liste");
                    missionschoix1.setText("choix poste 1");

                }
                else
                {

                    Utils.debug("test", " liste 2 " + Posteselectionnechoix2.get((Posteselectionnechoix2.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix2.size() );
                    Utils.debug("test", " liste 3 " + Posteselectionnechoix3.get((Posteselectionnechoix3.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix3.size() );
                    Utils.debug("test", " liste 4 " + Posteselectionnechoix4.get((Posteselectionnechoix4.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix4.size() );
                    Utils.debug("test", " liste 5 " + Posteselectionnechoix5.get((Posteselectionnechoix5.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix5.size() );

                    Object item = parent.getItemAtPosition(position);
                    String getselectedposte = item.toString();

                    Utils.debug("spinner1", getselectedposte);

                    missionschoix1.setError(null);
                    stockerIdPoste = ListIdPoste.get(position-1);


                    Posteselectionnechoix1.add(ListIdPoste.get(position-1));

                    Utils.debug(TAG, Posteselectionnechoix1.toString());


                    //centrer la map en fonction du poste selectionné dans le menu déroulant
                    mapController.setCenter(ListGeopointPoste.get(position-1));

                    //lors de la selection d'un poste, on affiche la mission associée
                    ApiRequestGet.getMissionsofOnePoste(context, token, (ListIdPoste.get(position-1)), "1");

                }
                break;

                case R.id.spinner2:

                    if (choix2.getSelectedItem() == "select an item" ){
                        missionschoix2.setText("choix poste 2");
                        missionschoix2.setError(null);
                    }
                    else if(Posteselectionnechoix1.get((Posteselectionnechoix1.size())-1) == ListIdPoste.get(position-1)
                            || Posteselectionnechoix3.get((Posteselectionnechoix3.size())-1) == ListIdPoste.get(position-1)
                            ||Posteselectionnechoix4.get((Posteselectionnechoix4.size())-1) == ListIdPoste.get(position-1)
                            ||Posteselectionnechoix5.get((Posteselectionnechoix5.size())-1) == ListIdPoste.get(position-1))
                    {
                        missionschoix2.setError("poste déjà selectionné dans une autre liste");
                        missionschoix2.setText("choix poste 2");
                    }

                    else {
                        Object item2 = parent.getItemAtPosition(position);
                        String getselectedposte2 = item2.toString();
                        Utils.debug("spinner2", getselectedposte2);

                        Utils.debug("test", " liste 1 " + Posteselectionnechoix1.get((Posteselectionnechoix1.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix1.size() );
                        Utils.debug("test", " liste 3 " + Posteselectionnechoix3.get((Posteselectionnechoix3.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix3.size() );
                        Utils.debug("test", " liste 4 " + Posteselectionnechoix4.get((Posteselectionnechoix4.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix4.size() );
                        Utils.debug("test", " liste 5 " + Posteselectionnechoix5.get((Posteselectionnechoix5.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix5.size() );

                        missionschoix2.setError(null);
                        stockerIdPoste = ListIdPoste.get(position-1);

                        Posteselectionnechoix2.add(ListIdPoste.get(position - 1));
                        Utils.debug(TAG, Posteselectionnechoix2.toString());

                        //centrer la map en fonction du poste selectionné dans le menu déroulant
                        mapController.setCenter(ListGeopointPoste.get(position-1));


                        //lors de la selection d'un poste, on affiche la mission associée
                        ApiRequestGet.getMissionsofOnePoste(context, token, ListIdPoste.get(position-1), "2");

                    }
                    break;
                case R.id.spinner3:

                    if (choix3.getSelectedItem() == "select an item"){
                        missionschoix3.setText("choix poste 3");
                        missionschoix3.setError(null);
                    }
                    else if(Posteselectionnechoix2.get((Posteselectionnechoix2.size())-1) == ListIdPoste.get(position-1)
                            || Posteselectionnechoix1.get((Posteselectionnechoix1.size())-1) == ListIdPoste.get(position-1)
                            ||Posteselectionnechoix4.get((Posteselectionnechoix4.size())-1) == ListIdPoste.get(position-1)
                            ||Posteselectionnechoix5.get((Posteselectionnechoix5.size())-1) == ListIdPoste.get(position-1))
                    {
                        missionschoix3.setError("poste déjà selectionné dans une autre liste");
                        missionschoix3.setText("choix poste 3");
                    }

                    else {
                        Object item3 = parent.getItemAtPosition(position);
                        String getselectedposte3 = item3.toString();
                        Utils.debug("spinner3", getselectedposte3);
                        Utils.debug("test", " liste 2 " + Posteselectionnechoix2.get((Posteselectionnechoix2.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix2.size() );
                        Utils.debug("test", " liste 1 " + Posteselectionnechoix1.get((Posteselectionnechoix1.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix1.size() );
                        Utils.debug("test", " liste 4 " + Posteselectionnechoix4.get((Posteselectionnechoix4.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix4.size() );
                        Utils.debug("test", " liste 5 " + Posteselectionnechoix5.get((Posteselectionnechoix5.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix5.size() );

                            missionschoix3.setError(null);

                        stockerIdPoste = ListIdPoste.get(position-1);

                        Posteselectionnechoix3.add(ListIdPoste.get(position-1));
                        Utils.debug(TAG, Posteselectionnechoix3.toString());


                        //centrer la map en fonction du poste selectionné dans le menu déroulant
                        mapController.setCenter(ListGeopointPoste.get(position-1));

                        //lors de la selection d'un poste, on affiche la mission associée
                        ApiRequestGet.getMissionsofOnePoste(context, token, ListIdPoste.get(position-1), "3");
                    }
                    break;
            case R.id.spinner4:

                if (choix4.getSelectedItem() == "select an item"){
                    missionschoix4.setText("choix poste 4");
                    missionschoix4.setError(null);
                }else if(Posteselectionnechoix2.get((Posteselectionnechoix2.size())-1) == ListIdPoste.get(position-1)
                        || Posteselectionnechoix3.get((Posteselectionnechoix3.size())-1) == ListIdPoste.get(position-1)
                        ||Posteselectionnechoix1.get((Posteselectionnechoix1.size())-1) == ListIdPoste.get(position-1)
                        ||Posteselectionnechoix5.get((Posteselectionnechoix5.size())-1) == ListIdPoste.get(position-1)) {

                    missionschoix4.setError("poste déjà selectionné dans une autre liste");
                    missionschoix4.setText("choix poste 4");

                }else {
                    Object item4 = parent.getItemAtPosition(position);
                    String getselectedposte4 = item4.toString();
                    Utils.debug("spinner4", getselectedposte4);

                    Utils.debug("test", " liste 2 " + Posteselectionnechoix2.get((Posteselectionnechoix2.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix2.size() );
                    Utils.debug("test", " liste 3 " + Posteselectionnechoix3.get((Posteselectionnechoix3.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix3.size() );
                    Utils.debug("test", " liste 1 " + Posteselectionnechoix1.get((Posteselectionnechoix1.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix1.size() );
                    Utils.debug("test", " liste 5 " + Posteselectionnechoix5.get((Posteselectionnechoix5.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix5.size() );


                    missionschoix4.setError(null);
                    stockerIdPoste = ListIdPoste.get(position-1);

                    Posteselectionnechoix4.add(ListIdPoste.get(position - 1));
                    Utils.debug(TAG, Posteselectionnechoix4.toString());


                    //centrer la map en fonction du poste selectionné dans le menu déroulant
                    mapController.setCenter(ListGeopointPoste.get(position - 1));

                    //lors de la selection d'un poste, on affiche la mission associée
                    ApiRequestGet.getMissionsofOnePoste(context, token, ListIdPoste.get(position - 1), "4");
                }
                    break;


                case R.id.spinner5:
                    if (choix5.getSelectedItem() == "select an item" ){
                        missionschoix5.setText("choix poste 5");
                        missionschoix5.setError(null);
                    }else if(Posteselectionnechoix2.get((Posteselectionnechoix2.size())-1) == ListIdPoste.get(position-1)
                            || Posteselectionnechoix3.get((Posteselectionnechoix3.size())-1) == ListIdPoste.get(position-1)
                            ||Posteselectionnechoix4.get((Posteselectionnechoix4.size())-1) == ListIdPoste.get(position-1)
                            ||Posteselectionnechoix1.get((Posteselectionnechoix1.size())-1) == ListIdPoste.get(position-1)) {

                        missionschoix5.setError("poste déjà selectionné dans une autre liste");
                        missionschoix5.setText("choix poste 5");
                    }
                    else {

                        Object item5 = parent.getItemAtPosition(position);
                        String getselectedposte5 = item5.toString();
                        Utils.debug("spinner3=5", getselectedposte5);

                        Utils.debug("test", " liste 2 " + Posteselectionnechoix2.get((Posteselectionnechoix2.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix2.size() );
                        Utils.debug("test", " liste 3 " + Posteselectionnechoix3.get((Posteselectionnechoix3.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix3.size() );
                        Utils.debug("test", " liste 4 " + Posteselectionnechoix4.get((Posteselectionnechoix4.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix4.size() );
                        Utils.debug("test", " liste 1 " + Posteselectionnechoix1.get((Posteselectionnechoix1.size())-1).toString() + " Taille de la liste " + Posteselectionnechoix1.size() );


                        missionschoix5.setError(null);
                        stockerIdPoste = ListIdPoste.get(position-1);

                        Posteselectionnechoix5.add(ListIdPoste.get(position - 1));
                        Utils.debug(TAG, Posteselectionnechoix5.toString());

                        //centrer la map en fonction du poste selectionné dans le menu déroulant
                        mapController.setCenter(ListGeopointPoste.get(position - 1));

                        //lors de la selection d'un poste, on affiche la mission associée
                        ApiRequestGet.getMissionsofOnePoste(context, token, ListIdPoste.get(position - 1), "5");
                    }
                    break;
            }
        }



    //si aucun élément n'est selectionné, là par defaut premier raid de la liste
    public void onNothingSelected(AdapterView<?> arg0) {
        switch (parent.getId()) {
            case R.id.spinner1:


        }

    }

    //afficher la liste des postes
    public static void createSpinner(List<String> posteListe){

        List<String>TestposteListemodified = new ArrayList<String>();
        TestposteListemodified.add("select an item");
        TestposteListemodified.addAll(posteListe);



        //choix 1
        LinearLayout ll2 = new LinearLayout(context);
        ll2.setOrientation(LinearLayout.HORIZONTAL);
        choix1 = new Spinner(context);
        choix1.setId(R.id.spinner1);
        choix1.setOnItemSelectedListener((OnItemSelectedListener) context);


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, TestposteListemodified);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choix1.setAdapter(dataAdapter);

        missionschoix1 = new TextView(context);
        missionschoix1.setText("faire crepe");
        ll2.addView(choix1);
        ll2.addView(missionschoix1);
        parent.addView(ll2);




//
//        choix1.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                //centrer la map en fonction du poste selectionné dans le menu déroulant
//                mapController.setCenter(ListGeopointPoste.get(position));
//
//                //lors de la selection d'un poste, on affiche la mission associée
//                ApiRequestGet.getMissionsofOnePoste(context, token, ListIdPoste.get(position),"1");            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//
//            }
//
//        });




        //choix 2
        LinearLayout ll3 = new LinearLayout(context);
        ll3.setOrientation(LinearLayout.HORIZONTAL);
        choix2 = new Spinner(context);
        choix2.setId(R.id.spinner2);
        choix2.setOnItemSelectedListener((OnItemSelectedListener) context);


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, TestposteListemodified);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choix2.setAdapter(dataAdapter2);


        missionschoix2 = new TextView(context);
        missionschoix2.setText("faire crepe");
        ll3.addView(choix2);
        ll3.addView(missionschoix2);
        parent.addView(ll3);
//
//        choix2.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                //centrer la map en fonction du poste selectionné dans le menu déroulant
//                mapController.setCenter(ListGeopointPoste.get(position));
//
//                //lors de la selection d'un poste, on affiche la mission associée
//                ApiRequestGet.getMissionsofOnePoste(context, token, ListIdPoste.get(position),"2");            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });





        //choix 3
        LinearLayout ll4 = new LinearLayout(context);
        ll4.setOrientation(LinearLayout.HORIZONTAL);
        choix3 = new Spinner(context);
        choix3.setId(R.id.spinner3);
        choix3.setOnItemSelectedListener((OnItemSelectedListener) context);


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, TestposteListemodified);

        // Drop down layout style - list view with radio button
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choix3.setAdapter(dataAdapter3);


        missionschoix3 = new TextView(context);
        missionschoix3.setText("faire crepe");
        ll4.addView(choix3);
        ll4.addView(missionschoix3);
        parent.addView(ll4);
//
//        choix3.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                //centrer la map en fonction du poste selectionné dans le menu déroulant
//                mapController.setCenter(ListGeopointPoste.get(position));
//
//                //lors de la selection d'un poste, on affiche la mission associée
//                ApiRequestGet.getMissionsofOnePoste(context, token, ListIdPoste.get(position),"3");            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });




        //choix 4
        LinearLayout ll5 = new LinearLayout(context);
        ll5.setOrientation(LinearLayout.HORIZONTAL);
        choix4 = new Spinner(context);
        choix4.setId(R.id.spinner4);
        choix4.setOnItemSelectedListener((OnItemSelectedListener) context);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, TestposteListemodified);

        // Drop down layout style - list view with radio button
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choix4.setAdapter(dataAdapter4);

        missionschoix4 = new TextView(context);
        missionschoix4.setText("faire crepe");
        ll5.addView(choix4);
        ll5.addView(missionschoix4);
        parent.addView(ll5);
//
//        choix4.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                //centrer la map en fonction du poste selectionné dans le menu déroulant
//                mapController.setCenter(ListGeopointPoste.get(position));
//
//                //lors de la selection d'un poste, on affiche la mission associée
//                ApiRequestGet.getMissionsofOnePoste(context, token, ListIdPoste.get(position),"4");            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });




        //choix 5
        LinearLayout ll6 = new LinearLayout(context);
        ll6.setOrientation(LinearLayout.HORIZONTAL);
        choix5 = new Spinner(context);
        choix5.setId(R.id.spinner5);
        choix5.setOnItemSelectedListener((OnItemSelectedListener) context);


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, TestposteListemodified);

        // Drop down layout style - list view with radio button
        dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choix5.setAdapter(dataAdapter5);


        missionschoix5 = new TextView(context);
        missionschoix5.setText("faire crepe");
        ll6.addView(choix5);
        ll6.addView(missionschoix5);
        parent.addView(ll6);


        missionschoix1.setError(null);
        missionschoix2.setError(null);
        missionschoix3.setError(null);
        missionschoix4.setError(null);
        missionschoix5.setError(null);
//        choix5.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                //centrer la map en fonction du poste selectionné dans le menu déroulant
//                mapController.setCenter(ListGeopointPoste.get(position));
//
//                //lors de la selection d'un poste, on affiche la mission associée
//                ApiRequestGet.getMissionsofOnePoste(context, token, ListIdPoste.get(position),"5");            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });



        // attaching data adapter to spinner
            //spinner.setAdapter(dataAdapter);

    }

    //récupérer la liste des postes d'un raid
    public static void posteListe(String response){

        JsonParser parser = new JsonParser();
        JsonArray posteliste = (JsonArray) parser.parse(response);
        List<String> posteRaid = new ArrayList<>();

        //parcours la liste avec le Json
        for (int i = 0; i < posteliste.size(); i ++) {

            JsonObject raid = (JsonObject) posteliste.get(i);

            //récupération de l'id de point d'un poste
            JsonObject deuxiem=raid.getAsJsonObject("idPoint");

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

    /**
     * afficher la mission associée au poste selectionné
     * @param response
     */
    public static void getMission(String response, String spinner){
        Utils.debug("getmission",spinner);
        JsonParser parser = new JsonParser();
        JsonArray mission = (JsonArray) parser.parse(response);
        JsonObject miss = (JsonObject) mission.get(0);
        String missionDescription=miss.get("objectif").toString().replace("\"", " ");

        switch (spinner) {
            case "1":
                Utils.debug("cas1",spinner);
                missionschoix1.setText(missionDescription);
                break;
            case "2":
                Utils.debug("cas2",spinner);
                missionschoix2.setText(missionDescription);
                break;
            case "3" :
                Utils.debug("cas3",spinner);
                missionschoix3.setText(missionDescription);
                break;
            case "4" :
                Utils.debug("cas4",spinner);
                missionschoix4.setText(missionDescription);
                break;
            case "5" :
                Utils.debug("cas5",spinner);
                missionschoix5.setText(missionDescription);
                break;
        }



//        String missionDescription=miss.get("objectif").toString().replace("\"", " ");
//        missionschoix2.setText(missionDescription);
    }

    /**
     * ajouter un bénévole à un raid
     * @param view
     */
    public static void  validerPreference(View view) {

        // Tester si les erreurs ne sont pas null

        if (missionschoix1.getError() == null &&
                missionschoix2.getError() == null &&
                missionschoix3.getError() == null &&
                missionschoix4.getError() == null &&
                missionschoix5.getError() == null
                ) {
            submit.setError(null);
            //ApiRequestPost.postNewBenevole(context, token, idRaid, iduser);
            Intent intent = new Intent(context, LandingActivity.class);
            context.startActivity(intent);
        }
        else{
            submit.setError("Il reste des erreurs sur vos choix de postes");
        }
    }

    /**
     *
     * @param response
     */
    public static void afficheParcours(String response) {

        JsonParser parser = new JsonParser();
        JsonArray parcourslist = (JsonArray) parser.parse(response);
        for (int i = 0; i < parcourslist.size(); i++) {
            JsonObject parcours = (JsonObject) parcourslist.get(i);
            String idParcours = parcours.get("id").toString();
            ApiRequestGet.getSpecificTraceFromParcours(context, Bdd.getValue(), idParcours, "VolunteerPreferenceActivity");
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
            ApiRequestGet.getPointsfromSpecificTrace(context,Bdd.getValue(),idTrace2,"VolunteerPreferenceActivity");
        }
    }

    /**
     * récupération de l'id bénévole d'un utilisateur
     * @param response
     */
    public static void recupId(String response) {

        JsonParser parser = new JsonParser();
        JsonObject RepAjoutUser = (JsonObject) parser.parse(response);

        //ajouter la préférence de poste
        ApiRequestPost.postPrefPostes(context,token,stockerIdPoste,RepAjoutUser.get("id").toString());
    }
}
