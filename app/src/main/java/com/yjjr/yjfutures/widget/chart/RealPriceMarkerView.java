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

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class RealPriceMarkerView extends MarkerView {

    private TextView tvContent;

    private int dataSet1Color = getResources().getColor(R.color.main_color_red);
    private int dataSet2Color = getResources().getColor(R.color.main_color);
    private int digital;


    public RealPriceMarkerView(Context context, int digital) {
        super(context, R.layout.view_mp_real_price_marker);
        tvContent = (TextView) findViewById(R.id.tvContent);
        this.digital = digital;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int dataIndex = highlight.getDataSetIndex();
        int color = dataIndex == 0 ? dataSet1Color : dataSet2Color;
        tvContent.setBackgroundColor(color);
        float value = e.getY();
        String parsedValue = null;
        if (digital > 0)
            parsedValue = DoubleUtil.formatDecimal((double) value, digital);
        tvContent.setText(parsedValue);
        super.refreshContent(e, highlight);
    }

}
