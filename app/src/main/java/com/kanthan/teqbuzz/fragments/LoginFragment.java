package com.kanthan.teqbuzz.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.kanthan.teqbuzz.AppController;
import com.kanthan.teqbuzz.MainActivity;
import com.kanthan.teqbuzz.R;
import com.kanthan.teqbuzz.models.User;
import com.kanthan.teqbuzz.utilities.Constants;
import com.kanthan.teqbuzz.utilities.Preferences;
import com.kanthan.teqbuzz.utilities.Utility;
import com.kanthan.teqbuzz.utilities.WebService;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final int ERROR_DIALOG_REQUEST_CODE = 2;
    View rootView;
    MaterialEditText username, password;
    Button google_login, fb_login;
    com.rey.material.widget.Button login;
    ProgressView progressView, googleProgressView;

    private Context mContext;
    private String TAG = "LoginFragment";
    String str_name, str_username, str_email_id, str_password, str_image_url, str_facebook_id, str_google_id, str_login_type;
    private String myDeviceId;
    private Preferences myPreferences;
    private String login_err_msg;

    private String mode;
    private Dialog progressDialog;
    Utility utility;
    private Activity mActivity;

    TextView tvForgotPwd;
    //Facebook
    private CallbackManager callbackManager;
    public static LoginManager loginManager;
    private WebService webService;
    private String str_temp_latitude, str_temp_longitude;
    private BroadcastReceiver loginResponseReceiver;
    User user;
    public static int RC_SIGN_IN = 0;
    private MainActivity mainActivity;

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
        rootView = inflater.inflate(R.layout.fragment_login, container, false);

        username = (MaterialEditText) rootView.findViewById(R.id.username);
        password = (MaterialEditText) rootView.findViewById(R.id.password);

        login = (com.rey.material.widget.Button) rootView.findViewById(R.id.login);
        google_login = (Button) rootView.findViewById(R.id.google_login);
        fb_login = (Button) rootView.findViewById(R.id.fb_login);
        tvForgotPwd = (TextView) rootView.findViewById(R.id.tv_ForgotPwd);
        tvForgotPwd.setOnClickListener(this);
        google_login.setOnClickListener(this);
        fb_login.setOnClickListener(this);
        login.setOnClickListener(this);
        progressView = (ProgressView) rootView.findViewById(R.id.progressView);
        googleProgressView = (ProgressView) rootView.findViewById(R.id.googleProgressView);
        myPreferences = new Preferences(mActivity.getApplicationContext());

        //Facebook setup starts
        /*FacebookSdk.sdkInitialize(mActivity);
        callbackManager = CallbackManager.Factory.create();*/
        Utility.getFacebokKeyHash(mActivity);

       /* loginManager = LoginManager.getInstance();

        FacebookCallback<LoginResult> mLoginResultFacebookCallback =
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Utility.showToast(mActivity, "fb login success");
                        GraphRequest request = GraphRequest.newMeRequest(
                                AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        Log.v("LoginActivity", response.toString());

                                        try {
                                            //and fill them here like so.
                                            str_facebook_id = object.getString("id");
                                            str_email_id = object.getString("email");
                                            str_name = object.getString("name");

                                            loginWithFacebook(str_name, str_email_id, str_facebook_id);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Utility.showToast(mActivity, "fb login cancel");
                        int x = 2 + 5;
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        int x = 2 + 5;
                        Utility.showToast(mActivity, "fb login error");
                    }
                };


        loginManager.registerCallback(callbackManager, mLoginResultFacebookCallback);
*/

        //Facebook setup ends

        utility = new Utility(mContext);
        progressDialog = utility.createProgressBar();

        return rootView;
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
          /*  case R.id.signup:
                ((MainActivity) mActivity).setFragment(6, null);
                break;*/

            default:
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mainActivity = (MainActivity) mActivity;

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

            case R.id.login:
                showProgress();
                login.setEnabled(false);
                login(Constants.NORMAL_MODE);

                break;

            case R.id.google_login:
                //showGoogleProgress();
                loginWithGoogle();

                break;

            case R.id.fb_login:

                showProgress();
                loginWithFacebook();
                // openPicker();

                break;

            case R.id.tv_ForgotPwd:
                showResetPwdDialog();

                break;
            default:
                break;
        }

    }

    private void showResetPwdDialog() {
        final Dialog resetPwdDialog = new Dialog(mActivity);
        resetPwdDialog.setTitle(mActivity.getResources().getString(R.string.reset_pwd));

        resetPwdDialog.setContentView(R.layout.reset_pwd_dialog);
        final EditText etResetEmail = (EditText) resetPwdDialog.findViewById(R.id.et_emailId);
        com.rey.material.widget.Button btnReset = (com.rey.material.widget.Button) resetPwdDialog.findViewById(R.id.reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etResetEmail.getText().toString();
                webService.resetPassword(email, LoginFragment.this);
                resetPwdDialog.dismiss();
            }
        });
        resetPwdDialog.show();
    }

    public void showProgress() {
        progressView.setVisibility(View.VISIBLE);
    }

    public void stopProgress() {
        progressView.setVisibility(View.GONE);
    }

    public void showGoogleProgress() {
        googleProgressView.setVisibility(View.VISIBLE);
    }

    public void stopGoogleProgress() {
        googleProgressView.setVisibility(View.GONE);
    }

    private void loginWithFacebook() {


      /*  AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken == null)
            loginManager.logInWithReadPermissions(mActivity, Arrays.asList("public_profile", "user_friends", "email"));
*/

        ((MainActivity) mActivity).loginWithFacebook();
    }


    private void login(String normal_mode) {

        str_username = username.getText().toString();
        str_email_id = username.getText().toString();
        str_password = password.getText().toString();
        str_login_type = Constants.NORMAL_MODE;
        str_facebook_id = "";
        str_google_id = "";
        myDeviceId = myPreferences.getGCM_TOKEN();

        user = myPreferences.getUser();
        str_temp_latitude = user.getTemp_latitude();
        str_temp_longitude = user.getTemp_longitude();


        String loginUrl = WebService.LOGIN_URL + "&name=" + str_username + "&email=" + str_email_id + "&password=" + str_password +
                "&latitude=" + str_temp_latitude + "&longitude=" + str_temp_longitude + "&device_id=" + myDeviceId + "&login_type=" + str_login_type +
                "&facebook_id=" + str_facebook_id + "&google_id=" + str_google_id;


        webService.login(mActivity, loginUrl);

    }

    private void loginWithGoogle(String username, String email_id, String google_id) {

        str_username = username;
        str_email_id = email_id;
        str_password = "";
        str_login_type = Constants.GOOGLE_MODE;
        str_facebook_id = "";
        str_google_id = google_id;
        myDeviceId = myPreferences.getGCM_TOKEN();

        str_temp_latitude = myPreferences.getUser().getTemp_latitude();
        str_temp_longitude = myPreferences.getUser().getTemp_longitude();


        String loginUrl = WebService.LOGIN_URL + "&name=" + str_username + "&email=" + str_email_id + "&password=" + str_password +
                "&latitude=" + str_temp_latitude + "&longitude=" + str_temp_longitude + "&device_id=" + myDeviceId + "&login_type=" + str_login_type +
                "&facebook_id=" + str_facebook_id + "&google_id=" + str_google_id;

        webService.login(mActivity, loginUrl);

    }


    public void loginWithFacebook(String username, String email_id, String facebook_id) {

        str_username = username;
        str_email_id = email_id;
        str_password = "";
        str_login_type = Constants.FB_MODE;
        str_facebook_id = facebook_id;
        str_google_id = "";
        myDeviceId = myPreferences.getGCM_TOKEN();
        str_temp_latitude = myPreferences.getUser().getTemp_latitude();
        str_temp_longitude = myPreferences.getUser().getTemp_longitude();


        String loginUrl = WebService.LOGIN_URL + "&name=" + str_username + "&email=" + str_email_id + "&password=" + str_password +
                "&latitude=" + str_temp_latitude + "&longitude=" + str_temp_longitude + "&device_id=" + myDeviceId + "&login_type=" + str_login_type +
                "&facebook_id=" + str_facebook_id + "&google_id=" + str_google_id;

        webService.login(mActivity, loginUrl);

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

    private void loginWithGoogle() {

        mainActivity.loginWithGoogle(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (loginResponseReceiver == null) {
            loginResponseReceiver = new LoginResponseReceiver();
            mActivity.registerReceiver(loginResponseReceiver, new IntentFilter(WebService.INTENT_FILTER_LOGIN_RESPONSE));
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (loginResponseReceiver != null) {
            mActivity.unregisterReceiver(loginResponseReceiver);
            loginResponseReceiver = null;
        }
    }


    public boolean isSocialLoginValidated() {

        return true;
    }

    public void onGoogleConnected(Person currentPerson, String gmail_id) {

        stopGoogleProgress();
        showProgress();
        str_name = currentPerson.getDisplayName();
        str_email_id = gmail_id;
        str_google_id = currentPerson.getId();
        str_name = currentPerson.getDisplayName();
        loginWithGoogle(str_name, str_email_id, str_google_id);

    }

    public void showGoogleLoginError() {

        Utility.showToast(mActivity, "google login failed");
    }

    public void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            stopGoogleProgress();
            showProgress();
            str_name = acct.getDisplayName();
            str_email_id = acct.getEmail();
            str_google_id = acct.getId();
            loginWithGoogle(str_name, str_email_id, str_google_id);
        } else {
            // Signed out, show unauthenticated UI.

        }
    }

    public void onPasswordReset(String s) {
        Utility.showSnack(mActivity, mActivity.getResources().getString(R.string.password_reset));
    }


    class LoginResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            login.setEnabled(true);
            String response = intent.getStringExtra("response");
            stopProgress();

            if (response.contains("Invalid")) {
                Utility.showToast(mActivity, mActivity.getResources().getString(R.string.invalid_both));
            } else if (response.contains("error")) {
                Utility.showToast(mActivity, mActivity.getString(R.string.some_error_occured));
            } else {

                updateUserFromResponse(response);
                Utility.showToast(mActivity, mActivity.getResources().getString(R.string.login_successfully));
                // make loginned flag true
                myPreferences.setLoginned(true);
                moveToMapBusFragment();
                updateNavigationDrawer(true);
                // show home icon
                ((MainActivity) mActivity).showHomeWorkIcon(View.VISIBLE);

                //getFragmentManager().beginTransaction().remove(LoginFragment.this).commit();


            }
        }

    }

    private void updateUserFromResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject status = jsonObject.getJSONObject("status");
            String user_id = status.getString("user_id");
            String name = status.getString("name");
            String email = status.getString("email");
            //String password = status.getString("password");
            String phone_number = status.getString("phone_number");
            String home_latitude = status.getString("home_latitude");
            String home_longitude = status.getString("home_longitude");
            String work_latitude = status.getString("work_latitude");
            String work_longitude = status.getString("work_longitude");
            String address = status.getString("address");
            String image = status.getString("image");
            String facebook_id = status.getString("facebook_id");
            String google_id = status.getString("google_id");
            //String login_type = status.getString("login_type");
            String date_added = status.getString("date_added");
            String user_status = "";

            User user = myPreferences.getUser();
            user.setUser_id(user_id);
            user.setName(name);
            user.setEmail(email);
            user.setPassword(str_password);
            user.setPhone_number(phone_number);
            user.setHomeLatitude(home_latitude);
            user.setHomeLongitude(home_longitude);
            user.setWork_longitude(work_longitude);
            user.setWork_latitude(work_latitude);
            user.setAddress(address);
            //user.setMode(Integer.parseInt(str_login_type));
            myPreferences.updateUser(user);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateNavigationDrawer(boolean isLoginned) {
        ((MainActivity) mActivity).updateNavigationDrawer(true);
    }

    private void moveToMapBusFragment() {

        //((MainActivity) (mActivity)).setFragment(1, null);
        mActivity.onBackPressed();
        BusMapFragment busMapFragment = ((MainActivity) mActivity).getBusMapFragment();
        if (busMapFragment != null) {
            busMapFragment.checkAndAddPendingSharedVehicles();
            //busMapFragment.runFavouritesBusVehicleService();
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                Place place = PlacePicker.getPlace(data, mActivity);
                String toastMsg = String.format("Place: %s", place.getName());

                str_temp_latitude = String.valueOf(place.getLatLng().latitude);
                str_temp_longitude = String.valueOf(place.getLatLng().longitude);

            }
        } else {
            //callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        // make sure to initiate connection


    }

    @Override
    public void onStop() {
        super.onStop();
        // disconnect api if it is connected

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       /* mGoogleApiClient.stopAutoManage(getActivity());*/
        //mGoogleApiClient.disconnect();
    }
}

