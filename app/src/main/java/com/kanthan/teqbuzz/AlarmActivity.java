package com.kanthan.teqbuzz;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gc.materialdesign.views.LayoutRipple;
import com.kanthan.teqbuzz.models.Schedule;
import com.kanthan.teqbuzz.models.User;
import com.kanthan.teqbuzz.service.MusicService;
import com.kanthan.teqbuzz.utilities.AppDatabaseHelper;
import com.kanthan.teqbuzz.utilities.Preferences;
import com.kanthan.teqbuzz.utilities.Utility;

import java.util.concurrent.TimeUnit;

/**
 * Created by suren on 9/26/2016.
 */
public class AlarmActivity extends AppCompatActivity {
    Button cancelBtn;
    private String stopName, stopId, arrivalTime;
    TextView timeTV;
    private AlertDialog.Builder closeAppDialog;
    Schedule schedule;
    AppDatabaseHelper appDatabaseHelper;
    Preferences appPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_activity);
        appDatabaseHelper = new AppDatabaseHelper(getApplicationContext());
        appPreferences = new Preferences(getApplicationContext());
        timeTV = (TextView) findViewById(R.id.time);
        Intent data = getIntent();
        stopName = data.getExtras().getString("stop_name").split("_teqbuzz_")[0];
        stopId = data.getExtras().getString("stop_name").split("_teqbuzz_")[1];
        schedule = (Schedule) data.getSerializableExtra("schedule");
        arrivalTime = data.getExtras().getString("arrival_time");
        initClosePageDialog();
        new CountDownTimer(300000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                timeTV.setText("" + String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                timeTV.setText("done!");
            }
        }.start();
        Utility.showToast(this, arrivalTime);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAppDialog.show();
            }
        });
    }

    private void initClosePageDialog() {
        closeAppDialog = new AlertDialog.Builder(this);
        closeAppDialog.setMessage("Do you want to quit this reminder?");
        closeAppDialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(AlarmActivity.this, MusicService.class);
                stopService(intent);
                dialogInterface.dismiss();
                String userId = "";
                if (appPreferences.isLoginned()) {
                    userId = appPreferences.getUser().getUser_id();
                } else {
                    userId = User.DEFAULT_USER_ID;
                }
                appDatabaseHelper.removeReminder(stopId, userId);
                finish();

            }
        });
        closeAppDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent data) {
        super.onNewIntent(data);
        arrivalTime = data.getExtras().getString("arrival_time");
        stopName = data.getExtras().getString("stop_name").split("_teqbuzz_")[0];
        stopId = data.getExtras().getString("stop_name").split("_teqbuzz_")[1];
        schedule = (Schedule) data.getSerializableExtra("schedule");
        Utility.showToast(this, arrivalTime);

    }
}
