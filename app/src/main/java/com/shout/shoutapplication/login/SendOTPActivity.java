package com.shout.shoutapplication.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.NetworkUtils;
import com.shout.shoutapplication.database.DatabaseHelper;
import com.shout.shoutapplication.login.model.CountryListModel;

import org.json.JSONObject;

import java.util.ArrayList;

public class SendOTPActivity extends Activity {

    TextView objTextViewCountryList;
    Button btnSendOtp;
    EditText objEditTextContactNumber;

    DatabaseHelper objDatabaseHelper;
    ArrayList<CountryListModel> arrCountryListModel;
    Parcelable state;
    ListView objListViewCountry;
    TextView objTextViewCancel;
    LayoutInflater objCountryPopupInflater;
    View objCountryPopupLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);

        objDatabaseHelper = new DatabaseHelper(this);

        init();

        TelephonyManager telemamanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String strSimCardNumber = telemamanger.getLine1Number();
        System.out.println("USER TELEPHONE NUMBER : " + strSimCardNumber);

        try {
            if (strSimCardNumber.contains(objTextViewCountryList.getText().toString())) {
                objEditTextContactNumber.setText(strSimCardNumber.replace(objTextViewCountryList.getText().toString(), ""));
            } else {
                objEditTextContactNumber.setText(strSimCardNumber);
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {

        // CUSTOM COUNTRY POPUP LAYOUT COMPONENTS
        objCountryPopupInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        objCountryPopupLayout = objCountryPopupInflater.inflate(R.layout.custom_choose_country_layout, null, true);
        objListViewCountry = (ListView) objCountryPopupLayout.findViewById(R.id.listview_custom_country_list);
        objTextViewCancel = (TextView) objCountryPopupLayout.findViewById(R.id.txt_custom_country_list_cancel_button);

        objTextViewCountryList = (TextView) findViewById(R.id.txt_country_list_send_otp_screen);
        btnSendOtp = (Button) findViewById(R.id.btn_send_otp);
        objEditTextContactNumber = (EditText) findViewById(R.id.edt_contact_number_send_otp);

        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objEditTextContactNumber.getText().toString().length() > 0) {
                    new SendOTPRequest().execute("+91", objEditTextContactNumber.getText().toString());
                    InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                } else {
                    Toast.makeText(SendOTPActivity.this, "Enter Contact Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

       /* objTextViewCountryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupWindow objPopupWindow;
                objPopupWindow = new PopupWindow(objCountryPopupLayout);
                objPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                objPopupWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
                objPopupWindow.setFocusable(true);
                objPopupWindow.showAtLocation(objCountryPopupLayout, Gravity.CENTER, 0, 0);

                state = objListViewCountry.onSaveInstanceState();
                arrCountryListModel = new ArrayList<CountryListModel>();
                arrCountryListModel.addAll(objDatabaseHelper.getAllCountries());
                System.out.println("SIZE COUNTRY LIST ARRAY : " + arrCountryListModel);
                if (arrCountryListModel.size() > 0) {
                    objListViewCountry.setAdapter(new CountryListAdapter(arrCountryListModel, SendOTPActivity.this));
                }
                objListViewCountry.onRestoreInstanceState(state);

                objTextViewCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objPopupWindow.dismiss();
                    }
                });

                objListViewCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CountryListModel objCountryListModel = arrCountryListModel.get(position);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            try {
                                objTextViewCountryList.setCompoundDrawablesWithIntrinsicBounds(getDrawable(objCountryListModel.getIso().toLowerCase().toString()), 0, R.drawable.down_arrow_fill, 0);
                                SharedPreferences objSharedPreferences = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
                                SharedPreferences.Editor objEditor = objSharedPreferences.edit();
                                objEditor.putInt(Constants.SELECTED_FLAG_POSITION, position);
                                objEditor.commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        objTextViewCountryList.setText(objCountryListModel.getPhonecode());
                        objPopupWindow.dismiss();
                    }
                });
            }
        });*/
    }

    public class SendOTPRequest extends AsyncTask<String, Void, String> {

        final ProgressDialog objProgressDialog = new ProgressDialog(SendOTPActivity.this);
        String strResult = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                objProgressDialog.setTitle("Loading...");
                objProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                SharedPreferences objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);


                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));
                objJsonObject.put("country_code", params[0]);
                objJsonObject.put("mobile_no", params[1]);

                strResult = NetworkUtils.postData(Constants.RECEIVE_OTP, objJsonObject.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return strResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (objProgressDialog.isShowing()) {
                    objProgressDialog.dismiss();
                }
                if (s.length() > 0) {
                    JSONObject objJsonObject = new JSONObject(s);
                    if (objJsonObject.getString("result").toString().equals("true")) {
                        Intent objIntent = new Intent(SendOTPActivity.this, OTPVerificationActivity.class);
                        objIntent.putExtra("OTP", objJsonObject.getString("otp"));
                        objIntent.putExtra("MOBILE_NO", objEditTextContactNumber.getText().toString());
                        startActivity(objIntent);
                        finish();
                        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getDrawable(String name) {
        int resourceId = SendOTPActivity.this.getResources().getIdentifier(name, "drawable", SendOTPActivity.this.getPackageName());
        return resourceId;
    }
}
