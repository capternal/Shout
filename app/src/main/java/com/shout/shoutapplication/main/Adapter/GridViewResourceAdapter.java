package com.shout.shoutapplication.main.Adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.ConnectivityBroadcastReceiver;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.main.CreateShoutActivity;
import com.shout.shoutapplication.main.Model.GridViewResourceModel;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Prasad on 6/23/2016.
 */
public class GridViewResourceAdapter extends BaseAdapter {

    ArrayList<GridViewResourceModel> arrGridViewModel = new ArrayList<GridViewResourceModel>();
    Activity objActivity;
    GridViewResourceModel objGridViewModel;

    public GridViewResourceAdapter(ArrayList<GridViewResourceModel> arrGridViewModel, Activity objActivity) {
        this.arrGridViewModel = arrGridViewModel;
        this.objActivity = objActivity;
    }

    @Override
    public int getCount() {
        return arrGridViewModel.size();
    }

    @Override
    public Object getItem(int position) {
        return arrGridViewModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class Holder {
        ImageView objImageViewCapturedImage;
        ImageView objImageViewCancelCapturedImage;
        Button objButtonCancelCapturedImage;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder objHolder = null;

        if (convertView == null) {
            LayoutInflater objInflater = (LayoutInflater) objActivity.getSystemService(objActivity.LAYOUT_INFLATER_SERVICE);
            convertView = objInflater.inflate(R.layout.grid_view_resources_cell, parent, false);
            objHolder = new Holder();
            objHolder.objImageViewCapturedImage = (ImageView) convertView.findViewById(R.id.captured_image);
            objHolder.objImageViewCancelCapturedImage = (ImageView) convertView.findViewById(R.id.image_cancel_captured);
            objHolder.objButtonCancelCapturedImage = (Button) convertView.findViewById(R.id.btn_cancel_captured_image);
            convertView.setTag(objHolder);
        } else {
            objHolder = (Holder) convertView.getTag();
        }

        objGridViewModel = arrGridViewModel.get(position);

        System.out.println(" IS ADD BUTTON : " + objGridViewModel.getIS_ADD_BUTTON());

        if (objGridViewModel.getIS_ADD_BUTTON()) {
            System.out.println("IN IS ADD BUTTON");
            objHolder.objImageViewCapturedImage.setImageDrawable(null);
            objHolder.objImageViewCapturedImage.setBackgroundResource(R.drawable.red_corner_icon);
            objHolder.objImageViewCancelCapturedImage.setVisibility(ImageView.GONE);
            objHolder.objButtonCancelCapturedImage.setVisibility(Button.GONE);
        } else {
            if (objGridViewModel.getRESUORCE_TYPE().equals("V")) {
                System.out.println("IN V");
//                objHolder.objImageViewCapturedImage.setImageURI(objGridViewModel.getPATH());
                Picasso.with(objActivity).load(objGridViewModel.getPATH()).resize(100, 100).centerCrop().into(objHolder.objImageViewCapturedImage);
//                System.out.println("THUMBNAIL PATH : " + ApplicationUtils.saveVideoThumbnail(bmThumbnail));

               /* objHolder.objImageViewCancelCapturedImage.setVisibility(ImageView.VISIBLE);
                objHolder.objButtonCancelCapturedImage.setVisibility(Button.VISIBLE);
                objHolder.objImageViewCapturedImage.setBackgroundResource(R.drawable.red_corner_icon);
                objHolder.objImageViewCapturedImage.setPadding(5, 5, 5, 5);*/

                objHolder.objImageViewCapturedImage.setPadding(5, 5, 5, 5);
                objHolder.objImageViewCancelCapturedImage.setVisibility(ImageView.INVISIBLE);
                objHolder.objImageViewCapturedImage.setBackgroundResource(R.drawable.red_corner_icon);
                objHolder.objButtonCancelCapturedImage.setVisibility(Button.VISIBLE);
                Animation fab_open = AnimationUtils.loadAnimation(objActivity, R.anim.fab_open);
                objHolder.objImageViewCancelCapturedImage.startAnimation(fab_open);

            } else if (objGridViewModel.getRESUORCE_TYPE().equals("C")) {
                System.out.println("IN C ");
//                objHolder.objImageViewCapturedImage.setImageURI(objGridViewModel.getPATH());
                Picasso.with(objActivity).load(objGridViewModel.getPATH()).resize(100, 100).centerCrop().into(objHolder.objImageViewCapturedImage);

                objHolder.objImageViewCapturedImage.setPadding(5, 5, 5, 5);
                objHolder.objImageViewCancelCapturedImage.setVisibility(ImageView.VISIBLE);
                objHolder.objImageViewCapturedImage.setBackgroundResource(R.drawable.red_corner_icon);
                objHolder.objButtonCancelCapturedImage.setVisibility(Button.VISIBLE);
            } else if (objGridViewModel.getRESUORCE_TYPE().equals("D")) {
                objHolder.objImageViewCapturedImage.setImageResource(R.drawable.default_file);
                objHolder.objImageViewCancelCapturedImage.setVisibility(ImageView.VISIBLE);
                objHolder.objButtonCancelCapturedImage.setVisibility(Button.VISIBLE);
                objHolder.objImageViewCapturedImage.setBackgroundResource(R.drawable.red_corner_icon);
                objHolder.objImageViewCapturedImage.setPadding(5, 5, 5, 5);
            } else {
                objHolder.objImageViewCapturedImage.setImageDrawable(null);
                objHolder.objImageViewCapturedImage.setBackgroundResource(R.drawable.grey_corner_icon);
                objHolder.objImageViewCancelCapturedImage.setVisibility(ImageView.GONE);
                objHolder.objButtonCancelCapturedImage.setVisibility(Button.GONE);
            }
        }

        objHolder.objImageViewCapturedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                && arrGridViewModel.size() <= Constants.MAX_RESOURCE_SIZE
//                && position <= Constants.MAX_RESOURCE_SIZE


                System.out.println("PRASANNA PRINT : POSITION : " + position);

                for (int index = 0; index < arrGridViewModel.size(); index++) {
                    GridViewResourceModel objGridViewResourceModel = arrGridViewModel.get(index);
                    System.out.println("ADAPTER POSITION : " + index + " PATH : " + objGridViewResourceModel.getPATH() + " TYPE : " + objGridViewResourceModel.getRESUORCE_TYPE() + " IS ADD : " + objGridViewResourceModel.getIS_ADD_BUTTON() + " CANCEL BUTTON : " + objGridViewResourceModel.getIS_VISIBLE());
                }

                GridViewResourceModel objGridViewResourceModelTemp = arrGridViewModel.get(position);
                System.out.println("PRASANNA PRINT : IS ADD BUTTON : " + objGridViewResourceModelTemp.getIS_ADD_BUTTON());


                if (objGridViewResourceModelTemp.getIS_ADD_BUTTON()) {
                    CreateShoutActivity.ResourcePosition = position;
                    System.out.println("PRASANNA PRINT : CALL SELECT IMAGE SUCCESS");
                    Utils.hideshowKeyboard(objActivity, false, v);
                    selectImage();
                } else {
                    if (objGridViewResourceModelTemp.getPATH() != null) {
                        if (objGridViewResourceModelTemp.getRESUORCE_TYPE().equals("V")) {

                        } else if (objGridViewResourceModelTemp.getRESUORCE_TYPE().equals("C")) {
                            System.out.println("SHOW POPUP WINDOW");
                            openPopupWindow(objGridViewResourceModelTemp.getPATH());
                        }
                    }
                }

            }
        });

        objHolder.objButtonCancelCapturedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("REMOVING POSITION : " + position);
                int intNextPosition = position + 1;

                int intResourceCounter = 0;
                int intAddButtonPosition = 0;
                for (int index = 0; index < arrGridViewModel.size(); index++) {
                    GridViewResourceModel objGridViewResourceModelCount = arrGridViewModel.get(index);
                    if (objGridViewResourceModelCount.getIS_ADD_BUTTON()) {
                        intResourceCounter++;
                        intAddButtonPosition = index;
                    }
                }
                System.out.println("RESOURCE COUNTER : " + intResourceCounter);
                if (intResourceCounter == 1) {
                    GridViewResourceModel objGridViewResourceModelCurrentCell = arrGridViewModel.get(position);
                    objGridViewResourceModelCurrentCell.setPATH(null);
                    objGridViewResourceModelCurrentCell.setRESUORCE_TYPE("");
                    objGridViewResourceModelCurrentCell.setIS_ADD_BUTTON(false);
                    objGridViewResourceModelCurrentCell.setIS_VISIBLE(true);
                    notifyDataSetChanged();
                } else {
                    GridViewResourceModel objGridViewResourceModelCurrentCell = arrGridViewModel.get(position);
                    objGridViewResourceModelCurrentCell.setPATH(null);
                    objGridViewResourceModelCurrentCell.setRESUORCE_TYPE("");
                    objGridViewResourceModelCurrentCell.setIS_ADD_BUTTON(true);
                    objGridViewResourceModelCurrentCell.setIS_VISIBLE(true);
                    notifyDataSetChanged();
                }
                for (int index = 0; index < arrGridViewModel.size(); index++) {
                    GridViewResourceModel objGridViewResourceModelObject = arrGridViewModel.get(index);
                    System.out.println("REMOVING POSITION : " + index + " PATH : " + objGridViewResourceModelObject.getPATH() + " TYPE : " + objGridViewResourceModelObject.getRESUORCE_TYPE() + " IS ADD : " + objGridViewResourceModelObject.getIS_ADD_BUTTON() + " CANCEL BUTTON : " + objGridViewResourceModelObject.getIS_VISIBLE());
                }
                for (int i = 0; i < arrGridViewModel.size(); i++) {
                    GridViewResourceModel objGridViewResourceModelTrace = arrGridViewModel.get(i);
                    GridViewResourceModel objGridViewResourceModelAddButton = arrGridViewModel.get(intAddButtonPosition);
                    if (i < intAddButtonPosition && objGridViewResourceModelTrace.getPATH() == null) {
                        arrGridViewModel.set(i, objGridViewResourceModelAddButton);
                        arrGridViewModel.set(intAddButtonPosition, objGridViewResourceModelTrace);
                        notifyDataSetChanged();
                        break;
                    }
                }
            }
        });
        return convertView;
    }

    private void openPopupWindow(Uri imagePath) {

        LayoutInflater objPopupInflater;
        View customCategoryAlertLayout;
        final PopupWindow objPopupWindowLargeSource;
        ImageView objClickedImage;
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
        System.out.println("IMAGE URI PATH: " + imagePath);
        File imgFile = new File(String.valueOf(imagePath));

        if (imgFile.exists()) {
            System.out.println("IMAGE URI : " + imagePath);
            objClickedImage.setVisibility(ImageView.VISIBLE);
            /*Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            objClickedImage.setImageBitmap(myBitmap);*/
            if (ConnectivityBroadcastReceiver.isConnected()) {
                Picasso.with(objActivity).load(imgFile).into(objClickedImage);
            } else {
                Picasso.with(objActivity).load(imgFile).networkPolicy(NetworkPolicy.OFFLINE).into(objClickedImage);
            }
        }
        System.out.println("IMAGE URI : " + imgFile.getAbsoluteFile());
        objClickedImage.setVisibility(ImageView.VISIBLE);
        /*Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        objClickedImage.setImageBitmap(myBitmap);*//*
        if (ConnectivityBroadcastReceiver.isConnected()) {
            Picasso.with(objActivity).load(imgFile.getAbsoluteFile()).into(objClickedImage);
        } else {
            Picasso.with(objActivity).load(imgFile.getAbsoluteFile()).networkPolicy(NetworkPolicy.OFFLINE).into(objClickedImage);
        }*/
        objClickedImage.setImageURI(imagePath);
        objClickedImageDissmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objPopupWindowLargeSource.dismiss();
            }
        });
    }

    private void selectImage() {

        // POPUP WINDOWS OBJECTS FOR SHOWING USER CHOOSED RESOURCES
        LayoutInflater objPopupInflater;
        View customCategoryAlertLayout;
        final PopupWindow objPopupWindowCategories;
        ImageView objImageViewVideo;
        ImageView objImageViewPhoto;
        ImageView objImageViewGallery;
        RelativeLayout objRelativeCategoryPopupOutsideTouch;

        objPopupInflater = (LayoutInflater) objActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customCategoryAlertLayout = objPopupInflater.inflate(R.layout.capture_shoot_popup, null, true);
        objRelativeCategoryPopupOutsideTouch = (RelativeLayout) customCategoryAlertLayout.findViewById(R.id.relative_shoot_out_side_touch);
        objImageViewVideo = (ImageView) customCategoryAlertLayout.findViewById(R.id.imageViewVideo);
        objImageViewPhoto = (ImageView) customCategoryAlertLayout.findViewById(R.id.imageViewPhoto);
        objImageViewGallery = (ImageView) customCategoryAlertLayout.findViewById(R.id.imageViewGallery);

        objPopupWindowCategories = new PopupWindow(customCategoryAlertLayout);
        objPopupWindowCategories.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        objPopupWindowCategories.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        objPopupWindowCategories.setFocusable(true);

        objPopupWindowCategories.showAtLocation(customCategoryAlertLayout, Gravity.CENTER, 0, 0);


        objRelativeCategoryPopupOutsideTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objPopupWindowCategories.dismiss();
            }
        });

        objImageViewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objPopupWindowCategories.dismiss();
                try {
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    Uri fileUri = getOutputMediaFileUri(CreateShoutActivity.MEDIA_TYPE_VIDEO);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                    objActivity.startActivityForResult(intent, Constants.REQUEST_MADE_FOR_VIDEO);
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        objImageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objPopupWindowCategories.dismiss();
                lanchCamera();

            }
        });
        objImageViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objPopupWindowCategories.dismiss();
                launchGallery();
            }
        });
    }

    private void launchGallery() {
        try {
            Intent intentGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            objActivity.startActivityForResult(intentGallery, Constants.REQUEST_MADE_FOR_GALLERY);
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openFile(String minmeType, int request_type) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(minmeType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // special intent for Samsung file manager
        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        // if you want any file type, you can skip next line
        sIntent.putExtra("CONTENT_TYPE", minmeType);
        sIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Intent chooserIntent;
        if (objActivity.getPackageManager().resolveActivity(sIntent, 0) != null) {
            // it is device with samsung file manager
            chooserIntent = Intent.createChooser(sIntent, "Open file");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
        } else {
            chooserIntent = Intent.createChooser(intent, "Open file");
        }
        try {
            objActivity.startActivityForResult(chooserIntent, request_type);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(objActivity, "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
        }
    }

    private static Uri getOutputMediaFileUri(int type) {

        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Constants.APPLICATION_PATH);
        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }

        // Create a media file name
        // For unique file name appending current timeStamp with file name
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());
        File mediaFile;

        if (type == CreateShoutActivity.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        return mediaFile;
    }

    private void lanchCamera() {
        try {
            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "CRASH_" + System.currentTimeMillis());
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            System.out.println("APPLICATION PATH IN LAUNCH CAMERA :" + Constants.APPLICATION_PATH);
            CreateShoutActivity.mCapturedImageURI = objActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, CreateShoutActivity.mCapturedImageURI);
            objActivity.startActivityForResult(intentCamera, Constants.REQUEST_MADE_FOR_CAMERA);
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
