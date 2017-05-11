package com.kanthan.teqbuzz.Entities;

import android.content.Context;

import com.kanthan.teqbuzz.models.Schedule;
import com.kanthan.teqbuzz.models.User;
import com.kanthan.teqbuzz.models.Vehicle;
import com.kanthan.teqbuzz.utilities.AppDatabaseHelper;
import com.kanthan.teqbuzz.utilities.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by suren on 8/22/2016.
 */
public class TeqBuzzParser {
    Preferences appPreferences;
    Context context;

    public TeqBuzzParser() {

    }

    public TeqBuzzParser(Context context) {
        appPreferences = new Preferences(context);
        this.context = context;
    }

    public VehicleListEntity getVehicleListEntity(String response) {

        VehicleListEntity vehicleListEntity = new VehicleListEntity();
        ArrayList<HashMap<String, Vehicle>> vehicleHashMaps = new ArrayList<HashMap<String, Vehicle>>();
        try {
            JSONObject mainJsonObject = new JSONObject(response);
            JSONArray jsonStatusArray = mainJsonObject.getJSONArray("status");
            ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
            String id, device_id, latitude, longitude, prev_stop, next_stop, start_stop, end_stop, location_date, location_time, distance, vehicle_id, vehicle_line_number, vehicle_latitude, vehicle_longitude, vehicle_agency, images, vehicle_type, date_added, date_modified, status;
            boolean isFavourite;
            for (int i = 0; i < jsonStatusArray.length(); i++) {
                Vehicle vehicle = new Vehicle();
                JSONObject vehicleJsonObject = jsonStatusArray.getJSONObject(i);
                id = vehicleJsonObject.getString("vehicle_id");
                vehicle.setVehicle_id(id);
                latitude = vehicleJsonObject.getString("latitude");
                vehicle.setLatitude(latitude);
                longitude = vehicleJsonObject.getString("longitude");
                vehicle.setLongitude(longitude);
                if (vehicleJsonObject.has("previous_stop")) {
                    prev_stop = vehicleJsonObject.getString("previous_stop");
                    vehicle.setPrev_stop(prev_stop);
                }
                if (vehicleJsonObject.has("next_stop")) {
                    next_stop = vehicleJsonObject.getString("next_stop");
                    vehicle.setNext_stop(next_stop);
                }
                if (vehicleJsonObject.has("start_stop")) {
                    start_stop = vehicleJsonObject.getString("start_stop");
                    vehicle.setStart_stop(start_stop);
                }
                if (vehicleJsonObject.has("end_stop")) {
                    end_stop = vehicleJsonObject.getString("end_stop");
                    vehicle.setEnd_stop(end_stop);
                }

                vehicle_line_number = vehicleJsonObject.getString("vehicle_line_number");
                vehicle.setVehicle_line_number(vehicle_line_number);
                if (vehicleJsonObject.has("location_date")) {
                    location_date = vehicleJsonObject.getString("location_date");
                    vehicle.setLocation_date(location_date);
                }
                if (vehicleJsonObject.has("location_time")) {
                    location_time = vehicleJsonObject.getString("location_time");
                    vehicle.setLocation_time(location_time);
                }
                distance = vehicleJsonObject.getString("distance");
                vehicle.setDistance(distance);
                vehicle_agency = vehicleJsonObject.getString("vehicle_agency");
                vehicle.setVehicle_agency(vehicle_agency);

                if (vehicleJsonObject.has("user_favourite")) {
                    isFavourite = vehicleJsonObject.getBoolean("user_favourite");
                    vehicle.setFavourite(isFavourite);
                }
                if (id != null && !id.equalsIgnoreCase("null")) {
                    HashMap<String, Vehicle> vehicleHashMap = new HashMap<String, Vehicle>();
                    vehicleHashMap.put(String.valueOf(i), vehicle);
                    vehicleHashMaps.add(vehicleHashMap);
                    vehicles.add(vehicle);
                }
            }
            vehicleListEntity.setVehicles(vehicles);
            vehicleListEntity.setVehicleHashMaps(vehicleHashMaps);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return vehicleListEntity;
    }

    public VehicleListEntity getVehicleDetailEntity(String response) {

        VehicleListEntity vehicleListEntity = new VehicleListEntity();

        try {
            JSONObject mainJsonObject = new JSONObject(response);
            JSONObject jsonStatusObject = mainJsonObject.getJSONObject("status");
            ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
            String id, device_id, latitude, longitude, start_stop, end_stop, previous_stop, next_stop,
                    location_date, location_time, distance, vehicle_id, vehicle_line_number, vehicle_latitude, vehicle_longitude, vehicle_agency, images, vehicle_type, date_added, date_modified, status;
            boolean isFavourite;
            Vehicle vehicle = new Vehicle();
            JSONObject vehicleJsonObject = jsonStatusObject;
            id = vehicleJsonObject.getString("vehicle_id");
            vehicle.setVehicle_id(id);
            vehicle_line_number = vehicleJsonObject.getString("vehicle_line_number");
            vehicle.setVehicle_line_number(vehicle_line_number);
            latitude = vehicleJsonObject.getString("latitude");
            vehicle.setLatitude(latitude);
            longitude = vehicleJsonObject.getString("longitude");
            vehicle.setLongitude(longitude);
            location_date = vehicleJsonObject.getString("location_date");
            vehicle.setLocation_date(location_date);
            location_time = vehicleJsonObject.getString("location_time");
            vehicle.setLocation_time(location_time);
            distance = vehicleJsonObject.getString("distance");
            vehicle.setDistance(distance);
            previous_stop = vehicleJsonObject.getString("previous_stop");
            vehicle.setPrev_stop(previous_stop);
            next_stop = vehicleJsonObject.getString("next_stop");
            vehicle.setNext_stop(next_stop);
            start_stop = vehicleJsonObject.getString("start_stop");
            vehicle.setStart_stop(start_stop);
            end_stop = vehicleJsonObject.getString("end_stop");
            vehicle.setEnd_stop(end_stop);
            if (vehicleJsonObject.has("user_favourite")) {
                isFavourite = vehicleJsonObject.getBoolean("user_favourite");
                vehicle.setFavourite(isFavourite);
            }
            vehicle_agency = vehicleJsonObject.getString("vehicle_agency");
            vehicle.setVehicle_agency(vehicle_agency);

            if (id != null && !id.equalsIgnoreCase("null"))
                vehicles.add(vehicle);

            vehicleListEntity.setVehicles(vehicles);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return vehicleListEntity;
    }

    public ArrayList<Schedule> parseAndGetTeqbuzzSchedules(String jsonResponse) {
        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(context);
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        String userId;
        if (appPreferences.isLoginned()) {
            userId = appPreferences.getUser().getUser_id();
        } else {
            userId = User.DEFAULT_USER_ID;
        }
        String schedule_id, vehicle_id, vehicle_from, vehicle_to, stop_id,
                arrival_time, departure_time, vehicle_line_number, latitude,
                longitude, vehicle_agency, vehicle_type, stop_name, stop_latitude,
                stop_longitude, is_alarm_set, routes, eta, alarm_status;
        try {

            //jsonResponse = "{\"status\":[{\"vehicle_id\":\"1\",\"vehicle_line_number\":\"1003\",\"latitude\":\"12.931575\",\"longitude\":\"77.609581\",\"vehicle_agency\":\"parveen\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-21 11:12:04\",\"date_modified\":\"2016-04-11 01:38:20\",\"status\":\"0\",\"distance\":\"32.771042327684356\"},{\"vehicle_id\":\"2\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"},{\"vehicle_id\":\"3\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"},{\"vehicle_id\":\"4\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"},{\"vehicle_id\":\"5\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"},{\"vehicle_id\":\"6\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"}]}";

            jsonResponse.replace("\\\"", "\"");
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray status_array = jsonObject.getJSONArray("status");
            for (int i = 0; i < status_array.length(); i++) {
                schedule_id = status_array.getJSONObject(i).getString("schedule_id");
                vehicle_id = status_array.getJSONObject(i).getString("vehicle_id");
                vehicle_from = status_array.getJSONObject(i).getString("vehicle_from");
                vehicle_to = status_array.getJSONObject(i).getString("vehicle_to");
                stop_id = status_array.getJSONObject(i).getString("stop_id");
                arrival_time = status_array.getJSONObject(i).getString("arrival_time");
                departure_time = status_array.getJSONObject(i).getString("departure_time");
                vehicle_line_number = status_array.getJSONObject(i).getString("vehicle_line_number");
                latitude = status_array.getJSONObject(i).getString("stop_latitude");
                longitude = status_array.getJSONObject(i).getString("stop_longitude");
                vehicle_agency = status_array.getJSONObject(i).getString("vehicle_agency");
                vehicle_type = status_array.getJSONObject(i).getString("vehicle_type");
                stop_name = status_array.getJSONObject(i).getString("stop_name");
                stop_latitude = status_array.getJSONObject(i).getString("stop_latitude");
                stop_longitude = status_array.getJSONObject(i).getString("stop_longitude");
                routes = status_array.getJSONObject(i).getString("routes");
                eta = status_array.getJSONObject(i).getString("eta");
                //alarm_status = status_array.getJSONObject(i).getString("alarm_status");
                alarm_status = "1";
                Schedule schedule = new Schedule(schedule_id, vehicle_id, vehicle_from, vehicle_to, stop_id, arrival_time, departure_time, vehicle_line_number, latitude, longitude, vehicle_agency, vehicle_type, stop_name, stop_latitude, stop_longitude, "0");
                if (alarm_status.equalsIgnoreCase(Schedule.ALARM_SET)) {
                    schedule.setAlarmSet(true);
                }
                schedule.setRoutes(routes);
                schedule.setEta(eta);
                schedules.add(schedule);
            }


        } catch (JSONException e) {
            /*if (!isErrorOccured)
                Utility.showToast(mActivity, mActivity.getResources().getString(R.string.some_error_occured));
            */
            e.printStackTrace();

        } catch (NullPointerException e) {
           /* if (!isErrorOccured)
                Utility.showToast(mActivity, mActivity.getResources().getString(R.string.some_error_occured));
           */
            e.printStackTrace();
        }
        return schedules;
    }


    public AlarmResponseEntity getAlarmResponseEntity(String jsonResponse) {

        String vehicleId, stopId, scheduleId, isAlarmSet;
        AlarmResponseEntity alarmResponseEntity = null;
        try {

            //jsonResponse = "{\"status\":[{\"vehicle_id\":\"1\",\"vehicle_line_number\":\"1003\",\"latitude\":\"12.931575\",\"longitude\":\"77.609581\",\"vehicle_agency\":\"parveen\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-21 11:12:04\",\"date_modified\":\"2016-04-11 01:38:20\",\"status\":\"0\",\"distance\":\"32.771042327684356\"},{\"vehicle_id\":\"2\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"},{\"vehicle_id\":\"3\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"},{\"vehicle_id\":\"4\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"},{\"vehicle_id\":\"5\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"},{\"vehicle_id\":\"6\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"}]}";
            jsonResponse.replace("\\\"", "\"");
            JSONObject jsonObject = new JSONObject(jsonResponse).getJSONObject("status");

            vehicleId = jsonObject.getString("vehicle_id");
            stopId = jsonObject.getString("stop_id");
            scheduleId = jsonObject.getString("schedule_id");
            isAlarmSet = jsonObject.getString("alarm_status");

            alarmResponseEntity = new AlarmResponseEntity(vehicleId, stopId, scheduleId, isAlarmSet);

        } catch (JSONException e) {

            e.printStackTrace();

        } catch (NullPointerException e) {

            e.printStackTrace();
        }

        return alarmResponseEntity;
    }
}
