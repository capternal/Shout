package com.shout.shoutapplication;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gms.gcm.GcmListenerService;
import com.shout.shoutapplication.Utils.Constants;
import com.shout.shoutapplication.Utils.NotificationBroadcastReceiver;
import com.shout.shoutapplication.main.MessageBoardActivity;

import java.util.Random;

/**
 * Created by Capternal on 04/02/16.
 */
public class GCMIntentListener extends GcmListenerService {
    private static final String TAG = "MyGcmListenerService";
    private String order_id;
    private String notification_id;

    private String from_id = "";
    private String to_id = "";
    private String shout_id = "";


    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
    // Sets a title for the Inbox style big view

    NotificationBroadcastReceiver objNotificationBroadcastReceiver;





    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        try {
            String alert_message = data.getString("alert");
            String profile_url = data.getString("profile_url");
            shout_id = data.getString("shout_id");
            String type = data.getString("type");
            from_id = data.getString("from_id");
            to_id = data.getString("to_id");
            String user_name = data.getString("user_name");
            String created = data.getString("created");
            String message_type = data.getString("m_type");
            String image_url = data.getString("image_path");
            String strIsProcessed = data.getString("is_processed");
            String strApponentName = data.getString("other_user");
            String strRequestCount = data.getString("send_request");
            String strChatRowId = data.getString("chat_id");
            String strChatThumbPath = data.getString("image_thumb_path");
            String strNotificationType = data.getString("notification_type");
            String strNotificationCount = data.getString("notification_count");


            Intent objIntent=new Intent("NOTIFICATION_COUNT_LISTENER");
            objIntent.putExtra("NOTIFICATION_COUNT",data.getString("notification_count"));
            sendBroadcast(objIntent);

            // UNIQUE NOTIFICATION ID USED FOR EACH NOTIFICATION
            String Unique_Integer_Number = data.getString("id");
            String strNotificationAlert = data.getString("notification_alert");
        /*order_id = data.getString("order_id");
        notification_id = data.getString("notification_id");
        Utils.d("ORDER ID RECEIVED", data.getString("order_id"));*/


            System.out.println("MESSAGE RECEIVED : " + alert_message);

            Log.d(TAG, "From: " + from);
            Log.d(TAG, "Message: " + alert_message);

            if (from.startsWith("/topics/")) {
                // message received from some topic.
            } else {
                // normal downstream message.
            }
//        newOrderReceivedNotification(message);
//        CustomNotification(message);
            sendNotification(alert_message, profile_url, shout_id, type, from_id, to_id, user_name, Unique_Integer_Number, strNotificationAlert, created, message_type, image_url, strIsProcessed, strApponentName, strRequestCount, strChatRowId, strChatThumbPath, strNotificationType);
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

       private void sendNotification(String message, String strProfileUrl, String strShoutId,
                                  String strType, String strFromId, String strToId, String strUserName,
                                  String Unique_Integer_Number, String strNotificationAlert,
                                  String created, String message_type, String image_url, String strIsProcessed,
                                  String strApponentName, String strRequestCount, String strChatRowId, String strChatThumbPath, String strNotificationType) {
        SharedPreferences objChatPrefrences = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
        int numMessages = 0;
        RegistrationIntentService.arrMessages.add(strNotificationAlert);

        System.out.println("NOTIFICATIONS ARRAY : " + RegistrationIntentService.arrMessages);

        System.out.println("NOTIFICATION TYPE IN  GCMIntentService : " + strNotificationType);

        if (strNotificationType.equals("C")) {
            if (objChatPrefrences.getString(Constants.CHAT_SCREEN_ACTIVE, "").equals("true")) {

                System.out.println("RECEIVED DATA : " + strProfileUrl);

                SharedPreferences objProfilePreferences = getSharedPreferences(Constants.CHAT_PREFERENCES, MODE_PRIVATE);
                if (strShoutId.equals(objProfilePreferences.getString(Constants.CHAT_SHOUT_ID, ""))) {

                    System.out.println("IS PROCESSEED : " + strIsProcessed);
                    Intent objEditor = new Intent();
                    objEditor.setAction("appendChatScreenMsg");
                    objEditor.putExtra(Constants.SHOUT_ID_FOR_DETAIL_SCREEN, strShoutId);
                    objEditor.putExtra(Constants.USER_ID_FOR_DETAIL_SCREEN, String.valueOf(strToId));
                    objEditor.putExtra(Constants.SHOUT_TYPE_FOR_DETAIL_SCREEN, String.valueOf(strType));
                    objEditor.putExtra(Constants.USER_PROFILE_URL_FOR_DETAIL_SCREEN, String.valueOf(strProfileUrl));
                    objEditor.putExtra(Constants.CHAT_MESSAGE, message);
                    objEditor.putExtra(Constants.CHAT_CREATED, created);
                    objEditor.putExtra(Constants.CHAT_MESSAGE_TYPE, message_type);
                    objEditor.putExtra(Constants.CHAT_IMAGE_URL, image_url);
                    objEditor.putExtra(Constants.CHAT_FROM_ID, strFromId);
                    objEditor.putExtra(Constants.CHAT_TO_ID, strToId);
                    objEditor.putExtra(Constants.CHAT_IS_PROCESSED, strIsProcessed);
                    objEditor.putExtra("REQUEST_COUNT", strRequestCount);
                    objEditor.putExtra("CHAT_ROW_ID", strChatRowId);
                    objEditor.putExtra("CHAT_THUMB_IMAGE", strChatThumbPath);

                    String[] strName = strUserName.toString().split(" ");
                    objEditor.putExtra(Constants.CHAT_SCREEN_APPONENT_USER_NAME, strName[0] + " " + strName[1].charAt(0));
                    this.sendBroadcast(objEditor);
                } else {
                    Intent intent = new Intent(this, MessageBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.facebook_logo)
                            .setContentTitle("Shout Application")
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentText(strNotificationAlert);

                    notificationBuilder.setContentIntent(pendingIntent);
                    // Moves events into the big view
                    for (int i = 0; i < RegistrationIntentService.arrMessages.size(); i++) {
                        inboxStyle.addLine(RegistrationIntentService.arrMessages.get(i));
                    }
                    inboxStyle.setBigContentTitle("Shout App");
                    notificationBuilder.setStyle(inboxStyle);
                    notificationManager.notify(Integer.parseInt(Unique_Integer_Number), notificationBuilder.build());
                }
            } else {
                Intent intent = new Intent(this, MessageBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            /*Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.facebook_logo)
                    .setContentTitle("Shout Application")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build());*/

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.facebook_logo)
                        .setContentTitle("Shout Application")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentText(strNotificationAlert);

                notificationBuilder.setContentIntent(pendingIntent);

                inboxStyle.setBigContentTitle("Shout App");
                // Moves events into the big view
                for (int i = 0; i < RegistrationIntentService.arrMessages.size(); i++) {
                    inboxStyle.addLine(RegistrationIntentService.arrMessages.get(i));
                }
                notificationBuilder.setStyle(inboxStyle);
                notificationManager.notify(Integer.parseInt(Unique_Integer_Number), notificationBuilder.build());
            }
        } else if (strNotificationType.equals("FR")) {
            // FRIEND REQUEST
        } else if (strNotificationType.equals("O")) {
            // OFFERS
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void newOrderReceivedNotification(String message) {
        int intTag = new Random().nextInt();
        System.out.println("Final Random Number====>" + intTag);
        /*Utils.d("ORDER ID NOTIFY", String.valueOf(order_id));

        // Prepare intent which is triggered if the
        // notification is selected
        AcceptButtonReciever acceptButtonReciever = new AcceptButtonReciever();
        registerReceiver(acceptButtonReciever, new IntentFilter("com.neonrunner.runner.ACCEPT"));
        Intent acceptIntent = new Intent("com.neonrunner.runner.ACCEPT");
        Bundle bundle = new Bundle();
        bundle.putString("order_id", order_id);
        bundle.putString("notification_id", notification_id);
        bundle.putInt("msg_id", intTag);
        acceptIntent.putExtra("order_details", bundle);
        PendingIntent pIntentAccept = PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), acceptIntent, 0);

        RejectButtonReciever rejectButtonReciever = new RejectButtonReciever();
        registerReceiver(acceptButtonReciever, new IntentFilter("com.neonrunner.runner.REJECT"));
        Intent rejectIntent = new Intent("com.neonrunner.runner.REJECT");
        PendingIntent pIntentReject = PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), rejectIntent, 0);
        Intent unAssignedOrderIntent = new Intent();
        unAssignedOrderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pIntentOrders = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), unAssignedOrderIntent, 0);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Order Received")
                    .setContentText("")
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pIntentOrders)
                    .setSmallIcon(R.drawable.symbol)
                    .addAction(R.drawable.tick, "Accept", pIntentAccept)
                    .addAction(R.drawable.cross, "Reject", pIntentReject)
                    .setAutoCancel(true)
                    .setColor(Color.BLACK)
                    .build();
        } else {
            // do something for phones running an SDK before lollipop
            notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Order Received")
                    .setContentText("")
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pIntentOrders)
                    .setSmallIcon(R.drawable.neon_launcher)
                    .addAction(R.drawable.tick, "Accept", pIntentAccept)
                    .addAction(R.drawable.cross, "Reject", pIntentReject)
                    .setAutoCancel(true)
                    .build();
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        // noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(intTag, notification);*/
    }

    public void CustomNotification(String message) {
        int intTag = new Random().nextInt();
        System.out.println("Final Random Number====>" + intTag);
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.customnotification);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.facebook_logo)
                // Set Ticker Message
                .setTicker(getString(R.string.app_name))
                // Dismiss Notification
                .setAutoCancel(true)
                // Set PendingIntent into Notification
                // .setContentIntent(pIntent)
                // Set RemoteViews into Notification
                .setContent(remoteViews);

        // Locate and set the Image into customnotificationtext.xml ImageViews
        remoteViews.setTextViewText(R.id.txt_ordered_item_title_description, "Order Received");

        /*Intent switchIntent = new Intent(this, AcceptButtonReciever.class);
        switchIntent.putExtra("id", intTag);
        switchIntent.setAction(String.valueOf(intTag));
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(this, 0,
                switchIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.btn_accept_order_notification, pendingSwitchIntent);

        Intent switchIntent1 = new Intent(this, RejectButtonReciever.class);
        switchIntent1.putExtra("id", intTag);
        switchIntent1.setAction(String.valueOf(intTag));
        PendingIntent pendingSwitchIntent1 = PendingIntent.getBroadcast(this, 0,
                switchIntent1, 0);
        remoteViews.setOnClickPendingIntent(R.id.btn_reject_order_notification, pendingSwitchIntent1);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(intTag, builder.build());*/
    }


    /*@Override
    public void onWebserviceResponce(String strUrl, String strResult) {
        try {
            Utils.d("PUSH MESSAGES SAVE API URL : ", strUrl);
            Utils.d("PUSH MESSAGES SAVE API RESULT : ", strResult);

            JSONObject objJsonObject = new JSONObject(strResult);
            if (objJsonObject.getBoolean("result")) {
                JSONArray objJsonArray = new JSONArray(objJsonObject.getString("chats"));
                Utils.d("CHAT_JSON", objJsonArray.toString());
                new DatabaseHelper(this).saveChatMessages(shout_id, to_id, objJsonArray.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
