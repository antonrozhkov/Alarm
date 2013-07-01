package com.aurozhkov.alarm.broadcast;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.aurozhkov.alarm.ui.AlarmActivity;
import com.aurozhkov.alarm.utils.AlarmCalculateUtils;
import com.aurozhkov.alarm.utils.AlarmStorageUtils;

import java.util.Calendar;

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent openActivityIntent = new Intent(context, AlarmActivity.class);
        openActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(openActivityIntent);


    }

    public void setAlarm(Context context) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 5);

        Intent intent = new Intent(context, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        //int time = AlarmCalculateUtils.getSecondsToNextAlarm(AlarmStorageUtils.getAlarmDays(context), AlarmStorageUtils.getAlarmTime(context));
        int time = 0;
        Toast.makeText(context, "Alarm was set " + time, Toast.LENGTH_LONG).show();
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);

        Toast.makeText(context, "Alarm was cancel", Toast.LENGTH_LONG).show();
    }
}