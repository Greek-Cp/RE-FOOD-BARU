package com.nicomot.re_food.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import androidx.core.app.NotificationCompat;

import com.nicomot.re_food.models.Action;
import com.nicomot.re_food.robj.notificationhelperlibrary.utils.NotificationUtils;
import com.nicomot.re_food.util.ShowMessage;

import java.util.Locale;


public class ServiceNotificationListener extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Bundle bundle = sbn.getNotification().extras;;
        String from = bundle.getString(NotificationCompat.EXTRA_TITLE);
        String message = bundle.getString(NotificationCompat.EXTRA_TEXT);
        if(message.contains("new messages")){

        } else{
            if(message != null  && !from.isEmpty() && from != null){
                if(from.contains("+62")){
                    sendMessageToFragmentAndFilter(from,message);
                }
            }
        }


    }

    void sendMessageToFragmentAndFilter(String from , String message_text){
            Intent intentData = new Intent("NOTIFICATION_DATA");
            intentData.putExtra("FROM",from);
            intentData.putExtra("CHAT",message_text);
            sendBroadcast(intentData);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
    }
}
