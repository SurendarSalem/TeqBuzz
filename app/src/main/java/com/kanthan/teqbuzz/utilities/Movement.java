package com.kanthan.teqbuzz.utilities;

import com.kanthan.teqbuzz.models.Vehicle;

/**
 * Created by suren on 8/30/2016.
 */
public class Movement {

    String id, latitude, longitude;
    Vehicle vehicle;

    public Movement() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
