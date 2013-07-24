package com.aurozhkov.alarm.utils;

import com.aurozhkov.alarm.beans.AlarmDays;
import com.aurozhkov.alarm.beans.AlarmTime;

import java.util.Calendar;

public class AlarmCalculateUtils {

    public static int getSecondsToNextAlarm(AlarmDays alarmDays, AlarmTime alarmTime) {
        if (alarmDays.isEmpty()) {
            return -1;
        }
        final int secondsFromStartWeekNow = getSecondsFromStartWeekNow();
        final int secondsFromStartDayAlarm = TimeUtils.hoursMinutesAndSecondsToSeconds(alarmTime.getTime().hours, alarmTime.getTime().minutes, 0);
        return getSecondsToNextAlarm(secondsFromStartWeekNow, secondsFromStartDayAlarm, alarmDays);
    }

    private static int getSecondsFromStartWeekNow() {
        final Calendar cal = Calendar.getInstance();
        final int dayNow = getDayOfWeekMonday0Format(cal.get(Calendar.DAY_OF_WEEK));
        final int hoursNow = cal.get(Calendar.HOUR_OF_DAY);
        final int minutesNow = cal.get(Calendar.MINUTE);
        final int secondsNow = cal.get(Calendar.SECOND);
        final int secondsFromStartDayNow = TimeUtils.hoursMinutesAndSecondsToSeconds(hoursNow, minutesNow, secondsNow);
        return TimeUtils.daysAndSecondsToSeconds(dayNow, secondsFromStartDayNow);
    }

    private static int getDayOfWeekMonday0Format(int daysNowCalendarFormat) {
        return daysNowCalendarFormat == 1 ? 6 : daysNowCalendarFormat - 2;
    }

    private static int getSecondsToNextAlarm(int secondsFromStartWeekNow, int secondsFromStartDayAlarm, AlarmDays alarmDays) {
        int seconds = 0;
        int day = 1;
        boolean nextWeek = false;
        while (seconds < secondsFromStartWeekNow) {
            seconds = addDayIfNecessary(secondsFromStartDayAlarm, alarmDays, seconds, day);
            seconds = addWeekIfNecessary(seconds, nextWeek);
            if (day == 6) {
                day = 0;
                nextWeek = true;
            } else {
                day++;
            }
        }
        return seconds - secondsFromStartWeekNow;
    }

    private static int addDayIfNecessary(int secondsFromStartDayAlarm, AlarmDays alarmDays, int seconds, int day) {
        if (alarmDays.isSelected(day)) {
            seconds = secondsFromStartDayAlarm + TimeUtils.daysToSeconds(day);
        }
        return seconds;
    }

    private static int addWeekIfNecessary(int seconds, boolean nextWeek) {
        if (nextWeek) {
            seconds += TimeUtils.daysToSeconds(7);
        }
        return seconds;
    }

    private AlarmCalculateUtils() {

    }
}
