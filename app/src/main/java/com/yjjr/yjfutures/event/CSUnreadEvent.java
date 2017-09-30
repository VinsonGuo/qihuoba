package com.yjjr.yjfutures.event;

/**
 * 客服的未读消息
 * Created by dell on 2017/9/30.
 */

public class CSUnreadEvent {
    private int count;

    public CSUnreadEvent(int unreadMsgCount) {
        this.count = unreadMsgCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "CSUnreadEvent{" +
                "count=" + count +
                '}';
    }
}
