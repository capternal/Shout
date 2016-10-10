package com.shout.shoutapplication.main.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.ConnectivityBroadcastReceiver;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.login.ProfileScreenActivity;
import com.shout.shoutapplication.main.Model.ShoutCommentModel;
import com.shout.shoutapplication.others.CircleTransform;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by CapternalSystems on 5/26/2016.
 */
public class ShoutCommentsAdapter extends BaseAdapter {

    ArrayList<ShoutCommentModel> arrShoutDetailUserListModel = new ArrayList<ShoutCommentModel>();
    Activity objActivity;

    public ShoutCommentsAdapter(ArrayList<ShoutCommentModel> arrShoutDetailUserListModel, Activity objContext) {
        this.arrShoutDetailUserListModel = arrShoutDetailUserListModel;
        this.objActivity = objContext;
    }

    @Override
    public int getCount() {
        return arrShoutDetailUserListModel.size();
    }

    @Override
    public Object getItem(int position) {
        return arrShoutDetailUserListModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        ImageView objImage;
        TextView txtMessage;
        TextView txtTimeHour;
        TextView txtUserName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder objViewHolder;
        if (convertView == null) {
            LayoutInflater objLayoutInflater = (LayoutInflater) objActivity.getSystemService(objActivity.LAYOUT_INFLATER_SERVICE);
            convertView = objLayoutInflater.inflate(R.layout.shout_detail_user_list_layout, parent, false);
            objViewHolder = new ViewHolder();
            objViewHolder.objImage = (ImageView) convertView.findViewById(R.id.image_shout_detail_user_list_profile);
            objViewHolder.txtMessage = (TextView) convertView.findViewById(R.id.txt_shout_detail_user_list_message);
            objViewHolder.txtTimeHour = (TextView) convertView.findViewById(R.id.txt_shout_detail_user_list_time_in_hour);
            objViewHolder.txtUserName = (TextView) convertView.findViewById(R.id.txt_shout_detail_user_list_name);
            convertView.setTag(objViewHolder);
        } else {
            objViewHolder = (ViewHolder) convertView.getTag();
        }
        final ShoutCommentModel objShoutDetailUserListModel = arrShoutDetailUserListModel.get(position);

        objViewHolder.txtUserName.setText(objShoutDetailUserListModel.getUSER_NAME());

        if (ConnectivityBroadcastReceiver.isConnected()) {
            if (objShoutDetailUserListModel.getUSER_ID().equals("")) {
                Picasso.with(objActivity).load(R.drawable.dummy_image).transform(new CircleTransform()).into(objViewHolder.objImage);
            } else {
                Picasso.with(objActivity).load(objShoutDetailUserListModel.getIMAGE_URL()).transform(new CircleTransform()).into(objViewHolder.objImage);
            }
        } else {
            if (objShoutDetailUserListModel.getUSER_ID().equals("")) {
                Picasso.with(objActivity).load(R.drawable.dummy_image).transform(new CircleTransform()).into(objViewHolder.objImage);
            } else {
                Picasso.with(objActivity).load(objShoutDetailUserListModel.getIMAGE_URL()).networkPolicy(NetworkPolicy.OFFLINE).transform(new CircleTransform()).into(objViewHolder.objImage);
            }
        }

        objViewHolder.objImage.setPadding(Constants.DEFAULT_CIRCLE_PADDING,Constants.DEFAULT_CIRCLE_PADDING,Constants.DEFAULT_CIRCLE_PADDING,Constants.DEFAULT_CIRCLE_PADDING);

        if (objShoutDetailUserListModel.getCOMMENT_MESSAGE().equals("")) {
            objViewHolder.txtMessage.setText("");
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(600);
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            objViewHolder.txtMessage.startAnimation(anim);
        } else {
            objViewHolder.txtMessage.setText(objShoutDetailUserListModel.getCOMMENT_MESSAGE());
        }

        if (objShoutDetailUserListModel.getTIME_IN_HOUR().equals("")) {
            objViewHolder.txtTimeHour.setText("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
            objViewHolder.txtTimeHour.setBackgroundResource(R.drawable.rounded_corner_shout_detail_light_grey);
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(600);
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            objViewHolder.txtTimeHour.startAnimation(anim);
        } else {
            objViewHolder.txtTimeHour.setText(objShoutDetailUserListModel.getTIME_IN_HOUR());
        }

        objViewHolder.objImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityBroadcastReceiver.isConnected()) {
                    SharedPreferences objSharedPreferences = objActivity.getSharedPreferences(Constants.PROFILE_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor objProfileEditor = objSharedPreferences.edit();
                    objProfileEditor.putString(Constants.PROFILE_SCREEN_USER_ID, objShoutDetailUserListModel.getUSER_ID());
//                objProfileEditor.putString(Constants.PROFILE_BACK_SCREEN_NAME, Constants.SHOUT_DETAIL_SCREEN);
                    objProfileEditor.commit();

                    Intent objIntent = new Intent(objActivity, ProfileScreenActivity.class);
                    objActivity.startActivity(objIntent);
                    objActivity.overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                    objActivity.finish();
                } else {
                    Constants.showInternetToast(objActivity);
                }
            }
        });
        return convertView;
    }
}