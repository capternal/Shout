package com.shout.shoutapplication.main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.main.Model.CategoryModel;

import java.util.ArrayList;

/**
 * Created by CapternalSystems on 5/21/2016.
 */
public class CategoryNewAdapter extends BaseAdapter {

    private Context context;
    ArrayList<CategoryModel> arrStrDummyDirectoryGridItemName;

    public CategoryNewAdapter(Context context, ArrayList<CategoryModel> arrStrDummyDirectoryGridItemName) {
        this.context = context;
        this.arrStrDummyDirectoryGridItemName = arrStrDummyDirectoryGridItemName;
    }

    @Override
    public int getCount() {
        return arrStrDummyDirectoryGridItemName.size();
    }

    @Override
    public Object getItem(int position) {
        return arrStrDummyDirectoryGridItemName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView textViewCategoryName;
        CheckBox checkBoxTick;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            Utils.d("ADAPTER", " IF Position :" + position);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sort_categories_listview_cell, null);
            holder.textViewCategoryName = (TextView) convertView.findViewById(R.id.textViewCategoryName);
            holder.checkBoxTick = (CheckBox) convertView.findViewById(R.id.chk_tick);

            final CategoryModel objCategoryModel = arrStrDummyDirectoryGridItemName.get(position);
            holder.checkBoxTick.setChecked(objCategoryModel.isSelected());
            convertView.setTag(holder);
        } else {
            Utils.d("ADAPTER", " ELSE Position :" + position);
            holder = (ViewHolder) convertView.getTag();
        }
        final CategoryModel objCategoryModel = arrStrDummyDirectoryGridItemName.get(position);

        holder.textViewCategoryName.setText(objCategoryModel.getTitle());

        if (objCategoryModel.getFlag() == 0) {
            holder.checkBoxTick.setChecked(false);
        } else {
            holder.checkBoxTick.setChecked(true);
        }

        holder.checkBoxTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                CategoryModel categoryModel = (CategoryModel) checkBox.getTag();
                categoryModel.setSelected(checkBox.isChecked());
                objCategoryModel.setSelected(checkBox.isChecked());
                if (checkBox.isChecked()) {
                    objCategoryModel.setFlag(1);
                } else {
                    objCategoryModel.setFlag(0);
                }


            }
        });
        holder.textViewCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objCategoryModel.isSelected()) {
                    objCategoryModel.setSelected(false);
                    objCategoryModel.setFlag(0);
                } else {
                    objCategoryModel.setSelected(true);
                    objCategoryModel.setFlag(1);
                }
                Utils.d("Debug", " data :" + position + " selected or not : " + objCategoryModel.isSelected());
                notifyDataSetChanged();
            }
        });
        holder.checkBoxTick.setChecked(objCategoryModel.isSelected());
        holder.checkBoxTick.setTag(objCategoryModel);
        return convertView;
    }
}
