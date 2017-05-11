package com.kanthan.teqbuzz.models;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by user on 4/29/2016.
 */
public class Schedule implements Serializable {

    String schedule_id;
    String vehicle_id;
    String vehicle_from;
    String vehicle_to;
    String stop_id;
    String arrival_time;
    String departure_time;
    String vehicle_line_number;
    String latitude;
    String longitude;
    String vehicle_agency;
    String vehicle_type;

    public static final String ALARM_SET = "1";
    public static final String ALARM_NOT_SET = "0";

    public String getAlarm_status() {
        return alarm_status;
    }

    public void setAlarm_status(String alarm_status) {
        this.alarm_status = alarm_status;
    }

    String stop_name;
    String stop_latitude;
    String stop_longitude;
    boolean isAlarmSet;
    String alarm_status;

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);
        Double d = Double.parseDouble(eta) / 60d;
        eta = df.format(d);
        this.eta = eta;
    }

    String eta;

    public boolean isAlarmSet() {
        return isAlarmSet;
    }

    public void setAlarmSet(boolean alarmSet) {
        isAlarmSet = alarmSet;
    }

    public String getRoutes() {
        return routes;
    }

    public void setRoutes(String routes) {
        this.routes = routes;
    }

    String is_alarm_set;
    String routes;

    public int getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(int alarm_id) {
        this.alarm_id = alarm_id;
    }

    int alarm_id;

    public Schedule(String schedule_id, String vehicle_id, String vehicle_from, String vehicle_to, String stop_id, String arrival_time, String departure_time, String vehicle_line_number, String latitude, String longitude, String vehicle_agency, String vehicle_type, String stop_name, String stop_latitude, String stop_longitude, String is_alarm_set) {
        this.schedule_id = schedule_id;
        this.vehicle_id = vehicle_id;
        this.vehicle_from = vehicle_from;
        this.vehicle_to = vehicle_to;
        this.stop_id = stop_id;
        this.arrival_time = arrival_time;
        this.departure_time = departure_time;
        this.vehicle_line_number = vehicle_line_number;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vehicle_agency = vehicle_agency;
        this.vehicle_type = vehicle_type;
        this.stop_name = stop_name;
        this.stop_latitude = stop_latitude;
        this.stop_longitude = stop_longitude;
        this.is_alarm_set = is_alarm_set;
    }

    public String getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getVehicle_from() {
        return vehicle_from;
    }

    public void setVehicle_from(String vehicle_from) {
        this.vehicle_from = vehicle_from;
    }

    public String getVehicle_to() {
        return vehicle_to;
    }

    public void setVehicle_to(String vehicle_to) {
        this.vehicle_to = vehicle_to;
    }

    public String getStop_id() {
        return stop_id;
    }

    public void setStop_id(String stop_id) {
        this.stop_id = stop_id;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
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

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getStop_name() {
        return stop_name;
    }

    public void setStop_name(String stop_name) {
        this.stop_name = stop_name;
    }

    public String getStop_latitude() {
        return stop_latitude;
    }

    public void setStop_latitude(String stop_latitude) {
        this.stop_latitude = stop_latitude;
    }

    public String getStop_longitude() {
        return stop_longitude;
    }

    public void setStop_longitude(String stop_longitude) {
        this.stop_longitude = stop_longitude;
    }

    public String getIs_alarm_set() {
        return is_alarm_set;
    }

    public void setIs_alarm_set(String is_alarm_set) {
        this.is_alarm_set = is_alarm_set;
    }
}
