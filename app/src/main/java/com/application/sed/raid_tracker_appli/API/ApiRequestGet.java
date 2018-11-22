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
import com.application.sed.raid_tracker_appli.organizer.CourseActivity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


    /*
    PARTIE BENEVOLE
     */


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



    /*
    PARTIE ORGANISATEUR
     */

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

    //
//    public static void getSpecificOrganisateur(Context context, String id){
//
//        String UrlFinale = urlOrganisateur + "/"+id;
//        Utils.debug("GetSpecificOrganisateur", UrlFinale);
//        final RequestQueue requestQueue = Volley.newRequestQueue(context);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET,
//                UrlFinale,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // Do something with response
//                        //mTextView.setText(response.toString());
//
//                        // Process the JSON
//                        try{
//                            // Loop through the array elements
//                            for(int i=1;i<response.length();i++){
//
//                                // Get the current account (json object) data
//                                String idUser = response.getString("idUser");
//                                String idRaid = response.getString("idRaid");
//                                Log.d("GetSpecificOrganisateur", idRaid);
//
//                                // Display the formatted json data in text view
////                                mTextView.append(firstName +" " + lastName +"\nAge : " + age);
////                                mTextView.append("\n\n");
//                            }
//                        }catch (Exception e){
//                            Log.e("Json","error");
//                        }
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Error.Response", error.toString());
//                    }
//                }
//        );
//
//        // Add JsonArrayRequest to the RequestQueue
//        requestQueue.add(jsonObjectRequest);
//    }

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


    /*
    PARTIE PARCOURS
     */

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


    /*
    PARTIE POINT
     */

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

     /*
     PARTIE TRACE
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


    /*
    PARTIE USER
     */

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









    public static void getSpecificTraceFromParcours(Context context, final String token, final String idParcours){

        String UrlFinale = urlTraces+"/parcours/"+idParcours ;
        Utils.debug("getSpecificTraceFromParcours", UrlFinale);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, UrlFinale,
                new Response.Listener<String>() {
                    /**
                     * Si tout se passe bien
                     * @param response
                     */
                    @Override
                    public void onResponse(String response) {
                        //CourseActivity.afficheParcours(response);
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



}

