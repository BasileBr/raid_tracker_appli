package com.application.sed.raid_tracker_appli;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

public class CreateParcours extends Activity {
    MapView map = null;

    MyLocationNewOverlay mLocationOverlay;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //handle permissions first, before map is created. not depicted here

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

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);


        //position de la carte
        IMapController mapController = map.getController();
        mapController.setZoom(9.0);
        GeoPoint startPoint = new GeoPoint(48.732084,-3.4591440000000375);
        mapController.setCenter(startPoint);


        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), map);
        mLocationOverlay.enableMyLocation();
        map.setMultiTouchControls(true);
        map.getOverlays().add(mLocationOverlay);

        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx),map);
        this.mLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.mLocationOverlay);

        // ajouter l'echelle
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(map);
        map.getOverlays().add(myScaleBarOverlay);

        //ajouter marqueur
        GeoPoint enssatpoint =  new GeoPoint(48.729673,-3.4624261999999817);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(enssatpoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        String latitude=String.valueOf(enssatpoint.getLatitude());
        String longitude=String.valueOf(enssatpoint.getLongitude());
        startMarker.setTitle("ENSSAT"+"\n"+"latitude: "+latitude+'\n'+"longitude: "+longitude);

        //ajouter un icone particuliere
        startMarker.setIcon(getResources().getDrawable(R.drawable.pointer));
        map.getOverlays().add(startMarker);
//
//        // ajouter boussolle
//        CompassOverlay mCompassOverlay = new CompassOverlay(getApplicationContext(), new InternalCompassOrientationProvider(getApplicationContext()), map);
//        mCompassOverlay.enableCompass();
//        map.getOverlays().add(mCompassOverlay);


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
//        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(view.getContext()),map);
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
}