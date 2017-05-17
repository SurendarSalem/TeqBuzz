package com.kanthan.teqbuzz.utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kanthan.teqbuzz.AppController;
import com.kanthan.teqbuzz.Entities.AlarmResponseEntity;
import com.kanthan.teqbuzz.Entities.TeqBuzzParser;
import com.kanthan.teqbuzz.Entities.VehicleListEntity;
import com.kanthan.teqbuzz.fragments.BusMapFragment;
import com.kanthan.teqbuzz.fragments.LoginFragment;
import com.kanthan.teqbuzz.fragments.SettingsFragment;
import com.kanthan.teqbuzz.models.Schedule;
import com.kanthan.teqbuzz.models.User;
import com.kanthan.teqbuzz.models.Vehicle;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 3/12/2016.
 */
public class WebService {

    public static final String ADD_PRODUCT_URL = "";
    public static final String SERVER_URL = "http://api.teqbuzz.com/index.php/";
    public static final String BASE_URL = "http://api.teqbuzz.com/";
    public static final String SIGNUP_URL = SERVER_URL + "users/register?";
    public static final String VEHICLE_IMAGE_URL = BASE_URL + "uploads/vehicle/images/";
    public static final String ADD_VEHICLE_AS_FAVOURITE_URL = SERVER_URL + "users/add_favourite?";
    public static final String LOGIN_URL = SERVER_URL + "users/login?";
    public static final String GET_FAVOURITE_VEHICLES_URL = SERVER_URL + "users/user_favourite?";
    public static final String GET_SHARED_VEHICLES_URL = SERVER_URL + "users/shared_vehicle_details?vehicle_id=";
    public static final String CHECK_FAVOURITE_VEHICLES_URL = SERVER_URL + "users/check_user_favourite?";
    public static final String SEND_ALARM_REQUEST_URL = SERVER_URL + "users/add_vehicle_alarm?";
    public static final String RESET_PASSWORD_URL = SERVER_URL + "users/reset_password?";
    public static final String GET_VEHICLE_LIST_URL = SERVER_URL + "users/get_vehicle?";
    public static final String GET_VEHICLE_DETAILS_URL = SERVER_URL + "users/vehicle_details?vehicle_id=";
    public static final String GET_SCHEDULE_LIST_URL = SERVER_URL + "users/vehicle_schedule?vehicle_id=";
    public static final String GET_USER_DETAILS = SERVER_URL + "users/user_update?";
    public static final String UPDATE_USER_URL = SERVER_URL + "users/user_update?";
    public static final String UPDATE_VEHICLE_LOCATION_URL = SERVER_URL + "users/update_vehicle?";
    public static final String INTENT_FILTER_LOGIN_RESPONSE = "login_response";
    public static final String INTENT_FILTER_UPDATE_USER_RESPONSE = "update_user_response";
    public static final String INTENT_FILTER_GPS_UPDATE_RESPONSE = "gps_update_response";
    public static final String INTENT_FILTER_SIGNUP_RESPONSE = "signup_response";
    public static final String INTENT_FILTER_VEHICLE_LIST_RESPONSE = "vehicle_list_response";
    public static final String INTENT_FILTER_SCHEDULE_LIST_RESPONSE = "vehicle_schedule_list_response";
    public static final String INTENT_FILTER_CHECK_VEHICLE_FAVOURITE_RESPONSE = "check_vehicle_favourite_response";
    public static final String INTENT_FILTER_FAV_VEHICLE_LIST_RESPONSE = "fav_vehicle_list_response";
    public static final String INTENT_FILTER_ADD_VEHICLE_FAVOURITE_RESPONSE = "add_vehicle_favourite_response";
    Context context;
    public static JsonObjectRequest getVehicleRequest;
    Preferences preferences;

    public WebService(Context context) {
        this.context = context;
        preferences = new Preferences(context);
    }

    private String TAG;

    public void addBus(Context context) {

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String url = "http://api.androidhive.info/volley/person_object.json";

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "Androidhive");
                params.put("email", "abc@androidhive.info");
                params.put("password", "password123");

                return params;
            }

        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    public void getfacebookImageService(String str_facebook_id) {


        String tag_json_obj = "json_obj_req";


        String fbGraphProfileImageApi = "http://graph.facebook.com/" + str_facebook_id + "/picture?type=square&redirect=false";


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                fbGraphProfileImageApi, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();

                        Log.d("", response.toString());
                        Intent responseIntent = new Intent("fb_image_response");
                        responseIntent.putExtra("response", res);
                        context.sendBroadcast(responseIntent);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Intent responseIntent = new Intent("fb_image_response");
                responseIntent.putExtra("response", "errors" + error.getMessage());
                context.sendBroadcast(responseIntent);
                VolleyLog.d("", "Error: " + error.getMessage());

            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
// Adding request to request queue
        AppController obj = AppController.getInstance();

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }


    public void signUp(final Context context, String signUpUrl) {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        signUpUrl = signUpUrl.replace(" ", "%20");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                signUpUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();
                        try {
                            res = URLDecoder.decode(res, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Log.d("", response.toString());
                        //Toast.makeText(context, "Sign up successfully", Toast.LENGTH_LONG).show();
                        Intent responseIntent = new Intent(INTENT_FILTER_SIGNUP_RESPONSE);
                        responseIntent.putExtra("response", res);
                        context.sendBroadcast(responseIntent);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Intent responseIntent = new Intent(INTENT_FILTER_SIGNUP_RESPONSE);
                responseIntent.putExtra("response", "errors" + error.getMessage());
                context.sendBroadcast(responseIntent);
                VolleyLog.d("", "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        AppController obj = AppController.getInstance();

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void login(final Context context, String signUpUrl) {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        signUpUrl = signUpUrl.replace(" ", "%20");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                signUpUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();
                        try {
                            res = URLDecoder.decode(res, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Log.d("", response.toString());
                        //Toast.makeText(context, "Sign up successfully", Toast.LENGTH_LONG).show();
                        Intent responseIntent = new Intent(INTENT_FILTER_LOGIN_RESPONSE);
                        responseIntent.putExtra("response", res);
                        context.sendBroadcast(responseIntent);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Intent responseIntent = new Intent(INTENT_FILTER_LOGIN_RESPONSE);
                responseIntent.putExtra("response", "errors" + error.getMessage());
                context.sendBroadcast(responseIntent);
                VolleyLog.d("", "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        AppController obj = AppController.getInstance();

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    // update user

    public void updateUser(final Context context, String updateUserUrl) {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        updateUserUrl = updateUserUrl.replace(" ", "%20");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                updateUserUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();
                        try {
                            res = URLDecoder.decode(res, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Log.d("", response.toString());
                        //Toast.makeText(context, "Sign up successfully", Toast.LENGTH_LONG).show();
                        Intent responseIntent = new Intent(INTENT_FILTER_UPDATE_USER_RESPONSE);
                        responseIntent.putExtra("response", res);
                        context.sendBroadcast(responseIntent);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Intent responseIntent = new Intent(INTENT_FILTER_UPDATE_USER_RESPONSE);
                responseIntent.putExtra("response", "errors" + error.getMessage());
                context.sendBroadcast(responseIntent);
                VolleyLog.d("", "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        AppController obj = AppController.getInstance();

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    // Get Vehicles list

    public void getVehicleList(final Context context, final BusMapFragment busMapFragment, String getVehicleListUrl, final int mode, final boolean favouriteFlagEnabled, final int callMode, final boolean isSingleRun) {
        // Tag used to cancel the request
        Log.d("getVehicleListMarch", getVehicleListUrl);
        String tag_json_obj = "getVehicleList";
        /*AppController.getInstance().cancelPendingRequests("getSharedVehicles");
        AppController.getInstance().cancelPendingRequests("getVehicleList");
        AppController.getInstance().cancelPendingRequests("getVehicleDetails");*/
        clearAll();

        getVehicleRequest = new JsonObjectRequest(Request.Method.GET,
                getVehicleListUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();
                        //res = "{\"status\":[{\"id\":\"42355\",\"device_id\":null,\"latitude\":\"48.413195\",\"longitude\":\"9.964432\",\"location_date\":\"2016-09-02\",\"location_time\":\"09:53:15\",\"created_datetime\":\"2016-09-02 09:53:17\",\"distance\":\"7438.560127132921\",\"vehicle_id\":null,\"vehicle_line_number\":null,\"vehicle_latitude\":null,\"vehicle_longitude\":null,\"vehicle_agency\":null,\"country_id\":null,\"images\":null,\"vehicle_type\":null,\"date_added\":null,\"date_modified\":null,\"status\":null},{\"id\":\"44825\",\"device_id\":\"491#\",\"latitude\":\"48.413227\",\"longitude\":\"9.964357\",\"location_date\":\"2016-09-08\",\"location_time\":\"14:39:54\",\"created_datetime\":\"2016-09-08 14:39:54\",\"distance\":\"7438.566133629853\",\"vehicle_id\":\"2\",\"vehicle_line_number\":\"3\",\"vehicle_latitude\":\"12.927900\",\"vehicle_longitude\":\"77.627098\",\"vehicle_agency\":\"SWU ULM\",\"country_id\":\"81\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-08 14:49:40\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"0\"},{\"id\":\"104485\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"1\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104486\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"3\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104487\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"4\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104486\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"5\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104487\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"6\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104486\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"7\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104487\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"8\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104486\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"9\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104487\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"10\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"}]}";
                        try {
                            res = URLDecoder.decode(res, "UTF-8");

                            final String finalRes = res;
                            new AsyncTask<Void, Void, Void>() {
                                VehicleListEntity vehicleListEntity;

                                @Override
                                protected Void doInBackground(Void... voids) {
                                    vehicleListEntity = new TeqBuzzParser().getVehicleListEntity(finalRes,favouriteFlagEnabled);
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    busMapFragment.onTeqBuzzVehicleListEntityLoaded(vehicleListEntity, callMode, isSingleRun);
                                }
                            }.execute();
                        } catch (UnsupportedEncodingException e) {
                            busMapFragment.onTeqBuzzVehicleListEntityLoaded(null, callMode, isSingleRun);
                            e.printStackTrace();
                        }
                        Log.d("getVehicleList", response.toString());
                        //Toast.makeText(context, "Sign up successfully", Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                busMapFragment.onTeqBuzzVehicleListEntityLoaded(null, callMode, isSingleRun);
                VolleyLog.d("getVehicleList", "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });

        // Adding request to request queue
        getVehicleRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        getVehicleRequest.setTag(tag_json_obj);
        AppController obj = AppController.getInstance();

        AppController.getInstance().addToRequestQueue(getVehicleRequest);

    }


    // Get Vehicles list

    public void getSharedVehicles(final Context context, final BusMapFragment busMapFragment, String url, final int mode, final boolean favouriteFlagEnabled, final int callMode) {


        // Tag used to cancel the request
        String tag_json_obj = "getSharedVehicles";
        String getSharedVehiclesUrl = GET_SHARED_VEHICLES_URL + url;
        Log.d("getSharedVehicles", getSharedVehiclesUrl);
        AppController.getInstance().cancelPendingRequests("getSharedVehicles");
        AppController.getInstance().cancelPendingRequests("getVehicleList");
        AppController.getInstance().cancelPendingRequests("getVehicleDetails");

        getVehicleRequest = new JsonObjectRequest(Request.Method.GET,
                getSharedVehiclesUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();
                        //res = "{\"status\":[{\"id\":\"42355\",\"device_id\":null,\"latitude\":\"48.413195\",\"longitude\":\"9.964432\",\"location_date\":\"2016-09-02\",\"location_time\":\"09:53:15\",\"created_datetime\":\"2016-09-02 09:53:17\",\"distance\":\"7438.560127132921\",\"vehicle_id\":null,\"vehicle_line_number\":null,\"vehicle_latitude\":null,\"vehicle_longitude\":null,\"vehicle_agency\":null,\"country_id\":null,\"images\":null,\"vehicle_type\":null,\"date_added\":null,\"date_modified\":null,\"status\":null},{\"id\":\"44825\",\"device_id\":\"491#\",\"latitude\":\"48.413227\",\"longitude\":\"9.964357\",\"location_date\":\"2016-09-08\",\"location_time\":\"14:39:54\",\"created_datetime\":\"2016-09-08 14:39:54\",\"distance\":\"7438.566133629853\",\"vehicle_id\":\"2\",\"vehicle_line_number\":\"3\",\"vehicle_latitude\":\"12.927900\",\"vehicle_longitude\":\"77.627098\",\"vehicle_agency\":\"SWU ULM\",\"country_id\":\"81\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-08 14:49:40\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"0\"},{\"id\":\"104485\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"1\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104486\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"3\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104487\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"4\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104486\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"5\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104487\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"6\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104486\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"7\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104487\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"8\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104486\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"9\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104487\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"10\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"}]}";
                        try {
                            res = URLDecoder.decode(res, "UTF-8");

                            final String finalRes = res;
                            new AsyncTask<Void, Void, Void>() {
                                VehicleListEntity vehicleListEntity;

                                @Override
                                protected Void doInBackground(Void... voids) {
                                    vehicleListEntity = new TeqBuzzParser().getVehicleListEntity(finalRes,false);
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    busMapFragment.onTeqBuzzVehicleListEntityLoaded(vehicleListEntity, callMode, false);
                                }
                            }.execute();
                        } catch (UnsupportedEncodingException e) {
                            busMapFragment.onTeqBuzzVehicleListEntityLoaded(null, callMode, false);
                            e.printStackTrace();
                        }
                        Log.d("getVehicleList", response.toString());
                        //Toast.makeText(context, "Sign up successfully", Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                busMapFragment.onTeqBuzzVehicleListEntityLoaded(null, callMode, false);
                VolleyLog.d("getVehicleList", "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });

        // Adding request to request queue
        getVehicleRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        getVehicleRequest.setTag(tag_json_obj);
        AppController obj = AppController.getInstance();
        AppController.getInstance().addToRequestQueue(getVehicleRequest);

    }

    public void clear() {

    }

    public void getVehicleDetails(Activity mActivity, final BusMapFragment busMapFragment, String vehicle_id, final int callMode) {
        // Tag used to cancel the request
        String tag_json_obj = "getVehicleDetails";
        String getVehicleListUrl = GET_VEHICLE_DETAILS_URL + vehicle_id;

        AppController.getInstance().cancelPendingRequests("getSharedVehicles");
        AppController.getInstance().cancelPendingRequests("getVehicleList");
        AppController.getInstance().cancelPendingRequests("getVehicleDetails");

        getVehicleRequest = new JsonObjectRequest(Request.Method.GET,
                getVehicleListUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();
                        //res = "{\"status\":[{\"id\":\"42355\",\"device_id\":null,\"latitude\":\"48.413195\",\"longitude\":\"9.964432\",\"location_date\":\"2016-09-02\",\"location_time\":\"09:53:15\",\"created_datetime\":\"2016-09-02 09:53:17\",\"distance\":\"7438.560127132921\",\"vehicle_id\":null,\"vehicle_line_number\":null,\"vehicle_latitude\":null,\"vehicle_longitude\":null,\"vehicle_agency\":null,\"country_id\":null,\"images\":null,\"vehicle_type\":null,\"date_added\":null,\"date_modified\":null,\"status\":null},{\"id\":\"44825\",\"device_id\":\"491#\",\"latitude\":\"48.413227\",\"longitude\":\"9.964357\",\"location_date\":\"2016-09-08\",\"location_time\":\"14:39:54\",\"created_datetime\":\"2016-09-08 14:39:54\",\"distance\":\"7438.566133629853\",\"vehicle_id\":\"2\",\"vehicle_line_number\":\"3\",\"vehicle_latitude\":\"12.927900\",\"vehicle_longitude\":\"77.627098\",\"vehicle_agency\":\"SWU ULM\",\"country_id\":\"81\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-08 14:49:40\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"0\"},{\"id\":\"104485\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"1\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104486\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"3\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104487\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"4\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104486\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"5\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104487\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"6\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104486\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"7\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104487\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"8\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104486\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"9\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"},{\"id\":\"104487\",\"device_id\":\"254#1\",\"latitude\":\"-1.293505\",\"longitude\":\"36.850340\",\"location_date\":\"2016-10-18\",\"location_time\":\"07:07:54\",\"created_datetime\":\"2016-10-18 07:07:55\",\"distance\":\"4786.35042863005\",\"vehicle_id\":\"10\",\"vehicle_line_number\":\"24\",\"vehicle_latitude\":\"-1.283330\",\"vehicle_longitude\":\"36.816669\",\"vehicle_agency\":\"Beat\",\"country_id\":\"0\",\"images\":\"\",\"vehicle_type\":\"1\",\"date_added\":\"2016-09-27 22:41:26\",\"date_modified\":\"2016-03-30 18:03:02\",\"status\":\"0\"}]}";
                        try {
                            res = URLDecoder.decode(res, "UTF-8");

                            VehicleListEntity vehicleListEntity = new TeqBuzzParser().getVehicleDetailEntity(res);
                            busMapFragment.onTeqBuzzVehicleListEntityLoaded(vehicleListEntity, callMode, false);
                        } catch (UnsupportedEncodingException e) {
                            busMapFragment.onTeqBuzzVehicleListEntityLoaded(null, callMode, false);
                            e.printStackTrace();
                        }
                        Log.d("", response.toString());
                        //Toast.makeText(context, "Sign up successfully", Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                busMapFragment.onTeqBuzzVehicleListEntityLoaded(null, callMode, false);
                VolleyLog.d("", "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });

        // Adding request to request queue
        getVehicleRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        getVehicleRequest.setTag(tag_json_obj);
        AppController.getInstance().addToRequestQueue(getVehicleRequest);

    }

    public void clearAll() {
        AppController.getInstance().cancelPendingRequests("getSharedVehicles");
        AppController.getInstance().cancelPendingRequests("getVehicleList");
        AppController.getInstance().cancelPendingRequests("getVehicleDetails");
        AppController.getInstance().cancelPendingRequests("getFavVehicleList");

    }

    public void getVehicleScheduleList(final Context context, String vehicleId, final BusMapFragment busMapFragment) {
        // Tag used to cancel the request
        String tag_json_obj = "json_get_vehicle_req";
        String userId = User.DEFAULT_USER_ID;
        if (preferences.isLoginned()) {
            userId = preferences.getUser().getUser_id();
        }
        String getVehicleScheduleListUrl = GET_SCHEDULE_LIST_URL + vehicleId + "&user_id=" + userId;

        getVehicleRequest = new JsonObjectRequest(Request.Method.GET,
                getVehicleScheduleListUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();
                        try {
                            res = URLDecoder.decode(res, "UTF-8");
                            ArrayList<Schedule> schedules = new TeqBuzzParser(context).parseAndGetTeqbuzzSchedules(res);
                            busMapFragment.onTeqBuzzSchedulesLoaded(schedules);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            busMapFragment.onTeqBuzzSchedulesLoaded(null);
                        }
                        Log.d("", response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("", "Error: " + error.getMessage());
                busMapFragment.onTeqBuzzSchedulesLoaded(null);


            }
        });

        // Adding request to request queue
        getVehicleRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        getVehicleRequest.setTag("json_get_vehicle_req");
        AppController obj = AppController.getInstance();

        AppController.getInstance().addToRequestQueue(getVehicleRequest);

    }

    public static void removeGetVehicleServiceRequest() {
        AppController.getInstance().getRequestQueue().cancelAll("json_get_vehicle_req");

    }

    // Get favourite Vehicles list

    public void getFavVehicleList(final Context context, final BusMapFragment busMapFragment, String url, final int mode, final boolean favouriteFlagEnabled, final int callMode) {
        // Tag used to cancel the request
        clearAll();
        String tag_json_obj = "getFavVehicleList";
        String getFavVehicleListUrl = GET_FAVOURITE_VEHICLES_URL + url;
        Log.d("", "");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                getFavVehicleListUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();
                        try {
                            res = URLDecoder.decode(res, "UTF-8");
                            final String finalRes = res;
                            new AsyncTask<Void, Void, Void>() {
                                VehicleListEntity vehicleListEntity;

                                @Override
                                protected Void doInBackground(Void... voids) {
                                    vehicleListEntity = new TeqBuzzParser().getVehicleListEntity(finalRes,false);
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    busMapFragment.onTeqBuzzVehicleListEntityLoaded(vehicleListEntity, callMode, false);
                                }
                            }.execute();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Log.d("", response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                busMapFragment.onTeqBuzzVehicleListEntityLoaded(null, callMode, false);
                VolleyLog.d("", "Error: " + error.getMessage());
            }
        });
        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        AppController obj = AppController.getInstance();
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void getFavVehicleListFromServer(final Context context, final BusMapFragment busMapFragment, String userId) {
        // Tag used to cancel the request
        clearAll();
        String tag_json_obj = "getFavVehicleListFromServer";
        String getFavVehicleListUrl = GET_FAVOURITE_VEHICLES_URL + userId;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                getFavVehicleListUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();
                        try {
                            res = URLDecoder.decode(res, "UTF-8");
                            final String finalRes = res;
                            new AsyncTask<Void, Void, Void>() {
                                VehicleListEntity vehicleListEntity;

                                @Override
                                protected Void doInBackground(Void... voids) {
                                    vehicleListEntity = new TeqBuzzParser().getVehicleListEntity(finalRes,false);
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    addFavVehiclesToLocalDB(context, busMapFragment, vehicleListEntity);
                                }
                            }.execute();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Log.d("", response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                addFavVehiclesToLocalDB(context, busMapFragment, null);
                VolleyLog.d("", "Error: " + error.getMessage());
            }
        });
        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        AppController obj = AppController.getInstance();
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void addFavVehiclesToLocalDB(Context context, BusMapFragment busMapFragment, VehicleListEntity vehicleListEntity) {
        AppDatabaseHelper appDatabaseHelper = new AppDatabaseHelper(context);
        ArrayList<Vehicle> favVehicles = vehicleListEntity.getVehicles();
        for (Vehicle vehicle : favVehicles) {
            appDatabaseHelper.addVehicleToFavList(vehicle);
        }
        busMapFragment.onFavVehiclesLoaded();
    }

    public void getUserDetails(final Context context, final SettingsFragment settingsFragment) {
        // Tag used to cancel the request
        String tag_json_obj = "getUserDetails";
        Preferences pref = new Preferences(context);
        String getUserDetailsUrl = GET_USER_DETAILS + pref.getUser().getUser_id();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                getUserDetailsUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();
                        try {
                            res = URLDecoder.decode(res, "UTF-8");
                            settingsFragment.refreshUserDetails(res);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            settingsFragment.refreshUserDetails(res);
                        }
                        Log.d("", response.toString());
                        //Toast.makeText(context, "Sign up successfully", Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                settingsFragment.refreshUserDetails(null);
            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        AppController obj = AppController.getInstance();

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    // check is favourite Vehicle

    public void checkIsVehicleFavourite(final Context context, String myUserId, String vehicle_id) {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String checkIsVehicleFavouriteUrl = CHECK_FAVOURITE_VEHICLES_URL + "user_id=" + myUserId + "&vehicle_id=" + vehicle_id;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                checkIsVehicleFavouriteUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();
                        try {
                            res = URLDecoder.decode(res, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Log.d("", response.toString());
                        //Toast.makeText(context, "Sign up successfully", Toast.LENGTH_LONG).show();
                        Intent responseIntent = new Intent(INTENT_FILTER_CHECK_VEHICLE_FAVOURITE_RESPONSE);
                        responseIntent.putExtra("response", res);
                        context.sendBroadcast(responseIntent);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Intent responseIntent = new Intent(INTENT_FILTER_CHECK_VEHICLE_FAVOURITE_RESPONSE);
                responseIntent.putExtra("response", "errors" + error.getMessage());
                context.sendBroadcast(responseIntent);
                VolleyLog.d("", "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        AppController obj = AppController.getInstance();

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void addRemoveVehicleAsFav(final Context context, final BusMapFragment busMapFragment, final Vehicle vehicle, String addVehicleAsFavouriteUrl) {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        addVehicleAsFavouriteUrl = ADD_VEHICLE_AS_FAVOURITE_URL + addVehicleAsFavouriteUrl;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                addVehicleAsFavouriteUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();
                        try {
                            res = URLDecoder.decode(res, "UTF-8");
                            if (res.contains("added")) {
                                Log.d("addRemoveVehicleAsFav", res);
                                busMapFragment.onVehicleFavOptionDone(Vehicle.IS_FAV, vehicle);
                            } else if (res.contains("removed")) {
                                Log.d("addRemoveVehicleAsFav", res);
                                busMapFragment.onVehicleFavOptionDone(Vehicle.IS_NOT_FAV, vehicle);
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        //Log.d("", response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("", "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        AppController obj = AppController.getInstance();

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void sendAlarmRequest(Schedule schedule, Vehicle selectedVehicle, final BusMapFragment busMapFragment, String alarmSet) {
        // Tag used to cancel the request
        String tag_json_obj = "sendAlarmRequest";
        String myUserId = preferences.getUser().getUser_id();

        String sendAlarmRequestUrl = SEND_ALARM_REQUEST_URL +
                "user_id=" + myUserId +
                "&vehicle_id=" + schedule.getVehicle_id() +
                "&schedule_id=" + schedule.getSchedule_id() +
                "&stop_id=" + schedule.getStop_id() +
                "&alarm_status=" + alarmSet;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                sendAlarmRequestUrl, null,
                new Response.Listener<JSONObject>() {
                    AlarmResponseEntity alarmResponseEntity;

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();
                        try {
                            res = URLDecoder.decode(res, "UTF-8");
                            alarmResponseEntity = new TeqBuzzParser().getAlarmResponseEntity(res);
                            busMapFragment.onAlarmRequestDone(alarmResponseEntity);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            busMapFragment.onAlarmRequestDone(alarmResponseEntity);
                        }
                        Log.d("sendAlarmRequest", response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("", "Error: " + error.getMessage());
                busMapFragment.onAlarmRequestDone(null);
                // hide the progress dialog

            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void resetPassword(String email, final LoginFragment loginFragment) {
        String tag_json_obj = "resetPassword";

        String resetPasswordUrl = RESET_PASSWORD_URL +
                "email_id=" + email;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                resetPasswordUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();
                        try {
                            res = URLDecoder.decode(res, "UTF-8");
                            loginFragment.onPasswordReset("");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            loginFragment.onPasswordReset("");
                        }
                        Log.d("sendAlarmRequest", response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("", "Error: " + error.getMessage());
                loginFragment.onPasswordReset("");
                // hide the progress dialog

            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
