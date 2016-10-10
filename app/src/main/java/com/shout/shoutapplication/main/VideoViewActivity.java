package com.shout.shoutapplication.main;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.proxy.HttpGetProxy;

import java.io.File;

public class VideoViewActivity extends AppCompatActivity {

    private final static String TAG = "testVideoPlayer";
    static private final int BUFFER_SIZE = 210 * 1024 * 1024;
    private VideoView mVideoView;
    private MediaController mediaController;
    private HttpGetProxy proxy;
    private long startTimeMills;
//    private String videoUrl = "http://master255.org/res/%D0%9A%D0%BB%D0%B8%D0%BF%D1%8B/S/SKRILLEX/Skrillex%20-%20Summit%20(feat.%20Ellie%20Goulding)%20%5BVideo%20by%20Pilerats%5D.mp4";
    private String videoUrl = "";
    private String id = null;
    private long waittime = 8000;//Wait buffer time

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        //Create a pre-loaded video file storage folder
        new File(getBufferDir()).mkdirs();

        // Initialization VideoView
        mediaController = new MediaController(this);
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mVideoView.setMediaController(mediaController);
        mVideoView.setOnPreparedListener(mOnPreparedListener);

        // Initialize proxy server
        proxy = new HttpGetProxy(getBufferDir(),// Pre-loaded video file storage path
                BUFFER_SIZE,// Pre-loaded video file max size
                10);// Pre-loaded files limit

        id = System.currentTimeMillis() + "";
        try {
            proxy.startDownload(id, videoUrl, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String proxyUrl = proxy.getLocalURL(id);
        mVideoView.setVideoPath(proxyUrl);
    }

    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mp) {
            mVideoView.start();
            long duration = System.currentTimeMillis() - startTimeMills;
            Log.e(TAG, "Wait buffer time:" + waittime + ",First buffer time:" + duration);
        }
    };


    static public String getBufferDir() {
        String bufferDir = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/ProxyBuffer/files";
        return bufferDir;
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
        System.exit(0);
    }
}
