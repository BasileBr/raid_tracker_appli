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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.sed.raid_tracker_appli.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ApiRequestGet {

    final static String url = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/users";
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
                url,
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



//    public static void getSpecificUsers(Context context, ){
//
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                Request.Method.GET,
//                url,
//                null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        // Do something with response
//                        //mTextView.setText(response.toString());
//
//                        // Process the JSON
//                        try{
//                            // Loop through the array elements
//                            for(int i=0;i<response.length();i++){
//                                // Get current json object
//                                JSONObject account = response.getJSONObject(i);
//
//                                // Get the current account (json object) data
//                                String name = account.getString("name");
//                                Log.d("Response", name);
//
//                                // Display the formatted json data in text view
////                                mTextView.append(firstName +" " + lastName +"\nAge : " + age);
////                                mTextView.append("\n\n");
//                            }
//                        }catch (JSONException e){
//                            e.printStackTrace();
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
//        requestQueue.add(jsonArrayRequest);
//    }



}

