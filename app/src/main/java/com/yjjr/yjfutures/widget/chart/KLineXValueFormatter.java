package com.yjjr.yjfutures.widget.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.yjjr.yjfutures.model.HisData;
import com.yjjr.yjfutures.utils.DateUtils;
import com.yjjr.yjfutures.utils.http.HttpConfig;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by dell on 2017/10/28.
 */

public class KLineXValueFormatter implements IAxisValueFormatter {
    private String mType;
    private List<HisData> mData;

    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());

    public KLineXValueFormatter(String type, List<HisData> hisDatas) {
        mType = type;
        mData = hisDatas;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (mData != null && value < mData.size() && value >= 0) {
            DateTime dateTime = new DateTime(mData.get((int) value).getsDate());
            if (mType.equals(HttpConfig.DAY) || mType.equals(HttpConfig.WEEK)) {
                return DateUtils.formatDataOnly(dateTime.getMillis());
            } else if (mType.equals(HttpConfig.MONTH)) {
                return mFormat.format(dateTime.getMillis());
            }
            return DateUtils.formatTime(dateTime.getMillis());
        }
        return "";
    }
}
