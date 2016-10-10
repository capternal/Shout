package com.shout.shoutapplication.main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.main.Model.CategoryModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by CapternalSystems on 5/21/2016.
 */
public class CategoryAdapter extends BaseAdapter {

    private Context context;
    ArrayList<CategoryModel> arrStrDummyDirectoryGridItemName;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> arrStrDummyDirectoryGridItemName) {
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
        ImageView objCategoryImage;
        RelativeLayout objRelativeLayoutBackground;
        TextView objTextViewCategoryImageTitle;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.category_grid_cell_layout, null);
            holder.objCategoryImage = (ImageView) convertView.findViewById(R.id.image_category);
            holder.objTextViewCategoryImageTitle = (TextView) convertView.findViewById(R.id.txt_category_image_title);
            holder.objRelativeLayoutBackground = (RelativeLayout) convertView.findViewById(R.id.relative_category_background);
            holder.objCategoryImage.setTag(position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CategoryModel objCategoryModel = arrStrDummyDirectoryGridItemName.get(position);

        holder.objTextViewCategoryImageTitle.setText(objCategoryModel.getTitle());

        System.out.println("FLAG VALUE : " + objCategoryModel.getFlag());

        if (objCategoryModel.getFlag() == 0) {
            Picasso.with(context).load(objCategoryModel.getImage()).into(holder.objCategoryImage);
        } else {
            Picasso.with(context).load(objCategoryModel.getSelected_image()).into(holder.objCategoryImage);
        }

        holder.objCategoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objCategoryModel.getFlag() == 0) {
                    for (int index = 0; index < arrStrDummyDirectoryGridItemName.size(); index++) {
                        CategoryModel objCategoryModelTemp = arrStrDummyDirectoryGridItemName.get(index);
                        if (objCategoryModelTemp.getFlag() == 1) {
                            objCategoryModelTemp.setFlag(0);
                        }
                    }
                    notifyDataSetChanged();
                    objCategoryModel.setFlag(1);
                    notifyDataSetChanged();
                    Picasso.with(context).load(objCategoryModel.getSelected_image()).into(holder.objCategoryImage);
                } else {
                    objCategoryModel.setFlag(0);
                    notifyDataSetChanged();
                    Picasso.with(context).load(objCategoryModel.getImage()).into(holder.objCategoryImage);
                }
            }
        });

        return convertView;
    }
}
