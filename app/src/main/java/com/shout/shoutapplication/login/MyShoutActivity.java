package com.shout.shoutapplication.login;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.CallWebService;
import com.shout.shoutapplication.Utils.ConnectivityBroadcastReceiver;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.database.DatabaseHelper;
import com.shout.shoutapplication.login.adapter.MyShoutsAdapter;
import com.shout.shoutapplication.main.Model.ShoutDefaultListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyShoutActivity extends Activity implements CallWebService.WebserviceResponse, View.OnClickListener {

    SharedPreferences objSharedPreferences;
    private MyShoutsAdapter myShoutAdapter;
    private ListView objMyShoutsListView;
    private Button btnBackToProfileScreen;


    DatabaseHelper objDatabaseHelper;
    private ArrayList<ShoutDefaultListModel> arrShoutDefaultListModel = new ArrayList<ShoutDefaultListModel>();
    Parcelable listviewState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shout);
        // PROFILE SHARED PREFERENCES
        objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
        objDatabaseHelper = new DatabaseHelper(this);

        init();
        listviewState = objMyShoutsListView.onSaveInstanceState();
        arrShoutDefaultListModel.clear();
        arrShoutDefaultListModel = objDatabaseHelper.getShoutDefaultListModelArray("1");
        if (arrShoutDefaultListModel.size() > 0) {
            myShoutAdapter = new MyShoutsAdapter(arrShoutDefaultListModel, MyShoutActivity.this, MyShoutActivity.this);
            objMyShoutsListView.setAdapter(myShoutAdapter);
            myShoutAdapter.notifyDataSetChanged();
            loadMyShouts(false);
        } else {
            if (ConnectivityBroadcastReceiver.isConnected()) {
                loadMyShouts(true);
            } else {
                Constants.showInternetToast(MyShoutActivity.this);
            }
        }
    }

    private void init() {
        btnBackToProfileScreen = (Button) findViewById(R.id.my_shouts_back);
        objMyShoutsListView = (ListView) findViewById(R.id.my_shouts_listview);
        setListener();
    }

    private void setListener() {
        btnBackToProfileScreen.setOnClickListener(this);
    }

    private void loadMyShouts(boolean showProgress) {
        try {
            JSONObject objJsonObject = new JSONObject();
            objJsonObject.put(Constants.USER_ID, objSharedPreferences.getString(Constants.USER_ID, ""));
            new CallWebService(Constants.LOGGED_IN_USER_SHOUTS_API, objJsonObject, this, this, showProgress).execute();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWebserviceResponce(String strUrl, String strResult) {
        if (Constants.LOGGED_IN_USER_SHOUTS_API.equals(strUrl)) {
            try {
                arrShoutDefaultListModel = new ArrayList<ShoutDefaultListModel>();
                JSONObject objJsonObject = new JSONObject(strResult);
                if (objJsonObject.getBoolean("result")) {
                    arrShoutDefaultListModel = objDatabaseHelper.getShoutDefaultListModelArray("1");
                    if (arrShoutDefaultListModel.size() > 0) {
                        objDatabaseHelper.deleteMyShouts();
                    }
                    arrShoutDefaultListModel.clear();
                    arrShoutDefaultListModel.addAll(objDatabaseHelper.saveShout(new JSONArray(objJsonObject.getString("shout")), "1"));
                    Utils.d("MY_SHOUTS", "MY SHOUT MODEL ARRAY COUNT : " + arrShoutDefaultListModel.size());
                    if (arrShoutDefaultListModel.size() > 0) {
                        objMyShoutsListView.setAdapter(new MyShoutsAdapter(arrShoutDefaultListModel, MyShoutActivity.this, MyShoutActivity.this));
                    }
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_shouts_back:
                super.onBackPressed();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        init();

        if (listviewState != null) {
        /*    arrShoutDefaultListModel.clear();
            arrShoutDefaultListModel = objDatabaseHelper.getShoutDefaultListModelArray("1");
            myShoutAdapter.notifyDataSetChanged();*/
            objMyShoutsListView.onRestoreInstanceState(listviewState);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        listviewState = objMyShoutsListView.onSaveInstanceState();
    }
}
