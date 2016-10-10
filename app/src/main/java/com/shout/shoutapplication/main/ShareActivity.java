package com.shout.shoutapplication.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.base.BaseActivity;

public class ShareActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        hideBottomTabs();
        hideDefaultTopHeader();

        initialize();

    }

    private void initialize() {
        BaseActivity.objTextViewDrawerCustomBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(ShareActivity.this, ShoutDetailActivity.class);
                startActivity(objIntent);
                finish();
                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
            }
        });


        BaseActivity.objTextViewDrawerCustomDownArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.objSlidingDrawer.animateOpen();
            }
        });
    }
}
