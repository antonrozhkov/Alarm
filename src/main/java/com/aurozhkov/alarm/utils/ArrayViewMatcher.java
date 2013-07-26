package com.aurozhkov.alarm.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;

public class ArrayViewMatcher {

    private Resources mResources;
    private String[] viewsNames;
    private String packageName;

    public ArrayViewMatcher(Context context, int arrayId) {
        mResources = context.getResources();
        viewsNames = mResources.getStringArray(arrayId);
        packageName = context.getPackageName();
    }

    public int getViewId(int position) {
        return mResources.getIdentifier(viewsNames[position], "id", packageName);
    }

    public View getView(Activity activity, int position) {
        final int viewId = getViewId(position);
        return activity.findViewById(viewId);
    }

    public View getView(View view, int position) {
        final int viewId = getViewId(position);
        return view.findViewById(viewId);
    }

    public int getLength() {
        return viewsNames.length;
    }
}
