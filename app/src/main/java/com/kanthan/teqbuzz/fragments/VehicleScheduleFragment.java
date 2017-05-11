package com.kanthan.teqbuzz.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kanthan.teqbuzz.R;
import com.kanthan.teqbuzz.adapter.VehicleScheduleAdapter;
import com.kanthan.teqbuzz.models.Schedule;
import com.kanthan.teqbuzz.utilities.Constants;
import com.kanthan.teqbuzz.utilities.MySupportFragment;
import com.kanthan.teqbuzz.utilities.WebService;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 5/4/2016.
 */
public class VehicleScheduleFragment extends Fragment {

    private View rootView;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    MySupportFragment scheduleMapFragment;
    private GoogleMap scheduleMap;
    private boolean isMapLoaded;
    private String TAG = "VehicleDetailFragment";
    Activity mActivity;
    private ListView schedule_list;
    VehicleScheduleAdapter vehicleScheduleAdapter;
    private ArrayList<Schedule> schedules;
    private BroadcastReceiver getVehicleScheduleListResponseReceiver;
    private WebService webService;
    public String str_vehicle_id;
    private Marker tempMarker;
    private BitmapDescriptor vehicle_marker;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_schedule_list, container, false);
        /*slidingUpPanelLayout = (SlidingUpPanelLayout) rootView.findViewById(R.id.sliding_layout);
        webService = new WebService(mActivity);
        str_vehicle_id = getArguments().getString("vehicle_id");
        vehicle_marker = BitmapDescriptorFactory.fromResource(R.drawable.orange_circle);
        panelListener();
        schedule_list = (ListView) rootView.findViewById(R.id.schedule_list);
        schedule_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                moveCameraToSelectedSchedule(i);

            }
        });
        schedules = new ArrayList<Schedule>();
        vehicleScheduleAdapter = new VehicleScheduleAdapter(mActivity, this, schedules);
        schedule_list.setAdapter(vehicleScheduleAdapter);

        try {
            MapsInitializer.initialize(mActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (scheduleMapFragment == null) {
            scheduleMapFragment = (MySupportFragment) getChildFragmentManager().findFragmentById(R.id.scheduleMapFragment);
            //scheduleMap = scheduleMapFragment.getMap();
        }
        scheduleMapFragment.onResume();
        scheduleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                isMapLoaded = true;
            }
        });
        if (schedules.size() == 0) {

            getVehicleScheduleList(str_vehicle_id);
        }*/


        return rootView;
    }

    private void moveCameraToSelectedSchedule(int i) {

        double double_latitude = Double.parseDouble(schedules.get(i).getStop_latitude());
        double double_longitude = Double.parseDouble(schedules.get(i).getStop_longitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(double_latitude, double_longitude)).tilt(Constants.MAP_TILT).zoom(Constants.ROUTE_MAP_ZOOM_LEVEL).build();
        scheduleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        //menu.clear();
        inflater.inflate(R.menu.menu_dummy, menu);

    }

    private void getVehicleScheduleList(String vehicle_id) {
        //webService.getVehicleScheduleList(mActivity, vehicle_id, (WebServiceListener) this);
    }

    class GetVehicleScheduleListResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String response = intent.getStringExtra("response");

            if (response.contains("schedule_id")) {

                parseAndAddSchedules(response);

            } else if (response.contains("No vehicle schedule")) {

            }

        }
    }

    private void parseAndAddSchedules(String jsonResponse) {

        String schedule_id, vehicle_id, vehicle_from, vehicle_to, stop_id,
                arrival_time, departure_time, vehicle_line_number, latitude,
                longitude, vehicle_agency, vehicle_type, stop_name, stop_latitude,
                stop_longitude, is_alarm_set;
        try {

            //jsonResponse = "{\"status\":[{\"vehicle_id\":\"1\",\"vehicle_line_number\":\"1003\",\"latitude\":\"12.931575\",\"longitude\":\"77.609581\",\"vehicle_agency\":\"parveen\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-21 11:12:04\",\"date_modified\":\"2016-04-11 01:38:20\",\"status\":\"0\",\"distance\":\"32.771042327684356\"},{\"vehicle_id\":\"2\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"},{\"vehicle_id\":\"3\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"},{\"vehicle_id\":\"4\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"},{\"vehicle_id\":\"5\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"},{\"vehicle_id\":\"6\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"}]}";

            jsonResponse.replace("\\\"", "\"");
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray status_array = jsonObject.getJSONArray("status");
            for (int i = 0; i < status_array.length(); i++) {
                schedule_id = status_array.getJSONObject(i).getString("schedule_id");
                vehicle_id = status_array.getJSONObject(i).getString("vehicle_id");
                vehicle_from = status_array.getJSONObject(i).getString("vehicle_from");
                vehicle_to = status_array.getJSONObject(i).getString("vehicle_to");
                stop_id = status_array.getJSONObject(i).getString("stop_id");
                arrival_time = status_array.getJSONObject(i).getString("arrival_time");
                departure_time = status_array.getJSONObject(i).getString("departure_time");
                vehicle_line_number = status_array.getJSONObject(i).getString("vehicle_line_number");
                latitude = status_array.getJSONObject(i).getString("latitude");
                longitude = status_array.getJSONObject(i).getString("longitude");
                vehicle_agency = status_array.getJSONObject(i).getString("vehicle_agency");
                vehicle_type = status_array.getJSONObject(i).getString("vehicle_type");
                stop_name = status_array.getJSONObject(i).getString("stop_name");
                stop_latitude = status_array.getJSONObject(i).getString("stop_latitude");
                stop_longitude = status_array.getJSONObject(i).getString("stop_longitude");

                Schedule schedule = new Schedule(schedule_id, vehicle_id, vehicle_from, vehicle_to, stop_id, arrival_time, departure_time, vehicle_line_number, latitude, longitude, vehicle_agency, vehicle_type, stop_name, stop_latitude, stop_longitude, "0");
                schedules.add(schedule);

            }

            vehicleScheduleAdapter.notifyDataSetChanged();
            drawRouteForSchedules();
            addMarkerForSchedules();
            moveCameraToSelectedSchedule(0);


        } catch (JSONException e) {

            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.some_error_occured),
                    Toast.LENGTH_LONG).show();

            e.printStackTrace();
        } catch (NullPointerException e) {

            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.some_error_occured),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void addMarkerForSchedules() {

        for (Schedule schedule : schedules) {

            MarkerOptions tempMarkerOptions = new MarkerOptions().position(
                    new LatLng(Double.parseDouble(schedule.getStop_latitude()), Double.parseDouble(schedule.getStop_longitude())))
                    .snippet(schedule.getStop_name()).title(schedule.getStop_name()).icon(vehicle_marker);

            tempMarker = scheduleMap.addMarker(tempMarkerOptions);
            //newMarkers.add(tempMarker);

        }
    }

    private void drawRouteForSchedules() {


        PolylineOptions options = new PolylineOptions().width(10).color(mActivity.getResources().getColor(R.color.orange)).geodesic(true);
        for (int i = 0; i < schedules.size(); i++) {
            LatLng point = new LatLng(Double.parseDouble(schedules.get(i).getStop_latitude()), Double.parseDouble(schedules.get(i).getStop_longitude()));
            options.add(point);
        }
        scheduleMap.addPolyline(options);

    }

   /* private void moveCameraToSchedules() {
        double double_latitude = Double.parseDouble(schedules.get(0).getStop_latitude());
        double double_longitude = Double.parseDouble(schedules.get(0).getStop_longitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(double_latitude, double_longitude)).tilt(Constants.MAP_TILT).zoom(Constants.MAP_ZOOM_LEVEL).build();
        scheduleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }*/


    private void registerGetVehicleScheduleListResponseReceiver() {

        if (getVehicleScheduleListResponseReceiver == null) {
            getVehicleScheduleListResponseReceiver = new GetVehicleScheduleListResponseReceiver();
            mActivity.registerReceiver(getVehicleScheduleListResponseReceiver, new IntentFilter(WebService.INTENT_FILTER_SCHEDULE_LIST_RESPONSE));
        }
    }

    private void unRegisterGetVehicleScheduleListResponseReceiver() {

        if (getVehicleScheduleListResponseReceiver != null) {
            mActivity.unregisterReceiver(getVehicleScheduleListResponseReceiver);
            getVehicleScheduleListResponseReceiver = null;
        }
    }

    private void panelListener() {

        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            // During the transition of expand and collapse onPanelSlide function will be called.
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

                Log.e(TAG, "onPanelSlide, offset " + slideOffset);
            }

            // Called when secondary layout is dragged up by user
            @Override
            public void onPanelExpanded(View panel) {

                Log.e(TAG, "onPanelExpanded");
                if (scheduleMapFragment != null)
                    scheduleMapFragment.onPause();
            }

            // Called when secondary layout is dragged down by user
            @Override
            public void onPanelCollapsed(View panel) {

                Log.e(TAG, "onPanelCollapsed");
                if (scheduleMapFragment != null)
                    scheduleMapFragment.onResume();
            }

            @Override
            public void onPanelAnchored(View panel) {

                Log.e(TAG, "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {

                Log.e(TAG, "onPanelHidden");
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerGetVehicleScheduleListResponseReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        unRegisterGetVehicleScheduleListResponseReceiver();
    }
}