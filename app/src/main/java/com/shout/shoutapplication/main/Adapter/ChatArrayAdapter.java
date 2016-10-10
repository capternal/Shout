package com.shout.shoutapplication.main.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.ConnectivityBroadcastReceiver;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.NetworkUtils;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.app.AppController;
import com.shout.shoutapplication.database.DatabaseHelper;
import com.shout.shoutapplication.main.ChatForShoutActivity;
import com.shout.shoutapplication.main.Model.ChatMessage;
import com.shout.shoutapplication.others.CircleTransform;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Capternal on 18/06/16.
 */
public class ChatArrayAdapter extends BaseAdapter {


    Activity objActivity;
    ArrayList<ChatMessage> arrChatMessage = new ArrayList<ChatMessage>();
    TextView chatText;
    TextView chatDate;
    ImageView objUserProfileImage;
    NetworkImageView objImageViewResource;
    ImageView objImageViewTemp;
    RelativeLayout objRelativeLayoutImageLoading;
    RelativeLayout objRelativeLayoutAdminView;
    TextView objTextViewAdminMessage;
    LinearLayout objNoAcceptPass;
    TextView objTextViewAdminAccept;
    TextView objTextViewAdminPass;
    ChatMessage chatMessageObj;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ChatArrayAdapter(Activity objActivity, ArrayList<ChatMessage> arrChatMessage) {
        this.objActivity = objActivity;
        this.arrChatMessage = arrChatMessage;
    }

    @Override
    public int getCount() {
        return arrChatMessage.size();
    }

    @Override
    public Object getItem(int position) {
        return arrChatMessage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (arrChatMessage.size() > 0) {
            try {
                chatMessageObj = arrChatMessage.get(position);
                LayoutInflater inflater = (LayoutInflater) objActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (chatMessageObj.left) {
                    row = inflater.inflate(R.layout.chat_left, parent, false);
                } else {
                    row = inflater.inflate(R.layout.chat_right, parent, false);
                }

                // INITIALIZING COPONENTS
                objUserProfileImage = (ImageView) row.findViewById(R.id.chat_profile_image);
                chatText = (TextView) row.findViewById(R.id.textview_chat_message);
                chatDate = (TextView) row.findViewById(R.id.date);
                objImageViewResource = (NetworkImageView) row.findViewById(R.id.image_resource_chat_message);
                objImageViewTemp = (ImageView) row.findViewById(R.id.temp_chat_image);
                objRelativeLayoutImageLoading = (RelativeLayout) row.findViewById(R.id.relative_resource_chat_image_loading);
                objRelativeLayoutAdminView = (RelativeLayout) row.findViewById(R.id.relative_admin_view);
                objTextViewAdminMessage = (TextView) row.findViewById(R.id.txt_admin_text_message);
                objNoAcceptPass = (LinearLayout) row.findViewById(R.id.linear_admin_no_accept_pass);
                objTextViewAdminAccept = (TextView) row.findViewById(R.id.txt_admin_accept);
                objTextViewAdminPass = (TextView) row.findViewById(R.id.txt_admin_pass);

                objTextViewAdminAccept.setTag(position);
                objTextViewAdminPass.setTag(position);
                objNoAcceptPass.setTag(position);

                objTextViewAdminAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ShoutEngageAPI("Y", position).execute();
                        objNoAcceptPass.setVisibility(LinearLayout.GONE);
                    }
                });

                objTextViewAdminPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ShoutEngageAPI("N", position).execute(chatMessageObj.getChat_row_id());
                        objNoAcceptPass.setVisibility(LinearLayout.GONE);
                    }
                });

                if (chatMessageObj.getMessage_type().equals("I")) {
                    chatText.setVisibility(TextView.GONE);
                    objImageViewResource.setVisibility(NetworkImageView.VISIBLE);
                    System.out.println("CHAT IMAGE TEMP URI : " + chatMessageObj.getTemp_uri());
                    System.out.println("CHAT IMAGE SERVER URL : " + chatMessageObj.getImage_url());
                    if (chatMessageObj.getChat_thumb_image().equals("")) {
//                          objImageViewResource.setImageURI(chatMessageObj.getTemp_uri());
                        System.out.println("TEMP URI : " + chatMessageObj.getTemp_uri());
                        objImageViewTemp.setImageURI(chatMessageObj.getTemp_uri());
                        objRelativeLayoutImageLoading.setVisibility(RelativeLayout.VISIBLE);
                    } else {
                        objRelativeLayoutImageLoading.setVisibility(RelativeLayout.GONE);
                        objImageViewResource.setImageUrl(chatMessageObj.getChat_thumb_image(), imageLoader);
                    }
                } else if (chatMessageObj.getMessage_type().equals("LR")) {
                    chatText.setVisibility(TextView.GONE);
                    SharedPreferences objSharedPreferences = objActivity.getSharedPreferences(Constants.PROFILE_PREFERENCES, Context.MODE_PRIVATE);
                    if (chatMessageObj.getFrom_id().equals(objSharedPreferences.getString(Constants.USER_ID, ""))) {
                        System.out.println("LR : OWNER");
                        objRelativeLayoutAdminView.setVisibility(RelativeLayout.VISIBLE);
                        objTextViewAdminMessage.setText("You sent request.");
                        objNoAcceptPass.setVisibility(LinearLayout.GONE);
                    } else {
                        System.out.println("LR : USER" + chatMessageObj.getIsProcessed());
                        objRelativeLayoutAdminView.setVisibility(RelativeLayout.VISIBLE);
                        if (chatMessageObj.getIsProcessed().equals("P")) {
                            objNoAcceptPass.setVisibility(LinearLayout.VISIBLE);
                            objTextViewAdminMessage.setText(chatMessageObj.getApponent_user_name() + " has requested you to accept sharing your product " + chatMessageObj.getShout_title() + " Please respond by clicking the button below.");
                        } else if (chatMessageObj.getIsProcessed().equals("Y")) {
                            objTextViewAdminMessage.setText("You accepted request.");
                        } else if (chatMessageObj.getIsProcessed().equals("N")) {
                            objTextViewAdminMessage.setText("You accepted request.");
                        }
                    }
                } else if (chatMessageObj.getMessage_type().equals("RR")) {
                    chatText.setVisibility(TextView.GONE);
                    SharedPreferences objSharedPreferences = objActivity.getSharedPreferences(Constants.PROFILE_PREFERENCES, Context.MODE_PRIVATE);
                    if (chatMessageObj.getFrom_id().equals(objSharedPreferences.getString(Constants.USER_ID, ""))) {
                        System.out.println("RR : OWNER");
                        objRelativeLayoutAdminView.setVisibility(RelativeLayout.VISIBLE);
                        objTextViewAdminMessage.setText("You sent request.");
                        objNoAcceptPass.setVisibility(LinearLayout.GONE);
                    } else {
                        System.out.println("RR : USER" + chatMessageObj.getIsProcessed());
                        objRelativeLayoutAdminView.setVisibility(RelativeLayout.VISIBLE);
                        if (chatMessageObj.getIsProcessed().equals("P")) {
                            objNoAcceptPass.setVisibility(LinearLayout.VISIBLE);
                            objTextViewAdminMessage.setText(chatMessageObj.getApponent_user_name() + " has requested you to accept shout '" + chatMessageObj.getShout_title() + "'. Please respond by clicking the button below.");
                        } else if (chatMessageObj.getIsProcessed().equals("Y")) {
                            objTextViewAdminMessage.setText("You accepted request.");
                        } else if (chatMessageObj.getIsProcessed().equals("N")) {
                            objTextViewAdminMessage.setText("You accepted request.");
                        }
                    }
                } else if (chatMessageObj.getMessage_type().equals("T")) {
                    chatText.setVisibility(TextView.VISIBLE);
                    objImageViewResource.setVisibility(NetworkImageView.GONE);
                    chatText.setText(chatMessageObj.message);
                }
                chatDate.setText(chatMessageObj.created_date);
                if (ConnectivityBroadcastReceiver.isConnected()) {
                    Picasso.with(objActivity).load(chatMessageObj.getProfile_image_url()).transform(new CircleTransform()).into(objUserProfileImage);
                } else {
                    Picasso.with(objActivity).load(chatMessageObj.getProfile_image_url()).transform(new CircleTransform()).networkPolicy(NetworkPolicy.OFFLINE).into(objUserProfileImage);
                }
                objUserProfileImage.setPadding(2, 2, 2, 2);

                objImageViewResource.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openPopupWindow(chatMessageObj.getImage_url());
                    }
                });

            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        row.setTag(position);
        return row;
    }

    public class ShoutEngageAPI extends AsyncTask<String, Void, String> {
        final ProgressDialog objProgressDialog = new ProgressDialog(objActivity);
        String strIsProcessed = "";
        int intChatId;
        SharedPreferences objChatPreferences = objActivity.getSharedPreferences(Constants.CHAT_PREFERENCES, objActivity.MODE_PRIVATE);
        SharedPreferences objProfileSharedPreferences = objActivity.getSharedPreferences(Constants.PROFILE_PREFERENCES, objActivity.MODE_PRIVATE);
        DatabaseHelper objDatabaseHelper = new DatabaseHelper(objActivity);

        public ShoutEngageAPI(String strIsProcessed, int intChatId) {
            this.strIsProcessed = strIsProcessed;
            this.intChatId = intChatId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                objProgressDialog.setMessage("Processing...");
                objProgressDialog.show();
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String strResult = "";
            try {
                System.out.println("CHAT ROW ID : " + arrChatMessage.get(this.intChatId).getChat_row_id());
                JSONObject objJsonObject = new JSONObject();
                objJsonObject.put("chat_id", arrChatMessage.get(this.intChatId).getChat_row_id());
                objJsonObject.put("is_processed", this.strIsProcessed);
                objJsonObject.put("from_id", objProfileSharedPreferences.getString(Constants.USER_ID, ""));
                objJsonObject.put("to_id", objChatPreferences.getString(Constants.CHAT_APPONENT_ID, ""));
                strResult = NetworkUtils.postData(Constants.SHOUT_ENGAGE_API, objJsonObject.toString());
                return strResult;
            } catch (NullPointerException ne) {
                ne.printStackTrace();
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
                JSONObject objJsonObject = new JSONObject(s);
                if (objJsonObject.getBoolean("result")) {
                    System.out.println("RESULT TRUE");
                    if (objJsonObject.getBoolean("result")) {
                        JSONArray objJsonArray = new JSONArray(objJsonObject.getString("chats"));
                        Utils.d("CHAT_JSON", objJsonArray.toString());
                        objDatabaseHelper.saveChatMessages(objChatPreferences.getString(Constants.CHAT_SHOUT_ID, ""), objChatPreferences.getString(Constants.CHAT_APPONENT_ID, ""), objJsonArray.toString());
                        /*arrChatMessage = objDatabaseHelper.getLastMessages(objChatPreferences.getString(Constants.CHAT_SHOUT_ID, ""), objProfileSharedPreferences.getString(Constants.USER_ID, ""));
                        System.out.println("ARRAY " + arrChatMessage);*/
                        ((ChatForShoutActivity) objActivity).loadLocalChatMessages();
                        notifyDataSetChanged();
                    }
                } else {
                    System.out.println("RESULT FALSE");
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void openPopupWindow(String imageUrl) {

        LayoutInflater objPopupInflater;
        View customCategoryAlertLayout;
        final PopupWindow objPopupWindowLargeSource;
        ImageView objClickedImage;
        NetworkImageView objClickedImageViaUrl;
        ImageView objClickedImageDissmiss;


        objPopupInflater = (LayoutInflater) objActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customCategoryAlertLayout = objPopupInflater.inflate(R.layout.resource_popup_layout, null, true);
        objClickedImage = (ImageView) customCategoryAlertLayout.findViewById(R.id.clicked_image);
        objClickedImageDissmiss = (ImageView) customCategoryAlertLayout.findViewById(R.id.clicked_image_dismiss);

        objPopupWindowLargeSource = new PopupWindow(customCategoryAlertLayout);
        objPopupWindowLargeSource.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        objPopupWindowLargeSource.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        objPopupWindowLargeSource.setFocusable(true);

        objPopupWindowLargeSource.showAtLocation(customCategoryAlertLayout, Gravity.CENTER, 0, 0);

        objClickedImage.setVisibility(ImageView.VISIBLE);
        if (ConnectivityBroadcastReceiver.isConnected()) {
            Picasso.with(objActivity).load(imageUrl).into(objClickedImage);
        } else {
            Picasso.with(objActivity).load(imageUrl).networkPolicy(NetworkPolicy.OFFLINE).into(objClickedImage);
        }
        objClickedImageDissmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objPopupWindowLargeSource.dismiss();
            }
        });
    }

}