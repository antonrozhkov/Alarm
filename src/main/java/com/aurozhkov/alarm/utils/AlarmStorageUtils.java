package com.aurozhkov.alarm.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.aurozhkov.alarm.R;
import com.aurozhkov.alarm.beans.AlarmDays;
import com.aurozhkov.alarm.beans.AlarmMusic;
import com.aurozhkov.alarm.beans.AlarmTime;

import java.util.BitSet;

public class AlarmStorageUtils {

    private static final String ON = "on";
    private final static String PREF = "pref";

    public static AlarmDays getAlarmDays(Context context) {
        final SharedPreferences sp = getSharedPreferences(context);
        AlarmDays alarmDays = new AlarmDays();
        alarmDays.restoreFromSharedPreferences(sp);
        return alarmDays;
    }

    public static void saveAlarmDays(Context context, AlarmDays alarmDays) {
        final SharedPreferences sp = getSharedPreferences(context);
        alarmDays.saveToSharedPreferences(sp);
    }

    public static AlarmTime getAlarmTime(Context context) {
        final SharedPreferences sp = getSharedPreferences(context);
        AlarmTime alarmTime = new AlarmTime();
        alarmTime.restoreFromSharedPreferences(sp);
        return alarmTime;
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
        AlarmMusic alarmMusic = new AlarmMusic();
        alarmMusic.restoreFromSharedPreferences(sp);
        return alarmMusic;
    }

    public static void saveAlarmMusic(Context context, AlarmMusic alarmMusic) {
        final SharedPreferences sp = getSharedPreferences(context);
        alarmMusic.saveToSharedPreferences(sp);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    private AlarmStorageUtils() {

    }
}
