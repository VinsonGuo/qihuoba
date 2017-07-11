package com.yjjr.yjfutures.model;

/**
 * Created by dell on 2017/7/11.
 */

public class Trade {

    /**
     * tradetime : 2017/7/11 8:48:46
     * price : 2.926
     * buysell : S
     * size : 1
     */

    private String tradetime;
    private double price;
    private String buysell;
    private int size;

    public String getTradetime() {
        return tradetime;
    }

    public void setTradetime(String tradetime) {
        this.tradetime = tradetime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBuysell() {
        return buysell;
    }

    public void setBuysell(String buysell) {
        this.buysell = buysell;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "tradetime='" + tradetime + '\'' +
                ", price=" + price +
                ", buysell='" + buysell + '\'' +
                ", size=" + size +
                '}';
    }
}
