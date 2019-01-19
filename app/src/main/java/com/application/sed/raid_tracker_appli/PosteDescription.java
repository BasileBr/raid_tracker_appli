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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import okhttp3.internal.Util;

public class PosteDescription extends AppCompatActivity {

    private static String idRaidReceive = "";
    private static Context context;
    private static String token;
    private static String iduser;
    private Toolbar toolbar;
    Double positionLatitude;
    Double positionLongitude;
    private static LinearLayout parentdescription;
    private static LinearLayout parentbouton;


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


        //récupérer la répartition d'un utilisateur sur un raid
        ApiRequestGet.getRepartitionfromIdUserIdRaid(context, token, idRaidReceive, iduser);


    }

    public void onClickGuideMe(View view) {
        Intent intent = new Intent(PosteDescription.this, GuideMeActivity.class);
        startActivity(intent);
    }

    public void checkIn(View view) {

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

                GeoPoint positionPoste = new GeoPoint(48.729709,  -3.465943);

                // récupération de la position en double
                Double positionPosteLatitude = positionPoste.getLatitude();
                Double positionPosteLongitude =positionPoste.getLongitude();

                // calcul de ratio
                Double ratiolatitude  = Math.abs(positionPosteLatitude)-Math.abs(positionLatitude);
                Double ratiolongitude = Math.abs(positionPosteLongitude)-Math.abs(positionLongitude);

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


                /**
                 * Calcul ratio :   - ratiolong = valeur absolue de la longitude du point - la longitude de la position
                 *                  - ratiolat = valeur absolue de la latitude du point - la latitude de la position
                 *
                 *                  si ratiolong < 0.0004 && ratiolat < 0.0004
                 *                     {
                 *                          ok
                 *                      }
                 *                  sinon si ratiolong < 0.001 && ratiolat < 0.001
                 *                      {
                 *                          presque ok
                 *                      }
                 *                  sinon
                 *                      {
                 *                          pas cool
                 *                      }
                 */
                //ne pas faire un double checkin


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
    public void geolocateMe() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        for (int i = 0; i < posteinfos.size(); i++) {
            JsonObject repartition = (JsonObject) posteinfos.get(i);
            JsonObject poste = repartition.getAsJsonObject("idPoste");

            //récupération du type du poste
            String typePoste = poste.get("type").toString().replace("\"", " ");

            //récupération de l'heure de début
            String hourdebut=poste.get("heureDebut").toString().substring(12,14);
            String minutedebut=poste.get("heureDebut").toString().substring(14,17);
            String heureDebut = hourdebut +minutedebut;

            //récupération de l'heure de fin
            String hourfin=poste.get("heureFin").toString().substring(12,14);
            String minutefin=poste.get("heureFin").toString().substring(14,17);
            String heureFin = hourfin +minutefin;

            //récupération de la date
            String dateY=poste.get("heureFin").toString().substring(1,5);
            String dateM=poste.get("heureFin").toString().substring(6,8);
            String dateD=poste.get("heureFin").toString().substring(9,11);
            String date=dateD+'/'+dateM+'/'+dateY;

            Utils.debug("heureDebut",heureDebut);
            Utils.debug("heureFin",heureFin);
            Utils.debug("date",date);

            //récupération des coordonnées du poste
            JsonObject coordposte = poste.getAsJsonObject("idPoint");
            String latitude = coordposte.get("lat").toString();
            String longitude = coordposte.get("lon").toString();


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
                layout3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 110));
                layout3.setPaddingRelative(0,20,20,0);
                tv1.setPaddingRelative(20,15,0,0);

            }
            else if (getAndroidVersion() == 26) {
                tv1.setHeight(180);
                layout3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 180));
                layout3.setPaddingRelative(0,20,0,0);
                tv1.setPaddingRelative(10,10,0,0);

            }
            else {
                tv1.setHeight(180);
                layout3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 180));
                layout3.setPaddingRelative(0,20,0,0);
                tv1.setPaddingRelative(10,10,0,0);
            }
            Button bt1 = new Button(context);
            Button bt2 = new Button(context);

            tv1.setText(typePoste+"\n"+"de : "+heureDebut+"à : "+heureFin+"\n"+"date : "+date);
            tv1.setTextSize(15);
            bt1.setText("Me Guider");
            bt2.setText("CheckIn");

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
