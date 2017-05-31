package com.kanthan.teqbuzz.utilities;

import android.os.Environment;

/**
 * Created by user on 2/29/2016.
 */
public class Constants {
    public static final int CALL_MODE = -1, CALL_MODE_NORMAL = 1, CALL_MODE_FAVORITE = 2, CALL_MODE_SHARED = 3;
    public static final String IS_COMING_FROM = "isComingFrom";
    public static final float MAP_ZOOM_LEVEL = 12;
    public static final float ROUTE_MAP_ZOOM_LEVEL = 9;
    public static final float MAP_TILT = 0;
    public static final String MY_VEHICLE_ID = "1";
    public static final double CIRCLE_RADIUS = 2;
    public static final String DISTANCE_OFFSET = "5000000000";
    public static final int ALARM_REQUEST_CODE = 1;
    public static final long MAP_SPEED = 1000;
    public static final String OBA_MARKER = "oba_marker";
    public static final String OBA_VEHICLE_MARKER = "oba_vehicle_marker";
    public static final String OBA_TRIP_MARKER = "trip_marker";
    public static final String TEQBUZZ_MARKER = "teqbuzz_marker";
    public static final String TEQBUZZ_STOP_MARKER = "teqbuzz_stop_marker";
    public static final String IS_COMING_FROM_DEEP_LINKING = "isComingFromDeepLinking";
    public static final String SHARED_VEHICLE_ID = "sharedVehicleId", SHARED_VEHICLE_LINE_NUMBER = "sharedVehicleLineNumber", SHARED_VEHICLE_AGENCY = "sharedVehicleAgency";
    public static final String IS_CONNECTED = "isConnected";
    public static final String VEHICLE_LIMIT = "100";
    public static boolean IS_GPS_INTENT_REQUESTED_IN_SIGNUP = false;
    public static boolean IS_GPS_INTENT_REQUESTED_IN_MAP = false;
    public static boolean IS_GPS_INTENT_REQUESTED = false;
    public static String APP_NAME = "TeqBuzz";
    public static String LOCAL_STORAGE_IMAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_NAME;
    public static String GOOGLE_MODE = "2", FB_MODE = "1", NORMAL_MODE = "0";
    public static final String GCM_SENDER_ID = "552200766764";
    public static final String API = "AIzaSyD4Xuj46Jwspqk3f8wFyBOdlQ1Z1e9PxCw";
    public static final String INTENT_FILTER_CONNECTIVITY = "internet_changed";
    public static int PLACE_PICKER_REQUEST = 2;
    public static int MAINACTIVITY_PLACE_PICKER_REQUEST = 3;
    public static final int GPS_REQUEST = 4;
    public static int HOME_PICKER_REQUEST = 5;
    public static int WORK_PICKER_REQUEST = 6;
    public static final int HOME_GPS_REQUEST = 7;
    public static final int WORK_GPS_REQUEST = 8;
    public static String MINT_ID = "448f065d";
    public static String GET_MAP_PATH_URL = "https://maps.googleapis.com/maps/api/directions/json?";
    public static String debug_hash_key = "EF:22:ED:97:8F:B8:03:AF:0B:8C:9F:C5:6C:EF:D5:00:9F:1C:CC:BF";
    public static String release_hash_key = "85:2E:C7:EB:0B:FC:D3:72:2B:E3:91:D1:FE:A6:E4:73:5A:BC:29:4A";
    //EF:22:ED:97:8F:B8:03:AF:0B:8C:9F:C5:6C:EF:D5:00:9F:1C:CC:BF
// fb hash: 2jmj7l5rSw0yVb/vlWAYkK/YBwk=
    public interface GoogleAdMob {
        String APP_ID = "ca-app-pub-4405966547814654~4977576325";
        String AD_UNIT_ID_ID = "ca-app-pub-4405966547814654/1744908327";
        int ADS_GAP = 2;

        //     public static String APP_ID = "ca-app-pub-4405966547814654~8950117520";
        //    public static String AD_UNIT_ID_ID = "ca-app-pub-4405966547814654/1426850723";
    }

    public interface CTABusTracker {
        String API_KEY = "xXSrtNCmd5UK9VSXBLUUxx5sD";
        String GET_ROUTE_URL = "http://www.ctabustracker.com/bustime/api/v1/getroutes?key=xXSrtNCmd5UK9VSXBLUUxx5sD";
        String GET_VEHICLES_URL = "http://www.ctabustracker.com/bustime/api/v1/getvehicles?key=xXSrtNCmd5UK9VSXBLUUxx5sD&rt=";
        String GET_DIRECTIONS_URL = "http://www.ctabustracker.com/bustime/api/v1/getdirections?key=xXSrtNCmd5UK9VSXBLUUxx5sD";
        String GET_STOPS_URL = "http://www.ctabustracker.com/bustime/api/v1/getstops?key=xXSrtNCmd5UK9VSXBLUUxx5sD";
        String BUS_ITEM_ARRAY = "bustime-response";
        String TEST_ROUTE = "15";
    }


}
/*     http://tamsonic.de/phpmyadmin
*      username : teqbuzz
*      password :  teq_buss;1
* */