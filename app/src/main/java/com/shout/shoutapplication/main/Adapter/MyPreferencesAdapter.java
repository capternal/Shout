package com.shout.shoutapplication.main.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.main.Model.MyPreferencesModel;

import java.util.ArrayList;

/**
 * Created by CapternalSystems on 7/19/2016.
 */
public class MyPreferencesAdapter extends BaseAdapter {

    ArrayList<MyPreferencesModel> arrMyPreferencesModel = new ArrayList<MyPreferencesModel>();
    Activity objActivity;

    public MyPreferencesAdapter(ArrayList<MyPreferencesModel> arrMyPreferencesModel, Activity objActivity) {
        this.arrMyPreferencesModel = arrMyPreferencesModel;
        this.objActivity = objActivity;
    }

    @Override
    public int getCount() {
        return arrMyPreferencesModel.size();
    }

    @Override
    public Object getItem(int position) {
        return arrMyPreferencesModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyPreferencesModel objMyPreferencesModel = arrMyPreferencesModel.get(position);
        if (convertView == null) {
            LayoutInflater objLayoutInflater = (LayoutInflater) objActivity.getSystemService(objActivity.LAYOUT_INFLATER_SERVICE);
            convertView = objLayoutInflater.inflate(R.layout.preferences_list_layout, parent, false);
        }
        convertView.setTag(objMyPreferencesModel);

        TextView objTextViewTitle = (TextView) convertView.findViewById(R.id.txt_title_my_preferences_layout);
        CheckBox objCheckBox = (CheckBox) convertView.findViewById(R.id.check_box_my_preferences_layout);


        objTextViewTitle.setText(objMyPreferencesModel.getTitle());

        objCheckBox.setChecked(objMyPreferencesModel.isCheckBoxChecked());

        objCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox objCheck = (CheckBox) v;
                MyPreferencesModel objMyPreferencesModel = (MyPreferencesModel) objCheck.getTag();
                objMyPreferencesModel.setCheckBoxChecked(objCheck.isChecked());
                if (objMyPreferencesModel.isCheckBoxChecked()) {
                    objMyPreferencesModel.setStatus("A");
                } else {
                    objMyPreferencesModel.setStatus("I");
                }
                notifyDataSetChanged();
            }
        });
        objCheckBox.setTag(objMyPreferencesModel);
        return convertView;
    }
}
