package com.yjjr.yjfutures.widget.chart;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.HisData;
import com.yjjr.yjfutures.utils.DateUtils;
import com.yjjr.yjfutures.utils.StringUtils;

import org.joda.time.DateTime;

import java.util.List;

/**
 * 分时图
 * Created by guoziwei on 2017/5/13.
 */
public class TimeSharingplanChart extends RelativeLayout {


    public static final int FULL_SCREEN_SHOW_COUNT = 200;
    public static final int DATA_SET_SELL = 0;
    private final double mTick;
    private LineChart mChart;
    private Context mContext;
    private int candleIncreaseColor = getResources().getColor(R.color.third_text_color);
    private int transparentColor = getResources().getColor(R.color.transparent);
    private int candleGridColor = getResources().getColor(R.color.color_333333);
    private int mTextColor = getResources().getColor(R.color.second_text_color);
    private boolean isIdle = true;
    private DateTime mStartTime;
    private IAxisValueFormatter xValueFormatter = new IAxisValueFormatter() {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            DateTime time = mStartTime.plusMinutes((int) value);
            return DateUtils.formatTime(time.getMillis());
        }
    };

    public TimeSharingplanChart(Context context, double tick) {
        super(context);
        mTick = tick;
        init(context);
    }


    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_mp_line_chart, this);
        mChart = (LineChart) findViewById(R.id.line_chart);
        setupSettingParameter();
    }

    public void addEntry(float bid) {
        refreshChart(bid);
    }

    public void addEntries(List<HisData> list) {
        LineData data = new LineData();
        mChart.setData(data);
        ILineDataSet setSell = data.getDataSetByIndex(DATA_SET_SELL);
        if (setSell == null) {
            setSell = createSet(candleIncreaseColor);
            data.addDataSet(setSell);
        }

        for (int i = 0; i < list.size(); i++) {
            HisData hisData = list.get(i);
            data.addEntry(new Entry(setSell.getEntryCount(), (float) hisData.getClose()), DATA_SET_SELL);
        }

        ILineDataSet testSet = createSet(transparentColor);
        for (int i = list.size(); i < list.size() + 20; i++) {
            testSet.addEntry(new Entry(i, (float) list.get(list.size()-1).getClose()));
        }
        data.addDataSet(testSet);

        Highlight chartHighlighter = new Highlight(setSell.getEntryCount() - 1, (float) list.get(setSell.getEntryCount() - 1).getClose(), DATA_SET_SELL);
        mChart.highlightValue(chartHighlighter);
        mChart.calculateOffsets();
        mChart.notifyDataSetChanged();
        mChart.setVisibleXRange(FULL_SCREEN_SHOW_COUNT, 50);
        mChart.moveViewToX(data.getEntryCount()/* - FULL_SCREEN_SHOW_COUNT - 1*/);

    }


    public void refreshEntry(float bid) {
        if (bid <= 0) {
            return;
        }
        LineData data = mChart.getData();

        if (data != null) {
            ILineDataSet setSell = data.getDataSetByIndex(DATA_SET_SELL);
            if (setSell == null) {
                setSell = createSet(candleIncreaseColor);
                data.addDataSet(setSell);
            }

            data.removeEntry(setSell.getEntryCount(), DATA_SET_SELL);
            data.addEntry(new Entry(setSell.getEntryCount(), bid), DATA_SET_SELL);


            mChart.calculateOffsets();
            if (isIdle) {
                Highlight chartHighlighter = new Highlight(data.getDataSetByIndex(DATA_SET_SELL).getEntryCount() - 1, bid, DATA_SET_SELL);
                mChart.highlightValue(chartHighlighter);
            }
            mChart.notifyDataSetChanged();

        }
    }

    private void refreshChart(float bid) {
        LineData data = mChart.getData();

        if (data != null) {
            ILineDataSet setSell = data.getDataSetByIndex(DATA_SET_SELL);
            if (setSell == null) {
                setSell = createSet(candleIncreaseColor);
                data.addDataSet(setSell);
            }

            data.addEntry(new Entry(setSell.getEntryCount(), bid), DATA_SET_SELL);


            mChart.calculateOffsets();
            Highlight chartHighlighter = new Highlight(data.getDataSetByIndex(DATA_SET_SELL).getEntryCount() - 1, bid, DATA_SET_SELL);
            mChart.highlightValue(chartHighlighter);
            mChart.notifyDataSetChanged();
//            mChart.moveViewToX(data.getEntryCount()/* - FULL_SCREEN_SHOW_COUNT - 1*/);

        }
    }

    private ILineDataSet createSet(int color) {
        LineDataSet set = new LineDataSet(null, null);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        set.setHighLightColor(color);
        set.setDrawHighlightIndicators(false);
        set.setDrawHorizontalHighlightIndicator(true);
        set.setDrawVerticalHighlightIndicator(false);
        set.setHighlightLineWidth(0.5f);

        set.enableDashedHighlightLine(10, 5, 0);
        set.setColor(color);
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(0.5f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        set.setDrawCircles(false);

        return set;
    }

    private void setupSettingParameter() {
        mChart.setDrawGridBackground(false);
        mChart.setBackgroundColor(ContextCompat.getColor(mContext, R.color.chart_background));
        RealPriceMarkerView mv = new RealPriceMarkerView(mContext,mTick);
        mv.setChartView(mChart);
        mChart.setMarker(mv);
        mChart.setNoDataText(getContext().getString(R.string.loading));
        mChart.setNoDataTextColor(ContextCompat.getColor(mContext, R.color.third_text_color));

        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(true);
        mChart.setScaleYEnabled(true);
        mChart.setAutoScaleMinMaxEnabled(false);
        mChart.setDrawMarkers(true);
        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                isIdle = false;
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                isIdle = true;
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

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });


        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(true);
        rightAxis.setGridColor(candleGridColor);
        rightAxis.setTextColor(mTextColor);
        rightAxis.setGridLineWidth(0.5f);
        rightAxis.enableGridDashedLine(20, 5, 0);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(true);
        rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return StringUtils.getStringByTick(value, mTick);
            }
        });
//        rightAxis.setMinWidth(50);
//        rightAxis.setMaxWidth(50);
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);
        leftAxis.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
//        xAxis.setDrawLabels(false);
        xAxis.setTextColor(mTextColor);
        xAxis.setGridColor(candleGridColor);

        xAxis.setValueFormatter(xValueFormatter);

    }

    public void setNoDataText(String text) {
        mChart.setNoDataText(text);
    }

    public void setStartTime(DateTime startTime) {
        mStartTime = startTime;
    }
}
