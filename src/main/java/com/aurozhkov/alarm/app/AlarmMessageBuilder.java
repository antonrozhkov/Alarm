package com.aurozhkov.alarm.app;

import android.content.Context;
import android.text.TextUtils;

import com.aurozhkov.alarm.R;
import com.aurozhkov.alarm.utils.TimeUtils;

public class AlarmMessageBuilder {

    private Context mContext;
    private int mSeconds;

    public AlarmMessageBuilder(Context mContext, int seconds) {
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
        messageBody += calculateAndFormPartMessage(TimeUtils.daysToSeconds(1), R.string.days_alias);
        messageBody += calculateAndFormPartMessage(TimeUtils.hoursToSeconds(1), R.string.hours_alias);
        messageBody += calculateAndFormPartMessage(TimeUtils.minutesToSeconds(1), R.string.minutes_alias);
        if (TextUtils.isEmpty(messageBody)) {
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
