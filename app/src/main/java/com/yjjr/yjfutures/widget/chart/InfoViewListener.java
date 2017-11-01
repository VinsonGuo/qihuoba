package com.yjjr.yjfutures.widget.chart;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.yjjr.yjfutures.model.HisData;
import com.yjjr.yjfutures.model.Quote;
import com.yjjr.yjfutures.utils.DisplayUtils;

import java.util.List;

/**
 * Created by dell on 2017/9/28.
 */

public class InfoViewListener implements OnChartValueSelectedListener {

    private List<HisData> mList;
    private Quote mQuote;
    private ChartInfoView mInfoView;
    private int mWidth;
    /**
     * 需要highlight联动的chart
     */
    private Chart mOtherChart;

    public InfoViewListener(Context context, Quote quote, List<HisData> list, ChartInfoView infoView) {
        mWidth = DisplayUtils.getWidthHeight(context)[0];
        mQuote = quote;
        mList = list;
        mInfoView = infoView;
    }

    public InfoViewListener(Context context, Quote quote, List<HisData> list, ChartInfoView infoView,Chart otherChart) {
        mWidth = DisplayUtils.getWidthHeight(context)[0];
        mQuote = quote;
        mList = list;
        mInfoView = infoView;
        mOtherChart = otherChart;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        int x = (int) e.getX();
        if (x < mList.size()) {
            mInfoView.setVisibility(View.VISIBLE);
            mInfoView.setData(mQuote == null ? 0 : mQuote.getLastclose(), mList.get(x));
        }
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInfoView.getLayoutParams();
        if (h.getXPx() < mWidth / 2) {
            lp.gravity = Gravity.RIGHT;
        } else {
            lp.gravity = Gravity.LEFT;
        }
        mInfoView.setLayoutParams(lp);
        if(mOtherChart != null) {
            mOtherChart.highlightValues(new Highlight[]{h});
        }
    }

    @Override
    public void onNothingSelected() {
        mInfoView.setVisibility(View.GONE); if(mOtherChart != null) {
            mOtherChart.highlightValues(null);
        }
    }
}
