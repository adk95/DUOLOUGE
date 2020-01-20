package com.hazyaz.ctup;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //  String notificationTitle = remoteMessage.getNotification().getTitle();
        //  String notificationText = remoteMessage.getNotification().getBody();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.beauty)
                        .setContentTitle("New Friend Request")
                        .setContentText("You have a new friend request");

        int mNotification = (int) System.currentTimeMillis();

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotification,builder.build());










    }
}
