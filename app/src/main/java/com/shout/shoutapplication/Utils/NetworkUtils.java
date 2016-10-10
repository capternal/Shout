package com.shout.shoutapplication.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shout.shoutapplication.app.AppController;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Capternal on 03/02/16.
 */
public class NetworkUtils {


    public static String getData(Context objContext, String url, JSONObject objJsonObject) {

        final String[] strResult = {""};
        String tag_json_obj = "json_obj_req";

        try {
            final ProgressDialog pDialog = new ProgressDialog(objContext);
            pDialog.setMessage("Loading...");
            pDialog.show();

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, objJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("RESPONSE : " + response.toString());
                    strResult[0] = response.toString();
                    pDialog.hide();
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
        return strResult[0];
    }


    public static String postData(String strURL, String strJsonObject) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        String strResultFromJson = "";
        try {
            int TIMEOUT_MILLISEC = 60000; // = 10 seconds
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
//            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

            HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
            HttpClient httpclient = new DefaultHttpClient(httpParams);
            HttpPost httppost = new HttpPost(strURL);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");

            // CLEAR LOG PROGRAMATICALLY
            Process process = new ProcessBuilder().command("logcat", "-c").redirectErrorStream(true).start();
            Utils.d("API INPUT : CALLED URL : " , strURL);
            Utils.d("API INPUT : JSON OBJECT : " , strJsonObject);

            httppost.setEntity(new ByteArrayEntity(strJsonObject.getBytes("UTF8")));
            HttpResponse objHttpResponse = httpclient.execute(httppost);
            System.out.println("FINAL HTTP STATUS : " + objHttpResponse.getStatusLine());
            InputStream objInputStream = objHttpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(objInputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            objInputStream.close();
            strResultFromJson = sb.toString();
            Utils.d("API OUTPUT : JSON OBJECT : " , strResultFromJson);
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strResultFromJson;
    }

    public static String getData(String strURL) {
        String strResultFromJson = "";
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            int TIMEOUT_MILLISEC = 60000; // = 10 seconds
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpClient objHttpClient = new DefaultHttpClient(httpParams);
            HttpGet objHttpGet = new HttpGet(strURL
            );
            HttpResponse objHttpResponse = objHttpClient.execute(objHttpGet);

            System.out.println("Status Code " + objHttpResponse.getStatusLine());

            InputStream objInputStream = objHttpResponse.getEntity().getContent();


            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    objInputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            objInputStream.close();

            strResultFromJson = sb.toString();
            System.out.println("Final Error Response Body Non==>" + strResultFromJson);
            System.out.println("Final Response From Server = "
                    + strResultFromJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResultFromJson;
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static String uploadResources(Context context, String strURL, String strShoutId, ArrayList<String> arrResourceType, ArrayList<Uri> arrResourcePath) {

        String strResult = "";

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(strURL);
            HttpResponse response = null;
            MultipartEntity entityBuilder = new MultipartEntity();

            entityBuilder.addPart("shout_id", new StringBody(strShoutId));

            for (int index = 0; index < arrResourcePath.size(); index++) {

                File file = new File(arrResourcePath.get(index).getPath());
                FileBody objFile = new FileBody(file);
                if (arrResourceType.get(index).equals("C")) {
                    entityBuilder.addPart("Image-" + index, objFile);
                } else if (arrResourceType.get(index).equals("V")) {
                    entityBuilder.addPart("Video-" + index, objFile);
                } else if (arrResourceType.get(index).equals("D")) {
                    entityBuilder.addPart("File-" + index, objFile);
                }
            }
            post.setEntity(entityBuilder);
            response = client.execute(post);
            HttpEntity httpEntity = response.getEntity();
            strResult = EntityUtils.toString(httpEntity);
            Utils.d("MULTIPART RESPONSE", strResult);
                    /*JSONObject objJsonObject = new JSONObject(result);
                    if (objJsonObject.has("result") && objJsonObject.getBoolean("result")) {
                        objDatabaseHelper.updateDOImageUploadStatus(arrobjDoImageModels.get(index).getIntId(), "Y");
                    }*/
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return strResult;
    }

    private static String getRealPathFromURI(Uri contentUri, Context context) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        System.out.println("REAL PATH : " + result);
        return result;
    }
}
