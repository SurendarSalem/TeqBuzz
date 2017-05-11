package com.kanthan.teqbuzz.models;

import java.io.Serializable;

/**
 * Created by user on 3/12/2016.
 */
public class Stop implements Serializable {


    String stop_name, stop_latitude, stop_longitude;
    boolean isAlarmSet;

    public boolean isAlarmSet() {
        return isAlarmSet;
    }

    public void setAlarmSet(boolean alarmSet) {
        isAlarmSet = alarmSet;
    }

    public Stop(String stop_name, String stop_latitude, String stop_longitude) {
        this.stop_name = stop_name;
        this.stop_latitude = stop_latitude;
        this.stop_longitude = stop_longitude;
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
}
