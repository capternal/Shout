package com.shout.shoutapplication.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.ApplicationUtils;
import com.shout.shoutapplication.Utils.CallWebService;
import com.shout.shoutapplication.Utils.ConnectivityBroadcastReceiver;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.ImageUtils;
import com.shout.shoutapplication.Utils.KeyboardUtils;
import com.shout.shoutapplication.Utils.NetworkUtils;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.app.AppController;
import com.shout.shoutapplication.base.BaseActivity;
import com.shout.shoutapplication.database.DatabaseHelper;
import com.shout.shoutapplication.login.ProfileScreenActivity;
import com.shout.shoutapplication.main.Adapter.LikedShoutersAdapter;
import com.shout.shoutapplication.main.Adapter.ShoutCommentsAdapter;
import com.shout.shoutapplication.main.Adapter.ShoutDetailViewPagerAdapter;
import com.shout.shoutapplication.main.EditShout.EditShoutActivity;
import com.shout.shoutapplication.main.Model.LikedShoutersModel;
import com.shout.shoutapplication.main.Model.ShoutCommentModel;
import com.shout.shoutapplication.main.Model.ShoutDefaultListModel;
import com.shout.shoutapplication.main.Model.ShoutDetailViewPagerModel;
import com.shout.shoutapplication.main.Model.ShoutHorizontalListModel;
import com.shout.shoutapplication.others.CircleTransform;
import com.shout.shoutapplication.others.SoftKeyboard;
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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CapternalSystems on 7/5/2016.
 */
public class ShoutDetailActivity extends BaseActivity implements View.OnClickListener, CallWebService.WebserviceResponse {

    public static final String EDIT_SHOUT_ID_KEY = "shout_id";
    public static final String EDIT_SHOUT_TITLE = "shout_title";
    public static final String EDIT_SHOUT_DESCRIPTION = "shout_description";
    public static final String EDIT_SHOUT_TYPE = "shout_type";
    public static final String EDIT_SHOUT_SPECIFY_DATE = "shout_spesify_date";
    public static final String EDIT_SHOUT_IS_SEARCHABLE = "shout_is_searchable";
    public static final String EDIT_SHOUT_LATITUDE = "shout_latitude";
    public static final String EDIT_SHOUT_LONGITUDE = "shout_longitude";
    public static final String EDIT_SHOUT_ADDRESS = "shout_address";
    public static final String EDIT_SHOUT_CATEGORY_NAME = "shout_category_name";
    public static final String EDIT_SHOUT_CATEGORY_ID = "shout_category_id";
    int is_shout_liked;
    SharedPreferences objSharedPreferences;
    SoftKeyboard softKeyboard;
    String strShoutId = "";
    String strShoutType = "";
    String strShoutTypeForEdit = "";
    String strUserProfileUrl = "";
    String strShoutLikeCount = "";
    String strShoutCreatedDate = "";
    String strShoutTitle = "";
    String strShoutDescription = "";
    String strShoutUsername = "";
    String strShoutHiddenStatus = "";
    String strShoutIsSearchable = "";
    String strShoutLatitude = "";
    String strShoutLongitude = "";
    String strShoutAddress = "";
    String strShoutCategoryName = "";
    String strShoutCategoryId = "";
    String strIsHide = "";
    String strStartDate = "";
    String strEndDate = "";
    String strReshout = "";
    String strContinueChat = "";
    String strDistance = "";
    String strShoutResourceJson = "";
    boolean is_nested_call = false;
    boolean keyBoardOpen = false;
    int intResourcePosition = 0;
    Uri strResourcePath = null;
    Uri videoUri = null;
    int intCommentCount = 0;
    ArrayList<ShoutDetailViewPagerModel> arrShoutDetailViewPagerModel = new ArrayList<ShoutDetailViewPagerModel>();
    ArrayList<Uri> arrResourcePath = new ArrayList<Uri>();
    ArrayList<String> arrResourceType = new ArrayList<String>();
    ArrayList<ShoutCommentModel> arrTempShoutCommentsModel;
    ArrayList<ShoutCommentModel> arrShoutCommentsModel = new ArrayList<ShoutCommentModel>();
    String strRecentUploadingComment = "";
    DatabaseHelper objDatabaseHelper;
    ShoutDefaultListModel objShoutDefaultModel;
    // LIKED SHOUTERS INFO VARIABLES
    PopupWindow objPopupWindowLargeSource;
    LayoutInflater objPopupInflater;
    View customLikeView;
    ImageView objImageLike;
    TextView objTextViewLike;
    RelativeLayout objRelativeLayoutLikeView;
    ListView objListViewShouters;
    TextView objTextViewNoLike;
    ArrayList<LikedShoutersModel> arrLikedShoutersModel = new ArrayList<LikedShoutersModel>();
    LikedShoutersAdapter objLikedShoutersAdapter;
    TextView objTextViewDateHeader;
    private TextView btnBackToShoutBook;
    private TextView objTextMessage;
    private ImageView imgBack;
    private LinearLayout LinearBackToShoutBook;
    private Button btnCanHelp;
    private Button btnOpenResourceSelector;
    private EditText objEditTextCommentHere;
    private ImageView objFetchableImageView;
    private HorizontalScrollView objFriendsSoFarList;
    private ScrollView objParentScrollView;
    private ImageView objImageViewFacebook;
    private ImageView objImageViewMail;
    private ImageView objImageViewMakeComment;
    private ImageView objImageViewTwitter;
    private ImageView objImageViewWhatsApp;
    private ListView objListViewShoutComments;
    private RelativeLayout objRelativeNoShoutImageAvailableWithDifferentLogin;
    private RelativeLayout objRelativeNoShoutImageAvailableWithSameLogin;
    private RelativeLayout objRootLayout;
    private TextView objTextViewCreatedDate;
    private TextView objTextViewAddress;
    private TextView objTextViewDistance;
    private TextView objTextViewDescription;
    private TextView objTextViewEdit;
    private TextView objTextViewEndDate;
    private TextView objTextViewHide;
    private ImageView imgShoutHideShow;
    private ImageView imgEditShout;
    private TextView objTextViewLikeShout;
    private ImageView objImageViewLikeShout;
    private TextView objTextViewReport;
    private TextView objTextViewShare;
    private TextView objTextViewStartDate;
    private TextView objTextViewTitle;
    private TextView objTextViewUserName;
    private View objViewIndicator1;
    private View objViewIndicator2;
    private View objViewIndicator3;
    private View objViewIndicator4;
    private View objViewIndicator5;
    private ViewPager objViewPagerUploads;
    private RelativeLayout objRelativeLayoutLargeResource;
    private ViewPager objViewPagerLargeResource;
    private View objViewIndicator1_large;
    private View objViewIndicator2_large;
    private View objViewIndicator3_large;
    private View objViewIndicator4_large;
    private View objViewIndicator5_large;
    private LinearLayout objLinearLayoutShare;
    private LinearLayout objLinearLayoutReport;
    private LinearLayout objLinearLayoutHide;
    private LinearLayout objLinearLayoutEdit;
    private ApplicationUtils applicationUtils;
    private Uri mCapturedImageURI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shout_detail);

        applicationUtils = new ApplicationUtils(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp((Context) this);
        objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, 0);
        strShoutId = objSharedPreferences.getString(Constants.SHOUT_ID_FOR_DETAIL_SCREEN, "");
        /*is_shout_liked = Integer.parseInt(objSharedPreferences.getString(Constants.IS_SHOUT_LIKED, ""));
        if (objSharedPreferences.getString(Constants.USER_ID_FOR_DETAIL_SCREEN, "").equals(objSharedPreferences.getString(Constants.USER_ID, ""))) {
            strShoutType = "NRNL";
        } else {
            strShoutType = objSharedPreferences.getString(Constants.SHOUT_TYPE_FOR_DETAIL_SCREEN, "");
        }
        strShoutTypeForEdit = objSharedPreferences.getString(Constants.SHOUT_TYPE_FOR_DETAIL_SCREEN, "");
        strUserProfileUrl = objSharedPreferences.getString(Constants.USER_PROFILE_URL_FOR_DETAIL_SCREEN, "");
        strShoutLikeCount = objSharedPreferences.getString(Constants.SHOUT_LIKE_COUNT, "");
        strShoutCreatedDate = objSharedPreferences.getString(Constants.SHOUT_CREATED_DATE, "");
        strShoutTitle = objSharedPreferences.getString(Constants.SHOUT_TITLE, "");
        strShoutDescription = objSharedPreferences.getString(Constants.SHOUT_DESCRIPTION, "");
        strShoutUsername = objSharedPreferences.getString(Constants.SHOUT_USER_NAME, "");
        strShoutHiddenStatus = objSharedPreferences.getString(Constants.SHOUT_HIDDEN_STATUS, "");*/

        hideBothTopHeader();
        hideBottomTabs();
        System.out.println("PRINT SHOUT ID : " + strShoutId);

        objDatabaseHelper = new DatabaseHelper(ShoutDetailActivity.this);
        objShoutDefaultModel = objDatabaseHelper.getShoutDetails(strShoutId);

        is_shout_liked = objShoutDefaultModel.getIS_SHOUT_LIKED();
        strShoutType = objShoutDefaultModel.getSHOUT_TYPE();
        strShoutTypeForEdit = objShoutDefaultModel.getSHOUT_TYPE();
        strUserProfileUrl = objShoutDefaultModel.getPROFILE_IMAGE_URL();
        strShoutLikeCount = objShoutDefaultModel.getLIKE_COUNT();
        strShoutLikeCount = objSharedPreferences.getString(Constants.SHOUT_LIKE_COUNT, "");
        strShoutCreatedDate = objShoutDefaultModel.getDATE_TIME();
        strShoutTitle = objShoutDefaultModel.getTITLE();
        strShoutDescription = objShoutDefaultModel.getDESCRIPTION();
        strShoutUsername = objShoutDefaultModel.getUSER_NAME();
        strShoutHiddenStatus = String.valueOf(objShoutDefaultModel.getIS_SHOUT_HIDDEN());
        strShoutIsSearchable = objShoutDefaultModel.getIS_SEARCHABLE();
        strShoutLatitude = objShoutDefaultModel.getSHOUT_LATITUDE();
        strShoutLongitude = objShoutDefaultModel.getSHOUT_LONGITUDE();
        strShoutAddress = objShoutDefaultModel.getSHOUT_ADDRESS();
        strShoutCategoryName = objShoutDefaultModel.getSHOUT_CATEGORY_NAME();
        strShoutCategoryId = objShoutDefaultModel.getSHOUT_CATEGORY_ID();
        strIsHide = objShoutDefaultModel.getIS_HIDE();
        strStartDate = objShoutDefaultModel.getSTART_DATE();
        strEndDate = objShoutDefaultModel.getEND_DATE();
        strReshout = objShoutDefaultModel.getRE_SHOUT();
        strContinueChat = objShoutDefaultModel.getCONTINUE_CHAT();
        strDistance = objShoutDefaultModel.getDISTANCE();
        strShoutResourceJson = objShoutDefaultModel.getStrShoutImages();
        intCommentCount = Integer.parseInt(objShoutDefaultModel.getCOMMENT_COUNT());

        System.out.println("PRASAD 1: " + strShoutType);
        System.out.println("SHOUT DETAIL COMMENT COUNT : " + intCommentCount);
        System.out.println("START DATE : " + strStartDate);
        System.out.println("END DATE : " + strEndDate);

        initialize();

        try{

            objTextViewAddress.setText(strShoutAddress);
            objTextViewDistance.setText(strDistance);
            if (strStartDate.equals("false")) {
                objTextViewStartDate.setVisibility(TextView.GONE);
                TextView objTextTo = (TextView) findViewById(R.id.txt_shout_detail_to);
                objTextTo.setVisibility(TextView.GONE);
            } else {
                objTextViewStartDate.setVisibility(TextView.VISIBLE);
                objTextViewStartDate.setText(strStartDate);
                TextView objTextTo = (TextView) findViewById(R.id.txt_shout_detail_to);
                objTextTo.setVisibility(TextView.VISIBLE);
            }

            if (strEndDate.equals("false")) {
                objTextViewEndDate.setVisibility(TextView.GONE);
                TextView objTextTo = (TextView) findViewById(R.id.txt_shout_detail_to);
                objTextTo.setVisibility(TextView.GONE);
            } else {
                objTextViewEndDate.setVisibility(TextView.VISIBLE);
                objTextViewEndDate.setText(strEndDate);
                TextView objTextTo = (TextView) findViewById(R.id.txt_shout_detail_to);
                objTextTo.setVisibility(TextView.VISIBLE);
            }
            if (strShoutType.equals("R")) {
                objTextViewDateHeader.setText("NEEDED FOR");
            } else if (strShoutType.equals("L")) {
                objTextViewDateHeader.setText("AVAILABLE FOR");
            }
            objTextViewDateHeader.setVisibility(TextView.VISIBLE);
            if (strStartDate.equals("false") && strEndDate.equals("false")) {
                System.out.println("");
                objTextViewDateHeader.setVisibility(TextView.GONE);
            }
            System.out.println("DATE HEADER VISIBILITY : " + objTextViewDateHeader.getVisibility());

            if (objShoutDefaultModel.getUSER_ID().equals(objSharedPreferences.getString(Constants.USER_ID, ""))) {
                if (strReshout.equals("0")) {
                    btnCanHelp.setBackgroundColor(Color.parseColor("#444444"));
                    btnCanHelp.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    btnCanHelp.setBackgroundColor(getResources().getColor(R.color.red_background_color));
                    btnCanHelp.setTextColor(Color.parseColor("#FFFFFF"));
                }
                btnCanHelp.setText("RESHOUT\n(" + strReshout + " LEFT)");
                objLinearLayoutReport.setVisibility(LinearLayout.GONE);
                objLinearLayoutEdit.setVisibility(LinearLayout.VISIBLE);
                objLinearLayoutHide.setVisibility(LinearLayout.VISIBLE);
            } else {
                objLinearLayoutReport.setVisibility(LinearLayout.VISIBLE);
                objLinearLayoutEdit.setVisibility(LinearLayout.GONE);
                objLinearLayoutHide.setVisibility(LinearLayout.GONE);
                if (strShoutType.equals("R")) {
                    btnCanHelp.setText("I CAN HELP!");
                    objTextViewDateHeader.setText("NEEDED FOR");
                } else if (strShoutType.equals("L")) {
                    btnCanHelp.setText("I NEED IT!");
                    objTextViewDateHeader.setText("AVAILABLE FOR");
                }
                System.out.println("CONTINUE CHAT : " + strContinueChat);
                if (strContinueChat.equals("1")) {
                    btnCanHelp.setText("CONTINUE CHAT");
                }
            }

            if (objSharedPreferences.getString(Constants.USER_ID_FOR_DETAIL_SCREEN, "").equals(objSharedPreferences.getString(Constants.USER_ID, ""))) {
                objLinearLayoutReport.setVisibility(LinearLayout.GONE);
                objLinearLayoutEdit.setVisibility(LinearLayout.VISIBLE);
                objLinearLayoutHide.setVisibility(LinearLayout.VISIBLE);

                if (objShoutDefaultModel.getIS_SHOUT_HIDDEN() == 0) {
                    imgShoutHideShow.setImageResource(R.drawable.shout_hide);
                    objTextViewHide.setText("HIDE");
                } else {
                    imgShoutHideShow.setImageResource(R.drawable.shout_show);
                    objTextViewHide.setText("SHOW");
                }
            } else {
                objLinearLayoutReport.setVisibility(LinearLayout.VISIBLE);
                objLinearLayoutEdit.setVisibility(LinearLayout.GONE);
                objLinearLayoutHide.setVisibility(LinearLayout.GONE);
                if (strShoutType.equals("R")) {
                    btnCanHelp.setText("I CAN HELP!");
                } else if (strShoutType.equals("L")) {
                    btnCanHelp.setText("I NEED IT!");
                }
                System.out.println("CONTINUE CHAT : " + strContinueChat);
                if (strContinueChat.equals("1")) {
                    btnCanHelp.setText("CONTINUE CHAT");
                }
            }

            objTextMessage.setVisibility(TextView.VISIBLE);

            if (strShoutTypeForEdit.equals("R"))

            {
                if (objSharedPreferences.getString(Constants.USER_ID_FOR_DETAIL_SCREEN, "").equals(objSharedPreferences.getString(Constants.USER_ID, ""))) {
                    objTextMessage.setText("No shouters so far");
                } else {
                    objTextMessage.setText("Be the first one to help");
                }
            } else if (strShoutTypeForEdit.equals("L"))

            {
                if (objSharedPreferences.getString(Constants.USER_ID_FOR_DETAIL_SCREEN, "").equals(objSharedPreferences.getString(Constants.USER_ID, ""))) {
                    objTextMessage.setText("No shouters so far");
                } else {
                    objTextMessage.setText("Be the first one to ask ");
                }
            }
            arrTempShoutCommentsModel = new ArrayList<ShoutCommentModel>();
            showLoadingCommentUI(intCommentCount, 0);
            objTextViewCreatedDate.setText(strShoutCreatedDate);
            Typeface custom_font = Typeface.createFromAsset(getAssets(), "AvenirNext-Medium.ttf");
            objTextViewDescription.setText(strShoutDescription);
            objTextViewDescription.setTypeface(custom_font);
            objTextViewTitle.setText(strShoutTitle);
            String strNameSpliter[] = strShoutUsername.split(" ");
            objTextViewUserName.setText(strNameSpliter[0] + " " + strNameSpliter[1].charAt(0));

        }catch(NullPointerException ne){
            ne.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

        try {
            System.out.println("RESOURCE JSON ARRAY : " + strShoutResourceJson);
            System.out.println("PRASAD 2: " + strShoutType);
            arrShoutDetailViewPagerModel = new ArrayList();
            JSONArray jSONArray = new JSONArray(strShoutResourceJson);
            for (int i = 0; i < jSONArray.length(); i++) {
                showIndicator(i);
                arrShoutDetailViewPagerModel.add(new ShoutDetailViewPagerModel(
                        jSONArray.getJSONObject(i).getString("id"),
                        Constants.HTTP_URL + jSONArray.getJSONObject(i).getString("image_url"),
                        jSONArray.getJSONObject(i).getString("image_type"),
                        Constants.HTTP_URL + jSONArray.getJSONObject(i).getString("video_url"),
                        jSONArray.getJSONObject(i).getString("video_local_path")));
            }
            System.out.println("SHOUT DETAIL VIEWPAGER MODEL ARRAY : " + arrShoutDetailViewPagerModel);
            if (arrShoutDetailViewPagerModel.size() > 0) {
                objViewPagerUploads.setVisibility(TextView.VISIBLE);
                objRelativeNoShoutImageAvailableWithSameLogin.setVisibility(RelativeLayout.GONE);
                objViewPagerUploads.setAdapter(new ShoutDetailViewPagerAdapter(ShoutDetailActivity.this, arrShoutDetailViewPagerModel));
                objViewPagerLargeResource.setAdapter(new ShoutDetailViewPagerAdapter(ShoutDetailActivity.this, arrShoutDetailViewPagerModel));

                objViewPagerUploads.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        setIndicatorView(position);
                        objViewPagerLargeResource.setCurrentItem(position);
                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                objViewPagerLargeResource.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        setIndicatorView(position);
                        intResourcePosition = position;
                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            } else {
                System.out.println("PRASAD 3: " + strShoutType);
                if (objSharedPreferences.getString(Constants.USER_ID, "").equals(objShoutDefaultModel.getUSER_ID())) {
                    objRelativeNoShoutImageAvailableWithSameLogin.setVisibility(RelativeLayout.VISIBLE);
                    objRelativeNoShoutImageAvailableWithDifferentLogin.setVisibility(RelativeLayout.GONE);
                } else {
                    objRelativeNoShoutImageAvailableWithSameLogin.setVisibility(RelativeLayout.GONE);
                    objRelativeNoShoutImageAvailableWithDifferentLogin.setVisibility(RelativeLayout.VISIBLE);
                }
                objViewPagerUploads.setVisibility(ViewPager.GONE);
            }


        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try

        {
            System.out.println("SHOUT ID : " + strShoutId);
            String tag_json_obj = "json_obj_req";

            JSONObject objJsonObject = new JSONObject();
            objJsonObject.put("shout_id", strShoutId);
            objJsonObject.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.VIEW_SHOUT_API, objJsonObject
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("RESPONSE : " + response.toString());
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

        try {
            new CallWebService(Constants.SHOUT_COMMENTS_API, new JSONObject().put("shout_id", strShoutId), ShoutDetailActivity.this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject objJsonObject = new JSONObject();
            objJsonObject.put("id", strShoutId);
            objJsonObject.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));
            new CallWebService(Constants.SHOUTERS_API, objJsonObject, ShoutDetailActivity.this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                Log.d("keyboard", "keyboard visible: " + isVisible);
                if (isVisible) {
                    keyBoardOpen = true;
                    btnCanHelp.setVisibility(Button.GONE);
                } else {
                    keyBoardOpen = false;
                    if (objRelativeLayoutLargeResource.getVisibility() == RelativeLayout.VISIBLE) {
                        btnCanHelp.setVisibility(Button.GONE);
                    } else {
                        btnCanHelp.setVisibility(Button.VISIBLE);
                    }
                }
            }
        });
    }

    public static void setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

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

        if (type == CreateShoutActivity.MEDIA_TYPE_VIDEO) {

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");

        } else {
            return null;
        }

        return mediaFile;
    }

    private void setLocalData() {
        objDatabaseHelper = new DatabaseHelper(ShoutDetailActivity.this);
        objShoutDefaultModel = objDatabaseHelper.getShoutDetails(strShoutId);

        is_shout_liked = objShoutDefaultModel.getIS_SHOUT_LIKED();
        if (objShoutDefaultModel.getUSER_ID().equals(objSharedPreferences.getString(Constants.USER_ID, ""))) {
            strShoutType = "NRNL";
        } else {
            strShoutType = objShoutDefaultModel.getSHOUT_TYPE();
        }
        strShoutTypeForEdit = objShoutDefaultModel.getSHOUT_TYPE();
        strUserProfileUrl = objShoutDefaultModel.getPROFILE_IMAGE_URL();
        strShoutLikeCount = objShoutDefaultModel.getLIKE_COUNT();
        strShoutLikeCount = objSharedPreferences.getString(Constants.SHOUT_LIKE_COUNT, "");
        strShoutCreatedDate = objShoutDefaultModel.getDATE_TIME();
        strShoutTitle = objShoutDefaultModel.getTITLE();
        strShoutDescription = objShoutDefaultModel.getDESCRIPTION();
        strShoutUsername = objShoutDefaultModel.getUSER_NAME();
        strShoutHiddenStatus = String.valueOf(objShoutDefaultModel.getIS_SHOUT_HIDDEN());
        strShoutIsSearchable = objShoutDefaultModel.getIS_SEARCHABLE();
        strShoutLatitude = objShoutDefaultModel.getSHOUT_LATITUDE();
        strShoutLongitude = objShoutDefaultModel.getSHOUT_LONGITUDE();
        strShoutAddress = objShoutDefaultModel.getSHOUT_ADDRESS();
        strShoutCategoryName = objShoutDefaultModel.getSHOUT_CATEGORY_NAME();
        strShoutCategoryId = objShoutDefaultModel.getSHOUT_CATEGORY_ID();
        strIsHide = objShoutDefaultModel.getIS_HIDE();
        strStartDate = objShoutDefaultModel.getSTART_DATE();
        strEndDate = objShoutDefaultModel.getEND_DATE();
        strReshout = objShoutDefaultModel.getRE_SHOUT();
        strContinueChat = objShoutDefaultModel.getCONTINUE_CHAT();
        strDistance = objShoutDefaultModel.getDISTANCE();
        strShoutResourceJson = objShoutDefaultModel.getStrShoutImages();
        intCommentCount = Integer.parseInt(objShoutDefaultModel.getCOMMENT_COUNT());
        System.out.println("SHOUT DETAIL COMMENT COUNT : " + intCommentCount);
        System.out.println("START DATE : " + strStartDate);
        System.out.println("END DATE : " + strEndDate);
    }

    private void showLoadingCommentUI(int intTempCommentCount, int flag) {
        if (intTempCommentCount > 0) {
            if (flag == 0) {
                for (int index = 0; index < intTempCommentCount; index++) {
                    arrTempShoutCommentsModel.add(new ShoutCommentModel(
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""));
                }
                objListViewShoutComments.setAdapter(new ShoutCommentsAdapter(arrTempShoutCommentsModel, ShoutDetailActivity.this));
                ShoutDetailActivity.setListViewHeightBasedOnItems(objListViewShoutComments);
            } else {
                arrTempShoutCommentsModel.clear();
                objListViewShoutComments.setAdapter(new ShoutCommentsAdapter(arrTempShoutCommentsModel, ShoutDetailActivity.this));
                ShoutDetailActivity.setListViewHeightBasedOnItems(objListViewShoutComments);
                arrTempShoutCommentsModel.add(new ShoutCommentModel(
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""));
                arrTempShoutCommentsModel = arrShoutCommentsModel;
                objListViewShoutComments.setAdapter(new ShoutCommentsAdapter(arrTempShoutCommentsModel, ShoutDetailActivity.this));
                ShoutDetailActivity.setListViewHeightBasedOnItems(objListViewShoutComments);
            }
        }
    }

    private void showIndicator(int i) {
        switch (i) {
            case 0:
                objViewIndicator1.setVisibility(View.VISIBLE);
                objViewIndicator1_large.setVisibility(View.VISIBLE);
                break;
            case 1:
                objViewIndicator2.setVisibility(View.VISIBLE);
                objViewIndicator2_large.setVisibility(View.VISIBLE);
                break;
            case 2:
                objViewIndicator3.setVisibility(View.VISIBLE);
                objViewIndicator3_large.setVisibility(View.VISIBLE);
                break;
            case 3:
                objViewIndicator4.setVisibility(View.VISIBLE);
                objViewIndicator4_large.setVisibility(View.VISIBLE);
                break;
            case 4:
                objViewIndicator5.setVisibility(View.VISIBLE);
                objViewIndicator5_large.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void initialize() {

        objTextViewDateHeader = (TextView) findViewById(R.id.txt_date_header_shout_detail);
        objTextViewAddress = (TextView) findViewById(R.id.txt_address_shout_detail);
        objTextViewDistance = (TextView) findViewById(R.id.txt_distance_shout_detail);
        objTextMessage = (TextView) findViewById(R.id.txt_no_shouters_shout_detail);
        btnBackToShoutBook = (TextView) findViewById(R.id.btn_shout_detail_back);
        imgBack = (ImageView) findViewById(R.id.image_back_shout_detail);
        LinearBackToShoutBook = (LinearLayout) findViewById(R.id.linear_shout_detail_back);

        btnCanHelp = (Button) findViewById(R.id.btn_shout_detail_can_help);
        objFetchableImageView = (ImageView) findViewById(R.id.fetchable_image_shout_detail);
        objParentScrollView = (ScrollView) findViewById(R.id.scrollview_shout_detail);
        objFriendsSoFarList = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview_shout_detail);
        objListViewShoutComments = (ListView) findViewById(R.id.listview_shout_detail_user_help_list);

        objTextViewShare = (TextView) findViewById(R.id.txt_shout_detail_share);
        objTextViewReport = (TextView) findViewById(R.id.txt_shout_detail_report);
        objTextViewHide = (TextView) findViewById(R.id.txt_shout_detail_hide);
        objTextViewEdit = (TextView) findViewById(R.id.txt_shout_detail_edit);

        imgShoutHideShow = (ImageView) findViewById(R.id.img_shout_hide_shout_detail);
        imgEditShout = (ImageView) findViewById(R.id.image_edit_shout_detail);

        objLinearLayoutShare = (LinearLayout) findViewById(R.id.linear_shout_detail_share);
        objLinearLayoutReport = (LinearLayout) findViewById(R.id.linear_shout_detail_report);
        objLinearLayoutHide = (LinearLayout) findViewById(R.id.linear_shout_detail_hide);
        objLinearLayoutEdit = (LinearLayout) findViewById(R.id.linear_shout_detail_edit);

        objLinearLayoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    /*Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[0]);
                    i.putExtra(Intent.EXTRA_SUBJECT, "Shout App");
                    i.putExtra(Intent.EXTRA_TEXT, "Try the new social app called Shout app.");
                    startActivity(i);*/

                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, "Shout Application.");
                    startActivity(Intent.createChooser(share, "Shout App"));

                    /*try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (ActivityNotFoundException ex) {
                        Toast.makeText(ShoutDetailActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                    Utils.d("SHARE", "MAIL");*/
                } catch (Exception e22) {
                    e22.printStackTrace();
                }
            }
        });

        imgEditShout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushToEditShoutScreen();
            }
        });

        objTextViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushToEditShoutScreen();
            }
        });

        objLinearLayoutEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushToEditShoutScreen();
            }
        });

        objTextViewUserName = (TextView) findViewById(R.id.txt_user_name_shout_details);
        objTextViewTitle = (TextView) findViewById(R.id.txt_shout_title_shout_detail);
        objTextViewDescription = (TextView) findViewById(R.id.txt_shout_description_shout_detail);
        objTextViewStartDate = (TextView) findViewById(R.id.txt_start_date_shout_detail);
        objTextViewEndDate = (TextView) findViewById(R.id.txt_end_date_shout_detail);
        objTextViewCreatedDate = (TextView) findViewById(R.id.txt_created_date_shout_detail);
        objTextViewLikeShout = (TextView) findViewById(R.id.txt_like_shout_detail);
        objImageViewLikeShout = (ImageView) findViewById(R.id.image_like_shout_detail);
        objViewPagerUploads = (ViewPager) findViewById(R.id.viewpager_shout_detail);
        objEditTextCommentHere = (EditText) findViewById(R.id.edt_comment_here_shout_detail);
        objImageViewMakeComment = (ImageView) findViewById(R.id.image_comment_shout_detail);
        objRelativeNoShoutImageAvailableWithSameLogin = (RelativeLayout) findViewById(R.id.relative_no_shout_image_available_with_same_login_shout_detail);
        btnOpenResourceSelector = (Button) findViewById(R.id.btn_add_photo_shout_detail);
        objRelativeNoShoutImageAvailableWithDifferentLogin = (RelativeLayout) findViewById(R.id.relative_no_shout_image_available_shout_detail);
        objImageViewWhatsApp = (ImageView) findViewById(R.id.image_whats_app_shout_detail);
        objImageViewFacebook = (ImageView) findViewById(R.id.image_facebook_shout_detail);
        objImageViewTwitter = (ImageView) findViewById(R.id.image_twitter_shout_detail);
        objImageViewMail = (ImageView) findViewById(R.id.image_mail_shout_detail);
        objViewIndicator1 = findViewById(R.id.view_indicator_1);
        objViewIndicator2 = findViewById(R.id.view_indicator_2);
        objViewIndicator3 = findViewById(R.id.view_indicator_3);
        objViewIndicator4 = findViewById(R.id.view_indicator_4);
        objViewIndicator5 = findViewById(R.id.view_indicator_5);

        objRelativeLayoutLargeResource = (RelativeLayout) findViewById(R.id.relativeLayout_shout_detail_large_resource);
        objViewPagerLargeResource = (ViewPager) findViewById(R.id.viewpager_large_shout_detail);
        objViewIndicator1_large = (View) findViewById(R.id.view_indicator_1_large);
        objViewIndicator2_large = (View) findViewById(R.id.view_indicator_2_large);
        objViewIndicator3_large = (View) findViewById(R.id.view_indicator_3_large);
        objViewIndicator4_large = (View) findViewById(R.id.view_indicator_4_large);
        objViewIndicator5_large = (View) findViewById(R.id.view_indicator_5_large);

        if (Integer.parseInt(strShoutLikeCount) > 0) {
            objImageViewLikeShout.setBackgroundResource(R.drawable.like_red);
            objTextViewLikeShout.setText(strShoutLikeCount.concat(" LIKE"));
            objTextViewLikeShout.setTextColor(getResources().getColor(R.color.text_color_shout_default_selected));
        } else {
            objImageViewLikeShout.setBackgroundResource(R.drawable.like_grey);
            objTextViewLikeShout.setTextColor(getResources().getColor(R.color.text_color_shout_default_un_select));
        }
        if (strUserProfileUrl.length() > 0) {
            Picasso.with(this).load(strUserProfileUrl).transform(new CircleTransform()).into(objFetchableImageView);
            objFetchableImageView.setPadding(Constants.DEFAULT_CIRCLE_PADDING, Constants.DEFAULT_CIRCLE_PADDING, Constants.DEFAULT_CIRCLE_PADDING, Constants.DEFAULT_CIRCLE_PADDING);
        }

        objFetchableImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityBroadcastReceiver.isConnected()) {
                    SharedPreferences.Editor objProfileEditor = objSharedPreferences.edit();
                    objProfileEditor.putString(Constants.PROFILE_SCREEN_USER_ID, objSharedPreferences.getString(Constants.USER_ID_FOR_DETAIL_SCREEN, ""));
//                objProfileEditor.putString(Constants.PROFILE_BACK_SCREEN_NAME, Constants.SHOUT_DETAIL_SCREEN);
                    objProfileEditor.commit();

                    Intent objIntent = new Intent(ShoutDetailActivity.this, ProfileScreenActivity.class);
                    startActivity(objIntent);
                    overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                    finish();
                } else {
                    Constants.showInternetToast(ShoutDetailActivity.this);
                }
            }
        });
        setListener();
    }

    private void pushToEditShoutScreen() {
        if (ConnectivityBroadcastReceiver.isConnected()) {
            Intent objIntent = new Intent(ShoutDetailActivity.this, EditShoutActivity.class);
            objIntent.putExtra(ShoutDetailActivity.EDIT_SHOUT_ID_KEY, strShoutId);
            objIntent.putExtra(ShoutDetailActivity.EDIT_SHOUT_TITLE, strShoutTitle);
            objIntent.putExtra(ShoutDetailActivity.EDIT_SHOUT_DESCRIPTION, strShoutDescription);
            objIntent.putExtra(ShoutDetailActivity.EDIT_SHOUT_TYPE, strShoutTypeForEdit);
            if (objTextViewStartDate.getVisibility() == TextView.VISIBLE && objTextViewEndDate.getVisibility() == TextView.VISIBLE) {
                objIntent.putExtra(ShoutDetailActivity.EDIT_SHOUT_SPECIFY_DATE, objTextViewStartDate.getText() + ":" + objTextViewEndDate.getText());
            } else {
                objIntent.putExtra(ShoutDetailActivity.EDIT_SHOUT_SPECIFY_DATE, "false");
            }
            objIntent.putExtra(ShoutDetailActivity.EDIT_SHOUT_IS_SEARCHABLE, strShoutIsSearchable);
            objIntent.putExtra(ShoutDetailActivity.EDIT_SHOUT_LATITUDE, strShoutLatitude);
            objIntent.putExtra(ShoutDetailActivity.EDIT_SHOUT_LONGITUDE, strShoutLongitude);
            objIntent.putExtra(ShoutDetailActivity.EDIT_SHOUT_ADDRESS, strShoutAddress);
            objIntent.putExtra(ShoutDetailActivity.EDIT_SHOUT_CATEGORY_NAME, strShoutCategoryName);
            objIntent.putExtra(ShoutDetailActivity.EDIT_SHOUT_CATEGORY_ID, strShoutCategoryId);
            startActivity(objIntent);
        } else {
            Constants.showInternetToast(ShoutDetailActivity.this);
        }
    }

    private void setListener() {
        btnBackToShoutBook.setOnClickListener(this);
        btnBackToShoutBook.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        LinearBackToShoutBook.setOnClickListener(this);
        objTextViewReport.setOnClickListener(this);
//        objTextViewShare.setOnClickListener(this);
        objTextViewLikeShout.setOnClickListener(this);
        objImageViewLikeShout.setOnClickListener(this);
        objImageViewMakeComment.setOnClickListener(this);
        btnCanHelp.setOnClickListener(this);
        objImageViewWhatsApp.setOnClickListener(this);
        objImageViewFacebook.setOnClickListener(this);
        objImageViewTwitter.setOnClickListener(this);
        objImageViewMail.setOnClickListener(this);
        imgShoutHideShow.setOnClickListener(this);
        objTextViewHide.setOnClickListener(this);
        btnOpenResourceSelector.setOnClickListener(this);
        objViewPagerUploads.setOnClickListener(this);

        final GestureDetector objGestureDetector = new GestureDetector(this, new TapGestureListener());

        objViewPagerUploads.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                objGestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }

    @Override
    public void onWebserviceResponce(String strUrl, String strResult) {
        if (strUrl.equals(Constants.SHOUT_COMMENTS_API)) {
            parseCommentsApiResponse(strResult);
        } else if (strUrl.equals(Constants.SHOUTERS_API)) {
            parseShoutersApiResponse(strResult);
        } else if (strUrl.equals(Constants.MAKE_COMMENT_API)) {
            try {
                JSONObject objJsonObject = new JSONObject(strResult);
                if (objJsonObject.getString("result").equals("true")) {
                    // DO NOTHING
                } else {
                    arrShoutCommentsModel.remove(0);
                    objListViewShoutComments.setAdapter(new ShoutCommentsAdapter(arrShoutCommentsModel, ShoutDetailActivity.this));
                    ShoutDetailActivity.setListViewHeightBasedOnItems(objListViewShoutComments);
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void parseShoutersApiResponse(String strResult) {
        try {
            ArrayList<ShoutHorizontalListModel> arrShoutHorizontalListModel = new ArrayList<ShoutHorizontalListModel>();
            JSONObject objJsonObject = new JSONObject(strResult);
            if (objJsonObject.getBoolean("result")) {
                JSONArray objShoutersList = new JSONArray(objJsonObject.getString("shouters"));
                for (int i = 0; i < objShoutersList.length(); i++) {
                    arrShoutHorizontalListModel.add(new ShoutHorizontalListModel(
                            objShoutersList.getJSONObject(i).getString("id"),
                            objShoutersList.getJSONObject(i).getString("name"),
                            objShoutersList.getJSONObject(i).getString("photo")));

                }
                LinearLayout objLinearLayout = new LinearLayout(ShoutDetailActivity.this);
                objLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                if (arrShoutHorizontalListModel.size() > 0) {
                    for (int index = 0; index < arrShoutHorizontalListModel.size(); index++) {
                        final ShoutHorizontalListModel objShoutHorizontalListModel = (ShoutHorizontalListModel) arrShoutHorizontalListModel.get(index);
                        final ImageView imageView = new ImageView(ShoutDetailActivity.this);
                        imageView.setTag(Integer.valueOf(index));
                        LinearLayout.LayoutParams objLinearLayoutParams = new LinearLayout.LayoutParams(convertDpToPx(60), convertDpToPx(60));
                        objLinearLayoutParams.setMargins(10, 0, 10, 0);
                        imageView.setLayoutParams(objLinearLayoutParams);
                        Picasso.with(ShoutDetailActivity.this).load(objShoutHorizontalListModel.getPhoto()).transform(new CircleTransform()).into(imageView);
                        imageView.setBackgroundResource(R.drawable.red_border);
                        imageView.setPadding(Constants.DEFAULT_CIRCLE_PADDING, Constants.DEFAULT_CIRCLE_PADDING, Constants.DEFAULT_CIRCLE_PADDING, Constants.DEFAULT_CIRCLE_PADDING);
                        objLinearLayout.addView(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (objSharedPreferences.getString(Constants.USER_ID_FOR_DETAIL_SCREEN, "").equals(objSharedPreferences.getString(Constants.USER_ID, ""))) {
                                    SharedPreferences objChatPreferences = getSharedPreferences(Constants.CHAT_PREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor objDataChatEditor = objChatPreferences.edit();
                                    objDataChatEditor.putString(Constants.CHAT_APPONENT_ID, objShoutHorizontalListModel.getId());
                                    objDataChatEditor.putString(Constants.CHAT_APPONENT_USER_NAME, objShoutHorizontalListModel.getName());
                                    objDataChatEditor.putString(Constants.CHAT_APPONENT_PROFILE_PIC, objShoutHorizontalListModel.getPhoto());
                                    objDataChatEditor.putString(Constants.CHAT_SHOUT_ID, strShoutId);
                                    objDataChatEditor.putString(Constants.CHAT_SHOUT_TITLE, strShoutTitle);
                                    objDataChatEditor.putString(Constants.CHAT_SHOUT_TYPE, strShoutType);
                                    objDataChatEditor.putString(Constants.CHAT_SHOUT_IMAGE, objSharedPreferences.getString(Constants.SHOUT_SINGLE_IMAGE_FOR_DETAIL, ""));
                                    objDataChatEditor.putString(Constants.CHAT_BACK, Constants.SHOUT_DETAIL_SCREEN);
                                    objDataChatEditor.commit();

                                    Intent objIntent = new Intent(ShoutDetailActivity.this, ChatForShoutActivity.class);
                                    startActivity(objIntent);
                                    overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                                } else {
                                    SharedPreferences.Editor objProfileEditor = objSharedPreferences.edit();
                                    objProfileEditor.putString(Constants.PROFILE_SCREEN_USER_ID, objShoutHorizontalListModel.getId());
                                    objProfileEditor.putString(Constants.PROFILE_BACK_SCREEN_NAME, Constants.SHOUT_DETAIL_SCREEN);
                                    objProfileEditor.commit();

                                    Intent objIntent = new Intent(ShoutDetailActivity.this, ProfileScreenActivity.class);
                                    startActivity(objIntent);
                                    overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                                }


                            }
                        });
                    }
                    objFriendsSoFarList.addView(objLinearLayout);
                    objTextMessage.setVisibility(TextView.GONE);
                    objFriendsSoFarList.setVisibility(HorizontalScrollView.VISIBLE);
                } else {
                    TextView objTextMessage = (TextView) findViewById(R.id.txt_no_shouters_shout_detail);
                    objTextMessage.setVisibility(TextView.VISIBLE);
                    objFriendsSoFarList.setVisibility(HorizontalScrollView.GONE);
                }
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int convertDpToPx(int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, getResources().getDisplayMetrics());
    }

    private void parseCommentsApiResponse(String strResult) {
        try {


//            objListViewShoutComments.setVisibility(ListView.GONE);

//            LinearLayout objLinearLayoutCommentsList = (LinearLayout) findViewById(R.id.listview_shout_detail_user_help_list_linear);

            JSONObject objJsonObject = new JSONObject(strResult.toString());
            if (objJsonObject.getString("result").equals("true")) {
                JSONArray objJsonArrayComments = new JSONArray(objJsonObject.getString("comments"));
                objListViewShoutComments.setAdapter(new ShoutCommentsAdapter(arrShoutCommentsModel, ShoutDetailActivity.this));
                ShoutDetailActivity.setListViewHeightBasedOnItems(objListViewShoutComments);
                arrShoutCommentsModel = new ArrayList<ShoutCommentModel>();
                for (int index = 0; index < objJsonArrayComments.length(); index++) {
                    arrShoutCommentsModel.add(new ShoutCommentModel(
                            objJsonArrayComments.getJSONObject(index).getString("id"),
                            objJsonArrayComments.getJSONObject(index).getString("user_id"),
                            objJsonArrayComments.getJSONObject(index).getString("user_name"),
                            objJsonArrayComments.getJSONObject(index).getString("profile_pic"),
                            objJsonArrayComments.getJSONObject(index).getString("comment"),
                            objJsonArrayComments.getJSONObject(index).getString("created_date")));
                }
                if (arrShoutCommentsModel.size() > 0) {
                    objDatabaseHelper.updateCommentCount(arrShoutCommentsModel.size(), strShoutId);
                }
                objParentScrollView.setEnabled(false);
                objListViewShoutComments.setFocusable(false);
                objListViewShoutComments.setAdapter(null);
                objListViewShoutComments.setAdapter(new ShoutCommentsAdapter(arrShoutCommentsModel, ShoutDetailActivity.this));
                ShoutDetailActivity.setListViewHeightBasedOnItems(objListViewShoutComments);
                objParentScrollView.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setIndicatorView(int viewPosition) {
        switch (viewPosition) {
            case 0:
                objViewIndicator1.setBackgroundResource(R.drawable.red_indicatior_icon);
                objViewIndicator2.setBackgroundResource(R.drawable.grey_indicator_icon);
                objViewIndicator3.setBackgroundResource(R.drawable.grey_indicator_icon);
                objViewIndicator4.setBackgroundResource(R.drawable.grey_indicator_icon);
                objViewIndicator5.setBackgroundResource(R.drawable.grey_indicator_icon);

                objViewIndicator1_large.setBackgroundResource(R.drawable.red_indicatior_icon);
                objViewIndicator2_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                objViewIndicator3_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                objViewIndicator4_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                objViewIndicator5_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                break;
            case 1:
                objViewIndicator1.setBackgroundResource(R.drawable.grey_indicator_icon);
                objViewIndicator2.setBackgroundResource(R.drawable.red_indicatior_icon);
                objViewIndicator3.setBackgroundResource(R.drawable.grey_indicator_icon);
                objViewIndicator4.setBackgroundResource(R.drawable.grey_indicator_icon);
                objViewIndicator5.setBackgroundResource(R.drawable.grey_indicator_icon);

                objViewIndicator1_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                objViewIndicator2_large.setBackgroundResource(R.drawable.red_indicatior_icon);
                objViewIndicator3_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                objViewIndicator4_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                objViewIndicator5_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);

                break;
            case 2:
                objViewIndicator1.setBackgroundResource(R.drawable.grey_indicator_icon);
                objViewIndicator2.setBackgroundResource(R.drawable.grey_indicator_icon);
                objViewIndicator3.setBackgroundResource(R.drawable.red_indicatior_icon);
                objViewIndicator4.setBackgroundResource(R.drawable.grey_indicator_icon);
                objViewIndicator5.setBackgroundResource(R.drawable.grey_indicator_icon);

                objViewIndicator1_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                objViewIndicator2_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                objViewIndicator3_large.setBackgroundResource(R.drawable.red_indicatior_icon);
                objViewIndicator4_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                objViewIndicator5_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                break;
            case 3:
                objViewIndicator1.setBackgroundResource(R.drawable.grey_indicator_icon);
                objViewIndicator2.setBackgroundResource(R.drawable.grey_indicator_icon);
                objViewIndicator3.setBackgroundResource(R.drawable.grey_indicator_icon);
                objViewIndicator4.setBackgroundResource(R.drawable.red_indicatior_icon);
                objViewIndicator5.setBackgroundResource(R.drawable.grey_indicator_icon);

                objViewIndicator1_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                objViewIndicator2_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                objViewIndicator3_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                objViewIndicator4_large.setBackgroundResource(R.drawable.red_indicatior_icon);
                objViewIndicator5_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                break;
            case 4:
                objViewIndicator1.setBackgroundResource(R.drawable.grey_indicator_icon);
                objViewIndicator2.setBackgroundResource(R.drawable.grey_indicator_icon);
                objViewIndicator3.setBackgroundResource(R.drawable.grey_indicator_icon);
                objViewIndicator4.setBackgroundResource(R.drawable.grey_indicator_icon);
                objViewIndicator5.setBackgroundResource(R.drawable.red_indicatior_icon);

                objViewIndicator1_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                objViewIndicator2_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                objViewIndicator3_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                objViewIndicator4_large.setBackgroundResource(R.drawable.shout_detail_large_resource_screen_indicator);
                objViewIndicator5_large.setBackgroundResource(R.drawable.red_indicatior_icon);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_shout_detail_back:
                super.onBackPressed();
                break;

            case R.id.image_back_shout_detail:
                super.onBackPressed();
                break;

            case R.id.linear_shout_detail_back:
                super.onBackPressed();
                break;
            case R.id.txt_shout_detail_report:
                System.out.println("PASS SHOUT ID : " + strShoutId);
                Intent objIntent = new Intent(this, ReportActivity.class);
                objIntent.putExtra("REPORT_SHOUT_ID", strShoutId);
                startActivity(objIntent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.txt_shout_detail_hide:
            case R.id.img_shout_hide_shout_detail:
                if (objShoutDefaultModel.getIS_SHOUT_HIDDEN() == 0) {
                    new AlertDialog.Builder(ShoutDetailActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("")
                            .setMessage("Are you sure, you want to hide this shout ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new HideShoutApi("1").execute();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else {
                    new AlertDialog.Builder(ShoutDetailActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("")
                            .setMessage("Are you sure, you want to activate this shout?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new HideShoutApi("0").execute();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
                break;
            case R.id.txt_shout_detail_share:
              /*  startActivity(new Intent(this, ShareActivity.class));
                finish();
                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);*/
                break;
            case R.id.btn_shout_detail_can_help:
                System.out.println("SHOUT DETAIL SHOUT TYPE : " + strShoutType);

                if (objSharedPreferences.getString(Constants.USER_ID, "").equals(objSharedPreferences.getString(Constants.USER_ID_FOR_DETAIL_SCREEN, ""))) {
                    String str = btnCanHelp.getText().toString().replaceAll("\\D+", "");
                    System.out.println("RE-SHOUT COUNT : " + str);
                    if (Integer.parseInt(str) > 0) {
                        new ReshoutApi().execute(new String[0]);
                    }
                } else {
                    if (strShoutType.equals("R") || strShoutType.equals("L")) {
                        SharedPreferences objChatPreferences = getSharedPreferences(Constants.CHAT_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor objDataChatEditor = objChatPreferences.edit();
                        objDataChatEditor.putString(Constants.CHAT_APPONENT_ID, objSharedPreferences.getString(Constants.USER_ID_FOR_DETAIL_SCREEN, ""));
                        objDataChatEditor.putString(Constants.CHAT_APPONENT_USER_NAME, strShoutUsername);
                        objDataChatEditor.putString(Constants.CHAT_APPONENT_PROFILE_PIC, strUserProfileUrl);
                        objDataChatEditor.putString(Constants.CHAT_SHOUT_ID, strShoutId);
                        objDataChatEditor.putString(Constants.CHAT_SHOUT_TITLE, strShoutTitle);
                        objDataChatEditor.putString(Constants.CHAT_SHOUT_TYPE, strShoutType);
                        objDataChatEditor.putString(Constants.CHAT_SHOUT_IMAGE, objSharedPreferences.getString(Constants.SHOUT_SINGLE_IMAGE_FOR_DETAIL, ""));
                        objDataChatEditor.putString(Constants.CHAT_BACK, Constants.SHOUT_DETAIL_SCREEN);
                        objDataChatEditor.commit();
                        startActivity(new Intent(this, ChatForShoutActivity.class));
                        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                    }
                }

                break;
            case R.id.btn_add_photo_shout_detail:
                selectImage();
                break;
            case R.id.txt_like_shout_detail:
                if (ConnectivityBroadcastReceiver.isConnected()) {
//                        new LikeAPI().execute();
                    openLikePopup();
                } else {
                    Constants.showInternetToast(ShoutDetailActivity.this);
                }
                break;
            case R.id.image_like_shout_detail:
                if (ConnectivityBroadcastReceiver.isConnected()) {
//                        new LikeAPI().execute();
                    openLikePopup();
                } else {
                    Constants.showInternetToast(ShoutDetailActivity.this);
                }
                break;
            case R.id.image_whats_app_shout_detail:
                Intent whatsappIntent = new Intent("android.intent.action.SEND");
                whatsappIntent.setType(HTTP.PLAIN_TEXT_TYPE);
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Shout App is very nice to use guys. Check it first and install it from play store for free.");
                try {
                    startActivity(whatsappIntent);
                    Utils.d("SHARE", "WHATSAPP");
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.image_facebook_shout_detail:
                ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                        .setContentTitle("Shout Application")
                        .setContentDescription("Try the new app called Shout")
                        .build();
                ShareDialog.show(ShoutDetailActivity.this, shareLinkContent);
                Utils.d("SHARE", "FACEBOOK");
                break;
            case R.id.image_twitter_shout_detail:
                try {
                    Utils.d("SHARE", "TWITTER");
                    ApplicationUtils applicationUtils = new ApplicationUtils(ShoutDetailActivity.this);
                    Intent intent = applicationUtils.getTwitterIntent(ShoutDetailActivity.this, "Shout Application try it out..!");
                    startActivity(intent);


                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                break;
            case R.id.image_mail_shout_detail:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[0]);
                    i.putExtra(Intent.EXTRA_SUBJECT, "Shout App");
                    i.putExtra(Intent.EXTRA_TEXT, "Try the new social app called Shout app.");
                    startActivity(i);
                    Utils.d("SHARE", "MAIL");
                } catch (Exception e22) {
                    e22.printStackTrace();
                }
                break;
            case R.id.image_comment_shout_detail:
                if (objEditTextCommentHere.getText().toString().length() > 0) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    try {

                        strRecentUploadingComment = objEditTextCommentHere.getText().toString().trim();

                        SharedPreferences objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, 0);
                        JSONObject objJsonObject = new JSONObject();
                        objJsonObject.put("shout_id", objSharedPreferences.getString(Constants.SHOUT_ID_FOR_DETAIL_SCREEN, ""));
                        objJsonObject.put(Constants.USER_ID, objSharedPreferences.getString(Constants.USER_ID, ""));
                        objJsonObject.put("comment", objEditTextCommentHere.getText().toString().trim());
                        showLoadingCommentUI(intCommentCount + 1, 1);
                        objEditTextCommentHere.setText("");

                        Collections.reverse(arrShoutCommentsModel);
                        arrShoutCommentsModel.add(new ShoutCommentModel("00", objSharedPreferences.getString(Constants.USER_ID, ""), objSharedPreferences.getString(Constants.USER_NAME, ""), objSharedPreferences.getString(Constants.PROFILE_IMAGE_URL, ""), strRecentUploadingComment, "Just now"));
                        Collections.reverse(arrShoutCommentsModel);

                        objListViewShoutComments.setAdapter(new ShoutCommentsAdapter(arrShoutCommentsModel, ShoutDetailActivity.this));
                        ShoutDetailActivity.setListViewHeightBasedOnItems(objListViewShoutComments);

                        objDatabaseHelper.updateCommentCount(arrShoutCommentsModel.size(), strShoutId);

                        new CallWebService(Constants.MAKE_COMMENT_API, objJsonObject, ShoutDetailActivity.this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    objEditTextCommentHere.setError("Enter Comment First.");
                }
            default:
                break;
        }
    }

    private void openLikePopup() {
        try {
            objPopupInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            customLikeView = objPopupInflater.inflate(R.layout.like_popup, null, true);
            objPopupWindowLargeSource = new PopupWindow(customLikeView);
            objPopupWindowLargeSource.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            objPopupWindowLargeSource.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
            objPopupWindowLargeSource.setOutsideTouchable(true);
            objPopupWindowLargeSource.setTouchable(true);
            objPopupWindowLargeSource.setFocusable(true);
            objPopupWindowLargeSource.setBackgroundDrawable(ShoutDetailActivity.this.getResources().getDrawable(R.drawable.rounded_corner_shout_detail_light_grey));
            objPopupWindowLargeSource.setAnimationStyle(R.style.PopupAnimation);
            objPopupWindowLargeSource.showAtLocation(customLikeView, Gravity.CENTER, 0, 0);

            objImageLike = (ImageView) customLikeView.findViewById(R.id.image_view_like_view);
            objTextViewLike = (TextView) customLikeView.findViewById(R.id.txt_like_view_like_text);
            objRelativeLayoutLikeView = (RelativeLayout) customLikeView.findViewById(R.id.relativeLayout_like_view);
            objListViewShouters = (ListView) customLikeView.findViewById(R.id.listview_shouters);
            objTextViewNoLike = (TextView) customLikeView.findViewById(R.id.txt_no_likes_popup);

            new GetLikedShoutersInfo().execute(objShoutDefaultModel.getSHOUT_ID());

            System.out.println("PRASANNA PRINT SHOUT LIKE COUNT : " + objShoutDefaultModel.getLIKE_COUNT());

            if (objShoutDefaultModel.getLIKE_COUNT().equals("0")) {
                objImageLike.setBackgroundResource(R.drawable.like_grey);
                objTextViewLike.setTextColor(ShoutDetailActivity.this.getResources().getColor(R.color.text_color_shout_default_un_select));
                objTextViewLike.setText("LIKES");
            } else {
                objImageLike.setBackgroundResource(R.drawable.like_red);
                objTextViewLike.setTextColor(ShoutDetailActivity.this.getResources().getColor(R.color.text_color_shout_default_selected));
                objTextViewLike.setText(objShoutDefaultModel.getLIKE_COUNT() + " LIKES");
            }

            objImageLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (objShoutDefaultModel.getIS_SHOUT_LIKED() == 0) {
                        if (ConnectivityBroadcastReceiver.isConnected()) {
                            new LikeAPI().execute();
                            objPopupWindowLargeSource.dismiss();
                        } else {
                            Constants.showInternetToast(ShoutDetailActivity.this);
                        }
                    } else {
                        System.out.println("YOU HAVE ALREADY LIKED TO THIS SHOUT.");
                    }
                }
            });

            objTextViewLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (objShoutDefaultModel.getIS_SHOUT_LIKED() == 0) {
                        if (ConnectivityBroadcastReceiver.isConnected()) {
                            new LikeAPI().execute();
                            objPopupWindowLargeSource.dismiss();
                        } else {
                            Constants.showInternetToast(ShoutDetailActivity.this);
                        }
                    } else {
                        System.out.println("YOU HAVE ALREADY LIKED TO THIS SHOUT.");
                    }
                }
            });

        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectImage() {
        // POPUP WINDOWS OBJECTS FOR SHOWING USER CHOOSED RESOURCES
        LayoutInflater objPopupInflater;
        View customCategoryAlertLayout;
        final PopupWindow objPopupWindowCategories;
        ImageView objImageViewVideo;
        ImageView objImageViewPhoto;
        ImageView objImageViewGallery;
        RelativeLayout objRelativeCategoryPopupOutsideTouch;

        objPopupInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customCategoryAlertLayout = objPopupInflater.inflate(R.layout.capture_shoot_popup, null, true);
        objRelativeCategoryPopupOutsideTouch = (RelativeLayout) customCategoryAlertLayout.findViewById(R.id.relative_shoot_out_side_touch);
        objImageViewVideo = (ImageView) customCategoryAlertLayout.findViewById(R.id.imageViewVideo);
        objImageViewPhoto = (ImageView) customCategoryAlertLayout.findViewById(R.id.imageViewPhoto);
        objImageViewGallery = (ImageView) customCategoryAlertLayout.findViewById(R.id.imageViewGallery);

        objPopupWindowCategories = new PopupWindow(customCategoryAlertLayout);
        objPopupWindowCategories.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        objPopupWindowCategories.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        objPopupWindowCategories.setFocusable(true);

        objPopupWindowCategories.showAtLocation(customCategoryAlertLayout, Gravity.CENTER, 0, 0);


        objRelativeCategoryPopupOutsideTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objPopupWindowCategories.dismiss();
            }
        });

        objImageViewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objPopupWindowCategories.dismiss();
                try{
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    // create a file to save the video
                    Uri fileUri = getOutputMediaFileUri(CreateShoutActivity.MEDIA_TYPE_VIDEO);
                    // set the image file name
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    // set the video image quality to high
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                /*// set the max size of video to 5 MB
                intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 5491520L);
                // set the max duration of video to 45 seconds
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 45);*/
                    startActivityForResult(intent, Constants.REQUEST_MADE_FOR_VIDEO);
                }catch(NullPointerException ne){
                    ne.printStackTrace();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        objImageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objPopupWindowCategories.dismiss();
                try{
                    Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "CRASH_" + System.currentTimeMillis());
                    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                    System.out.println("APPLICATION PATH IN LAUNCH CAMERA :" + Constants.APPLICATION_PATH);
                    mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    startActivityForResult(intentCamera, Constants.REQUEST_MADE_FOR_CAMERA);
                }catch(NullPointerException ne){
                    ne.printStackTrace();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        objImageViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    objPopupWindowCategories.dismiss();
                    Intent intentGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intentGallery, Constants.REQUEST_MADE_FOR_GALLERY);
                }catch(NullPointerException ne){
                    ne.printStackTrace();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (objRelativeLayoutLargeResource.getVisibility() == RelativeLayout.VISIBLE) {
            objViewPagerUploads.setCurrentItem(intResourcePosition);
            objRelativeLayoutLargeResource.setVisibility(RelativeLayout.GONE);
            btnCanHelp.setVisibility(Button.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // After camera screen this code will excuted
        if (requestCode == Constants.REQUEST_MADE_FOR_VIDEO) {
            try {
                if (resultCode == RESULT_OK) {

                    System.out.println("VIDEO SAVED TO PATH : " + data.getData());
                    Bitmap bmThumbnail;
                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(data.getData().getPath(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                    System.out.println("THUMBNAIL PATH : " + ApplicationUtils.saveVideoThumbnail(bmThumbnail));
                    reInitializeResourceArray();

                    arrResourceType.add("V");
                    arrResourcePath.add(ApplicationUtils.saveVideoThumbnail(bmThumbnail));
                    arrResourceType.add("V");
                    arrResourcePath.add(Uri.parse(data.getData().toString()));
                    new UploadResource().execute();
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
                                    arrResourceType.add("C");
                                    arrResourcePath.add(applicationUtils.saveImageOnSdCard(imageBytes));
                                    new UploadResource().execute();
                                }catch(NullPointerException ne){
                                    ne.printStackTrace();
                                }catch(Exception e){
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                };
                try{
                    imageCompressionAsyncTaskCamera.execute(applicationUtils.getRealPathFromURI(mCapturedImageURI));// imagePath as a string
                }catch(NullPointerException ne){
                    ne.printStackTrace();
                }catch(Exception e){
                    e.printStackTrace();
                }

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
                                    arrResourceType.add("C");
                                    arrResourcePath.add(applicationUtils.saveImageOnSdCard(imageBytes));
                                    new UploadResource().execute();
                                }catch(NullPointerException ne){
                                    ne.printStackTrace();
                                }catch(Exception e){
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                };
                try{
                    imageCompressionAsyncTaskCamera.execute(applicationUtils.getRealPathFromURI(uri));// imagePath as a string
                }catch(NullPointerException ne){
                    ne.printStackTrace();
                }catch(Exception e){
                    e.printStackTrace();
                }

            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void reInitializeResourceArray() {
        arrResourceType = new ArrayList<String>();
        arrResourcePath = new ArrayList<Uri>();
    }

    class TapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            System.out.println("IN TAP_GESTURE CLICKED");
            btnCanHelp.setVisibility(Button.GONE);
            objRelativeLayoutLargeResource.setVisibility(RelativeLayout.VISIBLE);
            return true;
        }
    }

    private class GetLikedShoutersInfo extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setDummyCellsForLikedShoutersIndo();
        }

        private void setDummyCellsForLikedShoutersIndo() {
            arrLikedShoutersModel.clear();
            for (int index = 0; index < 2; index++) {
                LikedShoutersModel objLikedShoutersModel = new LikedShoutersModel("", "", "", "");
                arrLikedShoutersModel.add(objLikedShoutersModel);
            }
            LayoutInflater objLayoutInflater = (LayoutInflater) ShoutDetailActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
            objLikedShoutersAdapter = new LikedShoutersAdapter(arrLikedShoutersModel, ShoutDetailActivity.this, ShoutDetailActivity.this, objLayoutInflater);
            objListViewShouters.setAdapter(objLikedShoutersAdapter);
        }

        @Override
        protected String doInBackground(String... params) {
            String strResult = "";
            try {
                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("shout_id", strShoutId);
                if (ConnectivityBroadcastReceiver.isConnected()) {
                    strResult = NetworkUtils.postData(Constants.GET_LIKED_SHOUTERS_INFO_API, objJsonObject.toString());
                } else {
                    strResult = "NC";
                }
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

                System.out.println("GET SHOUTERS API RESULT : " + s);
                if (s.equals("NC")) {
                    // No internet connection
                } else {
                    arrLikedShoutersModel.clear();
                    JSONObject objJsonObject = new JSONObject(s);
                    if (objJsonObject.getBoolean("result")) {
                        JSONArray objJsonArray = new JSONArray(objJsonObject.getString("data"));
                        for (int index = 0; index < objJsonArray.length(); index++) {
                            LikedShoutersModel objLikedShoutersModel = new LikedShoutersModel(
                                    objJsonArray.getJSONObject(index).getString("user_id"),
                                    objJsonArray.getJSONObject(index).getString("name"),
                                    objJsonArray.getJSONObject(index).getString("profile_pic"),
                                    objJsonArray.getJSONObject(index).getString("time"));
                            arrLikedShoutersModel.add(objLikedShoutersModel);
                        }
                        LayoutInflater objLayoutInflater = (LayoutInflater) ShoutDetailActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                        if (arrLikedShoutersModel.size() > 0) {

                            System.out.println("LIKE COUNT OLD : " + objShoutDefaultModel.getLIKE_COUNT());
                            objShoutDefaultModel.setLIKE_COUNT(String.valueOf(arrLikedShoutersModel.size()));
                            System.out.println("LIKE COUNT UPDATE : " + objShoutDefaultModel.getLIKE_COUNT());
                            objShoutDefaultModel = objDatabaseHelper.updateShout(objShoutDefaultModel, objShoutDefaultModel.getSHOUT_ID());

                            if (objShoutDefaultModel.getLIKE_COUNT().equals("0")) {
                                objImageLike.setBackgroundResource(R.drawable.like_grey);
                                objTextViewLike.setTextColor(ShoutDetailActivity.this.getResources().getColor(R.color.text_color_shout_default_un_select));
                                objTextViewLike.setText("LIKES");
                                objTextViewLikeShout.setText("LIKES");
                                objImageViewLikeShout.setBackgroundResource(R.drawable.like_grey);
                            } else {
                                objImageLike.setBackgroundResource(R.drawable.like_red);
                                objTextViewLike.setTextColor(ShoutDetailActivity.this.getResources().getColor(R.color.text_color_shout_default_selected));
                                objTextViewLike.setText(objShoutDefaultModel.getLIKE_COUNT() + " LIKES");
                                objTextViewLikeShout.setText(objShoutDefaultModel.getLIKE_COUNT() + " LIKES");
                                objImageViewLikeShout.setBackgroundResource(R.drawable.like_red);
                                objTextViewLikeShout.setTextColor(ShoutDetailActivity.this.getResources().getColor(R.color.text_color_shout_default_selected));
                            }

                            objTextViewNoLike.setVisibility(TextView.GONE);
                            objListViewShouters.setVisibility(ListView.VISIBLE);
                            objLikedShoutersAdapter = new LikedShoutersAdapter(arrLikedShoutersModel, ShoutDetailActivity.this, ShoutDetailActivity.this, objLayoutInflater);
                            objListViewShouters.setAdapter(objLikedShoutersAdapter);
                            objLikedShoutersAdapter.notifyDataSetChanged();
                        } else {
                            objTextViewNoLike.setVisibility(TextView.VISIBLE);
                            objListViewShouters.setVisibility(ListView.GONE);
                        }
                    } else {
                        objTextViewNoLike.setVisibility(TextView.VISIBLE);
                        objListViewShouters.setVisibility(ListView.GONE);
                    }
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class LikeAPI extends AsyncTask<String, Void, String> {
        String strResult;

        public LikeAPI() {
            strResult = "";
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            try {
                SharedPreferences objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, 0);
                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("shout_id", objSharedPreferences.getString(Constants.SHOUT_ID_FOR_DETAIL_SCREEN, ""));
                objDatabaseHelper.updateLikeCount(Integer.parseInt(strShoutLikeCount) + 1, strShoutId);
                ShoutDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        objImageViewLikeShout.setBackgroundResource(R.drawable.like_red);
                        objTextViewLikeShout.setTextColor(getResources().getColor(R.color.text_color_shout_default_selected));
                        objTextViewLikeShout.setText(Integer.parseInt(strShoutLikeCount) + 1 + " LIKES");
                    }
                });
                objJsonObject.put(Constants.USER_ID, objSharedPreferences.getString(Constants.USER_ID, ""));
                strResult = NetworkUtils.postData(Constants.LIKE_SHOUT_API, objJsonObject.toString());
                return strResult;
            } catch (Exception e) {
                e.printStackTrace();
                return strResult;
            }
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (new JSONObject(s).getString("result").equals("false")) {
                    objImageViewLikeShout.setBackgroundResource(R.drawable.like_grey);
                    objTextViewLikeShout.setTextColor(getResources().getColor(R.color.text_color_shout_default_un_select));
                    objTextViewLikeShout.setText("LIKES");
                    objDatabaseHelper.updateLikeCount(Integer.parseInt(strShoutLikeCount) - 1, strShoutId);
                }
            } catch (Exception e) {
                objImageViewLikeShout.setBackgroundResource(R.drawable.like_grey);
                objTextViewLikeShout.setTextColor(getResources().getColor(R.color.text_color_shout_default_un_select));
                objTextViewLikeShout.setText("LIKES");
                objDatabaseHelper.updateLikeCount(Integer.parseInt(strShoutLikeCount) - 1, strShoutId);
                e.printStackTrace();
            }
        }
    }

    public class ReshoutApi extends AsyncTask<String, Void, String> {
        String strResult;

        public ReshoutApi() {
            strResult = "";
        }

        protected String doInBackground(String... params) {
            try {
                System.out.println("SHOUT ID : " + strShoutId);
                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("shout_id", strShoutId);
                strResult = NetworkUtils.postData(Constants.RESHOUT_API, objJsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject objJsonObject = new JSONObject(s);
                if (objJsonObject.getString("result").equals("true")) {
                    ShoutDefaultListModel objShoutDefaultModel = objDatabaseHelper.getShoutDetails(strShoutId);
                    objShoutDefaultModel.setRE_SHOUT(objJsonObject.getString("reshout_count"));
                    objDatabaseHelper.updateShout(objShoutDefaultModel, strShoutId);
                    if (objJsonObject.getString("reshout_count").equals("0")) {
                        btnCanHelp.setBackgroundColor(Color.parseColor("#444444"));
                        btnCanHelp.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        btnCanHelp.setBackgroundColor(getResources().getColor(R.color.red_background_color));
                        btnCanHelp.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                    btnCanHelp.setText("RESHOUT\n(" + objJsonObject.getString("reshout_count") + " LEFT)");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class HideShoutApi extends AsyncTask<String, Void, String> {
        final ProgressDialog objProgressDialog;
        String strResult;

        public HideShoutApi(String status) {
            strResult = "";
            objProgressDialog = new ProgressDialog(ShoutDetailActivity.this);
            strIsHide = status;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            try {
                objProgressDialog.setMessage("Updating changes...");
                objProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected String doInBackground(String... params) {
            try {
                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("shout_id", strShoutId);
                objJsonObject.put("status", strIsHide);
                strResult = NetworkUtils.postData(Constants.HIDE_SHOUT_API, objJsonObject.toString());
                return strResult;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return strResult;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (objProgressDialog.isShowing()) {
                    objProgressDialog.dismiss();
                }
                JSONObject objJsonObject = new JSONObject(s);
                if (objJsonObject.getString("result").equals("true")) {
                    if (strIsHide.equals("0")) {
                        objShoutDefaultModel.setIS_SHOUT_HIDDEN(0);
                        objShoutDefaultModel.setIS_HIDE("0");
                        objDatabaseHelper.updateShout(objShoutDefaultModel, strShoutId);
                        imgShoutHideShow.setImageResource(R.drawable.shout_hide);
                        objTextViewHide.setText("HIDE");
                    } else {
                        objShoutDefaultModel.setIS_SHOUT_HIDDEN(1);
                        objShoutDefaultModel.setIS_HIDE("1");
                        objDatabaseHelper.updateShout(objShoutDefaultModel, strShoutId);
                        imgShoutHideShow.setImageResource(R.drawable.shout_show);
                        objTextViewHide.setText("SHOW");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class UploadResource extends AsyncTask<String, Void, String> {

        final ProgressDialog objProgressDialog = new ProgressDialog(ShoutDetailActivity.this);
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

            System.out.println("RESOURCE TYPE : " + arrResourceType);
            System.out.println("RESOURCE PATH : " + arrResourcePath);

            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(Constants.UPLOAD_RESOURCES);
                System.out.println("UPLOAD RESOURCE URL : " + Constants.UPLOAD_RESOURCES);
                HttpResponse response = null;
                MultipartEntity entityBuilder = new MultipartEntity();

                entityBuilder.addPart("shout_id", new StringBody(strShoutId));
                for (int index = 0; index < arrResourceType.size(); index++) {
                    if (arrResourceType.get(index).equals("C")) {
                        File file = new File(arrResourcePath.get(index).getPath());
                        FileBody objFile = new FileBody(file);
                        entityBuilder.addPart("Image-" + index, objFile);
                    } else if (arrResourceType.get(index).equals("V")) {
                        File thumbnailFile = new File(arrResourcePath.get(index).getPath());
                        FileBody objThumbnailFileBody = new FileBody(thumbnailFile);
                        entityBuilder.addPart("Video-" + index, objThumbnailFileBody);
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
                    Toast.makeText(ShoutDetailActivity.this, "To add more media, Click Edit", Toast.LENGTH_SHORT).show();
                    ShoutDefaultListModel objTempShoutModel = objDatabaseHelper.getShoutDetails(strShoutId);
                    System.out.println("PRASAD PRINT : " + new JSONObject(objJsonObject.getString("shout_media")).getString("shout_image"));
                    System.out.println("PRASAD PRINT : " + new JSONObject(objJsonObject.getString("shout_media")).getString("images"));

                    objTempShoutModel.setSHOUT_IMAGE(new JSONObject(objJsonObject.getString("shout_media")).getString("shout_image"));
                    objTempShoutModel.setStrShoutImages(new JSONObject(objJsonObject.getString("shout_media")).getString("images"));
                    strShoutResourceJson = new JSONObject(objJsonObject.getString("shout_media")).getString("images");
                    objDatabaseHelper.updateShout(objTempShoutModel, strShoutId);
                    try {
                        JSONArray jSONArray = new JSONArray(strShoutResourceJson);
                        arrShoutDetailViewPagerModel = new ArrayList();
                        for (int i = 0; i < jSONArray.length(); i++) {
                            showIndicator(i);
                            arrShoutDetailViewPagerModel.add(new ShoutDetailViewPagerModel(jSONArray.getJSONObject(i).getString(DatabaseHelper.strCountryId), Constants.HTTP_URL + jSONArray.getJSONObject(i).getString("image_url"), jSONArray.getJSONObject(i).getString("image_type"), Constants.HTTP_URL + jSONArray.getJSONObject(i).getString("video_url"),jSONArray.getJSONObject(i).getString("video_local_path")));
                        }
                        System.out.println("SHOUT DETAIL VIEWPAGER MODEL ARRAY : " + arrShoutDetailViewPagerModel);
                        if (arrShoutDetailViewPagerModel.size() > 0) {
                            objViewPagerUploads.setVisibility(TextView.VISIBLE);
                            objRelativeNoShoutImageAvailableWithSameLogin.setVisibility(RelativeLayout.GONE);
                            objViewPagerUploads.setAdapter(new ShoutDetailViewPagerAdapter(ShoutDetailActivity.this, arrShoutDetailViewPagerModel));
                            objViewPagerLargeResource.setAdapter(new ShoutDetailViewPagerAdapter(ShoutDetailActivity.this, arrShoutDetailViewPagerModel));

                            objViewPagerUploads.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                    setIndicatorView(position);
                                    objViewPagerLargeResource.setCurrentItem(position);
                                }

                                @Override
                                public void onPageSelected(int position) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });

                            objViewPagerLargeResource.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                    setIndicatorView(position);
                                    intResourcePosition = position;
                                }

                                @Override
                                public void onPageSelected(int position) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });
                        } else {
                            if (objSharedPreferences.getString(Constants.USER_ID, "").equals(objShoutDefaultModel.getUSER_ID())) {
                                objRelativeNoShoutImageAvailableWithSameLogin.setVisibility(RelativeLayout.VISIBLE);
                                objRelativeNoShoutImageAvailableWithDifferentLogin.setVisibility(RelativeLayout.GONE);
                            } else {
                                objRelativeNoShoutImageAvailableWithSameLogin.setVisibility(RelativeLayout.GONE);
                                objRelativeNoShoutImageAvailableWithDifferentLogin.setVisibility(RelativeLayout.VISIBLE);
                            }
                            objViewPagerUploads.setVisibility(ViewPager.GONE);
                        }
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
