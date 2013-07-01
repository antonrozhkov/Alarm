package com.aurozhkov.alarm.beans;


import android.content.SharedPreferences;

public class AlarmTime {

    private static final String PREF_KEY = AlarmTime.class.getSimpleName();

    private int minutes;
    private int hours;

    public AlarmTime() {

    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getHours() {
        return hours;
    }

    public void saveToSharedPreferences(SharedPreferences sp) {
        final SharedPreferences.Editor editor = sp.edit();
        final int timeCode = hoursAndMinutesToInt();
        editor.putInt(PREF_KEY, timeCode);
        editor.commit();
    }

    private int hoursAndMinutesToInt() {
        return hours * 100 + minutes;
    }

    public void restoreFromSharedPreferences(SharedPreferences sp) {
        final int timeCode = sp.getInt(PREF_KEY, 0);
        hoursAndMinutesFromInt(timeCode);
    }

    private void hoursAndMinutesFromInt(int value) {
        minutes = value % 100;
        hours = value / 100;
    }

    @Override
    public String toString() {
        return formatTimePart(hours) + ":" + formatTimePart(minutes);
    }

    private static String formatTimePart(int part) {
        if (part < 10) {
            return "0" + part;
        } else {
            return String.valueOf(part);
        }
    }

    public void fromString(String time) {
        final String[] timeParts = time.split(":");
        hours = Integer.parseInt(timeParts[0]);
        minutes = Integer.parseInt(timeParts[1]);
    }
}
