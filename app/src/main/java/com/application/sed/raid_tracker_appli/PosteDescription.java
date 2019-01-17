package com.application.sed.raid_tracker_appli;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

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
    Double latitude;
    Double longitude;

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

        //récupération de l'id du Raid depuis Landing Activity
        idRaidReceive = intent.getStringExtra("idRaid");

        //récupération de la toolbar depuis le XML
        toolbar = (Toolbar) findViewById(R.id.toolbarDescr);

        //définir la toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

                GeoPoint tmpgeo = new GeoPoint(48.731038, -3.450802);

                Double tmpgeolatitude = tmpgeo.getLatitude();
                Utils.debug("doublelatitude",tmpgeolatitude.toString());
                tmpgeolatitude= (double)Math.round(tmpgeolatitude * 1000d) / 1000d;
                Utils.debug("after",tmpgeolatitude.toString());
                Double tmpgeolongitude =tmpgeo.getLongitude();

                /**TODO
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
        latitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
        longitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
        Utils.debug("latitudeposition",latitude.toString());
        Utils.debug("longitudeposition",longitude.toString());





    }

    /*
    ** Traiter les informations de la répartition d'un bénévole en fonction du raid
     */
    public static void recupInfosPoste(String response) {

        JsonParser parser = new JsonParser();
        JsonArray posteinfos = (JsonArray) parser.parse(response);






//        //parcours la liste avec le Json
//        for (int i = 0; i < posteinfos.size(); i++) {
//
//            //JsonParser parser1 = new JsonParser();
//            JsonObject raid = (JsonObject) posteinfos.get(i);
//
//            //récupération de l'id de point d'un poste
//            JsonObject deuxiem = raid.getAsJsonObject("idPoint");
//
//            String test = deuxiem.get("id").toString();
//            //String posteraid = raid.get("nom").toString().replace("\""," ");
//
//
//            String type = raid.get("type").toString().replace("\"", " ");
//            ;

//        }
    }
}
