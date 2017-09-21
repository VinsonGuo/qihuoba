package com.yjjr.yjfutures.widget.chart;

/**
 * Created by Administrator on 2016/2/1.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.utils.DoubleUtil;
import com.yjjr.yjfutures.utils.StringUtils;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class RealPriceMarkerView extends MarkerView {

    private final double mTick;
    private TextView tvContent;

    public RealPriceMarkerView(Context context, double tick) {
        super(context, R.layout.view_mp_real_price_marker);
        mTick = tick;
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.third_text_color));
        float value = e.getY();
        tvContent.setText(StringUtils.getStringByTick(value, mTick));
        super.refreshContent(e, highlight);
    }

}
