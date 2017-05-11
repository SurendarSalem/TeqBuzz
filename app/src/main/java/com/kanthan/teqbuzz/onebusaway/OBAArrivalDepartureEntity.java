package com.kanthan.teqbuzz.onebusaway;

import java.util.ArrayList;

/**
 * Created by suren on 7/30/2016.
 */
public class OBAArrivalDepartureEntity {

    String code, currentTime, text, version;
    Data data;

    public OBAArrivalDepartureEntity() {
    }

    public OBAArrivalDepartureEntity(String code, String currentTime, String text, String version, Data data) {
        this.code = code;
        this.currentTime = currentTime;
        this.text = text;
        this.version = version;
        this.data = data;
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

        ArrayList<ArrivalAndDeparture> arrivalAndDepartures;

        public Data() {
        }

        public ArrayList<ArrivalAndDeparture> getArrivalAndDeparture() {
            return arrivalAndDepartures;
        }

        public void setArrivalAndDeparture(ArrayList<ArrivalAndDeparture> and) {
            this.arrivalAndDepartures = and;
        }

        public class ArrivalAndDeparture {
            String arrivalEnabled, blockTripSequence, departureEnabled, distanceFromStop, lastUpdateTime, numberOfStopsAway, predicted, predictedArrivalInterval, predictedArrivalTime, predictedDepartureInterval, predictedDepartureTime, routeId, routeLongName, routeShortName, scheduledArrivalInterval, scheduledArrivalTime, scheduledDepartureInterval, scheduledDepartureTime, serviceDate, situationIds, status, stopId, stopSequence, totalStopsInTrip, tripHeadsign, tripId, vehicleId;
            TripStatus tripStatus;

            public ArrivalAndDeparture() {
            }

            public ArrivalAndDeparture(String arrivalEnabled, String blockTripSequence, String departureEnabled, String distanceFromStop, String lastUpdateTime, String numberOfStopsAway, String predicted, String predictedArrivalInterval, String predictedArrivalTime, String predictedDepartureInterval, String predictedDepartureTime, String routeId, String routeLongName, String routeShortName, String scheduledArrivalInterval, String scheduledArrivalTime, String scheduledDepartureInterval, String scheduledDepartureTime, String serviceDate, String situationIds, String status, String stopId, String stopSequence, String totalStopsInTrip, String tripHeadsign, String tripId, String vehicleId, TripStatus tripStatus) {
                this.arrivalEnabled = arrivalEnabled;
                this.blockTripSequence = blockTripSequence;
                this.departureEnabled = departureEnabled;
                this.distanceFromStop = distanceFromStop;
                this.lastUpdateTime = lastUpdateTime;
                this.numberOfStopsAway = numberOfStopsAway;
                this.predicted = predicted;
                this.predictedArrivalInterval = predictedArrivalInterval;
                this.predictedArrivalTime = predictedArrivalTime;
                this.predictedDepartureInterval = predictedDepartureInterval;
                this.predictedDepartureTime = predictedDepartureTime;
                this.routeId = routeId;
                this.routeLongName = routeLongName;
                this.routeShortName = routeShortName;
                this.scheduledArrivalInterval = scheduledArrivalInterval;
                this.scheduledArrivalTime = scheduledArrivalTime;
                this.scheduledDepartureInterval = scheduledDepartureInterval;
                this.scheduledDepartureTime = scheduledDepartureTime;
                this.serviceDate = serviceDate;
                this.situationIds = situationIds;
                this.status = status;
                this.stopId = stopId;
                this.stopSequence = stopSequence;
                this.totalStopsInTrip = totalStopsInTrip;
                this.tripHeadsign = tripHeadsign;
                this.tripId = tripId;
                this.vehicleId = vehicleId;
                this.tripStatus = tripStatus;
            }

            public String getArrivalEnabled() {
                return arrivalEnabled;
            }

            public void setArrivalEnabled(String arrivalEnabled) {
                this.arrivalEnabled = arrivalEnabled;
            }

            public String getBlockTripSequence() {
                return blockTripSequence;
            }

            public void setBlockTripSequence(String blockTripSequence) {
                this.blockTripSequence = blockTripSequence;
            }

            public String getDepartureEnabled() {
                return departureEnabled;
            }

            public void setDepartureEnabled(String departureEnabled) {
                this.departureEnabled = departureEnabled;
            }

            public String getDistanceFromStop() {
                return distanceFromStop;
            }

            public void setDistanceFromStop(String distanceFromStop) {
                this.distanceFromStop = distanceFromStop;
            }

            public String getLastUpdateTime() {
                return lastUpdateTime;
            }

            public void setLastUpdateTime(String lastUpdateTime) {
                this.lastUpdateTime = lastUpdateTime;
            }

            public String getNumberOfStopsAway() {
                return numberOfStopsAway;
            }

            public void setNumberOfStopsAway(String numberOfStopsAway) {
                this.numberOfStopsAway = numberOfStopsAway;
            }

            public String getPredicted() {
                return predicted;
            }

            public void setPredicted(String predicted) {
                this.predicted = predicted;
            }

            public String getPredictedArrivalInterval() {
                return predictedArrivalInterval;
            }

            public void setPredictedArrivalInterval(String predictedArrivalInterval) {
                this.predictedArrivalInterval = predictedArrivalInterval;
            }

            public String getPredictedArrivalTime() {
                return predictedArrivalTime;
            }

            public void setPredictedArrivalTime(String predictedArrivalTime) {
                this.predictedArrivalTime = predictedArrivalTime;
            }

            public String getPredictedDepartureInterval() {
                return predictedDepartureInterval;
            }

            public void setPredictedDepartureInterval(String predictedDepartureInterval) {
                this.predictedDepartureInterval = predictedDepartureInterval;
            }

            public String getPredictedDepartureTime() {
                return predictedDepartureTime;
            }

            public void setPredictedDepartureTime(String predictedDepartureTime) {
                this.predictedDepartureTime = predictedDepartureTime;
            }

            public String getRouteId() {
                return routeId;
            }

            public void setRouteId(String routeId) {
                this.routeId = routeId;
            }

            public String getRouteLongName() {
                return routeLongName;
            }

            public void setRouteLongName(String routeLongName) {
                this.routeLongName = routeLongName;
            }

            public String getRouteShortName() {
                return routeShortName;
            }

            public void setRouteShortName(String routeShortName) {
                this.routeShortName = routeShortName;
            }

            public String getScheduledArrivalInterval() {
                return scheduledArrivalInterval;
            }

            public void setScheduledArrivalInterval(String scheduledArrivalInterval) {
                this.scheduledArrivalInterval = scheduledArrivalInterval;
            }

            public String getScheduledArrivalTime() {
                return scheduledArrivalTime;
            }

            public void setScheduledArrivalTime(String scheduledArrivalTime) {
                this.scheduledArrivalTime = scheduledArrivalTime;
            }

            public String getScheduledDepartureInterval() {
                return scheduledDepartureInterval;
            }

            public void setScheduledDepartureInterval(String scheduledDepartureInterval) {
                this.scheduledDepartureInterval = scheduledDepartureInterval;
            }

            public String getScheduledDepartureTime() {
                return scheduledDepartureTime;
            }

            public void setScheduledDepartureTime(String scheduledDepartureTime) {
                this.scheduledDepartureTime = scheduledDepartureTime;
            }

            public String getServiceDate() {
                return serviceDate;
            }

            public void setServiceDate(String serviceDate) {
                this.serviceDate = serviceDate;
            }

            public String getSituationIds() {
                return situationIds;
            }

            public void setSituationIds(String situationIds) {
                this.situationIds = situationIds;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getStopId() {
                return stopId;
            }

            public void setStopId(String stopId) {
                this.stopId = stopId;
            }

            public String getStopSequence() {
                return stopSequence;
            }

            public void setStopSequence(String stopSequence) {
                this.stopSequence = stopSequence;
            }

            public String getTotalStopsInTrip() {
                return totalStopsInTrip;
            }

            public void setTotalStopsInTrip(String totalStopsInTrip) {
                this.totalStopsInTrip = totalStopsInTrip;
            }

            public String getTripHeadsign() {
                return tripHeadsign;
            }

            public void setTripHeadsign(String tripHeadsign) {
                this.tripHeadsign = tripHeadsign;
            }

            public String getTripId() {
                return tripId;
            }

            public void setTripId(String tripId) {
                this.tripId = tripId;
            }

            public String getVehicleId() {
                return vehicleId;
            }

            public void setVehicleId(String vehicleId) {
                this.vehicleId = vehicleId;
            }

            public TripStatus getTripStatus() {
                return tripStatus;
            }

            public void setTripStatus(TripStatus tripStatus) {
                this.tripStatus = tripStatus;
            }

            public class TripStatus {
                String activeTripId, blockTripSequence, closestStop, closestStopTimeOffset, distanceAlongTrip, frequency, lastKnownDistanceAlongTrip, lastKnownOrientation, lastLocationUpdateTime, lastUpdateTime, nextStop, nextStopTimeOffset, orientation, phase, predicted, scheduleDeviation, scheduledDistanceAlongTrip, serviceDate, situationIds, status, totalDistanceAlongTrip;
                LastKnownLocation lastKnownLocation;
                Position position;

                public String getActiveTripId() {
                    return activeTripId;
                }

                public void setActiveTripId(String activeTripId) {
                    this.activeTripId = activeTripId;
                }

                public String getBlockTripSequence() {
                    return blockTripSequence;
                }

                public void setBlockTripSequence(String blockTripSequence) {
                    this.blockTripSequence = blockTripSequence;
                }

                public String getClosestStop() {
                    return closestStop;
                }

                public void setClosestStop(String closestStop) {
                    this.closestStop = closestStop;
                }

                public String getClosestStopTimeOffset() {
                    return closestStopTimeOffset;
                }

                public void setClosestStopTimeOffset(String closestStopTimeOffset) {
                    this.closestStopTimeOffset = closestStopTimeOffset;
                }

                public String getDistanceAlongTrip() {
                    return distanceAlongTrip;
                }

                public void setDistanceAlongTrip(String distanceAlongTrip) {
                    this.distanceAlongTrip = distanceAlongTrip;
                }

                public String getFrequency() {
                    return frequency;
                }

                public void setFrequency(String frequency) {
                    this.frequency = frequency;
                }

                public String getLastKnownDistanceAlongTrip() {
                    return lastKnownDistanceAlongTrip;
                }

                public void setLastKnownDistanceAlongTrip(String lastKnownDistanceAlongTrip) {
                    this.lastKnownDistanceAlongTrip = lastKnownDistanceAlongTrip;
                }

                public String getLastKnownOrientation() {
                    return lastKnownOrientation;
                }

                public void setLastKnownOrientation(String lastKnownOrientation) {
                    this.lastKnownOrientation = lastKnownOrientation;
                }

                public String getLastLocationUpdateTime() {
                    return lastLocationUpdateTime;
                }

                public void setLastLocationUpdateTime(String lastLocationUpdateTime) {
                    this.lastLocationUpdateTime = lastLocationUpdateTime;
                }

                public String getLastUpdateTime() {
                    return lastUpdateTime;
                }

                public void setLastUpdateTime(String lastUpdateTime) {
                    this.lastUpdateTime = lastUpdateTime;
                }

                public String getNextStop() {
                    return nextStop;
                }

                public void setNextStop(String nextStop) {
                    this.nextStop = nextStop;
                }

                public String getNextStopTimeOffset() {
                    return nextStopTimeOffset;
                }

                public void setNextStopTimeOffset(String nextStopTimeOffset) {
                    this.nextStopTimeOffset = nextStopTimeOffset;
                }

                public String getOrientation() {
                    return orientation;
                }

                public void setOrientation(String orientation) {
                    this.orientation = orientation;
                }

                public String getPhase() {
                    return phase;
                }

                public void setPhase(String phase) {
                    this.phase = phase;
                }

                public String getPredicted() {
                    return predicted;
                }

                public void setPredicted(String predicted) {
                    this.predicted = predicted;
                }

                public String getScheduleDeviation() {
                    return scheduleDeviation;
                }

                public void setScheduleDeviation(String scheduleDeviation) {
                    this.scheduleDeviation = scheduleDeviation;
                }

                public String getScheduledDistanceAlongTrip() {
                    return scheduledDistanceAlongTrip;
                }

                public void setScheduledDistanceAlongTrip(String scheduledDistanceAlongTrip) {
                    this.scheduledDistanceAlongTrip = scheduledDistanceAlongTrip;
                }

                public String getServiceDate() {
                    return serviceDate;
                }

                public void setServiceDate(String serviceDate) {
                    this.serviceDate = serviceDate;
                }

                public String getSituationIds() {
                    return situationIds;
                }

                public void setSituationIds(String situationIds) {
                    this.situationIds = situationIds;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getTotalDistanceAlongTrip() {
                    return totalDistanceAlongTrip;
                }

                public void setTotalDistanceAlongTrip(String totalDistanceAlongTrip) {
                    this.totalDistanceAlongTrip = totalDistanceAlongTrip;
                }

                public LastKnownLocation getLastKnownLocation() {
                    return lastKnownLocation;
                }

                public void setLastKnownLocation(LastKnownLocation lastKnownLocation) {
                    this.lastKnownLocation = lastKnownLocation;
                }

                public Position getPosition() {
                    return position;
                }

                public void setPosition(Position position) {
                    this.position = position;
                }

                public TripStatus() {
                }

                public TripStatus(String activeTripId, String blockTripSequence, String closestStop, String closestStopTimeOffset, String distanceAlongTrip, String frequency, String lastKnownDistanceAlongTrip, String lastKnownOrientation, String lastLocationUpdateTime, String lastUpdateTime, String nextStop, String nextStopTimeOffset, String orientation, String phase, String predicted, String scheduleDeviation, String scheduledDistanceAlongTrip, String serviceDate, String situationIds, String status, String totalDistanceAlongTrip, LastKnownLocation lastKnownLocation, Position position) {
                    this.activeTripId = activeTripId;
                    this.blockTripSequence = blockTripSequence;
                    this.closestStop = closestStop;
                    this.closestStopTimeOffset = closestStopTimeOffset;
                    this.distanceAlongTrip = distanceAlongTrip;
                    this.frequency = frequency;
                    this.lastKnownDistanceAlongTrip = lastKnownDistanceAlongTrip;
                    this.lastKnownOrientation = lastKnownOrientation;
                    this.lastLocationUpdateTime = lastLocationUpdateTime;
                    this.lastUpdateTime = lastUpdateTime;
                    this.nextStop = nextStop;
                    this.nextStopTimeOffset = nextStopTimeOffset;
                    this.orientation = orientation;
                    this.phase = phase;
                    this.predicted = predicted;
                    this.scheduleDeviation = scheduleDeviation;
                    this.scheduledDistanceAlongTrip = scheduledDistanceAlongTrip;
                    this.serviceDate = serviceDate;
                    this.situationIds = situationIds;
                    this.status = status;
                    this.totalDistanceAlongTrip = totalDistanceAlongTrip;
                    this.lastKnownLocation = lastKnownLocation;
                    this.position = position;
                }

                public class LastKnownLocation {
                    String lat, lon;

                    public LastKnownLocation() {
                    }

                    public LastKnownLocation(String lat, String lon) {
                        this.lat = lat;
                        this.lon = lon;
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
                }

                public class Position {
                    String lat, lon;

                    public Position() {
                    }

                    public Position(String lat, String lon) {
                        this.lat = lat;
                        this.lon = lon;
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
                }
            }
        }
    }
}
