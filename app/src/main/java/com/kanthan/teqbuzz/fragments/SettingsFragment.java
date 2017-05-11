package com.kanthan.teqbuzz.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.kanthan.teqbuzz.MainActivity;
import com.kanthan.teqbuzz.R;
import com.kanthan.teqbuzz.adapter.SharedVehicleListAdapter;
import com.kanthan.teqbuzz.models.User;
import com.kanthan.teqbuzz.models.Vehicle;
import com.kanthan.teqbuzz.service.GPSTracker;
import com.kanthan.teqbuzz.utilities.AppDatabaseHelper;
import com.kanthan.teqbuzz.utilities.Constants;
import com.kanthan.teqbuzz.utilities.Preferences;
import com.kanthan.teqbuzz.utilities.Utility;
import com.kanthan.teqbuzz.utilities.WebService;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    View rootView;
    MaterialEditText name, username, password, confirm_password, phone_number;
    EditText address;
    com.rey.material.widget.Button updateBtn;
    TextView txt_home_latitude, txt_home_longitude, txt_work_latitude, txt_work_longitude;
    ImageView home_gps, home_picker, work_gps, work_picker;
    AppDatabaseHelper appDatabaseHelper;

    // Google client to interact with Google API

    private Context mContext;
    private String TAG = "LoginFragment";
    String str_user_id, str_name, str_email_id, str_password, str_confirm_password, str_phone_number, str_address,
            str_home_latitude, str_home_longitude, str_work_latitude, str_work_longitude, myDeviceId;
    private Preferences myPreferences;
    private String login_err_msg;

    private String mode;
    private Dialog progressDialog;
    Utility utility;
    private Activity mActivity;

    ProgressView progressView;
    private WebService webService;
    private String str_temp_latitude, str_temp_longitude;
    private BroadcastReceiver updateUserResponseReceiver;
    private boolean IS_GPS_FOR_HOME_REQUESTED, IS_GPS_FOR_WORK_REQUESTED;
    private RelativeLayout menuBadgeView;
    android.widget.TextView sharedCount;
    int sharedVehiclesCount;
    ArrayList<Vehicle> sharedVehicles;
    private ImageView deleteSharedVehicleBtn;
    private SharedVehicleListAdapter sharedVehicleListAdapter;

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webService = new WebService(mActivity);
        appDatabaseHelper = new AppDatabaseHelper(mActivity);
        sharedVehicles = appDatabaseHelper.getSharedVehicles();
        sharedVehiclesCount = appDatabaseHelper.getSharedVehicles().size();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        myPreferences = new Preferences(mActivity.getApplicationContext());
        initViews(rootView);
        utility = new Utility(mContext);
        progressDialog = utility.createProgressBar();


        return rootView;
    }

    private void initViews(View rootView) {

        name = (MaterialEditText) rootView.findViewById(R.id.name);
        username = (MaterialEditText) rootView.findViewById(R.id.username);
        password = (MaterialEditText) rootView.findViewById(R.id.password);
        confirm_password = (MaterialEditText) rootView.findViewById(R.id.confirm_password);
        if (String.valueOf(myPreferences.getUser().getMode()).equalsIgnoreCase(Constants.GOOGLE_MODE) || String.valueOf(myPreferences.getUser().getMode()).equalsIgnoreCase(Constants.FB_MODE)) {
            password.setVisibility(View.GONE);
            confirm_password.setVisibility(View.GONE);
        }
        phone_number = (MaterialEditText) rootView.findViewById(R.id.phone_number);
        txt_home_latitude = (TextView) rootView.findViewById(R.id.home_latitude);
        txt_home_longitude = (TextView) rootView.findViewById(R.id.home_longitude);
        txt_work_latitude = (TextView) rootView.findViewById(R.id.work_latitude);
        txt_work_longitude = (TextView) rootView.findViewById(R.id.work_longitude);
        address = (EditText) rootView.findViewById(R.id.txt_address);
        progressView = (ProgressView) rootView.findViewById(R.id.progressView);
        updateBtn = (com.rey.material.widget.Button) rootView.findViewById(R.id.update);
        home_gps = (ImageView) rootView.findViewById(R.id.home_gps);
        home_picker = (ImageView) rootView.findViewById(R.id.home_picker);
        work_gps = (ImageView) rootView.findViewById(R.id.work_gps);
        work_picker = (ImageView) rootView.findViewById(R.id.work_picker);
        home_gps.setOnClickListener(this);
        home_picker.setOnClickListener(this);
        work_gps.setOnClickListener(this);
        work_picker.setOnClickListener(this);
        updateBtn.setOnClickListener(this);

        setUpUser();

    }

    public void setUpUser() {

        myPreferences = new Preferences(mActivity.getApplicationContext());
        User myUser = myPreferences.getUser();

        name.setText(myUser.getName());
        username.setText(myUser.getEmail());
        password.setText(myUser.getPassword());
        confirm_password.setText(myUser.getPassword());
        phone_number.setText(myUser.getPhone_number());
        txt_home_latitude.setText(myUser.getHomeLatitude());
        txt_home_longitude.setText(myUser.getHomeLongitude());
        txt_work_latitude.setText(myUser.getWork_latitude());
        txt_work_longitude.setText(myUser.getWork_longitude());
        address.setText(myUser.getAddress());


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.setting_menu, menu);
        ((MainActivity) mActivity).setActionBarTitle("Settings");
        ((MainActivity) mActivity).setNavigationItemSelected(2);
    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.setting_menu, menu);
*//*
        MenuItem item1 = menu.findItem(R.id.actionbar_item);
        MenuItemCompat.setActionView(item1, R.layout.notification_update_count_layout);
        menuBadgeView = (RelativeLayout) MenuItemCompat.getActionView(item1);
        menuBadgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedVehicles != null && sharedVehicles.size() > 0) {
                    showSharedVehicleListDialog();
                } else {
                    Utility.showAlert(mActivity, mActivity.getResources().getString(R.string.no_shared_vehicles));
                }
            }
        });
        sharedCount = (android.widget.TextView) menuBadgeView.findViewById(R.id.badge_count);
        sharedCount.setText("" + sharedVehiclesCount);*//*
        //super.onCreateOptionsMenu(menu, inflater);
    }*/


    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.actionbar_item:
                if (sharedVehicles.size() > 0)
                    showSharedVehicleListDialog();
                break;

            default:
                break;


        }


        return super.onOptionsItemSelected(item);

    }*/


    /*private void showSharedVehicleListDialog() {
        Dialog sharedVehicleDialog = new Dialog(mActivity);
        sharedVehicleDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sharedVehicleDialog.setContentView(R.layout.shared_vehicle_list_dialg);
        ListView shareVehicleListView = (ListView) sharedVehicleDialog.findViewById(R.id.shared_bus_list);
        deleteSharedVehicleBtn = (ImageView) sharedVehicleDialog.findViewById(R.id.delete);
        deleteSharedVehicleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = sharedVehicles.size();
                for (int i = 0; i < sharedVehicles.size(); i++) {
                    Vehicle vehicle = sharedVehicles.get(i);
                    sharedVehicles.remove(vehicle);
                    appDatabaseHelper.deleteVehicleFromSharedList(vehicle);
                }
                sharedVehicleListAdapter.notifyDataSetChanged();
            }
        });
        sharedVehicleListAdapter = new SharedVehicleListAdapter(mActivity, this, sharedVehicles);
        shareVehicleListView.setAdapter(sharedVehicleListAdapter);
        sharedVehicleDialog.show();
    }*/


    private void updateUser() {

        str_user_id = myPreferences.getUser().getUser_id();
        str_name = name.getText().toString();
        str_email_id = username.getText().toString();
        str_password = password.getText().toString();
        str_confirm_password = confirm_password.getText().toString();
        str_phone_number = phone_number.getText().toString();
        str_address = address.getText().toString();
        str_home_latitude = txt_home_latitude.getText().toString();
        str_home_longitude = txt_home_longitude.getText().toString();
        str_work_latitude = txt_work_latitude.getText().toString();
        str_work_longitude = txt_work_longitude.getText().toString();
        myDeviceId = myPreferences.getGCM_TOKEN();

/**/

        if (str_password.equals(str_confirm_password)) {

            String updateUserUrl = WebService.UPDATE_USER_URL + "user_id=" + str_user_id + "&name=" + str_name + "&password=" + str_password + "&phone_number=" + str_phone_number +
                    "&latitude=" + str_home_latitude + "&longitude=" + str_home_longitude +
                    "&home_latitude=" + str_home_latitude + "&home_longitude=" + str_home_longitude +
                    "&work_latitude=" + str_work_latitude + "&work_longitude=" + str_work_longitude +
                    "&address=" + str_address;

            webService.updateUser(mActivity, updateUserUrl);
        } else {
            Utility.showToast(mActivity, mActivity.getResources().getString(R.string.please_confirm_password));
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;

    }

    @Override
    public void onHiddenChanged(boolean hidden) {

        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.home_picker:
                openHomePicker();
                break;

            case R.id.home_gps:
                if (Utility.isGpsEnabled(mActivity)) {

                    Utility.showLocationLoadingProgressBar(mActivity);
                    getLocationUsingGps(Constants.HOME_GPS_REQUEST);

                } else {
                    openGpsIntentForHome();
                }
                break;

            case R.id.work_picker:
                openWorkPicker();
                break;

            case R.id.work_gps:
                if (Utility.isGpsEnabled(mActivity)) {

                    Utility.showLocationLoadingProgressBar(mActivity);
                    getLocationUsingGps(Constants.WORK_GPS_REQUEST);

                } else {
                    openGpsIntentForWork();
                }
                break;

            case R.id.update:
                if (Utility.isConnectingToInternet(mActivity)) {
                    showProgress();
                    updateUser();
                } else {
                    Utility.showToast(mActivity, mActivity.getResources().getString(R.string.no_internet));
                }
                break;

            default:
                break;
        }

    }

    private void getLocationUsingGps(int request) {
        GPSTracker gpsTracker = new GPSTracker(mActivity);
        if (gpsTracker.canGetLocation()) {
            Utility.stopLocationLoadingProgressBar();
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            if (request == Constants.HOME_GPS_REQUEST) {
                str_home_latitude = String.valueOf(latitude);
                str_home_longitude = String.valueOf(longitude);
                txt_home_latitude.setText(str_home_latitude);
                txt_home_longitude.setText(str_home_longitude);
            } else if (request == Constants.WORK_GPS_REQUEST) {
                str_work_latitude = String.valueOf(latitude);
                str_work_longitude = String.valueOf(longitude);
                txt_work_latitude.setText(str_work_latitude);
                txt_work_longitude.setText(str_work_longitude);
            }

        } else {
            Utility.showToast(mActivity, mActivity.getString(R.string.location_not_found));
        }
    }

    private void showProgress() {
        updateBtn.setEnabled(false);
        progressView.setVisibility(View.VISIBLE);
    }

    private void stopProgress() {
        updateBtn.setEnabled(true);
        progressView.setVisibility(View.GONE);
    }

    private void openHomePicker() {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            mActivity.startActivityForResult(builder.build(mActivity), Constants.HOME_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void openWorkPicker() {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            mActivity.startActivityForResult(builder.build(mActivity), Constants.WORK_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (updateUserResponseReceiver == null) {
            updateUserResponseReceiver = new UpdateUserResponseReceiver();
            mActivity.registerReceiver(updateUserResponseReceiver, new IntentFilter(WebService.INTENT_FILTER_UPDATE_USER_RESPONSE));
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (updateUserResponseReceiver != null) {
            getActivity().unregisterReceiver(updateUserResponseReceiver);
            updateUserResponseReceiver = null;
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.HOME_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                Place place = PlacePicker.getPlace(data, mActivity);
                String toastMsg = String.format("Place: %s", place.getName());

                str_home_latitude = String.valueOf(place.getLatLng().latitude);
                str_home_longitude = String.valueOf(place.getLatLng().longitude);
                txt_home_latitude.setText(str_home_latitude);
                txt_home_longitude.setText(str_home_longitude);


            }
        } else if (requestCode == Constants.WORK_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                Place place = PlacePicker.getPlace(data, mActivity);
                String toastMsg = String.format("Place: %s", place.getName());

                str_work_latitude = String.valueOf(place.getLatLng().latitude);
                str_work_longitude = String.valueOf(place.getLatLng().longitude);
                txt_work_latitude.setText(str_work_latitude);
                txt_work_longitude.setText(str_work_longitude);


            }
        }

    }

    public void showSharedVehicleDeleteBtn(int count) {
        if (count > 0) {
            deleteSharedVehicleBtn.setVisibility(View.VISIBLE);
        } else {
            deleteSharedVehicleBtn.setVisibility(View.GONE);
        }
    }

    public void refreshUserDetails(String res) {
        updateUserFromResponse(res);
        setUpUser();
    }


    class UpdateUserResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            stopProgress();
            String response = intent.getStringExtra("response");
            progressDialog.dismiss();

            if (response.contains("user_id")) {
                Utility.showToast(mActivity, mActivity.getResources().getString(R.string.user_updated_success));
                updateUserFromResponse(response);
            } else {
                Utility.showToast(mActivity, mActivity.getResources().getString(R.string.user_updated_failed));

            }
        }

    }

    private void updateUserFromResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject statusObject = jsonObject.getJSONObject("status");
            str_user_id = statusObject.getString("user_id");
            str_name = statusObject.getString("name");
            str_email_id = statusObject.getString("email");
            //str_password = statusObject.getString("password");
            str_phone_number = statusObject.getString("phone_number");
            str_home_latitude = statusObject.getString("home_latitude");
            str_home_longitude = statusObject.getString("home_longitude");
            str_work_latitude = statusObject.getString("work_latitude");
            str_work_longitude = statusObject.getString("work_longitude");
            str_address = statusObject.getString("address");
            myDeviceId = myPreferences.getGCM_TOKEN();

            User myUser = myPreferences.getUser();
            myUser.setName(str_name);
            myUser.setEmail(str_email_id);
            myUser.setPassword(str_password);
            myUser.setPhone_number(str_phone_number);
            myUser.setHomeLatitude(str_home_latitude);
            myUser.setHomeLongitude(str_home_longitude);
            myUser.setWork_latitude(str_work_latitude);
            myUser.setWork_longitude(str_work_longitude);
            myUser.setAddress(str_address);
            myPreferences.updateUser(myUser);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void openGpsIntentForHome() {
        IS_GPS_FOR_HOME_REQUESTED = true;
        String action = android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        startActivityForResult(new Intent(action), Constants.GPS_REQUEST);
    }

    public void openGpsIntentForWork() {
        IS_GPS_FOR_WORK_REQUESTED = true;
        String action = android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        startActivityForResult(new Intent(action), Constants.GPS_REQUEST);
    }

    public void getUserDetails() {
        IS_GPS_FOR_WORK_REQUESTED = true;
        String action = android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        startActivityForResult(new Intent(action), Constants.GPS_REQUEST);
    }

}
