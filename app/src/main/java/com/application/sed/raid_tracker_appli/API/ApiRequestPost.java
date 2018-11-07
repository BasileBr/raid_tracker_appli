package com.application.sed.raid_tracker_appli.API;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.sed.raid_tracker_appli.Bdd;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiRequestPost {

    final static String urlUser = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/users";
    final static String urlAuthToken = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/auth-tokens";

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
                        Log.d("Error.Response", error.toString());
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

                /*try {
                    pass.put("first",pwd1);
                    pass.put("second",pwd2);
                }catch (Exception e){
                    return null;
                }*/





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

    public static void postToken(Context context, final String name, final String pwd){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlAuthToken,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response

                        Log.d("Response", response);
                        Bdd.setResponse(response);
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
}
