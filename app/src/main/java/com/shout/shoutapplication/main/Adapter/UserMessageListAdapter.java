package com.shout.shoutapplication.main.Adapter;

import android.content.Context;
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
import com.shout.shoutapplication.main.Model.UserMessageListModel;
import com.shout.shoutapplication.others.CircleTransform;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by CapternalSystems on 5/23/2016.
 */
public class UserMessageListAdapter extends BaseAdapter {


    ArrayList<UserMessageListModel> arrUserMessageListModels = new ArrayList<UserMessageListModel>();
    Context objContext;

    public UserMessageListAdapter(ArrayList<UserMessageListModel> arrUserMessageListModels, Context objContext) {
        this.arrUserMessageListModels = arrUserMessageListModels;
        this.objContext = objContext;
    }

    @Override
    public int getCount() {
        return arrUserMessageListModels.size();
    }

    @Override
    public Object getItem(int position) {
        return arrUserMessageListModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        ImageView objItemImage;
        TextView objTextViewItemTitle;
        TextView objTextViewGroupMembers;
        TextView objTextViewMessageCount;
        ImageView objIMageChatIcon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View objView = convertView;
        final ViewHolder objViewHolder;

        if (convertView == null) {
            LayoutInflater objLayoutInflater = (LayoutInflater) objContext.getSystemService(objContext.LAYOUT_INFLATER_SERVICE);
            objView = objLayoutInflater.inflate(R.layout.messageboard_list_layout, parent, false);

            objViewHolder = new ViewHolder();

            objViewHolder.objItemImage = (ImageView) objView.findViewById(R.id.message_board_item_image);
            objViewHolder.objTextViewItemTitle = (TextView) objView.findViewById(R.id.txt_message_board_title);
            objViewHolder.objTextViewGroupMembers = (TextView) objView.findViewById(R.id.txt_message_board_group_member);
            objViewHolder.objTextViewMessageCount = (TextView) objView.findViewById(R.id.txt_message_board_count);
            objViewHolder.objIMageChatIcon = (ImageView) objView.findViewById(R.id.image_message_chat);
            objView.setTag(objViewHolder);
        } else {
            objViewHolder = (ViewHolder) objView.getTag();
        }

        UserMessageListModel objUserMessageListModel = arrUserMessageListModels.get(position);

        if (objUserMessageListModel.getItemImage().equals("")) {
            Picasso.with(objContext).load(R.drawable.dummy_image).transform(new CircleTransform()).into(objViewHolder.objItemImage);
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(600);
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            objViewHolder.objItemImage.startAnimation(anim);
            setCustomAnimation(objViewHolder.objItemImage);

        } else {
            if (ConnectivityBroadcastReceiver.isConnected()) {
                Picasso.with(objContext).load(objUserMessageListModel.getItemImage()).transform(new CircleTransform()).into(objViewHolder.objItemImage);
            } else {
                Picasso.with(objContext).load(objUserMessageListModel.getItemImage()).transform(new CircleTransform()).networkPolicy(NetworkPolicy.OFFLINE).into(objViewHolder.objItemImage);
            }
        }

        objViewHolder.objItemImage.setPadding(
                Constants.DEFAULT_MESSAGE_BOARD_PADDING,
                Constants.DEFAULT_MESSAGE_BOARD_PADDING,
                Constants.DEFAULT_MESSAGE_BOARD_PADDING,
                Constants.DEFAULT_MESSAGE_BOARD_PADDING);

        if (objUserMessageListModel.getMessageTitle().equals("")) {
            objViewHolder.objTextViewItemTitle.setText("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
            objViewHolder.objTextViewItemTitle.setBackgroundResource(R.drawable.rounded_corner_shout_detail_light_pink);
            setCustomAnimation(objViewHolder.objTextViewItemTitle);
        } else {
            objViewHolder.objTextViewItemTitle.setText(objUserMessageListModel.getMessageTitle());
        }

        if (objUserMessageListModel.getShoutLastSeenTime().equals("")) {
            objViewHolder.objTextViewGroupMembers.setText("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
            objViewHolder.objTextViewGroupMembers.setBackgroundResource(R.drawable.rounded_corner_shout_detail_light_grey);
            setCustomAnimation(objViewHolder.objTextViewGroupMembers);
        } else {
            objViewHolder.objTextViewGroupMembers.setText(objUserMessageListModel.getShoutLastSeenTime());
        }

        if (objUserMessageListModel.getMessageCount().length() > 0) {

            if (Integer.parseInt(objUserMessageListModel.getMessageCount().toString()) > 0) {
                objViewHolder.objIMageChatIcon.setBackgroundResource(R.drawable.chat_red);
                objViewHolder.objTextViewMessageCount.setText(objUserMessageListModel.getMessageCount());
            }
        }
        return objView;
    }

    private void setCustomAnimation(View view) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(600);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        view.startAnimation(anim);
    }
}
