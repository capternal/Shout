package com.shout.shoutapplication.main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.main.Model.ShoutHorizontalListModel;
import com.shout.shoutapplication.others.CircleTransform;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by CapternalSystems on 5/26/2016.
 */
public class ShoutHorizontalListAdapter extends BaseAdapter {

    ArrayList<ShoutHorizontalListModel> arrShoutHorizontalListModel = new ArrayList<ShoutHorizontalListModel>();
    Context objContext;

    public ShoutHorizontalListAdapter(ArrayList<ShoutHorizontalListModel> arrShoutHorizontalListModel, Context objContext) {
        this.arrShoutHorizontalListModel = arrShoutHorizontalListModel;
        this.objContext = objContext;
    }

    @Override
    public int getCount() {
        return arrShoutHorizontalListModel.size();
    }

    @Override
    public Object getItem(int position) {
        return arrShoutHorizontalListModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        ImageView objImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder objViewHolder;
        if (convertView == null) {
            LayoutInflater objLayoutInflater = (LayoutInflater) objContext.getSystemService(objContext.LAYOUT_INFLATER_SERVICE);
            convertView = objLayoutInflater.inflate(R.layout.shout_detail_horizontal_list_layout, null);
            objViewHolder = new ViewHolder();
            objViewHolder.objImage = (ImageView) convertView.findViewById(R.id.shout_detail_horizontal_image);
            convertView.setTag(objViewHolder);
        } else {
            objViewHolder = (ViewHolder) convertView.getTag();
        }

        ShoutHorizontalListModel objShoutHorizontalListModel = arrShoutHorizontalListModel.get(position);

        Picasso.with(objContext).load(objShoutHorizontalListModel.getPhoto()).memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE).transform(new CircleTransform()).into(objViewHolder.objImage);

        return convertView;
    }
}
