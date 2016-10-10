package com.shout.shoutapplication.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.app.AppController;
import com.shout.shoutapplication.main.Adapter.MyPreferencesAdapter;
import com.shout.shoutapplication.main.Model.MyPreferencesModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PreferencesActivity extends Activity {

    private ListView objListViewPreferences;
    private TextView objTextViewBack;
    private TextView objTextViewSavePreferences;

    private ArrayList<MyPreferencesModel> arrMyPreferencesModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        initializeView();

        try {
            String tag_json_obj = "json_obj_req";
            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            SharedPreferences objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);


            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.MY_PREFRENCES_API, new JSONObject().put("user_id", objSharedPreferences.getString(Constants.USER_ID, "")), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("API RESPONSE : " + response.toString());
                    pDialog.hide();
                    try {
                        JSONObject objJsonObject = new JSONObject(response.toString());

                        arrMyPreferencesModel = new ArrayList<MyPreferencesModel>();
                        if (objJsonObject.getString("result").equals("true")) {
                            JSONArray objJsonArray = new JSONArray(objJsonObject.getString("preferences"));
                            for (int index = 0; index < objJsonArray.length(); index++) {

                                if (objJsonArray.getJSONObject(index).getString("status").equals("A")) {
                                    MyPreferencesModel objMyPreferencesModel = new MyPreferencesModel(
                                            objJsonArray.getJSONObject(index).getString("id"),
                                            objJsonArray.getJSONObject(index).getString("preference_id"),
                                            objJsonArray.getJSONObject(index).getString("title"),
                                            objJsonArray.getJSONObject(index).getString("status"),
                                            true);
                                    arrMyPreferencesModel.add(objMyPreferencesModel);
                                } else {
                                    MyPreferencesModel objMyPreferencesModel = new MyPreferencesModel(
                                            objJsonArray.getJSONObject(index).getString("id"),
                                            objJsonArray.getJSONObject(index).getString("preference_id"),
                                            objJsonArray.getJSONObject(index).getString("title"),
                                            objJsonArray.getJSONObject(index).getString("status"),
                                            false);
                                    arrMyPreferencesModel.add(objMyPreferencesModel);
                                }
                            }
                            objListViewPreferences.setAdapter(new MyPreferencesAdapter(arrMyPreferencesModel, PreferencesActivity.this));
                        }
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("ERROR : " + error.toString());
                    pDialog.hide();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Accept", "application/json");
                    return headers;
                }
            };
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initializeView() {
        objListViewPreferences = (ListView) findViewById(R.id.listview_my_preferences);
        objTextViewBack = (TextView) findViewById(R.id.btn_back_my_preferences);
        objTextViewSavePreferences = (TextView) findViewById(R.id.btn_save_my_preferences);
        writeListener();
    }

    private void writeListener() {
        objTextViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(PreferencesActivity.this, ShoutDefaultActivity.class);
                startActivity(objIntent);
                finish();
                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
            }
        });

        objTextViewSavePreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONArray objJsonArray = new JSONArray();

                System.out.println("ARRAY SIZE" + arrMyPreferencesModel.size());

                for (int index = 0; index < arrMyPreferencesModel.size(); index++) {
                    MyPreferencesModel objMyPreferencesModel = arrMyPreferencesModel.get(index);
                    if (objMyPreferencesModel.getStatus().equals("A")) {
                        try {
                            JSONObject objNewJsonObject = new JSONObject();
                            objNewJsonObject.put("id", objMyPreferencesModel.getId());
                            objNewJsonObject.put("preference_id", objMyPreferencesModel.getPreference_id());
                            objJsonArray.put(objNewJsonObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                try {
                    String tag_json_obj = "json_obj_req";
                    final ProgressDialog pDialog = new ProgressDialog(PreferencesActivity.this);
                    pDialog.setMessage("Loading...");
                    pDialog.show();

                    SharedPreferences objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);

                    JSONObject objJsonObject = new JSONObject();
                    objJsonObject.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));
                    objJsonObject.put("preferences", objJsonArray);

                    System.out.println("INPUT JSON : " + objJsonObject);

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.SAVE_PREFRENCES_API, objJsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("API RESPONSE : " + response.toString());
                            pDialog.hide();
                            try {
                                JSONObject objJsonObject = new JSONObject(response.toString());
                                if (objJsonObject.getString("result").equals("true")) {
                                    Toast.makeText(PreferencesActivity.this, objJsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (NullPointerException ne) {
                                ne.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("ERROR : " + error.toString());
                            pDialog.hide();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json");
                            headers.put("Accept", "application/json");
                            return headers;
                        }
                    };
                    // Adding request to request queue
                    AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent objIntent = new Intent(PreferencesActivity.this, ShoutDefaultActivity.class);
        startActivity(objIntent);
        finish();
        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
    }
}
