package com.yjjr.yjfutures.model;

/**
 * Created by dell on 2017/7/14.
 */

public class FastTakeOrderConfig {
    private int qty;
    private double stopLose;
    private double stopWin;

    public FastTakeOrderConfig() {
    }

    public FastTakeOrderConfig(int qty, double stopLose, double stopWin) {
        this.qty = qty;
        this.stopLose = stopLose;
        this.stopWin = stopWin;
    }

    @Override
    public String toString() {
        return "FastTakeOrderConfig{" +
                ", qty=" + qty +
                ", stopLose=" + stopLose +
                ", stopWin=" + stopWin +
                '}';
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getStopLose() {
        return stopLose;
    }

    public void setStopLose(double stopLose) {
        this.stopLose = stopLose;
    }

    public double getStopWin() {
        return stopWin;
    }

    public void setStopWin(double stopWin) {
        this.stopWin = stopWin;
    }
}
