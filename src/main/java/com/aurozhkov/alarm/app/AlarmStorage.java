package com.aurozhkov.alarm.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.aurozhkov.alarm.beans.AlarmDays;
import com.aurozhkov.alarm.beans.AlarmMusic;
import com.aurozhkov.alarm.beans.AlarmTime;

public class AlarmStorage {

    private static final String ON = "on";
    private final static String PREF = "pref";

    public static AlarmDays getAlarmDays(Context context) {
        final SharedPreferences sp = getSharedPreferences(context);
        return AlarmDays.restoreFromSharedPreferences(sp);
    }

    public static void saveAlarmDays(Context context, AlarmDays alarmDays) {
        final SharedPreferences sp = getSharedPreferences(context);
        alarmDays.saveToSharedPreferences(sp);
    }

    public static AlarmTime getAlarmTime(Context context) {
        final SharedPreferences sp = getSharedPreferences(context);
        return AlarmTime.restoreFromSharedPreferences(sp);
    }

    public static void saveAlarmTime(Context context, AlarmTime time) {
        final SharedPreferences sp = getSharedPreferences(context);
        time.saveToSharedPreferences(sp);
    }

    public static boolean getAlarmOn(Context context) {
        final SharedPreferences sp = getSharedPreferences(context);
        return sp.getBoolean(ON, false);
    }

    public static void saveAlarmOn(Context context, boolean on) {
        final SharedPreferences sp = getSharedPreferences(context);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(ON, on);
        editor.commit();
    }

    public static AlarmMusic getAlarmMusic(Context context) {
        final SharedPreferences sp = getSharedPreferences(context);
        return AlarmMusic.restoreFromSharedPreferences(sp);
    }

    public static void saveAlarmMusic(Context context, AlarmMusic alarmMusic) {
        final SharedPreferences sp = getSharedPreferences(context);
        alarmMusic.saveToSharedPreferences(sp);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    private AlarmStorage() {

    }
}
