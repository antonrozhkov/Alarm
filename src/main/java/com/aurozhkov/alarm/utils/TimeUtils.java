package com.aurozhkov.alarm.utils;

public class TimeUtils {

    public static final int COUNT_MINUTES_IN_HOUR = 60;
    public static final int COUNT_SECONDS_IN_MINUTE = 60;
    public static final int COUNT_HOURS_IN_DAY = 24;

    public static int hoursMinutesAndSecondsToSeconds(int hours, int minutes, int seconds) {
        return hoursToSeconds(hours) + minutesToSeconds(minutes) + seconds;
    }

    public static int hoursToSeconds(int hours) {
        return hours * COUNT_MINUTES_IN_HOUR * COUNT_SECONDS_IN_MINUTE;
    }

    public static int minutesToSeconds(int minutes) {
        return minutes * COUNT_SECONDS_IN_MINUTE;
    }

    public static int daysToSeconds(int days) {
        return days * COUNT_HOURS_IN_DAY * COUNT_MINUTES_IN_HOUR * COUNT_SECONDS_IN_MINUTE;
    }

    public static int daysAndSecondsToSeconds(int days, int seconds) {
        return daysToSeconds(days) + seconds;
    }
}
