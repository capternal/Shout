package com.shout.shoutapplication.main.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.shout.shoutapplication.CustomClasses.CustomPager;
import com.shout.shoutapplication.R;
import com.shout.shoutapplication.main.Model.ShoutDefaultListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CapternalSystems on 5/27/2016.
 */
public class ShoutDefaultListAdapter extends BaseAdapter implements Filterable, ShoutDefaultViewPagerAdapter.RefreshListListener {

    ArrayList<ShoutDefaultListModel> originalShoutDefaultModel = null;
    ArrayList<ShoutDefaultListModel> filteredShoutDefaultModel = null;
    Context objContext;
    Activity objActivity;
    ViewHolder objViewHolder;
    private ItemFilter mFilter = new ItemFilter();
    SwipeRefreshLayout objSwipeRefreshLayout;

    public ShoutDefaultListAdapter(ArrayList<ShoutDefaultListModel> arrShoutDefaultListModel, Context objContext, Activity objActivity) {
        this.originalShoutDefaultModel = arrShoutDefaultListModel;
        this.filteredShoutDefaultModel = arrShoutDefaultListModel;
        this.objContext = objContext;
        this.objActivity = objActivity;
        objSwipeRefreshLayout = (SwipeRefreshLayout) objActivity.findViewById(R.id.shout_default_swipe_layout);
    }

    @Override
    public int getCount() {
        return originalShoutDefaultModel.size();
    }

    @Override
    public Object getItem(int position) {
        return originalShoutDefaultModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    @Override
    public void refreshList() {
        notifyDataSetChanged();
    }

    class ViewHolder {
        CustomPager objViewPager;
    }

    @Override
    public View getView(final int intPosition, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater objLayoutInflater = (LayoutInflater) objContext.getSystemService(objContext.LAYOUT_INFLATER_SERVICE);
            convertView = objLayoutInflater.inflate(R.layout.shout_default_front_layout, parent, false);
            objViewHolder = new ViewHolder();
            objViewHolder.objViewPager = (CustomPager) convertView.findViewById(R.id.view_pager_shout_default_list_layout);
            convertView.setId(intPosition);
            convertView.setTag(objViewHolder);
        } else {
            objViewHolder = (ViewHolder) convertView.getTag();
        }
        ShoutDefaultViewPagerAdapter adapter = new ShoutDefaultViewPagerAdapter(objContext, objActivity, originalShoutDefaultModel, intPosition);
        adapter.setCallback(this);
//        objViewHolder.objViewPager.setId(intPosition + getCount());
        ShoutDefaultListModel objShoutDefaultListModel = (ShoutDefaultListModel) originalShoutDefaultModel.get(intPosition);
        objViewHolder.objViewPager.setAdapter(adapter);
        objViewHolder.objViewPager.setMinimumWidth(100);
        objViewHolder.objViewPager.setTag(intPosition);
        objViewHolder.objViewPager.setCurrentItem(objShoutDefaultListModel.getDEFAULT_PAGE_POSITION());

        objViewHolder.objViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                objSwipeRefreshLayout.setEnabled(false);
            }
        });
        return convertView;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<ShoutDefaultListModel> list = filteredShoutDefaultModel;

            int count = list.size();
            final ArrayList<ShoutDefaultListModel> nlist = new ArrayList<ShoutDefaultListModel>();

            String filterableTitle;
            String filterableStringUserName;
            String filterableStringDesc;

            for (int i = 0; i < count; i++) {
                filterableTitle = list.get(i).getTITLE();
                filterableStringUserName = list.get(i).getUSER_NAME();
                filterableStringDesc = list.get(i).getDESCRIPTION();
                if (filterableTitle.toLowerCase().contains(filterString) || filterableStringDesc.toLowerCase().contains(filterString) || filterableStringUserName.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            originalShoutDefaultModel = (ArrayList<ShoutDefaultListModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
