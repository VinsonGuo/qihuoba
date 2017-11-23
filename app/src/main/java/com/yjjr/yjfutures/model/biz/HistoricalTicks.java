package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/11/23.
 */

public class HistoricalTicks {

    private long time;
    private int buySell;
    private double price;
    private int size;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getBuySell() {
        return buySell;
    }

    public void setBuySell(int buySell) {
        this.buySell = buySell;
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

    @Override
    public String toString() {
        return "HistoricalTicks{" +
                "time=" + time +
                ", buySell=" + buySell +
                ", price=" + price +
                ", size=" + size +
                '}';
    }
}
