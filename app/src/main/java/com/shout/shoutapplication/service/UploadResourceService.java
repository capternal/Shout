package com.shout.shoutapplication.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;

import com.shout.shoutapplication.R;
import com.shout.shoutapplication.Utils.ConnectivityBroadcastReceiver;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.Utils;
import com.shout.shoutapplication.database.DatabaseHelper;
import com.shout.shoutapplication.main.Adapter.UploadResourceModel;
import com.shout.shoutapplication.main.Model.ShoutDefaultListModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class UploadResourceService extends Service {

    DatabaseHelper objDatabaseHelper = new DatabaseHelper(this);
    final int id = 1;
    NotificationManager notificationManager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        objDatabaseHelper = new DatabaseHelper(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Utils.d("SERVICE :", "RESOURCE UPLOAD BACKGROUND SERVICE STARTED");
        }

        notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (ConnectivityBroadcastReceiver.isConnected()) {
            callWebService();
        } else {
            Utils.d("SERVICE :", "NO INTERNET CONNECTION");
        }
        return Service.START_NOT_STICKY;
    }

    public void showNotification() {
        System.out.println("NOTIFICATION CALLED");
        final Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Shout video uploading")
                .setContentText("Upload in progress")
                .setAutoCancel(false)
                .setOngoing(false)
                .setSmallIcon(R.drawable.shout_placeholder);
        builder.setProgress(0, 0, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationManager.notify(id, builder.build());
        }
    }


    private void callWebService() {
        Utils.d("SERVICE :", "VIDEO UPLOADING SERVICE CALLED");
        new UploadResource().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public class UploadResource extends AsyncTask<String, Void, String> {

        String strResourceId = "";
        String strShoutId = "";
        UploadResourceModel objUploadResourceModel;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objUploadResourceModel = objDatabaseHelper.getShoutResource();
            if (objUploadResourceModel != null) {
                Utils.d("SERVICE :", "VIDEO UPLOADING STARTED");
                showNotification();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String strResult = "";
            try {
                if (objUploadResourceModel != null) {
                    strResourceId = String.valueOf(objUploadResourceModel.getId());
                    this.strShoutId = objUploadResourceModel.getShoutId();
                    Utils.d("SERVICE :", " RESOURCE ID " + objUploadResourceModel.getShoutId());
                    Utils.d("SERVICE :", " RESOURCE THUMB PATH " + objUploadResourceModel.getTumbnail_Path());
                    Utils.d("SERVICE :", " RESOURCE VIDEO PATH " + objUploadResourceModel.getVideo_path());
                    Utils.d("SERVICE :", " RESOURCE THUMB URI " + Uri.parse(objUploadResourceModel.getTumbnail_Path()).getPath());
                    Utils.d("SERVICE :", " RESOURCE VIDEO URI " + Uri.parse(objUploadResourceModel.getVideo_path()).getPath());

                    // TODO: 04/10/16 CHECKING RESOURCE EXISTS OR NOT IN LOCAL STORAGE i.e SD-CARD OR INTERNAL STORAGE
                    if (new File(Uri.parse(objUploadResourceModel.getTumbnail_Path()).getPath()).isFile() && new File(Uri.parse(objUploadResourceModel.getVideo_path()).getPath()).isFile()) {
                        Utils.d("SERVICE :", "BOTH FILE EXITS UPLOADING RECORD ID : " + objUploadResourceModel.getId());
                        HttpClient client = new DefaultHttpClient();
                        HttpPost post = new HttpPost(Constants.UPLOAD_RESOURCES);
                        System.out.println("UPLOAD RESOURCE URL : " + Constants.UPLOAD_RESOURCES);
                        HttpResponse response = null;
                        MultipartEntity entityBuilder = new MultipartEntity();


                        entityBuilder.addPart("shout_id", new StringBody(objUploadResourceModel.getShoutId()));
                        entityBuilder.addPart("video_local_path", new StringBody(objUploadResourceModel.getVideo_path().toString()));

                        File thumbnailFile = new File(Uri.parse(objUploadResourceModel.getTumbnail_Path()).getPath());
                        FileBody objThumbnailFileBody = new FileBody(thumbnailFile);
                        entityBuilder.addPart("Video-0", objThumbnailFileBody);

                        File videoFile = new File(Uri.parse(objUploadResourceModel.getVideo_path()).getPath());
                        FileBody objVideoFileBody = new FileBody(videoFile);
                        entityBuilder.addPart("Video-1", objVideoFileBody);
                        post.setEntity(entityBuilder);
                        response = client.execute(post);
                        HttpEntity httpEntity = response.getEntity();
                        strResult = EntityUtils.toString(httpEntity);
                        Utils.d("VIDEO MULTIPART RESPONSE", strResult);
                    } else {
                        // TODO: 04/10/16 DELETE RESOURCE ROW FROM LOCAL
                        notificationManager.cancel(id);
                        boolean deleteResult = objDatabaseHelper.deleteUploadedResource(strResourceId);
                        if (deleteResult) {
                            Utils.d("SERVICE", "RESOURCE DELETED");
                        } else {
                            Utils.d("SERVICE", "RESOURCE NOT DELETED");
                        }
                    }
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (ClientProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return strResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject objJsonObject = new JSONObject(s);
                if (objJsonObject.getString("result").toString().equals("true")) {
                    boolean deleteResult = objDatabaseHelper.deleteUploadedResource(strResourceId);
                    if (deleteResult) {
                        Utils.d("SERVICE", "RESOURCE DELETED");
                    } else {
                        Utils.d("SERVICE", "RESOURCE NOT DELETED");
                    }
                    System.out.println("PRASAD PRINT : RESOURCE SHOUT ID : " + this.strShoutId);
                    ShoutDefaultListModel objTempShoutModel = objDatabaseHelper.getShoutDetails(this.strShoutId);
                    System.out.println("PRASAD PRINT : " + new JSONObject(objJsonObject.getString("shout_media")).getString("shout_image"));
                    System.out.println("PRASAD PRINT : " + new JSONObject(objJsonObject.getString("shout_media")).getString("images"));
                    objTempShoutModel.setSHOUT_IMAGE(new JSONObject(objJsonObject.getString("shout_media")).getString("shout_image"));
                    objTempShoutModel.setStrShoutImages(new JSONObject(objJsonObject.getString("shout_media")).getString("images"));
                    objDatabaseHelper.updateShout(objTempShoutModel, strShoutId);
                    if (objDatabaseHelper.isResourceExists()) {
                        if (ConnectivityBroadcastReceiver.isConnected()) {
                            notificationManager.cancel(id);
                            callWebService();
                        } else {
                            Utils.d("SERVICE :", "NO INTERNET CONNECTION");
                        }
                    } else {
                        // STOP SERVICE
                        Utils.d("SERVICE :", "NO RESOURCE IN LOCAL DATA");
                        notificationManager.cancel(id);
                        stopSelf();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
