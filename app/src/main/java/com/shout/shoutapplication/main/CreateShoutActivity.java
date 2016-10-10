package com.shout.shoutapplication.main;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.ApplicationUtils;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.ImageUtils;
import com.shout.shoutapplication.Utils.KeyboardUtils;
import com.shout.shoutapplication.Utils.LocationDetails;
import com.shout.shoutapplication.Utils.NetworkUtils;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.base.BaseActivity;
import com.shout.shoutapplication.database.DatabaseHelper;
import com.shout.shoutapplication.main.Adapter.CategoryAdapter;
import com.shout.shoutapplication.main.Adapter.GridViewResourceAdapter;
import com.shout.shoutapplication.main.Model.CategoryModel;
import com.shout.shoutapplication.main.Model.GridViewResourceModel;
import com.shout.shoutapplication.others.SoftKeyboard;
import com.shout.shoutapplication.others.TouchableMapFragment;
import com.shout.shoutapplication.others.TouchableWrapper;
import com.shout.shoutapplication.service.UploadResourceService;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CreateShoutActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {


    public DatabaseHelper objDatabaseHelper;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final ArrayList<CategoryModel> arrCategoryModel = new ArrayList<CategoryModel>();
    // POST REQUEST COMPONENTS
    public static GridView objGridViewResourcePostRequest;
    public static GridViewResourceAdapter objGridViewResourceAdapter;
    public static ImageView imageViewPostRequestLocation;
    public static CreateShoutActivity ActivityContext = null;
    public static TextView output;
    public static CameraUpdate zoom;
    //    public static boolean isListingShown = false;
    public static boolean isRequestShown = true;
    public static int ResourcePosition;
    private static GoogleMap objGoogleMap;
    public GridView objCategoryGridView;
    public List<Address> addresses;
    public TextView textViewDoneLoading;
    RelativeLayout objRootLayout;
    Button btnPostRequest;
    Button btnPostListing;
    TextView btnGiveShout;
    TextView btnCreateShoutBack;
    ScrollView scrPostRequest;
    CategoryAdapter objCategoryAdapter;
    ArrayList<GridViewResourceModel> arrGridViewResourceModel;
    EditText objEditTextPostRequestTitle;
    TextView objTextViewPostRequestTitleCount;
    EditText objEditTextShoutDescriptionPostRequest;
    TextView objTextViewShoutHereTextCount;
    CheckBox objCheckPostRequestEngage;
    EditText objEditTextRequestStartDate;
    EditText objEditTextRequestEndDate;
    ImageButton objImageButtonRequestStartDate;
    ImageButton objImageButtonRequestEndDate;
    EditText objEditTextRequestAddress;
    LinearLayout objLinearDatePickerShorcutsPostRequest;
    Button btnWeekPostRequest;
    Button btnMonthPostRequest;
    Button btnYearPostRequest;
    Button btnLifePostRequest;
    CheckBox objCheckBoxHideShowDateLayout;
    LinearLayout objLinearLayoutHideShowDateLayout;
    TextView objTextViewShowCurrentLocation;
    TextView objTextViewShowHomeLocation;
    SoftKeyboard softKeyboard;
    Calendar objCalendar;
    boolean mUpdatesRequested = false;
    String UserLatitude = "";
    String UserLongitude = "";
    LatLng latLong;
    CameraUpdate objCameraUpdate;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    SharedPreferences objProfileSharedPreferences;
    SharedPreferences objTempLocationSharedPreferences;
    // POPUP WINDOWS OBJECTS FOR SHOWING USER CHOOSED RESOURCES
    LayoutInflater objPopupInflater;
    View objPopupLayout;
    VideoView objVideoView;
    ImageView objPopupImage;
    TextView objTextViewRemove;
    TextView objTextViewClose;
    String strDateType = "";
    // CATEGORY LAYOUT VIEW OBJECTS
    View customCategoryAlertLayout;
    PopupWindow objPopupWindowCategories;
    boolean showBackClickAlert = false;
    TextView objTextViewPostRequestSelectedCategoryTitle;
    TextView objTextViewPostRequestSpecifyDate;
    TextView objTextViewPostRequestEngageNeighbourhood;
    LinearLayout objLinearLayoutSearchBar;
    ImageView objImageViewSearchAddress;
    String is_engage_with_neighbourhood = "Y";
    Uri videoUri = null;
    String strShoutType = "R";
    // GPS ON ACTIVITY RESULT INITIALISATION
    int GPS_REQUEST_CODE = 2016;
    // SOFT KEYBORAD
    InputMethodManager imm;
    private RelativeLayout objRelativeLayoutOpenCategoryPopupRequest;
    private RelativeLayout objRelativeCategoryPopupOutsideTouch;
    private Button btnOkCategoryPopup;
    private Button btnCancelCategoryPopup;
    private String blockCharacterSet = "~#^|$%&*!";
    // SELECTING RESUORCES FROM SD-CARD OR INTERNAL STORAGE
    private Uri fileUri;
    //MAP OBJECTS FOR GETTING ADDRESS FROM MAP
    private TouchableMapFragment mGoogleMap;
    private LatLng center;
    private Geocoder geocoder;
    private RelativeLayout relativeLayoutMap;
    private TextView objTextViewMapAddress;
    private HashMap<Integer, Uri> requestImageList;
    private ArrayList<String> arrResourceType = new ArrayList<String>();
    private ApplicationUtils applicationUtils;
    public static Uri mCapturedImageURI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shout);

        objDatabaseHelper = new DatabaseHelper(this);

        /*startService(new Intent(CreateShoutActivity.this,UploadResourceService.class));*/

        applicationUtils = new ApplicationUtils(this);
        isRequestShown = getIntent().getExtras().getBoolean("IS_REQUEST_SHOWN");
        objProfileSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor objEditor = objProfileSharedPreferences.edit();
        objEditor.putString(Constants.CURRENT_ACTIVITY_NAME_FOR_SHOW_MAP, Constants.CREATE_SHOUT_ATIVITY);
        objEditor.commit();

        ActivityContext = this;
        // TO HIDE TOP AND BOTTOM CUSTOM DEFAULT ACTION BAR AND TAB VIEW
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        hideAllView();
        inititialize();
//        Utils.isGpsEnable(CreateShoutActivity.this, GPS_REQUEST_CODE);

        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                Log.d("keyboard", "keyboard visible: " + isVisible);
                if (isVisible) {
                    RelativeLayout objRelativeLayout = (RelativeLayout) findViewById(R.id.relative_bottom_buttons_create_shout);
                    objRelativeLayout.setVisibility(RelativeLayout.GONE);
                } else {
                    Handler objHandler = new Handler();
                    objHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RelativeLayout objRelativeLayout = (RelativeLayout) findViewById(R.id.relative_bottom_buttons_create_shout);
                            objRelativeLayout.setVisibility(RelativeLayout.VISIBLE);
                        }
                    }, 300);
                }
            }
        });

        new DisplayAddress().execute(
                objProfileSharedPreferences.getString(Constants.USER_REGISTERED_LATITUDE, ""),
                objProfileSharedPreferences.getString(Constants.USER_REGISTERED_LONGITUDE, ""),
                objProfileSharedPreferences.getString(Constants.USER_REGISTERED_LOCATION_ZOOM, ""));
        UserLatitude = objProfileSharedPreferences.getString(Constants.USER_REGISTERED_LATITUDE, "");
        UserLongitude = objProfileSharedPreferences.getString(Constants.USER_REGISTERED_LONGITUDE, "");

        objTempLocationSharedPreferences = getSharedPreferences(Constants.LOCATION_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor objEditorLocation = objProfileSharedPreferences.edit();
        objEditorLocation.putString(Constants.LOCATION_LATITUDE, UserLatitude);
        objEditorLocation.putString(Constants.LOCATION_LONGITUDE, UserLongitude);
        objEditorLocation.commit();

        setUpMap();
        new LoadCategoryApi().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        scrPostRequest.setVisibility(ScrollView.VISIBLE);

        if (isRequestShown) {
            strShoutType = "R";
            btnPostRequest.setBackgroundColor(getResources().getColor(R.color.red_background_color));
            btnPostRequest.setTextColor(getResources().getColor(R.color.white_text_color));
            btnPostListing.setBackgroundColor(Color.parseColor("#EFEFEF"));
            btnPostListing.setTextColor(Color.parseColor("#737373"));
            btnPostRequest.setText("I NEED HELP");
            objTextViewPostRequestEngageNeighbourhood.setText("Request People in your Neighbourhood");
        } else {
            strShoutType = "L";
            objTextViewPostRequestEngageNeighbourhood.setText("Share with People in your Neighbourhood");
            btnPostRequest.setBackgroundColor(getResources().getColor(R.color.red_background_color));
            btnPostRequest.setTextColor(getResources().getColor(R.color.white_text_color));
            btnPostRequest.setBackgroundColor(Color.parseColor("#EFEFEF"));
            btnPostRequest.setTextColor(Color.parseColor("#737373"));
            btnPostListing.setText("I WANT TO GIVE");
            btnPostListing.setBackgroundColor(getResources().getColor(R.color.red_background_color));
            btnPostListing.setTextColor(getResources().getColor(R.color.white_text_color));
        }
        setDefaultItem();
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Constants.APPLICATION_PATH);
        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {

                output.setText("Failed to create directory MyCameraVideo.");

                Toast.makeText(ActivityContext, "Failed to create directory MyCameraVideo.",
                        Toast.LENGTH_LONG).show();

                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }
        // Create a media file name
        // For unique file name appending current timeStamp with file name
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());

        File mediaFile;

        if (type == MEDIA_TYPE_VIDEO) {
            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        return mediaFile;
    }

    public static void updateMapLocation(double latitude, double longitude) {
        LatLng latLong = new LatLng(latitude, longitude);
        CameraUpdate objCameraUpdate = CameraUpdateFactory.newLatLng(latLong);
        zoom = CameraUpdateFactory.zoomTo(15.552834f);
        objGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 15.552834f));
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationDetails objLocationDetails = new LocationDetails(CreateShoutActivity.this);
        objLocationDetails.startTracking();
    }

    public void inititialize() {

        // CUSTOM POPUP LAYOUT COMPONENTS
        objLinearLayoutSearchBar = (LinearLayout) findViewById(R.id.linear_top_map_search_bar);
        objPopupInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        objPopupLayout = objPopupInflater.inflate(R.layout.create_shout_screen_resource_select_popup_layout, null, true);
        objVideoView = (VideoView) objPopupLayout.findViewById(R.id.video_view_popup_layout);
        objPopupImage = (ImageView) objPopupLayout.findViewById(R.id.image_create_shout_popup_layout);
        objTextViewRemove = (TextView) objPopupLayout.findViewById(R.id.txt_create_shout_remove_popup_layout);
        objTextViewClose = (TextView) objPopupLayout.findViewById(R.id.txt_create_shout_close_popup_layout);

        // CUSTOM ALERT DIALOG FOR CATEGORY LIST
        objPopupInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customCategoryAlertLayout = objPopupInflater.inflate(R.layout.category_popup_layout, null, true);
        objCategoryGridView = (GridView) customCategoryAlertLayout.findViewById(R.id.gridview_category);
        objRelativeCategoryPopupOutsideTouch = (RelativeLayout) customCategoryAlertLayout.findViewById(R.id.relative_category_popup_outside_touch);
        btnCancelCategoryPopup = (Button) customCategoryAlertLayout.findViewById(R.id.btn_category_popup_cancel);
        btnOkCategoryPopup = (Button) customCategoryAlertLayout.findViewById(R.id.btn_category_popup_okay);

        objPopupWindowCategories = new PopupWindow(customCategoryAlertLayout);
        objPopupWindowCategories.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        objPopupWindowCategories.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        objPopupWindowCategories.setFocusable(true);


        //map initialization
        objImageViewSearchAddress = (ImageView) findViewById(R.id.img_create_shout_search_icon);
        objTextViewMapAddress = (TextView) findViewById(R.id.textViewMapAddress);
        relativeLayoutMap = (RelativeLayout) findViewById(R.id.relative_layout_create_shout_map);
        //POST REQUEST MAP COMPONENTS
        imageViewPostRequestLocation = (ImageView) findViewById(R.id.imageView_post_request_location);
        textViewDoneLoading = (TextView) findViewById(R.id.map_loading_done_create_shout_screen);
        btnPostRequest = (Button) findViewById(R.id.btn_create_shout_post_request);
        btnPostListing = (Button) findViewById(R.id.btn_create_shout_post_listing);
        btnGiveShout = (TextView) findViewById(R.id.btn_give_shout);
        btnCreateShoutBack = (TextView) findViewById(R.id.btn_create_shout_back);
        scrPostRequest = (ScrollView) findViewById(R.id.scrollview_post_request_layout);

        // POST REQUEST COMPONENTS
        objGridViewResourcePostRequest = (GridView) findViewById(R.id.gridview_image_post_request);
        objEditTextPostRequestTitle = (EditText) findViewById(R.id.edt_post_request_title);
        objTextViewPostRequestTitleCount = (TextView) findViewById(R.id.txt_post_request_title_count);
        objEditTextShoutDescriptionPostRequest = (EditText) findViewById(R.id.edt_post_request_shout_here);
        objTextViewShoutHereTextCount = (TextView) findViewById(R.id.txt_shout_post_request_here_count);
        objCheckPostRequestEngage = (CheckBox) findViewById(R.id.check_engage_post_request);
        objEditTextRequestStartDate = (EditText) findViewById(R.id.edt_post_request_start_date);
        objEditTextRequestEndDate = (EditText) findViewById(R.id.edt_post_request_end_date);
        objImageButtonRequestStartDate = (ImageButton) findViewById(R.id.imgbutton_request_start_date);
        objImageButtonRequestEndDate = (ImageButton) findViewById(R.id.imgbutton_request_end_date);
        objEditTextRequestAddress = (EditText) findViewById(R.id.editText_post_request_address);
        objLinearDatePickerShorcutsPostRequest = (LinearLayout) findViewById(R.id.linear_date_shortcuts_post_request);
        btnWeekPostRequest = (Button) findViewById(R.id.btn_week_post_request);
        btnMonthPostRequest = (Button) findViewById(R.id.btn_month_post_request);
        btnYearPostRequest = (Button) findViewById(R.id.btn_year_post_request);
        btnLifePostRequest = (Button) findViewById(R.id.btn_life_post_request);
        objRelativeLayoutOpenCategoryPopupRequest = (RelativeLayout) findViewById(R.id.relative_category_post_request);
        objCheckBoxHideShowDateLayout = (CheckBox) findViewById(R.id.open_date_layout_post_request);
        objLinearLayoutHideShowDateLayout = (LinearLayout) findViewById(R.id.linear_hide_show_date_post_request);
        objTextViewShowCurrentLocation = (TextView) findViewById(R.id.txt_current_location_post_request);
        objTextViewShowHomeLocation = (TextView) findViewById(R.id.txt_home_location_post_request);
        objTextViewPostRequestSelectedCategoryTitle = (TextView) findViewById(R.id.txt_selected_category_name_post_request);
        objTextViewPostRequestSpecifyDate = (TextView) findViewById(R.id.txt_specify_date_post_request);
        objTextViewPostRequestEngageNeighbourhood = (TextView) findViewById(R.id.txt_neighbourhood_post_request);

        objTextViewPostRequestSpecifyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                if (objCheckBoxHideShowDateLayout.isChecked()) {
                    objCheckBoxHideShowDateLayout.setChecked(false);
                    objLinearLayoutHideShowDateLayout.setVisibility(LinearLayout.GONE);
                } else {
                    objCheckBoxHideShowDateLayout.setChecked(true);
                    objLinearLayoutHideShowDateLayout.setVisibility(LinearLayout.VISIBLE);
                }
            }
        });

        objCheckBoxHideShowDateLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hideKeyboard(buttonView);
                if (isChecked) {
                    objLinearLayoutHideShowDateLayout.setVisibility(LinearLayout.VISIBLE);
                    strDateType = "";
                } else {
                    objLinearLayoutHideShowDateLayout.setVisibility(LinearLayout.GONE);
                    strDateType = "L";
                }
            }
        });

        objTextViewPostRequestEngageNeighbourhood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objCheckPostRequestEngage.isChecked()) {
                    objCheckPostRequestEngage.setChecked(false);
                    is_engage_with_neighbourhood = "N";
                } else {
                    objCheckPostRequestEngage.setChecked(true);
                    is_engage_with_neighbourhood = "Y";
                }
                hideKeyboard(v);
            }
        });

        /*objCheckPostRequestEngage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    objCheckPostRequestEngage.setChecked(false);
                    is_engage_with_neighbourhood = "N";
                } else {
                    objCheckPostRequestEngage.setChecked(true);
                    is_engage_with_neighbourhood = "Y";
                }
                hideKeyboard(buttonView);
            }
        });*/

        /*// POST LISTING COMPONENTS
        objGridViewResourcePostListing = (GridView) findViewById(R.id.gridview_image_post_listing);
        objEditTextPostRequestTitle = (EditText) findViewById(R.id.edt_post_request_title_listing);
        objTextViewPostListingTitleCount = (TextView) findViewById(R.id.txt_post_request_title_count_listing);
        objEditTextShoutDescriptionPostRequest = (EditText) findViewById(R.id.edt_post_request_shout_here_listing);
        objTextViewShoutHereTextCountListing = (TextView) findViewById(R.id.txt_shout_post_request_here_count_listing);
        objCheckPostRequestEngageListing = (CheckBox) findViewById(R.id.check_engage_post_request_listing);
        objEditTextRequestStartDate = (EditText) findViewById(R.id.edt_post_request_start_date_listing);
        objEditTextRequestEndDate = (EditText) findViewById(R.id.edt_post_request_end_date_listing);
        objImageButtonRequestStartDateListing = (ImageButton) findViewById(R.id.imgbutton_request_start_date_listing);
        objImageButtonRequestEndDateListing = (ImageButton) findViewById(R.id.imgbutton_request_end_date_listing);
        objEditTextRequestAddress = (EditText) findViewById(R.id.editText_post_listing_address);
        objLinearDatePickerShorcutsPostListing = (LinearLayout) findViewById(R.id.linear_date_shortcuts_post_listing);
        btnWeekPostListing = (Button) findViewById(R.id.btn_week_post_listing);
        btnMonthPostListing = (Button) findViewById(R.id.btn_month_post_listing);
        btnYearPostListing = (Button) findViewById(R.id.btn_year_post_listing);
        btnLifePostListing = (Button) findViewById(R.id.btn_life_post_listing);
        objRelativeLayoutOpenCategorypopupListing = (RelativeLayout) findViewById(R.id.relative_category_post_request_listing);
        objCheckBoxHideShowDateLayoutListing = (CheckBox) findViewById(R.id.open_date_layout_post_request_listing);
        objTextViewShowCurrentLocationListing = (TextView) findViewById(R.id.txt_current_location_post_listing);
        objTextViewShowHomeLocationListing = (TextView) findViewById(R.id.txt_home_location_post_listing);
        objTextViewPostListingSelectedCategoryTitle = (TextView) findViewById(R.id.txt_selected_category_name_post_listing);
        objTextViewPostListingSpecifyDate = (TextView) findViewById(R.id.txt_specify_date_post_listing);
        objTextViewPostListingEngageNeighbourhood = (TextView) findViewById(R.id.txt_neighbourhood_post_listing);

        objTextViewPostListingSpecifyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objCheckBoxHideShowDateLayoutListing.isChecked()) {
                    objCheckBoxHideShowDateLayoutListing.setChecked(false);
                } else {
                    objCheckBoxHideShowDateLayoutListing.setChecked(true);
                }
            }
        });

        objCheckBoxHideShowDateLayoutListing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    objLinearLayoutHideShowDateLayoutListing.setVisibility(LinearLayout.VISIBLE);
                    strDateType = "";
                } else {
                    objLinearLayoutHideShowDateLayoutListing.setVisibility(LinearLayout.GONE);
                    strDateType = "";
                }
            }
        });

        objTextViewPostListingEngageNeighbourhood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objCheckPostRequestEngageListing.isChecked()) {
                    objCheckPostRequestEngageListing.setChecked(false);
                    is_engage_with_neighbourhood = "N";
                } else {
                    objCheckPostRequestEngageListing.setChecked(true);
                    is_engage_with_neighbourhood = "Y";
                }
            }
        });*/

        objEditTextPostRequestTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("STRING TITLE LENGTH : " + s.length());
                if (s.length() <= 30) {
                    objTextViewPostRequestTitleCount.setText(s.length() + "/30");
                }
                if (s.length() > 0) {
                    showBackClickAlert = true;
                } else {
                    showBackClickAlert = false;
                }
            }
        });

        objEditTextShoutDescriptionPostRequest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("SHOUT TEXT HERE LENGTH : " + s.length());
                if (s.length() <= 300) {
                    objTextViewShoutHereTextCount.setText(s.length() + "/300");
                }
                if (s.length() > 0) {
                    showBackClickAlert = true;
                } else {
                    showBackClickAlert = false;
                }
            }
        });

        /*InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);

        objRootLayout = (RelativeLayout) findViewById(R.id.relative_create_shout);

        softKeyboard = new SoftKeyboard(objRootLayout, im);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {

            @Override
            public void onSoftKeyboardHide() {
                System.out.println("KEYBOARD CLOSE");
                CreateShoutActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            RelativeLayout objRelativeLayout = (RelativeLayout) findViewById(R.id.relative_bottom_buttons_create_shout);
                            objRelativeLayout.setVisibility(RelativeLayout.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onSoftKeyboardShow() {
                System.out.println("KEYBOARD OPEN");
                CreateShoutActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            RelativeLayout objRelativeLayout = (RelativeLayout) findViewById(R.id.relative_bottom_buttons_create_shout);
                            objRelativeLayout.setVisibility(RelativeLayout.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });*/

        setListener();

        objTextViewMapAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(CreateShoutActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        objImageViewSearchAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(CreateShoutActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        objLinearLayoutSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(CreateShoutActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
    }

    private void hideKeyboard(View view) {
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setDefaultItem() {
        arrGridViewResourceModel = new ArrayList<GridViewResourceModel>();
        for (int index = 0; index < Constants.MAX_RESOURCE_SIZE; index++) {
            GridViewResourceModel objGridViewResourceModel;
            if (index == 0) {
                objGridViewResourceModel = new GridViewResourceModel(null, "", true, true, null);
            } else {
                objGridViewResourceModel = new GridViewResourceModel(null, "", false, true, null);
            }
            arrGridViewResourceModel.add(objGridViewResourceModel);
        }
        objGridViewResourceAdapter = new GridViewResourceAdapter(arrGridViewResourceModel, CreateShoutActivity.this);
        objGridViewResourcePostRequest.setAdapter(objGridViewResourceAdapter);
        System.out.println("DEFAULT MODEL OBJECT REQUEST : " + arrGridViewResourceModel);

    }

    private void setMyLoactionButtonPosition() {
        Fragment fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        ViewGroup v1 = (ViewGroup) fragment.getView();
        ViewGroup v2 = (ViewGroup) v1.getChildAt(0);
        ViewGroup v3 = (ViewGroup) v2.getChildAt(2);
        View position = (View) v3.getChildAt(0);
        int positionWidth = position.getLayoutParams().width;
        int positionHeight = position.getLayoutParams().height;

        //lay out position button
        RelativeLayout.LayoutParams positionParams = new RelativeLayout.LayoutParams(positionWidth, positionHeight);
     /*   int margin = positionWidth / 5;
        positionParams.setMargins(margin, 0, 0, margin);*/
        positionParams.setMargins(0, 0, 10, 10);
        positionParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        positionParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        position.setLayoutParams(positionParams);
    }

    private void setUpMap() {
        try {
            mGoogleMap = (TouchableMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mGoogleMap.getMapAsync(this);
            setMyLoactionButtonPosition();
            mGoogleMap.setTouchListener(new TouchableWrapper.OnTouchListener() {
                ImageView objMarker = (ImageView) findViewById(R.id.imageView_marker);

                @Override
                public void onTouch() {
                    System.out.println("MAP TOUCH ACTIVATED");
                    objLinearLayoutSearchBar.setVisibility(LinearLayout.GONE);
//                    objTextViewMapAddress.setVisibility(TextView.GONE);
                    Constants.hideToTop(objLinearLayoutSearchBar);
                    textViewDoneLoading.setVisibility(TextView.GONE);
                    Constants.hideToBottom(textViewDoneLoading);
                }

                @Override
                public void onRelease() {
                    System.out.println("MAP TOUCH DE-ACTIVATED");
                    objLinearLayoutSearchBar.setVisibility(LinearLayout.VISIBLE);
                    Constants.show(objLinearLayoutSearchBar);
                    textViewDoneLoading.setVisibility(TextView.VISIBLE);
                    Constants.show(textViewDoneLoading);
                }
            });
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListener() {
        btnGiveShout.setOnClickListener(this);
        btnCreateShoutBack.setOnClickListener(this);
        btnPostRequest.setOnClickListener(this);
        btnPostListing.setOnClickListener(this);


        //POST REQUEST OBJECTS
        btnWeekPostRequest.setOnClickListener(this);
        btnMonthPostRequest.setOnClickListener(this);
        btnYearPostRequest.setOnClickListener(this);
        btnLifePostRequest.setOnClickListener(this);
        objRelativeLayoutOpenCategoryPopupRequest.setOnClickListener(this);
        objTextViewShowCurrentLocation.setOnClickListener(this);
        objTextViewShowHomeLocation.setOnClickListener(this);


        //MAP IMAGEVIEW LISTENER
        imageViewPostRequestLocation.setOnClickListener(this);


        textViewDoneLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objTextViewMapAddress.getText().equals(" Getting location ")) {
                    Toast.makeText(CreateShoutActivity.this, "Location not traced. Please wait...", Toast.LENGTH_SHORT).show();
                } else {
                    if (UserLatitude.length() > 0 && UserLongitude.length() > 0) {
                        String strStaticMapUrl = getGoogleMapThumbnail(Double.parseDouble(UserLatitude), Double.parseDouble(UserLongitude));
                        System.out.println("STATIC MAP URL : " + strStaticMapUrl);
                        Picasso.with(CreateShoutActivity.this).load(strStaticMapUrl).into(imageViewPostRequestLocation);
                        objEditTextRequestAddress.setText(objTextViewMapAddress.getText());
                      /*  if (isRequestShown) {

                        } else {
                            Picasso.with(CreateShoutActivity.this).load(strStaticMapUrl).into(imageViewPostListingtLocation);
                            objEditTextRequestAddress.setText(objTextViewMapAddress.getText());
                        }*/
                    }
                    showMap(false);
                }
//                stopService(new Intent(CreateShoutActivity.this, LocationBackGroundService.class));
            }
        });

        objEditTextRequestStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                System.out.println("IN REQUEST START DATE ");
                try {
                    if (objEditTextRequestStartDate.getText().toString().length() > 0) {
                        objCalendar = new GregorianCalendar();
                        String strFromDate = objEditTextRequestStartDate.getText().toString();
                        String dateSpliterFirst[] = strFromDate.split("/");
                        objCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSpliterFirst[0].toString()));
                        objCalendar.set(Calendar.MONTH, Integer.parseInt(dateSpliterFirst[1].toString()) - 1);
                        objCalendar.set(Calendar.YEAR, Integer.parseInt(dateSpliterFirst[2].toString()));
                    } else {
                        objCalendar = Calendar.getInstance();
                    }
                    new DatePickerDialog(CreateShoutActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            objCalendar.set(Calendar.YEAR, year);
                            objCalendar.set(Calendar.MONTH, monthOfYear);
                            objCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                            SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat("yyyy-MMMM-dd");
                            SimpleDateFormat sendDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String strDate1 = sdf.format(objCalendar.getTime());
                            String sendDate1 = sendDateFormat.format(objCalendar.getTime());
                            System.out.println("REQUEST START DATE :" + sendDate1);
                            objEditTextRequestStartDate.setText(sendDate1);
                            objEditTextRequestEndDate.setText("");
                            resetDateSelector();
                            if (sendDate1.length() > 0) {
                                objLinearDatePickerShorcutsPostRequest.setVisibility(LinearLayout.VISIBLE);
                            } else {
                                objLinearDatePickerShorcutsPostRequest.setVisibility(LinearLayout.GONE);
                            }

                        }
                    }, objCalendar.get(Calendar.YEAR), objCalendar.get(Calendar.MONTH), objCalendar.get(Calendar.DAY_OF_MONTH)).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        objImageButtonRequestStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                System.out.println("IN REQUEST START DATE ");
                try {
                    if (objEditTextRequestStartDate.getText().toString().length() > 0) {
                        objCalendar = new GregorianCalendar();
                        String strFromDate = objEditTextRequestStartDate.getText().toString();
                        String dateSpliterFirst[] = strFromDate.split("/");
                        objCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSpliterFirst[0].toString()));
                        objCalendar.set(Calendar.MONTH, Integer.parseInt(dateSpliterFirst[1].toString()) - 1);
                        objCalendar.set(Calendar.YEAR, Integer.parseInt(dateSpliterFirst[2].toString()));
                    } else {
                        objCalendar = Calendar.getInstance();
                    }
                    new DatePickerDialog(CreateShoutActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            objCalendar.set(Calendar.YEAR, year);
                            objCalendar.set(Calendar.MONTH, monthOfYear);
                            objCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                            SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat("yyyy-MMMM-dd");
                            SimpleDateFormat sendDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String strDate1 = sdf.format(objCalendar.getTime());
                            String sendDate1 = sendDateFormat.format(objCalendar.getTime());
                            System.out.println("REQUEST START DATE :" + sendDate1);
                            objEditTextRequestStartDate.setText(sendDate1);
                            objEditTextRequestEndDate.setText("");
                            resetDateSelector();
                            if (sendDate1.length() > 0) {
                                objLinearDatePickerShorcutsPostRequest.setVisibility(LinearLayout.VISIBLE);
                            } else {
                                objLinearDatePickerShorcutsPostRequest.setVisibility(LinearLayout.GONE);
                            }

                        }
                    }, objCalendar.get(Calendar.YEAR), objCalendar.get(Calendar.MONTH), objCalendar.get(Calendar.DAY_OF_MONTH)).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        objEditTextRequestEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                System.out.println("IN REQUEST START DATE ");
                try {
                    if (objEditTextRequestStartDate.getText().length() > 0) {
                        if (objEditTextRequestEndDate.getText().toString().length() > 0) {
                            objCalendar = new GregorianCalendar();
                            String strFromDate = objEditTextRequestEndDate.getText().toString();
                            String dateSpliterFirst[] = strFromDate.split("/");
                            objCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSpliterFirst[0].toString()));
                            objCalendar.set(Calendar.MONTH, Integer.parseInt(dateSpliterFirst[1].toString()) - 1);
                            objCalendar.set(Calendar.YEAR, Integer.parseInt(dateSpliterFirst[2].toString()));
                        } else {
                            objCalendar = Calendar.getInstance();
                        }
                        new DatePickerDialog(CreateShoutActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                objCalendar.set(Calendar.YEAR, year);
                                objCalendar.set(Calendar.MONTH, monthOfYear);
                                objCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                                SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat("yyyy-MMMM-dd");
                                SimpleDateFormat sendDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String strDate1 = sdf.format(objCalendar.getTime());
                                String sendDate1 = sendDateFormat.format(objCalendar.getTime());
                                System.out.println("REQUEST START DATE :" + sendDate1);

                                if (isDateCorrect(objEditTextRequestStartDate.getText().toString(), sendDate1)) {
                                    objEditTextRequestEndDate.setText(sendDate1);
                                } else {
                                    objEditTextRequestEndDate.setText("");
                                    Toast.makeText(CreateShoutActivity.this, "End Date should be greater then Start Date.", Toast.LENGTH_SHORT).show();
                                }
                                if (sendDate1.length() > 0) {
                                    objLinearDatePickerShorcutsPostRequest.setVisibility(LinearLayout.VISIBLE);
                                } else {
                                    objLinearDatePickerShorcutsPostRequest.setVisibility(LinearLayout.GONE);
                                }
                            }
                        }, objCalendar.get(Calendar.YEAR), objCalendar.get(Calendar.MONTH), objCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    } else {
                        Toast.makeText(CreateShoutActivity.this, "Please select start date first.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        objImageButtonRequestEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                System.out.println("IN REQUEST START DATE ");
                try {
                    if (objEditTextRequestStartDate.getText().length() > 0) {
                        if (objEditTextRequestEndDate.getText().toString().length() > 0) {
                            objCalendar = new GregorianCalendar();
                            String strFromDate = objEditTextRequestEndDate.getText().toString();
                            String dateSpliterFirst[] = strFromDate.split("/");
                            objCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSpliterFirst[0].toString()));
                            objCalendar.set(Calendar.MONTH, Integer.parseInt(dateSpliterFirst[1].toString()) - 1);
                            objCalendar.set(Calendar.YEAR, Integer.parseInt(dateSpliterFirst[2].toString()));
                        } else {
                            objCalendar = Calendar.getInstance();
                        }
                        new DatePickerDialog(CreateShoutActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                objCalendar.set(Calendar.YEAR, year);
                                objCalendar.set(Calendar.MONTH, monthOfYear);
                                objCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                                SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat("yyyy-MMMM-dd");
                                SimpleDateFormat sendDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String strDate1 = sdf.format(objCalendar.getTime());
                                String sendDate1 = sendDateFormat.format(objCalendar.getTime());
                                System.out.println("REQUEST START DATE :" + sendDate1);

                                if (isDateCorrect(objEditTextRequestStartDate.getText().toString(), sendDate1)) {
                                    objEditTextRequestEndDate.setText(sendDate1);
                                } else {
                                    objEditTextRequestEndDate.setText("");
                                    Toast.makeText(CreateShoutActivity.this, "End Date should be greater then Start Date.", Toast.LENGTH_SHORT).show();
                                }
                                if (sendDate1.length() > 0) {
                                    objLinearDatePickerShorcutsPostRequest.setVisibility(LinearLayout.VISIBLE);
                                } else {
                                    objLinearDatePickerShorcutsPostRequest.setVisibility(LinearLayout.GONE);
                                }
                            }
                        }, objCalendar.get(Calendar.YEAR), objCalendar.get(Calendar.MONTH), objCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    } else {
                        Toast.makeText(CreateShoutActivity.this, "Please select start date first.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private boolean isDateCorrect(String startDate, String endDate) {
        boolean dateCorrect = false;
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = myFormat.parse(startDate.toString());
            Date date2 = myFormat.parse(endDate.toString());
            long diff = date2.getTime() - date1.getTime();
            System.out.println("EDIT DAY COUNT : " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) > 0) {
                dateCorrect = true;
                resetDateSelector();
                return dateCorrect;
            } else {
                resetDateSelector();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateCorrect;
    }

    private void resetDateSelector() {
        // REQUEST
        btnWeekPostRequest.setBackgroundResource(R.drawable.deselect_date_background);
        btnMonthPostRequest.setBackgroundResource(R.drawable.deselect_date_background);
        btnYearPostRequest.setBackgroundResource(R.drawable.deselect_date_background);
        btnLifePostRequest.setBackgroundResource(R.drawable.deselect_date_background);

        btnWeekPostRequest.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
        btnMonthPostRequest.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
        btnYearPostRequest.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
        btnLifePostRequest.setTextColor(getResources().getColor(R.color.text_color_date_deselect));

    }

    private String ConvertDate(String strDateToConvert) {

        String strConvertedDate = "";
        try {
            SimpleDateFormat from = new SimpleDateFormat("dd MMM yyyy");
            SimpleDateFormat to = new SimpleDateFormat("dd/MM/yyyy");
            Date date = from.parse(strDateToConvert);
            System.out.println("CONVERTED DATE :" + to.format(date));
            strConvertedDate = to.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strConvertedDate;
    }

    @Override
    public void onClick(View v) {
        Intent objIntent;
        String newDate;
        LocationDetails objLocationDetails;
        SharedPreferences objLocationPreferences;
        switch (v.getId()) {

            case R.id.txt_current_location_post_request:
                hideKeyboard(v);
                if (Utils.isGPSEnabled(CreateShoutActivity.this)) {
                    objLocationDetails = new LocationDetails(CreateShoutActivity.this);
                    objLocationDetails.startTracking();
                    new DisplayAddress().execute(objTempLocationSharedPreferences.getString(Constants.LOCATION_LATITUDE, ""),
                            objTempLocationSharedPreferences.getString(Constants.LOCATION_LONGITUDE, ""), "");
                }
                break;
            case R.id.txt_home_location_post_request:
                hideKeyboard(v);
                new DisplayAddress().execute(objProfileSharedPreferences.getString(Constants.USER_REGISTERED_LATITUDE, ""),
                        objProfileSharedPreferences.getString(Constants.USER_REGISTERED_LONGITUDE, ""), "");
                break;
            case R.id.relative_category_post_request:
                hideKeyboard(objRelativeLayoutOpenCategoryPopupRequest);
                openCategoryPopupDialog();
                break;
            case R.id.btn_create_shout_back:
                hideKeyboard(v);
                if (showBackClickAlert) {
                    new AlertDialog.Builder(CreateShoutActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("")
                            .setMessage("Do you want to discard this shout?")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pushBackToShoutDefaultScreen();
                                }
                            })
                            .setNegativeButton("Stay", null)
                            .show();
                } else {
                    System.out.println("RESOURCE MODEL : " + arrGridViewResourceModel);
                    int intResourceCounter = 0;
                    for (int index = 0; index < arrGridViewResourceModel.size(); index++) {
                        GridViewResourceModel objGridViewResourceModel = arrGridViewResourceModel.get(index);
                        if (objGridViewResourceModel.getPATH() != null) {
                            intResourceCounter++;
                        }
                    }
                    System.out.println("RESOURCE COUNT : " + intResourceCounter);
                    if (intResourceCounter >= 1) {
                        new AlertDialog.Builder(CreateShoutActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("")
                                .setMessage("Do you want to discard this shout?")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        pushBackToShoutDefaultScreen();
                                    }
                                })
                                .setNegativeButton("Stay", null)
                                .show();
                    } else {
                        pushBackToShoutDefaultScreen();
                    }
                }
                break;


            case R.id.btn_give_shout:
                if (isRequestShown) {

                    if (objCheckPostRequestEngage.isChecked()) {
                        is_engage_with_neighbourhood = "Y";
                    } else {
                        is_engage_with_neighbourhood = "N";
                    }
                    int intCounter = 0;
                    for (int index = 0; index < arrCategoryModel.size(); index++) {
                        CategoryModel objCategoryModel = arrCategoryModel.get(index);
                        if (objCategoryModel.getFlag() == 0) {
                            intCounter = intCounter + 1;
                        }
                    }

                    arrResourceType = new ArrayList<String>();

                    for (int index = 0; index < arrGridViewResourceModel.size(); index++) {
                        GridViewResourceModel objGridViewResourceModel = arrGridViewResourceModel.get(index);
                        if (objGridViewResourceModel.getRESUORCE_TYPE().equals("C") || objGridViewResourceModel.getRESUORCE_TYPE().equals("V") || objGridViewResourceModel.getRESUORCE_TYPE().equals("D")) {
                            arrResourceType.add(objGridViewResourceModel.getRESUORCE_TYPE());
                        }
                    }

                    System.out.println("REQUEST : RESOURCE ARRAY :" + arrResourceType);
                    System.out.println("FINAL USER LATITUDE AND LONGITUDE : " + UserLatitude + " : " + UserLongitude);

                    if (objEditTextPostRequestTitle.getText().toString().length() > 0) {
                        if (objEditTextShoutDescriptionPostRequest.getText().toString().length() > 0) {
                            if (objLinearLayoutHideShowDateLayout.getVisibility() == LinearLayout.VISIBLE) {
                                if (!strDateType.equals("L")) {
                                    if (objEditTextRequestStartDate.getText().toString().length() > 0 && objEditTextRequestEndDate.getText().toString().length() > 0) {
                                        if (intCounter == arrCategoryModel.size()) {
                                            Toast.makeText(CreateShoutActivity.this, "Please select at least one category.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (UserLatitude.length() > 0 && UserLongitude.length() > 0) {
                                                new MakeShout().execute(objEditTextPostRequestTitle.getText().toString(),
                                                        objEditTextShoutDescriptionPostRequest.getText().toString(),
                                                        strDateType,
                                                        objEditTextRequestStartDate.getText().toString(),
                                                        objEditTextRequestEndDate.getText().toString(),
                                                        objEditTextRequestAddress.getText().toString(),
                                                        UserLatitude,
                                                        UserLongitude,
                                                        strShoutType,
                                                        is_engage_with_neighbourhood);
                                            } else {
                                                Toast.makeText(CreateShoutActivity.this, "Please select your location.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } else {
                                        Toast.makeText(CreateShoutActivity.this, "Please select date and try again.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    if (intCounter == arrCategoryModel.size()) {
                                        Toast.makeText(CreateShoutActivity.this, "Please select at least one category.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (UserLatitude.length() > 0 && UserLongitude.length() > 0) {
                                            new MakeShout().execute(objEditTextPostRequestTitle.getText().toString(),
                                                    objEditTextShoutDescriptionPostRequest.getText().toString(),
                                                    strDateType,
                                                    objEditTextRequestStartDate.getText().toString(),
                                                    objEditTextRequestEndDate.getText().toString(),
                                                    objEditTextRequestAddress.getText().toString(),
                                                    UserLatitude,
                                                    UserLongitude,
                                                    strShoutType,
                                                    is_engage_with_neighbourhood);
                                        } else {
                                            Toast.makeText(CreateShoutActivity.this, "Please select your location.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            } else {
                                strDateType = "false";
                                if (intCounter == arrCategoryModel.size()) {
                                    Toast.makeText(CreateShoutActivity.this, "Please select at least one category.", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (UserLatitude.length() > 0 && UserLongitude.length() > 0) {
                                        new MakeShout().execute(objEditTextPostRequestTitle.getText().toString(),
                                                objEditTextShoutDescriptionPostRequest.getText().toString(),
                                                strDateType,
                                                objEditTextRequestStartDate.getText().toString(),
                                                objEditTextRequestEndDate.getText().toString(),
                                                objEditTextRequestAddress.getText().toString(),
                                                UserLatitude,
                                                UserLongitude,
                                                strShoutType,
                                                is_engage_with_neighbourhood);
                                    } else {
                                        Toast.makeText(CreateShoutActivity.this, "Please select your location.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } else {
                            objEditTextShoutDescriptionPostRequest.requestFocus();
                            objEditTextShoutDescriptionPostRequest.setError("Enter shout discription");
                        }
                    } else {
                        objEditTextPostRequestTitle.requestFocus();
                        objEditTextPostRequestTitle.setError("Enter shout title");
                    }
                } else { // LISTING SECTION

                    if (objCheckPostRequestEngage.isChecked()) {
                        is_engage_with_neighbourhood = "Y";
                    } else {
                        is_engage_with_neighbourhood = "N";
                    }

                    int intCounter = 0;

                    for (int index = 0; index < arrCategoryModel.size(); index++) {
                        CategoryModel objCategoryModel = arrCategoryModel.get(index);
                        if (objCategoryModel.getFlag() == 0) {
                            intCounter = intCounter + 1;
                        }
                    }

                    arrResourceType = new ArrayList<String>();
                    for (int index = 0; index < arrGridViewResourceModel.size(); index++) {
                        GridViewResourceModel objGridViewResourceModel = arrGridViewResourceModel.get(index);
                        if (objGridViewResourceModel.getRESUORCE_TYPE().equals("C") || objGridViewResourceModel.getRESUORCE_TYPE().equals("V") || objGridViewResourceModel.getRESUORCE_TYPE().equals("D")) {
                            arrResourceType.add(objGridViewResourceModel.getRESUORCE_TYPE());
                        }
                    }
                    System.out.println("LISTING : RESOURCE ARRAY :" + arrResourceType);
                    System.out.println("FINAL USER LATITUDE AND LONGITUDE : " + UserLatitude + " : " + UserLongitude);

                    if (arrResourceType.size() > 0) {
                        if (objEditTextPostRequestTitle.getText().toString().length() > 0) {
                            if (objEditTextShoutDescriptionPostRequest.getText().toString().length() > 0) {
                                if (objLinearLayoutHideShowDateLayout.getVisibility() == LinearLayout.VISIBLE) {
                                    if (!strDateType.equals("L")) {
                                        if (objEditTextRequestStartDate.getText().toString().length() > 0 && objEditTextRequestEndDate.getText().toString().length() > 0) {

                                            if (intCounter == arrCategoryModel.size()) {
                                                Toast.makeText(CreateShoutActivity.this, "Please select at least one category.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (UserLatitude.length() > 0 && UserLongitude.length() > 0) {
                                                    new MakeShout().execute(objEditTextPostRequestTitle.getText().toString(),
                                                            objEditTextShoutDescriptionPostRequest.getText().toString(),
                                                            strDateType,
                                                            objEditTextRequestStartDate.getText().toString(),
                                                            objEditTextRequestEndDate.getText().toString(),
                                                            objEditTextRequestAddress.getText().toString(),
                                                            UserLatitude,
                                                            UserLongitude,
                                                            strShoutType,
                                                            is_engage_with_neighbourhood);
                                                } else {
                                                    Toast.makeText(CreateShoutActivity.this, "Please select your location.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(CreateShoutActivity.this, "Please select date and try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        if (intCounter == arrCategoryModel.size()) {
                                            Toast.makeText(CreateShoutActivity.this, "Please select at least one category.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (UserLatitude.length() > 0 && UserLongitude.length() > 0) {
                                                new MakeShout().execute(objEditTextPostRequestTitle.getText().toString(),
                                                        objEditTextShoutDescriptionPostRequest.getText().toString(),
                                                        strDateType,
                                                        objEditTextRequestStartDate.getText().toString(),
                                                        objEditTextRequestEndDate.getText().toString(),
                                                        objEditTextRequestAddress.getText().toString(),
                                                        UserLatitude,
                                                        UserLongitude,
                                                        strShoutType,
                                                        is_engage_with_neighbourhood);
                                            } else {
                                                Toast.makeText(CreateShoutActivity.this, "Please select your location.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                } else {
                                    strDateType = "false";
                                    if (intCounter == arrCategoryModel.size()) {
                                        Toast.makeText(CreateShoutActivity.this, "Please select at least one category.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (UserLatitude.length() > 0 && UserLongitude.length() > 0) {
                                            new MakeShout().execute(objEditTextPostRequestTitle.getText().toString(),
                                                    objEditTextShoutDescriptionPostRequest.getText().toString(),
                                                    strDateType,
                                                    objEditTextRequestStartDate.getText().toString(),
                                                    objEditTextRequestEndDate.getText().toString(),
                                                    objEditTextRequestAddress.getText().toString(),
                                                    UserLatitude,
                                                    UserLongitude,
                                                    strShoutType,
                                                    is_engage_with_neighbourhood);
                                        } else {
                                            Toast.makeText(CreateShoutActivity.this, "Please select your location.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            } else {
                                objEditTextShoutDescriptionPostRequest.setError("");
                            }
                        } else {
                            objEditTextPostRequestTitle.setError("");
                        }
                    } else {
                        Toast.makeText(CreateShoutActivity.this, "Please upload at lease one media file.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_create_shout_post_request:
                hideKeyboard(v);
                strShoutType = "R";
                btnPostRequest.setBackgroundColor(getResources().getColor(R.color.red_background_color));
                btnPostRequest.setTextColor(getResources().getColor(R.color.white_text_color));
                btnPostListing.setBackgroundColor(Color.parseColor("#EFEFEF"));
                btnPostListing.setTextColor(Color.parseColor("#737373"));
                btnPostRequest.setText("I NEED HELP");
                objTextViewPostRequestEngageNeighbourhood.setText("Request People in your Neighbourhood");
                /*objTextViewPostRequestSelectedCategoryTitle.setText("");
                objTextViewPostListingSelectedCategoryTitle.setText("");*/
                isRequestShown = true;
                break;
            case R.id.btn_create_shout_post_listing:
                hideKeyboard(v);
                strShoutType = "L";
                btnPostRequest.setBackgroundColor(getResources().getColor(R.color.red_background_color));
                btnPostRequest.setTextColor(getResources().getColor(R.color.white_text_color));
                btnPostRequest.setBackgroundColor(Color.parseColor("#EFEFEF"));
                btnPostRequest.setTextColor(Color.parseColor("#737373"));
                btnPostListing.setText("I WANT TO GIVE");
                objTextViewPostRequestEngageNeighbourhood.setText("Share with People in your Neighbourhood");
                btnPostListing.setBackgroundColor(getResources().getColor(R.color.red_background_color));
                btnPostListing.setTextColor(getResources().getColor(R.color.white_text_color));
                isRequestShown = false;
                break;
            case R.id.imageView_post_request_location:

                hideKeyboard(v);
                if (Utils.checkGooglePlayServicesAvailable(CreateShoutActivity.this)) {
                    Utils.d("debug", "CLIKED imageView_post_request_location ");
                    //startService(new Intent(CreateShoutActivity.this, LocationBackGroundService.class));
                    isRequestShown = true;
                    showMap(true);
                } else {
                    Utils.showGPSDisabledAlertToUser(CreateShoutActivity.this);
                }
                break;
            // POST REQUEST CLICK EVENTS
            case R.id.btn_week_post_request:
                hideKeyboard(v);
                setBackgroundForDates(R.id.btn_week_post_request);
                if (objEditTextRequestStartDate.getText().toString().length() == 0) {
                    objCalendar = Calendar.getInstance();
                    SimpleDateFormat sendDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    objEditTextRequestStartDate.setText(sendDateFormat.format(objCalendar.getTime()));
                }
                objEditTextRequestEndDate.setText(getNextDate(objEditTextRequestStartDate.getText().toString(), 7));
                System.out.println("WEEK DATE  : " + getNextDate(objEditTextRequestStartDate.getText().toString(), 7));
                strDateType = "W";
                break;
            case R.id.btn_month_post_request:
                hideKeyboard(v);
                setBackgroundForDates(R.id.btn_month_post_request);
                if (objEditTextRequestStartDate.getText().toString().length() == 0) {
                    objCalendar = Calendar.getInstance();
                    SimpleDateFormat sendDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    objEditTextRequestStartDate.setText(sendDateFormat.format(objCalendar.getTime()));
                }
                objEditTextRequestEndDate.setText(getNextDate(objEditTextRequestStartDate.getText().toString(), 30));
                System.out.println("MONTH DATE  : " + getNextDate(objEditTextRequestStartDate.getText().toString(), 30));
                strDateType = "M";
                break;
            case R.id.btn_year_post_request:
                hideKeyboard(v);
                setBackgroundForDates(R.id.btn_year_post_request);
                if (objEditTextRequestStartDate.getText().toString().length() == 0) {
                    objCalendar = Calendar.getInstance();
                    SimpleDateFormat sendDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    objEditTextRequestStartDate.setText(sendDateFormat.format(objCalendar.getTime()));
                }
                objEditTextRequestEndDate.setText(getNextYearDate(objEditTextRequestStartDate.getText().toString()));
                strDateType = "Y";
                break;
            case R.id.btn_life_post_request:
                hideKeyboard(v);
                setBackgroundForDates(R.id.btn_life_post_request);
                /*if (objEditTextRequestStartDate.getText().toString().length() == 0) {
                    objCalendar = Calendar.getInstance();
                    SimpleDateFormat sendDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    objEditTextRequestStartDate.setText(sendDateFormat.format(objCalendar.getTime()));
                }*/
                strDateType = "L";
                break;
            default:
                break;
        }
    }

    public void setBackgroundForDates(int intResourceId) {
        switch (intResourceId) {
            // REQUEST
            case R.id.btn_week_post_request:
                btnWeekPostRequest.setBackgroundResource(R.drawable.select_date_background);
                btnMonthPostRequest.setBackgroundResource(R.drawable.deselect_date_background);
                btnYearPostRequest.setBackgroundResource(R.drawable.deselect_date_background);
                btnLifePostRequest.setBackgroundResource(R.drawable.deselect_date_background);

                btnWeekPostRequest.setTextColor(getResources().getColor(R.color.text_color_date_select));
                btnMonthPostRequest.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnYearPostRequest.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnLifePostRequest.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                break;
            case R.id.btn_month_post_request:
                btnWeekPostRequest.setBackgroundResource(R.drawable.deselect_date_background);
                btnMonthPostRequest.setBackgroundResource(R.drawable.select_date_background);
                btnYearPostRequest.setBackgroundResource(R.drawable.deselect_date_background);
                btnLifePostRequest.setBackgroundResource(R.drawable.deselect_date_background);

                btnWeekPostRequest.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnMonthPostRequest.setTextColor(getResources().getColor(R.color.text_color_date_select));
                btnYearPostRequest.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnLifePostRequest.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                break;
            case R.id.btn_year_post_request:
                btnWeekPostRequest.setBackgroundResource(R.drawable.deselect_date_background);
                btnMonthPostRequest.setBackgroundResource(R.drawable.deselect_date_background);
                btnYearPostRequest.setBackgroundResource(R.drawable.select_date_background);
                btnLifePostRequest.setBackgroundResource(R.drawable.deselect_date_background);

                btnWeekPostRequest.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnMonthPostRequest.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnYearPostRequest.setTextColor(getResources().getColor(R.color.text_color_date_select));
                btnLifePostRequest.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                break;
            case R.id.btn_life_post_request:
                btnWeekPostRequest.setBackgroundResource(R.drawable.deselect_date_background);
                btnMonthPostRequest.setBackgroundResource(R.drawable.deselect_date_background);
                btnYearPostRequest.setBackgroundResource(R.drawable.deselect_date_background);
                btnLifePostRequest.setBackgroundResource(R.drawable.select_date_background);

                btnWeekPostRequest.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnMonthPostRequest.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnYearPostRequest.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnLifePostRequest.setTextColor(getResources().getColor(R.color.text_color_date_select));
                break;
        }
    }

    private void openCategoryPopupDialog() {
        objPopupWindowCategories.showAtLocation(customCategoryAlertLayout, Gravity.CENTER, 0, 0);
       /* objRelativeCategoryPopupOutsideTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objPopupWindowCategories.dismiss();
            }
        });*/

        btnOkCategoryPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int index = 0; index < arrCategoryModel.size(); index++) {
                    CategoryModel objCategoryModelTemp = arrCategoryModel.get(index);
                    if (objCategoryModelTemp.getFlag() == 1) {
                        objTextViewPostRequestSelectedCategoryTitle.setText(objCategoryModelTemp.getTitle());
                    }
                }
                objPopupWindowCategories.dismiss();
            }
        });

        btnCancelCategoryPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*for (int index = 0; index < arrCategoryModel.size(); index++) {
                    CategoryModel objCategoryModelTemp = arrCategoryModel.get(index);
                    if (objCategoryModelTemp.getFlag() == 1) {
                        objCategoryModelTemp.setFlag(0);
                    }
                }
                objTextViewPostRequestSelectedCategoryTitle.setText("");
                objTextViewPostListingSelectedCategoryTitle.setText("");
                objCategoryAdapter.notifyDataSetChanged();*/
                objPopupWindowCategories.dismiss();
            }
        });
    }

    private void pushBackToShoutDefaultScreen() {

        imm.hideSoftInputFromWindow(objEditTextPostRequestTitle.getWindowToken(), 0);

        /*Intent objIntent = new Intent(CreateShoutActivity.this, ShoutDefaultActivity.class);
        startActivity(objIntent);
        objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
        finish();*/
        super.onBackPressed();
    }

    public String getNextDate(String date, int numberOfDays) {
        objCalendar = new GregorianCalendar();
        String dateSpliterFirst[] = date.split("/");
        objCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSpliterFirst[0].toString()));
        objCalendar.set(Calendar.MONTH, Integer.parseInt(dateSpliterFirst[1].toString()) - 1);
        objCalendar.set(Calendar.YEAR, Integer.parseInt(dateSpliterFirst[2].toString()));
        objCalendar.add(Calendar.DATE, numberOfDays);
        SimpleDateFormat sendDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = sendDateFormat.format(objCalendar.getTime());
        return strDate;
    }

    public String getNextYearDate(String date) {
        objCalendar = new GregorianCalendar();
        String dateSpliterFirst[] = date.split("/");

        int year = Integer.parseInt(dateSpliterFirst[2].toString());

        objCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSpliterFirst[0].toString()));
        objCalendar.set(Calendar.MONTH, Integer.parseInt(dateSpliterFirst[1].toString()) - 1);
        objCalendar.set(Calendar.YEAR, year + 1);
        objCalendar.add(Calendar.DATE, -1);
        SimpleDateFormat sendDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = sendDateFormat.format(objCalendar.getTime());
        return strDate;
    }

    private void showMap(boolean status) {
        if (status)
            relativeLayoutMap.setVisibility(View.VISIBLE);
        else
            relativeLayoutMap.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // After camera screen this code will excuted
        if (requestCode == Constants.REQUEST_MADE_FOR_VIDEO) {
            try {
                if (resultCode == RESULT_OK) {
                    System.out.println("VIDEO SAVED TO PATH : " + data.getData());
                    videoUri = null;
                    videoUri = data.getData();
                    Bitmap bmThumbnail;
                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(videoUri.getPath(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                    System.out.println("THUMBNAIL PATH : " + ApplicationUtils.saveVideoThumbnail(bmThumbnail));
                    setResourceToView(ApplicationUtils.saveVideoThumbnail(bmThumbnail), "V", data.getData());
                } else if (resultCode == RESULT_CANCELED) {
                    // USER CANCELLED
                } else {
                    // FAILED TO CAPTURE VIDEO
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == Constants.REQUEST_MADE_FOR_CAMERA) {
            try {
                Uri uri = null;
                ImageCompressionAsyncTask imageCompressionAsyncTaskCamera = new ImageCompressionAsyncTask() {
                    @Override
                    protected void onPostExecute(final byte[] imageBytes) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    setResourceToView(applicationUtils.saveImageOnSdCard(imageBytes), "C", null);
                                }catch(NullPointerException ne){
                                    ne.printStackTrace();
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                };
                imageCompressionAsyncTaskCamera.execute(applicationUtils.getRealPathFromURI(mCapturedImageURI));// imagePath as a string
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == Constants.REQUEST_MADE_FOR_GALLERY) {
            try {
                final Uri uri = data.getData();
                ImageCompressionAsyncTask imageCompressionAsyncTaskCamera = new ImageCompressionAsyncTask() {
                    @Override
                    protected void onPostExecute(final byte[] imageBytes) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    setResourceToView(applicationUtils.saveImageOnSdCard(imageBytes), "C", null);
                                }catch(NullPointerException ne){
                                    ne.printStackTrace();
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                };
                imageCompressionAsyncTaskCamera.execute(applicationUtils.getRealPathFromURI(uri));// imagePath as a string
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            try{
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    objTextViewMapAddress.setText(place.getAddress());
                    objGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.552834f));

                    SharedPreferences.Editor objEditor = objTempLocationSharedPreferences.edit();
                    objEditor.putString(Constants.LOCATION_LATITUDE, String.valueOf(place.getLatLng().latitude));
                    objEditor.putString(Constants.LOCATION_LONGITUDE, String.valueOf(place.getLatLng().longitude));
                    objEditor.commit();
                    System.out.println("LATLNG UPDATED : " + objTempLocationSharedPreferences.getString(Constants.LOCATION_LATITUDE, "") + " : " + objTempLocationSharedPreferences.getString(Constants.LOCATION_LONGITUDE, ""));
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
            }catch(NullPointerException ne){
                ne.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void setResourceToView(Uri imageUri, String type, Uri videoUri) {

        System.out.println("PRASANNA PRINT : URI : " + imageUri + " TYPE : " + type);

        GridViewResourceModel objGridViewResourceModelObject = arrGridViewResourceModel.get(ResourcePosition);
        objGridViewResourceModelObject.setPATH(imageUri);
        objGridViewResourceModelObject.setRESUORCE_TYPE(type);
        objGridViewResourceModelObject.setIS_ADD_BUTTON(false);
        objGridViewResourceModelObject.setIS_VISIBLE(true);
        if (type.equals("C")) {
            objGridViewResourceModelObject.setVIDEO_PATH(null);
        } else if (type.equals("V")) {
            objGridViewResourceModelObject.setVIDEO_PATH(videoUri);
        }
        objGridViewResourceAdapter.notifyDataSetChanged();
        for (int index = 0; index < arrGridViewResourceModel.size(); index++) {
            GridViewResourceModel objGridViewResourceModelInner = arrGridViewResourceModel.get(index);
            if (objGridViewResourceModelInner.getPATH() == null) {
                objGridViewResourceModelInner.setPATH(null);
                objGridViewResourceModelInner.setRESUORCE_TYPE("");
                objGridViewResourceModelInner.setIS_ADD_BUTTON(true);
                objGridViewResourceModelInner.setIS_VISIBLE(true);
                objGridViewResourceModelInner.setVIDEO_PATH(null);
                objGridViewResourceAdapter.notifyDataSetChanged();
                break;
            }
        }
        for (int index = 0; index < arrGridViewResourceModel.size(); index++) {
            GridViewResourceModel objGridViewResourceModel = arrGridViewResourceModel.get(index);
            System.out.println("POSITION : " + index + " PATH : " + objGridViewResourceModel.getPATH() + " TYPE : " + objGridViewResourceModel.getRESUORCE_TYPE() + " IS ADD : " + objGridViewResourceModel.getIS_ADD_BUTTON() + " CANCEL BUTTON : " + objGridViewResourceModel.getIS_VISIBLE() + " VIDEO PATH : " + objGridViewResourceModel.getVIDEO_PATH());
        }
    }

    private boolean checkUriExists(HashMap hashMap, Uri uri) {
        if (hashMap.containsValue(uri))
            return true;
        else
            return false;
    }

    @Override
    public void onBackPressed() {
       /* new AlertDialog.Builder(CreateShoutActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
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
                .show();*/

        if (relativeLayoutMap.getVisibility() == RelativeLayout.VISIBLE) {
            /*SharedPreferences.Editor objEditor = objTempLocationSharedPreferences.edit();
            objEditor.putString(Constants.LOCATION_LATITUDE, UserLatitude);
            objEditor.putString(Constants.LOCATION_LONGITUDE, UserLongitude);
            objEditor.commit();*/
            relativeLayoutMap.setVisibility(RelativeLayout.GONE);
        } else {
            if (showBackClickAlert) {
                new AlertDialog.Builder(CreateShoutActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("")
                        .setMessage("Do you want to discard this shout?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pushBackToShoutDefaultScreen();
                            }
                        })
                        .setNegativeButton("Stay", null)
                        .show();
            } else {
                System.out.println("RESOURCE MODEL : " + arrGridViewResourceModel);
                int intResourceCounter = 0;
                for (int index = 0; index < arrGridViewResourceModel.size(); index++) {
                    GridViewResourceModel objGridViewResourceModel = arrGridViewResourceModel.get(index);
                    if (objGridViewResourceModel.getPATH() != null) {
                        intResourceCounter++;
                    }
                }
                System.out.println("RESOURCE COUNT : " + intResourceCounter);
                if (intResourceCounter >= 1) {
                    new AlertDialog.Builder(CreateShoutActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("")
                            .setMessage("Do you want to discard this shout?")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pushBackToShoutDefaultScreen();
                                }
                            })
                            .setNegativeButton("Stay", null)
                            .show();
                } else {
                    pushBackToShoutDefaultScreen();
                }
            }
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        objGoogleMap = googleMap;
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            } else {

                googleMap.clear();
                System.out.println("LOCATION_TAG : DEFAULT LOCATION ");
                if (objTempLocationSharedPreferences.getString(Constants.LOCATION_LATITUDE, "").length() > 0) {
                    double lat = Double.parseDouble(objTempLocationSharedPreferences.getString(Constants.LOCATION_LATITUDE, ""));
                    double lng = Double.parseDouble(objTempLocationSharedPreferences.getString(Constants.LOCATION_LONGITUDE, ""));
                    latLong = new LatLng(lat, lng);
                } else {
                    double lat = 0.0000;
                    double lng = 0.0000;
                    latLong = new LatLng(lat, lng);
                }
                objCameraUpdate = CameraUpdateFactory.newLatLng(latLong);
                zoom = CameraUpdateFactory.zoomTo(15.552834f);
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 15.552834f));
            googleMap.setMyLocationEnabled(true);
            googleMap.clear();
            googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                @Override
                public void onCameraChange(CameraPosition arg0) {
                    center = googleMap.getCameraPosition().target;
                    googleMap.clear();
                    try {
                        UserLatitude = String.valueOf(center.latitude);
                        UserLongitude = String.valueOf(center.longitude);
                        new GetLocationAsync(center.latitude, center.longitude).execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getGoogleMapThumbnail(double lati, double longi) {
        String url = "http://maps.google.com/maps/api/staticmap?center=" + lati + "," + longi + "&zoom=15&size=500x200&markers=" + lati + "," + longi + "&sensor=false";
        return url;
    }

    private void refreshCategoryList() {
        for (int index = 0; index < arrCategoryModel.size(); index++) {
            CategoryModel objCategoryModel = arrCategoryModel.get(index);
            objCategoryModel.setFlag(0);
        }
        objCategoryAdapter = new CategoryAdapter(CreateShoutActivity.this, arrCategoryModel);
        objCategoryGridView.setAdapter(objCategoryAdapter);
        objCategoryGridView.setBackgroundColor(getResources().getColor(R.color.white_text_color));

    }

    private class DisplayAddress extends AsyncTask<String, Void, String> {
        double x = 0.0, y = 0.0;
        String zoomLevel = "";
        StringBuilder str;

        @Override
        protected String doInBackground(String... params) {

            System.out.println("MY CURRENT LATITUDE : " + params[0]);
            System.out.println("MY CURRENT LONGITUDE : " + params[1]);
            if (params[0].length() > 0 && params[1].length() > 0) {
                x = Double.parseDouble(params[0]);
                y = Double.parseDouble(params[1]);
                zoomLevel = params[2];
            }

            try {
                addresses = null;
                geocoder = new Geocoder(CreateShoutActivity.this, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                if (geocoder.isPresent()) {
                    if (addresses.size() > 0) {
                        String localityString = addresses.get(0).getLocality();
                        String city = addresses.get(0).getCountryName();
                        String region_code = addresses.get(0).getCountryCode();
                        String zipcode = addresses.get(0).getPostalCode();

                        str.append(localityString + "");
                        str.append(city + "" + region_code + "");
                        str.append(zipcode + "");
                    }
                } else {
                    CreateShoutActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isRequestShown) {
                                objEditTextRequestAddress.setText("No Address found");
                            } else {
                                objEditTextRequestAddress.setText("No Address found");
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (addresses != null && addresses.size() > 0) {
                    objEditTextRequestAddress.setText(addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1) + " ");
                    objEditTextRequestAddress.setText(addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1) + " ");
                }
                String strStaticMapUrl = getGoogleMapThumbnail(x, y);
                System.out.println("STATIC MAP URL : " + strStaticMapUrl);
                Picasso.with(CreateShoutActivity.this).load(strStaticMapUrl).into(imageViewPostRequestLocation);
                LatLng objLatLong = new LatLng(x, y);
                objCameraUpdate = CameraUpdateFactory.newLatLng(objLatLong);
                if (zoomLevel.length() > 0) {
                    objGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(objLatLong, Float.parseFloat(zoomLevel)));
                } else {
                    objGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(objLatLong, 15.552834f));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private class GetLocationAsync extends AsyncTask<String, Void, String> {
        // boolean duplicateResponse;
        double x, y;
        StringBuilder str;

        public GetLocationAsync(double latitude, double longitude) {
            // TODO Auto-generated constructor stub
            x = latitude;
            y = longitude;
        }

        @Override
        protected void onPreExecute() {
            objTextViewMapAddress.setText(" Getting location ");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                addresses = null;
                geocoder = new Geocoder(CreateShoutActivity.this, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                if (geocoder.isPresent()) {
                    if (addresses.size() > 0) {
                        String localityString = addresses.get(0).getLocality();
                        String city = addresses.get(0).getCountryName();
                        String region_code = addresses.get(0).getCountryCode();
                        String zipcode = addresses.get(0).getPostalCode();

                        str.append(localityString + "");
                        str.append(city + "" + region_code + "");
                        str.append(zipcode + "");
                    }
                } else {
                    CreateShoutActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            objTextViewMapAddress.setText("No Address found");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (addresses != null && addresses.size() > 0) {
                    objTextViewMapAddress.setText(addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1) + " ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class LoadCategoryApi extends AsyncTask<Void, Void, String> {


        String strResult = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objCategoryGridView.setBackgroundColor(getResources().getColor(R.color.transparent));
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                strResult = NetworkUtils.postData(Constants.CATEGORY_LIST_API, "");
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
                if (s.length() > 0) {
                    JSONObject objJsonObject = new JSONObject(s);
                    String result = objJsonObject.getString("result");
                    arrCategoryModel.clear();
                    if (result.equals("true")) {

                        JSONArray objJsonArray = new JSONArray(objJsonObject.getString("categories"));

                        for (int index = 0; index < objJsonArray.length(); index++) {
                            CategoryModel objCategoryModel = new CategoryModel(
                                    objJsonArray.getJSONObject(index).getString("id"),
                                    objJsonArray.getJSONObject(index).getString("title"),
                                    Constants.HTTP_URL + objJsonArray.getJSONObject(index).getString("image"),
                                    Constants.HTTP_URL + objJsonArray.getJSONObject(index).getString("selected_image"),
                                    objJsonArray.getJSONObject(index).getString("created"),
                                    0
                            );
                            arrCategoryModel.add(objCategoryModel);
                        }
                        refreshCategoryList();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class MakeShout extends AsyncTask<String, Void, String> {

        final ProgressDialog objProgressDialog = new ProgressDialog(CreateShoutActivity.this);
        String strResult = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objProgressDialog.setTitle("Loading...");
            objProgressDialog.show();
            objProgressDialog.setCancelable(false);
            objProgressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String strCategoryId = "";
                for (int index = 0; index < arrCategoryModel.size(); index++) {
                    CategoryModel objCategoryModel = arrCategoryModel.get(index);
                    if (objCategoryModel.getFlag() == 1) {
                        strCategoryId = objCategoryModel.getId();
                    }
                }
                System.out.println("SELECTED CATEGORY JSON ARRAY : " + strCategoryId);

                JSONObject objJsonObject = new JSONObject();

                objJsonObject.put("user_id", objProfileSharedPreferences.getString(Constants.USER_ID, ""));
                objJsonObject.put("title", params[0]);
                objJsonObject.put("description", params[1]);
                objJsonObject.put("date_type", params[2]);
                objJsonObject.put("start_date", params[3]);
                objJsonObject.put("end_date", params[4]);
                objJsonObject.put("address", params[5]);
                objJsonObject.put("latitude", params[6]);
                objJsonObject.put("longitude", params[7]);
                objJsonObject.put("shout_type", params[8]);
                objJsonObject.put("is_searchable", params[9]);
                objJsonObject.put("category_id", strCategoryId);
                objJsonObject.put("op", "NEW");

                System.out.println("CREATE SHOUT INPUT JSON OBJECT : " + objJsonObject.toString());

                strResult = NetworkUtils.postData(Constants.CREATE_SHOUT, objJsonObject.toString());

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
                if (objProgressDialog.isShowing()) {
                    objProgressDialog.dismiss();
                }
                final JSONObject objJsonObject = new JSONObject(s);
                if (objJsonObject.getString("result").equals("true")) {
                    arrResourceType = new ArrayList<String>();
                    for (int index = 0; index < arrGridViewResourceModel.size(); index++) {
                        GridViewResourceModel objGridViewResourceModel = arrGridViewResourceModel.get(index);
                        arrResourceType.add(objGridViewResourceModel.getRESUORCE_TYPE());
                    }
                    System.out.println("TEST REQUEST RESOURCE TYPE : " + arrResourceType);
                    if (arrResourceType.size() > 0) {
                        new UploadResources().execute(objJsonObject.getString("shout_id"));
                    } else {
                        objDatabaseHelper.deleteShoutEntries();
                        Intent objIntent = new Intent(CreateShoutActivity.this, ShoutDefaultActivity.class);
                        objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(objIntent);
                        finish();
                        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                    }
                } else {
                    Toast.makeText(CreateShoutActivity.this, "Failed to create shout.Please try again.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class UploadResources extends AsyncTask<String, Void, String> {

        final ProgressDialog objProgressDialog = new ProgressDialog(CreateShoutActivity.this);
        String strResult = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objProgressDialog.setTitle("Loading...");
            objProgressDialog.show();
            objProgressDialog.setCancelable(false);
            objProgressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<Uri> arrResourcePath = new ArrayList<Uri>();

            if (isRequestShown) {
                arrResourceType = new ArrayList<String>();
                for (int index = 0; index < arrGridViewResourceModel.size(); index++) {
                    GridViewResourceModel objGridViewResourceModel = arrGridViewResourceModel.get(index);
                    if (objGridViewResourceModel.getPATH() != null) {
                        if (objGridViewResourceModel.getRESUORCE_TYPE().equals("C")) {
                            arrResourceType.add("C");
                            arrResourcePath.add(objGridViewResourceModel.getPATH());
                        } else if (objGridViewResourceModel.getRESUORCE_TYPE().equals("V")) {
                            objDatabaseHelper.addToBackgroundTask(params[0], String.valueOf(objGridViewResourceModel.getPATH()), String.valueOf(objGridViewResourceModel.getVIDEO_PATH()));
                        }
                    }
                }

                System.out.println("REQUEST RESOURCE TYPE : " + arrResourceType);
                System.out.println("REQUEST RESOURCE PATH : " + arrResourcePath);

                try {

                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(Constants.UPLOAD_RESOURCES);
                    System.out.println("UPLOAD RESOURCE URL : " + Constants.UPLOAD_RESOURCES);
                    HttpResponse response = null;
                    MultipartEntity entityBuilder = new MultipartEntity();

                    entityBuilder.addPart("shout_id", new StringBody(params[0]));
                    for (int index = 0; index < arrResourceType.size(); index++) {
                        if (arrResourceType.get(index).equals("C")) {
                            File file = new File(arrResourcePath.get(index).getPath());
                            FileBody objFile = new FileBody(file);
                            entityBuilder.addPart("Image-" + index, objFile);
                        }
                        /*else if (arrResourceType.get(index).equals("V")) {
                            File thumbnailFile = new File(arrResourcePath.get(index).getPath());
                            FileBody objThumbnailFileBody = new FileBody(thumbnailFile);
                            entityBuilder.addPart("Video-" + index, objThumbnailFileBody);
                        } else if (arrResourceType.get(index).equals("D")) {
                            File file = new File(arrResourcePath.get(index).getPath());
                            FileBody objFile = new FileBody(file);
                            entityBuilder.addPart("File-" + index, objFile);
                        }*/
                    }
                    post.setEntity(entityBuilder);
                    response = client.execute(post);
                    HttpEntity httpEntity = response.getEntity();
                    strResult = EntityUtils.toString(httpEntity);
                    Utils.d("MULTIPART RESPONSE", strResult);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                } catch (ClientProtocolException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                /*for (int index = 0; index < arrGridViewResourceModel.size(); index++) {
                    GridViewResourceModel objGridViewResourceModel = arrGridViewResourceModel.get(index);
                    if (objGridViewResourceModel.getPATH() != null) {
                        arrResourcePath.add(objGridViewResourceModel.getPATH());
                    }
                }*/

                arrResourceType = new ArrayList<String>();

                for (int index = 0; index < arrGridViewResourceModel.size(); index++) {
                    GridViewResourceModel objGridViewResourceModel = arrGridViewResourceModel.get(index);
                    if (objGridViewResourceModel.getPATH() != null) {
                        if (objGridViewResourceModel.getRESUORCE_TYPE().equals("C")) {
                            arrResourceType.add("C");
                            arrResourcePath.add(objGridViewResourceModel.getPATH());
                        } else if (objGridViewResourceModel.getRESUORCE_TYPE().equals("V")) {
                            /*arrResourcePath.add(objGridViewResourceModel.getPATH());
                            arrResourceType.add("V");*/
                            /*arrResourcePath.add(objGridViewResourceModel.getVIDEO_PATH());
                            arrResourceType.add("V");*/
                            objDatabaseHelper.addToBackgroundTask(params[0], String.valueOf(objGridViewResourceModel.getPATH()), String.valueOf(objGridViewResourceModel.getVIDEO_PATH()));
                        }
                    }
                }

                System.out.println("LISTING RESOURCE TYPE : " + arrResourceType);
                System.out.println("LISTING RESOURCE PATH : " + arrResourcePath);
//                NetworkUtils.uploadResources(CreateShoutActivity.this, Constants.UPLOAD_RESOURCES, params[0], arrResourceType, arrResourcePath);

                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(Constants.UPLOAD_RESOURCES);
                    HttpResponse response = null;
                    MultipartEntity entityBuilder = new MultipartEntity();

                    entityBuilder.addPart("shout_id", new StringBody(params[0]));

                    for (int index = 0; index < arrResourceType.size(); index++) {

                        /*File file = new File(arrResourcePath.get(index).getPath());
                        FileBody objFile = new FileBody(file);
                        if (arrResourceType.get(index).equals("C")) {
                            entityBuilder.addPart("Image-" + index, objFile);
                        } else if (arrResourceType.get(index).equals("V")) {
                            entityBuilder.addPart("Video-" + index, objFile);
                        } else if (arrResourceType.get(index).equals("D")) {
                            entityBuilder.addPart("File-" + index, objFile);
                        }*/

                        if (arrResourceType.get(index).equals("C")) {
                            File file = new File(arrResourcePath.get(index).getPath());
                            FileBody objFile = new FileBody(file);
                            entityBuilder.addPart("Image-" + index, objFile);
                        }
                        /*else if (arrResourceType.get(index).equals("V")) {
                            File thumbnailFile = new File(arrResourcePath.get(index).getPath());
                            FileBody objThumbnailFileBody = new FileBody(thumbnailFile);
                            entityBuilder.addPart("Video-" + index, objThumbnailFileBody);
                        } else if (arrResourceType.get(index).equals("D")) {
                            File file = new File(arrResourcePath.get(index).getPath());
                            FileBody objFile = new FileBody(file);
                            entityBuilder.addPart("File-" + index, objFile);
                        }*/
                    }
                    post.setEntity(entityBuilder);
                    response = client.execute(post);
                    HttpEntity httpEntity = response.getEntity();
                    strResult = EntityUtils.toString(httpEntity);
                    Utils.d("MULTIPART RESPONSE", strResult);

                    return strResult;

                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                } catch (ClientProtocolException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return strResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (objProgressDialog.isShowing()) {
                    objProgressDialog.dismiss();
                }
                JSONObject objJsonObject = new JSONObject(s);
                if (objJsonObject.getString("result").toString().equals("true")) {

                    /*if(isMyServiceRunning(UploadResourceService.class)==false){
                        startService(new Intent(CreateShoutActivity.this,UploadResourceService.class));
                    }*/
                    startService(new Intent(CreateShoutActivity.this, UploadResourceService.class));
                    objDatabaseHelper.deleteShoutEntries();
                    Intent objIntent = new Intent(CreateShoutActivity.this, ShoutDefaultActivity.class);
                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(objIntent);
                    finish();
                    overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public abstract class ImageCompressionAsyncTask extends AsyncTask<String, Void, byte[]> {
        @Override
        protected byte[] doInBackground(String... strings) {
            if (strings.length == 0 || strings[0] == null)
                return null;
            return ImageUtils.compressImage(strings[0]);
        }

        protected abstract void onPostExecute(byte[] imageBytes);
    }
}