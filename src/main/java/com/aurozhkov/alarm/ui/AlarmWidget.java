package com.aurozhkov.alarm.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.aurozhkov.alarm.beans.AlarmDays;
import com.aurozhkov.alarm.beans.AlarmTime;
import com.aurozhkov.alarm.broadcast.Alarm;
import com.aurozhkov.alarm.R;
import com.aurozhkov.alarm.utils.AlarmStorageUtils;

public class AlarmWidget extends AppWidgetProvider {

    public static final String TAG = AlarmWidget.class.getSimpleName();
    public static final int SELECTED_COLOR = Color.parseColor("#00ff00");
    public static final int NOT_SELECTED_COLOR = Color.parseColor("#ffffff");

    final static String ACTION_CHANGE = "com.aurozhkov.alarm.on_off";

    private static Alarm mAlarm = new Alarm();

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(isValidIntent(intent)) {
            final int mAppWidgetId = getWidgetId(intent);
            if(isValidWidgetId(mAppWidgetId)) {
                changeOnOff(context);
                updateWidget(context, AppWidgetManager.getInstance(context), mAppWidgetId);
            }
        }
    }

    private boolean isValidIntent(Intent intent) {
        return intent != null && intent.getAction() != null && intent.getAction().equalsIgnoreCase(ACTION_CHANGE);
    }

    private int getWidgetId(Intent intent) {
        int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        Bundle extras = intent.getExtras();
        if(extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        return mAppWidgetId;
    }

    private boolean isValidWidgetId(int mAppWidgetId) {
        return mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID;
    }

    private void changeOnOff(Context context) {
        final boolean on = AlarmStorageUtils.getAlarmOn(context);
        if(on) {
            mAlarm.cancelAlarm(context);
        } else {
            mAlarm.setAlarm(context);
        }
        AlarmStorageUtils.saveAlarmOn(context, !on);
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetID) {
        final RemoteViews updatedWidgetView = getUpdatedWidgetView(context);
        final PendingIntent openConfigIntent = getOpenConfigIntent(context, widgetID);
        updatedWidgetView.setOnClickPendingIntent(R.id.alarm_container, openConfigIntent);

        final PendingIntent onOffIntent = getOnOffIntent(context, widgetID);
        updatedWidgetView.setOnClickPendingIntent(R.id.on, onOffIntent);

        appWidgetManager.updateAppWidget(widgetID, updatedWidgetView);
    }

    private static RemoteViews getUpdatedWidgetView(Context context) {
        final RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        updateTime(widgetView, context);
        updateDays(widgetView, context);
        updateOnOff(widgetView, context);
        return widgetView;
    }

    private static void updateOnOff(RemoteViews widgetView, Context context) {
        final boolean on = AlarmStorageUtils.getAlarmOn(context);
        final int buttonId = R.id.on;
        final int drawableForButtonId = on ? R.drawable.on : R.drawable.off;
        widgetView.setInt(buttonId, "setImageResource", drawableForButtonId);
    }

    private static void updateTime(RemoteViews widgetView, Context context) {
        final AlarmTime alarmTime = AlarmStorageUtils.getAlarmTime(context);
        widgetView.setTextViewText(R.id.time, alarmTime.getTimeString());
    }

    private static void updateDays(RemoteViews widgetView, Context context) {
        final AlarmDays alarmDays = AlarmStorageUtils.getAlarmDays(context);

        int day = 0;
        updateWidgetForDay(widgetView, R.id.monday, alarmDays.isSelected(day++));
        updateWidgetForDay(widgetView, R.id.tuesday, alarmDays.isSelected(day++));
        updateWidgetForDay(widgetView, R.id.wednesday, alarmDays.isSelected(day++));
        updateWidgetForDay(widgetView, R.id.thursday, alarmDays.isSelected(day++));
        updateWidgetForDay(widgetView, R.id.friday, alarmDays.isSelected(day++));
        updateWidgetForDay(widgetView, R.id.saturday, alarmDays.isSelected(day++));
        updateWidgetForDay(widgetView, R.id.sunday, alarmDays.isSelected(day));
    }

    private static void updateWidgetForDay(RemoteViews widgetView, int id, boolean isSelected) {
        int color;
        if (isSelected) {
            color = SELECTED_COLOR;
        } else {
            color = NOT_SELECTED_COLOR;
        }
        widgetView.setInt(id, "setTextColor", color);
    }

    private static PendingIntent getOpenConfigIntent(Context context, int widgetID) {
        Intent configIntent = new Intent(context, ConfigActivity.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        return PendingIntent.getActivity(context, widgetID, configIntent, 0);
    }

    private static PendingIntent getOnOffIntent(Context context, int widgetID) {
        Intent onOffIntent = new Intent(context, AlarmWidget.class);
        onOffIntent.setAction(ACTION_CHANGE);
        onOffIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        return PendingIntent.getBroadcast(context, widgetID, onOffIntent, 0);
    }
}
