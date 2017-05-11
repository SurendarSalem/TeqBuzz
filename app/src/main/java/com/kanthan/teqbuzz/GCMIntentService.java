package com.kanthan.teqbuzz;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.kanthan.teqbuzz.utilities.CommonUtilities;
import com.kanthan.teqbuzz.utilities.Preferences;
import com.kanthan.teqbuzz.utilities.ServerUtilities;


public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";
    Preferences myPreferences;
    // private DBHelper dbHelper;

    public GCMIntentService() {
        super(CommonUtilities.SENDER_ID);

    }

    /**
     * Method called on device registered
     */
    @Override
    protected void onRegistered(final Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        //  Log.d("NAME", MainActivity.name);

        myPreferences = new Preferences(context.getApplicationContext());
        if (GCMRegistrar.isRegisteredOnServer(this)) {
            myPreferences.setGCM_TOKEN(registrationId);
        } else {
            // Try to register again, but not in the UI thread.
            // It's also necessary to cancel the thread onDestroy(),
            // hence the use of AsyncTask instead of a raw thread.

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    // Register on our server
                    // On server creates a new user
                    String regId = GCMRegistrar.getRegistrationId(context);
                    ServerUtilities.register(context, regId);
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {


                   /* Intent intent1 = new Intent(context, MainActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);*/
                }

            }.execute();

        }

    }


    /**
     * Method called on device un registred
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        //displayMessage(context, getString(R.string.gcm_unregistered));
        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
       /* String message = intent.getExtras().getString("price");

        displayMessage(context, message);
       */ // notifies user

        // dbHelper = new DBHelper(context);
        myPreferences = new Preferences(context.getApplicationContext());
        String str_msg = intent.getExtras().getString("message");

       /* if (str_msg.contains("tamsonic")) {
        }*/

      /*  Message message = Utility.getMessageFromString(str_msg);

        String from_id = message.getFrom_user_id();
        String to_id = message.getTo_user_id();


        message.setFrom_user_id(to_id);
        message.setTo_user_id(from_id);


        from_id = message.getFrom_user_id();
        to_id = message.getTo_user_id();

        message.setMessage_type(String.valueOf(Message.RECEIVED));


        String my_user_id = myPreferences.getUser().getUser_id();

        if (from_id.equalsIgnoreCase(my_user_id) || from_id.equalsIgnoreCase("0")) {

            //checking same user login


            if (ConversationFragment.isInside) {
                message.setRead(Message.READ);
            } else {
                message.setRead(Message.UNREAD);
            }


            dbHelper.addMessage(message);
            if (!ConversationFragment.isInside) {
                try {
                    str_msg = URLDecoder.decode(str_msg, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                generateNotification(context, str_msg);
            } else {
                Intent newMessageIntent = new Intent("new_message");
                newMessageIntent.putExtra("message", str_msg);
                sendBroadcast(newMessageIntent);
            }

        } else {
            Toast.makeText(context, "msg came but different user", Toast.LENGTH_SHORT).show();
        }
*/
    }


    /**
     * Method called on receiving a deleted message
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        // String message = getString(R.string.gcm_deleted, total);
        //  displayMessage(context, message);
        // notifies user
        // generateNotification(context, message);
    }

    /**
     * Method called on Error
     */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        //  displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
      /*  displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));*/
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
       /* int icon = R.drawable.bazr;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Message msg_obj = Utility.getMessageFromString(message);
        Notification notification = new Notification(icon, msg_obj.getMessage(), when);

        String title = context.getString(R.string.app_name);

        Intent notificationIntent = new Intent(context, ChatActivity.class);
        notificationIntent.putExtra("isComingFrom", "push");
        notificationIntent.putExtra("product_id", msg_obj.getProduct_id());
        notificationIntent.putExtra("from_user_id", msg_obj.getTo_user_id());
        notificationIntent.putExtra("to_user_id", msg_obj.getFrom_user_id());
        notificationIntent.putExtra("product_name", msg_obj.getProduct_name());
        notificationIntent.putExtra("product_image_url", msg_obj.getProduct_image_url());


        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, title, msg_obj.getMessage(), intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);
*/
    }

}
