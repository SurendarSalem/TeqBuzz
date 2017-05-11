package com.kanthan.teqbuzz.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kanthan.teqbuzz.AppController;
import com.kanthan.teqbuzz.utilities.Constants;
import com.kanthan.teqbuzz.utilities.WebService;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by user on 3/1/2016.
 */
public class GetLocationService extends Service {
    private static final String TAG = "GetLocationService";
    GPSTracker gpsTracker;
    private String str_latitude, str_longitude;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gpsTracker = new GPSTracker(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Thread parentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                startLocationUpdationService();
            }
        });
        parentThread.start();

        return START_STICKY;
    }

    private void startLocationUpdationService() {
        //do job here

        gpsTracker = new GPSTracker(getApplicationContext());

        if (gpsTracker.canGetLocation()) {

            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();


           /* Preferences preferences = new Preferences(this);
            double counter = new Preferences(this).getLocalCounter();
            counter += 0.1d;
            long x = (long) counter; // x
            preferences.setLocalCounter(x);
            counter = 1 * counter;
            str_latitude = String.valueOf(latitude + counter);
           */
            str_latitude = String.valueOf(latitude);
            str_longitude = String.valueOf(longitude);


            if (str_latitude.length() > 0 && str_longitude.length() > 0) {

                updateGpsInServer(str_latitude, str_longitude);
                Log.e(TAG, str_latitude);
                Log.e(TAG, str_longitude);

            } else {
                //Toast.makeText(mActivity, context.getResources().getString(R.string.location_null), Toast.LENGTH_LONG).show();
            }


        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            // gpsTracker.showSettingsAlert();
            // Utility.displayDialogForEnablingGPS(mActivity);
        }


        //job completed. Rest for 5 second before doing another one

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //run service again
        startLocationUpdationService();
    }

    private void updateGpsInServer(String str_latitude, String str_longitude) {

        String updateVehicleUrl = WebService.UPDATE_VEHICLE_LOCATION_URL +
                "vehicle_id=" + Constants.MY_VEHICLE_ID + "&latitude=" + str_latitude + "&longitude=" + str_longitude;

        String tag_json_obj = "json_obj_req";

        //Toast.makeText(this, updateVehicleUrl, Toast.LENGTH_SHORT).show();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                updateVehicleUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();
                        try {
                            res = URLDecoder.decode(res, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Log.e("GPS Update Response", response.toString());
                        //Toast.makeText(context, "Sign up successfully", Toast.LENGTH_LONG).show();
                        Intent responseIntent = new Intent(WebService.INTENT_FILTER_GPS_UPDATE_RESPONSE);
                        responseIntent.putExtra("response", res);
                        sendBroadcast(responseIntent);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Intent responseIntent = new Intent(WebService.INTENT_FILTER_GPS_UPDATE_RESPONSE);
                responseIntent.putExtra("response", "errors" + error.getMessage());
                sendBroadcast(responseIntent);
                VolleyLog.d("", "Error: " + error.getMessage());
                Log.e("GPS Update Response", "error");


            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        AppController obj = AppController.getInstance();

        try {
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        } catch (StackOverflowError error) {
            error.printStackTrace();
        }


    }

}