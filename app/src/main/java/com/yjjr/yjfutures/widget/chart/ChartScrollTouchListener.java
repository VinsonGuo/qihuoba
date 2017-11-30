package com.yjjr.yjfutures.widget.chart;

import android.view.MotionEvent;
import android.view.View;

import com.yjjr.yjfutures.event.ChartTouchEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by dell on 2017/11/21.
 */

public class ChartScrollTouchListener implements View.OnTouchListener {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                EventBus.getDefault().post(new ChartTouchEvent(true));
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                EventBus.getDefault().post(new ChartTouchEvent(false));
                break;
            }
        }

        return false;
    }
}
