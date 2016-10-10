package com.shout.shoutapplication.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by student on 11/06/16.
 */
public class ApplicationUtils {
    Context context;

    public ApplicationUtils(Context context) {
        this.context = context;
    }

    public Uri saveImageOnSdCard(byte[] imageBytes) {
        File parentFolder = new File(Constants.APPLICATION_PATH);
        if (!parentFolder.exists()) {
            parentFolder.mkdirs();
        }
        File destination = new File(Constants.APPLICATION_PATH, System.currentTimeMillis() + ".png");
        FileOutputStream fo;
        try {

            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(imageBytes);
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri imageUri = Uri.fromFile(destination);
        return imageUri;
    }

    private static void createDirectory(String applicationPath) {
        if (!new File(Constants.APPLICATION_PATH).exists()) {
            //create directory if not exist's
            File dir = new File(Constants.APPLICATION_PATH);
            dir.mkdir();
        }
    }

    public static Bitmap resizeBitmap(Bitmap bitmap) {
        int width = 300; // - Dimension in pixels
        int height = 300;  // - Dimension in pixels
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    public static Uri onCaptureImageResult(Intent data) {
        if (data != null) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //assert thumbnail != null;
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

            InputStream in = new ByteArrayInputStream(bytes.toByteArray());
            ContentBody foto = new InputStreamBody(in, "image/jpeg", "filename");

            //CREATE FOLDER IF NOT EXIST'S
            createDirectory(Constants.APPLICATION_PATH);
            File destination = new File(Constants.APPLICATION_PATH, System.currentTimeMillis() + ".jpeg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri imageUri = Uri.fromFile(destination);
            return imageUri;
        }
        return null;
    }


    public static Uri saveVideoThumbnail(Bitmap thumbnailImage) {
        if (thumbnailImage != null) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //assert thumbnail != null;
            thumbnailImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            //CREATE FOLDER IF NOT EXIST'S
            createDirectory(Constants.APPLICATION_PATH);
            File destination = new File(Constants.APPLICATION_PATH, System.currentTimeMillis() + ".png");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri imageUri = Uri.fromFile(destination);
            return imageUri;
        }
        return null;
    }

    //capture image from gallery
    /*
    just pass onActivityResult's intent object to this method.
    * */
    public Uri onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());

//                profileImageUri
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Uri imageUri = getImageUri(context, bm);
        Utils.d("debug", "PROFILE IMAGE URI 12:" + getImageUri(context, bm));
        return imageUri;
    }

    //to ge uri
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        String path = "";
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Intent getTwitterIntent(Context context, String shareText) {
        Intent shareIntent;

        if (isPackageExisted(context, "com.twitter")) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setClassName("com.twitter.android",
                    "com.twitter.android.PostActivity");
            shareIntent.setType("text/*");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
            return shareIntent;
        } else {
            String tweetUrl = "https://twitter.com/intent/tweet?text=" + shareText;
            Uri uri = Uri.parse(tweetUrl);
            shareIntent = new Intent(Intent.ACTION_VIEW, uri);
            return shareIntent;
        }
    }

    public boolean isPackageExisted(Context context, String targetPackage) {
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }

}
