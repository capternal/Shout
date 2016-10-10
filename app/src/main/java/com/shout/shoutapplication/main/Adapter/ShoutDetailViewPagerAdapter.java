package com.shout.shoutapplication.main.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.shout.shoutapplication.R;
import com.shout.shoutapplication.app.AppController;
import com.shout.shoutapplication.main.Model.ShoutDetailViewPagerModel;
import com.shout.shoutapplication.main.VideoDemoActivity;

import java.util.ArrayList;

/**
 * Created by CapternalSystems on 6/21/2016.
 */
public class ShoutDetailViewPagerAdapter extends PagerAdapter {

    private Activity objActivity;
    ArrayList<ShoutDetailViewPagerModel> arrShoutDetailViewPagerModel = new ArrayList<ShoutDetailViewPagerModel>();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private Matrix matrix;
    NetworkImageView image;
    ImageView imagePlayVideo;

    public ShoutDetailViewPagerAdapter(Activity objActivity, ArrayList<ShoutDetailViewPagerModel> arrShoutDetailViewPagerModel) {
        this.objActivity = objActivity;
        this.arrShoutDetailViewPagerModel = arrShoutDetailViewPagerModel;
        matrix = new Matrix();
    }

    @Override
    public int getCount() {
        return arrShoutDetailViewPagerModel.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater mLayoutInflater = (LayoutInflater) objActivity.getSystemService(objActivity.LAYOUT_INFLATER_SERVICE);
        View itemView = mLayoutInflater.inflate(R.layout.shout_detail_viewpager_cell, container, false);
        image = (NetworkImageView) itemView.findViewById(R.id.image_shout_detail_viewpager_cell);
        imagePlayVideo = (ImageView) itemView.findViewById(R.id.image_play_video);

        final ShoutDetailViewPagerModel objShoutDetailViewPagerModel = arrShoutDetailViewPagerModel.get(position);
        System.out.println("TEMP IMAGE URL :  " + objShoutDetailViewPagerModel.getIMAGE_URL());
        if (objShoutDetailViewPagerModel.getTYPE().equals("V")) {
            image.setImageUrl(objShoutDetailViewPagerModel.getIMAGE_URL(), imageLoader);
            imagePlayVideo.setVisibility(ImageView.VISIBLE);
        } else {
            imagePlayVideo.setVisibility(ImageView.GONE);
            image.setImageUrl(objShoutDetailViewPagerModel.getIMAGE_URL(), imageLoader);
        }
        imagePlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CLICKED VIDEO URL : " + objShoutDetailViewPagerModel.getVIDEO_URL());
                System.out.println("CLICKED VIDEO LOCAL PATH : " + objShoutDetailViewPagerModel.getVIDEO_LOCAL_PATH());
                Intent objIntent = new Intent(objActivity, VideoDemoActivity.class);
                objIntent.putExtra("VIDEO_URL",objShoutDetailViewPagerModel.getVIDEO_URL());
                objIntent.putExtra("VIDEO_LOCAL_PATH",objShoutDetailViewPagerModel.getVIDEO_LOCAL_PATH());
                objActivity.startActivity(objIntent);
                objActivity.overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
            }
        });
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}
