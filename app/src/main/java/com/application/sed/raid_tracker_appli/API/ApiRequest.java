package com.application.sed.raid_tracker_appli.API;

import android.content.Context;
import android.os.Bundle;
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
import com.application.sed.raid_tracker_appli.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiRequest extends AppCompatActivity {

    final String url = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/users";

    /**
     * https://android--examples.blogspot.com/2017/02/android-volley-json-array-request.html
//     */



    public void getUsers(){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
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


    public void postMethod(){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
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
            protected Map<String, String> getParams()
            {


                Map<String, String>  params = new HashMap<String, String>();
                //Map<String, String>  params2 = new HashMap<String, String>();
                params.put("name","ulas");


                params.put("email","test@test.fr");
                params.put("password","test");
//                params2.put("first","coucou");
//                params2.put("second","coucou2");


//                JSONObject params = null;
//                JSONObject params2 = null;
//
//                try{
//                    params2 = new JSONObject();
//                    params2.put("first","coucou");
//                    params2.put("second","coucou2");
//                    params = new JSONObject();
//                    params.put("name", "ulas");
//                    params.put("email", "ulas@gmail.com");
//                    params.put("password",params2);
//
//                }catch(JSONException e) {
//                    Log.e("Erro.JSON",e.toString());
//                }

                return params;
            }
        };
        requestQueue.add(postRequest);

    }
}

