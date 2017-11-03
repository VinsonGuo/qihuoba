package com.yjjr.yjfutures.model;

/**
 * Created by dell on 2017/11/3.
 */

public class MarketDepth {

    /**
     * position : 1
     * operation : 1
     * side : 0
     * price : 54.83
     * size : 50
     */

    private int position;
    private int operation;
    private int side;
    private double price;
    private int size;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    /**
     * 0 卖 1买
     * @return
     */
    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
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
        return "MarketDepth{" +
                "position=" + position +
                ", operation=" + operation +
                ", side=" + side +
                ", price=" + price +
                ", size=" + size +
                '}';
    }
}
