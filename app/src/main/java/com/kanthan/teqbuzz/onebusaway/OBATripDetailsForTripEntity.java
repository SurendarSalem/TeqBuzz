package com.kanthan.teqbuzz.onebusaway;

import java.util.ArrayList;

/**
 * Created by suren on 8/20/2016.
 */
public class OBATripDetailsForTripEntity {
    String code, currentTime;
    Data data;

    public OBATripDetailsForTripEntity() {
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
        Reference reference;

        public Data() {
        }

        public Entry getEntry() {
            return entry;
        }

        public void setEntry(Entry entry) {
            this.entry = entry;
        }

        public Reference getReference() {
            return reference;
        }

        public void setReference(Reference reference) {
            this.reference = reference;
        }

        public class Entry {

            Schedule schedule;

            public Entry() {
            }

            public Schedule getSchedule() {
                return schedule;
            }

            public void setSchedule(Schedule schedule) {
                this.schedule = schedule;
            }

            public class Schedule {

                String nextTripId, previousTripId;
                ArrayList<StopTime> stopTimes;

                public Schedule() {
                }

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

                    String arrivalTime, departureTime, distanceAlongTrip, tripId, stopId;

                    public String getStopId() {
                        return stopId;
                    }

                    public void setStopId(String stopId) {
                        this.stopId = stopId;
                    }

                    public StopTime() {

                    }

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

                    public String getTripId() {
                        return tripId;
                    }

                    public void setTripId(String tripId) {
                        this.tripId = tripId;
                    }

                }
            }

        }

        public class Reference {

            public Reference() {
            }

            ArrayList<Stop> stops;

            public ArrayList<Stop> getStops() {
                return stops;
            }

            public void setStops(ArrayList<Stop> stops) {
                this.stops = stops;
            }

            public class Stop {
                String code, direction, id, lat, lon, locationType, name;
                ArrayList<String> routeIds;

                public Stop() {
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getDirection() {
                    return direction;
                }

                public void setDirection(String direction) {
                    this.direction = direction;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

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

                public String getLocationType() {
                    return locationType;
                }

                public void setLocationType(String locationType) {
                    this.locationType = locationType;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public ArrayList<String> getRouteIds() {
                    return routeIds;
                }

                public void setRouteIds(ArrayList<String> routeIds) {
                    this.routeIds = routeIds;
                }
            }
        }
    }
}
