package com.shout.shoutapplication.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    NotificationCountListener objNotificationCountListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            objNotificationCountListener.onNotificationReceived(Integer.parseInt(intent.getExtras().getString("NOTIFICATION_COUNT")));
        }catch(NullPointerException ne){
            ne.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public interface NotificationCountListener {
        public void onNotificationReceived(int count);
    }

}


