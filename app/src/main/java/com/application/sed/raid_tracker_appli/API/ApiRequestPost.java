package com.application.sed.raid_tracker_appli.API;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.sed.raid_tracker_appli.Accueil;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.internal.Util;

public class ApiRequestPost {

    final static String urlUser = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/users";
    final static String urlAuthToken = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/auth-tokens";
    final static String urlRaid = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/raids";

    public static void postUser(Context context, final String name, final String mail, final String pwd){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlUser,
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
                        Log.d("Error.Response post us", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String>  params = new HashMap<String, String>();
//                JSONObject pass = new JSONObject();
                //Map<String, String>  params2 = new HashMap<String, String>();
                params.put("username",name);
                params.put("email",mail);
                params.put("plainPassword",pwd);

                return params;
            }
        };
        requestQueue.add(postRequest);

    }


    /**
     * FOnctionne : connexion utilisateur
     * @param context
     * @param name
     * @param pwd
     */
    public static void postToken(Context context, final String name, final String pwd){


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlAuthToken,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response

                        Log.d("Response", response);

                        Accueil.redirection(response);
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
            protected Map<String, String> getParams()
            {

                Map<String, String>  params = new HashMap<String, String>();
                params.put("login",name);
                params.put("password",pwd);


                return params;
            }
        };
        requestQueue.add(postRequest);

    }

    public static void postRaid(Context context, final String token, final String name, final String lieu, final String date, final String edition, final String equipe){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObject params = new JsonObject();
        JsonParser parser = new JsonParser();
        Utils.debug("CreateRaid", "nom " + name+" lieu " + lieu+ " date " + date+" edition  " + edition+ " equipe" +equipe);
        params.add("nom",parser.parse(name));
        params.add("lieu",parser.parse(lieu);
        params.add("date",parser.parse(date);
        params.add("edition",parser.parse(edition));
        params.add("equipe", parser.parse(equipe);

        Utils.debug("CreateRaid",params.toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, urlRaid,
                new Response.Listener<String>()
                {   
                    @Override
                    public void onResponse(String response) {
                        // response


                        Log.d("Response creation raid", response);

                        Accueil.redirection(response);
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                String auth;
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

//            @Override
//            protected Map<String, String> getParams()
//            {
//
//                Map<String, String>  params = new HashMap<>();
//                Utils.debug("CreateRaid", "nom " + name+" lieu " + lieu+ " date " + date+" edition  " + edition+ " equipe" +equipe);
//                params.put("nom",name);
//                params.put("lieu",lieu);
//                params.put("date",date);
//                params.put("edition",edition);
//                params.put("equipe", equipe);
//
//                Utils.debug("CreateRaid",params.toString());
//                return params;
//            }


        };
        requestQueue.add(postRequest);

    }
}
