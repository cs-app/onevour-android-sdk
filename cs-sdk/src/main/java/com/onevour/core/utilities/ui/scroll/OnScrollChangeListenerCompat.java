package com.onevour.core.utilities.ui.scroll;
//
// Created by  on 2019-12-30.
//

import android.view.View;

public interface OnScrollChangeListenerCompat {

    void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
}
