package xyz.mrdeveloper.ignite;

/**
 * Created by Vaibhav on 18-01-2017.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;


public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {

    public StringBuilder key;
    public StringBuilder value;

    private static final String TAG = "MyAndroidFCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Log data to Log Cat
        Log.d("Check", "Here I am, this is me 0!");
/*
        UpdateFromFirebase updateFromFirebase = new UpdateFromFirebase(this);
        updateFromFirebase.Update();*/

       /* Looper.prepare();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 1000);*/

        Log.d("Check", "Here I am, this is me 6!");

        if (!remoteMessage.getData().isEmpty()) {
            Log.d(TAG, "hehe From: " + remoteMessage.getFrom());
            Log.d(TAG, "hehe Data Message Body: " + remoteMessage.getData());

            int i = 0;
            StringBuilder key = new StringBuilder();
            StringBuilder value = new StringBuilder();

            for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                key.append(entry.getKey()).append(",");
                value.append(entry.getValue()).append(",");
                //Log.d(TAG, "hehe " + "key, " + key + " value " + value);
            }

            String[] values = value.toString().split(",");
            String[] keys = key.toString().split(",");

            for (i = 0; i < values.length; ++i) {
                Log.d(TAG, "hehe value123: " + values[i]);
                if (ExistInWishlist(keys[i])) {
                    createNotificationFromData(keys[i], values[i], " ", "Tap to view updates");
                }
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "hehe hehe From: " + remoteMessage.getFrom());
            Log.e(TAG, " hehe Message Notification Title: " + remoteMessage.getNotification().getTitle());
            createNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

        /*int i = 0;
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();

        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            key.append(entry.getKey()).append(",");
            value.append(entry.getValue()).append(",");
            //Log.d(TAG, "hehe " + "key, " + key + " value " + value);
        }
        String[] values = value.toString().split(",");
        String[] keys = key.toString().split(",");

        //create notification
        for (i = 0; i < values.length; ++i) {
            Log.d(TAG, "hehe value123: " + values[i]);
            if (ExistInWishlist(values[i])) {
                createNotificationFromData(remoteMessage.getNotification().getBody(), values[i]);
            }
        }*/
    }

    private void createNotificationFromData(String firstString, String secondString, String inBetween, String contentText) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("ToOpen", 12);
        PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //Log.d(TAG, "hehe value456: ");

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ignite_logo_medium)
                .setContentText(secondString)
                .setSound(notificationSoundURI)
                .setContentTitle(firstString + inBetween + secondString)
                .setVibrate(new long[]{1000, 1000})
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setLights(Color.RED, 0, 0)
                .setAutoCancel(true)
                .setContentText(contentText)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        Random random = new Random();

        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(random.nextInt(), mNotificationBuilder.build());
    }

    private void createNotification(String title, String contentText) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        /*PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );*/
        //Log.d(TAG, "hehe value456: ");

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ignite_logo_medium)
                .setSound(notificationSoundURI)
                .setContentTitle(title)
                .setVibrate(new long[]{1000, 1000})
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setLights(Color.RED, 0, 0)
                .setAutoCancel(true)
                .setContentText(contentText)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        Random random = new Random();

        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(random.nextInt(), mNotificationBuilder.build());
    }

    public boolean ExistInWishlist(String title) {
        SharedPreferences pref = this.getSharedPreferences("WishList", MODE_PRIVATE);
        String wishedEvents = pref.getString("WishList", null);

        if (wishedEvents != null) {
            String[] events = wishedEvents.split(",");

            if (Arrays.asList(events).contains(title)) {
                return true;
            }
        }
        return false;
    }
}
