package com.yjjr.yjfutures.widget.chart;

/**
 * Created by Administrator on 2016/2/1.
 */

import android.content.Context;
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

    private int dataSet1Color = getResources().getColor(R.color.main_color_red);
    private int dataSet2Color = getResources().getColor(R.color.main_color);


    public RealPriceMarkerView(Context context, double tick) {
        super(context, R.layout.view_mp_real_price_marker);
        mTick = tick;
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int dataIndex = highlight.getDataSetIndex();
        int color = dataIndex == 0 ? dataSet1Color : dataSet2Color;
        tvContent.setBackgroundColor(color);
        float value = e.getY();
        tvContent.setText(StringUtils.getStringByTick(value, mTick));
        super.refreshContent(e, highlight);
    }

}
