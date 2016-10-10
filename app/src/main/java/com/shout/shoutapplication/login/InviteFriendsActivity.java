package com.shout.shoutapplication.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.ConnectivityBroadcastReceiver;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.NetworkUtils;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.database.DatabaseHelper;
import com.shout.shoutapplication.login.model.ContactModel;
import com.shout.shoutapplication.main.Adapter.ContactExpandableListAdapter;
import com.shout.shoutapplication.main.Model.Continent;
import com.shout.shoutapplication.main.ShoutDefaultActivity;
import com.shout.shoutapplication.others.SoftKeyboard;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

public class InviteFriendsActivity extends Activity implements View.OnClickListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    //    SpotsDialog progressDialog;
    private ProgressBar progressBar;
    ProgressDialog objProgressDialog;
    RelativeLayout objRelativeBeforeLoading;
    RelativeLayout objRelativeAfterLoading;
    ImageView objImageProgress;
    Button btnSkipAddingShouts;
    public static Button btnGiveThemShout;
    SearchView objEditTextSearchFilter;
    LinearLayout objLinearTopView;

    ArrayList<String> arrStrContactName;
    ArrayList<String> arrStrContactNumber;
    ContactExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    private ArrayList<Continent> continentList = new ArrayList<Continent>();
    ArrayList<ContactModel> NonFriendList = new ArrayList<ContactModel>();
    ArrayList<ContactModel> arrayListFriendList = new ArrayList<ContactModel>();


    SoftKeyboard softKeyboard;
    SharedPreferences objSharedPreferences;
    Boolean isNewUser = true;

    DatabaseHelper objDatabaseHelper;
    JSONArray objFriendsJson = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        objDatabaseHelper = new DatabaseHelper(InviteFriendsActivity.this);
        objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
        if (objSharedPreferences.getString(Constants.IS_NEW_USER, "").equals("true")) {
            isNewUser = true;
        } else {
            isNewUser = false;
        }
        init();
        setLocalData();
        try {
            objDatabaseHelper.deleteTable(DatabaseHelper.strTableNameShout);
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        getFacebookFriends();

        arrStrContactName = new ArrayList<String>();
        arrStrContactNumber = new ArrayList<String>();
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        RelativeLayout objRelativeLayoutRoot = (RelativeLayout) findViewById(R.id.relative_root_invite_friends);
        softKeyboard = new SoftKeyboard(objRelativeLayoutRoot, im);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {
                System.out.println("KEYBOARD CLOSE");
                InviteFriendsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            objLinearTopView.setVisibility(LinearLayout.VISIBLE);
                            btnGiveThemShout.setVisibility(Button.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onSoftKeyboardShow() {
                System.out.println("KEYBOARD OPEN");
                InviteFriendsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            objLinearTopView.setVisibility(LinearLayout.GONE);
                            btnGiveThemShout.setVisibility(Button.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void setLocalData() {
        try {
            objRelativeBeforeLoading.setVisibility(RelativeLayout.GONE);
            objRelativeAfterLoading.setVisibility(RelativeLayout.VISIBLE);
            objImageProgress.setVisibility(ImageView.GONE);
            arrayListFriendList.clear();
            continentList.clear();
            NonFriendList.clear();
            arrayListFriendList = objDatabaseHelper.getAllFriendsList();
            NonFriendList = objDatabaseHelper.getAllNonFriendsList();
            Continent continentFriends = new Continent("DISPLAYING USERS ON SHOUT ", arrayListFriendList);
            continentList.add(continentFriends);
            Continent continentNotFriends = new Continent("DISPLAYING USERS NOT ON SHOUT ", NonFriendList);
            continentList.add(continentNotFriends);
            listAdapter = new ContactExpandableListAdapter(InviteFriendsActivity.this,InviteFriendsActivity.this, continentList);
            expListView.setAdapter(listAdapter);
            expandAll();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFacebookFriends() {
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
                                    System.out.println("SURESH RESPONSE 1: " + response);
                                    System.out.println("SURESH RESPONSE 2: " + jsonArray);
                                    if (isNewUser) {
                                        objRelativeAfterLoading.setVisibility(RelativeLayout.GONE);
                                        objRelativeBeforeLoading.setVisibility(RelativeLayout.VISIBLE);
                                        objImageProgress.setVisibility(ImageView.VISIBLE);
                                        Animation sampleFadeAnimation = AnimationUtils.loadAnimation(InviteFriendsActivity.this, R.anim.rotating_progress);
                                        objImageProgress.startAnimation(sampleFadeAnimation);

                                        Handler objHandler = new Handler();
                                        objHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                               /* prasad
                                                new LoadingDataForFirstTime().execute(jsonArray.toString());*/
                                                setLocalData();
                                            }
                                        }, 3000);
                                    } else {
                                        if (ConnectivityBroadcastReceiver.isConnected()) {
                                            objRelativeBeforeLoading.setVisibility(RelativeLayout.GONE);
                                            objRelativeAfterLoading.setVisibility(RelativeLayout.VISIBLE);
                                            objImageProgress.setVisibility(ImageView.GONE);

                                            /*arrayListFriendList.clear();
                                            continentList.clear();
                                            NonFriendList.clear();
                                            arrayListFriendList = objDatabaseHelper.getAllFriendsList();
                                            System.out.println("DATA FROM DATABASE :" + arrayListFriendList);
                                            Continent continentFriends = new Continent("DISPLAYING USERS ON SHOUT ", arrayListFriendList);
                                            continentList.add(continentFriends);
                                            Continent continentNotFriends = new Continent("DISPLAYING USERS NOT ON SHOUT ", NonFriendList);
                                            continentList.add(continentNotFriends);
                                            listAdapter = new ContactExpandableListAdapter(InviteFriendsActivity.this, continentList);
                                            expListView.setAdapter(listAdapter);
                                            //expand all Groups
                                            expandAll();
                                            expListView.setGroupIndicator(null);
                                            expListView.setChildIndicator(null);
                                            expListView.setChildDivider(getResources().getDrawable(R.color.contact_list_divider_color));
                                            expListView.setDivider(getResources().getDrawable(R.color.transparent));*/

                                            new LoadingData().execute(jsonArray.toString());
                                        }
                                    }
                                    /*try {
                                        JSONObject jsonObject = response.getJSONObject();
                                        System.out.println("getFriendsData onCompleted : jsonObject " + jsonObject);
                                        JSONObject summary = jsonObject.getJSONObject("summary");
                                        System.out.println("getFriendsData onCompleted : summary total_count - " + summary.getString("total_count"));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }*/
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
        /*FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        AccessToken token = AccessToken.getCurrentAccessToken();*/
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        expListView = (ExpandableListView) findViewById(R.id.contact_list);
        objRelativeBeforeLoading = (RelativeLayout) findViewById(R.id.relative_request_contact_list);
        objRelativeAfterLoading = (RelativeLayout) findViewById(R.id.relative_updated_contact_list);
        objImageProgress = (ImageView) findViewById(R.id.loading_progress);
        btnSkipAddingShouts = (Button) findViewById(R.id.btn_skip_invite_friends);
        btnGiveThemShout = (Button) findViewById(R.id.btn_give_them_a_shout);
        objLinearTopView = (LinearLayout) findViewById(R.id.linear_invite_contact_screen_top_view);

        setListener();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        objEditTextSearchFilter = (SearchView) findViewById(R.id.edt_search_invite_friends);
        objEditTextSearchFilter.setQueryHint("Search Address Book");
        objEditTextSearchFilter.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        objEditTextSearchFilter.setIconifiedByDefault(false);
        objEditTextSearchFilter.setOnQueryTextListener(this);
        objEditTextSearchFilter.setOnCloseListener(this);

        LinearLayout ll = (LinearLayout) objEditTextSearchFilter.getChildAt(0);
        LinearLayout ll2 = (LinearLayout) ll.getChildAt(2);
        LinearLayout ll3 = (LinearLayout) ll2.getChildAt(1);
        SearchView.SearchAutoComplete autoComplete = (SearchView.SearchAutoComplete) ll3.getChildAt(0);
        autoComplete.setHintTextColor(Color.GRAY);
        autoComplete.setTextSize(13);
        autoComplete.setTextColor(Color.BLACK);
    }

    private void setListener() {
        btnSkipAddingShouts.setOnClickListener(this);
        btnGiveThemShout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_skip_invite_friends:
                Intent objIntent = new Intent(InviteFriendsActivity.this, ShoutDefaultActivity.class);
                startActivity(objIntent);
                finish();
                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                break;
            case R.id.btn_give_them_a_shout:
                if (btnGiveThemShout.getText().equals("Add to Shoutbook")) {
                    ArrayList<String> arrSelectedContactNumbers = new ArrayList<String>();
                    for (int index = 0; index < NonFriendList.size(); index++) {
                        ContactModel objContactModel = NonFriendList.get(index);
                        if (objContactModel.getCheckBokChecked()) {
                            arrSelectedContactNumbers.add(objContactModel.getContactNumber());
                        }
                    }
                    if (arrSelectedContactNumbers.size() > 0) {
                        String toNumbers = "";
                        for (String number : arrSelectedContactNumbers) {
                            toNumbers = toNumbers + number + ";";
                        }
                        // TO REMOVE LAST SEMI COLON FROM STRING
                        toNumbers = toNumbers.substring(0, toNumbers.length() - 1);
                        System.out.println("SELECTED CONTACTS : " + toNumbers);
                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                        smsIntent.setType("vnd.android-dir/mms-sms");
                        smsIntent.putExtra("address", toNumbers);
                        smsIntent.putExtra("sms_body", "Checkout shout application for your smartphone. Download it today from. http://shout.com");
                        smsIntent.putExtra(android.content.Intent.EXTRA_PHONE_NUMBER, toNumbers);
                        startActivity(smsIntent);
                    } else {
                        Toast.makeText(InviteFriendsActivity.this, "Please select at least one friend", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    objIntent = new Intent(InviteFriendsActivity.this, ShoutDefaultActivity.class);
                    startActivity(objIntent);
                    finish();
                    overridePendingTransition(0, 0);
                }
                break;
            default:
                break;
        }
    }

    public class LoadingDataForFirstTime extends AsyncTask<String, Void, String> {

        String strResult = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONObject objRootJsonObject = new JSONObject();
                JSONArray objContactJsonArray = new JSONArray();
                Hashtable<String, String> objPhoneDirectory = new Hashtable<String, String>();
                ContentResolver cr = InviteFriendsActivity.this.getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                int phoneType = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                                String phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                switch (phoneType) {
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                        Log.e(name + "(mobile number)", phoneNumber);
                                        arrStrContactName.add(name);
                                        arrStrContactNumber.add(phoneNumber);
                                        objPhoneDirectory.put(phoneNumber.replaceAll("[^+0-9]", ""), name);
                                        /*JSONObject objNewJsonObject = new JSONObject();
                                        objNewJsonObject.put("name", name);
                                        objNewJsonObject.put("phone", phoneNumber);
                                        objContactJsonArray.put(objNewJsonObject);*/
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                        Log.e(name + "(home number)", phoneNumber);
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                        Log.e(name + "(work number)", phoneNumber);
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                        Log.e(name + "(other number)", phoneNumber);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            pCur.close();
                        }
                    }
                }

                for (String key : objPhoneDirectory.keySet()) {
                    System.out.println("PHONE NUMBER : " + key + " NAME : " + objPhoneDirectory.get(key));
                    JSONObject objNewJsonObject = new JSONObject();
                    objNewJsonObject.put("name", objPhoneDirectory.get(key));
                    objNewJsonObject.put("phone", key);
                    objContactJsonArray.put(objNewJsonObject);
                }

                ArrayList<ContactModel> arrContactModel = new ArrayList<ContactModel>();

                arrContactModel = objDatabaseHelper.getAllNonFriendsList();
                for (int i=0;i<arrContactModel.size();i++){
                    ContactModel objContactModel = arrContactModel.get(i);
                    JSONObject objNewJsonObject = new JSONObject();
                    objNewJsonObject.put("name", objContactModel.getContactName());
                    objNewJsonObject.put("phone", objContactModel.getContactNumber());
                    objContactJsonArray.put(objNewJsonObject);
                }

                SharedPreferences objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
                objRootJsonObject.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));
                objRootJsonObject.put("contacts", objContactJsonArray);
                objRootJsonObject.put("facebook", params[0]);
                strResult = NetworkUtils.postData(Constants.FRIENDS_COMPARE_API, objRootJsonObject.toString());
                return strResult;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return strResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Utils.d("CONTACT API RESPONSE : ", s);
            objRelativeBeforeLoading.setVisibility(RelativeLayout.GONE);
            objRelativeAfterLoading.setVisibility(RelativeLayout.VISIBLE);

            System.out.println("ARRAY NAME:" + arrStrContactName);
            System.out.println("ARRAY NUMBER:" + arrStrContactNumber);

            try {
                JSONObject objJsonObject = new JSONObject(s);

                if (objJsonObject.getString("result").equals("true")) {

                    enableSearchView(objEditTextSearchFilter, true);

                    JSONArray objJsonArray = new JSONArray(objJsonObject.getString("friends"));

                    ArrayList<ContactModel> FriendList = new ArrayList<ContactModel>();
                    NonFriendList.clear();

                    /*for (int index = 0; index < objJsonArray.length(); index++) {
                        if (objJsonArray.getJSONObject(index).getString("is_friend").equals("Y")) {
                            if (objJsonArray.getJSONObject(index).getString("phone").length() >= 10) {
                                ContactModel objContactModel = new ContactModel(
                                        objJsonArray.getJSONObject(index).getString("name"),
                                        objJsonArray.getJSONObject(index).getString("phone"),
                                        objJsonArray.getJSONObject(index).getString("id"),
                                        objJsonArray.getJSONObject(index).getString("is_facebook_friend"),
                                        objJsonArray.getJSONObject(index).getString("is_phone_friend"),
                                        false, 0);
                                FriendList.add(objContactModel);
                            }
                        } else if (objJsonArray.getJSONObject(index).getString("is_friend").equals("N")) {
                            if (objJsonArray.getJSONObject(index).getString("phone").length() >= 10) {
                                ContactModel objContactModel = new ContactModel(
                                        objJsonArray.getJSONObject(index).getString("name"),
                                        objJsonArray.getJSONObject(index).getString("phone"),
                                        objJsonArray.getJSONObject(index).getString("id"),
                                        objJsonArray.getJSONObject(index).getString("is_facebook_friend"),
                                        objJsonArray.getJSONObject(index).getString("is_phone_friend"),
                                        false, 1);
                                NonFriendList.add(objContactModel);
                            }
                        }
                    }

                    Continent continentFriends = new Continent("DISPLAYING USERS ON SHOUT ", FriendList);
                    continentList.add(continentFriends);
                    Continent continentNotFriends = new Continent("DISPLAYING USERS NOT ON SHOUT ", NonFriendList);
                    continentList.add(continentNotFriends);
                    listAdapter = new ContactExpandableListAdapter(InviteFriendsActivity.this, continentList);
                    expListView.setAdapter(listAdapter);
                    //expand all Groups
                    expandAll();
                    expListView.setGroupIndicator(null);
                    expListView.setChildIndicator(null);
                    expListView.setChildDivider(getResources().getDrawable(R.color.contact_list_divider_color));
                    expListView.setDivider(getResources().getDrawable(R.color.transparent));*/


                    // SAVING CONTACT JSON FOR FIRST TIME WHILE COMING FROM LOGIN FLOW
                    objDatabaseHelper.saveFriends(objJsonArray);

                    arrayListFriendList.clear();
                    continentList.clear();
                    NonFriendList.clear();

                    arrayListFriendList = objDatabaseHelper.getAllFriendsList();
                    NonFriendList = objDatabaseHelper.getAllNonFriendsList();

                    /*for (int index = 0; index < objJsonArray.length(); index++) {
                        if (objJsonArray.getJSONObject(index).getString("is_friend").equals("N")) {
                            if (objJsonArray.getJSONObject(index).getString("phone").length() >= 10) {
                                ContactModel objContactModel = new ContactModel(
                                        objJsonArray.getJSONObject(index).getString("name"),
                                        objJsonArray.getJSONObject(index).getString("phone"),
                                        objJsonArray.getJSONObject(index).getString("id"),
                                        objJsonArray.getJSONObject(index).getString("is_facebook_friend"),
                                        objJsonArray.getJSONObject(index).getString("is_phone_friend"),
                                        false, "", 1);
                                NonFriendList.add(objContactModel);
                            }
                        }
                    }*/

                    Continent continentFriends = new Continent("DISPLAYING USERS ON SHOUT ", arrayListFriendList);
                    continentList.add(continentFriends);
                    Continent continentNotFriends = new Continent("DISPLAYING USERS NOT ON SHOUT ", NonFriendList);
                    continentList.add(continentNotFriends);
                    listAdapter = new ContactExpandableListAdapter(InviteFriendsActivity.this,InviteFriendsActivity.this, continentList);
                    expListView.setAdapter(listAdapter);
                    //expand all Groups
                    expandAll();
                    expListView.setGroupIndicator(null);
                    expListView.setChildIndicator(null);
                    expListView.setChildDivider(getResources().getDrawable(R.color.contact_list_divider_color));
                    expListView.setDivider(getResources().getDrawable(R.color.transparent));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showProgressBar(boolean show) {
        if (show)
            progressBar.setVisibility(ProgressBar.VISIBLE);
        else
            progressBar.setVisibility(ProgressBar.GONE);
    }

    public class LoadingData extends AsyncTask<String, Void, String> {

        String strResult = "";
//        final ProgressDialog objProgressDialog = new ProgressDialog(InviteFriendsActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar(true);
/*            objProgressDialog.setMessage("Loading...");
            objProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                JSONObject objRootJsonObject = new JSONObject();
                JSONArray objContactJsonArray = new JSONArray();

                Hashtable<String, String> objPhoneDirectory = new Hashtable<String, String>();

                ContentResolver cr = InviteFriendsActivity.this.getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                int phoneType = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                                String phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                switch (phoneType) {
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                        Log.e(name + "(mobile number)", phoneNumber);
                                        arrStrContactName.add(name);
                                        arrStrContactNumber.add(phoneNumber);
                                        /*JSONObject objNewJsonObject = new JSONObject();
                                        objNewJsonObject.put("name", name);
                                        objNewJsonObject.put("phone", phoneNumber);
                                        objContactJsonArray.put(objNewJsonObject);*/
                                        objPhoneDirectory.put(phoneNumber.replaceAll("[^+0-9]", ""), name);
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                        Log.e(name + "(home number)", phoneNumber);
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                        Log.e(name + "(work number)", phoneNumber);
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                        Log.e(name + "(other number)", phoneNumber);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            pCur.close();
                        }
                    }
                }

                for (String key : objPhoneDirectory.keySet()) {
                    System.out.println("PHONE NUMBER : " + key + " NAME : " + objPhoneDirectory.get(key));
                    JSONObject objNewJsonObject = new JSONObject();
                    objNewJsonObject.put("name", objPhoneDirectory.get(key));
                    objNewJsonObject.put("phone", key);
                    objContactJsonArray.put(objNewJsonObject);
                }

                SharedPreferences objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
                objRootJsonObject.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));
                objRootJsonObject.put("contacts", objContactJsonArray);
                objRootJsonObject.put("facebook", params[0]);

                Utils.d("CONTACT API INPUT : ", objRootJsonObject.toString());

                strResult = NetworkUtils.postData(Constants.FRIENDS_COMPARE_API, objRootJsonObject.toString());
                return strResult;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return strResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            showProgressBar(false);
            System.out.println("ARRAY NAME:" + arrStrContactName);
            System.out.println("ARRAY NUMBER:" + arrStrContactNumber);

            try {

               /* if (objProgressDialog.isShowing()) {
                    objProgressDialog.dismiss();
                }*/

                JSONObject objJsonObject = new JSONObject(s);

                if (objJsonObject.getString("result").equals("true")) {

                    enableSearchView(objEditTextSearchFilter, true);

                    JSONArray objJsonArray = new JSONArray(objJsonObject.getString("friends"));

                    // ArrayList<ContactModel> FriendList = new ArrayList<ContactModel>();

                    arrayListFriendList.clear();
                    continentList.clear();
                    NonFriendList.clear();
                    arrayListFriendList.clear();
                   /* for (int index = 0; index < objJsonArray.length(); index++) {
                        if (objJsonArray.getJSONObject(index).getString("is_friend").equals("N")) {
                            if (objJsonArray.getJSONObject(index).getString("phone").length() >= 10) {
                                ContactModel objContactModel = new ContactModel(
                                        objJsonArray.getJSONObject(index).getString("name"),
                                        objJsonArray.getJSONObject(index).getString("phone"),
                                        objJsonArray.getJSONObject(index).getString("id"),
                                        objJsonArray.getJSONObject(index).getString("is_facebook_friend"),
                                        objJsonArray.getJSONObject(index).getString("is_phone_friend"),
                                        false, "", 1);
                                NonFriendList.add(objContactModel);
                            }
                        }
                    }*/
                    // TODO: 9/2/2016 add Friends to the database for performance loading

                    objDatabaseHelper.saveFriends(objJsonArray);
                    arrayListFriendList = objDatabaseHelper.getAllFriendsList();
                    NonFriendList = objDatabaseHelper.getAllNonFriendsList();

                    Continent continentFriends = new Continent("DISPLAYING USERS ON SHOUT ", arrayListFriendList);
                    continentList.add(continentFriends);
                    Continent continentNotFriends = new Continent("DISPLAYING USERS NOT ON SHOUT ", NonFriendList);
                    continentList.add(continentNotFriends);
                    listAdapter = new ContactExpandableListAdapter(InviteFriendsActivity.this,InviteFriendsActivity.this, continentList);
                    expListView.setAdapter(listAdapter);
                    //expand all Groups
                    expandAll();
                    expListView.setGroupIndicator(null);
                    expListView.setChildIndicator(null);
                    expListView.setChildDivider(getResources().getDrawable(R.color.contact_list_divider_color));
                    expListView.setDivider(getResources().getDrawable(R.color.transparent));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expListView.expandGroup(i);
        }
    }

    @Override
    public boolean onClose() {
        System.out.println("SEARCH CLOSE");
//        objLinearTopView.setVisibility(LinearLayout.VISIBLE);
        return false;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
//        objLinearTopView.setVisibility(LinearLayout.GONE);
        if (arrayListFriendList.size() > 0 && NonFriendList.size() > 0) {
            listAdapter.filterData(query);
            expandAll();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
//        objLinearTopView.setVisibility(LinearLayout.GONE);
        if (arrayListFriendList.size() > 0 && NonFriendList.size() > 0) {
            listAdapter.filterData(query);
            expandAll();
        }
        return false;
    }

    // TODO: 9/29/2016 BELOW METHOD USED FOR ENABLE AND DISABLE THE SEARCH VIEW. USED WHEN THERE IS NO DATA IN BELOW LIST AT THAT TIME DISABLE SEARCH VIEW ELSE ENABLE IT.
    private void enableSearchView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                enableSearchView(child, enabled);
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(InviteFriendsActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("")
                .setMessage("Do you wish to exit the Shout App ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}
