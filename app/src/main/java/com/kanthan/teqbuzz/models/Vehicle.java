package com.kanthan.teqbuzz.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by user on 3/3/2016.
 */
public class Vehicle {

    public static final String IS_ALARM_SET = "1";
    public static final String IS_ALARM_UNSET = "1";
    public static final String IS_FAV = "1";
    public static final String IS_NOT_FAV = "0";
    String vehicle_id, vehicle_line_number, distance, location_date, location_time, latitude, longitude, start_stop, end_stop, prev_stop, next_stop, vehicle_agency, images, vehicle_type, date_added, date_modified, status;
    ArrayList<Stop> stops;
    boolean isEdited, isMoving, isFavourite;

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    ArrayList<LatLng> routePaths;

    public boolean isMoving() {
        return isMoving;
    }

    public String getStart_stop() {
        return start_stop;
    }

    public void setStart_stop(String start_stop) {
        this.start_stop = start_stop;
    }

    public String getEnd_stop() {
        return end_stop;
    }

    public void setEnd_stop(String end_stop) {
        this.end_stop = end_stop;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public ArrayList<LatLng> getRoutePaths() {
        return routePaths;
    }

    public void setRoutePaths(ArrayList<LatLng> routePaths) {
        this.routePaths = routePaths;
    }

    public void setSchedules(ArrayList<Schedule> schedules) {
        this.schedules = schedules;
    }

    ArrayList<Schedule> schedules;

    public String getLocation_time() {
        return location_time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setLocation_time(String location_time) {
        this.location_time = location_time;
    }

    public String getLocation_date() {
        return location_date;
    }

    public void setLocation_date(String location_date) {
        this.location_date = location_date;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected, isNativeAd;

    public boolean isNativeAd() {
        return isNativeAd;
    }

    public void setNativeAd(boolean nativeAd) {
        isNativeAd = nativeAd;
    }

    public Vehicle() {

    }

    public Vehicle(String vehicle_id, String vehicle_line_number, String latitude, String longitude, String prev_stop, String next_stop, String vehicle_agency, String images, String vehicle_type, String date_added, String date_modified, String status, ArrayList<Stop> stops) {
        this.vehicle_id = vehicle_id;
        this.vehicle_line_number = vehicle_line_number;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vehicle_agency = vehicle_agency;
        this.prev_stop = prev_stop;
        this.next_stop = next_stop;
        this.images = images;
        this.vehicle_type = vehicle_type;
        this.date_added = date_added;
        this.date_modified = date_modified;
        this.status = status;
        this.stops = stops;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getPrev_stop() {
        return prev_stop;
    }

    public void setPrev_stop(String prev_stop) {
        this.prev_stop = prev_stop;
    }

    public String getNext_stop() {
        return next_stop;
    }

    public void setNext_stop(String next_stop) {
        this.next_stop = next_stop;
    }

    public String getVehicle_line_number() {
        return vehicle_line_number;
    }

    public void setVehicle_line_number(String vehicle_line_number) {
        this.vehicle_line_number = vehicle_line_number;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getVehicle_agency() {
        return vehicle_agency;
    }

    public void setVehicle_agency(String vehicle_agency) {
        this.vehicle_agency = vehicle_agency;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Stop> getStops() {
        return stops;
    }

    public void setStops(ArrayList<Stop> stops) {
        this.stops = stops;
    }
}
