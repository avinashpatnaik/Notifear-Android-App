package com.project.astroavi.hellonotif.Service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by Avinash on 19/03/17.
 */

public class NotifService extends NotificationListenerService {

    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        final String pack = sbn.getPackageName();
        final Bundle extras = sbn.getNotification().extras;

        final String title = extras.getString("android.title");
        final String text = extras.getCharSequence("android.text").toString();
        final long timeStamp = System.currentTimeMillis();

        if (!pack.equals("android")) {
            final Intent notifMsg = new Intent("NotifMsg");
            notifMsg.putExtra("package", pack);
            notifMsg.putExtra("title", title);
            notifMsg.putExtra("text", text);
            notifMsg.putExtra("timeStamp", String.valueOf(timeStamp));
            LocalBroadcastManager.getInstance(context).sendBroadcast(notifMsg);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
