package com.yjjr.yjfutures.widget.chart;

import android.content.Context;
import android.graphics.Color;
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
import com.yjjr.yjfutures.utils.DoubleUtil;

/**
 * 分时图
 * Created by guoziwei on 2017/5/13.
 */
public class TimeSharingplanChart extends RelativeLayout {

    public static final int FULL_SCREEN_SHOW_COUNT = 100;
    public static final int DATA_SET_SELL = 0;
    public static final int DATA_SET_BUY = 1;
    private LineChart mChart;
    private Context mContext;
    private int candleIncreaseColor = getResources().getColor(R.color.main_color_red);
    private int candleDecreaseColor = getResources().getColor(R.color.main_color);
    private int candleGridColor = getResources().getColor(R.color.color_333333);
    private int mTextColor = getResources().getColor(R.color.second_text_color);
    private int digits = 5;
    private boolean isIdle = true;

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
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

    }

   /* public void addEntry(MT4Symbol symbol) {
        if(null == symbol) return;
        float ask = (float) symbol.getAsk();
        float bid = (float) symbol.getBid();
        this.digits = symbol.getDigits();
        refreshChart(ask, bid);
    }

    public void addEntry(PriceEventResponse response) {
        if(null == response) return;
        float ask = (float) response.getAsk();
        float bid = (float) response.getBid();
        refreshChart(ask, bid);
    }*/

    public void addEntry(float ask, float bid) {
        refreshChart(ask, bid);
    }

    public void addLastEntry() {
        LineData data = mChart.getData();
        ILineDataSet setSell = data.getDataSetByIndex(DATA_SET_SELL);
        ILineDataSet setBuy = data.getDataSetByIndex(DATA_SET_BUY);
        Entry bid = setSell.getEntryForIndex(setSell.getEntryCount() - 1);
        Entry ask = setBuy.getEntryForIndex(setSell.getEntryCount() - 1);
        refreshChart(ask.getY(), bid.getY());
    }

    public void refreshEntry(float ask, float bid) {
        LineData data = mChart.getData();

        if (data != null) {
            ILineDataSet setSell = data.getDataSetByIndex(DATA_SET_SELL);
            ILineDataSet setBuy = data.getDataSetByIndex(DATA_SET_BUY);
            if (setSell == null) {
                setSell = createSet(DATA_SET_SELL);
                data.addDataSet(setSell);
            }
            if (setBuy == null) {
                setBuy = createSet(DATA_SET_BUY);
                data.addDataSet(setBuy);
            }

            data.removeEntry(setBuy.getEntryCount(), DATA_SET_BUY);
            data.removeEntry(setSell.getEntryCount(), DATA_SET_SELL);
            data.addEntry(new Entry(setBuy.getEntryCount(), ask), DATA_SET_BUY);
            data.addEntry(new Entry(setSell.getEntryCount(), bid), DATA_SET_SELL);


            mChart.calculateOffsets();
            if(isIdle) {
                Highlight chartHighlighter = new Highlight(data.getDataSetByIndex(DATA_SET_BUY).getEntryCount() - 1, ask, DATA_SET_BUY);
                Highlight chartHighlighter2 = new Highlight(data.getDataSetByIndex(DATA_SET_SELL).getEntryCount() - 1, bid, DATA_SET_SELL);
                Highlight highlight[] = new Highlight[]{chartHighlighter, chartHighlighter2};
                mChart.highlightValues(highlight);
            }
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRange(FULL_SCREEN_SHOW_COUNT, FULL_SCREEN_SHOW_COUNT);

        }
    }

    private void refreshChart(float ask, float bid) {
        LineData data = mChart.getData();

        if (data != null) {
            ILineDataSet setSell = data.getDataSetByIndex(DATA_SET_SELL);
            ILineDataSet setBuy = data.getDataSetByIndex(DATA_SET_BUY);
            if (setSell == null) {
                setSell = createSet(DATA_SET_SELL);
                data.addDataSet(setSell);
            }
            if (setBuy == null) {
                setBuy = createSet(DATA_SET_BUY);
                data.addDataSet(setBuy);
            }

            data.addEntry(new Entry(setBuy.getEntryCount(), ask), DATA_SET_BUY);
            data.addEntry(new Entry(setSell.getEntryCount(), bid), DATA_SET_SELL);


            mChart.calculateOffsets();
            Highlight chartHighlighter = new Highlight(data.getDataSetByIndex(DATA_SET_BUY).getEntryCount() - 1, ask, DATA_SET_BUY);
            Highlight chartHighlighter2 = new Highlight(data.getDataSetByIndex(DATA_SET_SELL).getEntryCount() - 1, bid, DATA_SET_SELL);
            Highlight highlight[] = new Highlight[]{chartHighlighter, chartHighlighter2};
            mChart.highlightValues(highlight);
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRange(FULL_SCREEN_SHOW_COUNT, FULL_SCREEN_SHOW_COUNT);
            mChart.moveViewToX(data.getEntryCount()/* - FULL_SCREEN_SHOW_COUNT - 1*/);

        }
    }

    private ILineDataSet createSet(int orderType) {
        int setIntroduce = orderType == DATA_SET_BUY ? R.string.buy_in : R.string.sold_out;
        LineDataSet set = new LineDataSet(null, mContext.getString(setIntroduce));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        int color = orderType == DATA_SET_BUY ? candleDecreaseColor : candleIncreaseColor;

        set.setHighLightColor(color);
        set.setDrawHighlightIndicators(false);
        set.setDrawHorizontalHighlightIndicator(true);
        set.setDrawVerticalHighlightIndicator(false);
        set.setHighlightLineWidth(1);

        set.setColor(color);
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(1f);
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
        RealPriceMarkerView mv = new RealPriceMarkerView(mContext, digits);
        mv.setChartView(mChart);
        mChart.setMarker(mv);

        mChart.setDescription(null);
        mChart.setPinchZoom(false);
        mChart.setDragEnabled(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setScaleXEnabled(false);
        mChart.setScaleYEnabled(false);
        mChart.setAutoScaleMinMaxEnabled(true);
        mChart.setDrawMarkers(true);
        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setAutoScaleMinMaxEnabled(false);
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
    }


}
