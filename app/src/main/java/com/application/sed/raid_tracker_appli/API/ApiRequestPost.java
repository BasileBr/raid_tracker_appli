package com.application.sed.raid_tracker_appli.API;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.sed.raid_tracker_appli.Accueil;
import com.application.sed.raid_tracker_appli.CreateAccount;
import com.application.sed.raid_tracker_appli.ManageParcoursActivity;
import com.application.sed.raid_tracker_appli.RegisterVolunteersActivity;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.application.sed.raid_tracker_appli.VolunteerPreferenceActivity;
import com.application.sed.raid_tracker_appli.organizer.CourseActivity;
import com.application.sed.raid_tracker_appli.organizer.CreateCourse;
import com.application.sed.raid_tracker_appli.organizer.CreateParcours;
import com.application.sed.raid_tracker_appli.organizer.EditCourse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.views.overlay.Marker;

import java.util.HashMap;
import java.util.Map;

public class ApiRequestPost {

    final static String urlUser = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/users";
    final static String urlAuthToken = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/auth-tokens";
    final static String urlRaid = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/raids";
    final static String urlParcours="http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/parcours";
    final static String urlPoints="http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/points";
    final static String urlPostes="http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/postes";
    final static String urlTraces="http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/traces";
    final static String urlOrganisateursRaids = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/organisateurs/raids";
    final static String urlBenevoles = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/benevoles";
    final static String urlPrefPostes = "http://raidtracker.ddns.net/raid_tracker_api/web/app.php/api/prefpostes";




    public static void postUser(Context context, final String name, final String mail, final String pwd){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlUser,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response

                        CreateAccount.sendmail();
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //CreateAccount.failedsend();
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

    public static void postRaid(final Context context, final String token, final String name, final String lieu, final String date, final String edition, final String equipe, final boolean visibility){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        Utils.debug("CreateRaid", "nom " + name+" lieu " + lieu+ " date " + date+" edition  " + edition+ " equipe" +equipe+ " Visibility "+visibility);
        try {
            jsonObject.put("nom",name);
            jsonObject.put("lieu",lieu);
            jsonObject.put("date",date);
            jsonObject.put("edition",Integer.valueOf(edition));
            jsonObject.put("equipe", equipe);
            jsonObject.put("visibility", visibility);
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
             //   Utils.debug("Header",token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token",token);
                return header;
            }



        };
        requestQueue.add(postRequest);

    }

    //update informations Raid
    public static void postUpdateRaid(final Context context, final String token, final String idRaid, final String name, final String lieu, final String date, final String edition, final String equipe, final boolean visibility ){

        String urlfinale=urlRaid+'/'+idRaid;
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
            jsonObject.put("visibility",visibility);
            jsonArray.put(jsonObject);
        }catch (Exception e){

        }

        //Utils.debug("CreateRaid",jsonArray.toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, urlfinale, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response


                        Log.d("Response update RAid", response.toString());
                        if (context.toString().contains("com.application.sed.raid_tracker_appli.organizer.EditCourse")){
                            Intent intent = new Intent(context, CourseActivity.class);
                            context.startActivity(intent);

                        }
                       /* try {
                            String idRaid = response.getString("id");
                           postUserToRaid(context, token, Bdd.getUserid(), idRaid, response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/

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
                //   Utils.debug("Header",token);
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

    public static void postParcours(final Context context, final String token, final String idRaid, final String idParcoursPere, final String name, final String type, final Boolean etat){


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idRaid",idRaid);
            if(!idParcoursPere.equals("-1")){
                jsonObject.put("idParcoursPere",idParcoursPere);
            }
            jsonObject.put("nom",name);
            jsonObject.put("type",type);
            jsonObject.put("etat",etat);
            jsonArray.put(jsonObject);
        }catch (Exception e){

        }

       // Utils.debug("CreateRaid",jsonArray.toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, urlParcours, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response

                        try {
                            String idParcours = response.getString("id");
                            ApiRequestPost.postTrace(context,token,idParcours,"false");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Response creation parc", response.toString());


                       /* try {
                            String idRaid = response.getString("id");
                           postUserToRaid(context, token, Bdd.getUserid(), idRaid, response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/

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


    public static void postPoint(final Context context, final String token, final String idTrace, final Double longitude, final Double latitude, final int type, final int ordre, final View view){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idTrace",idTrace);
            jsonObject.put("lon",longitude);
            jsonObject.put("lat",latitude);
            jsonObject.put("type",type);
            jsonObject.put("ordre",ordre);
            jsonArray.put(jsonObject);
            Utils.debug("TAG",jsonObject.toString(2));
        }catch (Exception e){

        }

        // Utils.debug("CreateRaid",jsonArray.toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, urlPoints, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response


                        Log.d("Response creation poin", response.toString());

                        if (view != null){
                            CreateParcours.ShowPoste(view,response.toString());
                        }
                       /* try {
                            String idRaid = response.getString("id");
                           postUserToRaid(context, token, Bdd.getUserid(), idRaid, response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/

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


    public static void postPoste(final Context context, final String token, final String idPoint, final String type, final int nombre, final String heureDebut, final String heureFin){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idPoint",idPoint);
            jsonObject.put("type",type);
            jsonObject.put("nombre",nombre);
            jsonObject.put("heureDebut",heureDebut);
            jsonObject.put("heureFin",heureFin);
            jsonArray.put(jsonObject);
            Utils.debug("TAG",jsonObject.toString(2));
        }catch (Exception e){

        }

        // Utils.debug("CreateRaid",jsonArray.toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, urlPostes, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {


                        Log.d("Response creation poin", response.toString());


                       /* try {
                            String idRaid = response.getString("id");
                           postUserToRaid(context, token, Bdd.getUserid(), idRaid, response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/

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



    public static void postTrace(final Context context, final String token, final String idParcours, final String isCalibre) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idParcours", idParcours);
            if (!isCalibre.equals("-1")) {
                jsonObject.put("isCalibre", Boolean.valueOf(isCalibre));
            }
            jsonArray.put(jsonObject);
        } catch (Exception e) {


        }

        // Utils.debug("CreateRaid",jsonArray.toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, urlTraces, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        if (isCalibre.equals("-1")) {

                            Log.d("Response creation trace", response.toString());
                            try {

                                String idTrace = response.getString("id");
                                CreateParcours.trace(idTrace);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (isCalibre.equals("true")){
                            String idTrace = null;
                            try {
                                idTrace = response.getString("id");
                                ManageParcoursActivity.recupIdTrace(idTrace);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                       /* try {
                            String idRaid = response.getString("id");
                           postUserToRaid(context, token, Bdd.getUserid(), idRaid, response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/

                    }
                },
                new Response.ErrorListener() {
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
                Utils.debug("Header", token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token", token);
                return header;
            }
        };
        requestQueue.add(postRequest);

    }



    public static void postSpecificTrace(final Context context, final String token, final String idParcours, final String isCalibre) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idParcours", idParcours);
            if (!isCalibre.equals("-1")) {
                jsonObject.put("isCalibre", isCalibre);
            }
            jsonArray.put(jsonObject);
        } catch (Exception e) {


        }

        // Utils.debug("CreateRaid",jsonArray.toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, urlTraces, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response


                        Log.d("Response creation trace", response.toString());

                       /* try {
                            String idRaid = response.getString("id");
                           postUserToRaid(context, token, Bdd.getUserid(), idRaid, response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/

                    }
                },
                new Response.ErrorListener() {
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
                Utils.debug("Header", token);
                //header.put("Content-Type", "application/json");
                header.put("X-Auth-Token", token);
                return header;
            }
        };
        requestQueue.add(postRequest);

    }


    public static void postPrefPostes(final Context context, final String token, final Integer idPoste, final String idBenevole){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idPoste",idPoste);
            jsonObject.put("idBenevole",idBenevole);
            jsonArray.put(jsonObject);
        }catch (Exception e){

        }

        // Utils.debug("CreateRaid",jsonArray.toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, urlPrefPostes, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Respons crea prefpostes", response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error prefposte", error.toString());
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


    public static void postNewBenevole(final Context context, final String token, final String idRaid, final String idUser){

        String urlFinale= urlBenevoles+"/raids/"+idRaid+"/users/"+idUser;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idUser",idUser);
            jsonObject.put("idRaid",idRaid);
            jsonArray.put(jsonObject);
        }catch (Exception e){

        }

        // Utils.debug("CreateRaid",jsonArray.toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, urlFinale, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("rep postNewBenevol", response.toString());
                        VolunteerPreferenceActivity.recupId(response.toString());
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
