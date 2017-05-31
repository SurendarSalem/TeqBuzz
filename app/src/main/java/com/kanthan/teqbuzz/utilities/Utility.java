package com.kanthan.teqbuzz.utilities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.kanthan.teqbuzz.AppController;
import com.kanthan.teqbuzz.MainActivity;
import com.kanthan.teqbuzz.R;
import com.kanthan.teqbuzz.fragments.BusMapFragment;
import com.kanthan.teqbuzz.models.Schedule;
import com.kanthan.teqbuzz.models.Vehicle;
import com.rey.material.widget.SnackBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.DoubleBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by user on 3/1/2016.
 */
public class Utility {

    Context context;
    private Dialog progressDialog;
    private static ProgressDialog locationProgressDialog;

    public Utility(Context ctx) {

        context = ctx;
    }

    public static boolean isGpsEnabled(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return statusOfGPS;
    }

    public static void setAlarm(Context context, Schedule schedule, int alarm_id) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("stop_name", schedule.getStop_name() + "_teqbuzz_" + schedule.getStop_id());
        intent.putExtra("stop_id", schedule.getStop_id().toString());
        String arrival_time = schedule.getArrival_time();
        intent.putExtra("arrival_time", schedule.getArrival_time());
        String time_array[] = arrival_time.substring(0, arrival_time.length() - 3).split(":");

        /*int hour = Integer.parseInt(time_array[0]);
        int minutes = Integer.parseInt(time_array[1]);
*/
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);

        int minutes = calendar.get(Calendar.MINUTE);

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 00);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm_id, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    public static void cancelAlarm(Context context, int alarm_id) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm_id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static void createCacheFolderIfEmpty() {

        File cacheFolder = new File(Constants.LOCAL_STORAGE_IMAGE_PATH);
        if (!cacheFolder.exists()) {
            cacheFolder.mkdir();
        }
    }

    public static void showLocationLoadingProgressBar(Activity mActivity) {

        locationProgressDialog = ProgressDialog.show(mActivity, mActivity.getString(R.string.please_wait), mActivity.getString(R.string.fetching_location), true);
        locationProgressDialog.setCancelable(true);

    }

    public static void stopLocationLoadingProgressBar() {

        if (locationProgressDialog != null)
            locationProgressDialog.dismiss();

    }

    public static String getFacebokKeyHash(Context context) {

        String keyHash = null;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.i(" Facebook KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", "Exception(NameNotFoundException) : " + e);

        } catch (NoSuchAlgorithmException e) {
            Log.e("", "Exception(NoSuchAlgorithmException) : " + e);
        }
        return keyHash;

    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash = ", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    public Dialog createProgressBar() {

        progressDialog = new Dialog(context);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.layout_progress);
        LinearLayout progress_bar = (LinearLayout) progressDialog.findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.VISIBLE);
        //progressDialog.create();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        //  progressDialog.show();

        return progressDialog;

    }

    public static ArrayList<Vehicle> createDummyVehiclesList() {

        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
       /* vehicles.add(new Vehicle("", "vehicle_line_number", "13.0531037", "80.1295945", "next stop", "prev stop", "Government",
                "images", "vehicle_type", "date_added", "date_modified",
                "status", null));*/
        return vehicles;
    }

    public static void runGcmService(final Context applicationContext) {


      /*  Intent gcmServiceIntent = new Intent(applicationContext, GcmRegisterService.class);
        applicationContext.startService(gcmServiceIntent);
*/
        new Thread(new Runnable() {
            @Override
            public void run() {

                Preferences myPreferences = new Preferences(applicationContext.getApplicationContext());

                {

                    GCMRegistrar.checkManifest(applicationContext);

                    final String regId = GCMRegistrar.getRegistrationId(applicationContext);

                    if (regId.equals("")) {

                        GCMRegistrar.register(applicationContext, CommonUtilities.SENDER_ID);
                    } else {
                        if (GCMRegistrar.isRegisteredOnServer(applicationContext)) {

                            myPreferences.setGCM_TOKEN(regId);

                        } else {
                            ServerUtilities.register(applicationContext, regId);
               /* Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);*/
                        }

                    }
                }


            }
        }).start();


    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static boolean isConnect() {
        ConnectivityManager connectivityManager = (ConnectivityManager) AppController.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] netArray = connectivityManager.getAllNetworks();
            NetworkInfo netInfo;
            for (Network net : netArray) {
                netInfo = connectivityManager.getNetworkInfo(net);
                if ((netInfo.getTypeName().equalsIgnoreCase("WIFI") || netInfo.getTypeName().equalsIgnoreCase("MOBILE")) && netInfo.isConnected() && netInfo.isAvailable()) {
                    //if (netInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    Log.d("Network", "NETWORKNAME: " + netInfo.getTypeName());
                    return true;
                }
            }
        } else {
            if (connectivityManager != null) {
                @SuppressWarnings("deprecation")
                NetworkInfo[] netInfoArray = connectivityManager.getAllNetworkInfo();
                if (netInfoArray != null) {
                    for (NetworkInfo netInfo : netInfoArray) {
                        if ((netInfo.getTypeName().equalsIgnoreCase("WIFI") || netInfo.getTypeName().equalsIgnoreCase("MOBILE")) && netInfo.isConnected() && netInfo.isAvailable()) {
                            //if (netInfo.getState() == NetworkInfo.State.CONNECTED) {
                            Log.d("Network", "NETWORKNAME: " + netInfo.getTypeName());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static ArrayList<LatLng> createDummyStopsList() {
        // file exists, it is the first boot

        String stop_names[] = {};
        String stop_lat[] = {"12.9318107", "12.7409126", "12.5186113", "11.7436657", "11.664325"};
        String stop_lon[] = {"77.6098599", "77.8252923", "78.2137366", "78.0475878", "78.1460142"};


        // linea init
        LatLng polyline = null;

        // array list of lines init
        ArrayList<LatLng> polylines = new ArrayList<LatLng>();

        // get json array


        for (int i = 0; i < stop_lat.length; i++) {
            polyline = new LatLng(Double.valueOf(stop_lat[i]),
                    Double.valueOf(stop_lon[i]));
            polylines.add(polyline);

        }


        return polylines;
    }


    public static void showToast(Activity mActivity, String string) {
        Toast.makeText(mActivity, string, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context mContext, String string) {
        Toast.makeText(mContext, string, Toast.LENGTH_SHORT).show();
    }

    public static boolean isLocationValid(String str_latitude, String str_longitude) {

        boolean isLocationValid;

        isLocationValid = str_latitude != null && str_latitude.length() > 0 && str_longitude != null && str_longitude.length() > 0 && (!str_latitude.equalsIgnoreCase("0.0")) && (!str_longitude.equalsIgnoreCase("0.0"));

        return isLocationValid;
    }

    public static void showSnack(Activity mActivity, String string) {
        SnackBar snackBar = SnackBar.make(mActivity).text(string).textColor(Color.WHITE).actionText("OK").
                actionTextColor(Color.RED).actionClickListener(new SnackBar.OnActionClickListener() {
            @Override
            public void onActionClick(SnackBar snackBar, int i) {
                snackBar.dismiss();
            }
        });

        snackBar.applyStyle(R.style.st);
        snackBar.singleLine(true);
        snackBar.show(mActivity);
    }

    public static SnackBar showSnackForFavBuses(Activity mActivity, final BusMapFragment busMapFragment) {
        String string = mActivity.getResources().getString(R.string.no_fav_vehicles);
        SnackBar snackBar = SnackBar.make(mActivity).text(string).textColor(Color.WHITE).actionText("OK").
                actionTextColor(Color.RED).actionClickListener(new SnackBar.OnActionClickListener() {
            @Override
            public void onActionClick(SnackBar snackBar, int i) {
                busMapFragment.onFavSnackClicked();
                snackBar.dismiss();
            }
        });

        snackBar.applyStyle(R.style.st);
        snackBar.singleLine(true);
        return snackBar;
    }

    public static SnackBar showSnackForInternet(Activity mActivity, String string, final BusMapFragment busMapFragment) {
        SnackBar snackBar = SnackBar.make(mActivity).text(string).textColor(Color.WHITE).actionText("Connect").
                actionTextColor(Color.RED).actionClickListener(new SnackBar.OnActionClickListener() {
            @Override
            public void onActionClick(SnackBar snackBar, int i) {
                busMapFragment.openInternetSettings();
                snackBar.dismiss();
            }
        });

        snackBar.applyStyle(R.style.st);
        snackBar.singleLine(true);
        //snackBar.show(mActivity);
        return snackBar;
    }

    public static SnackBar showSnackForInternet(MainActivity mActivity, String string, final MainActivity mainActivity) {
        SnackBar snackBar = SnackBar.make(mActivity).text(string).textColor(Color.WHITE).actionText("Connect").
                actionTextColor(Color.RED).actionClickListener(new SnackBar.OnActionClickListener() {
            @Override
            public void onActionClick(SnackBar snackBar, int i) {
                mainActivity.openInternetSettings();
                snackBar.dismiss();
            }
        });

        snackBar.applyStyle(R.style.st);
        snackBar.singleLine(true);
        //snackBar.show(mActivity);
        return snackBar;
    }

    public static String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    public static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Error downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public static Uri getDrawableURI(Context context, int drawableId) {
        //like as: android.resource://cn.bihu/2130837700
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId));
        return imageUri;
    }

    public static boolean isLocationValid(Location location) {

        if (location == null) {
            return false;
        }
        if (location.getLatitude() < -90 || location.getLatitude() > 90) {
            return false;
        } else if (location.getLongitude() < -180 || location.getLongitude() > 180) {
            return false;
        }
        return true;
    }

    public static boolean isLocationValid(SingleShotLocationProvider.GPSCoordinates location) {

        return location != null && location.latitude > 0 && location.longitude > 0;

    }

    public static List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public static List<LatLng> decodePoly(String encoded, int numPoints) {
        assert (numPoints >= 0);
        ArrayList<LatLng> array = new ArrayList<LatLng>(numPoints);

        final int len = encoded.length();
        int i = 0;
        int lat = 0, lon = 0;

        while (i < len) {
            int shift = 0;
            int result = 0;

            int a, b;
            do {
                a = encoded.charAt(i);
                b = a - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
                ++i;
            } while (b >= 0x20);

            final int dlat = ((result & 1) == 1 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                a = encoded.charAt(i);
                b = a - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
                ++i;
            } while (b >= 0x20);

            final int dlon = ((result & 1) == 1 ? ~(result >> 1) : (result >> 1));
            lon += dlon;

            // The polyline encodes in degrees * 1E5, we need decimal degrees
            array.add(makeLocation(lat / 1E5, lon / 1E5));
        }

        return array;
    }

    public static final LatLng makeLocation(double lat, double lon) {
        LatLng l = new LatLng(lat, lon);

        return l;
    }

    public static String getDate(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        Date d = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm a");
        return sdf.format(d);
    }


    public static String createPendingMovementId() {
        String id = String.valueOf(System.currentTimeMillis());
        return id;
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static String getConvertedStringDate(long unixSeconds) {
        Date date = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);
        return formattedDate;
    }


    public static SnackBar getVehicleNotFoundSnackBar(Activity mActivity, final BusMapFragment busMapFragment) {
        SnackBar snackBar = SnackBar.make(mActivity).text(mActivity.getResources().getString(R.string.no_vehicle_found)).textColor(Color.WHITE).actionText(mActivity.getResources().getString(R.string.ok)).
                actionTextColor(Color.RED).actionClickListener(new SnackBar.OnActionClickListener() {
            @Override
            public void onActionClick(SnackBar snackBar, int i) {
                snackBar.dismiss();
                busMapFragment.onVehicleNotFoundSnackBarClosed();
            }
        });

        snackBar.applyStyle(R.style.st);
        snackBar.singleLine(true);
        return snackBar;
    }

    public static String convertArrayListToString(ArrayList<String> sharedVehicleIds) {
        String result = "";
        for (String str : sharedVehicleIds) {
            result = result + str + ",";
        }
        if (result.length() > 0)
            result = result.substring(0, result.length() - 1);
        return result;
    }

    public static void showAlert(Activity mActivity, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
        alert.setMessage(message);
        alert.setPositiveButton(mActivity.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }

    public static BitmapDescriptor getCustomMarker(Activity mActivity, Schedule schedule) {
        LinearLayout etaMarkerLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.distance_marker_layout, null);

        etaMarkerLayout.setDrawingCacheEnabled(true);
        etaMarkerLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        etaMarkerLayout.layout(0, 0, etaMarkerLayout.getMeasuredWidth(), etaMarkerLayout.getMeasuredHeight());
        etaMarkerLayout.buildDrawingCache(true);

        TextView eta = (TextView) etaMarkerLayout.findViewById(R.id.eta);

        eta.setText(schedule.getEta() + " mins");

        Bitmap flagBitmap = Bitmap.createBitmap(etaMarkerLayout.getDrawingCache());
        etaMarkerLayout.setDrawingCacheEnabled(false);
        BitmapDescriptor flagBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(flagBitmap);
        return flagBitmapDescriptor;


    }

    public static ArrayList<Vehicle> setDummyMovements(ArrayList<Vehicle> receivedVehicles, ArrayList<Vehicle> teqBuzzVehicles) {
        for (int i = 0; i < receivedVehicles.size(); i++) {
            receivedVehicles.get(i).setLatitude(String.valueOf(Double.parseDouble(teqBuzzVehicles.get(i).getLatitude()) + 0.0001));
            receivedVehicles.get(i).setLongitude(String.valueOf(Double.parseDouble(teqBuzzVehicles.get(i).getLongitude()) + 0.0001));
        }
        return receivedVehicles;
    }

    public static String getDirectionsUrl(LatLng origin, LatLng dest, ArrayList<Schedule> schedules) {

/*
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
*/


// Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";
        for (int i = 1; i < schedules.size(); i++) {
            Double lat = Double.parseDouble(schedules.get(i).getStop_latitude());
            Double lon = Double.parseDouble(schedules.get(i).getStop_longitude());

            LatLng point = new LatLng(lat, lon);
            if (i == 1)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }
}



