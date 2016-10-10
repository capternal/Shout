package com.shout.shoutapplication.login;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.shout.shoutapplication.R;
import com.shout.shoutapplication.RegistrationIntentService;
import com.shout.shoutapplication.Utils.ConnectivityBroadcastReceiver;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.LocationDetails;
import com.shout.shoutapplication.Utils.NetworkUtils;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.main.ShoutDefaultActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog objProgressDialog;
    Button btnFacebookLogin;
    private List<String> arrPermission;
    private CallbackManager callbackManager;
    ProfileTracker profileTracker;

    public static String strProfilePicUrl = "";
    public static String strFirstName = "";
    public static String strLastName = "";
    public static String strEmail = "";
    public static String strFacebookUserId = "";
    SharedPreferences objSharedPreferences;
    LocationDetails objLocationDetails;
    // GCM INTEGRATION
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        objProgressDialog = new ProgressDialog(LoginActivity.this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

       try{
           objLocationDetails = new LocationDetails(LoginActivity.this);
           objLocationDetails.startTracking();
       }catch(NullPointerException ne){
           ne.printStackTrace();
       }catch(Exception e){
           e.printStackTrace();
       }

        arrPermission = Arrays.asList("public_profile", "email", "user_friends");

        objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.shout.shoutapplication", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
//            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean sentToken = objSharedPreferences.getBoolean(Constants.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    // Get sentToken
                    //setResultCode(Activity.RESULT_OK);
                } else {
                    // Token Error
                }
            }
        };

        if (Utils.checkPlayServices(LoginActivity.this)) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.REGISTRATION_COMPLETE));
        try{
            objLocationDetails = new LocationDetails(LoginActivity.this);
            objLocationDetails.startTracking();
        }catch(NullPointerException ne){
            ne.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("");
        try{
            objLocationDetails = new LocationDetails(LoginActivity.this);
            objLocationDetails.startTracking();
        }catch(NullPointerException ne){
            ne.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        if (objProgressDialog.isShowing()) {
            objProgressDialog.dismiss();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (objProgressDialog.isShowing()) {
            objProgressDialog.dismiss();
        }
    }

    private void init() {
        btnFacebookLogin = (Button) findViewById(R.id.btn_facebook_login);

        btnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityBroadcastReceiver.isConnected()) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, arrPermission);
                } else {
                    Constants.showInternetToast(LoginActivity.this);
                }
            }
        });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                setProfile(currentProfile);
            }
        };

        //Register a callback
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        System.out.println("FACEBOOK SUCCESS");
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("FACEBOOK LOGIN CANCELLED BY USER");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        exception.printStackTrace();
                    }
                });
    }


    private void setProfile(Profile profile) {
        if (profile != null) {
            try {
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/{education-experience-id}",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                System.out.println("NEW RESPONSE : " + response);
                            }
                        }
                ).executeAsync();

                GraphRequestBatch batch = new GraphRequestBatch(
                        GraphRequest.newMyFriendsRequest(
                                AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONArrayCallback() {
                                    @Override
                                    public void onCompleted(
                                            final JSONArray jsonArray,
                                            GraphResponse response) {
                                        System.out.println("FRIENDS RESPONSE 1: " + response);
                                        System.out.println("FRIENDS RESPONSE 2: " + jsonArray);

                                        SharedPreferences objSharedPreferences=getSharedPreferences(Constants.PROFILE_PREFERENCES,MODE_PRIVATE);
                                        SharedPreferences.Editor objEditor = objSharedPreferences.edit();
                                        objEditor.putString(Constants.USER_FACEBOOK_FRIENDS_JSON,String.valueOf(jsonArray));
                                        objEditor.commit();
                                    }
                                })
                );
                batch.addCallback(new GraphRequestBatch.Callback() {
                    @Override
                    public void onBatchCompleted(GraphRequestBatch graphRequests) {
                        // Application code for when the batch finishes
                    }
                });
                batch.executeAsync();
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,name,email,link,picture.type(large),gender,user_birthday,user_education_history,user_work_history");
            } catch (NullPointerException ne) {
                ne.printStackTrace();
                System.out.println("FACEBOOK ACCESS TOKEN EXPIRED");
            } catch (Exception e) {
                e.printStackTrace();
            }
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject me,
                                GraphResponse response) {

                            System.out.println("PRASANNA RESPONSE 1: " + response);
                            System.out.println("PRASANNA RESPONSE 2: " + me);

                            if (response.getError() != null) {
                            } else {
                                try {
                                    System.out.println("\nFACEBOOK DATA NAME        : " + me.optString("name"));
                                    System.out.println("\nFACEBOOK DATA FIRST NAME  : " + me.optString("first_name"));
                                    System.out.println("\nFACEBOOK DATA LAST NAME   : " + me.optString("last_name"));
                                    System.out.println("\nFACEBOOK DATA FACEBOOK ID : " + me.optString("id"));
                                    System.out.println("\nFACEBOOK DATA EMAIL       : " + me.getString("email"));
                                    System.out.println("\nFACEBOOK DATA PROFILE PIC : " + me.optString("picture"));

                                    JSONObject objOutterJsonObject = new JSONObject(me.optString("picture"));
                                    String strData = objOutterJsonObject.getString("data");
                                    JSONObject objInnerJsonobject = new JSONObject(strData);
                                    System.out.println("FACEBOOK PROFILE URL : " + objInnerJsonobject.getString("url"));

                                    strFacebookUserId = me.optString("id");
                                    strProfilePicUrl = objInnerJsonobject.getString("url");
                                    strFirstName = me.optString("first_name");
                                    strLastName = me.optString("last_name");
                                    strEmail = me.optString("email");

                                    if (me.getString("email").isEmpty()) {
                                        Toast toast = Toast.makeText(LoginActivity.this, "Please register your email to your facebook account", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    } else {
                                        try {
//                                            LoginManager.getInstance().logOut();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        Double latitude = null;
                                        Double longitude = null;
                                        try {
                                            latitude = objLocationDetails.getCurrentLocation().getLatitude();
                                            longitude = objLocationDetails.getCurrentLocation().getLongitude();
                                        } catch (NullPointerException ne) {
                                            latitude = 0.00;
                                            longitude = 0.00;
                                            ne.printStackTrace();
/*                                            new android.app.AlertDialog.Builder(LoginActivity.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .setTitle("")
                                                    .setMessage("No Location Detected....Please try again.")
                                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    })
                                                    .show();*/
//                                            objLocationDetails.startTracking();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        System.out.println("USER LOCATION : " + latitude + " : " + longitude);
                                        new LoginApi().execute(strFirstName, strLastName, strEmail, strFacebookUserId, String.valueOf(latitude), String.valueOf(longitude));
//                                            LoginManager.getInstance().logOut();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    String stackTrace = Log.getStackTraceString(e);
                                }
                            }
                        }
                    });
            Bundle parameters2 = new Bundle();
            parameters2.putString("fields", "id,first_name,last_name,name,email,picture.type(large),gender,age_range");
            request.setParameters(parameters2);
            request.executeAsync();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("REQUEST CODE" + requestCode);
        switch (requestCode) {
            case 64206:
                if (data != null) {
                    callbackManager.onActivityResult(requestCode, resultCode, data);
                }
                break;
            case 1000:
                System.out.println("GPS IS ENABLED ");
                try{
                    objLocationDetails = new LocationDetails(LoginActivity.this);
                    objLocationDetails.startTracking();
                }catch(NullPointerException ne){
                    ne.printStackTrace();
                }catch(Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    public class LoginApi extends AsyncTask<String, Void, String> {

        final ProgressDialog objProgressDialog = new ProgressDialog(LoginActivity.this);
        String strResult = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
//                LoginManager.getInstance().logOut();
                objProgressDialog.setTitle("Loading...");
                objProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                SharedPreferences objMypreferences = getSharedPreferences(Constants.MY_PREFERENCES, 0);
                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("first_name", params[0]);
                objJsonObject.put("last_name", params[1]);
                objJsonObject.put("email", params[2]);
                objJsonObject.put("facebook_id", params[3]);
                objJsonObject.put("device_token", objMypreferences.getString(Constants.DEVICE_TOKEN, ""));
                objJsonObject.put("latitude", params[4]);
                objJsonObject.put("longitude", params[5]);

                strResult = NetworkUtils.postData(Constants.LOGIN_URL, objJsonObject.toString());
                return strResult;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return strResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.length() > 0) {
                try {
                    if (objProgressDialog.isShowing()) {
                        objProgressDialog.dismiss();
                    }
                    JSONObject objJsonObject = new JSONObject(s);
                    String result = objJsonObject.getString("result");
                    if (result.equals("true")) {
                        JSONObject objInnerJsonObject = new JSONObject(objJsonObject.getString("user"));
                        if (objInnerJsonObject.getString("is_registered").equals("N")) {
                            SharedPreferences.Editor objEditor = objSharedPreferences.edit();
                            objEditor.putString(Constants.USER_NAME, strFirstName + " " + strLastName);
                            objEditor.putString(Constants.PROFILE_IMAGE_URL, strProfilePicUrl);
                            objEditor.putString(Constants.PROFILE_EMAIL_ID, strEmail);
                            objEditor.putString(Constants.PROFILE_LOGIN_STATUS, "true");
                            objEditor.putString(Constants.LOGIN_FLAG, objInnerJsonObject.getString("is_registered"));
                            objEditor.putString(Constants.USER_ID, objInnerJsonObject.getString("id"));
                            objEditor.putString(Constants.USER_FACEBOOK_ID, objInnerJsonObject.getString("facebook_id"));
                            objEditor.putString(Constants.IS_NEW_USER, "true");
                            objEditor.putString(Constants.USER_REGISTERED_LATITUDE, objInnerJsonObject.getString("latitude"));
                            objEditor.putString(Constants.USER_REGISTERED_LONGITUDE, objInnerJsonObject.getString("longitude"));
                            objEditor.putString(Constants.SORT_POPULARITY, "0");
                            objEditor.putString(Constants.SORT_RECENCY, "0");
                            objEditor.putString(Constants.SORT_LOCATION, "0");
                            objEditor.putString(Constants.OTP_VERIFIED, objInnerJsonObject.getString("otp_verified"));
                            objEditor.putString(Constants.PROFILE_COMPLETE, objInnerJsonObject.getString("profile_complete"));
//                            objEditor.putString(Constants.SHOUT_PASS_ALERT_SHOW_STATUS, "true");
                            objEditor.commit();

                            if (objSharedPreferences.getString(Constants.OTP_VERIFIED, "").equals("0")) {
                                Intent objIntent = new Intent(LoginActivity.this, SendOTPActivity.class);
                                startActivity(objIntent);
                                finish();
                                overridePendingTransition(0, 0);
                            } else if (objSharedPreferences.getString(Constants.OTP_VERIFIED, "").equals("1")) {
                                if (objSharedPreferences.getString(Constants.PROFILE_COMPLETE, "").equals("0")) {
                                    Intent objIntent = new Intent(LoginActivity.this, ProfileScreenActivity.class);
                                    startActivity(objIntent);
                                    finish();
                                    overridePendingTransition(0, 0);
                                } else {
                                    Intent objIntent = new Intent(LoginActivity.this, ShoutDefaultActivity.class);
                                    startActivity(objIntent);
                                    finish();
                                    overridePendingTransition(0, 0);
                                }
                            } else {
                                Intent objIntent = new Intent(LoginActivity.this, SendOTPActivity.class);
                                startActivity(objIntent);
                                finish();
                                overridePendingTransition(0, 0);
                            }
                        } else {
                            SharedPreferences.Editor objEditor = objSharedPreferences.edit();
                            objEditor.putString(Constants.USER_NAME, strFirstName + " " + strLastName);
                            objEditor.putString(Constants.PROFILE_IMAGE_URL, objInnerJsonObject.getString("profile_pic"));
                            objEditor.putString(Constants.PROFILE_EMAIL_ID, strEmail);
                            objEditor.putString(Constants.PROFILE_LOGIN_STATUS, "true");
                            objEditor.putString(Constants.LOGIN_FLAG, objInnerJsonObject.getString("is_registered"));
                            objEditor.putString(Constants.USER_ID, objInnerJsonObject.getString("id"));
                            objEditor.putString(Constants.USER_FACEBOOK_ID, objInnerJsonObject.getString("facebook_id"));
                            objEditor.putString(Constants.IS_NEW_USER, "false");
                            objEditor.putString(Constants.USER_REGISTERED_LATITUDE, objInnerJsonObject.getString("latitude"));
                            objEditor.putString(Constants.USER_REGISTERED_LONGITUDE, objInnerJsonObject.getString("longitude"));
                            objEditor.putString(Constants.SORT_POPULARITY, "0");
                            objEditor.putString(Constants.SORT_RECENCY, "0");
                            objEditor.putString(Constants.SORT_LOCATION, "0");
                            objEditor.putString(Constants.OTP_VERIFIED, objInnerJsonObject.getString("otp_verified"));
                            objEditor.putString(Constants.PROFILE_COMPLETE, objInnerJsonObject.getString("profile_complete"));
//                            objEditor.putString(Constants.SHOUT_PASS_ALERT_SHOW_STATUS, "true");
                            objEditor.commit();

                            if (objSharedPreferences.getString(Constants.OTP_VERIFIED, "").equals("0")) {
                                Intent objIntent = new Intent(LoginActivity.this, SendOTPActivity.class);
                                startActivity(objIntent);
                                finish();
                                overridePendingTransition(0, 0);
                            } else if (objSharedPreferences.getString(Constants.OTP_VERIFIED, "").equals("1")) {
                                if (objSharedPreferences.getString(Constants.PROFILE_COMPLETE, "").equals("0")) {
                                    Intent objIntent = new Intent(LoginActivity.this, ProfileScreenActivity.class);
                                    startActivity(objIntent);
                                    finish();
                                    overridePendingTransition(0, 0);
                                } else {
                                    Intent objIntent = new Intent(LoginActivity.this, ShoutDefaultActivity.class);
                                    startActivity(objIntent);
                                    finish();
                                    overridePendingTransition(0, 0);
                                }
                            } else {
                                Intent objIntent = new Intent(LoginActivity.this, SendOTPActivity.class);
                                startActivity(objIntent);
                                finish();
                                overridePendingTransition(0, 0);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
