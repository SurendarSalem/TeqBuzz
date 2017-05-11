package com.kanthan.teqbuzz;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.kanthan.teqbuzz.adapter.NavMenuAdapter;
import com.kanthan.teqbuzz.fragments.AboutUsFragment;
import com.kanthan.teqbuzz.fragments.AuthenticationFragment;
import com.kanthan.teqbuzz.fragments.BusMapFragment;
import com.kanthan.teqbuzz.fragments.LoginFragment;
import com.kanthan.teqbuzz.fragments.RegisterFragment;
import com.kanthan.teqbuzz.fragments.RouteListFragment;
import com.kanthan.teqbuzz.fragments.SettingsFragment;
import com.kanthan.teqbuzz.models.NavItem;
import com.kanthan.teqbuzz.models.User;
import com.kanthan.teqbuzz.models.Vehicle;
import com.kanthan.teqbuzz.onebusaway.OBAArrivalDepartureEntity;
import com.kanthan.teqbuzz.onebusaway.OBAJsonParser;
import com.kanthan.teqbuzz.onebusaway.OBARouteEntity;
import com.kanthan.teqbuzz.onebusaway.OBAStopEntity;
import com.kanthan.teqbuzz.onebusaway.OBAStopListEntity;
import com.kanthan.teqbuzz.onebusaway.OBATripsForLocationEntity;
import com.kanthan.teqbuzz.onebusaway.OBATripsForRouteEntity;
import com.kanthan.teqbuzz.onebusaway.OBAWebService;
import com.kanthan.teqbuzz.service.GPSTracker;
import com.kanthan.teqbuzz.utilities.Constants;
import com.kanthan.teqbuzz.utilities.Preferences;
import com.kanthan.teqbuzz.utilities.SingleShotLocationProvider;
import com.kanthan.teqbuzz.utilities.Utility;
import com.kanthan.teqbuzz.utilities.WebService;
import com.rey.material.widget.Button;
import com.rey.material.widget.SnackBar;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        GoogleApiClient.ConnectionCallbacks, ResultCallback<People.LoadPeopleResult>,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {


    public static final int CHAT_REQUEST = 103;
    private static final int RC_SIGN_IN = 0;
    public static int IS_IN_PAGE = 0;
    public static int selectedPosition = 0;
    public static boolean isForLogin;
    public static boolean mMapIsTouched = false;
    public static boolean isShareCancelBtnClicked;
    public Toolbar toolbar;
    ActionBar actionBar;
    private DrawerLayout Drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    String[] navMenus;
    int[] navIcons;
    NavMenuAdapter adapter;
    ListView navListView;
    FragmentManager fragmentManager;
    String userName, avatar_url;
    SharedPreferences preferences;
    ArrayList<NavItem> navItems, loginItems, logoutItems;
    private NavItem loginItem, logoutItem, settingsItem, myActivityItem;
    private TextView txt_username;
    private int save;
    public static TextView toolbar_title;
    FrameLayout toolbar_shadow;
    boolean isRegistered, isLoginned;
    Preferences myPreferences;
    private ViewGroup header;
    public static Context context;
    String Home, Login, My_Favourites, Settings, Help_About, Logout, sharedBuses;
    private boolean isItemClicked;
    private LocationManager locationManager;
    private long LOCATION_REFRESH_TIME = 1000;
    private float LOCATION_REFRESH_DISTANCE = 1;
    private BroadcastReceiver getVehicleListResponseReceiver;
    String str_latitude = "12.7200", str_longitude = "77.8200", str_distance = "100", limit = "10", offset = "0";
    private ProgressDialog locationProgressDialog;
    private ArrayList<NavItem> tempNavItems;
    private boolean isFavouriteSelected;
    private CallbackManager callbackManager;
    User myUser;
    private boolean isGetVehicleServiceRunning;


    private boolean mSignInClicked;
    private boolean mIntentInProgress;
    // contains all possible error codes for when a client fails to connect to
    // Google Play services
    private ConnectionResult mConnectionResult;
    private LoginFragment loginFragment;
    private String mode;
    private boolean isLoginClicked;
    View navListHeaderView;
    LayoutInflater layoutInflater;
    private Transformation headerImageTranformation;
    public static int NAVIGATION_ITEM_SELECTED_INDEX;
    private GPSTracker gpsTracker;

    /*fsed location*/
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    private boolean isGPSLocationCalculated;
    private LoginManager loginManager;
    private FacebookCallback<LoginResult> mLoginResultFacebookCallback;
    boolean isCmgFromDeepLink;
    private Intent activityIntent;
    String sharedVehicleId, sharedVehicleLineNumber, sharedVehicleAgency;
    private AsyncTask<Void, Void, Void> gpsAsyncTask;
    private boolean isLogoutClicked;
    private boolean isUserClickedHome;
    private boolean IS_INTERNET_REQUESTED;
    private Dialog internetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utility.getFacebokKeyHash(this);
        gpsTracker = new GPSTracker(this);
        gpsTracker.setActivity(this);
        activityIntent = getIntent();
        isCmgFromDeepLink = activityIntent.getBooleanExtra(Constants.IS_COMING_FROM_DEEP_LINKING, false);
        if (isCmgFromDeepLink) {
            sharedVehicleId = activityIntent.getStringExtra(Constants.SHARED_VEHICLE_ID);
            sharedVehicleLineNumber = activityIntent.getStringExtra(Constants.SHARED_VEHICLE_LINE_NUMBER);
            sharedVehicleAgency = activityIntent.getStringExtra(Constants.SHARED_VEHICLE_AGENCY);
            isShareCancelBtnClicked = false;
        }
        /*
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/Roboto-Italic.ttf", true);*/
        AppController.getInstance().trackScreenView("Main Screen");

        initUI();

        initialiseLocationManager();
        // set last used location
        if (Utility.isConnectingToInternet(this)) {
            setLastUsedLocation();
        } else {
            if (internetSnackBar == null) {
                internetSnackBar = Utility.showSnackForInternet(MainActivity.this, getResources().getString(R.string.no_internet), MainActivity.this);
            }
            internetSnackBar.show(MainActivity.this);
            IS_INTERNET_SNACKBAR_SHOWN = true;
            //showInternetConnectionDialog();
        }

        registerGetVehicleListResponseReceiver();

        //initiateGoogleApiClient();

        getSupportFragmentManager().addOnBackStackChangedListener(getListener());


        //checkIsLocationAvailable();

        initalizeSocialLoginSetup();


        //testJson();
        //testRouteListOBA();

    }

    private void showInternetConnectionDialog() {
        if (internetAlertDialog == null) {
            internetAlertDialog = new Dialog(this);
            internetAlertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            internetAlertDialog.setContentView(R.layout.internet_connection_dialog);
            Button exitBtn = (Button) internetAlertDialog.findViewById(R.id.exit);
            Button connectBtn = (Button) internetAlertDialog.findViewById(R.id.connect);
            connectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    internetAlertDialog.dismiss();
                    openInternetSettings();
                }
            });
            exitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    internetAlertDialog.dismiss();
                    finish();
                }
            });

            internetAlertDialog.setCancelable(false);
        }
        if (internetAlertDialog != null && !IS_INTERNET_SNACKBAR_SHOWN)
            internetAlertDialog.show();
    }

    private void hideInternetConnectionDialog() {
        if (internetAlertDialog != null)
            internetAlertDialog.dismiss();
    }

    public void openInternetSettings() {
        IS_INTERNET_REQUESTED = true;
        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        activityIntent = getIntent();
        String action = intent.getAction();
        String data = intent.getDataString();
        /*if (Intent.ACTION_VIEW.equals(action) && data != null) {
            sharedVehicleId = data.substring(data.lastIndexOf("/") + 1);
            sharedVehicleId = sharedVehicleId.replace("vehicleId=", "");
            *//*if (myPreferences.isLoginned()) {
                addVehicleToSharedList(sharedVehicleId);
            }*//*
            isCmgFromDeepLink = true;
        }*/
        isCmgFromDeepLink = activityIntent.getBooleanExtra(Constants.IS_COMING_FROM_DEEP_LINKING, false);
        if (isCmgFromDeepLink) {
            sharedVehicleId = activityIntent.getStringExtra(Constants.SHARED_VEHICLE_ID);
            sharedVehicleLineNumber = activityIntent.getStringExtra(Constants.SHARED_VEHICLE_LINE_NUMBER);
            sharedVehicleAgency = activityIntent.getStringExtra(Constants.SHARED_VEHICLE_AGENCY);
            isShareCancelBtnClicked = false;
        }
        /*
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/Roboto-Italic.ttf", true);*/
        AppController.getInstance().trackScreenView("Main Screen");


        initialiseLocationManager();
        // set last used location
        setLastUsedLocation();

        registerGetVehicleListResponseReceiver();

        //initiateGoogleApiClient();


        //checkIsLocationAvailable();
    }

    private void initialiseLocationManager() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (Utility.isGpsEnabled(this)) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, this);
        }
    }


    public void testRouteListOBA() {
        String json = "{\"code\":200,\"currentTime\":1470939466435,\"data\":{\"limitExceeded\":true,\"list\":[{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"South Renton P&R to University District\",\"id\":\"1_100059\",\"longName\":\"\",\"shortName\":\"167\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/167/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"Juanita to University District\",\"id\":\"1_100168\",\"longName\":\"\",\"shortName\":\"277\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/277/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"U-District to Fremont to Central Magnolia\",\"id\":\"1_100184\",\"longName\":\"\",\"shortName\":\"31\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/031/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"UW Bothell Campus to University District\",\"id\":\"1_100214\",\"longName\":\"\",\"shortName\":\"372\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/372/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"Loyal Heights to University District\",\"id\":\"1_100225\",\"longName\":\"\",\"shortName\":\"45\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/045/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"Mt Baker to University District\",\"id\":\"1_100228\",\"longName\":\"\",\"shortName\":\"48\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/048/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"University District to Eastlake to CBD\",\"id\":\"1_100264\",\"longName\":\"\",\"shortName\":\"70\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/070/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"Laurelhurst to U-Link Station\",\"id\":\"1_100273\",\"longName\":\"\",\"shortName\":\"78\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/078/n0.html\"},{\"agencyId\":\"29\",\"color\":\"\",\"description\":\"\",\"id\":\"29_810\",\"longName\":\"McCollum Park - University District\",\"shortName\":\"810\",\"textColor\":\"\",\"type\":3,\"url\":\"\"},{\"agencyId\":\"40\",\"color\":\"\",\"description\":\"\",\"id\":\"40_586\",\"longName\":\"Tacoma - U. District\",\"shortName\":\"586\",\"textColor\":\"\",\"type\":3,\"url\":\"\"}],\"outOfRange\":false,\"references\":{\"agencies\":[{\"disclaimer\":\"\",\"email\":\"\",\"fareUrl\":\"\",\"id\":\"1\",\"lang\":\"EN\",\"name\":\"Metro Transit\",\"phone\":\"206-553-3000\",\"privateService\":false,\"timezone\":\"America/Los_Angeles\",\"url\":\"http://metro.kingcounty.gov\"},{\"disclaimer\":\"\",\"email\":\"\",\"fareUrl\":\"\",\"id\":\"29\",\"lang\":\"en\",\"name\":\"Community Transit\",\"phone\":\"(800) 562-1375\",\"privateService\":false,\"timezone\":\"America/Los_Angeles\",\"url\":\"http://www.communitytransit.org/\"},{\"disclaimer\":\"\",\"email\":\"\",\"fareUrl\":\"\",\"id\":\"40\",\"lang\":\"EN\",\"name\":\"Sound Transit\",\"phone\":\"1-888-889-6368\",\"privateService\":false,\"timezone\":\"America/Los_Angeles\",\"url\":\"http://www.soundtransit.org\"}],\"routes\":[],\"situations\":[],\"stops\":[],\"trips\":[]}},\"text\":\"OK\",\"version\":2}";

        OBARouteEntity obaRouteEntity = new OBAJsonParser().parseAndGetRouteListEntity(json);
        BusMapFragment busMapFragment = (BusMapFragment) getSupportFragmentManager().findFragmentByTag("BusMapFragment");
        if (busMapFragment != null) {
            busMapFragment.onOBARouteListLoaded(obaRouteEntity);
        }
        Log.e("", "");


    }

    public boolean isCmgFromDeepLink() {
        return isCmgFromDeepLink;
    }

    public Vehicle getSharedVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicle_id(sharedVehicleId);
        vehicle.setVehicle_line_number(sharedVehicleLineNumber);
        vehicle.setVehicle_agency(sharedVehicleAgency);
        return vehicle;
    }

    public void testJson() {

        String json = "{\"code\":200,\"currentTime\":1468957573416,\"data\":{\"limitExceeded\":false,\"list\":[{\"code\":\"10914\",\"direction\":\"S\",\"id\":\"1_10914\",\"lat\":47.656422,\"locationType\":0,\"lon\":-122.312164,\"name\":\"15th Ave NE & NE Campus Pkwy\",\"routeIds\":[\"1_100223\",\"1_100224\",\"1_100228\",\"1_100447\",\"1_100264\",\"1_100059\",\"1_100088\",\"1_100162\",\"40_102640\",\"40_100511\",\"40_100451\",\"40_586\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"10917\",\"direction\":\"S\",\"id\":\"1_10917\",\"lat\":47.655048,\"locationType\":0,\"lon\":-122.312195,\"name\":\"15th Ave NE & NE 40th St\",\"routeIds\":[\"1_100223\",\"1_100224\",\"1_100228\",\"1_100254\",\"1_100259\",\"1_100273\",\"1_100059\",\"1_100088\",\"1_100162\",\"1_100168\",\"40_100235\",\"40_102640\",\"40_100511\",\"40_100451\",\"40_586\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"25240\",\"direction\":\"SW\",\"id\":\"1_25240\",\"lat\":47.650547,\"locationType\":0,\"lon\":-122.304283,\"name\":\"Montlake Blvd NE & NE Pacific Pl - Bay 4\",\"routeIds\":[\"1_100224\",\"1_100225\",\"1_100265\",\"1_100267\",\"1_100215\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"25765\",\"direction\":\"NE\",\"id\":\"1_25765\",\"lat\":47.651367,\"locationType\":0,\"lon\":-122.303604,\"name\":\"Montlake Blvd NE & NE Pacific Pl - Bay 3\",\"routeIds\":[\"1_100254\",\"1_100273\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"29240\",\"direction\":\"SE\",\"id\":\"1_29240\",\"lat\":47.652245,\"locationType\":0,\"lon\":-122.311211,\"name\":\"NE Pacific St & 15th Ave NE\",\"routeIds\":[\"1_100223\",\"1_100224\",\"1_100225\",\"1_100228\",\"1_100254\",\"1_100259\",\"1_100265\",\"1_100267\",\"1_100273\",\"1_100059\",\"1_100088\",\"1_100162\",\"1_100168\",\"1_100215\",\"40_100235\",\"40_102640\",\"40_100511\",\"40_100451\",\"40_586\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"29242\",\"direction\":\"NE\",\"id\":\"1_29242\",\"lat\":47.650486,\"locationType\":0,\"lon\":-122.306839,\"name\":\"NE Pacific Pl & NE Pacific St\",\"routeIds\":[\"1_100224\",\"1_100225\",\"1_100254\",\"1_100259\",\"1_100265\",\"1_100267\",\"1_100273\",\"1_100215\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"29243\",\"direction\":\"E\",\"id\":\"1_29243\",\"lat\":47.650509,\"locationType\":0,\"lon\":-122.305923,\"name\":\"NE Pacific Pl & NE Pacific St\",\"routeIds\":[\"1_100224\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"29247\",\"direction\":\"SE\",\"id\":\"1_29247\",\"lat\":47.649143,\"locationType\":0,\"lon\":-122.30497,\"name\":\"NE Pacific St & Montlake Blvd NE - Bay 1\",\"routeIds\":[\"1_100223\",\"1_100228\",\"1_100059\",\"1_100088\",\"1_100162\",\"1_100168\",\"40_100235\",\"40_102640\",\"40_100511\",\"40_100451\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"29405\",\"direction\":\"NW\",\"id\":\"1_29405\",\"lat\":47.649727,\"locationType\":0,\"lon\":-122.305817,\"name\":\"NE Pacific St & Montlake Blvd NE - Bay 2\",\"routeIds\":[\"1_100223\",\"1_100224\",\"1_100225\",\"1_100228\",\"1_100265\",\"1_100267\",\"1_100059\",\"1_100162\",\"1_100168\",\"1_100215\",\"40_100235\",\"40_102640\",\"40_100511\",\"40_100451\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"29430\",\"direction\":\"N\",\"id\":\"1_29430\",\"lat\":47.653572,\"locationType\":0,\"lon\":-122.312027,\"name\":\"15th Ave NE & NE Pacific St\",\"routeIds\":[\"1_100223\",\"1_100224\",\"1_100228\",\"1_100059\",\"1_100162\",\"1_100168\",\"40_100235\",\"40_102640\",\"40_100511\",\"40_100451\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"29440\",\"direction\":\"N\",\"id\":\"1_29440\",\"lat\":47.655708,\"locationType\":0,\"lon\":-122.311989,\"name\":\"15th Ave NE & NE Campus Pkwy\",\"routeIds\":[\"1_100223\",\"1_100224\",\"1_100228\",\"1_100059\",\"1_100162\",\"1_100168\",\"40_100235\",\"40_102640\",\"40_100511\",\"40_100451\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"75402\",\"direction\":\"SW\",\"id\":\"1_75402\",\"lat\":47.652336,\"locationType\":0,\"lon\":-122.306427,\"name\":\"Stevens Way & Rainier Vis\",\"routeIds\":[\"1_100184\",\"1_100193\",\"1_100259\",\"1_100269\",\"1_100273\",\"1_100168\",\"1_100214\",\"40_100235\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"75403\",\"direction\":\"S\",\"id\":\"1_75403\",\"lat\":47.654365,\"locationType\":0,\"lon\":-122.305214,\"name\":\"Stevens Way & Benton Ln\",\"routeIds\":[\"1_100184\",\"1_100193\",\"1_100259\",\"1_100269\",\"1_100273\",\"1_100168\",\"1_100214\",\"40_100235\",\"29_810\",\"29_821\",\"29_855\",\"29_860\",\"29_871\",\"29_880\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"75404\",\"direction\":\"W\",\"id\":\"1_75404\",\"lat\":47.652149,\"locationType\":0,\"lon\":-122.308495,\"name\":\"Stevens Way & Garfield Ln\",\"routeIds\":[\"1_100184\",\"1_100193\",\"1_100259\",\"1_100269\",\"1_100273\",\"1_100168\",\"1_100214\",\"40_100235\",\"29_810\",\"29_821\",\"29_855\",\"29_860\",\"29_871\",\"29_880\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"75405\",\"direction\":\"W\",\"id\":\"1_75405\",\"lat\":47.655102,\"locationType\":0,\"lon\":-122.310921,\"name\":\"Grant Ln & Stevens Way\",\"routeIds\":[\"1_100184\",\"1_100193\",\"1_100259\",\"1_100269\",\"1_100273\",\"1_100168\",\"1_100214\",\"40_100235\",\"29_810\",\"29_821\",\"29_855\",\"29_860\",\"29_871\",\"29_880\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"75406\",\"direction\":\"S\",\"id\":\"1_75406\",\"lat\":47.656876,\"locationType\":0,\"lon\":-122.304909,\"name\":\"Stevens Way & Pend Oreille Rd\",\"routeIds\":[\"1_100184\",\"1_100193\",\"1_100259\",\"1_100269\",\"1_100273\",\"1_100168\",\"1_100214\",\"40_100235\",\"29_810\",\"29_821\",\"29_855\",\"29_860\",\"29_871\",\"29_880\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"75410\",\"direction\":\"NE\",\"id\":\"1_75410\",\"lat\":47.657257,\"locationType\":0,\"lon\":-122.304573,\"name\":\"Stevens Way & Pend Oreille Rd\",\"routeIds\":[\"1_100184\",\"1_100269\",\"1_100214\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"75412\",\"direction\":\"SE\",\"id\":\"1_75412\",\"lat\":47.655045,\"locationType\":0,\"lon\":-122.311172,\"name\":\"Grant Ln & George Washington Lane\",\"routeIds\":[\"1_100184\",\"1_100269\",\"1_100214\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"75414\",\"direction\":\"N\",\"id\":\"1_75414\",\"lat\":47.653713,\"locationType\":0,\"lon\":-122.305038,\"name\":\"Stevens Way & Benton Ln\",\"routeIds\":[\"1_100184\",\"1_100269\",\"1_100214\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"75415\",\"direction\":\"NE\",\"id\":\"1_75415\",\"lat\":47.652329,\"locationType\":0,\"lon\":-122.306099,\"name\":\"Stevens Way & Rainier Vis\",\"routeIds\":[\"1_100269\",\"1_100214\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"75417\",\"direction\":\"E\",\"id\":\"1_75417\",\"lat\":47.652046,\"locationType\":0,\"lon\":-122.308693,\"name\":\"Stevens Way & Okanogan Ln\",\"routeIds\":[\"1_100184\",\"1_100269\",\"1_100214\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"98360\",\"direction\":\"E\",\"id\":\"1_98360\",\"lat\":47.650875,\"locationType\":0,\"lon\":-122.304581,\"name\":\"NE Pacific Pl & Montlake Blvd NE\",\"routeIds\":[\"1_100225\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"99604\",\"direction\":\"S\",\"id\":\"1_99604\",\"lat\":47.649704,\"locationType\":0,\"lon\":-122.303886,\"name\":\"UW / Husky Stadium Link Station\",\"routeIds\":[\"40_100479\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"99605\",\"direction\":\"N\",\"id\":\"1_99605\",\"lat\":47.649704,\"locationType\":0,\"lon\":-122.30352,\"name\":\"UW / Husky Stadium Link Station\",\"routeIds\":[\"40_100479\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"3081\",\"direction\":\"W\",\"id\":\"29_3081\",\"lat\":47.652191,\"locationType\":0,\"lon\":-122.306816,\"name\":\"W Stevens Way NE & Rainier Vista\",\"routeIds\":[\"29_810\",\"29_821\",\"29_855\",\"29_860\",\"29_871\",\"29_880\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"4073\",\"direction\":\"SE\",\"id\":\"3_19473\",\"lat\":47.649575,\"locationType\":0,\"lon\":-122.30579,\"name\":\"Pacific St /  Pacific Pl\",\"routeIds\":[\"40_586\"],\"wheelchairBoarding\":\"UNKNOWN\"},{\"code\":\"755003\",\"direction\":\"\",\"id\":\"98_755003\",\"lat\":47.6494,\"locationType\":0,\"lon\":-122.3035,\"name\":\"UW-Station\",\"routeIds\":[\"98_869\"],\"wheelchairBoarding\":\"UNKNOWN\"}],\"outOfRange\":false,\"references\":{\"agencies\":[{\"disclaimer\":\"\",\"email\":\"\",\"fareUrl\":\"\",\"id\":\"1\",\"lang\":\"EN\",\"name\":\"Metro Transit\",\"phone\":\"206-553-3000\",\"privateService\":false,\"timezone\":\"America/Los_Angeles\",\"url\":\"http://metro.kingcounty.gov\"},{\"disclaimer\":\"\",\"email\":\"\",\"fareUrl\":\"\",\"id\":\"40\",\"lang\":\"EN\",\"name\":\"Sound Transit\",\"phone\":\"1-888-889-6368\",\"privateService\":false,\"timezone\":\"America/Los_Angeles\",\"url\":\"http://www.soundtransit.org\"},{\"disclaimer\":\"\",\"email\":\"\",\"fareUrl\":\"\",\"id\":\"29\",\"lang\":\"en\",\"name\":\"Community Transit\",\"phone\":\"(800) 562-1375\",\"privateService\":false,\"timezone\":\"America/Los_Angeles\",\"url\":\"http://www.communitytransit.org/\"},{\"disclaimer\":\"\",\"email\":\"\",\"fareUrl\":\"\",\"id\":\"98\",\"lang\":\"\",\"name\":\"Seattle Children's Hospital Shuttle\",\"phone\":\"\",\"privateService\":false,\"timezone\":\"America/Los_Angeles\",\"url\":\"http://www.commutetools.org/\"}],\"routes\":[{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"U Dist - Montlake - Capitol Hill - Downtown\",\"id\":\"1_100223\",\"longName\":\"\",\"shortName\":\"43\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/043/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"Ballard - Montlake\",\"id\":\"1_100224\",\"longName\":\"\",\"shortName\":\"44\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/044/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"Mt Baker to University District\",\"id\":\"1_100228\",\"longName\":\"\",\"shortName\":\"48\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/048/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"U Dist - Broadway - Downtown Sea\",\"id\":\"1_100447\",\"longName\":\"\",\"shortName\":\"49\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/049/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"University District to Eastlake to CBD\",\"id\":\"1_100264\",\"longName\":\"\",\"shortName\":\"70\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/070/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"South Renton P&R to University District\",\"id\":\"1_100059\",\"longName\":\"\",\"shortName\":\"167\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/167/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"Twin Lakes P&R to University District\",\"id\":\"1_100088\",\"longName\":\"\",\"shortName\":\"197\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/197/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"Issaquah to University District\",\"id\":\"1_100162\",\"longName\":\"\",\"shortName\":\"271\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/271/n0.html\"},{\"agencyId\":\"40\",\"color\":\"\",\"description\":\"Overlake P&R to U-District\",\"id\":\"40_102640\",\"longName\":\"\",\"shortName\":\"541\",\"textColor\":\"\",\"type\":3,\"url\":\"http://www.soundtransit.org/Schedules/ST-Express-Bus/541\"},{\"agencyId\":\"40\",\"color\":\"\",\"description\":\"Redmond University District\",\"id\":\"40_100511\",\"longName\":\"Redmond - University District\",\"shortName\":\"542\",\"textColor\":\"\",\"type\":3,\"url\":\"http://www.soundtransit.org/Schedules/ST-Express-Bus/542\"},{\"agencyId\":\"40\",\"color\":\"\",\"description\":\"Issaquah University District Northgate\",\"id\":\"40_100451\",\"longName\":\"Issaquah - Northgate\",\"shortName\":\"556\",\"textColor\":\"\",\"type\":3,\"url\":\"http://www.soundtransit.org/Schedules/ST-Express-Bus/556\"},{\"agencyId\":\"40\",\"color\":\"\",\"description\":\"\",\"id\":\"40_586\",\"longName\":\"Tacoma - U. District\",\"shortName\":\"586\",\"textColor\":\"\",\"type\":3,\"url\":\"\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"Lake City  to University District\",\"id\":\"1_100254\",\"longName\":\"\",\"shortName\":\"65\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/065/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"Northgate TC to Roosevelt to University District\",\"id\":\"1_100259\",\"longName\":\"\",\"shortName\":\"67\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/067/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"Laurelhurst to U-Link Station\",\"id\":\"1_100273\",\"longName\":\"\",\"shortName\":\"78\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/078/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"Juanita to University District\",\"id\":\"1_100168\",\"longName\":\"\",\"shortName\":\"277\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/277/n0.html\"},{\"agencyId\":\"40\",\"color\":\"\",\"description\":\"Kirkland University District\",\"id\":\"40_100235\",\"longName\":\"Kirkland - University District\",\"shortName\":\"540\",\"textColor\":\"\",\"type\":3,\"url\":\"http://www.soundtransit.org/Schedules/ST-Express-Bus/540\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"Loyal Heights to University District\",\"id\":\"1_100225\",\"longName\":\"\",\"shortName\":\"45\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/045/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"Wedgwood to University District to CBD\",\"id\":\"1_100265\",\"longName\":\"\",\"shortName\":\"71\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/071/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"Jackson Park to University District to CBD\",\"id\":\"1_100267\",\"longName\":\"\",\"shortName\":\"73\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/073/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"Aurora Village TC to University District\",\"id\":\"1_100215\",\"longName\":\"\",\"shortName\":\"373\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/373/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"U-District to Fremont to Central Magnolia\",\"id\":\"1_100184\",\"longName\":\"\",\"shortName\":\"31\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/031/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"U-District to Fremont to Interbay to Sea Center W\",\"id\":\"1_100193\",\"longName\":\"\",\"shortName\":\"32\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/032/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"Northgate TC to Lake City to Sand Pt to U-District\",\"id\":\"1_100269\",\"longName\":\"\",\"shortName\":\"75\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/075/n0.html\"},{\"agencyId\":\"1\",\"color\":\"\",\"description\":\"UW Bothell Campus to University District\",\"id\":\"1_100214\",\"longName\":\"\",\"shortName\":\"372\",\"textColor\":\"\",\"type\":3,\"url\":\"http://metro.kingcounty.gov/schedules/372/n0.html\"},{\"agencyId\":\"29\",\"color\":\"\",\"description\":\"\",\"id\":\"29_810\",\"longName\":\"McCollum Park - University District\",\"shortName\":\"810\",\"textColor\":\"\",\"type\":3,\"url\":\"\"},{\"agencyId\":\"29\",\"color\":\"\",\"description\":\"\",\"id\":\"29_821\",\"longName\":\"Marysville - University District\",\"shortName\":\"821\",\"textColor\":\"\",\"type\":3,\"url\":\"\"},{\"agencyId\":\"29\",\"color\":\"\",\"description\":\"\",\"id\":\"29_855\",\"longName\":\"Lynnwood - University District\",\"shortName\":\"855\",\"textColor\":\"\",\"type\":3,\"url\":\"\"},{\"agencyId\":\"29\",\"color\":\"\",\"description\":\"\",\"id\":\"29_860\",\"longName\":\"McCollum Park - University District\",\"shortName\":\"860\",\"textColor\":\"\",\"type\":3,\"url\":\"\"},{\"agencyId\":\"29\",\"color\":\"\",\"description\":\"\",\"id\":\"29_871\",\"longName\":\"Edmonds P&R - University District\",\"shortName\":\"871\",\"textColor\":\"\",\"type\":3,\"url\":\"\"},{\"agencyId\":\"29\",\"color\":\"\",\"description\":\"\",\"id\":\"29_880\",\"longName\":\"Mukilteo - University District\",\"shortName\":\"880\",\"textColor\":\"\",\"type\":3,\"url\":\"\"},{\"agencyId\":\"40\",\"color\":\"\",\"description\":\"LINK to Sea-Tac\",\"id\":\"40_100479\",\"longName\":\"Link light rail\",\"shortName\":\"LINK\",\"textColor\":\"\",\"type\":0,\"url\":\"http://www.soundtransit.org/Schedules/ST-Express-Bus/599\"},{\"agencyId\":\"98\",\"color\":\"\",\"description\":\"\",\"id\":\"98_869\",\"longName\":\"SCH-Gold\",\"shortName\":\"\",\"textColor\":\"\",\"type\":3,\"url\":\"\"}],\"situations\":[],\"stops\":[],\"trips\":[]}},\"text\":\"OK\",\"version\":2}";

        OBAStopEntity obaStopEntity = new OBAJsonParser().parseAndGetRouteEntity(json);
        BusMapFragment busMapFragment = (BusMapFragment) getSupportFragmentManager().findFragmentByTag("BusMapFragment");
        if (busMapFragment != null) {
            busMapFragment.onOBARoutesLoaded(obaStopEntity);
        }
        Log.e("", "");


    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    /*google fused location*/
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .build();

    }


    /*Google fsed location*/
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }
    /*Google fsed location*/


    private void initUI() {

        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Utility.printKeyHash(this);
        Home = getResources().getString(R.string.Home);
        Login = getResources().getString(R.string.Login);
        My_Favourites = getResources().getString(R.string.My_Favourites);

        Settings = getResources().getString(R.string.Settings);
        Help_About = getResources().getString(R.string.Help);
        Logout = getResources().getString(R.string.Logout);
        sharedBuses = getResources().getString(R.string.shared_buses);


        context = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(null);
        actionBar.setDisplayShowHomeEnabled(false);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_shadow = (FrameLayout) findViewById(R.id.toolbar_shadow);
        toolbar_title.setText(getResources().getString(R.string.app_name));
        myPreferences = new Preferences(getApplicationContext());
        isRegistered = myPreferences.isRegistered();
        isLoginned = myPreferences.isLoginned();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //String token = myPreferences.getGCM_TOKEN();

        //  Toast.makeText(getApplicationContext(), myPreferences.getGCM_TOKEN(), Toast.LENGTH_LONG).show();

        fragmentManager = getSupportFragmentManager();
        navItems = new ArrayList<NavItem>();
        loginItems = new ArrayList<NavItem>();
        logoutItems = new ArrayList<NavItem>();

        navMenus = getResources().getStringArray(R.array.drawer_menu);
        navIcons = new int[]{
                R.drawable.home,
                R.drawable.login,
                R.drawable.bus_white,
                R.drawable.settings,
                R.drawable.help,
                R.drawable.logout,
                R.drawable.bus_white

        };

        for (int i = 0; i < navMenus.length; i++) {
            navItems.add(new NavItem(navMenus[i], navIcons[i]));
            if (i != 2)
                loginItems.add(new NavItem(navMenus[i], navIcons[i]));
            if (i != 4 && i != 5 && i != 7)
                logoutItems.add(new NavItem(navMenus[i], navIcons[i]));


        }

        navListHeaderView = layoutInflater.inflate(R.layout.nav_list_header, Drawer);
        navListView = (ListView) findViewById(R.id.nav_listview);
        ImageView headerImageView = (ImageView) navListHeaderView.findViewById(R.id.header_image);

        navListView.addHeaderView(navListHeaderView);


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (isItemClicked) {
                    setFragment(NAVIGATION_ITEM_SELECTED_INDEX, null);
                }

            }


        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();

       /* Picasso.with(this)
                .load(R.drawable.bus)
                .fit()
                .centerCrop()
                .transform(headerImageTranformation)
                .into(headerImageView);*/


        adapter = new NavMenuAdapter(getApplicationContext(), navItems);
        navListView.setOnItemClickListener(this);
        navListView.setAdapter(adapter);

        updateNavigationDrawer(myPreferences.isLoginned());
        // Finally we set the drawer toggle sync State

        initLocationProgressDialog();

    }

    private void initalizeSocialLoginSetup() {

        // gplus setup
        initiateGoogleApiClient();

        //facebook setup
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();

        mLoginResultFacebookCallback =
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Utility.showToast(MainActivity.this, "fb login success");
                        GraphRequest request = GraphRequest.newMeRequest(
                                AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        Log.v("LoginActivity", response.toString());
                                        Utility.showToast(MainActivity.this, "fb connected");

                                        try {
                                            //and fill them here like so.
                                            String str_facebook_id = object.getString("id");
                                            String str_email_id = object.getString("email");
                                            String str_name = object.getString("name");

                                            loginFragment.loginWithFacebook(str_name, str_email_id, str_facebook_id);

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
                        BusMapFragment busMapFragment = (BusMapFragment) getSupportFragmentManager().findFragmentByTag("BusMapFragment");
                        if (busMapFragment != null && busMapFragment.isVisible()) {
                            busMapFragment.stopProgress();
                        }
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        int x = 2 + 5;
                        Utility.showToast(MainActivity.this, "fb login error");
                    }
                }

        ;


        loginManager.registerCallback(callbackManager, mLoginResultFacebookCallback);
    }


    private void initLocationProgressDialog() {

        locationProgressDialog = new ProgressDialog(this);
        locationProgressDialog.setMessage(getResources().getString(R.string.fetching_location));
        locationProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        locationProgressDialog.setIndeterminate(true);
        locationProgressDialog.setCancelable(false);


    }

    private void setLastUsedLocation() {

        Log.d("MainActivity : ", "setLastUsedLocation() -  Setting last used location");
        myUser = myPreferences.getUser();
        int mode = myUser.getMode();
        setLocationBasedOnMode(mode);
    }

    private void setLocationBasedOnMode(int mode) {

        Location lastUsedLocation = null;


        if (mode == User.NONE) {
            // App opened first time. So show location selection dialog
            showLocationSelectionDialogInPage();
            return;

        } else {

            // App already opened. Get last used location based on mode
            Log.d("setLocationBasedOnMode", "Mode is " + mode);
            Double lastUsedLatitude;
            Double lastUsedLongitude;

            if (mode == User.GPS) {

                lastUsedLatitude = Double.parseDouble(myUser.getTemp_latitude());
                lastUsedLongitude = Double.parseDouble(myUser.getTemp_longitude());
                lastUsedLocation = new Location("");
                lastUsedLocation.setLatitude(lastUsedLatitude);
                lastUsedLocation.setLongitude(lastUsedLongitude);


            } else if (mode == User.PICKER) {

                lastUsedLatitude = Double.parseDouble(myUser.getTemp_latitude());
                lastUsedLongitude = Double.parseDouble(myUser.getTemp_longitude());
                lastUsedLocation = new Location("");
                lastUsedLocation.setLatitude(lastUsedLatitude);
                lastUsedLocation.setLongitude(lastUsedLongitude);


            } else if (mode == User.HOME) {

                lastUsedLatitude = Double.parseDouble(myUser.getTemp_latitude());
                lastUsedLongitude = Double.parseDouble(myUser.getTemp_longitude());
                lastUsedLocation = new Location("");
                lastUsedLocation.setLatitude(lastUsedLatitude);
                lastUsedLocation.setLongitude(lastUsedLongitude);

            } else if (mode == User.WORK) {

                lastUsedLatitude = Double.parseDouble(myUser.getTemp_latitude());
                lastUsedLongitude = Double.parseDouble(myUser.getTemp_longitude());
                lastUsedLocation = new Location("");
                lastUsedLocation.setLatitude(lastUsedLatitude);
                lastUsedLocation.setLongitude(lastUsedLongitude);

            }


            if (Utility.isLocationValid(lastUsedLocation)) {
                setFragment(0, null);
                //moveGPSMarker(lastUsedLocation);
                //runGetVehicleService();
            } else {
                showLocationSelectionDialogInPage();
            }
        }


    }

    private void checkIsLocationAvailable() {

        User user = myPreferences.getUser();
        str_latitude = user.getTemp_latitude();
        str_longitude = user.getTemp_longitude();

        if (str_latitude != null && str_longitude != null &&
                str_latitude.length() > 0 && str_longitude.length() > 0) {
            setFragment(0, null);
            // run
            if (Utility.isConnectingToInternet(this)) {
                runGetVehicleService(myPreferences.getUser().getMode(), isFavouriteSelected);
                isGetVehicleServiceRunning = true;
            }


        } else {
            // show dialog to select location
            showLocationSelectionDialogInPage();

        }

    }


    private void runGetVehicleService() {

        // run
        if (Utility.isConnectingToInternet(this)) {
            runGetVehicleService(myPreferences.getUser().getMode(), isFavouriteSelected);
            isGetVehicleServiceRunning = true;
        }

    }

    public void showLocationSelectionDialogInPage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getResources().getString(R.string.location_select));
        builder.setPositiveButton(getResources().getString(R.string.gps), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                dialogInterface.dismiss();


                if (Utility.isGpsEnabled(MainActivity.this)) {

                    runGetLastUsedLocationTask();
                    Location lastKnownLocation = gpsTracker.getLocation();
                    myUser = myPreferences.getUser();
                    if (Utility.isLocationValid(lastKnownLocation)) {
                        myUser = myPreferences.getUser();
                        myUser.setMode(User.GPS);
                        myUser.setTemp_latitude(String.valueOf(lastKnownLocation.getLatitude()));
                        myUser.setTemp_longitude(String.valueOf(lastKnownLocation.getLongitude()));
                        myUser.setGps_latitude(String.valueOf(lastKnownLocation.getLatitude()));
                        myUser.setGps_longitude(String.valueOf(lastKnownLocation.getLongitude()));
                        myPreferences.updateUser(myUser);
                        setFragment(0, null);
                    } else {
                        Log.d("locationDialogInMap", "Location is invalid");
                    }

                } else {
                    Constants.IS_GPS_INTENT_REQUESTED = true;
                    openGpsIntent();
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.pick), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                openPicker();
            }
        });
        builder.setCancelable(false);
        builder.show();

    }

    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            Utility.showToast(MainActivity.this, String.valueOf(latitude) + " " + String.valueOf(longitude));

        } else {
            Utility.showToast(MainActivity.this, "location is disabled");


        }
    }

    private void runGetLastUsedLocationTask() {

        // new GetLastLocationTask().execute();
        getLastUsedLocationAndMoveToMapPage();

    }

    private void getLastUsedLocationAndMoveToMapPage() {
/*
        gpsAsyncTask = new AsyncTask<Void, Void, Void>() {
            String receivedLat, receivedLong;
            Location lastKnownLocation;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showLocationLoadingProgressBar();
            }

            @Override
            protected Void doInBackground(Void... voids) {

                gpsTracker = new GPSTracker(MainActivity.this,MainActivity.this);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                //hideLocationLoadingProgressBar();

                if (gpsTracker.canGetLocation()) {
                    lastKnownLocation = gpsTracker.getLocation();
                    if (lastKnownLocation != null) {
                        receivedLat = String.valueOf(lastKnownLocation.getLatitude());
                        receivedLong = String.valueOf(lastKnownLocation.getLongitude());
                        if (Utility.isLocationValid(receivedLat, receivedLong)) {
                            hideLocationLoadingProgressBar();
                            isGPSLocationCalculated = true;
                            //myPreferences = new Preferences(getApplicationContext());
                            myUser = myPreferences.getUser();
                            myUser.setTemp_latitude(receivedLat);
                            myUser.setTemp_longitude(receivedLong);
                            myUser.setGps_latitude(receivedLat);
                            myUser.setGps_longitude(receivedLong);
                            myUser.setMode(User.GPS);
                            myPreferences.updateUser(myUser);
                            setFragment(0, null);
                            runGetVehicleService();
                        } else {
                            cancel(true);
                            getLastUsedLocationAndMoveToMapPage();
                        }

                        //Constants.IS_GPS_INTENT_REQUESTED = true;

                    } else {
                        if (!isGPSLocationCalculated) {
                            // gpsTracker.stopUsingGPS();
                            cancel(true);
                            getLastUsedLocationAndMoveToMapPage();
                        }
                    }
                } else {
                    //gpsTracker.stopUsingGPS();
                    if (!isGPSLocationCalculated) {
                        cancel(true);
                        getLastUsedLocationAndMoveToMapPage();
                    }
                }
            }

        }.execute();*/
        gpsTracker = new GPSTracker(this);
        gpsTracker.setActivity(this);


    }


    public Location getGpsLocation() {
        return gpsTracker.getLocation();
    }


    public void openGpsIntent() {
        String action = android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        startActivityForResult(new Intent(action), Constants.GPS_REQUEST);
    }


    private void openPicker() {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(MainActivity.this), Constants.MAINACTIVITY_PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void runGetVehicleService(int mode, boolean favouriteFlagEnabled) {


        /*WebService webService = new WebService(this);

        if (!isFavouriteSelected) {
            myUser = myPreferences.getUser();
            str_latitude = myUser.getTemp_latitude();
            str_longitude = myUser.getTemp_longitude();
            webService.getVehicleList(this, this, WebService.GET_VEHICLE_LIST_URL +
                    "latitude=" + str_latitude + "&longitude=" + str_longitude + "&distance=" + str_distance + "&limit=" + limit + "&offset=" + offset, mode, favouriteFlagEnabled);
        } else {
            myUser = myPreferences.getUser();
            String user_id = myPreferences.getUser().getUser_id();
            String getfavVehiclesUrl = "user_id=" + user_id + "&latitude=" + myUser.getTemp_latitude() +
                    "&longitude=" + myUser.getTemp_longitude() + "&distance=" + Constants.DISTANCE_OFFSET;
            webService.getFavVehicleList(this, this, getfavVehiclesUrl, mode, favouriteFlagEnabled);
        }*/

    }

    public void setFavouriteFlag(boolean fav) {
        isFavouriteSelected = fav;
    }

    public boolean getFavouriteFlag() {
        return isFavouriteSelected;
    }

    private void registerGetVehicleListResponseReceiver() {

        if (getVehicleListResponseReceiver == null) {
            getVehicleListResponseReceiver = new GetVehicleListResponseReceiver();
            registerReceiver(getVehicleListResponseReceiver, new IntentFilter(WebService.INTENT_FILTER_VEHICLE_LIST_RESPONSE));
        }
    }

    private void unRegisterGetVehicleListResponseReceiver() {

        if (getVehicleListResponseReceiver != null) {
            unregisterReceiver(getVehicleListResponseReceiver);
            getVehicleListResponseReceiver = null;
        }
    }

    private void initiateGoogleApiClient() {

        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();*/

       /* mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();*/

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void loginWithGoogle(boolean isLoginClicked) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void resolveSignInError() {


        if (mConnectionResult != null && mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        } else {
            // account picker not opened .. show error message or reconnect GoogleApiClient...
            mGoogleApiClient.connect();
        }
    }


    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();

                if (!isItemClicked)
                    setFragmentSelected();

            }


        };

        return result;
    }


    private void setFragmentSelected() {

        BusMapFragment busMapFragment = (BusMapFragment) getSupportFragmentManager().findFragmentByTag("BusMapFragment");
        if (busMapFragment != null && busMapFragment.isVisible()) {
            busMapFragment.setHasOptionsMenu(true);
            if (busMapFragment.isSharedVehicleModeEnabled()) {
                toolbar_title.setText(getResources().getString(R.string.shared_vehicles));
                setToolbarShadowVisibility(1);
            } else {
                toolbar_title.setText(getResources().getString(R.string.Home));
                setToolbarShadowVisibility(0);
            }
            /*if (busMapFragment.isAddSharedVehiclesPending) {
                busMapFragment.addPendingSharedVehicles();
            }*/

            //navListView.setItemChecked(0, true);
            setNavigationItemSelected(0);
        } else {
            //NOT VISIBLE =(
        }


        AuthenticationFragment authenticationFragment = (AuthenticationFragment) getSupportFragmentManager().findFragmentByTag("AuthenticationFragment");
        if (authenticationFragment != null && authenticationFragment.isVisible()) {
            authenticationFragment.setHasOptionsMenu(true);
            toolbar_title.setText(getResources().getString(R.string.welcome_teqbuzz));
            setToolbarShadowVisibility(8);
            //navListView.setItemChecked(1, true);
            setNavigationItemSelected(1);
        } else {
            //NOT VISIBLE =(
        }

        SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("SettingsFragment");
        if (settingsFragment != null && settingsFragment.isVisible()) {
            settingsFragment.setHasOptionsMenu(true);
            toolbar_title.setText(getResources().getString(R.string.Settings));
            setToolbarShadowVisibility(0);
            //navListView.setItemChecked(3, true);
            setNavigationItemSelected(2);
        } else {
            //NOT VISIBLE =(
        }


    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Runtime.getRuntime().gc();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        isItemClicked = true;
        Drawer.closeDrawers();
        NAVIGATION_ITEM_SELECTED_INDEX = i - 1;
        if (NAVIGATION_ITEM_SELECTED_INDEX == 0) {
            isUserClickedHome = true;
        }
        setNavigationItemSelected(NAVIGATION_ITEM_SELECTED_INDEX);


    }

    public void setFragment(int i, Bundle bundle) {

        // Drawer.closeDrawers();
        boolean isLoginned = myPreferences.isLoginned();
        Fragment fragment = null, currentFragment;
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();


        switch (i) {
            case 0://Map Bus Page

                /*for (int x = 0; x < fragmentManager.getBackStackEntryCount(); x++) {
                    fragmentManager.popBackStackImmediate();
                }

                fragmentManager.executePendingTransactions();

                fragment = fragmentManager.findFragmentByTag("BusMapFragment");

                currentFragment = fragmentManager.findFragmentById(R.id.container);
                if (currentFragment != null)
                    currentFragment.setHasOptionsMenu(false);

                if (fragment == null) {
                    super.onPostResume();
                    BusMapFragment busFragment = new BusMapFragment();
                    busFragment.isSharedVehicleModeEnabled = false;
                    busFragment.setHasOptionsMenu(true);
                    fTransaction.add(R.id.container, busFragment, "BusMapFragment");
                    fTransaction.addToBackStack("");
                    fTransaction.commit();
                } else { // re-use the old fragment
                    fTransaction.hide(currentFragment);
                    fTransaction.show(fragment);
                    fTransaction.commit();
                    fragment.setHasOptionsMenu(true);
                    ((BusMapFragment) fragment).onSharedFilterDisabled();

                }*/

                currentFragment = fragmentManager.findFragmentById(R.id.container);
                fragment = fragmentManager.findFragmentByTag("BusMapFragment");
                if (fragment != null) /*{

                    // dec 18 change
                    // if share vehicle enabled, on clicking on slider menu, disable the shared filter and move to map page
                    if (((BusMapFragment) fragment).isSharedVehicleModeEnabled() && !(currentFragment instanceof SettingsFragment)) {
                        if (!isCmgFromDeepLink) {
                            ((BusMapFragment) fragment).onSharedFilterDisabled();
                        } else {
                            //((BusMapFragment) fragment).setSharedVehicleId(sharedVehicleId);
                            ((BusMapFragment) fragment).onSharedFilterEnabled();
                            *//*if (isCmgFromDeepLink && !isLogoutClicked()) {
                                ((BusMapFragment) fragment).setCmgFromDeepLink(isCmgFromDeepLink);
                            }*//*
                            //setNavigationItemSelected(0);
                        }
                    } else {
                        // re-use the old fragment
                        fTransaction.hide(currentFragment);
                        fTransaction.show(fragment);
                        fTransaction.commit();
                        fragment.setHasOptionsMenu(true);
                        ((BusMapFragment) fragment).onSharedFilterDisabled();
                        if (isCmgFromDeepLink && !isLogoutClicked()) {
                            ((BusMapFragment) fragment).setCmgFromDeepLink(isCmgFromDeepLink);
                        }
                        setNavigationItemSelected(0);
                        setActionBarTitle(Home);

                    }
                }*/ {
                    BusMapFragment busMapFragment = (BusMapFragment) fragment;
                    if (busMapFragment != null) {
                        busMapFragment.clearSelectedVehicle();
                        if (isLogoutClicked()) {
                            isLogoutClicked = false;
                            busMapFragment.onSharedFilterDisabled();
                        } else if (isUserClickedHome) {
                            busMapFragment.onSharedFilterDisabled();
                        } else if (isCmgFromDeepLink) {
                            busMapFragment.setCmgFromDeepLink(isCmgFromDeepLink);
                        } else if (!isCmgFromDeepLink) {
                            busMapFragment.onSharedFilterDisabled();
                        } else if (busMapFragment.isSharedVehicleModeEnabled()) {
                            busMapFragment.onSharedFilterDisabled();
                        }
                        fTransaction.hide(currentFragment);
                        fTransaction.show(fragment);
                        fTransaction.commit();
                        fragment.setHasOptionsMenu(true);
                    }
                } else {
                    for (int x = 0; x < fragmentManager.getBackStackEntryCount(); x++) {
                        fragmentManager.popBackStackImmediate();
                    }

                    fragmentManager.executePendingTransactions();
                    BusMapFragment busFragment = new BusMapFragment();
                    busFragment.setHasOptionsMenu(true);
                    fTransaction.add(R.id.container, busFragment, "BusMapFragment");
                    fTransaction.addToBackStack("");
                    fTransaction.commit();
                    setActionBarTitle(Home);
                }

                isUserClickedHome = false;
                break;

            case 1:

                if (isLoginned) {
                    // set shared vehicle
                    fragment = fragmentManager.findFragmentByTag("BusMapFragment");

                    currentFragment = fragmentManager.findFragmentById(R.id.container);
                    if (currentFragment != null)
                        currentFragment.setHasOptionsMenu(false);

                    if (fragment == null) {
                        Fragment authenticationFragment = new BusMapFragment();
                        authenticationFragment.setHasOptionsMenu(true);
                        fTransaction.add(R.id.container, authenticationFragment, "BusMapFragment");
                        fTransaction.addToBackStack("");
                        fTransaction.commit();
                    } else { // re-use the old fragment
                        fTransaction.hide(currentFragment);
                        fTransaction.show(fragment);
                        fTransaction.commit();
                        ((BusMapFragment) fragment).onSharedFilterEnabled();
                        fragment.setHasOptionsMenu(true);

                    }
                    BusMapFragment busMapFragment = (BusMapFragment) fragmentManager.findFragmentByTag("BusMapFragment");
                    if (busMapFragment != null) {
                        busMapFragment.selectSharedVehicleMode();
                    }
                    setActionBarTitle(getResources().getString(R.string.shared_vehicles));
                } else {
                    //open Authentication Fragment
                    fragment = fragmentManager.findFragmentByTag("AuthenticationFragment");

                    currentFragment = fragmentManager.findFragmentById(R.id.container);
                    if (currentFragment != null)
                        currentFragment.setHasOptionsMenu(false);

                    if (fragment == null) {
                        Fragment authenticationFragment = new AuthenticationFragment();
                        authenticationFragment.setHasOptionsMenu(true);
                        fTransaction.add(R.id.container, authenticationFragment, "AuthenticationFragment");
                        fTransaction.addToBackStack("");
                        fTransaction.commit();
                    } else { // re-use the old fragment
                        fTransaction.hide(currentFragment);
                        fTransaction.show(fragment);
                        fTransaction.commit();
                        fragment.setHasOptionsMenu(true);

                    }
                    setActionBarTitle(getResources().getString(R.string.welcome_teqbuzz));
                    setToolbarShadowVisibility(8);
                }

                break;


            case 2:// Settings or About us

                if (isLoginned) {
                    // open my settings
                    fragment = fragmentManager.findFragmentByTag("SettingsFragment");

                    currentFragment = fragmentManager.findFragmentById(R.id.container);
                    if (currentFragment != null)
                        currentFragment.setHasOptionsMenu(false);

                    if (fragment == null) {
                        Fragment settingsFragment = new SettingsFragment();
                        settingsFragment.setHasOptionsMenu(true);
                        fTransaction.add(R.id.container, settingsFragment, "SettingsFragment");
                        fTransaction.addToBackStack("");
                        fTransaction.commit();
                    } else { // re-use the old fragment
                        fTransaction.hide(currentFragment);
                        fTransaction.show(fragment);
                        ((SettingsFragment) fragment).setUpUser();
                        fTransaction.commit();
                        fragment.setHasOptionsMenu(true);

                    }
                    setActionBarTitle(Settings);
                } else {

                    fragment = fragmentManager.findFragmentByTag("AboutUsFragment");

                    currentFragment = fragmentManager.findFragmentById(R.id.container);
                    if (currentFragment != null)
                        currentFragment.setHasOptionsMenu(false);

                    if (fragment == null) {
                        Fragment settingsFragment = new AboutUsFragment();
                        settingsFragment.setHasOptionsMenu(true);
                        fTransaction.add(R.id.container, settingsFragment, "AboutUsFragment");
                        fTransaction.addToBackStack("");
                        fTransaction.commit();
                    } else { // re-use the old fragment
                        fTransaction.hide(currentFragment);
                        fTransaction.show(fragment);
                        //((AboutUsFragment) fragment).setUpUser();
                        fTransaction.commit();
                        fragment.setHasOptionsMenu(true);

                    }
                    setActionBarTitle("About Us");

                }


                break;


            case 3:
                myPreferences = new Preferences(this);
                isLoginned = myPreferences.isLoginned();
                if (!isLoginned) {
                    // don't open IOB exception
                } else {
                    fragment = fragmentManager.findFragmentByTag("AboutUsFragment");

                    currentFragment = fragmentManager.findFragmentById(R.id.container);
                    if (currentFragment != null)
                        currentFragment.setHasOptionsMenu(false);

                    if (fragment == null) {
                        Fragment settingsFragment = new AboutUsFragment();
                        settingsFragment.setHasOptionsMenu(true);
                        fTransaction.add(R.id.container, settingsFragment, "AboutUsFragment");
                        fTransaction.addToBackStack("");
                        fTransaction.commit();
                    } else { // re-use the old fragment
                        fTransaction.hide(currentFragment);
                        /*((SettingsFragment) fragment).setUpUser();*/
                        fTransaction.show(fragment);
                        fTransaction.commit();
                        fragment.setHasOptionsMenu(true);

                    }
                    setActionBarTitle(Settings);

                }


                break;

            case 4:

                if (isLoginned) {
                    // don;t do anything
                    logout();
                } else {


                }

                break;


            case 5:

                //logout();

                break;


            case 6:

                fragment = fragmentManager.findFragmentByTag("RegisterFragment");

                currentFragment = fragmentManager.findFragmentById(R.id.container);
                if (currentFragment != null)
                    currentFragment.setHasOptionsMenu(false);

                if (fragment == null) {
                    Fragment registerFragment = new RegisterFragment();
                    registerFragment.setHasOptionsMenu(true);
                    fTransaction.add(R.id.container, registerFragment, "RegisterFragment");
                    fTransaction.addToBackStack("");
                    fTransaction.commit();
                } else { // re-use the old fragment
                    fTransaction.hide(currentFragment);
                    fTransaction.show(fragment);
                    fTransaction.commit();
                    fragment.setHasOptionsMenu(true);

                }
                setActionBarTitle(getResources().getString(R.string.signup));


                break;

            case 7:

/*
                fragment = new VehicleDetailsFragment();
                if (bundle != null)
                    fragment.setArguments(bundle);
                fTransaction.add(R.id.container, fragment, "VehicleDetailsFragment").addToBackStack("").commit();

                setActionBarTitle("Vehicle Details");*/

                fragment = new RouteListFragment();
                if (bundle != null)
                    fragment.setArguments(bundle);
                fTransaction.add(R.id.container, fragment, "RouteListFragment").addToBackStack("").commit();

                setActionBarTitle("Vehicle Details");


                break;


            default:
                break;

        }


        //setNavigationItemSelected(i);
        isItemClicked = false;
    }

    public void setNavigationItemSelected(int i) {

        try {
            navListView.setItemChecked(i, true);
            navItems.get(i).setSelected(true);
            adapter.setSelecedPosition(i);
            adapter.notifyDataSetChanged();
            //navListView.setSelection(i);
        } catch (IndexOutOfBoundsException e) {

        }

    }

    public void setActionBarTitle(String title) {

        toolbar_title.setText(title);
    }

    @Override
    public void onBackPressed() {

        isItemClicked = false;

        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();

            Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);
            if (currentFragment != null) {
                currentFragment.setHasOptionsMenu(false);

            }

        } else {
            //super.onBackPressed();
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);
            if (currentFragment instanceof BusMapFragment) {

                if (((BusMapFragment) currentFragment).isDrawerOpened()) {
                    ((BusMapFragment) currentFragment).closeVehicleDetailFragment();
                } else {
                    showConfirmToExitApp();
                }

            } else {
                showConfirmToExitApp();
            }
        }

    }


    private void showConfirmToExitApp() {
        AlertDialog.Builder exitAppAlert = new AlertDialog.Builder(this);
        exitAppAlert.setMessage(getResources().getString(R.string.confirm_exit_app));
        exitAppAlert.setPositiveButton(getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        exitAppAlert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        exitAppAlert.show();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (isLoginClicked) {
            mSignInClicked = false;
            //Utility.showToast(this, "is connected");
            loginFragment = getLoginFragment();
            if (loginFragment != null)
                loginFragment.stopGoogleProgress();
            try {
                if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                    Person currentPerson = Plus.PeopleApi
                            .getCurrentPerson(mGoogleApiClient);
                    String personPhotoUrl = currentPerson.getImage().getUrl();
                    String personGooglePlusProfile = currentPerson.getUrl();
                    String gmail_id = Plus.AccountApi.getAccountName(mGoogleApiClient);


                    AppController.getInstance().setClient(mGoogleApiClient);


                    if (loginFragment != null)
                        loginFragment.onGoogleConnected(currentPerson, gmail_id);


                } else {
                    Toast.makeText(this,
                            "Person information is null", Toast.LENGTH_LONG).show();
                }


            } catch (Exception e) {
                Toast.makeText(this,
                        e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            isLoginClicked = false;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        Log.d("", "");

       /* if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }*/

    }


    @Override
    protected void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
        registerConnectivityReceiver();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {

                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

        } else {
            // Signed out, show unauthenticated UI.

        }
    }

    // [START revokeAccess]
    private void revokeGoogleAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]


    @Override
    protected void onResume() {
        super.onResume();

        if (IS_INTERNET_REQUESTED) {
            if (Utility.isConnectingToInternet(this)) {
                //hideInternetConnectionDialog();
                if (internetSnackBar == null) {
                    internetSnackBar = Utility.showSnackForInternet(MainActivity.this, getResources().getString(R.string.no_internet), MainActivity.this);
                }
                internetSnackBar.dismiss();
                IS_INTERNET_SNACKBAR_SHOWN = true;
                setLastUsedLocation();
                IS_INTERNET_REQUESTED = false;
            } else {
                internetSnackBar = Utility.showSnackForInternet(MainActivity.this, getResources().getString(R.string.no_internet), MainActivity.this);
                internetSnackBar.show(MainActivity.this);
                IS_INTERNET_SNACKBAR_SHOWN = true;
                IS_INTERNET_REQUESTED = false;
                //showInternetConnectionDialog();
            }
        }
        if (Constants.IS_GPS_INTENT_REQUESTED && Utility.isGpsEnabled(this)) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, this);
            showLocationLoadingProgressBar();
            runGetLastUsedLocationTask();
        }

        mode = String.valueOf(myPreferences.getUser().getMode());
        if (mGoogleApiClient != null && mode.equalsIgnoreCase(Constants.GOOGLE_MODE))
            mGoogleApiClient.connect();
    }

    public void showLocationLoadingProgressBar() {

        //Do something after 100ms
        locationProgressDialog.show();

    }

    public void hideLocationLoadingProgressBar() {

        if (locationProgressDialog.isShowing()) {
            locationProgressDialog.dismiss();
        }

    }

    protected void onStop() {
        super.onStop();
        unRegisterConnectivityReceiver();
        /*if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }*/
    }

    @Override
    public void onResult(People.LoadPeopleResult arg0) {
        // TODO Auto-generated method stub

    }

    public void updateGoogleApiClient(GoogleApiClient client) {

        mGoogleApiClient = client;
    }

    public void updateNavigationDrawer(boolean isLoginned) {
        tempNavItems = new ArrayList<NavItem>();
        tempNavItems.clear();

        for (int i = 0; i < navMenus.length; i++) {
            tempNavItems.add(new NavItem(navMenus[i], navIcons[i]));
        }

        NavItem loginItem = tempNavItems.get(1);
        NavItem favItem = tempNavItems.get(2);
        NavItem settingsItem = tempNavItems.get(3);
        NavItem logoutItem = tempNavItems.get(5);

        if (isLoginned) {
            tempNavItems.remove(loginItem);

        } else {
            tempNavItems.remove(favItem);
            tempNavItems.remove(settingsItem);
            tempNavItems.remove(logoutItem);
        }
        navItems.clear();
        navItems.addAll(tempNavItems);

        adapter.notifyDataSetChanged();

    }

    public void showLocationSelectionDialogInMap() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getResources().getString(R.string.location_select));
        builder.setPositiveButton(getResources().getString(R.string.gps), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                /*myUser = myPreferences.getUser();
                myUser.setMode(User.GPS);
                myPreferences.updateUser(myUser);
                */
                Constants.IS_GPS_INTENT_REQUESTED_IN_MAP = true;

                if (Utility.isGpsEnabled(MainActivity.this)) {

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    //myPreferences = new Preferences(getApplicationContext());
                    myUser = myPreferences.getUser();
                    if (Utility.isLocationValid(lastKnownLocation)) {
                        //myPreferences = new Preferences(getApplicationContext());
                        myUser = myPreferences.getUser();
                        myUser.setMode(User.GPS);
                        myUser.setTemp_latitude(String.valueOf(lastKnownLocation.getLatitude()));
                        myUser.setTemp_longitude(String.valueOf(lastKnownLocation.getLongitude()));
                        myPreferences.updateUser(myUser);
                        setFragment(0, null);
                    } else {
                        Log.d("locationDialogInMap", "Location is invalid");
                    }

                } else {

                    openGpsIntent();
                }
            }
        });
        builder.show();


    }

    public void setLoginFragment(LoginFragment loginFragment) {

        this.loginFragment = loginFragment;
    }


    public void getStopListForRoute(ArrayList<OBARouteEntity.Data.Route> routes) {

        new OBAWebService(this).getOBAStopListForRoute(routes, this);
    }

    public void onOBAStopsForRoutesLoaded(String s, ArrayList<OBAStopListEntity> obaStopListEntities) {
        Log.e("", "");
        Utility.showToast(MainActivity.this, "stop list entity size is" + String.valueOf(obaStopListEntities.size()));

        BusMapFragment busMapFragment = (BusMapFragment) getSupportFragmentManager().findFragmentByTag("BusMapFragment");
        if (busMapFragment != null)
            busMapFragment.onOBAStopsForRoutesLoaded("", obaStopListEntities);

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return super.onRetainCustomNonConfigurationInstance();
    }

    public void getOBAArrivalDepartureForStop(String stopId) {

        new OBAWebService(this).getArrivalDepartureForStop(stopId, this);
    }

    public void onOBAArrivalDepartureLoaded(String res, OBAArrivalDepartureEntity obaArrivalDepartureEntity) {

        BusMapFragment busMapFragment = (BusMapFragment) getSupportFragmentManager().findFragmentByTag("BusMapFragment");
        if (busMapFragment != null)
            busMapFragment.onOBAArrivalDepartureEntityLoaded(obaArrivalDepartureEntity);
        Log.e("", "");
    }

    public void getOBAStopListForRoute(String selectedRouteId) {
        new OBAWebService(this).getOBAStopsForRoute(selectedRouteId, this);
    }

    public void onOBAStopsForRouteLoaded(String res, OBAStopListEntity obaStopListEntity) {
        try {
            BusMapFragment busMapFragment = (BusMapFragment) getSupportFragmentManager().findFragmentById(R.id.container);
            if (busMapFragment != null) {
                busMapFragment.onOBAStopsForRouteLoaded(res, obaStopListEntity);
            }
        } catch (ClassCastException e) {

        }

    }

    public void getOBATripsForRoute(String selectedRouteId) {
        new OBAWebService(this).getOBATripsForRoute(selectedRouteId, this);
    }

    public void onOBATripsForRouteLoaded(String res, OBATripsForRouteEntity obaTripsForRouteEntity) {
        if (obaTripsForRouteEntity != null) {
            BusMapFragment busMapFragment = (BusMapFragment) getSupportFragmentManager().findFragmentById(R.id.container);
            try {
                if (busMapFragment != null) {
                    busMapFragment.onOBATripsForRouteLoaded(res, obaTripsForRouteEntity);
                }
            } catch (ClassCastException e) {
                busMapFragment.onOBATripsForRouteLoaded(res, obaTripsForRouteEntity);
            }
        }
    }

    public void onOBATripsForLocationLoaded(String res, OBATripsForLocationEntity obaTripsForLocationEntity) {
        if (obaTripsForLocationEntity != null) {
            BusMapFragment busMapFragment = (BusMapFragment) getSupportFragmentManager().findFragmentById(R.id.container);
            try {
                if (busMapFragment != null) {
                    busMapFragment.onOBATripsForLocationLoaded(res, obaTripsForLocationEntity);
                }
            } catch (ClassCastException e) {
                busMapFragment.onOBATripsForLocationLoaded(res, obaTripsForLocationEntity);
            }
        }
    }

    public void loginWithFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken == null)
            loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));

    }

    public void setCmgFromDeepLink(boolean b) {
        isCmgFromDeepLink = false;
    }

    /*public void onGpsLocationFound() {
        Utility.showToast(this,"loc found in service");
    }*/

    class GetVehicleListResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String response = intent.getStringExtra("response");
            int mode = intent.getIntExtra("mode", -1);
            boolean favouriteFlagEnabled = intent.getBooleanExtra("favouriteFlagEnabled", false);


            if (response.contains("status")) {

                updateVehicleMarkerInMap(response, mode, favouriteFlagEnabled);
            } else {

            }


            /*new Thread(new Runnable() {
                @Override

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!BusMapFragment.IS_GET_VEHICLE_SERVICE_BLOCKED)
                                runGetVehicleService();
                        }
                    });
                }
            }).start();*/


        }

    }

    private void updateVehicleMarkerInMap(String response, int mode, boolean favouriteFlagEnabled) {

        BusMapFragment busMapFragment = (BusMapFragment) getSupportFragmentManager().findFragmentByTag("BusMapFragment");
        if (busMapFragment != null)
            busMapFragment.updateVehiclesList(response, mode, favouriteFlagEnabled);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RC_SIGN_IN) {

                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                loginFragment.handleSignInResult(result);


            } else if (requestCode == Constants.PLACE_PICKER_REQUEST) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                fragment.onActivityResult(requestCode, resultCode, data);
            } else if (requestCode == Constants.MAINACTIVITY_PLACE_PICKER_REQUEST) {

                if (resultCode == RESULT_OK) {

                    Place place = PlacePicker.getPlace(data, this);
                    //String toastMsg = String.format("Place: %s", place.getName());
                    //myPreferences = new Preferences(getApplicationContext());
                    myUser = myPreferences.getUser();
                    myUser.setMode(User.PICKER);
                    str_latitude = String.valueOf(place.getLatLng().latitude);
                    str_longitude = String.valueOf(place.getLatLng().longitude);
                    myUser.setTemp_latitude(str_latitude);
                    myUser.setTemp_longitude(str_longitude);
                    myPreferences.updateUser(myUser);

                    setFragment(0, null);
                    if (Utility.isConnectingToInternet(this))
                        runGetVehicleService(myPreferences.getUser().getMode(), isFavouriteSelected);

                } else {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                    fragment.onActivityResult(requestCode, resultCode, data);
                }

            } else {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            /*Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
            fragment.onActivityResult(requestCode, resultCode, data);*/
            if (requestCode == RC_SIGN_IN) {

                if (resultCode != RESULT_OK) {
                    mSignInClicked = false;
                } else
                    loginFragment.showGoogleProgress();

                mIntentInProgress = false;

                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        String lat = String.valueOf(location.getLatitude()), lon = String.valueOf(location.getLongitude());

        //myPreferences = new Preferences(getApplicationContext());
        myUser = myPreferences.getUser();
        int mode = myUser.getMode();


        // update GPS location
        if (Utility.isLocationValid(lat, lon)) {
            myUser.setGps_latitude(lat);
            myUser.setGps_longitude(lon);
            myPreferences.updateUser(myUser);
            Log.d("onLocationChanged() valid", lat + " : " + lon);

        } else {
            Log.d("onLocationChanged() invalid", lat + " : " + lon);
            Utility.showToast(MainActivity.this, getResources().getString(R.string.location_loading_message));
        }

        Log.d("prefs values", getSharedPreferences("teqbuzz_prefs", 0).getString(Preferences.GPS_LATITUDE, ""));

        // Close location Progress Dialog if opened
        closeProgressIfOpened();

        if ((!BusMapFragment.isUserMarkerAnimationEnabled && mode == User.GPS) || (Constants.IS_GPS_INTENT_REQUESTED)) {

            // Gps mode is ON. Set the GPS location to last used location in background
            // Mo picker, fav or drag on map selected

            myUser.setTemp_latitude(lat);
            myUser.setTemp_longitude(lon);
            myPreferences.updateUser(myUser);


            if (Constants.IS_GPS_INTENT_REQUESTED) {
                // Gps requested in location selection dialog in main page . Open BusMapFragment
                Constants.IS_GPS_INTENT_REQUESTED = false;
                myUser = myPreferences.getUser();
                myUser.setMode(User.GPS);
                myPreferences.updateUser(myUser);
                setFragment(0, null);
                if (!isGetVehicleServiceRunning)
                    runGetVehicleService(myUser.getMode(), false);

            } else if (Constants.IS_GPS_INTENT_REQUESTED_IN_SIGNUP) {

            } else if (Constants.IS_GPS_INTENT_REQUESTED_IN_MAP) {
                // Gps requested in location selection dialog in map page . Animate User marker
                myUser = myPreferences.getUser();
                myUser.setMode(User.GPS);
                myPreferences.updateUser(myUser);
                moveGPSMarker(location);

            } else {
                if (mode == User.GPS)
                    moveGPSMarker(location);
                else
                    setLastUsedLocation();
            }


        }
    }

    private void closeProgressIfOpened() {

        if (locationProgressDialog != null && locationProgressDialog.isShowing())
            locationProgressDialog.dismiss();
    }

    private void moveGPSMarker(Location location) {
        try {
            BusMapFragment busMapFragment = (BusMapFragment) getSupportFragmentManager().findFragmentById(R.id.container);
            if (busMapFragment != null) {
                busMapFragment.updateGPSMarker(location);
            }
        } catch (ClassCastException e) {

        }


    }

    public void showHomeWorkIcon(int visibility) {
        try {
            BusMapFragment busMapFragment = (BusMapFragment) getSupportFragmentManager().findFragmentByTag("BusMapFragment");
            if (busMapFragment != null) {
                busMapFragment.showHomeWorkIcon(visibility);
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }


    }


    public void showWorkIcon(int visibility) {
        try {
            BusMapFragment busMapFragment = (BusMapFragment) getSupportFragmentManager().findFragmentByTag("BusMapFragment");
            if (busMapFragment != null) {
                busMapFragment.showHomeWorkIcon(visibility);
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void logout() {
        isLogoutClicked = true;
        logoutWithGoogle();
        logoutWithFacebook();

        User user = new User("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
        //myPreferences = new Preferences(getApplicationContext());
        user.setTemp_latitude(myPreferences.getUser().getTemp_latitude());
        user.setTemp_longitude(myPreferences.getUser().getTemp_longitude());
        user.setHomeLatitude(user.getHomeLatitude());
        user.setHomeLongitude(user.getHomeLongitude());
        user.setWork_latitude(user.getWork_latitude());
        user.setWork_longitude(user.getWork_longitude());

        user.setGps_latitude(myPreferences.getUser().getGps_latitude());
        user.setGps_longitude(myPreferences.getUser().getGps_longitude());
        // to set mode for non loginned user to show customer markers
        int lastMode = myPreferences.getUser().getMode();
        if (lastMode == User.GPS) {
            user.setMode(User.GPS);
        } else {
            user.setMode(User.PICKER);
        }

        myPreferences.updateUser(user);
        myPreferences.setLoginned(false);

       /*updateSliderMenu(MainActivity.this);
         updateChatIconVisibility(false);
         setFragment(1);*/
        //hide home icon in map view
        //showHomeWorkIcon(View.GONE);

        updateNavigationDrawer(false);

        setFragment(0, null);

    }

    private void logoutWithFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager loginManager = LoginManager.getInstance();
        if (loginManager != null)
            loginManager.logOut();

    }

    private void logoutWithGoogle() {
        /*if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            //mGoogleApiClient.connect();

        }*/
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
    }

    private void initiateGoogleApiClientAgain() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unRegisterGetVehicleListResponseReceiver();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();


        locationManager.removeUpdates(this);

    }

    class GetLastLocationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLocationLoadingProgressBar();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getLastUsedLocationAndMoveToMapPage();
                    //getSingleShotLocation();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //hideLocationLoadingProgressBar();
        }

    }

    private void getSingleShotLocation() {

        SingleShotLocationProvider.requestSingleUpdate(context,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        Log.d("Location", "my location is " + location.toString());
                        if (Utility.isLocationValid(location)) {
                            myUser = myPreferences.getUser();
                            myUser.setTemp_latitude(String.valueOf(location.latitude));
                            myUser.setTemp_longitude(String.valueOf(location.longitude));
                            myUser.setMode(User.GPS);
                            myPreferences.updateUser(myUser);
                            setFragment(0, null);
                            runGetVehicleService();
                        } else {
                            getSingleShotLocation();
                        }
                    }
                });
    }


    public void setToolbarShadowVisibility(int visibility) {
        toolbar_shadow.setVisibility(visibility);
    }

    public LoginFragment getLoginFragment() {
        return loginFragment;
    }

    public BusMapFragment getBusMapFragment() {
        BusMapFragment busMapFragment = (BusMapFragment) getSupportFragmentManager().findFragmentByTag("BusMapFragment");
        return busMapFragment;
    }

    public boolean isLogoutClicked() {
        return isLogoutClicked;
    }

    private void registerConnectivityReceiver() {

        if (connectivityReceiver != null) {
            LocalBroadcastManager.getInstance(this).registerReceiver(connectivityReceiver, new IntentFilter(Constants.INTENT_FILTER_CONNECTIVITY));
        }
    }

    private void unRegisterConnectivityReceiver() {

        if (connectivityReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(connectivityReceiver);
        }
    }

    private SnackBar internetSnackBar;
    private boolean IS_INTERNET_SNACKBAR_SHOWN;
    private BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isConnected = intent.getBooleanExtra(Constants.IS_CONNECTED, Utility.isConnectingToInternet(MainActivity.this));
            if (IS_IN_PAGE == 1) {
                if (isConnected) {
                    getBusMapFragment().quickRunGetVehicleService();
                    if (internetSnackBar == null) {
                        internetSnackBar = Utility.showSnackForInternet(MainActivity.this, getResources().getString(R.string.no_internet), MainActivity.this);
                    }
                    internetSnackBar.dismiss();
                    IS_INTERNET_SNACKBAR_SHOWN = false;
                } else {
                    if (internetSnackBar == null) {
                        internetSnackBar = Utility.showSnackForInternet(MainActivity.this, getResources().getString(R.string.no_internet), MainActivity.this);
                    }
                    internetSnackBar.show(MainActivity.this);
                    IS_INTERNET_SNACKBAR_SHOWN = true;
                }
            } else {
                if (isConnected) {

                    if (internetSnackBar == null) {
                        internetSnackBar = Utility.showSnackForInternet(MainActivity.this, getResources().getString(R.string.no_internet), MainActivity.this);
                    }
                    internetSnackBar.dismiss();
                    //hideInternetConnectionDialog();
                    setLastUsedLocation();
                    IS_INTERNET_SNACKBAR_SHOWN = false;
                } else {
                    if (internetSnackBar == null) {
                        internetSnackBar = Utility.showSnackForInternet(MainActivity.this, getResources().getString(R.string.no_internet), MainActivity.this);
                    }
                    //showInternetConnectionDialog();
                    internetSnackBar.show(MainActivity.this);
                    IS_INTERNET_SNACKBAR_SHOWN = true;

                }
            }
        }
    };

}
