package com.shout.shoutapplication.main.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.ConnectivityBroadcastReceiver;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.main.Model.NotificationListModel;
import com.shout.shoutapplication.others.CircleTransform;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by CapternalSystems on 9/26/2016.
 */
public class NotificationListAdapter extends BaseAdapter {

    ArrayList<NotificationListModel> arrNotificationListModel = new ArrayList<NotificationListModel>();
    Activity objActivity;

    public NotificationListAdapter(ArrayList<NotificationListModel> arrNotificationListModel, Activity objActivity) {
        this.arrNotificationListModel = arrNotificationListModel;
        this.objActivity = objActivity;
    }

    @Override
    public int getCount() {
        return arrNotificationListModel.size();
    }

    @Override
    public Object getItem(int position) {
        return arrNotificationListModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        ImageView objImageViewProfile;
        TextView objTextViewName;
        TextView objTextViewCreated;
        TextView objTextViewmessage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NotificationListModel objNotificationListModel = arrNotificationListModel.get(position);

        View objView = convertView;
        ViewHolder objViewHolder = null;

        if (objView == null) {
            LayoutInflater objLayoutInflater = (LayoutInflater) objActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            objView = objLayoutInflater.inflate(R.layout.notification_list_cell, parent, false);
            objViewHolder = new ViewHolder();
            objView.setTag(position);
            objView.setTag(objViewHolder);
        } else {
            objViewHolder = (ViewHolder) objView.getTag();
        }
        objViewHolder.objImageViewProfile = (ImageView) objView.findViewById(R.id.profile_image_notification);
        objViewHolder.objTextViewName = (TextView) objView.findViewById(R.id.txt_user_name_notification);
        objViewHolder.objTextViewmessage = (TextView) objView.findViewById(R.id.txt_message_notification);
        objViewHolder.objTextViewCreated = (TextView) objView.findViewById(R.id.txt_created_date_notification);

        objViewHolder.objTextViewName.setText(objNotificationListModel.getUsername());
        objViewHolder.objTextViewmessage.setText(objNotificationListModel.getMessage());
        objViewHolder.objTextViewCreated.setText(objNotificationListModel.getCreated());
        if (ConnectivityBroadcastReceiver.isConnected()) {
            if (objNotificationListModel.getPhoto().isEmpty()) {
                Picasso.with(objActivity).load(R.drawable.shout_placeholder).transform(new CircleTransform()).into(objViewHolder.objImageViewProfile);
            } else {
                Picasso.with(objActivity).load(objNotificationListModel.getPhoto()).transform(new CircleTransform()).into(objViewHolder.objImageViewProfile);
            }
        } else {
            if (objNotificationListModel.getPhoto().isEmpty()) {
                Picasso.with(objActivity).load(R.drawable.shout_placeholder).transform(new CircleTransform()).into(objViewHolder.objImageViewProfile);
            } else {
                Picasso.with(objActivity).load(objNotificationListModel.getPhoto()).transform(new CircleTransform()).networkPolicy(NetworkPolicy.OFFLINE).into(objViewHolder.objImageViewProfile);
            }
        }

        objViewHolder.objImageViewProfile.setPadding(Constants.DEFAULT_CIRCLE_PADDING,
                Constants.DEFAULT_CIRCLE_PADDING,
                Constants.DEFAULT_CIRCLE_PADDING,
                Constants.DEFAULT_CIRCLE_PADDING);
        return objView;
    }
}
