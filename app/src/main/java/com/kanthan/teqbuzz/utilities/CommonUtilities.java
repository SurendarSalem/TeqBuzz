package com.kanthan.teqbuzz.utilities;

/**
 * Created by user on 10/18/2015.
 */

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

    // give your server registration url here
    // static final String SERVER_URL = "http://10.0.2.2/gcm_server_php/register.php";

    // Google project id
    public static String SENDER_ID = "566865380820";

    /**
     * Tag used on log messages.
     */
    public static String TAG = "AndroidHive GCM";

    public static String DISPLAY_MESSAGE_ACTION =
            "com.kanthan.teqbuzz.DISPLAY_MESSAGE";

    public static String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p/>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}