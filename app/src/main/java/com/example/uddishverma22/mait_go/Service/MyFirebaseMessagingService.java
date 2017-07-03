package com.example.uddishverma22.mait_go.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.example.uddishverma22.mait_go.Activities.Announcements;
import com.example.uddishverma22.mait_go.Activities.MainActivity;
import com.example.uddishverma22.mait_go.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData() != null) {
            showFirebaseNotificationMessage(remoteMessage);
        }

        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//        }

        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void showFirebaseNotificationMessage(RemoteMessage remoteMessage) {
        try {
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            Intent intent = new Intent(this, Announcements.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Notification.Builder notificationBuilder = new Notification.Builder(this);
            notificationBuilder.setContentTitle(remoteMessage.getNotification().getTitle());
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSmallIcon(R.drawable.icon_error);
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_error));
            notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
            notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(m, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
