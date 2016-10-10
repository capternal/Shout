package com.shout.shoutapplication.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.CallWebService;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.main.Adapter.NotificationListAdapter;
import com.shout.shoutapplication.main.Model.NotificationListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationListActivity extends Activity implements CallWebService.WebserviceResponse {

    ListView objNotificationList;
    ArrayList<NotificationListModel> arrNotificationListModel = new ArrayList<NotificationListModel>();

    SharedPreferences objSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
        initialize();
        try {
            JSONObject object = new JSONObject();
            object.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));
            new CallWebService(Constants.NOTIFICATION_LIST_API, object, NotificationListActivity.this, this, true).execute();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        objNotificationList = (ListView) findViewById(R.id.listview_notification);
    }

    @Override
    public void onWebserviceResponce(String strUrl, String strResult) {
        if (Constants.NOTIFICATION_LIST_API.equals(strUrl)) {
            try {
                JSONObject objJsonObject = new JSONObject(strResult);
                if (objJsonObject.getBoolean("result")) {
                    JSONArray objJsonArray = new JSONArray(objJsonObject.getString("notifications"));
                    arrNotificationListModel.clear();
                    for (int index = 0; index < objJsonArray.length(); index++) {
                        NotificationListModel objNotificationListModel = new NotificationListModel(
                                objJsonArray.getJSONObject(index).getString("id"),
                                objJsonArray.getJSONObject(index).getString("message"),
                                objJsonArray.getJSONObject(index).getString("user_id"),
                                objJsonArray.getJSONObject(index).getString("username"),
                                objJsonArray.getJSONObject(index).getString("notification_type"),
                                objJsonArray.getJSONObject(index).getString("created"),
                                objJsonArray.getJSONObject(index).getString("user_photo")
                        );
                        arrNotificationListModel.add(objNotificationListModel);
                    }
                    if (arrNotificationListModel.size() > 0) {
                        showDefaultMessgae(false);
                        objNotificationList.setAdapter(new NotificationListAdapter(arrNotificationListModel, NotificationListActivity.this));
                    } else {
                        showDefaultMessgae(true);
                    }
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showDefaultMessgae(boolean b) {
        TextView objTextViewNotificationNotAvailable = (TextView) findViewById(R.id.txt_no_notification_found);
        if (b) {
            objTextViewNotificationNotAvailable.setVisibility(TextView.VISIBLE);
        } else {
            objTextViewNotificationNotAvailable.setVisibility(TextView.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
