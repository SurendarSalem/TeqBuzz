package com.kanthan.teqbuzz.onebusaway;

import java.util.ArrayList;

/**
 * Created by suren on 7/19/2016.
 */
public class OBAStopEntity {
    String code, currentTime, text, version;
    Data data;

    public OBAStopEntity() {
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public OBAStopEntity(String code, String currentTime, String text, String version, Data data) {
        this.code = code;
        this.currentTime = currentTime;
        this.text = text;
        this.version = version;
        this.data = data;


    }

    public class Data {
        String limitExceeded, outOfRange;
        ArrayList<List> lists;

        public ArrayList<List> getLists() {
            return lists;
        }

        public void setLists(ArrayList<List> lists) {
            this.lists = lists;
        }

        References references;

        public Data(String limitExceeded, String outOfRange, ArrayList<List> lists, References references) {
            this.limitExceeded = limitExceeded;
            this.outOfRange = outOfRange;
            this.lists = lists;
            this.references = references;
        }

        public Data() {

        }

        public String getLimitExceeded() {
            return limitExceeded;
        }

        public void setLimitExceeded(String limitExceeded) {
            this.limitExceeded = limitExceeded;
        }

        public String getOutOfRange() {
            return outOfRange;
        }

        public void setOutOfRange(String outOfRange) {
            this.outOfRange = outOfRange;
        }


        public References getReferences() {
            return references;
        }

        public void setReferences(References references) {
            this.references = references;
        }
    }


    public class List {
        String code, directions, id, lat, locationType, lon, name;
        ArrayList<RouteId> routeIds;


        public List(String code, String directions, String id, String lat, String locationType, String lon, String name, ArrayList<RouteId> routeIds) {
            this.code = code;
            this.directions = directions;
            this.id = id;
            this.lat = lat;
            this.locationType = locationType;
            this.lon = lon;
            this.name = name;
            this.routeIds = routeIds;
        }

        public List() {

        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDirections() {
            return directions;
        }

        public void setDirections(String directions) {
            this.directions = directions;
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

        public ArrayList<RouteId> getRouteIds() {
            return routeIds;
        }

        public void setRouteIds(ArrayList<RouteId> routeIds) {
            this.routeIds = routeIds;
        }
    }

    public class RouteId {
        String index, value;

        public RouteId(String index, String value) {
            this.index = index;
            this.value = value;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


    public class References {
        ArrayList<Agency> agencies;
        ArrayList<Situations> situations;
        ArrayList<Stops> stops;
        ArrayList<Trips> trips;
        ArrayList<Route> routes;

        public ArrayList<Route> getRoutes() {
            return routes;
        }

        public void setRoutes(ArrayList<Route> routes) {
            this.routes = routes;
        }

        public References() {

        }

        public ArrayList<Agency> getAgencies() {
            return agencies;
        }

        public void setAgencies(ArrayList<Agency> agencies) {
            this.agencies = agencies;
        }

        public References(ArrayList<Situations> situations, ArrayList<Stops> stops, ArrayList<Trips> trips, ArrayList<Agency> agencies, ArrayList<Route> routes) {
            this.situations = situations;
            this.stops = stops;
            this.routes = routes;
            this.trips = trips;
            this.agencies = agencies;
        }

        public ArrayList<Situations> getSituations() {
            return situations;
        }

        public void setSituations(ArrayList<Situations> situations) {
            this.situations = situations;
        }

        public ArrayList<Stops> getStops() {
            return stops;
        }

        public void setStops(ArrayList<Stops> stops) {
            this.stops = stops;
        }

        public ArrayList<Trips> getTrips() {
            return trips;
        }

        public void setTrips(ArrayList<Trips> trips) {
            this.trips = trips;
        }
    }

    public class Agency {
        String disclaimer, email, fareUrl, id, lang, name, phone, privateService, timezone, url;

        public Agency(String disclaimer, String email, String fareUrl, String id, String lang, String name, String phone, String privateService, String timezone, String url) {
            this.disclaimer = disclaimer;
            this.email = email;
            this.fareUrl = fareUrl;
            this.id = id;
            this.lang = lang;
            this.name = name;
            this.phone = phone;
            this.privateService = privateService;
            this.timezone = timezone;
            this.url = url;
        }

        public String getDisclaimer() {
            return disclaimer;
        }

        public void setDisclaimer(String disclaimer) {
            this.disclaimer = disclaimer;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFareUrl() {
            return fareUrl;
        }

        public void setFareUrl(String fareUrl) {
            this.fareUrl = fareUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPrivateService() {
            return privateService;
        }

        public void setPrivateService(String privateService) {
            this.privateService = privateService;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public class Route {
        String agencyId, color, description, id, longName, shortName, textColor, type, url;

        public Route(String agencyId, String color, String description, String id, String longName, String shortName, String textColor, String type, String url) {
            this.agencyId = agencyId;
            this.color = color;
            this.description = description;
            this.id = id;
            this.longName = longName;
            this.shortName = shortName;
            this.textColor = textColor;
            this.type = type;
            this.url = url;
        }

        public String getAgencyId() {
            return agencyId;
        }

        public void setAgencyId(String agencyId) {
            this.agencyId = agencyId;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLongName() {
            return longName;
        }

        public void setLongName(String longName) {
            this.longName = longName;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public class Situations {

    }

    public class Stops {

    }

    public class Trips {

    }

}

