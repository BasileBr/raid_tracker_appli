package com.application.sed.raid_tracker_appli.API;

import android.content.Context;
import android.media.session.MediaSession;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.sed.raid_tracker_appli.Bdd;
import com.application.sed.raid_tracker_appli.Utils;

import java.util.HashMap;
import java.util.Map;


public class ApiRequestDelete {

    final static String urlUser = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/users";
    final static String urlAuthToken = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/auth-tokens";


    public static void deleteToken(Context context, final String token, final String id){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlAuthToken,
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
                super.getHeaders();

                Map<String, String> header = new HashMap<>();
                Utils.debug("Header",token);
                header.put("X-Auth-Token",token);
                return header;
            }

            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String>  params = new HashMap<>();
                Utils.debug("params", id);
                params.put("id",id);

                return params;
            }
        };
        requestQueue.add(postRequest);

    }

}
