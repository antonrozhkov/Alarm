package com.aurozhkov.alarm.ui;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.aurozhkov.alarm.utils.TimeUtils;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static interface OnTimeSelectedListener {
        public void onTimeSelected(int[] time);
    }

    public static final String TIME_KEY = "time_key";
    private OnTimeSelectedListener mOnTimeSelectedListener;
    private int mMinutes;

    public static TimePickerFragment getInstance(int minutes) {
        final TimePickerFragment tpf = new TimePickerFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt(TIME_KEY, minutes);
        tpf.setArguments(bundle);
        return tpf;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int[] time = initFromArguments(savedInstanceState == null ? getArguments() : savedInstanceState);
        return new TimePickerDialog(getActivity(), this, time[0], time[1],
                DateFormat.is24HourFormat(getActivity()));
    }

    private int[] initFromArguments(Bundle bundle) {
        mMinutes = bundle.getInt(TIME_KEY, 0);
        return TimeUtils.hoursAndMinutesFromMinutes(mMinutes);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TIME_KEY, mMinutes);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (mOnTimeSelectedListener != null) {
            mOnTimeSelectedListener.onTimeSelected(new int[]{hourOfDay, minute});
        }
    }

    public void setOnTimeSelectedListener(OnTimeSelectedListener onTimeSelectedListener) {
        mOnTimeSelectedListener = onTimeSelectedListener;
    }
}