package com.aurozhkov.alarm.broadcast;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.aurozhkov.alarm.app.AlarmTimeCalculator;
import com.aurozhkov.alarm.ui.AlarmActivity;
import com.aurozhkov.alarm.app.AlarmMessageBuilder;
import com.aurozhkov.alarm.app.AlarmStorage;
import com.aurozhkov.alarm.utils.TimeUtils;

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final Intent openActivityIntent = new Intent(context, AlarmActivity.class);
        openActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(openActivityIntent);
    }

    public void setAlarm(Context context) {
        final int time = AlarmTimeCalculator.getSecondsToNextAlarm(AlarmStorage.getAlarmDays(context), AlarmStorage.getAlarmTime(context));
        if (time > 0) {
            setupAlarmManager(context, time);
            showMessage(context, time);
        }
    }

    private void setupAlarmManager(Context context, int seconds) {
        final PendingIntent pendingIntent = formIntent(context);
        final AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, TimeUtils.secondsToMillisecondsFromNow(seconds), pendingIntent);
    }

    private PendingIntent formIntent(Context context) {
        final Intent intent = new Intent(context, Alarm.class);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private void showMessage(Context context, int seconds) {
        final AlarmMessageBuilder alarmMessageBuilder = new AlarmMessageBuilder(context, seconds);
        Toast.makeText(context, alarmMessageBuilder.formMessage(), Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context) {
        final PendingIntent pendingIntent = formIntent(context);
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}