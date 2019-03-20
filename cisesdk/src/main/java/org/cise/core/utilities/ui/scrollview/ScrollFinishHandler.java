package org.cise.core.utilities.ui.scrollview;

import android.view.MotionEvent;
import android.view.View;

public class ScrollFinishHandler implements View.OnTouchListener {

    private static final int SCROLL_TASK_INTERVAL = 100;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
/*
    private Runnable mScrollerTask;
    private int mInitialPosition = 0;

    public ScrollFinishHandler() {
        mScrollerTask = new Runnable() {

            public void run() {

                int newPosition = getScrollY();
                if (mInitialPosition - newPosition == 0) {//has stopped
                    onScrollStopped(); // Implement this on your main ScrollView class
                } else {
                    mInitialPosition = getScrollY();
                    ExpandingLinearLayout.this.postDelayed(mScrollerTask, SCROLL_TASK_INTERVAL);
                }
            }
        };
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {

            startScrollerTask();
        } else {
            stopScrollerTask();
        }

        return false;
    }
    */


}
