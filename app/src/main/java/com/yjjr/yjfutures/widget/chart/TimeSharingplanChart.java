package com.yjjr.yjfutures.widget.chart;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

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
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.yjjr.yjfutures.R;
import com.yjjr.yjfutures.model.HisData;
import com.yjjr.yjfutures.utils.DateUtils;
import com.yjjr.yjfutures.utils.StringUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * 分时图
 * Created by guoziwei on 2017/5/13.
 */
public class TimeSharingplanChart extends RelativeLayout {

    /**
     * 实线
     */
    public static final int TYPE_FULL = 0;
    /**
     * 虚线
     */
    public static final int TYPE_DASHED = 1;

    public static final int FULL_SCREEN_SHOW_COUNT = 200;
    public static final int DATA_SET_PRICE = 0;
    public static final int DATA_SET_PADDING = 1;
    private double mTick;
    private List<HisData> mList = new ArrayList<>();
    private AppLineChart mChart;
    private Context mContext;
    private int mLineColor = getResources().getColor(R.color.third_text_color);
    private int transparentColor = getResources().getColor(R.color.transparent);
    private int candleGridColor = getResources().getColor(R.color.color_333333);
    private int mTextColor = getResources().getColor(R.color.second_text_color);
    private boolean isIdle = true;
    private IAxisValueFormatter xValueFormatter = new IAxisValueFormatter() {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (mList != null && value < mList.size()) {
                DateTime dateTime = new DateTime(mList.get((int) value).getsDate());
                return DateUtils.formatTime(dateTime.getMillis());
            }
            return "";
        }
    };
    /**
     * 具体信息的View
     */
    private LineChartInfoView mInfoView;

    public TimeSharingplanChart(Context context) {
        super(context);
        init(context);
    }

    public TimeSharingplanChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AppLineChart getChart() {
        return mChart;
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_mp_line_chart, this);
        mChart = (AppLineChart) findViewById(R.id.line_chart);
        mInfoView = (LineChartInfoView) findViewById(R.id.info);
        setupSettingParameter();
    }

    public void addEntries(List<HisData> list) {
        mList.clear();
        mList.addAll(list);
        LineData data = new LineData();
        mChart.setData(data);
        ILineDataSet setSell = data.getDataSetByIndex(DATA_SET_PRICE);
        if (setSell == null) {
            setSell = createSet(TYPE_FULL);
            data.addDataSet(setSell);
        }

        ILineDataSet paddingSet = data.getDataSetByIndex(DATA_SET_PADDING);
        if (paddingSet == null) {
            paddingSet = createSet(DATA_SET_PADDING);
            data.addDataSet(paddingSet);
        }

        for (int i = 0; i < list.size(); i++) {
            HisData hisData = list.get(i);
            data.addEntry(new Entry(setSell.getEntryCount(), (float) hisData.getClose()), DATA_SET_PRICE);
        }

        for (int i = mList.size(); i < mList.size() + 20; i++) {
            data.addEntry(new Entry(i, (float) mList.get(list.size() - 1).getClose()), DATA_SET_PADDING);
        }

        Highlight chartHighlighter = new Highlight(setSell.getEntryCount() - 1, (float) list.get(setSell.getEntryCount() - 1).getClose(), DATA_SET_PRICE);
        mChart.highlightValue(chartHighlighter);
        mChart.notifyDataSetChanged();
        mChart.setVisibleXRange(FULL_SCREEN_SHOW_COUNT, 50);
        mChart.moveViewToX(data.getEntryCount());

    }


    public void refreshEntry(float bid) {
        if (bid <= 0) {
            return;
        }
        LineData data = mChart.getData();

        if (data != null) {
            ILineDataSet setSell = data.getDataSetByIndex(DATA_SET_PRICE);
            if (setSell == null) {
                setSell = createSet(TYPE_FULL);
                data.addDataSet(setSell);
            }

            data.removeEntry(setSell.getEntryCount(), DATA_SET_PRICE);
            data.addEntry(new Entry(setSell.getEntryCount(), bid), DATA_SET_PRICE);

            if (isIdle) {
                Highlight chartHighlighter = new Highlight(data.getDataSetByIndex(DATA_SET_PRICE).getEntryCount() - 1, bid, DATA_SET_PRICE);
                mChart.highlightValue(chartHighlighter);
            }
            mChart.notifyDataSetChanged();

        }
    }


    public void addEntry(HisData hisData) {
        mList.add(hisData);
        float price = (float) hisData.getClose();
        LineData data = mChart.getData();

        if (data != null) {
            ILineDataSet setSell = data.getDataSetByIndex(DATA_SET_PRICE);
            if (setSell == null) {
                setSell = createSet(TYPE_FULL);
                data.addDataSet(setSell);
            }
            data.addEntry(new Entry(setSell.getEntryCount(), price), DATA_SET_PRICE);

            ILineDataSet paddingSet = data.getDataSetByIndex(DATA_SET_PADDING);
            if (paddingSet == null) {
                paddingSet = createSet(TYPE_DASHED);
                data.addDataSet(paddingSet);
            }
            data.addEntry(new Entry(setSell.getEntryCount(), price), DATA_SET_PADDING);

            Highlight chartHighlighter = new Highlight(data.getDataSetByIndex(DATA_SET_PRICE).getEntryCount() - 1, price, DATA_SET_PRICE);
            mChart.highlightValue(chartHighlighter);
            mChart.notifyDataSetChanged();

        }
    }

    private ILineDataSet createSet(int type) {
        LineDataSet set = new LineDataSet(null, null);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        if (type == TYPE_FULL) {
            set.setHighLightColor(mLineColor);
            set.setDrawHighlightIndicators(true);
            set.setHighlightLineWidth(0.5f);
            set.setCircleColor(mLineColor);
            set.setCircleRadius(2);
            set.setDrawCircleHole(false);
            set.setDrawFilled(true);
            set.setColor(mLineColor);
//            set.setFillColor(mLineColor);
            set.setFillDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_chart_fade));
        } else {
            set.setHighlightEnabled(false);
            set.setVisible(false);
            set.setColor(mLineColor);
            set.enableDashedLine(10, 5, 0);
            set.setDrawCircleHole(false);
            set.setCircleColor(transparentColor);
        }
        set.setDrawCircles(false);
        set.setLineWidth(1f);
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);

        return set;
    }

    public void setTick(double tick) {
        mTick = tick;
        LineChartYMarkerView mv = new LineChartYMarkerView(mContext, mTick);
        mv.setChartView(mChart);
        mChart.setMarker(mv);
    }

    private void setupSettingParameter() {
        mChart.setDrawGridBackground(false);
        mChart.setBackgroundColor(ContextCompat.getColor(mContext, R.color.chart_background));
        LineChartXMarkerView mvx = new LineChartXMarkerView(mContext, mList);
        mvx.setChartView(mChart);
        mChart.setXMarker(mvx);
        mChart.setNoDataText(getContext().getString(R.string.loading));
        mChart.setNoDataTextColor(ContextCompat.getColor(mContext, R.color.third_text_color));
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(true);
        mChart.setScaleYEnabled(false);
        mChart.setAutoScaleMinMaxEnabled(false);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
//                mChart.highlightValue(h);
                int x = (int) e.getX();
                if (x < mList.size()) {
                    mInfoView.setVisibility(VISIBLE);
                    mInfoView.setData(mList.get(x));
                }
            }

            @Override
            public void onNothingSelected() {
                mInfoView.setVisibility(GONE);
            }
        });

        mChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                isIdle = false;
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                isIdle = true;
                mChart.setDragEnabled(true);
                mInfoView.setVisibility(GONE);
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
                mChart.setDragEnabled(false);
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
        rightAxis.enableGridDashedLine(5, 5, 0);
        rightAxis.setLabelCount(6,true);
        rightAxis.setDrawAxisLine(false);

        rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return StringUtils.getStringByTick(value, mTick);
            }
        });
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(mTextColor);
        xAxis.setGridColor(candleGridColor);

        xAxis.setValueFormatter(xValueFormatter);

    }

    public void setNoDataText(String text) {
        mChart.setNoDataText(text);
    }
}
