package com.kanthan.teqbuzz.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.NativeAd;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kanthan.teqbuzz.AppController;
import com.kanthan.teqbuzz.Entities.AlarmResponseEntity;
import com.kanthan.teqbuzz.Entities.VehicleListEntity;
import com.kanthan.teqbuzz.MainActivity;
import com.kanthan.teqbuzz.R;
import com.kanthan.teqbuzz.WebServiceListener;
import com.kanthan.teqbuzz.adapter.AdvertisingAdapter;
import com.kanthan.teqbuzz.adapter.CustomListAdapter;
import com.kanthan.teqbuzz.adapter.SharedVehicleListAdapter;
import com.kanthan.teqbuzz.adapter.VehicleListAdapter;
import com.kanthan.teqbuzz.adapter.VehicleScheduleAdapter;
import com.kanthan.teqbuzz.models.Schedule;
import com.kanthan.teqbuzz.models.User;
import com.kanthan.teqbuzz.models.Vehicle;
import com.kanthan.teqbuzz.onebusaway.ArrivalsAndDepartureAdapter;
import com.kanthan.teqbuzz.onebusaway.OBAArrivalDepartureEntity;
import com.kanthan.teqbuzz.onebusaway.OBAConstants;
import com.kanthan.teqbuzz.onebusaway.OBAJsonParser;
import com.kanthan.teqbuzz.onebusaway.OBARouteEntity;
import com.kanthan.teqbuzz.onebusaway.OBAStopEntity;
import com.kanthan.teqbuzz.onebusaway.OBAStopListEntity;
import com.kanthan.teqbuzz.onebusaway.OBATripDetailsForTripEntity;
import com.kanthan.teqbuzz.onebusaway.OBATripDetailsForVehicleEntity;
import com.kanthan.teqbuzz.onebusaway.OBATripsForLocationEntity;
import com.kanthan.teqbuzz.onebusaway.OBATripsForRouteEntity;
import com.kanthan.teqbuzz.onebusaway.OBAVehicle;
import com.kanthan.teqbuzz.onebusaway.OBAVehiclesAdapter;
import com.kanthan.teqbuzz.onebusaway.OBAWebService;
import com.kanthan.teqbuzz.onebusaway.OBAWebServiceCallBack;
import com.kanthan.teqbuzz.utilities.AppDatabaseHelper;
import com.kanthan.teqbuzz.utilities.Constants;
import com.kanthan.teqbuzz.utilities.DirectionsJSONParser;
import com.kanthan.teqbuzz.utilities.LatLngInterpolator;
import com.kanthan.teqbuzz.utilities.MarkerAnimation;
import com.kanthan.teqbuzz.utilities.Movement;
import com.kanthan.teqbuzz.utilities.MySupportFragment;
import com.kanthan.teqbuzz.utilities.Preferences;
import com.kanthan.teqbuzz.utilities.Utility;
import com.kanthan.teqbuzz.utilities.WebService;
import com.rey.material.app.Dialog;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.SnackBar;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import it.carlom.stikkyheader.core.StikkyHeaderBuilder;
import it.carlom.stikkyheader.core.animator.AnimatorBuilder;
import it.carlom.stikkyheader.core.animator.HeaderStikkyAnimator;

//import com.nirhart.parallaxscroll.views.ParallaxListView;

/**
 * Created by user on 3/2/2016.
 */
public class BusMapFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener, WebServiceListener, OBAWebServiceCallBack {

    Activity mActivity;
    Context mContext;
    private View rootView;
    // private MapView mMapView;
    private GoogleMap googleMap;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<String> vehicleIds;
    Preferences myPreferences;
    Double double_latitude, double_longitude;
    String str_home_latitude, str_home_longitude, str_work_latitude, str_work_longitude, str_gps_latitude, str_gps_longitude, str_latitude, str_longitude, str_picker_latitude, str_picker_longitude;
    BitmapDescriptor gps_marker, picker_marker, home_marker, work_marker, vehicle_marker, stop_marker;
    ListView bus_listview;
    private MarkerOptions user_marker_options;
    private Marker user_marker;
    private ArrayList<LatLng> points; //added
    Polyline line; //added
    private long MARKER_ANIMATION_DURATION = 500;
    ArrayList<Marker> markerArrayList, scheduleMarkers;
    private ArrayList<MarkerOptions> markers;
    private MarkerOptions tempMarkerOptions;
    private Marker centralMarker;
    private Marker tempMarker;
    private Vehicle tempVehicle;
    private VehicleListAdapter vehicleListAdapter;
    private ArrayList<LatLng> stops;
    FloatingActionButton fabGPS, fabPick, fabWork, fabHome;
    public static boolean isUserMarkerAnimationEnabled;
    User user;
    private AlertDialog.Builder locationSelectionDialog;
    RelativeLayout map_container, bus_list_container;
    MySupportFragment mapFragment;
    ProgressBarCircularIndeterminate progress_bar;
    int strokeColor = 0xffff0000;
    int shadeColor = 0x44ff0000;
    private Circle user_circle;
    private boolean isMapLoaded;
    private MenuItem choiceMenuItem;
    ProgressView progressView;
    TextView vehicle_not_found_textview;
    public static boolean IS_GET_VEHICLE_SERVICE_BLOCKED = false;
    private String vehicleListJsonResponse;
    // private SlidingUpPanelLayout slidingUpPanelLayout;
    private String TAG = "BusMapFragment";
    //private View headerView;
    MapView busMapView;
    private View container_layout;
    public DrawerLayout mDrawerLayout;
    private WebService webService;
    private BroadcastReceiver vehicleScheduleListResponseReceiver;
    private ArrayList<Schedule> schedules;
    private VehicleScheduleAdapter vehicleScheduleAdapter;
    ListView schedule_list;
    LinearLayout progress_container;
    private boolean isDrawerOpened;
    private RelativeLayout stop_list_container;

    TextView line_number, start_location, end_location, agency_name, vehicle_type;
    private int selectedVehiclePosition;
    private String selectedVehicleId;
    private Transformation headerImageTranformation;
    View scheduleListViewHeader;
    private CircularImageView headerImageView;
    private NativeExpressAdView adView;
    private LinearLayout nativeAdContainer;
    private LinearLayout fbAdView;
    AdvertisingAdapter nativeAdAdapter;
    SwipeRefreshLayout swipe_layout;
    private boolean isOBAStopsForRoutesLoaded;
    private ArrayList<OBAStopListEntity.Stop> obaRouteListStops;
    HashMap<String, Marker> visibleMarkers = new HashMap<String, Marker>();
    private ArrayList<OBAArrivalDepartureEntity.Data.ArrivalAndDeparture> arrivalAndDepartures;
    private ArrivalsAndDepartureAdapter arrivalsAndDepartureAdapter;
    private ArrayList<Marker> obaStopMarkers = new ArrayList<Marker>();
    private String selectedRouteId;
    private ArrayList<OBATripsForRouteEntity.Data.Trip> obaTrips;
    private PolylineOptions obaRoutePolyLines;
    private ArrayList<Polyline> vehicleRoutePolylines;
    private ArrayList<OBATripsForLocationEntity.Data.Trip> obaTripsForLocations;
    private ArrayList<Marker> obaVehicleMarkers;
    private Location lastUsedLocation;
    private boolean isFavouriteSelected;
    private ArrayList<Vehicle> teqBuzzVehicles, teqBuzzFavVehicles;
    VehicleListAdapter teqbuzzVehicleListAdapter;
    private ArrayList<Marker> teqBuzzVehicleMarkers = new ArrayList<Marker>();
    private ArrayList<Marker> teqBuzzStopMarkers;
    HashMap<String, Marker> teqBuzzVehicleMarkerHash;
    private Dialog vehicleOptionDialog, filterDialog;
    float mapZoomLevel;
    private boolean isErrorOccured;
    private int selectedVehicleIndex = -1, clickedVehicleIndex = -1;
    HashMap<String, Movement> pendingMovements;
    AppDatabaseHelper teqBuzzDbHelper;
    private TextView isFavText;
    ProgressBar vehicleLoadingBar;
    private AlertDialog.Builder loginDialog;
    private boolean isAdsLoaded;
    private boolean IS_INTERNET_REQUESTED;
    private String sharedVehicleId, totalSharedVehicleIds;
    Vehicle sharedVehicle;
    public boolean isCmgFromDeepLink, isSharedVehicleModeEnabled;
    private Vehicle selectedVehicle;
    LinearLayout vehicleLoadingView;
    SnackBar vehicleNotFoundSnackBar;
    private boolean isVehicleNotFoundSnackBarShowing;
    private String[] spinnerFilters;
    private int distanceRange;
    private CheckBox distance_flag;
    private boolean isDistanceFlagEnabled;
    private TextView distance_value;
    private Spinner filterSpinner;
    private int spinnerPosition;
    private SeekBar distanceSeekBar;
    private ArrayAdapter<String> spinnerDataAdapter;
    private Button btnFilter, btnReset;
    private AlertDialog.Builder homeWorkLocationDialog;
    String homeLocationDialogMsg, workLocationDialogMsg;
    FloatingActionMenu fabMenu;
    RelativeLayout stop_overlay;
    ProgressBar stop_loading_bar;
    TextView no_schedules_selected, stops_loading;
    private long GET_VEHICLE_SERVICE_DELAY = 0;
    private ArrayList<Vehicle> sharedVehicles;
    private RelativeLayout menuBadgeView;
    private TextView sharedCount;
    private ImageView deleteSharedVehicleBtn;
    private int sharedVehiclesCount;
    private AppDatabaseHelper appDatabaseHelper;
    private SharedVehicleListAdapter sharedVehicleListAdapter;
    private MenuItem sharedVehicleMenuItem;
    public String pendingAddSharedVehicleId;
    public boolean isAddSharedVehiclesPending;
    private SnackBar favVehiclesSnackBar, internetSnackBar;
    private ListView shareVehicleListView;
    public static int CALL_MODE = -1;
    private boolean IS_INTERNET_SNACKBAR_SHOWN, IS_NO_VEHICLE_SNACKBAR_SHOWN;
    private PolylineOptions selectedBusPolyLines;
    private boolean isMapFocusedRoute;
    private DownloadTask polyLinesDownloadTask;
    private ParserTask polyLinesParserTask;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        myPreferences = new Preferences(mActivity.getApplicationContext());
        webService = new WebService(mActivity);
        pendingMovements = new HashMap<String, Movement>();
        teqBuzzDbHelper = new AppDatabaseHelper(mActivity);
        appDatabaseHelper = new AppDatabaseHelper(mActivity);
        sharedVehicles = appDatabaseHelper.getSharedVehicles();
        sharedVehiclesCount = appDatabaseHelper.getSharedVehicles().size();
        // Run favorites bus service
        /*if (Utility.isConnectingToInternet(mActivity) && myPreferences.isLoginned()) {
            runFavouritesBusVehicleService();
        } else {
        }*/
    }

    public void runFavouritesBusVehicleService() {
        user = myPreferences.getUser();
        String getfavVehiclesUrl = "user_id=" + user.getUser_id() + "&latitude=" + user.getTemp_latitude() +
                "&longitude=" + user.getTemp_longitude() + "&distance=" + "200000";
        webService.getFavVehicleListFromServer(mActivity, this, getfavVehiclesUrl);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_routelist, container, false);
       /* Calligrapher calligrapher = new Calligrapher(mActivity);
        calligrapher.setFont(mActivity, "fonts/Roboto-Italic.ttf", true);
*/
        MainActivity.IS_IN_PAGE = 1;
        swipe_layout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);
        swipe_layout.setColorScheme(R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary);
        //  swipe_layout.setOnRefreshListener(null);
        swipe_layout.setEnabled(false);
        homeLocationDialogMsg = mActivity.getResources().getString(R.string.homeLocationDialogMsg);
        workLocationDialogMsg = mActivity.getResources().getString(R.string.workLocationDialogMsg);
        fabMenu = (FloatingActionMenu) rootView.findViewById(R.id.menu);

        //drawer layout declarations

        mDrawerLayout = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);        // Drawer object Assigned to the view
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isDrawerOpened = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawerOpened = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        schedules = new ArrayList<Schedule>();
        schedule_list = (ListView) rootView.findViewById(R.id.stops_listview);
        schedule_list.setOnItemClickListener(this);
        vehicleScheduleAdapter = new VehicleScheduleAdapter(mActivity, this, schedules);
        progress_container = (LinearLayout) rootView.findViewById(R.id.progress_container);
        stop_list_container = (RelativeLayout) rootView.findViewById(R.id.stop_list_container);
        stop_overlay = (RelativeLayout) rootView.findViewById(R.id.stop_overlay);
        stop_loading_bar = (ProgressBar) rootView.findViewById(R.id.stop_loading_bar);
        no_schedules_selected = (TextView) rootView.findViewById(R.id.no_schedules_selected);
        stops_loading = (TextView) rootView.findViewById(R.id.stops_loading);

        scheduleListViewHeader = rootView.findViewById(R.id.bus_detail_header);
        headerImageView = (CircularImageView) scheduleListViewHeader.findViewById(R.id.stops_header_image);
        vehicleNotFoundSnackBar = Utility.getVehicleNotFoundSnackBar(mActivity, this);
        //scheduleListViewHeader.setVisibility(View.GONE);
       /* StikkyHeaderBuilder.stickTo(schedule_list)
                .setHeader(R.id.bus_detail_header, stop_list_container)
                .minHeightHeader(100)
                .animator(new ScheduleListStikkyAnimator())
                .build();*/

      /*  headerImageTranformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(5)
                .cornerRadiusDp(60)
                .oval(false)
                .build();*/

        schedule_list.setAdapter(vehicleScheduleAdapter);



      /*  Picasso.with(mActivity)
                .load(R.drawable.bus)
                .fit()
                .centerCrop()
                .transform(headerImageTranformation)
                .into(headerImageView);*/

        /*Stop list header views*/
        line_number = (TextView) rootView.findViewById(R.id.line_number);
        start_location = (TextView) rootView.findViewById(R.id.start_location);
        end_location = (TextView) rootView.findViewById(R.id.end_location);
        agency_name = (TextView) rootView.findViewById(R.id.agency_name);
        vehicle_type = (TextView) rootView.findViewById(R.id.vehicle_type);

        try {
            MapsInitializer.initialize(mActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkForGooglePlayServices();

        bus_listview = (ListView) rootView.findViewById(R.id.bus_listview);
        bus_listview.setItemsCanFocus(true);
        bus_listview.setFocusable(false);
        bus_listview.setFocusableInTouchMode(false);
        bus_listview.setClickable(false);
        vehicleLoadingView = (LinearLayout) rootView.findViewById(R.id.back_view);
        //headerView = inflater.inflate(R.layout.bus_mapview_header, bus_listview, false);
        mapFragment = (MySupportFragment) getChildFragmentManager().findFragmentById(R.id.vehicleMapFragment);

        container_layout = rootView.findViewById(R.id.container_layout);
        StikkyHeaderBuilder.stickTo(bus_listview)
                .setHeader(R.id.header, (ViewGroup) rootView)
                .minHeightHeader(200)
                .animator(new ParallaxStikkyAnimator())
                .build();

       /* googleMap = mapFragment.getMap();
*/

        fabGPS = (FloatingActionButton) rootView.findViewById(R.id.fab_gps);
        fabPick = (FloatingActionButton) rootView.findViewById(R.id.fab_pick);
        fabHome = (FloatingActionButton) rootView.findViewById(R.id.fab_home);
        fabWork = (FloatingActionButton) rootView.findViewById(R.id.fab_work);
        fabGPS.setOnClickListener(this);
        fabHome.setOnClickListener(this);
        fabWork.setOnClickListener(this);
        fabPick.setOnClickListener(this);
        /*bus_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onBusListItemSelcted(i - 1);
            }
        });*/
        //bus_listview.setOnItemLongClickListener(this);
        initFilterDialog();
        initVehicleOptionDialog();
        initHomeWorkLocationDialog();
        initLoginDialog();

        progressView = (ProgressView) rootView.findViewById(R.id.progressView);
        vehicle_not_found_textview = (TextView) rootView.findViewById(R.id.vehicle_not_found);
        vehicleLoadingBar = (ProgressBar) rootView.findViewById(R.id.vehicleLoadingBar);
        // show home and work icon if user loginned
        if (myPreferences.isLoginned()) {
            fabHome.setVisibility(View.VISIBLE);
            fabWork.setVisibility(View.VISIBLE);

        }


        vehicles = new ArrayList<Vehicle>();
        vehicleIds = new ArrayList<String>();

        points = new ArrayList<LatLng>(); //added
        //vehicles = Utility.createDummyVehiclesList();
        vehicles = new ArrayList<Vehicle>();

        //get last used Location
        str_latitude = myPreferences.getUser().getTemp_latitude();
        str_longitude = myPreferences.getUser().getTemp_longitude();
        double_latitude = Double.parseDouble(str_latitude);
        double_longitude = Double.parseDouble(str_longitude);


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                onMapLoaded(googleMap);

            }
        });

       /* final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                nativeAdAdapter.onAdsLoaded(null);
            }
        }, 8000);*/
        //showNativeAd(null, null);


        /*NativeExpressAdView adView = (NativeExpressAdView) rootView.findViewById(R.id.adView);

        AdRequest request = new AdRequest.Builder()
                .addTestDevice("D95A94C986AD4863551F454541EA861F")
                .build();
        adView.loadAd(request);*/
        return rootView;

    }

    private void initHomeWorkLocationDialog() {
        homeWorkLocationDialog = new AlertDialog.Builder(mActivity);
        homeWorkLocationDialog.setPositiveButton(mActivity.getResources().getString(R.string.set), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((MainActivity) mActivity).setFragment(2, null);
            }
        });
        homeWorkLocationDialog.setNegativeButton(mActivity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }

    private void initLoginDialog() {
        loginDialog = new AlertDialog.Builder(mActivity);
        loginDialog.setMessage(mActivity.getResources().getString(R.string.login_dialog_msg));
        loginDialog.setPositiveButton(mActivity.getResources().getString(R.string.login), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((MainActivity) mActivity).setFragment(1, null);
            }
        });
        loginDialog.setNegativeButton(mActivity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

    }


    private void initVehicleOptionDialog() {

        vehicleOptionDialog = new Dialog(mActivity);
        vehicleOptionDialog.setContentView(R.layout.vehicle_options_dialog);
        vehicleOptionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LinearLayout showRoute = (LinearLayout) vehicleOptionDialog.findViewById(R.id.show_route);
        LinearLayout focusVehicle = (LinearLayout) vehicleOptionDialog.findViewById(R.id.focus_vehicle);
        LinearLayout showSchedule = (LinearLayout) vehicleOptionDialog.findViewById(R.id.show_schedule);
        LinearLayout addAsFav = (LinearLayout) vehicleOptionDialog.findViewById(R.id.add_fav);
        LinearLayout shareVehicle = (LinearLayout) vehicleOptionDialog.findViewById(R.id.share_vehicle);
        isFavText = (TextView) vehicleOptionDialog.findViewById(R.id.fav_text);
        showRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedVehicleIndex = clickedVehicleIndex;
                selectedVehicle = teqBuzzVehicles.get(selectedVehicleIndex);
                vehicleOptionDialog.dismiss();
                schedules = new ArrayList<Schedule>();
                Vehicle vehicle = teqBuzzVehicles.get(selectedVehicleIndex);
                webService.getVehicleScheduleList(mActivity, vehicle.getVehicle_id(), BusMapFragment.this);
                setSelectedVehicle(selectedVehicle);
            }
        });
        focusVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    selectedVehicleIndex = clickedVehicleIndex;
                    selectedVehicle = teqBuzzVehicles.get(selectedVehicleIndex);
                    vehicleOptionDialog.dismiss();
                    moveToSelectedTeqBuzzVehicle(selectedVehicleIndex);
                    setSelectedVehicle(selectedVehicle);
                } catch (IndexOutOfBoundsException e) {
                    Utility.showToast(mActivity, mActivity.getResources().getString(R.string.please_wait));
                }
            }
        });
        showSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSelectedVehicle();
                isMapFocusedRoute = false;
                selectedVehicleIndex = clickedVehicleIndex;
                selectedVehicle = teqBuzzVehicles.get(selectedVehicleIndex);
                AppController.getInstance().trackScreenView("Schedules Screen");
                vehicleOptionDialog.dismiss();
                mDrawerLayout.openDrawer(Gravity.RIGHT);
                Vehicle selectedVehicle = teqBuzzVehicles.get(selectedVehicleIndex);
                if (selectedVehicle.getSchedules() != null && selectedVehicle.getSchedules().size() > 0) {
                    // already schedules loaded
                    onTeqBuzzSchedulesLoaded(selectedVehicle.getSchedules());
                } else {
                    getTeqbuzzVehicleScheduleList(selectedVehicle);
                }
                setSelectedVehicle(selectedVehicle);
            }
        });
        addAsFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedVehicleIndex = clickedVehicleIndex;
                selectedVehicle = teqBuzzVehicles.get(selectedVehicleIndex);
                vehicleOptionDialog.dismiss();
                Vehicle selectedVehicle = teqBuzzVehicles.get(selectedVehicleIndex);
                if (!teqBuzzDbHelper.isVehicleFav(selectedVehicle)) {
                    if (myPreferences.isLoginned()) {
                        //long isAdded = teqBuzzDbHelper.addVehicleToFavList(selectedVehicle);
                        boolean vehicleFlag = selectedVehicle.isFavourite();
                        String strVehicleFavFlag;
                        if (vehicleFlag) {
                            strVehicleFavFlag = Vehicle.IS_NOT_FAV;
                        } else {
                            strVehicleFavFlag = Vehicle.IS_FAV;
                        }
                        String addFavVehicleUrl = "user_id=" + user.getUser_id() + "&vehicle_id=" + selectedVehicle.getVehicle_id() + "&flag=" + strVehicleFavFlag;
                        webService.addRemoveVehicleAsFav(mActivity, BusMapFragment.this, selectedVehicle, addFavVehicleUrl);
                    } else {
                        loginDialog.setMessage(mActivity.getResources().getString(R.string.login_dialog_message_favourites));
                        loginDialog.show();
                    }
                } else {
                    //teqBuzzDbHelper.deleteVehicleFromFavList(selectedVehicle);
                    String addFavVehicleUrl = "user_id=" + user.getUser_id() + "&vehicle_id=" + selectedVehicle.getVehicle_id() + "&flag=" + Vehicle.IS_NOT_FAV;
                    webService.addRemoveVehicleAsFav(mActivity, BusMapFragment.this, selectedVehicle, addFavVehicleUrl);
                    //teqbuzzVehicleListAdapter.notifyDataSetChanged();
                    teqBuzzFavVehicles = teqBuzzDbHelper.getFavVehicles();
                    if (teqBuzzFavVehicles == null || teqBuzzFavVehicles.size() <= 0) {
                        favVehiclesSnackBar = Utility.showSnackForFavBuses(mActivity, BusMapFragment.this);
                        if (isFavouriteFlagEnabled() && !isSharedVehicleModeEnabled)
                            favVehiclesSnackBar.show(mActivity);
                    }

                }
                setSelectedVehicle(selectedVehicle);
            }
        });
        shareVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedVehicleIndex = clickedVehicleIndex;
                selectedVehicle = teqBuzzVehicles.get(selectedVehicleIndex);
                vehicleOptionDialog.dismiss();
                shareVehicle(teqBuzzVehicles.get(selectedVehicleIndex));
                setSelectedVehicle(selectedVehicle);
            }
        });


    }

    private void setSelectedVehicle(Vehicle selectedVehicle) {
        teqBuzzVehicles.get(teqBuzzVehicles.indexOf(selectedVehicle)).setSelected(true);
        for (Vehicle tempVehicle : teqBuzzVehicles) {
            if (!tempVehicle.getVehicle_id().equalsIgnoreCase(selectedVehicle.getVehicle_id())) {
                teqBuzzVehicles.get(teqBuzzVehicles.indexOf(tempVehicle)).setSelected(false);
            }
        }
        teqbuzzVehicleListAdapter.notifyDataSetChanged();

    }

    private void initFilterDialog() {

        filterDialog = new Dialog(mActivity);
        filterDialog.setContentView(R.layout.filter_dialog);
        filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        distanceSeekBar = (SeekBar) filterDialog.findViewById(R.id.distance_seekbar);
        distance_value = (TextView) filterDialog.findViewById(R.id.distance_value);
        distance_flag = (CheckBox) filterDialog.findViewById(R.id.distance_flag);
        distance_flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                     @Override
                                                     public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {

                                                         if (flag) {
                                                             distanceSeekBar.setEnabled(true);
                                                             distance_value.setVisibility(View.VISIBLE);
                                                             if (distanceRange == 150000) {
                                                                 distance_value.setText("0");
                                                                 distanceSeekBar.setProgress(0);
                                                             } else {
                                                                 distance_value.setText("0");
                                                                 distanceSeekBar.setProgress(distanceRange);
                                                             }
                                                         } else {
                                                             distance_value.setVisibility(View.INVISIBLE);
                                                             distanceSeekBar.setEnabled(false);
                                                         }
                                                     }
                                                 }

        );

        distanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                                       @Override
                                                       public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                                           int selectedValue = i;
                                                           distance_value.setText(String.valueOf(selectedValue));
                                                       }

                                                       @Override
                                                       public void onStartTrackingTouch(SeekBar seekBar) {

                                                       }

                                                       @Override
                                                       public void onStopTrackingTouch(SeekBar seekBar) {

                                                       }
                                                   }
        );
        filterSpinner = (Spinner) filterDialog.findViewById(R.id.filter_seekbar);
        spinnerFilters = mActivity.getResources().getStringArray(R.array.filter_array);
        spinnerDataAdapter = new ArrayAdapter<String>(mActivity, R.layout.filter_spinner_item, spinnerFilters);
        filterSpinner.setAdapter(spinnerDataAdapter);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnFilter = (Button) filterDialog.findViewById(R.id.btn_filter);
        btnReset = (Button) filterDialog.findViewById(R.id.btn_reset);
        btnFilter.setOnClickListener(new View.OnClickListener()

                                     {
                                         @Override
                                         public void onClick(View view) {
                                             filterDialog.dismiss();
                                             isDistanceFlagEnabled = distance_flag.isChecked();
                                             myPreferences.setDistanceFlag(isDistanceFlagEnabled);
                                             distanceRange = distanceSeekBar.getProgress();
                                             if (!isDistanceFlagEnabled)
                                                 distanceRange = 150000;
                                             myPreferences.setDistanceRange(distanceRange);

                                             spinnerPosition = filterSpinner.getSelectedItemPosition();
                                             if (spinnerPosition == 0) { // clear fav.. All
                                                 if (!isCmgFromDeepLink) {
                                                     onFavFilterDisabled();
                                                 } else {
                                                     clearDeepLinkMode();
                                                 }
                                             } else if (spinnerPosition == 1) {  // set fav..
                                                 if (!isCmgFromDeepLink) {
                                                     if (myPreferences.isLoginned()) {
                                                         onFavFilterEnabled();
                                                     } else {
                                                         loginDialog.setMessage(mActivity.getResources().getString(R.string.login_dialog_message_favourites));
                                                         loginDialog.show();
                                                     }
                                                 } else {
                                                     clearDeepLinkMode();
                                                 }
                                             }

                                         }
                                     }

        );

        resetFilterDialog();

    }

    private void resetFilterDialog() {
        distanceRange = myPreferences.getDistanceRange();
        isDistanceFlagEnabled = myPreferences.getDistanceFlag();
        distance_flag.setChecked(isDistanceFlagEnabled);
        distanceSeekBar.setProgress(distanceRange);
        distance_value.setText(String.valueOf(distanceRange));
        if (!isDistanceFlagEnabled) {
            distance_value.setVisibility(View.INVISIBLE);
            distanceSeekBar.setEnabled(false);
        }
        if (isFavouriteFlagEnabled()) {
            filterSpinner.setSelection(1);
        } else {
            filterSpinner.setSelection(0);
        }
    }

    private void shareVehicle(Vehicle vehicle) {
        String shareBody = "http://teqbuzz.com/vehicleId=" + vehicle.getVehicle_id() + "/lineNumber=" + vehicle.getVehicle_line_number() + "/to=" + vehicle.getStart_stop();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "share"));
    }

    private void getTeqbuzzVehicleScheduleList(Vehicle selectedVehicle) {
        progress_container.setVisibility(View.VISIBLE);
        stop_overlay.setVisibility(View.VISIBLE);
        stop_loading_bar.setVisibility(View.VISIBLE);
        no_schedules_selected.setVisibility(View.GONE);
        stops_loading.setVisibility(View.VISIBLE);

        webService.getVehicleScheduleList(mActivity, selectedVehicle.getVehicle_id(), BusMapFragment.this);
    }

    private void onMapLoaded(final GoogleMap map) {
        isMapLoaded = true;
        googleMap = map;
        if (googleMap != null) {
            isMapLoaded = true;
            setTeqBuzzStopMarkersBasedOnZoom(googleMap.getCameraPosition().zoom);
            setCustomMarkersInMap();
            moveMapCameraToLastUsedLocation();
            initialiseUserMarker();

            teqBuzzVehicles = new ArrayList<Vehicle>();
            teqBuzzFavVehicles = teqBuzzDbHelper.getFavVehicles();
            teqbuzzVehicleListAdapter = new VehicleListAdapter(this, mActivity, teqBuzzVehicles);
            bus_listview.setAdapter(teqbuzzVehicleListAdapter);
            AppDatabaseHelper appDatabaseHelper = new AppDatabaseHelper(mActivity);
            if (isCmgFromDeepLink) {
                /*is coming for     m deep link*/
                //onSharedFilterEnabled();
                webService.clearAll();
                teqBuzzVehicles.clear();
                teqbuzzVehicleListAdapter.notifyDataSetChanged();
                myPreferences = new Preferences(mActivity);
                boolean isLoginned = myPreferences.isLoginned();
                sharedVehicleId = ((MainActivity) mActivity).getSharedVehicle().getVehicle_id();
                sharedVehicle = ((MainActivity) mActivity).getSharedVehicle();
                if (isLoginned) {
                    /*user loginned*/
                    if (!appDatabaseHelper.isVehicleAlreadyInSharedList(sharedVehicleId) && !MainActivity.isShareCancelBtnClicked) {
                        /*vehicle not found in shared list. So add vehicle in shared list*/
                        //showAddSharedVehicleDialog(isLoginned);
                        Vehicle vehicle = new Vehicle();
                        vehicle.setVehicle_id(sharedVehicleId);
                        vehicle.setVehicle_line_number(sharedVehicle.getVehicle_line_number());
                        vehicle.setVehicle_agency(sharedVehicle.getVehicle_agency());
                        addVehicleToSharedList(vehicle);
                    } else {
                        /*vehicle already in shared list. Do nothing*/
                    }
                } else {
                    /*user not loginned*/
                    showAddSharedVehicleDialog(isLoginned);
                }
            }

            if (Utility.isConnectingToInternet(mActivity)) {
                if (!isCmgFromDeepLink) {
                    runGetVehicleService(user.getMode(), isFavouriteFlagEnabled());
                }
            } else {
                if (internetSnackBar == null) {
                    internetSnackBar = Utility.showSnackForInternet(mActivity, mActivity.getResources().getString(R.string.no_internet), this);

                }
                if (!IS_INTERNET_SNACKBAR_SHOWN) {
                    internetSnackBar.show(mActivity);
                    IS_INTERNET_SNACKBAR_SHOWN = true;
                }
                vehicleLoadingBar.setVisibility(View.GONE);
                vehicle_not_found_textview.setVisibility(View.GONE);
            }

            addGoogleMapListeners();

        } else {
            Utility.showSnack(mActivity, "map is null");
        }
    }

    private void showAddSharedVehicleDialog(boolean isLoginned) {
        AlertDialog.Builder addSharedVehicleDialog = new AlertDialog.Builder(mActivity);
        isLoginned = myPreferences.isLoginned();
        String message, addBtnString;
        if (isLoginned) {
            message = mActivity.getResources().getString(R.string.add_shared_vehicle_msg_login);
            addBtnString = mActivity.getResources().getString(R.string.add);
        } else {
            message = mActivity.getResources().getString(R.string.add_shared_vehicle_msg_non_login);
            addBtnString = mActivity.getResources().getString(R.string.login);
        }
        addSharedVehicleDialog.setMessage(message);
        final boolean finalIsLoginned = isLoginned;
        addSharedVehicleDialog.setPositiveButton(addBtnString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (finalIsLoginned) {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setVehicle_id(pendingAddSharedVehicleId);
                    vehicle.setVehicle_line_number(pendingAddSharedVehicleId);
                    vehicle.setVehicle_agency(pendingAddSharedVehicleId);
                    addVehicleToSharedList(vehicle);
                    ((MainActivity) mActivity).setNavigationItemSelected(1);
                } else {
                    isAddSharedVehiclesPending = true;
                    pendingAddSharedVehicleId = sharedVehicleId;
                    ((MainActivity) mActivity).setFragment(1, null);
                }

            }
        });
        addSharedVehicleDialog.setNegativeButton(mActivity.getResources().getString(R.string.no_thanks), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                MainActivity.isShareCancelBtnClicked = true;
                if (myPreferences.isLoginned()) {
                    ((MainActivity) mActivity).setNavigationItemSelected(1);
                } else {
                    ((MainActivity) mActivity).setNavigationItemSelected(0);
                }
            }
        });
        addSharedVehicleDialog.setCancelable(false);
        addSharedVehicleDialog.show();
    }

    private void addVehicleToSharedList(Vehicle sharedVehicle) {
        Log.d("addVehicleToSharedList", "adding a vehicle");
        AppDatabaseHelper appDatabaseHelper = new AppDatabaseHelper(mActivity);
        appDatabaseHelper.addVehicleToSharedList(sharedVehicle);
        refreshSharedBadge();
        //Utility.showAlert(mActivity, mActivity.getResources().getString(R.string.shared_vehicle_added_message));

    }

    private void refreshSharedBadge() {
        sharedVehicles = appDatabaseHelper.getSharedVehicles();
        sharedVehiclesCount = sharedVehicles.size();
        if (isSharedVehicleModeEnabled && sharedCount != null) {
            sharedCount.setText("" + sharedVehiclesCount);
            if (sharedVehiclesCount <= 0) {
                sharedVehicleMenuItem.setVisible(false);
            } else {
                sharedVehicleMenuItem.setVisible(true);
            }
        }
    }

    private void addGoogleMapListeners() {
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                if (isOBAStopsForRoutesLoaded) {
                    //addItemsToMap(obaRouteListStops);
                }
                setTeqBuzzStopMarkersBasedOnZoom(cameraPosition.zoom);
            }
        });


        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                Log.d("Suren", "location changed");
                isUserMarkerAnimationEnabled = false;
                setTeqBuzzStopMarkersBasedOnZoom(googleMap.getCameraPosition().zoom);
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {


                if (marker.getTitle().contains(Constants.OBA_TRIP_MARKER)) {

                    View snippetView = LayoutInflater.from(mActivity).inflate(R.layout.bus_snippet, (ViewGroup) rootView, false);

                    TextView bus_number = (TextView) snippetView.findViewById(R.id.bus_number);
                    TextView bus_agency = (TextView) snippetView.findViewById(R.id.bus_agency);
                    CircularImageView bus_snippet_image = (CircularImageView) snippetView.findViewById(R.id.bus_snippet_image);
                    bus_number.setTextColor(Color.GRAY);
                    bus_number.setText(": " + marker.getTitle().replace(Constants.OBA_TRIP_MARKER, ""));
                    bus_agency.setText(": " + marker.getTitle().replace(Constants.OBA_TRIP_MARKER, ""));

                    return snippetView;
                } else if (marker.getTitle().contains(Constants.TEQBUZZ_MARKER)) {

                    View snippetView = LayoutInflater.from(mActivity).inflate(R.layout.bus_snippet, (ViewGroup) rootView, false);

                    TextView bus_number = (TextView) snippetView.findViewById(R.id.bus_number);
                    TextView bus_agency = (TextView) snippetView.findViewById(R.id.bus_agency);
                    CircularImageView bus_snippet_image = (CircularImageView) snippetView.findViewById(R.id.bus_snippet_image);
                    Vehicle clickedVehicle = (Vehicle) marker.getTag();
                    if (clickedVehicle != null) {
                        bus_number.setText(": " + String.valueOf(clickedVehicle.getVehicle_line_number()));
                        bus_agency.setText(": " + clickedVehicle.getEnd_stop());
                    }
                        /*bus_number.setText(": " + marker.getTitle().replace(Constants.TEQBUZZ_MARKER + "_", ""));
                        bus_agency.setText(": " + marker.getTitle().replace(Constants.TEQBUZZ_MARKER, ""));*/

                    return snippetView;
                } else if (marker.getTitle().contains(Constants.TEQBUZZ_STOP_MARKER)) {

                    View snippetView = LayoutInflater.from(mActivity).inflate(R.layout.stop_snippet, (ViewGroup) rootView, false);

                    TextView bus_number = (TextView) snippetView.findViewById(R.id.bus_number);
                    TextView bus_agency = (TextView) snippetView.findViewById(R.id.bus_agency);
                    CircularImageView bus_snippet_image = (CircularImageView) snippetView.findViewById(R.id.bus_snippet_image);
                    bus_snippet_image.setImageResource(R.drawable.bus_info_icon);
                    String stopId = marker.getTitle().split("_")[3];
                    String stopName = marker.getTitle().split("_")[4];
                    bus_number.setText(" : " + stopId);
                    bus_agency.setText(" : " + stopName);

                    return snippetView;
                }

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });


        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                String markerTitle = marker.getTitle();
                String markerSnippet = marker.getSnippet();


                if (markerTitle.equalsIgnoreCase("me")) {

                    String title = marker.getTitle();

                } else if (markerTitle.equalsIgnoreCase("stop")) {

                    String title = marker.getTitle();

                } else if (markerTitle.equalsIgnoreCase(Constants.OBA_MARKER)) {

                    String snippet = marker.getSnippet();

                } else if (markerTitle.contains(Constants.TEQBUZZ_MARKER)) {
                    clickedVehicleIndex = teqBuzzVehicleMarkers.indexOf(marker);
                    progress_container.setVisibility(View.VISIBLE);
                    if (teqBuzzDbHelper.isVehicleFav(teqBuzzVehicles.get(selectedVehicleIndex))) {
                        isFavText.setText("Remove from Fav");
                    } else {
                        isFavText.setText("Add as Fav");
                    }
                    vehicleOptionDialog.show();
                }
            }
        });
    }

    private void setTeqBuzzStopMarkersBasedOnZoom(float tempZoomLevel) {
        //MainActivity.toolbar_title.setText(String.valueOf(tempZoomLevel));
        int stopIcon = 0;
        if (tempZoomLevel != mapZoomLevel) {
            mapZoomLevel = tempZoomLevel;
            if (mapZoomLevel <= 6) {
                stop_marker = BitmapDescriptorFactory.fromResource(R.drawable.red_ring_8);
                stopIcon = R.drawable.red_ring_8;
            } else if (mapZoomLevel > 6 && mapZoomLevel <= 10) {
                stop_marker = BitmapDescriptorFactory.fromResource(R.drawable.red_ring_8);
                stopIcon = R.drawable.red_ring_8;
            } else if (mapZoomLevel > 10 && mapZoomLevel <= 14) {
                stop_marker = BitmapDescriptorFactory.fromResource(R.drawable.red_ring_12);
                stopIcon = R.drawable.red_ring_12;
            } else if (mapZoomLevel > 14) {
                stop_marker = BitmapDescriptorFactory.fromResource(R.drawable.red_ring_16);
                stopIcon = R.drawable.red_ring_16;
            }


            if (teqBuzzStopMarkers != null && teqBuzzStopMarkers.size() > 0) {
                for (Marker marker : teqBuzzStopMarkers) {
                    BitmapDescriptor flagBitmapDescriptor = Utility.getCustomMarker(mActivity, schedules.get(teqBuzzStopMarkers.indexOf(marker)));
                    /*LinearLayout etaMarkerLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.distance_marker_layout, null);

                    etaMarkerLayout.setDrawingCacheEnabled(true);
                    etaMarkerLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    etaMarkerLayout.layout(0, 0, etaMarkerLayout.getMeasuredWidth(), etaMarkerLayout.getMeasuredHeight());
                    etaMarkerLayout.buildDrawingCache(true);

                    TextView eta = (TextView) etaMarkerLayout.findViewById(R.id.eta);
                    ImageView stopImage = (ImageView) etaMarkerLayout.findViewById(R.id.stop);
                    stopImage.setImageResource(stopIcon);
                    eta.setText(schedules.get(teqBuzzStopMarkers.indexOf(marker)).getEta() + " mins");

                    Bitmap flagBitmap = Bitmap.createBitmap(etaMarkerLayout.getDrawingCache());
                    etaMarkerLayout.setDrawingCacheEnabled(false);
                    BitmapDescriptor flagBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(flagBitmap);
*/
                    int index = teqBuzzStopMarkers.indexOf(marker);
                    marker.setIcon(flagBitmapDescriptor);
                    teqBuzzStopMarkers.set(index, marker);
                }
            }
        }


    }

    private void initialiseUserMarker() {

        user = myPreferences.getUser();
        BitmapDescriptor userMapIcon = null;
        if (user.getMode() == User.GPS) {
            userMapIcon = gps_marker;
        } else if (user.getMode() == User.PICKER) {
            userMapIcon = picker_marker;
        } else if (user.getMode() == User.HOME) {
            userMapIcon = home_marker;
        } else if (user.getMode() == User.WORK) {
            userMapIcon = work_marker;
        }

        user_marker_options = new MarkerOptions().position(
                new LatLng(lastUsedLocation.getLatitude(), lastUsedLocation.getLongitude()))
                .snippet("me").title("me").icon(userMapIcon);

        user_marker = googleMap.addMarker(user_marker_options);

    }

    private void moveMapCameraToLastUsedLocation() {
        user = myPreferences.getUser();
        Double selectedLatitude = Double.parseDouble(user.getTemp_latitude());
        Double selectedLongitude = Double.parseDouble(user.getTemp_longitude());
        lastUsedLocation = new Location("");
        lastUsedLocation.setLatitude(selectedLatitude);
        lastUsedLocation.setLongitude(selectedLongitude);
        moveMapCameraToPosition(lastUsedLocation);

    }

    private void checkForGooglePlayServices() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
        if (status == ConnectionResult.SUCCESS) {
            //Success! Do what you want

        } else {
            //GooglePlayServicesUtil.getErrorDialog(status, mActivity, status);
        }
    }

    private void setCustomMarkersInMap() {

        gps_marker = BitmapDescriptorFactory.fromResource(R.drawable.gps_marker);
        picker_marker = BitmapDescriptorFactory.fromResource(R.drawable.picker__marker);
        stop_marker = BitmapDescriptorFactory.fromResource(R.drawable.red_ring_20);
        home_marker = BitmapDescriptorFactory.fromResource(R.drawable.home_marker);
        work_marker = BitmapDescriptorFactory.fromResource(R.drawable.work_marker);
        vehicle_marker = BitmapDescriptorFactory.fromResource(R.drawable.green_bus);

    }

    public void showProgress() {
        progressView.setVisibility(View.GONE);
        //vehicle_not_found_textview.setVisibility(View.GONE);
        swipe_layout.post(new Runnable() {
            @Override
            public void run() {
                swipe_layout.setRefreshing(true);
            }
        });
    }

    public void stopProgress() {
        progressView.setVisibility(View.GONE);
        swipe_layout.setRefreshing(false);
    }


    private void addVehiclesInList() {

        vehicleListAdapter = new VehicleListAdapter(this, mActivity, vehicles);
        //nativeAdAdapter = new NativeAdAdapter(mActivity, vehicleListAdapter, isAdsLoaded);
        CustomListAdapter adapter = new CustomListAdapter(LayoutInflater.from(getActivity()));

        //bus_listview.setAdapter(nativeAdAdapter);

        //bus_listview.setSelection(adapter.getCount() - 1);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
        choiceMenuItem = menu.findItem(R.id.home);

        sharedVehicleMenuItem = menu.findItem(R.id.actionbar_item);
        MenuItemCompat.setActionView(sharedVehicleMenuItem, R.layout.notification_update_count_layout);
        menuBadgeView = (RelativeLayout) MenuItemCompat.getActionView(sharedVehicleMenuItem);
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
        sharedCount.setText("" + sharedVehiclesCount);

        // show fav menu item if user is loginned

        if (isCmgFromDeepLink || isSharedVehicleModeEnabled) {
            showSharedVehicleScreenForDeepLink();
        } else if (isFavouriteFlagEnabled()) {
            choiceMenuItem.setTitle("All");
            sharedVehicleMenuItem.setVisible(false);
        } else {
            setupOptionsMenu();
        }


    }

    private void showSharedVehicleScreenForDeepLink() {
        ((MainActivity) mActivity).setActionBarTitle(mActivity.getResources().getString(R.string.shared_vehicles));
        ((MainActivity) mActivity).setNavigationItemSelected(1);
        onSharedFilterEnabled();
    }

    private void showSharedVehicleListDialog() {
        android.app.Dialog sharedVehicleDialog = new android.app.Dialog(mActivity);
        sharedVehicleDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sharedVehicleDialog.setContentView(R.layout.shared_vehicle_list_dialg);
        shareVehicleListView = (ListView) sharedVehicleDialog.findViewById(R.id.shared_bus_list);
        deleteSharedVehicleBtn = (ImageView) sharedVehicleDialog.findViewById(R.id.delete);
        deleteSharedVehicleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedVehicles = sharedVehicleListAdapter.getSharedVehicles();
                ArrayList<Vehicle> selectedVehicles = new ArrayList<Vehicle>();
                for (int i = 0; i < sharedVehicles.size(); i++) {
                    Vehicle vehicle = sharedVehicles.get(i);
                    if (vehicle.isSelected()) {
                        selectedVehicles.add(vehicle);
                    }
                }
                sharedVehicles.removeAll(selectedVehicles);
                sharedVehicleListAdapter.notifyDataSetChanged();

                for (Vehicle vehicle : selectedVehicles) {
                    appDatabaseHelper.deleteVehicleFromSharedList(vehicle);
                }

                refreshSharedBadge();
                quickRunGetVehicleService();
            }
        });
        sharedVehicleListAdapter = new SharedVehicleListAdapter(mActivity, this, sharedVehicles);
        shareVehicleListView.setAdapter(sharedVehicleListAdapter);
        sharedVehicleDialog.show();
    }

    public void setupOptionsMenu() {
        sharedVehicles = appDatabaseHelper.getSharedVehicles();
        sharedVehiclesCount = sharedVehicles.size();
        if (myPreferences.isLoginned()) {
            if (isCmgFromDeepLink) {
                choiceMenuItem.setVisible(false);
                if (sharedVehiclesCount > 0) {
                    sharedVehicleMenuItem.setVisible(true);
                } else {
                    sharedVehicleMenuItem.setVisible(false);
                }
            } else {
                if (!isSharedVehicleModeEnabled) {
                    choiceMenuItem.setVisible(true);
                    choiceMenuItem.setTitle("Fav");
                    sharedVehicleMenuItem.setVisible(false);
                } else {
                    choiceMenuItem.setVisible(false);
                    if (sharedVehiclesCount > 0) {
                        sharedVehicleMenuItem.setVisible(true);
                    } else {
                        sharedVehicleMenuItem.setVisible(false);
                    }
                }
            }
        } else {
            sharedVehicleMenuItem.setVisible(false);
            if (isCmgFromDeepLink) {
                choiceMenuItem.setVisible(true);
                choiceMenuItem.setTitle("All");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.home:
                if (!isCmgFromDeepLink) {
                    // NOT COMING FROM DEEP LINK
                    if (myPreferences.isLoginned()) {
                        // IS USER LOGIN\
                        if (isSharedVehicleModeEnabled) {
                            // SHARED VEHICLE MODE ON
                            onSharedFilterDisabled();
                            ((MainActivity) mActivity).setNavigationItemSelected(0);
                        } else if (isFavouriteFlagEnabled()) {
                            // FAVOURITE AND SHARED VEHICLE MODE DISABLE. MOVE TO NORMAL MODE
                            onFavFilterDisabled();
                        } else {
                            // ENABLE FAVOURITE MODE
                            onFavFilterEnabled();
                        }
                    } else {
                        loginDialog.setMessage(mActivity.getResources().getString(R.string.login_dialog_message_favourites));
                        loginDialog.show();
                    }
                } else {
                    // CAME FROM DEEP LINK. MOVE TO NORMAL MODE
                    clearDeepLinkMode();
                }
                break;

            case R.id.filter:
                openFilterDialog();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    private void openFilterDialog() {
        resetFilterDialog();
        filterDialog.show();
    }

    private void clearDeepLinkMode() {
        if (!myPreferences.isLoginned()) {
            choiceMenuItem.setTitle("Fav");
        } else {
            choiceMenuItem.setTitle("Fav");
        }
        isCmgFromDeepLink = false;
        ((MainActivity) mActivity).setCmgFromDeepLink(false);
        if (isSharedVehicleModeEnabled()) {
            onSharedFilterDisabled();
        }
        quickRunGetVehicleService();
    }

    private void onFavFilterDisabled() {
        hideFavVehicleSnackBar();
        setFavouriteFlag(false);
        choiceMenuItem.setTitle(mActivity.getResources().getString(R.string.fav));
        ((MainActivity) mActivity).setActionBarTitle(mActivity.getResources().getString(R.string.Home));
        showProgress();
    }

    public void onFavFilterEnabled() {
        showProgress();
        onSharedFilterDisabled();
        setFavouriteFlag(true);
        choiceMenuItem.setTitle(mActivity.getResources().getString(R.string.all));
        ((MainActivity) mActivity).setActionBarTitle(mActivity.getResources().getString(R.string.favourites));

        teqBuzzFavVehicles = teqBuzzDbHelper.getFavVehicles();
        if (teqBuzzFavVehicles == null || teqBuzzFavVehicles.size() == 0) {
            favVehiclesSnackBar = Utility.showSnackForFavBuses(mActivity, this);
            favVehiclesSnackBar.show(mActivity);
        }
    }

    public void onSharedFilterEnabled() {
        try {
            if (webService != null)
                webService.clearAll();
            if (teqBuzzVehicles != null)
                teqBuzzVehicles.clear();
            if (teqbuzzVehicleListAdapter != null)
                teqbuzzVehicleListAdapter.notifyDataSetChanged();
            if (myPreferences != null) {
                if (!myPreferences.isLoginned()) {
                    onFavFilterDisabled();
                } else {
                    if (isFavouriteFlagEnabled()) {
                        onFavFilterDisabled();
                    }
                }
            }
            isSharedVehicleModeEnabled = true;
            hideVehicleNotFoundSnack();
            if (fabMenu != null)
                fabMenu.setVisibility(View.GONE);
        /*isCmgFromDeepLink = false;
        ((MainActivity) mActivity).setCmgFromDeepLink(false);*/
            ((MainActivity) mActivity).setActionBarTitle(mActivity.getResources().getString(R.string.shared_vehicles));
            if (myPreferences.isLoginned())
                ((MainActivity) mActivity).setNavigationItemSelected(1);
            else
                ((MainActivity) mActivity).setNavigationItemSelected(0);
            AppDatabaseHelper appDatabaseHelper = new AppDatabaseHelper(mActivity);
            sharedVehicles = appDatabaseHelper.getSharedVehicles();
            Log.d("selectSharedVehicleMode", "Getting shared Vehicles");
            //choiceMenuItem.setTitle(mActivity.getResources().getString(R.string.all));
            setupOptionsMenu();
            quickRunGetVehicleService();
            //showProgress();
        } catch (NullPointerException e) {

        }
    }

    public void onSharedFilterDisabled() {
        try {
            isSharedVehicleModeEnabled = false;
            fabMenu.setVisibility(View.VISIBLE);
            //choiceMenuItem.setTitle(mActivity.getResources().getString(R.string.fav));
            ((MainActivity) mActivity).setCmgFromDeepLink(false);
            isCmgFromDeepLink = false;
            ((MainActivity) mActivity).setActionBarTitle(mActivity.getString(R.string.Home));
            setupOptionsMenu();
            quickRunGetVehicleService();
        } catch (NullPointerException e) {

        }
    }

    private void setUpMapForMyLocation() {


        markers = new ArrayList<MarkerOptions>();
        BitmapDescriptor myMarkerIcon = null;
        int mode = myPreferences.getUser().getMode();

        // map markers based on location mode

        if (mode == User.GPS)
            myMarkerIcon = gps_marker;
        else if ((mode == User.PICKER))
            myMarkerIcon = picker_marker;
        else if ((mode == User.HOME))
            myMarkerIcon = home_marker;


        // create marker
        //googleMap.clear();
        user = myPreferences.getUser();
        double_latitude = Double.parseDouble(user.getTemp_latitude());
        double_longitude = Double.parseDouble(user.getTemp_longitude());


        user_marker_options = new MarkerOptions().position(
                new LatLng(double_latitude, double_longitude)).snippet("me").icon(home_marker);

        user_marker = googleMap.addMarker(user_marker_options);

       /* user_circle = googleMap.addCircle(new CircleOptions()
                .center(new LatLng(double_latitude, double_longitude))
                .radius(Constants.CIRCLE_RADIUS)
                .strokeColor(strokeColor)
                .strokeWidth(1)
                .fillColor(shadeColor));*/

        markerArrayList = new ArrayList<Marker>();
        //markerArrayList.add(user_marker);
        user_marker_options.icon(myMarkerIcon);
        //markers.add(user_marker_options);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(double_latitude, double_longitude)).tilt(Constants.MAP_TILT).zoom(Constants.MAP_ZOOM_LEVEL).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


        //redrawLine();

        ((MainActivity) mActivity).hideLocationLoadingProgressBar();

    }

    private void moveToVehicleDetailFragment(int index) {


        Vehicle vehicle = vehicles.get(index);
        Bundle vehicleDetailBundle = new Bundle();
        vehicleDetailBundle.putString("vehicle_id", vehicle.getVehicle_id());
        vehicleDetailBundle.putString("vehicle_line_number", vehicle.getVehicle_line_number());
        vehicleDetailBundle.putString("latitude", vehicle.getLatitude());
        vehicleDetailBundle.putString("longitude", vehicle.getLongitude());
        vehicleDetailBundle.putString("vehicle_agency", vehicle.getVehicle_agency());
        vehicleDetailBundle.putString("images", vehicle.getImages());
        vehicleDetailBundle.putString("vehicle_type", vehicle.getVehicle_type());
        vehicleDetailBundle.putString("date_added", vehicle.getDate_added());
        vehicleDetailBundle.putString("date_modified", vehicle.getDate_modified());
        vehicleDetailBundle.putString("status", vehicle.getStatus());

        ((MainActivity) mActivity).setFragment(7, vehicleDetailBundle);


    }

    public void setFavouriteFlag(boolean fav) {
        ((MainActivity) mActivity).setFavouriteFlag(fav);

    }


    public boolean isFavouriteFlagEnabled() {

        boolean isFav = ((MainActivity) mActivity).getFavouriteFlag();
        return isFav;
    }

    private void removeAllVehicleMarkers() {

        googleMap.clear();
        markerArrayList.clear();
        markers.clear();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        isCmgFromDeepLink = ((MainActivity) mActivity).isCmgFromDeepLink();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
      /*  if (mapFragment != null)
            mapFragment.onResume();
        if (busMapView != null)
            busMapView.onResume();*/

        //registerGetVehicleScheduleListResponseReceiver();
        //registerConnectivityReceiver();

        if (IS_INTERNET_REQUESTED) {
            IS_INTERNET_REQUESTED = false;
            if (Utility.isConnectingToInternet(mActivity)) {
                quickRunGetVehicleService();
                //runGetVe\hicleService(user.getMode(), isFavouriteFlagEnabled());
            } else {
                if (internetSnackBar == null)
                    internetSnackBar = Utility.showSnackForInternet(mActivity, mActivity.getResources().getString(R.string.no_internet), this);
                if (!IS_INTERNET_SNACKBAR_SHOWN) {
                    internetSnackBar.show(mActivity);
                    IS_INTERNET_SNACKBAR_SHOWN = true;
                }
                vehicleLoadingBar.setVisibility(View.GONE);
                vehicle_not_found_textview.setVisibility(View.GONE);
            }
        }

        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapFragment != null)
            mapFragment.onPause();
        if (busMapView != null)
            busMapView.onPause();

        //unRegisterGetVehicleScheduleListResponseReceiver();
        //unRegisterConnectivityReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*if (mapFragment != null)
            mapFragment.onDestroy();*/
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapFragment != null)
            mapFragment.onLowMemory();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


    public void animateMarker(String latitude, String longitude, int teqBuzzVehiclePosition, Vehicle teqBuzzVehicle, final Marker marker, final LatLng toPosition,
                              final boolean hideMarker, final boolean isGpsMarker) {

        LatLngInterpolator latLngInterpolator = new LatLngInterpolator.Linear();
        MarkerAnimation.animateMarkerToICS(this, latitude, longitude, teqBuzzVehiclePosition, teqBuzzVehicle, marker, toPosition, latLngInterpolator, user_circle, isGpsMarker);
        if (isGpsMarker) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(toPosition).tilt(60).zoom(Constants.MAP_ZOOM_LEVEL).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            // user_circle.setCenter(toPosition);
        }

    }


    public void animateSmoothMarker(String latitude, String longitude, int teqBuzzVehiclePosition, Vehicle receivedVehicle, final Marker marker, final LatLng toPosition,
                                    final boolean hideMarker, final boolean isGpsMarker) {

        LatLngInterpolator latLngInterpolator = new LatLngInterpolator.Linear();
        MarkerAnimation.animateMarkerToICS(this, latitude, longitude, teqBuzzVehiclePosition, receivedVehicle, marker, toPosition, latLngInterpolator, user_circle, isGpsMarker);
        if (isGpsMarker) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(toPosition).tilt(60).zoom(Constants.MAP_ZOOM_LEVEL).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            // user_circle.setCenter(toPosition);
        }

    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker, final boolean isGpsMarker) {


        LatLngInterpolator latLngInterpolator = new LatLngInterpolator.Linear();
        MarkerAnimation.animateMarkerToICS(marker, toPosition, latLngInterpolator, user_circle, isGpsMarker);
        if (isGpsMarker) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(toPosition).tilt(60).zoom(Constants.MAP_ZOOM_LEVEL).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            // user_circle.setCenter(toPosition);
        }

    }


    public void updateGPSMarker(Location location) {

        str_latitude = String.valueOf(location.getLatitude());
        str_longitude = String.valueOf(location.getLongitude());


        /*if (!isUserMarkerAnimationEnabled) {
            if (isMapLoaded)
                animateMarker(user_marker, new LatLng(location.getLatitude(), location.getLongitude()), false, true);
        }*/
    }

    public void focusCurrentVehicle(int i) {
        try {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(markerArrayList.get(i).getPosition().latitude, markerArrayList.get(i).getPosition().longitude)).tilt(Constants.MAP_TILT).zoom(Constants.MAP_ZOOM_LEVEL).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        } catch (IndexOutOfBoundsException e) {

        }
        //Toast.makeText(mActivity, String.valueOf(markerArrayList.size()), Toast.LENGTH_SHORT).show();

    }

    public void updateVehiclesList(String jsonResponse, int mode, boolean favouriteFlagEnabled) {

        vehicleListJsonResponse = jsonResponse;
        //stopProgress();
        if (vehicleListJsonResponse.contains("No Vehicle Found") || vehicleListJsonResponse.contains("[]")) {
            WebService.removeGetVehicleServiceRequest();
            IS_GET_VEHICLE_SERVICE_BLOCKED = true;
            stopProgress();
            if (vehicle_not_found_textview.getVisibility() != View.VISIBLE) {
                //Utility.showToast(mActivity, mActivity.getResources().getString(R.string.no_vehicle_found));
                //vehicle_not_found_textview.setVisibility(View.VISIBLE);
                clearPreviousVehicles();

            }
        } else {
            //vehicle_not_found_textview.setVisibility(View.GONE);
            //if (!IS_GET_VEHICLE_SERVICE_BLOCKED) {

            //parseAndAddVehicle(vehicleListJsonResponse, mode, favouriteFlagEnabled);

            //}

        }

    }

    private void parseAndAddVehicle(String jsonResponse, int mode, boolean favouriteFlagEnabled) {


        if (myPreferences.getUser().getMode() == mode && favouriteFlagEnabled == isFavouriteFlagEnabled()) {

            String vehicle_id, vehicle_line_number, latitude, longitude, prev_stop, next_stop, vehicle_agency, images, vehicle_type, date_added, date_modified, status;

            try {
                //jsonResponse = "{\"status\":[{\"vehicle_id\":\"1\",\"vehicle_line_number\":\"1003\",\"latitude\":\"12.931575\",\"longitude\":\"77.609581\",\"vehicle_agency\":\"parveen\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-21 11:12:04\",\"date_modified\":\"2016-04-11 01:38:20\",\"status\":\"0\",\"distance\":\"32.771042327684356\"},{\"vehicle_id\":\"2\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"},{\"vehicle_id\":\"3\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"},{\"vehicle_id\":\"4\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"},{\"vehicle_id\":\"5\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"},{\"vehicle_id\":\"6\",\"vehicle_line_number\":\"1004\",\"latitude\":\"12.937502\",\"longitude\":\"77.585098\",\"vehicle_agency\":\"Government\",\"images\":\"parveen.jpg\",\"vehicle_type\":\"1\",\"date_added\":\"2016-03-30 01:56:25\",\"date_modified\":\"2016-04-05 14:30:59\",\"status\":\"1\",\"distance\":\"35.121664945576725\"}]}";

                jsonResponse.replace("\\\"", "\"");
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray status_array = jsonObject.getJSONArray("status");
                for (int i = 0; i < status_array.length(); i++) {
                    vehicle_id = status_array.getJSONObject(i).getString("vehicle_id");
                    vehicle_line_number = status_array.getJSONObject(i).getString("vehicle_line_number");
                    latitude = status_array.getJSONObject(i).getString("latitude");
                    longitude = status_array.getJSONObject(i).getString("longitude");
                    prev_stop = status_array.getJSONObject(i).getString("prev_stop");
                    next_stop = status_array.getJSONObject(i).getString("next_stop");
                    vehicle_agency = status_array.getJSONObject(i).getString("vehicle_agency");
                    images = status_array.getJSONObject(i).getString("images");
                    vehicle_type = status_array.getJSONObject(i).getString("vehicle_type");
                    date_added = status_array.getJSONObject(i).getString("date_added");
                    date_modified = status_array.getJSONObject(i).getString("date_modified");
                    status = status_array.getJSONObject(i).getString("status");

                    if (vehicleIds.contains(vehicle_id)) {
                        // already vehicle added in list and map
                        for (Vehicle vehicle : vehicles) {
                            if (vehicle.getVehicle_id().equalsIgnoreCase(vehicle_id)) {
                                int index = vehicles.indexOf(vehicle);
                                vehicle.setLatitude(latitude);
                                vehicle.setLongitude(longitude);
                                vehicles.set(index, vehicle);
                            }
                        }
                        // remove
                    } else {

                        // this vehicle not added in list and map
                        tempVehicle = new Vehicle(vehicle_id, vehicle_line_number, latitude, longitude, prev_stop, next_stop, vehicle_agency, images, vehicle_type, date_added, date_modified, status, null);
                        vehicles.add(tempVehicle);

                        vehicleIds.add(vehicle_id);

                    }


                }

                vehicles.add(vehicles.get(0));
                vehicles.add(vehicles.get(0));
                vehicles.add(vehicles.get(0));
                vehicles.add(vehicles.get(0));
                vehicles.add(vehicles.get(0));
                vehicles.add(vehicles.get(0));

                vehicleIds.add(vehicles.get(0).getVehicle_id());
                vehicleIds.add(vehicles.get(0).getVehicle_id());
                vehicleIds.add(vehicles.get(0).getVehicle_id());
                vehicleIds.add(vehicles.get(0).getVehicle_id());
                vehicleIds.add(vehicles.get(0).getVehicle_id());
                vehicleIds.add(vehicles.get(0).getVehicle_id());

                stopProgress();

                vehicleListAdapter.notifyDataSetChanged();

                //nativeAdAdapter.notifyDataSetChanged();


                updateVehicleMarkers();


                // run get vehicle service


            } catch (JSONException e) {

               /* Toast.makeText(mActivity, mActivity.getResources().getString(R.string.some_error_occured),
                        Toast.LENGTH_LONG).show();*/

                e.printStackTrace();
            } catch (NullPointerException e) {

                /*Toast.makeText(mActivity, mActivity.getResources().getString(R.string.some_error_occured),
                        Toast.LENGTH_LONG).show();*/
            }

        }


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!BusMapFragment.IS_GET_VEHICLE_SERVICE_BLOCKED)
                            ((MainActivity) mActivity).runGetVehicleService(myPreferences.getUser().getMode(), isFavouriteFlagEnabled());
                    }
                });
            }
        }).start();

    }

    private void updateVehicleMarkers() {/*

        int vehiclesSize = vehicles.size();
        Vehicle tempVehicle;
        int markersSize = markerArrayList.size();
        Marker tempMarker;

        ArrayList<Marker> newMarkers = new ArrayList<Marker>();
        ArrayList<MarkerOptions> newMarkerOptions = new ArrayList<MarkerOptions>();

        for (int i = 0; i < vehiclesSize; i++) {
            tempVehicle = vehicles.get(i);

            String vehicle_id = tempVehicle.getVehicle_id();
            String latitude = tempVehicle.getLatitude();
            String longitude = tempVehicle.getLongitude();

            for (int j = 0; j < markersSize; j++) {
                tempMarker = markerArrayList.get(j);

                if (vehicle_id.equalsIgnoreCase(tempMarker.getTitle())) {
                    LatLng tempLatLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    newMarkers.add(tempMarker);
                    // if (marker.getPosition() != tempLatLng)
                    //i++;
                    j = markersSize;
                    animateMarker(teqBuzzVehiclePosition, teqBuzzVehicle, tempMarker, tempLatLng, false, false);


                } else {
                    if (j == markersSize - 1) {

                        tempMarkerOptions = new MarkerOptions().position(
                                new LatLng(Double.parseDouble(latitude),
                                        Double.parseDouble(longitude)))
                                .snippet(vehicle_id).title(vehicle_id);

                        tempMarker = googleMap.addMarker(tempMarkerOptions);
                        newMarkers.add(tempMarker);
                        newMarkerOptions.add(tempMarkerOptions);

                    } else {

                    }

                }


            }

            if (markerArrayList.size() == 0) {
                tempMarkerOptions = new MarkerOptions().position(
                        new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
                        .snippet(vehicle_id).title(vehicle_id).icon(vehicle_marker);

                tempMarker = googleMap.addMarker(tempMarkerOptions);
                newMarkers.add(tempMarker);
                // markerArrayList.add(tempMarker);
                //tempMarkerOptions.icon(myMarkerIcon);
                newMarkerOptions.add(tempMarkerOptions);
            }

        }


        markers.clear();
        markers.addAll(newMarkerOptions);
        markerArrayList.clear();
        markerArrayList.addAll(newMarkers);


        // is favourite bus flag selected, move the user marker to a nearby favourite bus
        if (isFavouriteFlagEnabled()) {
            double fav_bus_latitude = Double.parseDouble(vehicles.get(0).getLatitude());
            double fav_bus_longitude = Double.parseDouble(vehicles.get(0).getLatitude());
            isUserMarkerAnimationEnabled = true;
            animateMarker(teqBuzzVehiclePosition, teqBuzzVehicle, user_marker,
                    new LatLng(fav_bus_latitude, fav_bus_longitude), false, true);
        }

*/
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
        //onBusListItemSelcted(index - 1);// reduce 1 due to header in it

       /* ArrayList<OBAStopListEntity.Stop> obaStops = new ArrayList<OBAStopListEntity.Stop>();
        if (view.getParent() == bus_listview) {
            //moveToSelectedTeqBuzzVehicle(index);

        } else if (view.getParent() == schedule_list) {
            *//*schedule_list.setItemChecked(index - 1, true);
            Location location = new Location("");
            location.setLatitude(Double.parseDouble(schedules.get(index - 1).getStop_latitude()));
            location.setLongitude(Double.parseDouble(schedules.get(index - 1).getStop_longitude()));
            mDrawerLayout.closeDrawers();
            moveMapCameraToPosition(location);*//*
        }*/
    }

    private void moveToSelectedTeqBuzzVehicle(int index) {
        Vehicle selectedVehicle = teqBuzzVehicles.get(index);
        Location selectedVehicleLocation = new Location("");
        selectedVehicleLocation.setLatitude(Double.parseDouble(selectedVehicle.getLatitude()));
        selectedVehicleLocation.setLongitude(Double.parseDouble(selectedVehicle.getLongitude()));
        moveMapCameraToPosition(selectedVehicleLocation);
    }

    private void onVehicleSelectedFromListView(String tripId) {

        new OBAWebService(mActivity).getObaTripDetailsForTrip(tripId, this);
    }

    private void drawOBARouteForStops(ArrayList<OBAStopListEntity.Stop> obaStops) {


        PolylineOptions options = new PolylineOptions().width(10).color(mActivity.getResources().getColor(R.color.orange)).geodesic(true);
        for (OBAStopListEntity.Stop stop : obaStops) {
            Double stopLatitude = Double.parseDouble(stop.getLat());
            Double stopLongitude = Double.parseDouble(stop.getLon());
            LatLng point = new LatLng(stopLatitude, stopLongitude);
            options.add(point);

        }
        googleMap.addPolyline(options);


    }

    public void onBusListItemSelcted(int index) {
        fabMenu.close(true);
        clickedVehicleIndex = index;
        if (teqBuzzDbHelper.isVehicleFav(teqBuzzVehicles.get(clickedVehicleIndex))) {
            isFavText.setText("Remove from Fav");
        } else {
            isFavText.setText("Add as Fav");
        }
        vehicleOptionDialog.show();


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
       /* focusCurrentVehicle(i);
        BusMapFragment.isUserMarkerAnimationEnabled = true;*/
        return false;
    }

    private void setVehicleSelected(int i) {

        /*selectedVehiclePosition = i;
        selectedVehicleId = vehicles.get(selectedVehiclePosition).getVehicle_id();
        vehicleListAdapter.setSelectedVehicle(selectedVehicleId);
        vehicleListAdapter.notifyDataSetChanged();*/
    }

    private void openVehicleDetailFragment(Vehicle vehicle) {
        mDrawerLayout.openDrawer(Gravity.RIGHT);
        schedules = new ArrayList<Schedule>();
        vehicleScheduleAdapter.notifyDataSetChanged();
        webService.getVehicleScheduleList(mActivity, vehicle.getVehicle_id(), this);
        isDrawerOpened = true;
    }

    public void closeVehicleDetailFragment() {

        mDrawerLayout.closeDrawers();
        isDrawerOpened = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean isDrawerOpened() {
        return isDrawerOpened;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_pick:
                clearSelectedVehicle();
                if (Utility.isConnectingToInternet(mActivity)) {
                    openPicker();
                } else {
                    if (internetSnackBar == null)
                        internetSnackBar = Utility.showSnackForInternet(mActivity, mActivity.getResources().getString(R.string.no_internet), this);
                    if (!IS_INTERNET_SNACKBAR_SHOWN) {
                        internetSnackBar.show(mActivity);
                        IS_INTERNET_SNACKBAR_SHOWN = true;
                    }
                }
                break;

            case R.id.fab_gps:
                clearSelectedVehicle();
                if (Utility.isConnectingToInternet(mActivity)) {
                    if (Utility.isGpsEnabled(mActivity)) {
                        //showProgress();
                        comeOutOfPicker();
                    } else
                        ((MainActivity) mActivity).showLocationSelectionDialogInMap();
                } else {
                    if (internetSnackBar == null)
                        internetSnackBar = Utility.showSnackForInternet(mActivity, mActivity.getResources().getString(R.string.no_internet), this);
                    if (!IS_INTERNET_SNACKBAR_SHOWN) {
                        internetSnackBar.show();
                        IS_INTERNET_SNACKBAR_SHOWN = true;
                    }
                }

                break;

            case R.id.fab_home:
                clearSelectedVehicle();
                if (Utility.isConnectingToInternet(mActivity)) {
                    //showProgress();
                    if (myPreferences.isLoginned()) {
                        setHomeLocationForMap();
                    } else {
                        loginDialog.setMessage(mActivity.getResources().getString(R.string.login_dialog_msg));
                        loginDialog.show();
                    }
                } else {
                    if (internetSnackBar == null)
                        internetSnackBar = Utility.showSnackForInternet(mActivity, mActivity.getResources().getString(R.string.no_internet), this);
                    if (!IS_INTERNET_SNACKBAR_SHOWN) {
                        internetSnackBar.show(mActivity);
                        IS_INTERNET_SNACKBAR_SHOWN = true;
                    }

                }

                break;

            case R.id.fab_work:
                clearSelectedVehicle();
                if (Utility.isConnectingToInternet(mActivity)) {
                    //showProgress();
                    if (myPreferences.isLoginned()) {
                        setWorkLocationForMap();
                    } else {
                        loginDialog.setMessage(mActivity.getResources().getString(R.string.login_dialog_msg));
                        loginDialog.show();
                    }

                } else {
                    if (internetSnackBar == null)
                        internetSnackBar = Utility.showSnackForInternet(mActivity, mActivity.getResources().getString(R.string.no_internet), this);
                    if (!IS_INTERNET_SNACKBAR_SHOWN) {
                        internetSnackBar.show();
                        IS_INTERNET_SNACKBAR_SHOWN = true;
                    }
                }
                break;

            default:
                break;
        }
    }

    private void setHomeLocationForMap() {
        user = myPreferences.getUser();
        str_home_latitude = user.getHomeLatitude();
        str_home_longitude = user.getHomeLongitude();
        double homeLatitude = Double.parseDouble(str_home_latitude);
        double homeLongitude = Double.parseDouble(str_home_longitude);
        Location homeLocation = new Location("");
        homeLocation.setLatitude(homeLatitude);
        homeLocation.setLongitude(homeLongitude);
        if (Utility.isLocationValid(homeLocation)) {
            user.setMode(User.HOME);
            user.setTemp_latitude(str_home_latitude);
            user.setTemp_longitude(str_home_longitude);
            myPreferences.updateUser(user);
            animateMarker(user_marker, new LatLng(Double.valueOf(str_home_latitude), Double.valueOf(str_home_longitude)), false, true);
            isUserMarkerAnimationEnabled = true;
            setUserMarkerBasedOnMode();
            // quick run get vehicle service
            quickRunGetVehicleService();
        } else {
            homeWorkLocationDialog.setMessage(homeLocationDialogMsg);
            homeWorkLocationDialog.show();
        }
    }

    private void setWorkLocationForMap() {

        user = myPreferences.getUser();
        str_work_latitude = user.getWork_latitude();
        str_work_longitude = user.getWork_longitude();
        double workLatitude = Double.parseDouble(str_work_latitude);
        double workLongitude = Double.parseDouble(str_work_longitude);
        Location workLocation = new Location("");
        workLocation.setLatitude(workLatitude);
        workLocation.setLongitude(workLongitude);
        if (Utility.isLocationValid(workLocation)) {
            user.setMode(User.WORK);
            user.setTemp_latitude(str_work_latitude);
            user.setTemp_longitude(str_work_longitude);
            myPreferences.updateUser(user);
            animateMarker(user_marker, new LatLng(Double.valueOf(str_work_latitude), Double.valueOf(str_work_longitude)), false, true);
            isUserMarkerAnimationEnabled = true;
            setUserMarkerBasedOnMode();
            // quick run get vehicle service
            quickRunGetVehicleService();
        } else {
            homeWorkLocationDialog.setMessage(workLocationDialogMsg);
            homeWorkLocationDialog.show();
        }

    }

    private void setUserMarkerBasedOnMode() {
        user = myPreferences.getUser();
        int mode = user.getMode();
        if (mode == User.GPS) {
            user_marker.setIcon(gps_marker);
        } else if (mode == User.PICKER) {
            user_marker.setIcon(picker_marker);
        } else if (mode == User.HOME) {
            user_marker.setIcon(home_marker);
        } else if (mode == User.WORK) {
            user_marker.setIcon(work_marker);
        }
    }

    private void comeOutOfPicker() {
        isUserMarkerAnimationEnabled = false;
        myPreferences = new Preferences(mActivity.getApplicationContext());
        user = myPreferences.getUser();
        str_gps_latitude = user.getGps_latitude();
        str_gps_longitude = user.getGps_longitude();
        myPreferences.updateUser(user);
        if (Utility.isLocationValid(str_gps_latitude, str_gps_longitude)) {
            user = myPreferences.getUser();
            user.setMode(User.GPS);
            user.setTemp_latitude(str_gps_latitude);
            user.setTemp_longitude(str_gps_longitude);
            myPreferences.updateUser(user);
            // clearPreviousVehicles();
            //user_marker.setIcon(gps_marker);
            animateMarker(user_marker, new LatLng(Double.valueOf(str_gps_latitude), Double.valueOf(str_gps_longitude)), false, true);
            // quick run get vehicle service
            setUserMarkerBasedOnMode();
            quickRunGetVehicleService();
        } else {
            Location location = ((MainActivity) mActivity).getGpsLocation();
            if (Utility.isLocationValid(location)) {
                lastUsedLocation = location;
                str_gps_latitude = String.valueOf(location.getLatitude());
                str_gps_longitude = String.valueOf(location.getLongitude());
                user = myPreferences.getUser();
                user.setMode(User.GPS);
                user.setTemp_latitude(str_gps_latitude);
                user.setTemp_longitude(str_gps_longitude);
                myPreferences.updateUser(user);
                animateMarker(user_marker, new LatLng(Double.valueOf(str_gps_latitude), Double.valueOf(str_gps_longitude)), false, true);
                // quick run get vehicle service
                setUserMarkerBasedOnMode();
                quickRunGetVehicleService();

            } else {
                Utility.showToast(mActivity, mActivity.getResources().getString(R.string.please_wait_location_loading));
            }

        }


    }

    private void clearPreviousVehicles() {

        vehicleListJsonResponse = "";
        WebService.removeGetVehicleServiceRequest();
        IS_GET_VEHICLE_SERVICE_BLOCKED = false;
        ((MainActivity) mActivity).runGetVehicleService(myPreferences.getUser().getMode(), isFavouriteFlagEnabled());
        vehicles.clear();
        vehicleIds.clear();
        vehicleListAdapter.notifyDataSetChanged();
        removeAllVehicleMarkers();
        setUpMapForMyLocation();
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
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                mapFragment.onResume();

                Place place = PlacePicker.getPlace(data, mActivity);
                String toastMsg = String.format("Place: %s", place.getName());

                isUserMarkerAnimationEnabled = true;
                str_picker_latitude = String.valueOf(place.getLatLng().latitude);
                str_picker_longitude = String.valueOf(place.getLatLng().longitude);
                user = myPreferences.getUser();
                user.setTemp_latitude(str_picker_latitude);
                user.setTemp_longitude(str_picker_longitude);
                user.setMode(User.PICKER);
                myPreferences.updateUser(user);
                showProgress();
                animateMarker(user_marker, place.getLatLng(), false, true);

                setUserMarkerBasedOnMode();

            }
        } else {

        }

    }


    public void showHomeWorkIcon(int isVisible) {
        fabHome.setVisibility(isVisible);
        fabWork.setVisibility(isVisible);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onWebServiceSuccess(String response) {
        Log.d("", "");
        progress_container.setVisibility(View.GONE);

        if (response.contains("schedule_id")) {

            // parseAndAddSchedules(response);

        } else if (response.contains("No vehicle schedule")) {

        }
    }

    @Override
    public void onWebServicefailed(String response) {
        Log.d("", "");
    }


    @Override
    public void onOBANearByRoutesLoaded(String routeResponse, OBAStopEntity obaStopEntity) {
        Log.e("routes response", routeResponse);
        Utility.showToast(mActivity, new OBAJsonParser().parseAndGetRouteEntity(routeResponse).getCode());
        addMarkerForRouteEntity(obaStopEntity);

    }

    @Override
    public void onOBAStopsForRoutesLoaded(String res, ArrayList<OBAStopListEntity> obaStopListEntities) {

        if (obaStopListEntities != null) {
            int count = 0;
            ArrayList<OBAStopListEntity.Stop> obaRouteListStops = new ArrayList<OBAStopListEntity.Stop>();
            for (OBAStopListEntity obaStopListEntity : obaStopListEntities) {
                if (obaStopListEntity != null)
                    obaRouteListStops.addAll(obaStopListEntity.getStops());
            }
            isOBAStopsForRoutesLoaded = true;
            this.obaRouteListStops = obaRouteListStops;

            for (OBAStopListEntity.Stop stop : obaRouteListStops) {
                String routeId = stop.getRoute_id();
                if (routeId.contains("1_100259")) {
                    count++;
                }

            }


            drawOBAMarkerForStops(obaRouteListStops);
        } else {
            Utility.showSnack(mActivity, "stops not loaded");
        }


    }

    private void drawOBAMarkerForStops(ArrayList<OBAStopListEntity.Stop> obaRouteListStops) {

        isOBAStopsForRoutesLoaded = true;
        this.obaRouteListStops = obaRouteListStops;
        for (OBAStopListEntity.Stop stop : obaRouteListStops) {
            Double stopLatitude = Double.parseDouble(stop.getLat());
            Double stopLongitude = Double.parseDouble(stop.getLon());
            LatLng point = new LatLng(stopLatitude, stopLongitude);

            MarkerOptions obaStopsMarkerOptions = new MarkerOptions().position(point).title(Constants.OBA_MARKER + String.valueOf(stop.getId()))
                    .snippet(String.valueOf(stop.getId())).icon(stop_marker);
            googleMap.addMarker(obaStopsMarkerOptions);

        }

    }

    private void addMarkerForRouteEntity(OBAStopEntity obaStopEntity) {

        ArrayList<OBAStopEntity.List> routeLists = obaStopEntity.getData().getLists();
        Log.e("", "");
    }

    public void onOBARoutesLoaded(OBAStopEntity obaStopEntity) {
      /*  ArrayList<OBAStopEntity.List> stopLists = obaStopEntity.getData().getLists();

        for (OBAStopEntity.List list : stopLists) {
            MarkerOptions schedulesMarkerOptions = new MarkerOptions().position(
                    new LatLng(Double.parseDouble(list.getLat()),
                            Double.parseDouble(list.getLon())))
                    .snippet(list.getId()).title(Constants.OBA_MARKER).icon(stop_marker);

            Marker tempScheduleMarker = googleMap.addMarker(schedulesMarkerOptions);

        }
        Utility.showToast(mActivity, String.valueOf(stopLists.size()));*/
    }


    public void onOBARouteListLoaded(OBARouteEntity obaRouteEntity) {

        Utility.showToast(mActivity, String.valueOf("Route size is " + obaRouteEntity.getData().getList().size()));
        ArrayList<OBARouteEntity.Data.Route> routes = obaRouteEntity.getData().getList();

        moveToSelectedRegion(OBAConstants.TEST_ROUTE_LIST_LATITUDE, OBAConstants.TEST_ROUTE_LIST_LONGITUDE);
        ((MainActivity) mActivity).getStopListForRoute(routes);

    }

    private void moveToSelectedRegion(String testRouteListLatitude, String testRouteListLongitude) {
        double latitude = Double.parseDouble(testRouteListLatitude);
        double longitude = Double.parseDouble(testRouteListLongitude);
        Location selectedRegionLocation = new Location("");
        selectedRegionLocation.setLatitude(latitude);
        selectedRegionLocation.setLongitude(longitude);
        moveMapCameraToPosition(selectedRegionLocation);

    }

    public void onOBAArrivalDepartureEntityLoaded(OBAArrivalDepartureEntity obaArrivalDepartureEntity) {
        if (obaArrivalDepartureEntity != null) {
            arrivalAndDepartures = obaArrivalDepartureEntity.getData().getArrivalAndDeparture();
            arrivalsAndDepartureAdapter = new ArrivalsAndDepartureAdapter(mActivity, arrivalAndDepartures);
            bus_listview.setAdapter(arrivalsAndDepartureAdapter);
            //addOBABusMarkers(arrivalAndDepartures);
        } else {
            if (internetSnackBar == null)
                internetSnackBar = Utility.showSnackForInternet(mActivity, mActivity.getResources().getString(R.string.no_internet), this);
            if (!IS_INTERNET_SNACKBAR_SHOWN) {
                internetSnackBar.show();
                IS_INTERNET_SNACKBAR_SHOWN = true;
            }
            //Utility.showSnack(mActivity, "No Arrivals found");
        }
    }

    private void addOBABusMarkers(ArrayList<OBAArrivalDepartureEntity.Data.ArrivalAndDeparture> arrivalAndDepartures) {

        ArrayList<OBAVehicle> obaVehicles = new ArrayList<OBAVehicle>();

        for (OBAArrivalDepartureEntity.Data.ArrivalAndDeparture arrivalAndDeparture :
                arrivalAndDepartures) {

            OBAVehicle obaVehicle = new OBAVehicle();
            obaVehicle.setId(arrivalAndDeparture.getVehicleId());
            obaVehicle.setLat(arrivalAndDeparture.getTripStatus().getPosition().getLat());
            obaVehicle.setLon(arrivalAndDeparture.getTripStatus().getPosition().getLon());
            obaVehicles.add(obaVehicle);

        }


        for (OBAVehicle obaVehicle : obaVehicles) {
            MarkerOptions vehicleMarker = getMarkerForVehicle(obaVehicle);
            googleMap.addMarker(vehicleMarker);
        }

    }

    public void onOBAStopsForRouteLoaded(String res, OBAStopListEntity obaStopListEntity) {
        if (obaStopListEntity != null) {
            ArrayList<OBAStopListEntity.Data.Entry.PolyLine> obaPolyLines = obaStopListEntity.getData().getEntry().getPolyLines();
            drawOBARouteForPolylines(obaPolyLines);
        } else {
            Utility.showToast(mActivity, "obaStopListEntity is null");
        }

    }

    private void drawOBARouteForPolylines(ArrayList<OBAStopListEntity.Data.Entry.PolyLine> obaPolyLines) {
        List<LatLng> stopLatLngs = new ArrayList<LatLng>();
        int i = 0;
        vehicleRoutePolylines = new ArrayList<Polyline>();
        for (OBAStopListEntity.Data.Entry.PolyLine obaPolyLine : obaPolyLines) {

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            obaRoutePolyLines = new PolylineOptions().width(10).color(mActivity.getResources().getColor(R.color.colorPrimary)).geodesic(true);
            List<LatLng> stopLatLng = Utility.decodePoly(obaPolyLine.getPoints(), Integer.parseInt(obaPolyLine.getLength()));
            obaRoutePolyLines.addAll(stopLatLng);
            vehicleRoutePolylines.add(googleMap.addPolyline(obaRoutePolyLines));
            stopLatLngs.addAll(stopLatLng);
        }

        CameraPosition firstSchedulePosition = new CameraPosition.Builder()
                .target(stopLatLngs.get(0)).
                        tilt(Constants.MAP_TILT).zoom(Constants.MAP_ZOOM_LEVEL).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(firstSchedulePosition));

    }

    private Marker addMarkerForLocation(GoogleMap googleMap, double latitude, double longitude, String id, BitmapDescriptor icon) {
        MarkerOptions schedulesMarkerOptions = new MarkerOptions().position(
                new LatLng(latitude, longitude))
                .snippet("stop").title(id).icon(icon);
        Marker marker = googleMap.addMarker(schedulesMarkerOptions);
        return marker;
    }

    private Marker addMarkerForTeqBuzzStop(GoogleMap googleMap, double latitude, double longitude, String id, BitmapDescriptor icon) {
        MarkerOptions schedulesMarkerOptions = new MarkerOptions().position(
                new LatLng(latitude, longitude))
                .snippet("stop").title(id).icon(icon);
        Marker marker = googleMap.addMarker(schedulesMarkerOptions);
        return marker;
    }

    private Marker addMarkerForTeqBuzzVehicle(Vehicle vehicle) {
        MarkerOptions schedulesMarkerOptions = new MarkerOptions().position(
                new LatLng(Double.parseDouble(vehicle.getLatitude()), Double.parseDouble(vehicle.getLongitude())))
                .title(Constants.TEQBUZZ_MARKER + "_" + vehicle.getVehicle_id()).icon(vehicle_marker);
        Marker marker = googleMap.addMarker(schedulesMarkerOptions);
        marker.setTag(vehicle);
        return marker;
    }

    public Bitmap GetBitmapMarker(Context mContext, int resourceId, String mText) {
        try {
            Resources resources = mContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);

            android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

            // set default bitmap config if none
            if (bitmapConfig == null)
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;

            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.BLACK);
            paint.setTextSize((int) (7 * scale));
            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);

            // draw text to the Canvas center
            Rect bounds = new Rect();
            paint.getTextBounds(mText, 0, mText.length(), bounds);
            int x = (bitmap.getWidth() - bounds.width()) / 2;
            int y = (bitmap.getHeight() + bounds.height()) / 2;

            canvas.drawText(mText, x * scale, y * scale, paint);

            return bitmap;

        } catch (Exception e) {
            return null;
        }
    }


    public void onOBATripsForRouteLoaded(String res, OBATripsForRouteEntity obaTripsForRouteEntity) {
        if (obaTripsForRouteEntity != null)
            drawOBAMarkerForTrips(obaTripsForRouteEntity);

        // run again for service
        ((MainActivity) mActivity).getOBATripsForRoute(selectedRouteId);
    }

    private void drawOBAMarkerForTrips(OBATripsForRouteEntity obaTripsForRouteEntity) {
        obaTrips = obaTripsForRouteEntity.getData().getTrips();
        for (OBATripsForRouteEntity.Data.Trip obaTrip : obaTrips) {
            OBATripsForRouteEntity.Data.Trip.Status.Position obaPosition = obaTrip.getStatus().getPosition();
            drawOBAMarkerForVehiclePosition(obaPosition, Constants.OBA_TRIP_MARKER + obaTrip.getTripId());
        }
    }

    private void drawOBAMarkerForVehiclePosition(OBATripsForRouteEntity.Data.Trip.Status.Position obaPosition, String tripId) {
        Double latitude = Double.parseDouble(obaPosition.getLat());
        Double longitude = Double.parseDouble(obaPosition.getLon());
        addMarkerForLocation(googleMap, latitude, longitude, tripId, vehicle_marker);

    }

    public void onOBATripsForLocationLoaded(String res, OBATripsForLocationEntity obaTripsForLocationEntity) {
        obaTripsForLocations = obaTripsForLocationEntity.getData().getTrips();
        addOBAVehicleMarkersForTrips(obaTripsForLocations);
        bus_listview.setAdapter(new OBAVehiclesAdapter(mActivity, obaTripsForLocations));
        Location targetLocation = new Location("");
        targetLocation.setLatitude(OBAConstants.TEST_BUS_LIST_LATITUDE);
        targetLocation.setLongitude(OBAConstants.TEST_BUS_LIST_LONGITUDE);
        moveMapCameraToPosition(targetLocation);
    }

    private void addOBAVehicleMarkersForTrips(ArrayList<OBATripsForLocationEntity.Data.Trip> obaTripsForLocations) {
        obaVehicleMarkers = new ArrayList<Marker>();
        for (OBATripsForLocationEntity.Data.Trip obaTripForLocation : obaTripsForLocations) {
            // create marker for trip vehicle
            String obaTripLatitude = obaTripForLocation.getStatus().getPosition().getLat();
            String obaTripLongitude = obaTripForLocation.getStatus().getPosition().getLon();
            LatLng vehicleLatLng = new LatLng(Double.parseDouble(obaTripLatitude), Double.parseDouble(obaTripLongitude));

            MarkerOptions obaTripsForLocationMarkerOptions = new MarkerOptions().position(vehicleLatLng)
                    .title(Constants.OBA_VEHICLE_MARKER + "_" + obaTripForLocation.getTripId())
                    .icon(vehicle_marker);
            obaVehicleMarkers.add(googleMap.addMarker(obaTripsForLocationMarkerOptions));
        }
        // Start Vehicle Location Update Service
        startOBAVehicleLocationUpdateService(obaTripsForLocations, obaVehicleMarkers);
    }

    private void startOBAVehicleLocationUpdateService(ArrayList<OBATripsForLocationEntity.Data.Trip> obaTripsForLocations, ArrayList<Marker> obaVehicleMarkers) {
        for (OBATripsForLocationEntity.Data.Trip obaTrip : obaTripsForLocations) {
            int indexOfMarker = obaTripsForLocations.indexOf(obaTrip);
            String vehicleId = obaTrip.getStatus().getVehicleId();
            getObaTripDetailsForVehicle(vehicleId, obaVehicleMarkers.get(indexOfMarker));
        }
    }

    private void getObaTripDetailsForVehicle(String vehicleId, Marker vehicleMarker) {
        new OBAWebService(mActivity).getObaTripDetailsForVehicle(vehicleId, vehicleMarker, this);
    }

    public void onOBATripDetailsForVehicleEntity(Object o, OBATripDetailsForVehicleEntity obaTripDetailsForVehicleEntity, Marker vehicleMarker) {
        if (obaTripDetailsForVehicleEntity != null) {

            double oldLatitude = vehicleMarker.getPosition().latitude;
            double oldLongitude = vehicleMarker.getPosition().longitude;

            double newLatitude = Double.parseDouble(obaTripDetailsForVehicleEntity.getData().getEntry().getStatus().getPosition().getLat());
            double newLongitude = Double.parseDouble(obaTripDetailsForVehicleEntity.getData().getEntry().getStatus().getPosition().getLon());

            if (oldLatitude == newLatitude && oldLongitude == newLongitude) {

            } else {
                animateMarker(vehicleMarker, new LatLng(newLatitude, newLongitude), false, false);
                //Utility.showToast(mActivity, "vehicle moving");
            }

            String vehicleId = obaTripDetailsForVehicleEntity.getData().getEntry().getStatus().getVehicleId();
            new OBAWebService(mActivity).getObaTripDetailsForVehicle(vehicleId, vehicleMarker, this);
        }
        Log.e("aaa", "aaa");
        Log.e("aaa", "aaa");
    }

    public void onOBATripDetailsForTripEntity(String res, OBATripDetailsForTripEntity obaTripDetailsForVehicleEntity) {

        drawOBAMarkersForStopsFromTripDetails(obaTripDetailsForVehicleEntity);
    }

    private void drawOBAMarkersForStopsFromTripDetails(OBATripDetailsForTripEntity obaTripDetailsForVehicleEntity) {
        ArrayList<OBATripDetailsForTripEntity.Data.Reference.Stop> obaStops = obaTripDetailsForVehicleEntity.getData().getReference().getStops();

        ArrayList<String> routeIds = new ArrayList<String>();
        for (OBATripDetailsForTripEntity.Data.Reference.Stop obaStop : obaStops) {
            Double stopLatitude = Double.parseDouble(obaStop.getLat());
            Double stopLongitude = Double.parseDouble(obaStop.getLon());
            routeIds.addAll(obaStop.getRouteIds());
            addMarkerForLocation(googleMap, stopLatitude, stopLongitude, obaStop.getId(), stop_marker);
        }

        for (int i = 0; i + 1 < obaStops.size(); i++) {
            LatLng origin = new LatLng(Double.parseDouble(obaStops.get(i).getLat()), Double.parseDouble(obaStops.get(i).getLon()));
            LatLng dest = new LatLng(Double.parseDouble(obaStops.get(i + 1).getLat()), Double.parseDouble(obaStops.get(i + 1).getLon()));

            String url = Utility.getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            downloadTask.execute(url);
        }

        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(routeIds);
        routeIds.clear();
        routeIds.addAll(hashSet);

    }

    public void onTeqBuzzVehicleListEntityLoaded(VehicleListEntity vehicleListEntity, int callMode) {
        //first time vehicle added
        Log.d("getVehicleListMarch", "onTeqBuzzVehicleListEntityLoaded");
        GET_VEHICLE_SERVICE_DELAY = 10000;
        if (callMode == CALL_MODE) {
            stopProgress();
            hideVehicleLoadingView();
            if (vehicleListEntity != null) {
                ArrayList<Vehicle> receivedVehicles = vehicleListEntity.getVehicles();
                if (receivedVehicles != null && receivedVehicles.size() > 0) {
                    hideProgressBar();
                    hideVehicleNotFoundSnack();
                    if (!isCmgFromDeepLink) {
                        receivedVehicles = sortDistanceWise(receivedVehicles);
                    }
                    if (isFavouriteFlagEnabled()) {
                        //Utility.showToast(mActivity, "fav part executed");
                        //receivedVehicles = removeNonFavBuses(receivedVehicles);
                        for (Marker marker : teqBuzzVehicleMarkers) {
                            if (marker.getTag() != null) {
                                Log.d("maarkertag before", String.valueOf(marker.getTag()));
                            } else {
                                Log.d("markertag before", "marker is null");
                            }
                        }
                        teqBuzzVehicleMarkers = removeNonFavMarkers(receivedVehicles);
                        hideProgressBar();
                    }
                    for (Marker marker : teqBuzzVehicleMarkers) {
                        if (marker.getTag() != null) {
                            Log.d("markertag after", String.valueOf(marker.getTag()));
                        } else {
                            Log.d("markertag after", "marker is null");
                        }
                    }
                    if (teqBuzzVehicles != null && teqBuzzVehicles.size() > 0) {
                        //already vehicle loaded
                        if (vehicleNotFoundSnackBar.isShown()) {
                            vehicleNotFoundSnackBar.dismiss();
                        }
                        hideFavVehicleSnackBar();
                        try {
                            //updateTeqBuzzVehicles(receivedVehicles);
                            updateNewTeqbuzzVehicles(receivedVehicles);
                        } catch (IndexOutOfBoundsException e) {
                            Log.e(TAG, "IndexOutOfBoundsException while updating vehicles");
                        }
                    } else {
                        addTeqBuzzMarkersForVehicles(receivedVehicles);
                    }
                    isErrorOccured = false;
                    //adding missing markers
                    if (teqBuzzVehicles.size() != teqBuzzVehicleMarkers.size()) {
                        for (Vehicle vehicle : teqBuzzVehicles) {
                            String vehicleId = vehicle.getVehicle_id();
                            if (!teqBuzzVehicleMarkerHash.keySet().contains(vehicleId)) {
                                Marker teqBuzzMarker = addMarkerForTeqBuzzVehicle(vehicle);
                                teqBuzzVehicleMarkers.add(teqBuzzMarker);
                                teqBuzzVehicleMarkerHash.put(vehicle.getVehicle_id(), teqBuzzMarker);

                            }
                        }
                    }
                } else {
                    // No vehicle from server
                    teqBuzzVehicles.clear();
                    teqbuzzVehicleListAdapter.notifyDataSetChanged();
                    Log.d(TAG, "vehicle list cleared");
                    if (teqBuzzVehicles == null || teqBuzzVehicles.size() <= 0) {
                        showVehicleNotFoundSnack();
                    }
                    // run get vehicle service again
               /* if (!isErrorOccured)
                    Utility.showToast(mActivity, mActivity.getResources().getString(R.string.some_error_occured));
*/
                    if (Utility.isConnectingToInternet(mActivity))
                        runGetVehicleService(user.getMode(), isFavouriteFlagEnabled());
                }
            } else {
                // No vehicle from server
                teqBuzzVehicles.clear();
                teqbuzzVehicleListAdapter.notifyDataSetChanged();
                Log.d(TAG, "vehicle list cleared");
                showVehicleNotFoundSnack();

                if (Utility.isConnectingToInternet(mActivity))
                    runGetVehicleService(user.getMode(), isFavouriteFlagEnabled());
            }
        }
    }

    private ArrayList<Marker> removeNonFavMarkers(ArrayList<Vehicle> receivedVehicles) {
        ArrayList<Marker> tempMarkers = new ArrayList<Marker>();
        for (Vehicle vehicle : receivedVehicles) {
            for (Marker marker : teqBuzzVehicleMarkers) {
                String listVehicleId = vehicle.getVehicle_id();
                String markerVehicleId = marker.getTitle().replace("teqbuzz_marker_", "");
                //if (markerVehicle != null) {
                //  String markerVehicleId = markerVehicle.getVehicle_id();
                if (listVehicleId.equalsIgnoreCase(markerVehicleId)) {
                    // Fav vehicle and marker
                    tempMarkers.add(marker);
                    Log.d("markerremoved", "marker NOT removed");
                } else {
                    Log.d("markerremoved", "marker removed");
                    //marker.remove();
                }
            } /*else {
                    Log.d("teqBuzzVehicleMarkers", "marker is null");
                }*/
        }
        //}
        ArrayList<Marker> markers = new ArrayList<Marker>();
        teqBuzzVehicleMarkers.removeAll(tempMarkers);
        for (Marker marker : teqBuzzVehicleMarkers) {
            marker.remove();
        }
        teqBuzzVehicleMarkers.clear();
        teqBuzzVehicleMarkers.addAll(tempMarkers);

        HashMap<String, Marker> tempTeqBuzzVehicleMarkerHash = new HashMap<String, Marker>();
        for (Vehicle vehicle : receivedVehicles) {
            tempTeqBuzzVehicleMarkerHash.put(vehicle.getVehicle_id(), teqBuzzVehicleMarkerHash.get(vehicle.getVehicle_id()));
        }
        teqBuzzVehicleMarkerHash.clear();
        teqBuzzVehicleMarkerHash.putAll(tempTeqBuzzVehicleMarkerHash);
        return teqBuzzVehicleMarkers;
    }

    private void hideFavVehicleSnackBar() {
        if (favVehiclesSnackBar != null && !isFavouriteFlagEnabled()) {
            favVehiclesSnackBar.dismiss();
        }
    }

    private void updateNewTeqbuzzVehicles(ArrayList<Vehicle> receivedVehicles) {

        teqBuzzVehicles.clear();
        teqBuzzVehicles.addAll(receivedVehicles);
        if (selectedVehicle != null) {
            String selectedVehicleId = selectedVehicle.getVehicle_id();
            for (Vehicle receivedVehicle : receivedVehicles) {
                if (selectedVehicleId.equalsIgnoreCase(receivedVehicle.getVehicle_id())) {
                    receivedVehicle.setSelected(true);
                }
            }
        }
        teqbuzzVehicleListAdapter.notifyDataSetChanged();
        setMarkersMovementForVehicles(teqBuzzVehicles, receivedVehicles);
        runGetVehicleService(user.getMode(), isFavouriteFlagEnabled());
        if (teqBuzzVehicleMarkers != null) {
            /*for (Marker marker : teqBuzzVehicleMarkers) {
                if (marker.getTag() != null) {
                    Log.d("markertag", String.valueOf(marker.getTag()));
                } else {
                    Log.d("markertag", "marker is null");
                }
            }*/
        }

    }

    private void setMarkersMovementForVehicles(ArrayList<Vehicle> teqBuzzVehicles, ArrayList<Vehicle> receivedVehicles) {

        for (Vehicle receivedVehicle : receivedVehicles) {
            for (Vehicle teqbuzzVehicle : teqBuzzVehicles) {
                if (receivedVehicle.getVehicle_id().equalsIgnoreCase(teqbuzzVehicle.getVehicle_id())) {
                    LatLngInterpolator latLngInterpolator = new LatLngInterpolator.Linear();
                    Marker vehicleMarker = teqBuzzVehicleMarkerHash.get(teqbuzzVehicle.getVehicle_id());
                    if (vehicleMarker != null) {
                        MarkerAnimation.animateTeqbuzzMarker(this, teqbuzzVehicle, vehicleMarker, latLngInterpolator);
                    }
                }
            }
        }

    }

    private void showVehicleNotFoundSnack() {
        if (Utility.isConnectingToInternet(mActivity)) {
            if (!IS_NO_VEHICLE_SNACKBAR_SHOWN) {
                IS_NO_VEHICLE_SNACKBAR_SHOWN = true;
                vehicleNotFoundSnackBar.show(mActivity);
            }
        }
    }

    private void hideVehicleNotFoundSnack() {
        vehicleNotFoundSnackBar.dismiss();
        IS_NO_VEHICLE_SNACKBAR_SHOWN = false;
        isVehicleNotFoundSnackBarShowing = false;
    }

    private void hideVehicleLoadingView() {
        vehicleLoadingView.setVisibility(View.INVISIBLE);
    }

    private void hideProgressBar() {
        vehicleLoadingBar.setVisibility(View.GONE);
        vehicle_not_found_textview.setVisibility(View.GONE);
    }

    private ArrayList<Vehicle> removeNonFavBuses(ArrayList<Vehicle> receivedVehicles) {
        ArrayList<Vehicle> tempVehicles = new ArrayList<Vehicle>();
        ArrayList<Vehicle> favVehicles = teqBuzzDbHelper.getFavVehicles();
        for (Vehicle receivedVehicle : receivedVehicles) {

            for (Vehicle favVehicle : favVehicles) {
                if (receivedVehicle.getVehicle_id().equalsIgnoreCase(favVehicle.getVehicle_id())) {
                    tempVehicles.add(receivedVehicle);
                }

            }
        }
        return tempVehicles;
    }

    private void addTeqBuzzMarkersForVehicles(ArrayList<Vehicle> receivedVehicles) {
        if (!isSharedVehicleModeEnabled)
            receivedVehicles = sortDistanceWise(receivedVehicles);
        teqBuzzVehicles.addAll(receivedVehicles);
        if (!isSharedVehicleModeEnabled)
            sortDistanceWise();
        //nativeAdAdapter.notifyDataSetChanged();
        teqBuzzVehicleMarkers = new ArrayList<Marker>();
        teqBuzzVehicleMarkerHash = new HashMap<String, Marker>();
        for (Vehicle vehicle : teqBuzzVehicles) {
            Marker teqBuzzMarker = addMarkerForTeqBuzzVehicle(vehicle);
            teqBuzzVehicleMarkers.add(teqBuzzMarker);
            teqBuzzVehicleMarkerHash.put(vehicle.getVehicle_id(), teqBuzzMarker);
        }

        /*for (Marker marker : teqBuzzVehicleMarkers) {
            Log.d("markertag", String.valueOf(marker.getTag()));
        }*/

        teqbuzzVehicleListAdapter.notifyDataSetChanged();
        runGetVehicleService(user.getMode(), isFavouriteFlagEnabled());
    }

    private ArrayList<Vehicle> sortDistanceWise() {
        Comparator<Vehicle> myComparator = new Comparator<Vehicle>() {
            public int compare(Vehicle obj1, Vehicle obj2) {
                return obj1.getDistance().compareTo(obj2.getDistance());
            }
        };
        Collections.sort(teqBuzzVehicles, myComparator);
        teqbuzzVehicleListAdapter.setData(teqBuzzVehicles);
        teqbuzzVehicleListAdapter.notifyDataSetChanged();

        if (teqBuzzVehicleMarkers != null) {
            ArrayList<Marker> tempMarkers = new ArrayList<Marker>();
            for (Vehicle vehicle : teqBuzzVehicles) {
                for (Marker marker : teqBuzzVehicleMarkers) {
                    if ((Constants.TEQBUZZ_MARKER + "_" + vehicle.getVehicle_id()).equalsIgnoreCase(marker.getTitle())) {
                        tempMarkers.add(marker);
                    }
                }
            }
            teqBuzzVehicleMarkers.clear();
            teqBuzzVehicleMarkers.addAll(tempMarkers);
        }

        return teqBuzzVehicles;
    }

    private ArrayList<Vehicle> sortDistanceWise(final ArrayList<Vehicle> receivedVehicles) {
        ((MainActivity) mActivity).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Comparator<Vehicle> myComparator = new Comparator<Vehicle>() {
                    public int compare(Vehicle obj1, Vehicle obj2) {
                        return obj1.getDistance().compareTo(obj2.getDistance());
                    }
                };
                Collections.sort(receivedVehicles, myComparator);
        /*teqbuzzVehicleListAdapter.setData(teqBuzzVehicles);
        teqbuzzVehicleListAdapter.notifyDataSetChanged();
*/
                if (teqBuzzVehicleMarkers != null) {
                    ArrayList<Marker> tempMarkers = new ArrayList<Marker>();
                    for (Vehicle vehicle : receivedVehicles) {
                        for (Marker marker : teqBuzzVehicleMarkers) {
                            if ((Constants.TEQBUZZ_MARKER + "_" + vehicle.getVehicle_id()).equalsIgnoreCase(marker.getTitle())) {
                                tempMarkers.add(marker);
                            }
                        }
                    }
                    teqBuzzVehicleMarkers.clear();
                    teqBuzzVehicleMarkers.addAll(tempMarkers);
                }

            }
        });
        /*if (teqBuzzVehicleMarkers != null) {
            for (Marker marker : teqBuzzVehicleMarkers) {
                if (marker.getTag() != null) {
                    Log.d("markertag", String.valueOf(marker.getTag()));
                } else {
                    Log.d("markertag", "marker is null");
                }
            }
        }*/
        return receivedVehicles;
    }

    private void updateTeqBuzzVehicles(ArrayList<Vehicle> vehicles) {
        final ArrayList<Vehicle> receivedVehicles = sortDistanceWise(vehicles);

        teqBuzzVehicles = sortDistanceWise();
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    ArrayList<Vehicle> newVehicles = new ArrayList<Vehicle>();

                    for (final Vehicle receivedVehicle : receivedVehicles) {
                        int receivedVehiclePosition = receivedVehicles.indexOf(receivedVehicle);
                        for (Vehicle teqBuzzVehicle : teqBuzzVehicles) {
                            final int teqBuzzVehiclePosition = teqBuzzVehicles.indexOf(teqBuzzVehicle);
                            int count = 0;
                            if (receivedVehicle.getVehicle_id().equalsIgnoreCase(teqBuzzVehicle.getVehicle_id())) {

                                // set edited flag
                                receivedVehicle.setEdited(true);
                                teqBuzzVehicle.setEdited(true);
                                teqBuzzVehicle.setDistance(receivedVehicle.getDistance());
                                receivedVehicles.set(receivedVehiclePosition, receivedVehicle);
                                teqBuzzVehicles.set(teqBuzzVehiclePosition, teqBuzzVehicle);

                                Log.d("old distance", teqBuzzVehicle.getDistance());
                                Log.d("new distance", receivedVehicle.getDistance());
                            /*teqBuzzVehicle.setDistance(receivedVehicle.getDistance());
                              teqBuzzVehicles.set(teqBuzzVehiclePosition, teqBuzzVehicle);
                              sortDistanceWise();
                              teqbuzzVehicleListAdapter.notifyDataSetChanged();*/


                                /*// testing dummy movements
                                receivedVehicle.setLatitude(String.valueOf(Double.parseDouble(teqBuzzVehicle.getLatitude()) + 0.0002));
                                receivedVehicle.setLongitude(String.valueOf(Double.parseDouble(teqBuzzVehicle.getLongitude()) + 0.0002));
                                // testing dummy movements*/

                                // check whether location changed
                                if (!isVehicleLocationSame(teqBuzzVehicle, receivedVehicle) && !teqBuzzVehicle.isMoving()) {
                                    // vehicle moved
                                    // add one movements to pending movements
                                    addPendingMovement(receivedVehicle);
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            updateTeqBuzzVehicleMarker(teqBuzzVehiclePosition, receivedVehicle);
                                        }
                                    });
                                    Log.d("UpdateTeqBuzzVehicles", teqBuzzVehicle.getVehicle_id() + " updated");
                                }

                                count = 0;
                            } else {


                    /*count++;
                    if (count >= teqBuzzVehicles.size()) {
                        newVehicles.add(teqBuzzVehicle);
                        Log.d("addTeqBuzzVehicle", receivedVehicle.getVehicle_id() + " added");
                    }*/
                            }
                        }
                    }

                    // adding new vehicles
                    for (int i = 0; i < receivedVehicles.size(); i++) {
                        if (!receivedVehicles.get(i).isEdited()) {
                            Vehicle vehicle = receivedVehicles.get(i);
                            vehicle.setEdited(true);
                            receivedVehicles.set(i, vehicle);
                            teqBuzzVehicles.add(vehicle);
                        }
                    }
// removing non found vehicles
                    ArrayList<Vehicle> vehiclesToBeRemoved = new ArrayList<Vehicle>();
                    for (int i = 0; i < teqBuzzVehicles.size(); i++) {
                        if (!teqBuzzVehicles.get(i).isEdited()) {
                            vehiclesToBeRemoved.add(teqBuzzVehicles.get(i));
                        }
                    }


                    for (Vehicle vehicleToBeRemoved : vehiclesToBeRemoved) {
                        teqBuzzVehicles.remove(vehicleToBeRemoved);
                    }


       /* if (newVehicles.size() > 0)
            teqBuzzVehicles.addAll(newVehicles);
*/

                    if (receivedVehicles.size() != teqBuzzVehicles.size()) {
                        //Utility.showToast(mActivity, "code is wrong");
                    }


                    for (Vehicle teqBuzzVehicle : teqBuzzVehicles) {
                        int teqBuzzVehiclePosition = teqBuzzVehicles.indexOf(teqBuzzVehicle);
                        teqBuzzVehicle.setEdited(false);
                        teqBuzzVehicles.set(teqBuzzVehiclePosition, teqBuzzVehicle);
                        Log.d("updated distance", teqBuzzVehicle.getDistance());
                    }


                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //teqbuzzVehicleListAdapter.sortDistanceWise();
                            sortDistanceWise();
                            Log.d("Sorting", "Sorted");
                            teqbuzzVehicleListAdapter.notifyDataSetChanged();
                        }
                    });

                } catch (IndexOutOfBoundsException e) {

                }
            }

        }).start();

        runGetVehicleService(user.getMode(), isFavouriteFlagEnabled());


    }

    private Vehicle addSchedulesAndRoutePoints(Vehicle teqBuzzVehicle, Vehicle receivedVehicle) {

        if (teqBuzzVehicle.getSchedules() != null && teqBuzzVehicle.getSchedules().size() > 0) {
            receivedVehicle.setSchedules(teqBuzzVehicle.getSchedules());
        }
        if (teqBuzzVehicle.getRoutePaths() != null && teqBuzzVehicle.getRoutePaths().size() > 0) {
            receivedVehicle.setRoutePaths(teqBuzzVehicle.getRoutePaths());
        }

        return receivedVehicle;

    }

    private void addPendingMovement(Vehicle receivedVehicle) {

        String pendingMovementId = Utility.createPendingMovementId();
        Movement movement = new Movement();
        movement.setLatitude(receivedVehicle.getLatitude());
        movement.setLatitude(receivedVehicle.getLongitude());
        movement.setId(pendingMovementId);
        pendingMovements.put(pendingMovementId, movement);

    }

    private boolean isVehicleLocationSame(Vehicle teqBuzzVehicle, Vehicle receivedVehicle) {
        return teqBuzzVehicle.getLatitude().equalsIgnoreCase(receivedVehicle.getLatitude()) && teqBuzzVehicle.getLongitude().equalsIgnoreCase(receivedVehicle.getLongitude());
    }

    private void updateTeqBuzzVehicleMarker(int teqBuzzVehiclePosition, Vehicle
            receivedVehicle) {
        String vehicleId = receivedVehicle.getVehicle_id();
        LatLng toPosition = new LatLng(Double.parseDouble(receivedVehicle.getLatitude()), Double.parseDouble(receivedVehicle.getLongitude()));
        for (Marker vehicleMarker : teqBuzzVehicleMarkers) {
            if (vehicleMarker.getTitle().contains(vehicleId)) {
                // animate vehicle marker to a new position
                String str_latitude = receivedVehicle.getLatitude();
                String str_longitude = receivedVehicle.getLongitude();
               /* double dbl_latitude = Double.parseDouble(str_latitude);
                double dbl_longitude = Double.parseDouble(str_longitude);

                if (receivedVehicle.getRoutePaths() != null) {
                    animateTeqBuzzVehicleInPath(str_latitude, str_longitude, teqBuzzVehiclePosition, receivedVehicle, vehicleMarker, toPosition);
                }*/

                animateSmoothMarker(str_latitude, str_longitude, teqBuzzVehiclePosition, receivedVehicle, vehicleMarker, toPosition, false, false);
            }
        }


    }

    private void animateTeqBuzzVehicleInPath(String str_latitude, String str_longitude,
                                             int teqBuzzVehiclePosition, Vehicle receivedVehicle, Marker vehicleMarker, LatLng
                                                     toPosition) {

    }

    public void onTeqBuzzSchedulesLoaded(ArrayList<Schedule> schedules) {
        progress_container.setVisibility(View.GONE);
        if (schedules != null) {
            this.schedules = schedules;
            // setting schedules for selected vehicle
            setSchedulesForSelectedVehicle(schedules);

            // draw google path for selected vehicle route
            drawGoogleAPIRouteForSelectedVehicle(schedules);

            vehicleScheduleAdapter = new VehicleScheduleAdapter(mActivity, this, schedules);
            vehicleScheduleAdapter.setSelectedVehicle(selectedVehicle);
            schedule_list.setAdapter(vehicleScheduleAdapter);
            updateScheduleListHeader(teqBuzzVehicles.get(selectedVehicleIndex), schedules);
            stop_overlay.setVisibility(View.GONE);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // this code will be executed after 2 seconds
                            if (selectedVehicle != null && selectedVehicle.getSchedules() != null && selectedVehicle.getSchedules().size() > 0) {
                                webService.getVehicleScheduleList(mActivity, selectedVehicle.getVehicle_id(), BusMapFragment.this);
                            }
                        }
                    }, 2000);
                }
            });

        } else {
            Utility.showToast(mActivity, mActivity.getResources().getString(R.string.some_error_occured));
        }
    }


    private void updateScheduleListHeader(Vehicle vehicle, ArrayList<Schedule> schedules) {
        if (schedules != null && schedules.size() > 0) {
            line_number.setText(vehicle.getVehicle_line_number());
            start_location.setText(schedules.get(0).getStop_name());
            end_location.setText(schedules.get(schedules.size() - 1).getStop_name());
            agency_name.setText(vehicle.getVehicle_agency());
            vehicle_type.setText("Bus");
        }
    }


    public void onTeqbuzzVehicleAnimationUpdtaing(int teqBuzzVehiclePosition, Vehicle
            teqBuzzVehicle, String latitude, String longitude, LatLng finalPosition) {
        // vehicle completed animation.. so update its location
        teqBuzzVehicle.setLatitude(latitude);
        teqBuzzVehicle.setLongitude(longitude);
        teqBuzzVehicles.set(teqBuzzVehiclePosition, teqBuzzVehicle);
        //sortDistanceWise();
        teqbuzzVehicleListAdapter.notifyDataSetChanged();
        //nativeAdAdapter.notifyDataSetChanged();

    }

    public void onFavSnackClicked() {
        ((MainActivity) mActivity).setActionBarTitle(mActivity.getResources().getString(R.string.Home));
        onFavFilterDisabled();
    }

    public void openInternetSettings() {
        IS_INTERNET_REQUESTED = true;
        IS_INTERNET_SNACKBAR_SHOWN = false;
        mActivity.startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
    }

    public void onFacebookAdsLoaded(NativeAd nativeAd, LinearLayout adView) {
        nativeAdAdapter.onAdsLoaded(nativeAd);

    }

    public void onVehicleNotFoundSnackBarClosed() {
        isVehicleNotFoundSnackBarShowing = false;
        IS_NO_VEHICLE_SNACKBAR_SHOWN = false;
    }

    public void selectSharedVehicleMode() {
        onFavFilterDisabled();
        onSharedFilterEnabled();
    }

    public void showSharedVehicles() {
        isSharedVehicleModeEnabled = true;
    }

    public void showSharedVehicleDeleteBtn(ArrayList<Vehicle> sharedVehicles) {
       /* if (count > 0) {
            deleteSharedVehicleBtn.setVisibility(View.VISIBLE);
        } else {
            deleteSharedVehicleBtn.setVisibility(View.GONE);
        }*/

        this.sharedVehicles = sharedVehicles;
        int selectedCount = 0;
        for (Vehicle vehicle : sharedVehicles) {
            if (vehicle.isSelected()) {
                selectedCount++;
            }
        }
        if (selectedCount > 0) {
            deleteSharedVehicleBtn.setVisibility(View.VISIBLE);
        } else {
            deleteSharedVehicleBtn.setVisibility(View.GONE);
        }

    }

    public void setCmgFromDeepLink(boolean isCmgFromDeepLink) {
        webService.clearAll();
        teqBuzzVehicles.clear();
        teqbuzzVehicleListAdapter.notifyDataSetChanged();
        sharedVehicleId = ((MainActivity) mActivity).getSharedVehicle().getVehicle_id();
        sharedVehicle = ((MainActivity) mActivity).getSharedVehicle();
        this.isCmgFromDeepLink = isCmgFromDeepLink;
        if (isCmgFromDeepLink) {
                /*is coming form deep link*/
            myPreferences = new Preferences(mActivity);
            boolean isLoginned = myPreferences.isLoginned();
            sharedVehicleId = ((MainActivity) mActivity).getSharedVehicle().getVehicle_id();
            sharedVehicle = ((MainActivity) mActivity).getSharedVehicle();
            if (isLoginned) {
                    /*user loginned*/
                if (!appDatabaseHelper.isVehicleAlreadyInSharedList(sharedVehicleId)) {
                        /*vehicle not found in shared list. So add vehicle in shared list*/
                    //showAddSharedVehicleDialog(isLoginned);
                    Vehicle vehicle = new Vehicle();
                    vehicle.setVehicle_id(sharedVehicle.getVehicle_id());
                    vehicle.setVehicle_line_number(sharedVehicle.getVehicle_line_number());
                    vehicle.setVehicle_agency(sharedVehicle.getVehicle_agency());
                    addVehicleToSharedList(vehicle);
                    sharedVehicles = appDatabaseHelper.getSharedVehicles();
                    sharedVehiclesCount = sharedVehicles.size();
                    sharedCount.setText("" + sharedVehiclesCount);

                } else {
                        /*vehicle already in shared list. Do nothing*/
                }
            } else {
                    /*user not loginned*/
                if (!appDatabaseHelper.isVehicleAlreadyInSharedList(sharedVehicleId) && !MainActivity.isShareCancelBtnClicked) {
                    showAddSharedVehicleDialog(isLoginned);
                    choiceMenuItem.setVisible(false);
                }
            }
        }
        onSharedFilterEnabled();
    }

    public void setSharedVehicleId(String sharedVehicleId) {
        this.sharedVehicleId = sharedVehicleId;
    }

    public void addPendingSharedVehicles() {
        if (myPreferences.isLoginned() && isAddSharedVehiclesPending) {
            Vehicle vehicle = new Vehicle();
            vehicle.setVehicle_id(pendingAddSharedVehicleId);
            vehicle.setVehicle_line_number(pendingAddSharedVehicleId);
            vehicle.setVehicle_agency(pendingAddSharedVehicleId);
            addVehicleToSharedList(vehicle);
            isAddSharedVehiclesPending = false;
            pendingAddSharedVehicleId = "";
        }
    }

    public void checkAndAddPendingSharedVehicles() {
        if (myPreferences.isLoginned()) {
            ((MainActivity) mActivity).setNavigationItemSelected(1);
            if (isAddSharedVehiclesPending && !appDatabaseHelper.isVehicleAlreadyInSharedList(sharedVehicleId) && !MainActivity.isShareCancelBtnClicked) {
                showAddSharedVehicleDialog(myPreferences.isLoginned());
            }
        }
    }

    public void clearSelectedVehicle() {
        if (polyLinesDownloadTask != null) {
            polyLinesDownloadTask.cancel(true);
        }
        if (polyLinesParserTask != null) {
            polyLinesParserTask.cancel(true);
        }
        isMapFocusedRoute = false;
        if (line != null) {
            line.remove();
            line = null;
        }
        if (teqBuzzStopMarkers != null) {
            for (Marker marker : teqBuzzStopMarkers) {
                marker.remove();
            }
            teqBuzzStopMarkers.clear();
        }
        selectedVehicle = null;
    }

    public Object getSelectedBusRouteLineLine() {
        return line;
    }

    public void onAlarmRequestDone(AlarmResponseEntity alarmResponseEntity) {
        vehicleScheduleAdapter.onAlarmRequestDone(alarmResponseEntity);
    }

    public void onVehicleFavOptionDone(String favFlag, Vehicle vehicle) {
        hideProgressBar();
        if (favFlag.equalsIgnoreCase(Vehicle.IS_FAV)) {
            long isAdded = teqBuzzDbHelper.addVehicleToFavList(vehicle);
            Utility.showSnack(mActivity, mActivity.getResources().getString(R.string.fav_added));
        } else if (favFlag.equalsIgnoreCase(Vehicle.IS_NOT_FAV)) {
            teqBuzzDbHelper.deleteVehicleFromFavList(selectedVehicle);
            Utility.showSnack(mActivity, mActivity.getResources().getString(R.string.fav_removed));
        }
        teqbuzzVehicleListAdapter.notifyDataSetChanged();
    }

    public void onFavVehiclesLoaded() {
        if (vehicleListAdapter != null)
            vehicleListAdapter.notifyDataSetChanged();
    }

    private class ScheduleListStikkyAnimator extends HeaderStikkyAnimator {
        @Override
        public AnimatorBuilder getAnimatorBuilder() {
            View mHeader_image = getHeader().findViewById(R.id.stop_list_scroll);
            return AnimatorBuilder.create().applyVerticalParallax(mHeader_image);
        }
    }

    private class ParallaxStikkyAnimator extends HeaderStikkyAnimator {
        @Override
        public AnimatorBuilder getAnimatorBuilder() {
            View mHeader_image = getHeader().findViewById(R.id.mapContainer);
            return AnimatorBuilder.create().applyVerticalParallax(mHeader_image);
        }
    }


    class VehicleScheduleListResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            progress_container.setVisibility(View.GONE);
            String response = intent.getStringExtra("response");

            if (response.contains("schedule_id")) {

                // parseAndAddSchedules(response);

            } else if (response.contains("No vehicle schedule")) {

            }

        }
    }


    private void setSchedulesForSelectedVehicle(ArrayList<Schedule> schedules) {

        Vehicle teqbuzzVehicle = teqBuzzVehicles.get(selectedVehicleIndex);
        teqbuzzVehicle.setSchedules(schedules);
        teqBuzzVehicles.set(selectedVehicleIndex, teqbuzzVehicle);
        //sortDistanceWise();
        teqbuzzVehicleListAdapter.notifyDataSetChanged();
        //nativeAdAdapter.notifyDataSetChanged();

    }

    private void parseAndAddTeqBuzzPolyLines(String routes) {

        try {
            JSONArray jsonRoutesArray = new JSONArray(routes);
            for (int i = 0; i < jsonRoutesArray.length(); i++) {
                String res = jsonRoutesArray.getJSONObject(i).toString();
                ParserTask parserTask = new ParserTask();
                //Utility.showToast(mActivity, res);
                parserTask.execute(res);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void drawRouteForSelectedVehicle(ArrayList<Schedule> schedules) {

        PolylineOptions options = new PolylineOptions().width(10).color(mActivity.getResources().getColor(R.color.orange)).geodesic(true);
        Double firstLatitude = 0.0, firstLongitude = 0.0;
        for (Schedule schedule : schedules) {
            Double stopLatitude = Double.parseDouble(schedule.getStop_latitude());
            Double stopLongitude = Double.parseDouble(schedule.getStop_longitude());
            LatLng point = new LatLng(stopLatitude, stopLongitude);
            options.add(point);
            if (firstLatitude == 0.0) {
                firstLatitude = stopLatitude;
                firstLongitude = stopLongitude;

            }


//        addMarker(); //add Marker in current position
            line = googleMap.addPolyline(options); //add Polyline

            CameraPosition firstSchedulePosition = new CameraPosition.Builder()
                    .target(new LatLng(firstLatitude, firstLongitude)).
                            tilt(Constants.MAP_TILT).zoom(Constants.MAP_ZOOM_LEVEL).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(firstSchedulePosition));
        }
    }


    private void drawGoogleAPIRouteForSelectedVehicle(ArrayList<Schedule> schedules) {

        /*for (int i = 0; i + 1 < schedules.size(); i++) {*/
        if (schedules != null && schedules.size() > 0) {
            LatLng origin = new LatLng(Double.parseDouble(schedules.get(0).getStop_latitude()), Double.parseDouble(schedules.get(0).getStop_longitude()));
            LatLng dest = new LatLng(Double.parseDouble(schedules.get(schedules.size() - 1).getStop_latitude()), Double.parseDouble(schedules.get(schedules.size() - 1).getStop_longitude()));

            String url = Utility.getDirectionsUrl(origin, dest);

            polyLinesDownloadTask = new DownloadTask();

            polyLinesDownloadTask.execute(url);
        /*}*/

            if (teqBuzzStopMarkers != null) {
                for (Marker marker : teqBuzzStopMarkers) {
                    marker.remove();
                }
                teqBuzzStopMarkers.clear();

            } else {
                teqBuzzStopMarkers = new ArrayList<Marker>();

            }
            for (int i = 0; i < schedules.size(); i++) {

                BitmapDescriptor flagBitmapDescriptor = Utility.getCustomMarker(mActivity, schedules.get(i));

                LatLng position = new LatLng(Double.parseDouble(schedules.get(i).getStop_latitude()), Double.parseDouble(schedules.get(i).getStop_longitude()));
                Marker stopMarker = addMarkerForLocation(googleMap, position.latitude, position.longitude, Constants.TEQBUZZ_STOP_MARKER + "_" + schedules.get(i).getStop_id() + "_" + schedules.get(i).getStop_name(), flagBitmapDescriptor);

                teqBuzzStopMarkers.add(stopMarker);
            }

        }

    }

    public void moveMapCameraToPosition(Location location) {
        isUserMarkerAnimationEnabled = false;
        CameraPosition firstSchedulePosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude())).
                        tilt(Constants.MAP_TILT).zoom(Constants.MAP_ZOOM_LEVEL).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(firstSchedulePosition));
    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = Utility.downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            polyLinesParserTask = new ParserTask();

            polyLinesParserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected void onPreExecute() {
            //showProgress();
            super.onPreExecute();
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            selectedBusPolyLines = null;

            // Traversing through all the routes
            if (result != null && result.size() == 0) {
                Log.e("", "");
            }
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                selectedBusPolyLines = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                selectedBusPolyLines.addAll(points);
                selectedBusPolyLines.width(15);
                selectedBusPolyLines.color(mActivity.getResources().getColor(R.color.colorPrimary));


                // setting route paths for vehicle
                setRoutePathsForSelectedVehicles(points);


            }

            // Drawing polyline in the Google Map for the i-th route
            if (googleMap != null && selectedBusPolyLines != null && !isMapFocusedRoute) {
                line = googleMap.addPolyline(selectedBusPolyLines);
                Log.d("Polyline in google map", line.toString());
            } else
                initialiseGoogleMap();

            Location location = new Location("");
            location.setLatitude(points.get(0).latitude);
            location.setLongitude(points.get(0).longitude);
            if (!isMapFocusedRoute) {
                moveMapCameraToPosition(location);
                isMapFocusedRoute = true;
            }

            //googleMap.addPolyline(polylineOptions);

            stopProgress();
        }

    }

    private void setRoutePathsForSelectedVehicles(ArrayList<LatLng> points) {

        Vehicle teqbuzzVehicle = teqBuzzVehicles.get(selectedVehicleIndex);
        teqbuzzVehicle.setRoutePaths(points);
        teqBuzzVehicles.set(selectedVehicleIndex, teqbuzzVehicle);
        //sortDistanceWise();
        teqbuzzVehicleListAdapter.notifyDataSetChanged();
        //nativeAdAdapter.notifyDataSetChanged();

    }

    private void initialiseGoogleMap() {
        //googleMap = mapFragment.getMap();
        //Utility.showToast(mActivity, "map is null");
    }


    private void addMarkersForSchedules() {

        int scheduleSize = schedules.size();

        if (scheduleMarkers != null)
            scheduleMarkers.clear();

        scheduleMarkers = new ArrayList<Marker>();

        for (int i = 0; i < scheduleSize; i++) {
            MarkerOptions schedulesMarkerOptions = new MarkerOptions().position(
                    new LatLng(Double.parseDouble(schedules.get(i).getStop_latitude()),
                            Double.parseDouble(schedules.get(i).getStop_longitude())))
                    .snippet("stop").title(schedules.get(i).getStop_name()).icon(stop_marker);

            Marker tempScheduleMarker = googleMap.addMarker(schedulesMarkerOptions);
            scheduleMarkers.add(tempScheduleMarker);

        }
    }

    private void registerGetVehicleScheduleListResponseReceiver() {

        if (vehicleScheduleListResponseReceiver == null) {
            vehicleScheduleListResponseReceiver = new VehicleScheduleListResponseReceiver();
            mActivity.registerReceiver(vehicleScheduleListResponseReceiver, new IntentFilter(WebService.INTENT_FILTER_SCHEDULE_LIST_RESPONSE));
        }
    }

    private void unRegisterGetVehicleScheduleListResponseReceiver() {

        if (vehicleScheduleListResponseReceiver != null) {
            mActivity.unregisterReceiver(vehicleScheduleListResponseReceiver);
        }
    }

    private void registerConnectivityReceiver() {

        if (connectivityReceiver != null) {
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(connectivityReceiver, new IntentFilter(Constants.INTENT_FILTER_CONNECTIVITY));
        }
    }

    private void unRegisterConnectivityReceiver() {

        if (connectivityReceiver != null) {
            LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(connectivityReceiver);
        }
    }
/*

    private void showNativeAd() {
        AdSettings.addTestDevice("5e87b7cf8d49f913432e0a6bc85d820d");
        nativeAd = new NativeAd(mActivity, "1112849102102984_1112852525435975");


        nativeAd.setAdListener(new AdListener() {

            @Override
            public void onError(Ad ad, AdError error) {
                Log.e("suren ", error.toString());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (ad != nativeAd) {
                    return;
                }
                // Add ad into the ad container.
                nativeAdContainer = (LinearLayout) rootView.findViewById(R.id.native_ad_container);
                LayoutInflater inflater = LayoutInflater.from(mActivity);
                fbAdView = (LinearLayout) inflater.inflate(R.layout.fb_ad, nativeAdContainer, false);
                nativeAdContainer.addView(fbAdView);

                // Create native UI using the ad metadata.
                ImageView nativeAdIcon = (ImageView) fbAdView.findViewById(R.id.native_ad_icon);
                TextView nativeAdTitle = (TextView) fbAdView.findViewById(R.id.native_ad_title);
                TextView nativeAdBody = (TextView) fbAdView.findViewById(R.id.native_ad_body);
                MediaView nativeAdMedia = (MediaView) fbAdView.findViewById(R.id.native_ad_media);
                TextView nativeAdSocialContext = (TextView) fbAdView.findViewById(R.id.native_ad_social_context);
                Button nativeAdCallToAction = (Button) fbAdView.findViewById(R.id.native_ad_call_to_action);

                // Setting the Text.
                nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
                nativeAdTitle.setText(nativeAd.getAdTitle());
                nativeAdBody.setText(nativeAd.getAdBody());

                // Downloading and setting the ad icon.
                NativeAd.Image adIcon = nativeAd.getAdIcon();
                NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

                // Download and setting the cover image.
                NativeAd.Image adCoverImage = nativeAd.getAdCoverImage();
                nativeAdMedia.setNativeAd(nativeAd);

                // Add adChoices icon
                if (adChoicesView == null) {
                    adChoicesView = new AdChoicesView(mActivity, nativeAd, true);
                    fbAdView.addView(adChoicesView, 0);
                }

                nativeAd.registerViewForInteraction(fbAdView);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }
        });

        nativeAd.loadAd();
    }

*/

    private void addItemsToMap(List<OBAStopListEntity.Stop> items) {
        if (googleMap != null) {
            //This is the current user-viewable region of the map
            LatLngBounds bounds = googleMap.getProjection().getVisibleRegion().latLngBounds;

            //Loop through all the items that are available to be placed on the map
            for (OBAStopListEntity.Stop item : items) {
                if (item.getId().equalsIgnoreCase("1_9978")) {

                    //If the item is within the the bounds of the screen
                    if (bounds.contains(new LatLng(Double.parseDouble(item.getLat()), Double.parseDouble(item.getLon())))) {
                        //If the item isn't already being displayed
                        if (!visibleMarkers.containsKey(item.getId())) {
                            //Add the Marker to the Map and keep track of it with the HashMap
                            //getMarkerForItem just returns a MarkerOptions object
                            visibleMarkers.put(item.getId(), googleMap.addMarker(getMarkerForItem(item)));
                        }
                    }

                    //If the marker is off screen
                    else {
                        //If the course was previously on screen
                        if (visibleMarkers.containsKey(item.getId())) {
                            //1. Remove the Marker from the GoogleMap
                            visibleMarkers.get(item.getId()).remove();

                            //2. Remove the reference to the Marker from the HashMap
                            visibleMarkers.remove(item.getId());
                        }
                    }
                }
            }
        }
    }

    private MarkerOptions getMarkerForItem(OBAStopListEntity.Stop item) {
        if (item.getId().equalsIgnoreCase("1_9978")) {
            return new MarkerOptions().position(
                    new LatLng(Double.parseDouble(item.getLat()), Double.parseDouble(item.getLon()))).title(Constants.OBA_MARKER + item.getId()).icon(stop_marker);
        } else
            return new MarkerOptions().position(
                    new LatLng(Double.parseDouble(item.getLat()), Double.parseDouble(item.getLon()))).title(Constants.OBA_MARKER + item.getId()).icon(stop_marker);
    }

    private MarkerOptions getMarkerForVehicle(OBAVehicle item) {
        return new MarkerOptions().position(
                new LatLng(Double.parseDouble(item.getLat()), Double.parseDouble(item.getLon()))).title(item.getId())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
    }

    public void runGetVehicleService(final int mode, final boolean favouriteFlagEnabled) {
        Log.d("runGetVehicleService", "runGetVehicleService90 called");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                String distanceRange = String.valueOf(myPreferences.getDistanceRange());
                WebService webService = new WebService(mActivity);

                if (!isCmgFromDeepLink) {
                    if (isSharedVehicleModeEnabled) {
                        user = myPreferences.getUser();
                        ArrayList<String> sharedVehicleIds = new ArrayList<String>();
                        sharedVehicles = appDatabaseHelper.getSharedVehicles();

                        for (Vehicle vehicle : sharedVehicles) {
                            sharedVehicleIds.add(vehicle.getVehicle_id());
                        }
                        String vehicleList = Utility.convertArrayListToString(sharedVehicleIds);
                        CALL_MODE = Constants.CALL_MODE_SHARED;
                        String getSharedVehiclesUrl = vehicleList + "&latitude=" + user.getTemp_latitude() +
                                "&longitude=" + user.getTemp_longitude();
                        webService.getSharedVehicles(mActivity, BusMapFragment.this, getSharedVehiclesUrl, mode, favouriteFlagEnabled, CALL_MODE);
                    } else if (isFavouriteFlagEnabled()) {
                        user = myPreferences.getUser();
                        String user_id = myPreferences.getUser().getUser_id();
                        CALL_MODE = Constants.CALL_MODE_FAVORITE;
                        String getfavVehiclesUrl = "user_id=" + user_id + "&latitude=" + user.getTemp_latitude() +
                                "&longitude=" + user.getTemp_longitude() + "&distance=" + "200000" + "&user_id=" + user.getUser_id();
                        webService.getVehicleList(mActivity, BusMapFragment.this, getfavVehiclesUrl, mode, favouriteFlagEnabled, CALL_MODE);
                    } else {
                        user = myPreferences.getUser();
                        str_latitude = user.getTemp_latitude();
                        str_longitude = user.getTemp_longitude();
                        CALL_MODE = Constants.CALL_MODE_NORMAL;
                        webService.getVehicleList(mActivity, BusMapFragment.this, WebService.GET_VEHICLE_LIST_URL +
                                        "latitude=" + str_latitude + "&longitude=" + str_longitude + "&distance=" + distanceRange + "&limit=" + Constants.VEHICLE_LIMIT + "&offset=" + "0" + "&user_id=" + user.getUser_id(), mode,
                                favouriteFlagEnabled, CALL_MODE);
                    }
                } else {

                    user = myPreferences.getUser();
                    ArrayList<String> sharedVehicleIds = new ArrayList<String>();
                    sharedVehicles = appDatabaseHelper.getSharedVehicles();

                    for (Vehicle vehicle : sharedVehicles) {
                        sharedVehicleIds.add(vehicle.getVehicle_id());
                    }
                    String vehicleList = Utility.convertArrayListToString(sharedVehicleIds);
                    if (myPreferences.isLoginned()) {
                        if (!sharedVehicleIds.contains(sharedVehicleId)) {
                            if (vehicleList.length() > 0) {
                                vehicleList = sharedVehicleId + "," + vehicleList;
                            } else {
                                vehicleList = sharedVehicleId;
                            }
                        } else {
                            sharedVehicleIds.remove(sharedVehicleId);
                            sharedVehicleIds.add(0, sharedVehicleId);
                            vehicleList = Utility.convertArrayListToString(sharedVehicleIds);
                        }
                    } else {
                        vehicleList = sharedVehicleId;
                    }
                    String getSharedVehiclesUrl = vehicleList + "&latitude=" + user.getTemp_latitude() +
                            "&longitude=" + user.getTemp_longitude();
                    CALL_MODE = Constants.CALL_MODE_SHARED;
                    webService.getSharedVehicles(mActivity, BusMapFragment.this, getSharedVehiclesUrl, mode, favouriteFlagEnabled, CALL_MODE);

                }
                //Do something after 100ms
            }
        }, GET_VEHICLE_SERVICE_DELAY);

    }

    public void runInstantGetVehicleService(final int mode,
                                            final boolean favouriteFlagEnabled) {
        //isCmgFromDeepLink = ((MainActivity) mActivity).isCmgFromDeepLink();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                String distanceRange = String.valueOf(myPreferences.getDistanceRange());
                WebService webService = new WebService(mActivity);

                if (!isCmgFromDeepLink) {

                    if (isSharedVehicleModeEnabled) {
                        user = myPreferences.getUser();
                        ArrayList<String> sharedVehicleIds = new ArrayList<String>();
                        sharedVehicles = appDatabaseHelper.getSharedVehicles();
                        for (Vehicle vehicle : sharedVehicles) {
                            sharedVehicleIds.add(vehicle.getVehicle_id());
                        }
                        String vehicleList = Utility.convertArrayListToString(sharedVehicleIds);
                        String getSharedVehiclesUrl = vehicleList + "&latitude=" + user.getTemp_latitude() +
                                "&longitude=" + user.getTemp_longitude();
                        CALL_MODE = Constants.CALL_MODE_SHARED;
                        webService.getSharedVehicles(mActivity, BusMapFragment.this, getSharedVehiclesUrl, mode, favouriteFlagEnabled, CALL_MODE);
                    } else if (isFavouriteSelected) {

                        user = myPreferences.getUser();
                        String user_id = myPreferences.getUser().getUser_id();
                        String getfavVehiclesUrl = "user_id=" + user_id + "&latitude=" + user.getTemp_latitude() +
                                "&longitude=" + user.getTemp_longitude() + "&distance=" + distanceRange + "&user_id=" + user.getUser_id();
                        CALL_MODE = Constants.CALL_MODE_FAVORITE;
                        webService.getVehicleList(mActivity, BusMapFragment.this, getfavVehiclesUrl, mode, favouriteFlagEnabled, CALL_MODE);

                    } else {
                        user = myPreferences.getUser();
                        str_latitude = user.getTemp_latitude();
                        str_longitude = user.getTemp_longitude();
                        CALL_MODE = Constants.CALL_MODE_NORMAL;
                        webService.getVehicleList(mActivity, BusMapFragment.this, WebService.GET_VEHICLE_LIST_URL +
                                "latitude=" + str_latitude + "&longitude=" + str_longitude + "&distance=" + distanceRange + "&limit=" + Constants.VEHICLE_LIMIT + "&offset=" + "0" + "&user_id=" + user.getUser_id(), mode, favouriteFlagEnabled, CALL_MODE);
                    }
                } else {
                    if (isSharedVehicleModeEnabled) {
                        user = myPreferences.getUser();
                        ArrayList<String> sharedVehicleIds = new ArrayList<String>();
                        sharedVehicles = appDatabaseHelper.getSharedVehicles();
                        for (Vehicle vehicle : sharedVehicles) {
                            sharedVehicleIds.add(vehicle.getVehicle_id());
                        }
                        String vehicleList = Utility.convertArrayListToString(sharedVehicleIds);
                        if (!myPreferences.isLoginned()) {
                            vehicleList = sharedVehicleId;
                        } else {
                            if (vehicleList.length() <= 0) {
                                vehicleList = sharedVehicleId;
                            }
                        }

                        String getSharedVehiclesUrl = vehicleList + "&latitude=" + user.getTemp_latitude() +
                                "&longitude=" + user.getTemp_longitude();
                        CALL_MODE = Constants.CALL_MODE_SHARED;
                        webService.getSharedVehicles(mActivity, BusMapFragment.this, getSharedVehiclesUrl, mode, favouriteFlagEnabled, CALL_MODE);

                    } else {
                        user = myPreferences.getUser();
                        str_latitude = user.getTemp_latitude();
                        str_longitude = user.getTemp_longitude();
                        CALL_MODE = Constants.CALL_MODE_SHARED;
                        webService.getVehicleDetails(mActivity, BusMapFragment.this, sharedVehicleId, CALL_MODE);
                    }
                }
                //Do something after 100ms
            }
        }, 0);

    }

    public void onTeqbuzzVehicleAnimationProgress(int teqBuzzVehiclePosition, Vehicle
            receivedVehicle, boolean isMoving) {
        try {
            receivedVehicle.setMoving(isMoving);
            //teqBuzzVehicles = sortDistanceWise();
            for (Vehicle vehicle : teqBuzzVehicles) {
                if (receivedVehicle.getVehicle_id().equalsIgnoreCase(vehicle.getVehicle_id())) {
                    teqBuzzVehiclePosition = teqBuzzVehicles.indexOf(vehicle);
                }
            }
            if (teqBuzzVehicles.size() <= 0) {
                Log.d("updateVehicles", "vehicle size is 0");
            }
            teqBuzzVehicles.set(teqBuzzVehiclePosition, receivedVehicle);
            //sortDistanceWise();
            teqbuzzVehicleListAdapter.notifyDataSetChanged();
            //nativeAdAdapter.notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e) {
            Log.e("onTeqbuzzVehicleAnimationProgress", "vehicle size mismatch error");
        }
    }

    public void onTeqbuzzVehicleAnimatedCompleted(int teqBuzzVehiclePosition, Vehicle
            teqBuzzVehicle, String latitude, String longitude, LatLng finalPosition) {
        try {
            // vehicle completed animation.. so update its location
            teqBuzzVehicle.setLatitude(latitude);
            teqBuzzVehicle.setLongitude(longitude);
            //teqBuzzVehicles = sortDistanceWise();
            for (Vehicle vehicle : teqBuzzVehicles) {
                if (teqBuzzVehicle.getVehicle_id().equalsIgnoreCase(vehicle.getVehicle_id())) {
                    teqBuzzVehiclePosition = teqBuzzVehicles.indexOf(vehicle);
                }
            }
            teqBuzzVehicles.set(teqBuzzVehiclePosition, teqBuzzVehicle);
            //sortDistanceWise();
            teqbuzzVehicleListAdapter.notifyDataSetChanged();
            //nativeAdAdapter.notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e) {
            Log.e("onTeqbuzzVehicleAnimatedCompleted", "vehicle size mismatch error");
        }
    }

    private BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isConnected = intent.getBooleanExtra(Constants.IS_CONNECTED, Utility.isConnectingToInternet(mActivity));
            if (isConnected) {
                quickRunGetVehicleService();
                if (internetSnackBar == null) {
                    internetSnackBar = Utility.showSnackForInternet(mActivity, mActivity.getResources().getString(R.string.no_internet), BusMapFragment.this);
                }
                internetSnackBar.dismiss();
                IS_INTERNET_SNACKBAR_SHOWN = false;
            } else {
                if (internetSnackBar == null) {
                    internetSnackBar = Utility.showSnackForInternet(mActivity, mActivity.getResources().getString(R.string.no_internet), BusMapFragment.this);
                }
                internetSnackBar.show(mActivity);
                IS_INTERNET_SNACKBAR_SHOWN = true;


            }
        }
    };


    private LinearLayout showNativeAd(View convertView, ViewGroup parent) {
        //AdSettings.addTestDevice("6f9fb2cde69803cc84b69a8895a02cfc");

        final NativeAd nativeAd = new NativeAd(mActivity, "1112849102102984_1112852525435975");
        nativeAd.setAdListener(new AdListener() {

            @Override
            public void onError(Ad ad, AdError error) {
                //Utility.showSnack(mActivity, error.getErrorMessage());
                isAdsLoaded = true;
                if (ad != nativeAd) {
                    return;
                }
                //nativeAdAdapter.onAdsLoaded(nativeAd);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                //Utility.showSnack(mActivity, "Ads Loaded");
                isAdsLoaded = true;
                if (ad != nativeAd) {
                    return;
                }
                teqbuzzVehicleListAdapter.onAdsLoaded(nativeAd);
                nativeAdAdapter.onAdsLoaded(nativeAd);
                //Utility.showSnack(mActivity, "ads loaded");
            }

            @Override
            public void onAdClicked(Ad ad) {

            }
        });

        nativeAd.loadAd();

        return fbAdView;

    }

    public void quickRunGetVehicleService() {
        hideVehicleNotFoundSnack();
        hideFavVehicleSnackBar();

        user = myPreferences.getUser();
        runInstantGetVehicleService(user.getMode(), isFavouriteFlagEnabled());
    }

    public boolean isSharedVehicleModeEnabled() {
        return isSharedVehicleModeEnabled;
    }
}
