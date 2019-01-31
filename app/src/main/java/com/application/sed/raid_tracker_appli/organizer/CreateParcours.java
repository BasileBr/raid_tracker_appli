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
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.sed.raid_tracker_appli.API.ApiRequestDelete;
import com.application.sed.raid_tracker_appli.API.ApiRequestPost;
import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


//piste garder les éléments lors d'un changement d'orientation setRetainInstance
public class CreateParcours extends AppCompatActivity implements MapEventsReceiver {
    private String TAG = "CreateParcours";
    static MapView map = null;
    private ArrayList<GeoPoint> ListGeoPoint = new ArrayList<>();

    int ParcoursListGeoPoint = 0;

    private int emptyname=0;
    private static int emptynom=0;
    private static int emptynombre=0;
    private static int emptydebut=0;
    private static int emptyfin=0;

    private String m_Text = "";
    private static String m_Textnom ="";
    private static String m_Textnombre ="";

    /**
     *
     * Calendrier
     */
    public static int yeardebut;
    public static int monthdebut;
    public static int daydebut;
    private static int hoursdebut;
    private static int mindebut;

    public static int yearfin;
    public static int monthfin;
    public static int dayfin;
    private static int hoursfin;
    private static int minfin;
    private static String getdatedebut= "";
    private static String getdatefin = "";


    private static Context context;
    private static String idTrace;
    public static int cpt = 0;


    private static DatePickerDialog.OnDateSetListener mDateSetListenerdebut;
    private static DatePickerDialog.OnDateSetListener mDateSetListenerfin;

    public Marker standardmarker; // = new Marker(map);
    public Marker standardmarker1; // = new Marker(map);
    public Marker standardmarker2;
    public static Marker standarmarker3;
    public Toolbar toolbar1;

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
        Utils.info(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_parcours);

        //on récupère l'identifiant de la toolbar
        Intent intent = getIntent();
        if (intent != null){

            idRaid = intent.getStringExtra("idRaid");
            toolbar1 = findViewById(R.id.toolbar);

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

            //load/initialize the osmdroid configuration, this can be done
            Context ctx = getApplicationContext();
            Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

            //création de la map
            map = findViewById(R.id.map);
            map.setTileSource(TileSourceFactory.MAPNIK);
            map.setBuiltInZoomControls(true);
            map.setMultiTouchControls(true);

            //positionnement lors de l'ouverture de la carte
            IMapController mapController = map.getController();
            mapController.setZoom(9.0);
            GeoPoint centermap = new GeoPoint(48.732084, -3.4591440000000375);
            mapController.setCenter(centermap);

//            //géolocaliser l'appareil
//            MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), map);
//            mLocationOverlay.enableMyLocation();
//            map.getOverlays().add(mLocationOverlay);

            // ajouter l'echelle
            ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(map);
            map.getOverlays().add(myScaleBarOverlay);

            // ajouter boussolle
            CompassOverlay mCompassOverlay = new CompassOverlay(getApplicationContext(), new InternalCompassOrientationProvider(getApplicationContext()), map);
            mCompassOverlay.enableCompass();
            map.getOverlays().add(mCompassOverlay);

            MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
            map.getOverlays().add(0, mapEventsOverlay);


            //récupère les identifiants des drapeaux
            final ImageButton greenflag = findViewById(R.id.greenflag);
            final ImageButton redflag = findViewById(R.id.redflag);
            final ImageButton passagepoint = findViewById(R.id.passagepoint);
            final ImageButton poi = findViewById(R.id.poi);

            //action sur le drapeau de départ (ajout d'un fond ecran et fond ecran par defaut pour les autres boutons)
            greenflag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    numbouton = 2;
                    redflag.setBackgroundColor(Color.rgb(209, 196, 190));
                    greenflag.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                    passagepoint.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                    poi.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                }
            });

            //action sur le drapeau de point de passage (ajout d'un fond ecran et fond ecran par defaut pour les autres boutons)
            passagepoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numbouton = 3;
                    passagepoint.setBackgroundColor(Color.rgb(209, 196, 190));
                    greenflag.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                    redflag.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
                    poi.setBackgroundColor(getResources().getColor(R.color.Blancnacre));
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
                    standardmarker1.setTitle("Point d'arrivée" + "\n" + "latitude: " + latitude + '\n' + "longitude: " + longitude);
                    map.getOverlays().add(standardmarker1);
                    ListGeoPoint.add(tmpgeo);
                    map.invalidate();
                    setRoad();
                    break;
                }
                else if (compteurpointarrivee==1) {
                    Toast.makeText(getApplicationContext(), "Vous ne pouvez pas ajouter ce point", Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
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
                    map.getOverlays().add(standardmarker2);
                    standardmarker2.setTitle("Point de passage" + "\n" + "latitude: " + latitude + '\n' + "longitude: " + longitude);
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
                poste.setCoords(tmpgeo.getLatitude(),tmpgeo.getLongitude());
                standarmarker3.setIcon(getResources().getDrawable(R.drawable.poi1));
                standarmarker3.setPosition(tmpgeo);
                standarmarker3.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_BOTTOM);
                ApiRequestPost.postPoint(context,Bdd.getValue(),idTrace,tmpgeo.getLongitude(),tmpgeo.getLatitude(),3,cpt, map);

                standarmarker3.setTitle("nom : "+nomParcours+"\n"+"latitude: "+latitude+'\n'+"longitude: "+longitude);

                break;


        }

        // Création d'un nouveau bouton
        Button b1= findViewById(R.id.validateparcours);

        //si le point d'arrivée a été placé alors on propose d'enregistrer les points
        if (compteurpointarrivee==1) {
            b1.setVisibility(View.VISIBLE);
        }

        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int k;
                for (k = 0; k<ListGeoPoint.size(); k++ ){
                    Double lon = ListGeoPoint.get(k).getLongitude();
                    Double lat = ListGeoPoint.get(k).getLatitude();
                    if (k == 0){
                        //Point départ type = 1
                        ApiRequestPost.postPoint(context,Bdd.getValue(),idTrace,lon,lat,1, k, null);
                    }

                    else if ( ListGeoPoint.size() - k ==1){
                        //Point arrivée type = 2
                        ApiRequestPost.postPoint(context,Bdd.getValue(),idTrace,lon,lat,2, k, null);
                    }
                    else {
                        //Point passage type = 0
                        ApiRequestPost.postPoint(context,Bdd.getValue(),idTrace,lon,lat,0, k, null);
                    }

                }

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
                parcours.add(ListGeoPoint.get(i));
                pointa = parcours.get(0);
                compteur +=1;
                ParcoursListGeoPoint += 1;
            }

            //on ajoute le deuxième arraylist (parcours) puis on envoi la tache de fond à perfomCalculations
            else if (compteur==1){
                parcours.add(ListGeoPoint.get(i));
                compteur +=1;
                pointb = parcours.get(1);
                GeoPoint[] toto = new GeoPoint[2];
                toto[0] = pointa;
                toto[1] = pointb;
                ParcoursListGeoPoint += 1;
                new PerfomCalculations().execute(pointa,pointb);
            }

            // on écrase la prremiere valeur de l'arraylist et on postionne le nouveau point
            else if (compteur==2){
                //recupere le deuxieme point dans parcours
                geotemporaire = parcours.get(1);
                //on l'ajoute en écrasant l'indice 0
                parcours.add(0,geotemporaire);
                parcours.add(1,ListGeoPoint.get(i));

                ParcoursListGeoPoint += 1;
                new PerfomCalculations().execute(geotemporaire,parcours.get(1));
            }
        }

    }


    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false;
    }



    public void onResume(){
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }


    public void onPause(){
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up

    }

    /**
     ** réaliser le calcul de tracé d'une ligne entre deux points
     */
    private class PerfomCalculations extends AsyncTask<GeoPoint,Void,Polyline> {
        @Override
        protected Polyline doInBackground(GeoPoint[] params) {
            ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();

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
                }
                else // sinon on ajoute le nom du parcours dans la textview
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

    /**
     *
     * @param view
     * @param response
     */
    public static void ShowPoste(final View view, final String response){

        //création de la popup
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Création d'un poste.");
        //indique que la popup ne peut pas disparaître si on appuie en dehors de la popup
        alert.setCancelable(false);

        JsonObject jsonObject;
        JsonParser jsonParser = new JsonParser();
        jsonObject = (JsonObject) jsonParser.parse(response);

        final String idPoint = jsonObject.get("id").toString();
        final LinearLayout finale = new LinearLayout(context);
        finale.setOrientation(LinearLayout.VERTICAL);

        final TextView info = new TextView(context);
        info.setText("Les informations seront modifiable ultérieurement");
        finale.addView(info);

        final LinearLayout ll1 = new LinearLayout(context);
        final TextView nomP = new TextView(context);
        nomP.setText("Merci d'indiquer le nom du poste");

        // création d'un edittext pour récupérer le nom du parcours
        final EditText nom = new EditText(context);
        nom.setHint("              ");

        // indiquer que l'input est de type texte
        nom.setInputType(InputType.TYPE_CLASS_TEXT);

        ll1.addView(nomP);
        ll1.addView(nom);
        finale.addView(ll1);

        final LinearLayout ll2 = new LinearLayout(context);
        final TextView nombre = new TextView(context);
        nombre.setText("Nombre de bénévole");

        final EditText nombreEntry = new EditText(context);
        nombreEntry.setHint("              ");
        nombreEntry.setInputType(InputType.TYPE_CLASS_NUMBER);

        ll2.addView(nombre);
        ll2.addView(nombreEntry);

        finale.addView(ll2);

        final LinearLayout ll3 = new LinearLayout(context);
        final TextView heuredebut = new TextView(context);
        heuredebut.setText("Date/heure début");

        final TextView anneedebutEntry = new TextView(context);
        anneedebutEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        anneedebutEntry.setHint(" Choisir une date de début");
        anneedebutEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                yeardebut = cal.get(Calendar.YEAR);
                monthdebut = cal.get(Calendar.MONTH);
                daydebut = cal.get(Calendar.DAY_OF_MONTH);
                hoursdebut = cal.get(Calendar.HOUR_OF_DAY);
                mindebut = cal.get(Calendar.MINUTE);
                DatePickerDialog dialog = new DatePickerDialog(
                        context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListenerdebut, yeardebut, monthdebut, daydebut);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenerdebut = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String datedebut = dayOfMonth + "/" + (month+1) + "/" + year + " " + hoursdebut + ":"+mindebut;
                anneedebutEntry.setText(datedebut);
                getdatedebut = " "+datedebut;
                anneedebutEntry.setError(null);
            }
        };

        ll3.addView(heuredebut);
        ll3.addView(anneedebutEntry);

        finale.addView(ll3);

        final LinearLayout ll4 = new LinearLayout(context);
        final TextView heurefin = new TextView(context);
        heurefin.setText("Date/heure de fin");

        final TextView anneefinEntry = new TextView(context);
        anneefinEntry.setInputType(InputType.TYPE_CLASS_TEXT);
        anneefinEntry.setHint(" Choisir une date de fin");

        anneefinEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                yearfin= cal.get(Calendar.YEAR);
                monthfin = cal.get(Calendar.MONTH);
                dayfin = cal.get(Calendar.DAY_OF_MONTH);
                hoursfin = cal.get(Calendar.HOUR_OF_DAY);
                minfin = cal.get(Calendar.MINUTE);

                DatePickerDialog dialog = new DatePickerDialog(
                        context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListenerfin, yearfin, monthfin, dayfin);

                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenerfin = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String datefin = dayOfMonth + "/" + (month+1) + "/" + year + " " + hoursfin + ":"+minfin;
                anneefinEntry.setText(datefin);
                getdatefin = " "+datefin;
                anneefinEntry.setError(null);
            }
        };

        ll4.addView(heurefin);
        ll4.addView(anneefinEntry);
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

                // si aucun nom de parcours n'est entrée, on incrémente le compteur et on affiche de nouveau la popup
                if(m_Textnom.isEmpty()){
                    emptynom=1;
                    ShowPoste(view, response);
                }
                else if(m_Textnombre.isEmpty()){
                    emptynombre=1;
                    ShowPoste(view, response);
                }
                else if (getdatedebut.isEmpty()){
                    emptydebut=1;
                    ShowPoste(view, response);
                }
                else if (getdatefin.isEmpty()){
                    emptyfin=1;
                    ShowPoste(view, response);
                }
                else // sinon on ajoute le nom du parcours dans la textview
                {
                    emptynom=0;
                    emptynombre=0;
                    emptydebut=0;
                    emptyfin=0;
                    cpt = cpt + 1;

                    int nbbene = Integer.valueOf(m_Textnombre);
                    String debut = getdatedebut;
                    String fin = getdatefin;

                    ApiRequestPost.postPoste(context, Bdd.getValue(), idPoint, m_Textnom, nbbene, debut, fin);

                    map.getOverlays().add(standarmarker3);
                    map.invalidate();
                }
            }
        });

        // si on appuie sur annuler, on retourne à la page de landing
        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ApiRequestDelete.deleteSpecificPoint(context,Bdd.getValue(), idPoint);
                dialog.dismiss();
            }
        });
        alert.show();
    }
}

