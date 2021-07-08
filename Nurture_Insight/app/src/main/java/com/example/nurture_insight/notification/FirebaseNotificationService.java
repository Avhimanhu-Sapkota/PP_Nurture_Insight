package com.example.nurture_insight.notification;

import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;

import com.example.nurture_insight.MainActivity;
import com.example.nurture_insight.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseNotificationService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nurtureInsightNotification();
        }

        if(remoteMessage.getNotification()!=null){
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            showNurtureInsightNotification(title,body);
        }

        if (remoteMessage.getData().size()>0){
            for(Map.Entry<String, String> entry: remoteMessage.getData().entrySet()){
                Log.d("UNIQUENAME", "onMessageReceived: " + entry.getKey() +  " ====> " + entry.getValue() );
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void nurtureInsightNotification(){
        NotificationChannel notificationChannel = new NotificationChannel("nurtureInsightNotifications", "Nurture Insight - Notification",
                NotificationManager.IMPORTANCE_HIGH);

        notificationChannel.setDescription("Enable this channel to get all the notifications from Nurture Insight.");
        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    private void showNurtureInsightNotification(String title, String body){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =  new NotificationCompat.Builder(this, "nurtureInsightNotifications")

                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.app_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(notifyPendingIntent);

        notificationManager.notify(1, builder.build());
    }
}
