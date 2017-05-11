package com.kanthan.teqbuzz.onebusaway;

import java.util.ArrayList;

/**
 * Created by suren on 8/19/2016.
 */
public class OBATripDetailsForVehicleEntity {
    String code, currentTime;
    Data data;

    public OBATripDetailsForVehicleEntity() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        Entry entry;

        public Data() {
        }

        public Entry getEntry() {
            return entry;
        }

        public void setEntry(Entry entry) {
            this.entry = entry;
        }

        public class Entry {

            Schedule schedule;
            Status status;

            public Status getStatus() {
                return status;
            }

            public void setStatus(Status status) {
                this.status = status;
            }

            public Entry() {
            }

            public Schedule getSchedule() {
                return schedule;
            }

            public void setSchedule(Schedule schedule) {
                this.schedule = schedule;
            }

            public class Schedule {

                public Schedule() {
                }

                String nextTripId, previousTripId;
                ArrayList<StopTime> stopTimes;

                public String getNextTripId() {
                    return nextTripId;
                }

                public void setNextTripId(String nextTripId) {
                    this.nextTripId = nextTripId;
                }

                public String getPreviousTripId() {
                    return previousTripId;
                }

                public void setPreviousTripId(String previousTripId) {
                    this.previousTripId = previousTripId;
                }

                public ArrayList<StopTime> getStopTimes() {
                    return stopTimes;
                }

                public void setStopTimes(ArrayList<StopTime> stopTimes) {
                    this.stopTimes = stopTimes;
                }

                public class StopTime {
                    String arrivalTime, departureTime, distanceAlongTrip, stopId;

                    public String getArrivalTime() {
                        return arrivalTime;
                    }

                    public void setArrivalTime(String arrivalTime) {
                        this.arrivalTime = arrivalTime;
                    }

                    public String getDepartureTime() {
                        return departureTime;
                    }

                    public void setDepartureTime(String departureTime) {
                        this.departureTime = departureTime;
                    }

                    public String getDistanceAlongTrip() {
                        return distanceAlongTrip;
                    }

                    public void setDistanceAlongTrip(String distanceAlongTrip) {
                        this.distanceAlongTrip = distanceAlongTrip;
                    }

                    public String getStopId() {
                        return stopId;
                    }

                    public void setStopId(String stopId) {
                        this.stopId = stopId;
                    }
                }
            }

            public class Status {

                String activeTripId, closestStop, nextStop, status, vehicleId;
                Position position;

                public Status() {
                }

                public String getActiveTripId() {
                    return activeTripId;
                }

                public void setActiveTripId(String activeTripId) {
                    this.activeTripId = activeTripId;
                }

                public String getClosestStop() {
                    return closestStop;
                }

                public void setClosestStop(String closestStop) {
                    this.closestStop = closestStop;
                }

                public String getNextStop() {
                    return nextStop;
                }

                public void setNextStop(String nextStop) {
                    this.nextStop = nextStop;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getVehicleId() {
                    return vehicleId;
                }

                public void setVehicleId(String vehicleId) {
                    this.vehicleId = vehicleId;
                }

                public Position getPosition() {
                    return position;
                }

                public void setPosition(Position position) {
                    this.position = position;
                }


                public class Position {
                    String lat, lon;

                    public String getLat() {
                        return lat;
                    }

                    public void setLat(String lat) {
                        this.lat = lat;
                    }

                    public String getLon() {
                        return lon;
                    }

                    public void setLon(String lon) {
                        this.lon = lon;
                    }
                }
            }
        }

    }
}
