package com.application.sed.raid_tracker_appli;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.application.sed.raid_tracker_appli.helper.VolunteerPreferenceActivity;
import com.application.sed.raid_tracker_appli.organizer.CourseActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.internal.Util;

public class PosteDescription extends AppCompatActivity {

    private static String idRaidReceive = "";
    private static Context context;
    private static String token;
    private static String iduser;
    private Toolbar toolbar;
    public static Double  positionLatitude;
    public static Double positionLongitude;
    private static LinearLayout parentdescription;
    private static LinearLayout parentbouton;
    private static String mois;
    private static ArrayList<GeoPoint> listPoste;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poste_description);

        //récupération de l'intent
        Intent intent = getIntent();

        //récupération du contexte
        context = this;

        //récupération du token
        token = Bdd.getValue();

        //récupération de l'identifiant de l'utilisateur
        iduser = Bdd.getUserid();

        Utils.debug("iduser",iduser);

        //récupération de l'id du Raid depuis Landing Activity

        if (intent != null) {
            idRaidReceive = intent.getStringExtra("idRaid");

            Utils.debug("idRaidReceive",idRaidReceive);

        }

        //récupération de la toolbar depuis le XML
        toolbar = (Toolbar) findViewById(R.id.toolbarDescr);

        //définir la toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        parentdescription = findViewById(R.id.postesDescription);
        parentbouton = findViewById(R.id.postesboutons);

       // parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
       // parent.setO(LinearLayout.VERTICAL);

        //children of parent linearlayout

       // ImageView iv = new ImageView(context);

//        LinearLayout layout2 = new LinearLayout(context);
//
//        layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        layout2.setOrientation(LinearLayout.HORIZONTAL);
//        layout2.setBackgroundColor(getResources().getColor(R.color.BleuPrimaire));
//
//       // parent.addView(iv);
//        parent.addView(layout2);
//
//        //children of layout2 LinearLayout
//
//        TextView tv1 = new TextView(context);
//        Button bt1 = new Button(context);
//        Button bt2 = new Button(context);
//        //TextView tv4 = new TextView(context);
//
//        tv1.setText("coucou");
//
//        layout2.addView(tv1);
//        layout2.addView(bt1);
//        layout2.addView(bt2);


        // page de retour
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PosteDescription.this, LandingActivity.class);
                startActivity(intent);

            }
        });

       // geolocateMe();

        //récupérer la répartition d'un utilisateur sur un raid
        ApiRequestGet.getRepartitionfromIdUserIdRaid(context, token, idRaidReceive, iduser);


    }

    public void onClickGuideMe(View view) {
        Intent intent = new Intent(PosteDescription.this, GuideMeActivity.class);
        startActivity(intent);
    }

    public static void launcher( GeoPoint depart, GeoPoint arrivee){
        Double latdepart = depart.getLatitude();
        Double latarrivee = arrivee.getLatitude();
        Double longdepart = depart.getLongitude();
        Double longarrivee = depart.getLongitude();

        Uri gmmIntentUri  = Uri.parse("geo:"+latdepart.toString()+","+longdepart+"?q="+latarrivee+","+longarrivee);
        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }

    private static void checkIn(final GeoPoint positionpostetest) {

        //géolocaliser l'utilisateur
        geolocateMe();
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle("Confirmer sa localisation ?");

        //indique que la popup ne peut pas disparaître si on appuie en dehors de la popup
        alert.setCancelable(false);

        //si validation arrête de la localisation
        alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //GeoPoint positionPoste = new GeoPoint(48.729709,  -3.465943);

                // récupération de la position en double
                Double positionPosteLatitude = positionpostetest.getLatitude();
                Double positionPosteLongitude =positionpostetest.getLongitude();

                Utils.debug("positionPosteLatitude",positionPosteLatitude.toString());
                Utils.debug("positionPosteLongitude",positionPosteLongitude.toString());


                // calcul de ratio
                Double ratiolatitude  = Math.abs(positionPosteLatitude-positionLatitude);
                Double ratiolongitude = Math.abs(positionPosteLongitude-positionLongitude);


                Utils.debug("ratiolatitude",ratiolatitude.toString());
                Utils.debug("ratiolongitude",ratiolongitude.toString());

                if (ratiolatitude < 0.0004 && ratiolongitude <0.0004){
                    // requête API /api/checkin
                    Toast.makeText(context, "Votre position est confirmée ", Toast.LENGTH_LONG).show();

                }
                else if (ratiolatitude < 0.001 && ratiolongitude <0.001){
                    // requête API /api/checkin
                    Toast.makeText(context, "Vous n'êtes pas loin, encore un petit effort ", Toast.LENGTH_LONG).show();
                }
                else {
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

    /**
     ** Géolocalisation de l'utilsateur
     */
    public static void geolocateMe() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        positionLatitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
        positionLongitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
        Utils.debug("latitudeposition",positionLatitude.toString());
        Utils.debug("longitudeposition",positionLongitude.toString());

    }

    /*
    ** Traiter les informations de la répartition d'un bénévole en fonction du raid
     */
    public static void recupInfosPoste(String response) {

        JsonParser parser = new JsonParser();
        JsonArray posteinfos = (JsonArray) parser.parse(response);


        int compteurposte;

        for (int i = 0; i < posteinfos.size(); i++) {

           compteurposte=i+1;

            JsonObject repartition = (JsonObject) posteinfos.get(i);

            String idrepartition = repartition.get("id").toString();

            JsonObject poste = repartition.getAsJsonObject("idPoste");

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

            Utils.debug("dateM",dateM);
            Utils.debug("dateY",dateY);
            Utils.debug("date",date);

            //récupération des coordonnées du poste
            JsonObject coordposte = poste.getAsJsonObject("idPoint");
            final Double latitude = Double.valueOf(coordposte.get("lat").toString());
            final Double longitude = Double.valueOf(coordposte.get("lon").toString());


//            LinearLayout layout2 = new LinearLayout(context);
//            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//            layout2.setGravity(LinearLayout.HORIZONTAL);
//            layout2.setBackgroundColor(context.getResources().getColor(R.color.BleuPrimaire));

            // parent.addView(iv);



//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//
//            layoutParams.setMarginEnd(20);
//
//            Button bt1 = new Button(context);
//            bt1.setText("some text");
//            bt1.setLayoutParams(layoutParams);
//            layout2.addView(bt1);



            //children of layout2 LinearLayout

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
            else if (getAndroidVersion() == 26) {
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
                    checkIn(positionpostetest);

                }
            });
            //Création d'un linearlayout de hauteur 180 et d'une orientation verticale

            layout3.setOrientation(LinearLayout.HORIZONTAL);

            //Ajout des boutons dans le linear layout
            layout3.addView(bt1);
            layout3.addView(bt2);

            //Ajout d'une marge en hauteur de 20 sur le linear qui contient les boutons
            //layout3.setPaddingRelative(0,20,0,0);
            //layout3.setBackgroundColor(context.getResources().getColor(R.color.MarronPrimaire));

            // Ajout d'une marge de 10 vers la gauche et de 10 vers le haut
            //tv1.setPaddingRelative(10,10,0,0);

            // Ajout d'une couleur et de la description du poste au linear de " droite "
            //parentdescription.setBackgroundColor(context.getResources().getColor(R.color.BleuPrimaire));
            parentdescription.addView(tv1);

            //Ajout du linear qui contient les boutons au linear de " gauche "
            parentbouton.addView(layout3);



        }
    }

    public static int getAndroidVersion() {
        int sdkVersion = Build.VERSION.SDK_INT;
        return  sdkVersion ;
    }
}
