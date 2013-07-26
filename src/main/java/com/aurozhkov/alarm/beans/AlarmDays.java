package com.aurozhkov.alarm.beans;

import android.content.SharedPreferences;

import java.util.BitSet;

public class AlarmDays {

    private static final String PREF_KEY = AlarmDays.class.getSimpleName();

    private BitSet mAlarmDays = new BitSet(7);

    public void select(int day, boolean isSelected) {
        mAlarmDays.set(day, isSelected);
    }

    public boolean isSelected(int day) {
        return mAlarmDays.get(day);
    }

    public void saveToSharedPreferences(SharedPreferences sp) {
        final SharedPreferences.Editor editor = sp.edit();
        final int daysCode = bitSetToInt(mAlarmDays);
        editor.putInt(PREF_KEY, daysCode);
        editor.commit();
    }

    public boolean isEmpty() {
        return mAlarmDays.isEmpty();
    }

    public static AlarmDays restoreFromSharedPreferences(SharedPreferences sp) {
        final AlarmDays alarmDays = new AlarmDays();
        final int daysCode = sp.getInt(PREF_KEY, 0);
        alarmDays.mAlarmDays = intToBitSet(daysCode);
        return alarmDays;
    }

    //1011001 -> 6320
    private static int bitSetToInt(BitSet value) {
        int result = 0;
        int currentDay = 0;
        for (int i = 0; i < 7; i++) {
            if (value.get(i)) {
                result += getCurrentFactor(currentDay++) * i;
            }
        }
        return result;
    }

    private static int getCurrentFactor(int currentDay) {
        final int factor = 10;
        int currentFactor = 1;
        for (int j = 0; j < currentDay; j++) {
            currentFactor *= factor;
        }
        return currentFactor;
    }

    //541 -> 0100110
    private static BitSet intToBitSet(int value) {
        final BitSet result = new BitSet(7);
        int currentValue = value;
        while (currentValue != 0) {
            result.set(currentValue % 10, true);
            currentValue /= 10;
        }
        return result;
    }
}
