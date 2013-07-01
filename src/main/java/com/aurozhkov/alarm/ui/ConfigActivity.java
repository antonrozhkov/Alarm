package com.aurozhkov.alarm.ui;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.aurozhkov.alarm.R;
import com.aurozhkov.alarm.beans.AlarmDays;
import com.aurozhkov.alarm.beans.AlarmTime;
import com.aurozhkov.alarm.utils.AlarmStorageUtils;
import com.aurozhkov.alarm.utils.TimeUtils;

import java.util.BitSet;

public class ConfigActivity extends FragmentActivity implements View.OnClickListener {

    public static final String TAG = ConfigActivity.class.getSimpleName();
    public static final int REQUEST_MUSIC_CODE = 1001;

    private int mWidgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Intent mResultValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        if (extras != null) {
            mWidgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mWidgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        mResultValue = new Intent();
        mResultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetID);

        setResult(RESULT_CANCELED, mResultValue);

        setContentView(R.layout.activity_config);

        initViews();
    }

    private void initViews() {
        findViewById(R.id.ok).setOnClickListener(this);
        findViewById(R.id.time).setOnClickListener(this);
        findViewById(R.id.music).setOnClickListener(this);

        initTime();
        initDays();
        initMusic();
    }

    private void initTime() {
        final AlarmTime alarmTime = AlarmStorageUtils.getAlarmTime(this);
        final TextView tv = (TextView) findViewById(R.id.time);
        tv.setText(alarmTime.toString());
    }

    private void initDays() {
        final AlarmDays alarmDays = AlarmStorageUtils.getAlarmDays(this);
        int day = 0;
        ((CheckBox) findViewById(R.id.monday)).setChecked(alarmDays.isSelected(day++));
        ((CheckBox) findViewById(R.id.tuesday)).setChecked(alarmDays.isSelected(day++));
        ((CheckBox) findViewById(R.id.wednesday)).setChecked(alarmDays.isSelected(day++));
        ((CheckBox) findViewById(R.id.thursday)).setChecked(alarmDays.isSelected(day++));
        ((CheckBox) findViewById(R.id.friday)).setChecked(alarmDays.isSelected(day++));
        ((CheckBox) findViewById(R.id.saturday)).setChecked(alarmDays.isSelected(day++));
        ((CheckBox) findViewById(R.id.sunday)).setChecked(alarmDays.isSelected(day));
    }

    private void initMusic() {
        final long musicId = AlarmStorageUtils.getAlarmMusicId(this);
        if (musicId != -1) {
            final Uri uri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicId);
            final TextView tv = (TextView) findViewById(R.id.music);
            tv.setText(getFilenameFromURI(uri));
        }
    }

    private String getFilenameFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DISPLAY_NAME};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                saveResult();
                break;
            case R.id.time:
                selectTime();
                break;
            case R.id.music:
                selectMusic();
                break;
        }
    }

    private void saveResult() {
        final AlarmDays alarmDays = getAlarmsDays();
        AlarmStorageUtils.saveAlarmDays(this, alarmDays);

        final AlarmTime alarmTime = getAlarmTime();
        AlarmStorageUtils.setAlarmTime(this, alarmTime);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        MyWidget.updateWidget(this, appWidgetManager, mWidgetID);

        setResult(RESULT_OK, mResultValue);
        finish();
    }

    private void selectTime() {
        final TextView tvTime = (TextView) findViewById(R.id.time);
        final String time = tvTime.getText().toString();
        final TimePickerFragment tpf = TimePickerFragment.getInstance(TimeUtils.minutesFromTimeString(time));
        tpf.setOnTimeSelectedListener(new TimePickerFragment.OnTimeSelectedListener() {
            @Override
            public void onTimeSelected(int[] time) {
                tvTime.setText(TimeUtils.timeStringFromTime(time));
            }
        });
        tpf.show(getSupportFragmentManager(), "Time");
    }

    private void selectMusic() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_music)), REQUEST_MUSIC_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MUSIC_CODE && resultCode == Activity.RESULT_OK) {
            AlarmStorageUtils.setAlarmMusicId(this, ContentUris.parseId(data.getData()));
            initMusic();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private AlarmDays getAlarmsDays() {
        final AlarmDays alarmDays = new AlarmDays();
        int day = 0;
        alarmDays.select(day++, ((CheckBox) findViewById(R.id.monday)).isChecked());
        alarmDays.select(day++, ((CheckBox) findViewById(R.id.tuesday)).isChecked());
        alarmDays.select(day++, ((CheckBox) findViewById(R.id.wednesday)).isChecked());
        alarmDays.select(day++, ((CheckBox) findViewById(R.id.thursday)).isChecked());
        alarmDays.select(day++, ((CheckBox) findViewById(R.id.friday)).isChecked());
        alarmDays.select(day++, ((CheckBox) findViewById(R.id.saturday)).isChecked());
        alarmDays.select(day, ((CheckBox) findViewById(R.id.sunday)).isChecked());
        return alarmDays;
    }

    private AlarmTime getAlarmTime() {
        final TextView tvTime = (TextView) findViewById(R.id.time);
        final String time = tvTime.getText().toString();
        AlarmTime alarmTime = new AlarmTime();
        alarmTime.fromString(time);
        return alarmTime;
    }
}
