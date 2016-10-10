package com.shout.shoutapplication.main.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.CallWebService;
import com.shout.shoutapplication.Utils.ConnectivityBroadcastReceiver;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.KeyboardUtils;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.database.DatabaseHelper;
import com.shout.shoutapplication.login.InviteFriendsActivity;
import com.shout.shoutapplication.login.model.ContactModel;
import com.shout.shoutapplication.main.Model.Continent;
import com.shout.shoutapplication.others.CircleTransform;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by CapternalSystems on 5/11/2016.
 */
public class ContactExpandableListAdapter extends BaseExpandableListAdapter implements KeyboardUtils.SoftKeyboardToggleListener, CallWebService.WebserviceResponse {

    private Context context;
    private Activity objActivity;
    private ArrayList<Continent> continentList;
    private ArrayList<Continent> originalList;
    private ArrayList<CompoundButton> arrSelectedContactPosition = new ArrayList<CompoundButton>();
    private InputMethodManager inputMethodManager;
    private DatabaseHelper objDatabaseHelper;
    private int rowId = 0;

    public ContactExpandableListAdapter(Context context, Activity objActivity, ArrayList<Continent> continentList) {
        this.context = context;
        this.objActivity = objActivity;
        this.continentList = new ArrayList<Continent>();
        this.continentList.addAll(continentList);
        this.originalList = new ArrayList<Continent>();
        this.originalList.addAll(continentList);
        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        objDatabaseHelper = new DatabaseHelper(objActivity);
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);


    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ContactModel> countryList = continentList.get(groupPosition).getArrContactList();
        return countryList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        final ContactModel objContactModel = (ContactModel) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.contact_listview_cell, null);
            final View finalView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KeyboardUtils.hideKeyboard(finalView, inputMethodManager);
                }
            });
        }

        view.setTag(objContactModel);

        TextView name = (TextView) view.findViewById(R.id.txt_contact_person_name);
        TextView number = (TextView) view.findViewById(R.id.txt_contact_person_number);
        final CheckBox objCheckBox = (CheckBox) view.findViewById(R.id.chk_tick);

        ImageView imageViewPhoneConnect = (ImageView) view.findViewById(R.id.imageViewPhoneConnect);
        ImageView imageViewFacebookConnect = (ImageView) view.findViewById(R.id.imageViewFacebookConnect);
        ImageView imageViewProfile = (ImageView) view.findViewById(R.id.imageViewProfile);
        ImageView imageViewBlueLove = (ImageView) view.findViewById(R.id.imageViewBlueLove);
        ImageView imageViewRefresh = (ImageView) view.findViewById(R.id.btn_refresh_friend_social_type);

        LinearLayout linearLayoutSocialConnect = (LinearLayout) view.findViewById(R.id.linearLayoutSocialConnect);
        Button btnAcceptFriendRequest = (Button) view.findViewById(R.id.btn_accept_friend_request);


        objCheckBox.setChecked(objContactModel.getCheckBokChecked());

        System.out.println("SEARCH CONTACT : GROUP POSITION : " + groupPosition);

        number.setVisibility(TextView.VISIBLE);

        if (objContactModel.getGroupPosition() == 0) {
            objCheckBox.setVisibility(CheckBox.GONE);
            imageViewProfile.setVisibility(ImageView.VISIBLE);
            imageViewBlueLove.setVisibility(ImageView.GONE);
            Picasso.with(context).load(R.drawable.shout_placeholder).transform(new CircleTransform()).into(imageViewProfile);
            imageViewProfile.setPadding(Constants.DEFAULT_CIRCLE_PADDING,
                    Constants.DEFAULT_CIRCLE_PADDING,
                    Constants.DEFAULT_CIRCLE_PADDING,
                    Constants.DEFAULT_CIRCLE_PADDING);

            if (!"".equals(objContactModel.getProfileImage())) {
                Picasso.with(context).load(objContactModel.getProfileImage()).transform(new CircleTransform()).into(imageViewProfile);
            } else {
                Picasso.with(context).load(R.drawable.shout_placeholder).transform(new CircleTransform()).into(imageViewProfile);
            }
            Utils.d("ADAPTER", "BUTTON TYPE : " + objContactModel.getButtonType());
            Utils.d("ADAPTER", "FACEBOOK FRIEND : " + objContactModel.getIsFacebookFriend());
            Utils.d("ADAPTER", "PHONE FRIEND : " + objContactModel.getIsPhoneFriend());

            if (!objContactModel.getButtonType().equals("N")) {
                linearLayoutSocialConnect.setVisibility(LinearLayout.VISIBLE);

                if (objContactModel.getButtonType().equals("A")) {

                    imageViewRefresh.setVisibility(ImageView.GONE);
                    imageViewPhoneConnect.setVisibility(ImageView.GONE);
                    imageViewFacebookConnect.setVisibility(ImageView.GONE);
                    btnAcceptFriendRequest.setVisibility(Button.VISIBLE);
                    number.setVisibility(TextView.GONE);
                    btnAcceptFriendRequest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //prasanna
                            Utils.d("ADAPTER", "USER ID : " + objContactModel.getId());
                            Utils.d("ADAPTER", "TABLE ID : " + objContactModel.getTableId());
                            rowId = objContactModel.getTableId();
                            if (ConnectivityBroadcastReceiver.isConnected()) {
                                callAcceptFriendAPI(objContactModel.getId());
                            } else {
                                Constants.showInternetToast(objActivity);
                            }
                        }
                    });
                } else {
                    imageViewRefresh.setVisibility(ImageView.VISIBLE);
                    imageViewPhoneConnect.setVisibility(ImageView.VISIBLE);
                    imageViewFacebookConnect.setVisibility(ImageView.VISIBLE);
                    btnAcceptFriendRequest.setVisibility(Button.GONE);

                    if (objContactModel.getIsFacebookFriend().equals("1")) {
                        imageViewRefresh.setBackgroundResource(R.drawable.refresh_grey);
                        imageViewFacebookConnect.setBackgroundResource(R.drawable.facebook_f);
                    } else {
                        imageViewFacebookConnect.setBackgroundResource(R.drawable.facebook_f_default);
                    }
                    if (objContactModel.getIsPhoneFriend().equals("1")) {
                        imageViewPhoneConnect.setVisibility(ImageView.VISIBLE);
                        imageViewPhoneConnect.setBackgroundResource(R.drawable.call_g);
                        imageViewRefresh.setBackgroundResource(R.drawable.refresh_grey);
                        number.setVisibility(TextView.VISIBLE);
                    } else {
                        number.setVisibility(TextView.GONE);
                        imageViewPhoneConnect.setBackgroundResource(R.drawable.call_g_default);
                    }
                    if(objContactModel.getIsFacebookFriend().equals("0") && objContactModel.getIsPhoneFriend().equals("0")){
                        imageViewRefresh.setBackgroundResource(R.drawable.refresh_red);
                    }
                }
                if (objContactModel.getIsPhoneFriend().equals("0"))
                    number.setVisibility(TextView.GONE);
            }else{
                imageViewRefresh.setVisibility(ImageView.VISIBLE);
                imageViewPhoneConnect.setVisibility(ImageView.VISIBLE);
                imageViewFacebookConnect.setVisibility(ImageView.VISIBLE);
                btnAcceptFriendRequest.setVisibility(Button.GONE);

                if (objContactModel.getIsFacebookFriend().equals("1")) {
                    imageViewRefresh.setBackgroundResource(R.drawable.refresh_grey);
                    imageViewFacebookConnect.setBackgroundResource(R.drawable.facebook_f);
                } else {
                    imageViewFacebookConnect.setBackgroundResource(R.drawable.facebook_f_default);
                }
                if (objContactModel.getIsPhoneFriend().equals("1")) {
                    imageViewPhoneConnect.setVisibility(ImageView.VISIBLE);
                    imageViewPhoneConnect.setBackgroundResource(R.drawable.call_g);
                    imageViewRefresh.setBackgroundResource(R.drawable.refresh_grey);
                    number.setVisibility(TextView.VISIBLE);
                } else {
                    number.setVisibility(TextView.GONE);
                    imageViewPhoneConnect.setBackgroundResource(R.drawable.call_g_default);
                }
                if(objContactModel.getIsFacebookFriend().equals("0") && objContactModel.getIsPhoneFriend().equals("0")){
                    imageViewRefresh.setBackgroundResource(R.drawable.refresh_red);
                }
            }
        } else {
            // disable the social connect layout null and display the profile default screen.
            linearLayoutSocialConnect.setVisibility(LinearLayout.GONE);
            imageViewProfile.setVisibility(ImageView.GONE);
            imageViewBlueLove.setVisibility(ImageView.VISIBLE);
            imageViewBlueLove.setBackgroundResource(R.drawable.invite_blue_love_icon);
            objCheckBox.setVisibility(CheckBox.VISIBLE);
            objCheckBox.setButtonDrawable(R.drawable.contact_list_blue_checkbok);
        }

        objCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox objCheck = (CheckBox) v;
                ContactModel objNewContactModel = (ContactModel) objCheck.getTag();
                objNewContactModel.setCheckBokChecked(objCheck.isChecked());
                notifyDataSetChanged();
            }
        });

        objCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked) {
                        arrSelectedContactPosition.add(buttonView);
                    } else {
                        arrSelectedContactPosition.remove(buttonView);
                    }
                    System.out.println("SELECTED CONTACT LIST SIZE : " + arrSelectedContactPosition.size());
                    if (arrSelectedContactPosition.size() > 0) {
                        InviteFriendsActivity.btnGiveThemShout.setText("Add to Shoutbook");
                    } else {
                        InviteFriendsActivity.btnGiveThemShout.setText("Go to Shoutboard");
                    }
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        name.setText(objContactModel.getContactName().trim());
        number.setText(objContactModel.getContactNumber().trim());
        objCheckBox.setTag(childPosition);
        objCheckBox.setTag(objContactModel);
        return view;
    }

    private void callAcceptFriendAPI(String toId) {
        //prasad
        try {
            SharedPreferences objProfileSharedPreferences = objActivity.getSharedPreferences(Constants.PROFILE_PREFERENCES, Context.MODE_PRIVATE);
            JSONObject objJsonObject = new JSONObject();
            objJsonObject.put("from_id", objProfileSharedPreferences.getString(Constants.USER_ID, ""));
            objJsonObject.put("to_id", toId);
            new CallWebService(Constants.FRIEND_ACCEPT_API, objJsonObject, objActivity, this, true).execute();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ContactModel> countryList = continentList.get(groupPosition).getArrContactList();
        return countryList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return continentList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return continentList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        Continent continent = (Continent) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.contact_listview_header, null);
            final View finalView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KeyboardUtils.hideKeyboard(finalView, inputMethodManager);
                }
            });
        }

        TextView heading = (TextView) view.findViewById(R.id.expandable_listview_contactlist_header);
        heading.setText(continent.getHeader().trim());

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void filterData(String query) {

        query = query.toLowerCase();
        Log.v("MyListAdapter", String.valueOf(continentList.size()));
        continentList.clear();

        if (query.isEmpty()) {
            continentList.addAll(originalList);
        } else {

            for (Continent continent : originalList) {

                ArrayList<ContactModel> countryList = continent.getArrContactList();
                ArrayList<ContactModel> newList = new ArrayList<ContactModel>();
                for (ContactModel country : countryList) {
                    if (country.getContactNumber().toLowerCase().contains(query) ||
                            country.getContactName().toLowerCase().contains(query)) {
                        newList.add(country);
                    }
                }
                if (newList.size() > 0) {
                    Continent nContinent = new Continent(continent.getHeader(), newList);
                    continentList.add(nContinent);
                }
            }
        }

        Log.v("MyListAdapter", String.valueOf(continentList.size()));
        notifyDataSetChanged();

    }

    @Override
    public void onToggleSoftKeyboard(boolean isVisible) {

    }

    @Override
    public void onWebserviceResponce(String strUrl, String strResult) {
        if (Constants.FRIEND_ACCEPT_API.equals(strUrl)) {
            try {

                JSONObject objJsonObject = new JSONObject(strResult);
                if (objJsonObject.getBoolean("result")) {
                    if (rowId == 0) {
                        Utils.d("ADAPTER", "BAD ROW ID");
                    } else {
                        ContactModel objContactModel = objDatabaseHelper.getFriendModel(rowId);
                        objContactModel.setButtonType("N");
                        boolean result = objDatabaseHelper.updateFriend(objContactModel);
                        if (result) {
                            Utils.d("ADAPTER", "FRIEND UPDATED.");
                        } else {
                            Utils.d("ADAPTER", "FRIEND NOT UPDATED.");
                        }
                        ((InviteFriendsActivity) objActivity).setLocalData();
                    }
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
