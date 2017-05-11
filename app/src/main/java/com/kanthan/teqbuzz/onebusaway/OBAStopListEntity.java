package com.kanthan.teqbuzz.onebusaway;

import java.util.ArrayList;

/**
 * Created by suren on 7/19/2016.
 */
public class OBAStopListEntity {

    String routeId, code, currentTime;
    ArrayList<Stop> stops;
    Data data;

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
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

    public ArrayList<Stop> getStops() {
        return stops;
    }

    public void setStops(ArrayList<Stop> stops) {
        this.stops = stops;
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
            ArrayList<PolyLine> polyLines;

            public Entry() {
            }

            public Entry(ArrayList<PolyLine> polyLines) {
                this.polyLines = polyLines;
            }

            public ArrayList<PolyLine> getPolyLines() {
                return polyLines;
            }

            public void setPolyLines(ArrayList<PolyLine> polyLines) {
                this.polyLines = polyLines;
            }

            public class PolyLine {
                String length, levels, points;

                public PolyLine() {
                }

                public PolyLine(String length, String levels, String points) {
                    this.length = length;
                    this.levels = levels;
                    this.points = points;
                }

                public String getLength() {
                    return length;
                }

                public void setLength(String length) {
                    this.length = length;
                }

                public String getLevels() {
                    return levels;
                }

                public void setLevels(String levels) {
                    this.levels = levels;
                }

                public String getPoints() {
                    return points;
                }

                public void setPoints(String points) {
                    this.points = points;
                }
            }
        }

    }

    public class Stop {
        String code, direction, id, lat, locationType, lon, name, wheelchairBoarding, route_id;
        ArrayList<String> routeIds;

        public String getRoute_id() {
            return route_id;
        }

        public void setRoute_id(String route_id) {
            this.route_id = route_id;
        }

        public Stop(String code, String direction, String id, String lat, String locationType, String lon, String name, String wheelchairBoarding, ArrayList<String> routeIds) {
            this.code = code;
            this.direction = direction;
            this.id = id;
            this.lat = lat;
            this.locationType = locationType;
            this.lon = lon;
            this.name = name;
            this.wheelchairBoarding = wheelchairBoarding;
            this.routeIds = routeIds;
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

        public String getLocationType() {
            return locationType;
        }

        public void setLocationType(String locationType) {
            this.locationType = locationType;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWheelchairBoarding() {
            return wheelchairBoarding;
        }

        public void setWheelchairBoarding(String wheelchairBoarding) {
            this.wheelchairBoarding = wheelchairBoarding;
        }

        public ArrayList<String> getRouteIds() {
            return routeIds;
        }

        public void setRouteIds(ArrayList<String> routeIds) {
            this.routeIds = routeIds;
        }
    }
}
