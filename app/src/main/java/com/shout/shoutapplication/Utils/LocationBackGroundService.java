package com.shout.shoutapplication.Utils;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.shout.shoutapplication.main.CreateShoutActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Capternal on 15/12/15.
 */
public class LocationBackGroundService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static GoogleApiClient objGoogleApiClient;
    private LocationRequest objLocationRequest;
    private static final long INTERVAL = 30 * 1000;
    private static final long FASTEST_INTERVAL = INTERVAL / 2;
    private Location objLastKnowLocation;
    private String strBatteryLevel;
    private static final int ONE_MINUTES = 1000 * 60;
    private SharedPreferences objSharedPreferences;
    ConnectionDetector objConnectionDetector;
    SimpleDateFormat objSimpleDateFormat;
    Calendar objCalendar;
    int order_id;
    boolean locationStart = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createGoogleApiClient();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        objSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        objCalendar = Calendar.getInstance();
        System.out.println("I m in Start Command");

        objSharedPreferences = getSharedPreferences(Constants.MY_PREFERENCES, 0);

        if (!objGoogleApiClient.isConnected()) {
            objGoogleApiClient.connect();
        }
        if (objGoogleApiClient != null && objGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (objGoogleApiClient != null && objGoogleApiClient.isConnected()) {
            stopLocationUpdates();
            objGoogleApiClient.disconnect();
        }
        super.onDestroy();
    }

    private void createGoogleApiClient() {
        objGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        createLocationRequest();
    }

    private void createLocationRequest() {
        objLocationRequest = new LocationRequest();
        objLocationRequest.setInterval(INTERVAL);
        objLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        objLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                objGoogleApiClient, objLocationRequest, this);
        objLastKnowLocation = LocationServices.FusedLocationApi.getLastLocation(objGoogleApiClient);
        Log.d("LOCATION SERVICE", "Location update started ..............: ");
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                objGoogleApiClient, this);
        if (objGoogleApiClient != null && objGoogleApiClient.isConnected()) {
            //stopLocationUpdates();
            objGoogleApiClient.disconnect();
        }
        Log.d("LOCATION SERVICE", "Location update stopped .......................");
    }

    private boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > ONE_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -ONE_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void onLocationChanged(Location location) {

        System.out.println("Location Detect Start");


        String strDate = objSimpleDateFormat.format(objCalendar.getTime());

        if (null != location) {

            String lat = String.valueOf(location.getLatitude());
            String lng = String.valueOf(location.getLongitude());
            System.out.println("At Time: " + DateFormat.getTimeInstance().format(new Date()) + "\n" +
                    "Latitude: " + lat + "\n" +
                    "Longitude: " + lng + "\n" +
                    "Accuracy: " + location.getAccuracy() + "\n" +
                    "Provider: " + location.getProvider() + "\n" +
                    "Battery Level" + strBatteryLevel);


            if (location.hasAccuracy() && location.getAccuracy() <= 200) {

                if (isBetterLocation(location, objLastKnowLocation)) {
                    SharedPreferences objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
                    if (objSharedPreferences.getString(Constants.CURRENT_ACTIVITY_NAME_FOR_SHOW_MAP, "").equals(Constants.PROFILE_ATIVITY)) {
//                        ProfileScreenActivity.updateMapLocation(location.getLatitude(), location.getLongitude());
                        stopLocationUpdates();
                        stopSelf();
                    } else {
                        CreateShoutActivity.updateMapLocation(location.getLatitude(), location.getLongitude());
                        stopLocationUpdates();
                        stopSelf();
                    }

                }
            }
        }

        System.out.println("Location Detect End");
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (objGoogleApiClient != null && objGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
