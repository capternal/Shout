package com.shout.shoutapplication.main.Adapter;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.main.Model.ShoutDefaultListModel;

import java.util.ArrayList;

/**
 * Created by CapternalSystems on 6/2/2016.
 */
public class CustomShoutDefaultRecyclerAdapter extends RecyclerView.Adapter<CustomShoutDefaultRecyclerAdapter.MyViewHolder> {


    ArrayList<ShoutDefaultListModel> arrShoutDefaultListModel = new ArrayList<ShoutDefaultListModel>();
    Activity objActivity;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ViewPager objViewPager;

        public MyViewHolder(View view) {
            super(view);
            objViewPager = (ViewPager) view.findViewById(R.id.view_pager_shout_default_list_layout);
        }
    }

    public CustomShoutDefaultRecyclerAdapter(ArrayList<ShoutDefaultListModel> arrShoutDefaultListModel, Activity objActivity) {
        this.arrShoutDefaultListModel = arrShoutDefaultListModel;
        this.objActivity = objActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shout_default_front_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        /*ShoutDefaultListModel objShoutDefaultListModel = originalShoutDefaultModel.get(position);
        ShoutDefaultViewPagerAdapter adapter = new ShoutDefaultViewPagerAdapter(objActivity, objActivity, objShoutDefaultListModel);
        holder.objViewPager.setId(position);
        holder.objViewPager.setAdapter(adapter);
        holder.objViewPager.setMinimumWidth(100);
        holder.objViewPager.setCurrentItem(1);*/

    }

    @Override
    public int getItemCount() {
        return arrShoutDefaultListModel.size();
    }


}
