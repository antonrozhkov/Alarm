package com.aurozhkov.alarm.beans;


import android.content.SharedPreferences;

public class AlarmTime {

    private static final String PREF_KEY = AlarmTime.class.getSimpleName();

    private TimeItem mTime;

    public TimeItem getTime() {
        return mTime;
    }

    public void saveToSharedPreferences(SharedPreferences sp) {
        final SharedPreferences.Editor editor = sp.edit();
        editor.putInt(PREF_KEY, mTime.toInt());
        editor.commit();
    }

    public static AlarmTime restoreFromSharedPreferences(SharedPreferences sp) {
        final AlarmTime alarmTime = new AlarmTime();
        final int timeCode = sp.getInt(PREF_KEY, 0);
        alarmTime.mTime = TimeItem.fromInt(timeCode);
        return alarmTime;
    }

    public String getTimeString() {
        return String.format("%s:%s",formatTimePart(mTime.hours),formatTimePart(mTime.minutes));
    }

    private String formatTimePart(int part) {
        if (part < 10) {
            return "0" + part;
        } else {
            return String.valueOf(part);
        }
    }

    public void update(TimeItem time) {
        mTime = time;
    }

}
