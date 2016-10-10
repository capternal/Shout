package com.shout.shoutapplication;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.Utils;

import java.util.ArrayList;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegistrationIntentService";
    private static final String[] TOPICS = {"global"};
    public static ArrayList<String> arrMessages = new ArrayList<String>();
    private SharedPreferences objSharedPreferences;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        objSharedPreferences = getSharedPreferences(Constants.MY_PREFERENCES, 0);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            objSharedPreferences.edit().putString(Constants.DEVICE_TOKEN,token).commit();
            // [END get_token]
            Utils.d(TAG, "GCM Registration Token: " + token);


            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            objSharedPreferences.edit().putBoolean(Constants.SENT_TOKEN_TO_SERVER, true).commit();
            // [END register_for_gcm]
        } catch (Exception e) {
            Utils.d(TAG, "Failed to complete token refresh");
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            objSharedPreferences.edit().putBoolean(Constants.SENT_TOKEN_TO_SERVER, false).commit();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }
}
