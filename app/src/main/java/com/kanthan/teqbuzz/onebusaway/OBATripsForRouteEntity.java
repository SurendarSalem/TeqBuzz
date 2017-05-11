package com.kanthan.teqbuzz.onebusaway;

import java.util.ArrayList;

/**
 * Created by suren on 8/11/2016.
 */
public class OBATripsForRouteEntity {
    String code, currentTime;
    Data data;

    public OBATripsForRouteEntity() {
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
        String limitExceeded;
        ArrayList<Trip> trips;

        public Data() {
        }

        public String getLimitExceeded() {
            return limitExceeded;
        }

        public void setLimitExceeded(String limitExceeded) {
            this.limitExceeded = limitExceeded;
        }

        public ArrayList<Trip> getTrips() {
            return trips;
        }

        public void setTrips(ArrayList<Trip> trips) {
            this.trips = trips;
        }

        public class Trip {
            String tripId;
            Status status;

            public Trip() {
            }

            public String getTripId() {
                return tripId;
            }

            public void setTripId(String tripId) {
                this.tripId = tripId;
            }

            public Status getStatus() {
                return status;
            }

            public void setStatus(Status status) {
                this.status = status;
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
