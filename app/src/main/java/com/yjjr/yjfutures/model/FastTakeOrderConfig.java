package com.yjjr.yjfutures.model;

/**
 * Created by dell on 2017/7/14.
 */

public class FastTakeOrderConfig {
    private int qty;
    private double stopLose;
    private double stopWin;
    private double fee;
    private double marginYJ;

    public FastTakeOrderConfig() {
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

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getMarginYJ() {
        return marginYJ;
    }

    public void setMarginYJ(double marginYJ) {
        this.marginYJ = marginYJ;
    }

    @Override
    public String toString() {
        return "FastTakeOrderConfig{" +
                "qty=" + qty +
                ", stopLose=" + stopLose +
                ", stopWin=" + stopWin +
                ", fee=" + fee +
                ", marginYJ=" + marginYJ +
                '}';
    }
}
