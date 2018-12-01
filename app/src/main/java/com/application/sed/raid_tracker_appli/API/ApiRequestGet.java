package com.application.sed.raid_tracker_appli.API;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.sed.raid_tracker_appli.Accueil;
import com.application.sed.raid_tracker_appli.InviteVolunteersActivity;
import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.ManageParcoursActivity;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.application.sed.raid_tracker_appli.VolunteerPreferenceActivity;
import com.application.sed.raid_tracker_appli.WelcomeActivity;
import com.application.sed.raid_tracker_appli.organizer.CourseActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;


public class ApiRequestGet {

    final static String urlUser = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/users";
    final static String urlOrganisateur = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/organisateurs";
    final static String urlBenevoles = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/benevoles";
    final static String urlRaid = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/raids";
    final static String urlRaidUser = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/raids/organisateurs/users";
    final static String urlParcours="http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/parcours";
    final static String urlTraces="http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/traces";
    final static String urlPoints="http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/points";
    final static String urlCheckin="http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/checkin";
    final static String urlMissions="http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/missions";
    final static String urlPostes="http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/postes";
    final static String urlPrefPostes="http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/prefpostes";
    final static String urlRepartitions="http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/repartitions";



    /**
     * https://android--examples.blogspot.com/2017/02/android-volley-json-array-request.html
     *
     * https://riptutorial.com/fr/android/example/12633/creer-un-objet-json-imbrique
     *
     * http://tutorielandroid.francoiscolin.fr/recupjson.php
     *
     *
     * http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/doc
     *
     * q@q.fr : q
     * madox@contact.fr : test
     */


    /**
    * PARTIE BENEVOLE
     **/


    //get all benevoles
    public static void getBenevoles(final Context context){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlBenevoles,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject account = response.getJSONObject(i);

                                // Get the current account (json object) data
                                String idRaid = account.getString("id");
                                String idUser = account.getString("idUser");

                                Log.d("GetBenevoles", idRaid);

                                // Display the formatted json data in text view
//                                mTextView.append(firstName +" " + lastName +"\nAge : " + age);
//                                mTextView.append("\n\n");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response", error.toString());
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    //get benevoles of one raid
    public static void getBenevolesOfOneRaid(Context context, final String token, String id_raid){

        String UrlFinale = urlBenevoles+'/'+"raids"+'/'+id_raid ;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                UrlFinale,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject account = response.getJSONObject(i);

                                // Get the current account (json object) data
                                String idRaid = account.getString("id");
                                String idUser = account.getString("idUser");

                                Log.d("GetBenevoles", idRaid);

                                // Display the formatted json data in text view
//                                mTextView.append(firstName +" " + lastName +"\nAge : " + age);
//                                mTextView.append("\n\n");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    //get benevole if present in a specific RAID
    public static void getBenevolefromSpecificRaid(Context context, final String token, String id_raid, String id_user){

        String UrlFinale = urlBenevoles+'/'+id_raid+'/'+"users"+id_user ;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                UrlFinale,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject account = response.getJSONObject(i);

                                // Get the current account (json object) data
                                String idRaid = account.getString("id");
                                String idUser = account.getString("idUser");

                                Log.d("GetBenevoles", idRaid);

                                // Display the formatted json data in text view
//                                mTextView.append(firstName +" " + lastName +"\nAge : " + age);
//                                mTextView.append("\n\n");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }


    //get one benevole
    public static void getSpecificBenevole(Context context, String id){

        String UrlFianle = urlBenevoles + "/"+id;
        Utils.debug("GetSpecificBenevole", UrlFianle);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                UrlFianle,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=1;i<response.length();i++){

                                // Get the current account (json object) data
                                String idUser = response.getString("idUser");
                                String idRaid = response.getString("idRaid");
                                Log.d("GetSpecificBenevole", idRaid);

                                // Display the formatted json data in text view
//                                mTextView.append(firstName +" " + lastName +"\nAge : " + age);
//                                mTextView.append("\n\n");
                            }
                        }catch (Exception e){
                            Log.e("Json","error");
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response", error.toString());
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }


    //get One benevole
    public static void getOneBenevole(Context context, final String token, String id_benevole){

        String UrlFinale = urlBenevoles+'/'+id_benevole;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                UrlFinale,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject account = response.getJSONObject(i);

                                // Get the current account (json object) data
                                String idRaid = account.getString("id");
                                String idUser = account.getString("idUser");

                                Log.d("GetBenevoles", idRaid);

                                // Display the formatted json data in text view
//                                mTextView.append(firstName +" " + lastName +"\nAge : " + age);
//                                mTextView.append("\n\n");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }


    /**
     *PARTIE CHECKIN
     **/

    //Get all parcours
    public static void getCheckin(Context context, final String token){

        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlCheckin,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                    }

                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    //Get checkin of one RAid
    public static void getCheckinOneRaid(Context context, final String token, final String id_raid ){

        String urlFinale=urlCheckin+'/'+"raids"+id_raid;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                    }

                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }


    //Get checkin of one User
    public static void getCheckinOneUser(Context context, final String token, final String id_user ){

        String urlFinale=urlCheckin+'/'+"users"+id_user;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                    }

                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    //Get checkin of one User
    public static void getOneCheckin(Context context, final String token, final String id_checkin ){

        String urlFinale=urlCheckin+'/'+id_checkin;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                    }

                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    /**
     *PARTIE MISSION
     **/

    //Get all mission
    public static void getAllMissions(Context context, final String token){

        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlMissions,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                    }

                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    //Get missions of one parcours
    public static void getMissionsofOneParcours(Context context, final String token, final String id_parcours ){

        String urlFinale=urlMissions+'/'+"parcours"+'/'+id_parcours;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                    }

                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }


    //Get missions of one poste
    public static void getMissionsofOnePoste(Context context, final String token, final Integer id_poste ){

        String urlFinale=urlMissions+'/'+"postes"+'/'+id_poste;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        VolunteerPreferenceActivity.getMission(response);
                    }

                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }


    //Get missions of one raid
    public static void getMissionsOfOneRaid(Context context, final String token, final String id_raid ){

        String urlFinale=urlMissions+'/'+"raids"+'/'+id_raid;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                    }

                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }


    //Get one mission
    public static void getOneMission(Context context, final String token, final String id_mission ){

        String urlFinale=urlMissions+id_mission;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                    }

                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }





    /**
    *PARTIE ORGANISATEUR
     **/

    //get all organisateurs
    public static void getOrganisateur(final Context context){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlOrganisateur,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject account = response.getJSONObject(i);

                                // Get the current account (json object) data
                                String idRaid = account.getString("idRaid");
                                String idUser = account.getString("idUser");

                                Log.d("Response", idRaid);

                                // Display the formatted json data in text view
//                                mTextView.append(firstName +" " + lastName +"\nAge : " + age);
//                                mTextView.append("\n\n");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response", error.toString());
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }


    //get organisateur of one raid
    public static void getOrganisateursofOneRaid(Context context, final String token, String id_raid){

        String UrlFinale = urlOrganisateur+'/'+"raids"+'/'+id_raid ;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                UrlFinale,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject account = response.getJSONObject(i);

                                // Get the current account (json object) data
                                String idRaid = account.getString("id");
                                String idUser = account.getString("idUser");

                                Log.d("GetBenevoles", idRaid);

                                // Display the formatted json data in text view
//                                mTextView.append(firstName +" " + lastName +"\nAge : " + age);
//                                mTextView.append("\n\n");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }


    //Get organisateur if present in a specific RAID
    public static void getOrganisateurinSpecRaid(Context context, final String token, String id_raid, String id_user){

        String UrlFinale = urlOrganisateur+'/'+"raids"+'/'+id_raid +'/'+"users"+'/'+id_user ;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                UrlFinale,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject account = response.getJSONObject(i);

                                // Get the current account (json object) data
                                String idRaid = account.getString("id");
                                String idUser = account.getString("idUser");

                                Log.d("GetBenevoles", idRaid);

                                // Display the formatted json data in text view
//                                mTextView.append(firstName +" " + lastName +"\nAge : " + age);
//                                mTextView.append("\n\n");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    //Get One organisateur
    public static void getOneOrganisateur(Context context, final String token, String id_raid, String id_organisateur){

        String UrlFinale = urlOrganisateur+'/'+id_organisateur ;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                UrlFinale,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject account = response.getJSONObject(i);

                                // Get the current account (json object) data
                                String idRaid = account.getString("id");
                                String idUser = account.getString("idUser");

                                Log.d("GetBenevoles", idRaid);

                                // Display the formatted json data in text view
//                                mTextView.append(firstName +" " + lastName +"\nAge : " + age);
//                                mTextView.append("\n\n");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }


    /**
    * PARTIE PARCOURS
     **/

    //Get all parcours
    public static void getParcours(Context context, final String token){

        String UrlFinale = urlParcours ;
        //Utils.debug("getSpecificRaid", UrlFinale);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        LandingActivity.raidlist(response);

                    }

                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    // get all parours of a specific raid
    public static void getSpecificParcours(Context context, final String token, final String idRaid){

        String UrlFinale = urlParcours+"/raids/"+idRaid ;
        Utils.debug("getSpecificParcours", UrlFinale);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {
                    /**
                     * Si tout se passe bien
                     * @param response
                     */
                    @Override
                    public void onResponse(String response) {
                        CourseActivity.afficheParcours(response);
                    }

                },
                /**
                 * Se tout se passe pas bien
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            /**
             * Envoie le header -> en gros, le token
             * @return
             * @throws AuthFailureError
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    //Get one parcours
    public static void getOneParcours(Context context, final String token, String id_parcours){

        String UrlFinale = urlParcours+'/'+id_parcours ;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                UrlFinale,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject account = response.getJSONObject(i);

                                // Get the current account (json object) data
                                String idRaid = account.getString("id");
                                String idUser = account.getString("idUser");

                                Log.d("GetBenevoles", idRaid);

                                // Display the formatted json data in text view
//                                mTextView.append(firstName +" " + lastName +"\nAge : " + age);
//                                mTextView.append("\n\n");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }


    /**
    *PARTIE POINT
     **/

    // get all points
    public static void getPoint(Context context, final String token){

        String UrlFinale = urlPoints ;
        //Utils.debug("getSpecificRaid", UrlFinale);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //LandingActivity.raidlist(response);
                        Log.d("Response creation poin", response);

                    }

                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);




    }
    // get all points of one trace
    public static void getSpecificPoint(Context context, final String token, final String id) {

        String UrlFinale = urlPoints + '/' + id;
        //Utils.debug("getSpecificRaid", UrlFinale);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response creation poin", response);
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header", token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token", token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);

    }

    // get all points of one trace
    public static void getPointsfromSpecificTrace(Context context, final String token, final int id) {

        String UrlFinale = urlPoints + '/' + "traces"+'/'+id;
        Utils.debug("getPointsfromSpecificTrace", UrlFinale);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response creation point", response);
                        ManageParcoursActivity.recupParcours(response);
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header", token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token", token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);

    }

    //get poste point of one trace
    public static void getPostepoint(Context context, final String token, final String id_poste) {

        String UrlFinale = urlPoints + '/' + "postes"+'/'+id_poste;
        Utils.debug("getPointsfromSpecificTrace", UrlFinale);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response creation point", response);
                        ManageParcoursActivity.recupParcours(response);
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header", token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token", token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);

    }

    //get arrival point of one trace

    public static void getArrivalpoint(Context context, final String token, final String id_trace) {

        String UrlFinale = urlPoints + '/' + "traces" + '/' + id_trace + '/' + "arrivee";
        Utils.debug("getPointsfromSpecificTrace", UrlFinale);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response creation point", response);
                        ManageParcoursActivity.recupParcours(response);
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header", token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token", token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    //get depart point of one trace

    public static void getDeparturepoint(Context context, final String token, final String id_trace) {

        String UrlFinale = urlPoints + '/' +"traces"+'/'+ id_trace+ '/'+"depart";
        Utils.debug("getPointsfromSpecificTrace", UrlFinale);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response creation point", response);
                        ManageParcoursActivity.recupParcours(response);
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header", token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token", token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);

    }

    //get one point

    public static void getOnePoint(Context context, final String token, final String id_point) {

        String UrlFinale = urlPoints + '/'+id_point;
        Utils.debug("getPointsfromSpecificTrace", UrlFinale);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response creation point", response);
                        ManageParcoursActivity.recupParcours(response);
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header", token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token", token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);

    }

    /**
     * PARTIE POSTE
     **/

    //get all postes
    public static void getAllPostes(final Context context,final String token){


        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlPostes,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                },
                /**
                 *
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }


    //get all postes by id benevole
    public static void getAllPostesfromSpecBenevole(final Context context,final String token, final String id_benevole){


        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlPostes,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                },
                /**
                 *
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    //get all postes by id parcours
    public static void getAllPostesfromSpecParcours(final Context context,final String token, final String id_parcours){

        String urlFinale=urlPostes+'/'+id_parcours;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlFinale,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                },
                /**
                 *
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    //get all postes  available by id parcours
    public static void getAllPostesfromSpecParcoursAvailable(final Context context,final String token, final String id_parcours){

        String urlFinale=urlPostes+'/'+id_parcours+'/'+"available";
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlFinale,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                },
                /**
                 *
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    //get all postes by id raid
    public static void getAllPostesfromOneRaid(final Context context,final String token, final String id_raid){

        String urlFinale=urlPostes+'/'+"raids"+'/'+id_raid+'/'+"available";
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlFinale,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        VolunteerPreferenceActivity.posteListe(response);

                    }

                },
                /**
                 *
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    //get all postes available by id raid
    public static void getAllPostesfromOneRaidAvailable(final Context context,final String token, final String id_raid){

        String urlFinale=urlPostes+'/'+"raids"+'/'+id_raid+'/'+"available";
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlFinale,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                },
                /**
                 *
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    //get onePoste
    public static void getOnePoste(final Context context,final String token, final String id_poste){

        String urlFinale=urlPostes+'/'+id_poste;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlFinale,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                },
                /**
                 *
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }



    /**
     * PARTIE PREFPOSTE
     **/

    //get all prefpostes
    public static void getOnePoste(final Context context,final String token){


        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlPrefPostes,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                },
                /**
                 *
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    //get all prefpostes
    public static void getPrefPosteofOneBenevole(final Context context,final String token,String id_benevole){

        String Urlfinale=urlPrefPostes+'/'+"benevoles"+id_benevole;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, Urlfinale,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                },
                /**
                 *
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    //get all prefpostes
    public static void getOnePrefPoste(final Context context,final String token,String id_prefposte){

        String Urlfinale=urlPrefPostes+'/'+id_prefposte;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, Urlfinale,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                },
                /**
                 *
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }





    //get all postes by id raid
    public static void getPostefromSpecificRaid(final Context context,final String token, final String idraid ){

        String urlfinale= urlRaid+'/'+idraid;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlfinale,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                },
                /**
                 *
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            /**
             * Envoie le header -> en gros, le token
             * @return
             * @throws AuthFailureError
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }


    /**
     * PARTIE RAID

     */


    //get all raids
    public static void getAllRaids(final Context context, final String classe){

        String urlfinale = urlRaid+'/'+"visible"+'/'+"all";
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlfinale,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Utils.debug("reponse des raids",response);


                        if(classe.equals("WelcomeActivity")) {
                            WelcomeActivity.recupRaid(response);
                        }
                        else if(classe.equals("LandingActivity")){
                            LandingActivity.recupRaid(response);
                        }

                    }

                },
                /**
                 *
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    public static void getraid(final Context context){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlRaid,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject account = response.getJSONObject(i);

                                // Get the current account (json object) data
                                String nom = account.getString("nom");
                                String lieu = account.getString("lieu");

                                Log.d("GetRaid", nom);

                                // Display the formatted json data in text view
//                                mTextView.append(firstName +" " + lastName +"\nAge : " + age);
//                                mTextView.append("\n\n");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response", error.toString());
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    /**
     *
     * Fonctionne correctement
     *
     * @param context
     * @param token
     * @param id
     */
    public static void getSpecificRaid(Context context, final String token, final String id, final String classe){

        String UrlFinale = urlRaidUser+'/'+id ;
        Utils.debug("getSpecificRaid", UrlFinale);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {
                    /**
                     * Si tout se passe bien
                     * @param response
                     */
                    @Override
                    public void onResponse(String response) {
                        if(classe.equals("LandingActivity")) {
                            LandingActivity.raidlist(response);
                        }
                        else if(classe.equals("InviteActivity")){
                            InviteVolunteersActivity.raidlist(response);
                        }
                  }

                },
                /**
                 * Se tout se passe pas bien
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            /**
             * Envoie le header -> en gros, le token
             * @return
             * @throws AuthFailureError
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }


    /**
     * PARTIE REPARTITION
     * @param context
     * @param token
     */

    //get all repartitions
    public static void getRepartition(final Context context,final String token){


        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlRepartitions,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                },
                /**
                 *
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    //get repartitions of one parcours
    public static void getRepartionsofOneParcours(final Context context,final String token, final String id_parcours){

        String UrlFinale=urlRepartitions+'/'+"parcours"+'/'+id_parcours;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                },
                /**
                 *
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    //get repartitions of one parcours
    public static void getRepartitionsofOneRaid(final Context context,final String token, final String id_raid){

        String UrlFinale=urlRepartitions+'/'+"raids"+'/'+id_raid;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                },
                /**
                 *
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }

    //get repartitions of one user
    public static void getRepartitionsofOneUser(final Context context,final String token, final String id_user){

        String UrlFinale=urlRepartitions+'/'+"users"+'/'+id_user;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                },
                /**
                 *
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }


    //get repartitions of one user
    public static void getOneRepartition(final Context context,final String token, final String id_repartition){

        String UrlFinale=urlRepartitions+'/'+id_repartition;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                },
                /**
                 *
                 */
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }






    /**
     * PARTIE TRACE
     * @param context
     * @param token
     */

    //get all traces
    public static void getTrace(Context context, final String token){

        String UrlFinale = urlTraces ;
        //Utils.debug("getSpecificRaid", UrlFinale);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        LandingActivity.raidlist(response);
                    }

                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);

    }

    //get one trace by parcours id
    public static void getSpecificTraceFromParcours(final Context context, final String token, final String idParcours) {

        String UrlFinale = urlTraces + "/parcours/" + idParcours;
        //Utils.debug("getSpecificRaid", UrlFinale);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        JsonArray jsonArray;
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonObject;
                        jsonArray = (JsonArray) jsonParser.parse(response);
                        jsonObject = (JsonObject) jsonArray.get(0);
                        String idParcours = jsonObject.get("id").toString();
                        //Utils.debug("getSpecificTraceFromParcours",jsonArray.get(0).toString());

                        ApiRequestGet.getPointsfromSpecificTrace(context, token, Integer.valueOf(idParcours));
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header", token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token", token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);

    }

    //get one trace
    public static void getSpecificTrace(Context context, final String token, final String idTrace) {

        String UrlFinale = urlTraces + '/' + idTrace;
        //Utils.debug("getSpecificRaid", UrlFinale);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        LandingActivity.raidlist(response);
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header", token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token", token);
                return header;
            }

        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);

    }


    /**
     * PARTIE USER
     **/

    /**
     * Ne doit pas fonctionner
     * @param context
     */

    //get all users
    public static void getUsers(Context context){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlUser,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject account = response.getJSONObject(i);
                                // Get the current account (json object) data
                                String name = account.getString("name");
                                Log.d("Response", name);

                                // Display the formatted json data in text view
//                                mTextView.append(firstName +" " + lastName +"\nAge : " + age);
//                                mTextView.append("\n\n");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response get user", error.toString());
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }


    /**
     *
     * NE doit pas fonctionner non plus
     * @param context
     * @param token
     * @param id
     */
    // get one user
    public static void getSpecificUsers(Context context, final String token, final String id){

        String UrlFianle = urlUser+'/'+id ;
        Utils.debug("GetSpecificUsers", UrlFianle);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFianle,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        JsonParser parser = new JsonParser();
                        JsonObject res = (JsonObject) parser.parse(response);
                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=1;i<res.size();i++){

                                // Get the current account (json object) data
                                String name = res.get("username").toString();
                                Log.d("GetSpecificUsers", name);
                                Bdd.setApiNomUtilisateur(name);
                                Accueil.change(name);

                                // Display the formatted json data in text view
//                                mTextView.append(firstName +" " + lastName +"\nAge : " + age);
//                                mTextView.append("\n\n");
                            }
                        }catch (Exception e){
                            Log.e("Json","error");
                        }
                    }

                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response specific", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                header.put("Accept", "application/json");
                header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }

//            @Override
//            protected Map<String, String> getParams()
//            {
//
//                Map<String, String>  params = new HashMap<String, String>();
//                Utils.debug("params", id);
//                params.put("id",id);
//
//                return params;
//            }
        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(getRequest);
    }






}

