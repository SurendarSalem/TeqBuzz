package com.kanthan.teqbuzz.onebusaway;

import java.util.ArrayList;

/**
 * Created by suren on 7/26/2016.
 */
public class OBARouteEntity {
    String code, currentTime, text, version;
    Data data;

    public OBARouteEntity(String code, String currentTime, String text, String version, Data data) {
        this.code = code;
        this.currentTime = currentTime;
        this.text = text;
        this.version = version;
        this.data = data;
    }

    public OBARouteEntity() {
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

    public class Data {
        String limitExceeded, outOfRange;
        ArrayList<Route> list;
        Reference references;

        public Data() {
        }

        public Data(String limitExceeded, String outOfRange, ArrayList<Route> list, Reference references) {
            this.limitExceeded = limitExceeded;
            this.outOfRange = outOfRange;
            this.list = list;
            this.references = references;
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

        public ArrayList<Route> getList() {
            return list;
        }

        public void setList(ArrayList<Route> list) {
            this.list = list;
        }

        public Reference getReferences() {
            return references;
        }

        public void setReferences(Reference references) {
            this.references = references;
        }

        public class Route {

            String agencyId, color, description, id, longName, shortName, textColor, type, url;

            public Route() {
            }

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

        public class Reference {
            ArrayList<Agency> agencies;
            ArrayList<Route> routes;
            ArrayList<Situation> situations;
            ArrayList<Stop> stops;
            ArrayList<Trip> trips;

            public Reference() {
            }

            public Reference(ArrayList<Agency> agencies, ArrayList<Route> routes, ArrayList<Situation> situations, ArrayList<Stop> stops, ArrayList<Trip> trips) {
                this.agencies = agencies;
                this.routes = routes;
                this.situations = situations;
                this.stops = stops;
                this.trips = trips;
            }


            public ArrayList<Agency> getAgencies() {
                return agencies;
            }

            public void setAgencies(ArrayList<Agency> agencies) {
                this.agencies = agencies;
            }

            public ArrayList<Route> getRoutes() {
                return routes;
            }

            public void setRoutes(ArrayList<Route> routes) {
                this.routes = routes;
            }

            public ArrayList<Situation> getSituations() {
                return situations;
            }

            public void setSituations(ArrayList<Situation> situations) {
                this.situations = situations;
            }

            public ArrayList<Stop> getStops() {
                return stops;
            }

            public void setStops(ArrayList<Stop> stops) {
                this.stops = stops;
            }

            public ArrayList<Trip> getTrips() {
                return trips;
            }

            public void setTrips(ArrayList<Trip> trips) {
                this.trips = trips;
            }

            class Agency {
                String disclaimer, email, fareUrl, id, lang, name, phone, privateService, timezone, url;

                public Agency() {
                }

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

            class Route {
                public Route() {
                }
            }

            class Situation {
                public Situation() {
                }
            }

            class Stop {
                public Stop() {
                }
            }

            class Trip {
                public Trip() {
                }
            }
        }
    }

}
