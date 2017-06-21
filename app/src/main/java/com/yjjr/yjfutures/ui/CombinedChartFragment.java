package com.yjjr.yjfutures.ui;


import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.utils.LogUtils;
import com.yjjr.yjfutures.utils.ToastUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CombinedChartFragment extends BaseFragment {


    private CombinedChart mChart;

    public CombinedChartFragment() {
        // Required empty public constructor
    }

    @Override
    protected View initViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mChart = new CombinedChart(mContext);
        mChart.setBackgroundColor(Color.WHITE);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(0);

        mChart.setScaleYEnabled(false);



        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int)value+"号";
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(7, false);
        leftAxis.setDrawGridLines(true);
//        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);


        mChart.getLegend().setEnabled(false);
        mChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                if (lastPerformedGesture.equals(ChartTouchListener.ChartGesture.DRAG)||lastPerformedGesture.equals(ChartTouchListener.ChartGesture.FLING)) {
                    float lowestVisibleX = mChart.getLowestVisibleX();
                    LogUtils.d("getLowestVisibleX is %f", lowestVisibleX);
                    if (lowestVisibleX <= 0) {
                        LogUtils.d("bottom");
                        ToastUtils.show(mContext, "bottom");
                    }
                }
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
//                LogUtils.d("onChartFling--------------X is %f, Y is %f", velocityX, velocityY);
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
            }
        });
        fullData();
        mChart.setVisibleXRange(21,9); // allow 20 values to be displayed at once on the x-axis, not more
        mChart.moveViewToX(mChart.getCandleData().getEntryCount());
        return mChart;
    }

    private void fullData() {
        ArrayList<CandleEntry> yVals1 = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            float mult = (40 + 1);
            float val = (float) (Math.random() * 40) + mult;

            float high = (float) (Math.random() * 9) + 8f;
            float low = (float) (Math.random() * 9) + 8f;

            float open = (float) (Math.random() * 6) + 1f;
            float close = (float) (Math.random() * 6) + 1f;

            boolean even = i % 2 == 0;

            yVals1.add(new CandleEntry(
                    i, val + high,
                    val - low,
                    even ? val + open : val - open,
                    even ? val - close : val + close
            ));
        }

        CandleDataSet set1 = new CandleDataSet(yVals1, "日期");

        set1.setDrawIcons(false);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set1.setColor(Color.rgb(80, 80, 80));
        set1.setShadowColor(Color.DKGRAY);
        set1.setShadowWidth(0.7f);
        set1.setDecreasingColor(ContextCompat.getColor(getContext(), R.color.main_color));
        set1.setDecreasingPaintStyle(Paint.Style.FILL);
        set1.setIncreasingColor(ContextCompat.getColor(getContext(), R.color.main_color_red));
        set1.setIncreasingPaintStyle(Paint.Style.STROKE);
        set1.setIncreasingPaintStyle(Paint.Style.FILL);
        set1.setNeutralColor(Color.BLUE);
        //set1.setHighlightLineWidth(1f);

//        CandleData data = new CandleData(set1);

        ArrayList<BarEntry> yVals2 = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            float mult = 40 / 2f;
            float val = (float) (Math.random() * mult);
            yVals2.add(new BarEntry(i, val));
        }
        BarDataSet set2 = new BarDataSet(yVals2,"test");
        CombinedData data = new CombinedData();
        data.setData(new CandleData(set1));
        data.setData(new BarData(set2));

        mChart.setData(data);
        mChart.invalidate();
    }


}
