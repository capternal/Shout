package com.shout.shoutapplication.main.EditShout;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.os.Build;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.shout.shoutapplication.Utils.RealPathUtil;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.base.BaseActivity;
import com.shout.shoutapplication.database.DatabaseHelper;
import com.shout.shoutapplication.main.EditShout.Adapter.EditCategoryAdapter;
import com.shout.shoutapplication.main.EditShout.Adapter.EditGridViewResourceAdapter;
import com.shout.shoutapplication.main.EditShout.Model.EditCategoryModel;
import com.shout.shoutapplication.main.EditShout.Model.EditGridViewResourceModel;
import com.shout.shoutapplication.main.EditShout.Model.OriginalDataModel;
import com.shout.shoutapplication.main.Model.ShoutDefaultListModel;
import com.shout.shoutapplication.main.ShoutDefaultActivity;
import com.shout.shoutapplication.main.ShoutDetailActivity;
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

public class EditShoutActivity extends BaseActivity implements OnMapReadyCallback {

    // COMPONENTS
    private Button btnShoutTypeRequest;
    private Button btnShoutTypeListing;
    private GridView objGridViewResources;
    private EditText edtShoutTitle;
    private EditText edtShoutDescription;
    private CheckBox chkSpecifyDate;
    private LinearLayout objLinearLayoutDateLayout;
    private EditText edtStartDate;
    private ImageButton imgbtnStartDate;
    private EditText edtEndDate;
    private ImageButton imgbtnEndDate;
    private Button btnWeek;
    private Button btnMonth;
    private Button btnYear;
    private Button btnForLife;
    private TextView objTextViewEngageText;
    private CheckBox chkEngage;
    private RelativeLayout objRelativeLayoutCategory;
    private TextView txtSelectedCategoryName;
    private EditText edtShoutAddress;
    private TextView txtCurrentLocation;
    private TextView txtHomeLocation;
    private ImageView imgStaticMap;
    private TextView txtBack;
    private TextView txtUpdateShout;


    // INTENT DATA
    private String strShoutId = "";
    private String strShoutTitle = "";
    private String strShoutDescription = "";
    private String strShoutType = "";
    private String strShoutSpecifyDate = "";
    private String strIsSearchable = "";
    private String strLatitude = "";
    private String strLongitude = "";
    private String strAddress = "";
    private String strCategoryName = "";
    private String strCategoryId = "";

    // LOCAL VARIABLES
    Calendar objCalendar;
    String strDateType = "";
    SharedPreferences objProfileSharedPreferences;
    LocationDetails objLocationDetails;
    boolean isRequestShown = false;
    String is_engage_with_neighbourhood = "";
    ArrayList<String> arrResourceType = new ArrayList<String>();
    public static ArrayList<String> arrResourceId = new ArrayList<String>();
    ArrayList<Uri> arrResourcePath = new ArrayList<Uri>();


    // SHOUT RESOURCE DATA
    Uri videoUri = null;
    public static int ResourcePosition;
    public static final int MEDIA_TYPE_VIDEO = 2;
    ArrayList<OriginalDataModel> arrOriginalDataModel = new ArrayList<OriginalDataModel>();
    ArrayList<EditGridViewResourceModel> arrEditGridViewResourceModel = new ArrayList<EditGridViewResourceModel>();
    public static ArrayList<EditGridViewResourceModel> arrTempEditGridViewResourceModel = new ArrayList<EditGridViewResourceModel>();
    EditGridViewResourceAdapter objEditGridViewResourceAdapter;


    // CUSTOM CATEGORY ALERT
    View customCategoryAlertLayout;
    LayoutInflater objPopupInflater;
    PopupWindow objPopupWindowCategories;
    GridView objCategoryGridView;
    private RelativeLayout objRelativeCategoryPopupOutsideTouch;
    private Button btnOkCategoryPopup;
    private Button btnCancelCategoryPopup;
    ArrayList<EditCategoryModel> arrCategoryModel = new ArrayList<EditCategoryModel>();
    EditCategoryAdapter objEditCategoryAdapter;

    // MAP UTILITY
    RelativeLayout objRelativeLayoutShowMap; //relative_layout_create_shout_map
    TouchableMapFragment mGoogleMap; //map
    LinearLayout objLinearLayoutSearch; //linear_top_map_search_bar
    ImageView objImageSearchAddress; //img_create_shout_search_icon
    TextView objTextViewMapAddress; //textViewMapAddress
    ImageView objImageViewMapPointer; //imageView_marker
    TextView objTextViewSelectAddress; //map_loading_done_create_shout_screen
    TextView objTextViewTitleCount;
    TextView objTextViewDescriptionCount;

    private LatLng center;
    private Geocoder geocoder;
    public List<Address> addresses;
    String UserLatitude = "";
    String UserLongitude = "";
    LatLng latLong;
    CameraUpdate objCameraUpdate;
    public static CameraUpdate zoom;
    private static GoogleMap objGoogleMap;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    SharedPreferences objTempLocationSharedPreferences;
    boolean showBackClickAlert = false;

    ShoutDefaultListModel objShoutDefaultListModel = null;
    private DatabaseHelper objDatabaseHelper;

    InputMethodManager imm;
    private ApplicationUtils applicationUtils;
    public static Uri mCapturedImageURI;
    private boolean isResourceAvaialable=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shout);
        applicationUtils = new ApplicationUtils(this);
        arrResourceId.clear();

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        try {
            // HIDE BOTH TOP AND BOTTOM VIEW OF BASE ACTIVITY
            hideBothTopHeader();

            // PROFILE & LOCATION SHARED PREFERENCES
            objProfileSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
            objTempLocationSharedPreferences = getSharedPreferences(Constants.LOCATION_PREFERENCES, MODE_PRIVATE);

            // INITIALIZE VIEW
            initComponents();
            // GET INTENT DATA
            setIntentData();
            // SETUP MAP
            setUpMap();
            // GET SHOUT RESOURCE API CALL
            new GetShoutResourceAPI().execute();
            // GET CATEGORY LIST
            new LoadCategoryApi().execute();

            objDatabaseHelper = new DatabaseHelper(EditShoutActivity.this);
            objShoutDefaultListModel = objDatabaseHelper.getShoutDetails(strShoutId);

            KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener() {
                @Override
                public void onToggleSoftKeyboard(boolean isVisible) {
                    Log.d("keyboard", "keyboard visible: " + isVisible);
                    if (isVisible) {
                        RelativeLayout objRelativeLayout = (RelativeLayout) findViewById(R.id.relative_bottom_buttons_create_shout_edit);
                        objRelativeLayout.setVisibility(RelativeLayout.GONE);
                    } else {
                        Handler objHandler = new Handler();
                        objHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                RelativeLayout objRelativeLayout = (RelativeLayout) findViewById(R.id.relative_bottom_buttons_create_shout_edit);
                                objRelativeLayout.setVisibility(RelativeLayout.VISIBLE);
                            }
                        }, 300);
                    }
                }
            });
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        positionParams.setMargins(0, 0, 10, 10);
        positionParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        positionParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        position.setLayoutParams(positionParams);
    }

    private void setUpMap() {
        try {
            mGoogleMap.getMapAsync(this);
            setMyLoactionButtonPosition();
            mGoogleMap.setTouchListener(new TouchableWrapper.OnTouchListener() {
                ImageView objMarker = (ImageView) findViewById(R.id.imageView_marker);

                @Override
                public void onTouch() {
                    System.out.println("MAP TOUCH ACTIVATED");
                    objLinearLayoutSearch.setVisibility(LinearLayout.GONE);
//                    objTextViewMapAddress.setVisibility(TextView.GONE);
                    Constants.hideToTop(objLinearLayoutSearch);
                    objTextViewSelectAddress.setVisibility(TextView.GONE);
                    Constants.hideToBottom(objTextViewSelectAddress);
                }

                @Override
                public void onRelease() {
                    System.out.println("MAP TOUCH DE-ACTIVATED");
                    objLinearLayoutSearch.setVisibility(LinearLayout.VISIBLE);
                    Constants.show(objLinearLayoutSearch);
                    objTextViewSelectAddress.setVisibility(TextView.VISIBLE);
                    Constants.show(objTextViewSelectAddress);
                }
            });
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        objGoogleMap = googleMap;
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            } else {

            }
            googleMap.clear();
            latLong = new LatLng(Double.parseDouble(String.valueOf(strLatitude)), Double.parseDouble(String.valueOf(strLongitude)));
            objCameraUpdate = CameraUpdateFactory.newLatLng(latLong);
            zoom = CameraUpdateFactory.zoomTo(15.552834f);
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
                geocoder = new Geocoder(EditShoutActivity.this, Locale.ENGLISH);
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
                    EditShoutActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            edtShoutAddress.setText("No Address found");
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
                    edtShoutAddress.setText(addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1) + " ");
                }
                String strStaticMapUrl = getGoogleMapThumbnail(x, y);
                System.out.println("STATIC MAP URL : " + strStaticMapUrl);
                Picasso.with(EditShoutActivity.this).load(strStaticMapUrl).into(imgStaticMap);
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
            System.out.println("LAT LONG : " + x + "_" + y);
        }

        @Override
        protected void onPreExecute() {
            objTextViewMapAddress.setText(" Getting location ");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                addresses = null;
                geocoder = new Geocoder(EditShoutActivity.this, Locale.ENGLISH);
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
                    EditShoutActivity.this.runOnUiThread(new Runnable() {
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

    public class GetShoutResourceAPI extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                setDefaultItem();
                /*arrEditGridViewResourceModel.clear();
                objEditGridViewResourceAdapter = new EditGridViewResourceAdapter(arrEditGridViewResourceModel, EditShoutActivity.this);
                objGridViewResources.setAdapter(objEditGridViewResourceAdapter);*/
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String strResult = "";
            try {
                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("shout_id", strShoutId);
                strResult = NetworkUtils.postData(Constants.GET_SHOUT_RESOURCES, objJsonObject.toString());
                return strResult;
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
            try {
                arrOriginalDataModel = new ArrayList<OriginalDataModel>();
                JSONObject objJsonObject = new JSONObject(s);
                if (objJsonObject.getString("result").equals("true")) {
                    JSONArray objJsonArray = new JSONArray(objJsonObject.getString("shout_photo"));
                    for (int index = 0; index < objJsonArray.length(); index++) {
                        OriginalDataModel objOriginalDataModel = new OriginalDataModel(
                                objJsonArray.getJSONObject(index).getString("id"),
                                objJsonArray.getJSONObject(index).getString("shout_id"),
                                Constants.HTTP_URL + objJsonArray.getJSONObject(index).getString("photo"),
                                Constants.HTTP_URL + objJsonArray.getJSONObject(index).getString("thumbnail"),
                                Constants.THUMB_BASE_URL + objJsonArray.getJSONObject(index).getString("shout_id") + "/" + objJsonArray.getJSONObject(index).getString("thumb"),
                                objJsonArray.getJSONObject(index).getString("photo_type")
                        );
                        arrOriginalDataModel.add(objOriginalDataModel);
                    }

                    for (int index = 0; index < arrOriginalDataModel.size(); index++) {
                        OriginalDataModel objOriginalDataModel = arrOriginalDataModel.get(index);
                        setResourceFromAPI(objOriginalDataModel.getThumb(),
                                objOriginalDataModel.getPhoto_type(),
                                index,
                                objOriginalDataModel.getId());
                    }
                    if (arrEditGridViewResourceModel.size() > 0) {
                        objEditGridViewResourceAdapter = new EditGridViewResourceAdapter(arrEditGridViewResourceModel, EditShoutActivity.this);
                        objGridViewResources.setAdapter(objEditGridViewResourceAdapter);
                    }else{
                        setDefaultItem();
                    }

                    for (int index = 0; index < arrEditGridViewResourceModel.size(); index++) {
                        arrTempEditGridViewResourceModel.add(arrEditGridViewResourceModel.get(index));
                        EditGridViewResourceModel objGridViewResourceModel = arrEditGridViewResourceModel.get(index);
                        System.out.println("STATUS : POSITION : " + index + " PATH : " + objGridViewResourceModel.getPATH() + " IMAGE URL : " + objGridViewResourceModel.getImagePath() + " TYPE : " + objGridViewResourceModel.getRESUORCE_TYPE() + " IS ADD : " + objGridViewResourceModel.getIS_ADD_BUTTON() + " VIDEO PATH : " + objGridViewResourceModel.getVIDEO_PATH() + " RESOURCE ID : " + objGridViewResourceModel.getRESOURCE_ID());
                    }
                    System.out.println("TEMP RESOURCE MODEL : " + arrTempEditGridViewResourceModel);
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setIntentData() {
        Bundle data = getIntent().getExtras();
        strShoutId = data.getString(ShoutDetailActivity.EDIT_SHOUT_ID_KEY);
        strShoutTitle = data.getString(ShoutDetailActivity.EDIT_SHOUT_TITLE);
        strShoutDescription = data.getString(ShoutDetailActivity.EDIT_SHOUT_DESCRIPTION);
        strShoutType = data.getString(ShoutDetailActivity.EDIT_SHOUT_TYPE);
        strShoutSpecifyDate = data.getString(ShoutDetailActivity.EDIT_SHOUT_SPECIFY_DATE);

        strIsSearchable = data.getString(ShoutDetailActivity.EDIT_SHOUT_IS_SEARCHABLE);
        strLatitude = data.getString(ShoutDetailActivity.EDIT_SHOUT_LATITUDE);
        strLongitude = data.getString(ShoutDetailActivity.EDIT_SHOUT_LONGITUDE);
        strAddress = data.getString(ShoutDetailActivity.EDIT_SHOUT_ADDRESS);
        strCategoryName = data.getString(ShoutDetailActivity.EDIT_SHOUT_CATEGORY_NAME);
        strCategoryId = data.getString(ShoutDetailActivity.EDIT_SHOUT_CATEGORY_ID);

        System.out.println("EDIT : SHOUT ID : " + strShoutId);
        System.out.println("EDIT : SHOUT TITLE : " + strShoutTitle);
        System.out.println("EDIT : SHOUT DESC : " + strShoutDescription);
        System.out.println("EDIT : SHOUT TYPE : " + strShoutType);
        System.out.println("EDIT : SHOUT SPECIFY DATE : " + strShoutSpecifyDate);

        if (strShoutType.equals("R")) {
            setToggleButtons("R");
            objTextViewEngageText.setText("Request People in your Neighbourhood");
            isRequestShown = true;
        } else if (strShoutType.equals("L")) {
            setToggleButtons("L");
            objTextViewEngageText.setText("Share with People in your Neighbourhood");
            isRequestShown = false;
        }
        edtShoutTitle.setText(strShoutTitle);
        edtShoutDescription.setText(strShoutDescription);
        if (!strShoutSpecifyDate.equals("false")) {
            objLinearLayoutDateLayout.setVisibility(LinearLayout.VISIBLE);
            chkSpecifyDate.setChecked(true);
            String strSpesifyDateSpliter[] = strShoutSpecifyDate.split(":");
            edtStartDate.setText(strSpesifyDateSpliter[0]);
            edtEndDate.setText(strSpesifyDateSpliter[1]);

            SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date1 = myFormat.parse(edtStartDate.getText().toString());
                Date date2 = myFormat.parse(edtEndDate.getText().toString());
                long diff = date2.getTime() - date1.getTime();
                System.out.println("EDIT DAY COUNT : " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

                if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 7) {
                    setBackgroundForDates(R.id.btn_week_edit);
                } else if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 30) {
                    setBackgroundForDates(R.id.btn_month_edit);
                } else if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 364) {
                    setBackgroundForDates(R.id.btn_year_edit);
                } else {
                    setBackgroundForDates(R.id.btn_life_edit);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            objLinearLayoutDateLayout.setVisibility(LinearLayout.GONE);
            chkSpecifyDate.setChecked(false);
        }

        if (strIsSearchable.equals("Y")) {
            chkEngage.setChecked(true);
        } else {
            chkEngage.setChecked(false);
        }
        txtSelectedCategoryName.setText(strCategoryName);

        edtShoutAddress.setText(strAddress);

        UserLatitude = strLatitude;
        UserLongitude = strLongitude;

        if (strLatitude.length() > 0 && strLongitude.length() > 0) {
            String strStaticMapUrl = getGoogleMapThumbnail(Double.parseDouble(strLatitude), Double.parseDouble(strLongitude));
            System.out.println("STATIC MAP URL : " + strStaticMapUrl);
            Picasso.with(EditShoutActivity.this).load(strStaticMapUrl).into(imgStaticMap);
        }
    }

    private void setToggleButtons(String l) {
        if (l.equals("R")) {
            isRequestShown = true;
            btnShoutTypeRequest.setBackgroundColor(getResources().getColor(R.color.red_background_color));
            btnShoutTypeRequest.setTextColor(getResources().getColor(R.color.white_text_color));
            btnShoutTypeListing.setBackgroundColor(Color.parseColor("#EFEFEF"));
            btnShoutTypeListing.setTextColor(Color.parseColor("#737373"));
            btnShoutTypeRequest.setText("I NEED HELP");
        } else if (l.equals("L")) {
            isRequestShown = false;
            btnShoutTypeRequest.setBackgroundColor(getResources().getColor(R.color.red_background_color));
            btnShoutTypeRequest.setTextColor(getResources().getColor(R.color.white_text_color));
            btnShoutTypeRequest.setBackgroundColor(Color.parseColor("#EFEFEF"));
            btnShoutTypeRequest.setTextColor(Color.parseColor("#737373"));
            btnShoutTypeListing.setText("I WANT TO GIVE");
            btnShoutTypeListing.setBackgroundColor(getResources().getColor(R.color.red_background_color));
            btnShoutTypeListing.setTextColor(getResources().getColor(R.color.white_text_color));
        }
    }

    private void initComponents() {
        btnShoutTypeRequest = (Button) findViewById(R.id.btn_edit_shout_post_request);
        btnShoutTypeListing = (Button) findViewById(R.id.btn_edit_shout_post_listing);

        objGridViewResources = (GridView) findViewById(R.id.gridview_image_edit);
        edtShoutTitle = (EditText) findViewById(R.id.edt_title_edit);
        edtShoutDescription = (EditText) findViewById(R.id.edt_shout_here_edit);
        chkSpecifyDate = (CheckBox) findViewById(R.id.open_date_layout_edit);
        objLinearLayoutDateLayout = (LinearLayout) findViewById(R.id.linear_hide_show_date_edit);
        edtStartDate = (EditText) findViewById(R.id.edt_start_date_edit);
        imgbtnStartDate = (ImageButton) findViewById(R.id.imgbutton_request_start_date_edit);
        edtEndDate = (EditText) findViewById(R.id.edt_end_date_edit);
        imgbtnEndDate = (ImageButton) findViewById(R.id.imgbutton_request_end_date_edit);
        btnWeek = (Button) findViewById(R.id.btn_week_edit);
        btnMonth = (Button) findViewById(R.id.btn_month_edit);
        btnYear = (Button) findViewById(R.id.btn_year_edit);
        btnForLife = (Button) findViewById(R.id.btn_life_edit);
        objTextViewEngageText = (TextView) findViewById(R.id.txt_neighbourhood_edit);
        chkEngage = (CheckBox) findViewById(R.id.check_engage_edit);
        objRelativeLayoutCategory = (RelativeLayout) findViewById(R.id.relative_category_edit);
        txtSelectedCategoryName = (TextView) findViewById(R.id.txt_selected_category_name_edit);
        edtShoutAddress = (EditText) findViewById(R.id.editText_address_edit);
        txtCurrentLocation = (TextView) findViewById(R.id.txt_current_location_edit);
        txtHomeLocation = (TextView) findViewById(R.id.txt_home_location_edit);
        imgStaticMap = (ImageView) findViewById(R.id.imageView_location_edit);
        txtBack = (TextView) findViewById(R.id.btn_create_shout_back_edit);
        txtUpdateShout = (TextView) findViewById(R.id.btn_give_shout_edit);
        objTextViewTitleCount = (TextView) findViewById(R.id.txt_title_count_edit);
        objTextViewDescriptionCount = (TextView) findViewById(R.id.txt_shout_here_count_edit);


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

        objRelativeLayoutShowMap = (RelativeLayout) findViewById(R.id.relative_layout_create_shout_map);
        mGoogleMap = (TouchableMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        objLinearLayoutSearch = (LinearLayout) findViewById(R.id.linear_top_map_search_bar);
        objImageSearchAddress = (ImageView) findViewById(R.id.img_create_shout_search_icon);
        objTextViewMapAddress = (TextView) findViewById(R.id.textViewMapAddress);
        objImageViewMapPointer = (ImageView) findViewById(R.id.imageView_marker);
        objTextViewSelectAddress = (TextView) findViewById(R.id.map_loading_done_create_shout_screen);

        allClickEvents();

    }

    public String getGoogleMapThumbnail(double lati, double longi) {
        String url = "http://maps.google.com/maps/api/staticmap?center=" + lati + "," + longi + "&zoom=15&size=500x200&markers=" + lati + "," + longi + "&sensor=false";
        return url;
    }

    private void allClickEvents() {

        edtShoutTitle.addTextChangedListener(new TextWatcher() {
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
                    objTextViewTitleCount.setText(s.length() + "/30");
                }
                if (s.length() > 0) {
                    showBackClickAlert = true;
                } else {
                    showBackClickAlert = false;
                }
            }
        });

        edtShoutDescription.addTextChangedListener(new TextWatcher() {
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
                    objTextViewDescriptionCount.setText(s.length() + "/300");
                }
                if (s.length() > 0) {
                    showBackClickAlert = true;
                } else {
                    showBackClickAlert = false;
                }
            }
        });

        btnShoutTypeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strShoutType = "R";
                setToggleButtons("R");
            }
        });

        btnShoutTypeListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strShoutType = "L";
                setToggleButtons("L");
            }
        });

        // UPDATE SHOUT CLICK
        txtUpdateShout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isRequestShown) {

                    System.out.println("SELECTED SHOUT TYPE : R");

                    if (chkEngage.isChecked()) {
                        is_engage_with_neighbourhood = "Y";
                    } else {
                        is_engage_with_neighbourhood = "N";
                    }
                    int intCounter = 0;
                    for (int index = 0; index < arrCategoryModel.size(); index++) {
                        EditCategoryModel objCategoryModel = arrCategoryModel.get(index);
                        if (objCategoryModel.getFlag() == 0) {
                            intCounter = intCounter + 1;
                        }
                    }

                    System.out.println("FINAL USER LATITUDE AND LONGITUDE : " + UserLatitude + " : " + UserLongitude);

                    if (edtShoutTitle.getText().toString().length() > 0) {
                        if (edtShoutDescription.getText().toString().length() > 0) {

                            if (objLinearLayoutDateLayout.getVisibility() == LinearLayout.VISIBLE) {

                                if (!strDateType.equals("L")) {

                                    if (edtStartDate.getText().toString().length() > 0 && edtEndDate.getText().toString().length() > 0) {
                                        if (intCounter == arrCategoryModel.size()) {
                                            Toast.makeText(EditShoutActivity.this, "Please select at least one category.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (UserLatitude.length() > 0 && UserLongitude.length() > 0) {
                                                new EditShout().execute(edtShoutTitle.getText().toString(),
                                                        edtShoutDescription.getText().toString(),
                                                        strDateType,
                                                        edtStartDate.getText().toString(),
                                                        edtEndDate.getText().toString(),
                                                        edtShoutAddress.getText().toString(),
                                                        UserLatitude,
                                                        UserLongitude,
                                                        strShoutType,
                                                        is_engage_with_neighbourhood);
                                            } else {
                                                Toast.makeText(EditShoutActivity.this, "Please select your location.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } else {
                                        Toast.makeText(EditShoutActivity.this, "Please select date and try again.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    if (intCounter == arrCategoryModel.size()) {
                                        Toast.makeText(EditShoutActivity.this, "Please select at least one category.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (UserLatitude.length() > 0 && UserLongitude.length() > 0) {
                                            new EditShout().execute(edtShoutTitle.getText().toString(),
                                                    edtShoutDescription.getText().toString(),
                                                    strDateType,
                                                    edtStartDate.getText().toString(),
                                                    edtEndDate.getText().toString(),
                                                    edtShoutAddress.getText().toString(),
                                                    UserLatitude,
                                                    UserLongitude,
                                                    strShoutType,
                                                    is_engage_with_neighbourhood);
                                        } else {
                                            Toast.makeText(EditShoutActivity.this, "Please select your location.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                /*if (edtStartDate.getText().length() > 0 && edtEndDate.getText().length() > 0 && strDateType.length() == 0) {
                                    strDateType = "C";
                                }
                                if (strDateType.length() > 0) {
                                    if (intCounter == arrCategoryModel.size()) {
                                        Toast.makeText(EditShoutActivity.this, "Please select at least one category.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (UserLatitude.length() > 0 && UserLongitude.length() > 0) {
                                            objShoutDefaultListModel.setTITLE(edtShoutTitle.getText().toString());
                                            objShoutDefaultListModel.setDESCRIPTION(edtShoutDescription.getText().toString());
                                            objShoutDefaultListModel.setSTART_DATE(edtStartDate.getText().toString());
                                            objShoutDefaultListModel.setEND_DATE(edtEndDate.getText().toString());
                                            objShoutDefaultListModel.setSHOUT_ADDRESS(edtShoutAddress.getText().toString());
                                            objShoutDefaultListModel.setSHOUT_LATITUDE(UserLatitude);
                                            objShoutDefaultListModel.setSHOUT_LONGITUDE(UserLongitude);
                                            objShoutDefaultListModel.setSHOUT_TYPE("R");

                                            new EditShout().execute(edtShoutTitle.getText().toString(),
                                                    edtShoutDescription.getText().toString(),
                                                    strDateType,
                                                    edtStartDate.getText().toString(),
                                                    edtEndDate.getText().toString(),
                                                    edtShoutAddress.getText().toString(),
                                                    UserLatitude,
                                                    UserLongitude,
                                                    "R",
                                                    is_engage_with_neighbourhood);
                                        } else {
                                            Toast.makeText(EditShoutActivity.this, "Please select your location.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(EditShoutActivity.this, "Please select date and try again.", Toast.LENGTH_SHORT).show();
                                }*/
                            } else {
                                strDateType = "";
                                if (intCounter == arrCategoryModel.size()) {
                                    Toast.makeText(EditShoutActivity.this, "Please select at least one category.", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (UserLatitude.length() > 0 && UserLongitude.length() > 0) {
                                        objShoutDefaultListModel.setTITLE(edtShoutTitle.getText().toString());
                                        objShoutDefaultListModel.setDESCRIPTION(edtShoutDescription.getText().toString());
                                        objShoutDefaultListModel.setSTART_DATE(edtStartDate.getText().toString());
                                        objShoutDefaultListModel.setEND_DATE(edtEndDate.getText().toString());
                                        objShoutDefaultListModel.setSHOUT_ADDRESS(edtShoutAddress.getText().toString());
                                        objShoutDefaultListModel.setSHOUT_LATITUDE(UserLatitude);
                                        objShoutDefaultListModel.setSHOUT_LONGITUDE(UserLongitude);
                                        objShoutDefaultListModel.setSHOUT_TYPE("R");
                                        new EditShout().execute(edtShoutTitle.getText().toString(),
                                                edtShoutDescription.getText().toString(),
                                                strDateType,
                                                edtStartDate.getText().toString(),
                                                edtEndDate.getText().toString(),
                                                edtShoutAddress.getText().toString(),
                                                UserLatitude,
                                                UserLongitude,
                                                "R",
                                                is_engage_with_neighbourhood);
                                    } else {
                                        Toast.makeText(EditShoutActivity.this, "Please select your location.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        } else {
                            edtShoutDescription.requestFocus();
                            edtShoutDescription.setError("Enter shout discription");
                        }
                    } else {
                        edtShoutTitle.requestFocus();
                        edtShoutTitle.setError("Enter shout title");
                    }
                } else {

                    System.out.println("SELECTED SHOUT TYPE : L");

                    // LISTING SECTION
                    if (chkEngage.isChecked()) {
                        is_engage_with_neighbourhood = "Y";
                    } else {
                        is_engage_with_neighbourhood = "N";
                    }

                    int intCounter = 0;

                    for (int index = 0; index < arrCategoryModel.size(); index++) {
                        EditCategoryModel objCategoryModel = arrCategoryModel.get(index);
                        if (objCategoryModel.getFlag() == 0) {
                            intCounter = intCounter + 1;
                        }
                    }

                    System.out.println("FINAL USER LATITUDE AND LONGITUDE : " + UserLatitude + " : " + UserLongitude);

                    for (int index=0;index<arrEditGridViewResourceModel.size();index++){
                        if(!arrEditGridViewResourceModel.get(index).getRESUORCE_TYPE().equals("")){
                            isResourceAvaialable = true;
                        }
                    }

                    if (isResourceAvaialable) {
                        if (edtShoutTitle.getText().toString().length() > 0) {
                            if (edtShoutDescription.getText().toString().length() > 0) {
                                if (objLinearLayoutDateLayout.getVisibility() == LinearLayout.VISIBLE) {

                                    if (!strDateType.equals("L")) {
                                    
                                   /* if (strDateType.length() > 0) {
                                        if (intCounter == arrCategoryModel.size()) {
                                            Toast.makeText(EditShoutActivity.this, "Please select at least one category.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (UserLatitude.length() > 0 && UserLongitude.length() > 0) {
                                                objShoutDefaultListModel.setTITLE(edtShoutTitle.getText().toString());
                                                objShoutDefaultListModel.setDESCRIPTION(edtShoutDescription.getText().toString());
                                                objShoutDefaultListModel.setSTART_DATE(edtStartDate.getText().toString());
                                                objShoutDefaultListModel.setEND_DATE(edtEndDate.getText().toString());
                                                objShoutDefaultListModel.setSHOUT_ADDRESS(edtShoutAddress.getText().toString());
                                                objShoutDefaultListModel.setSHOUT_LATITUDE(UserLatitude);
                                                objShoutDefaultListModel.setSHOUT_LONGITUDE(UserLongitude);
                                                objShoutDefaultListModel.setSHOUT_TYPE("L");
                                                new EditShout().execute(
                                                        edtShoutTitle.getText().toString(),
                                                        edtShoutDescription.getText().toString(),
                                                        strDateType,
                                                        edtStartDate.getText().toString(),
                                                        edtEndDate.getText().toString(),
                                                        edtShoutAddress.getText().toString(),
                                                        UserLatitude,
                                                        UserLongitude,
                                                        "L",
                                                        is_engage_with_neighbourhood);
                                            } else {
                                                Toast.makeText(EditShoutActivity.this, "Please select your location.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } else {
                                        Toast.makeText(EditShoutActivity.this, "Please select date and tr again.", Toast.LENGTH_SHORT).show();
                                    }*/
                                        if (edtStartDate.getText().toString().length() > 0 && edtEndDate.getText().toString().length() > 0) {

                                            if (intCounter == arrCategoryModel.size()) {
                                                Toast.makeText(EditShoutActivity.this, "Please select at least one category.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (UserLatitude.length() > 0 && UserLongitude.length() > 0) {
                                                    new EditShout().execute(edtShoutTitle.getText().toString(),
                                                            edtShoutDescription.getText().toString(),
                                                            strDateType,
                                                            edtStartDate.getText().toString(),
                                                            edtEndDate.getText().toString(),
                                                            edtShoutAddress.getText().toString(),
                                                            UserLatitude,
                                                            UserLongitude,
                                                            strShoutType,
                                                            is_engage_with_neighbourhood);
                                                } else {
                                                    Toast.makeText(EditShoutActivity.this, "Please select your location.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(EditShoutActivity.this, "Please select date and try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        if (intCounter == arrCategoryModel.size()) {
                                            Toast.makeText(EditShoutActivity.this, "Please select at least one category.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (UserLatitude.length() > 0 && UserLongitude.length() > 0) {
                                                new EditShout().execute(edtShoutTitle.getText().toString(),
                                                        edtShoutDescription.getText().toString(),
                                                        strDateType,
                                                        edtStartDate.getText().toString(),
                                                        edtEndDate.getText().toString(),
                                                        edtShoutAddress.getText().toString(),
                                                        UserLatitude,
                                                        UserLongitude,
                                                        strShoutType,
                                                        is_engage_with_neighbourhood);
                                            } else {
                                                Toast.makeText(EditShoutActivity.this, "Please select your location.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                } else {
                                    strDateType = "false";
                                    if (intCounter == arrCategoryModel.size()) {
                                        Toast.makeText(EditShoutActivity.this, "Please select at least one category.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (UserLatitude.length() > 0 && UserLongitude.length() > 0) {
                                            new EditShout().execute(edtShoutTitle.getText().toString(),
                                                    edtShoutDescription.getText().toString(),
                                                    strDateType,
                                                    edtStartDate.getText().toString(),
                                                    edtEndDate.getText().toString(),
                                                    edtShoutAddress.getText().toString(),
                                                    UserLatitude,
                                                    UserLongitude,
                                                    strShoutType,
                                                    is_engage_with_neighbourhood);
                                        } else {
                                            Toast.makeText(EditShoutActivity.this, "Please select your location.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            } else {
                                edtShoutDescription.setError("");
                            }
                        } else {
                            edtShoutTitle.setError("");
                        }
                    } else {
                        Toast.makeText(EditShoutActivity.this, "Please upload at lease one media file.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        txtCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isGPSEnabled(EditShoutActivity.this)) {
                    objLocationDetails = new LocationDetails(EditShoutActivity.this);
                    objLocationDetails.startTracking();
                    new DisplayAddress().execute(objTempLocationSharedPreferences.getString(Constants.LOCATION_LATITUDE, ""),
                            objTempLocationSharedPreferences.getString(Constants.LOCATION_LONGITUDE, ""), "");
                }
            }
        });

        txtHomeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DisplayAddress().execute(objProfileSharedPreferences.getString(Constants.USER_REGISTERED_LATITUDE, ""), objProfileSharedPreferences.getString(Constants.USER_REGISTERED_LONGITUDE, ""), "");
                UserLatitude = objProfileSharedPreferences.getString(Constants.USER_REGISTERED_LATITUDE, "");
                UserLongitude = objProfileSharedPreferences.getString(Constants.USER_REGISTERED_LONGITUDE, "");
            }
        });

        objTextViewSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objTextViewMapAddress.getText().equals(" Getting location ")) {
                    Toast.makeText(EditShoutActivity.this, "Location not traced. Please wait...", Toast.LENGTH_SHORT).show();
                } else {
                    if (UserLatitude.length() > 0 && UserLongitude.length() > 0) {
                        String strStaticMapUrl = getGoogleMapThumbnail(Double.parseDouble(UserLatitude), Double.parseDouble(UserLongitude));
                        System.out.println("STATIC MAP URL : " + strStaticMapUrl);
                        Picasso.with(EditShoutActivity.this).load(strStaticMapUrl).into(imgStaticMap);
                        edtShoutAddress.setText(objTextViewMapAddress.getText());
                        showMap(false);
                    } else {
                        Toast.makeText(EditShoutActivity.this, "No Location found...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        imgStaticMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkGooglePlayServicesAvailable(EditShoutActivity.this)) {
                    Utils.d("debug", "CLIKED imageView_post_request_location ");
                    //startService(new Intent(EditShoutActivity.this, LocationBackGroundService.class));
                    showMap(true);
                } else {
                    Utils.showGPSDisabledAlertToUser(EditShoutActivity.this);
                }
            }
        });

        edtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("IN REQUEST START DATE ");
                try {
                    if (edtStartDate.getText().toString().length() > 0) {
                        objCalendar = new GregorianCalendar();
                        String strFromDate = edtStartDate.getText().toString();
                        String dateSpliterFirst[] = strFromDate.split("/");
                        objCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSpliterFirst[0].toString()));
                        objCalendar.set(Calendar.MONTH, Integer.parseInt(dateSpliterFirst[1].toString()) - 1);
                        objCalendar.set(Calendar.YEAR, Integer.parseInt(dateSpliterFirst[2].toString()));
                    } else {
                        objCalendar = Calendar.getInstance();
                    }
                    new DatePickerDialog(EditShoutActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
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
                            edtStartDate.setText(sendDate1);
                            edtEndDate.setText("");
                            resetDateSelector();
                        }
                    }, objCalendar.get(Calendar.YEAR), objCalendar.get(Calendar.MONTH), objCalendar.get(Calendar.DAY_OF_MONTH)).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        imgbtnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("IN REQUEST START DATE ");
                try {
                    if (edtStartDate.getText().toString().length() > 0) {
                        objCalendar = new GregorianCalendar();
                        String strFromDate = edtStartDate.getText().toString();
                        String dateSpliterFirst[] = strFromDate.split("/");
                        objCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSpliterFirst[0].toString()));
                        objCalendar.set(Calendar.MONTH, Integer.parseInt(dateSpliterFirst[1].toString()) - 1);
                        objCalendar.set(Calendar.YEAR, Integer.parseInt(dateSpliterFirst[2].toString()));
                    } else {
                        objCalendar = Calendar.getInstance();
                    }
                    new DatePickerDialog(EditShoutActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
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
                            edtStartDate.setText(sendDate1);
                            edtEndDate.setText("");
                            resetDateSelector();
                        }
                    }, objCalendar.get(Calendar.YEAR), objCalendar.get(Calendar.MONTH), objCalendar.get(Calendar.DAY_OF_MONTH)).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        edtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("IN REQUEST START DATE ");
                try {
                    if (edtStartDate.getText().length() > 0) {
                        if (edtEndDate.getText().toString().length() > 0) {
                            objCalendar = new GregorianCalendar();
                            String strFromDate = edtEndDate.getText().toString();
                            String dateSpliterFirst[] = strFromDate.split("/");
                            objCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSpliterFirst[0].toString()));
                            objCalendar.set(Calendar.MONTH, Integer.parseInt(dateSpliterFirst[1].toString()) - 1);
                            objCalendar.set(Calendar.YEAR, Integer.parseInt(dateSpliterFirst[2].toString()));
                        } else {
                            objCalendar = Calendar.getInstance();
                        }
                        new DatePickerDialog(EditShoutActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
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

                                if (isDateCorrect(edtStartDate.getText().toString(), sendDate1)) {
                                    edtEndDate.setText(sendDate1);
                                } else {
                                    edtEndDate.setText("");
                                    Toast.makeText(EditShoutActivity.this, "End Date should be greater then Start Date.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, objCalendar.get(Calendar.YEAR), objCalendar.get(Calendar.MONTH), objCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    } else {
                        Toast.makeText(EditShoutActivity.this, "Please select start date first.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        imgbtnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("IN REQUEST START DATE ");
                try {
                    if (edtStartDate.getText().length() > 0) {
                        if (edtEndDate.getText().toString().length() > 0) {
                            objCalendar = new GregorianCalendar();
                            String strFromDate = edtEndDate.getText().toString();
                            String dateSpliterFirst[] = strFromDate.split("/");
                            objCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSpliterFirst[0].toString()));
                            objCalendar.set(Calendar.MONTH, Integer.parseInt(dateSpliterFirst[1].toString()) - 1);
                            objCalendar.set(Calendar.YEAR, Integer.parseInt(dateSpliterFirst[2].toString()));
                        } else {
                            objCalendar = Calendar.getInstance();
                        }
                        new DatePickerDialog(EditShoutActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
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
                                if (isDateCorrect(edtStartDate.getText().toString(), sendDate1)) {
                                    edtEndDate.setText(sendDate1);
                                } else {
                                    edtEndDate.setText("");
                                    Toast.makeText(EditShoutActivity.this, "End Date should be greater then Start Date.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, objCalendar.get(Calendar.YEAR), objCalendar.get(Calendar.MONTH), objCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    } else {
                        Toast.makeText(EditShoutActivity.this, "Please select start date first.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        objRelativeLayoutCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryPopupDialog();
            }
        });

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        chkSpecifyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                if (objLinearLayoutDateLayout.getVisibility() == LinearLayout.VISIBLE) {
                    strDateType = "";
                    chkSpecifyDate.setChecked(false);
                    objLinearLayoutDateLayout.setVisibility(LinearLayout.GONE);
                } else {
                    strDateType = "L";
                    chkSpecifyDate.setChecked(true);
                    objLinearLayoutDateLayout.setVisibility(LinearLayout.VISIBLE);
                }
            }
        });

        btnWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                setBackgroundForDates(R.id.btn_week_edit);
                if (edtStartDate.getText().toString().length() == 0) {
                    objCalendar = Calendar.getInstance();
                    SimpleDateFormat sendDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    edtStartDate.setText(sendDateFormat.format(objCalendar.getTime()));
                }
                edtEndDate.setText(getNextDate(edtStartDate.getText().toString(), 7));
                System.out.println("WEEK DATE  : " + getNextDate(edtStartDate.getText().toString(), 7));
                strDateType = "W";
            }
        });

        btnMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                setBackgroundForDates(R.id.btn_month_edit);
                setBackgroundForDates(R.id.btn_month_post_request);
                if (edtStartDate.getText().toString().length() == 0) {
                    objCalendar = Calendar.getInstance();
                    SimpleDateFormat sendDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    edtStartDate.setText(sendDateFormat.format(objCalendar.getTime()));
                }
                edtEndDate.setText(getNextDate(edtStartDate.getText().toString(), 30));
                System.out.println("MONTH DATE  : " + getNextDate(edtStartDate.getText().toString(), 30));
                strDateType = "M";
            }
        });

        btnYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                setBackgroundForDates(R.id.btn_year_edit);
                if (edtStartDate.getText().toString().length() == 0) {
                    objCalendar = Calendar.getInstance();
                    SimpleDateFormat sendDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    edtStartDate.setText(sendDateFormat.format(objCalendar.getTime()));
                }
                edtEndDate.setText(getNextYearDate(edtStartDate.getText().toString()));
                strDateType = "Y";
            }
        });

        btnForLife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                setBackgroundForDates(R.id.btn_life_edit);
                if (edtStartDate.getText().toString().length() == 0) {
                    objCalendar = Calendar.getInstance();
                    SimpleDateFormat sendDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    edtStartDate.setText(sendDateFormat.format(objCalendar.getTime()));
                }
                strDateType = "L";
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        objLocationDetails = new LocationDetails(EditShoutActivity.this);
        objLocationDetails.startTracking();
    }

    private void showMap(boolean status) {
        if (status)
            objRelativeLayoutShowMap.setVisibility(View.VISIBLE);
        else
            objRelativeLayoutShowMap.setVisibility(View.INVISIBLE);
    }

    public class EditShout extends AsyncTask<String, Void, String> {

        final ProgressDialog objProgressDialog = new ProgressDialog(EditShoutActivity.this);
        String strResult = "";
        String strShoutType="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objProgressDialog.setTitle("Loading...");
            objProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String strCategoryId = "";
                for (int index = 0; index < arrCategoryModel.size(); index++) {
                    EditCategoryModel objCategoryModel = arrCategoryModel.get(index);
                    if (objCategoryModel.getFlag() == 1) {
                        strCategoryId = objCategoryModel.getId();
                        objShoutDefaultListModel.setSHOUT_CATEGORY_ID(strCategoryId);
                        objShoutDefaultListModel.setSHOUT_CATEGORY_NAME(objCategoryModel.getTitle());
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
                objJsonObject.put("op", "EDIT");
                objJsonObject.put("shout_id", strShoutId);

                strShoutType = params[8];

                if (params[3].equals("")) {
                    objShoutDefaultListModel.setSTART_DATE("false");
                }
                if (params[4].equals("")) {
                    objShoutDefaultListModel.setEND_DATE("false");
                }

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

                    // UPDATING MODIFIED TEXTUAL DATA INTO LOCAL DB
                    objDatabaseHelper.updateShout(objShoutDefaultListModel, strShoutId);

                    arrResourceType = new ArrayList<String>();
                    arrResourcePath = new ArrayList<Uri>();
//                    arrResourceId = new ArrayList<String>();


                    for (int index = 0; index < arrEditGridViewResourceModel.size(); index++) {
                        EditGridViewResourceModel objGridViewResourceModel = arrEditGridViewResourceModel.get(index);
                        System.out.println("FINAL STATUS  : POSITION : " + index + " PATH : " + objGridViewResourceModel.getPATH() + " IMAGE URL : " + objGridViewResourceModel.getImagePath() + " TYPE : " + objGridViewResourceModel.getRESUORCE_TYPE() + " IS ADD : " + objGridViewResourceModel.getIS_ADD_BUTTON() + " VIDEO PATH : " + objGridViewResourceModel.getVIDEO_PATH() + " RESOURCE ID : " + objGridViewResourceModel.getRESOURCE_ID());
                    }

                    for (int index = 0; index < arrEditGridViewResourceModel.size(); index++) {
                        EditGridViewResourceModel objEditGridViewResourceModel = arrEditGridViewResourceModel.get(index);
                        EditGridViewResourceModel objTempEditGridViewResourceModel = arrTempEditGridViewResourceModel.get(index);

                        System.out.println("TEMP RESOURCE ID : " + objTempEditGridViewResourceModel.getRESOURCE_ID());
                        System.out.println("ORIG RESOURCE ID : " + objEditGridViewResourceModel.getRESOURCE_ID());

                        if (objTempEditGridViewResourceModel.getRESOURCE_ID() == objEditGridViewResourceModel.getRESOURCE_ID()) {

                            System.out.println("ORIG RESOURCE IMAGE PATH : " + objEditGridViewResourceModel.getImagePath());

                            if (objEditGridViewResourceModel.getImagePath().isEmpty()) {
                                System.out.println("ORIG RESOURCE REPEAT ID : " + objEditGridViewResourceModel.getRESOURCE_ID());
//                                arrResourceId.add(objEditGridViewResourceModel.getRESOURCE_ID());
                                if (objEditGridViewResourceModel.getRESUORCE_TYPE().equals("P")) {
                                    arrResourcePath.add(objEditGridViewResourceModel.getPATH());
                                    arrResourceType.add(objEditGridViewResourceModel.getRESUORCE_TYPE());
                                } else if (objEditGridViewResourceModel.getRESUORCE_TYPE().equals("V")) {
                                    arrResourcePath.add(objEditGridViewResourceModel.getPATH());
                                    arrResourceType.add(objEditGridViewResourceModel.getRESUORCE_TYPE());
                                    arrResourcePath.add(objEditGridViewResourceModel.getVIDEO_PATH());
                                    arrResourceType.add(objEditGridViewResourceModel.getRESUORCE_TYPE());
                                    // TODO: 04/10/16 ADDING VIDEO RESOURCE TO LOCAL DATABASE
                                    objDatabaseHelper.addToBackgroundTask(strShoutId, objEditGridViewResourceModel.getPATH().getPath(), objEditGridViewResourceModel.getVIDEO_PATH().getPath());
                                }
                            }
                        } else {
                            if (objEditGridViewResourceModel.getRESUORCE_TYPE().equals("P")) {
                                if (objEditGridViewResourceModel.getPATH() != null) {
                                    arrResourcePath.add(objEditGridViewResourceModel.getPATH());
                                    arrResourceType.add(objEditGridViewResourceModel.getRESUORCE_TYPE());
                                }
                            } else if (objEditGridViewResourceModel.getRESUORCE_TYPE().equals("V")) {
                                if (objEditGridViewResourceModel.getPATH() != null) {
                                    arrResourcePath.add(objEditGridViewResourceModel.getPATH());
                                    arrResourceType.add(objEditGridViewResourceModel.getRESUORCE_TYPE());
                                    arrResourcePath.add(objEditGridViewResourceModel.getVIDEO_PATH());
                                    arrResourceType.add(objEditGridViewResourceModel.getRESUORCE_TYPE());
                                    // TODO: 04/10/16 ADDING VIDEO RESOURCE TO LOCAL DATABASE
                                    objDatabaseHelper.addToBackgroundTask(strShoutId, objEditGridViewResourceModel.getPATH().getPath(), objEditGridViewResourceModel.getVIDEO_PATH().getPath());
                                }
                            }
                        }
                    }
                    System.out.println("FINAL RESOURCE PATH : " + arrResourcePath);
                    System.out.println("UPLOADING : RESOURCE TYPE : " + arrResourceType);
                    System.out.println("UPLOADING : RESOURCE PATH : " + arrResourcePath);
                    System.out.println("UPLOADING : RESOURCE ID : " + arrResourceId);
                        new UploadResources().execute();
                } else {
//                    Toast.makeText(EditShoutActivity.this, "Failed to create shout.Please try again.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class UploadResources extends AsyncTask<String, Void, String> {

        final ProgressDialog objProgressDialog = new ProgressDialog(EditShoutActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                objProgressDialog.setMessage("Uploading shout resources...");
                objProgressDialog.show();
                objProgressDialog.setCancelable(false);
                objProgressDialog.setCanceledOnTouchOutside(false);
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String strResult = "";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(Constants.UPLOAD_RESOURCES);
                System.out.println("UPLOAD RESOURCE URL : " + Constants.UPLOAD_RESOURCES);
                HttpResponse response = null;
                MultipartEntity entityBuilder = new MultipartEntity();


                HashMap<String,String> objHashMapDeletedResourceId = new HashMap<String,String>();

                JSONArray objJsonArray = new JSONArray();
                for (int idIndex = 0; idIndex < arrResourceId.size(); idIndex++) {
                    if(!arrResourceId.get(idIndex).equals("")){
                        objHashMapDeletedResourceId.put(arrResourceId.get(idIndex),arrResourceId.get(idIndex));
                    }
                }
//                objJsonArray.put(new JSONObject().put("delete_id", arrResourceId.get(idIndex)));
                for(String key : objHashMapDeletedResourceId.keySet()){
                    objJsonArray.put(new JSONObject().put("delete_id", objHashMapDeletedResourceId.get(key)));
                }

                System.out.println("DELETED RESOURCE JSON : " + objJsonArray);

                entityBuilder.addPart("shout_id", new StringBody(strShoutId));
                entityBuilder.addPart("resource_id", new StringBody(objJsonArray.toString()));

                ArrayList<Uri> arrTempVideoResources = new ArrayList<Uri>();

                for (int index = 0; index < arrResourceType.size(); index++) {
                    if (arrResourceType.get(index).equals("P")) {
                        File file = new File(arrResourcePath.get(index).getPath());
                        FileBody objFile = new FileBody(file);
                        entityBuilder.addPart("Image-" + index, objFile);
                    } else if (arrResourceType.get(index).equals("V")) {
                        /*Utils.d("EDIT SHOUT RESOURCE ",arrResourcePath.get(index).getPath());
                        File thumbnailFile = new File(arrResourcePath.get(index).getPath());
                        FileBody objThumbnailFileBody = new FileBody(thumbnailFile);
                        entityBuilder.addPart("Video-" + index, objThumbnailFileBody);*/
                        arrTempVideoResources.add(arrResourcePath.get(index));
                    } else if (arrResourceType.get(index).equals("D")) {
                        File file = new File(arrResourcePath.get(index).getPath());
                        FileBody objFile = new FileBody(file);
                        entityBuilder.addPart("File-" + index, objFile);
                    }
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
            try {
                if (objProgressDialog.isShowing()) {
                    objProgressDialog.dismiss();
                }
                JSONObject objJsonObject = new JSONObject(s);
                if (objJsonObject.getString("result").equals("true")) {

                    startService(new Intent(EditShoutActivity.this, UploadResourceService.class));

                    // UPDATING SHOUT IMAGES INTO LOCAL DB
                    JSONObject innerJSONObject = new JSONObject(objJsonObject.getString("shout_media"));
                    objShoutDefaultListModel.setSHOUT_IMAGE(innerJSONObject.getString("shout_image"));
                    objShoutDefaultListModel.setStrShoutImages(innerJSONObject.getString("images"));
                    objDatabaseHelper.updateShout(objShoutDefaultListModel, strShoutId);

                    Intent objIntent = new Intent(EditShoutActivity.this, ShoutDefaultActivity.class);
                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(objIntent);
                    finish();
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void resetDateSelector() {

        // CHANGING DATE TYPE TO EMPTY
        strDateType = "";
        // CHANGING BACK COLOR
        btnWeek.setBackgroundResource(R.drawable.deselect_date_background);
        btnMonth.setBackgroundResource(R.drawable.deselect_date_background);
        btnYear.setBackgroundResource(R.drawable.deselect_date_background);
        btnForLife.setBackgroundResource(R.drawable.deselect_date_background);

        // CHANGING TEXT COLOR
        btnWeek.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
        btnMonth.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
        btnYear.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
        btnForLife.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
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
                    EditCategoryModel objCategoryModelTemp = arrCategoryModel.get(index);
                    if (objCategoryModelTemp.getFlag() == 1) {
                        txtSelectedCategoryName.setText(objCategoryModelTemp.getTitle());
                    }
                }
                objPopupWindowCategories.dismiss();
            }
        });

        btnCancelCategoryPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*for (int index = 0; index < arrCategoryModel.size(); index++) {
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

    @Override
    public void onBackPressed() {
        if (objRelativeLayoutShowMap.getVisibility() == RelativeLayout.VISIBLE) {
            objRelativeLayoutShowMap.setVisibility(RelativeLayout.GONE);
        } else {
            super.onBackPressed();
        }
    }

    public void setBackgroundForDates(int intResourceId) {
        switch (intResourceId) {
            case R.id.btn_week_edit:
                strDateType = "W";
                btnWeek.setBackgroundResource(R.drawable.select_date_background);
                btnMonth.setBackgroundResource(R.drawable.deselect_date_background);
                btnYear.setBackgroundResource(R.drawable.deselect_date_background);
                btnForLife.setBackgroundResource(R.drawable.deselect_date_background);

                btnWeek.setTextColor(getResources().getColor(R.color.text_color_date_select));
                btnMonth.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnYear.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnForLife.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                break;
            case R.id.btn_month_edit:
                strDateType = "M";
                btnWeek.setBackgroundResource(R.drawable.deselect_date_background);
                btnMonth.setBackgroundResource(R.drawable.select_date_background);
                btnYear.setBackgroundResource(R.drawable.deselect_date_background);
                btnForLife.setBackgroundResource(R.drawable.deselect_date_background);

                btnWeek.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnMonth.setTextColor(getResources().getColor(R.color.text_color_date_select));
                btnYear.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnForLife.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                break;
            case R.id.btn_year_edit:
                strDateType = "Y";
                btnWeek.setBackgroundResource(R.drawable.deselect_date_background);
                btnMonth.setBackgroundResource(R.drawable.deselect_date_background);
                btnYear.setBackgroundResource(R.drawable.select_date_background);
                btnForLife.setBackgroundResource(R.drawable.deselect_date_background);

                btnWeek.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnMonth.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnYear.setTextColor(getResources().getColor(R.color.text_color_date_select));
                btnForLife.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                break;
            case R.id.btn_life_edit:
                strDateType = "L";
                btnWeek.setBackgroundResource(R.drawable.deselect_date_background);
                btnMonth.setBackgroundResource(R.drawable.deselect_date_background);
                btnYear.setBackgroundResource(R.drawable.deselect_date_background);
                btnForLife.setBackgroundResource(R.drawable.select_date_background);

                btnWeek.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnMonth.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnYear.setTextColor(getResources().getColor(R.color.text_color_date_deselect));
                btnForLife.setTextColor(getResources().getColor(R.color.text_color_date_select));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                                    setResourceToView(applicationUtils.saveImageOnSdCard(imageBytes), "P", null);
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
                                    setResourceToView(applicationUtils.saveImageOnSdCard(imageBytes), "P", null);
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
        }
        /*else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
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
                // TODO: Handle the error.
//                    Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }*/
    }


    private void galleryIntent(Intent data) {
        try {
            String realPath;
            // SDK < API11
            if (Build.VERSION.SDK_INT < 11)
                realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());
                // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19)
                realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
                // SDK > 19 (Android 4.4)
            else
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());

//            new ImageCompressionAsyncTask(true, Uri.parse(realPath), "P").execute();

        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cameraIntent(Intent data) {
        System.out.println("CAMERA IMAGE PATH : " + data.getDataString());
//        new ImageCompressionAsyncTask(false, data.getData(), "C").execute();
    }


    public void setResourceFromAPI(String imageUrl, String type, int position, String resourceId) {
        EditGridViewResourceModel objGridViewResourceModelObject = arrEditGridViewResourceModel.get(position);
        objGridViewResourceModelObject.setPATH(null);
        objGridViewResourceModelObject.setIS_ADD_BUTTON(false);
        if (type.equals("P")) {
            objGridViewResourceModelObject.setRESUORCE_TYPE(type);
            objGridViewResourceModelObject.setVIDEO_PATH(null);
            objGridViewResourceModelObject.setImagePath(imageUrl);
        } else if (type.equals("V")) {
            objGridViewResourceModelObject.setRESUORCE_TYPE(type);
            objGridViewResourceModelObject.setVIDEO_PATH(null);
            objGridViewResourceModelObject.setImagePath(imageUrl);
        }
        objGridViewResourceModelObject.setRESOURCE_ID(resourceId);

        objEditGridViewResourceAdapter.notifyDataSetChanged();

        for (int index = 0; index < arrEditGridViewResourceModel.size(); index++) {
            EditGridViewResourceModel objGridViewResourceModelInner = arrEditGridViewResourceModel.get(index);
            if (objGridViewResourceModelInner.getPATH() == null && objGridViewResourceModelInner.getImagePath().equals("")) {
                objGridViewResourceModelInner.setPATH(null);
                objGridViewResourceModelInner.setRESUORCE_TYPE("");
                objGridViewResourceModelInner.setImagePath("");
                objGridViewResourceModelInner.setIS_ADD_BUTTON(true);

                objGridViewResourceModelInner.setVIDEO_PATH(null);
                objGridViewResourceModelInner.setRESOURCE_ID("");
                objEditGridViewResourceAdapter.notifyDataSetChanged();
                break;
            }
        }
    }


    private void setResourceToView(Uri imageUri, String type, Uri videoUri) {
        System.out.println("PRASANNA PRINT : URI : " + imageUri + " TYPE : " + type);
        EditGridViewResourceModel objGridViewResourceModelObject = arrEditGridViewResourceModel.get(ResourcePosition);
        objGridViewResourceModelObject.setPATH(imageUri);
        objGridViewResourceModelObject.setIS_ADD_BUTTON(false);

        if (type.equals("P")) {
            objGridViewResourceModelObject.setRESUORCE_TYPE(type);
            objGridViewResourceModelObject.setVIDEO_PATH(null);
            objGridViewResourceModelObject.setImagePath("");
        } else if (type.equals("V")) {
            objGridViewResourceModelObject.setRESUORCE_TYPE("V");
            objGridViewResourceModelObject.setVIDEO_PATH(videoUri);
            objGridViewResourceModelObject.setImagePath("");
        }
        objEditGridViewResourceAdapter.notifyDataSetChanged();

        for (int index = 0; index < arrEditGridViewResourceModel.size(); index++) {
            EditGridViewResourceModel objGridViewResourceModelInner = arrEditGridViewResourceModel.get(index);
            if (objGridViewResourceModelInner.getPATH() == null && objGridViewResourceModelInner.getImagePath().equals("")) {
                objGridViewResourceModelInner.setPATH(null);
                objGridViewResourceModelInner.setRESUORCE_TYPE("");
                objGridViewResourceModelInner.setImagePath("");
                objGridViewResourceModelInner.setIS_ADD_BUTTON(true);

                objGridViewResourceModelInner.setVIDEO_PATH(null);
                objEditGridViewResourceAdapter.notifyDataSetChanged();
                break;
            }
        }
        for (int index = 0; index < arrEditGridViewResourceModel.size(); index++) {
            EditGridViewResourceModel objGridViewResourceModel = arrEditGridViewResourceModel.get(index);
            System.out.println("NEW STATUS : POSITION : " + index + " PATH : " + objGridViewResourceModel.getPATH() + " IMAGE URL : " + objGridViewResourceModel.getImagePath() + " TYPE : " + objGridViewResourceModel.getRESUORCE_TYPE() + " IS ADD : " + objGridViewResourceModel.getIS_ADD_BUTTON() + " VIDEO PATH : " + objGridViewResourceModel.getVIDEO_PATH());
        }
    }

    public void setDefaultItem() {
        arrEditGridViewResourceModel = new ArrayList<EditGridViewResourceModel>();
        for (int index = 0; index < Constants.MAX_RESOURCE_SIZE; index++) {
            EditGridViewResourceModel objEditGridViewResourceModel;

            if (index == 0) {
                objEditGridViewResourceModel = new EditGridViewResourceModel(null, "", "", true, null, "");
            } else {
                objEditGridViewResourceModel = new EditGridViewResourceModel(null, "", "", false, null, "");
            }
            arrEditGridViewResourceModel.add(objEditGridViewResourceModel);
        }
        objEditGridViewResourceAdapter = new EditGridViewResourceAdapter(arrEditGridViewResourceModel, EditShoutActivity.this);
        objGridViewResources.setAdapter(objEditGridViewResourceAdapter);
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

                            EditCategoryModel objCategoryModel;

                            if (objJsonArray.getJSONObject(index).getString("id").equals(strCategoryId)) {
                                objCategoryModel = new EditCategoryModel(
                                        objJsonArray.getJSONObject(index).getString("id"),
                                        objJsonArray.getJSONObject(index).getString("title"),
                                        Constants.HTTP_URL + objJsonArray.getJSONObject(index).getString("image"),
                                        Constants.HTTP_URL + objJsonArray.getJSONObject(index).getString("selected_image"),
                                        objJsonArray.getJSONObject(index).getString("created"),
                                        1
                                );
                            } else {
                                objCategoryModel = new EditCategoryModel(
                                        objJsonArray.getJSONObject(index).getString("id"),
                                        objJsonArray.getJSONObject(index).getString("title"),
                                        Constants.HTTP_URL + objJsonArray.getJSONObject(index).getString("image"),
                                        Constants.HTTP_URL + objJsonArray.getJSONObject(index).getString("selected_image"),
                                        objJsonArray.getJSONObject(index).getString("created"),
                                        0
                                );
                            }
                            arrCategoryModel.add(objCategoryModel);
                        }
                        objEditCategoryAdapter = new EditCategoryAdapter(EditShoutActivity.this, arrCategoryModel);
                        objCategoryGridView.setAdapter(objEditCategoryAdapter);
                        objCategoryGridView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void hideKeyboard(View view) {
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
