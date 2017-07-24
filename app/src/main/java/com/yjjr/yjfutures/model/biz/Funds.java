package com.yjjr.yjfutures.model.biz;

/**
 * Created by dell on 2017/7/24.
 */

public class Funds {

    /**
     * availableFunds : 1369701704
     * frozenMargin : 1.369701704E7
     * netAssets : 2.739413408E7
     */

    private double availableFunds;
    private double frozenMargin;
    private double netAssets;

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
