package com.shout.shoutapplication.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Capternal on 10/30/2015.
 */
public class LocationDetails implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {


    private static final long INTERVAL = 2000 * 1;
    private static final long FASTEST_INTERVAL = 2000 * 1;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation = null;
    Location tempCurrentLocation = null;
    String mLastUpdateTime;

    private String strDbCountryName;
    private String strDbCountryCity;
    private String strDbPhoneCode;

    private String strLCountryName;
    private String strLCountryCity;
    private String strLCountryCode;
    private String strLLocality;

    SharedPreferences objSharedPreferences;


    Activity objActivity;
    public static Context objContext;
//    CurrentLocationListener objCurrentLocationListener;

    public LocationDetails(Context objContext) {
        this.objContext = objContext;
    }

    public LocationDetails(Activity objActivity, Context objContext) {
        this.objActivity = objActivity;
        this.objContext = objContext;
//        objCurrentLocationListener = (CurrentLocationListener) objActivity;
    }

    public void startTracking() {
        if (!isGooglePlayServicesAvailable()) {
//            Utils.showGPSDisabledAlertToUser((ActionBarActivity) objContext);
            Toast.makeText(objActivity, "Please update your google play services", Toast.LENGTH_SHORT).show();
        } else {
            createLocationRequest();
            mGoogleApiClient = new GoogleApiClient.Builder(objContext)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();
            System.out.println("Location tracing has been started.");
        }
    }

    public void stopTracking() {
        System.out.println("Location tracing has been stoped.");
        mGoogleApiClient.disconnect();
        mLocationRequest = null;
        mGoogleApiClient = null;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(objContext);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
//            GooglePlayServicesUtil.getErrorDialog(status, objActivity, 0).show();
            return false;
        }
    }

    protected void startLocationUpdates() {
        try {
            if (ActivityCompat.checkSelfPermission(objContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(objContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            System.out.println("Location update started ..............: ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
//        getLocation();

        try {
            Double latitude = mCurrentLocation.getLatitude();
            Double longitude = mCurrentLocation.getLongitude();

//            objCurrentLocationListener.getLatitudeLongitude(new LatLng(latitude, longitude));

            double locationAccuraccy = mCurrentLocation.getAccuracy();

            System.out.println("FINAL LAT LONG : " + latitude + " : " + longitude);

            objSharedPreferences = objContext.getSharedPreferences(Constants.LOCATION_PREFERENCES, Context.MODE_PRIVATE);

            LocationManager manager = (LocationManager) objContext.getSystemService(Context.LOCATION_SERVICE);
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (statusOfGPS) {
                SharedPreferences.Editor objEditor = objSharedPreferences.edit();
                objEditor.putString(Constants.LOCATION_LATITUDE, String.valueOf(latitude));
                objEditor.putString(Constants.LOCATION_LONGITUDE, String.valueOf(longitude));
                objEditor.commit();
            } else {
                SharedPreferences.Editor objEditor = objSharedPreferences.edit();
                objEditor.putString(Constants.LOCATION_LATITUDE, "");
                objEditor.putString(Constants.LOCATION_LONGITUDE, "");
                objEditor.commit();
            }
            if (latitude != null && longitude != null) {
                stopTracking();
            }
        } catch (Exception elocation) {
            elocation.printStackTrace();
        }
    }

    public String getGoogleMapThumbnail(double lati, double longi) {
        String url = "http://maps.google.com/maps/api/staticmap?center=" + lati + "," + longi + "&zoom=15&size=500x200&markers=" + lati + "," + longi + "&sensor=false";
        return url;
    }

    public void getLocation() {
        try {

           /* double latitude = mCurrentLocation.getLatitude();
            double longitude = mCurrentLocation.getLongitude();

            System.out.println("FINAL LAT LONG : " + latitude + " : " + longitude);

            objSharedPreferences = objContext.getSharedPreferences(Constants.DATA, Context.MODE_PRIVATE);

            SharedPreferences.Editor objEditor = objSharedPreferences.edit();
            objEditor.putString(Constants.KEY_LATITUDE, String.valueOf(latitude));
            objEditor.putString(Constants.KEY_LONGITUDE, String.valueOf(longitude));
            objEditor.commit();*/

           /* Geocoder gcd = new Geocoder(objContext, Locale.getDefault());
            List<Address> addresses;
            try {
                if (latitude != 0 && longitude != 0) {
                    addresses = gcd.getFromLocation(latitude, longitude, 1);
                    if (addresses.size() > 0) {
                        System.out.println("LOCATION ADDRESS=" + addresses.get(0));
                        System.out.println("LOCATION Country=" + addresses.get(0).getCountryName());
                        System.out.println("LOCATION Country Code=" + addresses.get(0).getCountryCode());
                        System.out.println("LOCATION SubAdmin=" + addresses.get(0).getSubAdminArea());
                        System.out.println("LOCATION Locality=" + addresses.get(0).getLocality());


                        strLCountryName = addresses.get(0).getCountryName().toString();
                        strLCountryCode = addresses.get(0).getCountryCode().toString();
//                        strLCountryCity = addresses.get(0).getSubAdminArea().toString();
                        strLLocality = addresses.get(0).getLocality().toString();


                        mGoogleApiClient.disconnect();
//                        }
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyDatabase(Context c, String DATABASE_NAME) {
        String databasePath = c.getDatabasePath(DATABASE_NAME).getPath();
        File f = new File(databasePath);
        OutputStream myOutput = null;
        InputStream myInput = null;
        Log.d("testing", " testing db path " + databasePath);
        Log.d("testing", " testing db exist " + f.exists());

        if (f.exists()) {
            try {

                File directory = new File("/sdcard/TRAVELU");
                if (!directory.exists())
                    directory.mkdir();

                myOutput = new FileOutputStream(directory.getAbsolutePath()
                        + "/" + DATABASE_NAME);
                myInput = new FileInputStream(databasePath);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
            } catch (Exception e) {
            } finally {
                try {
                    if (myOutput != null) {
                        myOutput.close();
                        myOutput = null;
                    }
                    if (myInput != null) {
                        myInput.close();
                        myInput = null;
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("onConnected - isConnected ...............: "
                + mGoogleApiClient.isConnected());
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        } else {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("Connection Suspended ");
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("Connection failed: " + connectionResult.toString());
        mGoogleApiClient.disconnect();
    }

    public Location getCurrentLocation() {
        /*String strLatLong = "";
        System.out.println("GET CURRENT LATITUDE : " + mCurrentLocation.getLatitude());
        System.out.println("GET CURRENT LONGITUDE : " + mCurrentLocation.getLongitude());

        strLatLong = "" + String.valueOf(mCurrentLocation.getLatitude()) + "-" + String.valueOf(mCurrentLocation.getLongitude());

        strLatLong.concat();
        strLatLong.concat("-");
        strLatLong.concat(String.valueOf(mCurrentLocation.getLongitude()));
        System.out.println("GET CURRENT LOCATION : " + strLatLong);*/
        return mCurrentLocation;
    }

   /* public interface CurrentLocationListener {
        public void getLatitudeLongitude(LatLng objLatLng);
    }*/

}
