package com.application.sed.raid_tracker_appli.API;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.sed.raid_tracker_appli.Utils.Utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class ApiRequestDelete {

    final static String urlUser = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/users";
    final static String urlAuthToken = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/auth-tokens";
    final static String urlPoint = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/points";


    /**
     * Benevole
     */

    /**
     * Checkin
     */

    /**
     * Mission
     */

    /**
     * Organisateur
     */

    /**
     * Parcours
     */

    /**
     * Point
     */

    public static void deleteSpecificPoint(Context context, final String token, final String idPoint){

        String urlFinale = urlPoint+"/"+idPoint;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, urlFinale,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response

                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                String auth;
                Utils.debug("Header",token);
                header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }
        };
        requestQueue.add(deleteRequest);

    }

    /**
     * Poste
     */

    /**
     * PrefPoste
     */

    /**
     * Raid
     */

    /**
     * Repartition
     */

    /**
     * Trace
     */

    /**
     * User
     */

    /**
     * X-auth-token
     */

    public static void deleteToken(Context context, final String token, final String id){

        String urlFinale = urlAuthToken+"/"+id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, urlFinale,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response

                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                String auth;
                Utils.debug("Header",token);
                header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }
        };
        requestQueue.add(deleteRequest);

    }

}
