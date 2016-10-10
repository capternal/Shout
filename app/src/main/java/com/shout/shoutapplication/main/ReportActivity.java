package com.shout.shoutapplication.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.CallWebService;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.base.BaseActivity;
import com.shout.shoutapplication.database.DatabaseHelper;

import org.json.JSONObject;

public class ReportActivity extends BaseActivity implements CallWebService.WebserviceResponse, View.OnClickListener {

    Button btnCloseReport;
    Button btnReport1;
    Button btnReport2;
    Button btnReport3;
    Button btnReport4;

    String strShoutId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        strShoutId = getIntent().getExtras().getString("REPORT_SHOUT_ID");

        setContentView(R.layout.activity_report);
        hideBottomTabs();
        hideBothTopHeader();
        initialize();
    }

    private void initialize() {
        btnCloseReport = (Button) findViewById(R.id.btn_report_close);
        btnReport1 = (Button) findViewById(R.id.report_1);
        btnReport2 = (Button) findViewById(R.id.report_2);
        btnReport3 = (Button) findViewById(R.id.report_3);
        btnReport4 = (Button) findViewById(R.id.report_4);

        btnCloseReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(ReportActivity.this, ShoutDetailActivity.class);
                startActivity(objIntent);
                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                finish();
            }
        });

        btnReport1.setOnClickListener(this);
        btnReport2.setOnClickListener(this);
        btnReport3.setOnClickListener(this);
        btnReport4.setOnClickListener(this);

    }

    @Override
    public void onWebserviceResponce(String strUrl, String strResult) {
        if (Constants.REPORT_API.equals(strUrl)) {
            try {
                JSONObject objJsonObject = new JSONObject(strResult);
                if (objJsonObject.getBoolean("result")) {
                    SharedPreferences objProfileSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
                    JSONObject object = new JSONObject();
                    object.put("user_id", objProfileSharedPreferences.getString(Constants.USER_ID, ""));
                    object.put("shout_id", strShoutId);
                    new CallWebService(Constants.SHOUT_PASS_API, object, ReportActivity.this, this, true).execute();
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Constants.SHOUT_PASS_API.equals(strUrl)) {
            try {
                JSONObject objJsonObject = new JSONObject(strResult);
                if (objJsonObject.getBoolean("result")) {
                    DatabaseHelper objDatabaseHelper = new DatabaseHelper(ReportActivity.this);
                    objDatabaseHelper.deleteShoutById(strShoutId);
                    ReportActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent objIntent = new Intent(ReportActivity.this, ShoutDefaultActivity.class);
                            objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(objIntent);
                            overridePendingTransition(0, 0);
                            finish();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int intPosition = 0;
        switch (v.getId()) {
            case R.id.report_1:
                intPosition = 1;
                break;
            case R.id.report_2:
                intPosition = 2;
                break;
            case R.id.report_3:
                intPosition = 3;
                break;
            case R.id.report_4:
                intPosition = 4;
                break;
        }
        reportShout(intPosition);
    }

    private void reportShout(int intReportPosition) {
        try {
            SharedPreferences objProfileSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
            JSONObject object = new JSONObject();
            object.put("user_id", objProfileSharedPreferences.getString(Constants.USER_ID, ""));
            object.put("shout_id", strShoutId);
            object.put("position_id", intReportPosition);
            new CallWebService(Constants.REPORT_API, object, ReportActivity.this, this, true).execute();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
