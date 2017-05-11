package com.kanthan.teqbuzz.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.kanthan.teqbuzz.models.User;

import java.util.Map;

/**
 * Created by user on 9/10/2015.
 */
public class Preferences {

    Context context;
    SharedPreferences preferences;
    public static String USER_ID = "user_id", AVATAR = "avatar", NAME = "name", EMAIL = "email",
            PASSWORD = "password", PHONE_NUMBER = "phone_number", LATITUDE = "latitude",
            LONGITUDE = "longitude", TEMP_LATITUDE = "temp_latitude",
            TEMP_LONGITUDE = "temp_longitude", GPS_LATITUDE = "gps_latitude",
            GPS_LONGITUDE = "gps_longitude", WORK_LATITUDE = "work_latitude",
            WORK_LONGITUDE = "work_longitude", ADDRESS = "address", DiSTANCE_FLAG = "distance_flag",
            GCM_TOKEN = "gcm_token", MODE = "mode", DOMAIN = "domain", IS_ROUTE_CALCULATED = "isRouteCalculated",
            LOCAL_COUNTER = "local_counter", IS_MAP_CAMERA_FOLLOW_GPS = "is_map_camera_follow_gps", DISTANCE_RANGE = "distance_range";

    boolean isMapCameraFollowGps;
    private int distanceRange;
    private int DISTANCE_LIMIT = 2000;

    public Preferences(Context context) {

        this.context = context;
        preferences = context.getSharedPreferences("teqbuzz_prefs", 0);
        preferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                Log.d("Preference changed", s);
            }
        });

    }

    boolean isRegistered, isLoginned;

    public boolean isRegistered() {

        isRegistered = preferences.getBoolean("isRegistered", false);

        return isRegistered;
    }

    public void setRegistered(boolean isRegistered) {
        preferences.edit().putBoolean("isRegistered", isRegistered).commit();
        this.isRegistered = isRegistered;
    }


    public void setDistanceRange(int distanceRange) {
        preferences.edit().putInt(DISTANCE_RANGE, distanceRange).commit();
        this.distanceRange = distanceRange;
    }

    public int getDistanceRange() {
        distanceRange = preferences.getInt(DISTANCE_RANGE, DISTANCE_LIMIT);
        return distanceRange;
    }


    public boolean isLoginned() {
        isLoginned = preferences.getBoolean("isLoginned", false);
        return isLoginned;
    }

    public void setLoginned(boolean isLoginned) {
        preferences.edit().putBoolean("isLoginned", isLoginned).commit();
        this.isLoginned = isLoginned;
    }

    public void updateUser(User user) {

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(USER_ID, user.getUser_id());
        editor.putString(NAME, user.getName());
        editor.putString(EMAIL, user.getEmail());
        editor.putString(AVATAR, user.getImage_url());
        editor.putString(PASSWORD, user.getPassword());
        editor.putInt(MODE, user.getMode());
        editor.putString(PHONE_NUMBER, user.getPhone_number());
        editor.putString(LATITUDE, user.getHomeLatitude());
        editor.putString(LONGITUDE, user.getHomeLongitude());
        editor.putString(TEMP_LATITUDE, user.getTemp_latitude());
        editor.putString(TEMP_LONGITUDE, user.getTemp_longitude());
        editor.putString(GPS_LATITUDE, user.getGps_latitude());
        editor.putString(GPS_LONGITUDE, user.getGps_longitude());
        editor.putString(WORK_LATITUDE, user.getWork_latitude());
        editor.putString(WORK_LONGITUDE, user.getWork_longitude());
        editor.putString(ADDRESS, user.getAddress());
        Log.d("today updateUser()", user.getGps_latitude() + "::" + user.getGps_longitude());
        editor.commit();

        Map<String, ?> allEntries = preferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }

    }

    public User getUser() {

        User user = new User();
        user.setGps_latitude(preferences.getString(GPS_LATITUDE, "0.0"));
        user.setGps_longitude(preferences.getString(GPS_LONGITUDE, "0.0"));
        user.setUser_id(preferences.getString(USER_ID, User.DEFAULT_USER_ID));
        user.setName(preferences.getString(NAME, ""));
        user.setEmail(preferences.getString(EMAIL, ""));
        user.setPassword(preferences.getString(PASSWORD, ""));
        user.setPhone_number(preferences.getString(PHONE_NUMBER, ""));
        user.setHomeLatitude(preferences.getString(LATITUDE, "0.0"));
        user.setHomeLongitude(preferences.getString(LONGITUDE, "0.0"));
        user.setGps_latitude(preferences.getString(GPS_LATITUDE, "0.0"));
        user.setGps_longitude(preferences.getString(GPS_LONGITUDE, "0.0"));
        user.setWork_latitude(preferences.getString(WORK_LATITUDE, "0.0"));
        user.setWork_longitude(preferences.getString(WORK_LONGITUDE, "0.0"));
        user.setTemp_latitude(preferences.getString(TEMP_LATITUDE, "0.0"));
        user.setTemp_longitude(preferences.getString(TEMP_LONGITUDE, "0.0"));
        user.setAddress(preferences.getString(ADDRESS, ""));
        user.setImage_url(preferences.getString(AVATAR, ""));
        user.setMode(preferences.getInt(MODE, 0));
        String gpsLatitude = preferences.getString(GPS_LATITUDE, "0.0");
        String gpsLongitude = preferences.getString(GPS_LONGITUDE, "0.0");

        Log.d("gpsLatitude gpsLongitude", gpsLatitude + "::" + gpsLongitude);
        Log.d("today getUser()", user.getGps_latitude() + "::" + user.getGps_longitude());
        return user;
    }

    public String getGCM_TOKEN() {
        String TOKEN = preferences.getString(GCM_TOKEN, "");

        return TOKEN;
    }

    public void setGCM_TOKEN(String TOKEN) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(GCM_TOKEN, TOKEN);
        editor.commit();

        this.GCM_TOKEN = GCM_TOKEN;
    }

    public void setDomain(String description) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DOMAIN, description);
        editor.commit();

    }

    public void setBUSTrackerRouteCalculated(boolean isCalculated) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_ROUTE_CALCULATED, isCalculated);
        editor.commit();

    }


    public boolean isCTABUSTrackerRouteCalculated() {
        boolean isBUSTrackerRouteCalculated = preferences.getBoolean(IS_ROUTE_CALCULATED, false);
        return isBUSTrackerRouteCalculated;

    }

    public String getDomain() {
        String domain = preferences.getString(DOMAIN, "domain");
        return domain;
    }

    public void setLocalCounter(long counter) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(LOCAL_COUNTER, counter);
        editor.commit();

    }

    public double getLocalCounter() {
        long counter = preferences.getLong(LOCAL_COUNTER, 0);
        return counter;
    }

    public void setIsMapCameraFollowGps(boolean isMapCameraFollowGps) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_MAP_CAMERA_FOLLOW_GPS, isMapCameraFollowGps);
        editor.commit();

    }

    public double isMapCameraFollowGps() {
        boolean isMapCameraFollowGps = preferences.getBoolean(IS_MAP_CAMERA_FOLLOW_GPS, false);
        return isMapCameraFollowGps();
    }

    public void setDistanceFlag(boolean flag) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(DiSTANCE_FLAG, flag);
        editor.commit();
    }

    public boolean getDistanceFlag() {
        boolean flag = preferences.getBoolean(DiSTANCE_FLAG, false);
        return flag;
    }
}
