package com.shout.shoutapplication.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.ConnectivityBroadcastReceiver;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.NotificationBroadcastReceiver;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.app.AppController;
import com.shout.shoutapplication.database.DatabaseHelper;
import com.shout.shoutapplication.login.InviteFriendsActivity;
import com.shout.shoutapplication.login.LoginActivity;
import com.shout.shoutapplication.login.ProfileScreenActivity;
import com.shout.shoutapplication.main.MessageBoardActivity;
import com.shout.shoutapplication.main.NotificationListActivity;
import com.shout.shoutapplication.main.PreferencesActivity;
import com.shout.shoutapplication.main.ShoutDefaultActivity;
import com.shout.shoutapplication.others.CircleTransform;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class BaseActivity extends ActionBarActivity implements NotificationBroadcastReceiver.NotificationCountListener {


    public static SlidingDrawer objSlidingDrawer;
    public static ImageView objDrawerOpen;
    ImageView objDrawerClose;
    RelativeLayout objRelativeBase;
    FrameLayout objFrameLayoutContainer;
    public static Button btnDrawer;
    TextView objTextViewProfileName;
    ImageView objProfileImage;
    SharedPreferences objSharedPreferences;

    boolean blockFrameLayout = false;
    DatabaseHelper objDatabaseHelper;


    TextView objTextViewShoutBook;
    TextView objTextViewNotifications;
    TextView objTextViewMessages;
    TextView objTextViewSpreadLove;
    TextView objTextViewPreferences;
    TextView objTextViewLogout;
    TextView objTextViewBaseEditProfile;

    // BOTTOM TABS IMAGE BUTTON
    public static LinearLayout objLinearLayoutDrawerOptions;

    public static ImageButton objImageButtonCreateShout;

    public static ImageButton objImageButtonShoutBook;
    public static ImageButton objImageButtonNotifications;
    public static ImageButton objImageButtonMessages;
    public static ImageButton objImageButtonSpreadLove;
    public static ImageButton objImageButtonPreferences;
    public static ImageButton objImageButtonLogout;

    public static RelativeLayout objRelativeDefaultTop;
    public static RelativeLayout objRelativeCustomTop;
    public static TextView objTextViewDrawerCustomBack;
    public static TextView objTextViewDrawerCustomDownArrow;

    public static RelativeLayout objRelativeBaseBottomTabs;
    public static ImageView objShoutImage;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    ImageView objImageViewDefaultLoading;
    TextView objTextViewNotificationCount;
    public static ImageButton objImageNotificationCount;
    public static RelativeLayout objRelativeLayoutDefaultLoading;


    @Override
    public void setContentView(int layoutResID) {
        objRelativeBase = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        objFrameLayoutContainer = (FrameLayout) objRelativeBase.findViewById(R.id.frame_layout_container);
        getLayoutInflater().inflate(layoutResID, objFrameLayoutContainer, true);
        super.setContentView(objRelativeBase);

        objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
        objDatabaseHelper = new DatabaseHelper(this);

        init();

        objFrameLayoutContainer.setOnTouchListener(onTouchListener);
    }

    protected void init() {

        objTextViewNotificationCount = (TextView) findViewById(R.id.txt_notification_count);
        objImageNotificationCount = (ImageButton)findViewById(R.id.img_notification_count);

        objImageViewDefaultLoading = (ImageView) findViewById(R.id.default_loading);
        objRelativeLayoutDefaultLoading = (RelativeLayout) findViewById(R.id.relative_default_progress);

        Animation sampleFadeAnimation = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.loading_progress_animation);
        objImageViewDefaultLoading.startAnimation(sampleFadeAnimation);

        objRelativeDefaultTop = (RelativeLayout) findViewById(R.id.relative_upper_base);
        objRelativeCustomTop = (RelativeLayout) findViewById(R.id.relative_upper_custom);
        objTextViewDrawerCustomBack = (TextView) findViewById(R.id.txt_drawer_back_custom);
        objTextViewDrawerCustomDownArrow = (TextView) findViewById(R.id.drawer_down_arrow_custom);
        objShoutImage = (ImageView) findViewById(R.id.image_shout_base_drawer);

        objSlidingDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);
        objDrawerOpen = (ImageView) findViewById(R.id.drawer_open);
        objDrawerClose = (ImageView) findViewById(R.id.drawer_close);
        btnDrawer = (Button) findViewById(R.id.btn_base_drawer);
        objTextViewProfileName = (TextView) findViewById(R.id.txt_profile_name);
        objProfileImage = (ImageView) findViewById(R.id.img_profile);


        objLinearLayoutDrawerOptions = (LinearLayout) findViewById(R.id.linear_drawer_options);
        objTextViewBaseEditProfile = (TextView) findViewById(R.id.txt_base_edit_profile);

        objLinearLayoutDrawerOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("DO NOTHING");
            }
        });

        objTextViewBaseEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences objProfileSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor objEditor = objProfileSharedPreferences.edit();
                objEditor.putString(Constants.PROFILE_BACK_SCREEN_NAME, Constants.SHOUT_DEFAULT_SCREEN);
                objEditor.commit();

                Intent objIntent = new Intent(BaseActivity.this, ProfileScreenActivity.class);
                startActivity(objIntent);
                finish();
                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
            }
        });

        objTextViewShoutBook = (TextView) findViewById(R.id.txt_base_shoutbook);
        objTextViewNotifications = (TextView) findViewById(R.id.txt_base_notifications);
        objTextViewMessages = (TextView) findViewById(R.id.txt_base_messages);
        objTextViewSpreadLove = (TextView) findViewById(R.id.txt_base_spreadlove);
        objTextViewPreferences = (TextView) findViewById(R.id.txt_base_preferences);
        objTextViewLogout = (TextView) findViewById(R.id.txt_base_logout);


        objImageButtonShoutBook = (ImageButton) findViewById(R.id.image_shout_book);
        objImageButtonNotifications = (ImageButton) findViewById(R.id.image_notification);
        objImageButtonMessages = (ImageButton) findViewById(R.id.image_messages);
        objImageButtonSpreadLove = (ImageButton) findViewById(R.id.image_spread_love);
        objImageButtonPreferences = (ImageButton) findViewById(R.id.image_preferences);
        objImageButtonLogout = (ImageButton) findViewById(R.id.imgbutton_drawer_logout);

        objImageButtonShoutBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(BaseActivity.this, InviteFriendsActivity.class);
                startActivity(objIntent);
                finish();


                /*
                CLEARING THE SORT FILTERS

                SharedPreferences.Editor objEditor = objSharedPreferences.edit();
                objEditor.putString(Constants.SORT_POPULARITY, "0");
                objEditor.putString(Constants.SORT_RECENCY, "0");
                objEditor.putString(Constants.SORT_LOCATION, "0");
                objEditor.commit();*/

                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
            }
        });

        objImageButtonNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objSlidingDrawer.animateClose();
                Intent objIntent = new Intent(BaseActivity.this, NotificationListActivity.class);
                startActivity(objIntent);
                overridePendingTransition(0, 0);
            }
        });

        objImageButtonMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(BaseActivity.this, MessageBoardActivity.class);
                startActivity(objIntent);
                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                finish();
            }
        });

        objImageButtonSpreadLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objSlidingDrawer.animateClose();
                String shareBody = "Spread the love";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share this"));

                /*Intent objIntent = new Intent(BaseActivity.this, ShareActivity.class);
                startActivity(objIntent);
                finish();
                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);*/
            }
        });

        objImageButtonPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objSlidingDrawer.animateClose();
                Intent objIntent = new Intent(BaseActivity.this, PreferencesActivity.class);
                startActivity(objIntent);
                finish();
                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
            }
        });

        objImageButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

//        setListener();

        objRelativeBaseBottomTabs = (RelativeLayout) findViewById(R.id.relative_bottom_tab);

        try {
            Typeface custom_font = Typeface.createFromAsset(BaseActivity.this.getAssets(), "AvenirNext-Medium.ttf");
            objTextViewShoutBook.setTypeface(custom_font);
            objTextViewNotifications.setTypeface(custom_font);
            objTextViewMessages.setTypeface(custom_font);
            objTextViewSpreadLove.setTypeface(custom_font);
            objTextViewPreferences.setTypeface(custom_font);
            objTextViewLogout.setTypeface(custom_font);
            objTextViewProfileName.setTypeface(custom_font);
            objTextViewBaseEditProfile.setTypeface(custom_font);

        } catch (Exception e) {
            e.printStackTrace();
        }


        objImageButtonCreateShout = (ImageButton) findViewById(R.id.imagebutton_base_add);

        btnDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShoutDefaultActivity.objRelativeLayoutSearchBox.getVisibility() == RelativeLayout.VISIBLE) {
                    ShoutDefaultActivity.openCloseSearchBar(false, BaseActivity.this);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                } else {
                    System.out.println("DRAWER CLICKED");
                    objSlidingDrawer.animateOpen();
                }
            }
        });

        objSlidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                objDrawerOpen.setVisibility(ImageView.GONE);
                objDrawerClose.setVisibility(ImageView.VISIBLE);
                blockFrameLayout = true;
            }
        });
        objSlidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                objDrawerOpen.setVisibility(ImageView.GONE);
                objDrawerClose.setVisibility(ImageView.GONE);
                blockFrameLayout = false;
            }
        });

        objDrawerClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objSlidingDrawer.animateClose();
            }
        });
        if (objSharedPreferences.getString(Constants.PROFILE_IMAGE_URL, "").toString().length() > 0) {
            System.out.println("UPDATED URL DRAWER : " + objSharedPreferences.getString(Constants.PROFILE_IMAGE_URL, ""));

            BaseActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //objProfileImage.setImageUrl(objSharedPreferences.getString(Constants.PROFILE_IMAGE_URL, ""), imageLoader);
                    if (ConnectivityBroadcastReceiver.isConnected()) {
                        Picasso.with(BaseActivity.this).load(objSharedPreferences.getString(Constants.PROFILE_IMAGE_URL, "").toString()).transform(new CircleTransform()).into(objProfileImage);
                    } else {
                        Picasso.with(BaseActivity.this).load(objSharedPreferences.getString(Constants.PROFILE_IMAGE_URL, "").toString()).networkPolicy(NetworkPolicy.OFFLINE).transform(new CircleTransform()).into(objProfileImage);
                    }

                }
            });
            objProfileImage.setPadding(4, 4, 4, 4);
        } else {
            Picasso.with(BaseActivity.this).load(R.drawable.dummy_image).transform(new CircleTransform()).
                    into(objProfileImage);
        }
        if (objSharedPreferences.getString(Constants.USER_NAME, "").toString().length() > 0) {
            objTextViewProfileName.setText(objSharedPreferences.getString(Constants.USER_NAME, "").toString());
        }

        objProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor objEditor = objSharedPreferences.edit();
                objEditor.putString(Constants.PROFILE_SCREEN_USER_ID, objSharedPreferences.getString(Constants.USER_ID, ""));
                objEditor.commit();

                Intent objIntent = new Intent(BaseActivity.this, ProfileScreenActivity.class);
                startActivity(objIntent);
                finish();
                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
            }
        });
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Utils.d("DEBUG_TAG", "SCROLL VIEW NOT SCROLL");
            return blockFrameLayout;
        }
    };

    public void hideBottomTabs() {
        objRelativeBaseBottomTabs.setVisibility(RelativeLayout.GONE);
    }

    public void showBottomTabs() {
        objRelativeBaseBottomTabs.setVisibility(RelativeLayout.VISIBLE);
    }

    public void hideDefaultTopHeader() {
        objRelativeDefaultTop.setVisibility(RelativeLayout.GONE);
        objRelativeCustomTop.setVisibility(RelativeLayout.VISIBLE);
    }

    public void showDefaultTopHeader() {
        objRelativeDefaultTop.setVisibility(RelativeLayout.VISIBLE);
        objRelativeCustomTop.setVisibility(RelativeLayout.GONE);
    }

    public void hideBothTopHeader() {
        objRelativeDefaultTop.setVisibility(RelativeLayout.GONE);
        objRelativeCustomTop.setVisibility(RelativeLayout.GONE);
    }

    public void hideAllView() {
        objRelativeDefaultTop.setVisibility(RelativeLayout.GONE);
        objRelativeCustomTop.setVisibility(RelativeLayout.GONE);
    }

    public void showDialog() {
        // CUSTOM ALERT DIALOG
        LayoutInflater factory = LayoutInflater.from(BaseActivity.this);
        final View customAlertLayout = factory.inflate(R.layout.custom_alert_dialog_layout, null);
        final AlertDialog objLogoutAlertDialog = new AlertDialog.Builder(BaseActivity.this).create();
        objLogoutAlertDialog.setView(customAlertLayout);
        customAlertLayout.findViewById(R.id.txt_alert_yes).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FacebookSdk.sdkInitialize(BaseActivity.this);
                LoginManager.getInstance().logOut();

                objDatabaseHelper.deleteTable(DatabaseHelper.strTableNameMessageUserList);
                objDatabaseHelper.deleteTable(DatabaseHelper.strTableNameMessageBoard);
                objDatabaseHelper.deleteTable(DatabaseHelper.strTableNameChat);
                objDatabaseHelper.deleteTable(DatabaseHelper.strTableNameFriends);
                objDatabaseHelper.deleteTable(DatabaseHelper.strTableNameShout);

                System.out.println("SURESH FACEBOOK LOGOUT SUCCESSFUL ...");
                System.out.println("LOGOUT BEFORE: LOGIN STATUS : " + objSharedPreferences.getString(Constants.PROFILE_LOGIN_STATUS, ""));
                objSharedPreferences.edit().clear().commit();
                System.out.println("LOGOUT AFTER: LOGIN STATUS : " + objSharedPreferences.getString(Constants.PROFILE_LOGIN_STATUS, ""));

                /*objEditor.putString(Constants.PROFILE_LOGIN_STATUS, "false");
                objEditor.putString(Constants.USER_NAME, "");
                objEditor.putString(Constants.PROFILE_IMAGE_URL, "");
                objEditor.putString(Constants.PROFILE_EMAIL_ID, "");
                objEditor.putString(Constants.LOGIN_FLAG, "0");
                objEditor.putString(Constants.USER_ID, "");
                objEditor.putString(Constants.USER_FACEBOOK_ID, "");
                objEditor.commit();*/

                Intent objIntent = new Intent(BaseActivity.this, LoginActivity.class);
                startActivity(objIntent);
                finish();
                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
            }
        });
        customAlertLayout.findViewById(R.id.txt_alert_no).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                objLogoutAlertDialog.dismiss();
            }
        });
        objLogoutAlertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        objLogoutAlertDialog.show();
    }

    @Override
    public void onNotificationReceived(int count) {
        Utils.d("NOTIFICATION_COUNT",String.valueOf(count));
        updateNotificationCount(String.valueOf(count));

    }

    public void updateNotificationCount(String strCount) {
        objTextViewNotificationCount.setText(strCount);
    }
}
