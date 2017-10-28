package com.yjjr.yjfutures.widget.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.yjjr.yjfutures.utils.StringUtils;

/**
 * Created by dell on 2017/10/28.
 */

public class YValueFormatter implements IAxisValueFormatter {
    private double mTick;

    public YValueFormatter(double tick) {
        mTick = tick;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return StringUtils.getStringByTick(value, mTick);
    }
}
