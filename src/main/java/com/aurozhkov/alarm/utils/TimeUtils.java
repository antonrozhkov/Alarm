package com.aurozhkov.alarm.utils;

import android.content.Context;
import android.text.TextUtils;

import com.aurozhkov.alarm.R;

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

    public static String secondsToString(Context context, int seconds) {
        return new AlarmMessageBuilder(context, seconds).formMessage();
    }

    private static class AlarmMessageBuilder {

        private Context mContext;
        private int mSeconds;

        private AlarmMessageBuilder(Context mContext, int seconds) {
            this.mContext = mContext;
            this.mSeconds = seconds;
        }

        public String formMessage() {
            final String messageHeader = mContext.getString(R.string.alarm_after);
            final String messageBody = formMessageBody();
            return messageHeader + messageBody;
        }

        private String formMessageBody() {
            String messageBody = "";
            messageBody += calculateAndFormPartMessage(daysToSeconds(1), R.string.days_alias);
            messageBody += calculateAndFormPartMessage(hoursToSeconds(1), R.string.hours_alias);
            messageBody += calculateAndFormPartMessage(minutesToSeconds(1), R.string.minutes_alias);
            if(TextUtils.isEmpty(messageBody)) {
                messageBody = calculateAndFormPartMessage(1, R.string.seconds_alias);
            }
            return messageBody;
        }

        private String calculateAndFormPartMessage(int interval, int partResourceId) {
            final int timePart = mSeconds / interval;
            final String timePartString = mContext.getString(partResourceId);
            mSeconds = mSeconds % interval;
            return formPartMessage(timePart, timePartString);
        }

        private String formPartMessage(int timeValue, String timeAlias) {
            return timeValue == 0 ? "" : String.format(" %d %s", timeValue, timeAlias);
        }
    }


}
