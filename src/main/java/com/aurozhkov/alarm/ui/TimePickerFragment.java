package com.aurozhkov.alarm.ui;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.aurozhkov.alarm.beans.TimeItem;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static interface OnTimeSelectedListener {
        public void onTimeSelected(TimeItem time);
    }

    public static final String TIME_KEY = "time_key";
    private OnTimeSelectedListener mOnTimeSelectedListener;
    private TimeItem mTime;

    public static TimePickerFragment getInstance(TimeItem time) {
        final TimePickerFragment tpf = new TimePickerFragment();
        final Bundle bundle = new Bundle();
        bundle.putParcelable(TIME_KEY, time);
        tpf.setArguments(bundle);
        return tpf;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initFromArguments(savedInstanceState == null ? getArguments() : savedInstanceState);
        return new TimePickerDialog(getActivity(), this, mTime.hours, mTime.minutes,
                DateFormat.is24HourFormat(getActivity()));
    }

    private void initFromArguments(Bundle bundle) {
        mTime = bundle.getParcelable(TIME_KEY);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TIME_KEY, mTime);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (mOnTimeSelectedListener != null) {
            mOnTimeSelectedListener.onTimeSelected(new TimeItem(hourOfDay, minute));
        }
    }

    public void setOnTimeSelectedListener(OnTimeSelectedListener onTimeSelectedListener) {
        mOnTimeSelectedListener = onTimeSelectedListener;
    }
}