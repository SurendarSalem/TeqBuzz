package com.kanthan.teqbuzz.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.kanthan.teqbuzz.models.Vehicle;

import java.util.ArrayList;

/**
 * Created by suren on 7/5/2016.
 */
public class AppDatabaseHelper extends SQLiteOpenHelper {

    Context mContext;
    Preferences myPreferences;

    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "TeqBuzzDBManager";

    // Contacts table name
    private static final String TABLE_ROUTES = "routes";

    // Route Table Columns names
    private static final String ROUTE_ID = "id";
    private static final String ROUTE_NAME = "name";
    private static final String ROUTE_COLOR = "color";

    // Contacts table name
    private static final String TABLE_FAVOURITES = "favourites";
    private static final String TABLE_SHARED_VEHICLES = "TableSharedVehicles";
    private static final String TABLE_REMINDERS = "TableReminders";

    private static final String USER_ID = "user_id";
    private static final String FAV_ID = "fav_id";
    private static final String VEHICLE_ID = "vehicle_id";
    private static final String VEHICLE_LINE = "vehicle_line";
    private static final String VEHICLE_AGENCY = "vehicle_agency";

    private static final String STOP_ID = "vehicle_line";
    private static final String ALARM_STATUS = "alarm_pending";

    private static final String SCHEDULE_ID = "vehicle_agency";


    private static final String SHARED_VEHICLE_ID = "shared_vehicle_id";

    public AppDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        mContext = context;
        myPreferences = new Preferences(mContext.getApplicationContext());
    }

    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        myPreferences = new Preferences(mContext.getApplicationContext());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        execCreateTableQuery(db);
        execSharedVehicleTableQuery(db);
        execAlarmTableQuery(db);
    }

    private void execCreateTableQuery(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_FAVOURITES + "("
                + FAV_ID + " INTEGER PRIMARY KEY,"
                + USER_ID + " TEXT,"
                + VEHICLE_ID + " TEXT,"
                + VEHICLE_LINE + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    private void execSharedVehicleTableQuery(SQLiteDatabase db) {
        String CREATE_SHARED_VEHICLE_TABLE = "CREATE TABLE " + TABLE_SHARED_VEHICLES + "("
                + SHARED_VEHICLE_ID + " INTEGER PRIMARY KEY,"
                + USER_ID + " TEXT,"
                + VEHICLE_ID + " TEXT,"
                + VEHICLE_LINE + " TEXT,"
                + VEHICLE_AGENCY + " TEXT"
                + ")";
        db.execSQL(CREATE_SHARED_VEHICLE_TABLE);
    }

    private void execAlarmTableQuery(SQLiteDatabase db) {
        String CREATE_ALARM_REMINDER_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "("
                + USER_ID + " TEXT,"
                + STOP_ID + " TEXT,"
                + SCHEDULE_ID + " TEXT,"
                + ALARM_STATUS + " TEXT"
                + ")";
        db.execSQL(CREATE_ALARM_REMINDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHARED_VEHICLES);
        // Create tables again
        onCreate(db);
    }

    public long addVehicleToFavList(Vehicle vehicle) {

        long result = 0;

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String str = "SELECT * FROM " + TABLE_FAVOURITES + " WHERE";

            ContentValues values = new ContentValues();
            values.put(FAV_ID, String.valueOf(System.currentTimeMillis()));
            values.put(USER_ID, myPreferences.getUser().getUser_id());
            values.put(VEHICLE_ID, vehicle.getVehicle_id());
            values.put(VEHICLE_LINE, vehicle.getVehicle_id());

            // Inserting Row
            result = db.insert(TABLE_FAVOURITES, null, values);
            db.close(); // Closing database connection
        } catch (SQLiteException e) {
            Log.e("addVehicleToFavList", e.getMessage());
        }
        return result;
    }

    public long addVehicleToSharedList(Vehicle vehicle) {

        long result = 0;
        myPreferences = new Preferences(mContext);
        String userId = myPreferences.getUser().getUser_id();
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SHARED_VEHICLE_ID, String.valueOf(System.currentTimeMillis()));
            values.put(VEHICLE_ID, vehicle.getVehicle_id());
            values.put(VEHICLE_LINE, vehicle.getVehicle_line_number());
            values.put(VEHICLE_AGENCY, vehicle.getVehicle_agency());
            values.put(USER_ID, userId);
            // Inserting Row
            result = db.insert(TABLE_SHARED_VEHICLES, null, values);
            db.close(); // Closing database connection
            Log.d("addVehicleToSharedList", "adding shared vehicle " + vehicle.getVehicle_id());
        } catch (SQLiteException e) {
            Log.e("addVehicleToSharedList", e.getMessage());
        }
        return result;
    }


    public long addReminderForStop(String userId, String stopId, String scheduleId) {

        long result = 0;
        myPreferences = new Preferences(mContext);
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(USER_ID, userId);
            values.put(STOP_ID, stopId);
            values.put(SCHEDULE_ID, "");
            values.put(ALARM_STATUS, "1");
            // Inserting Row
            result = db.insert(TABLE_REMINDERS, null, values);
            db.close(); // Closing database connection
            Log.d("addReminderForStop", "adding reminder vehicle ");
        } catch (SQLiteException e) {
            Log.e("addReminderForStop", e.getMessage());
        }
        return result;
    }

    public boolean isReminderSetForStop(String stopId, String userId) {
        boolean isReminderSet = false;
        myPreferences = new Preferences(mContext);
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            // query to check is vehicle in shared list
            Cursor cursor = db.query(TABLE_REMINDERS, null, USER_ID + "=? AND " + STOP_ID + "=?",
                    new String[]{userId, stopId},
                    null, null, null, null);
            if (cursor.getCount() > 0) {
                isReminderSet = true;
                Log.d("isReminderSetForStop", "Stop " + stopId + "in shared list of user id " + userId);
            } else {
                Log.d("isReminderSetForStop", "Stop " + stopId + "is not in shared list of user id " + userId);
            }
            db.close(); // Closing database connection
        } catch (SQLiteException e) {
            Log.e("isReminderSetForStop", e.getMessage());
        }
        return isReminderSet;
    }

    public void removeReminder(String stopId, String userId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_REMINDERS, USER_ID + "=? AND " + STOP_ID + "=?",
                    new String[]{userId, stopId}
            );
            db.close(); // Closing database connection
        } catch (SQLiteException e) {
            Log.e("removeReminder", e.getMessage());
        }
    }


    public boolean isVehicleAlreadyInSharedList(String vehicleId) {
        boolean isInSharedList = false;
        myPreferences = new Preferences(mContext);
        String userId = myPreferences.getUser().getUser_id();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            // query to check is vehicle in shared list
            Cursor cursor = db.query(TABLE_SHARED_VEHICLES, null, USER_ID + "=? AND " + VEHICLE_ID + "=?",
                    new String[]{userId, vehicleId},
                    null, null, null, null);
            if (cursor.getCount() > 0) {
                isInSharedList = true;
                Log.d("isVehicleAlreadyInSharedList", "Vehicle " + vehicleId + "in shared list of user id " + userId);
            } else {
                Log.d("isVehicleAlreadyInSharedList", "Vehicle " + vehicleId + "is not in shared list of user id " + userId);
            }
            db.close(); // Closing database connection
        } catch (SQLiteException e) {
            Log.e("isVehicleAlreadyInSharedList", e.getMessage());
        }
        return isInSharedList;
    }

    public boolean isVehicleFav(Vehicle vehicle) {
        myPreferences = new Preferences(mContext.getApplicationContext());
        String userId = myPreferences.getUser().getUser_id();
        boolean isFav;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.query(TABLE_FAVOURITES, null, USER_ID + "=? AND " + VEHICLE_ID + "=?",
                    new String[]{String.valueOf(userId), String.valueOf(vehicle.getVehicle_id())},
                    null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                isFav = true;
            } else {
                isFav = false;
            }
            db.close(); // Closing database connection
        } catch (SQLiteException e) {
            Log.e("isVehicleFav", e.getMessage());
            isFav = false;
        }
        return isFav;
    }

    public ArrayList<Vehicle> getFavVehicles() {
        String userId = myPreferences.getUser().getUser_id();
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Vehicle> favVehicles = new ArrayList<Vehicle>();

        Cursor cursor = db.query(TABLE_FAVOURITES, null, USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Vehicle favVehicle = new Vehicle();
                    String vehicleId = cursor.getString(cursor.getColumnIndex(VEHICLE_ID));
                    String vehicleLineNum = cursor.getString(cursor.getColumnIndex(VEHICLE_LINE));
                    favVehicle.setVehicle_id(vehicleId);
                    favVehicle.setVehicle_line_number(vehicleLineNum);
                    favVehicles.add(favVehicle);
                } while (cursor.moveToNext());
            }
        }

        return favVehicles;
    }

    public ArrayList<Vehicle> getSharedVehicles() {
        String userId = myPreferences.getUser().getUser_id();
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Vehicle> sharedVehicles = new ArrayList<Vehicle>();

        Cursor cursor = db.query(TABLE_SHARED_VEHICLES, null, USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, SHARED_VEHICLE_ID + " DESC", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Vehicle vehicle = new Vehicle();
                    String vehicleId = cursor.getString(cursor.getColumnIndex(VEHICLE_ID));
                    vehicle.setVehicle_id(vehicleId);
                    String vehicleLineNumber = cursor.getString(cursor.getColumnIndex(VEHICLE_LINE));
                    vehicle.setVehicle_line_number(vehicleLineNumber);
                    String vehicleAgency = cursor.getString(cursor.getColumnIndex(VEHICLE_AGENCY));
                    vehicle.setVehicle_agency(vehicleAgency);
                    sharedVehicles.add(vehicle);
                } while (cursor.moveToNext());
            }
        }
     /*   // to remove duplicates
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(sharedVehicles);
        sharedVehicles.clear();
        sharedVehicles.addAll(hashSet);*/
        return sharedVehicles;
    }

    public void deleteVehicleFromFavList(Vehicle vehicle) {
        String userId = myPreferences.getUser().getUser_id();
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            db.delete(TABLE_FAVOURITES, USER_ID + "=? AND " + VEHICLE_ID + "=?",
                    new String[]{String.valueOf(userId), String.valueOf(vehicle.getVehicle_id())}
            );

            db.close(); // Closing database connection
        } catch (SQLiteException e) {
            Log.e("isVehicleFav", e.getMessage());
        }
    }

    public void deleteVehicleFromSharedList(Vehicle vehicle) {
        String userId = myPreferences.getUser().getUser_id();
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            int result = db.delete(TABLE_SHARED_VEHICLES, USER_ID + "=? AND " + VEHICLE_ID + "=?",
                    new String[]{String.valueOf(userId), String.valueOf(vehicle.getVehicle_id())}
            );
            Log.d("deleteVehicleFromSharedList ", "" + result);
            db.close(); // Closing database connection
        } catch (SQLiteException e) {
            Log.e("deleteVehicleFromSharedList", e.getMessage());
        }
    }
}
