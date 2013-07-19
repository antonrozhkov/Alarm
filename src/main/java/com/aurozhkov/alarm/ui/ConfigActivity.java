package com.aurozhkov.alarm.ui;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.aurozhkov.alarm.R;
import com.aurozhkov.alarm.beans.AlarmDays;
import com.aurozhkov.alarm.beans.AlarmMusic;
import com.aurozhkov.alarm.beans.AlarmTime;
import com.aurozhkov.alarm.beans.TimeItem;
import com.aurozhkov.alarm.utils.AlarmStorageUtils;

public class ConfigActivity extends FragmentActivity implements View.OnClickListener {

    public static final String TAG = ConfigActivity.class.getSimpleName();
    public static final int REQUEST_MUSIC_CODE = 1001;

    private int mWidgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Intent mResultValue;
    private AlarmMusic mAlarmMusic;
    private AlarmTime mAlarmTime;
    private AlarmDays mAlarmDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkWidgetId();
        formResult();

        setContentView(R.layout.activity_config);

        initBeans();
        initViews();
    }

    private void formResult() {
        mResultValue = new Intent();
        mResultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetID);
        setResult(RESULT_CANCELED, mResultValue);
    }

    private void checkWidgetId() {
        final Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        if (extras != null) {
            mWidgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mWidgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    private void initBeans() {
        mAlarmMusic = AlarmStorageUtils.getAlarmMusic(this);
        mAlarmTime = AlarmStorageUtils.getAlarmTime(this);
        mAlarmDays = AlarmStorageUtils.getAlarmDays(this);
    }

    private void initViews() {
        initClickableViews();

        initTime();
        initDays();
        initMusic();
    }

    private void initClickableViews() {
        findViewById(R.id.ok).setOnClickListener(this);
        findViewById(R.id.time).setOnClickListener(this);
        findViewById(R.id.music).setOnClickListener(this);
    }

    private void initTime() {
        final TextView tv = (TextView) findViewById(R.id.time);
        tv.setText(mAlarmTime.getTimeString());
    }

    private void initDays() {
        int day = 0;
        ((CheckBox) findViewById(R.id.monday)).setChecked(mAlarmDays.isSelected(day++));
        ((CheckBox) findViewById(R.id.tuesday)).setChecked(mAlarmDays.isSelected(day++));
        ((CheckBox) findViewById(R.id.wednesday)).setChecked(mAlarmDays.isSelected(day++));
        ((CheckBox) findViewById(R.id.thursday)).setChecked(mAlarmDays.isSelected(day++));
        ((CheckBox) findViewById(R.id.friday)).setChecked(mAlarmDays.isSelected(day++));
        ((CheckBox) findViewById(R.id.saturday)).setChecked(mAlarmDays.isSelected(day++));
        ((CheckBox) findViewById(R.id.sunday)).setChecked(mAlarmDays.isSelected(day));
    }

    private void initMusic() {
        final TextView tv = (TextView) findViewById(R.id.music);
        tv.setText(mAlarmMusic.getFileName(this));
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
        saveBeans();
        updateWidget();
        setResult(RESULT_OK, mResultValue);
        finish();
    }

    private void saveBeans() {
        getAlarmsDays();
        AlarmStorageUtils.saveAlarmDays(this, mAlarmDays);
        AlarmStorageUtils.saveAlarmMusic(this, mAlarmMusic);
        AlarmStorageUtils.saveAlarmTime(this, mAlarmTime);
    }

    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        AlarmWidget.updateWidget(this, appWidgetManager, mWidgetID);
    }

    private void getAlarmsDays() {
        int day = 0;
        mAlarmDays.select(day++, ((CheckBox) findViewById(R.id.monday)).isChecked());
        mAlarmDays.select(day++, ((CheckBox) findViewById(R.id.tuesday)).isChecked());
        mAlarmDays.select(day++, ((CheckBox) findViewById(R.id.wednesday)).isChecked());
        mAlarmDays.select(day++, ((CheckBox) findViewById(R.id.thursday)).isChecked());
        mAlarmDays.select(day++, ((CheckBox) findViewById(R.id.friday)).isChecked());
        mAlarmDays.select(day++, ((CheckBox) findViewById(R.id.saturday)).isChecked());
        mAlarmDays.select(day, ((CheckBox) findViewById(R.id.sunday)).isChecked());
    }

    private void selectTime() {
        final TimePickerFragment tpf = TimePickerFragment.getInstance(mAlarmTime.getTime());
        tpf.setOnTimeSelectedListener(new TimePickerFragment.OnTimeSelectedListener() {
            @Override
            public void onTimeSelected(TimeItem time) {
                mAlarmTime.update(time);
                initTime();
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
            mAlarmMusic.update(data.getData());
            initMusic();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
