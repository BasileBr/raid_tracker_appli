package com.application.sed.raid_tracker_appli.helper;

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
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.API.ApiRequestPost;
import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PosteDescription extends AppCompatActivity {

    private static String TAG = "PosteDescription";
    private static String idRaidReceive = "";
    private static Context context;
    public static Double positionLatitude;
    public static Double positionLongitude;
    private static LinearLayout parentdescription;
    private static LinearLayout parentbouton;
    private static String mois;
    private static String token;
    private static String iduser;
    private static ArrayList<String> listidrepartition = new ArrayList<>();

    public static MyLocationNewOverlay mLocationOverlay;


    private static HashMap<String, Button> listButton;

    public static double latitude;
    public static double longitude;
    public static LocationManager locationManager;
    public static Criteria criteria;
    public static String bestProvider;

    public static LocationManager lm;
    private static double my_latitude = 0;
    private static double my_longitude = 0;

    private static String fournisseur;
    public static LocationListener ecouteurGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.info(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poste_description);
        context = this;
        positionLongitude = 0.0;
        positionLatitude = 0.0;



        ecouteurGPS = new LocationListener() {

            @Override
            public void onLocationChanged(Location localisation)
            {
                positionLongitude = localisation.getLatitude();
                positionLongitude = localisation.getLongitude();

            }

            @Override
            public void onProviderDisabled(String fournisseur)
            {

            }


            @Override
            public void onProviderEnabled(String fournisseur)
            {

            }

            @Override
            public void onStatusChanged(String fournisseur, int status, Bundle extras)
            {

            }
        };



        geolocateMe();


        //geolocateMe();
        //récupération de l'intent
        Intent intent = getIntent();

        //récupération du contexte


        //récupération du token
        token = Bdd.getValue();

        //récupération de l'identifiant de l'utilisateur
        iduser = Bdd.getUserid();

        //récupération de l'id du Raid depuis Landing Activity

        if (intent != null) {
            idRaidReceive = intent.getStringExtra("idRaid");
        }

        //récupération de la toolbar depuis le XML
        Toolbar toolbar = findViewById(R.id.toolbarDescr);

        //définir la toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        parentdescription = findViewById(R.id.postesDescription);
        parentbouton = findViewById(R.id.postesboutons);

        // page de retour
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PosteDescription.this, LandingActivity.class);
                startActivity(intent);

            }
        });

        //récupérer la répartition d'un utilisateur sur un raid
        ApiRequestGet.getRepartitionfromIdUserIdRaid(context, token, idRaidReceive, iduser);

    }


    public static void launcher(GeoPoint depart, GeoPoint arrivee) {
        Double latdepart = depart.getLatitude();
        Double latarrivee = arrivee.getLatitude();
        Double longdepart = depart.getLongitude();
        Double longarrivee = depart.getLongitude();

        Uri gmmIntentUri = Uri.parse("geo:" + latdepart.toString() + "," + longdepart + "?q=" + latarrivee + "," + longarrivee);
        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }

    private static void checkIn(final GeoPoint positionpostetest, final String idposte, final String idrepartition) {


        geolocateMe();

        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle("Confirmer sa localisation ?");

        //indique que la popup ne peut pas disparaître si on appuie en dehors de la popup
        alert.setCancelable(false);

        //si validation arrête de la localisation
        alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // récupération de la position en double
                Double positionPosteLatitude = positionpostetest.getLatitude();
                Double positionPosteLongitude = positionpostetest.getLongitude();

                // calcul de ratio
                Double ratiolatitude = Math.abs(positionPosteLatitude - positionLatitude);
                Double ratiolongitude = Math.abs(positionPosteLongitude - positionLongitude);


                if (ratiolatitude < 0.001 && ratiolongitude < 0.001) {
                    // requête API /api/checkin
                    Toast.makeText(context, "Votre position est confirmée ", Toast.LENGTH_LONG).show();
                    Button button = listButton.get(idposte);
                    button.setBackgroundColor(context.getResources().getColor(R.color.BleuPrimaire));
                    Date actuelle = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String dat = dateFormat.format(actuelle);
                    ApiRequestPost.postCheckin(context, token, idrepartition, dat);
                } else if (ratiolatitude < 0.002 && ratiolongitude < 0.002) {
                    // requête API /api/checkin
                    Toast.makeText(context, "Vous n'êtes pas loin, encore un petit effort ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Vous n'y êtes pas du tout ", Toast.LENGTH_LONG).show();
                }
            }
        });

        //retour à la navigation
        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }


    private static void initialiserLocalisation() {


        if (locationManager == null) {
            locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
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
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
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
        else {
        }
    }

    /**
     ** Géolocalisation de l'utilsateur
     */
    public static void geolocateMe() {
        initialiserLocalisation();


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;

        }

        Location localisation = locationManager.getLastKnownLocation(fournisseur);
        if(localisation != null)
        {
            positionLongitude = localisation.getLongitude();
            positionLatitude = localisation.getLatitude();
            //Nothing to do
        }

        // on configure la mise à jour automatique : au moins 10 mètres et 15 secondes
        locationManager.requestLocationUpdates(fournisseur, 15000, 10, ecouteurGPS);


    }
    /**
     * Traiter les informations de la répartition d'un bénévole en fonction du raid
     */
    public static void recupInfosPoste(String response) {


        //géolocaliser l'utilisateur
        geolocateMe();

        JsonParser parser = new JsonParser();
        JsonArray posteinfos = (JsonArray) parser.parse(response);

        int compteurposte;

        listButton = new HashMap<>();
        for (int i = 0; i < posteinfos.size(); i++) {
            compteurposte=i+1;
            JsonObject repartition = (JsonObject) posteinfos.get(i);
            final String idrepartition = repartition.get("id").toString();

            listidrepartition.add(idrepartition);
            JsonObject poste = repartition.getAsJsonObject("idPoste");

            final String idposte = poste.get("id").toString().replace("\""," ");
            //récupération du type du poste
            String typePoste = poste.get("type").toString().replace("\"", " ");

            //récupération de l'heure de début
            String hourdebut=poste.get("heureDebut").toString().substring(12,14);
            String minutedebut=poste.get("heureDebut").toString().substring(15,17);
            String heureDebut = hourdebut+"h"+minutedebut;

            //récupération de l'heure de fin
            String hourfin=poste.get("heureFin").toString().substring(12,14);
            String minutefin=poste.get("heureFin").toString().substring(15,17);
            String heureFin = hourfin+"h"+minutefin ;

            //récupération de la date
            String dateY=poste.get("heureFin").toString().substring(1,5);
            String dateM=poste.get("heureFin").toString().substring(6,8);
            String dateD=poste.get("heureFin").toString().substring(9,11);

            if (dateD.contains("0")) {
                dateD = dateD.replace("0", " ");
            }

            HashMap<String,String> map = new HashMap<>();
            map.put("01","Janvier");
            map.put("02","Février");
            map.put("03","Mars");
            map.put("04","Avril");
            map.put("05","Mai");
            map.put("06","Juin");
            map.put("07","Juillet");
            map.put("08","Aout");
            map.put("09","Septembre");
            map.put("10","Octobre");
            map.put("11","Novembre");
            map.put("12","Décembre");

            for (Map.Entry months : map.entrySet()){
                if (months.getKey().equals(dateM)){
                     mois = months.getValue().toString();
                }
            }

            String date=dateD+' '+mois+' '+dateY;

            //récupération des coordonnées du poste
            JsonObject coordposte = poste.getAsJsonObject("idPoint");
            final Double latitude = Double.valueOf(coordposte.get("lat").toString());
            final Double longitude = Double.valueOf(coordposte.get("lon").toString());

            TextView tv1 = new TextView(context);
            LinearLayout layout3 = new LinearLayout(context);

            //Ajoute une hauteur de 180
            if (getAndroidVersion() == 21) {
                tv1.setHeight(110);
                tv1.setWidth(35);
                layout3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 110));
                layout3.setPaddingRelative(0,20,30,0);
                tv1.setPaddingRelative(30,15,0,0);
            }
            else if (getAndroidVersion() == 28) {
                tv1.setHeight(220);
                layout3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 220));
                layout3.setPaddingRelative(0,30,0,0);
                tv1.setPaddingRelative(10,10,0,0);
            }
            else {
                tv1.setHeight(220);
                layout3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 220));
                layout3.setPaddingRelative(0,30,0,0);
                tv1.setPaddingRelative(10,10,0,0);
            }

            Button bt1 = new Button(context);
            Button bt2 = new Button(context);

            tv1.setText("Poste " +compteurposte+" : "+typePoste+"\n"+"Heure de début :  "+heureDebut+"\n"+"Heure de fin : "+heureFin+"\n"+"le "+date);
            tv1.setTextSize(15);
            bt1.setText("Me Guider");
            bt2.setText("CheckIn");
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    GeoPoint depart = new GeoPoint(positionLatitude, positionLongitude);
                    GeoPoint arrivee = new GeoPoint(latitude, longitude);
                    launcher(depart,arrivee);
                }
            });

            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    GeoPoint positionpostetest = new GeoPoint(latitude, longitude);
                    checkIn(positionpostetest,idposte, idrepartition);

                }
            });

            listButton.put(idposte,bt2);
            //Création d'un linearlayout de hauteur 180 et d'une orientation verticale

            layout3.setOrientation(LinearLayout.HORIZONTAL);

            //Ajout des boutons dans le linear layout
            layout3.addView(bt1);
            layout3.addView(bt2);

            parentdescription.addView(tv1);

            //Ajout du linear qui contient les boutons au linear de " gauche "
            parentbouton.addView(layout3);

            //récupération des id de répartation à partir de checkin

        }
        ApiRequestGet.getCheckinOneUser(context,token,iduser);
    }

    /**
     * Vérifier si le checkin a déjà été réalisé
     *
     */
    public static void checkCkeckin(String response) {

        JsonParser parser = new JsonParser();
        JsonArray checkid = (JsonArray) parser.parse(response);


        geolocateMe();
        for (int i = 0; i < checkid.size(); i++) {
            JsonObject checkin = (JsonObject) checkid.get(i);
            JsonObject idRepartition = checkin.getAsJsonObject("idRepartition");
            String verification = idRepartition.get("id").toString();

            JsonObject poste = idRepartition.getAsJsonObject("idPoste");
            String idposte  = poste.get("id").toString().replace("\""," ");

            Button button = listButton.get(idposte);
            if (button != null) {
                button.setBackgroundColor(context.getResources().getColor(R.color.VertPrimaire));
            }
        }
    }


        /**
         * Retourne la version de l'api d'Android du device
         * @return
         */
    public static int getAndroidVersion() {
        int sdkVersion = Build.VERSION.SDK_INT;
        return  sdkVersion ;
    }

}
