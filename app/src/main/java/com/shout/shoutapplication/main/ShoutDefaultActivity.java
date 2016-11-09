package com.shout.shoutapplication.main;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Process;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.PlusShare;
import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.ConnectivityBroadcastReceiver;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.KeyboardUtils;
import com.shout.shoutapplication.Utils.NetworkUtils;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.app.AppController;
import com.shout.shoutapplication.base.BaseActivity;
import com.shout.shoutapplication.database.DatabaseHelper;
import com.shout.shoutapplication.main.Adapter.PopupComunityLayoutAdapter;
import com.shout.shoutapplication.main.Adapter.SearchViewPagerAdapter;
import com.shout.shoutapplication.main.Adapter.ShoutDefaultListAdapter;
import com.shout.shoutapplication.main.Model.MyPreferencesModel;
import com.shout.shoutapplication.main.Model.SearchViewPagerModel;
import com.shout.shoutapplication.main.Model.ShoutDefaultListModel;
import com.shout.shoutapplication.others.SoftKeyboard;
import com.shout.shoutapplication.service.ShoutBoardBackgroundService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by CapternalSystems on 7/5/2016.
 */
public class ShoutDefaultActivity extends BaseActivity implements View.OnClickListener, ConnectivityBroadcastReceiver.ConnectivityReceiverListener {

    private static final int MAX_ROWS = 50;
    private static final int STATE_OFFSCREEN = 1;
    private static final int STATE_ONSCREEN = 0;
    private static final int STATE_RETURNING = 2;
    public static Boolean isListViewIdle;
    public static ShoutDefaultListAdapter objShoutDefaultListAdapter;
    public static int VIEW_PAGER_DEFAULT_POSITION = 1;
    public static Button btnFilter;
    public static EditText objEditTextSearch;
    public static RelativeLayout objRelativeLayoutSearchBox;
    public static boolean keyBoardOpen;
    public static int inSearchMode = 0;
    public SwipeRefreshLayout objShoutDefaultSwipableLayout;
    ArrayList<ShoutDefaultListModel> arrShoutDefaultListModel;
    Button btnSearch;
    Button btnSearchBoxCancelDone;
    ImageButton objImageButtonToggle;
    int intToggleFlag = 0;
    ImageView objImageViewCancelDone;
    RelativeLayout objRelativeLayoutHeader;
    SharedPreferences objSharedPreferences;
    SoftKeyboard softKeyboard;
    RelativeLayout objRootLayout;
    int intOffset = 0;
    int intLimit = 10;
    RelativeLayout objRelativeToast;
    View objViewFilterApply;
    LinearLayout objLinearLayoutAskShoutType;
    RelativeLayout objRelativeLayoutNeedHelp;
    RelativeLayout objRelativeLayoutWantToHelp;

    // KEYBOARD VARIABLES
    ImageButton objImageButtonNeedHelp;
    TextView objTextViewNeedHelp;
    ImageButton objImageButtonWantToHelp;
    TextView objTextViewWantToHelp;
    static LinearLayout objLinearBottomLoad;
    DatabaseHelper objDatabaseHelper;
    boolean isLoading = false;
    Parcelable state;
    // BROADCAST RECEIVER FOR UPDATING SHOUTS FROM LIVE
    ShoutUpdateBroadcastReceiver objShoutUpdateBroadcastReceiver;
    IntentFilter shoutUpdateFilter;
    private TranslateAnimation anim;
    private int lastTopValue;
    private int mCachedVerticalScrollRange;
    private View mHeader;
    private int mMinRawY;
    private View mPlaceHolder;
    private int mQuickReturnHeight;
    private RelativeLayout mQuickReturnView;
    private int mScrollY;
    private int mState;
    private ListView objListViewShoutList;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    // VIEW PAGER SEARCHING COMPONENTS
    private ViewPager searchViewPager;
    private RelativeLayout linearLayoutViewPager;
    private SearchViewPagerAdapter searchViewPagerAdapter;
    private ArrayList<SearchViewPagerModel> arrayListSearchViewPagerModel = new ArrayList<SearchViewPagerModel>();
    private ShoutDefaultListModel objShoutDefaultListModel;


    // // TODO: 30/09/16 COMMUNITY WHEEL POPUP WINDOW

    private RelativeLayout relativeLayout;
    private PopupWindow popupWindowComunity;
    private PopupComunityLayoutAdapter popupComunityLayoutAdapter;
    private Activity activity;
    private ListView listViewComunityPopup;
    private View viewComunityPopup;
    public boolean isPopupOpen = false;
    private LinearLayout objLinearCommunityPopupRoot;
    public ArrayList<MyPreferencesModel> arrMyPreferencesModel = new ArrayList<MyPreferencesModel>();
    private String strPreferenceId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shout_default);

        activity = ShoutDefaultActivity.this;

        /*try{
            ContactsObserver objContactsObserver = new ContactsObserver(getApplicationContext());
            objContactsObserver.addContactsChangeListener(this);
        }catch(NullPointerException ne){
            ne.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }*/

        try {
            shoutUpdateFilter = new IntentFilter(Constants.SHOUT_UPDATE_INTENT);
            objShoutUpdateBroadcastReceiver = new ShoutUpdateBroadcastReceiver();
            registerReceiver(objShoutUpdateBroadcastReceiver, shoutUpdateFilter);
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        objDatabaseHelper = new DatabaseHelper(ShoutDefaultActivity.this);

        updateNotificationCount(getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE).getString(Constants.USER_NOTIFICATION_COUNT, ""));

        objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, STATE_ONSCREEN);
        SharedPreferences.Editor objEditor = objSharedPreferences.edit();
        objEditor.putString(Constants.IS_NEW_USER, "false");
        objEditor.commit();

        // FOR FIRST TIME WHEN USER GETS FRESH LOGIN TO THE APP
        if (objSharedPreferences.getString(Constants.IS_CURRENT_DATE, "").equals("")) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat(Constants.SHOUT_LIST_DATE_FORMAT);
            String currentDate = df.format(c.getTime());
            objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, STATE_ONSCREEN);
            SharedPreferences.Editor objDateEditor = objSharedPreferences.edit();
            objDateEditor.putString(Constants.IS_CURRENT_DATE, currentDate);
            objDateEditor.commit();
        }
        arrShoutDefaultListModel = new ArrayList<ShoutDefaultListModel>();

        initialize();

        try {
            arrMyPreferencesModel = new ArrayList<MyPreferencesModel>();
            setDefaultItems();
            String tag_json_obj = "json_obj_req";
            SharedPreferences objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.MY_PREFRENCES_API, new JSONObject().put("user_id", objSharedPreferences.getString(Constants.USER_ID, "")), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("API RESPONSE : " + response.toString());
                    try {
                        JSONObject objJsonObject = new JSONObject(response.toString());

                        if (objJsonObject.getString("result").equals("true")) {
                            JSONArray objJsonArray = new JSONArray(objJsonObject.getString("preferences"));

                            for (int index = 0; index < objJsonArray.length(); index++) {
                                if (objJsonArray.getJSONObject(index).getString("status").equals("A")) {
                                    MyPreferencesModel objMyPreferencesModel = new MyPreferencesModel(
                                            objJsonArray.getJSONObject(index).getString("id"),
                                            objJsonArray.getJSONObject(index).getString("preference_id"),
                                            objJsonArray.getJSONObject(index).getString("title"),
                                            objJsonArray.getJSONObject(index).getString("status"),
                                            true);
                                    arrMyPreferencesModel.add(objMyPreferencesModel);
                                }
                            }
                            initViewPagerComponents();
                        }
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("ERROR : " + error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Accept", "application/json");
                    return headers;
                }
            };
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                Log.d("keyboard", "keyboard visible: " + isVisible);
                if (isVisible) {
                    keyBoardOpen = true;
                } else {
                    keyBoardOpen = false;
                    /*openCloseSearchBar(false, ShoutDefaultActivity.this);
                    objEditTextSearch.setText("");*/
                }
            }
        });
        if (objSharedPreferences.getString(Constants.PROFILE_BACK_SCREEN_NAME, "").equals(Constants.CALL_FROM_MY_SHOUTS)) {
            SharedPreferences.Editor objProfileEditor = objSharedPreferences.edit();
            objProfileEditor.putString(Constants.PROFILE_BACK_SCREEN_NAME, "");
            objProfileEditor.commit();
            new GetLoggedInUserShout().execute();
        } else {
            System.out.println("STORED DATE : " + objSharedPreferences.getString(Constants.IS_CURRENT_DATE, ""));
            System.out.println("CURRENT DATE : " + new SimpleDateFormat(Constants.SHOUT_LIST_DATE_FORMAT).format(Calendar.getInstance().getTime()));

            if (objSharedPreferences.getString(Constants.IS_CURRENT_DATE, "").equals(new SimpleDateFormat(Constants.SHOUT_LIST_DATE_FORMAT).format(Calendar.getInstance().getTime()))) {
                System.out.println("LOADING SHOUTS FOR CURRENT DATE : " + new SimpleDateFormat(Constants.SHOUT_LIST_DATE_FORMAT).format(Calendar.getInstance().getTime()));
                if (ConnectivityBroadcastReceiver.isConnected()) {
                    arrShoutDefaultListModel.clear();
                    arrShoutDefaultListModel = objDatabaseHelper.getShoutDefaultListModelArray("0");
                    if (arrShoutDefaultListModel.size() == 0) {
                        new StoreShoutDataForFirstTime("0").execute();
                    } else {
                        objShoutDefaultListAdapter = new ShoutDefaultListAdapter(arrShoutDefaultListModel, ShoutDefaultActivity.this, ShoutDefaultActivity.this);
                        objListViewShoutList.setAdapter(objShoutDefaultListAdapter);
                    }
                } else {
                    arrShoutDefaultListModel.clear();
                    arrShoutDefaultListModel = objDatabaseHelper.getShoutDefaultListModelArray("0");
                    System.out.println("OFFLINE DATA : " + arrShoutDefaultListModel);
                    if (arrShoutDefaultListModel.size() > 0) {
                        objShoutDefaultListAdapter = new ShoutDefaultListAdapter(arrShoutDefaultListModel, ShoutDefaultActivity.this, ShoutDefaultActivity.this);
                        objListViewShoutList.setAdapter(objShoutDefaultListAdapter);
                    }
                }
            } else {

                objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, STATE_ONSCREEN);
                SharedPreferences.Editor objDateEditor = objSharedPreferences.edit();
                objDateEditor.putString(Constants.IS_CURRENT_DATE, new SimpleDateFormat(Constants.SHOUT_LIST_DATE_FORMAT).format(Calendar.getInstance().getTime()));
                objDateEditor.commit();

                arrShoutDefaultListModel.clear();
                arrShoutDefaultListModel = objDatabaseHelper.getShoutDefaultListModelArray("0");
                System.out.println("OFFLINE DATA : " + arrShoutDefaultListModel);
                if (arrShoutDefaultListModel.size() > 0) {
                    objShoutDefaultListAdapter = new ShoutDefaultListAdapter(arrShoutDefaultListModel, ShoutDefaultActivity.this, ShoutDefaultActivity.this);
                    objListViewShoutList.setAdapter(objShoutDefaultListAdapter);
                } else {
                    new StoreShoutDataForFirstTime("0").execute();
                }
            }
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public static void openCloseSearchBar(boolean status, Context context) {
        if (status == true) {
            inSearchMode = 1;
            btnFilter.setVisibility(Button.INVISIBLE);
            btnFilter.setEnabled(false);
            objRelativeLayoutSearchBox.setVisibility(RelativeLayout.VISIBLE);
            Animation openAnimation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
            objRelativeLayoutSearchBox.setAnimation(openAnimation);
            objRelativeLayoutSearchBox.animate();
        } else {
            inSearchMode = 0;
            Animation closeAnimation = AnimationUtils.loadAnimation(context, R.anim.push_right_out);
            objRelativeLayoutSearchBox.setAnimation(closeAnimation);
            objRelativeLayoutSearchBox.animate();
            objRelativeLayoutSearchBox.setVisibility(RelativeLayout.GONE);
            btnFilter.setVisibility(Button.VISIBLE);
            btnFilter.setEnabled(true);

            BaseActivity.objImageButtonCreateShout.setVisibility(ImageButton.VISIBLE);
            objLinearBottomLoad.setVisibility(LinearLayout.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            AppController.getInstance().setConnectivityListener(this);
            // BROADCAST RECEIVER FOR UPDATING LOCAL SHOUTS
            objShoutUpdateBroadcastReceiver = new ShoutUpdateBroadcastReceiver();
            registerReceiver(objShoutUpdateBroadcastReceiver, shoutUpdateFilter);

            Calendar calendar = Calendar.getInstance();
            Intent objShoutBoardService = new Intent(ShoutDefaultActivity.this, ShoutBoardBackgroundService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, objShoutBoardService, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 1000, pendingIntent);

            startService(new Intent(ShoutDefaultActivity.this, ShoutBoardBackgroundService.class));

            init();

            objLinearLayoutAskShoutType.setVisibility(LinearLayout.GONE);
            objDatabaseHelper = new DatabaseHelper(ShoutDefaultActivity.this);
            arrShoutDefaultListModel.clear();
            arrShoutDefaultListModel = objDatabaseHelper.getShoutDefaultListModelArray("0");
            objListViewShoutList.setAdapter(new ShoutDefaultListAdapter(arrShoutDefaultListModel, ShoutDefaultActivity.this, ShoutDefaultActivity.this));
            if (state != null) {
                objListViewShoutList.onRestoreInstanceState(state);
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        state = objListViewShoutList.onSaveInstanceState();
        Utils.d("SERVICE :", "STOPPED");
        stopService(new Intent(ShoutDefaultActivity.this, ShoutBoardBackgroundService.class));
        unregisterReceiver(objShoutUpdateBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initViewPagerComponents() {
        linearLayoutViewPager = (RelativeLayout) findViewById(R.id.linearLayoutViewPager);
        searchViewPager = (ViewPager) findViewById(R.id.searchViewPager);
        searchViewPager.setOnClickListener(this);
        final GestureDetector objGestureDetector = new GestureDetector(this, new TapGestureListener());
        searchViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                objGestureDetector.onTouchEvent(event);
                return false;
            }
        });

        searchViewPagerAdapter = new SearchViewPagerAdapter(ShoutDefaultActivity.this, arrMyPreferencesModel);
        searchViewPager.setAdapter(searchViewPagerAdapter);
//        searchViewPager.setPageMargin(-50);
        searchViewPager.setClipToPadding(false);
        searchViewPager.setPadding(70, 0, 70, 0);
    }

    private void initialize() {
        objLinearBottomLoad = (LinearLayout) findViewById(R.id.linear_list_down_swipe_loading);
        objLinearLayoutAskShoutType = (LinearLayout) findViewById(R.id.linear_ask_shout_type);
        objRelativeLayoutNeedHelp = (RelativeLayout) findViewById(R.id.relative_need_help);
        objRelativeLayoutWantToHelp = (RelativeLayout) findViewById(R.id.relative_want_to_help);
        objImageButtonNeedHelp = (ImageButton) findViewById(R.id.image_button_need_help);
        objTextViewNeedHelp = (TextView) findViewById(R.id.txt_need_help);
        objImageButtonWantToHelp = (ImageButton) findViewById(R.id.image_button_want_to_help);
        objTextViewWantToHelp = (TextView) findViewById(R.id.txt_want_to_help);
        objViewFilterApply = (View) findViewById(R.id.filter_applyied_view);
        objRelativeToast = (RelativeLayout) findViewById(R.id.relative_toast);
        objShoutDefaultSwipableLayout = (SwipeRefreshLayout) findViewById(R.id.shout_default_swipe_layout);
        objShoutDefaultSwipableLayout.setEnabled(false);
        objRelativeLayoutHeader = (RelativeLayout) findViewById(R.id.relative_grey_background_menu);
        btnSearch = (Button) findViewById(R.id.btn_shout_default_search);
        btnFilter = (Button) findViewById(R.id.btn_shout_default_filter);
        objEditTextSearch = (EditText) findViewById(R.id.edt_search_shout_default_header);
        objImageViewCancelDone = (ImageView) findViewById(R.id.image_search_cancel_done_shout_default_header);
        btnSearchBoxCancelDone = (Button) findViewById(R.id.btn_cancel_done_shout_default_header);
        objRelativeLayoutSearchBox = (RelativeLayout) findViewById(R.id.relative_search_box_shout_default_header);
        objImageButtonToggle = (ImageButton) findViewById(R.id.image_button_toggle_icon_shout_default);

        objImageButtonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intToggleFlag == 0) {
                    objImageButtonToggle.setBackgroundResource(R.drawable.toggle_icon_green);
                    intToggleFlag = 1;
                } else {
                    objImageButtonToggle.setBackgroundResource(R.drawable.toggle_icon_red);
                    intToggleFlag = 0;
                }
            }
        });

        showDefaultTopHeader();
        showBottomTabs();

        BaseActivity.objImageNotificationCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(ShoutDefaultActivity.this, NotificationListActivity.class);
                startActivity(objIntent);
            }
        });

        BaseActivity.objImageButtonCreateShout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
                final Animation rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

                if (objLinearLayoutAskShoutType.getVisibility() == LinearLayout.GONE) {
                    objLinearLayoutAskShoutType.setVisibility(LinearLayout.VISIBLE);
                    objShoutDefaultSwipableLayout.setEnabled(false);
                    // HIDING + BUTTON
                    BaseActivity.objImageButtonCreateShout.startAnimation(rotate_forward);
                    Animation ComeFromLeftAnim = AnimationUtils.loadAnimation(ShoutDefaultActivity.this, R.anim.come_from_right);
                    objRelativeLayoutNeedHelp.startAnimation(ComeFromLeftAnim);
                    Animation ComeFromRightAnim = AnimationUtils.loadAnimation(ShoutDefaultActivity.this, R.anim.come_from_left);
                    objRelativeLayoutWantToHelp.startAnimation(ComeFromRightAnim);

                    objRelativeLayoutNeedHelp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //DO NOTHING
                            System.out.println("DO NOTHING");
                            BaseActivity.objImageButtonCreateShout.startAnimation(rotate_backward);
                            pushToCreateShoutScreen("N", true);
                        }
                    });

                    objRelativeLayoutWantToHelp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //DO NOTHING
                            System.out.println("DO NOTHING");
                            BaseActivity.objImageButtonCreateShout.startAnimation(rotate_backward);
                            pushToCreateShoutScreen("W", false);
                        }
                    });
                    objImageButtonNeedHelp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("NEED HELP CLICKED");
                            BaseActivity.objImageButtonCreateShout.startAnimation(rotate_backward);
                            pushToCreateShoutScreen("N", true);
                        }
                    });

                    objImageButtonWantToHelp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("WANT TO GIVE CLICKED");
                            BaseActivity.objImageButtonCreateShout.startAnimation(rotate_backward);
                            pushToCreateShoutScreen("W", false);
                        }
                    });
                } else {
                    BaseActivity.objImageButtonCreateShout.startAnimation(rotate_backward);
                    Animation outAnimationForNeedHelp = AnimationUtils.loadAnimation(ShoutDefaultActivity.this, R.anim.push_left_out);
                    objRelativeLayoutNeedHelp.startAnimation(outAnimationForNeedHelp);
                    Animation outAnimationForWantToHelp = AnimationUtils.loadAnimation(ShoutDefaultActivity.this, R.anim.push_right_out);
                    objRelativeLayoutWantToHelp.startAnimation(outAnimationForWantToHelp);

                    outAnimationForNeedHelp.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            objLinearLayoutAskShoutType.setVisibility(LinearLayout.GONE);
                            BaseActivity.objImageButtonCreateShout.setVisibility(ImageButton.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }
        });
        objListViewShoutList = (ListView) findViewById(R.id.shout_default_listview);
        objListViewShoutList.setFastScrollEnabled(true);
        mQuickReturnView = (RelativeLayout) findViewById(R.id.relative_grey_background_menu);

        setListener();

        objListViewShoutList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (objRelativeLayoutSearchBox.getVisibility() == RelativeLayout.VISIBLE) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                return false;
            }
        });

        objListViewShoutList.setOnScrollListener(new AbsListView.OnScrollListener() {
                                                     @Override
                                                     public void onScrollStateChanged(AbsListView view, int scrollState) {

                                                     }

                                                     @Override
                                                     public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                                         if (objListViewShoutList.getChildCount() > 0) {

                                                             boolean topOfFirstItemVisible = objListViewShoutList.getChildAt(0).getTop() == 0;
                                                             if (firstVisibleItem == 0 && topOfFirstItemVisible) {
                                                                 objShoutDefaultSwipableLayout.setEnabled(true);
                                                             } else {
                                                                 objShoutDefaultSwipableLayout.setEnabled(false);
                                                             }
                                                         }
                                                         try {
                                                             if (objListViewShoutList.getAdapter() == null)
                                                                 return;

                                                             if (objListViewShoutList.getAdapter().getCount() == 0)
                                                                 return;

                                                             int l = visibleItemCount + firstVisibleItem;
                                                             if (l >= totalItemCount && !isLoading) {
                                                                 isLoading = true;
                                                                 System.out.println("LAST ITEM CELL DETECTED");

                                                                 Animation fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
                                                                 BaseActivity.objImageButtonCreateShout.startAnimation(fab_close);
                                                                 objLinearBottomLoad.setVisibility(LinearLayout.VISIBLE);
                                                                 Constants.show(objLinearBottomLoad);

                                                                 if (inSearchMode == 0) {
                                                                     int offset = objDatabaseHelper.getNearByFriendsShoutCount();
                                                                     System.out.println("SHOUT NEAR BY QUERY RESULT : " + offset);
                                                                     if (offset == 0) {
                                                                         System.out.println("AA : LOAD MORE SHOUTS");
                                                                         new LoadMoreShouts().execute();
                                                                     } else {
                                                                         System.out.println("AA : LOAD NEAR BY SHOUTS");
                                                                         new LoadNearByShouts().execute();
                                                                     }
                                                                 }
                                                                 /*else {
                                                                     isLoading = false;
                                                                     Animation fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
                                                                     BaseActivity.objImageButtonCreateShout.startAnimation(fab_close);
                                                                     objLinearBottomLoad.setVisibility(LinearLayout.VISIBLE);
                                                                     Constants.show(objLinearBottomLoad);
                                                                 }*/
                                                             }
                                                         } catch (NullPointerException ne) {
                                                             ne.printStackTrace();
                                                         } catch (Exception e) {
                                                             e.printStackTrace();
                                                         }
                                                     }
                                                 }

        );
        objShoutDefaultSwipableLayout.setColorSchemeResources(R.color.red_background_color);
//        objShoutDefaultSwipableLayout.setY(100);
        objShoutDefaultSwipableLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                               @Override
                                                               public void onRefresh() {
                                                                   state=objListViewShoutList.onSaveInstanceState();
                                                                   objDatabaseHelper.deleteUnFriendShouts();
                                                                   arrShoutDefaultListModel.clear();
                                                                   arrShoutDefaultListModel = objDatabaseHelper.getShoutDefaultListModelArray("0");
                                                                   objShoutDefaultListAdapter = new ShoutDefaultListAdapter(arrShoutDefaultListModel, ShoutDefaultActivity.this, ShoutDefaultActivity.this);
                                                                   objListViewShoutList.setAdapter(objShoutDefaultListAdapter);
                                                                   objShoutDefaultListAdapter.notifyDataSetChanged();
                                                                   objListViewShoutList.onRestoreInstanceState(state);
                                                                   new StoreShoutDataForFirstTime("1").execute();
                                                                   objShoutDefaultSwipableLayout.setRefreshing(true);
                                                               }
                                                           }
        );

        objEditTextSearch.addTextChangedListener(new TextWatcher() {
                                                     @Override
                                                     public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                     }

                                                     @Override
                                                     public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                     }

                                                     @Override
                                                     public void afterTextChanged(Editable s) {
                                                         if (s.length() > 0) {
                                                             String strInput = objEditTextSearch.getText().toString().toLowerCase(Locale.getDefault());
                                                             objShoutDefaultListAdapter.getFilter().filter(s.toString());
                                                         } else {
                                                             objShoutDefaultListAdapter = new ShoutDefaultListAdapter(arrShoutDefaultListModel, ShoutDefaultActivity.this, ShoutDefaultActivity.this);
                                                             objListViewShoutList.setAdapter(objShoutDefaultListAdapter);
                                                         }
                                                     }
                                                 }
        );
    }

    private void pushToCreateShoutScreen(final String identifier, boolean isRequestShown) {
        Intent objIntent = (new Intent(ShoutDefaultActivity.this, CreateShoutActivity.class));
        objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        objIntent.putExtra("IS_REQUEST", identifier);
        objIntent.putExtra("IS_REQUEST_SHOWN", isRequestShown);
        startActivity(objIntent);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    private void setListener() {
        btnSearch.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
        btnSearchBoxCancelDone.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        System.out.println("CONNECTION STATUS : " + isConnected);
    }

    public void onBackPressed() {
        System.out.println("IN BACK PRESS OF SHOUT DEFAULT ACTIVITY");
       /* try {
            if (objPopupWindowLargeSource.isShowing()) {
                System.out.println("IN BACK PRESS OF SHOUT DEFAULT ACTIVITY");
                objPopupWindowLargeSource.dismiss();
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        if (isPopupOpen) {
            popupWindowComunity.dismiss();
        } else if (objRelativeLayoutSearchBox.getVisibility() == RelativeLayout.VISIBLE) {
            openCloseSearchBar(false, ShoutDefaultActivity.this);
            objEditTextSearch.setText("");
        } else if (keyBoardOpen) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(objEditTextSearch.getApplicationWindowToken(), InputMethodManager.RESULT_HIDDEN);
        } else if (objLinearLayoutAskShoutType.getVisibility() == LinearLayout.VISIBLE) {

            Animation rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
            BaseActivity.objImageButtonCreateShout.startAnimation(rotate_backward);

            Animation outAnimationForNeedHelp = AnimationUtils.loadAnimation(ShoutDefaultActivity.this, R.anim.push_left_out);
            objRelativeLayoutNeedHelp.startAnimation(outAnimationForNeedHelp);
            Animation outAnimationForWantToHelp = AnimationUtils.loadAnimation(ShoutDefaultActivity.this, R.anim.push_right_out);
            objRelativeLayoutWantToHelp.startAnimation(outAnimationForWantToHelp);

            outAnimationForNeedHelp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    objLinearLayoutAskShoutType.setVisibility(LinearLayout.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            new AlertDialog.Builder(ShoutDefaultActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("")
                    .setMessage("Do you wish to exit the Shout App ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int pid = Process.myPid();
                            Process.killProcess(pid);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_shout_default_search:
                InputMethodManager inputMethodManager;
                if (objRelativeLayoutSearchBox.getVisibility() == RelativeLayout.VISIBLE) {
                    inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(objEditTextSearch.getApplicationWindowToken(), InputMethodManager.RESULT_HIDDEN, 0);
                } else {
                    objEditTextSearch.requestFocus();
                    inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(objEditTextSearch.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                    openCloseSearchBar(true, ShoutDefaultActivity.this);
                }
                break;
            case R.id.btn_shout_default_filter:
              /*  startActivity(new Intent(this, SortScreenActivity.class));
                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                finish();*/
                break;
            case R.id.btn_cancel_done_shout_default_header:
                objEditTextSearch.setText("");
                inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(objEditTextSearch.getApplicationWindowToken(), 0);
                objShoutDefaultListAdapter = new ShoutDefaultListAdapter(arrShoutDefaultListModel, ShoutDefaultActivity.this, ShoutDefaultActivity.this);
                objListViewShoutList.setAdapter(objShoutDefaultListAdapter);
                openCloseSearchBar(false, ShoutDefaultActivity.this);
                break;
            default:
                break;
        }
    }

    public class GetLoggedInUserShout extends AsyncTask<String, Void, String> {

        final ProgressDialog objProgressDialog = new ProgressDialog(ShoutDefaultActivity.this);

        public GetLoggedInUserShout() {

        }

        protected void onPreExecute() {
            super.onPreExecute();
            BaseActivity.objRelativeLayoutDefaultLoading.setVisibility(RelativeLayout.GONE);
            objProgressDialog.setMessage("Loading...");
            objProgressDialog.show();
        }

        protected String doInBackground(String... params) {
            String strResult = "";
            try {
                SharedPreferences objSharedPreferences = ShoutDefaultActivity.this.getSharedPreferences(Constants.PROFILE_PREFERENCES, ShoutDefaultActivity.STATE_ONSCREEN);
                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put(Constants.USER_ID, objSharedPreferences.getString(Constants.USER_ID, ""));
                System.out.println("INPUT JSON : " + objJsonObject.toString());
                strResult = NetworkUtils.postData(Constants.LOGGED_IN_USER_SHOUTS_API, objJsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            BaseActivity.objRelativeLayoutDefaultLoading.setVisibility(RelativeLayout.GONE);
            if (objProgressDialog.isShowing())
                objProgressDialog.dismiss();
            try {
                JSONObject jSONObject = new JSONObject(s);
                if (jSONObject.getString("result").equals("true")) {
                    JSONArray jSONArray = new JSONArray(jSONObject.getString("shout"));
                    ShoutDefaultActivity.this.arrShoutDefaultListModel.clear();
                    for (int index = ShoutDefaultActivity.STATE_ONSCREEN; index < jSONArray.length(); index += ShoutDefaultActivity.STATE_OFFSCREEN) {
                        if (jSONArray.getJSONObject(index).getString("shout_image").equals("null")) {
                            arrShoutDefaultListModel.add(new ShoutDefaultListModel(
                                    jSONArray.getJSONObject(index).getString("shout_id"),
                                    jSONArray.getJSONObject(index).getString(Constants.USER_ID),
                                    jSONArray.getJSONObject(index).getString(Constants.USER_NAME),
                                    jSONArray.getJSONObject(index).getString("user_pic"),
                                    jSONArray.getJSONObject(index).getString("comment_count"),
                                    jSONArray.getJSONObject(index).getString("like_count"),
                                    jSONArray.getJSONObject(index).getString("engaging_count"),
                                    jSONArray.getJSONObject(index).getString("shout_type"),
                                    jSONArray.getJSONObject(index).getString(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE),
                                    jSONArray.getJSONObject(index).getString(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION),
                                    Integer.parseInt(jSONArray.getJSONObject(index).getString("is_shout_like")),
                                    jSONArray.getJSONObject(index).getString("created_date"),
                                    "",
                                    Integer.parseInt(jSONArray.getJSONObject(index).getString("shout_hide_status")),
                                    ShoutDefaultActivity.this.VIEW_PAGER_DEFAULT_POSITION, Constants.SHOUT_PASS_ENGAGE_BUTTON_DYNAMIC_HEIGHT, Constants.DEFAULT_Y,
                                    jSONArray.getJSONObject(index).getString("images"),
                                    jSONArray.getJSONObject(index).getString("is_searchable"),
                                    jSONArray.getJSONObject(index).getString("latitude"),
                                    jSONArray.getJSONObject(index).getString("longitude"),
                                    jSONArray.getJSONObject(index).getString("address"),
                                    jSONArray.getJSONObject(index).getString("category"),
                                    jSONArray.getJSONObject(index).getString("category_id"),
                                    jSONArray.getJSONObject(index).getString("is_hidden"),
                                    jSONArray.getJSONObject(index).getString("start_date"),
                                    jSONArray.getJSONObject(index).getString("end_date"),
                                    jSONArray.getJSONObject(index).getString("reshout"),
                                    jSONArray.getJSONObject(index).getString("continue_chat"),
                                    jSONArray.getJSONObject(index).getString("km"),
                                    jSONArray.getJSONObject(index).getString("is_friend")));
                        } else {
                            arrShoutDefaultListModel.add(new ShoutDefaultListModel(
                                    jSONArray.getJSONObject(index).getString("shout_id"),
                                    jSONArray.getJSONObject(index).getString(Constants.USER_ID),
                                    jSONArray.getJSONObject(index).getString(Constants.USER_NAME),
                                    jSONArray.getJSONObject(index).getString("user_pic"),
                                    jSONArray.getJSONObject(index).getString("comment_count"),
                                    jSONArray.getJSONObject(index).getString("like_count"),
                                    jSONArray.getJSONObject(index).getString("engaging_count"),
                                    jSONArray.getJSONObject(index).getString("shout_type"),
                                    jSONArray.getJSONObject(index).getString(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE),
                                    jSONArray.getJSONObject(index).getString(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION),
                                    Integer.parseInt(jSONArray.getJSONObject(index).getString("is_shout_like")),
                                    jSONArray.getJSONObject(index).getString("created_date"),
                                    jSONArray.getJSONObject(index).getString("shout_image"),
                                    Integer.parseInt(jSONArray.getJSONObject(index).getString("shout_hide_status")),
                                    ShoutDefaultActivity.this.VIEW_PAGER_DEFAULT_POSITION,
                                    Constants.SHOUT_PASS_ENGAGE_BUTTON_DYNAMIC_HEIGHT,
                                    Constants.DEFAULT_Y,
                                    jSONArray.getJSONObject(index).getString("images"),
                                    jSONArray.getJSONObject(index).getString("is_searchable"),
                                    jSONArray.getJSONObject(index).getString("latitude"),
                                    jSONArray.getJSONObject(index).getString("longitude"),
                                    jSONArray.getJSONObject(index).getString("address"),
                                    jSONArray.getJSONObject(index).getString("category"),
                                    jSONArray.getJSONObject(index).getString("category_id"),
                                    jSONArray.getJSONObject(index).getString("is_hidden"),
                                    jSONArray.getJSONObject(index).getString("start_date"),
                                    jSONArray.getJSONObject(index).getString("end_date"),
                                    jSONArray.getJSONObject(index).getString("reshout"),
                                    jSONArray.getJSONObject(index).getString("continue_chat"),
                                    jSONArray.getJSONObject(index).getString("km"),
                                    jSONArray.getJSONObject(index).getString("is_friend")));
                        }
                    }
                    objShoutDefaultListAdapter = new ShoutDefaultListAdapter(arrShoutDefaultListModel, ShoutDefaultActivity.this, ShoutDefaultActivity.this);
                    objListViewShoutList.setAdapter(objShoutDefaultListAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class LoadMoreShouts extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            String strResult = "";
            try {
                intOffset = objDatabaseHelper.getShoutId("LAST");
                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));
                objJsonObject.put("offset", intOffset);
                objJsonObject.put("pull_refresh", "0");

                if (SortScreenActivity.arrSortFilterSelectedCategoryId.size() > 0) {
                    JSONArray objJSONArraycategories = new JSONArray();
                    for (int index = 0; index < SortScreenActivity.arrSortFilterSelectedCategoryId.size(); index++) {
                        objJSONArraycategories.put(SortScreenActivity.arrSortFilterSelectedCategoryId.get(index));
                    }
                    objJsonObject.put("categories", objJSONArraycategories);
                }

                objJsonObject.put("popularity", objSharedPreferences.getString(Constants.SORT_POPULARITY, ""));
                objJsonObject.put("recency", objSharedPreferences.getString(Constants.SORT_RECENCY, ""));
                objJsonObject.put("location", objSharedPreferences.getString(Constants.SORT_LOCATION, ""));
                objJsonObject.put("latitude", objSharedPreferences.getString(Constants.USER_REGISTERED_LATITUDE, ""));
                objJsonObject.put("longitude", objSharedPreferences.getString(Constants.USER_REGISTERED_LONGITUDE, ""));

                objJsonObject.put("preference_id", strPreferenceId);

                strResult = NetworkUtils.postData(Constants.SHOUT_LIST, objJsonObject.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Animation fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
                BaseActivity.objImageButtonCreateShout.startAnimation(fab_open);
                objLinearBottomLoad.setVisibility(LinearLayout.GONE);
                Constants.hideToBottom(objLinearBottomLoad);
                JSONObject jSONObject = new JSONObject(s);
                if (jSONObject.getString("result").equals("true")) {
                    // TELL THE USER THAT HE HAVE LOAD MORE SHOUTS AND NOW HE CAN SCROLL LISTVIEW TO BOTTOM AGAIN TILL NEWLY INSERTED LAST ITEM
                    // IT IS USED FOR CALLING LoadMoreAPI for once.

                    if (new JSONArray(jSONObject.getString("shout")).length() > 0) {
                        arrShoutDefaultListModel.addAll(objDatabaseHelper.saveShout(new JSONArray(jSONObject.getString("shout")), "0"));
                        System.out.println("SHOUT LOCAL DATA ARRAY COUNT : " + arrShoutDefaultListModel.size());
                        objShoutDefaultListAdapter.notifyDataSetChanged();
                        isLoading = false;
                    } else {
                        new LoadNearByShouts().execute();
                    }
                    // TODO: 30/09/16 SAVING NOTIFICATION COUNT INTO PROFILE SHAREDPREFERENCES
                    SharedPreferences.Editor editor = objSharedPreferences.edit();
                    editor.putString(Constants.USER_NOTIFICATION_COUNT, jSONObject.getString("notification_count"));
                    editor.commit();

                    updateNotificationCount(jSONObject.getString("notification_count"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class StoreShoutDataForFirstTime extends AsyncTask<String, Void, String> {
        final ProgressDialog objProgressDialog = new ProgressDialog(ShoutDefaultActivity.this);
        String key = "";

        public StoreShoutDataForFirstTime(String key) {
            this.key = key;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("KEY : " + key);
            if (key.equals("0")) {
                BaseActivity.objRelativeLayoutDefaultLoading.setVisibility(RelativeLayout.GONE);
                objProgressDialog.setMessage("Searching for shouts");
                objProgressDialog.show();
                /*objProgressDialog.setCanceledOnTouchOutside(false);
                objProgressDialog.setCancelable(false);*/
            } else {
                objShoutDefaultSwipableLayout.setRefreshing(true);
            }
        }

        protected String doInBackground(String... params) {
            String strResult = "";
            try {
                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));
                if (key.equals("1")) {
                    objJsonObject.put("offset", objDatabaseHelper.getShoutId("FIRST"));
                } else {
                    objJsonObject.put("offset", intOffset);
                }
                objJsonObject.put("pull_refresh", key);

                if (SortScreenActivity.arrSortFilterSelectedCategoryId.size() > 0) {
                    JSONArray objJSONArraycategories = new JSONArray();
                    for (int index = 0; index < SortScreenActivity.arrSortFilterSelectedCategoryId.size(); index++) {
                        objJSONArraycategories.put(SortScreenActivity.arrSortFilterSelectedCategoryId.get(index));
                    }
                    objJsonObject.put("categories", objJSONArraycategories);
                    ShoutDefaultActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            objViewFilterApply.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    ShoutDefaultActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            objViewFilterApply.setVisibility(View.GONE);
                        }
                    });
                }
                objJsonObject.put("popularity", objSharedPreferences.getString(Constants.SORT_POPULARITY, ""));
                objJsonObject.put("recency", objSharedPreferences.getString(Constants.SORT_RECENCY, ""));
                objJsonObject.put("location", objSharedPreferences.getString(Constants.SORT_LOCATION, ""));
                objJsonObject.put("latitude", objSharedPreferences.getString(Constants.USER_REGISTERED_LATITUDE, ""));
                objJsonObject.put("longitude", objSharedPreferences.getString(Constants.USER_REGISTERED_LONGITUDE, ""));
                objJsonObject.put("preference_id", strPreferenceId);


                if (objJsonObject.getString("popularity").equals("0") && objJsonObject.getString("recency").equals("0") && objJsonObject.getString("location").equals("0") && SortScreenActivity.arrSortFilterSelectedCategoryId.size() == 0) {
                    System.out.println("IN POPULARITY EMPTY");
                    ShoutDefaultActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            objViewFilterApply.setVisibility(View.GONE);
                        }
                    });
                } else {
                    System.out.println("IN POPULARITY SELECTED");
                    ShoutDefaultActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            objViewFilterApply.setVisibility(View.VISIBLE);
                        }
                    });
                }
                strResult = NetworkUtils.postData(Constants.SHOUT_LIST, objJsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            strPreferenceId="";
            objShoutDefaultSwipableLayout.setRefreshing(false);
            BaseActivity.objRelativeLayoutDefaultLoading.setVisibility(RelativeLayout.GONE);
            if (objProgressDialog.isShowing())
                objProgressDialog.dismiss();
            try {
                JSONObject jSONObject = new JSONObject(s);
                if (jSONObject.getString("result").equals("true")) {
                    JSONArray objJsonArray = new JSONArray(jSONObject.getString("shout"));


                    // TODO: 30/09/16 SAVING NOTIFICATION COUNT INTO PROFILE SHAREDPREFERENCES
                    SharedPreferences.Editor editor = objSharedPreferences.edit();
                    editor.putString(Constants.USER_NOTIFICATION_COUNT, jSONObject.getString("notification_count"));
                    editor.commit();

                    updateNotificationCount(jSONObject.getString("notification_count"));

                    System.out.println("SHOUT JSON ARRAY LENGTH : " + objJsonArray.toString().length());
                    System.out.println("TEST : " + key);
                    if (objJsonArray.length() > 0) {
                        if (key.equals("0")) {
                            objDatabaseHelper.deleteShoutEntries();
                        }
                        objDatabaseHelper.saveShout(objJsonArray, "0");
                        arrShoutDefaultListModel = objDatabaseHelper.getShoutDefaultListModelArray("0");
                        System.out.println("PRASANNA PRINT : MODEL ARRAY : " + arrShoutDefaultListModel.size());
                        objShoutDefaultListAdapter = new ShoutDefaultListAdapter(arrShoutDefaultListModel, ShoutDefaultActivity.this, ShoutDefaultActivity.this);
                        objListViewShoutList.setAdapter(objShoutDefaultListAdapter);
                    } else {
                        // TODO: 06/10/16 LOADING NEAR BY FRIENDS SHOUT IF FRIENDS SHOUTS ARE NOT AVAILABLE.
                        new LoadNearByShouts().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class ShoutUpdateBroadcastReceiver extends BroadcastReceiver {

        public ShoutUpdateBroadcastReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (inSearchMode == 0) {
                    objDatabaseHelper = new DatabaseHelper(ShoutDefaultActivity.this);
                    state = objListViewShoutList.onSaveInstanceState();
                    arrShoutDefaultListModel.clear();
                    arrShoutDefaultListModel = objDatabaseHelper.getShoutDefaultListModelArray("0");
                    objListViewShoutList.setAdapter(new ShoutDefaultListAdapter(arrShoutDefaultListModel, ShoutDefaultActivity.this, ShoutDefaultActivity.this));
                }
                if (state != null) {
                    objListViewShoutList.onRestoreInstanceState(state);
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class LoadNearByShouts extends AsyncTask<String, Void, String> {

        String strResult = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                int offset = objDatabaseHelper.getNearByFriendsShoutCount();
                System.out.println("SHOUT NEAR BY QUERY RESULT : " + offset);
                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));
                objJsonObject.put("offset", offset);
                objJsonObject.put("pull_refresh", "0");

                if (SortScreenActivity.arrSortFilterSelectedCategoryId.size() > 0) {
                    JSONArray objJSONArraycategories = new JSONArray();
                    for (int index = 0; index < SortScreenActivity.arrSortFilterSelectedCategoryId.size(); index++) {
                        objJSONArraycategories.put(SortScreenActivity.arrSortFilterSelectedCategoryId.get(index));
                    }
                    objJsonObject.put("categories", objJSONArraycategories);
                }
                objJsonObject.put("popularity", objSharedPreferences.getString(Constants.SORT_POPULARITY, ""));
                objJsonObject.put("recency", objSharedPreferences.getString(Constants.SORT_RECENCY, ""));
                objJsonObject.put("location", objSharedPreferences.getString(Constants.SORT_LOCATION, ""));
                objJsonObject.put("latitude", objSharedPreferences.getString(Constants.USER_REGISTERED_LATITUDE, ""));
                objJsonObject.put("longitude", objSharedPreferences.getString(Constants.USER_REGISTERED_LONGITUDE, ""));
                objJsonObject.put("preference_id", strPreferenceId);
                strResult = NetworkUtils.postData(Constants.NEIGHBOURS_SHOUT_API, objJsonObject.toString());
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return strResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            strPreferenceId="";
            try {
                Animation fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
                BaseActivity.objImageButtonCreateShout.startAnimation(fab_open);
                objLinearBottomLoad.setVisibility(LinearLayout.GONE);
                Constants.hideToBottom(objLinearBottomLoad);
                JSONObject jSONObject = new JSONObject(s);
                if (jSONObject.getString("result").equals("true")) {
                    // TELL THE USER THAT HE HAVE LOAD MORE SHOUTS AND NOW HE CAN SCROLL LISTVIEW TO BOTTOM AGAIN TILL NEWLY INSERTED LAST ITEM
                    // IT IS USED FOR CALLING LoadMoreAPI for once.
                    if (new JSONArray(jSONObject.getString("shout")).length() > 0) {
                        arrShoutDefaultListModel.addAll(objDatabaseHelper.saveShout(new JSONArray(jSONObject.getString("shout")), "0"));
                        System.out.println("SHOUT LOCAL DATA ARRAY COUNT : " + arrShoutDefaultListModel.size());
                        objShoutDefaultListAdapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class TapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            System.out.println("IN TAP_GESTURE CLICKED");
//            showCommunityPopup(true,"");
            return true;
        }
    }

    public void showCommunityPopup(final boolean result, String title) {
        // COMMUNITY POPUP INTIALIZATION
//        initPoup();
        if (result) {
            LayoutInflater objCommunityPopupInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewComunityPopup = objCommunityPopupInflater.inflate(R.layout.popup_comunity_layout, null, true);
            listViewComunityPopup = (ListView) viewComunityPopup.findViewById(R.id.listViewComunityPopup);
            objLinearCommunityPopupRoot = (LinearLayout) viewComunityPopup.findViewById(R.id.linear_community_wheel_root);
            popupWindowComunity = new PopupWindow(viewComunityPopup);
            popupWindowComunity.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            popupWindowComunity.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
            popupWindowComunity.setFocusable(true);
            popupWindowComunity.showAtLocation(viewComunityPopup, Gravity.CENTER, 0, 0);

            popupComunityLayoutAdapter = new PopupComunityLayoutAdapter(activity, activity, arrMyPreferencesModel, title);
            listViewComunityPopup.setAdapter(popupComunityLayoutAdapter);

            listViewComunityPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    popupWindowComunity.dismiss();
                    isPopupOpen = false;
                    try{
                        searchViewPager.setCurrentItem(position);
                        MyPreferencesModel objMyPreferencesModel = arrMyPreferencesModel.get(position);
                        strPreferenceId = objMyPreferencesModel.getPreference_id();
                        System.out.println("SELECTED PREFERENCE ID : " + strPreferenceId);
                        objDatabaseHelper.deleteShoutEntries();

//                    startActivity(new Intent(ShoutDefaultActivity.this, ShoutDefaultActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        arrShoutDefaultListModel.clear();
                        objShoutDefaultListAdapter.notifyDataSetChanged();
                        objListViewShoutList.setAdapter(null);
                        intOffset=0;
                        new StoreShoutDataForFirstTime("0").execute();
                    }catch(NullPointerException ne){
                        ne.printStackTrace();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
            objLinearCommunityPopupRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindowComunity.dismiss();
                }
            });
            isPopupOpen = true;
        } else {
            isPopupOpen = false;
        }
    }

    private void initPoup() {
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_default_root);
        objLinearCommunityPopupRoot = (LinearLayout) findViewById(R.id.linear_community_wheel_root);
        viewComunityPopup = LayoutInflater.from(activity).inflate(R.layout.popup_comunity_layout, objLinearCommunityPopupRoot, false);
        popupWindowComunity = new PopupWindow(ShoutDefaultActivity.this);
        popupWindowComunity.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindowComunity.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindowComunity.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.rounded_corner_like_popup_background));
        listViewComunityPopup = (ListView) viewComunityPopup.findViewById(R.id.listViewComunityPopup);
    }

    private void setDefaultItems() {
        arrMyPreferencesModel.clear();
        for (int i = 0; i < 3; i++) {
            MyPreferencesModel objMyPreferencesModel = new MyPreferencesModel();
            switch (i) {
                case 0:
                    objMyPreferencesModel = new MyPreferencesModel(
                            "0",
                            "0",
                            "All Shouts",
                            "0",
                            true);
                    arrMyPreferencesModel.add(objMyPreferencesModel);
                    break;
                case 1:
                    objMyPreferencesModel = new MyPreferencesModel(
                            "0",
                            "1",
                            "School",
                            "0",
                            true);
                    arrMyPreferencesModel.add(objMyPreferencesModel);
                    break;
                case 2:
                    objMyPreferencesModel = new MyPreferencesModel(
                            "0",
                            "2",
                            "Neighbour",
                            "0",
                            true);
                    arrMyPreferencesModel.add(objMyPreferencesModel);
                    break;
            }
        }
    }

    public void refreshWholeActivity() {

        try {
            updateNotificationCount(getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE).getString(Constants.USER_NOTIFICATION_COUNT, ""));

            objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, STATE_ONSCREEN);
            SharedPreferences.Editor objEditor = objSharedPreferences.edit();
            objEditor.putString(Constants.IS_NEW_USER, "false");
            objEditor.commit();

            // FOR FIRST TIME WHEN USER GETS FRESH LOGIN TO THE APP
            if (objSharedPreferences.getString(Constants.IS_CURRENT_DATE, "").equals("")) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat(Constants.SHOUT_LIST_DATE_FORMAT);
                String currentDate = df.format(c.getTime());
                objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, STATE_ONSCREEN);
                SharedPreferences.Editor objDateEditor = objSharedPreferences.edit();
                objDateEditor.putString(Constants.IS_CURRENT_DATE, currentDate);
                objDateEditor.commit();
            }
            arrShoutDefaultListModel = new ArrayList<ShoutDefaultListModel>();

            System.out.println("STORED DATE : " + objSharedPreferences.getString(Constants.IS_CURRENT_DATE, ""));
            System.out.println("CURRENT DATE : " + new SimpleDateFormat(Constants.SHOUT_LIST_DATE_FORMAT).format(Calendar.getInstance().getTime()));

            if (objSharedPreferences.getString(Constants.IS_CURRENT_DATE, "").equals(new SimpleDateFormat(Constants.SHOUT_LIST_DATE_FORMAT).format(Calendar.getInstance().getTime()))) {
                System.out.println("LOADING SHOUTS FOR CURRENT DATE : " + new SimpleDateFormat(Constants.SHOUT_LIST_DATE_FORMAT).format(Calendar.getInstance().getTime()));
                if (ConnectivityBroadcastReceiver.isConnected()) {
                    arrShoutDefaultListModel.clear();
                    arrShoutDefaultListModel = objDatabaseHelper.getShoutDefaultListModelArray("0");
                    if (arrShoutDefaultListModel.size() == 0) {
                        new StoreShoutDataForFirstTime("0").execute();
                    } else {
                        objShoutDefaultListAdapter = new ShoutDefaultListAdapter(arrShoutDefaultListModel, ShoutDefaultActivity.this, ShoutDefaultActivity.this);
                        objListViewShoutList.setAdapter(objShoutDefaultListAdapter);
                    }
                } else {
                    arrShoutDefaultListModel.clear();
                    arrShoutDefaultListModel = objDatabaseHelper.getShoutDefaultListModelArray("0");
                    System.out.println("OFFLINE DATA : " + arrShoutDefaultListModel);
                        objShoutDefaultListAdapter = new ShoutDefaultListAdapter(arrShoutDefaultListModel, ShoutDefaultActivity.this, ShoutDefaultActivity.this);
                        objListViewShoutList.setAdapter(objShoutDefaultListAdapter);
                }
            } else {

                objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, STATE_ONSCREEN);
                SharedPreferences.Editor objDateEditor = objSharedPreferences.edit();
                objDateEditor.putString(Constants.IS_CURRENT_DATE, new SimpleDateFormat(Constants.SHOUT_LIST_DATE_FORMAT).format(Calendar.getInstance().getTime()));
                objDateEditor.commit();

                arrShoutDefaultListModel.clear();
                arrShoutDefaultListModel = objDatabaseHelper.getShoutDefaultListModelArray("0");
                System.out.println("OFFLINE DATA : " + arrShoutDefaultListModel);
                if (arrShoutDefaultListModel.size() > 0) {
                    objShoutDefaultListAdapter = new ShoutDefaultListAdapter(arrShoutDefaultListModel, ShoutDefaultActivity.this, ShoutDefaultActivity.this);
                    objListViewShoutList.setAdapter(objShoutDefaultListAdapter);
                } else {
                    new StoreShoutDataForFirstTime("0").execute();
                }
            }

            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
