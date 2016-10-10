package com.shout.shoutapplication.main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shout.shoutapplication.CustomClasses.CustomFontTextView;
import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.NetworkUtils;
import com.shout.shoutapplication.base.BaseActivity;
import com.shout.shoutapplication.main.Adapter.CategoryAdapter;
import com.shout.shoutapplication.main.Adapter.CategoryNewAdapter;
import com.shout.shoutapplication.main.Model.CategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SortScreenActivity extends BaseActivity implements OnClickListener {

    //top layout
    ImageView imageViewSortFilterCancel;
    ImageView imageViewSortFilterOk;
    CustomFontTextView textViewSortScreenTitle;

    private ImageView imageViewTick, imageViewCancel;
    private LinearLayout linearLayoutCategories, linearLayoutAvailaible, linearLayoutFilter;
    private LinearLayout linearLayoutCategoriesSelect;
    private LinearLayout linearLayoutAvailabilitySelect;
    private LinearLayout linearLayoutClearFilter;

    private ListView categoriesListView;

    public static ArrayList<CategoryModel> arrCategoryModel = new ArrayList<CategoryModel>();

    // SORT OPTIONS
    public static String strPopularity = "";
    public static String strRecency = "";
    public static String strLocation = "";

    LinearLayout objLinearPopularity;
    LinearLayout objLinearRecency;
    LinearLayout objLinearLocation;

    ImageView objImageViewPopularity;
    ImageView objImageViewRecency;
    ImageView objImageViewLocation;

    TextView objTextViewPopularity;
    TextView objTextViewRecency;
    TextView objTextViewLocation;

    ImageView objImageViewCategoryArrow;
    ImageView objImageViewAvailabilityArrow;

    // AVAILABILITY UI COMPONENTS
    RelativeLayout objRelativeLayoutSortStartDate;
    TextView objTextViewSortStartDate;
    ImageView objImageViewSortStartDateArrow;

    RelativeLayout objRelativeLayoutSortEndDate;
    TextView objTextViewSortEndDate;
    ImageView objImageViewSortEndDateArrow;
    ImageView objImageViewClearFilter;


    // SORT & FILTER VARIABLES
//    static HashMap<String, Integer> objHashMapSortData;
    static ArrayList<String> arrSortFilterSelectedCategoryId = new ArrayList<String>();
    static ArrayList<String> arrTempSortFilterSelectedCategoryId = new ArrayList<String>();


    SharedPreferences objProfileSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_screen);

        hideAllView();

        objProfileSharedPreferences = getSharedPreferences(Constants.PROFILE_PREFERENCES, MODE_PRIVATE);

        initialize();

        for (int index = 0; index < arrCategoryModel.size(); index++) {
            CategoryModel objCategoryModel = arrCategoryModel.get(index);
            if (objCategoryModel.isSelected()) {
                arrSortFilterSelectedCategoryId.add(objCategoryModel.getId());
            }
        }
        if (arrSortFilterSelectedCategoryId.size() > 0) {
            System.out.println("SELECTED CATEGORY ID : " + arrSortFilterSelectedCategoryId);
            objImageViewCategoryArrow.setImageDrawable(null);
            objImageViewCategoryArrow.setImageResource(R.drawable.sort_arrow_red);
            setFilterIcon();
        } else {
            objImageViewCategoryArrow.setImageDrawable(null);
            objImageViewCategoryArrow.setImageResource(R.drawable.sort_arrow_grey);
            setFilterIcon();
        }
        if (objProfileSharedPreferences.getString(Constants.SORT_POPULARITY, "").equals("1")) {
            objImageViewPopularity.setImageResource(R.drawable.sort_popularity_red);
            objTextViewPopularity.setTextColor(getResources().getColor(R.color.sort_red_color));
            setFilterIcon();
        } else {
            objImageViewPopularity.setImageResource(R.drawable.sort_popularity_grey);
            objTextViewPopularity.setTextColor(getResources().getColor(R.color.sort_grey_color));
            setFilterIcon();
        }

        if (objProfileSharedPreferences.getString(Constants.SORT_RECENCY, "").equals("1")) {
            objImageViewRecency.setImageResource(R.drawable.sort_recency_red);
            objTextViewRecency.setTextColor(getResources().getColor(R.color.sort_red_color));
            setFilterIcon();
        } else {
            objImageViewRecency.setImageResource(R.drawable.sort_recency_grey);
            objTextViewRecency.setTextColor(getResources().getColor(R.color.sort_grey_color));
            setFilterIcon();
        }

        if (objProfileSharedPreferences.getString(Constants.SORT_LOCATION, "").equals("1")) {
            objImageViewLocation.setImageResource(R.drawable.sort_location_red);
            objTextViewLocation.setTextColor(getResources().getColor(R.color.sort_red_color));
            setFilterIcon();
        } else {
            objImageViewLocation.setImageResource(R.drawable.sort_location_grey);
            objTextViewLocation.setTextColor(getResources().getColor(R.color.sort_grey_color));
            setFilterIcon();
        }

        new CategoryListApi().execute();
        setListener();
//        showFilterAndDisableAll();
    }

    private void setListener() {
        linearLayoutCategoriesSelect.setOnClickListener(this);
        linearLayoutAvailabilitySelect.setOnClickListener(this);
        imageViewSortFilterCancel.setOnClickListener(this);
        imageViewSortFilterOk.setOnClickListener(this);
        linearLayoutClearFilter.setOnClickListener(this);

        //linearLayout.setOnClickListener(this);
    }

    public void setFilterIcon() {
        if (objProfileSharedPreferences.getString(Constants.SORT_POPULARITY, "").equals("0") && objProfileSharedPreferences.getString(Constants.SORT_RECENCY, "").equals("0") && objProfileSharedPreferences.getString(Constants.SORT_LOCATION, "").equals("0") && arrSortFilterSelectedCategoryId.size() == 0) {
            objImageViewClearFilter.setImageResource(R.drawable.grey_indicator_icon);
        } else {
            objImageViewClearFilter.setImageResource(R.drawable.red_indicatior_icon);
        }
    }

    private void initialize() {

        objImageViewClearFilter = (ImageView) findViewById(R.id.image_clear_filter);

        textViewSortScreenTitle = (CustomFontTextView) findViewById(R.id.textViewSortScreenTitle);
        //Filter
        linearLayoutFilter = (LinearLayout) findViewById(R.id.linearLayoutFilter);
        //Categories
        linearLayoutCategories = (LinearLayout) findViewById(R.id.linearLayoutCategories);
        linearLayoutCategoriesSelect = (LinearLayout) findViewById(R.id.linearLayoutCategoriesSelect);
        categoriesListView = (ListView) findViewById(R.id.categoriesListView);
        //Available
        linearLayoutAvailaible = (LinearLayout) findViewById(R.id.linearLayoutAvailability);
        linearLayoutAvailabilitySelect = (LinearLayout) findViewById(R.id.linearLayoutAvailabilitySelect);
        linearLayoutClearFilter = (LinearLayout) findViewById(R.id.linear_clear_filter);
        //Top Layout
        imageViewSortFilterOk = (ImageView) findViewById(R.id.imageViewSortFilterOk);
        imageViewSortFilterCancel = (ImageView) findViewById(R.id.imageViewSortFilterCancel);

        objLinearPopularity = (LinearLayout) findViewById(R.id.linear_popularity_sort_filter);
        objLinearRecency = (LinearLayout) findViewById(R.id.linear_recency_sort_filter);
        objLinearLocation = (LinearLayout) findViewById(R.id.linear_location_sort_filter);

        objImageViewPopularity = (ImageView) findViewById(R.id.image_view_popularity);
        objImageViewRecency = (ImageView) findViewById(R.id.image_view_recency);
        objImageViewLocation = (ImageView) findViewById(R.id.image_view_location);

        objTextViewPopularity = (TextView) findViewById(R.id.txt_popularity);
        objTextViewRecency = (TextView) findViewById(R.id.txt_recency);
        objTextViewLocation = (TextView) findViewById(R.id.txt_location);

        objImageViewCategoryArrow = (ImageView) findViewById(R.id.image_view_sort_category_next_arrow);
        objImageViewAvailabilityArrow = (ImageView) findViewById(R.id.image_view_sort_availability_next_arrow);

        objRelativeLayoutSortStartDate = (RelativeLayout) findViewById(R.id.relative_layout_sort_start_date);
        objTextViewSortStartDate = (TextView) findViewById(R.id.txt_set_start_date);
        objImageViewSortStartDateArrow = (ImageView) findViewById(R.id.image_start_date_arrow);

        objRelativeLayoutSortEndDate = (RelativeLayout) findViewById(R.id.relative_layout_sort_end_date);
        objTextViewSortEndDate = (TextView) findViewById(R.id.txt_set_end_date);
        objImageViewSortEndDateArrow = (ImageView) findViewById(R.id.image_end_date_arrow);


        objRelativeLayoutSortStartDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    objImageViewSortStartDateArrow.setImageResource(R.drawable.sort_arrow_red);
                    objImageViewSortStartDateArrow.setRotation(270);
                    final Calendar objCalendar;
                    /*if (objTextViewSortStartDate.getText().toString().length() > 0) {
                        objCalendar = new GregorianCalendar();
                        String strStartDate = objTextViewSortStartDate.getText().toString();
                        String dateSpliterFirst[] = strStartDate.split("/");
                        objCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSpliterFirst[0].toString()));
                        objCalendar.set(Calendar.MONTH, Integer.parseInt(dateSpliterFirst[1].toString()) - 1);
                        objCalendar.set(Calendar.YEAR, Integer.parseInt(dateSpliterFirst[2].toString()));
                    } else {
                        objCalendar = Calendar.getInstance();
                    }*/

                    objCalendar = Calendar.getInstance();

                    new DatePickerDialog(SortScreenActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            objCalendar.set(Calendar.YEAR, year);
                            objCalendar.set(Calendar.MONTH, monthOfYear);
                            objCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            SimpleDateFormat sendDateFormat = new SimpleDateFormat("MMM dd, yyyy");
                            String sendDate1 = sendDateFormat.format(objCalendar.getTime());
                            System.out.println("REQUEST START DATE :" + sendDate1);
                            objTextViewSortStartDate.setText(sendDate1);
                            objImageViewSortStartDateArrow.setRotation(0);
                            objImageViewSortStartDateArrow.setImageResource(R.drawable.sort_arrow_grey);
                        }
                    }, objCalendar.get(Calendar.YEAR), objCalendar.get(Calendar.MONTH), objCalendar.get(Calendar.DAY_OF_MONTH)).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        objRelativeLayoutSortEndDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    objImageViewSortEndDateArrow.setImageResource(R.drawable.sort_arrow_red);
                    objImageViewSortEndDateArrow.setRotation(270);
                    final Calendar objCalendar;
                    /*if (objTextViewSortEndDate.getText().toString().length() > 0) {
                        objCalendar = new GregorianCalendar();
                        String strEndDate = objTextViewSortEndDate.getText().toString();
                        String dateSpliterFirst[] = strEndDate.split("/");
                        objCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSpliterFirst[0].toString()));
                        objCalendar.set(Calendar.MONTH, Integer.parseInt(dateSpliterFirst[1].toString()) - 1);
                        objCalendar.set(Calendar.YEAR, Integer.parseInt(dateSpliterFirst[2].toString()));
                    } else {
                        objCalendar = Calendar.getInstance();
                    }*/

                    objCalendar = Calendar.getInstance();

                    new DatePickerDialog(SortScreenActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            objCalendar.set(Calendar.YEAR, year);
                            objCalendar.set(Calendar.MONTH, monthOfYear);
                            objCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            SimpleDateFormat sendDateFormat = new SimpleDateFormat("MMM dd, yyyy");
                            String sendDate1 = sendDateFormat.format(objCalendar.getTime());
                            System.out.println("REQUEST END DATE :" + sendDate1);
                            objTextViewSortEndDate.setText(sendDate1);
                            objImageViewSortEndDateArrow.setRotation(0);
                            objImageViewSortEndDateArrow.setImageResource(R.drawable.sort_arrow_grey);
                        }
                    }, objCalendar.get(Calendar.YEAR), objCalendar.get(Calendar.MONTH), objCalendar.get(Calendar.DAY_OF_MONTH)).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /*if (objHashMapSortData.get(strPopularity) == 1) {
            objImageViewPopularity.setImageDrawable(null);
            objImageViewPopularity.setImageResource(R.drawable.sort_popularity_red);
            objTextViewPopularity.setTextColor(getResources().getColor(R.color.sort_red_color));
        } else {
            objImageViewPopularity.setImageDrawable(null);
            objImageViewPopularity.setImageResource(R.drawable.sort_popularity_grey);
            objTextViewPopularity.setTextColor(getResources().getColor(R.color.sort_grey_color));
        }

        if (objHashMapSortData.get(strRecency) == 1) {
            objImageViewRecency.setImageDrawable(null);
            objImageViewRecency.setImageResource(R.drawable.sort_recency_red);
            objTextViewRecency.setTextColor(getResources().getColor(R.color.sort_red_color));
        } else {
            objImageViewRecency.setImageDrawable(null);
            objImageViewRecency.setImageResource(R.drawable.sort_recency_grey);
            objTextViewRecency.setTextColor(getResources().getColor(R.color.sort_grey_color));
        }

        if (objHashMapSortData.get(strLocation) == 1) {
            objImageViewLocation.setImageDrawable(null);
            objImageViewLocation.setImageResource(R.drawable.sort_location_red);
            objTextViewLocation.setTextColor(getResources().getColor(R.color.sort_red_color));
        } else {
            objImageViewLocation.setImageDrawable(null);
            objImageViewLocation.setImageResource(R.drawable.sort_location_grey);
            objTextViewLocation.setTextColor(getResources().getColor(R.color.sort_grey_color));
        }*/


        objLinearPopularity.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objProfileSharedPreferences.getString(Constants.SORT_POPULARITY, "").equals("0")) {
                    updateSort(Constants.SORT_POPULARITY, "1");
                    objImageViewPopularity.setImageDrawable(null);
                    objImageViewPopularity.setImageResource(R.drawable.sort_popularity_red);
                    objTextViewPopularity.setTextColor(getResources().getColor(R.color.sort_red_color));
                } else {
                    updateSort(Constants.SORT_POPULARITY, "0");
                    objImageViewPopularity.setImageDrawable(null);
                    objImageViewPopularity.setImageResource(R.drawable.sort_popularity_grey);
                    objTextViewPopularity.setTextColor(getResources().getColor(R.color.sort_grey_color));
                }
            }
        });

        objLinearRecency.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objProfileSharedPreferences.getString(Constants.SORT_RECENCY, "").equals("0")) {
                    updateSort(Constants.SORT_RECENCY, "1");
                    objImageViewRecency.setImageDrawable(null);
                    objImageViewRecency.setImageResource(R.drawable.sort_recency_red);
                    objTextViewRecency.setTextColor(getResources().getColor(R.color.sort_red_color));
                } else {
                    updateSort(Constants.SORT_RECENCY, "0");
                    objImageViewRecency.setImageDrawable(null);
                    objImageViewRecency.setImageResource(R.drawable.sort_recency_grey);
                    objTextViewRecency.setTextColor(getResources().getColor(R.color.sort_grey_color));
                }
            }
        });

        objLinearLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objProfileSharedPreferences.getString(Constants.SORT_LOCATION, "").equals("0")) {
                    updateSort(Constants.SORT_LOCATION, "1");
                    objImageViewLocation.setImageDrawable(null);
                    objImageViewLocation.setImageResource(R.drawable.sort_location_red);
                    objTextViewLocation.setTextColor(getResources().getColor(R.color.sort_red_color));
                } else {
                    updateSort(Constants.SORT_LOCATION, "0");
                    objImageViewLocation.setImageDrawable(null);
                    objImageViewLocation.setImageResource(R.drawable.sort_location_grey);
                    objTextViewLocation.setTextColor(getResources().getColor(R.color.sort_grey_color));
                }
            }
        });
    }

    public void updateSort(String strKey, String strValue) {
        SharedPreferences.Editor objEditor = objProfileSharedPreferences.edit();
        objEditor.putString(strKey, strValue);
        objEditor.commit();
        setFilterIcon();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearLayoutCategoriesSelect:
                showCategoryWindow(true);
                break;
            case R.id.linearLayoutAvailabilitySelect:
                showAvailability();
                break;
            case R.id.linear_clear_filter:
                SharedPreferences.Editor objEditor = objProfileSharedPreferences.edit();
                objEditor.putString(Constants.SORT_POPULARITY, "0");
                objEditor.putString(Constants.SORT_RECENCY, "0");
                objEditor.putString(Constants.SORT_LOCATION, "0");
                objEditor.commit();
                arrSortFilterSelectedCategoryId.clear();
                objImageViewClearFilter.setImageResource(R.drawable.grey_indicator_icon);
                objImageViewCategoryArrow.setImageDrawable(null);
                objImageViewCategoryArrow.setImageResource(R.drawable.sort_arrow_grey);
                objImageViewPopularity.setImageResource(R.drawable.sort_popularity_grey);
                objImageViewRecency.setImageResource(R.drawable.sort_recency_grey);
                objImageViewLocation.setImageResource(R.drawable.sort_location_grey);
                objTextViewPopularity.setTextColor(getResources().getColor(R.color.sort_grey_color));
                objTextViewRecency.setTextColor(getResources().getColor(R.color.sort_grey_color));
                objTextViewLocation.setTextColor(getResources().getColor(R.color.sort_grey_color));
                break;
            case R.id.imageViewSortFilterCancel:
                filterCancelClicked();
                break;

            case R.id.imageViewSortFilterOk:
                filterOkClicked();
                break;
        }
    }

    private void showAvailability() {
        showLinearLayout(linearLayoutAvailaible, true);
        showLinearLayout(linearLayoutFilter, false);
        showLinearLayout(linearLayoutCategories, false);
        textViewSortScreenTitle.setText("AVAILABILITY");
    }

    private void filterOkClicked() {
        // FILTER IS VISIBLE
        if (linearLayoutFilter.getVisibility() == View.VISIBLE) {
            Intent intent = new Intent(SortScreenActivity.this, ShoutDefaultActivity.class);
            startActivity(intent);
            finish();
        } else
        //CATEGORY IS VISIBLE
        {
            showFilterAndDisableAll();
        }
    }

    private void filterCancelClicked() {
        // FILTER IS VISIBLE
        if (linearLayoutFilter.getVisibility() == View.VISIBLE) {
            Intent intent = new Intent(SortScreenActivity.this, ShoutDefaultActivity.class);
            startActivity(intent);
            finish();
        } else {
            showFilterAndDisableAll();
        }
    }

    /**
     * show the filter screen and disable all other screen i.e Categories and availability
     */
    private void showFilterAndDisableAll() {
        //ANY CATEGORIES OR AVAILABILITY VISIBLE
        showLinearLayout(linearLayoutFilter, true);
        showLinearLayout(linearLayoutAvailaible, false);
        showLinearLayout(linearLayoutCategories, false);
        textViewSortScreenTitle.setText("SORT & FILTER");

        arrSortFilterSelectedCategoryId = new ArrayList<String>();

        for (int index = 0; index < arrCategoryModel.size(); index++) {
            CategoryModel objCategoryModel = arrCategoryModel.get(index);
            if (objCategoryModel.isSelected()) {
                arrSortFilterSelectedCategoryId.add(objCategoryModel.getId());
            }
        }

        if (arrSortFilterSelectedCategoryId.size() > 0) {
            System.out.println("SELECTED CATEGORY ID : " + arrSortFilterSelectedCategoryId);
            objImageViewCategoryArrow.setImageDrawable(null);
            objImageViewCategoryArrow.setImageResource(R.drawable.sort_arrow_red);
            setFilterIcon();
        } else {
            objImageViewCategoryArrow.setImageDrawable(null);
            objImageViewCategoryArrow.setImageResource(R.drawable.sort_arrow_grey);
            setFilterIcon();
        }
    }

    private void showCategoryWindow(boolean state) {
        //show Categories and gone other views
        if (state) {
            textViewSortScreenTitle.setText("CATEGORIES");
            showLinearLayout(linearLayoutCategories, true);
            showLinearLayout(linearLayoutAvailaible, false);
            showLinearLayout(linearLayoutFilter, false);
        } else {
            showFilterAndDisableAll();
        }
    }

    /**
     * To hide/show the linear Layout, just pass the object of linear
     * layout and state with boolean value
     *
     * @param linearLayout Linear layout to show/hide
     * @param state        state true or false
     */
    public void showLinearLayout(LinearLayout linearLayout, boolean state) {
        if (state)
            linearLayout.setVisibility(LinearLayout.VISIBLE);
        else
            linearLayout.setVisibility(LinearLayout.GONE);
    }

    public class CategoryListApi extends AsyncTask<Void, Void, String> {

        String strResult = "";

        //        final ProgressDialog objProgressDialog = new ProgressDialog(SortScreenActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                //objRelativeLayoutCategoryProgress.setVisibility(RelativeLayout.VISIBLE);
//                objProgressDialog.setTitle("Loading...");
//                objProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                strResult = NetworkUtils.postData(Constants.CATEGORY_LIST_API, "");
                return strResult;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return strResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                //objRelativeLayoutCategoryProgress.setVisibility(RelativeLayout.GONE);
//                if (objProgressDialog.isShowing()) {
//                    objProgressDialog.dismiss();
//                }
                if (s.length() > 0) {
                    JSONObject objJsonObject = new JSONObject(s);
                    String result = objJsonObject.getString("result");
                    arrCategoryModel = new ArrayList<CategoryModel>();
                    if (result.equals("true")) {
                        JSONArray objJsonArray = new JSONArray(objJsonObject.getString("categories"));
                        for (int index = 0; index < objJsonArray.length(); index++) {
                            CategoryModel objCategoryModel = new CategoryModel(
                                    objJsonArray.getJSONObject(index).getString("id"),
                                    objJsonArray.getJSONObject(index).getString("title"),
                                    Constants.HTTP_URL + objJsonArray.getJSONObject(index).getString("image"),
                                    Constants.HTTP_URL + objJsonArray.getJSONObject(index).getString("selected_image"),
                                    objJsonArray.getJSONObject(index).getString("created"),
                                    0
                            );
                            arrCategoryModel.add(objCategoryModel);
                        }
                        categoriesListView.setAdapter(new CategoryNewAdapter(SortScreenActivity.this, arrCategoryModel));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (s.length() > 0) {
                    JSONObject objJsonObject = null;
                    try {
                        objJsonObject = new JSONObject(s);
                        String result = objJsonObject.getString("result");
                        ArrayList<CategoryModel> arrCategoryModel = new ArrayList<CategoryModel>();
                        if (result.equals("true")) {

                            JSONArray objJsonArray = new JSONArray(objJsonObject.getString("categories"));

                            for (int index = 0; index < objJsonArray.length(); index++) {
                                CategoryModel objCategoryModel = new CategoryModel(
                                        objJsonArray.getJSONObject(index).getString("id"),
                                        objJsonArray.getJSONObject(index).getString("title"),
                                        Constants.HTTP_URL + objJsonArray.getJSONObject(index).getString("image"),
                                        Constants.HTTP_URL + objJsonArray.getJSONObject(index).getString("selected_image"),
                                        objJsonArray.getJSONObject(index).getString("created"),
                                        0
                                );
                                arrCategoryModel.add(objCategoryModel);
                            }
                            categoriesListView.setAdapter(new CategoryAdapter(SortScreenActivity.this, arrCategoryModel));
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (linearLayoutFilter.getVisibility() == View.VISIBLE) {
            Intent intent = new Intent(SortScreenActivity.this, ShoutDefaultActivity.class);
            startActivity(intent);
            finish();
        } else {
            showFilterAndDisableAll();
        }
    }
}
