package com.yjjr.yjfutures.event;

/**
 * Created by dell on 2017/6/26.
 */

public class FastTakeOrderEvent {
    private boolean isOpened;

    public FastTakeOrderEvent(boolean isOpened) {
        this.isOpened = isOpened;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }
}
