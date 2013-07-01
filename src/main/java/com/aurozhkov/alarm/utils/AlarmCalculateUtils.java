package com.aurozhkov.alarm.utils;

import java.util.BitSet;
import java.util.Calendar;

public class AlarmCalculateUtils {

    public static int getSecondsToNextAlarm(BitSet alarmDays, int[] time) {
        final int secondsFromStartWeekNow = getSecondsFromStartWeekNow();
        return -1;
    }

    private static int getSecondsFromStartWeekNow() {
        final Calendar cal = Calendar.getInstance();
        final int dayNow = getDayOfWeekMonday0Format(cal.get(Calendar.DAY_OF_WEEK));
        final int hoursNow = cal.get(Calendar.HOUR);
        final int minutesNow = cal.get(Calendar.MINUTE);
        final int secondsNow = cal.get(Calendar.SECOND);
        final int secondsFromStartDayNow = TimeUtils.hoursMinutesAndSecondsToSeconds(hoursNow, minutesNow, secondsNow);
        return TimeUtils.daysAndSecondsToSeconds(dayNow, secondsFromStartDayNow);
    }

    private static int getDayOfWeekMonday0Format(int daysNowCalendarFormat) {
        return daysNowCalendarFormat == 1 ? 6 : daysNowCalendarFormat - 1;
    }



    private AlarmCalculateUtils() {

    }
}
