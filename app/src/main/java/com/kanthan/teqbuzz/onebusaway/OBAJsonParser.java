package com.kanthan.teqbuzz.onebusaway;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by suren on 7/20/2016.
 */
public class OBAJsonParser {

    public OBAStopEntity parseAndGetRouteEntity(String response) {

        OBAStopEntity obaStopEntity = new OBAStopEntity();

        try {
            JSONObject mainJsonObject = new JSONObject(response);

            if (mainJsonObject.has("code")) {
                String code = mainJsonObject.getString("code");
                obaStopEntity.setCode(code);
            }

            if (mainJsonObject.has("currentTime")) {
                String currentTime = mainJsonObject.getString("currentTime");
                obaStopEntity.setCurrentTime(currentTime);
            }

            if (mainJsonObject.has("data")) {
                OBAStopEntity.List obaList = null;
                JSONObject data = mainJsonObject.getJSONObject("data");
                OBAStopEntity.Data obaData = new OBAStopEntity().new Data();

                if (data.has("limitExceeded")) {
                    String limitExceeded = data.getString("limitExceeded");
                    obaData.setLimitExceeded(limitExceeded);
                }

                if (data.has("list")) {
                    JSONArray jsonList = data.getJSONArray("list");
                    ArrayList<OBAStopEntity.List> obaLists = new ArrayList<OBAStopEntity.List>();

                    for (int i = 0; i < jsonList.length(); i++) {

                        JSONObject jsonListObject = jsonList.getJSONObject(i);
                        String code = jsonListObject.getString("code");
                        String directions = jsonListObject.getString("direction");
                        String id = jsonListObject.getString("id");
                        String lat = jsonListObject.getString("lat");
                        String locationType = jsonListObject.getString("locationType");
                        String lon = jsonListObject.getString("lon");
                        String name = jsonListObject.getString("name");

                        JSONArray jsonRouteIdArray = jsonListObject.getJSONArray("routeIds");

                        ArrayList<OBAStopEntity.RouteId> routeIds = new ArrayList<OBAStopEntity.RouteId>();
                        for (int j = 0; j < jsonRouteIdArray.length(); j++) {

                            String index = String.valueOf(j);
                            String value = jsonRouteIdArray.getString(j);
                            OBAStopEntity.RouteId routeId = new OBAStopEntity().new RouteId(index, value);
                            routeIds.add(routeId);
                        }

                        obaList = new OBAStopEntity().new List(code, directions, id, lat, locationType, lon, name, routeIds);
                        obaLists.add(obaList);

                    }

                    obaData.setLists(obaLists);
                }


                if (data.has("outOfRange")) {

                    obaData.setOutOfRange(data.getString("outOfRange"));
                }

                if (data.has("references")) {
                    OBAStopEntity.References references = new OBAStopEntity().new References();
                    JSONObject jsonReferences = data.getJSONObject("references");
                    JSONArray jsonAgencyArray = jsonReferences.getJSONArray("agencies");
                    ArrayList<OBAStopEntity.Agency> agencies = new ArrayList<OBAStopEntity.Agency>();
                    for (int i = 0; i < jsonAgencyArray.length(); i++) {
                        JSONObject jsonAgency = jsonAgencyArray.getJSONObject(i);
                        String disclaimer = jsonAgency.getString("disclaimer");
                        String email = jsonAgency.getString("email");
                        String fareUrl = jsonAgency.getString("fareUrl");
                        String id = jsonAgency.getString("id");
                        String lang = jsonAgency.getString("lang");
                        String name = jsonAgency.getString("name");
                        String phone = jsonAgency.getString("phone");
                        String privateService = jsonAgency.getString("privateService");
                        String timezone = jsonAgency.getString("timezone");
                        String url = jsonAgency.getString("url");
                        OBAStopEntity.Agency agency = new OBAStopEntity().new Agency(disclaimer, email, fareUrl, id, lang, name, phone, privateService, timezone, url);
                        agencies.add(agency);

                    }
                    references.setAgencies(agencies);


                    JSONArray jsonRoutesArray = jsonReferences.getJSONArray("routes");
                    ArrayList<OBAStopEntity.Route> routes = new ArrayList<OBAStopEntity.Route>();
                    for (int i = 0; i < jsonRoutesArray.length(); i++) {
                        JSONObject jsonRoute = jsonRoutesArray.getJSONObject(i);
                        String agencyId = jsonRoute.getString("agencyId");
                        String color = jsonRoute.getString("color");
                        String description = jsonRoute.getString("description");
                        String id = jsonRoute.getString("id");
                        String longName = jsonRoute.getString("longName");
                        String shortName = jsonRoute.getString("shortName");
                        String textColor = jsonRoute.getString("textColor");
                        String type = jsonRoute.getString("type");
                        String url = jsonRoute.getString("url");
                        OBAStopEntity.Route route = new OBAStopEntity().new Route(agencyId, color, description, id, longName, shortName, textColor, type, url);
                        routes.add(route);

                    }
                    references.setRoutes(routes);


                    references.setSituations(null);
                    references.setStops(null);
                    references.setTrips(null);

                    obaData.setReferences(references);

                }

                obaStopEntity.setData(obaData);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return obaStopEntity;

    }


    public OBARouteEntity parseAndGetRouteListEntity(String response) {

        OBARouteEntity obaRouteEntity = new OBARouteEntity();

        try {
            JSONObject mainJsonObject = new JSONObject(response);

            if (mainJsonObject.has("code")) {
                String code = mainJsonObject.getString("code");
                obaRouteEntity.setCode(code);
            }

            if (mainJsonObject.has("currentTime")) {
                String currentTime = mainJsonObject.getString("currentTime");
                obaRouteEntity.setCurrentTime(currentTime);
            }

            if (mainJsonObject.has("data")) {
                JSONObject data = mainJsonObject.getJSONObject("data");
                OBARouteEntity.Data obaData = new OBARouteEntity().new Data();

                if (data.has("limitExceeded")) {
                    String limitExceeded = data.getString("limitExceeded");
                    obaData.setLimitExceeded(limitExceeded);
                }

                if (data.has("list")) {
                    JSONArray jsonList = data.getJSONArray("list");
                    ArrayList<OBARouteEntity.Data.Route> obaRoutes = new ArrayList<OBARouteEntity.Data.Route>();
                    OBARouteEntity.Data.Route obaRoute = new OBARouteEntity().new Data().new Route();
                    for (int i = 0; i < jsonList.length(); i++) {

                        JSONObject jsonListObject = jsonList.getJSONObject(i);
                        String agencyId = jsonListObject.getString("agencyId");
                        String color = jsonListObject.getString("color");
                        String description = jsonListObject.getString("description");
                        String id = jsonListObject.getString("id");
                        String longName = jsonListObject.getString("longName");
                        String shortName = jsonListObject.getString("shortName");
                        String textColor = jsonListObject.getString("textColor");
                        String type = jsonListObject.getString("type");
                        String url = jsonListObject.getString("url");


                        obaRoute = new OBARouteEntity().new Data().new Route(agencyId, color, description, id, longName, shortName, textColor, type, url);
                        obaRoutes.add(obaRoute);

                    }

                    obaData.setList(obaRoutes);
                }


                if (data.has("outOfRange")) {

                    obaData.setOutOfRange(data.getString("outOfRange"));
                }

                if (data.has("references")) {
                    OBARouteEntity.Data.Reference reference = new OBARouteEntity().new Data().new Reference();
                    JSONObject jsonReferences = data.getJSONObject("references");
                    JSONArray jsonAgencyArray = jsonReferences.getJSONArray("agencies");
                    ArrayList<OBARouteEntity.Data.Reference.Agency> agencies = new ArrayList<OBARouteEntity.Data.Reference.Agency>();
                    for (int i = 0; i < jsonAgencyArray.length(); i++) {
                        JSONObject jsonAgency = jsonAgencyArray.getJSONObject(i);
                        String disclaimer = jsonAgency.getString("disclaimer");
                        String email = jsonAgency.getString("email");
                        String fareUrl = jsonAgency.getString("fareUrl");
                        String id = jsonAgency.getString("id");
                        String lang = jsonAgency.getString("lang");
                        String name = jsonAgency.getString("name");
                        String phone = jsonAgency.getString("phone");
                        String privateService = jsonAgency.getString("privateService");
                        String timezone = jsonAgency.getString("timezone");
                        String url = jsonAgency.getString("url");
                        OBARouteEntity.Data.Reference.Agency agency = new OBARouteEntity().new Data().new Reference().new Agency(disclaimer, email, fareUrl, id, lang, name, phone, privateService, timezone, url);
                        agencies.add(agency);

                    }
                    reference.setAgencies(agencies);


                    JSONArray jsonRoutesArray = jsonReferences.getJSONArray("routes");
                    ArrayList<OBARouteEntity.Data.Reference.Route> routes = new ArrayList<OBARouteEntity.Data.Reference.Route>();
                    for (int i = 0; i < jsonRoutesArray.length(); i++) {
                        /*JSONObject jsonRoute = jsonRoutesArray.getJSONObject(i);
                        String agencyId = jsonRoute.getString("agencyId");
                        String color = jsonRoute.getString("color");
                        String description = jsonRoute.getString("description");
                        String id = jsonRoute.getString("id");
                        String longName = jsonRoute.getString("longName");
                        String shortName = jsonRoute.getString("shortName");
                        String textColor = jsonRoute.getString("textColor");
                        String type = jsonRoute.getString("type");
                        String url = jsonRoute.getString("url");*/
                        OBARouteEntity.Data.Reference.Route route = new OBARouteEntity().new Data().new Reference().new Route();
                        routes.add(route);

                    }
                    reference.setRoutes(routes);
                    reference.setSituations(null);
                    reference.setStops(null);
                    reference.setTrips(null);

                    obaData.setReferences(reference);

                }

                obaRouteEntity.setData(obaData);

            }


        } catch (JSONException e) {
            Log.v("suren", e.getMessage().toString());

            e.printStackTrace();
        }


        return obaRouteEntity;

    }


    public OBAStopListEntity parseAndGetStopListEntity(String response, String route_id) {

        OBAStopListEntity obaStopListEntity = new OBAStopListEntity();

        try {
            JSONObject mainJsonObject = new JSONObject(response);

            if (mainJsonObject.has("code")) {
                String code = mainJsonObject.getString("code");
                obaStopListEntity.setCode(code);
            }

            if (mainJsonObject.has("currentTime")) {
                String currentTime = mainJsonObject.getString("currentTime");
                obaStopListEntity.setCurrentTime(currentTime);
            }

            if (mainJsonObject.has("data")) {
                JSONObject data = mainJsonObject.getJSONObject("data");
                OBAStopListEntity.Data obaData = new OBAStopListEntity().new Data();

                if (data.has("entry")) {
                    OBAStopListEntity.Data.Entry obaEntry = new OBAStopListEntity().new Data().new Entry();
                    JSONObject jsonEntry = data.getJSONObject("entry");
                    if (jsonEntry.has("polylines")) {
                        ArrayList<OBAStopListEntity.Data.Entry.PolyLine> obaPolyLines = new ArrayList<OBAStopListEntity.Data.Entry.PolyLine>();
                        JSONArray jsonPolylinesArray = jsonEntry.getJSONArray("polylines");
                        for (int i = 0; i < jsonPolylinesArray.length(); i++) {
                            String length = jsonPolylinesArray.getJSONObject(i).getString("length");
                            String levels = jsonPolylinesArray.getJSONObject(i).getString("levels");
                            String points = jsonPolylinesArray.getJSONObject(i).getString("points");
                            OBAStopListEntity.Data.Entry.PolyLine obaPolyLine = new OBAStopListEntity().new Data().new Entry().new PolyLine(length, levels, points);
                            obaPolyLines.add(obaPolyLine);
                        }
                        obaEntry.setPolyLines(obaPolyLines);
                    }
                    obaData.setEntry(obaEntry);
                }

                if (data.has("references")) {
                    JSONObject jsonReference = data.getJSONObject("references");
                    if (jsonReference.has("stops")) {
                        String code, direction, id, lat, locationType, lon, name, wheelchairBoarding;
                        OBAStopListEntity.Stop obaStop;
                        ArrayList<String> routeIds = new ArrayList<String>();
                        ArrayList<OBAStopListEntity.Stop> stops = new ArrayList<OBAStopListEntity.Stop>();

                        JSONArray jsonStopsArray = jsonReference.getJSONArray("stops");
                        for (int i = 0; i < jsonStopsArray.length(); i++) {
                            code = jsonStopsArray.getJSONObject(i).getString("code");
                            direction = jsonStopsArray.getJSONObject(i).getString("direction");
                            id = jsonStopsArray.getJSONObject(i).getString("id");
                            lat = jsonStopsArray.getJSONObject(i).getString("lat");
                            locationType = jsonStopsArray.getJSONObject(i).getString("locationType");
                            lon = jsonStopsArray.getJSONObject(i).getString("lon");
                            name = jsonStopsArray.getJSONObject(i).getString("name");
                            wheelchairBoarding = jsonStopsArray.getJSONObject(i).getString("wheelchairBoarding");
                            obaStop = new OBAStopListEntity().new Stop(code, direction, id, lat, locationType, lon, name, wheelchairBoarding, routeIds);
                            obaStop.setRoute_id(route_id);
                            //if (id.equalsIgnoreCase("1_9978"))// temporary stops
                            stops.add(obaStop);

                        }
                        obaStopListEntity.setStops(stops);
                    }


                }
                obaStopListEntity.setData(obaData);

            }


        } catch (JSONException e) {
            Log.v("suren", e.getMessage().toString());

            e.printStackTrace();
        }

        obaStopListEntity.setRouteId(route_id);
        return obaStopListEntity;

    }

    public OBAArrivalDepartureEntity parseAndGetArrivalDepartureEntity(String response) {

        OBAArrivalDepartureEntity obaArrivalDepartureEntity = new OBAArrivalDepartureEntity();

        try {
            JSONObject mainJsonObject = new JSONObject(response);

            if (mainJsonObject.has("code")) {
                String code = mainJsonObject.getString("code");
                obaArrivalDepartureEntity.setCode(code);
            }

            if (mainJsonObject.has("currentTime")) {
                String currentTime = mainJsonObject.getString("currentTime");
                obaArrivalDepartureEntity.setCurrentTime(currentTime);
            }

            if (mainJsonObject.has("data")) {
                JSONObject jsonData = mainJsonObject.getJSONObject("data");
                OBAArrivalDepartureEntity.Data obaData = new OBAArrivalDepartureEntity().new Data();

                if (jsonData.has("entry")) {
                    JSONObject jsonEntry = jsonData.getJSONObject("entry");

                    if (jsonEntry.has("arrivalsAndDepartures")) {
                        ArrayList<OBAArrivalDepartureEntity.Data.ArrivalAndDeparture> obaArrivalAndDepartures = new ArrayList<OBAArrivalDepartureEntity.Data.ArrivalAndDeparture>();
                        JSONArray jsonArrivalsAndDepartureJsonArray = jsonEntry.getJSONArray("arrivalsAndDepartures");

                        for (int i = 0; i < jsonArrivalsAndDepartureJsonArray.length(); i++) {
                            OBAArrivalDepartureEntity.Data.ArrivalAndDeparture obaArrivalAndDeparture = new OBAArrivalDepartureEntity().new Data().new ArrivalAndDeparture();
                            JSONObject jsonArrivalsAndDepartureObject = jsonArrivalsAndDepartureJsonArray.getJSONObject(i);
                            String arrivalEnabled = jsonArrivalsAndDepartureObject.getString("arrivalEnabled");
                            String blockTripSequence = jsonArrivalsAndDepartureObject.getString("blockTripSequence");
                            String departureEnabled = jsonArrivalsAndDepartureObject.getString("departureEnabled");
                            String distanceFromStop = jsonArrivalsAndDepartureObject.getString("distanceFromStop");
                            String lastUpdateTime = jsonArrivalsAndDepartureObject.getString("lastUpdateTime");
                            String numberOfStopsAway = jsonArrivalsAndDepartureObject.getString("numberOfStopsAway");
                            String predicted = jsonArrivalsAndDepartureObject.getString("predicted");
                            String predictedArrivalInterval = jsonArrivalsAndDepartureObject.getString("predictedArrivalInterval");
                            String predictedArrivalTime = jsonArrivalsAndDepartureObject.getString("predictedArrivalTime");
                            String predictedDepartureTime = jsonArrivalsAndDepartureObject.getString("predictedDepartureTime");
                            String routeId = jsonArrivalsAndDepartureObject.getString("routeId");
                            String routeLongName = jsonArrivalsAndDepartureObject.getString("routeLongName");
                            String routeShortName = jsonArrivalsAndDepartureObject.getString("routeShortName");
                            String scheduledArrivalInterval = jsonArrivalsAndDepartureObject.getString("scheduledArrivalInterval");
                            String scheduledArrivalTime = jsonArrivalsAndDepartureObject.getString("scheduledArrivalTime");
                            String scheduledDepartureInterval = jsonArrivalsAndDepartureObject.getString("scheduledDepartureInterval");
                            String scheduledDepartureTime = jsonArrivalsAndDepartureJsonArray.getJSONObject(i).getString("scheduledDepartureTime");
                            String serviceDate = jsonArrivalsAndDepartureObject.getString("serviceDate");
                            String situationIds = jsonArrivalsAndDepartureObject.getString("situationIds");
                            String status = jsonArrivalsAndDepartureObject.getString("status");
                            String stopId = jsonArrivalsAndDepartureObject.getString("stopId");
                            String stopSequence = jsonArrivalsAndDepartureObject.getString("stopSequence");
                            String totalStopsInTrip = jsonArrivalsAndDepartureObject.getString("totalStopsInTrip");
                            String tripHeadSign = jsonArrivalsAndDepartureObject.getString("tripHeadsign");
                            String tripId = jsonArrivalsAndDepartureObject.getString("tripId");
                            String vehicleId = jsonArrivalsAndDepartureObject.getString("vehicleId");

                            obaArrivalAndDeparture.setArrivalEnabled(arrivalEnabled);
                            obaArrivalAndDeparture.setBlockTripSequence(blockTripSequence);
                            obaArrivalAndDeparture.setDepartureEnabled(departureEnabled);
                            obaArrivalAndDeparture.setDistanceFromStop(distanceFromStop);
                            obaArrivalAndDeparture.setLastUpdateTime(lastUpdateTime);
                            obaArrivalAndDeparture.setNumberOfStopsAway(numberOfStopsAway);
                            obaArrivalAndDeparture.setPredicted(predicted);
                            obaArrivalAndDeparture.setPredictedArrivalInterval(predictedArrivalInterval);
                            obaArrivalAndDeparture.setPredictedArrivalTime(predictedArrivalTime);
                            obaArrivalAndDeparture.setPredictedDepartureTime(predictedDepartureTime);
                            obaArrivalAndDeparture.setRouteId(routeId);
                            obaArrivalAndDeparture.setRouteLongName(routeLongName);
                            obaArrivalAndDeparture.setRouteShortName(routeShortName);
                            obaArrivalAndDeparture.setScheduledArrivalInterval(scheduledArrivalInterval);
                            obaArrivalAndDeparture.setScheduledArrivalTime(scheduledArrivalTime);
                            obaArrivalAndDeparture.setScheduledDepartureInterval(scheduledDepartureInterval);
                            obaArrivalAndDeparture.setScheduledDepartureTime(scheduledDepartureTime);
                            obaArrivalAndDeparture.setServiceDate(serviceDate);
                            obaArrivalAndDeparture.setSituationIds(situationIds);
                            obaArrivalAndDeparture.setStatus(status);
                            obaArrivalAndDeparture.setStopId(stopId);
                            obaArrivalAndDeparture.setStopSequence(stopSequence);
                            obaArrivalAndDeparture.setTotalStopsInTrip(totalStopsInTrip);
                            obaArrivalAndDeparture.setTripHeadsign(tripHeadSign);
                            obaArrivalAndDeparture.setTripId(tripId);
                            obaArrivalAndDeparture.setVehicleId(vehicleId);

                            if (jsonArrivalsAndDepartureObject.has("tripStatus") && !jsonArrivalsAndDepartureObject.isNull("tripStatus")) {
                                JSONObject jsonTripStatus = jsonArrivalsAndDepartureObject.getJSONObject("tripStatus");

                                OBAArrivalDepartureEntity.Data.ArrivalAndDeparture.TripStatus obaTripStatus = new OBAArrivalDepartureEntity().new Data().new ArrivalAndDeparture().new TripStatus();

                                String activeTripId = jsonTripStatus.getString("activeTripId");
                                String closestStop = jsonTripStatus.getString("closestStop");
                                String closestStopTimeOffset = jsonTripStatus.getString("closestStopTimeOffset");
                                String distanceAlongTrip = jsonTripStatus.getString("distanceAlongTrip");
                                String frequency = jsonTripStatus.getString("frequency");
                                String lastKnownDistanceAlongTrip = jsonTripStatus.getString("lastKnownDistanceAlongTrip");
                                String lastKnownOrientation = jsonTripStatus.getString("lastKnownOrientation");
                                String lastLocationUpdateTime = jsonTripStatus.getString("lastLocationUpdateTime");
                                String nextStop = jsonTripStatus.getString("nextStop");
                                String nextStopTimeOffset = jsonTripStatus.getString("nextStopTimeOffset");
                                String orientation = jsonTripStatus.getString("orientation");
                                String phase = jsonTripStatus.getString("phase");
                                String scheduleDeviation = jsonTripStatus.getString("scheduleDeviation");
                                String scheduledDistanceAlongTrip = jsonTripStatus.getString("scheduledDistanceAlongTrip");
                                String totalDistanceAlongTrip = jsonTripStatus.getString("totalDistanceAlongTrip");

                                obaTripStatus.setActiveTripId(activeTripId);
                                obaTripStatus.setClosestStop(closestStop);
                                obaTripStatus.setClosestStopTimeOffset(closestStopTimeOffset);
                                obaTripStatus.setDistanceAlongTrip(distanceAlongTrip);
                                obaTripStatus.setFrequency(frequency);
                                obaTripStatus.setLastKnownDistanceAlongTrip(lastKnownDistanceAlongTrip);
                                obaTripStatus.setLastKnownOrientation(lastKnownOrientation);
                                obaTripStatus.setLastLocationUpdateTime(lastLocationUpdateTime);
                                obaTripStatus.setNextStop(nextStop);
                                obaTripStatus.setNextStopTimeOffset(nextStopTimeOffset);
                                obaTripStatus.setOrientation(orientation);
                                obaTripStatus.setPhase(phase);
                                obaTripStatus.setScheduleDeviation(scheduleDeviation);
                                obaTripStatus.setScheduledDistanceAlongTrip(scheduledDistanceAlongTrip);
                                obaTripStatus.setTotalDistanceAlongTrip(totalDistanceAlongTrip);

                               /* if (jsonTripStatus.has("lastKnownLocation")) {
                                    if (!jsonTripStatus.isNull("lastKnownLocation")) {
                                        JSONObject jsonLastKnownLocationObject = jsonTripStatus.getJSONObject("lastKnownLocation");
                                        OBAArrivalDepartureEntity.Data.ArrivalAndDeparture.TripStatus.LastKnownLocation obaLastKnownLocation = new OBAArrivalDepartureEntity().new Data().new ArrivalAndDeparture().new TripStatus().new LastKnownLocation();
                                        String lat = jsonLastKnownLocationObject.getString("lat");
                                        String lon = jsonLastKnownLocationObject.getString("lon");
                                        obaLastKnownLocation.setLat(lat);
                                        obaLastKnownLocation.setLon(lon);
                                        obaTripStatus.setLastKnownLocation(obaLastKnownLocation);
                                    }
                                }*/
                                if (jsonTripStatus.has("position")) {
                                    JSONObject jsonPositionObject = jsonTripStatus.getJSONObject("position");
                                    OBAArrivalDepartureEntity.Data.ArrivalAndDeparture.TripStatus.Position obaPosition = new OBAArrivalDepartureEntity().new Data().new ArrivalAndDeparture().new TripStatus().new Position();
                                    String lat = jsonPositionObject.getString("lat");
                                    String lon = jsonPositionObject.getString("lon");
                                    obaPosition.setLat(lat);
                                    obaPosition.setLon(lon);
                                    obaTripStatus.setPosition(obaPosition);

                                }

                                obaArrivalAndDeparture.setTripStatus(obaTripStatus);
                            }

                            obaArrivalAndDepartures.add(obaArrivalAndDeparture);
                        }
                        obaData.setArrivalAndDeparture(obaArrivalAndDepartures);
                    }


                }
                obaArrivalDepartureEntity.setData(obaData);
            }


        } catch (JSONException e) {
            Log.v("suren", e.getMessage().toString());

            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return obaArrivalDepartureEntity;
    }

    public OBATripsForRouteEntity parseAndGetOBATripsForRouteEntity(String response, String routeId) {
        OBATripsForRouteEntity obaTripsForRouteEntity = new OBATripsForRouteEntity();

        try {
            JSONObject mainJsonObject = new JSONObject(response);
            if (mainJsonObject.has("code")) {
                String code = mainJsonObject.getString("code");
                obaTripsForRouteEntity.setCode(code);
            }
            if (mainJsonObject.has("currentTime")) {
                String currentTime = mainJsonObject.getString("currentTime");
                obaTripsForRouteEntity.setCurrentTime(currentTime);
            }

            if (mainJsonObject.has("data")) {
                OBATripsForRouteEntity.Data obaData = new OBATripsForRouteEntity().new Data();
                JSONObject jsonData = mainJsonObject.getJSONObject("data");
                if (jsonData.has("limitExceeded")) {
                    String limitExceeded = jsonData.getString("limitExceeded");
                    obaData.setLimitExceeded(limitExceeded);
                }
                if (jsonData.has("list")) {
                    ArrayList<OBATripsForRouteEntity.Data.Trip> obaTrips = new ArrayList<OBATripsForRouteEntity.Data.Trip>();
                    JSONArray jsonTrips = jsonData.getJSONArray("list");
                    for (int i = 0; i < jsonTrips.length(); i++) {
                        OBATripsForRouteEntity.Data.Trip obaTrip = new OBATripsForRouteEntity().new Data().new Trip();
                        JSONObject jsonTrip = jsonTrips.getJSONObject(i);

                        String tripId = jsonTrip.getString("tripId");
                        obaTrip.setTripId(tripId);

                        OBATripsForRouteEntity.Data.Trip.Status obaStatus = new OBATripsForRouteEntity().new Data().new Trip().new Status();
                        JSONObject jsonStatus = jsonTrip.getJSONObject("status");

                        String activeTripId = jsonStatus.getString("activeTripId");
                        obaStatus.setActiveTripId(activeTripId);
                        String closestStop = jsonStatus.getString("closestStop");
                        obaStatus.setClosestStop(closestStop);
                        String nextStop = jsonStatus.getString("nextStop");
                        obaStatus.setNextStop(nextStop);
                        String status = jsonStatus.getString("status");
                        obaStatus.setStatus(status);
                        String vehicleId = jsonStatus.getString("vehicleId");
                        obaStatus.setVehicleId(vehicleId);

                        JSONObject jsonPosition = jsonStatus.getJSONObject("position");
                        OBATripsForRouteEntity.Data.Trip.Status.Position obaPosition = new OBATripsForRouteEntity().new Data().new Trip().new Status().new Position();
                        String lat = jsonPosition.getString("lat");
                        String lon = jsonPosition.getString("lon");
                        obaPosition.setLat(lat);
                        obaPosition.setLon(lon);

                        obaStatus.setPosition(obaPosition);
                        obaTrip.setStatus(obaStatus);
                        obaTrips.add(obaTrip);
                    }
                    obaData.setTrips(obaTrips);
                }
                obaTripsForRouteEntity.setData(obaData);
            }

        } catch (JSONException e) {
        } catch (Exception e) {
        }

        return obaTripsForRouteEntity;

    }

    public OBATripsForLocationEntity parseAndGetOBATripsForLocationEntity(String res) {
        OBATripsForLocationEntity obaTripsForLocationEntity = new OBATripsForLocationEntity();

        try {
            JSONObject mainJsonObject = new JSONObject(res);
            if (mainJsonObject.has("code")) {
                String code = mainJsonObject.getString("code");
                obaTripsForLocationEntity.setCode(code);
            }
            if (mainJsonObject.has("currentTime")) {
                String currentTime = mainJsonObject.getString("currentTime");
                obaTripsForLocationEntity.setCurrentTime(currentTime);
            }

            if (mainJsonObject.has("data")) {
                OBATripsForLocationEntity.Data obaData = new OBATripsForLocationEntity().new Data();
                JSONObject jsonData = mainJsonObject.getJSONObject("data");
                if (jsonData.has("limitExceeded")) {
                    String limitExceeded = jsonData.getString("limitExceeded");
                    obaData.setLimitExceeded(limitExceeded);
                }
                if (jsonData.has("list")) {
                    ArrayList<OBATripsForLocationEntity.Data.Trip> obaTrips = new ArrayList<OBATripsForLocationEntity.Data.Trip>();
                    JSONArray jsonTrips = jsonData.getJSONArray("list");
                    for (int i = 0; i < jsonTrips.length(); i++) {
                        OBATripsForLocationEntity.Data.Trip obaTrip = new OBATripsForLocationEntity().new Data().new Trip();
                        JSONObject jsonTrip = jsonTrips.getJSONObject(i);

                        String tripId = jsonTrip.getString("tripId");
                        obaTrip.setTripId(tripId);

                        OBATripsForLocationEntity.Data.Trip.Status obaStatus = new OBATripsForLocationEntity().new Data().new Trip().new Status();
                        JSONObject jsonStatus = jsonTrip.getJSONObject("status");

                        String activeTripId = jsonStatus.getString("activeTripId");
                        obaStatus.setActiveTripId(activeTripId);
                        String closestStop = jsonStatus.getString("closestStop");
                        obaStatus.setClosestStop(closestStop);
                        String nextStop = jsonStatus.getString("nextStop");
                        obaStatus.setNextStop(nextStop);
                        String status = jsonStatus.getString("status");
                        obaStatus.setStatus(status);
                        String vehicleId = jsonStatus.getString("vehicleId");
                        obaStatus.setVehicleId(vehicleId);

                        JSONObject jsonPosition = jsonStatus.getJSONObject("position");
                        OBATripsForLocationEntity.Data.Trip.Status.Position obaPosition = new OBATripsForLocationEntity().new Data().new Trip().new Status().new Position();
                        String lat = jsonPosition.getString("lat");
                        String lon = jsonPosition.getString("lon");
                        obaPosition.setLat(lat);
                        obaPosition.setLon(lon);

                        obaStatus.setPosition(obaPosition);
                        obaTrip.setStatus(obaStatus);
                        obaTrips.add(obaTrip);
                    }
                    obaData.setTrips(obaTrips);
                }
                obaTripsForLocationEntity.setData(obaData);
            }

        } catch (JSONException e) {
        } catch (Exception e) {
        }

        return obaTripsForLocationEntity;
    }

    public OBATripDetailsForVehicleEntity parseAndGetOBATripDetailsForVehicleEntity(String res) {
        OBATripDetailsForVehicleEntity obaTripDetailsForVehicleEntity = null;
        try {
            JSONObject mainJsonObject = new JSONObject(res);
            obaTripDetailsForVehicleEntity = new OBATripDetailsForVehicleEntity();

            String code = mainJsonObject.getString("code");
            obaTripDetailsForVehicleEntity.setCode(code);

            String currentTime = mainJsonObject.getString("currentTime");
            obaTripDetailsForVehicleEntity.setCurrentTime(currentTime);

            if (mainJsonObject.has("data")) {
                OBATripDetailsForVehicleEntity.Data obaData = new OBATripDetailsForVehicleEntity().new Data();
                JSONObject jsonData = mainJsonObject.getJSONObject("data");

                if (jsonData.has("entry")) {
                    OBATripDetailsForVehicleEntity.Data.Entry obaEntry = new OBATripDetailsForVehicleEntity().new Data().new Entry();
                    JSONObject jsonEntry = jsonData.getJSONObject("entry");

                    if (jsonEntry.has("schedule") && !jsonEntry.isNull("schedule")) {
                        OBATripDetailsForVehicleEntity.Data.Entry.Schedule obaSchedule = new OBATripDetailsForVehicleEntity().new Data().new Entry().new Schedule();
                        JSONObject jsonSchedule = jsonEntry.getJSONObject("schedule");

                        String nextTripId = jsonSchedule.getString("nextTripId");
                        obaSchedule.setNextTripId(nextTripId);

                        String previousTripId = jsonSchedule.getString("previousTripId");
                        obaSchedule.setPreviousTripId(previousTripId);

                        if (jsonSchedule.has("stopTimes")) {
                            JSONArray jsonArrayStopTimes = jsonSchedule.getJSONArray("stopTimes");
                            ArrayList<OBATripDetailsForVehicleEntity.Data.Entry.Schedule.StopTime> obaStopTimes = new ArrayList<OBATripDetailsForVehicleEntity.Data.Entry.Schedule.StopTime>();
                            for (int i = 0; i < jsonArrayStopTimes.length(); i++) {
                                OBATripDetailsForVehicleEntity.Data.Entry.Schedule.StopTime obaStopTime = new OBATripDetailsForVehicleEntity().new Data().new Entry().new Schedule().new StopTime();
                                JSONObject jsonStopTime = jsonArrayStopTimes.getJSONObject(i);
                                String arrivalTime = jsonStopTime.getString("arrivalTime");
                                obaStopTime.setArrivalTime(arrivalTime);
                                String departureTime = jsonStopTime.getString("departureTime");
                                obaStopTime.setDepartureTime(departureTime);
                                String distanceAlongTrip = jsonStopTime.getString("distanceAlongTrip");
                                obaStopTime.setDistanceAlongTrip(distanceAlongTrip);
                                String stopId = jsonStopTime.getString("stopId");
                                obaStopTime.setStopId(stopId);

                                obaStopTimes.add(obaStopTime);
                            }
                            obaSchedule.setStopTimes(obaStopTimes);
                        }

                        obaEntry.setSchedule(obaSchedule);

                    }
                    // status
                    if (jsonEntry.has("status")) {
                        JSONObject jsonTripStatus = jsonEntry.getJSONObject("status");

                        OBATripDetailsForVehicleEntity.Data.Entry.Status obaTripStatus = new OBATripDetailsForVehicleEntity().new Data().new Entry().new Status();

                        String activeTripId = jsonTripStatus.getString("activeTripId");
                        String closestStop = jsonTripStatus.getString("closestStop");
                        String nextStop = jsonTripStatus.getString("nextStop");
                        String vehicleId = jsonTripStatus.getString("vehicleId");


                        obaTripStatus.setActiveTripId(activeTripId);
                        obaTripStatus.setClosestStop(closestStop);
                        obaTripStatus.setNextStop(nextStop);
                        obaTripStatus.setVehicleId(vehicleId);

                        if (jsonTripStatus.has("position")) {
                            JSONObject jsonPositionObject = jsonTripStatus.getJSONObject("position");
                            OBATripDetailsForVehicleEntity.Data.Entry.Status.Position obaPosition = new OBATripDetailsForVehicleEntity().new Data().new Entry().new Status().new Position();
                            String lat = jsonPositionObject.getString("lat");
                            String lon = jsonPositionObject.getString("lon");
                            obaPosition.setLat(lat);
                            obaPosition.setLon(lon);
                            obaTripStatus.setPosition(obaPosition);

                        }

                        obaEntry.setStatus(obaTripStatus);
                    }

                    obaData.setEntry(obaEntry);
                }
                obaTripDetailsForVehicleEntity.setData(obaData);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obaTripDetailsForVehicleEntity;
    }

    public OBATripDetailsForTripEntity parseAndGetOBATripDetailsForTripEntity(String res) {

        OBATripDetailsForTripEntity obaTripDetailsForTripEntity = null;
        try {
            JSONObject mainJsonObject = new JSONObject(res);
            obaTripDetailsForTripEntity = new OBATripDetailsForTripEntity();

            String code = mainJsonObject.getString("code");
            obaTripDetailsForTripEntity.setCode(code);

            String currentTime = mainJsonObject.getString("currentTime");
            obaTripDetailsForTripEntity.setCurrentTime(currentTime);

            if (mainJsonObject.has("data")) {
                OBATripDetailsForTripEntity.Data obaData = new OBATripDetailsForTripEntity().new Data();
                JSONObject jsonData = mainJsonObject.getJSONObject("data");

                if (jsonData.has("entry")) {
                    OBATripDetailsForTripEntity.Data.Entry obaEntry = new OBATripDetailsForTripEntity().new Data().new Entry();
                    JSONObject jsonEntry = jsonData.getJSONObject("entry");

                    if (jsonEntry.has("schedule") && !jsonEntry.isNull("schedule")) {
                        OBATripDetailsForTripEntity.Data.Entry.Schedule obaSchedule = new OBATripDetailsForTripEntity().new Data().new Entry().new Schedule();
                        JSONObject jsonSchedule = jsonEntry.getJSONObject("schedule");

                        String nextTripId = jsonSchedule.getString("nextTripId");
                        obaSchedule.setNextTripId(nextTripId);

                        String previousTripId = jsonSchedule.getString("previousTripId");
                        obaSchedule.setPreviousTripId(previousTripId);

                        if (jsonSchedule.has("stopTimes")) {
                            JSONArray jsonArrayStopTimes = jsonSchedule.getJSONArray("stopTimes");
                            ArrayList<OBATripDetailsForTripEntity.Data.Entry.Schedule.StopTime> obaStopTimes = new ArrayList<OBATripDetailsForTripEntity.Data.Entry.Schedule.StopTime>();
                            for (int i = 0; i < jsonArrayStopTimes.length(); i++) {
                                OBATripDetailsForTripEntity.Data.Entry.Schedule.StopTime obaStopTime = new OBATripDetailsForTripEntity().new Data().new Entry().new Schedule().new StopTime();
                                JSONObject jsonStopTime = jsonArrayStopTimes.getJSONObject(i);
                                String arrivalTime = jsonStopTime.getString("arrivalTime");
                                obaStopTime.setArrivalTime(arrivalTime);
                                String departureTime = jsonStopTime.getString("departureTime");
                                obaStopTime.setDepartureTime(departureTime);
                                String distanceAlongTrip = jsonStopTime.getString("distanceAlongTrip");
                                obaStopTime.setDistanceAlongTrip(distanceAlongTrip);
                                String stopId = jsonStopTime.getString("stopId");
                                obaStopTime.setStopId(stopId);

                                obaStopTimes.add(obaStopTime);
                            }
                            obaSchedule.setStopTimes(obaStopTimes);
                        }

                        obaEntry.setSchedule(obaSchedule);

                    }

                    obaData.setEntry(obaEntry);
                }

                if (jsonData.has("references")) {

                    JSONObject jsonReference = jsonData.getJSONObject("references");
                    OBATripDetailsForTripEntity.Data.Reference reference = new OBATripDetailsForTripEntity().new Data().new Reference();
                    JSONArray jsonStopJsonArray = jsonReference.getJSONArray("stops");
                    ArrayList<OBATripDetailsForTripEntity.Data.Reference.Stop> obaStops = new ArrayList<OBATripDetailsForTripEntity.Data.Reference.Stop>();
                    for (int i = 0; i < jsonStopJsonArray.length(); i++) {

                        OBATripDetailsForTripEntity.Data.Reference.Stop obaStop = new OBATripDetailsForTripEntity().new Data().new Reference().new Stop();
                        ArrayList<String> routeIds = new ArrayList<String>();
                        JSONObject jsonStopObject = jsonStopJsonArray.getJSONObject(i);

                        String stopCode = jsonStopObject.getString("code");
                        obaStop.setCode(stopCode);
                        String direction = jsonStopObject.getString("direction");
                        obaStop.setDirection(direction);
                        String id = jsonStopObject.getString("id");
                        obaStop.setId(id);
                        String lat = jsonStopObject.getString("lat");
                        obaStop.setLat(lat);
                        String lon = jsonStopObject.getString("lon");
                        obaStop.setLon(lon);
                        String locationType = jsonStopObject.getString("locationType");
                        obaStop.setLocationType(locationType);
                        String name = jsonStopObject.getString("name");
                        obaStop.setName(name);

                        JSONArray jsonRouteIds = jsonStopObject.getJSONArray("routeIds");
                        for (int j = 0; j < jsonRouteIds.length(); j++) {
                            routeIds.add(jsonRouteIds.getString(j));
                        }
                        obaStop.setRouteIds(routeIds);

                        obaStops.add(obaStop);
                    }
                    reference.setStops(obaStops);
                    obaData.setReference(reference);
                }

                obaTripDetailsForTripEntity.setData(obaData);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obaTripDetailsForTripEntity;
    }
}
