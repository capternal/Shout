package com.shout.shoutapplication.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.shout.shoutapplication.CustomClasses.CustomFontTextView;
import com.shout.shoutapplication.R;
import com.shout.shoutapplication.RegistrationIntentService;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.NetworkUtils;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.base.BaseActivity;
import com.shout.shoutapplication.database.DatabaseHelper;
import com.shout.shoutapplication.main.Adapter.MessageBoardListAdapter;
import com.shout.shoutapplication.main.Model.MessageBoardModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageBoardActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    ListView objListViewMessageBoard;
    ImageButton objImageButtonBack;
    TextView objTextViewNoShoutFound;
    CustomFontTextView objTextViewScreenTitle;

    ArrayList<MessageBoardModel> arrMessageBoardModels;
    ArrayList<String> arrMessageTitle;
    ArrayList<String> arrMessageGroupMembers;
    ArrayList<String> arrMessageMessageCount;
    ArrayList<String> arrMessageImageUrl;

    DatabaseHelper objDatabaseHelper;
    // SWIPRE REFRESH LAYOUT
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);

        RegistrationIntentService.arrMessages.clear();

        objDatabaseHelper = new DatabaseHelper(this);

        updateNotificationCount(getSharedPreferences(Constants.PROFILE_PREFERENCES,MODE_PRIVATE).getString(Constants.USER_NOTIFICATION_COUNT,""));

        hideBottomTabs();
        showDefaultTopHeader();

        BaseActivity.objShoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(MessageBoardActivity.this, ShoutDefaultActivity.class);
                startActivity(objIntent);
                finish();
                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
            }
        });

        initialize();

        // SET OFFLINE DATA FOR MESSAGE BOARD LIST SCREEN
        try {
            String strResult = objDatabaseHelper.getMessageBoardListItems();
            if (strResult.length() > 0) {
                arrMessageBoardModels = new ArrayList<MessageBoardModel>();
                JSONArray objDBJsonArray = new JSONArray(strResult);
                for (int index = 0; index < objDBJsonArray.length(); index++) {
                    MessageBoardModel objMessageBoardModel = new MessageBoardModel(
                            objDBJsonArray.getJSONObject(index).getString("id"),//shout_id
                            objDBJsonArray.getJSONObject(index).getString("title"),//shout_title
                            objDBJsonArray.getJSONObject(index).getString("count"),//group_message_count
                            Constants.HTTP_URL + objDBJsonArray.getJSONObject(index).getString("photo"), // shout_image
                            objDBJsonArray.getJSONObject(index).getString("shout_owner_id"),// shout_owner_id
                            objDBJsonArray.getJSONObject(index).getString("users"),// shout_members_name
                            objDBJsonArray.getJSONObject(index).getString("user_pic"),// shout owner profile image
                            objDBJsonArray.getJSONObject(index).getString("user_name"),// shout_owner_name
                            objDBJsonArray.getJSONObject(index).getString("shout_type"));//shout_type
                    arrMessageBoardModels.add(objMessageBoardModel);
                }
                if (arrMessageBoardModels.size() > 0) {
                    objListViewMessageBoard.setVisibility(ListView.VISIBLE);
                    objTextViewNoShoutFound.setVisibility(TextView.GONE);
                    objListViewMessageBoard.setAdapter(new MessageBoardListAdapter(arrMessageBoardModels, MessageBoardActivity.this));
                } else {
                    objTextViewNoShoutFound.setVisibility(TextView.VISIBLE);
                    objListViewMessageBoard.setVisibility(ListView.GONE);
                }
            } else {
                Utils.d("MESSAGE_BOARD", "DATA NOT AVAILABLE IN LOCAL DB.");
                arrMessageBoardModels = new ArrayList<MessageBoardModel>();
                for (int index = 0; index < 2; index++) {
                    MessageBoardModel objMessageBoardModel = new MessageBoardModel(
                            "",//shout_id
                            "",//shout_title
                            "",//group_message_count
                            "", // shout_image
                            "",// shout_owner_id
                            "",// shout_members_name
                            "",// shout owner profile image
                            "",// shout_owner_name
                            "");//shout_type
                    arrMessageBoardModels.add(objMessageBoardModel);
                }
                if (arrMessageBoardModels.size() > 0) {
                    objListViewMessageBoard.setVisibility(ListView.VISIBLE);
                    objTextViewNoShoutFound.setVisibility(TextView.GONE);
                    objListViewMessageBoard.setAdapter(new MessageBoardListAdapter(arrMessageBoardModels, MessageBoardActivity.this));
                } else {
                    objTextViewNoShoutFound.setVisibility(TextView.VISIBLE);
                    objListViewMessageBoard.setVisibility(ListView.GONE);
                }
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new CallShoutListAPI().execute();

        objListViewMessageBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageBoardModel objMessageBoardModel = null;
                if (arrMessageBoardModels.size() > 0) {
                    System.out.print("DATA Message Board Position : " + position);
                    objMessageBoardModel = arrMessageBoardModels.get(position);
                    SharedPreferences objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
                    if (objMessageBoardModel.getShoutOwnerId().equals(objSharedPreferences.getString(Constants.USER_ID, ""))) {

                        SharedPreferences objChatPreferences = getSharedPreferences(Constants.CHAT_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor objDataChatEditor = objChatPreferences.edit();
                        objDataChatEditor.putString(Constants.CHAT_APPONENT_ID, objMessageBoardModel.getShoutOwnerId());
                        objDataChatEditor.putString(Constants.CHAT_APPONENT_USER_NAME, objMessageBoardModel.getUserName());
                        objDataChatEditor.putString(Constants.CHAT_APPONENT_PROFILE_PIC, objMessageBoardModel.getProfilePic());
                        objDataChatEditor.putString(Constants.CHAT_SHOUT_ID, objMessageBoardModel.getShoutId());
                        objDataChatEditor.putString(Constants.CHAT_SHOUT_TITLE, objMessageBoardModel.getMessageTitle());
                        objDataChatEditor.putString(Constants.CHAT_SHOUT_TYPE, objMessageBoardModel.getShout_type());
                        objDataChatEditor.putString(Constants.CHAT_SHOUT_IMAGE, objMessageBoardModel.getItemImage());
                        objDataChatEditor.putString(Constants.CHAT_BACK, Constants.SHOUT_MESSAGE_BOARD_SCREEN);
                        objDataChatEditor.commit();


                        Intent objIntent = new Intent(MessageBoardActivity.this, ShoutUsersListActivity.class);
                        startActivity(objIntent);
                        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                    } else {
                        SharedPreferences objChatPreferences = getSharedPreferences(Constants.CHAT_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor objDataChatEditor = objChatPreferences.edit();
                        objDataChatEditor.putString(Constants.CHAT_APPONENT_ID, objMessageBoardModel.getShoutOwnerId());
                        objDataChatEditor.putString(Constants.CHAT_APPONENT_USER_NAME, objMessageBoardModel.getUserName());
                        objDataChatEditor.putString(Constants.CHAT_APPONENT_PROFILE_PIC, objMessageBoardModel.getProfilePic());
                        objDataChatEditor.putString(Constants.CHAT_SHOUT_ID, objMessageBoardModel.getShoutId());
                        objDataChatEditor.putString(Constants.CHAT_SHOUT_TITLE, objMessageBoardModel.getMessageTitle());
                        objDataChatEditor.putString(Constants.CHAT_SHOUT_TYPE, objMessageBoardModel.getShout_type());
                        objDataChatEditor.putString(Constants.CHAT_SHOUT_IMAGE, objMessageBoardModel.getItemImage());
                        objDataChatEditor.putString(Constants.CHAT_BACK, Constants.SHOUT_MESSAGE_BOARD_SCREEN);
                        objDataChatEditor.commit();

                        Intent objIntent = new Intent(MessageBoardActivity.this, ChatForShoutActivity.class);
                        startActivity(objIntent);
                        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
        new CallShoutListAPI().execute();
    }

    private void initialize() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.red_background_color);
        objListViewMessageBoard = (ListView) findViewById(R.id.listview_message_board);
        objImageButtonBack = (ImageButton) findViewById(R.id.image_button_message_board_back);
        objTextViewNoShoutFound = (TextView) findViewById(R.id.txt_no_shout_found_message_board);
        objTextViewScreenTitle = (CustomFontTextView) findViewById(R.id.txt_screen_message_board_title);

        BaseActivity.objImageNotificationCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(MessageBoardActivity.this,NotificationListActivity.class);
                startActivity(objIntent);
            }
        });

        setListener();
    }

    private void setListener() {
        objImageButtonBack.setOnClickListener(this);
        objListViewMessageBoard.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }

            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent objIntent;
        switch (v.getId()) {
            case R.id.image_button_message_board_back:
                objIntent = new Intent(MessageBoardActivity.this, ShoutDefaultActivity.class);
                objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(objIntent);
                finish();
                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent objIntent = new Intent(MessageBoardActivity.this, ShoutDefaultActivity.class);
        objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(objIntent);
        finish();
        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
    }

    @Override
    public void onRefresh() {
        new CallShoutListAPI().execute();
    }

    public class CallShoutListAPI extends AsyncTask<String, Void, String> {
        // final ProgressDialog objProgressDialog = new ProgressDialog(MessageBoardActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  if (objDatabaseHelper.getRecordCountOfTable(DatabaseHelper.strTableNameMessageBoard) == false) {
                objProgressDialog.setMessage("Loading...");
                objProgressDialog.show();
                objProgressDialog.setCanceledOnTouchOutside(false);
            }*/
        }

        @Override
        protected String doInBackground(String... params) {
            String strResult = "";
            try {
                SharedPreferences objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));
                strResult = NetworkUtils.postData(Constants.MESSAGE_SHOUT_LIST_API, objJsonObject.toString());
                return strResult;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return strResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
               /* if (objProgressDialog.isShowing()) {
                    objProgressDialog.dismiss();
                }*/
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                JSONObject objJsonObject = new JSONObject(s);
                if (objJsonObject.getString("result").equals("true")) {

                    // DELETING MESSAGE BOARD DATA FROM DATABASE
                    objDatabaseHelper.deleteTable(DatabaseHelper.strTableNameMessageBoard);

                    // SAVING MESSAGE BOARD LIST DATA INTO DATABASE
                    objDatabaseHelper.insertMessageBoardItems(objJsonObject.getString("shouts"));

                    JSONArray objJsonArray = new JSONArray(objJsonObject.getString("shouts"));

                    arrMessageBoardModels = new ArrayList<MessageBoardModel>();
                    for (int index = 0; index < objJsonArray.length(); index++) {
                        MessageBoardModel objMessageBoardModel = new MessageBoardModel(
                                objJsonArray.getJSONObject(index).getString("id"),//shout_id
                                objJsonArray.getJSONObject(index).getString("title"),//shout_title
                                objJsonArray.getJSONObject(index).getString("count"),//group_message_count
                                objJsonArray.getJSONObject(index).getString("photo"), // shout_image
                                objJsonArray.getJSONObject(index).getString("shout_owner_id"),// shout_owner_id
                                objJsonArray.getJSONObject(index).getString("users"),// shout_members_name
                                objJsonArray.getJSONObject(index).getString("user_pic"),// shout owner profile image
                                objJsonArray.getJSONObject(index).getString("user_name"),// shout_owner_name
                                objJsonArray.getJSONObject(index).getString("shout_type"));//shout_type
                        arrMessageBoardModels.add(objMessageBoardModel);
                    }
                    if (arrMessageBoardModels.size() > 0) {
                        objListViewMessageBoard.setVisibility(ListView.VISIBLE);
                        objTextViewNoShoutFound.setVisibility(TextView.GONE);
                        objListViewMessageBoard.setAdapter(new MessageBoardListAdapter(arrMessageBoardModels, MessageBoardActivity.this));
                    } else {
                        objTextViewNoShoutFound.setVisibility(TextView.VISIBLE);
                        objListViewMessageBoard.setVisibility(ListView.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
