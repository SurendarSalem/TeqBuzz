package com.kanthan.teqbuzz.Entities;

/**
 * Created by suren on 2/27/2017.
 */

public class AlarmResponseEntity {
    String vehicleId, stopId, scheduleId, isAlarmSet;

    public AlarmResponseEntity(String vehicleId, String stopId, String scheduleId, String isAlarmSet) {
        this.vehicleId = vehicleId;
        this.stopId = stopId;
        this.scheduleId = scheduleId;
        this.isAlarmSet = isAlarmSet;
    }

    public String getVehicleId() {

        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getIsAlarmSet() {
        return isAlarmSet;
    }

    public void setIsAlarmSet(String isAlarmSet) {
        this.isAlarmSet = isAlarmSet;
    }
}
