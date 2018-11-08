package com.application.sed.raid_tracker_appli.API;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.sed.raid_tracker_appli.Bdd;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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

    /**
     * https://android--examples.blogspot.com/2017/02/android-volley-json-array-request.html
     *
     * https://riptutorial.com/fr/android/example/12633/creer-un-objet-json-imbrique
     *
     * http://tutorielandroid.francoiscolin.fr/recupjson.php
     *
     *
     */
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
                        Log.e("Error.Response", error.toString());
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }





    public static void getSpecificUsers(Context context, String id){

        String UrlFianle = urlUser + "/"+id;
        Utils.debug("GetSpecificUsers", UrlFianle);
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
                                String name = response.getString("name");
                                Log.d("GetSpecificUsers", name);
                                Bdd.setApiNomUtilisateur(name);

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


    public static void getSpecificOrganisateur(Context context, String id){

        String UrlFianle = urlOrganisateur + "/"+id;
        Utils.debug("GetSpecificOrganisateur", UrlFianle);
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
                                Log.d("GetSpecificOrganisateur", idRaid);

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

}

