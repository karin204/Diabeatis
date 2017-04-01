package com.example.karin.diabeatis.BroadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v7.app.NotificationCompat;

import com.example.karin.diabeatis.R;
import com.example.karin.diabeatis.UI.MainPage;

/**
 * Created by Avi on 31/03/2017.
 */

public class MyBroadcastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.info);
        mBuilder.setContentTitle("בדיקת סוכר");
        mBuilder.setContentText("הגיע הזמן לבדוק מה רמת הסוכר שלך");
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setVibrate(new long[] { 1000, 1000, 1000});
        mBuilder.setLights(Color.BLUE, 3000, 3000);
        mBuilder.setAutoCancel(true);
        mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent resultIntent = new Intent(context, MainPage.class);
        resultIntent.putExtra("from","noti");
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(100, mBuilder.build());

    }
}
