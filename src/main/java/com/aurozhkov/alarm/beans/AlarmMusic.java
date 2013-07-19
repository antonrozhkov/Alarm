package com.aurozhkov.alarm.beans;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.aurozhkov.alarm.R;

public class AlarmMusic {

    private static final String PREF_KEY = AlarmMusic.class.getSimpleName();
    private static final long INVALID_FILE_MUSIC_ID = -1;

    private long mMusicFileId;

    public int getDefaultMusicFileId() {
        return R.raw.alarm;
    }

    public boolean hasMusicFile() {
        return mMusicFileId != INVALID_FILE_MUSIC_ID;
    }

    public long getMusicFileId() {
        return mMusicFileId;
    }

    public void saveToSharedPreferences(SharedPreferences sp) {
        final SharedPreferences.Editor editor = sp.edit();
        editor.putLong(PREF_KEY, mMusicFileId);
        editor.commit();
    }

    public void restoreFromSharedPreferences(SharedPreferences sp) {
        mMusicFileId = sp.getLong(PREF_KEY, INVALID_FILE_MUSIC_ID);
    }

    public void update(Uri fileMusicUri) {
        mMusicFileId = ContentUris.parseId(fileMusicUri);
    }

    public String getFileName(Context context) {
        if (hasMusicFile()) {
            final Uri uri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mMusicFileId);
            return getFilenameFromURI(context, uri);
        } else {
            return context.getResources().getResourceEntryName(getDefaultMusicFileId()) + ".mp3";
        }
    }

    private String getFilenameFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DISPLAY_NAME};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
