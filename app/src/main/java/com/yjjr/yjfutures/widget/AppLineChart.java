package com.yjjr.yjfutures.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

/**
 * Created by dell on 2017/6/22.
 */

public class AppLineChart extends LineChart {

    public AppLineChart(Context context) {
        this(context, null);
    }

    public AppLineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void drawMarkers(Canvas canvas) {
        if (mMarker == null || !isDrawMarkersEnabled() || !valuesToHighlight())
            return;

        for (int i = 0; i < mIndicesToHighlight.length; i++) {

            Highlight highlight = mIndicesToHighlight[i];

            IDataSet set = mData.getDataSetByIndex(highlight.getDataSetIndex());

            Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);
            int entryIndex = set.getEntryIndex(e);

            // make sure entry not null
            if (e == null || entryIndex > set.getEntryCount() * mAnimator.getPhaseX())
                continue;

            float[] pos = getMarkerPosition(highlight);

            // check bounds
            if (!mViewPortHandler.isInBounds(pos[0], pos[1]))
                continue;

            // callbacks to update the content
            mMarker.refreshContent(e, highlight);

            // draw the marker
            if (mMarker instanceof RealPriceMarkerView) {
                RealPriceMarkerView realPriceMarkerView = (RealPriceMarkerView) mMarker;
                int width = realPriceMarkerView.getMeasuredWidth();
                mMarker.draw(canvas, getMeasuredWidth() - width, pos[1]);
            } else {
                mMarker.draw(canvas, pos[0], pos[1]);
            }
        }
    }
}
