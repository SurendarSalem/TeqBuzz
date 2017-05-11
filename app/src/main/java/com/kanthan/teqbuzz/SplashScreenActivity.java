package com.kanthan.teqbuzz;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.kanthan.teqbuzz.models.User;
import com.kanthan.teqbuzz.models.Vehicle;
import com.kanthan.teqbuzz.onebusaway.OBAWebService;
import com.kanthan.teqbuzz.service.GPSTracker;
import com.kanthan.teqbuzz.service.GetLocationService;
import com.kanthan.teqbuzz.utilities.AppDatabaseHelper;
import com.kanthan.teqbuzz.utilities.Constants;
import com.kanthan.teqbuzz.utilities.Preferences;
import com.kanthan.teqbuzz.utilities.Utility;
import com.splunk.mint.Mint;

import java.util.List;


public class SplashScreenActivity extends AppCompatActivity {

    BackgroundSplashTask backgroundSplashTask;
    private GPSTracker gpsTracker;
    AsyncTask<Void, Void, Void> getGpsTask;
    Utility utility;
    private double double_latitude, double_longitude;
    private String str_latitude, str_longitude;
    Preferences myPreferences;
    private User myUser;
    Button btn_refresh;
    boolean isCmgFromDeepLink;
    private String sharedVehicleId, sharedVehicleLineNumber, sharedVehicleAgency;
    private Intent newIntent;
    private boolean isNewIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
        onNewIntent(getIntent());

        utility = new Utility(this);
        myPreferences = new Preferences(getApplicationContext());
        Utility.decodePoly(OBAWebService.TEST_DECODED_POLYLINES);

        if (Utility.isGpsEnabled(this)) {
            // running GPS and splash task
            initializeGpsTask();
        } else {
            // running splash task
            backgroundSplashTask = new BackgroundSplashTask();
            backgroundSplashTask.execute();
        }
        // To get Device ID
        Utility.runGcmService(this);

        // Start running Vehicle Location updation Service in server
        //runGPSUpdationService();


    }

    private void runGPSUpdationService() {
        Intent intent = new Intent(this, GetLocationService.class);
        startService(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class BackgroundSplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            //Mint.initAndStartSession(SplashScreenActivity.this, Constants.MINT_ID);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Utility.createCacheFolderIfEmpty();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            moveToMapPage();

        }

    }

    private void moveToMapPage() {

        new Handler().postDelayed(new Runnable() {

			/*
             * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                finish();
                Intent i = new Intent(SplashScreenActivity.this,
                        MainActivity.class);
                i.putExtra(Constants.IS_COMING_FROM, "SplashScreenActivity");
                i.putExtra(Constants.IS_COMING_FROM_DEEP_LINKING, isCmgFromDeepLink);
                if (isCmgFromDeepLink) {
                    i.putExtra(Constants.SHARED_VEHICLE_ID, sharedVehicleId);
                    i.putExtra(Constants.SHARED_VEHICLE_LINE_NUMBER, sharedVehicleLineNumber);
                    i.putExtra(Constants.SHARED_VEHICLE_AGENCY, sharedVehicleAgency);
                }
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }, 3000);

    }

    private void initializeGpsTask() {

        getGpsTask = new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {

                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {


                /*try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
*/


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                gpsTracker = new GPSTracker(SplashScreenActivity.this);

                if (gpsTracker.canGetLocation()) {

                    // GPS success and store in cache and move to map page

                    double_latitude = gpsTracker.getLatitude();
                    double_longitude = gpsTracker.getLongitude();
                    str_latitude = String.valueOf(double_latitude);
                    str_longitude = String.valueOf(double_longitude);
                    // update user
                    /*myUser = myPreferences.getUser();
                    myUser.setTemp_latitude(str_latitude);
                    myUser.setTemp_longitude(str_longitude);
                    //myUser.setMode(User.GPS);
                    myPreferences.updateUser(myUser);
*/
                    moveToMapPage();

                } else {
                    // GPS failed and restart GPS service
                    getGpsTask.cancel(true);
                    getGpsTask = null;
                    initializeGpsTask();
                }


            }
        };

        getGpsTask.execute();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        myPreferences = new Preferences(this);
        newIntent = intent;
        isNewIntent = true;
        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            String sharedUrl = data;
            String sharedVehicleData[] = sharedUrl.split("/");
            sharedVehicleAgency = sharedVehicleData[sharedVehicleData.length - 1].replace("to=", "");
            sharedVehicleLineNumber = sharedVehicleData[sharedVehicleData.length - 2].replace("lineNumber=", "");
            sharedVehicleId = sharedVehicleData[sharedVehicleData.length - 3].replace("vehicleId=", "");
            /*sharedVehicleId = data.substring(data.lastIndexOf("/") + 1);
            sharedVehicleId = sharedVehicleId.replace("vehicleId=", "");*/

            isCmgFromDeepLink = true;
        }
    }

    private void addVehicleToSharedList(Vehicle sharedVehicle) {
        AppDatabaseHelper appDatabaseHelper = new AppDatabaseHelper(this);
        appDatabaseHelper.addVehicleToSharedList(sharedVehicle);
    }
}
