package com.kanthan.teqbuzz.Entities;

import com.kanthan.teqbuzz.models.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by suren on 8/22/2016.
 */
public class VehicleListEntity {

    public ArrayList<Vehicle> vehicles;

    public ArrayList<HashMap<String, Vehicle>> getVehicleHashMaps() {
        return vehicleHashMaps;
    }

    public void setVehicleHashMaps(ArrayList<HashMap<String, Vehicle>> vehicleHashMaps) {
        this.vehicleHashMaps = vehicleHashMaps;
    }

    public ArrayList<HashMap<String, Vehicle>> vehicleHashMaps;

    public VehicleListEntity() {
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
