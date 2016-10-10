package com.shout.shoutapplication.main;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.shout.shoutapplication.R;

import java.io.IOException;

public class VideoDemoActivity extends AppCompatActivity {

    FullscreenVideoLayout objFullscreenVideoLayout;
    String VideoURL = "";
    String VideoLocalPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_demo);

        objFullscreenVideoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);

        VideoURL = getIntent().getExtras().getString("VIDEO_URL");
        VideoLocalPath = getIntent().getExtras().getString("VIDEO_LOCAL_PATH");

        System.out.println("VIDEO URL HERE : " + VideoURL);
        System.out.println("VIDEO LOCAL PATH HERE : " + VideoLocalPath);

        objFullscreenVideoLayout.setVisibility(FullscreenVideoLayout.VISIBLE);
        objFullscreenVideoLayout.setActivity(this);

        Uri videoUri = null;

        if (VideoLocalPath.isEmpty()) {
            videoUri = Uri.parse(VideoURL);
            try {
                objFullscreenVideoLayout.setVideoURI(videoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            videoUri=Uri.parse(VideoLocalPath);
            try {
                objFullscreenVideoLayout.setVideoURI(videoUri);
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    objFullscreenVideoLayout.setVideoURI(Uri.parse(VideoURL));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
