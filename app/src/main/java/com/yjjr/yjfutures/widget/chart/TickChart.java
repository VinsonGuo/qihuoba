package com.yjjr.yjfutures.widget.chart;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.utils.DoubleUtil;

/**
 * 闪电图
 * Created by guoziwei on 2017/5/13.
 */
public class TickChart extends RelativeLayout {

    public static final int FULL_SCREEN_SHOW_COUNT = 100;
    public static final int DATA_SET_SELL = 0;
    private LineChart mChart;
    private Context mContext;
    private int candleIncreaseColor = getResources().getColor(R.color.third_text_color);
    private int candleGridColor = getResources().getColor(R.color.color_333333);
    private int mTextColor = getResources().getColor(R.color.second_text_color);

    public TickChart(Context context) {
        this(context, null);
    }

    public TickChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TickChart(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public void addEntry(float bid) {
        refreshChart(bid);
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


            Highlight chartHighlighter = new Highlight(data.getDataSetByIndex(DATA_SET_SELL).getEntryCount() - 1, bid, DATA_SET_SELL);
            mChart.highlightValue(chartHighlighter);
            mChart.calculateOffsets();
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRange(FULL_SCREEN_SHOW_COUNT, FULL_SCREEN_SHOW_COUNT);
            mChart.setAutoScaleMinMaxEnabled(true);
            mChart.moveViewToX(data.getEntryCount()/* - FULL_SCREEN_SHOW_COUNT - 1*/);
        }
    }

    private ILineDataSet createSet() {
        int setIntroduce = R.string.sold_out;
        LineDataSet set = new LineDataSet(null, mContext.getString(setIntroduce));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        int color = candleIncreaseColor;

        set.setHighLightColor(color);
        set.setDrawHighlightIndicators(false);
        set.setDrawHorizontalHighlightIndicator(true);
        set.setDrawVerticalHighlightIndicator(false);
        set.setHighlightLineWidth(1);
        set.enableDashedHighlightLine(10, 5, 0);

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
        RealPriceMarkerView mv = new RealPriceMarkerView(mContext);
        mChart.setMarker(mv);

        mChart.setDescription(null);
        mChart.setPinchZoom(false);
        mChart.setDragEnabled(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setScaleXEnabled(false);
        mChart.setScaleYEnabled(false);
        mChart.setAutoScaleMinMaxEnabled(true);
        mChart.setDrawMarkers(true);
        mChart.setClickable(false);
        mChart.setTouchEnabled(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(true);
        rightAxis.setGridColor(candleGridColor);
        rightAxis.setTextColor(mTextColor);
        rightAxis.setGridLineWidth(0.5f);
        rightAxis.enableGridDashedLine(20, 5, 0);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(true);
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
        xAxis.setDrawLabels(false);
        xAxis.setGridColor(candleGridColor);
    }


}
