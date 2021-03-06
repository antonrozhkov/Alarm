package com.aurozhkov.alarm.ui;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ContentUris;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.aurozhkov.alarm.R;
import com.aurozhkov.alarm.beans.AlarmMusic;
import com.aurozhkov.alarm.app.AlarmStorage;

import java.io.IOException;

public class AlarmActivity extends Activity implements View.OnClickListener {

    public static final String TAG = AlarmActivity.class.getSimpleName();

    private MediaPlayer mediaPlayer;
    private PowerManager.WakeLock wakeLock;
    private KeyguardManager.KeyguardLock keyguardLock;
    private boolean isScreenWasOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_activity_layout);

        initManagers();
        initViews();
        initScreen();

        startMusic();
    }

    private void initManagers() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "WakeLock");

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        keyguardLock = keyguardManager.newKeyguardLock("KeyguardLock");
    }

    private void initViews() {
        findViewById(R.id.ok).setOnClickListener(this);
    }

    private void initScreen() {
        isScreenWasOff = isScreenWasOff();
        if (isScreenWasOff) {
            wakeDevice();
        }
    }

    private boolean isScreenWasOff() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        return !pm.isScreenOn();
    }

    private void wakeDevice() {
        wakeLock.acquire();
        keyguardLock.disableKeyguard();
    }

    private void startMusic()  {
        final AlarmMusic alarmMusic = AlarmStorage.getAlarmMusic(this);
        if (alarmMusic.hasMusicFile()) {
            prepareWithMusicFile(alarmMusic);
        } else {
            preparePlayerWithDefaultMusic(alarmMusic);
        }
        mediaPlayer.start();
    }

    private void prepareWithMusicFile(AlarmMusic alarmMusic) {
        final Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, alarmMusic.getMusicFileId());
        try {
            preparePlayer(uri);
        } catch (IOException e) {
            preparePlayerWithDefaultMusic(alarmMusic);
        }
    }

    private void preparePlayer(Uri uri) throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(this, uri);
        mediaPlayer.prepare();
    }

    private void preparePlayerWithDefaultMusic(AlarmMusic alarmMusic) {
        mediaPlayer = MediaPlayer.create(this, alarmMusic.getDefaultMusicFileId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMP();
    }

    private void sleepDevice() {
        wakeLock.release();
        keyguardLock.reenableKeyguard();
    }

    private void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (isScreenWasOff) {
            sleepDevice();
        }
        finish();
    }
}
