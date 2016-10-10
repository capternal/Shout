package com.shout.shoutapplication.main.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.ConnectivityBroadcastReceiver;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.main.Model.LikedShoutersModel;
import com.shout.shoutapplication.others.CircleTransform;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LikedShoutersAdapter extends BaseAdapter {

    private ArrayList<LikedShoutersModel> objects = new ArrayList<LikedShoutersModel>();
    private Context context;
    private Activity objActivity;
    private LayoutInflater layoutInflater;

    public LikedShoutersAdapter(ArrayList<LikedShoutersModel> objects, Context context, Activity objActivity, LayoutInflater layoutInflater) {
        this.objects = objects;
        this.context = context;
        this.objActivity = objActivity;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public LikedShoutersModel getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.like_popup_cell, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((LikedShoutersModel) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(LikedShoutersModel object, ViewHolder holder) {
        //TODO implement
        if (object.getUserProfile().equals("")) {
            Picasso.with(objActivity).load(R.drawable.dummy_image).transform(new CircleTransform()).into(holder.imgLikeUserProfile);

            setBlinkingAnimation(holder.imgLikeUserProfile);
        } else {
            if (ConnectivityBroadcastReceiver.isConnected()) {
                Picasso.with(objActivity).load(object.getUserProfile()).transform(new CircleTransform()).into(holder.imgLikeUserProfile);
            } else {
                Picasso.with(objActivity).load(object.getUserProfile()).networkPolicy(NetworkPolicy.OFFLINE).transform(new CircleTransform()).into(holder.imgLikeUserProfile);
            }
        }

        holder.imgLikeUserProfile.setPadding(Constants.DEFAULT_MESSAGE_BOARD_PADDING,
                Constants.DEFAULT_MESSAGE_BOARD_PADDING,
                Constants.DEFAULT_MESSAGE_BOARD_PADDING,
                Constants.DEFAULT_MESSAGE_BOARD_PADDING);

        if (object.getUserName().equals("")) {
            holder.txtLikeUserName.setBackgroundResource(R.drawable.rounded_corner_shout_detail_light_pink);
            holder.txtLikeUserName.setText("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
            setBlinkingAnimation(holder.txtLikeUserName);
        } else {
            holder.txtLikeUserName.setText(object.getUserName());
        }

        if (object.getUserLikeTime().equals("")) {
            holder.txtLikeTime.setBackgroundResource(R.drawable.rounded_corner_shout_detail_light_grey);
            holder.txtLikeTime.setText("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
            setBlinkingAnimation(holder.txtLikeTime);
        } else {
            holder.txtLikeTime.setText(object.getUserLikeTime());
        }
    }

    private void setBlinkingAnimation(View view) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(600);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        view.startAnimation(anim);
    }

    protected class ViewHolder {
        private RelativeLayout relativeLikeItemImage;
        private ImageView imgLikeUserProfile;
        private TextView txtLikeUserName;
        private TextView txtLikeTime;

        public ViewHolder(View view) {
            relativeLikeItemImage = (RelativeLayout) view.findViewById(R.id.relative_like_item_image);
            imgLikeUserProfile = (ImageView) view.findViewById(R.id.img_like_user_profile);
            txtLikeUserName = (TextView) view.findViewById(R.id.txt_like_user_name);
            txtLikeTime = (TextView) view.findViewById(R.id.txt_like_time);
        }
    }
}
