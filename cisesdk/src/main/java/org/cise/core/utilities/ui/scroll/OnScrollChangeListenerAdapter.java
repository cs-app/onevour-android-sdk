package org.cise.core.utilities.ui.scroll;
//
// Created by  on 2019-12-30.
//

import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class OnScrollChangeListenerAdapter implements View.OnScrollChangeListener {
    private final OnScrollChangeListenerCompat mOnScrollChangeListener;

    public OnScrollChangeListenerAdapter(OnScrollChangeListenerCompat onScrollChangeListener) {
        mOnScrollChangeListener = onScrollChangeListener;
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        mOnScrollChangeListener.onScrollChange(v, scrollX, scrollY, oldScrollX, oldScrollY);
    }

}
