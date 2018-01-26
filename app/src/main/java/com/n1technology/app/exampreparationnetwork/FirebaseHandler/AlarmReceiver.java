package com.n1technology.app.exampreparationnetwork.FirebaseHandler;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.n1technology.app.exampreparationnetwork.EntryPointActivity;
import com.n1technology.app.exampreparationnetwork.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by RAKEZ on 12/19/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    List<String> quotes = new ArrayList<>();
    @Override
    public void onReceive(Context context, Intent intent) {
        Random random = new Random();
        int pos = random.nextInt(10);
        Intent resultIntent = new Intent(context, EntryPointActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(context)
                .setSound(soundUri)
                .setContentTitle("Come back here")
                .setContentText(context.getResources().getStringArray(R.array.notification_quotes)[pos])
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(resultPendingIntent)
                .build();
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(127, notification);
    }
}
