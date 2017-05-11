package com.kanthan.teqbuzz.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.kanthan.teqbuzz.MainActivity;
import com.kanthan.teqbuzz.R;
import com.kanthan.teqbuzz.service.GPSTracker;
import com.kanthan.teqbuzz.utilities.Constants;
import com.kanthan.teqbuzz.utilities.Preferences;
import com.kanthan.teqbuzz.utilities.Utility;
import com.kanthan.teqbuzz.utilities.WebService;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.TextView;


public class RegisterFragment extends Fragment implements View.OnClickListener {

    View rootView;
    MaterialEditText name, username, password, confirm_password, phone_number;
    EditText address;
    com.rey.material.widget.Button signup;
    TextView txt_latitude, txt_longitude;
    ImageView btn_gps, btn_picker;

    // Google client to interact with Google API

    private Context mContext;
    private String TAG = "LoginFragment";
    String str_name, str_email_id, str_password, str_confirm_password, str_phone_number, str_address, str_latitude, str_longitude, str_login_type, str_facebook_id, str_google_id, myDeviceId;
    private Preferences myPreferences;
    private String login_err_msg;

    private String mode;
    private Dialog progressDialog;
    Utility utility;
    private Activity mActivity;

    ProgressView progressView;
    private WebService webService;
    private String str_temp_latitude, str_temp_longitude;
    private BroadcastReceiver registerResponseReceiver;
    private AuthenticationFragment authenticationFragment;

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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_signup, container, false);

        initViews(rootView);


        myPreferences = new Preferences(mActivity.getApplicationContext());
        utility = new Utility(mContext);
        progressDialog = utility.createProgressBar();


        return rootView;
    }

    private void initViews(View rootView) {

        name = (MaterialEditText) rootView.findViewById(R.id.name);
        username = (MaterialEditText) rootView.findViewById(R.id.username);
        password = (MaterialEditText) rootView.findViewById(R.id.password);
        confirm_password = (MaterialEditText) rootView.findViewById(R.id.confirm_password);
        phone_number = (MaterialEditText) rootView.findViewById(R.id.phone_number);
        txt_latitude = (TextView) rootView.findViewById(R.id.home_latitude);
        txt_longitude = (TextView) rootView.findViewById(R.id.home_longitude);
        address = (EditText) rootView.findViewById(R.id.txt_address);
        progressView = (ProgressView) rootView.findViewById(R.id.progressView);
        signup = (com.rey.material.widget.Button) rootView.findViewById(R.id.signup);
        btn_gps = (ImageView) rootView.findViewById(R.id.btn_gps);
        btn_picker = (ImageView) rootView.findViewById(R.id.btn_picker);
        btn_gps.setOnClickListener(this);
        btn_picker.setOnClickListener(this);
        signup.setOnClickListener(this);

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
        inflater.inflate(R.menu.menu_dummy, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

           /* case R.id.login:
                moveToLoginFragment();
                break;*/

            default:
                break;


        }


        return super.onOptionsItemSelected(item);

    }

    private void moveToLoginFragment() {
        //((MainActivity) mActivity).getSupportFragmentManager().popBackStack();
        //((MainActivity) (mActivity)).setFragment(1, null);
        //((MainActivity) (mActivity)).onBackPressed();
        authenticationFragment.moveToLoginFragment();
    }

    private void signUp(String normal_mode) {

        str_name = name.getText().toString();
        str_email_id = username.getText().toString();
        str_password = password.getText().toString();
        str_confirm_password = confirm_password.getText().toString();
        str_phone_number = "";
        str_address = "";
        str_login_type = Constants.NORMAL_MODE;
        str_facebook_id = "";
        str_google_id = "";
        myDeviceId = myPreferences.getGCM_TOKEN();


        if (str_password.equals(str_confirm_password)) {

            String signUpUrl = WebService.SIGNUP_URL + "name=" + str_name + "&email=" + str_email_id + "&password=" + str_password +
                    "&phone_number=" + str_phone_number + "&latitude=" + str_latitude + "&longitude=" + str_longitude +
                    "&address=" + str_address + "&facebook_id=" + str_facebook_id + "&twitter_id=" + str_google_id +
                    "&login_type=" + str_login_type + "&device_id=" + myDeviceId;

            webService.signUp(mActivity, signUpUrl);
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

            case R.id.btn_picker:
                openPicker();
                break;

            case R.id.btn_gps:
                if (Utility.isGpsEnabled(mActivity)) {

                    Utility.showLocationLoadingProgressBar(mActivity);
                    getLocationUsingGps();

                } else {
                    openGpsIntent();
                }
                break;

            case R.id.signup:
                if (Utility.isConnectingToInternet(mActivity)) {
                    showProgress();
                    signUp(Constants.NORMAL_MODE);
                } else {
                    Utility.showToast(mActivity, mActivity.getResources().getString(R.string.no_internet));
                }
                break;

            default:
                break;
        }

    }

    private void getLocationUsingGps() {
        GPSTracker gpsTracker = new GPSTracker(mActivity);
        if (gpsTracker.canGetLocation()) {
            Utility.stopLocationLoadingProgressBar();
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            str_latitude = String.valueOf(latitude);
            str_longitude = String.valueOf(longitude);
            txt_latitude.setText(str_latitude);
            txt_longitude.setText(str_longitude);

        } else {
            Utility.showToast(mActivity, mActivity.getString(R.string.location_not_found));
        }
    }

    private void showProgress() {
        signup.setEnabled(false);
        progressView.setVisibility(View.VISIBLE);
    }

    private void stopProgress() {
        signup.setEnabled(true);
        progressView.setVisibility(View.GONE);
    }

    private void openGpsIntent() {
        ((MainActivity) mActivity).openGpsIntent();

    }

    private void openPicker() {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            mActivity.startActivityForResult(builder.build(mActivity), Constants.PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (registerResponseReceiver == null) {
            registerResponseReceiver = new RegisterResponseReceiver();
            mActivity.registerReceiver(registerResponseReceiver, new IntentFilter(WebService.INTENT_FILTER_SIGNUP_RESPONSE));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (registerResponseReceiver != null) {
            getActivity().unregisterReceiver(registerResponseReceiver);
            registerResponseReceiver = null;
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                Place place = PlacePicker.getPlace(data, mActivity);
                String toastMsg = String.format("Place: %s", place.getName());

                str_latitude = String.valueOf(place.getLatLng().latitude);
                str_longitude = String.valueOf(place.getLatLng().longitude);
                txt_latitude.setText(str_latitude);
                txt_longitude.setText(str_longitude);


            }
        } else {

        }

    }


    class RegisterResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            stopProgress();
            String response = intent.getStringExtra("response");
            progressDialog.dismiss();

            if (response.contains("Exist")) {
                Utility.showToast(mActivity, mActivity.getResources().getString(R.string.user_already_exists));
            } else if (response.contains("error")) {
                Utility.showToast(mActivity, mActivity.getResources().getString(R.string.some_error_occured));
            } else {
                Utility.showToast(mActivity, mActivity.getResources().getString(R.string.signup_successfully));
                myPreferences.setRegistered(true);
                //getFragmentManager().beginTransaction().remove(RegisterFragment.this).commit();
                moveToLoginFragment();

            }
        }

    }

    public void setAuthenticationFragment(AuthenticationFragment authenticationFragment) {
        this.authenticationFragment = authenticationFragment;
    }

}
