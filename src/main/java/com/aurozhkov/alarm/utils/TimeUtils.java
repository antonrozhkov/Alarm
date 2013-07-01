package com.aurozhkov.alarm.utils;

public class TimeUtils {

    public static final int COUNT_MINUTES_IN_HOUR = 60;
    public static final int COUNT_SECONDS_IN_MINUTE = 60;
    public static final int COUNT_HOURS_IN_DAY = 24;

    public static int hoursAndMinutesToMinutes(int hours, int minutes) {
        return hours * COUNT_MINUTES_IN_HOUR + minutes;
    }

    public static int[] hoursAndMinutesFromMinutes(int minutes) {
        final int[] time = new int[2];
        time[0] = minutes / COUNT_MINUTES_IN_HOUR;
        time[1] = minutes % COUNT_MINUTES_IN_HOUR;
        return time;
    }

    public static String timeStringFromTime(int[] time) {
        return formatTimePart(time[0]) + ":" + formatTimePart(time[1]);
    }

    private static String formatTimePart(int part) {
        if (part < 10) {
            return "0" + part;
        } else {
            return String.valueOf(part);
        }
    }

    public static int minutesFromTimeString(String time) {
        final String[] timeParts = time.split(":");
        final int hour = Integer.parseInt(timeParts[0]);
        final int minute = Integer.parseInt(timeParts[1]);
        return hoursAndMinutesToMinutes(hour, minute);
    }

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
