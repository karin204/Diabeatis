package com.example.karin.diabeatis.BroadcastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.karin.diabeatis.logic.FoodDbHandler;
import com.example.karin.diabeatis.logic.MyNotification;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Avi on 27/03/2017.
 */

public class MyBootReceiver extends BroadcastReceiver
{
    private FoodDbHandler dbHandler;
    private ArrayList<MyNotification> notifications = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        dbHandler = FoodDbHandler.getInstance(context);
        notifications = dbHandler.getAllNotifications();
        for (MyNotification myNotification: notifications)
        {
            if(myNotification.isActive())
                setNotiTime(context, myNotification.getHour(), myNotification.getMinute(), myNotification.getId());
        }
    }

    public void setNotiTime(Context context, int hour, int minute, int id)
    {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, MyBroadcastReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        if(calendar.getTimeInMillis() < System.currentTimeMillis())
            calendar.add(Calendar.DAY_OF_YEAR, 1);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }
}
