package com.shout.shoutapplication.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Environment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.util.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Constants {


    public static final String SHOUT_UPDATE_INTENT = "com.capternal.shoutupdate";

    public static final int SPLASHSCREENTIME = 3000;
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final int DEFAULT_CIRCLE_PADDING = 3;
    public static final int DEFAULT_MESSAGE_BOARD_PADDING = 2;
    public static final int CHAT_SCREEN_SHOUT_IMAGE_BORDER_WIDTH = 2;
    public static final int MAX_RESOURCE_SIZE = 3;
    public static final int SHOUT_PASS_ENGAGE_BUTTON_DYNAMIC_HEIGHT = 100;
    public static final int SHOUT_PASS_ENGAGE_BUTTON_DYNAMIC_WIDTH = 100;
    public static final int DEFAULT_Y = 50;

    public static final String INTERNET_CONNECTION_MESSAGE = "No Internet Connection";

    public static final String SHOUT_LIST_DATE_FORMAT = "dd/MM/yyyy";


    // USED FOR CUSTOM LOCATION SEARCH
    public static final String API_NOT_CONNECTED = "Google API not connected";
    public static final String SOMETHING_WENT_WRONG = "OOPs!!! Something went wrong...";
    public static final String SMS_ORIGIN = "-MSHOUT";
    public static String PlacesTag = "Google Places Auto Complete";

    public static final String LOCATION_PREFERENCES = "location_preferences";
    public static final String LOCATION_LATITUDE = "location_latitude";
    public static final String LOCATION_LONGITUDE = "location_longitude";


    public static final String MY_PREFERENCES = "app_details";
    public static final String SELECTED_FLAG_POSITION = "selected_flag_position";


    public static final String APPLICATION_PATH = Environment.getExternalStorageDirectory() + "/shout/";
    public static final String VIDEO_THUMBNAIL_PATH = Environment.getExternalStorageDirectory() + "/shout/video/thumbnails/";

    public static final int REQUEST_MADE_FOR_CAMERA = 100;
    public static final int REQUEST_MADE_FOR_GALLERY = 200;
    public static final int REQUEST_MADE_FOR_VIDEO = 300;

    // API
    public static final String HTTP_URL = "http://capternal.com/shout_dev/";
    public static final String THUMB_BASE_URL = "http://capternal.com/shout_dev/files/shout/";
    public static final String BASE_URL = "http://capternal.com/shout_dev/api/";

    public static final String CATEGORY_LIST_API = BASE_URL + "category/list";
    public static final String CHAT_ACTIVE_FROM_ID = "active_from_id";
    public static final String CHAT_ACTIVE_TO_ID = "active_to_id";
    public static final String CHAT_MESSAGE = "chat_message";
    public static final String CHAT_CREATED = "chat_created";
    public static final String CHAT_MESSAGE_TYPE = "chat_message_type";
    public static final String CHAT_IMAGE_URL = "chat_image_url";
    public static final String CHAT_FROM_ID = "chat_from_id";
    public static final String CHAT_TO_ID = "chat_to_id";
    public static final String CHAT_IS_PROCESSED = "chat_is_processed";
    public static final String CHAT_MESSAGE_GROUP_MEMBERS = "chat_message_group_members";
    public static final String CHAT_MESSAGE_TITLE = "chat_message_title";
    public static final String CHAT_SCREEN_ACTIVE = "screen_active";
    public static final String CHAT_SCREEN_APPONENT_USER_NAME = "chat_screen_apponent_user_name";
    public static final String CHAT_SEND_MESSAGE_API = BASE_URL + "messages/save";
    public static final String CHAT_SEND_PHOTO_API = BASE_URL + "messages/save_photo";
    public static final String CREATE_SHOUT = BASE_URL + "shout/save";
    public static final String CREATE_SHOUT_ATIVITY = "CreateShoutActivity";
    public static final String CURRENT_ACTIVITY_NAME_FOR_SHOW_MAP = "activity_context";
    public static final String DEVICE_TOKEN = "device_token";
    public static final String FRIENDS_COMPARE_API = BASE_URL + "friends/compare";
    public static final String HIDE_SHOUT_API = BASE_URL + "shout/hide";
    public static final String VERIFY_OTP = BASE_URL + "user/verify_otp";
    public static final String LIKE_SHOUT_API = BASE_URL + "shout/like_shout";
    public static final String LOAD_OLD_MESSAGES_API = BASE_URL + "messages/history";
    public static final String LOGIN_URL = BASE_URL + "user/login";
    public static final String MAKE_COMMENT_API = BASE_URL + "shout/comment_shout";
    public static final String MESSAGE_SHOUT_LIST_API = BASE_URL + "messages/shout_list";
    public static final String MESSAGE_USER_SHOUT_LIST_API = BASE_URL + "messages/user_list";
    public static final String RECEIVE_OTP = BASE_URL + "user/send_otp";
    public static final String RESHOUT_API = BASE_URL + "shout/reshout";
    public static final String SHOUT_DETAIL_API = BASE_URL + "shout/shout_details";
    public static final String SHOUT_LIST = BASE_URL + "shout/list";
    public static final String SHOUT_PASS_API = BASE_URL + "shout/pass";
    public static final String UPLOAD_RESOURCES = BASE_URL + "upload/save";
    public static final String GET_USER_PROFILE_DETAILS = BASE_URL + "user/user_details";
    public static final String UPDATE_PROFILE_API = BASE_URL + "user/edit_profile";
    public static final String UPLOAD_PROFILE_IMAGE_API = BASE_URL + "user/upload_profile_pic";
    public static final String LOGGED_IN_USER_SHOUTS_API = BASE_URL + "user/user_shout";
    public static final String MY_PREFRENCES_API = BASE_URL + "user/preferences";
    public static final String SAVE_PREFRENCES_API = BASE_URL + "user/save_preferences";
    public static final String SHOUT_COMMENTS_API = BASE_URL + "shout/comment";
    public static final String VIEW_SHOUT_API = BASE_URL + "shout/view_shout";
    public static final String CHAT_MESSAGES_READ_API = BASE_URL + "messages/read";
    public static final String GET_SHOUT_RESOURCES = BASE_URL + "shout/media";
    public static final String RESET_FACEBOOK_PROFILE_API = BASE_URL + "user/reset_profile";
    public static final String SHOUT_ENGAGE_API = BASE_URL + "shout/engage";
    public static final String SHOUTERS_API = BASE_URL + "shout/shouters";
    public static final String PUSH_NOTIFICATION_CHATS_INTO_DB = BASE_URL + "messages/push_history";
    public static final String GET_LIKED_SHOUTERS_INFO_API = BASE_URL + "shout/like";
    public static final String GET_SHOUT_BOARD_UPDATED_RECORDS = BASE_URL + "shout/update_data";
    public static final String NEIGHBOURS_SHOUT_API = BASE_URL + "shout/near";
    public static final String NOTIFICATION_LIST_API = BASE_URL + "notification/list";
    public static final String REPORT_API = BASE_URL + "report/add";
    public static final String FRIEND_ACCEPT_API = BASE_URL + "friends/accept";



    // PREFERENCES FOR CHAT SCREEN
    public static final String CHAT_PREFERENCES = "chat_preferences";

    public static final String CHAT_APPONENT_ID = "chat_apponent_id";
    public static final String CHAT_APPONENT_USER_NAME = "chat_apponent_user_name";
    public static final String CHAT_APPONENT_PROFILE_PIC = "chat_apponent_user_profile";
    public static final String CHAT_SHOUT_ID = "chat_shout_id";
    public static final String CHAT_SHOUT_TITLE = "chat_shout_title";
    public static final String CHAT_SHOUT_TYPE = "chat_shout_type";
    public static final String CHAT_SHOUT_IMAGE = "chat_shout_image";
    public static final String CHAT_BACK = "chat_back";
    public static final String CHAT_LAST_TRANSACTION_SIDE = "chat_last_transaction_";


    // LOCAL TEMP STORAGE VARIABLES
    public static final String PROFILE_PREFERENCES = "user_profile";

    public static final String USER_ID = "user_id";
    public static final String USER_REGISTERED_LATITUDE = "user_latitude";
    public static final String USER_REGISTERED_LONGITUDE = "user_longitude";
    public static final String USER_REGISTERED_LOCATION_ZOOM = "user_location_zoom";
    public static final String SORT_POPULARITY = "sort_popularity";
    public static final String SORT_RECENCY = "sort_recency";
    public static final String SORT_LOCATION = "sort_location";
    public static final String OTP_VERIFIED = "otp_verified";
    public static final String PROFILE_COMPLETE = "profile_complete";
    public static final String USER_NOTIFICATION_COUNT = "user_notification_count";
    public static final String USER_FACEBOOK_FRIENDS_JSON = "user_friends_json";


    public static final String USER_NAME = "user_name";
    public static final String PROFILE_SCREEN_USER_ID = "profile_screen_user_id";
    public static final String IS_NEW_USER = "is_registered_first_time";
    public static final String IS_SHOUT_LIKED = "is_shout_liked";
    public static final String LOGIN_FLAG = "login_flag_for_profile_screen";
    public static final String PROFILE_ATIVITY = "ProfileScreenActivity";
    public static final String PROFILE_EMAIL_ID = "user_profile_email";
    public static final String PROFILE_IMAGE_URL = "user_profile_url";
    public static final String PROFILE_LOGIN_STATUS = "login_status";
    public static final String SHOUT_PASS_ALERT_SHOW_STATUS = "shout_pass_alert_show_status";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String SHOUT_HIDDEN_FOR_DETAIL_SCREEN = "shout_hidden_status";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String USER_FACEBOOK_ID = "user_facebook_id";
    public static final String USER_ID_FOR_DETAIL_SCREEN = "shout_detail_screen_user_id";
    public static final String SHOUT_SINGLE_IMAGE_FOR_DETAIL = "shout_detail_screen_single_shout_image";
    public static final String USER_PROFILE_URL_FOR_DETAIL_SCREEN = "temp_user_profile_url";
    public static final String SHOUT_LIKE_COUNT = "shout_like_count_for_detail_screen";
    public static final String SHOUT_CREATED_DATE = "shout_create_date_for_detail_screen";
    public static final String SHOUT_TITLE = "shout_title_for_detail_screen";
    public static final String SHOUT_DESCRIPTION = "shout_description_for_detail_screen";
    public static final String SHOUT_USER_NAME = "shout_user_name_for_detail_screen";
    public static final String IS_CURRENT_DATE = "is_current_date"; // STORE CURRENT DATE INSTANCE TO LOAD SHOUTS FROM SERVER
    public static final String SHOUT_HIDDEN_STATUS = "is_shout_hidden"; // STORE CURRENT DATE INSTANCE TO LOAD SHOUTS FROM SERVER

    public static final String CHAT_BACK_SCREEN_NAME = "chat_back_screen_name";
    public static final String PROFILE_BACK_SCREEN_NAME = "profile_back_screen_name";

    //TO HANDLE BACK EVENT, BACK ACTIITY SCREEN NAMES
    public static final String CALL_FROM_MY_SHOUTS = "display_logged_in_user_shouts";
    public static final String SHOUT_DEFAULT_SCREEN = "ShoutDefaultActivity";
    public static final String SHOUT_DETAIL_SCREEN = "ShoutDetailActivity";
    public static final String SHOUT_MESSAGE_USER_LIST_SCREEN = "ShoutUsersListActivity";
    public static final String SHOUT_MESSAGE_BOARD_SCREEN = "MessageBoardActivity";

    public static final String SENDER_ID = "483344937684";

    // TO HANDLE BACK EVENT ON CHAT SCREEN
    public static final String SHOUT_ID_FOR_DETAIL_SCREEN = "shout_detail_screen_shout_id";
    public static final String SHOUT_TYPE_FOR_DETAIL_SCREEN = "shout_detail_screen_shout_type";

    public static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void generateKeyHash(Context context, String packageName) {
        // CODE TO GENERATE KEY HASH
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void hideToBottom(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight() + 300);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    public static void show(final View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void hideToTop(final View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight() - 300);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        DisplayMetrics objDisplayMetrics = new DisplayMetrics();
//        final int initialWidth = v.getMeasuredWidth();
        final int initialWidth = objDisplayMetrics.widthPixels;
        System.out.println("SCREEN WIDTH " + initialWidth);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.INVISIBLE);
                } else {
                   /* v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();*/
                    System.out.println("TIME : " + (initialWidth - (int) (initialWidth * interpolatedTime)));
                    v.getLayoutParams().width = initialWidth - (int) (initialWidth * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration((int) (initialWidth / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        final int targetWidth = v.getMeasuredWidth();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().width = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().width = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetWidth * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration((int) (targetWidth / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void showInternetToast(Activity objActivity) {
        Toast.makeText(objActivity, Constants.INTERNET_CONNECTION_MESSAGE, Toast.LENGTH_SHORT).show();
    }

}
