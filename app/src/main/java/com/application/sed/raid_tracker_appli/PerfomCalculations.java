package com.application.sed.raid_tracker_appli;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

import okhttp3.Route;


/** https://stackoverflow.com/questions/40539617/how-to-create-an-asynctask-properly
 * AsynTask permet d'effectuer des tâches de fond
 * Trois paramètres ( paramètres fournis a la tache) - (type de données transmises durant la progression)- (type du résultat de la tâche)
 */
public class PerfomCalculations extends AsyncTask<GeoPoint,Void,Integer> {
    private Context mcontext;
    private MapEventsReceiver mapEventsReceiver;
    private Road route;


    //récupérer le contexte et eventsreceiver
    public PerfomCalculations(Context context, MapEventsReceiver mapEventsReceiver) {
        this.mapEventsReceiver = mapEventsReceiver;
        this.mcontext = context;
    }



    // permet d'effectuer des tâches de fond, tâches lourdes executées sur un autre thread
    @Override
    protected Integer doInBackground(GeoPoint[] params) {
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(params[0]);
        waypoints.add(params[1]);

        try {
            RoadManager roadManager = new MapQuestRoadManager("o7gFRAppOrsTtcBhEVYrY6L7AGRtXldE");
            roadManager.addRequestOption("routeType=pedestrian");
            route = roadManager.getRoad(waypoints);
            return 1;
        } catch (Exception e) {
            return -1;
        }

    }

    //optionnel appelé avant le traitement (!! s'execute avec le thread UI)
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //optionnel appelé après le traitement (!! s'execute avec le thread UI)
     @Override
    protected void onPostExecute(Integer success) {
//
//        Polyline roadOverlay = RoadManager.buildRoadOverlay(route);
//        map.getOverlays().add(roadOverlay);
//
//        map.invalidate();
//
//        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
//
//        map.getOverlays().add(0, mapEventsOverlay);
    }

    // optionnel appelé pendant la progression (!! s'execute avec le thread UI)
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public interface CalculRoute {
        public void calculroute(boolean success, Route route);
    }
}


