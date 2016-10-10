package com.shout.shoutapplication.main.Adapter;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.shout.shoutapplication.CustomClasses.CustomFontTextView;
import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.ConnectivityBroadcastReceiver;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.NetworkUtils;
import com.shout.shoutapplication.app.AppController;
import com.shout.shoutapplication.database.DatabaseHelper;
import com.shout.shoutapplication.login.ProfileScreenActivity;
import com.shout.shoutapplication.main.ChatForShoutActivity;
import com.shout.shoutapplication.main.Model.LikedShoutersModel;
import com.shout.shoutapplication.main.Model.ShoutDefaultListModel;
import com.shout.shoutapplication.main.ShoutDefaultActivity;
import com.shout.shoutapplication.main.ShoutDetailActivity;
import com.shout.shoutapplication.others.BitmapBorderTransformation;
import com.shout.shoutapplication.others.CircleTransform;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by CapternalSystems on 5/31/2016.
 */
public class ShoutDefaultViewPagerAdapter extends PagerAdapter implements View.OnClickListener {

    ArrayList<ShoutDefaultListModel> arrShoutDefaultListModel = new ArrayList<ShoutDefaultListModel>();
    ImageView leftImage;
    RelativeLayout objRelativeLayoutReshout;
    TextView objTextViewReshoutCount;
    TextView objTextViewReshoutText;
    ImageView rightImage;
    RelativeLayout objRelativeLayoutShoutHideShow;
    ImageView objImageViewHideShowIcon;
    TextView objTextViewHideShowActionLable;
    TextView txtLike;
    TextView txtShare;
    TextView txtComments;
    ImageView imageLike;
    ImageView imageShare;
    ImageView imageComments;
    ShoutDefaultListModel objShoutDefaultListModel;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SharedPreferences objProfileSharedPreferences;
    EditText objEditTextSearchView;
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
    private Context objContext;
    private Activity objActivity;
    private int intPosition;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;

    private RefreshListListener objRefreshListListener;


    public ShoutDefaultViewPagerAdapter(Context objContext, Activity objActivity, ArrayList<ShoutDefaultListModel> arrShoutDefaultListModel, int intPosition) {
        this.objContext = objContext;
        this.objActivity = objActivity;
        this.arrShoutDefaultListModel = arrShoutDefaultListModel;
        this.intPosition = intPosition;
        objShoutDefaultListModel = arrShoutDefaultListModel.get(intPosition);
        objEditTextSearchView = (EditText) objActivity.findViewById(R.id.edt_search_shout_default_header);
        objProfileSharedPreferences = objActivity.getSharedPreferences(Constants.PROFILE_PREFERENCES, Context.MODE_PRIVATE);
        objPopupInflater = (LayoutInflater) objActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customLikeView = objPopupInflater.inflate(R.layout.like_popup, null, true);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object instantiateItem(final View container, int position) {
        LayoutInflater inflater = (LayoutInflater) objContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout view = null;
        if (position == 0) {
            view = (RelativeLayout) inflater.inflate(R.layout.left_layout, null);
            leftImage = (ImageView) view.findViewById(R.id.image_engage);
            leftImage.setOnClickListener(this);
            objRelativeLayoutReshout = (RelativeLayout) view.findViewById(R.id.relative_shout_default_reshout);
            objTextViewReshoutCount = (TextView) view.findViewById(R.id.txt_reshout_count);
            objTextViewReshoutText = (TextView) view.findViewById(R.id.txt_reshout_text);
            ((ViewPager) container).addView(view, 0);

            if (objShoutDefaultListModel.getUSER_ID().equals(objProfileSharedPreferences.getString(Constants.USER_ID, ""))) {
                objRelativeLayoutReshout.setVisibility(RelativeLayout.VISIBLE);
                leftImage.setVisibility(ImageView.GONE);
                if (Integer.parseInt(objShoutDefaultListModel.getRE_SHOUT()) > 0) {
                    objTextViewReshoutCount.setText(objShoutDefaultListModel.getRE_SHOUT());
                    objTextViewReshoutText.setText("RESHOUT");
                    objRelativeLayoutReshout.setBackgroundResource(R.drawable.rounded_corner_shout_default_red);
                } else {
                    objTextViewReshoutCount.setText(objShoutDefaultListModel.getRE_SHOUT());
                    objTextViewReshoutText.setText("RESHOUT");
                    objRelativeLayoutReshout.setBackgroundResource(R.drawable.rounded_corner_shout_default_grey);
                }
                objRelativeLayoutReshout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewPager) container).setCurrentItem(1);
                        if (Integer.parseInt(objShoutDefaultListModel.getRE_SHOUT()) != 0) {
                            new ReshoutApi(objShoutDefaultListModel.getSHOUT_ID()).execute();
                        }
                    }
                });
            } else {
                objRelativeLayoutReshout.setVisibility(RelativeLayout.GONE);
                leftImage.setVisibility(ImageView.VISIBLE);
                leftImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewPager) container).setCurrentItem(1);
                        SharedPreferences objSharedPreferences = objActivity.getSharedPreferences(Constants.PROFILE_PREFERENCES, objActivity.MODE_PRIVATE);
                        if (!objShoutDefaultListModel.getUSER_ID().equals(objSharedPreferences.getString(Constants.USER_ID, ""))) {
                            if (ConnectivityBroadcastReceiver.isConnected()) {

                                SharedPreferences objChatPreferences = objActivity.getSharedPreferences(Constants.CHAT_PREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor objDataChatEditor = objChatPreferences.edit();
                                objDataChatEditor.putString(Constants.CHAT_APPONENT_ID, objShoutDefaultListModel.getUSER_ID());
                                objDataChatEditor.putString(Constants.CHAT_APPONENT_USER_NAME, objShoutDefaultListModel.getUSER_NAME());
                                objDataChatEditor.putString(Constants.CHAT_APPONENT_PROFILE_PIC, objShoutDefaultListModel.getPROFILE_IMAGE_URL());
                                objDataChatEditor.putString(Constants.CHAT_SHOUT_ID, objShoutDefaultListModel.getSHOUT_ID());
                                objDataChatEditor.putString(Constants.CHAT_SHOUT_TITLE, objShoutDefaultListModel.getTITLE());
                                objDataChatEditor.putString(Constants.CHAT_SHOUT_TYPE, objShoutDefaultListModel.getSHOUT_TYPE());
                                objDataChatEditor.putString(Constants.CHAT_SHOUT_IMAGE, objShoutDefaultListModel.getSHOUT_IMAGE());
                                objDataChatEditor.putString(Constants.CHAT_BACK, Constants.SHOUT_DEFAULT_SCREEN);
                                objDataChatEditor.commit();


                                Intent objIntent = new Intent(objActivity, ChatForShoutActivity.class);
                                objActivity.startActivity(objIntent);
                                objActivity.overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                            } else {
                                Constants.showInternetToast(objActivity);
                            }
                        }
                    }
                });
            }

        }
        if (position == 1) {
            if (objShoutDefaultListModel.getSHOUT_TYPE().equals("L")) {
                view = (RelativeLayout) inflater.inflate(R.layout.view_pager_row_item_left, null);
            } else {
                view = (RelativeLayout) inflater.inflate(R.layout.view_pager_row_item_right, null);
            }
            CustomFontTextView txtMessage = (CustomFontTextView) view.findViewById(R.id.txt_shout_default_message);
            CustomFontTextView txtTitle = (CustomFontTextView) view.findViewById(R.id.txt_shout_default_title);
            TextView txtDate = (TextView) view.findViewById(R.id.txt_shout_default_time_date);
            TextView txtDistance = (TextView) view.findViewById(R.id.txt_shout_default_distance);

            ImageView objProfileImage = (ImageView) view.findViewById(R.id.profile_image_shout_default);
            // TEXT
            txtLike = (TextView) view.findViewById(R.id.txt_shout_default_like);
            txtShare = (TextView) view.findViewById(R.id.txt_shout_default_share);
            txtComments = (TextView) view.findViewById(R.id.txt_shout_default_comments);

            txtLike.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_un_select));
            txtShare.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_un_select));
            txtComments.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_un_select));

            txtLike.setAllCaps(true);
            txtShare.setAllCaps(true);
            txtComments.setAllCaps(true);

            // IMAGE
            imageLike = (ImageView) view.findViewById(R.id.image_shout_default_like);
            imageShare = (ImageView) view.findViewById(R.id.image_shout_default_share);
            imageComments = (ImageView) view.findViewById(R.id.image_shout_default_comments);


            TextView txtUserName = (TextView) view.findViewById(R.id.txt_user_name_shout_default);
            final ImageView shoutImage = (ImageView) view.findViewById(R.id.img_shout_image);
            final RelativeLayout objRelativeLayoutMessageBody = (RelativeLayout) view.findViewById(R.id.relative_message);

            txtTitle.setText(objShoutDefaultListModel.getTITLE());
            txtMessage.setText(objShoutDefaultListModel.getDESCRIPTION());
            txtDate.setText(objShoutDefaultListModel.getDATE_TIME());
            txtDistance.setText(objShoutDefaultListModel.getDISTANCE());
            txtUserName.setText(objShoutDefaultListModel.getUSER_NAME());

//            txtComments.setText(objShoutDefaultListModel.getCOMMENT_COUNT() + " Comments\n" + objShoutDefaultListModel.getENGAGE_COUNT() + " Engaging");
            System.out.println("COMMENT COUNT : " + Integer.parseInt(objShoutDefaultListModel.getCOMMENT_COUNT()));
            if (Integer.parseInt(objShoutDefaultListModel.getCOMMENT_COUNT()) > 0) {
                imageComments.setBackgroundResource(R.drawable.comments_red);
                txtComments.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_selected));
                txtComments.setText(objShoutDefaultListModel.getCOMMENT_COUNT() + " COMMENTS ");
            } else {
                imageComments.setBackgroundResource(R.drawable.comments_grey);
                txtComments.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_un_select));
                txtComments.setText(objShoutDefaultListModel.getCOMMENT_COUNT() + " COMMENTS ");
            }
            System.out.println("PRASAD IMAGE URL : " + objShoutDefaultListModel.getSHOUT_IMAGE());
            if (objShoutDefaultListModel.getSHOUT_IMAGE().equals("")) {
                shoutImage.setVisibility(ImageView.GONE);
            } else {
                Picasso.with(objActivity).setLoggingEnabled(true);
                if (ConnectivityBroadcastReceiver.isConnected()) {
                    Picasso.with(objActivity).load(objShoutDefaultListModel.getSHOUT_IMAGE()).transform(new BitmapBorderTransformation(0, 15, Color.RED)).noFade().into(shoutImage);
                } else {
                    Picasso.with(objActivity).load(objShoutDefaultListModel.getSHOUT_IMAGE()).transform(new BitmapBorderTransformation(0, 15, Color.RED)).noFade().networkPolicy(NetworkPolicy.OFFLINE).into(shoutImage);
                }
            }
         /*   Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            objProfileImage.setBackgroundColor(color);*/
            try {
                if (ConnectivityBroadcastReceiver.isConnected()) {
                    Picasso.with(objActivity).load(objShoutDefaultListModel.getPROFILE_IMAGE_URL()).noFade().transform(new CircleTransform()).into(objProfileImage);
                } else {
                    Picasso.with(objActivity).load(objShoutDefaultListModel.getPROFILE_IMAGE_URL()).noFade().transform(new CircleTransform()).networkPolicy(NetworkPolicy.OFFLINE).into(objProfileImage);
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            objProfileImage.setPadding(Constants.DEFAULT_CIRCLE_PADDING,
                    Constants.DEFAULT_CIRCLE_PADDING,
                    Constants.DEFAULT_CIRCLE_PADDING,
                    Constants.DEFAULT_CIRCLE_PADDING);

            if (Integer.parseInt(objShoutDefaultListModel.getLIKE_COUNT()) > 0) {
                imageLike.setBackgroundResource(R.drawable.like_red);
                txtLike.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_selected));
                txtLike.setText(objShoutDefaultListModel.getLIKE_COUNT() + " Likes");
            } else {
                imageLike.setBackgroundResource(R.drawable.like_grey);
                txtLike.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_un_select));
            }

            txtMessage.setOnClickListener(this);
            txtShare.setOnClickListener(this);
            txtLike.setOnClickListener(this);
            imageShare.setOnClickListener(this);
            imageComments.setOnClickListener(this);
            imageLike.setOnClickListener(this);
            objProfileImage.setOnClickListener(this);
            objRelativeLayoutMessageBody.setOnClickListener(this);

            // TO CALLCULATE HEIGHT AND WIDTH OF CURRENT LAYOUT i.e RelativeLayout
            ViewTreeObserver vto = objRelativeLayoutMessageBody.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    objRelativeLayoutMessageBody.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int width = objRelativeLayoutMessageBody.getMeasuredWidth();
                    int height = objRelativeLayoutMessageBody.getMeasuredHeight();
                    System.out.println("LAYOUT POSITION : " + intPosition + " HEIGHT : " + height);
                    objShoutDefaultListModel.setDYNAMIC_HEIGHT(height);
                    System.out.println("");
                    objShoutDefaultListModel.setDYNAMIC_Y(objRelativeLayoutMessageBody.getY());
                    notifyDataSetChanged();
                }
            });
            ((ViewPager) container).addView(view, 0);
        }
        if (position == 2) {
            view = (RelativeLayout) inflater.inflate(R.layout.right_layout, null);

            objRelativeLayoutShoutHideShow = (RelativeLayout) view.findViewById(R.id.relative_owner_shout_hide_show);
            objImageViewHideShowIcon = (ImageView) view.findViewById(R.id.image_shout_hide_show);
            objTextViewHideShowActionLable = (TextView) view.findViewById(R.id.txt_hide_show);

            rightImage = (ImageView) view.findViewById(R.id.image_pass);
            rightImage.setOnClickListener(this);
            ((ViewPager) container).addView(view, 0);

            if (objProfileSharedPreferences.getString(Constants.USER_ID, "").equals(objShoutDefaultListModel.getUSER_ID())) {
                rightImage.setVisibility(ImageView.GONE);
                objRelativeLayoutShoutHideShow.setVisibility(RelativeLayout.VISIBLE);
                if (objShoutDefaultListModel.getIS_SHOUT_HIDDEN() == 0) {
                    objImageViewHideShowIcon.setImageResource(R.drawable.shout_hide_white);
                    objTextViewHideShowActionLable.setText("HIDE");
                } else {
                    objImageViewHideShowIcon.setImageResource(R.drawable.shout_show_white);
                    objTextViewHideShowActionLable.setText("SHOW");
                }
            } else {
                objRelativeLayoutShoutHideShow.setVisibility(RelativeLayout.GONE);
                rightImage.setVisibility(ImageView.VISIBLE);
            }

            objRelativeLayoutShoutHideShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ViewPager) container).setCurrentItem(1);
                    if (objProfileSharedPreferences.getString(Constants.USER_ID, "").equals(objShoutDefaultListModel.getUSER_ID())) {
                        System.out.println("CONDITION MATCH");
                        if (objShoutDefaultListModel.getIS_SHOUT_HIDDEN() == 0) {
                            new AlertDialog.Builder(objActivity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
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
                            new AlertDialog.Builder(objActivity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
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
                    }
                }
            });

            objImageViewHideShowIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ViewPager) container).setCurrentItem(1);
                    if (objProfileSharedPreferences.getString(Constants.USER_ID, "").equals(objShoutDefaultListModel.getUSER_ID())) {
                        System.out.println("CONDITION MATCH");
                        if (objShoutDefaultListModel.getIS_SHOUT_HIDDEN() == 0) {
                            new AlertDialog.Builder(objActivity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
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
                            new AlertDialog.Builder(objActivity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
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
                    }
                }
            });

            objTextViewHideShowActionLable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ViewPager) container).setCurrentItem(1);
                    if (objProfileSharedPreferences.getString(Constants.USER_ID, "").equals(objShoutDefaultListModel.getUSER_ID())) {
                        System.out.println("CONDITION MATCH");
                        if (objShoutDefaultListModel.getIS_SHOUT_HIDDEN() == 0) {
                            new AlertDialog.Builder(objActivity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
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
                            new AlertDialog.Builder(objActivity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
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
                    }
                }
            });

            rightImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ViewPager) container).setCurrentItem(1);
                    if (objActivity.getSharedPreferences(Constants.PROFILE_PREFERENCES, objActivity.MODE_PRIVATE).getString(Constants.SHOUT_PASS_ALERT_SHOW_STATUS, "").equals("true")) {
                        showDialog();
                    } else {
                        new ShoutPassApi().execute();
                    }
                }
            });
        }
        view.setTag("myview" + position);
        /*try {
            System.out.println("PRASANNA PRINT HEIGHT : " + intPosition + "  :  " + objShoutDefaultListModel.getDYNAMIC_HEIGHT());
            System.out.println("PRASANNA PRINT Y CORD : " + intPosition + "  :  " + objShoutDefaultListModel.getDYNAMIC_Y());

            RelativeLayout.LayoutParams objRelativeLayoutParamsLeft = new RelativeLayout.LayoutParams(Constants.SHOUT_PASS_ENGAGE_BUTTON_DYNAMIC_WIDTH, objShoutDefaultListModel.getDYNAMIC_HEIGHT());
            objRelativeLayoutParamsLeft.setMargins(5, 2, 5, 0);
            leftImage.setY(objShoutDefaultListModel.getDYNAMIC_Y());
            leftImage.setLayoutParams(objRelativeLayoutParamsLeft);

            RelativeLayout.LayoutParams objRelativeLayoutParamsRight = new RelativeLayout.LayoutParams(Constants.SHOUT_PASS_ENGAGE_BUTTON_DYNAMIC_WIDTH, objShoutDefaultListModel.getDYNAMIC_HEIGHT());
            objRelativeLayoutParamsRight.setMargins(5, 2, 5, 0);
            rightImage.setY(objShoutDefaultListModel.getDYNAMIC_Y());
            rightImage.setLayoutParams(objRelativeLayoutParamsRight);

        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        return view;
    }

    public void showDialog() {
        // CUSTOM ALERT DIALOG
        LayoutInflater factory = LayoutInflater.from(objActivity);
        final View customAlertLayout = factory.inflate(R.layout.shout_pass_alert_dialog_layout, null);
        final AlertDialog objShoutPassAlertDialog = new AlertDialog.Builder(objActivity).create();
        objShoutPassAlertDialog.setView(customAlertLayout);
        customAlertLayout.findViewById(R.id.txt_alert_yes_shout_pass).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                objShoutPassAlertDialog.dismiss();
                new ShoutPassApi().execute();
            }
        });
        customAlertLayout.findViewById(R.id.txt_alert_no_shout_pass).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                objShoutPassAlertDialog.dismiss();
            }
        });
        CheckBox objCheckBox = (CheckBox) customAlertLayout.findViewById(R.id.check_box_dont_show_pass_alert_again_shout_pass);
        objCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    System.out.println("SHOUT PASS ALERT TRUE");
                    SharedPreferences.Editor objEditor = objActivity.getSharedPreferences(Constants.PROFILE_PREFERENCES, objActivity.MODE_PRIVATE).edit();
                    objEditor.putString(Constants.SHOUT_PASS_ALERT_SHOW_STATUS, "false");
                    objEditor.commit();
                } else {
                    System.out.println("SHOUT PASS ALERT FALSE");
                    SharedPreferences.Editor objEditor = objActivity.getSharedPreferences(Constants.PROFILE_PREFERENCES, objActivity.MODE_PRIVATE).edit();
                    objEditor.putString(Constants.SHOUT_PASS_ALERT_SHOW_STATUS, "true");
                    objEditor.commit();
                }
            }
        });
        objShoutPassAlertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        objShoutPassAlertDialog.show();
    }

    @Override
    public float getPageWidth(int position) {
        float nbPages = 3;
        if (position == 0 || position == 2)
            return 0.2f;
        return super.getPageWidth(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == ((View) obj);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public void onClick(View v) {
        Intent objIntent = null;
        SharedPreferences objSharedPreferences;
        SharedPreferences.Editor objEditor;

        switch (v.getId()) {
            case R.id.image_shout_default_comments:
            case R.id.txt_shout_default_comments:
                pushUserToDetailScreen();
                break;

            case R.id.relative_message:
                pushUserToDetailScreen();
                break;
            case R.id.profile_image_shout_default:
                if (ShoutDefaultActivity.keyBoardOpen) {
                    InputMethodManager inputMethodManager = (InputMethodManager) objActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(objEditTextSearchView.getApplicationWindowToken(), InputMethodManager.RESULT_HIDDEN, 0);
//                    ShoutDefaultActivity.openCloseSearchBar(false, objActivity);
                } else {
                    SharedPreferences objProfileSharedPreferences = objActivity.getSharedPreferences(Constants.PROFILE_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor objProfileEditor = objProfileSharedPreferences.edit();
                    objProfileEditor.putString(Constants.PROFILE_SCREEN_USER_ID, objShoutDefaultListModel.getUSER_ID());
                    objProfileEditor.putString(Constants.PROFILE_BACK_SCREEN_NAME, Constants.SHOUT_DEFAULT_SCREEN);
                    objProfileEditor.commit();
                    objIntent = new Intent(objActivity, ProfileScreenActivity.class);
                    objActivity.startActivity(objIntent);
//                    objActivity.finish();
                    objActivity.overridePendingTransition(0, 0);
                }
                break;
            case R.id.txt_shout_default_message:
                pushUserToDetailScreen();
                break;
            case R.id.txt_shout_default_share:
            case R.id.image_shout_default_share:
                if (ShoutDefaultActivity.keyBoardOpen) {
                    InputMethodManager inputMethodManager = (InputMethodManager) objActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(objEditTextSearchView.getApplicationWindowToken(), InputMethodManager.RESULT_HIDDEN, 0);
//                    ShoutDefaultActivity.openCloseSearchBar(false, objActivity);
                } else {
                    /*objIntent = new Intent(objActivity, ShareActivity.class);
                    objActivity.startActivity(objIntent);
                    objActivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    objActivity.finish();*/

                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, "Shout Application.");
                    objActivity.startActivity(Intent.createChooser(share, "Shout App"));
                }
                break;
            case R.id.txt_shout_default_like:
            case R.id.image_shout_default_like:
                if (ShoutDefaultActivity.keyBoardOpen) {
                    InputMethodManager inputMethodManager = (InputMethodManager) objActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(objEditTextSearchView.getApplicationWindowToken(), InputMethodManager.RESULT_HIDDEN, 0);
//                    ShoutDefaultActivity.openCloseSearchBar(false, objActivity);
                } else {
                    try {
                        objPopupWindowLargeSource = new PopupWindow(customLikeView);
                        objPopupWindowLargeSource.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                        objPopupWindowLargeSource.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
                        objPopupWindowLargeSource.setOutsideTouchable(true);
                        objPopupWindowLargeSource.setTouchable(true);
                        objPopupWindowLargeSource.setFocusable(true);
                        objPopupWindowLargeSource.setBackgroundDrawable(objActivity.getResources().getDrawable(R.drawable.rounded_corner_shout_detail_light_grey));
                        objPopupWindowLargeSource.setAnimationStyle(R.style.PopupAnimation);
                        objPopupWindowLargeSource.showAtLocation(customLikeView, Gravity.CENTER, 0, 0);

                        objImageLike = (ImageView) customLikeView.findViewById(R.id.image_view_like_view);
                        objTextViewLike = (TextView) customLikeView.findViewById(R.id.txt_like_view_like_text);
                        objRelativeLayoutLikeView = (RelativeLayout) customLikeView.findViewById(R.id.relativeLayout_like_view);
                        objListViewShouters = (ListView) customLikeView.findViewById(R.id.listview_shouters);
                        objTextViewNoLike = (TextView) customLikeView.findViewById(R.id.txt_no_likes_popup);

                        new GetLikedShoutersInfo().execute(objShoutDefaultListModel.getSHOUT_ID());

                        System.out.println("PRASANNA PRINT SHOUT LIKE COUNT : " + objShoutDefaultListModel.getLIKE_COUNT());

                        if (objShoutDefaultListModel.getLIKE_COUNT().equals("0")) {
                            objImageLike.setBackgroundResource(R.drawable.like_grey);
                            objTextViewLike.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_un_select));
                            objTextViewLike.setText("LIKES");
                        } else {
                            objImageLike.setBackgroundResource(R.drawable.like_red);
                            objTextViewLike.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_selected));
                            objTextViewLike.setText(objShoutDefaultListModel.getLIKE_COUNT() + " LIKES");
                        }

                        objImageLike.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (objShoutDefaultListModel.getIS_SHOUT_LIKED() == 0) {
                                    if (ConnectivityBroadcastReceiver.isConnected()) {
                                        objShoutDefaultListModel.setLIKE_COUNT("1");
                                        imageLike.setBackgroundResource(R.drawable.like_red);
                                        objImageLike.setBackgroundResource(R.drawable.like_red);
                                        objTextViewLike.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_selected));
                                        txtLike.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_selected));
                                        notifyDataSetChanged();
                                        txtLike.setText(objShoutDefaultListModel.getLIKE_COUNT() + " Likes");
                                        objTextViewLike.setText(objShoutDefaultListModel.getLIKE_COUNT() + " Likes");
                                        new LikeAPI().execute();
                                        objPopupWindowLargeSource.dismiss();
                                    } else {
                                        Constants.showInternetToast(objActivity);
                                    }
                                } else {
                                    System.out.println("YOU HAVE ALREADY LIKED TO THIS SHOUT.");
                                }
                            }
                        });

                        objTextViewLike.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (objShoutDefaultListModel.getIS_SHOUT_LIKED() == 0) {
                                    if (ConnectivityBroadcastReceiver.isConnected()) {
                                        objShoutDefaultListModel.setLIKE_COUNT("1");
                                        imageLike.setBackgroundResource(R.drawable.like_red);
                                        objImageLike.setBackgroundResource(R.drawable.like_red);
                                        objTextViewLike.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_selected));
                                        txtLike.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_selected));
                                        notifyDataSetChanged();
                                        txtLike.setText(objShoutDefaultListModel.getLIKE_COUNT() + " Likes");
                                        objTextViewLike.setText(objShoutDefaultListModel.getLIKE_COUNT() + " Likes");
                                        new LikeAPI().execute();
                                        objPopupWindowLargeSource.dismiss();
                                    } else {
                                        Constants.showInternetToast(objActivity);
                                    }
                                } else {
                                    System.out.println("YOU HAVE ALREADY LIKED TO THIS SHOUT.");
                                }
                            }
                        });

                        /*customLikeView.findViewById(R.id.linear_like_top_bar).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (objShoutDefaultListModel.getIS_SHOUT_LIKED() == 0) {
                                    if (ConnectivityBroadcastReceiver.isConnected()) {
                                        objShoutDefaultListModel.setLIKE_COUNT("1");
                                        imageLike.setBackgroundResource(R.drawable.like_red);
                                        objImageLike.setBackgroundResource(R.drawable.like_red);
                                        objTextViewLike.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_selected));
                                        txtLike.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_selected));
                                        notifyDataSetChanged();
                                        txtLike.setText(objShoutDefaultListModel.getLIKE_COUNT() + " Likes");
                                        objTextViewLike.setText(objShoutDefaultListModel.getLIKE_COUNT() + " Likes");
                                        new LikeAPI().execute();
                                        objPopupWindowLargeSource.dismiss();
                                    } else {
                                        Constants.showInternetToast(objActivity);
                                    }
                                } else {
                                    System.out.println("YOU HAVE ALREADY LIKED TO THIS SHOUT.");
                                }
                            }
                        });*/
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void pushUserToDetailScreen() {
        if (ShoutDefaultActivity.keyBoardOpen) {
            InputMethodManager inputMethodManager = (InputMethodManager) objActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(objEditTextSearchView.getApplicationWindowToken(), InputMethodManager.RESULT_HIDDEN, 0);
//            ShoutDefaultActivity.openCloseSearchBar(false, objActivity);
        } else {
            Intent objIntent = new Intent(objActivity, ShoutDetailActivity.class);
            objIntent.putExtra("SHOUT_IMAGES", objShoutDefaultListModel.getStrShoutImages());
            objIntent.putExtra("COMMENT_COUNT", objShoutDefaultListModel.getCOMMENT_COUNT());
            objActivity.startActivity(objIntent);
            objActivity.overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);

            SharedPreferences objSharedPreferences = objActivity.getSharedPreferences(Constants.PROFILE_PREFERENCES, objActivity.MODE_PRIVATE);
            SharedPreferences.Editor objEditor = objSharedPreferences.edit();
            objEditor.putString(Constants.SHOUT_ID_FOR_DETAIL_SCREEN, objShoutDefaultListModel.getSHOUT_ID());
            objEditor.putString(Constants.IS_SHOUT_LIKED, String.valueOf(objShoutDefaultListModel.getIS_SHOUT_LIKED()));
            objEditor.putString(Constants.SHOUT_SINGLE_IMAGE_FOR_DETAIL, objShoutDefaultListModel.getSHOUT_IMAGE());
            objEditor.putString(Constants.USER_ID_FOR_DETAIL_SCREEN, String.valueOf(objShoutDefaultListModel.getUSER_ID()));
            objEditor.putString(Constants.SHOUT_TYPE_FOR_DETAIL_SCREEN, String.valueOf(objShoutDefaultListModel.getSHOUT_TYPE()));
            objEditor.putString(Constants.USER_PROFILE_URL_FOR_DETAIL_SCREEN, String.valueOf(objShoutDefaultListModel.getPROFILE_IMAGE_URL()));
            objEditor.putString(Constants.SHOUT_LIKE_COUNT, String.valueOf(objShoutDefaultListModel.getLIKE_COUNT()));
            objEditor.putString(Constants.SHOUT_CREATED_DATE, String.valueOf(objShoutDefaultListModel.getDATE_TIME()));
            objEditor.putString(Constants.SHOUT_TITLE, String.valueOf(objShoutDefaultListModel.getTITLE()));
            objEditor.putString(Constants.SHOUT_DESCRIPTION, String.valueOf(objShoutDefaultListModel.getDESCRIPTION()));
            objEditor.putString(Constants.SHOUT_USER_NAME, String.valueOf(objShoutDefaultListModel.getUSER_NAME()));
            objEditor.putString(Constants.SHOUT_HIDDEN_STATUS, String.valueOf(objShoutDefaultListModel.getIS_SHOUT_HIDDEN()));


            objEditor.commit();
//            objActivity.finish();
        }
    }

    public class HideShoutApi extends AsyncTask<String, Void, String> {
        final ProgressDialog objProgressDialog;
        String strResult;
        String strIsHide = "";

        public HideShoutApi(String status) {
            strResult = "";
            objProgressDialog = new ProgressDialog(objActivity);
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
                objJsonObject.put("shout_id", objShoutDefaultListModel.getSHOUT_ID());
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
                    objShoutDefaultListModel.setIS_SHOUT_HIDDEN(Integer.parseInt(strIsHide));
                    notifyDataSetChanged();
                    DatabaseHelper objDatabaseHelper = new DatabaseHelper(objActivity);
                    ShoutDefaultListModel objDatabaseModelObject = objDatabaseHelper.getShoutDetails(objShoutDefaultListModel.getSHOUT_ID());
                    if (strIsHide.equals("0")) {
                        objDatabaseModelObject.setIS_SHOUT_HIDDEN(0);
                        objDatabaseModelObject.setIS_HIDE("0");
                        objDatabaseHelper.updateShout(objDatabaseModelObject, objShoutDefaultListModel.getSHOUT_ID());
                        objImageViewHideShowIcon.setImageResource(R.drawable.shout_hide_white);
                        objTextViewHideShowActionLable.setText("HIDE");
                    } else {
                        objDatabaseModelObject.setIS_SHOUT_HIDDEN(1);
                        objDatabaseModelObject.setIS_HIDE("1");
                        objDatabaseHelper.updateShout(objDatabaseModelObject, objShoutDefaultListModel.getSHOUT_ID());
                        objImageViewHideShowIcon.setImageResource(R.drawable.shout_show_white);
                        objTextViewHideShowActionLable.setText("SHOW");
                    }
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class ShoutPassApi extends AsyncTask<String, Void, String> {

        String strResult = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                SharedPreferences objSharedPreferences = objActivity.getSharedPreferences(Constants.PROFILE_PREFERENCES, objActivity.MODE_PRIVATE);

                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("shout_id", objShoutDefaultListModel.getSHOUT_ID());
                objJsonObject.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));

                strResult = NetworkUtils.postData(Constants.SHOUT_PASS_API, objJsonObject.toString());

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
                JSONObject objJsonObject = new JSONObject(s);
                if (objJsonObject.getString("result").equals("true")) {
                    /*DatabaseHelper objDatabaseHelper = new DatabaseHelper(objActivity);
                    System.out.println("AFTER PASS SHOUT ID :" + objShoutDefaultListModel.getSHOUT_ID());
                    objDatabaseHelper.deleteShoutById(objShoutDefaultListModel.getSHOUT_ID());*/
                    arrShoutDefaultListModel.remove(intPosition);
                    ShoutDefaultActivity.objShoutDefaultListAdapter.notifyDataSetChanged();
                    notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class LikeAPI extends AsyncTask<String, Void, String> {

        String strResult = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                SharedPreferences objSharedPreferences = objActivity.getSharedPreferences(Constants.PROFILE_PREFERENCES, objContext.MODE_PRIVATE);
                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("shout_id", objShoutDefaultListModel.getSHOUT_ID());
                objJsonObject.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));
                new DatabaseHelper(objActivity).updateLikeCount(Integer.parseInt(objShoutDefaultListModel.getLIKE_COUNT()) + 1, objShoutDefaultListModel.getSHOUT_ID());
                strResult = NetworkUtils.postData(Constants.LIKE_SHOUT_API, objJsonObject.toString());
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
                JSONObject objJsonObject = new JSONObject(s);
                if (objJsonObject.getString("result").equals("true")) {
                    /*objShoutDefaultListModel.setLIKE_COUNT("1");
                    imageLike.setBackgroundResource(R.drawable.like_red);
                    txtLike.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_selected));
                    notifyDataSetChanged();
                    txtLike.setText(objShoutDefaultListModel.getLIKE_COUNT() + " Likes");*/

                    // DO NOTHING

                } else {
                    objTextViewNoLike.setVisibility(TextView.VISIBLE);
                    objListViewShouters.setVisibility(ListView.GONE);
                    objShoutDefaultListModel.setLIKE_COUNT("0");
                    imageLike.setBackgroundResource(R.drawable.like_grey);
                    txtLike.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_un_select));
                    notifyDataSetChanged();
                    txtLike.setText("LIKES");
                    new DatabaseHelper(objActivity).updateLikeCount(Integer.parseInt(objShoutDefaultListModel.getLIKE_COUNT()) - 1, objShoutDefaultListModel.getSHOUT_ID());
                }
            } catch (Exception e) {
                objTextViewNoLike.setVisibility(TextView.VISIBLE);
                objListViewShouters.setVisibility(ListView.GONE);
                objShoutDefaultListModel.setLIKE_COUNT("0");
                imageLike.setBackgroundResource(R.drawable.like_grey);
                txtLike.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_un_select));
                notifyDataSetChanged();
                txtLike.setText("LIKES");
                new DatabaseHelper(objActivity).updateLikeCount(Integer.parseInt(objShoutDefaultListModel.getLIKE_COUNT()) - 1, objShoutDefaultListModel.getSHOUT_ID());
                e.printStackTrace();
            }
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
            LayoutInflater objLayoutInflater = (LayoutInflater) objActivity.getSystemService(objContext.LAYOUT_INFLATER_SERVICE);
            objLikedShoutersAdapter = new LikedShoutersAdapter(arrLikedShoutersModel, objActivity, objActivity, objLayoutInflater);
            objListViewShouters.setAdapter(objLikedShoutersAdapter);
        }

        @Override
        protected String doInBackground(String... params) {
            String strResult = "";
            try {
                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("shout_id", objShoutDefaultListModel.getSHOUT_ID());
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
                        LayoutInflater objLayoutInflater = (LayoutInflater) objActivity.getSystemService(objContext.LAYOUT_INFLATER_SERVICE);
                        if (arrLikedShoutersModel.size() > 0) {
                            System.out.println("LIKE COUNT OLD : " + objShoutDefaultListModel.getLIKE_COUNT());
                            objShoutDefaultListModel.setLIKE_COUNT(String.valueOf(arrLikedShoutersModel.size()));
                            notifyDataSetChanged();
                            System.out.println("LIKE COUNT UPDATE : " + objShoutDefaultListModel.getLIKE_COUNT());
                            DatabaseHelper objDatabaseHelper = new DatabaseHelper(objActivity);
                            objShoutDefaultListModel = objDatabaseHelper.updateShout(objShoutDefaultListModel, objShoutDefaultListModel.getSHOUT_ID());
                            notifyDataSetChanged();

                            if (objRefreshListListener != null)
                                objRefreshListListener.refreshList();

                            objTextViewNoLike.setVisibility(TextView.GONE);
                            objListViewShouters.setVisibility(ListView.VISIBLE);
                            objLikedShoutersAdapter = new LikedShoutersAdapter(arrLikedShoutersModel, objActivity, objActivity, objLayoutInflater);
                            objListViewShouters.setAdapter(objLikedShoutersAdapter);
                            objLikedShoutersAdapter.notifyDataSetChanged();

                            if (objShoutDefaultListModel.getLIKE_COUNT().equals("0")) {
                                objImageLike.setBackgroundResource(R.drawable.like_grey);
                                objTextViewLike.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_un_select));
                                objTextViewLike.setText("LIKES");
                            } else {
                                objImageLike.setBackgroundResource(R.drawable.like_red);
                                objTextViewLike.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_selected));
                                objTextViewLike.setText(objShoutDefaultListModel.getLIKE_COUNT() + " LIKES");
                            }
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

    public class ReshoutApi extends AsyncTask<String, Void, String> {
        String strResult;
        String strShoutId = "";
        DatabaseHelper objDatabaseHelper;

        public ReshoutApi(String shout_id) {
            strResult = "";
            strShoutId = shout_id;
            objDatabaseHelper = new DatabaseHelper(objActivity);
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
                    ShoutDefaultListModel objShoutDefaultListModel = objDatabaseHelper.getShoutDetails(strShoutId);
                    objShoutDefaultListModel.setRE_SHOUT(objJsonObject.getString("reshout_count"));
                    objDatabaseHelper.updateShout(objShoutDefaultListModel, strShoutId);
                    if (objJsonObject.getString("reshout_count").equals("0")) {
                        objRelativeLayoutReshout.setBackgroundResource(R.drawable.rounded_corner_shout_default_grey);
                    } else {
                        objRelativeLayoutReshout.setBackgroundResource(R.drawable.rounded_corner_shout_default_red);
                    }
                    objTextViewReshoutCount.setText(objJsonObject.getString("reshout_count"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setCallback(RefreshListListener objRefreshListListener) {
        this.objRefreshListListener = objRefreshListListener;
    }


    public interface RefreshListListener {
        public void refreshList();
    }
}
