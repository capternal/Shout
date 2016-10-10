package com.shout.shoutapplication.login.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shout.shoutapplication.CustomClasses.CustomFontTextView;
import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.ConnectivityBroadcastReceiver;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.main.Model.ShoutDefaultListModel;
import com.shout.shoutapplication.main.ShoutDetailActivity;
import com.shout.shoutapplication.others.BitmapBorderTransformation;
import com.shout.shoutapplication.others.CircleTransform;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by CapternalSystems on 9/13/2016.
 */
public class MyShoutsAdapter extends BaseAdapter {

    ArrayList<ShoutDefaultListModel> arrShoutDefaultListModel = new ArrayList<ShoutDefaultListModel>();
    Activity objActivity;
    Context objContext;

    public MyShoutsAdapter(ArrayList<ShoutDefaultListModel> arrShoutDefaultListModel, Activity objActivity, Context objContext) {
        this.arrShoutDefaultListModel = arrShoutDefaultListModel;
        this.objActivity = objActivity;
        this.objContext = objContext;
    }

    @Override
    public int getCount() {
        return arrShoutDefaultListModel.size();
    }

    @Override
    public Object getItem(int position) {
        return arrShoutDefaultListModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        CustomFontTextView txtMessage;
        CustomFontTextView txtTitle;
        TextView txtDate;
        ImageView objProfileImage;

        TextView txtLike;
        TextView txtShare;
        TextView txtComments;

        ImageView imageLike;
        ImageView imageShare;
        ImageView imageComments;

        TextView txtUserName;
        ImageView shoutImage;
        RelativeLayout objRelativeLayoutMessageBody;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder objViewHolder;
        try {
            final ShoutDefaultListModel objShoutDefaultListModel = arrShoutDefaultListModel.get(position);
            if (view == null) {
                LayoutInflater objLayoutInflater = (LayoutInflater) objActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (objShoutDefaultListModel.getSHOUT_TYPE().equals("L")) {
                    view = objLayoutInflater.inflate(R.layout.profile_shouts_left_side_cell, parent, false);
                } else {
                    view = objLayoutInflater.inflate(R.layout.profile_shouts_right_side_cell, parent, false);
                }
                objViewHolder = new ViewHolder();

                objViewHolder.txtMessage = (CustomFontTextView) view.findViewById(R.id.txt_shout_default_message);
                objViewHolder.txtTitle = (CustomFontTextView) view.findViewById(R.id.txt_shout_default_title);
                objViewHolder.txtDate = (TextView) view.findViewById(R.id.txt_shout_default_time_date);
                objViewHolder.objProfileImage = (ImageView) view.findViewById(R.id.profile_image_shout_default);
                objViewHolder.txtLike = (TextView) view.findViewById(R.id.txt_shout_default_like);
                objViewHolder.txtShare = (TextView) view.findViewById(R.id.txt_shout_default_share);
                objViewHolder.txtComments = (TextView) view.findViewById(R.id.txt_shout_default_comments);
                objViewHolder.imageLike = (ImageView) view.findViewById(R.id.image_shout_default_like);
                objViewHolder.imageShare = (ImageView) view.findViewById(R.id.image_shout_default_share);
                objViewHolder.imageComments = (ImageView) view.findViewById(R.id.image_shout_default_comments);
                objViewHolder.txtUserName = (TextView) view.findViewById(R.id.txt_user_name_shout_default);
                objViewHolder.shoutImage = (ImageView) view.findViewById(R.id.img_shout_image);
                objViewHolder.objRelativeLayoutMessageBody = (RelativeLayout) view.findViewById(R.id.relative_message);

                view.setTag(position);
                view.setTag(objViewHolder);
            } else {
                objViewHolder = (ViewHolder) view.getTag();
            }
            //SET DEFAULT COLOR
            objViewHolder.txtLike.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_un_select));
            objViewHolder.txtShare.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_un_select));
            objViewHolder.txtComments.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_un_select));
            // SET LISTENER
            objViewHolder.txtLike.setAllCaps(true);
            objViewHolder.txtShare.setAllCaps(true);
            objViewHolder.txtComments.setAllCaps(true);

            objViewHolder.objRelativeLayoutMessageBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("MY SHOUT : SHOUT ID : " + objShoutDefaultListModel.getSHOUT_ID());
                    pushToDetailScreen(objShoutDefaultListModel);
                }
            });

            objViewHolder.txtTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("MY SHOUT : SHOUT ID : " + objShoutDefaultListModel.getSHOUT_ID());
                    pushToDetailScreen(objShoutDefaultListModel);
                }
            });

            objViewHolder.txtMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("MY SHOUT : SHOUT ID : " + objShoutDefaultListModel.getSHOUT_ID());
                    pushToDetailScreen(objShoutDefaultListModel);
                }
            });

            objViewHolder.txtTitle.setText(objShoutDefaultListModel.getTITLE());
            objViewHolder.txtMessage.setText(objShoutDefaultListModel.getDESCRIPTION());
            objViewHolder.txtDate.setText(objShoutDefaultListModel.getDATE_TIME());
            objViewHolder.txtUserName.setText(objShoutDefaultListModel.getUSER_NAME());

            if (objShoutDefaultListModel.getPROFILE_IMAGE_URL().length() > 0) {
                if (ConnectivityBroadcastReceiver.isConnected()) {
                    Picasso.with(objActivity).load(objShoutDefaultListModel.getPROFILE_IMAGE_URL()).transform(new CircleTransform()).into(objViewHolder.objProfileImage);
                } else {
                    Picasso.with(objActivity).load(objShoutDefaultListModel.getPROFILE_IMAGE_URL()).transform(new CircleTransform()).networkPolicy(NetworkPolicy.OFFLINE).into(objViewHolder.objProfileImage);
                }
            }

            objViewHolder.objProfileImage.setPadding(Constants.DEFAULT_CIRCLE_PADDING,
                    Constants.DEFAULT_CIRCLE_PADDING,
                    Constants.DEFAULT_CIRCLE_PADDING,
                    Constants.DEFAULT_CIRCLE_PADDING);
/*            System.out.println("COMMENT COUNT : " + Integer.parseInt(objShoutDefaultListModel.getCOMMENT_COUNT()));
            if (Integer.parseInt(objShoutDefaultListModel.getCOMMENT_COUNT()) > 0) {
                objViewHolder.imageComments.setBackgroundResource(R.drawable.comments_red);
                objViewHolder.txtComments.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_selected));
                objViewHolder.txtComments.setText(objShoutDefaultListModel.getCOMMENT_COUNT() + " COMMENTS ");
            } else {
                objViewHolder.imageComments.setBackgroundResource(R.drawable.comments_grey);
                objViewHolder.txtComments.setTextColor(objActivity.getResources().getColor(R.color.text_color_shout_default_un_select));
                objViewHolder.txtComments.setText(objShoutDefaultListModel.getCOMMENT_COUNT() + " COMMENTS ");
            }*/
            System.out.println("PRASAD IMAGE URL : " + objShoutDefaultListModel.getSHOUT_IMAGE());
            if (objShoutDefaultListModel.getSHOUT_IMAGE().equals("")) {
                objViewHolder.shoutImage.setVisibility(ImageView.GONE);
            } else {
                Picasso.with(objActivity).setLoggingEnabled(true);
                if (ConnectivityBroadcastReceiver.isConnected()) {
                    Picasso.with(objActivity).load(objShoutDefaultListModel.getSHOUT_IMAGE()).noFade().transform(new BitmapBorderTransformation(0, 15, Color.RED)).into(objViewHolder.shoutImage);
                } else {
                    Picasso.with(objActivity).load(objShoutDefaultListModel.getSHOUT_IMAGE()).noFade().transform(new BitmapBorderTransformation(0, 15, Color.RED)).networkPolicy(NetworkPolicy.OFFLINE).into(objViewHolder.shoutImage);
                }
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void pushToDetailScreen(ShoutDefaultListModel objShoutModel) {
        Intent objIntent = new Intent(objActivity, ShoutDetailActivity.class);
        objIntent.putExtra("SHOUT_IMAGES", objShoutModel.getStrShoutImages());
        objIntent.putExtra("COMMENT_COUNT", objShoutModel.getCOMMENT_COUNT());
        objActivity.startActivity(objIntent);
        objActivity.overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);

        SharedPreferences objSharedPreferences = objActivity.getSharedPreferences(Constants.PROFILE_PREFERENCES, objActivity.MODE_PRIVATE);
        SharedPreferences.Editor objEditor = objSharedPreferences.edit();
        objEditor.putString(Constants.SHOUT_ID_FOR_DETAIL_SCREEN, objShoutModel.getSHOUT_ID());
        objEditor.putString(Constants.IS_SHOUT_LIKED, String.valueOf(objShoutModel.getIS_SHOUT_LIKED()));
        objEditor.putString(Constants.SHOUT_SINGLE_IMAGE_FOR_DETAIL, objShoutModel.getSHOUT_IMAGE());
        objEditor.putString(Constants.USER_ID_FOR_DETAIL_SCREEN, String.valueOf(objShoutModel.getUSER_ID()));
        objEditor.putString(Constants.SHOUT_TYPE_FOR_DETAIL_SCREEN, String.valueOf(objShoutModel.getSHOUT_TYPE()));
        objEditor.putString(Constants.USER_PROFILE_URL_FOR_DETAIL_SCREEN, String.valueOf(objShoutModel.getPROFILE_IMAGE_URL()));
        objEditor.putString(Constants.SHOUT_LIKE_COUNT, String.valueOf(objShoutModel.getLIKE_COUNT()));
        objEditor.putString(Constants.SHOUT_CREATED_DATE, String.valueOf(objShoutModel.getDATE_TIME()));
        objEditor.putString(Constants.SHOUT_TITLE, String.valueOf(objShoutModel.getTITLE()));
        objEditor.putString(Constants.SHOUT_DESCRIPTION, String.valueOf(objShoutModel.getDESCRIPTION()));
        objEditor.putString(Constants.SHOUT_USER_NAME, String.valueOf(objShoutModel.getUSER_NAME()));
        objEditor.putString(Constants.SHOUT_HIDDEN_STATUS, String.valueOf(objShoutModel.getIS_SHOUT_HIDDEN()));
        objEditor.commit();
    }
}
