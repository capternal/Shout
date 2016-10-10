package com.shout.shoutapplication.main.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.main.Model.MyPreferencesModel;

import java.util.ArrayList;


public class PopupComunityLayoutAdapter extends BaseAdapter {

    private Context context;
    private Activity objActivity;
    private ArrayList<MyPreferencesModel> arrMyPreferencesModel = new ArrayList<MyPreferencesModel>();
    private String title;

    public PopupComunityLayoutAdapter(Context context, Activity objActivity, ArrayList<MyPreferencesModel> arrMyPreferencesModel,String id) {
        this.context = context;
        this.objActivity = objActivity;
        this.arrMyPreferencesModel = arrMyPreferencesModel;
        this.title =id;
    }

    @Override
    public int getCount() {
        return arrMyPreferencesModel.size();
    }

    @Override
    public MyPreferencesModel getItem(int position) {
        return arrMyPreferencesModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_comunity_listview, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((MyPreferencesModel) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(MyPreferencesModel objMyPreferencesModel, ViewHolder holder) {
        //TODO implement
        holder.textViewComunityName.setText(objMyPreferencesModel.getTitle());
        holder.textViewComunityName.setBackgroundColor(Color.WHITE);
        holder.textViewComunityName.setTextColor(Color.BLACK);
        System.out.println("ID : " + title);
        try{
            if(title.length()>0){
                if(title.equals(objMyPreferencesModel.getTitle())){
                    holder.textViewComunityName.setBackgroundResource(R.drawable.community_popup_selected_text_background);
                    holder.textViewComunityName.setTextColor(Color.WHITE);
                }
            }else{
                holder.textViewComunityName.setBackgroundColor(Color.WHITE);
                holder.textViewComunityName.setTextColor(Color.BLACK);
            }
        }catch(NullPointerException ne){
            ne.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    protected class ViewHolder {
        private TextView textViewComunityName;
        public ViewHolder(View view) {
            textViewComunityName = (TextView) view.findViewById(R.id.textViewComunityName);
        }
    }
}
