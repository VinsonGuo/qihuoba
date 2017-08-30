package com.yjjr.yjfutures.event;

/**
 * 下单成功event
 * Created by dell on 2017/7/11.
 */

public class SendOrderEvent {
    private int delay;

    public SendOrderEvent() {
    }

    public SendOrderEvent(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "SendOrderEvent{" +
                "delay=" + delay +
                '}';
    }
}
