package com.yjjr.yjfutures.event;

/**
 * Created by dell on 2017/11/21.
 */

public class ChartTouchEvent {
    private boolean statues;

    public ChartTouchEvent(boolean statues) {
        this.statues = statues;
    }

    public boolean isStatues() {
        return statues;
    }

    public void setStatues(boolean statues) {
        this.statues = statues;
    }
}
