package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/11/23.
 */

public class HistoricalTicks {

    /**
     * time : 20171123 11:24:37
     * mask : 0
     * price : 57.849998
     * size : 7
     */

    private String time;
    private int mask;
    private double price;
    private int size;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
