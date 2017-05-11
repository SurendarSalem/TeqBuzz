package com.kanthan.teqbuzz.onebusaway;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.Marker;
import com.kanthan.teqbuzz.AppController;
import com.kanthan.teqbuzz.MainActivity;
import com.kanthan.teqbuzz.fragments.BusMapFragment;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by suren on 7/19/2016.
 */
public class OBAWebService {

    public static String OBA_BASE_URL = "http://api.pugetsound.onebusaway.org/api/where/";
    public static String GET_STOPS_NEARBY = OBA_BASE_URL + "stops-for-location.json?key=TEST&lat=47.653435&lon=-122.305641";
    public static String GET_TRIPS_FOR_ROUTE = OBA_BASE_URL + "trips-for-route/route_id.json?key=TEST&includeStatus=true&includeSchedules=true";
    public static String GET_STOPS_FOR_ROUTE = OBA_BASE_URL + "stops-for-route/route_id.json?key=TEST&version=2";
    public static String GET_ARRIVAL_DEPARTURE_FOR_STOP = OBA_BASE_URL + "arrivals-and-departures-for-stop/stop_id.json?key=TEST";
    public static String GET_TRIPS_FOR_LOCATION = OBA_BASE_URL + "trips-for-location.json?key=TEST";
    public static String GET_TRIPS_DETAILS_FOR_VEHICLE_URL = OBA_BASE_URL + "trip-for-vehicle/vehicle_id.json?key=TEST";
    public static String GET_TRIPS_DETAILS_FOR_TRIP_URL = OBA_BASE_URL + "trip-details/trip_id.json?key=TEST&includeSchedules=true&includeStatus=true&includeTrips=true";
    public static String TEST_DECODED_LEVEL = "212";
    public static String TEST_DECODED_POLYLINES = "ynqaHzytiVhB_BlCcCnC_ClCaCtBmBVUnCcCnBcB\\[dCyBHGHKxAqAb@_@DEVUR]LSHYX?lC?~B@`@?D?d@?~BuB?}A?U?U?yD?C@W?uD?aC?eG?aB?a@?_C?uE@o@As@@S?G?o@?MAm@?E?}@?yB@uDAuMrAMvA?fD?d@?nCBjCBL?F?~B@R?H?HALGNEFEHELMFERYNUVi@tCwFT[RQPM`@SHEJGv@I`FBfA?nF@rH?p@?rAB@?lB?VIVMPMLCxA?l@BvB?dE?fD?\\?pBBfE?R?J[JMrCiCt@q@~FiFb@c@b@_@`@_@~@w@fD{CzBsB`@_@`Ay@z@a@fFiAVGnE_A|Dw@lH_BjJuBHAlOcDj@OlLcChH{Af@M~Bi@^AnEUJAHAfE}@@?pAWHCB?z@Q`GmArB_@jH}Ad@KzAWfA[@?|EcAv@OvDu@zCm@xCo@j@MtCm@pAWvD{@VEhBc@fAOrFgAVEpEaAxCo@|@QtAWj@MhCk@\\GXInB_@~@Sz@Sh@Q`Ai@v@e@j@c@p@s@DE@Ab@e@jAmB\\m@xAeC?IB_DBaIH{N@SJe@^yAH[j@eC~AqG@K^wALg@XeAZyANo@XiAFSJy@@GFwAAwD?I}IHK?CCEK?iBAqB?Q";
    int getOBAStopListForRouteCount = 0;


    Context context;

    public OBAWebService(Context context) {
        this.context = context;
    }

    public void getNearbyOBAStops(final Context context, String getNearbyUrl, final OBAWebServiceCallBack obaWebServiceCallBack) {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        getNearbyUrl = getNearbyUrl.replace(" ", "%20");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                getNearbyUrl, null,
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
                        OBAStopEntity obaStopEntity = new OBAJsonParser().parseAndGetRouteEntity(res);
                        obaWebServiceCallBack.onOBANearByRoutesLoaded(res, obaStopEntity);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("", "Error: " + error.getMessage());
                obaWebServiceCallBack.onOBANearByRoutesLoaded(error.getMessage(), null);
                // hide the progress dialog

            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        AppController obj = AppController.getInstance();

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void getOBAStopListForRoute(final ArrayList<OBARouteEntity.Data.Route> routes, final MainActivity obaWebServiceCallBack) {

        String tag_json_obj = "json_obj_req";
        getOBAStopListForRouteCount = 0;

        final ArrayList<OBAStopListEntity> obaStopListEntities = new ArrayList<OBAStopListEntity>();
        for (final OBARouteEntity.Data.Route route : routes) {

            String getStopsForRouteUrl = GET_STOPS_FOR_ROUTE.replace("route_id", route.getId());

            getStopsForRouteUrl = getStopsForRouteUrl.replace(" ", "%20");

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    getStopsForRouteUrl, null,
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
                            OBAStopListEntity obaStopListEntity = new OBAJsonParser().parseAndGetStopListEntity(res, route.getId());
                            obaStopListEntities.add(obaStopListEntity);
                            getOBAStopListForRouteCount++;
                            if (getOBAStopListForRouteCount == routes.size()) {
                                obaWebServiceCallBack.onOBAStopsForRoutesLoaded("", obaStopListEntities);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    VolleyLog.d("", "Error: " + error.getMessage());
                    obaStopListEntities.add(null);
                    getOBAStopListForRouteCount++;
                    if (getOBAStopListForRouteCount == routes.size()) {
                        obaWebServiceCallBack.onOBAStopsForRoutesLoaded(null, null);
                    }


                }
            });

            // Adding request to request queue
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
            AppController obj = AppController.getInstance();

            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        }

    }

    public void getArrivalDepartureForStop(final String stop_id, final MainActivity obaWebServiceCallBack) {

        String tag_json_obj = "json_obj_req";

        String getArrivalDeparturesForStopUrl = GET_ARRIVAL_DEPARTURE_FOR_STOP.replace("stop_id", stop_id);

        getArrivalDeparturesForStopUrl = getArrivalDeparturesForStopUrl.replace(" ", "%20");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                getArrivalDeparturesForStopUrl, null,
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
                        OBAArrivalDepartureEntity obaArrivalDepartureEntity = new OBAJsonParser().parseAndGetArrivalDepartureEntity(res);
                        obaWebServiceCallBack.onOBAArrivalDepartureLoaded(res, obaArrivalDepartureEntity);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("", "Error: " + error.getMessage());
                // hide the progress dialog
                obaWebServiceCallBack.onOBAArrivalDepartureLoaded(null, null);

            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        AppController obj = AppController.getInstance();

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void getOBAStopsForRoute(final String routeId, final MainActivity mainActivity) {

        String TAG = "getOBAStopsForRoute";

        String getStopsForRouteUrl = GET_STOPS_FOR_ROUTE.replace("route_id", routeId);

        getStopsForRouteUrl = getStopsForRouteUrl.replace(" ", "%20");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                getStopsForRouteUrl, null,
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
                        OBAStopListEntity obaStopListEntity = new OBAJsonParser().parseAndGetStopListEntity(res, routeId);
                        mainActivity.onOBAStopsForRouteLoaded(null, obaStopListEntity);


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("", "Error: " + error.getMessage());
                mainActivity.onOBAStopsForRouteLoaded(null, null);

                // hide the progress dialog

            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        AppController obj = AppController.getInstance();

        AppController.getInstance().addToRequestQueue(jsonObjReq, TAG);
    }

    public void getOBATripsForRoute(final String routeId, final MainActivity mainActivity) {

        String TAG = "getOBATripsForRoute";

        String getStopsForRouteUrl = GET_TRIPS_FOR_ROUTE.replace("route_id", routeId);

        getStopsForRouteUrl = getStopsForRouteUrl.replace(" ", "%20");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                getStopsForRouteUrl, null,
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
                        OBATripsForRouteEntity obaTripsForRouteEntity = new OBAJsonParser().parseAndGetOBATripsForRouteEntity(res, routeId);
                        mainActivity.onOBATripsForRouteLoaded(null, obaTripsForRouteEntity);


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("", "Error: " + error.getMessage());
                mainActivity.onOBATripsForRouteLoaded(null, null);

            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        AppController obj = AppController.getInstance();

        AppController.getInstance().addToRequestQueue(jsonObjReq, TAG);


    }

    public void getOBATripsForLocation(double latitude, double longitude, double latSpan, double lonSpan, boolean includeSchedules, boolean includeStatus, boolean includeTrips, final MainActivity mainActivity) {

        String TAG = "getOBATripsForLocation";

        /*http://api.pugetsound.onebusaway.org/api/where/trips-for-location.json?key=TEST&lat=47.653&lon=-122.307
        &latSpan=0.008&lonSpan=0.008&includeSchedules=true&includeStatus=true&incluseTrips=true*/

        String getOBATripsForLocationUrl = GET_TRIPS_FOR_LOCATION + "&lat=" + latitude + "&lon=" + longitude + "&latSpan=" + latSpan + "&lonSpan=" + lonSpan;

        if (includeSchedules)
            getOBATripsForLocationUrl = getOBATripsForLocationUrl + "&includeSchedules=" + includeSchedules;
        if (includeStatus)
            getOBATripsForLocationUrl = getOBATripsForLocationUrl + "&includeStatus=" + includeStatus;
        if (includeTrips)
            getOBATripsForLocationUrl = getOBATripsForLocationUrl + "&includeTrips=" + includeTrips;

        getOBATripsForLocationUrl = getOBATripsForLocationUrl.replace(" ", "%20");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                getOBATripsForLocationUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();

                        try {
                            res = URLDecoder.decode(res, "UTF-8");
                            OBATripsForLocationEntity obaTripsForLocationEntity = new OBAJsonParser().parseAndGetOBATripsForLocationEntity(res);
                            mainActivity.onOBATripsForLocationLoaded(null, obaTripsForLocationEntity);

                        } catch (UnsupportedEncodingException e) {
                            mainActivity.onOBATripsForLocationLoaded(null, null);
                            e.printStackTrace();
                        }
                        Log.d("", response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mainActivity.onOBATripsForLocationLoaded(null, null);
                VolleyLog.d("", "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        //jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        AppController obj = AppController.getInstance();

        AppController.getInstance().addToRequestQueue(jsonObjReq, TAG);


    }

    public void getObaTripDetailsForVehicle(String vehicleId, final Marker vehicleMarker, final BusMapFragment busMapFragment) {

        String TAG = "getOBATripsForLocation";

        /*http://api.pugetsound.onebusaway.org/api/where/trips-for-location.json?key=TEST&lat=47.653&lon=-122.307
        &latSpan=0.008&lonSpan=0.008&includeSchedules=true&includeStatus=true&incluseTrips=true*/

        String getObaTripDetailsForVehicle = GET_TRIPS_DETAILS_FOR_VEHICLE_URL.replace("vehicle_id", vehicleId);
        getObaTripDetailsForVehicle = getObaTripDetailsForVehicle + "&includeSchedule=" + "true";
        getObaTripDetailsForVehicle = getObaTripDetailsForVehicle + "&includeStatus=" + "true";
        getObaTripDetailsForVehicle = getObaTripDetailsForVehicle + "&includeTrips=" + "true";

        getObaTripDetailsForVehicle = getObaTripDetailsForVehicle.replace(" ", "%20");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                getObaTripDetailsForVehicle, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();

                        try {
                            res = URLDecoder.decode(res, "UTF-8");
                            OBATripDetailsForVehicleEntity obaTripDetailsForVehicleEntity = new OBAJsonParser().parseAndGetOBATripDetailsForVehicleEntity(res);
                            busMapFragment.onOBATripDetailsForVehicleEntity(null, obaTripDetailsForVehicleEntity, vehicleMarker);

                        } catch (UnsupportedEncodingException e) {
                            busMapFragment.onOBATripDetailsForVehicleEntity(null, null, vehicleMarker);
                            e.printStackTrace();
                        }
                        Log.d("", response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                busMapFragment.onOBATripDetailsForVehicleEntity(null, null, vehicleMarker);
                VolleyLog.d("", "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        AppController obj = AppController.getInstance();

        AppController.getInstance().addToRequestQueue(jsonObjReq, TAG);


    }

    public void getObaTripDetailsForTrip(String tripId, final BusMapFragment busMapFragment) {

        String TAG = "getOBATripsForLocation";

        String getObaTripDetailsForTripUrl = GET_TRIPS_DETAILS_FOR_TRIP_URL.replace("trip_id", tripId);

        getObaTripDetailsForTripUrl = getObaTripDetailsForTripUrl.replace(" ", "%20");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                getObaTripDetailsForTripUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String res = response.toString();

                        try {
                            res = URLDecoder.decode(res, "UTF-8");
                            OBATripDetailsForTripEntity obaTripDetailsForTripEntity = new OBAJsonParser().parseAndGetOBATripDetailsForTripEntity(res);
                            busMapFragment.onOBATripDetailsForTripEntity(res, obaTripDetailsForTripEntity);

                        } catch (UnsupportedEncodingException e) {
                            busMapFragment.onOBATripDetailsForTripEntity(null, null);
                            e.printStackTrace();
                        }
                        Log.d("", response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                busMapFragment.onOBATripDetailsForTripEntity(null, null);
                VolleyLog.d("", "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 2));
        AppController obj = AppController.getInstance();

        AppController.getInstance().addToRequestQueue(jsonObjReq, TAG);


    }
}


