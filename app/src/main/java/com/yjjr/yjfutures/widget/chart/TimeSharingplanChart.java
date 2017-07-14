package com.yjjr.yjfutures.widget.chart;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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
import com.yjjr.yjfutures.utils.DoubleUtil;

import org.joda.time.DateTime;

import java.util.List;

/**
 * 分时图
 * Created by guoziwei on 2017/5/13.
 */
public class TimeSharingplanChart extends RelativeLayout {

    public static final int FULL_SCREEN_SHOW_COUNT = 100;
    public static final int DATA_SET_SELL = 0;
    private LineChart mChart;
    private Context mContext;
    private int candleIncreaseColor = getResources().getColor(R.color.third_text_color);
    private int candleGridColor = getResources().getColor(R.color.color_333333);
    private int mTextColor = getResources().getColor(R.color.second_text_color);
    private int digits = 2;
    private boolean isIdle = true;
    private DateTime mStartTime;
    private IAxisValueFormatter xValueFormatter = new IAxisValueFormatter() {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            DateTime time = mStartTime.plusMinutes((int) value);
            return DateUtils.formatTime(time.getMillis());
        }
    };

    public TimeSharingplanChart(Context context) {
        this(context, null);
    }

    public TimeSharingplanChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public TimeSharingplanChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_mp_line_chart, this);
        mChart = (LineChart) findViewById(R.id.line_chart);
        mChart.setDrawGridBackground(false);
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
            setSell = createSet();
            data.addDataSet(setSell);
        }

        for (int i = 0; i < list.size(); i++) {
            HisData hisData = list.get(i);
            data.addEntry(new Entry(setSell.getEntryCount(), (float) hisData.getClose()), DATA_SET_SELL);
        }

        Highlight chartHighlighter = new Highlight(setSell.getEntryCount() - 1, (float) list.get(setSell.getEntryCount() - 1).getClose(), DATA_SET_SELL);
        mChart.highlightValue(chartHighlighter);
        mChart.calculateOffsets();
        mChart.notifyDataSetChanged();
//        mChart.setVisibleXRange(FULL_SCREEN_SHOW_COUNT, FULL_SCREEN_SHOW_COUNT);
        mChart.setVisibleXRange(list.size()+100, list.size()+100);
        mChart.moveViewToX(data.getEntryCount()/* - FULL_SCREEN_SHOW_COUNT - 1*/);

    }

    public void addLastEntry() {
        LineData data = mChart.getData();
        ILineDataSet setSell = data.getDataSetByIndex(DATA_SET_SELL);
        Entry bid = setSell.getEntryForIndex(setSell.getEntryCount() - 1);
        refreshChart(bid.getY());
    }

    public void refreshEntry(float bid) {
        LineData data = mChart.getData();

        if (data != null) {
            ILineDataSet setSell = data.getDataSetByIndex(DATA_SET_SELL);
            if (setSell == null) {
                setSell = createSet();
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
            mChart.setVisibleXRange(FULL_SCREEN_SHOW_COUNT, FULL_SCREEN_SHOW_COUNT);

        }
    }

    private void refreshChart(float bid) {
        LineData data = mChart.getData();

        if (data != null) {
            ILineDataSet setSell = data.getDataSetByIndex(DATA_SET_SELL);
            if (setSell == null) {
                setSell = createSet();
                data.addDataSet(setSell);
            }

            data.addEntry(new Entry(setSell.getEntryCount(), bid), DATA_SET_SELL);


            mChart.calculateOffsets();
            Highlight chartHighlighter = new Highlight(data.getDataSetByIndex(DATA_SET_SELL).getEntryCount() - 1, bid, DATA_SET_SELL);
            mChart.highlightValue(chartHighlighter);
            mChart.notifyDataSetChanged();
//            mChart.setVisibleXRange(FULL_SCREEN_SHOW_COUNT, FULL_SCREEN_SHOW_COUNT);
            mChart.moveViewToX(data.getEntryCount()/* - FULL_SCREEN_SHOW_COUNT - 1*/);

        }
    }

    private ILineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, null);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        int color = candleIncreaseColor;

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
        mChart.setBackgroundColor(ContextCompat.getColor(mContext, R.color.chart_background));
        RealPriceMarkerView mv = new RealPriceMarkerView(mContext, digits);
        mv.setChartView(mChart);
        mChart.setMarker(mv);
        mChart.setNoDataText(getContext().getString(R.string.loading));
        mChart.setNoDataTextColor(ContextCompat.getColor(mContext, R.color.third_text_color));

        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDragEnabled(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setAutoScaleMinMaxEnabled(true);
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
                return DoubleUtil.formatDecimal((double) value, digits);
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
