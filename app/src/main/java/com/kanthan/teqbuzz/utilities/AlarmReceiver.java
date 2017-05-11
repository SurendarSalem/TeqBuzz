package com.kanthan.teqbuzz.utilities;

/**
 * Created by user on 5/1/2016.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import com.kanthan.teqbuzz.AlarmActivity;
import com.kanthan.teqbuzz.MainActivity;
import com.kanthan.teqbuzz.R;
import com.kanthan.teqbuzz.service.MusicService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent data) {

        String stopName = data.getExtras().getString("stop_name");
        String arrivalTime = data.getExtras().getString("arrival_time");


        Intent musicService = new Intent(context, MusicService.class);
        context.startService(musicService);
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.putExtra("stop_name", stopName);
        intent.putExtra("arrival_time", arrivalTime);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
        String stop_name = data.getStringExtra("stop_name");
        //String arrival_time = data.getStringExtra("arrival_time");


        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(context)
                .setContentTitle("TeqBuzz Alert")
                .setContentText("Your bus will arrive to " + stop_name + "\nin 5 minutes").setSmallIcon(R.drawable.bell_black)
                .setContentIntent(pIntent)
                /*.addAction(R.drawable.bell_black, "Call", pIntent)
                .addAction(R.drawable.bell_black, "More", pIntent)
                .addAction(R.drawable.bell_black, "And more", pIntent)*/
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
    }
}