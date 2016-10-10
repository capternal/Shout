package com.shout.shoutapplication.login.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.login.model.CountryListModel;

import java.util.ArrayList;

/**
 * Created by CapternalSystems on 4/3/2016.
 */
public class CountryListAdapter extends BaseAdapter {

    ArrayList<CountryListModel> arrCountryListModel = new ArrayList<CountryListModel>();
    Context objContext;

    public CountryListAdapter(ArrayList<CountryListModel> arrCountryListModel, Context objContext) {
        this.arrCountryListModel = arrCountryListModel;
        this.objContext = objContext;
    }

    @Override
    public int getCount() {
        return arrCountryListModel.size();
    }

    @Override
    public Object getItem(int position) {
        return arrCountryListModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        ImageView imgCountryImage;
        TextView objTextViewCountryName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder objViewHolder;
        View objView = convertView;

        if (convertView == null) {
            LayoutInflater objLayoutInflater = (LayoutInflater) objContext.getSystemService(objContext.LAYOUT_INFLATER_SERVICE);
            objView = objLayoutInflater.inflate(R.layout.country_list_layout, parent, false);

            objViewHolder = new ViewHolder();

            objViewHolder.imgCountryImage = (ImageView) objView.findViewById(R.id.image_country_list_layout);
            objViewHolder.objTextViewCountryName = (TextView) objView.findViewById(R.id.txt_country_name_country_list_layout);

            objView.setTag(objViewHolder);
        } else {
            objViewHolder = (ViewHolder) objView.getTag();
        }

        CountryListModel objCountryListModel = arrCountryListModel.get(position);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                objViewHolder.imgCountryImage.setBackground(getDrawable(objCountryListModel.getIso().toLowerCase().toString()));
            }
            objViewHolder.objTextViewCountryName.setText(objCountryListModel.getNicename());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objView;
    }

    public Drawable getDrawable(String name) {
        int resourceId = objContext.getResources().getIdentifier(name, "drawable", objContext.getPackageName());
        return objContext.getResources().getDrawable(resourceId);
    }
}

