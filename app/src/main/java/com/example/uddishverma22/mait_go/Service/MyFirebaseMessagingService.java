package com.example.uddishverma22.mait_go.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.example.uddishverma22.mait_go.Activities.Announcements;
import com.example.uddishverma22.mait_go.Activities.Assignments;
import com.example.uddishverma22.mait_go.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if (remoteMessage.getData().get("title").equals("New Announcement!")) {
            showFirebaseNotificationForAnnouncements(remoteMessage);
        }
        else if (remoteMessage.getData().get("title").equals("New Assignment!")) {
            showFirebaseNotificationForAssignments(remoteMessage);
        }

    }

    private void showFirebaseNotificationForAnnouncements(RemoteMessage remoteMessage) {

        try {
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
                Intent intent = new Intent(this, Announcements.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                Notification.Builder notificationBuilder = new Notification.Builder(this);
                notificationBuilder.setContentTitle(remoteMessage.getData().get("title"));
                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                notificationBuilder.setContentText(remoteMessage.getData().get("body"));
                notificationBuilder.setContentIntent(pendingIntent);
                notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
                notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(m, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFirebaseNotificationForAssignments(RemoteMessage remoteMessage) {

        try {
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            Intent intent = new Intent(this, Assignments.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Notification.Builder notificationBuilder = new Notification.Builder(this);
            notificationBuilder.setContentTitle(remoteMessage.getData().get("title"));
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            notificationBuilder.setContentText(remoteMessage.getData().get("body"));
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
