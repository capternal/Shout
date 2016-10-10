package com.shout.shoutapplication.main.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.main.Model.MyPreferencesModel;
import com.shout.shoutapplication.main.ShoutDefaultActivity;

import java.util.ArrayList;

/**
 * Created by student on 15/09/16.
 */
public class SearchViewPagerAdapter extends PagerAdapter {
    private Context context;
    ArrayList<MyPreferencesModel> arrMyPreferencesModel;
    LayoutInflater layoutInflater;

    public SearchViewPagerAdapter(Context context, ArrayList<MyPreferencesModel> arrMyPreferencesModel) {
        this.context = context;
        this.arrMyPreferencesModel = arrMyPreferencesModel;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrMyPreferencesModel.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.seach_viewpager_layout, container, false);
        TextView textView = (TextView) viewGroup.findViewById(R.id.textViewSearchTitle);
        textView.setText(arrMyPreferencesModel.get(position).getTitle());
        container.addView(viewGroup);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("OPEN COMUNITY WHEEL POPUP ID : " + arrMyPreferencesModel.get(position).getTitle());
                ((ShoutDefaultActivity)context).showCommunityPopup(true,arrMyPreferencesModel.get(position).getTitle());
            }
        });
        return viewGroup;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return arrMyPreferencesModel.get(position).getTitle();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("DESTROY ITEM CALLED.");
        container.removeView((View) object);

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (LinearLayout) object;
    }
}
