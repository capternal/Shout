package com.shout.shoutapplication.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;

import com.shout.shoutapplication.Utils.CallWebService;
import com.shout.shoutapplication.Utils.ConnectivityBroadcastReceiver;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.database.DatabaseHelper;
import com.shout.shoutapplication.main.Model.ShoutDefaultListModel;
import com.shout.shoutapplication.main.ShoutDefaultActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CapternalSystems on 9/15/2016.
 */
public class ShoutBoardBackgroundService extends Service implements CallWebService.WebserviceResponse {


    DatabaseHelper objDatabaseHelper = new DatabaseHelper(this);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        objDatabaseHelper = new DatabaseHelper(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            System.out.println("SHOUTBOARD BACKGROUND SERVICE STARTED");
            Utils.d("SERVICE :", "CALLED");
        }
        if (ConnectivityBroadcastReceiver.isConnected()) {
            callWebService();
        } else {
            Utils.d("SERVICE :", "NO INTERNET CONNECTION");
        }
        return Service.START_NOT_STICKY;
    }

    private void callWebService() {
        try {
            objDatabaseHelper = new DatabaseHelper(this);
            JSONObject objJsonObject = new JSONObject();
            SharedPreferences objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
            objJsonObject.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));
            objJsonObject.put("latitude", objSharedPreferences.getString(Constants.USER_REGISTERED_LATITUDE, ""));
            objJsonObject.put("longitude", objSharedPreferences.getString(Constants.USER_REGISTERED_LONGITUDE, ""));
            objJsonObject.put("shout_ids", objDatabaseHelper.getAllShoutId());
            new CallWebService(Constants.GET_SHOUT_BOARD_UPDATED_RECORDS, objJsonObject, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onWebserviceResponce(String strUrl, String strResult) {
        if (strUrl.equals(Constants.GET_SHOUT_BOARD_UPDATED_RECORDS)) {
            try {
                JSONObject objJsonObject = new JSONObject(strResult);
                if (objJsonObject.getBoolean("result")) {
                    JSONArray objJsonArray = objJsonObject.getJSONArray("shout");
                    for (int index = 0; index < objJsonArray.length(); index++) {
                        try {
                            ShoutDefaultListModel objShoutDefaultListModel;
                            if (objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutDistance).equals("NAN Km")) {
                                objShoutDefaultListModel = new ShoutDefaultListModel(
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutId).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutUserId).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutUserName).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutUserPic).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutCommentCount).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutLikeCount).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutEngageCount).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutType).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutTitle).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutDescription).toString(),
                                        Integer.parseInt(objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutLikeStatus).toString()),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutCreateDate).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutImage).toString(),
                                        Integer.parseInt(objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutHideStatus).toString()),
                                        ShoutDefaultActivity.VIEW_PAGER_DEFAULT_POSITION,
                                        Constants.SHOUT_PASS_ENGAGE_BUTTON_DYNAMIC_HEIGHT,
                                        Constants.DEFAULT_Y,
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutImages).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutIsSearchable),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutLatitude),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutLongitude),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutAddress),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutCategoryName),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutCategoryId),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutIsHidden),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutStartDate),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutEndDate),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutReShout),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutContinueChat),
                                        "0 Km",
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutIsFriend)
                                );
                            } else {
                                objShoutDefaultListModel = new ShoutDefaultListModel(
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutId).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutUserId).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutUserName).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutUserPic).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutCommentCount).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutLikeCount).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutEngageCount).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutType).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutTitle).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutDescription).toString(),
                                        Integer.parseInt(objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutLikeStatus).toString()),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutCreateDate).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutImage).toString(),
                                        Integer.parseInt(objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutHideStatus).toString()),
                                        ShoutDefaultActivity.VIEW_PAGER_DEFAULT_POSITION,
                                        Constants.SHOUT_PASS_ENGAGE_BUTTON_DYNAMIC_HEIGHT,
                                        Constants.DEFAULT_Y,
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutImages).toString(),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutIsSearchable),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutLatitude),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutLongitude),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutAddress),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutCategoryName),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutCategoryId),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutIsHidden),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutStartDate),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutEndDate),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutReShout),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutContinueChat),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutDistance),
                                        objJsonArray.getJSONObject(index).getString(DatabaseHelper.strShoutIsFriend)
                                );
                            }
                            objShoutDefaultListModel = objDatabaseHelper.updateShout(objShoutDefaultListModel, objShoutDefaultListModel.getSHOUT_ID());
                            if (objShoutDefaultListModel != null) {
                                Intent objIntentShoutUpdateBroadcast = new Intent(Constants.SHOUT_UPDATE_INTENT);
//                                objIntentShoutUpdateBroadcast.putExtra("SHOUT_ID", objShoutDefaultListModel.getSHOUT_ID());
                                sendBroadcast(objIntentShoutUpdateBroadcast);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
