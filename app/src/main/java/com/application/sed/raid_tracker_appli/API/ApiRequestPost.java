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
import com.application.sed.raid_tracker_appli.organizer.CreateCourse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.internal.Util;

public class ApiRequestPost {

    final static String urlUser = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/users";
    final static String urlAuthToken = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/auth-tokens";
    final static String urlRaid = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/raids";
    final static String urlOrganisateursRaids = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/organisateurs/raids";

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

    public static void postRaid(final Context context, final String token, final String name, final String lieu, final String date, final String edition, final String equipe){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        Utils.debug("CreateRaid", "nom " + name+" lieu " + lieu+ " date " + date+" edition  " + edition+ " equipe" +equipe);
        try {
            jsonObject.put("nom",name);
            jsonObject.put("lieu",lieu);
            jsonObject.put("date",date);
            jsonObject.put("edition",Integer.valueOf(edition));
            jsonObject.put("equipe", equipe);
            jsonArray.put(jsonObject);
            }catch (Exception e){

        }

        Utils.debug("CreateRaid",jsonArray.toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, urlRaid, jsonObject,
                new Response.Listener<JSONObject>()
                {   
                    @Override
                    public void onResponse(JSONObject response) {
                        // response


                        Log.d("Response creation raid", response.toString());

                        try {
                            String idRaid = response.getString("id");
                            postUserToRaid(context, token, Bdd.getUserid(), idRaid, response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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



        };
        requestQueue.add(postRequest);

    }

    public static void postUserToRaid(Context context, final String token, final String idUser, final String idRaid, final JSONObject infoRaid){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String urlFinale = urlOrganisateursRaids+"/"+idRaid+"/"+"users"+"/"+idUser;
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        Utils.debug("CreateRaid", "idUser " + idUser+" idRaid " + idRaid);
        try {
            jsonObject.put("idUser",idUser);
            jsonObject.put("idRaid",idRaid);
            jsonArray.put(jsonObject);
        }catch (Exception e){

        }

        Utils.debug("CreateRaid",jsonArray.toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, urlFinale, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response


                        Log.d("Response creation raid", response.toString());

                        try {
                            CreateCourse.createRaid(infoRaid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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



        };
        requestQueue.add(postRequest);

    }
}
