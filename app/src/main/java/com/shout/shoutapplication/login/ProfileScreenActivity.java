package com.shout.shoutapplication.login;

import android.Manifest;
import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
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
import com.shout.shoutapplication.CustomClasses.CircularNetworkImageView;
import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.ApplicationUtils;
import com.shout.shoutapplication.Utils.ConnectivityBroadcastReceiver;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.ImageUtils;
import com.shout.shoutapplication.Utils.LocationDetails;
import com.shout.shoutapplication.Utils.NetworkUtils;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.app.AppController;
import com.shout.shoutapplication.database.DatabaseHelper;
import com.shout.shoutapplication.main.ShoutDefaultActivity;
import com.shout.shoutapplication.main.ShoutUsersListActivity;
import com.shout.shoutapplication.others.CircleTransform;
import com.shout.shoutapplication.others.SoftKeyboard;
import com.shout.shoutapplication.others.TouchableMapFragment;
import com.shout.shoutapplication.others.TouchableWrapper;
import com.squareup.picasso.NetworkPolicy;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class ProfileScreenActivity extends ActionBarActivity implements OnMapReadyCallback, View.OnClickListener {

    ImageView profileImage;
    ImageView objImageViewLargeImage;
    Button btnInviteFriends;

    SharedPreferences objSharedPreferences;

    RelativeLayout objRelativeLayoutRoot;
    LinearLayout objLinearNextScreen;
    TextView objTextViewEditProfileClick;
    String strLargeImageUrl = "";


    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;

    SoftKeyboard softKeyboard;
    //edit fields
    private EditText editTextFirstName, editTextLastName;
    private EditText objEditTextEmail;
    private EditText objEditTextCity;
    private EditText objEditTextContactNumber;
    private EditText objEditTextProfileAdderss;
    private EditText objEditTextDescription;
    private ImageView objImageViewOpenMap;

    private TextView objTextViewAge;
    private TextView objTextViewBirthDateErrorText;
    private ImageView objImageViewGenderImage;
    private ImageButton objImageButtonGender;

    // GETTING ADDRESS FROM MAP
    private TouchableMapFragment mGoogleMap;
    boolean mUpdatesRequested = false;
    private LatLng center;
    private Geocoder geocoder;
    private RelativeLayout objRelativeLayoutMap;
    private TextView objTextViewMapAddress;
    public List<Address> addresses;
    LatLng latLong;
    CameraUpdate objCameraUpdate;
    public static CameraUpdate zoom;
    private TextView objTextViewMapAddressSelect;
    private static GoogleMap objGoogleMap;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    //upload profile image Constants
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, REQUEST_GALLERY = 1;
    private Uri profileImageUri = null;
    private Button btnProfileImage;


    private String EDIT_PROFILE_TEXT = "EDIT YOUR PROFILE";
    private String SAVE_PROFILE_TEXT = "SAVE YOUR PROFILE";

    private LinearLayout objLinearLayoutTopLayout;
    String strBackScreenName = "";

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private LinearLayout objLinearLayoutSearchBar;

    // LOADING MY SHOUTS
    LinearLayout objLinearLayoutMyShouts;

//    Button btnLoadShouts;

    private String strDateOfBirth = "";
    private String strGender = "M";

    private Button objButtonSelectDob;

    int intFlag = 0;

    LocationDetails objLocationDetails;
    Double currentLatitude;
    Double currentLongitude;
    private Uri mCapturedImageURI;
    private ApplicationUtils applicationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applicationUtils = new ApplicationUtils(this);
        setContentView(R.layout.activity_profile_screen);
        objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
        strBackScreenName = objSharedPreferences.getString(Constants.PROFILE_BACK_SCREEN_NAME, "");
        SharedPreferences.Editor objEditor = objSharedPreferences.edit();
        objEditor.putString(Constants.CURRENT_ACTIVITY_NAME_FOR_SHOW_MAP, Constants.PROFILE_ATIVITY);
        objEditor.commit();


        objLocationDetails = new LocationDetails(ProfileScreenActivity.this);
        objLocationDetails.startTracking();

        init();

        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        objRelativeLayoutRoot = (RelativeLayout) findViewById(R.id.relative_profile_screen);

        softKeyboard = new SoftKeyboard(objRelativeLayoutRoot, im);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {

            @Override
            public void onSoftKeyboardHide() {
                System.out.println("KEYBOARD CLOSE");
                ProfileScreenActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            btnInviteFriends.setVisibility(Button.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onSoftKeyboardShow() {
                System.out.println("KEYBOARD OPEN");
                ProfileScreenActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            btnInviteFriends.setVisibility(Button.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    private void init() {
        objButtonSelectDob = (Button) findViewById(R.id.edt_profile_screen_dob);
        objTextViewAge = (TextView) findViewById(R.id.txt_user_age);
        objTextViewBirthDateErrorText = (TextView) findViewById(R.id.txt_profile_screen_bdate_error_message);
        objImageButtonGender = (ImageButton) findViewById(R.id.imgbtn_gender);
        objImageViewGenderImage = (ImageView) findViewById(R.id.gender_image);
        /*objImageViewGenderImage.setBackgroundResource(R.drawable.male);
        strGender = "M";*/
        strGender = "M";

        objLinearLayoutMyShouts = (LinearLayout) findViewById(R.id.linear_my_shouts);

        objLinearLayoutSearchBar = (LinearLayout) findViewById(R.id.linear_top_map_search_bar_profile_screen);
        profileImage = (ImageView) findViewById(R.id.profile_screen_user_image);
        btnProfileImage = (Button) findViewById(R.id.button_profile_screen_user_image);
        btnInviteFriends = (Button) findViewById(R.id.btn_add_friends_to_shoutbook_profile_screen);
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        objEditTextEmail = (EditText) findViewById(R.id.edt_profile_screen_email);
        objLinearNextScreen = (LinearLayout) findViewById(R.id.linear_next_screen_profile_sceen);
        objTextViewEditProfileClick = (TextView) findViewById(R.id.txt_edit_profile_screen);
        objEditTextCity = (EditText) findViewById(R.id.edt_profile_screen_city);
        objEditTextContactNumber = (EditText) findViewById(R.id.edt_profile_screen_contact_no);
        objEditTextProfileAdderss = (EditText) findViewById(R.id.edt_profile_screen_address);
        objEditTextDescription = (EditText) findViewById(R.id.edt_profile_screen_description);
        objImageViewOpenMap = (ImageView) findViewById(R.id.imageview_open_map_profile_screen);
        objTextViewMapAddressSelect = (TextView) findViewById(R.id.map_loading_done_profile_screen);
        objLinearLayoutTopLayout = (LinearLayout) findViewById(R.id.linear_profile);
        objImageViewLargeImage = (ImageView) findViewById(R.id.expanded_profile_image);
        // MAP LAYOUT REFERENCES
        objRelativeLayoutMap = (RelativeLayout) findViewById(R.id.relative_profile_screen_map);
        objTextViewMapAddress = (TextView) findViewById(R.id.txt_map_address_profile_screen);

        setMap();

        objImageViewOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager img = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                img.hideSoftInputFromWindow(objImageViewOpenMap.getApplicationWindowToken(), 0);
                /*// Get Location Manager and check for GPS & Network location services
                LocationManager lm = (LocationManager) ProfileScreenActivity.this.getSystemService(ProfileScreenActivity.this.LOCATION_SERVICE);
                if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    // Build the alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileScreenActivity.this);
                    builder.setTitle("Location Services Not Active");
                    builder.setMessage("Please enable Location Services and GPS");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Show location settings when the user acknowledges the alert dialog
                   *//* Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent);*//*
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1000);
                        }
                    });
                    Dialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                }*/
                if (Utils.isGPSEnabled(ProfileScreenActivity.this)) {
                    objRelativeLayoutMap.setVisibility(RelativeLayout.VISIBLE);
                }
            }
        });

        objTextViewMapAddressSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!objTextViewMapAddress.getText().toString().equals(" Getting location ")) {
                    objRelativeLayoutMap.setVisibility(RelativeLayout.GONE);
                    objEditTextProfileAdderss.setText(objTextViewMapAddress.getText().toString());
                } else {
                    Toast.makeText(ProfileScreenActivity.this, "Waiting for address to be load...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        objTextViewMapAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(ProfileScreenActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        if (objSharedPreferences.getString(Constants.PROFILE_COMPLETE, "").equals("0") || objSharedPreferences.getString(Constants.LOGIN_FLAG, "").equals("N")) {
            btnInviteFriends.setText("GO TO SHOUTBOOK");
            objLinearNextScreen.setVisibility(LinearLayout.GONE);
            objLinearLayoutTopLayout.setVisibility(LinearLayout.GONE);
            new GetUserDetailsAPI().execute(objSharedPreferences.getString(Constants.USER_ID, ""));
        } else {
            btnInviteFriends.setText("GO TO SHOUTBOARD");
            if (objSharedPreferences.getString(Constants.USER_ID, "").equals(objSharedPreferences.getString(Constants.PROFILE_SCREEN_USER_ID, ""))) {
                objLinearNextScreen.setVisibility(LinearLayout.VISIBLE);
                objLinearLayoutTopLayout.setVisibility(LinearLayout.VISIBLE);
            } else {
                objLinearNextScreen.setVisibility(LinearLayout.GONE);
                objLinearLayoutTopLayout.setVisibility(LinearLayout.GONE);
            }
            new GetUserDetailsAPI().execute(objSharedPreferences.getString(Constants.PROFILE_SCREEN_USER_ID, ""));
        }

        objTextViewEditProfileClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.d("debug", "IN EDIT CLICK EVENT");
                if (v.getId() == R.id.txt_edit_profile_screen) {
                    Utils.d("debug", "TEXT : " + objTextViewEditProfileClick.getText());
                    if (objTextViewEditProfileClick.getText().equals(EDIT_PROFILE_TEXT)) {
                        makeAllEditableOrNot(true);
                    } else {
                        if (objEditTextCity.getText().toString().trim().length() > 0) {
                            if (objEditTextProfileAdderss.getText().toString().trim().length() > 0) {
                                if (strDateOfBirth.length() > 0) {
                                    new UpdateProfileAPI("BOARD").execute(objEditTextCity.getText().toString().trim(), objEditTextProfileAdderss.getText().toString().trim(), objEditTextDescription.getText().toString().trim());
                                } else {
                                    objTextViewBirthDateErrorText.setVisibility(TextView.VISIBLE);
                                }
                            } else {
                                objEditTextProfileAdderss.setHint(" * Mandatory field");
                                objEditTextProfileAdderss.setHintTextColor(getResources().getColor(R.color.red_background_color));
                            }
                        } else {
                            objEditTextCity.setHint(" * Mandatory field");
                            objEditTextCity.setHintTextColor(getResources().getColor(R.color.red_background_color));
                        }
                    }
                }
            }
        });

        objLinearNextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(ProfileScreenActivity.this, MyShoutActivity.class);
                startActivity(objIntent);
                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
            }
        });

        btnInviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnInviteFriends.getText().toString().equals("GO TO SHOUTBOOK")) {
                    /*if (objTextViewEditProfileClick.getText().toString().equals(SAVE_PROFILE_TEXT)) {
                        Toast.makeText(ProfileScreenActivity.this, "First  click to SAVE YOUR PROFILE", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent objIntent = new Intent(ProfileScreenActivity.this, InviteFriendsActivity.class);
                        startActivity(objIntent);
                        finish();
                        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                    }*/
                    if (objEditTextCity.getText().toString().trim().length() > 0) {
                        if (objEditTextProfileAdderss.getText().toString().trim().length() > 0) {
                            if (strDateOfBirth.length() > 0) {
                                new UpdateProfileAPI("BOOK").execute(objEditTextCity.getText().toString().trim(), objEditTextProfileAdderss.getText().toString().trim(), objEditTextDescription.getText().toString().trim());
                            } else {
                                objTextViewBirthDateErrorText.setVisibility(TextView.VISIBLE);
                            }
                        } else {
                            objEditTextProfileAdderss.setHint(" * Mandatory field");
                            objEditTextProfileAdderss.setHintTextColor(getResources().getColor(R.color.red_background_color));
                        }
                    } else {
                        objEditTextCity.setHint(" * Mandatory field");
                        objEditTextCity.setHintTextColor(getResources().getColor(R.color.red_background_color));
                    }
                } else {
                    if (objTextViewEditProfileClick.getText().toString().equals(SAVE_PROFILE_TEXT)) {
                        Toast.makeText(ProfileScreenActivity.this, "First  click to SAVE YOUR PROFILE", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent objIntent = new Intent(ProfileScreenActivity.this, ShoutDefaultActivity.class);
                        objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(objIntent);
                        finish();
                        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                    }
                }
            }
        });


        btnProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.d("debug", "PROFILE IMAGE CLICK EVENT" + v);
                if (objTextViewEditProfileClick.getText().equals(SAVE_PROFILE_TEXT)) {
                    selectImage();
                }
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objTextViewEditProfileClick.getText().equals(EDIT_PROFILE_TEXT)) {
                    System.out.println("SHOW LARGE IMAGE");
//                    zoomImageFromThumb(objRelativeLayoutLargeImage, profileImage, strLargeImageUrl);
                    openResourcePopup(strLargeImageUrl);
                }
            }
        });
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    private void showGenderPopup() {
        System.out.println("GENDER POPUP CALLED");
        LayoutInflater objLayoutInflater;
        final View objGenderView;
        final PopupWindow objPopupWindowGender;
        objLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        objGenderView = objLayoutInflater.inflate(R.layout.gender_popup_layout, null, true);

        final ImageView objImageViewMale = (ImageView) objGenderView.findViewById(R.id.image_popup_male);
        final ImageView objImageViewFeMale = (ImageView) objGenderView.findViewById(R.id.image_popup_female);
        TextView objTextViewMale = (TextView) objGenderView.findViewById(R.id.txt_gender_male);
        TextView objTextViewFeMale = (TextView) objGenderView.findViewById(R.id.txt_gender_female);
        Button btnSelectGender = (Button) objGenderView.findViewById(R.id.btn_gender_popup_okay);

        objPopupWindowGender = new PopupWindow(objGenderView);
        objPopupWindowGender.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        objPopupWindowGender.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        objPopupWindowGender.setFocusable(true);
        objPopupWindowGender.showAtLocation(objGenderView, Gravity.CENTER, 0, 0);

        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                objPopupWindowGender.showAtLocation(objGenderView, Gravity.CENTER, 0, 0);
            }
        }, 500);*/

        if (strGender.equals("M")) {
            objImageViewMale.setImageResource(R.drawable.male);
            objImageViewFeMale.setImageResource(R.drawable.unselect_female);
        } else {
            objImageViewFeMale.setImageResource(R.drawable.female);
            objImageViewMale.setImageResource(R.drawable.unselect_male);
        }

        btnSelectGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objSharedPreferences.getString(Constants.LOGIN_FLAG, "").equals("N")) {
                    openDatePickerPopup();
                }
                System.out.println("LOGIN FLAG PRINT : " + objSharedPreferences.getString(Constants.LOGIN_FLAG, ""));
                if (strGender.equals("M")) {
                    objImageViewGenderImage.setImageBitmap(null);
                    objImageViewGenderImage.setBackgroundResource(R.drawable.male);
                } else {
                    objImageViewGenderImage.setImageBitmap(null);
                    objImageViewGenderImage.setBackgroundResource(R.drawable.female);
                }
                objPopupWindowGender.dismiss();
            }
        });

        objImageViewMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strGender = "M";
                objImageViewMale.setImageResource(R.drawable.male);
                objImageViewFeMale.setImageResource(R.drawable.unselect_female);
            }
        });

        objImageViewFeMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strGender = "F";
                objImageViewFeMale.setImageResource(R.drawable.female);
                objImageViewMale.setImageResource(R.drawable.unselect_male);
            }
        });

        objTextViewMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strGender = "M";
                objImageViewMale.setImageResource(R.drawable.male);
                objImageViewFeMale.setImageResource(R.drawable.unselect_female);
            }
        });

        objTextViewFeMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strGender = "F";
                objImageViewFeMale.setImageResource(R.drawable.female);
                objImageViewMale.setImageResource(R.drawable.unselect_male);
            }
        });
    }

    private void openDatePickerPopup() {
        LayoutInflater objLayoutInflater;
        final View objDatePickerView;
        final PopupWindow objPopupWindowDatePicker;
        objLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        objDatePickerView = objLayoutInflater.inflate(R.layout.date_picker_popup_layout, null, true);

        final EditText objEditTextBirthDate = (EditText) objDatePickerView.findViewById(R.id.edt_birth_date_picker);
        Button btnSelectDate = (Button) objDatePickerView.findViewById(R.id.btn_date_picker_popup_okay);

        objPopupWindowDatePicker = new PopupWindow(objDatePickerView);
        objPopupWindowDatePicker.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        objPopupWindowDatePicker.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        objPopupWindowDatePicker.setFocusable(true);
        objPopupWindowDatePicker.showAtLocation(objDatePickerView, Gravity.CENTER, 0, 0);

        final int[] intAge = {0};

        if (strDateOfBirth.length() > 0) {
            objEditTextBirthDate.setText(strDateOfBirth);
        }

        objEditTextBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("OPEN DATE PICKER CALLED");

                final Calendar objCalendar = Calendar.getInstance();
                if (strDateOfBirth.length() > 0) {
                    String dateSpliterFirst[] = strDateOfBirth.split("/");
                    objCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSpliterFirst[0].toString()));
                    objCalendar.set(Calendar.MONTH, Integer.parseInt(dateSpliterFirst[1].toString()));
                    objCalendar.set(Calendar.YEAR, Integer.parseInt(dateSpliterFirst[2].toString()));
                }

                new DatePickerDialog(ProfileScreenActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
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
                        intAge[0] = getAge(year, monthOfYear, dayOfMonth);
                        System.out.println("PRASANNA PRINT AGE : " + intAge[0]);
                        if (intAge[0] < 0) {
                            Toast.makeText(ProfileScreenActivity.this, "Age must greater then 0", Toast.LENGTH_SHORT).show();
                        } else {
                            objButtonSelectDob.setText(sendDate1);
                            objEditTextBirthDate.setText(sendDate1);
                            strDateOfBirth = sendDate1;
                            if (sendDate1.length() > 0) {
                                objTextViewAge.setText(getAge(year, monthOfYear, dayOfMonth) + " YRS");
                                objTextViewBirthDateErrorText.setVisibility(TextView.INVISIBLE);
                            }
                        }
                    }
                }, objCalendar.get(Calendar.YEAR), objCalendar.get(Calendar.MONTH), objCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    currentLatitude = objLocationDetails.getCurrentLocation().getLatitude();
                    currentLongitude = objLocationDetails.getCurrentLocation().getLongitude();
                } catch (NullPointerException ne) {
                    currentLatitude = 0.00;
                    currentLongitude = 0.00;
                    ne.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (strDateOfBirth.length() > 0) {
                    objPopupWindowDatePicker.dismiss();
                    new SaveProfileAPI().execute(strGender, strDateOfBirth, String.valueOf(currentLatitude), String.valueOf(currentLongitude));
                } else {
                    Toast.makeText(ProfileScreenActivity.this, "Please select your birth date", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void openResourcePopup(String strLargeImageUrl) {
        LayoutInflater objPopupInflater;
        View customCategoryAlertLayout;
        final PopupWindow objPopupWindowLargeSource;
        ImageView objClickedImage;

        ImageView objClickedImageDissmiss;


        objPopupInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customCategoryAlertLayout = objPopupInflater.inflate(R.layout.resource_popup_layout, null, true);
        objClickedImage = (ImageView) customCategoryAlertLayout.findViewById(R.id.clicked_image);
        objClickedImageDissmiss = (ImageView) customCategoryAlertLayout.findViewById(R.id.clicked_image_dismiss);

        objPopupWindowLargeSource = new PopupWindow(customCategoryAlertLayout);
        objPopupWindowLargeSource.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        objPopupWindowLargeSource.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        objPopupWindowLargeSource.setFocusable(true);
        objPopupWindowLargeSource.showAtLocation(customCategoryAlertLayout, Gravity.CENTER, 0, 0);

        System.out.println("IMAGE URL : " + strLargeImageUrl);
        if (ConnectivityBroadcastReceiver.isConnected()) {
            Picasso.with(ProfileScreenActivity.this).load(strLargeImageUrl).into(objClickedImage);
        } else {
            Picasso.with(ProfileScreenActivity.this).load(strLargeImageUrl).networkPolicy(NetworkPolicy.OFFLINE).into(objClickedImage);
        }
        objClickedImageDissmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objPopupWindowLargeSource.dismiss();
            }
        });
    }


    public class SaveProfileAPI extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                System.out.println("LATLNG NEW : " + objSharedPreferences.getString(Constants.USER_REGISTERED_LATITUDE, "") + " : " + objSharedPreferences.getString(Constants.USER_REGISTERED_LONGITUDE, ""));

                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));
                objJsonObject.put("city", "");
                objJsonObject.put("address", "");
                objJsonObject.put("about_me", "");
                objJsonObject.put("latitude", params[2]);
                objJsonObject.put("longitude", params[3]);
                objJsonObject.put("gender", params[0]);
                objJsonObject.put("dob", params[1]);
                return NetworkUtils.postData(Constants.UPDATE_PROFILE_API, objJsonObject.toString());
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                makeAllEditableOrNot(true);
                JSONObject objJsonObject = new JSONObject(s);
                if (objJsonObject.getBoolean("result")) {
                    SharedPreferences.Editor objEditor = objSharedPreferences.edit();
                    objEditor.putString(Constants.PROFILE_COMPLETE, "1");
                    objEditor.putString(Constants.LOGIN_FLAG, "Y");
                    objEditor.commit();
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setDataToMyShoutAdapter() {

/*
 for (int index = 0; index < arrMyShouts.size(); index++) {
                            final MyShoutModel objMyShoutModel = (MyShoutModel) arrMyShouts.get(index);
                            View objView;
                            if (objMyShoutModel.getSHOUT_TYPE().equals("L")) {
                                objView = getLayoutInflater().inflate(R.layout.profile_shouts_left_side_cell, objLinearLayoutMyShouts, false);
                            } else {
                                objView = getLayoutInflater().inflate(R.layout.profile_shouts_right_side_cell, objLinearLayoutMyShouts, false);
                            }
                            CustomFontTextView txtMessage = (CustomFontTextView) objView.findViewById(R.id.txt_shout_default_message);
                            CustomFontTextView txtTitle = (CustomFontTextView) objView.findViewById(R.id.txt_shout_default_title);
                            TextView txtDate = (TextView) objView.findViewById(R.id.txt_shout_default_time_date);
                            ImageView objProfileImage = (ImageView) objView.findViewById(R.id.profile_image_shout_default);
                            // TEXT
                            TextView txtLike = (TextView) objView.findViewById(R.id.txt_shout_default_like);
                            TextView txtShare = (TextView) objView.findViewById(R.id.txt_shout_default_share);
                            TextView txtComments = (TextView) objView.findViewById(R.id.txt_shout_default_comments);
                            // IMAGE
                            ImageView imageLike = (ImageView) objView.findViewById(R.id.image_shout_default_like);
                            ImageView imageShare = (ImageView) objView.findViewById(R.id.image_shout_default_share);
                            ImageView imageComments = (ImageView) objView.findViewById(R.id.image_shout_default_comments);

                            txtLike.setTextColor(this.getResources().getColor(R.color.text_color_shout_default_un_select));
                            txtShare.setTextColor(this.getResources().getColor(R.color.text_color_shout_default_un_select));
                            txtComments.setTextColor(this.getResources().getColor(R.color.text_color_shout_default_un_select));

                            txtLike.setAllCaps(true);
                            txtShare.setAllCaps(true);
                            txtComments.setAllCaps(true);


                            TextView txtUserName = (TextView) objView.findViewById(R.id.txt_user_name_shout_default);
                            final ImageView shoutImage = (ImageView) objView.findViewById(R.id.img_shout_image);
                            final RelativeLayout objRelativeLayoutMessageBody = (RelativeLayout) objView.findViewById(R.id.relative_message);

                            txtTitle.setText(objMyShoutModel.getSHOUT_TITLE());
                            txtMessage.setText(objMyShoutModel.getSHOUT_DESC());
                            txtDate.setText(objMyShoutModel.getCREATED_DATE());
                            txtUserName.setText(objMyShoutModel.getUSER_NAME());

                            if (Integer.parseInt(objMyShoutModel.getCOMMENT_COUNT()) > 0) {
                                imageComments.setBackgroundResource(R.drawable.comments_red);
                                txtComments.setTextColor(this.getResources().getColor(R.color.text_color_shout_default_selected));
                                txtComments.setText(objMyShoutModel.getCOMMENT_COUNT() + " COMMENTS ");
                            } else {
                                imageComments.setBackgroundResource(R.drawable.comments_grey);
                                txtComments.setTextColor(this.getResources().getColor(R.color.text_color_shout_default_un_select));
                                txtComments.setText(objMyShoutModel.getCOMMENT_COUNT() + " COMMENTS ");
                            }
                            System.out.println("PRASAD IMAGE URL : " + objMyShoutModel.getSHOUT_IMAGE());
                            if (objMyShoutModel.getSHOUT_IMAGE().equals("")) {
                                shoutImage.setVisibility(ImageView.GONE);
                            } else {
                                //                shoutImage.setImageUrl(objMyShoutModel.getSHOUT_IMAGE(), imageLoader);
                                if (ConnectivityBroadcastReceiver.isConnected()) {
                                    Picasso.with(this).load(objMyShoutModel.getSHOUT_IMAGE()).noFade().into(shoutImage);
                                } else {
                                    Picasso.with(this).load(objMyShoutModel.getSHOUT_IMAGE()).noFade().networkPolicy(NetworkPolicy.OFFLINE).into(shoutImage);
                                }
                            }

                            if (ConnectivityBroadcastReceiver.isConnected()) {
                                Picasso.with(this).load(objMyShoutModel.getUSER_PIC()).noFade().transform(new CircleTransform()).into(objProfileImage);
                            } else {
                                Picasso.with(this).load(objMyShoutModel.getUSER_PIC()).noFade().transform(new CircleTransform()).networkPolicy(NetworkPolicy.OFFLINE).into(objProfileImage);
                            }
                            objProfileImage.setPadding(Constants.DEFAULT_CIRCLE_PADDING,
                                    Constants.DEFAULT_CIRCLE_PADDING,
                                    Constants.DEFAULT_CIRCLE_PADDING,
                                    Constants.DEFAULT_CIRCLE_PADDING);

                            if (Integer.parseInt(objMyShoutModel.getLIKE_COUNT()) > 0) {
                                imageLike.setBackgroundResource(R.drawable.like_red);
                                txtLike.setTextColor(this.getResources().getColor(R.color.text_color_shout_default_selected));
                                txtLike.setText(objMyShoutModel.getLIKE_COUNT() + " Likes");
                            } else {
                                imageLike.setBackgroundResource(R.drawable.like_grey);
                                txtLike.setTextColor(this.getResources().getColor(R.color.text_color_shout_default_un_select));
                            }
                            objLinearLayoutMyShouts.addView(objView);
                        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn_gender:
                showGenderPopup();
                break;
            case R.id.edt_profile_screen_dob:
                System.out.println("DATE PICKER IS OPEN PRINT");
                final Calendar objCalendar = Calendar.getInstance();
                new DatePickerDialog(ProfileScreenActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
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
                        objButtonSelectDob.setText(sendDate1);
                        strDateOfBirth = sendDate1;
                        if (sendDate1.length() > 0) {
                            objTextViewAge.setText(getAge(year, monthOfYear, dayOfMonth) + " YRS");
                            objTextViewBirthDateErrorText.setVisibility(TextView.INVISIBLE);
                        }
                    }
                }, objCalendar.get(Calendar.YEAR), objCalendar.get(Calendar.MONTH), objCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    /*  @Override
      public void getLatitudeLongitude(LatLng objLatLng) {
          currentLatitude = objLatLng.latitude;
          currentLongitude = objLatLng.longitude;
          System.out.println("MY CURRENT LAT : " + currentLatitude + " LONG : " + currentLongitude);
      }
  */
    public class GetUserDetailsAPI extends AsyncTask<String, Void, String> {

        final ProgressDialog objProgressDialog = new ProgressDialog(ProfileScreenActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objProgressDialog.setMessage("Loading User Details");
            objProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("user_id", params[0]);
                return NetworkUtils.postData(Constants.GET_USER_PROFILE_DETAILS, objJsonObject.toString());
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
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
                    JSONArray objJsonArray = new JSONArray(objJsonObject.getString("user"));
                    for (int index = 0; index < objJsonArray.length(); index++) {
                        btnProfileImage.setVisibility(Button.GONE);
                        profileImage.setImageDrawable(null);
                        profileImage.setBackgroundResource(R.drawable.red_border);
                        if (objJsonArray.getJSONObject(index).getString("id").toString().equals(objSharedPreferences.getString(Constants.USER_ID, ""))) {
                            SharedPreferences.Editor objEditor = objSharedPreferences.edit();
                            objEditor.putString(Constants.PROFILE_IMAGE_URL, objJsonArray.getJSONObject(index).getString("profile_pic"));
                            objEditor.commit();
                        }
                        strLargeImageUrl = objJsonArray.getJSONObject(index).getString("profile_pic");
                        profileImage.setImageDrawable(null);
//                        profileImage.setImageUrl(strLargeImageUrl, imageLoader);
                        if (ConnectivityBroadcastReceiver.isConnected()) {
                            Picasso.with(ProfileScreenActivity.this).load(strLargeImageUrl).transform(new CircleTransform()).into(profileImage);
                        } else {
                            Picasso.with(ProfileScreenActivity.this).load(strLargeImageUrl).networkPolicy(NetworkPolicy.OFFLINE).transform(new CircleTransform()).into(profileImage);
                        }
                        profileImage.setPadding(6, 6, 6, 6);
                        profileImage.setScaleType(CircularNetworkImageView.ScaleType.FIT_XY);
                        editTextFirstName.setText(objJsonArray.getJSONObject(index).getString("first_name").toString());
                        editTextLastName.setText(objJsonArray.getJSONObject(index).getString("last_name").toString());
                        objEditTextContactNumber.setText(objJsonArray.getJSONObject(index).getString("mobile_no").toString());
                        objEditTextEmail.setText(objJsonArray.getJSONObject(index).getString("email").toString());
                        objEditTextCity.setText(objJsonArray.getJSONObject(index).getString("city").toString());
                        objEditTextProfileAdderss.setText(objJsonArray.getJSONObject(index).getString("address").toString());
                        objEditTextDescription.setText(objJsonArray.getJSONObject(index).getString("about_me").toString());
                        String strDOB[] = objJsonArray.getJSONObject(index).getString("dob").split("/");
                        strDateOfBirth = objJsonArray.getJSONObject(index).getString("dob");
                        if (objJsonArray.getJSONObject(index).getString("gender").equals("M")) {
                            strGender = "M";
                            objImageViewGenderImage.setImageResource(R.drawable.male);
                        } else if (objJsonArray.getJSONObject(index).getString("gender").equals("F")) {
                            strGender = "F";
                            objImageViewGenderImage.setImageResource(R.drawable.female);
                        } else {
                            showGenderPopup();
                            makeAllEditableOrNot(true);
                        }
                        if (objJsonArray.getJSONObject(index).getString("dob").length() > 0) {
                            objButtonSelectDob.setText(objJsonArray.getJSONObject(index).getString("dob"));
                            int age = getAge(Integer.parseInt(strDOB[2]), Integer.parseInt(strDOB[1]), Integer.parseInt(strDOB[0]));
                            System.out.println("CALCULATED AGE : " + age);
                            objTextViewAge.setText(age + " YRS");
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

    public class UpdateProfileAPI extends AsyncTask<String, Void, String> {

        String from = "";

        public UpdateProfileAPI(String from) {
            this.from = from;
        }

        final ProgressDialog objProgressDialog = new ProgressDialog(ProfileScreenActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objProgressDialog.setMessage("Saving your profile...");
            objProgressDialog.show();
            objProgressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                System.out.println("LATLNG NEW : " + objSharedPreferences.getString(Constants.USER_REGISTERED_LATITUDE, "") + " : " + objSharedPreferences.getString(Constants.USER_REGISTERED_LONGITUDE, ""));

                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));
                objJsonObject.put("city", params[0]);
                objJsonObject.put("address", params[1]);
                objJsonObject.put("about_me", params[2]);
                objJsonObject.put("latitude", objSharedPreferences.getString(Constants.USER_REGISTERED_LATITUDE, ""));
                objJsonObject.put("longitude", objSharedPreferences.getString(Constants.USER_REGISTERED_LONGITUDE, ""));
                objJsonObject.put("gender", strGender);
                objJsonObject.put("dob", strDateOfBirth);
                return NetworkUtils.postData(Constants.UPDATE_PROFILE_API, objJsonObject.toString());
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (objProgressDialog.isShowing())
                    objProgressDialog.dismiss();
                makeAllEditableOrNot(false);

                if (from.equals("BOOK")) {
                    Intent objIntent = new Intent(ProfileScreenActivity.this, InviteFriendsActivity.class);
                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(objIntent);
                    finish();
                    overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                } else if (from.equals("BOARD")) {
                    Intent objIntent = new Intent(ProfileScreenActivity.this, ShoutDefaultActivity.class);
                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(objIntent);
                    finish();
                    overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                }

            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setMyLoactionButtonPosition() {
        Fragment fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_profile_screen));
        ViewGroup v1 = (ViewGroup) fragment.getView();
        ViewGroup v2 = (ViewGroup) v1.getChildAt(0);
        ViewGroup v3 = (ViewGroup) v2.getChildAt(2);
        View position = (View) v3.getChildAt(0);
        int positionWidth = position.getLayoutParams().width;
        int positionHeight = position.getLayoutParams().height;

        //lay out position button
        RelativeLayout.LayoutParams positionParams = new RelativeLayout.LayoutParams(positionWidth, positionHeight);
        /*int margin = positionWidth / 5;
        positionParams.setMargins(margin, 0, 0, margin);*/
        positionParams.setMargins(0, 0, 10, 10);
        positionParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        positionParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        position.setLayoutParams(positionParams);
    }

    private void setMap() {
        try {
            mGoogleMap = (TouchableMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_profile_screen);
            mGoogleMap.getMapAsync(this);

            setMyLoactionButtonPosition();

            mGoogleMap.setTouchListener(new TouchableWrapper.OnTouchListener() {
                ImageView objMarker = (ImageView) findViewById(R.id.map_profile_screen_marker);

                @Override
                public void onTouch() {
                    System.out.println("MAP TOUCH ACTIVATED");
                    objLinearLayoutSearchBar.setVisibility(LinearLayout.GONE);
                    Constants.hideToTop(objLinearLayoutSearchBar);
                    objTextViewMapAddressSelect.setVisibility(TextView.GONE);
                    Constants.hideToBottom(objTextViewMapAddressSelect);
                }

                @Override
                public void onRelease() {
                    System.out.println("MAP TOUCH DE-ACTIVATED");
                    objLinearLayoutSearchBar.setVisibility(LinearLayout.VISIBLE);
                    Constants.show(objLinearLayoutSearchBar);
                    objTextViewMapAddress.setVisibility(TextView.VISIBLE);
                    Constants.show(objTextViewMapAddressSelect);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //profile upload goes here
    private void selectImage() {


        // POPUP WINDOWS OBJECTS FOR SHOWING USER CHOOSED RESOURCES
        LayoutInflater objPopupInflater;
        View customCategoryAlertLayout;
        final PopupWindow objPopupWindowCategories;
        ImageView objImageViewVideo;
        ImageView objImageViewPhoto;
        ImageView objImageViewGallery;
        ImageView objImageViewFacebook;


        TextView objTextViewVideo;
        TextView objTextViewFacebook;
        RelativeLayout objRelativeCategoryPopupOutsideTouch;

        objPopupInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customCategoryAlertLayout = objPopupInflater.inflate(R.layout.capture_shoot_popup, null, true);
        objRelativeCategoryPopupOutsideTouch = (RelativeLayout) customCategoryAlertLayout.findViewById(R.id.relative_shoot_out_side_touch);
        objImageViewVideo = (ImageView) customCategoryAlertLayout.findViewById(R.id.imageViewVideo);
        objImageViewPhoto = (ImageView) customCategoryAlertLayout.findViewById(R.id.imageViewPhoto);
        objImageViewGallery = (ImageView) customCategoryAlertLayout.findViewById(R.id.imageViewGallery);
        objImageViewFacebook = (ImageView) customCategoryAlertLayout.findViewById(R.id.imageViewFacebook);


        objTextViewVideo = (TextView) customCategoryAlertLayout.findViewById(R.id.textViewVideo);
        objTextViewFacebook = (TextView) customCategoryAlertLayout.findViewById(R.id.textViewfacebook);


        objPopupWindowCategories = new PopupWindow(customCategoryAlertLayout);
        objPopupWindowCategories.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        objPopupWindowCategories.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        objPopupWindowCategories.setFocusable(true);

        objPopupWindowCategories.showAtLocation(customCategoryAlertLayout, Gravity.CENTER, 0, 0);

        objImageViewVideo.setVisibility(ImageView.GONE);
        objTextViewVideo.setVisibility(TextView.GONE);
        objImageViewFacebook.setVisibility(ImageView.VISIBLE);
        objTextViewFacebook.setVisibility(TextView.VISIBLE);

        objRelativeCategoryPopupOutsideTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objPopupWindowCategories.dismiss();
            }
        });

        objImageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objPopupWindowCategories.dismiss();
                cameraIntent();
            }
        });
        objImageViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objPopupWindowCategories.dismiss();
                galleryIntent();
            }
        });

        objImageViewFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objPopupWindowCategories.dismiss();
                new ResetFacebookProfile().execute();
            }
        });
    }

    public class ResetFacebookProfile extends AsyncTask<String, Void, String> {

        RelativeLayout objRelativeLayoutProfileLoading = (RelativeLayout) findViewById(R.id.relative_profile_loading);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                objRelativeLayoutProfileLoading.setVisibility(RelativeLayout.VISIBLE);
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
                objJsonObject.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));
                strResult = NetworkUtils.postData(Constants.RESET_FACEBOOK_PROFILE_API, objJsonObject.toString());
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
                final JSONObject objJsonObject = new JSONObject(s);
                if (objJsonObject.getString("result").equals("true")) {
                    System.out.println("UPDATED URL : " + objJsonObject.getString("url").toString());
                    if (objJsonObject.getString("url").toString().length() > 0) {
                        imageLoader.get(objJsonObject.getString("url"), new ImageLoader.ImageListener() {
                            @Override
                            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                try {
//                                    profileImage.setImageUrl(objJsonObject.getString("url"), imageLoader);
                                    if (ConnectivityBroadcastReceiver.isConnected()) {
                                        Picasso.with(ProfileScreenActivity.this).load(objJsonObject.getString("url")).transform(new CircleTransform()).into(profileImage);
                                    } else {
                                        Picasso.with(ProfileScreenActivity.this).load(objJsonObject.getString("url")).networkPolicy(NetworkPolicy.OFFLINE).transform(new CircleTransform()).into(profileImage);
                                    }
                                    objRelativeLayoutProfileLoading.setVisibility(RelativeLayout.GONE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                objRelativeLayoutProfileLoading.setVisibility(RelativeLayout.GONE);
                            }
                        });
                        SharedPreferences.Editor objEditor = objSharedPreferences.edit();
                        objEditor.putString(Constants.PROFILE_IMAGE_URL, objJsonObject.getString("url"));
                        objEditor.commit();
                        strLargeImageUrl = objJsonObject.getString("url");
                        System.out.println("UPDATED URL : " + objSharedPreferences.getString(Constants.PROFILE_IMAGE_URL, ""));
                        DatabaseHelper objDatabaseHelper = new DatabaseHelper(ProfileScreenActivity.this);
                        objDatabaseHelper.updateProfilePic(objJsonObject.getString("url"), objSharedPreferences.getString(Constants.USER_ID, ""));
                    }
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void galleryIntent() {
        try{
            Intent intentGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentGallery, Constants.REQUEST_MADE_FOR_GALLERY);
        }catch(NullPointerException ne){
            ne.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void cameraIntent() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("RESULT CODE : " + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.REQUEST_MADE_FOR_GALLERY) {
                try {
                    final Uri uri = data.getData();
                    ImageCompressionAsyncTask imageCompressionAsyncTaskCamera = new ImageCompressionAsyncTask() {
                        @Override
                        protected void onPostExecute(final byte[] imageBytes) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        new UploadProfilePicAPI(applicationUtils.saveImageOnSdCard(imageBytes)).execute(applicationUtils.saveImageOnSdCard(imageBytes).getPath());
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
                                        new UploadProfilePicAPI(applicationUtils.saveImageOnSdCard(imageBytes)).execute(applicationUtils.saveImageOnSdCard(imageBytes).getPath());
                                    }catch(NullPointerException ne){
                                        ne.printStackTrace();
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    };
                    imageCompressionAsyncTaskCamera.execute(applicationUtils.getRealPathFromURI(mCapturedImageURI));
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    objTextViewMapAddress.setText(place.getAddress());
                    objGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.552834f));
                    SharedPreferences.Editor objEditor = objSharedPreferences.edit();
                    objEditor.putString(Constants.USER_REGISTERED_LATITUDE, String.valueOf(place.getLatLng().latitude));
                    objEditor.putString(Constants.USER_REGISTERED_LONGITUDE, String.valueOf(place.getLatLng().longitude));
                    objEditor.commit();
                    System.out.println("LATLNG UPDATED : " + objSharedPreferences.getString(Constants.USER_REGISTERED_LATITUDE, "") + " : " + objSharedPreferences.getString(Constants.USER_REGISTERED_LONGITUDE, ""));
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    // TODO: Handle the error.
//                    Log.i(TAG, status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
            }
        }
    }

    private void onCaptureImageResult(Intent data) {

    }

    public class UploadProfilePicAPI extends AsyncTask<String, Void, String> {

        RelativeLayout objRelativeLayoutProfileLoading = (RelativeLayout) findViewById(R.id.relative_profile_loading);
        Uri resourcePath = null;

        public UploadProfilePicAPI(Uri resourcePath) {
            this.resourcePath = resourcePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objRelativeLayoutProfileLoading.setVisibility(RelativeLayout.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String strResult = "";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(Constants.UPLOAD_PROFILE_IMAGE_API);
                HttpResponse response = null;
                MultipartEntity entityBuilder = new MultipartEntity();
                entityBuilder.addPart("user_id", new StringBody(objSharedPreferences.getString(Constants.USER_ID, "").toString()));
                System.out.println("PROFILE IMAGE UPLOAD PATH : " + params[0] + " USER ID : " + objSharedPreferences.getString(Constants.USER_ID, "").toString());
                File file = new File(Uri.parse(params[0]).getPath());
                FileBody objFile = new FileBody(file);
                entityBuilder.addPart("Image", objFile);
                post.setEntity(entityBuilder);
                response = client.execute(post);
                HttpEntity httpEntity = response.getEntity();
                strResult = EntityUtils.toString(httpEntity);
                Utils.d("UPLOAD PROFILE IMAGE MULTIPART RESPONSE", strResult);
                return strResult;
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (ClientProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                final JSONObject objJsonObject = new JSONObject(s);
                System.out.println("UPDATED URL : " + objJsonObject.getString("url").toString());
                if (objJsonObject.getString("url").toString().length() > 0) {

                    // TODO: 9/29/2016 SET IMAGE PATH (URI) DIRECTLY TO IMAGE VIEW TO GET NOTIFY QUIKLY. i.e IMAGE UPLOADED SUCCESSFULLY.
                    Picasso.with(ProfileScreenActivity.this).load(resourcePath).transform(new CircleTransform()).noFade().into(profileImage);
                    /*if (ConnectivityBroadcastReceiver.isConnected()) {
                        Picasso.with(ProfileScreenActivity.this).load(objJsonObject.getString("url")).transform(new CircleTransform()).into(profileImage);
                    } else {
                        Picasso.with(ProfileScreenActivity.this).load(objJsonObject.getString("url")).networkPolicy(NetworkPolicy.OFFLINE).transform(new CircleTransform()).into(profileImage);
                    }*/

                    objRelativeLayoutProfileLoading.setVisibility(RelativeLayout.GONE);

                    SharedPreferences.Editor objEditor = objSharedPreferences.edit();
                    objEditor.putString(Constants.PROFILE_IMAGE_URL, objJsonObject.getString("url"));
                    objEditor.commit();
                    strLargeImageUrl = objJsonObject.getString("url");
                    System.out.println("UPDATED URL : " + objSharedPreferences.getString(Constants.PROFILE_IMAGE_URL, ""));
                    DatabaseHelper objDatabaseHelper = new DatabaseHelper(ProfileScreenActivity.this);
                    objDatabaseHelper.updateProfilePic(objJsonObject.getString("url"), objSharedPreferences.getString(Constants.USER_ID, ""));
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap createThumbNail(Uri uri) {

        InputStream imageStream = null;
        try {
            imageStream = ProfileScreenActivity.this.getContentResolver().openInputStream(
                    uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        try {
            stream.close();
            stream = null;
        } catch (IOException e) {

            e.printStackTrace();
        }
        return bitmap;
    }

    private void createDirectory(String applicationPath) {
        if (!new File(Constants.APPLICATION_PATH).exists()) {
            //create directory if not exist's
            File dir = new File(Constants.APPLICATION_PATH);
            dir.mkdir();
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

    }

    @Override
    public void onBackPressed() {
      /*  new AlertDialog.Builder(ProfileScreenActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
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
        if (objRelativeLayoutMap.getVisibility() == RelativeLayout.VISIBLE) {
            objRelativeLayoutMap.setVisibility(RelativeLayout.GONE);
        } else {
            handleBackEvent();
        }
    }

    private void handleBackEvent() {
        Intent objIntent;

        System.out.println("BACK SCREEN ACTIVITY NAME : " + strBackScreenName);

        if (strBackScreenName.equals(Constants.SHOUT_DETAIL_SCREEN)) {
            /*objIntent = new Intent(ProfileScreenActivity.this, ShoutDetailActivity.class);
            startActivity(objIntent);
            finish();
            SharedPreferences.Editor objEditor = objSharedPreferences.edit();
            objEditor.putString(Constants.PROFILE_BACK_SCREEN_NAME, "");
            objEditor.commit();
            overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);*/
            super.onBackPressed();

        } else if (strBackScreenName.equals(Constants.SHOUT_MESSAGE_USER_LIST_SCREEN)) {
            objIntent = new Intent(ProfileScreenActivity.this, ShoutUsersListActivity.class);
            startActivity(objIntent);
            finish();
            SharedPreferences.Editor objEditor = objSharedPreferences.edit();
            objEditor.putString(Constants.PROFILE_BACK_SCREEN_NAME, "");
            objEditor.commit();
            overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
        } else if (strBackScreenName.equals(Constants.SHOUT_DEFAULT_SCREEN)) {
           /* objIntent = new Intent(ProfileScreenActivity.this, ShoutDefaultActivity.class);
            objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(objIntent);
            finish();
            SharedPreferences.Editor objEditor = objSharedPreferences.edit();
            objEditor.putString(Constants.PROFILE_BACK_SCREEN_NAME, "");
            objEditor.commit();
            overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);*/
            super.onBackPressed();
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void makeAllEditableOrNot(boolean status) {
        Utils.d("debug", "MAKE EDITABLE OR NOT EVENT FIRED");

        if (status) {

            objImageViewOpenMap.setVisibility(ImageView.VISIBLE);
            //user want to edit the fields
            objTextViewEditProfileClick.setText(SAVE_PROFILE_TEXT);
            //set background for textView
            objTextViewEditProfileClick.setTextColor(getResources().getColor(R.color.green_background_color));
            //if true all are editable
            editableOrNot(status);

            btnProfileImage.setVisibility(Button.VISIBLE);

            //set padding
            int leftPadding = 15;
            objEditTextCity.setPadding(leftPadding, 0, 0, 0);
            objEditTextProfileAdderss.setPadding(leftPadding, 0, 0, 0);
            objEditTextDescription.setPadding(leftPadding, 0, 0, 0);
            objButtonSelectDob.setPadding(0, 0, 0, 0);

            //set background
            objEditTextCity.setBackground(getResources().getDrawable(R.drawable.background_edit_profile_default_grey));
            objEditTextProfileAdderss.setBackground(getResources().getDrawable(R.drawable.background_edit_profile_default_grey));
            objEditTextDescription.setBackground(getResources().getDrawable(R.drawable.background_edit_profile_default_grey));
            objButtonSelectDob.setBackground(getResources().getDrawable(R.drawable.background_edit_profile_default_grey));

            //set focus to the city
            objEditTextCity.requestFocus();
        } else {

            btnProfileImage.setVisibility(Button.GONE);

            objImageViewOpenMap.setVisibility(ImageView.GONE);
            objTextViewEditProfileClick.setText(EDIT_PROFILE_TEXT);
            //set background for textView
            objTextViewEditProfileClick.setTextColor(getResources().getColor(R.color.red_background_color));
            //if true all are editable

            editableOrNot(status);

            //set padding
            objEditTextCity.setPadding(0, 0, 0, 0);
            objEditTextProfileAdderss.setPadding(0, 0, 0, 0);
            objButtonSelectDob.setPadding(0, 0, 0, 0);
            //set background
            objEditTextCity.setBackground(getResources().getDrawable(R.color.transparent));
            objEditTextProfileAdderss.setBackground(getResources().getDrawable(R.color.transparent));
            objEditTextDescription.setBackground(getResources().getDrawable(R.color.transparent));
            objButtonSelectDob.setBackground(getResources().getDrawable(R.color.transparent));
        }
    }


    public int getAge(int _year, int _month, int _day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a = 0;
        try {
            y = cal.get(Calendar.YEAR);
            m = cal.get(Calendar.MONTH);
            d = cal.get(Calendar.DAY_OF_MONTH);
            cal.set(_year, _month, _day);
            a = y - cal.get(Calendar.YEAR);
            if ((m < cal.get(Calendar.MONTH))
                    || ((m == cal.get(Calendar.MONTH)) && (d < cal
                    .get(Calendar.DAY_OF_MONTH)))) {
                --a;
            }
            if (a < 0) {
//            throw new IllegalArgumentException("Age < 0");
                Toast.makeText(ProfileScreenActivity.this, "Age must greater then 15", Toast.LENGTH_SHORT).show();
            } else {
                return a;
            }
        } catch (IllegalArgumentException ae) {
            ae.printStackTrace();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return a;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void editableOrNot(boolean status) {
        objButtonSelectDob.setEnabled(status);
        objEditTextCity.setEnabled(status);
        objEditTextProfileAdderss.setEnabled(status);
        objEditTextDescription.setEnabled(status);
        objTextViewAge.setEnabled(status);
        objImageButtonGender.setEnabled(status);
        if (status) {
            objTextViewAge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDatePickerPopup();
                }
            });
            objButtonSelectDob.setOnClickListener(this);
            objImageButtonGender.setOnClickListener(this);
            objImageButtonGender.setClickable(status);
        } else {
            objButtonSelectDob.setOnClickListener(null);
            objImageButtonGender.setOnClickListener(null);
            objImageButtonGender.setClickable(status);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        objGoogleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                googleMap.clear();
                System.out.println("LOCATION_TAG : DEFAULT LOCATION ");

                System.out.println("PRASANNA PRINT VALUE : " + objSharedPreferences.getString(Constants.USER_REGISTERED_LATITUDE, ""));

                if (objSharedPreferences.getString(Constants.USER_REGISTERED_LATITUDE, "").equals("")) {
                    latLong = new LatLng(0.0, 0.0);
                } else {
                    latLong = new LatLng(Double.parseDouble(objSharedPreferences.getString(Constants.USER_REGISTERED_LATITUDE, "")),
                            Double.parseDouble(objSharedPreferences.getString(Constants.USER_REGISTERED_LONGITUDE, "")));
                }
                if (objSharedPreferences.getString(Constants.USER_REGISTERED_LOCATION_ZOOM, "").equals("")) {
                    try {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 15.552834f));
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        float zoomlevel = Float.parseFloat(objSharedPreferences.getString(Constants.USER_REGISTERED_LOCATION_ZOOM, ""));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, zoomlevel));
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                    @Override
                    public void onCameraChange(CameraPosition arg0) {
                        // TODO Auto-generated method stub
                        center = googleMap.getCameraPosition().target;
                        googleMap.clear();
                        try {
                            System.out.println("LATLNG OLD : " + objSharedPreferences.getString(Constants.USER_REGISTERED_LATITUDE, "") + " : " + objSharedPreferences.getString(Constants.USER_REGISTERED_LONGITUDE, ""));
                            SharedPreferences.Editor objEditor = objSharedPreferences.edit();
                            objEditor.putString(Constants.USER_REGISTERED_LATITUDE, String.valueOf(center.latitude));
                            objEditor.putString(Constants.USER_REGISTERED_LONGITUDE, String.valueOf(center.longitude));
                            objEditor.putString(Constants.USER_REGISTERED_LOCATION_ZOOM, String.valueOf(googleMap.getCameraPosition().zoom));
                            objEditor.commit();
                            new GetLocationAsync(center.latitude, center.longitude).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public static void updateMapLocation(double latitude, double longitude) {
        LatLng latLong = new LatLng(latitude, longitude);
        CameraUpdate objCameraUpdate = CameraUpdateFactory.newLatLng(latLong);
        zoom = CameraUpdateFactory.zoomTo(15.552834f);
        objGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 15.552834f));
    }*/

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
                geocoder = new Geocoder(ProfileScreenActivity.this, Locale.ENGLISH);
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
                    ProfileScreenActivity.this.runOnUiThread(new Runnable() {
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
                objTextViewMapAddress.setText(addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1) + " ");
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
