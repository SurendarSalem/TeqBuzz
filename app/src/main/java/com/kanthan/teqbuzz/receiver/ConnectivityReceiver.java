package com.kanthan.teqbuzz.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.kanthan.teqbuzz.AppController;
import com.kanthan.teqbuzz.utilities.Constants;
import com.kanthan.teqbuzz.utilities.Utility;

/**
 * Created by suren on 9/19/2016.
 */
public class ConnectivityReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();
        switch (action) {
            case ConnectivityManager.CONNECTIVITY_ACTION:
                if (Utility.isConnect()) {
                    Intent connectivityIntent = new Intent(Constants.INTENT_FILTER_CONNECTIVITY);
                    connectivityIntent.putExtra(Constants.IS_CONNECTED, true);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(connectivityIntent);
                } else {
                    Intent connectivityIntent = new Intent(Constants.INTENT_FILTER_CONNECTIVITY);
                    connectivityIntent.putExtra(Constants.IS_CONNECTED, false);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(connectivityIntent);
                }
                break;
            default:
                break;
        }


    }


}
