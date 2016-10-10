package com.shout.shoutapplication.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONObject;

/**
 * Created by CapternalSystems on 8/30/2016.
 */
public class CallWebService extends AsyncTask<String, Void, String> {

    String URL = "";
    JSONObject objJsonObject = null;
    Activity objActivity;
    WebserviceResponse objWebserviceResponse;
    private boolean showLoader = false;
    ProgressDialog progressDialog;

    public CallWebService(String URL, JSONObject objJsonObject, Activity objActivity, WebserviceResponse objWebserviceResponse, boolean showLoader) {
        this.URL = URL;
        this.objJsonObject = objJsonObject;
        this.objActivity = objActivity;
        this.objWebserviceResponse = objWebserviceResponse;
        this.showLoader = showLoader;
    }

    public CallWebService(String URL, JSONObject objJsonObject, Activity objActivity, WebserviceResponse objWebserviceResponse) {
        this.URL = URL;
        this.objJsonObject = objJsonObject;
        this.objActivity = objActivity;
        this.objWebserviceResponse = objWebserviceResponse;
    }

    public CallWebService(String URL, JSONObject objJsonObject, WebserviceResponse objWebserviceResponse) {
        this.URL = URL;
        this.objJsonObject = objJsonObject;
        this.objActivity = objActivity;
        this.objWebserviceResponse = objWebserviceResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (showLoader) {
            progressDialog = new ProgressDialog(objActivity);
            progressDialog.setTitle("Loading..");
            progressDialog.show();
        }

    }

    @Override
    protected String doInBackground(String... params) {
        String strResult = "";

        strResult = NetworkUtils.postData(this.URL, this.objJsonObject.toString());

        return strResult;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (showLoader) {
            progressDialog.dismiss();
        }
        objWebserviceResponse.onWebserviceResponce(this.URL, s);
    }


    public interface WebserviceResponse {
        public void onWebserviceResponce(String strUrl, String strResult);
    }
}
