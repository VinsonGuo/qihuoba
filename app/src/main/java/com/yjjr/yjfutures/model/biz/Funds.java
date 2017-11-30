package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/7/24.
 */

public class Funds {

    private double availableFunds = Double.NaN;
    private double frozenMargin = Double.NaN;
    private double netAssets = Double.NaN;

    public double getAvailableFunds() {
        return availableFunds;
    }

    public void setAvailableFunds(double availableFunds) {
        this.availableFunds = availableFunds;
    }

    public double getFrozenMargin() {
        return frozenMargin;
    }

    public void setFrozenMargin(double frozenMargin) {
        this.frozenMargin = frozenMargin;
    }

    public double getNetAssets() {
        return netAssets;
    }

    public void setNetAssets(double netAssets) {
        this.netAssets = netAssets;
    }

    @Override
    public String toString() {
        return "Funds{" +
                "availableFunds=" + availableFunds +
                ", frozenMargin=" + frozenMargin +
                ", netAssets=" + netAssets +
                '}';
    }
}
