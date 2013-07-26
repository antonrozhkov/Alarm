package com.aurozhkov.alarm.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeItem implements Parcelable {
    public final int minutes;
    public final int hours;

    public TimeItem(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int toInt() {
        return hours * 100 + minutes;
    }

    public static TimeItem fromInt(int value) {
        final int minutes = value % 100;
        final int hours = value / 100;
        return new TimeItem(hours, minutes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(toInt());
    }

    public static final Creator<TimeItem> CREATOR = new Creator<TimeItem>() {
        public TimeItem createFromParcel(Parcel in) {
            return fromInt(in.readInt());
        }

        public TimeItem[] newArray(int size) {
            return new TimeItem[size];
        }
    };
}
