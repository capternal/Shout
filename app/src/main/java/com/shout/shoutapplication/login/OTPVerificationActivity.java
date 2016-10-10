package com.shout.shoutapplication.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.NetworkUtils;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.database.DatabaseHelper;
import com.shout.shoutapplication.login.model.CountryListModel;
import com.shout.shoutapplication.main.ShoutDefaultActivity;
import com.shout.shoutapplication.service.ContactResultReceiver;
import com.shout.shoutapplication.service.ContactSyncService;

import org.json.JSONObject;

import java.util.ArrayList;

public class OTPVerificationActivity extends Activity  {

    Button btnAddFriendsToShoutBook;
    Button btnOtpSubmit;
    TextView txtOTPUserMessage;
    TextView objTextViewOtpCountryList;
    EditText edtOtp;
    LinearLayout linearOtpSubmit;
    LinearLayout linearOtpSuccess;
    LinearLayout linearOtpFailed;
    Button btnResendOtp;
    EditText objEditTextContactNumber;
    public int OTP_ATTEMPT = 0;

    DatabaseHelper objDatabaseHelper;
    ArrayList<CountryListModel> arrCountryListModel;
    Parcelable state;
    ListView objListViewCountry;
    TextView objTextViewCancel;
    LayoutInflater objCountryPopupInflater;
    View objCountryPopupLayout;
    String OTP;
    String MOBILE_NO="";
    String Code;
    //    IncomingSms incomingSms;
    MessageOn messageOn;

    SharedPreferences objSharedPreferences;
    ContactResultReceiver contactResultReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        objDatabaseHelper = new DatabaseHelper(this);
        objSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);
        /*OTP = getIntent().getExtras().getString("OTP");

        */

//        incomingSms = new IncomingSms(this);
        //incomingSms.setOnReceiveMessageListener(this);
        init();

        try{
            MOBILE_NO = getIntent().getExtras().getString("MOBILE_NO");
        }catch(NullPointerException ne){

            ne.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();

        }
        if (MOBILE_NO.isEmpty()) {
            objEditTextContactNumber.setText("");
        }else{
            objEditTextContactNumber.setText(MOBILE_NO);
        }

        final IntentFilter smsFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

        try {
            smsFilter.setPriority(999);
            messageOn = new MessageOn();
            registerReceiver(messageOn, smsFilter);
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(messageOn);
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class MessageOn extends BroadcastReceiver {

        public MessageOn() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("YOGESHWAR TESTING");
//            Toast.makeText(context, "On Receive in same class", Toast.LENGTH_LONG).show();
            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {
                    Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (Object aPdusObj : pdusObj) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                        String senderAddress = currentMessage.getDisplayOriginatingAddress();
                        String message = currentMessage.getDisplayMessageBody();
                        // if the SMS is not from our gateway, ignore the message
                        /*if (Constants.SMS_ORIGIN.contains(senderAddress)) {
                            String otp = message.split(":")[1].trim();
                            edtOtp.setText(otp);
                            if (!otp.isEmpty()) {
                                new VerifyOtp().execute(edtOtp.getText().toString(), objEditTextContactNumber.getText().toString());
                            }
                            Utils.d("MESSAGE", "Received SMS : " + message + ", Sender: " + senderAddress);
                            Utils.d("MESSAGE", "Received OTP : " + otp);
                        }*/
                        String otp = message.split(":")[1].trim();
                        edtOtp.setText(otp);
                        if (!otp.isEmpty()) {
                            new VerifyOtp().execute(edtOtp.getText().toString(), objEditTextContactNumber.getText().toString());
                        }
                        Utils.d("MESSAGE", "Received SMS : " + message + ", Sender: " + senderAddress);
                        Utils.d("MESSAGE", "Received OTP : " + otp);
                    }
                }
            } catch (Exception e) {
                Utils.d("MESSAGE", "Exception: " + e.getMessage());
            }
        }
    }

    private void init() {
        // CUSTOM COUNTRY POPUP LAYOUT COMPONENTS
        objCountryPopupInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        objCountryPopupLayout = objCountryPopupInflater.inflate(R.layout.custom_choose_country_layout, null, true);
        objListViewCountry = (ListView) objCountryPopupLayout.findViewById(R.id.listview_custom_country_list);
        objTextViewCancel = (TextView) objCountryPopupLayout.findViewById(R.id.txt_custom_country_list_cancel_button);
        txtOTPUserMessage = (TextView) findViewById(R.id.txt_otp_message);
        edtOtp = (EditText) findViewById(R.id.edt_otp_code);
        btnAddFriendsToShoutBook = (Button) findViewById(R.id.btn_add_friends_to_shoutbook);
        btnOtpSubmit = (Button) findViewById(R.id.btn_otp_submit);
        linearOtpSubmit = (LinearLayout) findViewById(R.id.linear_otp_submit);
        linearOtpSuccess = (LinearLayout) findViewById(R.id.linear_otp_success);
        linearOtpFailed = (LinearLayout) findViewById(R.id.linear_otp_failed);
        btnResendOtp = (Button) findViewById(R.id.btn_resend_otp_otp_screen);
        objEditTextContactNumber = (EditText) findViewById(R.id.edt_contact_number_otp);
        objTextViewOtpCountryList = (TextView) findViewById(R.id.txt_select_country_otp_screen);

       /* objTextViewOtpCountryList.setOnClickListener(new View.OnClickListener() {
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
                    objListViewCountry.setAdapter(new CountryListAdapter(arrCountryListModel, OTPVerificationActivity.this));
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
                                objTextViewOtpCountryList.setCompoundDrawablesWithIntrinsicBounds(getDrawable(objCountryListModel.getIso().toLowerCase().toString()), 0, R.drawable.down_arrow_fill, 0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        objTextViewOtpCountryList.setText(objCountryListModel.getPhonecode());
                        objPopupWindow.dismiss();
                    }
                });
            }
        });*/

        btnResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnResendOtp.getText().toString().equals("SKIP")) {
                    if (objEditTextContactNumber.getText().toString().length() > 0) {
                        new SendOTPRequest().execute("+91", objEditTextContactNumber.getText().toString());
                        ++OTP_ATTEMPT;
                        System.out.println("OTP_ATTEMPT : " + OTP_ATTEMPT);
                        if (OTP_ATTEMPT == 2) {
                            btnResendOtp.setText("SKIP");
                        }
                    } else {
                        Toast.makeText(OTPVerificationActivity.this, "Please Enter the Contact Number.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                        if (btnResendOtp.getText().toString().equals("SKIP")) {
                            Intent objIntent = new Intent(OTPVerificationActivity.this, ProfileScreenActivity.class);
                            startActivity(objIntent);
                            finish();
                        }
                }
            }
        });


        edtOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                linearOtpSuccess.setVisibility(LinearLayout.GONE);
                linearOtpFailed.setVisibility(LinearLayout.GONE);
                linearOtpSubmit.setVisibility(LinearLayout.VISIBLE);
                btnAddFriendsToShoutBook.setVisibility(Button.GONE);
                if (s.length() == 4) {
                    View view = OTPVerificationActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        });

        btnOtpSubmit.setOnClickListener(new View.OnClickListener() {
            /*
            //IF USER PASTE THE CODE FROM OTHER AREA THE USE THIS CODE, WHICH CHECKS THE STRING CONTAINS A DIGIT'S OR NOT..
            String otp = edtOtp.getText().toString();
                if (otp.length() > 0) {
                    if (otp.length() > 0) {
                        boolean hasDigit = false;
                        for (int i = 0; i < otp.length(); i++) {
                            if (Character.isDigit(otp.charAt(i)))
                                hasDigit = true;
                            else
                                hasDigit = false;
                        }
                        if (hasDigit) {
                            linearOtpSuccess.setVisibility(LinearLayout.VISIBLE);
                            linearOtpFailed.setVisibility(LinearLayout.GONE);
                            linearOtpSubmit.setVisibility(LinearLayout.GONE);
                            btnAddFriendsToShoutBook.setVisibility(Button.GONE);

                            new VerifyOtp().execute(edtOtp.getText().toString(), objEditTextContactNumber.getText().toString());
                        }

                    } else {
                        linearOtpSuccess.setVisibility(LinearLayout.GONE);
                        linearOtpFailed.setVisibility(LinearLayout.VISIBLE);
                        linearOtpSubmit.setVisibility(LinearLayout.GONE);
                        btnAddFriendsToShoutBook.setVisibility(Button.GONE);
                    }
                }*/
            @Override
            public void onClick(View v) {
                System.out.println("ON OTP SUBMIT CLICK");
                if ((edtOtp.getText().toString().length() > 0 || !edtOtp.getText().toString().isEmpty()) && (objEditTextContactNumber.getText().length() > 0 || !objEditTextContactNumber.getText().toString().isEmpty())) {
                    linearOtpSuccess.setVisibility(LinearLayout.VISIBLE);
                    linearOtpFailed.setVisibility(LinearLayout.GONE);
                    linearOtpSubmit.setVisibility(LinearLayout.GONE);
                    btnAddFriendsToShoutBook.setVisibility(Button.GONE);

                    new VerifyOtp().execute(edtOtp.getText().toString(), objEditTextContactNumber.getText().toString());

                } else {
                    linearOtpSuccess.setVisibility(LinearLayout.GONE);
                    linearOtpFailed.setVisibility(LinearLayout.VISIBLE);
                    linearOtpSubmit.setVisibility(LinearLayout.GONE);
                    btnAddFriendsToShoutBook.setVisibility(Button.GONE);
                }
            }
        });

    }


    public int getDrawable(String name) {
        int resourceId = OTPVerificationActivity.this.getResources().getIdentifier(name, "drawable", OTPVerificationActivity.this.getPackageName());
        return resourceId;
    }


/*
    @Override
    public void onReceiveMessage(String sender, String message) {
        Utils.d("MESSAGE", "CALLED OTP VERIFIED CALLED : ");
        Toast.makeText(OTPVerificationActivity.this, "Sender : " + sender + ", Message : " + message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onReceiveMessage(String sender, String message) {
        System.out.println("Message Sender Name :" + sender + ", Verification Code Is:" + message);
        edtOtp.setText(message);


    }
*/


    public class VerifyOtp extends AsyncTask<String, Void, String> {

        final ProgressDialog objProgressDialog = new ProgressDialog(OTPVerificationActivity.this);
        String strResult = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                objProgressDialog.setTitle("Loading...");
                objProgressDialog.setCancelable(false);
                objProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("user_id", objSharedPreferences.getString(Constants.USER_ID, ""));
                objJsonObject.put("otp", params[0]);
                objJsonObject.put("mobile_no", params[1]);

                strResult = NetworkUtils.postData(Constants.VERIFY_OTP, objJsonObject.toString());
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

                        SharedPreferences.Editor objEditor = objSharedPreferences.edit();
                        objEditor.putString(Constants.OTP_VERIFIED, "1");
                        objEditor.commit();

                        if (objSharedPreferences.getString(Constants.PROFILE_COMPLETE, "").equals("0")) {
                            // TODO: 30/09/16 STARTING BACKGROUND SERVICE FOR SYNCING CONTACT
                            Intent objIntentService=new Intent(OTPVerificationActivity.this, ContactSyncService.class);
                            startService(objIntentService);
                            Toast.makeText(OTPVerificationActivity.this, objJsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            Intent objIntent = new Intent(OTPVerificationActivity.this, ProfileScreenActivity.class);
                            startActivity(objIntent);
                            finish();
                            overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                        } else {
                            Intent objIntent = new Intent(OTPVerificationActivity.this, ShoutDefaultActivity.class);
                            startActivity(objIntent);
                            finish();
                            overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                        }
                    } else {
                        Toast.makeText(OTPVerificationActivity.this, objJsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class SendOTPRequest extends AsyncTask<String, Void, String> {
        final ProgressDialog objProgressDialog = new ProgressDialog(OTPVerificationActivity.this);
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
                        //edtOtp.setText(objJsonObject.getString("otp"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
